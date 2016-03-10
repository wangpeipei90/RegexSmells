package node_count;

import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import node_count.build_corpus.RegexProjectSet;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import node_count.metric.FeatureCountFactory;
import node_count.metric.FeatureDictionary;
import node_count.metric.FeatureSetClass;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeIterator;

// refactoring tree node. It's one node from the diagram in the paper...
public class RTNode extends TreeSet<RegexProjectSet> {
	private static boolean verbose = true;
	private static int attemptCounter = 0;
	private static final char delim = '•';
	private static final long serialVersionUID = 6428904144599866954L;
	private final String name;
	private final FeatureSetClass requiredFeatures;
	private final Pattern filter;
	private static final Pattern CCC_WITH_RANGE = Pattern.compile("CHARACTER_CLASS•DOWN•(((.|\\\\0x..)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W))•)*(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP•)(((.|\\\\0x..)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)|(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP))•)*UP•");
	private static final Pattern CCC_WITH_DEFAULT = Pattern.compile("CHARACTER_CLASS•DOWN•(((.|\\\\0x..)|(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP))•)*(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)•(((.|\\\\0x..)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)|(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP))•)*UP•");
	private static final Pattern CCC_WITH_DEFAULT_OR_RANGE = Pattern.compile("CHARACTER_CLASS•DOWN•(((\\\\0x..)|.)•)*((RANGE•DOWN•((\\\\0x..)|.)•((\\\\0x..)|.)•UP•)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)•)+(((\\\\0x..)|.|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)|(RANGE•DOWN•((\\\\0x..)|.)•((\\\\0x..)|.)•UP))•)*UP•");

	//private static final Pattern S2_repeatingChars = Pattern.compile("(ELEMENT•DOWN•.•UP•)\\1+");
	private static final Pattern S2_repeatingElement = Pattern.compile("(ELEMENT•[^ ]+)\\1");

	private static final Pattern OR_WITHOUT_PREFIX_OR_SUFFIX = Pattern.compile("^OR•DOWN•");
	private static final Pattern OR_IN_CG_FIRST_ELEMENT = Pattern.compile("^ALTERNATIVE•DOWN•ELEMENT•DOWN•CAPTURING_GROUP•DOWN•OR•DOWN•");

	private static final Pattern CCC_WRAPPED_CHAR = Pattern.compile("(?<=\\[).(?=\\])");

	private static final Pattern CCC_WRAPPED_ESCAPE_CHAR = Pattern.compile("(?<=\\[)([.^$*+?(){}\\\\|\\[])(?=\\])");
	private static final Pattern CCC_WRAPPED_NONESCAPE_CHAR = Pattern.compile("(?<=\\[)([^.^$*+?(){}\\\\|\\[])(?=\\])");
	private static final Pattern HEX_OR_OCTAL = Pattern.compile("(\\\\x[a-f0-9A-F]{2})|((\\\\0\\d*)|(\\\\\\d{3}))");
	private static final Pattern C5_ORofLITERALS_OR_DEFAULTS_OR_RANGES = Pattern.compile("OR•DOWN•(ALTERNATIVE•DOWN•ELEMENT•DOWN•(.|\\\\0x..|\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W|ANY|(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP•))•UP•UP•)+UP•");

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
			sb.append(rps.getContent() + "\t" + rps.getProjectsCSV() + "\n");
		}
		return sb.toString();
	}

	// /////////matching logic for all////////

	public boolean addIfMatches(RegexProjectSet regex,
			StringBuilder errorLogContent) throws InterruptedException,
			ExecutionException {
		if (verbose && attemptCounter++ % 500 == 0) {
			System.out.println("counter: " + attemptCounter);
		}

		if (regex.subsumes(requiredFeatures)) {
			Matcher m = filter.matcher(regex.unquoted);
			if (m.find()) {
				switch (name) {
				case C.D1:
				case C.D2:
				case C.D3:

				case C.S1:
					return this.add(regex);
				case C.S2:
					return matchesS2(regex) ? this.add(regex) : false;
				case C.S3:

				case C.L1:
				case C.L2:
				case C.L3:
					return this.add(regex);

				case C.C1:
					return matchesC1(regex) ? this.add(regex) : false;
				case C.C2:
					return matchesC2(regex) ? this.add(regex) : false;
				case C.C3:
					return this.add(regex);
				case C.C4:
					return matchesC4(regex) ? this.add(regex) : false;
				case C.C5:
					return matchesC5(regex) ? this.add(regex) : false;

				case C.T1:
					return matchesT1(regex) ? this.add(regex) : false;
				case C.T2:
					return this.add(regex);
				case C.T3:
					return matchesT3(regex) ? this.add(regex) : false;
				case C.T4:

				default:
					return this.add(regex);
				}
			}
		}
		return false;
	}

	private boolean matchesS2(RegexProjectSet regex) {
		String tokenStream = getTokenStream(regex);
//		Matcher m1 = S2_repeatingChars.matcher(tokenStream);
//		String charPairsRemoved = m1.replaceAll("$1");
		Matcher m = S2_repeatingElement.matcher(tokenStream);
		return m.find();
	}

	private static boolean matchesC1(RegexProjectSet regex) {
		String tokenStream = getTokenStream(regex);
		Matcher m1 = CCC_WITH_RANGE.matcher(tokenStream);
		return m1.find();
	}
	
	private static boolean matchesC2(RegexProjectSet regex){
		// C2 is any CCC that has no ranges or defaults,
		// and we know it has a CCC
		String tokenStream = getTokenStream(regex);

		// this matcher REQUIRES there to be a CCC containing at least one range
		// or default
		Matcher m1 = CCC_WITH_DEFAULT_OR_RANGE.matcher(tokenStream);

		// so if none can be found, than all CCCs present don't have these
		// features, as needed for C2
		return !m1.find();
	}
	
	private static boolean matchesC4(RegexProjectSet regex) {
		String tokenStream = getTokenStream(regex);
		Matcher m1 = CCC_WITH_DEFAULT.matcher(tokenStream);
		return m1.find();
	}
	
	private static boolean matchesC5(RegexProjectSet regex) {
		String tokenStream = getTokenStream(regex);
		Matcher m1 = C5_ORofLITERALS_OR_DEFAULTS_OR_RANGES.matcher(tokenStream);
		return (m1.find());
	}

	private static boolean matchesT1(RegexProjectSet regex) {
		Matcher m = CCC_WRAPPED_CHAR.matcher(regex.unquoted);
		Matcher m3 = HEX_OR_OCTAL.matcher(regex.unquoted);
		return !m.find() && !m3.find();
	}

	private static boolean matchesT3(RegexProjectSet regex) {
		Matcher m = CCC_WRAPPED_CHAR.matcher(regex.unquoted);
		return m.find();
	}

	// //////////helpers////////////

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

	public static void main(String[] args) throws IllegalArgumentException,
			QuoteRuleException, PythonParsingException {
		// testing some ideas
		TreeSet<Integer> testPIDs = new TreeSet<Integer>();
		testPIDs.add(1);
		RegexProjectSet testRegex = new RegexProjectSet("'([^[]+)\\[([^]]+)\\]'", testPIDs);
		System.out.println(matchesT3(testRegex));
	}
}
