package readability_analysis;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProcessDataMain {
	public static String basePath = "/Users/carlchapman/git/regex_readability_study/data/";
	public static String ORIGINAL = "original/";
	public static String IN = "Rinput/";
	public static String OUT = "Routput/";
	public static String SIMPLE = "simplified/";

	public static void main(String[] args) throws IOException {
		
		List<AnswerColumn> pairsFrom2 = getColumns(getLines(basePath + ORIGINAL + "pairs_from_2.csv"));
		List<AnswerColumn> pairsFrom3 = getColumns(getLines(basePath + ORIGINAL + "pairs_from_3.csv"));
		List<AnswerColumn> tripleLines = getColumns(getLines(basePath + ORIGINAL + "triples.csv"));
		
		System.out.println(getColumnRange(pairsFrom3,0,2));
		System.out.println(getColumnRange(tripleLines,12,3));
		System.out.println(getColumnRange(pairsFrom2,4,2));

	}
	
	private static String getColumnRange(List<AnswerColumn> allColumns, int firstInclusive, int rangeSize){
		//List<AnswerColumn> colsInRange = new ArrayList<AnswerColumn>(rangeSize);
		StringBuilder sb = new StringBuilder();
		int nRows = allColumns.get(0).getNRows();

		for(int i = 0;i<rangeSize;i++){
			int index = firstInclusive + i;
			sb.append(allColumns.get(index).getRegexCode());
			if(i<rangeSize-1){
				sb.append(",");
			}else{
				sb.append("\n");
			}
		}
		for(int j=0;j<nRows;j++){	
			for(int i = 0;i<rangeSize;i++){
				int index = firstInclusive + i;
				sb.append(allColumns.get(index).getValues()[j]);
				if(i<rangeSize-1){
					sb.append(",");
				}else{
					sb.append("\n");
				}
			}
		}
		return sb.toString();
	}
	
	private static List<AnswerColumn> getColumns(List<String> lines){

		String[] colNames = lines.get(0).split(",");
		int nCols = colNames.length;
		int nRows = lines.size()-1;
		
		//assume that the first line is column headers, well formed matrix, etc.
		//notice the divergence from convention - this is an array of columns
		Double[][] values = new Double[nCols][nRows];
		for(int i=0;i<nRows;i++){
			String[] rowValues = lines.get(i+1).split(",");
			for(int j=0;j<nCols;j++){
				values[j][i] = rowValues[j].equals("NA") ? Double.NaN : Double.parseDouble(rowValues[j]);
			}
		}
		List<AnswerColumn> answerColumns = new ArrayList<AnswerColumn>(nCols);
		for(int j=0;j<nCols;j++){
			answerColumns.add(new AnswerColumn(colNames[j],values[j]));
		}
		return answerColumns;
	}
	
	private static List<String> getLines(String filePathString)  throws IOException  {
		Path filePath = new File(filePathString).toPath();
		Charset charset = Charset.defaultCharset();        
		return Files.readAllLines(filePath, charset);
	}

}
