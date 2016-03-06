package edge_experiments_calculator;

import java.text.DecimalFormat;
import java.util.List;

public class EdgeExperimentsList {

	private final String edgeIndex;
	private final String edgeDescription;
	private final List<ExperimentPair> experiments;
	private static final String between = " & ";
	private static DecimalFormat df3 = new DecimalFormat("0.00");
	private static DecimalFormat df7 = new DecimalFormat("0.000000");
	private static DecimalFormat dfWhole = new DecimalFormat("#.0");

	public EdgeExperimentsList(String edgeIndex, String edgeDescription,
			List<ExperimentPair> experiments) {
		this.edgeIndex = edgeIndex;
		this.edgeDescription = edgeDescription;
		this.experiments = experiments;
	}

	public String getEdgeIndex() {
		return edgeIndex;
	}

	public String getEdgeDescription() {
		return edgeDescription;
	}

	public List<ExperimentPair> getExperiments() {
		return experiments;
	}

	@Override
	public String toString() {
		return "EdgeExperimentsList [edgeIndex=" + edgeIndex +
			", edgeDescription=" + edgeDescription + ", experiments=" +
			experiments + "]";
	}

	public String getLatexRow() {
		String end = "\\\\\n";
		return edgeIndex + between + edgeDescription + between + experiments.size() + between +
			getNumberSectionOfLatex() + end;
	}
	
	

	private String getNumberSectionOfLatex() {
		double[] numbers = new double[6];
		double counter=0;
		for(ExperimentPair ep : experiments){
			counter++;
			numbers[0]+=ep.getMatchingAvgLeft();
			numbers[1]+=ep.getMatchingAvgRight();
			numbers[2]+=ep.getComposingAvgLeft();
			numbers[3]+=ep.getComposingAvgRight();
			numbers[4]+=ep.getMatchingPValue();
			numbers[5]+=ep.getComposingPValue();
		}
		for(int i=0;i<6;i++){
			numbers[i]=numbers[i]/counter;
		}
		return df3.format(numbers[0]) + between + df3.format(numbers[1]) + between +
				dfWhole.format(numbers[2]) + between + dfWhole.format(numbers[3]) +
				between + df7.format(numbers[4]) + between + df7.format(numbers[5]);
	}

}
