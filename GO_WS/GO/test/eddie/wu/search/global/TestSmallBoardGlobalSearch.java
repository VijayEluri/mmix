package eddie.wu.search.global;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.search.small.SmallBoardGlobalSearch;

public class TestSmallBoardGlobalSearch extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);

	/**
	 * 围棋的规则很重要，从二路棋盘的变化即可深刻体会到这一点。<br/>
	 * 如果简单的同型禁现，那么双方必须弃权才能打成平手 如果试图落子，反而要输。<br/>
	 * (以上说法不正确, 即使严格的同型禁现, 2*2 棋盘上先手方下实着仍然可以赢一目!)<br/>
	 * 似乎偶数路的小棋盘容易平局。tie
	 */
	public void test2() {
		GoBoardSearch goS = new SmallBoardGlobalSearch(2, 4, -4);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);
		assertEquals(0, score);
	}

	/**
	 * 1.6s net = 9
	 */
	public void test3() {
		GoBoardSearch goS = new SmallBoardGlobalSearch(3, 9, -9);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);
		assertEquals(9, score);
	}

	/**
	 * 
	 */
	public void test4() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);
		Logger.getLogger(SmallBoardGlobalSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(GoBoard.class).setLevel(Level.DEBUG);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.DEBUG);
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		GoBoardSearch goS = new SmallBoardGlobalSearch(4, 16, -16);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);
		assertEquals(0, score);
	}

	/**
	 * 
	 */
	public void test5() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);
		Logger.getLogger(SmallBoardGlobalSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(GoBoard.class).setLevel(Level.DEBUG);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.DEBUG);
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		GoBoardSearch goS = new SmallBoardGlobalSearch(5, 25, -25);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);
		assertEquals(0, score);
	}

}
