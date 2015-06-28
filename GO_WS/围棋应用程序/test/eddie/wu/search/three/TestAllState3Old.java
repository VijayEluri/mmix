package eddie.wu.search.three;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.small.ThreeThreeBoardSearch;

public class TestAllState3Old extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);
	static {
		Constant.INTERNAL_CHECK = false;
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j_error.xml";
//		System.setProperty(key, value);
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.INFO);
//		Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
//		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.DEBUG);
	}

	public void testState_init() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
				Constant.BLACK, 1, 0);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);

		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
			if (count % 100 == 0)
				if(log.isEnabledFor(Level.WARN)) log.warn("count=" + count);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(1, score);
	}

	/** 198 seconds */
	public void testState() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
				Constant.BLACK, 4, 3);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(4, score);
		goS.outputSearchStatistics();

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
	}

	public void testState_BentFive() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
				Constant.WHITE, 4, 3);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
			if (count % 100 == 0)
				if(log.isEnabledFor(Level.WARN)) log.warn("count=" + count);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(4, score);
	}

	public void testState_GlobalLoop() {
		String[] text = new String[3];
		text[0] = new String("[W, _, W]");
		text[1] = new String("[B, W, B]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
				Constant.WHITE, 3, 2);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(3, score);
		goS.outputSearchStatistics();

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
	}

	public void testCandidate() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[W, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if(log.isEnabledFor(Level.WARN)) log.warn(can.getStep());
		}

	}

	public void testCandidate2() {
		String[] text = new String[3];
		text[0] = new String("[B, B, B]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if(log.isEnabledFor(Level.WARN)) log.warn(can.getStep());
		}

	}
	
	public void testState_Final2() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state,Constant.WHITE);
		assertFalse(ta.isFinalState_deadExist());
	}

	// [INIT]B-->[1,3]W-->[1,1]B-->[1,2]W-->[1,1]B-->[2,2]W-->[1,2]B-->[2,3]W-->[2,1]B-->[3,2](FINAL
	// 1)
	public void testState_Final() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		assertTrue(ta.isFinalState_deadExist());
		assertEquals(9, ta.finalResult_deadExist().getScore());

		assertFalse(ta.isFinalState_deadCleanedUp());
		// assertEquals(9, ta.finalResult_deadCleanedUp().getScore());
		// ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
		// Constant.BLACK, 4, 3);
		// int score = goS.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		// assertEquals(4, score);
		// goS.outputSearchStatistics();
		//
		// for (String list : goS.getSearchProcess()) {
		// if(log.isEnabledFor(Level.WARN)) log.warn(list);
		// }
	}

	public void testState_1() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.BLACK,
				-2, -3);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
			if (count % 100 == 0)
				if(log.isEnabledFor(Level.WARN)) log.warn("count=" + count);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(-3, score);
	}

	public void testState_oneBreathCoLive() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.BLACK, 4,
				3);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
			if (count % 100 == 0)
				if(log.isEnabledFor(Level.WARN)) log.warn("count=" + count);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(4, score);
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
		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.BLACK, 9,
				8);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
	}

	public void testState_2() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
				Constant.BLACK, 9, 8);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);
		goS.outputSearchStatistics();

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
	}

	public void testState_311() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[_, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));
		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertTrue(finalState);
	}

	public void testState_3110() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new ThreeThreeBoardSearch(state, Constant.BLACK,9,-9);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());

	}

	public void testState_3111() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[W, B, _]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SurviveAnalysis sa = new SurviveAnalysis(state);
		assertFalse(sa.isAlreadyLive_dynamic(Point.getPoint(3, 2, 1)));

	}

	public void testState_311V() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Point point = Point.getPoint(3, 2, 2);
		assertFalse(ta.isAlreadyLive_dynamic(point));

		GoBoardSearch goS;
		int score;
		// goS = new ThreeThreeBoardSearch(state, Constant.BLACK);
		goS = new ThreeThreeBoardSearch(state, Constant.WHITE,9,-9);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
	}

	public void testState_312() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		finalState = new SmallGoBoard(state).isFinalState_deadExist();
//		assertTrue(finalState);
		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.BLACK, -2, -3);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
			if (count % 100 == 0)
				if(log.isEnabledFor(Level.WARN)) log.warn("count=" + count);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(-3, score);
		
		 goS = new ThreeThreeBoardSearch(state, Constant.WHITE, -2,
				-3);
		 score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		 count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
			if (count % 100 == 0)
				if(log.isEnabledFor(Level.WARN)) log.warn("count=" + count);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(-3, score);

	}

	public void testState_313_blackFirst() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		finalState = new SmallGoBoard(state).isFinalState_deadExist();
		assertFalse(finalState);

		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.BLACK, 3, 2);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
			if (count % 100 == 0)
				if(log.isEnabledFor(Level.WARN)) log.warn("count=" + count);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(3, score);

	}

	public void testState_313_whiteFirst() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		finalState = new SmallGoBoard(state).isFinalState_deadExist();
		assertFalse(finalState);

		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.WHITE, 3,
				2);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
			if (count % 100 == 0)
				if(log.isEnabledFor(Level.WARN)) log.warn("count=" + count);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(3, score);

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

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.WHITE,9,-9);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);

		goS = new ThreeThreeBoardSearch(state, Constant.BLACK,9,-9);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());

		// finalState = new SmallGoBoard(state).isFinalState();
		// assertFalse(finalState);
	}

	public void testState_3141() {
		String[] text = new String[3];
		text[0] = new String("[_, B, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Point point = Point.getPoint(3, 1, 2);
		assertTrue(ta.isAlreadyLive_dynamic(point));

		boolean finalState = ta.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.WHITE,9,-9);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
	}

	public void testState_3142() {
		String[] text = new String[3];
		text[0] = new String("[_, B, B]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Point point = Point.getPoint(3, 1, 2);
		assertTrue(ta.isAlreadyLive_dynamic(point));

		boolean finalState = ta.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.WHITE,9,-9);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
	}

	public void testState_3143() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, B, _]");
		text[2] = new String("[_, W, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Point point = Point.getPoint(3, 1, 2);
		// assertTrue(ta.isLive(point));

		boolean finalState = ta.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		GoBoardSearch goS = new ThreeThreeBoardSearch(state, Constant.WHITE,9,-9);
		int score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
	}

	public void testState_315() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);

		finalState = new SmallGoBoard(state).isFinalState_deadExist();
		assertTrue(finalState);
	}

	public void testState_316() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new ThreeThreeBoardSearch(state, Constant.BLACK,9,8);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);
		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());

		goS = new ThreeThreeBoardSearch(state, Constant.WHITE, 4, 3);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(3, score);
		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		// finalState = new SmallGoBoard(state).isFinalState();
		// assertFalse(finalState);
	}

	public void testState_317() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new ThreeThreeBoardSearch(state, Constant.BLACK,9,-9);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(9, score);
		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());

		goS = new ThreeThreeBoardSearch(state, Constant.WHITE, 5, 4);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(4, score);
		// finalState = new SmallGoBoard(state).isFinalState();
		// assertFalse(finalState);
	}

	public void testState_318() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new ThreeThreeBoardSearch(state, Constant.WHITE, 4, 3);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(4, score);

	}

	/**
	 * 8 seconds.
	 */
	public void testState_319() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new ThreeThreeBoardSearch(state, Constant.BLACK, -2, -3);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
		assertEquals(-3, score);
	}

	/**
	 * we calculate steps = 2491 <br/>
	 * we know the result = 1575<br/>
	 * 8 seconds.--> 12 seconds
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

		boolean finalState = new SmallGoBoard(state)
				.isFinalState_deadCleanedUp();
		assertFalse(finalState);
		ThreeThreeBoardSearch goS;
		int score;
		goS = new ThreeThreeBoardSearch(state, Constant.WHITE, -8, -9);
		score = goS.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		assertEquals(-9, score);
		goS.outputSearchStatistics();

		for (String list : goS.getSearchProcess()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(list);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(goS.getSearchProcess().size());
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

		point = Point.getPoint(3, 2, 3);
		live = new SurviveAnalysis(state).isAlreadyLive_dynamic(point);
		assertFalse(live);

	}

	public void testoneStepForward() {
		Logger log = Logger.getLogger(SurviveAnalysis.class);
		log.setLevel(Level.DEBUG);

		String[] text = new String[3];
		text[0] = new String("[B, _, W]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state);
		sa.oneStepForward(new Step(Point.getPoint(3, 2, 3), Constant.WHITE));
		sa.oneStepForward(new Step(Point.getPoint(3, 1, 2), Constant.BLACK));
		sa.printState(log);
		sa.printInternalData(log);

		sa.oneStepBackward();
		sa.oneStepBackward();
		sa.printState(log);
		sa.printInternalData(log);

		sa.oneStepForward(new Step(Point.getPoint(3, 1, 2), Constant.WHITE));
		sa.printState(log);
		sa.printInternalData(log);
	}

}
