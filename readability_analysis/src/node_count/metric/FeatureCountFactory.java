package node_count.metric;

import java.util.HashMap;

public class FeatureCountFactory {
	
	public static FeatureCount constructWithOneOfEach(Integer...keyList){
		HashMap<Integer,Integer> counts = new HashMap<Integer,Integer>();
		for(Integer key : keyList){
			counts.put(key, 1);
		}
		return new FeatureCount(counts);
	}
}
