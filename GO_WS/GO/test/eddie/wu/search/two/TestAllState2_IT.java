package eddie.wu.search.two;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.StateLoader;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.HistoryDepScore;
import eddie.wu.search.ScopeScore;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.ListAllState;
import eddie.wu.search.global.VerifySearchResult;
import eddie.wu.search.small.SmallBoardGlobalSearch;
import eddie.wu.search.small.ThreeThreeBoardSearch;
import eddie.wu.search.small.TwoTwoBoardSearch;

public class TestAllState2_IT extends TestCase {
	private static Logger log = Logger.getLogger(TestAllState2.class);
	private static String manualFolder = Constant.DYNAMIC_DATA
			+ "small_board/two_two/";
	

	/**
	 * B-->[1,1]W-->[2,2]B-->[1,2]W-->[2,1
	 */
	public void testGetCandiate_suicide() {

		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard sa = new SmallGoBoard(BoardColorState.getInstance(state,
				Constant.BLACK));
		List<Candidate> candidate = sa.getCandidate(Constant.BLACK, false);
		log.warn(candidate);
	}

	


	public void testState1_blackFirst_lose_2_0() {
		testState1_blackFirst_lose(2, 2, 0);// 5s
	}

	/**
	 * both lose with [2,0]<br/>
	 * Win/Lose with Score=1<br/>
	 * WARN (TestAllState2.java:112) - searched path = 25615<br/>
	 * WARN (TestAllState2.java:122) - Before Cleanup <br/>
	 * WARN (TestAllState2.java:123) - Init score = 1<br/>
	 * WARN (TestAllState2.java:126) - After Cleanup 1: <br/>
	 * WARN (TestAllState2.java:128) - INIT variant=255, score=1, max=true<br/>
	 * After Cleanup 2: <br/>
	 * WARN (TestAllState2.java:134) - INIT variant=28, score=1, max=true<br/>
	 * both lose with [2,0]<br/>
	 * searched path = 844276<br/>
	 * WARN (TestAllState2.java:137) - Before Cleanup <br/>
	 * WARN (TestAllState2.java:138) - Init score = 1<br/>
	 * WARN (TestAllState2.java:141) - After Cleanup 1: <br/>
	 * WARN (TestAllState2.java:143) - INIT variant=786, s<br/>
	 * WARN (TestAllState2.java:147) - After Cleanup 2: <br/>
	 * WARN (TestAllState2.java:149) - INIT variant=28, score=1<br/>
	 * TODO: no enough memory!
	 */
	protected void testState1_blackFirst_lose(int boardSize, int high, int low) {
		BoardColorState boardState = null;
		int expScore = 1;
		if (boardSize == 2) {
			String[] text = new String[2];
			text[0] = new String("[_, _]");
			text[1] = new String("[_, _]");
			byte[][] state = StateLoader.LoadStateFromText(text);
			boardState = new BoardColorState(state, Constant.BLACK);
		} else if (boardSize == 3) {
			String[] text = new String[3];
			text[0] = new String("[_, _, _]");
			text[1] = new String("[_, _, _]");
			text[2] = new String("[_, _, _]");
			byte[][] state = StateLoader.LoadStateFromText(text);
			boardState = new BoardColorState(state, Constant.BLACK);
			expScore = 9;
		}

		SmallBoardGlobalSearch goS = null;
		if (boardSize == 2) {
			goS = new TwoTwoBoardSearch(boardState, high, low);
		} else {
			goS = new ThreeThreeBoardSearch(boardState, high, low);
		}
		goS.setVariant(1500000);
		// goS.setDeepth(18); cannot help. the setup means we need to cover all
		// variants.
		int score = goS.globalSearch();
		TreeGoManual manual = goS.getTreeGoManual();
		int initScore = manual.initScore();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("Win/Lose with Score=" + score);
			// log.warn("searched path = " + goS.getSearchProcess().size());
			// log.warn("Most Expensive path: ");
			// log.warn(Constant.lineSeparator + manual.getMostExpPath());
			goS.outputSearchStatistics();
		}
		assertEquals(expScore, score);
		assertTrue(score != high);
		assertTrue(score != low);

		String name1 = goS.getGoBoard().getInitColorState()
				.getStateAsOneLineString()
				+ goS.getMaxExp() + goS.getMinExp() + " win.sgf";
		String name11 = goS.getGoBoard().getInitColorState()
				.getStateAsOneLineString()
				+ goS.getMaxExp() + goS.getMinExp() + " multiple win.sgf";

		String fileName1 = manualFolder + name1;
		String fileName11 = manualFolder + name11;
		String name2 = goS.getGoBoard().getInitColorState()
				.getStateAsOneLineString()
				+ goS.getMaxExp() + goS.getMinExp() + " lose.sgf";
		String name21 = goS.getGoBoard().getInitColorState()
				.getStateAsOneLineString()
				+ goS.getMaxExp() + goS.getMinExp() + " multiple lose.sgf";
		String fileName2 = manualFolder + name2;
		String fileName21 = manualFolder + name21;

		int variant = manual.initVariant();
		log.warn("Before Cleanup ");
		log.warn("Init score = " + initScore + ", variant=" + variant);
		TreeGoManual maxWin = manual.getMaxWinResult();
		TreeGoManual maxWin2 = maxWin.getMaxWinResult();
		log.warn("Maxwin: ");
		log.warn(maxWin.getSGFBodyString(false));
		SGFGoManual.storeGoManual(fileName11, maxWin);

		maxWin.chooseBestMoveForWinner(true);
		log.warn("after choose Max best move only!");
		log.warn(maxWin.getSGFBodyString(false));
		SGFGoManual.storeGoManual(fileName1, maxWin);

		TreeGoManual minWin = manual.getMinWinResult();
		TreeGoManual minWin2 = minWin.getMinWinResult();// copy
		log.warn("Minwin: ");
		log.warn(minWin.getSGFBodyString(false));
		SGFGoManual.storeGoManual(fileName21, minWin);

		minWin.chooseBestMoveForWinner(false);
		log.warn("after choose Min best move only!");
		log.warn(minWin.getSGFBodyString(false));
		SGFGoManual.storeGoManual(fileName2, minWin);

		log.warn("best moves of both: 1");
		maxWin2.cleanupBadMoveForBoth();
		log.warn(maxWin2.getSGFBodyString(false));
		log.warn("best moves of both: 2");
		minWin2.cleanupBadMoveForBoth();
		log.warn(minWin2.getSGFBodyString(false));

		;
		// manual2.cleanupBadMoveForWinner(true);cl
		// manual2.initVariant();
		// log.warn("After Cleanup 2.1: ");
		// log.warn(manual.getSGFBodyString(false));
		//
		// manual2.chooseBestMoveForWinner(true);
		// manual2.initVariant();
		// log.warn("After Cleanup 2.2: ");
		// log.warn(manual2.getSGFBodyString(false));
		// SGFGoManual.storeGoManual(fileName1, manual2);

	}

	/**
	 * final path:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Score=0<br/>
	 * New result after allow symmetric different candidate:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=White, index=10, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=Give Up, color=Black, index=11, loopSuperior= false,
	 * name=null]<br/>
	 */
	public void testState1_blackFirst_win() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 1);
		// goS.setDeepth(2*2*4+1);
		goS.setDepth(11);
		int score = 0;
		try {
			score = goS.globalSearch();
		} catch (Exception e) {
			// this.fail(message);
		}

		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("Win with Score=" + score);
			goS.outputSearchStatistics();
		}
		// assertEquals(1, score);

		String name = goS.getGoBoard().getInitColorState()
				.getStateAsOneLineString()
				+ " win.sgf";
		String fileName = manualFolder + name;
		TreeGoManual manual = goS.getTreeGoManual();
		int initScore = manual.initScore();
		log.warn("Before Cleanup ");
		int initVariant = manual.initVariant();
		log.warn("Init score = " + initScore + ", variant= " + initVariant);
		if (initVariant < 500) {
			log.warn(manual.getSGFBodyString(false));
		}

		if (initScore == SearchNode.UNKNOWN_SCORE) {
			return;
		}
		// manual.cleanupBadMove_firstWin(goS.initTurn, goS.getMaxExp());
		manual.cleanupBadMoveForWinner(true);
		log.warn("After Cleanup: ");
		log.debug(manual.getExpandedString(false));// init variant
		log.warn(manual.getSGFBodyString(false));
		SGFGoManual.storeGoManual(fileName, manual);
	}

	public void testState1_blackFirst_lose() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 2);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("Lose with Score=" + score);
			goS.outputSearchStatistics();
		}
		assertEquals(1, score);

		String name = goS.getGoBoard().getInitColorState()
				.getStateAsOneLineString()
				+ " lose.sgf";
		String fileName = manualFolder + name;
		TreeGoManual manual = goS.getTreeGoManual();
		int initScore = manual.initScore();
		log.warn("Before Cleanup ");
		log.warn("Init score = " + initScore + ", variant= "
				+ manual.initVariant());
		log.warn(manual.getSGFBodyString(false));
		manual.cleanupBadMoveForWinner(false);
		log.warn("After Cleanup: ");
		log.warn(manual.getSGFBodyString(false));
		SGFGoManual.storeGoManual(fileName, manual);
	}

	public void testState1_whiteFirst() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		// if(log.isEnabledFor(org.apache.log4j.Level.WARN))
		// log.warn(Arrays.deepToString(state));
		BoardColorState boardState = new BoardColorState(state, Constant.WHITE);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, -1);

		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(-1, score);

	}

	/**
	 * about 5 seconds: 04s without log<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=White, index=10, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=Give Up, color=Black, index=11, loopSuperior= false,
	 * name=null]<br/>
	 */
	public void testState1A_blackFirst() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 2);
		goS.setDepth(12);
		int score = goS.globalSearch();
		assertEquals(1, score);
		
		goS.outputSearchStatistics();
		String name = goS.getGoBoard().getInitColorState()
				.getStateAsOneLineString()+ goS.getExpScore().toFileName()
				+ " lose.sgf";
		String fileName = manualFolder + name;
		TreeGoManual manual = goS.getTreeGoManual();		
		SGFGoManual.storeGoManual(fileName, manual);
		log.info("result is stored into "+fileName);
		
		VerifySearchResult.verifyBetterImpossible(boardState, 1, manual);
	}

	public void testState1A_whiteFirst() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState boardState = new BoardColorState(state, Constant.WHITE);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, -2);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(-1, score);
		
		goS.outputSearchStatistics();
		TreeGoManual manual = goS.getTreeGoManual();	
		VerifySearchResult.verifyBetterImpossible(boardState, -1, manual);
	}

	/**
	 * Step [point=null, color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Score=0<br/>
	 * new result<br/>
	 * Step [point=[1,2], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=White, index=4, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=[1,2], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=Black, index=7, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=Give Up, color=White, index=8, loopSuperior= false,
	 * name=null]<br/>
	 * Score=1
	 */
	public void testState2() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));
		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 1);
		// GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 1,
		// 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(1, score);
	}

	public void testState2_black() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));
		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 1);
		// GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 1,
		// 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(1, score);
	}

	public void testState2_A() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState boardState = new BoardColorState(state, Constant.WHITE);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, -1);
		// TwoTwoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE,
		// 0,
		// -1);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn(goS.getGoBoard().getInitColorState().getStateString());
			log.warn("Score=" + score);
		}

		assertEquals(-1, score);
	}

	public void testState3_A() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState boardState = new BoardColorState(state, Constant.WHITE);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 0);
		// GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE, 1,
		// 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(org.apache.log4j.Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(1, score);
	}

	/**
	 * the state reached firstly by passing is still not allowed for
	 * duplication. <br/>
	 * [INIT]W-->[PAS]B-->[2,1]W-->[2,2]B-->[2,1]W-->[1,2]B-->[2,2]W-->[1,1]
	 */
	public void testDuplicateA() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state, Constant.WHITE);
		for (BoardColorState stateC : go.getStepHistory().getColorStates()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(stateC.getStateString());
		}

		List<Step> steps = new ArrayList<Step>();
		Step step;
		step = new Step(null, Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 1), Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 2), Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 1), Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 1, 2), Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 2), Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 1, 1), Constant.WHITE);
		steps.add(step);

		boolean valid = false;
		for (Step stepT : steps) {
			valid = go.oneStepForward(stepT);
			if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
				log.warn(stepT + "reach state as blow: valid >>>> " + valid);
				log.warn(go.getBoardColorState().getStateString());
				log.warn("KNown history state "
						+ go.getStepHistory().getColorStates().size());
				for (BoardColorState stateC : go.getStepHistory()
						.getColorStates()) {
					log.warn(stateC.getStateString());
				}
			}

		}

		assertFalse(valid);
	}

	public void testAAA() {
		List<Step> steps = new ArrayList<Step>();
		Step step;
		step = new Step(null, Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 1), Constant.BLACK);
		steps.add(step);
		step = new Step(null, Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 2), Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 1, 1), Constant.WHITE);
		steps.add(step);
		step = new Step(null, Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 1, 2), Constant.WHITE);
		steps.add(step);

		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state, Constant.WHITE);
		for (BoardColorState stateC : go.getStepHistory().getColorStates()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(stateC.getStateString());
		}

		boolean valid = false;
		for (Step stepT : steps) {
			valid = go.oneStepForward(stepT);
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(stepT + ">>>>" + valid);
		}
		assertFalse(valid);
		for (BoardColorState stateC : go.getStepHistory().getColorStates()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(stateC.getStateString());
		}
	}

	public void testState3_blackFirst() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));
		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 1);
		// GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 1,
		// 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);

		for (String list : goS.getSearchProcess()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
		}
		assertEquals(1, score);

	}

	public void testState3_ABB() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));
		BoardColorState boardState = new BoardColorState(state, Constant.WHITE);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 0);
		// GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE, 1,
		// 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);

		for (String list : goS.getSearchProcess()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
		}
		assertEquals(1, score);

	}

	public void testState3_duplicate() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));
		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 0);
		// GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 0,
		// -1);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);

		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(org.apache.log4j.Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(-1, score);

	}

	public void testState3_AB() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));
		BoardColorState boardState = new BoardColorState(state, Constant.WHITE);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 1);
		// GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE, 2,
		// 1);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(1, score);

		for (String list : goS.getSearchProcess()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
		}
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(goS.getSearchProcess().size());
	}

	/**
	 * still failed, too deep!
	 */
	public void testState2_AA() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);

		GoBoardSearch goS = new TwoTwoBoardSearch(boardState, 4);
		int score = goS.globalSearch();
		if (log.isInfoEnabled()) {
			log.info(boardState.getStateString());
			log.warn("Score=" + score);
		}
		assertEquals(1, score);

		if (log.isInfoEnabled()) {
			log.info("");
			log.info("Independent: " + goS.getStateReached().size());
			for (Entry<BoardColorState, ScopeScore> entry : goS
					.getStateReached().entrySet()) {
				log.info(entry.getKey());
				log.info(entry.getValue());
				log.info("");
			}
			log.info("");
			log.info("Dependent" + goS.getStateHisDepReached().size());
			for (Entry<BoardColorState, HistoryDepScore> entry : goS
					.getStateHisDepReached().entrySet()) {
				log.info(entry.getKey());

				for (Entry<BoardColorState, ScopeScore> entry2 : entry
						.getValue().getMap().entrySet()) {
					log.info(entry2.getValue() + " depends on: ");
					log.info(entry2.getKey());
					log.info("");
				}
			}
		}
	}

	public void testState2_AA1() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));
		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);
		GoBoardSearch goS = new TwoTwoBoardSearch(boardState, 4);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(1, score);
		boardState = new BoardColorState(state, Constant.WHITE);
		goS = new TwoTwoBoardSearch(boardState, -4);
		// goS = new TwoTwoBoardSearch(state, Constant.WHITE, -3, -4);
		score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(-1, score);
	}

	/**
	 * final path:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Score=0<br/>
	 * new result:<br/>
	 * final path:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=White, index=6, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=[1,2], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=Black, index=9, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=Give Up, color=White, index=10, loopSuperior= false,
	 * name=null]<br/>
	 * Score=1
	 */
	public void testState223() {
		String fileName = manualFolder + "_WWW.sgf.complex";
		String[] text = new String[2];
		text[0] = new String("[_, W]");
		text[1] = new String("[W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		TreeGoManual manual = null;
		BoardColorState boardState = new BoardColorState(state, Constant.BLACK);
		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(boardState, 1);
		// GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 1,
		// 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("Score=" + score);
			for (String list : goS.getSearchProcess()) {
				log.warn(list);
			}
			log.warn(goS.getSearchProcess().size());
			manual = goS.getTreeGoManual();
			log.warn("Search Tree");
			log.warn(manual.getSGFBodyString());
			log.warn("simple manual");
			log.warn(manual.getExpandedString());
			log.warn(manual.getVariant());
		}
		assertEquals(1, score);

		manual = goS.getGoBoard().getTreeGoManual();

		if (log.isEnabledFor(Level.WARN)) {
			log.warn("Tree manual");
			log.warn(manual.getSGFBodyString());
			log.warn("simple manual");
			log.warn(manual.getExpandedString());
			log.warn(manual.getVariant());
		}
		SGFGoManual.storeGoManual(fileName, manual);

	}

	public void testFinalResult() {
		String[] text = new String[2];
		text[0] = new String("[_, B]");
		text[1] = new String("[B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis analysis = new TerritoryAnalysis(state);
		boolean state2 = analysis.isFinalState_deadExist();
		assertTrue(state2);

		state2 = analysis.isFinalState_deadCleanedUp();
		assertTrue(state2);

		int finalState = analysis.finalStateType();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(finalState);
		assertEquals(2, finalState);
		FinalResult result = analysis.finalResult_deadExist();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(result);
		// if (log.isDebugEnabled())
		// log.debug(result);
	}

	/**
	 * There are only 26 real states.
	 */
	public void testAllState() {
		int count = 0;
		Set<BoardColorState> validState = new ListAllState().getValidState(2);
		log.warn("Real valid states = " + validState.size());
		for (BoardColorState state : validState) {
			int score = 0;
			score = TwoTwoBoardSearch.getAccurateScore(state);
			state.setScore(score);
		}

		for (BoardColorState state : validState) {
			count++;
			if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
				log.warn("==========================");
				log.warn("count=" + count);
				log.warn("Score=" + state.getScore());
				log.warn("variant=" + state.getVariant());
				log.warn("State=" + state.getStateString());
				log.warn("");
			}
		}
	}

}
