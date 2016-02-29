package node_count;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import node_count.build_corpus.CorpusUtil;
import node_count.build_corpus.RegexProjectSet;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import readability_analysis.IOUtil;

public class Step2_CreateReport {
	
	private static File cleanRoot = new File(IOUtil.basePath + IOUtil.CLEAN_NODES);
	private static DecimalFormat df3 = new DecimalFormat("0.00");

	
	public static void main(String[] args) throws IOException, IllegalArgumentException, QuoteRuleException, PythonParsingException {
		String header = "name         \tdescription                           \tnPatterns\t%Patterns\tnProjects\t%Projects\texample\n";
		DescriptionDictionary desc = new DescriptionDictionary();
		int longestDesc = desc.getLongestLength();
		StringBuilder report = new StringBuilder();
		report.append(header);
		TreeSet<RegexProjectSet> corpus = CorpusUtil.reloadCorpus();
		double corpusSize = corpus.size() + 0.0;
		TreeSet<Integer> corpusProjectIDs = aggregateProjectIDs(corpus);
		double allProjectsSize = corpusProjectIDs.size() + 0.0;
		for (File cleanDir : cleanRoot.listFiles()) {
			if (!cleanDir.isHidden() && cleanDir.isDirectory()) {
				for (File nodeFile : cleanDir.listFiles()) {
					if (nodeFile.getName().endsWith(".tsv")) {
						TreeSet<RegexProjectSet> cleanRTNodeMembers = new TreeSet<RegexProjectSet>();
						List<String> lines = IOUtil.getLines(nodeFile.getAbsolutePath());
						RegexProjectSet first = null;
						for(String line : lines){
							String[] parts = line.split("\t");
							String[] IDs = parts[1].split(",");
							TreeSet<Integer> IDSet = new TreeSet<Integer>();
							for(String id : IDs){
								IDSet.add(Integer.parseInt(id));
							}
							RegexProjectSet current = new RegexProjectSet(parts[0],IDSet);
							cleanRTNodeMembers.add(current);
							if(first==null){
								first = current;
							}
						}
						TreeSet<Integer> fileMemberIDs = aggregateProjectIDs(cleanRTNodeMembers);
						String nProjects = "" + fileMemberIDs.size();
						String nPatterns = "" + cleanRTNodeMembers.size();
						double percentPatterns = cleanRTNodeMembers.size()/corpusSize;
						double percentProjects = fileMemberIDs.size()/allProjectsSize;
						String nodeName = nodeFile.getName().replaceAll(".tsv","");
						String firstPattern = first==null ? "NO_PATTERN_FOUND" : "'"+first.getUnescapedPattern()+"'";
						String shortDescription = desc.get(nodeName);
						report.append(StringUtils.rightPad(nodeName, 13) +
							"\t" +
							StringUtils.rightPad(shortDescription, longestDesc) +
							"\t" +
							StringUtils.rightPad(nPatterns, 9) + 
							"\t" + StringUtils.rightPad(df3.format(percentPatterns),9) + 
							"\t" +StringUtils.rightPad(nProjects,9) + 
							"\t" + StringUtils.rightPad(df3.format(percentProjects),9) + 
							"\t" + firstPattern+"\n");
					}
					
				}
				report.append("\n");
			}
		}
		File reportFile = new File(cleanRoot,"report.txt");
		IOUtil.createAndWrite(reportFile,report.toString());

	}

	private static TreeSet<Integer> aggregateProjectIDs(
			TreeSet<RegexProjectSet> regexSet) {
		TreeSet<Integer> allProjectIDs = new TreeSet<Integer>();
		for(RegexProjectSet member : regexSet){
			allProjectIDs.addAll(member.getProjectIDSet());
		}
		return allProjectIDs;
	}
}
