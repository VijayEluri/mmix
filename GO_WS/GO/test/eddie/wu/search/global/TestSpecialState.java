package eddie.wu.search.global;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.EyeResult;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.eye.BigEyeSearch;

public class TestSpecialState extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);
	static {
		Logger.getLogger(TerritoryAnalysis.class).setLevel(Level.INFO);
	}

	public void testStraightThree() {
		String inname = "doc/围棋程序数据/围棋规则/循环劫单劫加摇橹劫.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(19, 17, 3);
		// 不是大眼
		// new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;

		/**
		 * 左下的单劫已经接近最终状态，可以在Islive中取得眼型信息。仅仅是一个对角点的控制问题。
		 */
		SurviveAnalysis sa = new SurviveAnalysis(state);
		sa.printState();
		EyeResult realEyes = sa.getRealEyes(point, false);
		if (log.isEnabledFor(Level.WARN))
			log.warn(realEyes);

		// search = new BigEyeSearch(state, point, false, false);
		// score = search.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		// assertEquals(score, BigEyeSearch.DEAD);
		//
		// search = new BigEyeSearch(state, point, false, true);
		// score = search.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		// assertEquals(score, BigEyeSearch.LIVE);

		// point = Point.getPoint(17, 17);
		// search = new BigEyeSearch(state, point, false, false);
		// score = search.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		// assertEquals(score, BigEyeSearch.LIVE);
	}

	/**
	 * [B, _, B, B]<br/>
	 * [B, B, B, W]<br/>
	 * [B, W, W, W]<br/>
	 * [_, W, _, W]
	 */
	public void testLiveState4_1() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, B]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, W, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 2);

		point = Point.getPoint(4, 3, 2);
		assertTrue(sa.isLiveWithoutTwoEye(point));

		boolean finalState = sa.isFinalState_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalState=" + finalState);
		this.assertTrue(finalState);
		//

		FinalResult finalResult = sa.finalResult_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn(finalResult);
		int score = finalResult.getScore();
		this.assertEquals(1, score);
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalResult: score=" + score);

		// score = sa.finalResult_noCandidate().getScore();
		// if(log.isEnabledFor(Level.WARN))
		// log.warn("finalResult_noCandidate(): score=" + score);
	}

	public void testLiveState6_1() {
		String[] text = new String[6];
		text[0] = new String("[W, W, B, _, W, _]");
		text[1] = new String("[W, W, B, B, W, W]");
		text[2] = new String("[W, W, W, W, W, _]");
		text[3] = new String("[W, B, B, B, W, W]");
		text[4] = new String("[W, B, _, W, B, B]");
		text[5] = new String("[W, B, B, B, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point;
		point = Point.getPoint(6, 5, 5);
		assertFalse(sa.isAlreadyLive_dynamic(point));
		point = Point.getPoint(6, 4, 2);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		// score = sa.finalResult_noCandidate().getScore();
		// if(log.isEnabledFor(Level.WARN))
		// log.warn("finalResult_noCandidate(): score=" + score);
	}

	public void testLiveState5_1() {
		String[] text = new String[5];
		text[0] = new String("[_, _, _, B, _]");
		text[1] = new String("[_, W, W, B, _]");
		text[2] = new String("[_, W, B, B, _]");
		text[3] = new String("[_, W, _, B, _]");
		text[4] = new String("[_, W, _, B, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(5, 1, 4);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		// point = Point.getPoint(4, 3, 2);
		// assertTrue(sa.isLive(point));
		//
		// boolean finalState = sa.isFinalState();
		// if(log.isEnabledFor(Level.WARN)) log.warn("finalState=" +
		// finalState);
		// this.assertTrue(finalState);
		// //
		//
		// FinalResult finalResult = sa.finalResult();
		// if(log.isEnabledFor(Level.WARN)) log.warn(finalResult);
		// int score = finalResult.getScore();
		// this.assertEquals(1, score);
		// if(log.isEnabledFor(Level.WARN)) log.warn("finalResult: score=" +
		// score);

		// score = sa.finalResult_noCandidate().getScore();
		// if(log.isEnabledFor(Level.WARN))
		// log.warn("finalResult_noCandidate(): score=" + score);
	}

	/**
	 * [B, _, B, B]<br/>
	 * [B, B, _, W]<br/>
	 * [W, W, W, W]<br/>
	 * [W, W, W, _]<br/>
	 */
	public void testState4_2() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, B]");
		text[1] = new String("[B, B, _, W]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[W, W, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 2);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(4, 3, 2);
		assertTrue(sa.isAlreadyLive_dynamic(point));
	}

	public void testState4_44() {
		String[] text = new String[4];
		text[0] = new String("[_, B, B, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[B, _, _, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 1, 2);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(4, 3, 1);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(4, 4, 1);
		assertTrue(sa.isRemovable_static(point));

		point = Point.getPoint(4, 4, 4);
		assertTrue(sa.isRemovable_static(point));

		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		assertTrue(sa.isFinalState_deadExist());
		FinalResult finalResult = sa.finalResult_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn(finalResult);
		int score = finalResult.getScore();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalResult: score=" + score);
		assertEquals(0, score);
	}

	/**
	 * [B, _, B, _]<br/>
	 * [B, B, B, B]<br/>
	 * [B, W, W, W]<br/>
	 * [B, B, W, _]<br/>
	 */
	public void testState4_5() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[B, B, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 2);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(4, 3, 2);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		boolean finalState = sa.isFinalState_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalState=" + finalState);
		assertTrue(finalState);
		//

		FinalResult finalResult = sa.finalResult_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn(finalResult);
		int score = finalResult.getScore();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalResult: score=" + score);
		assertEquals(16, score);

	}

	/**
	 * [_, B, B, B]<br/>
	 * [B, B, B, B]<br/>
	 * [B, W, W, W]<br/>
	 * [B, W, _, _]<br/>
	 */
	public void testState4_6() {
		String[] text = new String[4];
		text[0] = new String("[_, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[B, W, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 2);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(4, 3, 2);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		boolean finalState = sa.isFinalState_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalState=" + finalState);
		assertFalse(finalState);
		//

		// FinalResult finalResult = sa.finalResult();
		// if(log.isEnabledFor(Level.WARN)) log.warn(finalResult);
		// int score = finalResult.getScore();
		// if(log.isEnabledFor(Level.WARN)) log.warn("finalResult: score=" +
		// score);
		// assertEquals(16, score);

	}

	/**
	 * [_, W, _, _]<br/>
	 * [W, _, _, W]<br/>
	 * [W, W, W, W]<br/>
	 * [W, W, W, _]<br/>
	 */
	public void testState4_4() {
		String[] text = new String[4];
		text[0] = new String("[_, W, _, _]");
		text[1] = new String("[W, _, _, W]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[W, W, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 1);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(4, 1, 2);
		assertTrue(sa.isAlreadyLive_dynamic(point));
	}

	/**
	 * [_, W, W, W]<br/>
	 * [W, W, W, W]<br/>
	 * [W, W, W, _]<br/>
	 * [W, W, _, W]<br/>
	 */
	public void testState4_8() {
		String[] text = new String[4];
		text[0] = new String("[_, W, W, W]");
		text[1] = new String("[W, W, W, W]");
		text[2] = new String("[W, W, W, _]");
		text[3] = new String("[W, W, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 1);
		assertTrue(sa.isAlreadyLive_dynamic(point));
		boolean finalState = sa.isFinalState_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalState=" + finalState);
		assertTrue(finalState);

		FinalResult finalResult = sa.finalResult_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn(finalResult);
		int score = finalResult.getScore();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalResult: score=" + score);
		assertEquals(-16, score);

		point = Point.getPoint(4, 4, 4);
		assertTrue(sa.isAlreadyLive_dynamic(point));
	}

	/**
	 * [B, _, B, W]<br/>
	 * [B, B, B, W]<br/>
	 * [B, W, W, W]<br/>
	 * [_, W, _, _]<br/>
	 * 白方能杀黑，黑方没有活；但是黑方可以紧白发的气达到双活。对杀的建模，迫在眉睫。
	 */
	public void testState4_9() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, W]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, W, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 1);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(4, 3, 2);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		boolean finalState = sa.isFinalState_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalState=" + finalState);
		assertFalse(finalState);

		// FinalResult finalResult = sa.finalResult();
		// if(log.isEnabledFor(Level.WARN)) log.warn(finalResult);
		// int score = finalResult.getScore();
		// if(log.isEnabledFor(Level.WARN)) log.warn("finalResult: score=" +
		// score);
		// assertEquals(-16, score);

	}

	public void testState4_92() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, W]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, W, _, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point;
		// point = Point.getPoint(4, 2, 1);
		// assertTrue(sa.isLive(point));

		// point = Point.getPoint(4, 3, 2);
		// assertTrue(sa.isLive(point));

		boolean finalState = sa.isFinalState_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalState=" + finalState);
		assertTrue(finalState);

		FinalResult finalResult = sa.finalResult_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn(finalResult);
		int score = finalResult.getScore();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalResult: score=" + score);
		assertEquals(-1, score);

	}

	/**
	 * [W, W, _, W]<br/>
	 * [_, W, W, W]<br/>
	 * [W, W, W, W]<br/>
	 * [_, W, W, _]<br/>
	 * 白方能杀黑，黑方没有活；但是黑方可以紧白发的气达到双活。对杀的建模，迫在眉睫。
	 */
	public void testState4_10() {
		String[] text = new String[4];
		text[0] = new String("[W, W, _, W]");
		text[1] = new String("[_, W, W, W]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[_, W, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 1, 1);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		boolean finalState = sa.isFinalState_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalState=" + finalState);
		assertTrue(finalState);

		FinalResult finalResult = sa.finalResult_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn(finalResult);
		int score = finalResult.getScore();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalResult: score=" + score);
		assertEquals(-16, score);

	}

	/**
	 * [_, W, _, W]<br/>
	 * [B, W, W, W]<br/>
	 * [W, W, W, W]<br/>
	 * [_, W, W, _]<br/>
	 * 白方能杀黑，黑方没有活；但是黑方可以紧白发的气达到双活。对杀的建模，迫在眉睫。
	 */
	public void testState4_11() {
		String[] text = new String[4];
		text[0] = new String("[_, W, _, W]");
		text[1] = new String("[B, W, W, W]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[_, W, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 1);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(4, 3, 2);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		boolean finalState = sa.isFinalState_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalState=" + finalState);
		assertTrue(finalState);

		FinalResult finalResult = sa.finalResult_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn(finalResult);
		int score = finalResult.getScore();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalResult: score=" + score);
		assertEquals(-16, score);

	}

	public void testState4_12() {
		Logger.getLogger(GoBoard.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[B, _, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, B, B, B]");
		text[3] = new String("[B, _, B, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);

		TerritoryAnalysis sa = new TerritoryAnalysis(state);
		sa.output();
		Point point = Point.getPoint(4, 1, 1);

		assertTrue(sa.isAlreadyLive_dynamic(point));

		boolean finalState = sa.isFinalState_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalState=" + finalState);
		assertTrue(finalState);

		FinalResult finalResult = sa.finalResult_deadExist();
		if (log.isEnabledFor(Level.WARN))
			log.warn(finalResult);
		int score = finalResult.getScore();
		if (log.isEnabledFor(Level.WARN))
			log.warn("finalResult: score=" + score);
		assertEquals(16, score);

	}

	/**
	 * [B, _, B, B]<br/>
	 * [B, B, _, _]<br/>
	 * [_, _, _, _]<br/>
	 * [_, _, _, B]<br/>
	 */
	public void testState4_2_1() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, B]");
		text[1] = new String("[B, B, _, _]");
		text[2] = new String("[_, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 2);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		// TOOD: the capture at [4,3]
	}

	/**
	 * 
	 * [_, W, _, _]<br/>
	 * [W, _, _, W]<br/>
	 * [W, W, W, W]<br/>
	 * [W, W, W, _]<br/>
	 */
	public void testState4_3() {
		String[] text = new String[4];
		text[0] = new String("[_, W, _, _]");
		text[1] = new String("[W, _, _, W]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[W, W, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(4, 2, 1);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(4, 1, 2);
		assertTrue(sa.isAlreadyLive_dynamic(point));
	}

	public void testState9_3() {
		String[] text = new String[9];
		text[0] = new String("[_, _, _, _, _, _, W, W, W]");
		text[1] = new String("[_, _, _, _, _, _, W, B, W]");
		text[2] = new String("[_, _, _, _, _, _, W, B, B]");
		text[3] = new String("[_, _, _, _, W, W, W, W, B]");
		text[4] = new String("[_, _, W, W, W, B, B, B, _]");
		text[5] = new String("[_, _, W, B, B, B, W, W, W]");
		text[6] = new String("[_, _, W, B, _, B, B, B, B]");
		text[7] = new String("[_, _, W, B, W, W, W, W, W]");
		text[8] = new String("[_, _, W, B, W, _, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(9, 6, 4);
		assert (sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(9, 8, 5);
		assertTrue(sa.isAlreadyLive_dynamic(point));

		// 如果相邻两块都不是活棋，就需要通过搜索确定结果
	}

}
