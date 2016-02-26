package node_count.metric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import node_count.build_corpus.WeightRankedRegex;
import node_count.exceptions.AlienFeatureException;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import node_count.pcre.PCREParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.RewriteRuleSubtreeStream;
import org.antlr.runtime.tree.TreeAdaptor;

public class NodeChecker {

	public static final String D1 = "D1";
	public static final String D2 = "D2";
	public static final String D3 = "D3";
	public static final String D4 = "D4";
	public static final String S3 = "S3";

	public static boolean containsNode(WeightRankedRegex wrr,
			String candidateCode) throws IllegalArgumentException,
			QuoteRuleException, PythonParsingException {
		switch (candidateCode) {
		case D1:
			if (wrr.hasFeature(FeatureDictionary.I_REP_DOUBLEBOUNDED)) {
				return hasZeroOneDBB(wrr);
			}

			// default is false
		default:
			return false;
		}
	}
	
	private static CommonTree getQuestionable(){
		TreeAdaptor adaptor = new CommonTreeAdaptor();
		CommonTree root_1 = (CommonTree)adaptor.nil();
		root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PCREParser.QUANTIFIER, "QUANTIFIER"), root_1);
		adaptor.addChild(root_1, (CommonTree)adaptor.create(PCREParser.NUMBER, "0"));
		adaptor.addChild(root_1, (CommonTree)adaptor.create(PCREParser.NUMBER, "1"));
		return root_1;
	}

	private static boolean hasZeroOneDBB(WeightRankedRegex wrr)
			throws IllegalArgumentException, QuoteRuleException,
			PythonParsingException {
		WeightRankedRegex target = new WeightRankedRegex("'.{0,1}'", 1);
		CommonTree y = target.getRootTree();
		meow(getQuestionable());

		return false;
	}

	private static void meow(CommonTree treeRoot)
			throws AlienFeatureException {

		List<CommonTree> firstStack = new ArrayList<CommonTree>();
		firstStack.add(treeRoot);

		List<List<CommonTree>> childListStack = new ArrayList<List<CommonTree>>();
		childListStack.add(firstStack);

		while (!childListStack.isEmpty()) {

			List<CommonTree> childStack = childListStack.get(childListStack.size() - 1);

			if (childStack.isEmpty()) {
				childListStack.remove(childListStack.size() - 1);
			} else {
				CommonTree subTree = childStack.remove(0);
				//business here
				System.out.println(subTree.getText());
				
				if (subTree.getChildCount() > 0) {
					childListStack.add(new ArrayList<CommonTree>((List<CommonTree>) subTree.getChildren()));
				}
			}
		}
	}

}
