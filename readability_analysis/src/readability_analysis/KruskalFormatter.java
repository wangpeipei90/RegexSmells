package readability_analysis;

import java.util.List;

public class KruskalFormatter implements RFormattable {

	public String formatData(List<AnswerColumn> allColumns, int firstInclusive,
			int rangeSize) {
		StringBuilder sb = new StringBuilder();
		sb.append("code\tvalue\n");
		
		int nRows = allColumns.get(0).getNRows();
		for (int i = 0; i < rangeSize; i++) {
			AnswerColumn column = allColumns.get(firstInclusive + i);
			String codeName = column.getRegexCode();
			for (int j = 0; j < nRows; j++) {
				sb.append(codeName +"\t" + column.getValues()[j] + "\n");
			}
		}
		return sb.toString();
	}

	//for our purposes this is always 3, but in fact only needs to be greater than 2
	public int getNColumns() {
		return 3;
	}

}
