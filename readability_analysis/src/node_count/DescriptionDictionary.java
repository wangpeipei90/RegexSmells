package node_count;

import java.util.HashMap;

public class DescriptionDictionary extends HashMap<String,String>{
	private static final long serialVersionUID = -4369023382292826732L;

	public DescriptionDictionary(){
		initializeDescriptions();
	}

	private void initializeDescriptions() {
		this.put("D1","DBB other than {0,1}");
		this.put("D2","DBB exactly {0,1}");
		this.put("D3","Contains QST");
		this.put("D4","OR expressing repetition");
		this.put("DBB_REMAINDER","Not found in any DBB Nodes");

		this.put("S1","Contains SNG");
		this.put("S2","Repeated non-literals");
		this.put("S3","DBB like {M,M}");
		this.put("SNG_REMAINDER","Not found in any SNG Nodes");

		this.put("L1","LWB like {M,}, M>0");
		this.put("L2","Contains KLE");
		this.put("L3","Contains ADD");
		this.put("LWB_REMAINDER","Not found in any LWB Nodes");

		this.put("C1","CCC and RNG");
		this.put("C2","CCC, no RNG or defaults");
		this.put("C3","OR of single chars");
		this.put("C4","Contains NCCC");
		this.put("C5","CCC and defaults, no RNG");
		this.put("C6","OR containing defaults");
		this.put("CCC_REMAINDER","Not found in any CCC Nodes");

		this.put("R1","OR with prefix or suffix");
		this.put("R2","top-level OR");
		this.put("OR_REMAINDER","Not found in any OR Nodes");

		this.put("T1","no HEX, OCT or CCC-wrapped");
		this.put("T2","Contains HEX literal");
		this.put("T3","Contains CCC-wrapped");
		this.put("T4","Contains OCT literal");
		this.put("LIT_REMAINDER","Not found in any LIT Nodes");
		
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
