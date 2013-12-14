package eddie.wu.search.global;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.survive.RelativeResult;

public class TestFlawedBigEye extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);

	public void testFlawedBent4() {
		// C:\Users\Eddie\scm\mmix\GO_WS\围棋应用程序\doc\围棋程序数据\有缺陷的大眼

		String inname = "doc/围棋程序数据/有缺陷的大眼/断头曲四.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(16, 16);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, true, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testFlawedBent4_2() {
		// C:\Users\Eddie\scm\mmix\GO_WS\围棋应用程序\doc\围棋程序数据\有缺陷的大眼

		String inname = "doc/围棋程序数据/有缺陷的大眼/断头曲四2.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(16, 16);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, true, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testFlawedFlat6() {
		// C:\Users\Eddie\scm\mmix\GO_WS\围棋应用程序\doc\围棋程序数据\有缺陷的大眼

		String inname = "doc/围棋程序数据/有缺陷的大眼/断头板六.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(16, 16);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, true, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}

	public void testFlawedFlat6_2() {
		// C:\Users\Eddie\scm\mmix\GO_WS\围棋应用程序\doc\围棋程序数据\有缺陷的大眼

		String inname = "doc/围棋程序数据/有缺陷的大眼/断头板六2.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(16, 16);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, true, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}
}
