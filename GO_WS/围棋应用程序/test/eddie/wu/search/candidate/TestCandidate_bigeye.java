package eddie.wu.search.candidate;

import junit.framework.TestCase;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.manual.StateLoader;

public class TestCandidate_bigeye extends TestCase{
	
	public void test(){
		
			String[] text = new String[5];
			text[0] = new String("[_, _, B, _, _]");
			text[1] = new String("[W, W, B, _, _]");
			text[2] = new String("[_, W, B, _, _]");
			text[3] = new String("[W, W, B, B, B]");
			text[4] = new String("[_, W, W, W, _]");
			byte[][] state = StateLoader.LoadStateFromText(text);
			
			
		SurviveAnalysis  sa = new SurviveAnalysis(state);
		//sa.getCandidate();
	}

}
