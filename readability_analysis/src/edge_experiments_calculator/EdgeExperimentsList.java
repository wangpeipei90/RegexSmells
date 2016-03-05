package edge_experiments_calculator;

import java.util.List;
import java.util.Set;

public class EdgeExperimentsList {
	
	private final String edgeIndex;
	private final String edgeDescription;
	private final List<ExperimentPair> experiments;
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
	
	
}
