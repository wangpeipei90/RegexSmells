package node_count;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import node_count.build_corpus.CorpusBuilder;
import node_count.build_corpus.WeightRankedRegex;
import node_count.exceptions.PythonParsingException;
import node_count.exceptions.QuoteRuleException;
import node_count.metric.FeatureSetClass;
import node_count.metric.NodeChecker;
import node_count.metric.Node_Candidate_Factory;

public class Main_Node_Counter {
	
	private static ArrayList<WeightRankedRegex> corpus;
	public static final String homePath = "/Users/carlchapman/Documents/SoftwareProjects/tour_de_source/";
	public static final String connectionString = "jdbc:sqlite:" + homePath +
		"tools/merged/merged_report.db";


	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, SQLException, QuoteRuleException, PythonParsingException {
		corpus = new ArrayList<WeightRankedRegex>(1024);
		//CorpusBuilder.initializeCorpus(connectionString,corpus);
		
		
		WeightRankedRegex wMeow = new WeightRankedRegex("'<p(?: [^>]*){0,1}>(.*?)</p>'",8);
		System.out.println(NodeChecker.containsNode(wMeow, "D1"));
		
		
		
		
//		FeatureSetClass DBB = Node_Candidate_Factory.getCandidate("D1");
//		
//		LinkedList<WeightRankedRegex> D1_Candidates = new LinkedList<WeightRankedRegex>();
//				
//
//		for(WeightRankedRegex wrr : corpus){
//			if(wrr.subsumes(DBB) && wrr.){
//				D1_Candidates.add(wrr);
//			}
//		}
//		
//		for(WeightRankedRegex wrr : D1_Candidates){
//			System.out.println(wrr.toString());
//		}

	}

}
