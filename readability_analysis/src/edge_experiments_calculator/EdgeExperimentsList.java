package edge_experiments_calculator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import node_count.Composer;
import readability_analysis.AnswerColumn;
import readability_analysis.IOUtil;
import readability_analysis.ProportionOutput;
import readability_analysis.WilcoxFormatter;
import readability_analysis.WilcoxOutput;

public class EdgeExperimentsList {
	private static File scriptTempFile = new File(IOUtil.dataPath + IOUtil.TMP +
			IOUtil.R_SCRIPTNAME);


	private final String edgeIndex;
	private final String edgeDescription;
	private final List<ExperimentPair> experiments;
	private static final String between = " & ";
	private static DecimalFormat df3 = new DecimalFormat("0.00");
	private static DecimalFormat df7 = new DecimalFormat("0.000000");
	private static DecimalFormat dfWhole = new DecimalFormat("#.0");

	public EdgeExperimentsList(String edgeIndex, String edgeDescription,
			List<ExperimentPair> experiments) {
		this.edgeIndex = edgeIndex;
		this.edgeDescription = edgeDescription;
		this.experiments = experiments;
	}

	public String getEdgeIndex() {
		return edgeIndex;
	}

	public String getEdgeDescription() {
		return edgeDescription;
	}

	public List<ExperimentPair> getExperiments() {
		return experiments;
	}
	
	public String getPFilename(boolean isMatchingPValue){
		String suffix = isMatchingPValue? "_match" : "_compose";
return edgeIndex+suffix;
	}
	
	public String getPValue(boolean isMatchingPValue) throws IOException, InterruptedException{
		String endFilename = getPFilename(isMatchingPValue);
		
		File RinputFile = new File(IOUtil.dataPath + IOUtil.IN + IOUtil.E_PATH +
				endFilename+".tsv");
		File RoutFile = new File(IOUtil.dataPath + IOUtil.OUT + IOUtil.E_PATH +
				endFilename+".Rout");
		IOUtil.createAndWrite(RinputFile, getWilcoxInputContent(isMatchingPValue));
		String scriptContent = wrapTestIO(RinputFile.getAbsolutePath(),getWilcoxTest(), RoutFile.getAbsolutePath(),"\\t");
		writeROutput(scriptContent, scriptTempFile);
		WilcoxOutput wo = new WilcoxOutput(IOUtil.getLines(RoutFile.getAbsolutePath()),endFilename);
		return Composer.formatPValue(wo.getPValue());
	}
	
	public String getWilcoxInputContent(boolean isMatching){
		StringBuilder sb = new StringBuilder();
		String[] colNames = edgeDescription.split("->");
		sb.append(colNames[0]+"\t"+colNames[1]+"\n");
		
		for(ExperimentPair ep: experiments){
			AnswerColumn[] acs = isMatching ? ep.getMatchingColumns() : ep.getComposingColumns();
			AnswerColumn leftAc = acs[0];
			AnswerColumn rightAc = acs[1];
			//should never happen, because we include NAN inputs here, so all should be 30.
			if(leftAc.getValues().length!=rightAc.getValues().length){
				throw new RuntimeException("cannot generate wilcox input with different number of measurements");
			}
			Double[] leftValues = leftAc.getValues();
			Double[] rightValues = rightAc.getValues();
			for(int k=0;k<leftValues.length;k++){
				sb.append(leftValues[k]+"\t"+rightValues[k]+"\n");
			}
		}
		return sb.toString();
	}
	
	public double getComposingPValue(){
		return 0.0;
	}

	@Override
	public String toString() {
		return "EdgeExperimentsList [edgeIndex=" + edgeIndex +
			", edgeDescription=" + edgeDescription + ", experiments=" +
			experiments + "]";
	}

	public String getLatexRow() throws IOException, InterruptedException {
		String end = "\\\\\n";
		return edgeIndex + between + edgeDescription + between + experiments.size() + between +
			getNumberSectionOfLatex() + end;
	}
	
	

	private String getNumberSectionOfLatex() throws IOException, InterruptedException {
		double[] numbers = new double[4];
		double counter=0;
		for(ExperimentPair ep : experiments){
			counter++;
			numbers[0]+=ep.getMatchingAvgLeft();
			numbers[1]+=ep.getMatchingAvgRight();
			numbers[2]+=ep.getComposingAvgLeft();
			numbers[3]+=ep.getComposingAvgRight();
		}
		for(int i=0;i<4;i++){
			numbers[i] = numbers[i]/counter;
		}
		return df3.format(numbers[0]) + between + df3.format(numbers[1]) + between +
				getPValue(true) + between + dfWhole.format(numbers[2]) +
				between + dfWhole.format(numbers[3]) + between + getPValue(false);
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

}
