package eddie.wu.search.global;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;
import eddie.wu.domain.Block;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.manual.StateLoader;

public class TestBigEyeSearch extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);
	public void testBent4() {

		String inname = "doc/围棋程序数据/大眼基本死活/中央曲四.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(2, 16))
				.printPattern();

		BigEyeSearch search;
		// search = new BigEyeSearch(state, Point.getPoint(2, 15),true);
		// search.globalSearch();
		int score;
		search = new BigEyeSearch(state, Point.getPoint(2, 16), false, false);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		this.assertEquals(RelativeResult.ALREADY_LIVE, score);

		search = new BigEyeSearch(state, Point.getPoint(2, 16), true, false);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		this.assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testCornerBent4_externalbreath0() {

		String inname = "doc/围棋程序数据/大眼基本死活/曲四无外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, false, false);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		this.assertEquals(score, RelativeResult.ALREADY_DEAD);

		search = new BigEyeSearch(state, point, false, true);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		this.assertEquals(score, RelativeResult.ALREADY_LIVE);
	}

	public void testCornerBent4_externalbreath1() {

		String inname = "doc/围棋程序数据/大眼基本死活/曲四一口外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, false, false);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		this.assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, false, true);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		this.assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testCornerBent4_externalbreath2() {

		String inname = "doc/围棋程序数据/大眼基本死活/曲四口两外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, false, false);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		this.assertEquals(RelativeResult.ALREADY_LIVE, score);

		// search = new BigEyeSearch(state, point, false, true);
		// score = search.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		// this.assertEquals(BigEyeSearch.LIVE, score);
	}

	public void testCrowd6() {

		String inname = "doc/围棋程序数据/大眼基本死活/梅花六.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		// new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(2, 15))
		// .printPattern();

		BigEyeSearch search;
		// search = new BigEyeSearch(state, Point.getPoint(2, 15),true);
		// search.globalSearch();
		search = new BigEyeSearch(state, Point.getPoint(2, 15), false, false);
		int score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
	}

	public void testCrowd7() {
		String inname = "doc/围棋程序数据/大眼基本死活/葡萄七.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(2, 15))
				.printPattern();

		BigEyeSearch search;
		// search = new BigEyeSearch(state, Point.getPoint(2, 15),true);
		// search.globalSearch();
		search = new BigEyeSearch(state, Point.getPoint(2, 15), false, false);
		int score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		this.assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	/**
	 * [B, _, B]<br/>
	 * [B, B, W]<br/>
	 * [W, B, _]<br/>
	 * [W, B, B]<br/>
	 */
	public void testBent4InCorner() {
		String inname = "doc/围棋程序数据/大眼基本死活/曲四无外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);

		inname = "doc/围棋程序数据/大眼基本死活/角上曲四打劫状态.txt";
		state = StateLoader.LoadStateFromTextFile(inname);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.printState();
		if(log.isEnabledFor(Level.WARN)) log.warn(survive.getStateString());
		// survive.generateBlockInfo();
		Point target = survive.getPoint(1, 2);
//		boolean dead = survive.isDead(target);
//		if(log.isEnabledFor(Level.WARN)) log.warn("dead=" + dead);
//		assertFalse(dead);
		boolean live = survive.isAlreadyLive_dynamic(target);
		if(log.isEnabledFor(Level.WARN)) log.warn("live=" + live);
		assertFalse(live);
		
		
		
//		boolean dead = survive.isAlreadyDead_dynamic(target);
//		if(log.isEnabledFor(Level.WARN)) log.warn("dead=" + dead);
//		assertFalse(dead);
	}
	
	
	public void testFinalState13B() {
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
		Assert.assertFalse(live);
		
		boolean dead = ta.isAlreadyDead_dynamic(point);
		Assert.assertFalse(dead);
		
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		Assert.assertFalse(finalState_deadExist);
		
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
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(5, 1, 4);
		assertTrue(sa.isAlreadyLive_dynamic(point));

	}

	public void testCornerFlat6_externalbreath0() {

		String inname = "doc/围棋程序数据/大眼基本死活/板六_无外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, false, false);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_DEAD);

		search = new BigEyeSearch(state, point, false, true);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
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

		BigEyeSearch search;
		int score;
		// search = new BigEyeSearch(state, point, false, false);
		// score = search.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		// assertEquals(score, BigEyeSearch.DEAD);

		search = new BigEyeSearch(state, point, false, true);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_LIVE);
	}

	public void testCornerFlat6_externalbreath2() {

		String inname = "doc/围棋程序数据/大眼基本死活/板六_两口外气.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, false, false);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_LIVE);

		// search = new BigEyeSearch(state, point, false, true);
		// score = search.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		// assertEquals(score, BigEyeSearch.LIVE);
	}

}
