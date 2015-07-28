package eddie.wu.search.global;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.manual.StateLoader;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.eye.BigEyeLiveSearch;
import eddie.wu.search.small.ThreeThreeBoardSearch;
/**
 * verified on 2015-06-22
 * @author think
 *
 */
public class TestBigEyeSearch_simple extends TestCase {
	private static Logger log = Logger.getLogger(TestBigEyeSearch_simple.class);

	static {
		Constant.INTERNAL_CHECK = false;
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j_error.xml";
		System.setProperty(key, value);
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoard.class).setLevel(Level.WARN);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.ERROR);
		Logger.getLogger(TestBigEyeSearch_simple.class).setLevel(Level.WARN);
		Logger.getLogger(BigEyeLiveSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.WARN);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.WARN);
	}

	public void testBent4() {

		String inname = "doc/围棋程序数据/大眼基本死活/中央曲四.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(2, 16))
				.printPattern();

		BigEyeLiveSearch search;
		// search = new BigEyeLiveSearch(state, Point.getPoint(2, 15),true);
		// search.globalSearch();
		int score;
		search = new BigEyeLiveSearch(state, Point.getPoint(2, 16), false,
				false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);

		search = new BigEyeLiveSearch(state, Point.getPoint(2, 16), true, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void test4_eb0() {
		String[] text = new String[5];
		text[0] = new String("[_, W, B, _, _]");
		text[1] = new String("[_, W, B, B, _]");
		text[2] = new String("[_, W, W, B, _]");
		text[3] = new String("[_, _, W, B, B]");
		text[4] = new String("[W, _, W, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(5, 2, 3))
				.printPattern();

		BigEyeLiveSearch goS;
		int score;
		// goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 3),
		// Constant.BLACK,
		// true, false);
		// score = search_internal(goS);
		// assertEquals(RelativeResult.ALREADY_LIVE, score);
		//
		// goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 3),
		// Constant.BLACK,
		// false, false);
		// score = search_internal(goS);
		// assertEquals(RelativeResult.ALREADY_DEAD, score);
		//
		goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 3), false, true);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

	}
	//不适合大眼搜索。
//	public void test6_eb23() {
//		Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
//		String[] text = new String[5];
//		text[0] = new String("[_, _, B, B, _]");
//		text[1] = new String("[W, W, B, W, B]");
//		text[2] = new String("[_, W, B, W, _]");
//		text[3] = new String("[W, W, B, B, B]");
//		text[4] = new String("[_, W, W, W, _]");
//		byte[][] state = StateLoader.LoadStateFromText(text);
//		BigEyeLiveSearch goS;
//		int score;
//		goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 3), false, false);
//		score = search_internal(goS);
//		assertEquals(RelativeResult.ALREADY_LIVE, score);
//	}

	public void test6_eb2() {
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		String[] text = new String[5];
		text[0] = new String("[_, _, B, _, _]");
		text[1] = new String("[W, W, B, _, _]");
		text[2] = new String("[_, W, B, _, _]");
		text[3] = new String("[W, W, B, B, B]");
		text[4] = new String("[_, W, W, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(5, 2, 3))
				.printPattern();

		BigEyeLiveSearch goS;
		int score;
		// goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 3),
		// Constant.BLACK,
		// true, false);
		// score = search_internal(goS);
		// assertEquals(RelativeResult.ALREADY_LIVE, score);

		goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 3), false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

	}

	public void testCornerBent4_externalbreath0() {

		String inname = "doc/围棋程序数据/大眼基本死活/曲四无外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeLiveSearch search;
		int score;
		search = new BigEyeLiveSearch(state, point, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_DEAD);

		search = new BigEyeLiveSearch(state, point, false, true);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_LIVE);
	}

	public void testCornerBent4_externalbreath1() {

		String inname = "doc/围棋程序数据/大眼基本死活/曲四一口外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeLiveSearch search;
		int score;
		search = new BigEyeLiveSearch(state, point, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeLiveSearch(state, point, false, true);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testCornerBent4_externalbreath2() {

		String inname = "doc/围棋程序数据/大眼基本死活/曲四口两外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeLiveSearch search;
		int score;
		search = new BigEyeLiveSearch(state, point, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

		// search = new BigEyeLiveSearch(state, point, false, true);
		// score = search.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		// assertEquals(BigEyeLiveSearch.LIVE, score);
	}

	public void testCrowd6() {

		String inname = "doc/围棋程序数据/大眼基本死活/梅花六.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		// new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(2, 15))
		// .printPattern();

		BigEyeLiveSearch search;
		// search = new BigEyeLiveSearch(state, Point.getPoint(2, 15),true);
		// search.globalSearch();
		search = new BigEyeLiveSearch(state, Point.getPoint(2, 15), false,
				false);
		int score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
	}

	public void test3_straight() {
		String[] text = new String[5];
		text[0] = new String("[B, B, B, B, B]");
		text[1] = new String("[W, B, _, B, _]");
		text[2] = new String("[W, W, B, B, B]");
		text[3] = new String("[W, W, W, W, W]");
		text[4] = new String("[_, _, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(5, 2, 1))
				.printPattern();
		System.out.println("Test Liveable?");
		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 1), true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);
		System.out.println(Constant.lineSeparator);
		System.out.println("Test Killable?");
		goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 1), false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_DEAD, score);
	}
	
	public void test2_straight() {
		String[] text = new String[5];
		text[0] = new String("[B, B, B, B, B]");
		text[1] = new String("[W, B, _, B, _]");
		text[2] = new String("[W, W, B, B, B]");
		text[3] = new String("[W, W, W, W, W]");
		text[4] = new String("[_, _, W, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(5, 2, 1))
				.printPattern();
		System.out.println("Test Liveable?");
		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 1), true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_DEAD, score);
		System.out.println(Constant.lineSeparator);
		System.out.println("Test Killable?");
		goS = new BigEyeLiveSearch(state, Point.getPoint(5, 2, 1), false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_DEAD, score);
	}


	public void testCrowd7() {
		String[] text = new String[7];
		text[0] = new String("[_, B, B, B, B, B, B]");
		text[1] = new String("[_, B, W, W, W, W, W]");
		text[2] = new String("[_, B, W, W, _, _, W]");
		text[3] = new String("[_, B, W, _, _, _, W]");
		text[4] = new String("[_, B, W, _, _, W, W]");
		text[5] = new String("[_, B, B, W, W, W, B]");
		text[6] = new String("[_, _, B, B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(7, 2, 7))
				.printPattern();

		BigEyeLiveSearch goS;
		// search = new BigEyeLiveSearch(state, Point.getPoint(2, 15),true);
		// search.globalSearch();
		goS = new BigEyeLiveSearch(state, Point.getPoint(7, 2, 7), false, false);
		int score = search_internal(goS);
		assertEquals(RelativeResult.DUAL_LIVE, score);
	}

	public int search_internal(BigEyeLiveSearch goS) {
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN)) {
			log.warn(goS.getGoBoard().getInitColorState().getStateString());
			log.warn("Score=" + score);
			log.warn("searched "
					+ (goS.getSearchProcess().size() - goS.dupCount));
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

			int initScore = manual.initScore();
			// assertEquals(score, initScore);
			log.warn("Init score = " + initScore);
			if (goS.isTargetFirst()) {
				if (score == RelativeResult.ALREADY_LIVE) {// success
					log.warn("after clean up fail node:");
//					manual.cleanupBadMove_firstWin(goS.getTargetColor(),
//							RelativeResult.ALREADY_LIVE);
					manual.cleanupBadMoveForWinner(true);
					log.debug(manual.getExpandedString(false));// init variant
					log.warn(manual.getSGFBodyString(false));
					// SGFGoManual.storeGoManual(fileName1, manual);
					// manual.blackWhiteSwitch();
					// SGFGoManual.storeGoManual(fileName3, manual);
				} else if (score < RelativeResult.ALREADY_LIVE) {// fail
					log.warn("after clean up fail node:");
//					manual.cleanupBadMove_firstLose(goS.getTargetColor(),
//							RelativeResult.ALREADY_LIVE);
					//TODO: check again! for colive
					manual.cleanupBadMoveForWinner(false);
					log.debug(manual.getExpandedString(false));// init variant
					log.warn(manual.getSGFBodyString(false));
					// SGFGoManual.storeGoManual(fileName2, manual);
					// manual.blackWhiteSwitch();
					// SGFGoManual.storeGoManual(fileName4, manual);
				}

			} else if (goS.isTargetFirst() == false) {
				if (score == RelativeResult.ALREADY_DEAD) {
					log.warn("after clean up fail node:");
//					manual.cleanupBadMove_firstWin(goS.getEnemyColor(),
//							RelativeResult.ALREADY_DEAD);
					manual.cleanupBadMoveForWinner(false);
					log.debug(manual.getExpandedString(false));// init variant
					log.warn(manual.getSGFBodyString(false));
					// SGFGoManual.storeGoManual(fileName1, manual);
					// manual.blackWhiteSwitch();
					// SGFGoManual.storeGoManual(fileName3, manual);

					// how many steps to kill?

					List<Step> list = manual.getRoot().getLongestBreathPath();
					log.warn("how many steps to kill? " + Step.getPasses(list));
					for (Step step : list) {
						log.warn(step);
					}
				} else {
					log.warn("after clean up fail node:");
//					manual.cleanupBadMove_firstLose(goS.getEnemyColor(),
//							RelativeResult.ALREADY_DEAD);
					manual.cleanupBadMoveForWinner(true);
					log.debug(manual.getExpandedString(false));// init variant
					log.warn(manual.getSGFBodyString(false));
					// SGFGoManual.storeGoManual(fileName2, manual);
					// manual.blackWhiteSwitch();
					// SGFGoManual.storeGoManual(fileName4, manual);
				}

			}

		}
		return score;
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
		survive.printState(log);
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

		String inname = "doc/围棋程序数据/大眼基本死活/板六_无外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeLiveSearch search;
		int score;
		search = new BigEyeLiveSearch(state, point, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_DEAD);

		search = new BigEyeLiveSearch(state, point, false, true);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_DEAD);
	}

	/**
	 * 缓一气劫。注意计算中可能出现相同的状态
	 */
	public void testCornerFlat6_externalbreath1() {

		String inname = "doc/围棋程序数据/大眼基本死活/板六_一口外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeLiveSearch search;
		int score;
		// search = new BigEyeLiveSearch(state, point, false,
		// false);
		// score = search.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		// assertEquals(score, BigEyeLiveSearch.DEAD);

		search = new BigEyeLiveSearch(state, point, false, true);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_LIVE);
	}

	public void testCornerFlat6_externalbreath2() {

		String inname = "doc/围棋程序数据/大眼基本死活/板六_两口外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeLiveSearch search;
		int score;
		search = new BigEyeLiveSearch(state, point, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_LIVE);

		// search = new BigEyeLiveSearch(state, point, false, true);
		// score = search.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		// assertEquals(score, BigEyeLiveSearch.LIVE);
	}

	public void test5_KinfeHandler() {

		String[] text = new String[4];
		text[0] = new String("[B, B, B, _]");
		text[1] = new String("[B, B, _, _]");
		text[2] = new String("[B, B, _, _]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, Point.getPoint(4, 2, 2), false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		// goS = new BigEyeLiveSearch(state, Point.getPoint(4, 2, 2),
		// Constant.BLACK,
		// true, false);
		// score = search_internal(goS);
		// assertEquals(RelativeResult.ALREADY_DEAD, score);
	}

	public void test4_Sqaure() {

		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, _, _]");
		text[2] = new String("[B, B, _, _]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, Point.getPoint(4, 2, 2), false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_DEAD, score);
	}

	/**
	 * ensure better move!
	 */
	public void test4_CornerSqaure() {

		String[] text = new String[4];
		text[0] = new String("[B, B, _, _]");
		text[1] = new String("[B, B, _, _]");
		text[2] = new String("[B, B, B, B]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, Point.getPoint(4, 2, 2), false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_DEAD, score);
	}

	/**
	 * TODO: support the case when the big eye is already filled in.
	 */
	public void testDualLive() {

		String[] text = new String[4];
		text[0] = new String("[W, W, W, W]");
		text[1] = new String("[B, _, B, _]");
		text[2] = new String("[B, B, B, B]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BigEyeLiveSearch goS;
		int score;
		Set<Point> eyePoint = new HashSet<Point>();
		Point point;
		point = Point.getPoint(4, 2, 2);
		eyePoint.add(point);
		point = Point.getPoint(4, 2, 4);
		eyePoint.add(point);
		point = Point.getPoint(4, 2, 1);
		goS = new BigEyeLiveSearch(state, point, eyePoint, false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.DUAL_LIVE, score);

		goS = new BigEyeLiveSearch(state, point, eyePoint, true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.DUAL_LIVE, score);

	}

	/**
	 * 盘角曲四，劫尽棋亡。
	 * 
	 */
	public void testDualLive_2() {

		String[] text = new String[4];
		text[0] = new String("[B, W, W, W]");
		text[1] = new String("[B, _, B, _]");
		text[2] = new String("[B, B, B, B]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BigEyeLiveSearch goS;
		int score;
		Set<Point> eyePoint = new HashSet<Point>();
		Point point;
		point = Point.getPoint(4, 2, 2);
		eyePoint.add(point);
		point = Point.getPoint(4, 2, 4);
		eyePoint.add(point);
		point = Point.getPoint(4, 2, 1);
		
		//目标方劫材不利,被杀.
		goS = new BigEyeLiveSearch(state, point, eyePoint, false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		//目标方劫材有利，也仅仅是双活。完全可待终局前后再提净.
		goS = new BigEyeLiveSearch(state, point, eyePoint, false, true);
		score = search_internal(goS);
		assertEquals(RelativeResult.DUAL_LIVE, score);
		// that means it depends on the loop threat.

		//目标方只能弃权,先走没有意义.
		goS = new BigEyeLiveSearch(state, point, eyePoint, true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_DEAD, score);
		goS = new BigEyeLiveSearch(state, point, eyePoint, true, true);
		score = search_internal(goS);
		assertEquals(RelativeResult.DUAL_LIVE, score);

		// so the result depends on the loop threat! it is whoseTurn
		// independent.
		// the state is decided, we can leave the play at the end of game.
		// so both side can clean up the loop threat before going. hence
		// the loop favorite is important here.

	}

	// public void testDualLive_Crowd7_can() {
	// String[] text = new String[6];
	// text[0] = new String("[_, B, B, B, B, B]");
	// text[1] = new String("[_, B, W, W, W, W]");
	// text[2] = new String("[_, B, W, W, W, _]");
	// text[3] = new String("[_, B, W, W, B, B]");
	// text[4] = new String("[_, B, W, _, B, W]");
	// text[5] = new String("[_, B, W, W, W, W]");
	// byte[][] state = StateLoader.LoadStateFromText(text);
	// GoBoard goB = new GoBoard(state);
	// Point target = Point.getPoint(6, 2, 3);
	// goB.getCandidate_forTarget(target,Constant.WHITE,can,);
	// }

	public void testDualLive_Crowd7() {
		String[] text = new String[6];
		text[0] = new String("[_, B, B, B, B, B]");
		text[1] = new String("[_, B, W, W, W, W]");
		text[2] = new String("[_, B, W, W, _, _]");
		text[3] = new String("[_, B, W, _, _, _]");
		text[4] = new String("[_, B, W, _, _, W]");
		text[5] = new String("[_, B, W, W, W, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		Point target = Point.getPoint(6, 2, 3);

		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, target, false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.DUAL_LIVE, score);

		// goS = new BigEyeLiveSearch(state, target, Constant.WHITE, true,
		// false);
		// score = search_internal(goS);
		// assertEquals(RelativeResult.ALREADY_LIVE, score);

		// 从死活的角度,先手已经没有价值.但是从官子的角度仍有价值.
	}

	public void testDuarlLirve_Crowd7_0() {
		String[] text = new String[6];
		text[0] = new String("[_, B, B, B, B, _]");
		text[1] = new String("[_, B, W, W, W, W]");
		text[2] = new String("[_, B, W, W, W, _]");
		text[3] = new String("[_, B, W, W, B, B]");
		text[4] = new String("[_, B, W, _, B, W]");
		text[5] = new String("[_, B, W, W, W, W]");
		Point target = Point.getPoint(6, 2, 3);

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard goB = new TerritoryAnalysis(state, Constant.WHITE);

		Set<Point> eyePoints = new HashSet<Point>();
		Point point = Point.getPoint(6, 3, 6);
		eyePoints.add(point);
		point = Point.getPoint(6, 5, 4);
		eyePoints.add(point);
		boolean isDualLivePotential = goB
				.isDualLivePotential(target, eyePoints);
		System.out.println("isDualLivePotential = " + isDualLivePotential);
		assertTrue(isDualLivePotential);

	}

	public void testDuarlLive_Crowd7r_1() {
		String[] text = new String[6];
		text[0] = new String("[_, B, B, B, B, B]");
		text[1] = new String("[_, B, W, W, W, W]");
		text[2] = new String("[_, B, W, W, W, _]");
		text[3] = new String("[_, B, W, W, B, B]");
		text[4] = new String("[_, B, W, _, B, W]");
		text[5] = new String("[_, B, W, W, W, W]");
		Point target = Point.getPoint(6, 2, 3);

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard goB = new TerritoryAnalysis(state, Constant.WHITE);

		Set<Point> eyePoints = new HashSet<Point>();
		Point point = Point.getPoint(6, 3, 6);
		eyePoints.add(point);
		point = Point.getPoint(6, 5, 4);
		eyePoints.add(point);
		boolean isDualLivePotential = goB
				.isDualLivePotential(target, eyePoints);
		System.out.println("isDualLivePotential = " + isDualLivePotential);

		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, target, eyePoints, true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.DUAL_LIVE, score);

		// goS = new BigEyeLiveSearch(state, target, Constant.WHITE, eyePoints,
		// false, false);
		// score = search_internal(goS);
		// assertEquals(RelativeResult.DUAL_LIVE, score);

		// 从死活的角度,先手已经没有价值.但是从官子的角度仍有价值.
	}

	public void testDuarlLive_Crowrd7r_3() {
		String[] text = new String[6];
		text[0] = new String("[_, B, B, B, B, _]");
		text[1] = new String("[_, B, W, W, W, W]");
		text[2] = new String("[_, B, W, W, W, _]");
		text[3] = new String("[_, B, W, W, B, B]");
		text[4] = new String("[_, B, W, _, B, W]");
		text[5] = new String("[_, B, W, W, W, W]");
		Point target = Point.getPoint(6, 2, 3);

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard goB = new TerritoryAnalysis(state, Constant.WHITE);

		Set<Point> eyePoints = new HashSet<Point>();
		Point point = Point.getPoint(6, 3, 6);
		eyePoints.add(point);
		point = Point.getPoint(6, 5, 4);
		eyePoints.add(point);
		boolean isDualLivePotential = goB
				.isDualLivePotential(target, eyePoints);
		System.out.println("isDualLivePotential = " + isDualLivePotential);
		assertTrue(isDualLivePotential);

		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, target, eyePoints, true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.DUAL_LIVE, score);

		// goS = new BigEyeLiveSearch(state, target, Constant.WHITE, eyePoints,
		// false, false);
		// score = search_internal(goS);
		// assertEquals(RelativeResult.DUAL_LIVE, score);

		// 从死活的角度,先手已经没有价值.但是从官子的角度仍有价值.
	}

	public void testProblem122() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, _, B, B]");
		text[3] = new String("[B, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		Point target = Point.getPoint(6, 2, 3);
		Set<Point> eyePoints = new HashSet<Point>();
		GoBoard goB = new TerritoryAnalysis(state, Constant.WHITE);
		eyePoints.addAll(goB.getBlock(target).getUniqueEyeBlock().getPoints());

		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, target, eyePoints, true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

		goS = new BigEyeLiveSearch(state, target, eyePoints, false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

	}

	public void testProblem123() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, B, B, B]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		Point target = Point.getPoint(6, 2, 3);
		Set<Point> eyePoints = new HashSet<Point>();
		GoBoard goB = new TerritoryAnalysis(state, Constant.WHITE);
		eyePoints.addAll(goB.getBlock(target).getUniqueEyeBlock().getPoints());

		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, target, eyePoints, true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

		goS = new BigEyeLiveSearch(state, target, eyePoints, false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

	}

	public void testProblem124() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, B, _, B]");
		text[3] = new String("[B, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		Point target = Point.getPoint(6, 2, 3);
		Set<Point> eyePoints = new HashSet<Point>();
		GoBoard goB = new TerritoryAnalysis(state, Constant.WHITE);
		eyePoints.addAll(goB.getBlock(target).getUniqueEyeBlock().getPoints());

		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, target, eyePoints, true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

		goS = new BigEyeLiveSearch(state, target, eyePoints, false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

	}

	public void testProblem125() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, _, _, B]");
		text[3] = new String("[B, B, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		Point target = Point.getPoint(6, 2, 3);
		Set<Point> eyePoints = new HashSet<Point>();
		GoBoard goB = new TerritoryAnalysis(state, Constant.WHITE);
		eyePoints.addAll(goB.getBlock(target).getUniqueEyeBlock().getPoints());

		BigEyeLiveSearch goS;
		int score;
		goS = new BigEyeLiveSearch(state, target, eyePoints, true, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

		goS = new BigEyeLiveSearch(state, target, eyePoints, false, false);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

	}

	public void testProblem126() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, B]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, B, _, _]");
		text[3] = new String("[B, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		Point target = Point.getPoint(6, 2, 3);
		Set<Point> eyePoints = new HashSet<Point>();
		GoBoard goB = new TerritoryAnalysis(state, Constant.WHITE);
		eyePoints.addAll(goB.getBlock(target).getUniqueEyeBlock().getPoints());

		BigEyeLiveSearch goS;
		int score;
		// goS = new BigEyeLiveSearch(state, target, eyePoints, true, false);
		// score = search_internal(goS);
		// assertEquals(RelativeResult.ALREADY_LIVE, score);

		// goS = new BigEyeLiveSearch(state, target, eyePoints, false, false);
		// score = search_internal(goS);
		// assertEquals(RelativeResult.ALREADY_DEAD, score);

		goS = new BigEyeLiveSearch(state, target, eyePoints, false, true);
		score = search_internal(goS);
		assertEquals(RelativeResult.ALREADY_LIVE, score);

	}

}
