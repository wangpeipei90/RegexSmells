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
		NodeGroup BnodeList = new NodeGroup(C.BKR);
		NodeGroup CnodeList = new NodeGroup(C.CCC);
		NodeGroup DnodeList = new NodeGroup(C.DBB);
		NodeGroup TnodeList = new NodeGroup(C.LIT);
		NodeGroup LnodeList = new NodeGroup(C.LWB);
		NodeGroup PnodeList = new NodeGroup(C.OPT);
		NodeGroup RnodeList = new NodeGroup(C.OR);
		NodeGroup SnodeList = new NodeGroup(C.SNG);
		NodeGroup WnodeList = new NodeGroup(C.WNW);


		DnodeList.add(new RTNode(C.D1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_DOUBLEBOUNDED)), Pattern.compile("\\{[1-9]\\d*,[02-9]\\d*\\}")));
		DnodeList.add(new RTNode(C.D2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_DOUBLEBOUNDED)), Pattern.compile("\\{0,1\\}")));
		DnodeList.add(new RTNode(C.D3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_QUESTIONABLE)), dotStar));
		DnodeList.add(new RTNode(C.D4, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile("(?<=[|(]|^)([^ \\\\]+)\\|\\1\\1")));

		SnodeList.add(new RTNode(C.S1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_SINGLEEXACTLY)), dotStar));
		SnodeList.add(new RTNode(C.S2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach()), dotStar));
		SnodeList.add(new RTNode(C.S3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_DOUBLEBOUNDED)), Pattern.compile("\\{(\\d+),\\1\\}")));

		LnodeList.add(new RTNode(C.L1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_LOWERBOUNDED)), Pattern.compile("\\{[1-9]\\d*,\\}")));
		LnodeList.add(new RTNode(C.L2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_KLEENISH)), dotStar));
		LnodeList.add(new RTNode(C.L3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_LOWERBOUNDED)), Pattern.compile("\\{0,\\}")));
		LnodeList.add(new RTNode(C.L4, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_REP_ADDITIONAL)), dotStar));

		CnodeList.add(new RTNode(C.C1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_CC_RANGE,FeatureDictionary.I_META_CC)), dotStar));
		CnodeList.add(new RTNode(C.C2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_CC)), dotStar));
		CnodeList.add(new RTNode(C.C3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), dotStar));
		CnodeList.add(new RTNode(C.C4, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_NCC)), dotStar));
		CnodeList.add(new RTNode(C.C5, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_CC)), dotStar));
		CnodeList.add(new RTNode(C.C6, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), dotStar));

		RnodeList.add(new RTNode(C.R1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile("\\([^|]+(\\|[^|]+)+\\).+|.+\\([^|]+(\\|[^|]+)+\\)")));
		RnodeList.add(new RTNode(C.R2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), Pattern.compile("^[^|]+(\\|[^|]+)+$")));
		RnodeList.add(new RTNode(C.R3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_OR)), dotStar));

		PnodeList.add(new RTNode(C.P1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach()), dotStar));
		PnodeList.add(new RTNode(C.P2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_DOT_ANY,FeatureDictionary.I_XTRA_OPTIONS)), dotStar));
		PnodeList.add(new RTNode(C.P3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_XTRA_OPTIONS,FeatureDictionary.I_META_LITERAL)), Pattern.compile(".*(\\\\n|\\\\\\\\n)$")));
		PnodeList.add(new RTNode(C.P4, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_XTRA_OPTIONS,FeatureDictionary.I_XTRA_END_SUBJECTLINE)), Pattern.compile(".*\\$$")));
		PnodeList.add(new RTNode(C.P5, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_XTRA_OPTIONS,FeatureDictionary.I_META_LITERAL)), dotStar));
		PnodeList.add(new RTNode(C.P6, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL)), dotStar)); //not sure what to do with P6

		TnodeList.add(new RTNode(C.T1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL)), dotStar));
		TnodeList.add(new RTNode(C.T2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL)), Pattern.compile("(?<!\\[)(\\\\x[a-f0-9A-F]{2})(?!\\])")));
		TnodeList.add(new RTNode(C.T3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL)), Pattern.compile("(?<!\\[)((\\\\0\\d*)|(\\\\\\d{3}))(?!\\])")));
		TnodeList.add(new RTNode(C.T4, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL,FeatureDictionary.I_META_CC)), dotStar));
		TnodeList.add(new RTNode(C.T5, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL,FeatureDictionary.I_META_CC)), dotStar));
		TnodeList.add(new RTNode(C.T6, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL,FeatureDictionary.I_META_CC)), Pattern.compile("(?<=\\[)(\\\\x[a-f0-9A-F]{2})(?=\\])")));
		TnodeList.add(new RTNode(C.T7, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_LITERAL,FeatureDictionary.I_META_CC)), Pattern.compile("(?<=\\[)((\\\\0\\d*)|(\\\\\\d{3}))(?=\\])")));

		WnodeList.add(new RTNode(C.W1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_WORD)), dotStar));
		WnodeList.add(new RTNode(C.W2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach()), Pattern.compile("\\(\\(\\?<=\\\\w\\)\\(\\?=\\\\W\\)\\|\\(\\?<=\\\\W\\)\\(\\?=\\\\w\\)\\)")));
		WnodeList.add(new RTNode(C.W3, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_POS_NONWORD)), dotStar));
		WnodeList.add(new RTNode(C.W4, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach()), Pattern.compile("\\(\\(\\?<=\\\\w\\)\\(\\?=\\\\w\\)\\|\\(\\?<=\\\\W\\)\\(\\?=\\\\W\\)\\)")));

		BnodeList.add(new RTNode(C.B1, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_META_NUMBERED_BACKREFERENCE)), dotStar));
		BnodeList.add(new RTNode(C.B2, new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_XTRA_NAMED_BACKREFERENCE,FeatureDictionary.I_XTRA_NAMED_GROUP_PYTHON)), dotStar));
		
		this.put(C.BKR,BnodeList);
		this.put(C.CCC,CnodeList);
		this.put(C.DBB,DnodeList);
		this.put(C.LIT,TnodeList);
		this.put(C.LWB,LnodeList);
		this.put(C.OPT,PnodeList);
		this.put(C.OR,RnodeList);
		this.put(C.SNG,SnodeList);
		this.put(C.WNW,WnodeList);
	}

}
