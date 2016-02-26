package node_count;

import java.sql.SQLException;
import java.util.ArrayList;

import node_count.build_corpus.CorpusBuilder;
import node_count.build_corpus.WeightRankedRegex;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;

public class Main_Node_Counter {
	
	private static ArrayList<WeightRankedRegex> corpus;
	public static final String homePath = "/Users/carlchapman/Documents/SoftwareProjects/tour_de_source/";
	public static final String connectionString = "jdbc:sqlite:" + homePath +
		"tools/merged/merged_report.db";


	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, SQLException, QuoteRuleException, PythonParsingException {
		corpus = new ArrayList<WeightRankedRegex>(1024);
		CorpusBuilder.initializeCorpus(connectionString,corpus);
		System.out.println("corpusLength: "+corpus.size());

	}

}
