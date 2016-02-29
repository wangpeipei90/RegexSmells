package node_count;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindPatternTask implements Callable<Boolean>{
	
	private Pattern pat;
	private String tokenStream;
	
	public FindPatternTask(Pattern pattern, String tokenStream){
		this.pat = pattern;
		this.tokenStream = tokenStream;
	}

	@Override
	public Boolean call() throws Exception {
		Matcher interruptableMatcher = pat.matcher(new InterruptibleCharSequence(tokenStream));
        return interruptableMatcher.find();
	}
	

}
