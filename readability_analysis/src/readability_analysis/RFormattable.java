package readability_analysis;

import java.util.List;

public interface RFormattable {
	
	public String formatData(List<AnswerColumn> answerColumns, int offset, int nColumns);

	public int getNColumns();

}
