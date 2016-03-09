package edge_experiments_calculator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import readability_analysis.AnswerColumn;

public class ExperimentPair {
	private final AnswerColumn[] matchingColumns;
	private final AnswerColumn[] composingColumns;
	private boolean isFrom2;
	private String fileName;
	
	public ExperimentPair(String code0, String code1,List<AnswerColumn> pairsFrom2,List<AnswerColumn> pairsFrom3,List<AnswerColumn> compositionAnswers) throws IOException {
		this.matchingColumns = new AnswerColumn[2];
		this.composingColumns = new AnswerColumn[2];
		Iterator<AnswerColumn> it2 = pairsFrom2.iterator();
		while(it2.hasNext()){
			
			AnswerColumn ac1 = it2.next();
			AnswerColumn ac2 = it2.next();
			if(code0.equals(ac1.getRegexCode()) && code1.equals(ac2.getRegexCode())){
				isFrom2 = true;
				matchingColumns[0] = ac1;
				matchingColumns[1] = ac2;
				this.fileName = ac1.getRegexCode()+"_"+ac2.getRegexCode();
				break;
			}else if(code1.equals(ac1.getRegexCode()) && code0.equals(ac2.getRegexCode())){
				isFrom2 = true;
				matchingColumns[0] = ac2;
				matchingColumns[1] = ac1;
				this.fileName = ac1.getRegexCode()+"_"+ac2.getRegexCode();
				break;
			}
		}
		if(!isFrom2){
			Iterator<AnswerColumn> it3 = pairsFrom3.iterator();
			while(it3.hasNext()){
				AnswerColumn ac1 = it3.next();
				AnswerColumn ac2 = it3.next();
				if(code0.equals(ac1.getRegexCode()) && code1.equals(ac2.getRegexCode())){
					isFrom2 = false;
					matchingColumns[0] = ac1;
					matchingColumns[1] = ac2;
					this.fileName = ac1.getRegexCode()+"_"+ac2.getRegexCode();
					break;
				}else if(code1.equals(ac1.getRegexCode()) && code0.equals(ac2.getRegexCode())){
					isFrom2 = false;
					matchingColumns[0] = ac2;
					matchingColumns[1] = ac1;
					this.fileName = ac1.getRegexCode()+"_"+ac2.getRegexCode();
					break;
				}
			}
		}
		for(AnswerColumn compAC : compositionAnswers){
			if(matchingColumns[0].getRegexCode().equals(compAC.getRegexCode())){
				composingColumns[0] = compAC;
			}
			if(matchingColumns[1].getRegexCode().equals(compAC.getRegexCode())){
				composingColumns[1] = compAC;
			}
		}
	}
	
	public double getMatchingAvgLeft(){
		return matchingColumns[0].getAvg();
	}
	public double getMatchingAvgRight(){
		return matchingColumns[1].getAvg();
	}
	
	public double getComposingAvgLeft(){
		return composingColumns[0].getX()/(composingColumns[0].getCount()+0.0);
	}
	public double getComposingAvgRight(){
		return composingColumns[1].getX()/(composingColumns[1].getCount()+0.0);
	}

	public AnswerColumn[] getMatchingColumns() {
		return matchingColumns;
	}

	public AnswerColumn[] getComposingColumns() {
		return composingColumns;
	}

	@Override
	public String toString() {
		return "ExperimentPair [matchingColumns=" +
			Arrays.toString(matchingColumns) + ", composingColumns=" +
			Arrays.toString(composingColumns) + ", isFrom2=" + isFrom2 +
			", fileName=" + fileName + "]";
	}
}
