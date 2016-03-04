package node_count;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import node_count.build_corpus.CorpusUtil;
import node_count.build_corpus.RegexProjectSet;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import node_count.metric.FeatureCountFactory;
import node_count.metric.FeatureSetClass;
import readability_analysis.IOUtil;

public class Step1_CreateCandidateFiles {

	private static ArrayList<RegexProjectSet> corpus;
	public static final String connectionString = "jdbc:sqlite:/Users/carlchapman/Documents/SoftwareProjects/tour_de_source/tools/merged/merged_report.db";

	public static void main(String[] args) throws ClassNotFoundException,
			IllegalArgumentException, SQLException, QuoteRuleException,
			PythonParsingException,IOException, InterruptedException, ExecutionException {
		TreeSet<RegexProjectSet> corpus = CorpusUtil.reloadCorpus();

		// new dictionary contains all node groups initialized with empty nodes
		GroupDictionary gd = new GroupDictionary();

		for (String groupName : C.groupNames) {
			StringBuilder errorLogContent = new StringBuilder();
			NodeGroup group = gd.get(groupName);
			for (RTNode node : group) {
				System.out.println("in group: "+node.getName());
				for (RegexProjectSet rps : corpus) {
					node.addIfMatches(rps,errorLogContent);
				}
			}
			File preprocessLog = new File(IOUtil.dataPath + IOUtil.NODES,"preprocessErrorLog.txt");
			if(errorLogContent.length()>0){
				IOUtil.createAndWrite(preprocessLog,errorLogContent.toString());
				errorLogContent = new StringBuilder();
			}
			TreeSet<RegexProjectSet> partialCover = new TreeSet<RegexProjectSet>();
			boolean first = true;
			for (RTNode node : group) {
				errorLogContent.append("\ncurrent node: " + node.getName()+"\n\n");
				if (first) {
					first = false;
					partialCover.addAll(node);
				} else {
					Iterator<RegexProjectSet> it = node.iterator();
					while (it.hasNext()) {
						RegexProjectSet current = it.next();
						if (!partialCover.add(current)) {
							for (RTNode testNode : group) {
								if (testNode.contains(current)) {
									errorLogContent.append("partial cover already contains: " +
										current +
										" in node: " +
										testNode.getName()+"\n");
								}
							}
						}
					}
				}
			}
			RTNode remainder = new RTNode(groupName+"_REMAINDER", new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach()),Pattern.compile(".*"));
			remainder.addAll(corpus);
			remainder.removeAll(partialCover);
			group.add(remainder);
			for (RTNode node : group) {
				String nodeName = node.getName();
				File nodeOutFile = new File(IOUtil.dataPath + IOUtil.NODES +
					groupName + "/", nodeName+".tsv");
				IOUtil.createAndWrite(nodeOutFile, node.getContent());
			}
			File errorLog = new File(IOUtil.dataPath + IOUtil.NODES +
					groupName + "/","errorLog.txt");
			IOUtil.createAndWrite(errorLog,errorLogContent.toString());
		}

	}

}

// RegexProjectSet wrr = new RegexProjectSet("'.'",new TreeSet<Integer>());
// System.out.println(wrr.hasFeature(FeatureDictionary.I_META_LITERAL));
// System.out.println(wrr.toString());
