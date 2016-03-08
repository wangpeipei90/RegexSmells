package complete_anova;

import java.io.File;
import java.io.IOException;

import readability_analysis.IOUtil;

public class QuickFindANOVA {
	private static File scriptTempFile = new File(IOUtil.dataPath + IOUtil.TMP +
			IOUtil.R_SCRIPTNAME);

	public static void main(String[] args) throws IOException, InterruptedException {
		File outDir = new File(IOUtil.dataPath + IOUtil.OUT);
		String filename = "COMPLETE_ANOVA.Rout";
		File inputFile = new File(IOUtil.dataPath+IOUtil.ORIGINAL,"COMPLETE_ANOVA_INPUT.csv");
		File outFile = new File(outDir, filename);
		writeROutput(wrapTestIO(inputFile.getAbsolutePath(), getANOVATest(), outFile.getAbsolutePath(), ","), scriptTempFile);
	}
	
	private static String getANOVATest() {
		return "test_output = aov(accuracy~string*abstract*node,data=tbl)\n"
			+ "results=summary(test_output)\n";
	}
	
	private static String wrapTestIO(String inPath, String testContent,
			String outPath, String sep) {
		StringBuilder sb = new StringBuilder();
		sb.append("tbl = read.table(\"" + inPath + "\",TRUE,\"" + sep + "\")\n");
		sb.append(testContent);
		sb.append("capture.output(results,file=\"" + outPath + "\")");
		return sb.toString();
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
