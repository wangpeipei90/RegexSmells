package readability_analysis;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Step2_CreateOutputFiles {	
	private static DecimalFormat df3 = new DecimalFormat("0.00");

	private static File scriptTempFile = new File(IOUtil.dataPath + IOUtil.TMP +
		IOUtil.R_SCRIPTNAME);
	
	private static File p2_in_directory = new File(IOUtil.dataPath + IOUtil.IN +
			IOUtil.P2_PATH);
	private static File p3_in_directory = new File(IOUtil.dataPath + IOUtil.IN +
			IOUtil.P3_PATH);
	private static File t_in_directory = new File(IOUtil.dataPath + IOUtil.IN +
			IOUtil.T_PATH);
	private static File m_in_directory = new File(IOUtil.dataPath + IOUtil.IN +
			IOUtil.M_PATH);
	private static File g_in_directory = new File(IOUtil.dataPath + IOUtil.IN +
			IOUtil.G_PATH);

	public static void main(String[] args) throws IOException,
			InterruptedException {

		File p2_out_directory = new File(IOUtil.dataPath + IOUtil.OUT +
			IOUtil.P2_PATH);
		File p3_out_directory = new File(IOUtil.dataPath + IOUtil.OUT +
			IOUtil.P3_PATH);
		File t_out_directory = new File(IOUtil.dataPath + IOUtil.OUT +
			IOUtil.T_PATH);
		File m_out_directory = new File(IOUtil.dataPath + IOUtil.OUT +
			IOUtil.M_PATH);
		File g_out_directory = new File(IOUtil.dataPath + IOUtil.OUT +
				IOUtil.G_PATH);

		for (File p2File : p2_in_directory.listFiles()) {
			if (!p2File.isHidden()) {
				String newFilename = p2File.getName().replaceAll("tsv", "Rout");
				File outFile = new File(p2_out_directory, newFilename);
				writeROutput(wrapTestIO(p2File.getAbsolutePath(), getWilcoxTest(), outFile.getAbsolutePath(), "\\t"), scriptTempFile);
			}
		}

		for (File p3File : p3_in_directory.listFiles()) {
			if (!p3File.isHidden()) {
				String newFilename = p3File.getName().replaceAll("tsv", "Rout");
				File outFile = new File(p3_out_directory, newFilename);
				writeROutput(wrapTestIO(p3File.getAbsolutePath(), getWilcoxTest(), outFile.getAbsolutePath(), "\\t"), scriptTempFile);
			}
		}

		for (File tFile : t_in_directory.listFiles()) {
			if (!tFile.isHidden()) {
				String newFilename = tFile.getName().replaceAll("tsv", "Rout");
				File outFile = new File(t_out_directory, newFilename);
				writeROutput(wrapTestIO(tFile.getAbsolutePath(), getKruskalTest(), outFile.getAbsolutePath(), "\\t"), scriptTempFile);
			}
		}

		for (File mFile : m_in_directory.listFiles()) {
			if (!mFile.isHidden()) {
				String newFilename = mFile.getName().replaceAll("csv", "Rout");
				File outFile = new File(m_out_directory, newFilename);
				writeROutput(wrapTestIO(mFile.getAbsolutePath(), getANOVATest(), outFile.getAbsolutePath(), ","), scriptTempFile);
			}
		}
		
		for (File gFile : g_in_directory.listFiles()) {
			if (!gFile.isHidden()) {
				String newFilename = gFile.getName().replaceAll("csv", "Rout");
				File outFile = new File(g_out_directory, newFilename);
				writeROutput(wrapTestIO(gFile.getAbsolutePath(), getGroupANOVATest(), outFile.getAbsolutePath(), ","), scriptTempFile);
			}
		}

		HashMap<String, Pattern> codePatternMap = IOUtil.getCodeMap(IOUtil.getLines(IOUtil.dataPath +
			IOUtil.ORIGINAL + "RenamingRegexes.tsv"), "\t");
		HashMap<String, List<String>> codeAnswerMap = IOUtil.getAnswerMap(IOUtil.getLines(IOUtil.dataPath +
			IOUtil.ORIGINAL + "compositionAnswers.tsv"), "\t");
		List<AnswerColumn> compositionAnswers = getCompositionAnswerColumns(codePatternMap, codeAnswerMap);
		writeCompositionOutputFiles(compositionAnswers);

	}

	private static String wrapTestIO(String inPath, String testContent,
			String outPath, String sep) {
		StringBuilder sb = new StringBuilder();
		sb.append("tbl = read.table(\"" + inPath + "\",TRUE,\"" + sep + "\")\n");
		sb.append(testContent);
		sb.append("capture.output(results,file=\"" + outPath + "\")");
		return sb.toString();
	}

	private static String getKruskalTest() {
		return "results = kruskal.test(value ~ code, data=tbl)\n";
	}

	private static String getWilcoxTest() {
		return "results = wilcox.test(tbl[,1],tbl[,2])\n";
	}

	private static String getANOVATest() {
		return "test_output = aov(accuracy~regex*refactoring,data=tbl)\n"
			+ "results=summary(test_output)\n";
	}
	
	private static String getGroupANOVATest() {
		return "test_output = aov(accuracy~abstract*node,data=tbl)\n"
			+ "results=summary(test_output)\n";
	}

	private static void writeROutput(String scriptContent, File scriptTempFile)
			throws IOException, InterruptedException {
		// write over the temp R input script with given content
		IOUtil.createAndWrite(scriptTempFile, scriptContent);

		// run the R script
		Process p = Runtime.getRuntime().exec("/usr/local/bin/R CMD BATCH " +
			scriptTempFile.getAbsolutePath());

		// add + " /dev/null" to the end to silence output
		p.waitFor();
	}

	private static List<AnswerColumn> getCompositionAnswerColumns(
			HashMap<String, Pattern> codePatternMap,
			HashMap<String, List<String>> codeAnswerMap) {
		List<AnswerColumn> compositionAnswers = new ArrayList<AnswerColumn>(60);
		for (Entry<String, Pattern> entry : codePatternMap.entrySet()) {
			String code = entry.getKey();
			Pattern p = entry.getValue();
			List<String> answers = codeAnswerMap.get(code);
			if (answers == null) {
				System.err.println("null answer list for code: " + code);
			} else if (answers.size() != 30) {
				System.err.println("wrong number of answers(" + answers.size() +
					") for code: " + code);
			} else {
				Double[] answerValues = new Double[30];
				int index = 0;
				for (String answer : answers) {
					Matcher m = p.matcher(answer);
					answerValues[index++] = m.find() ? 1.0 : 0.0;
				}
				compositionAnswers.add(new AnswerColumn(code, answerValues));
			}
		}
		return compositionAnswers;
	}
	
	private static void writeCompositionOutputFiles(
			List<AnswerColumn> compositionAnswers) throws IOException, InterruptedException {
		Pattern metacode = Pattern.compile("M\\d+R\\dV\\d[^M.]+[^_M.]");
		Pattern highestMetacode = Pattern.compile("M\\d");
		File p2_out_composition_directory = new File(IOUtil.dataPath + IOUtil.OUT + IOUtil.COMPOSITION + 
				IOUtil.P2_PATH);
		File p3_out_composition_directory = new File(IOUtil.dataPath + IOUtil.OUT + IOUtil.COMPOSITION + 
				IOUtil.P3_PATH);
		File m_in_composition_directory = new File(IOUtil.dataPath + IOUtil.IN + IOUtil.COMPOSITION + 
				IOUtil.M_PATH);
		File m_out_composition_directory = new File(IOUtil.dataPath + IOUtil.OUT + IOUtil.COMPOSITION + 
				IOUtil.M_PATH);

		for (File p2File : p2_in_directory.listFiles()) {
			if (!p2File.isHidden()) {
				String newFilename = p2File.getName().replaceAll("tsv", "Rout");
				File outFile = new File(p2_out_composition_directory, newFilename);
				AnswerColumn[] pair = new AnswerColumn[2];
				Matcher codeMatcher = metacode.matcher(p2File.getName());
				int pairIndex = 0;
				while(codeMatcher.find()){
					for(AnswerColumn ac : compositionAnswers){
						if(codeMatcher.group().equals(ac.getRegexCode())){
							pair[pairIndex++] = ac;
						}
					}
				}
				writeROutput(getRCompositionContent(outFile.getAbsolutePath(), pair), scriptTempFile);
			}
		}

		for (File p3File : p3_in_directory.listFiles()) {
			if (!p3File.isHidden()) {
				String newFilename = p3File.getName().replaceAll("tsv", "Rout");
				File outFile = new File(p3_out_composition_directory, newFilename);
				AnswerColumn[] pair = new AnswerColumn[2];
				Matcher codeMatcher = metacode.matcher(p3File.getName());
				int pairIndex = 0;
				while(codeMatcher.find()){
					for(AnswerColumn ac : compositionAnswers){
						if(codeMatcher.group().equals(ac.getRegexCode())){
							pair[pairIndex++] = ac;
						}
					}
				}
				writeROutput(getRCompositionContent(outFile.getAbsolutePath(), pair), scriptTempFile);
			}
		}

		for (File mFile : m_in_directory.listFiles()) {
			if (!mFile.isHidden()) {
				File inFile = new File(m_in_composition_directory, mFile.getName());
				Matcher partMatcher = highestMetacode.matcher(mFile.getName());
				partMatcher.find();
				String prefixPart = partMatcher.group();
				AnswerColumn[] columns = new AnswerColumn[6];
				int colIndex = 0;
				for(AnswerColumn ac : compositionAnswers){
					if(ac.getRegexCode().startsWith(prefixPart)){
						columns[colIndex++] = ac;
					}
				}
				IOUtil.createAndWrite(inFile,getMetaCompositionRInContent(columns));
				
				String newFilename = mFile.getName().replaceAll("csv", "Rout");
				File outFile = new File(m_out_composition_directory, newFilename);
				writeROutput(wrapTestIO(inFile.getAbsolutePath(), getANOVATest(), outFile.getAbsolutePath(), ","), scriptTempFile);
			}
		}
	}

	private static String getMetaCompositionRInContent(AnswerColumn[] columns) {
		StringBuilder sb = new StringBuilder();
		Pattern finder = Pattern.compile("M\\d(R\\d)V\\d_(.*)");
		sb.append("regex,refactoring,accuracy\n");
		for(AnswerColumn ac : columns){
			Matcher m = finder.matcher(ac.getRegexCode());
			m.find();
			String regex = m.group(1);
			String refactoring = m.group(2);
			for(Double d : ac.getExistingValues()){
				sb.append(regex+","+refactoring+","+df3.format(d)+"\n");
			}
		}
		return sb.toString();
	}

	private static String getRCompositionContent(String outPath, AnswerColumn[] pair) {
		StringBuilder sb = new StringBuilder();
		sb.append("results = prop.test(x=c(" + pair[0].getX() + "," + pair[1].getX()+"),n=c("+pair[0].getCount()+","+pair[1].getCount()+"))\n");
		sb.append("capture.output(results,file=\"" + outPath + "\")");
		return sb.toString();
	}
}
