package readability_analysis;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Step1_CreateInputFiles {
	private static WilcoxFormatter wilcoxFormatter = new WilcoxFormatter();
	private static KruskalFormatter kruskalFormatter = new KruskalFormatter();


	public static void main(String[] args) throws IOException {
		
		List<AnswerColumn> pairsFrom2 = getColumns(getLines(IOUtil.basePath + IOUtil.ORIGINAL + "pairs_from_2.csv"));
		List<AnswerColumn> pairsFrom3 = getColumns(getLines(IOUtil.basePath + IOUtil.ORIGINAL + "pairs_from_3.csv"));
		List<AnswerColumn> tripleLines = getColumns(getLines(IOUtil.basePath + IOUtil.ORIGINAL + "triples.csv"));

		writeInputFiles(pairsFrom2, IOUtil.P2_PATH, wilcoxFormatter);
		writeInputFiles(pairsFrom3, IOUtil.P3_PATH, wilcoxFormatter);
		writeInputFiles(tripleLines, IOUtil.T_PATH, kruskalFormatter);
	}
	
	private static void writeInputFiles(List<AnswerColumn> answerColumns,
			String path, RFormattable formatter) {
		int columnsPerFile = formatter.getNColumns();
		int nFiles = answerColumns.size()/columnsPerFile;
		if(nFiles * columnsPerFile != answerColumns.size()){
			throw new RuntimeException("number of columns must be evenly divisible by " + columnsPerFile);
		}
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
			IOUtil.createAndWrite(f, formatter.formatData(answerColumns, offset, columnsPerFile));
		}
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
