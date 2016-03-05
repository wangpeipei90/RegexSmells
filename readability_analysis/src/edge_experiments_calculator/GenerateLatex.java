package edge_experiments_calculator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import readability_analysis.AnswerColumn;
import readability_analysis.IOUtil;

public class GenerateLatex {
	
	private static String sourceFilename = "EXP_EDG_LST_TRANSPOSED.tsv";

	public static void main(String[] args) throws IOException {
		List<AnswerColumn> pairsFrom2 = IOUtil.getColumns(IOUtil.getLines(IOUtil.dataPath +
			IOUtil.ORIGINAL + "pairs_from_2.csv"), ",");
		List<AnswerColumn> pairsFrom3 = IOUtil.getColumns(IOUtil.getLines(IOUtil.dataPath +
			IOUtil.ORIGINAL + "pairs_from_3.csv"), ",");
		
		ArrayList<EdgeExperimentsList> edges = new ArrayList<EdgeExperimentsList>(20);
		List<String> sourceLines = IOUtil.getLines(IOUtil.dataPath+IOUtil.ORIGINAL+sourceFilename);
		for(String line : sourceLines){
			String[] parts = line.split("\t");
			String edgeIndex = parts[0];
			String edgeDescription = parts[1];
			int nPairs = (parts.length-2)/2;
			LinkedList<ExperimentPair> experiments = new LinkedList<ExperimentPair>();
			for(int i = 0;i<nPairs;i++){
				experiments.add(new ExperimentPair(parts[i+2],parts[i+2+nPairs],pairsFrom2,pairsFrom3));
			}
			edges.add(new EdgeExperimentsList(edgeIndex,edgeDescription,experiments));
		}
		System.out.println("nEdges: "+edges.size());
		for(EdgeExperimentsList eel : edges){
			System.out.println(eel);
		}
		
		
		

	}

}
