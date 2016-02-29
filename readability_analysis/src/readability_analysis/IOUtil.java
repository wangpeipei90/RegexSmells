package readability_analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class IOUtil {

	public static String basePath = "/Users/carlchapman/git/regex_readability_study/data/";
	public static String ORIGINAL = "original/";
	public static String COMPOSITION = "composition/";
	public static String NODES = "nodes/";
	public static String IN = "Rinput/";
	public static String OUT = "Routput/";
	public static String TMP = "temp/";
	public static String SIMPLE = "simplified/";
	public static String P2_PATH = "P2/";
	public static String P3_PATH = "P3/";
	public static String T_PATH = "T/";
	public static final String R_SCRIPTNAME = "scRipt.r";
	public static final String M_PATH = "M/";
	public static final String CORPUS = "corpus/";

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

	public static List<String> getLines(String filePathString)
			throws IOException {
		Path filePath = new File(filePathString).toPath();
		Charset charset = Charset.defaultCharset();
		return Files.readAllLines(filePath, charset);
	}

	public static List<AnswerColumn> getColumns(List<String> lines, String delim) {

		String[] colNames = lines.get(0).split(delim);
		int nCols = colNames.length;
		int nRows = lines.size() - 1;

		// assume that the first line is column headers, well formed matrix,
		// etc.
		// notice the divergence from convention - this is an array of columns
		Double[][] values = new Double[nCols][nRows];
		for (int i = 0; i < nRows; i++) {
			String[] rowValues = lines.get(i + 1).split(delim);
			for (int j = 0; j < nCols; j++) {
				values[j][i] = rowValues[j].equals("NA") ? Double.NaN
						: Double.parseDouble(rowValues[j]);
			}
		}
		List<AnswerColumn> answerColumns = new ArrayList<AnswerColumn>(nCols);
		for (int j = 0; j < nCols; j++) {
			answerColumns.add(new AnswerColumn(colNames[j], values[j]));
		}
		return answerColumns;
	}

	public static List<AnswerColumn> getTripleColumns(List<String> lines,
			String delim) {
		List<AnswerColumn> answers = new ArrayList<AnswerColumn>(3);
		lines.remove(0);
		int nRowsPerColumn = lines.size() / 3;
		if (nRowsPerColumn * 3 != lines.size()) {
			throw new RuntimeException("number of lines may not be divisible by three in getTripleColumns");
		}
		for (int colIndex = 0; colIndex < 3; colIndex++) {
			int offset = colIndex * nRowsPerColumn;
			Double[] values = new Double[nRowsPerColumn];
			for (int i = 0; i < nRowsPerColumn; i++){
				String[] tokens = lines.get(i+offset).split(delim);
				String numberToken = tokens[1];
				Double val =  (numberToken.equals("NaN") || numberToken.equals("NA"))? Double.NaN : Double.parseDouble(tokens[1]);
				values[i] = val;
				if(i==nRowsPerColumn-1){
					answers.add(new AnswerColumn(tokens[0],values));
				}
			}
		}
		return answers;
	}

	public static HashMap<String, Pattern> getCodeMap(List<String> lines,
			String delim) {
		HashMap<String,Pattern> codeMap = new HashMap<String,Pattern>();

		for(String line : lines){
			//System.out.println(line);
			String[] tokens = line.split(delim);
			Pattern p = Pattern.compile(tokens[1].substring(0,tokens[1].length()-1));
			Pattern y = codeMap.put(tokens[0], p);
			if (y!=null){
				System.err.println("duplicate key");
			}
		}
		return codeMap;
	}

	public static HashMap<String, List<String>> getAnswerMap(
			List<String> lines, String delim) {
		HashMap<String,List<String>> answerMap = new HashMap<String,List<String>>();

		for(String line : lines){
			String[] tokens = line.split(delim);
			String tempCode = "";
			boolean isCode = true;
			for(String token : tokens){
				if(isCode){
					tempCode = token;
				}else{
					List<String> answers = answerMap.get(tempCode);
					if(answers==null){
						answers = new LinkedList<String>();
					}
					answers.add(token);
					answerMap.put(tempCode, answers);
				}
				isCode=!isCode;
			}
		}
		return answerMap;
	}

}
