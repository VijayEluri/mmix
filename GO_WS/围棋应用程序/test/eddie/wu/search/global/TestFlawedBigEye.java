package eddie.wu.search.global;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.survive.RelativeResult;

public class TestFlawedBigEye extends TestCase {
	private static Logger log = Logger.getLogger(TestFlawedBigEye.class);
	static{
		log.setLevel(Level.WARN);
		Logger.getLogger(BigEyeSearch.class).setLevel(Level.INFO);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
	}

	public void testFlawedBent4() {
		// C:\Users\Eddie\scm\mmix\GO_WS\围棋应用程序\doc\围棋程序数据\有缺陷的大眼

		String inname = "doc/围棋程序数据/有缺陷的大眼/断头曲四.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(16, 16);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, Constant.BLACK, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, Constant.BLACK, true, false);
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
		search = new BigEyeSearch(state, point, Constant.BLACK, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, Constant.BLACK, true, false);
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
		search = new BigEyeSearch(state, point, Constant.BLACK, false, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, Constant.BLACK, true, false);
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
		GoBoard goBoard = new GoBoard(state);
		goBoard.getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
//		search = new BigEyeSearch(state, point, Constant.WHITE, false, false);
//		score = search.globalSearch();
//		if (log.isEnabledFor(Level.WARN))
//			log.warn("score=" + score);
//		assertEquals(RelativeResult.ALREADY_DEAD, score);

		search = new BigEyeSearch(state, point, Constant.WHITE, true, false);
		score = search.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		
		int count = 0;
		for (String list : search.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
		}
		
		assertEquals(RelativeResult.ALREADY_LIVE, score);
	}
}
