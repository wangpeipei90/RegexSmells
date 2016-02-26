package node_count;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import node_count.build_corpus.WeightRankedRegex;
import node_count.metric.FeatureSetClass;

//refactoring tree node.  It's one node from the diagram in the paper...
public class RTNode {

	private final String name;
	private final FeatureSetClass requiredFeatures;
	private final Pattern filter;
	
	public RTNode(String name, FeatureSetClass requiredFeatures, Pattern filter) {
		this.filter = filter;
		this.name = name;
		this.requiredFeatures = requiredFeatures;
	}
	
	public boolean matches(WeightRankedRegex wrr){
		if(wrr.subsumes(requiredFeatures)){
			Matcher m = filter.matcher(wrr.unquoted);
			if(m.find()){
				return true;
			}
		}
		return false;
	}
	

}
