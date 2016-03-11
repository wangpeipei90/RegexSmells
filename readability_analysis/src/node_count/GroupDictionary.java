package node_count;

import java.util.HashMap;
import java.util.regex.Pattern;

import node_count.metric.FeatureCountFactory;
import node_count.metric.FeatureDictionary;
import node_count.metric.FeatureSetClass;

public class GroupDictionary extends HashMap<String, NodeGroup> {
	private static final long serialVersionUID = 608250329975520302L;
	private static final Pattern dotStar = Pattern.compile(".*");

	public GroupDictionary() {
		initializeNodeGroups();
	}

	public void initializeNodeGroups() {
		NodeGroup CnodeList = new NodeGroup(C.CCC);
		NodeGroup DnodeList = new NodeGroup(C.DBB);
		NodeGroup TnodeList = new NodeGroup(C.LIT);
		NodeGroup LnodeList = new NodeGroup(C.LWB);
		NodeGroup SnodeList = new NodeGroup(C.SNG);


		DnodeList.add(new RTNode(C.D1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_DOUBLEBOUNDED)), dotStar));
		DnodeList.add(new RTNode(C.D2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_QUESTIONABLE)), dotStar));
		DnodeList.add(new RTNode(C.D3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile("(?<=[|])([^ \\\\]+)\\1+|([^ \\\\]+)\\1+(?=[|])")));

		SnodeList.add(new RTNode(C.S1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_SINGLEEXACTLY)), dotStar));
		SnodeList.add(new RTNode(C.S2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach()), dotStar));
		SnodeList.add(new RTNode(C.S3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_DOUBLEBOUNDED)), Pattern.compile("\\{(\\d+),\\1\\}")));

		LnodeList.add(new RTNode(C.L1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_LOWERBOUNDED)), dotStar));
		LnodeList.add(new RTNode(C.L2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_KLEENISH)), dotStar));
		LnodeList.add(new RTNode(C.L3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_ADDITIONAL)), dotStar));

		CnodeList.add(new RTNode(C.C1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_CC_RANGE,FeatureDictionary.I_META_CC)), dotStar));
		CnodeList.add(new RTNode(C.C2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_CC)), dotStar));
		CnodeList.add(new RTNode(C.C3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_NCC)), dotStar));
		CnodeList.add(new RTNode(C.C4, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_CC)), dotStar));
		CnodeList.add(new RTNode(C.C5, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), dotStar));

		TnodeList.add(new RTNode(C.T1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL)), dotStar));
		TnodeList.add(new RTNode(C.T2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL)), Pattern.compile("(\\\\x[a-f0-9A-F]{2})")));
		TnodeList.add(new RTNode(C.T3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL)), dotStar));
		TnodeList.add(new RTNode(C.T4, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL)), Pattern.compile("((\\\\0\\d*)|(\\\\\\d{3}))")));
		
		this.put(C.CCC,CnodeList);
		this.put(C.DBB,DnodeList);
		this.put(C.LIT,TnodeList);
		this.put(C.LWB,LnodeList);
		this.put(C.SNG,SnodeList);
	}

}
