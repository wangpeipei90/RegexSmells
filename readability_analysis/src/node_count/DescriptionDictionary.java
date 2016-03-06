package node_count;

import java.util.HashMap;

public class DescriptionDictionary extends HashMap<String,String>{
	private static final long serialVersionUID = -4369023382292826732L;

	public DescriptionDictionary(){
		initializeDescriptions();
	}

	private void initializeDescriptions() {
		this.put("D1","curly brace repetition like \\{M,N\\} with M<N");
		this.put("D2","zero-or-one repetition using question mark");
		this.put("D3","repetition expressed using an OR");

		this.put("S1","curly brace repetition like \\{M\\}");
		this.put("S2","explicit sequential repetition");
		this.put("S3","curly brace repetition like \\{M,M\\}");

		this.put("L1","curly brace repetition like \\{M,\\}");
		this.put("L2","zero-or-more repetition using kleene star");
		this.put("L3","one-or-more repetition using plus");

		this.put("C1","char class using ranges");
		this.put("C2","char class explicitly listing all chars");
		this.put("C3","any negated char class");
		this.put("C4","char class using defaults");
		this.put("C5","an OR of length-one sub-patterns");

		this.put("T1","no HEX, OCT or char-class-wrapped literals");
		this.put("T2","has HEX literal like \\verb!\\xF5!");
		this.put("T3","has char-class-wrapped literals like [\\$]");
		this.put("T4","has OCT literal like \\verb!\\0177!");
		
	}

	public int getLongestLength() {
		int longestLength = 0;
		for(String value : this.values()){
			if(longestLength < value.length()){
				longestLength = value.length();
			}
		}
		return longestLength;
	}

}
