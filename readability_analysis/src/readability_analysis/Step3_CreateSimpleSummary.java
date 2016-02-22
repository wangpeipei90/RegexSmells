package readability_analysis;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Variance;

public class Step3_CreateSimpleSummary {
	private static DecimalFormat df3 = new DecimalFormat("0.00");
	private static DecimalFormat df5 = new DecimalFormat("0.0000");
	private static List<KruskalOutput> kOuts = new LinkedList<KruskalOutput>();
	private static double alpha = 0.05;

	public static void main(String[] args) throws IOException {
		File p2_in_directory = new File(IOUtil.basePath + IOUtil.IN +
			IOUtil.P2_PATH);
		File p3_in_directory = new File(IOUtil.basePath + IOUtil.IN +
			IOUtil.P3_PATH);
		File t_in_directory = new File(IOUtil.basePath + IOUtil.IN +
			IOUtil.T_PATH);

		File p2_out_directory = new File(IOUtil.basePath + IOUtil.OUT +
			IOUtil.P2_PATH);
		File p3_out_directory = new File(IOUtil.basePath + IOUtil.OUT +
			IOUtil.P3_PATH);
		File t_out_directory = new File(IOUtil.basePath + IOUtil.OUT +
			IOUtil.T_PATH);
		File m_out_directory = new File(IOUtil.basePath + IOUtil.OUT +
			IOUtil.M_PATH);
		File allDataSimplified = new File(IOUtil.basePath + IOUtil.SIMPLE +
			"simpleSummary.tsv");
		IOUtil.createAndWrite(allDataSimplified, getPairsFromTwo(p2_out_directory, p2_in_directory) +
			getTriples(t_out_directory, t_in_directory) +
			getPairsFromThree(p3_out_directory, p3_in_directory) +
			getMetagroupANOVAs(m_out_directory));
	}

	private static String getPairsFromTwo(File p2_out_directory,
			File p2_in_directory) throws IOException {
		File p2_out_compose_directory = new File(IOUtil.basePath + IOUtil.OUT +
				IOUtil.COMPOSITION + IOUtil.P2_PATH);
		
		
		StringBuilder sb = new StringBuilder();
		sb.append(":::::::::BEGIN PAIRS FROM TWO::::::::::\n");
		sb.append("regex_Code1\tregex_Code2\tavg1\tavg2\tvar1\tvar2\tcCount1\tcCount2\twValue\tpValue\tpValueComposed\n");
		for (File p2File : p2_out_directory.listFiles()) {
			if (!p2File.isHidden()) {
				String inputFilename = p2File.getName().replaceAll("Rout", "tsv");
				List<AnswerColumn> inputColumns = IOUtil.getColumns(IOUtil.getLines(p2_in_directory.getAbsolutePath() +
					"/" + inputFilename), "\t");
				for (AnswerColumn ac : inputColumns) {
					sb.append(ac.getRegexCode() + "\t");
				}
				for (AnswerColumn ac : inputColumns) {
					sb.append(df3.format(ac.getAvg()) + "\t");
				}
				for (AnswerColumn ac : inputColumns) {
					Variance variance = new Variance();
					sb.append(df3.format(variance.evaluate(ac.getExistingValues())) +
						"\t");
				}
				File p2C = new File(p2_out_compose_directory, p2File.getName());
				ProportionOutput po = new ProportionOutput(IOUtil.getLines(p2C.getAbsolutePath()), p2File.getName());
				sb.append(po.getCountText());
				
				WilcoxOutput wo = new WilcoxOutput(IOUtil.getLines(p2File.getAbsolutePath()), p2File.getName());

				sb.append(df3.format(wo.getWValue()) + "\t" +
					df5.format(wo.getPValue()) + "\t"+po.getP_value()+"\n");
			}
		}
		sb.append(":::::::::END PAIRS FROM TWO::::::::::\n\n");
		return sb.toString();
	}

	private static String getTriples(File t_out_directory, File t_in_directory)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(":::::::::BEGIN TRIPLES::::::::::\n");
		sb.append("regex_Code1\tregex_Code2\tregex_Code3\tavg1\tavg2\tavg3\tvar1\tvar2\tvar3\tchiValue\tdfValue\tPvalue\tvalid\n");
		for (File tFile : t_out_directory.listFiles()) {
			if (!tFile.isHidden()) {
				String inputFilename = tFile.getName().replaceAll("Rout", "tsv");
				List<AnswerColumn> inputColumns = IOUtil.getTripleColumns(IOUtil.getLines(t_in_directory.getAbsolutePath() +
					"/" + inputFilename), "\t");
				for (AnswerColumn ac : inputColumns) {
					sb.append(ac.getRegexCode() + "\t");
				}
				for (AnswerColumn ac : inputColumns) {
					sb.append(df3.format(ac.getAvg()) + "\t");
				}
				for (AnswerColumn ac : inputColumns) {
					Variance variance = new Variance();
					sb.append(df3.format(variance.evaluate(ac.getExistingValues())) +
						"\t");
				}
				KruskalOutput ko = new KruskalOutput(IOUtil.getLines(tFile.getAbsolutePath()), tFile.getName(), inputColumns, alpha);
				kOuts.add(ko);
				sb.append(df5.format(ko.getChiValue()) + "\t" +
					df5.format(ko.getDfValue()) + "\t" +
					df5.format(ko.getPValue()) + "\t" + ko.isValid() + "\n");
			}
		}
		sb.append(":::::::::END TRIPLES::::::::::\n\n");
		return sb.toString();
	}

	private static String getPairsFromThree(File p3_out_directory,
			File p3_in_directory) throws IOException {
		File p3_out_compose_directory = new File(IOUtil.basePath + IOUtil.OUT +
				IOUtil.COMPOSITION + IOUtil.P3_PATH);
		
		
		StringBuilder sb = new StringBuilder();
		sb.append(":::::::::BEGIN PAIRS FROM THREE::::::::::\n");
		sb.append("regex_Code1\tregex_Code2\tavg1\tavg2\tvar1\tvar2\tcCount1\tcCount2\twValue\tpValue\tpValueComposed\n");
		for (File p3File : p3_out_directory.listFiles()) {
			if (!p3File.isHidden()) {
				String inputFilename = p3File.getName().replaceAll("Rout", "tsv");
				List<AnswerColumn> inputColumns = IOUtil.getColumns(IOUtil.getLines(p3_in_directory.getAbsolutePath() +
					"/" + inputFilename), "\t");
				for (AnswerColumn ac : inputColumns) {
					sb.append(ac.getRegexCode() + "\t");
				}
				for (AnswerColumn ac : inputColumns) {
					sb.append(df3.format(ac.getAvg()) + "\t");
				}
				for (AnswerColumn ac : inputColumns) {
					Variance variance = new Variance();
					sb.append(df3.format(variance.evaluate(ac.getExistingValues())) +
						"\t");
				}
				File p3C = new File(p3_out_compose_directory, p3File.getName());
				ProportionOutput po = new ProportionOutput(IOUtil.getLines(p3C.getAbsolutePath()), p3File.getName());
				sb.append(po.getCountText());
				
				WilcoxOutput wo = new WilcoxOutput(IOUtil.getLines(p3File.getAbsolutePath()), p3File.getName());
				sb.append(df3.format(wo.getWValue()) + "\t" +
					df5.format(wo.getPValue()) + "\t");
				boolean relevantValidity = getValidity(inputColumns.get(0).getRegexCode(), inputColumns.get(1).getRegexCode());
				sb.append(relevantValidity + "\t"+po.getP_value()+"\n");
			}
		}
		sb.append(":::::::::END PAIRS FROM THREE::::::::::\n\n");
		return sb.toString();
	}

	private static String getMetagroupANOVAs(File m_out_directory)
			throws IOException {
		File m_out_compose_directory = new File(IOUtil.basePath + IOUtil.OUT +
			IOUtil.COMPOSITION + IOUtil.M_PATH);
		StringBuilder sb = new StringBuilder();
		sb.append(":::::::::BEGIN METAGROUP ANOVA RESULTS::::::::::\n");
		sb.append("Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1\n\n\n");
		for (File mFile : m_out_directory.listFiles()) {
			if (!mFile.isHidden()) {
				sb.append(mFile.getName().subSequence(0, mFile.getName().indexOf(".")) +
					"\n");
				List<String> lines = IOUtil.getLines(mFile.getAbsolutePath());
				int nLinesToInclude = 5;
				int lineCounter = 0;
				while (lineCounter < nLinesToInclude) {
					sb.append(lines.get(lineCounter++) + "\n");
				}

				List<String> cLines = IOUtil.getLines(m_out_compose_directory +
					"/" + mFile.getName());
				lineCounter = 0;
				while (lineCounter < nLinesToInclude) {
					sb.append(cLines.get(lineCounter++) + "\n");
				}
				sb.append("\n\n");
			}
		}
		sb.append(":::::::::END METAGROUP ANOVA RESULTS::::::::::\n\n");
		return sb.toString();
	}

	private static boolean getValidity(String regexCode, String regexCode2) {
		for (KruskalOutput ku : kOuts) {
			if (ku.hasBothNames(regexCode, regexCode2) && ku.isValid()) {
				return true;
			}
		}
		return false;
	}
}
