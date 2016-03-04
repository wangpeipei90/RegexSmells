package node_count;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.TreeSet;

import node_count.build_corpus.CorpusUtil;
import node_count.build_corpus.RegexProjectSet;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import readability_analysis.IOUtil;

public class Step2_CreateReport {
	
	private static File cleanRoot = new File(IOUtil.dataPath + IOUtil.CLEAN_NODES);
	private static File generatedDir = new File(IOUtil.paperPath + IOUtil.TABLE);
	private static DecimalFormat df3 = new DecimalFormat("0.00");

	
	public static void main(String[] args) throws IOException, IllegalArgumentException, QuoteRuleException, PythonParsingException {
		String between = " & \n";
		String tableLatex = "\\begin{table*}\n\\begin{center}\n"
			+ "\\caption{How frequently is each alternative expression style used?}\n"
			+ "\\label{table:nodeCount}\n"
			+ "\\begin{tabular}\n{lllcccc}\n";
		String topRow = "name & description & example & nPatterns & \\% patterns & nProjects & \\% projects \\\\ \n\\toprule[0.16em]\n";
		double width = 1.5;
		String widthS = df3.format(width);
		String beforePattern = "\\begin{minipage}{" + widthS +
				"in}\\begin{verbatim}\n";
			String afterPattern = "\\end{verbatim}\\end{minipage}\n";

		DescriptionDictionary desc = new DescriptionDictionary();
		//int longestDesc = desc.getLongestLength();
		StringBuilder report = new StringBuilder();
		report.append(tableLatex+topRow);
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
						int nProjects = fileMemberIDs.size();
						int nPatterns = cleanRTNodeMembers.size();
						double percentPatterns = cleanRTNodeMembers.size()/corpusSize;
						double percentProjects = fileMemberIDs.size()/allProjectsSize;
						String nodeName = nodeFile.getName().replaceAll(".tsv","").replaceAll("_"," ");
						String firstPattern = first==null ? "NO_PATTERN_FOUND" : "'"+first.getUnescapedPattern()+"'";
						String shortDescription = desc.get(nodeName);
						report.append(nodeName);
						report.append(between);
						report.append(shortDescription);
						report.append(between);
						report.append(beforePattern);
						report.append(firstPattern);
						report.append(afterPattern);
						report.append(between);
						report.append(Composer.commafy(nPatterns));
						report.append(between);
						report.append(df3.format(percentPatterns));
						report.append(between);
						report.append(Composer.commafy(nProjects));
						report.append(between);
						report.append(df3.format(percentProjects));
						report.append("\\\\\n");
					}
					
				}
				report.append("\n");
			}
		}
		String tableFoot = " \\\\ \n\\bottomrule[0.13em]\n\\end{tabular}\n\\end{center}\n\\end{table*}\n";
		report.append(tableFoot);
		File reportFile = new File(generatedDir,"nodeCountTable.tex");
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
//	if(rankIndex==8 || rankIndex==27){
//		sb.append(" \\\\ \n\\midrule[0.12em]\n");
//	}else if(rankIndex < sortedFeatures.size()){
//		sb.append(" \\\\ \n\\midrule\n");
//	}
//	rankIndex++;
//}
//sb.append(" \\\\ \n\\bottomrule[0.13em]\n\\end{tabular}\n"
//	+ "\\end{center}\n\\end{table*}\n");
//return sb.toString();
