package node_count;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import node_count.build_corpus.RegexProjectSet;
import node_count.metric.FeatureSetClass;

// refactoring tree node. It's one node from the diagram in the paper...
public class RTNode extends TreeSet<RegexProjectSet> {
	private static final long serialVersionUID = 6428904144599866954L;
	private final String name;
	private final FeatureSetClass requiredFeatures;
	private final Pattern filter;

	public RTNode(String name, FeatureSetClass requiredFeatures, Pattern filter) {
		super();
		this.filter = filter;
		this.name = name;
		this.requiredFeatures = requiredFeatures;
	}

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
				case C.S2:
				case C.S3:

				case C.L1:
				case C.L2:
				case C.L3:
				case C.L4:

				case C.C1:
				case C.C2:
				case C.C3:
				case C.C4:
				case C.C5:
				case C.C6:

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
	
	public String getName() {
		return name;
	}

	public FeatureSetClass getRequiredFeatures() {
		return requiredFeatures;
	}

	public Pattern getFilter() {
		return filter;
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

	public String getContent() {
		StringBuilder sb = new StringBuilder();
		for(RegexProjectSet rps : this){
			sb.append(rps + "\n");
		}
		return sb.toString();
	}
}
