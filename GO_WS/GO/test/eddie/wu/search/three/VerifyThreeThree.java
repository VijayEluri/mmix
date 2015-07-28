package eddie.wu.search.three;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.small.ThreeThreeBoardSearch;
import junit.framework.TestCase;

public class VerifyThreeThree extends TestCase {
	static {
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j_error.xml";
		System.setProperty(key, value);
//		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
//		Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
//		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
//		Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.ERROR);
//		Logger.getLogger(TestAllState3.class).setLevel(Level.WARN);
//		 Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.WARN);
//		 Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		 //Logger.getLogger(GoBoardForward.class).setLevel(Level.WARN);
	}
	public void testInitState() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = ThreeThreeBoardSearch
				.getAccurateScore(stateB);
		assertEquals(9, score_blackTurn);

	}

}
