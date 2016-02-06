package readability_analysis;

import java.util.List;
import java.util.TreeSet;

public class KruskalOutput {
	private final Double chi_value;
	private final Double df_value;
	private final Double p_value;
	private final List<String> textContent;
	private final String filename;
	private final List<AnswerColumn> dataColumns;
	private final double alpha;

	public KruskalOutput(List<String> content, String filename,
			List<AnswerColumn> dataColumns, double alpha) {
		this.dataColumns = dataColumns;
		this.alpha = alpha;
		this.filename = filename;
		this.textContent = content;
		String line5 = content.get(4).substring("Kruskal-Wallis".length());
		line5 = line5.replaceAll(" ", "");
		String[] variables = line5.split(",");
		this.chi_value = Double.parseDouble(variables[0].split("=")[1]);
		this.df_value = Double.parseDouble(variables[1].split("=")[1]);
		this.p_value = Double.parseDouble(variables[2].split("=")[1]);
	}

	// also trim the trailing underscore, so that order becomes irrelevant
	public TreeSet<String> getNames() {
		String prefix = filename.substring(0, filename.indexOf('.'));
		String[] names = prefix.split("(?=M)");
		TreeSet<String> nameTree = new TreeSet<String>();
		for (int i = 0; i < names.length; i++) {
			if (names[i].endsWith("_")) {
				names[i] = names[i].substring(0, names[i].length() - 1);
			}
			nameTree.add(names[i]);
		}
		return nameTree;
	}

	public Double getDfValue() {
		return df_value;
	}

	public Double getChiValue() {
		return chi_value;
	}

	public Double getPValue() {
		return p_value;
	}

	public boolean isValid() {
		return p_value <= alpha;
	}
	
	public boolean hasBothNames(String name1, String name2){
		TreeSet<String> nameTree = getNames();
		return nameTree.contains(name1) && nameTree.contains(name2);
	}

}
