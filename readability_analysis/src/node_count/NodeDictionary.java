package node_count;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import node_count.build_corpus.WeightRankedRegex;
import node_count.metric.FeatureCountFactory;
import node_count.metric.FeatureDictionary;
import node_count.metric.FeatureSetClass;

public class NodeDictionary extends HashMap<RTNode, List<WeightRankedRegex>> {
	private static ArrayList<RTNode> DnodeList;
	private static ArrayList<RTNode> SnodeList;
	private static ArrayList<RTNode> LnodeList;
	private static ArrayList<RTNode> CnodeList;
	private static ArrayList<RTNode> RnodeList;
	private static ArrayList<RTNode> PnodeList;
	private static ArrayList<RTNode> TnodeList;
	private static ArrayList<RTNode> WnodeList;
	private static ArrayList<RTNode> BnodeList;

	public NodeDictionary() {
		initializeNodeLists();

	}

	public static void initializeNodeLists() {
		DnodeList = new ArrayList<RTNode>();
		SnodeList = new ArrayList<RTNode>();
		LnodeList = new ArrayList<RTNode>();
		CnodeList = new ArrayList<RTNode>();
		RnodeList = new ArrayList<RTNode>();
		PnodeList = new ArrayList<RTNode>();
		TnodeList = new ArrayList<RTNode>();
		WnodeList = new ArrayList<RTNode>();
		BnodeList = new ArrayList<RTNode>();

		DnodeList.add(new RTNode("D1", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_DOUBLEBOUNDED)), Pattern.compile(".*")));
		DnodeList.add(new RTNode("D2", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_DOUBLEBOUNDED)), Pattern.compile(".*")));
		DnodeList.add(new RTNode("D3", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_QUESTIONABLE)), Pattern.compile(".*")));
		DnodeList.add(new RTNode("D4", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile(".*")));

		SnodeList.add(new RTNode("S1", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_SINGLEEXACTLY)), Pattern.compile(".*")));
		SnodeList.add(new RTNode("S2", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach()), Pattern.compile(".*")));
		SnodeList.add(new RTNode("S3", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_DOUBLEBOUNDED)), Pattern.compile(".*")));

		LnodeList.add(new RTNode("L1", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_LOWERBOUNDED)), Pattern.compile(".*")));
		LnodeList.add(new RTNode("L2", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_KLEENISH)), Pattern.compile(".*")));
		LnodeList.add(new RTNode("L3", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_LOWERBOUNDED)), Pattern.compile(".*")));
		LnodeList.add(new RTNode("L4", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_ADDITIONAL)), Pattern.compile(".*")));

		CnodeList.add(new RTNode("C1", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_CC_RANGE,FeatureDictionary.I_META_CC)), Pattern.compile(".*")));
		CnodeList.add(new RTNode("C2", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_CC)), Pattern.compile(".*")));
		CnodeList.add(new RTNode("C3", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile(".*")));
		CnodeList.add(new RTNode("C4", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_NCC)), Pattern.compile(".*")));
		CnodeList.add(new RTNode("C5", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_CC)), Pattern.compile(".*")));
		CnodeList.add(new RTNode("C6", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile(".*")));

		RnodeList.add(new RTNode("R1", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile(".*")));
		RnodeList.add(new RTNode("R2", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile(".*")));
		RnodeList.add(new RTNode("R3", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile(".*")));

		PnodeList.add(new RTNode("P1", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach()), Pattern.compile(".*")));
		PnodeList.add(new RTNode("P2", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_DOT_ANY,FeatureDictionary.I_XTRA_OPTIONS)), Pattern.compile(".*")));
		PnodeList.add(new RTNode("P3", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_XTRA_OPTIONS,FeatureDictionary.I_META_LITERAL)), Pattern.compile(".*")));
		PnodeList.add(new RTNode("P4", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_XTRA_OPTIONS,FeatureDictionary.I_XTRA_END_SUBJECTLINE)), Pattern.compile(".*")));
		PnodeList.add(new RTNode("P5", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_XTRA_OPTIONS,FeatureDictionary.I_META_LITERAL)), Pattern.compile(".*")));
		PnodeList.add(new RTNode("P6", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL)), Pattern.compile(".*")));

		TnodeList.add(new RTNode("T1", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		TnodeList.add(new RTNode("T2", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		TnodeList.add(new RTNode("T3", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		TnodeList.add(new RTNode("T4", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		TnodeList.add(new RTNode("T5", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		TnodeList.add(new RTNode("T6", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		TnodeList.add(new RTNode("T7", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));

		WnodeList.add(new RTNode("W1", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		WnodeList.add(new RTNode("W2", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		WnodeList.add(new RTNode("W3", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		WnodeList.add(new RTNode("W4", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));

		BnodeList.add(new RTNode("B1", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
		BnodeList.add(new RTNode("B2", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), Pattern.compile(".*")));
	}

}
