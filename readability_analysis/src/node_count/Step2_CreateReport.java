package node_count;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import analyze.Composer;
import analyze.FeatureDetail;
import metric.FeatureDictionary;
import node_count.build_corpus.CorpusUtil;
import node_count.build_corpus.RegexProjectSet;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import readability_analysis.IOUtil;

public class Step2_CreateReport {
	
	private static File cleanRoot = new File(IOUtil.basePath + IOUtil.CLEAN_NODES);
	private static DecimalFormat df3 = new DecimalFormat("0.00");

	
	public static void main(String[] args) throws IOException, IllegalArgumentException, QuoteRuleException, PythonParsingException {
		String between = " & ";
		String header = "name         \tdescription              \tnPatterns\t%Patterns\tnProjects\t%Projects\texample\n";
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
//sb.append("rank & code & description & example & brics & hampi & Rex & RE2 & nPatterns & \\% patterns & nProjects & \\% projects \\\\ \n\\toprule[0.16em]\n");
//TreeSet<FeatureDetail> sortedFeatures = new TreeSet<FeatureDetail>();
//for (int i = 0; i < nFeatures; i++) {
//	if (i == FeatureDictionary.I_META_LITERAL || presentCounter[i] == 0) {
//		continue;
//	}
//	// int featureID, int nFiles, int nPresent, int nProjects, int max,
//	// int nTokens)
//	sortedFeatures.add(new FeatureDetail(i, filesWithFeature[i], presentCounter[i], nProjectsPerFeature[i], max[i], tokensCounter[i]));
//}
//int rankIndex = 1;
//for (FeatureDetail featureDetail : sortedFeatures) {
//	int ID = featureDetail.getID();
//	String featureCode = fd.getCode(ID);
//	String description = fd.getDescription(ID);
//	String verbatimBlock = fd.getVerbatim(ID);
//
//	String nPresent = Composer.commafy(presentCounter[ID]);
//	String percentPresent = Composer.percentify(presentCounter[ID], nPatterns);
//
//	String nTokens = Composer.commafy(tokensCounter[ID]);
//	String percentTokens = Composer.percentify(tokensCounter[ID], adjustedTokens);
//
//	String maxOccurances = Composer.commafy(max[ID]);
//
//	String weightInt = Composer.commafy(featureDetail.getRankableValue());
//	String weightPercent = df.format(100 * (featureDetail.getRankableValue() / totalWeight));
//
//	// System.out.println("filesWithFeature[ID]: "+filesWithFeature[ID]+" totalNFiles[0]: "+totalNFiles[0]+" nProjectsPerFeature[ID]: "+nProjectsPerFeature[ID]+" totalNProjects[0]: "+totalNProjects[0]);
//
////	String nFiles = Composer.commafy(filesWithFeature[ID]);
////	String percentFiles = Composer.percentify(filesWithFeature[ID], totalNFiles[0]);
//
//	String nProjects = Composer.commafy(nProjectsPerFeature[ID]);
//	String percentProjects = Composer.percentify(nProjectsPerFeature[ID], totalNProjects[0]);
//
//	sb.append("" + rankIndex);
//	sb.append(between);
//	sb.append(featureCode);
//	sb.append(between);
//	sb.append(description);
//	sb.append(between);
//	sb.append(verbatimBlock);
//	sb.append(between);
//	sb.append(projectFeatureInclusion(ID, 0));
//	sb.append(between);
//	sb.append(projectFeatureInclusion(ID, 1));
//	sb.append(between);
//	sb.append(projectFeatureInclusion(ID, 2));
//	sb.append(between);
//	sb.append(projectFeatureInclusion(ID, 3));
//	sb.append(between);
//
//	sb.append(nPresent);
//	sb.append(between);
//	sb.append(percentPresent);
//	sb.append(between);
//
//	// sb.append(nTokens);
//	// sb.append(between);
//	// sb.append(percentTokens);
//	// sb.append(between);
//
//	// sb.append(maxOccurances);
//	// sb.append(between);
//
//	// sb.append(weightInt);
//	// sb.append(between);
//	// sb.append(weightPercent);
//	// sb.append(between);
//
////	sb.append(nFiles);
////	sb.append(between);
////	sb.append(percentFiles);
////	sb.append(between);
//	sb.append(nProjects);
//	sb.append(between);
//	sb.append(percentProjects);
//
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
