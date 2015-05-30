package eddie.wu.search.global;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.survive.RelativeResult;

public class TestSmallEye extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);
	public void testStraightThree() {
		String inname = "doc/围棋程序数据/大眼基本死活/直三.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		Point point = Point.getPoint(1, 17);
		new GoBoard(state).getBigEyeBreathPattern(point).printPattern();

		BigEyeSearch search;
		int score;
		search = new BigEyeSearch(state, point, Constant.BLACK,false, false);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_DEAD);

		search = new BigEyeSearch(state, point, Constant.BLACK,true, false);
		score = search.globalSearch();
		if(log.isEnabledFor(Level.WARN)) log.warn("score=" + score);
		assertEquals(score, RelativeResult.ALREADY_LIVE);
	}

}
