package eddie.wu.search.smallboard;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.ThreeThreeBoardSearch;

public class TestAllState3 extends TestCase {
	private static Logger log = Logger.getLogger(TestAllState3.class);
	static {
		Constant.INTERNAL_CHECK = false;
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j_error.xml";
		System.setProperty(key, value);
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		Logger.getLogger(TestAllState3.class).setLevel(Level.WARN);
	}

	/** 198 seconds TODO 2*/
	public void testState_V0() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");

		testState_internal(text, Constant.BLACK, 4);

	}

	public void testState_V1() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, _, B]");
		testState_internal(text, Constant.BLACK, 3);
	}

	/**
	 * TODO 2
	 */
	public void testState_V21() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, B]");
		testState_internal(text, Constant.BLACK, 4);
	}

	/**
	 * findings: 1. without filtering symmetric moves in the later phase, too
	 * many variants are wasted.<br/>
	 * 2. for 3*3 board, there is almost impossible to have loop in best-best
	 * competition. shall we always filtering symmetric moves. <br/>
	 * 3. TODO 2<br/>
	 * 4. How to break the loop<br/>
	 * 5. <br/>
	 */
	public void testState_V3() {
		String[] text = new String[3];
		text[0] = new String("[W, _, _]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, B, B]");
		testState_internal(text, Constant.WHITE, -1);
		// testState_internal(text, Constant.BLACK, -1);

	}
	
	public void testState_V31() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[_, B, B]");
		testState_internal(text, Constant.WHITE, -1);
		// testState_internal(text, Constant.BLACK, -1);

	}

	public void testState_V4() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[B, B, B]");

		// testState_internal(text, Constant.BLACK, -4);
		testState_internal(text, Constant.WHITE, -4);

	}

	public void testState_V41() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[_, B, B]");

		// testState_internal(text, Constant.BLACK, -4);
		testState_internal(text, Constant.WHITE, -1);

	}

	/**
	 * TODO 2
	 */
	public void testState_V6() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[B, B, B]");

		testState_internal(text, Constant.BLACK, -4);
		testState_internal(text, Constant.WHITE, -4);

	}

	public void testState_V5() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[_, B, B]");

		testState_internal(text, Constant.BLACK, -1);
		testState_internal(text, Constant.WHITE, -1);

	}

	/**
	 * 3. TODO 2
	 */
	public void testState_V2() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, B, B]");
		testState_internal(text, Constant.WHITE, -1);
	}

	public void testState_4() {
		String[] text = new String[3];
		text[0] = new String("[_, _, B]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");

		testState_internal(text, Constant.WHITE, -9);

	}

	/**
	 * <br/>
	 * - Score=-3 <br/>
	 * - [INIT]W-->[PAS]B-->[PAS](DB_PASS -3) <br/>
	 * - [INIT]W-->[PAS]B-->[1,1]W-->[3,1](FINAL -9) <br/>
	 * - [INIT]W-->[PAS](-3 EXHAUST)
	 */
	public void testState_42() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");

		testState_internal(text, Constant.WHITE, -3);

	}

	public void testState_5() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		testState_internal(text, Constant.BLACK, 9);
	}

	public int testState_int(ThreeThreeBoardSearch goS) {
		int score = goS.globalSearch();

		if (log.isEnabledFor(Level.WARN)) {
			log.warn(goS.getGoBoard().getInitColorState().getStateString());
			log.warn("Score=" + score);
			log.warn("searched "
					+ (goS.getSearchProcess().size() - goS.dupCount));
			goS.outputSearchStatistics(log);
			TreeGoManual manual = goS.getTreeGoManual();
//			System.out.println(manual.getInitState());
			log.debug(manual.getExpandedString(false));
			log.warn(Constant.lineSeparator + manual.getMostExpPath());
			log.warn(manual.getSGFBodyString(false));
			for (String list : goS.getSearchProcess()) {
				log.warn(list);
			}
		}
		return score;

	}

	public void testState_internal(String[] text, int whoseTurn,
			int expectedScore) {
		boolean exetrem = false;
		byte[][] state = StateLoader.LoadStateFromText(text);
		int high, low;
		int boardSize = text.length;
		if (expectedScore + boardSize * boardSize == 0) {
			low = expectedScore;
			high = low + 1;
			exetrem = true;
		} else if (expectedScore == boardSize * boardSize) {
			high = expectedScore;
			low = high - 1;
			exetrem = true;
		} else {
			high = expectedScore;
			low = high - 1;
		}

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state, whoseTurn,
				high, low);
		int score = this.testState_int(goS);

		if (exetrem) {
			Assert.assertTrue(score == expectedScore);
			return;
		} else {
			Assert.assertTrue(score != Constant.UNKOWN);
			Assert.assertTrue(score >= expectedScore);
		}

		low = expectedScore;
		high = low + 1;

		goS = new ThreeThreeBoardSearch(state, whoseTurn, high, low);

		score = this.testState_int(goS);

		Assert.assertTrue(score != Constant.UNKOWN);
		Assert.assertTrue(score <= expectedScore);

	}

	/**
	 * TODO 2
	 */
	public void testState_1() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		testState_internal(text, Constant.BLACK, 4);

	}

	public void testState_12() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		testState_internal(text, Constant.BLACK, -3);
		testState_internal(text, Constant.WHITE, -3);
	}

	public void testState_13() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, _, W]");
		text[2] = new String("[_, W, _]");
		testState_internal(text, Constant.BLACK, -3);
		testState_internal(text, Constant.WHITE, -9);
	}

	/**
	 * [B, B, B]<br/>
	 * [_, _, _]
	 */
	public void testState_11() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.BLACK, 9);
	}

	public void testState_2() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, W, _]");
		testState_internal(text, Constant.BLACK, 9);
	}

	public void testState_3110() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_deadCleanedUp();
		Assert.assertFalse(finalState);

		testState_internal(text, Constant.BLACK, 9);

	}

	public void testState_3111() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[W, B, _]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SurviveAnalysis sa = new SurviveAnalysis(state);
		Assert.assertFalse(sa.isAlreadyLive_dynamic(Point.getPoint(3, 2, 1)));

	}

	public void testState_311V() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(3, 2, 2);
		Assert.assertFalse(ta.isAlreadyLive_dynamic(point));

		testState_internal(text, Constant.WHITE, 9);
	}

	/**
	 * 虽然是结果确定的状态（先后手结果相同）<br/>
	 * 但是只看一步没有办法判断，先手黑两眼活<br/>
	 * 计算过程中有重复的局面<br/>
	 * 简化终局的判断周后反而速度快了，避免了没有意义的isLive调用<br/>
	 * 
	 */
	public void testState_314() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_deadCleanedUp();
		Assert.assertFalse(finalState);

		testState_internal(text, Constant.WHITE, 9);
	}

	public void testState_3141() {
		String[] text = new String[3];
		text[0] = new String("[_, B, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(3, 1, 2);
		Assert.assertTrue(ta.isAlreadyLive_dynamic(point));

		boolean finalState = ta.isFinalState_deadCleanedUp();
		Assert.assertFalse(finalState);

		testState_internal(text, Constant.WHITE, 9);
	}

	public void testState_3142() {
		String[] text = new String[3];
		text[0] = new String("[_, B, B]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(3, 1, 2);
		Assert.assertTrue(ta.isAlreadyLive_dynamic(point));

		boolean finalState = ta.isFinalState_deadCleanedUp();
		Assert.assertFalse(finalState);

		testState_internal(text, Constant.WHITE, 9);
	}

	public void testState_3143() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, B, _]");
		text[2] = new String("[_, W, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(3, 1, 2);
		// Assert.assertTrue(ta.isLive(point));

		boolean finalState = ta.isFinalState_deadCleanedUp();
		Assert.assertFalse(finalState);

		testState_internal(text, Constant.WHITE, 9);
	}

	public void testState_316() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_deadCleanedUp();
		Assert.assertFalse(finalState);

		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, 3);

	}

	/**
	 * TODO 2: 106s
	 */
	public void testState_317() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, B]");
		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, 4);
	}

	public void testState_3176() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, 9);
	}

	public void testState_3174() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, 3);
	}

	public void testState_3177() {
		String[] text = new String[3];
		text[0] = new String("[B, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, -9);
	}
	
	public void testState_3178() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, 3);
	}
	
	public void testState_3180() {
		String[] text = new String[3];		
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, B, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, 9);
	}
	
	public void testState_3179() {
		String[] text = new String[3];
		text[0] = new String("[B, B, B]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, -9);
	}

	/**
	 * 99s: Score=4 <br/>
	 * we calculate steps = 58420
	 * TODO 2
	 */
	public void testState_3175() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");
		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, 4);
	}

	public void testState_3171() {
		String[] text = new String[3];
		text[0] = new String("[W, _, _]");
		text[1] = new String("[_, W, _]");
		text[2] = new String("[_, B, _]");
		testState_internal(text, Constant.WHITE, -9);
	}

	public void testState_3172() {
		String[] text = new String[3];
		text[0] = new String("[_, _, W]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, B, B]");
		testState_internal(text, Constant.BLACK, 3);
	}

	public void testState_3173() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		testState_internal(text, Constant.WHITE, -3);
	}

	/**
	 * TODO 2
	 */
	public void testState_318() {
		Constant.DEBUG_CGCL = false;
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		testState_internal(text, Constant.WHITE, 4);
	}
	/**
	 * 3. TODO 2
	 */
	public void testState_3181() {
		Constant.DEBUG_CGCL = false;
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		testState_internal(text, Constant.WHITE, 4);
	}

	/**
	 * TODO:8 seconds.
	 */
	public void testState_319() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_deadCleanedUp();
		Assert.assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new ThreeThreeBoardSearch(state, Constant.BLACK, -3, -4);
		score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);
		Assert.assertEquals(-3, score);
		for (String list : goS.getSearchProcess()) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());

	}

	/**
	 * we calculate steps = 2491 <br/>
	 * we know the result = 1575<br/>
	 * 8 seconds.--> 12 seconds
	 * TODO 2
	 */
	public void testState_3191() {
		// Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		// Constant.INTERNAL_CHECK=true;
		String[] text = new String[3];
		text[0] = new String("[B, B, B]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_deadCleanedUp();
		Assert.assertFalse(finalState);
		testState_internal(text, Constant.WHITE, -9);
	}

	public void testState_31911() {
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, W, B]");
		text[2] = new String("[_, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		Point point = Point.getPoint(3, 1, 2);
		boolean live = new SurviveAnalysis(state).isLive_Special(point,
				Constant.BLACK);
		Assert.assertFalse(live);

		point = Point.getPoint(3, 2, 1);
		live = new SurviveAnalysis(state).isAlreadyLive_dynamic(point);
		Assert.assertFalse(live);

		point = Point.getPoint(3, 2, 3);
		live = new SurviveAnalysis(state).isAlreadyLive_dynamic(point);
		Assert.assertFalse(live);

	}

}
