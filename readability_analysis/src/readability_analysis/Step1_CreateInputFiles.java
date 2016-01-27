package readability_analysis;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Step1_CreateInputFiles {


	public static void main(String[] args) throws IOException {
		
		List<AnswerColumn> pairsFrom2 = getColumns(getLines(IOUtil.basePath + IOUtil.ORIGINAL + "pairs_from_2.csv"));
		List<AnswerColumn> pairsFrom3 = getColumns(getLines(IOUtil.basePath + IOUtil.ORIGINAL + "pairs_from_3.csv"));
		List<AnswerColumn> tripleLines = getColumns(getLines(IOUtil.basePath + IOUtil.ORIGINAL + "triples.csv"));
		
		int nPairs2 = pairsFrom2.size()/2;
		int nPairs3 = pairsFrom3.size()/2;
		int nTriplets = tripleLines.size()/3;
		
		writeInputFiles(pairsFrom2, nPairs2, 2, IOUtil.P2_PATH);
		writeInputFiles(pairsFrom3, nPairs3, 2, IOUtil.P3_PATH);
		writeInputFiles(tripleLines, nTriplets, 3, IOUtil.T_PATH);
	}
	
	private static void writeInputFiles(List<AnswerColumn> answerColumns, int nFiles, int columnsPerFile, String path){
		for(int fileIndex = 0;fileIndex < nFiles; fileIndex++){
			StringBuilder filenameBuilder = new StringBuilder();
			int offset = fileIndex * columnsPerFile;
			for(int colIndex = 0; colIndex <columnsPerFile; colIndex++){
				filenameBuilder.append(answerColumns.get(offset + colIndex).getRegexCode());
				if(colIndex<columnsPerFile-1){
					filenameBuilder.append("_");
				}else{
					filenameBuilder.append(".tsv");
				}
			}
			File f = new File(IOUtil.basePath + IOUtil.IN + path + filenameBuilder.toString());
			IOUtil.createAndWrite(f, getColumnRange(answerColumns,offset,columnsPerFile));
		}
	}
	
	private static String getColumnRange(List<AnswerColumn> allColumns, int firstInclusive, int rangeSize){
		//List<AnswerColumn> colsInRange = new ArrayList<AnswerColumn>(rangeSize);
		StringBuilder sb = new StringBuilder();
		int nRows = allColumns.get(0).getNRows();

		for(int i = 0;i<rangeSize;i++){
			int index = firstInclusive + i;
			sb.append(allColumns.get(index).getRegexCode());
			if(i<rangeSize-1){
				sb.append("\t");
			}else{
				sb.append("\n");
			}
		}
		for(int j=0;j<nRows;j++){	
			for(int i = 0;i<rangeSize;i++){
				int index = firstInclusive + i;
				Double valueDouble = allColumns.get(index).getValues()[j];
				String valueString = valueDouble.equals(Double.NaN) ? "NA" : valueDouble.toString();
				sb.append(valueString);
				if(i<rangeSize-1){
					sb.append("\t");
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
