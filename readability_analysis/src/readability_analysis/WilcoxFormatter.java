package readability_analysis;

import java.util.List;

public class WilcoxFormatter implements RFormattable{

	public String formatData(List<AnswerColumn> allColumns, int firstInclusive,
			int rangeSize) {
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

	public int getNColumns() {
		return 2;
	}
}

