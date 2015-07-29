mpackage eddie.wu.search.two;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.small.TwoTwoBoardSearch;

public class VerifyTwoTwo extends TestCase {
	static {
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j_error.xml";
		System.setProperty(key, value);
//		Logger.getLogger(TwoTwoBoardSearch.class).setLevel(Level.WARN);
//		Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
//		Logger.getLogger(GoBoardForward.class).setLevel(Level.WARN);
	}

	public void testInitState() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_blackTurn);

		System.out.println("with expScore = 4");
		score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB, 4);
		assertEquals(1, score_blackTurn);
		
		System.out.println("with expScore = -4");
		score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB, -4);
		assertEquals(1, score_blackTurn);
	}
	
	public void testInitState_white_turn() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_whiteTurn);

		System.out.println("with expScore = 4");
		score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB, 4);
		assertEquals(-1, score_whiteTurn);
		
		System.out.println("with expScore = -4");
		score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB, -4);
		assertEquals(-1, score_whiteTurn);
	}
}
