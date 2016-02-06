package readability_analysis;

import java.io.File;
import java.io.IOException;

public class Step2_CreateOutputFiles {
	private static File scriptTempFile = new File(IOUtil.basePath + IOUtil.TMP +
		IOUtil.R_SCRIPTNAME);

	public static void main(String[] args) throws IOException,
			InterruptedException {
		File p2_in_directory = new File(IOUtil.basePath + IOUtil.IN +
			IOUtil.P2_PATH);
		File p3_in_directory = new File(IOUtil.basePath + IOUtil.IN +
			IOUtil.P3_PATH);
		File t_in_directory = new File(IOUtil.basePath + IOUtil.IN +
			IOUtil.T_PATH);
		File m_in_directory = new File(IOUtil.basePath + IOUtil.IN +
			IOUtil.M_PATH);

		File p2_out_directory = new File(IOUtil.basePath + IOUtil.OUT +
			IOUtil.P2_PATH);
		File p3_out_directory = new File(IOUtil.basePath + IOUtil.OUT +
			IOUtil.P3_PATH);
		File t_out_directory = new File(IOUtil.basePath + IOUtil.OUT +
			IOUtil.T_PATH);
		File m_out_directory = new File(IOUtil.basePath + IOUtil.OUT +
			IOUtil.M_PATH);

		for (File p2File : p2_in_directory.listFiles()) {
			if (!p2File.isHidden()) {
				String newFilename = p2File.getName().replaceAll("tsv", "Rout");
				File outFile = new File(p2_out_directory, newFilename);
				writeROutput(wrapTestIO(p2File.getAbsolutePath(), getWilcoxTest(), outFile.getAbsolutePath(), "\\t"), scriptTempFile);
			}
		}

		for (File p3File : p3_in_directory.listFiles()) {
			if (!p3File.isHidden()) {
				String newFilename = p3File.getName().replaceAll("tsv", "Rout");
				File outFile = new File(p3_out_directory, newFilename);
				writeROutput(wrapTestIO(p3File.getAbsolutePath(), getWilcoxTest(), outFile.getAbsolutePath(), "\\t"), scriptTempFile);
			}
		}

		for (File tFile : t_in_directory.listFiles()) {
			if (!tFile.isHidden()) {
				String newFilename = tFile.getName().replaceAll("tsv", "Rout");
				File outFile = new File(t_out_directory, newFilename);
				writeROutput(wrapTestIO(tFile.getAbsolutePath(), getKruskalTest(), outFile.getAbsolutePath(), "\\t"), scriptTempFile);
			}
		}

		for (File mFile : m_in_directory.listFiles()) {
			if (!mFile.isHidden()) {
				String newFilename = mFile.getName().replaceAll("csv", "Rout");
				File outFile = new File(m_out_directory, newFilename);
				writeROutput(wrapTestIO(mFile.getAbsolutePath(), getANOVATest(), outFile.getAbsolutePath(), ","), scriptTempFile);
			}
		}

	}

	private static String wrapTestIO(String inPath, String testContent,
			String outPath, String sep) {
		StringBuilder sb = new StringBuilder();
		sb.append("tbl = read.table(\"" + inPath + "\",TRUE,\"" + sep + "\")\n");
		sb.append(testContent);
		sb.append("capture.output(results,file=\"" + outPath + "\")");
		return sb.toString();
	}

	private static String getKruskalTest() {
		return "results = kruskal.test(value ~ code, data=tbl)\n";
	}

	private static String getWilcoxTest() {
		return "results = wilcox.test(tbl[,1],tbl[,2])\n";
	}

	private static String getANOVATest() {
		return "test_output = aov(accuracy~regex*refactoring,data=tbl)\n"
			+ "results=summary(test_output)\n";
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
}
