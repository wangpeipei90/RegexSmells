package node_count;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import node_count.build_corpus.RegexProjectSet;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import node_count.metric.FeatureCount;
import node_count.metric.FeatureCountFactory;
import node_count.metric.FeatureDictionary;
import node_count.metric.FeatureSetClass;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeIterator;

// refactoring tree node. It's one node from the diagram in the paper...
public class RTNode extends TreeSet<RegexProjectSet> {
	private static final char delim = '•';
	private static final long serialVersionUID = 6428904144599866954L;
	private final String name;
	private final FeatureSetClass requiredFeatures;
	private final Pattern filter;
	private static final Pattern CCC_WITH_RANGE = Pattern.compile("CHARACTER_CLASS•DOWN•(((.|\\\\0x..)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W))•)*(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP•)(((.|\\\\0x..)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)|(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP))•)*UP•");
	private static final Pattern CCC_WITH_DEFAULT = Pattern.compile("CHARACTER_CLASS•DOWN•(((.|\\\\0x..)|(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP))•)*(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)•(((.|\\\\0x..)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)|(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP))•)*UP•");
	private static final Pattern CCC_WITH_DEFAULT_OR_RANGE = Pattern.compile("CHARACTER_CLASS•DOWN•(((\\\\0x..){1,2}|.)•)*((RANGE•DOWN•((\\\\0x..){1,2}|.)•((\\\\0x..){1,2}|.)•UP•)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)•)+(((\\\\0x..){1,2}|.|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)|(RANGE•DOWN•((\\\\0x..){1,2}|.)•((\\\\0x..){1,2}|.)•UP))•)*UP•");
	private static final Pattern C3_ORofLITERALS_NO_DEFAULTS = Pattern.compile("OR•DOWN•(ALTERNATIVE•DOWN•ELEMENT•DOWN•(.|\\\\0x..)•UP•UP•)+UP•");
	private static final Pattern C6_ORofLITERALS_REQUIRING_DEFAULT = Pattern.compile("OR•DOWN•(ALTERNATIVE•DOWN•ELEMENT•DOWN•(.|\\\\0x..)•UP•UP•)*(ALTERNATIVE•DOWN•ELEMENT•DOWN•(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W|ANY)•UP•UP•)+(ALTERNATIVE•DOWN•ELEMENT•DOWN•(.|\\\\0x..|\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W|ANY)•UP•UP•)*UP•");
	
	private static final Pattern S2_repeatingChars = Pattern.compile("(ELEMENT•DOWN•.•UP•)\\1+");
	private static final Pattern S2_repeatingElement = Pattern.compile("(ELEMENT•[^ ]+)\\1");
	private static final FeatureSetClass rangeFSC = new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_CC_RANGE));
	private static final FeatureSetClass defaultsFSC = new FeatureSetClass(FeatureCountFactory.constructWithOneOfEach(FeatureDictionary.I_CC_DECIMAL, FeatureDictionary.I_CC_NDECIMAL, FeatureDictionary.I_CC_NWHITESPACE, FeatureDictionary.I_CC_NWORD, FeatureDictionary.I_CC_WHITESPACE, FeatureDictionary.I_CC_WORD));

	public RTNode(String name, FeatureSetClass requiredFeatures, Pattern filter) {
		super();
		this.filter = filter;
		this.name = name;
		this.requiredFeatures = requiredFeatures;
	}
	
	public String getName() {
		return name;
	}

	public FeatureSetClass getRequiredFeatures() {
		return requiredFeatures;
	}

	public Pattern getFilter() {
		return filter;
	}
	
	public String getContent() {
		StringBuilder sb = new StringBuilder();
		for (RegexProjectSet rps : this) {
			sb.append(rps + "\n");
		}
		return sb.toString();
	}
	
	///////////matching logic for all////////

	public boolean addIfMatches(RegexProjectSet regex) {
		if (regex.subsumes(requiredFeatures)) {
			Matcher m = filter.matcher(regex.unquoted);
			if (m.find()) {
				switch (name) {
				case C.D1:
				case C.D2:
				case C.D3:
				case C.D4:

				case C.S1:
					return this.add(regex);
				case C.S2:
					return matchesS2(regex) ? this.add(regex) : false;
				case C.S3:

				case C.L1:
				case C.L2:
				case C.L3:
				case C.L4:

				case C.C1:
					return this.add(regex);
				case C.C2:
					return matchesC2(regex) ? this.add(regex) : false;
				case C.C3:
					return matchesC3(regex) ? this.add(regex) : false;
				case C.C4:
					return this.add(regex);
				case C.C5:
					return matchesC5(regex) ? this.add(regex) : false;
				case C.C6:
					return matchesC6(regex) ? this.add(regex) : false;

				case C.R1:
				case C.R2:
				case C.R3:

				case C.P1:
				case C.P2:
				case C.P3:
				case C.P4:
				case C.P5:
				case C.P6:

				case C.T1:
				case C.T2:
				case C.T3:
				case C.T4:
				case C.T5:
				case C.T6:
				case C.T7:

				case C.W1:
				case C.W2:
				case C.W3:
				case C.W4:

				case C.B1:
				case C.B2:
				default:
					return this.add(regex);
				}
			}
		}
		return false;
	}
	
	private boolean matchesS2(RegexProjectSet regex) {
		String tokenStream = getTokenStream(regex);
		if (true) {
			Matcher m1 = S2_repeatingChars.matcher(tokenStream);
			String charPairsRemoved = m1.replaceAll("$1");
			Matcher m = S2_repeatingElement.matcher(charPairsRemoved);
			return m.find();
		} else {
			Matcher m = S2_repeatingElement.matcher(tokenStream);
			return m.find();
		}

	}
	
	private static boolean matchesC2(RegexProjectSet regex) {
		//C2 is any CCC that has no ranges or defaults,
		//and we know it has a CCC
		String tokenStream = getTokenStream(regex);
		System.out.println(tokenStream);
		
		//this matcher REQUIRES there to be a CCC containing at least one range or default
		Matcher m1 = CCC_WITH_DEFAULT_OR_RANGE.matcher(tokenStream);
		
		//so if none can be found, than all CCCs present don't have these features, as needed for C2
		return !m1.find();
	}
	
	private static boolean matchesC3(RegexProjectSet regex) {
		String tokenStream = getTokenStream(regex);
		Matcher m1 = C3_ORofLITERALS_NO_DEFAULTS.matcher(tokenStream);
		return(m1.find());
	}
	
	private static boolean matchesC5(RegexProjectSet regex) {
		//C5 is any CCC that has defaults, but no ranges
		//and we know it has a CCC 
		String tokenStream = getTokenStream(regex);
		
		//this matcher REQUIRES there to be a CCC containing at least one range
		Matcher m1 = CCC_WITH_RANGE.matcher(tokenStream);
		//this matcher REQUIRES there to be a CCC containing at least one default
		Matcher m2 = CCC_WITH_DEFAULT.matcher(tokenStream);
		
		//so if no CCC with a range can be found and we can find a CCC with a default, it matches C5
		return !m1.find() && m2.find();
	}
	
	private static boolean matchesC6(RegexProjectSet regex) {
		String tokenStream = getTokenStream(regex);
		Matcher m1 = C6_ORofLITERALS_REQUIRING_DEFAULT.matcher(tokenStream);
		return(m1.find());
	}

	////////////helpers////////////
	
	// cite:
	// http://stackoverflow.com/questions/7487917/convert-byte-array-to-escaped-string
	private static String escape(byte[] data) {
		StringBuilder cbuf = new StringBuilder();
		for (byte b : data) {
			if (b >= 0x20 && b <= 0x7e) {
				cbuf.append((char) b);
			} else {
				cbuf.append(String.format("\\0x%02x", b & 0xFF));
			}
		}
		return cbuf.toString();
	}

	private static String getTokenStream(RegexProjectSet regex) {
		TreeIterator it = new TreeIterator(regex.getRootTree());
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			sb.append(escape(((CommonTree) it.next()).getText().getBytes()) +
				delim);
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RTNode other = (RTNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public static void main(String[] args) throws IllegalArgumentException, QuoteRuleException, PythonParsingException{
		//testing some ideas
		TreeSet<Integer> testPIDs = new TreeSet<Integer>();
		testPIDs.add(1);
		 RegexProjectSet testRegex = new RegexProjectSet("'[\\s\\dabcc]'",testPIDs);
		 System.out.println(matchesC5(testRegex));
	}
}
