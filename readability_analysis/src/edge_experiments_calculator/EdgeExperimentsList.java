package edge_experiments_calculator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import node_count.Composer;
import readability_analysis.AnswerColumn;
import readability_analysis.IOUtil;
import readability_analysis.WilcoxOutput;

public class EdgeExperimentsList implements Comparable<EdgeExperimentsList> {
	private static File scriptTempFile = new File(IOUtil.dataPath + IOUtil.TMP +
		IOUtil.R_SCRIPTNAME);

	private final String[] colNames;
	private final double matchingP;
	private final double composingP;
	private final String edgeIndex;
	private final List<ExperimentPair> experiments;
	private static final String between = " & ";
	private static DecimalFormat df3 = new DecimalFormat("0.00");

	public EdgeExperimentsList(String edgeIndex, String edgeDescription,
			List<ExperimentPair> experiments) throws IOException,
			InterruptedException {
		this.edgeIndex = edgeIndex;
		colNames = edgeDescription.split("->");
		this.experiments = experiments;
		this.matchingP = getPValue(true);
		this.composingP = getPValue(false);
	}

	public String getEdgeIndex() {
		return edgeIndex;
	}

	public List<ExperimentPair> getExperiments() {
		return experiments;
	}

	public String getPFilename(boolean isMatchingPValue) {
		String suffix = isMatchingPValue ? "_match" : "_compose";
		return edgeIndex + suffix;
	}

	public double getPValue(boolean isMatchingPValue) throws IOException,
			InterruptedException {
		String endFilename = getPFilename(isMatchingPValue);

		File RinputFile = new File(IOUtil.dataPath + IOUtil.IN + IOUtil.E_PATH +
			endFilename + ".tsv");
		File RoutFile = new File(IOUtil.dataPath + IOUtil.OUT + IOUtil.E_PATH +
			endFilename + ".Rout");
		IOUtil.createAndWrite(RinputFile, getWilcoxInputContent(isMatchingPValue));
		String scriptContent = wrapTestIO(RinputFile.getAbsolutePath(), getWilcoxTest(), RoutFile.getAbsolutePath(), "\\t");
		writeROutput(scriptContent, scriptTempFile);
		WilcoxOutput wo = new WilcoxOutput(IOUtil.getLines(RoutFile.getAbsolutePath()), endFilename);
		return wo.getPValue();
	}

	public String getWilcoxInputContent(boolean isMatching) {
		StringBuilder sb = new StringBuilder();
		sb.append(colNames[0] + "\t" + colNames[1] + "\n");

		for (ExperimentPair ep : experiments) {
			AnswerColumn[] acs = isMatching ? ep.getMatchingColumns()
					: ep.getComposingColumns();
			AnswerColumn leftAc = acs[0];
			AnswerColumn rightAc = acs[1];
			// should never happen, because we include NAN inputs here, so all
			// should be 30.
			if (leftAc.getValues().length != rightAc.getValues().length) {
				throw new RuntimeException("cannot generate wilcox input with different number of measurements");
			}
			Double[] leftValues = leftAc.getValues();
			Double[] rightValues = rightAc.getValues();
			for (int k = 0; k < leftValues.length; k++) {
				sb.append(leftValues[k] + "\t" + rightValues[k] + "\n");
			}
		}
		return sb.toString();
	}

	public double getComposingPValue() {
		return 0.0;
	}

	@Override
	public String toString() {
		return "EdgeExperimentsList [edgeIndex=" + edgeIndex +
			", experiments=" + experiments + "]";
	}

	public String getLatexRow(int edgeNumber) throws IOException, InterruptedException {
		String end = "\\\\\n";
		return "E"+ edgeNumber + between + colNames[0] + " -- " + colNames[1] +
			between + experiments.size() + between + getNumberSectionOfLatex() +
			end;
	}

	private String getNumberSectionOfLatex() throws IOException,
			InterruptedException {
		double[] numbers = new double[4];
		double counter = 0;
		for (ExperimentPair ep : experiments) {
			counter++;
			numbers[0] += ep.getMatchingAvgLeft();
			numbers[1] += ep.getMatchingAvgRight();
			numbers[2] += ep.getComposingAvgLeft();
			numbers[3] += ep.getComposingAvgRight();
		}
		for (int i = 0; i < 4; i++) {
			numbers[i] = numbers[i] / counter;
		}
		String matchP = Composer.formatPValue(matchingP);
		String compP = Composer.formatPValue(composingP);
		boolean shouldBoldMatch = false;
		boolean shouldBoldComp = false;
		
		if(matchingP<0.1 || composingP<0.1){
			if(matchingP<composingP){
				shouldBoldMatch = true;
			}else if(composingP<matchingP){
				shouldBoldComp = true;
			}
		}
		matchP = shouldBoldMatch? "\\textbf{"+matchP +"}" : matchP;
		compP = shouldBoldComp? "\\textbf{"+compP +"}" : compP;
		

		
		return df3.format(numbers[0]) + between + df3.format(numbers[1]) +
			between + matchP + between +
			df3.format(numbers[2]) + between + df3.format(numbers[3]) +
			between + compP;
	}

	private static void writeROutput(String scriptContent, File scriptTempFile)
			throws IOException, InterruptedException {
		// write over the temp R input script with given content
		IOUtil.createAndWrite(scriptTempFile, scriptContent);

		// run the R script
		Process p = Runtime.getRuntime().exec("/usr/local/bin/R CMD BATCH " +
			scriptTempFile.getAbsolutePath());

		// add + " /dev/null" to the end to silence output
		p.waitFor();
	}

	private static String wrapTestIO(String inPath, String testContent,
			String outPath, String sep) {
		StringBuilder sb = new StringBuilder();
		sb.append("tbl = read.table(\"" + inPath + "\",TRUE,\"" + sep + "\")\n");
		sb.append(testContent);
		sb.append("capture.output(results,file=\"" + outPath + "\")");
		return sb.toString();
	}

	private static String getWilcoxTest() {
		return "results = wilcox.test(tbl[,1],tbl[,2])\n";
	}

	// first by matchingP, then by composingP then by experiments.size()
	@Override
	public int compareTo(EdgeExperimentsList o) {
		if (this.matchingP == o.matchingP  && this.composingP == o.composingP) {
			return 0;
		} else if(this.matchingP == o.matchingP){
			if(this.composingP < o.composingP){
				return -1;
			}else{
				return 1;
			}
		}else if(this.composingP == o.composingP){
			if(this.matchingP < o.matchingP){
				return -1;
			}else{
				return 1;
			}
		}else{
			return compareMins(o);
		}
	}

	private int compareMins(EdgeExperimentsList o) {
		double[] pValues = new double[4];
		pValues[0] = this.matchingP;
		pValues[1] = this.composingP;
		pValues[2] = o.matchingP;
		pValues[3] = o.composingP;
		double minValue = Double.MAX_VALUE;
		int minIndex = 0;
		for (int i = 0; i < 4; i++) {
			if (pValues[i] < minValue) {
				minValue = pValues[i];
				minIndex = i;
			}
		}
		if (minIndex < 2) {
			return -1;
		} else {
			return 1;
		}
	}
}
