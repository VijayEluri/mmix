package eddie.wu.search.smallboard;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.domain.state.StateUtil;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.eye.BigEyeSearch;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.small.ThreeThreeBoardSearch;

public class TestAllState4Old extends TestCase {

	private transient static final Logger log = Logger
			.getLogger(TestAllState4Old.class);
	static {
		Constant.INTERNAL_CHECK = false;
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j_error.xml";
		System.setProperty(key, value);
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.ERROR);
	}

	public void testState_init() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[_, _, _, _]");
		text[2] = new String("[_, _, _, _]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
				Constant.BLACK, 1, 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);

		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (count % 100 == 0) {
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
				if (log.isEnabledFor(Level.WARN))
					log.warn(list);
			}
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(1, score);
	}

	public void testState_init2() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
				Constant.WHITE, 1, 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);

		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (count % 100 == 0) {
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
				if (log.isEnabledFor(Level.WARN))
					log.warn(list);
			}
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(1, score);
	}

	public void testCandidate_131() {
		//
		//
		// SmallGoBoard ta = new SmallGoBoard(state);
		// List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		// for (Candidate can : candidate) {
		// if(log.isEnabledFor(Level.WARN)) log.warn(can);
		// }

	}

	public void testLive_captured() {
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[W, _, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state);
		Point point = Point.getPoint(4, 2, 1);
		boolean dynamic = sa.isBigEyeLive(point);
		if (log.isEnabledFor(Level.WARN))
			log.warn("live= " + dynamic);
		assertTrue(dynamic);
		dynamic = sa.isAlreadyLive_dynamic(point);
		if (log.isEnabledFor(Level.WARN))
			log.warn("live= " + dynamic);
		assertTrue(dynamic);
	}

	public void testLive_captured3() {
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[_, W, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[B, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard sa = new SmallGoBoard(state);
		boolean deadExist = sa.isFinalState_deadExist();
		assertTrue(deadExist);
	}

	public void testLive_captured4() {
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[_, W, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[B, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state);
		Point point;
		boolean dynamic;
		// dynamic = sa.isBigEyeLive(point);
		// if(log.isEnabledFor(Level.WARN)) log.warn("live= " + dynamic);
		// assertTrue(dynamic);
		point = Point.getPoint(4, 4, 4);
		dynamic = sa.isAlreadyLive_dynamic(point);
		if (log.isEnabledFor(Level.WARN))
			log.warn("live= " + dynamic);
		assertTrue(dynamic);
	}

	public void testLive_captured2() {
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[_, W, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[B, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state);
		Point point;
		boolean dynamic;
		// dynamic = sa.isBigEyeLive(point);
		// if(log.isEnabledFor(Level.WARN)) log.warn("live= " + dynamic);
		// assertTrue(dynamic);
		point = Point.getPoint(4, 2, 1);
		dynamic = sa.isAlreadyLive_dynamic(point);
		if (log.isEnabledFor(Level.WARN))
			log.warn("live= " + dynamic);
		assertTrue(dynamic);

		point = Point.getPoint(4, 1, 2);
		dynamic = sa.isAlreadyLive_dynamic(point);
		if (log.isEnabledFor(Level.WARN))
			log.warn("live= " + dynamic);
		assertFalse(dynamic);

		point = Point.getPoint(4, 4, 4);
		dynamic = sa.isAlreadyLive_dynamic(point);
		if (log.isEnabledFor(Level.WARN))
			log.warn("live= " + dynamic);
		assertFalse(dynamic);
	}

	public void testLive_Se1arch() {
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		log.setLevel(Level.DEBUG);
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);

		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		Point blankP = Point.getPoint(4, 3, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), false, false);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testLive_Search22() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[_, W, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[B, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 4, 4);
		Point blankP = Point.getPoint(4, 3, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), false, false);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testLive_Search222() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[B, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 4, 4);
		Point blankP = Point.getPoint(4, 3, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), false, false);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_DEAD, score);
	}

	public void testLive_Search222_targetFirst() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[B, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 4, 4);
		Point blankP = Point.getPoint(4, 3, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), true, false);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	// [INIT]W-->[1,2]B-->[1,3]W-->[1,4]B-->[2,4]W-->[PAS](FINAL -128)
	public void testLive_Search231() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[_, W, B, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		boolean live = sa.isOneBlockTwoEyeLive(point);
		assertTrue(live);
	}

	// [INIT]W-->[1,2]B-->[1,4]W-->[1,1]B-->[1,3]W-->[1,1]B-->[2,4](FINAL 128)
	public void testLive_Search232() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[W, _, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		boolean live = sa.isOneBlockTwoEyeLive(point);
		assertFalse(live);
	}

	public void testLive_Search23() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		Point blankP = Point.getPoint(4, 1, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), false, false);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_DEAD, score);
	}

	public void testLive_Search_liveDead() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		Point blankP = Point.getPoint(4, 3, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), false, false);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_DEAD, score);
	}

	public void testLive_Search_enemyfirst_targetLoopAdv() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		Point blankP = Point.getPoint(4, 3, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), false, true);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testLive_Search_finalState2() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, _, B, B]");
		text[3] = new String("[_, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());

		Point point = Point.getPoint(4, 2, 1);
		boolean live = sa.isStaticLive(point);
		assertTrue(live);

	}

	public void testLive_Search_finalState22() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		String[] text = new String[4];
		text[0] = new String("[_, W, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, W, _]");
		text[3] = new String("[B, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard sa = new SmallGoBoard(state, Constant.BLACK);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());

		Point point = Point.getPoint(4, 2, 1);
		boolean finalState = sa.isFinalState_deadExist();
		assertTrue(finalState);

	}

	// [INIT]B-->[2,2]W-->[3,3]B-->[2,3]W-->[3,2]B-->[2,1]W-->[3,4]B-->[2,4]W-->[3,1]B-->[1,2](FINAL
	// 16)
	public void testLive_Search_finalState3() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard sa = new SmallGoBoard(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());

		Point point = Point.getPoint(4, 2, 1);
		boolean live = sa.isStaticLive(point);
		assertFalse(live);
		boolean dead = sa.isAlreadyDead_dynamic(point);
		assertFalse(dead);

		Point point2 = Point.getPoint(4, 3, 1);
		boolean live2 = sa.isStaticLive(point2);
		assertFalse(live2);
		boolean dead2 = sa.isAlreadyDead_dynamic(point2);
		assertFalse(dead2);

		boolean res = sa.isFinalState_deadExist();
		assertFalse(res);

	}

	public void testLive_Search_finalState31() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		String[] text = new String[4];
		text[0] = new String("[_, B, _, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard sa = new SmallGoBoard(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());

		Point point = Point.getPoint(4, 2, 1);
		boolean live = sa.isStaticLive(point);
		assertTrue(live);
		boolean dead = sa.isAlreadyDead_dynamic(point);
		assertFalse(dead);

		Point point2 = Point.getPoint(4, 3, 1);
		boolean live2 = sa.isStaticLive(point2);
		assertFalse(live2);
		boolean dead2 = sa.isAlreadyDead_dynamic(point2);
		assertFalse(dead2);

		boolean res = sa.isFinalState_deadExist();
		assertFalse(res);

	}

	public void testLive_Search_liveDead2() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.BLACK);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());

		Point point = Point.getPoint(4, 2, 1);
		Point blankP = Point.getPoint(4, 3, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), true, false);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	// [INIT]W-->[4,2]B-->[3,2]W-->[3,3]B-->[3,4](FINAL -128)
	// [INIT]W-->[4,2]B-->[3,2]W-->[3,3]B-->[3,4]W-->[4,1](FINAL -128)
	public void testLive_Search1() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point target = Point.getPoint(4, 1, 1);
		Point blankP = Point.getPoint(4, 3, 2);

		Point point = Point.getPoint(4, 4, 2);
		sa.oneStepForward(point, Constant.WHITE);
		point = Point.getPoint(4, 3, 2);
		sa.oneStepForward(point, Constant.BLACK);
		point = Point.getPoint(4, 3, 3);
		sa.oneStepForward(point, Constant.WHITE);
		point = Point.getPoint(4, 3, 4);
		sa.oneStepForward(point, Constant.BLACK);
		point = Point.getPoint(4, 4, 1);
		sa.oneStepForward(point, Constant.WHITE);

		boolean score = sa.isAlreadyDead_dynamic(target);
		if (log.isEnabledFor(Level.WARN))
			log.warn("score = " + score);
	}

	// [INIT]W-->[3,3]B-->[4,2]W-->[4,3]B-->[3,4]W-->[2,4]B-->[3,4]W-->[3,2]B
	public void testLive_Search21() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point target = Point.getPoint(4, 1, 1);
		Point blankP = Point.getPoint(4, 3, 2);

		Point point = Point.getPoint(4, 3, 3);
		sa.oneStepForward(point, Constant.WHITE);
		point = Point.getPoint(4, 4, 2);
		sa.oneStepForward(point, Constant.BLACK);

		point = Point.getPoint(4, 4, 3);
		sa.oneStepForward(point, Constant.WHITE);
		point = Point.getPoint(4, 3, 4);
		sa.oneStepForward(point, Constant.BLACK);

		point = Point.getPoint(4, 2, 4);
		sa.oneStepForward(point, Constant.WHITE);
		point = Point.getPoint(4, 3, 4);
		sa.oneStepForward(point, Constant.BLACK);

		point = Point.getPoint(4, 3, 2);
		sa.oneStepForward(point, Constant.WHITE);

		// BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
		// sa.getBlankBlock(blankP), false, false);
		// for(Candidate candidate:sa.getCandidate(target, candidates,
		// Constant.BLACK,
		// false, false)){
		// if(log.isEnabledFor(Level.WARN)) log.warn(candidate);
		// }

	}

	public void testLive_Search2() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[W, _, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		Point blankP = Point.getPoint(4, 3, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), false, false);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testLive_Search25() {
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);

		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[B, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(sa.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		Point blankP = Point.getPoint(4, 3, 2);
		BigEyeSearch goS = new BigEyeSearch(state, point, Constant.BLACK,
				sa.getBlankBlock(blankP), false, false);
		int score = goS.globalSearch();
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testState_captured() {
		String[] text = new String[4];
		text[0] = new String("[W, _, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		log.setLevel(Level.DEBUG);
		StateUtil.printState(state, log);

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
				Constant.WHITE, 16, 15);
		goS.getGoBoard().printState(log);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);

		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(16, score);
	}

	/**
	 * expected result:
	 */
	public void testCandidate_1() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, W]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	// [INIT]B-->[2,2]W-->[3,3]B-->[2,3]W-->[3,2]B-->[2,1]W
	// -->[3,4]B-->[3,1]W-->[2,4]B-->[1,3](EXHAUST 1)
	public void testCandidate_1111() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	public void testCandidate_12() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[_, W, W, W]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	public void testCandidate_13() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	public void testCandidate_4() {
		String[] text = new String[4];
		text[0] = new String("[W, _, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, B, W]");
		text[3] = new String("[_, _, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can.getStep());
		}

	}

	// [INIT]B-->[2,2]W-->[3,3]B-->[2,3]W-->[3,2]B-->[2,1]W-->[3,1]B-->[3,4]W-->[1,3]B-->[1,2]W-->[4,4]B-->[1,4]
	// W-->[2,4]B-->[4,2]W-->[1,3]B-->[4,3]W-->[1,1]B-->[2,1]W-->[2,2]B-->[PAS]W-->[PAS](DB_PASS
	// -11)
	public void testCandidate_6() {
		String[] text = new String[4];
		text[0] = new String("[_, B, W, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[_, _, _, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	// [INIT]B-->[2,2]W-->[3,3]B-->[2,3]W-->[3,2]B-->[2,1]W-->[3,1]B-->[3,4]W-->[4,4]B-->[4,3]
	// W-->[1,2]B-->[1,3]W-->[2,4]B-->[4,2]W-->[4,4]B-->[1,1]W-->[4,1]B-->[4,2]W-->[PAS]B-->[1,4]W-->[1,2]B-->[2,3]W-->[2,2]B-->[1,1]W-->[1,3](EXHAUST
	// -10)
	public void testCandidate_7() {
		String[] text = new String[4];
		text[0] = new String("[_, W, B, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[_, _, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	// [INIT]B-->[2,2]W-->[3,3]B-->[2,3]W-->[3,2]B-->[2,1]W-->[2,4]
	// B-->[1,3]W-->[3,4]B-->[3,1]W-->[4,2]B-->[1,4]W-->[4,1]B-->[4,3]W-->[PAS]B-->[PAS](DB_PASS
	// 3)
	public void testCandidate_8() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, B]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[W, W, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		/**
		 * 后面的变化会导致很多重复的局面,所谓殊途同归的情况. 4*4约有3^16种局面.
		 */

		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	/**
	 * [INIT]B-->[2,2]W-->[3,3]B-->[2,3]W-->[3,2]B-->[2,1]W-->[3,1]B-->[3,4]W-->
	 * [4,4]B-->[1,3]W-->[2,4]B-->[4,3]W-->[1,4]B-->[3,4]W-->[2,4]B-->[1,1]W-->[
	 * 4,4]B-->[1,4]W-->[1,2]B-->[1,4]W-->[1,3]B-->[2,3]W-->[2,2]B-->[3,4]W-->[4
	 * ,2](FINAL -16)
	 */
	public void testCandidate_5() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[_, _, _, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	/**
	 * avoid big gifting
	 */
	public void testCandidate_2() {
		String[] text = new String[4];
		text[0] = new String("[W, _, B, B]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, W, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can.getStep());
		}

	}

	public void testCandidate_3() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, B]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, W, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can.getStep());
		}

		candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can.getStep());
		}

	}

	/**
	 * the state after capturing/clean up should be identified.
	 */
	public void testFinalState_4() {
		String[] text = new String[4];
		text[0] = new String("[W, _, B, B]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[B, W, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Step step = new Step(4, 4, 4, Constant.BLACK);
		ta.oneStepForward(step);
		boolean state_deadCleanedUp = ta.isFinalState_deadCleanedUp();

		if (log.isEnabledFor(Level.WARN))
			log.warn("state_deadCleanedUp=" + state_deadCleanedUp);

		boolean state_deadExist = ta.isFinalState_deadExist();

		if (log.isEnabledFor(Level.WARN))
			log.warn("state_deadExist=" + state_deadExist);

	}

	/**
	 * the state after capturing/clean up should be identified.
	 */
	public void testFinalState_41() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, W, B]");
		text[3] = new String("[B, W, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Point point = Point.getPoint(4, 4, 4);

		boolean dead = ta.isAlreadyDead_dynamic(point);
		if (log.isEnabledFor(Level.WARN))
			log.warn("dead=" + dead);
		assertFalse(dead);

		boolean live = ta.isStaticLive(point);
		if (log.isEnabledFor(Level.WARN))
			log.warn("live=" + live);
		assertFalse(live);

		Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		live = ta.isAlreadyLive_dynamic(point);
		if (log.isEnabledFor(Level.WARN))
			log.warn("dynamic live=" + live);
		assertTrue(live);

	}

}
