package eddie.wu.domain;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.analy.BothLiveAnalysis;
import eddie.wu.domain.analy.StateAnalysis;
import junit.framework.TestCase;

/**
 * may be "LiveLive" is a better name than ShuangHuo is.
 * 
 * three block in test file: black: (3,13); (19,2); (19,16) white: (3,15);
 * (18,1) and (18,3); (16,18)
 */
public class TestShuangHuo extends TestCase {
	private String root = "doc/围棋程序数据/双活/";
	private String name1 = "基本双活1.wjm";
	private String name = "基本双活.wjm";

	private static final Logger log = Logger.getLogger(BothLiveAnalysis.class);
	static {
		log.setLevel(Level.INFO);
	}

	public void test1() {
		byte[][] state = StateAnalysis.LoadState(root + name1);
		BothLiveAnalysis bl = new BothLiveAnalysis(state);
		bl.printState(log);
		assertTrue(bl.isBothLive(Point.getPoint(Constant.BOARD_SIZE, 3, 14)) > 0);
		assertTrue(bl.isBothLive(Point.getPoint(Constant.BOARD_SIZE, 3, 15)) > 0);

	}

	public void test2() {
		String[] text = new String[5];
		text[0] = new String("[W, B, _, W, B]");
		text[1] = new String("[W, B, _, W, B]");
		text[2] = new String("[W, B, B, W, B]");
		text[3] = new String("[W, W, W, B, B]");
		text[4] = new String("[_, W, W, B, _]");
		
		byte[][] state = StateAnalysis.LoadState(root + name);
		BothLiveAnalysis bl = new BothLiveAnalysis(state);
		bl.printState(log);
		assertTrue(bl.isBothLive(Point.getPoint(Constant.BOARD_SIZE, 18, 1)) > 0);
		// assertTrue(bl.isBothLive(Point.getPoint(Constant.BOARD_SIZE,18, 2)) >
		// 0);
		// assertTrue(bl.isBothLive(Point.getPoint(Constant.BOARD_SIZE,18, 3)) >
		// 0);

	}

	public void test3() {
		byte[][] state = StateAnalysis.LoadState(root + name);
		BothLiveAnalysis bl = new BothLiveAnalysis(state);
		bl.printState(log);
		assertTrue(bl.isBothLive(Point.getPoint(Constant.BOARD_SIZE, 18, 16)) > 0);
		assertTrue(bl.isBothLive(Point.getPoint(Constant.BOARD_SIZE, 18, 17)) > 0);

	}
}
