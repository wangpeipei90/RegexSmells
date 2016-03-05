package edge_experiments_calculator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import readability_analysis.AnswerColumn;

public class ExperimentPair {
	private final AnswerColumn[] columns;
	private boolean isFrom2;
	private final String fileName;
	public ExperimentPair(String code0, String code1,List<AnswerColumn> pairsFrom2,List<AnswerColumn> pairsFrom3) {
		this.columns = new AnswerColumn[2];
		Iterator<AnswerColumn> it2 = pairsFrom2.iterator();
		while(it2.hasNext()){
			
			AnswerColumn ac1 = it2.next();
			AnswerColumn ac2 = it2.next();
			if(code0.equals(ac1.getRegexCode()) && code1.equals(ac2.getRegexCode())){
				isFrom2 = true;
				columns[0] = ac1;
				columns[1] = ac2;
				break;
			}else if(code1.equals(ac1.getRegexCode()) && code0.equals(ac2.getRegexCode())){
				isFrom2 = true;
				columns[0] = ac2;
				columns[1] = ac1;
				break;
			}
		}
		if(!isFrom2){
			Iterator<AnswerColumn> it3 = pairsFrom3.iterator();
			while(it3.hasNext()){
				AnswerColumn ac1 = it3.next();
				AnswerColumn ac2 = it3.next();
				if(code0.equals(ac1.getRegexCode()) && code1.equals(ac2.getRegexCode())){
					isFrom2 = true;
					columns[0] = ac1;
					columns[1] = ac2;
					break;
				}else if(code1.equals(ac1.getRegexCode()) && code0.equals(ac2.getRegexCode())){
					isFrom2 = true;
					columns[0] = ac2;
					columns[1] = ac1;
					break;
				}
			}
		}
		fileName = "tired now will code later";
	}
	
	public AnswerColumn getLeftCol(){
		return columns[0];
	}
	
	public AnswerColumn getRightCol(){
		return columns[1];
	}

	@Override
	public String toString() {
		return "ExperimentPair [columns=" + Arrays.toString(columns) +
			", isFrom2=" + isFrom2 + ", fileName=" + fileName + "]";
	}
	
	

}
