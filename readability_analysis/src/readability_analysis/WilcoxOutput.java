package readability_analysis;

import java.util.List;

public class WilcoxOutput {
	private final Double w_value;
	private final Double p_value;
	private final List<String> textContent;
	private final String filename;
	
	//this assumes lots of good behavior from R outputs!  
	//we will see how consistent it is...
	public WilcoxOutput(List<String> content, String filename){
		this.filename = filename;
		this.textContent = content;
		String line5 = content.get(4);
		line5 = line5.replaceAll(" ", "");
		String[] variables = line5.split(",");
		this.w_value = Double.parseDouble(variables[0].split("=")[1]);
		this.p_value = Double.parseDouble(variables[1].split("=")[1]);
	}

	public Double getWValue() {
		return w_value;
	}
	
	public Double getPValue() {
		return p_value;
	}
}
