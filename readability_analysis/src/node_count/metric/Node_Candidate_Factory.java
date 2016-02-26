package node_count.metric;

import java.util.HashMap;

public class Node_Candidate_Factory {
	
	public static final String D1 = "D1";
	public static final String D2 = "D2";
	public static final String D3 = "D3";
	public static final String D4 = "D4";
	public static final String S3 = "S3";
	
	public static FeatureSetClass getCandidate(String candidateCode){
		switch(candidateCode){
		case D1: 
		case S3:
			//intentionally blank - both D1, S3 and D2 do the same thing
		case D2:
			return new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_DOUBLEBOUNDED));
			
			
		case D3:
			return new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_QUESTIONABLE));
		case D4:
			return new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_KLEENISH));
			
			
			//default is empty one
		default: return new FeatureSetClass(new FeatureCount(new HashMap<Integer,Integer>()));
		}
	}

}
