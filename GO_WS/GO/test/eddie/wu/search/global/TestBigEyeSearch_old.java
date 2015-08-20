package eddie.wu.search.global;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.manual.StateLoader;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.eye.BigEyeSearch;
import eddie.wu.search.small.ThreeThreeBoardSearch;
import eddie.wu.search.three.TestAllState3;

public class TestBigEyeSearch_old extends TestCase {
	private static Logger log = Logger.getLogger(TestBigEyeSearch_old.class);

	static {
		Constant.INTERNAL_CHECK = false;
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j_error.xml";
		System.setProperty(key, value);
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.ERROR);
		Logger.getLogger(TestBigEyeSearch_old.class).setLevel(Level.WARN);
		Logger.getLogger(BigEyeSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.WARN);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.WARN);
	}

	public void testBent4() {

		String[] text = new String[4];
		text[0] = new String("[B, B, _, _]");
		text[1] = new String("[B, B, _, B]");
		text[2] = new String("[B, B, _, B]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		Point target  = Point.getPoint(4,2, 2);

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, target, Constant.BLACK,
				false, false);
		score = search.globalSearch();
		assertEquals(RelativeResult.ALREADY_LIVE, score);
		if (log.isEnabledFor(Level.WARN)){
			log.warn("score=" + score);
		}
		search = new BigEyeSearch(state, target, Constant.BLACK,
				true, false);
		score = search.globalSearch();
		assertEquals(RelativeResult.ALREADY_LIVE, score);
		if (log.isEnabledFor(Level.WARN)){
			log.warn("score=" + score);
		}
	}

	public void testCornerBent4_externalbreath0() {

		String[] text = new String[4];
		text[0] = new String("[B, B, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, B, B, _]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		
		Point point = Point.getPoint(4,1, 2);

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, Constant.BLACK, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_DEAD);

		search = new BigEyeSearch(state, point, Constant.BLACK, false, true);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_LIVE);
	}

	public void testCornerBent4_externalbreath1() {
		String[] text = new String[5];
		text[0] = new String("[_, W, B, _, _]");
		text[1] = new String("[_, W, B, B, _]");
		text[2] = new String("[_, W, B, B, _]");
		text[3] = new String("[_, W, B, B, B]");
		text[4] = new String("[_, W, _, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		
		Point point = Point.getPoint(4,1, 3);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, Constant.BLACK, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, Constant.BLACK, false, true);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testCornerBent4_externalbreath2() {
		String[] text = new String[5];
		text[0] = new String("[_, W, B, _, _]");
		text[1] = new String("[_, W, B, B, _]");
		text[2] = new String("[_, W, _, B, _]");
		text[3] = new String("[_, W, B, B, B]");
		text[4] = new String("[_, W, _, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		
		Point point = Point.getPoint(4,1, 3);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch  search;
		int score;
		search = new BigEyeSearch(state, point, Constant.BLACK, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

	}

	public void testCrowd6() {
		  String[] text = new String[6];
			text[0] = new String("[_, B, B, B, B, B]");
			text[1] = new String("[_, B, W, W, W, W]");
			text[2] = new String("[_, B, W, W, _, _]");
			text[3] = new String("[_, B, W, _, _, _]");
			text[4] = new String("[_, B, W, W, _, W]");
		 	text[5] = new String("[_, B, W, W, W, W]");
			byte[][] state = StateLoader.LoadStateFromText(text);
		BigEyeSearch search;
		search = new BigEyeSearch(state, Point.getPoint(4,2, 3), Constant.WHITE,
				false, false);
		int score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
	}

	public void testCrowd7() {
		//String inname = "doc/围棋程序数据/大眼基本死活/葡萄七.wjm";
		String[] text = new String[6];
		text[0] = new String("[_, B, B, B, B, B]");
		text[1] = new String("[_, B, W, W, W, W]");
		text[2] = new String("[_, B, W, W, _, _]");
		text[3] = new String("[_, B, W, _, _, _]");
		text[4] = new String("[_, B, W, _, _, W]");
		text[5] = new String("[_, B, W, W, W, W]");
		
		byte[][] state = StateLoader.LoadStateFromText(text);

		Point target  = Point.getPoint(4,2, 3);

		BigEyeSearch goS;
		goS = new BigEyeSearch(state, target, Constant.WHITE,
				false, false);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN)) {
			log.warn(goS.getGoBoard().getInitColorState().getStateString());
			log.warn("Score=" + score);
			log.warn("searched "
					+ goS.getSearchProcess().size());
			// goS.outputSearchStatistics(log);
			TreeGoManual manual = goS.getTreeGoManual();
			// System.out.println(manual.getInitState());
			log.debug(manual.getExpandedString(false));
			log.warn("Most Expensive path: ");
			log.warn(Constant.lineSeparator + manual.getMostExpPath());
			log.warn(manual.getSGFBodyString(false));
			for (String list : goS.getSearchProcess()) {
				log.warn(list);
			}
		}
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	/**
	 * [B, _, B]<br/>
	 * [B, B, W]<br/>
	 * [W, B, _]<br/>
	 * [W, B, B]<br/>
	 */
	public void testBent4InCorner() {
		String[] text = new String[4];
		text[0] = new String("[B, B, _, B]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, B, B, _]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.printState();
		if (log.isEnabledFor(Level.WARN))
			log.warn(survive.getStateString());
		// survive.generateBlockInfo();
		Point target = survive.getPoint(1, 2);
		// boolean dead = survive.isDead(target);
		// if(log.isEnabledFor(Level.WARN)) log.warn("dead=" + dead);
		// assertFalse(dead);
		boolean live = survive.isAlreadyLive_dynamic(target);
		if (log.isEnabledFor(Level.WARN))
			log.warn("live=" + live);
		assertFalse(live);

		// boolean dead = survive.isAlreadyDead_dynamic(target);
		// if(log.isEnabledFor(Level.WARN)) log.warn("dead=" + dead);
		// assertFalse(dead);
	}

	public void testFinalState13B11() {
		String[] text = new String[4];
		text[0] = new String("[B, B, W, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, B, B, _]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Logger.getLogger(TerritoryAnalysis.class).setLevel(Level.WARN);

		Point point = Point.getPoint(4, 2, 1);
		boolean live = ta.isAlreadyLive_dynamic(point);
		assertFalse(live);

		boolean dead = ta.isAlreadyDead_dynamic(point);
		assertFalse(dead);

		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertFalse(finalState_deadExist);

	}

	/**
	 * ##01,02,03,04,05 <br/>
	 * 01[_, _, _, B, _]01<br/>
	 * 02[_, W, W, B, _]02<br/> 
	 * 03[_, W, B, B, _]03<br/>
	 * 04[_, W, _, B, _]04<br/>
	 * 05[_, W, _, B, _]05<br/>
	 * ##01,02,03,04,05 <br/>
	 */
	public void test5_bigeye() {
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

	}

	public void testCornerFlat6_externalbreath0() {
		String[] text = new String[5];
		text[0] = new String("[_, W, B, _, _]");
		text[1] = new String("[_, W, B, _, _]");
		text[2] = new String("[_, W, B, _, _]");
		text[3] = new String("[_, W, B, B, B]");
		text[4] = new String("[_, W, W, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		Point point = Point.getPoint(4,1, 3);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, Constant.BLACK, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_DEAD);

		search = new BigEyeSearch(state, point, Constant.BLACK, false, true);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_DEAD);

		for (String list : search.getSearchProcess()) {
			log.warn(list);
		}
	}

	/**
	 * 缓一气劫。注意计算中可能出现相同的状态
	 */
	public void testCornerFlat6_externalbreath1() {
		String[] text = new String[5];
		text[0] = new String("[_, W, B, _, _]");
		text[1] = new String("[W, W, B, _, _]");
		text[2] = new String("[W, _, B, _, _]");
		text[3] = new String("[W, W, B, B, B]");
		text[4] = new String("[_, W, W, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		Point point = Point.getPoint(4,1, 3);
		BigEyeSearch search;
		int score;

		search = new BigEyeSearch(state, point, Constant.BLACK, false, true);
		score = search.globalSearch();
		assertEquals(RelativeResult.ALREADY_LIVE, score);
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("score=" + score);
			for (String list : search.getSearchProcess()) {
				log.warn(list);
			}
		}

		search = new BigEyeSearch(state, point, Constant.BLACK, false, false);
		score = search.globalSearch();
		assertEquals(RelativeResult.ALREADY_DEAD, score);
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("score=" + score);
			for (String list : search.getSearchProcess()) {
				log.warn(list);
			}
		}
	}

	public void testCornerFlat6_externalbreath2() {

		String[] text = new String[5];
		text[0] = new String("[_, W, B, _, _]");
		text[1] = new String("[W, W, B, _, _]");
		text[2] = new String("[_, _, B, _, _]");
		text[3] = new String("[W, W, B, B, B]");
		text[4] = new String("[_, W, W, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		Point point = Point.getPoint(4,1, 3);
		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, Constant.BLACK, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_LIVE);
		for (String list : search.getSearchProcess()) {
			log.warn(list);
		}
	}

}
