package readability_analysis;

import java.io.File;
import java.io.IOException;

public class Step2_CreateOutputFiles {
	private static File scriptTempFile = new File(IOUtil.basePath + IOUtil.TMP + IOUtil.R_SCRIPTNAME);
	
	public static void main(String[] args) throws IOException, InterruptedException {
		File p2_in_directory = new File(IOUtil.basePath + IOUtil.IN + IOUtil.P2_PATH);
		File p3_in_directory = new File(IOUtil.basePath + IOUtil.IN + IOUtil.P3_PATH);
		File t_in_directory = new File(IOUtil.basePath + IOUtil.IN + IOUtil.T_PATH);
		
		File p2_out_directory = new File(IOUtil.basePath + IOUtil.OUT + IOUtil.P2_PATH);
		File p3_out_directory = new File(IOUtil.basePath + IOUtil.OUT + IOUtil.P3_PATH);
		File t_out_directory = new File(IOUtil.basePath + IOUtil.OUT + IOUtil.T_PATH);

		for( File p2File : p2_in_directory.listFiles()){
			if(!p2File.isHidden()){
				String newFilename = p2File.getName().replaceAll("tsv", "Rout");
				File outFile = new File(p2_out_directory,newFilename);
				writeROutput(getWilcoxTest(p2File.getAbsolutePath(),outFile.getAbsolutePath()), scriptTempFile);
			}
		}
		
		for( File p3File : p3_in_directory.listFiles()){
			if(!p3File.isHidden()){
				String newFilename = p3File.getName().replaceAll("tsv", "Rout");
				File outFile = new File(p3_out_directory,newFilename);
				writeROutput(getWilcoxTest(p3File.getAbsolutePath(),outFile.getAbsolutePath()), scriptTempFile);
			}
		}
		
		for( File tFile : t_in_directory.listFiles()){
			if(!tFile.isHidden()){
				String newFilename = tFile.getName().replaceAll("tsv", "Rout");
				File outFile = new File(t_out_directory,newFilename);
				//writeROutput(getKruskalTest(p3File.getAbsolutePath(),outFile.getAbsolutePath()), scriptTempFile);
			}
		}
		
	}
	
	private static String getWilcoxTest(String inPath, String outPath){
		StringBuilder sb = new StringBuilder();
		sb.append("tbl = read.table(\""+inPath+"\",TRUE)\n");
		sb.append("results = wilcox.test(tbl[,1],tbl[,2])\n");
		sb.append("capture.output(results,file=\""+outPath+"\")");
		return sb.toString();
	}


	private static void writeROutput(String scriptContent, File scriptTempFile) throws IOException, InterruptedException {
		//write over the temp R input script with given content
		IOUtil.createAndWrite(scriptTempFile, scriptContent);
		
		// run the R script
		Process p = Runtime.getRuntime().exec("/usr/local/bin/R CMD BATCH " +
				scriptTempFile.getAbsolutePath());

		// add + " /dev/null" to the end to silence output
		p.waitFor();
	}
}
