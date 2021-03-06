package eddie.wu.search.three;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.StateLoader;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.ListAllState;
import eddie.wu.search.small.ThreeThreeBoardSearch;
import eddie.wu.util.FileUtil;

public class TestAllState3 extends TestCase {
	private static Logger log = Logger.getLogger(TestAllState3.class);
	private static String manualFolder = Constant.DYNAMIC_DATA
			+ "small_board/three_three/";
	

	public void testState_init() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");

		testState_internal(text, Constant.BLACK, +9);

	}
	
	public void testState_init3() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, B, W]");

		testState_internal(text, Constant.BLACK, +9);

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

	public void testState_1314() {
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[W, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.BLACK, -3);
	}

	public void testState_2() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, W, _]");
		testState_internal(text, Constant.BLACK, 9);
	}

	public void testState_3111() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[W, B, _]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		// SurviveAnalysis sa = new SurviveAnalysis(state);
		// assertFalse(sa.isAlreadyLive_dynamic(Point.getPoint(3, 2, 1)));

	}

	public void testState_311V() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(3, 2, 2);
		assertFalse(ta.isAlreadyLive_dynamic(point));

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
		assertFalse(finalState);

		testState_internal(text, Constant.WHITE, 9);

		// finalState = new TerritoryAnalysis(state).isFinalState_deadExist();
		// assertTrue(finalState);
	}

	public void testState_3141() {
		String[] text = new String[3];
		text[0] = new String("[_, B, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(3, 1, 2);
		assertTrue(ta.isAlreadyLive_dynamic(point));

		boolean finalState = ta.isFinalState_deadCleanedUp();
		assertFalse(finalState);

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
		assertTrue(ta.isAlreadyLive_dynamic(point));

		boolean finalState = ta.isFinalState_deadCleanedUp();
		assertFalse(finalState);

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
		// assertTrue(ta.isLive(point));

		boolean finalState = ta.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		testState_internal(text, Constant.WHITE, 9);
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

	public void testState_3174() {
		String[] text = new String[3];
		text[0] = new String("[W, _, _]");
		text[1] = new String("[_, W, _]");
		text[2] = new String("[_, B, _]");
		testState_internal(text, Constant.BLACK, 1);
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
		assertFalse(live);

		point = Point.getPoint(3, 2, 1);
		live = new SurviveAnalysis(state).isAlreadyLive_dynamic(point);
		assertFalse(live);
		//
		// point = Point.getPoint(3, 2, 3);
		// live = new SurviveAnalysis(state).isAlreadyLive_dynamic(point);
		// assertFalse(live);

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

	/**
	 * TODO:8 seconds.
	 */
	public void testState_eye_10() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new ThreeThreeBoardSearch(state, Constant.BLACK, -3, -4);
		score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);
		assertEquals(-3, score);
		for (String list : goS.getSearchProcess()) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());

	}

	public void testState_eye_100() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.BLACK, -3);
	}

	public void testState_eye_101() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, 3);
	}

	public void testState_eye_11() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, 9);
	}

	/**
	 * root cause: WARN (TestAllState3.java:556) -
	 * [INIT]W-->[2,2]B-->[1,2]W-->[2,3]B-->[2,1]W-->[3,2](FINAL -1)<br/>
	 * [INIT]W-->[2,2]B-->[2,3]W-->[3,2]B-->[1,2]W-->[2,1](FINAL 0) <br/>
	 * WARN (TestAllState3.java:575) -
	 * [INIT]W-->[1,2]B-->[2,2]W-->[2,1]B-->[2,3]
	 * W-->[1,3]B-->[1,1]W-->[3,2]B-->[
	 * 3,1]W-->[1,2]B-->[1,3]W-->[2,1]B-->[PAS]W--
	 * >[1,2]B-->[PAS]W-->[3,3]B-->[2,3]W-->[2,2]B-->[PAS]W-->[PAS](DB_PASS -6)<BR/>
	 * this double pass should not be treated as Known result.<br/>
	 * WARN (TestAllState3.java:575) -
	 * [INIT]W-->[1,2]B-->[2,2]W-->[2,1]B-->[2,3]
	 * W-->[1,3]B-->[1,1]W-->[3,2]B-->[
	 * 3,1]W-->[1,2]B-->[1,3]W-->[2,1]B-->[1,2]W--
	 * >[3,3]B-->[2,3]W-->[2,2]B-->[1,2]W-->[1,3]B-->[PAS]W-->[PAS](DB_PASS -6)<br/>
	 * WARN (TestAllState3.java:575) -
	 * [INIT]W-->[1,2]B-->[2,2]W-->[2,1]B-->[2,3]
	 * W-->[1,3]B-->[1,1]W-->[3,2]B-->[
	 * 3,1]W-->[1,2]B-->[1,3]W-->[2,1]B-->[1,2]W--
	 * >[3,3]B-->[2,3]W-->[2,2]B-->[1,3]W-->[1,2](KNOWN -6) <br/>
	 * 01[B, B, _]01<br/>
	 * 02[B, W, W]02<br/>
	 * 03[_, W, _]03<br/>
	 * REAL root cause is that<br/>
	 * add non-final (but decided) state with score = 6<br/>
	 * WARN (ThreeThreeBoardSearch.java:370) -
	 * W[2,3]B[2,2]W[3,2]B[1,2]W[3,1]B[PAS]W[2,1]B[3,3]W[3,1]B[1,3]W[2,1]B[3,2]<br/>
	 * 01[B, B, B]01<br/>
	 * 02[_, B, _]02<br/>
	 * 03[_, B, B]03<br/>
	 * whoseTurn=White
	 */

	public void testState_eye_12() {
		String[] text = new String[3];
		text[0] = new String("[B, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, -9);
	}

	public void testState_eye_1210() {
		String[] text = new String[3];
		text[0] = new String("[B, W, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, -1);
	}

	public void testState_eye_1211() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[_, W, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.WHITE, -9);
	}

	public void testState_eye_1212() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, W, _]");
		testState_internal(text, Constant.WHITE, -9);
	}

	public void testState_eye_21() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, 3);

	}

	public void testState_eye_24() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, B, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, 9);
	}

	public void testState_eye_31() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		testState_internal(text, Constant.BLACK, 9);

	}

	/**
	 * [B, B, B]<br/>
	 * [_, _, _]
	 */
	public void testState_eye_32() {
		String[] text = new String[3];
		text[0] = new String("[B, B, B]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, -9);
	}

	public void testState_eye_33() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, 9);
	}

	public void testState_eye_34() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[B, _, _]");
		text[2] = new String("[_, _, _]");
		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, 3);
	}

	/**
	 * 99s: Score=4 <br/>
	 * we calculate steps = 58420 TODO 2
	 */
	public void testState_eye_42() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");
		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, 4);
	}

	/**
	 * TODO 2: 106s
	 */
	public void testState_eye_50() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, B]");
		testState_internal(text, Constant.BLACK, 9);
		testState_internal(text, Constant.WHITE, 4);
	}

	public void testState_Colinve_0() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, _, B]");
		text[2] = new String("[B, B, B]");
		testState_internal(text, Constant.BLACK, -4);
		testState_internal(text, Constant.WHITE, -4);
	}

	/**
	 * we calculate steps = 2491 <br/>
	 * we know the result = 1575<br/>
	 * 8 seconds.--> 12 seconds TODO 2
	 */
	public void testState_eye_62() {
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
		assertFalse(finalState);
		testState_internal(text, Constant.WHITE, -9);
	}

	public void testState_internal(String[] text, int whoseTurn,
			int expectedScore) {
		int boardSize = text.length;
		int deepth = boardSize * boardSize + 4;
		testState_internal(text, whoseTurn, expectedScore, deepth);
	}

	public void testState_internal(String[] text, int whoseTurn,
			int expectedScore, int depth) {
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState boardState = new BoardColorState(state, whoseTurn);
		testState_internal(boardState, expectedScore, depth, false);
	}

	public void testState_internal(BoardColorState boardState,
			int expectedScore, int depth) {
		testState_internal(boardState, expectedScore, depth, false);
	}

	public void testState_internal(BoardColorState boardState,
			int expectedScore, int depth, boolean tillBothPass) {
		boolean exetrem = false;
		// no need to check whether first player can reach the expected Score.
		boolean noCheck = false;

		int boardSize = boardState.boardSize;
		int maxScore = boardSize * boardSize;
		int whoseTurn = boardState.getWhoseTurn();
		if (expectedScore == -maxScore) {
			exetrem = true;
			if (whoseTurn == Constant.BLACK) {
				noCheck = true;
			}

		} else if (expectedScore == maxScore) {
			exetrem = true;
			if (whoseTurn == Constant.WHITE) {
				noCheck = true;
			}
		}
		//
		// byte[][] state = StateLoader.LoadStateFromText(text);
		// BoardColorState boardState = new BoardColorState(state, whoseTurn);

		// 1. check we can reach the expected score.
		ThreeThreeBoardSearch goS = null;

		int score = 0;
		if (noCheck != true) {
			goS = new ThreeThreeBoardSearch(boardState, expectedScore);
			goS.setDepth(depth);
			goS.setTillBothPass(tillBothPass);
			int variant = 1;
			for (int i = 0; i < depth; i++) {
				variant *= 3;
			}
			goS.setVariant(variant);
			score = this.testSearch_internal(goS);
			assertEquals(expectedScore, score);
			log.warn("end of positive check");
		}

		if (exetrem) {
			return;
		}
		// check that we cannot get better result
		int originalExp = expectedScore;
		if (whoseTurn == Constant.BLACK) {
			expectedScore++;
		} else {
			expectedScore--;
		}
		goS = new ThreeThreeBoardSearch(boardState, expectedScore);
		goS.setDepth(depth);
		goS.setTillBothPass(tillBothPass);
		score = this.testSearch_internal(goS);
		assertTrue(score != Constant.UNKNOWN);
		assertEquals(originalExp, score);

	}

	public int testSearch_internal(ThreeThreeBoardSearch goS) {
		int score = 0;
		try {
			score = goS.globalSearch();
		} catch (Exception e) {
			throw e;
		}
		String name = goS.getGoBoard().getInitColorState()
				.getStateAsOneLineString()
				+ goS.getExpScore().toFileName();

		String fileName1 = manualFolder + name + "win.sgf";
		String fileName2 = manualFolder + name + "lose.sgf";

		String nameReverse = goS.getGoBoard().getInitColorState()
				.blackWhiteSwitch().getStateAsOneLineString();
		String fileName3 = manualFolder + nameReverse + "win.sgf";
		String fileName4 = manualFolder + nameReverse + "lose.sgf";
		String filePath_searchProcess = manualFolder + nameReverse
				+ "searchProcess.txt";
		String filePath_rawManual = manualFolder + nameReverse
				+ "rawManual.txt";
		StringBuilder sb = new StringBuilder();
		if (log.isEnabledFor(Level.WARN)) {
			log.warn(goS.getGoBoard().getInitColorState().getStateString());
			log.warn("Score=" + score);
			log.warn("searched " + goS.getSearchProcess().size());
			goS.outputSearchStatistics();
			TreeGoManual manual = goS.getTreeGoManual();
			log.warn("Most Expensive path: ");
			log.warn(Constant.lineSeparator + manual.getMostExpPath());
			for (String list : goS.getSearchProcess()) {
				sb.append(list);
			}
			FileUtil.stringToFile(sb.toString(), filePath_searchProcess);

			log.warn("duplicated state begin:");
			goS.getGoBoard().getStepHistory().printDupState();
			log.warn("duplicated state end:");
			goS.printKnownResult();
			goS.logStateReached();

			int initScore = manual.initScore();
			log.warn("Init score = " + initScore);
			log.warn(goS.getExpScore());
			log.warn("before clean up");
			if (manual.getVariant() < 200) {
				log.warn(manual.getSGFBodyString(false));
			} else {
				FileUtil.stringToFile(manual.getSGFBodyString(false),
						filePath_rawManual);
			}
			if (goS.initTurn == Constant.BLACK) {
				if (score == goS.getExpScore().getHighExp()) {// success
					log.warn("after clean up fail node:");
					// manual.cleanupBadMove_firstWin(goS.initTurn,
					// goS.getMaxExp());
					manual.cleanupBadMoveForWinner(true);
					log.warn(manual.getSGFBodyString(false));
					SGFGoManual.storeGoManual(fileName1, manual);
					manual.blackWhiteSwitch();
					SGFGoManual.storeGoManual(fileName3, manual);
				} else if (score < goS.getExpScore().getHighExp()) {// fail
					log.warn("after clean up fail node:");
					// manual.cleanupBadMove_firstLose(goS.initTurn,
					// goS.getMaxExp());
					manual.cleanupBadMoveForWinner(false);
					log.warn(manual.getSGFBodyString(false));
					SGFGoManual.storeGoManual(fileName2, manual);
					manual.blackWhiteSwitch();
					SGFGoManual.storeGoManual(fileName4, manual);
				}

			} else if (goS.initTurn == Constant.WHITE) {
				if (score == goS.getExpScore().getLowExp()) {
					log.warn("after clean up fail node:");
					// manual.cleanupBadMove_firstWin(goS.initTurn,
					// goS.getMinExp());
					manual.cleanupBadMoveForWinner(false);
					log.warn(manual.getSGFBodyString(false));
					SGFGoManual.storeGoManual(fileName1, manual);
					manual.blackWhiteSwitch();
					SGFGoManual.storeGoManual(fileName3, manual);
				} else {
					log.warn("after clean up fail node:");
					manual.cleanupBadMoveForWinner(true);
					SGFGoManual.storeGoManual(fileName2, manual);
					manual.blackWhiteSwitch();
					SGFGoManual.storeGoManual(fileName4, manual);
				}

			}
		}
		goS.shownInitKnownState();
		return score;

	}

	/** 198 seconds TODO 2 */
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

	public void testState_V11() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[_, B, _]");
		testState_internal(text, Constant.WHITE, 3);
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
	 * finally knife 5 loops a lot until reach duplication status! It works but
	 * not efficient
	 */
	public void testState_V3() {
		String[] text = new String[3];
		text[0] = new String("[W, _, _]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, B, B]");
		testState_internal(text, Constant.WHITE, -1);
		// testState_internal(text, Constant.BLACK, -1);

	}

	/**
	 * this is super ko, no easy to know who fail!
	 */
	public void testState_V30() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, W, B]");
		text[2] = new String("[W, _, _]");
		testState_internal(text, Constant.WHITE, -9);
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

	public void testState_V410() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[_, B, B]");
		testState_internal(text, Constant.WHITE, -1, 18);
	}

	public void testState_V411() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[_, B, B]");
		testState_internal(text, Constant.BLACK, -1);
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

	/**
	 * 相互印证。<br/>
	 * 
	 */
	public void test_verifyFinalStateWithDynamicSearch() {
		Logger.getLogger(ListAllState.class).setLevel(Level.ERROR);
		Logger.getLogger(TestAllState3.class).setLevel(Level.ERROR);
		ListAllState las = new ListAllState();
		Set<BoardColorState> finalState = las.getFinalState(3);
		Set<BoardColorState> wrongState = new HashSet<BoardColorState>();
		int count = 0;
		for (BoardColorState state : finalState) {
			System.out.println("count = " + (count++));

			if (count != 184)
				continue;
			else {
				System.out.println("Score = " + state.getScore());
			}
			System.out.println(state.getStateString());
			int depth = 15;
			this.testState_internal(state, state.getScore(), depth, true);
			BoardColorState stateSwitch = state.getReverseTurnCopy();
			System.out.println(stateSwitch.getStateString());
			this.testState_internal(stateSwitch, state.getScore(), depth, true);
			// wrongState.add(state);
		}
		// for (BoardColorState state : wrongState) {
		// log.warn(state.toString());
		// log.warn(state.getScore());
		// }
	}

}
