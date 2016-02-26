package node_count.build_corpus;

import java.util.TreeSet;

import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import node_count.metric.FeatureCount;
import node_count.metric.FeatureSetClass;
import node_count.pcre.PCRE;

import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.lang3.StringUtils;
import org.python.util.PythonInterpreter;

public final class RegexProjectSet implements RankableContent {
	private final String pattern;
	private final CommonTree rootTree;

	private final FeatureCount features;
	private final TreeSet<Integer> projectIDSet;
	public final String unquoted;
	private final String unescaped;
	private final String projectsCSV;

	public RegexProjectSet(String pattern, TreeSet<Integer> projectIDSet) throws QuoteRuleException, IllegalArgumentException, PythonParsingException {
		if (pattern == null) {
			throw new IllegalArgumentException("pattern cannot be null: "+pattern);
		} 
		this.unquoted = getUnquotedPythonPattern(pattern);
		this.unescaped = getUnescaped(unquoted);
		
		if ("".equals(unescaped)) {
			throw new IllegalArgumentException("pattern cannot be empty: "+pattern);
		} else {
			this.pattern = pattern;
			if (projectIDSet==null || projectIDSet.isEmpty()) {
				throw new IllegalArgumentException("projectIDSet cannot be null or empty for pattern: "+pattern);
			}
			this.projectIDSet = projectIDSet;
			StringBuilder sb = new StringBuilder();
			for(Integer pID : projectIDSet){
				sb.append(pID);
				sb.append(",");
			}
			String allWithExtraComma = sb.toString();
			this.projectsCSV = allWithExtraComma.substring(0,allWithExtraComma.length()-1);

			try{
				
				// make sure the pattern is a valid regex
				PythonInterpreter interpreter = new PythonInterpreter();
				interpreter.exec("import re");
				interpreter.exec("x = re.compile(" + pattern + ")");
			}catch(Exception e){
				throw new PythonParsingException("Failure when trying to compile pattern in Python: "+pattern);
			}


			// parse into the commontree
			this.rootTree = new PCRE(getUnescapedPattern()).getCommonTree();
			this.features = new FeatureCount(rootTree,pattern);
		}
	}

	public boolean subsumes(FeatureSetClass candidate){
		return new FeatureSetClass(features).subsumes(candidate);
	}
	
	public static String getUnescaped(String unquotedPattern){
		return unquotedPattern.replaceAll("\\\\\\\\", "\\\\");
	}
	
	public boolean hasFeature(int featureIndex){
		return features.getFeatureCountArray()[featureIndex]!=0;
	}

	public CommonTree getRootTree() {
		return rootTree;
	}

	public FeatureCount getFeatures() {
		return features;
	}

	public int getRankableValue() {
		return projectIDSet.size();
	}

	@Override
	public int compareTo(RankableContent other) {
		if (other.getClass() != this.getClass()) {
			System.err.println("class mismatch");
			return 1;
		}
		RegexProjectSet wrrOther = (RegexProjectSet) other;
		// higher weight is earlier
		if (this.getRankableValue() > wrrOther.getRankableValue()) {
			return -1;
		} else if (this.getRankableValue() < wrrOther.getRankableValue()) {
			return 1;
		} else {
			// shorter length is earlier
			if (this.pattern.length() > wrrOther.pattern.length()) {
				return 1;
			} else if (this.pattern.length() < wrrOther.pattern.length()) {
				return -1;
			} else {

				if(this.pattern.equals(wrrOther.pattern)){
					return 0;
				}
				else if(this.hashCode() > other.hashCode()){
					return -1;
				}else if(this.hashCode() < other.hashCode()){
					return 1;
				}else{
					return -1;
				}
			}
		}
	}

	@Override
	public String toString() {
		return unescaped + "\t" + projectsCSV;
	}

	public String dump(int index, int paddedSize) {
		return StringUtils.leftPad(String.valueOf(index), paddedSize, "0")  + " | "+StringUtils.leftPad(String.valueOf(getRankableValue()), paddedSize, "0")+" | "+pattern +"\n";
	}

	@Override
	public String getContent() {
		return pattern;
	}
	
	public String getUnescapedPattern(){
		return this.unescaped;
	}
	
	public static String getEscapedPythonPattern(String unescapedPattern){
		String escaped= unescapedPattern.replaceAll("\\\\", "\\\\\\\\");
//		escaped = escaped.replaceAll("\\\\\\\\n","\\\\n");
//		escaped = escaped.replaceAll("\\\\\\\\t","\\\\t");
//		escaped = escaped.replaceAll("\\\\\\\\r","\\\\r");
//		escaped = escaped.replaceAll("\\\\\\\\b","\\\\b");
//		escaped = escaped.replaceAll("\\\\\\\\f","\\\\f");
		//escaped.replaceAll("\\\\\\\\.","\\\\.");
		return "'" + escaped + "'";
	}

	public static String getUnquotedPythonPattern(String pat) throws QuoteRuleException {

		// python can do: u'...', ur'...', r'...'
		String removedUR = pat.startsWith("ur") ? pat.substring(2)
				: pat;
		if(removedUR.length()==0){
			return "";
		}
		String removedPrefix = (removedUR.startsWith("u") || removedUR.startsWith("r")) ? removedUR.substring(1)
				: removedUR;
		if(removedPrefix.length()<2){
			return "";
		}
		char firstChar = removedPrefix.charAt(0);
		char lastChar = removedPrefix.charAt(removedPrefix.length() - 1);
		char singleQuote = '\'';
		char doubleQuote = '"';

		if (!(firstChar == singleQuote && lastChar == singleQuote) &&
			!(firstChar == doubleQuote && lastChar == doubleQuote)) {
			throw new QuoteRuleException("the pattern: " + pat +
				" does not conform to the expected quotation rules");
		}
		String unQuoted = null;
		String trippleSingleQuote = "'''";
		String trippleDoubleQuote = "\"\"\"";
		if (isTripple(removedPrefix, trippleSingleQuote) ||
			isTripple(removedPrefix, trippleDoubleQuote)) {
			unQuoted = removeQuotes(removedPrefix, 3);
		} else {
			unQuoted = removeQuotes(removedPrefix, 1);
		}
		if(unQuoted.length()==0){
			return "";
		}
		return unQuoted;
	}

	private static String removeQuotes(String s, int i) {
		return s.substring(i, s.length() - i);
	}

	private static boolean isTripple(String s, String tripple) {
		if (s.length() < 6) {
			return false;
		}
		int threeFromEnd = s.length() - 3;
		return s.startsWith(tripple) &&
			s.substring(threeFromEnd).equals(tripple);
	}
	
	public static void main(String[] args) throws QuoteRuleException{
		String r = "'^(boot(\\\\.\\\\d+)?$|kernel\\\\.)'";
		System.out.println(r);
		System.out.println(getUnquotedPythonPattern(r));
	}
}
