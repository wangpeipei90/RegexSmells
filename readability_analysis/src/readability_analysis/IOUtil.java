package readability_analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IOUtil {

	public static String basePath = "/Users/carlchapman/git/regex_readability_study/data/";
	public static String ORIGINAL = "original/";
	public static String IN = "Rinput/";
	public static String OUT = "Routput/";
	public static String TMP = "temp/";
	public static String SIMPLE = "simplified/";
	public static String P2_PATH = "P2/";
	public static String P3_PATH = "P3/";
	public static String T_PATH = "T/";
	public static final String R_SCRIPTNAME = "scRipt.r";
	public static void createAndWrite(File file, String content) {
		FileWriter fw;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
