package readability_analysis;

import java.util.Arrays;

public class AnswerColumn {
	private String regexCode;
	private Double[] values;
	
	public AnswerColumn(String regexCode, Double[] values){
		this.regexCode = regexCode;
		this.values = values;
	}

	@Override
	public String toString() {
		return "AnswerColumn [regexCode=" + regexCode + ", values=" +
			Arrays.toString(values) + "]";
	}

	public int getNRows() {
		return values.length;
	}

	public String getRegexCode() {
		return regexCode;
	}

	public Double[] getValues() {
		return values;
	}
	
}
