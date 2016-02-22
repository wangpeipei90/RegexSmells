package readability_analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Step1_CreateInputFiles {
	private static WilcoxFormatter wilcoxFormatter = new WilcoxFormatter();
	private static KruskalFormatter kruskalFormatter = new KruskalFormatter();

	public static void main(String[] args) throws IOException {

		List<AnswerColumn> pairsFrom2 = IOUtil.getColumns(IOUtil.getLines(IOUtil.basePath +
			IOUtil.ORIGINAL + "pairs_from_2.csv"), ",");
		List<AnswerColumn> pairsFrom3 = IOUtil.getColumns(IOUtil.getLines(IOUtil.basePath +
			IOUtil.ORIGINAL + "pairs_from_3.csv"), ",");
		List<AnswerColumn> tripleLines = IOUtil.getColumns(IOUtil.getLines(IOUtil.basePath +
			IOUtil.ORIGINAL + "triples.csv"), ",");


		writeInputFiles(pairsFrom2, IOUtil.P2_PATH, wilcoxFormatter);
		writeInputFiles(pairsFrom3, IOUtil.P3_PATH, wilcoxFormatter);
		writeInputFiles(tripleLines, IOUtil.T_PATH, kruskalFormatter);
	}

	private static void writeInputFiles(List<AnswerColumn> answerColumns,
			String path, RFormattable formatter) {
		int columnsPerFile = formatter.getNColumns();
		int nFiles = answerColumns.size() / columnsPerFile;
		if (nFiles * columnsPerFile != answerColumns.size()) {
			throw new RuntimeException("number of columns must be evenly divisible by " +
				columnsPerFile);
		}
		for (int fileIndex = 0; fileIndex < nFiles; fileIndex++) {
			StringBuilder filenameBuilder = new StringBuilder();
			int offset = fileIndex * columnsPerFile;
			for (int colIndex = 0; colIndex < columnsPerFile; colIndex++) {
				filenameBuilder.append(answerColumns.get(offset + colIndex).getRegexCode());
				if (colIndex < columnsPerFile - 1) {
					filenameBuilder.append("_");
				} else {
					filenameBuilder.append(".tsv");
				}
			}
			File f = new File(IOUtil.basePath + IOUtil.IN + path +
				filenameBuilder.toString());
			IOUtil.createAndWrite(f, formatter.formatData(answerColumns, offset, columnsPerFile));
		}
	}
}
