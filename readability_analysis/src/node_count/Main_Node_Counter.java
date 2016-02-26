package node_count;

import java.sql.SQLException;
import java.util.ArrayList;

import node_count.build_corpus.WeightRankedRegex;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import node_count.metric.FeatureDictionary;

public class Main_Node_Counter {
	
	private static ArrayList<WeightRankedRegex> corpus;
	public static final String homePath = "/Users/carlchapman/Documents/SoftwareProjects/tour_de_source/";
	public static final String connectionString = "jdbc:sqlite:" + homePath +
		"tools/merged/merged_report.db";


	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, SQLException, QuoteRuleException, PythonParsingException {
		corpus = new ArrayList<WeightRankedRegex>(1024);
		
		//CorpusBuilder.initializeCorpus(connectionString,corpus);
		WeightRankedRegex wrr = new WeightRankedRegex("'.'",9);
		System.out.println(wrr.hasFeature(FeatureDictionary.I_META_LITERAL));
		System.out.println(wrr.toString());

		
		

	}

}
