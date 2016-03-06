package readability_analysis;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProportionOutput {
	
	
	private String filename;
	private List<String> textContent;
	private int[] counts;
	double p_value;

	//this assumes lots of good behavior from R outputs!  
	//we will see how consistent it is...
	public ProportionOutput(List<String> content, String filename){
		this.filename = filename;
		this.textContent = content;
		String line4 = content.get(3);		
		Pattern p = Pattern.compile("c\\((\\d+), (\\d+)\\)");
		Matcher m = p.matcher(line4);
		if(m.find()){
			String s1 = m.group(1);
			String s2 = m.group(2);
			counts = new int[2];
			counts[0] = Integer.parseInt(s1);
			counts[1] = Integer.parseInt(s2);
			String line5 = content.get(4);
			line5 = line5.replaceAll(" ", "");
			String[] variables = line5.split(",");
			this.p_value = Double.parseDouble(variables[2].split("=")[1]);
		}else{
			m = p.matcher(content.get(4));
			m.find();
			String s1 = m.group(1);
			String s2 = m.group(2);
			counts = new int[2];
			counts[0] = Integer.parseInt(s1);
			counts[1] = Integer.parseInt(s2);
			String line5 = content.get(5);
			line5 = line5.replaceAll(" ", "");
			String[] variables = line5.split(",");
			this.p_value = Double.parseDouble(variables[2].split("=")[1]);
		}

		

	}

	public String getFilename() {
		return filename;
	}

	public int[] getCounts() {
		return counts;
	}

	public double getP_value() {
		return p_value;
	}

	public String getCountText() {
		return counts[0] +"\t"+counts[1] + "\t";
	}

	@Override
	public String toString() {
		return "ProportionOutput [filename=" + filename + ", textContent=" +
			textContent + ", counts=" + Arrays.toString(counts) + ", p_value=" +
			p_value + "]";
	}
}
