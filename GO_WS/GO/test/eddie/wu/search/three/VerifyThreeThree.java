package eddie.wu.search.three;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.small.SmallBoardGlobalSearch;
import eddie.wu.search.small.ThreeThreeBoardSearch;

public class VerifyThreeThree extends TestCase {
	static {
//		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
//		String value = "log4j.xml";
//		System.setProperty(key, value);
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		// Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.ERROR);
		// Logger.getLogger(TestAllState3.class).setLevel(Level.WARN);
		// Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(SmallBoardGlobalSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.WARN);
		Logger.getLogger("search.state.add").setLevel(Level.DEBUG);
		Logger.getLogger("search.state.apply").setLevel(Level.INFO);
		Logger.getLogger("search.statistic").setLevel(Level.DEBUG);
		// Logger.getLogger("search.terminal.state.add").setLevel(Level.DEBUG);
	}

	public void testInitState() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = ThreeThreeBoardSearch.getAccurateScore(stateB);
		assertEquals(9, score_blackTurn);
	}

	/**
	 * total saved duplicate state: 178 <br/>
	 * - Black expect: 9, White expect:8 <br/>
	 * - we calculate steps = 1885 <br/>
	 * - forwardMoves = 1885 <br/>
	 * - backwardMoves = 1885 <br/>
	 * - we know the result = 64 <br/>
	 * - searched path = 1031 <br/>
	 * - Pure searched path = 725 <br/>
	 * after apply duplicate state's known score.
	 */
	public void testInitStateWith9() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state_ = StateLoader.LoadStateFromText(text);
		BoardColorState state = new BoardColorState(state_, Constant.BLACK);

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state, 9);
		int score_blackTurn = goS.globalSearch();
		state.setVariant(goS.getSearchProcess().size());
		goS.logStateReached();
		goS.outputSearchStatistics();
		assertEquals(9, score_blackTurn);
	}

	public void testSpecialState() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, _, _]");
		text[2] = new String("[_, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.WHITE);
		int score_blackTurn = ThreeThreeBoardSearch.getAccurateScore(stateB);
		assertEquals(3, score_blackTurn);

	}

	public void testSpecialState1() {
		String[] text = new String[3];
		text[0] = new String("[B, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.WHITE);
		int score_blackTurn = ThreeThreeBoardSearch.getAccurateScore(stateB);
		assertEquals(9, score_blackTurn);

	}

}
