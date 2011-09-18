package eddie.wu.domain;

import go.BothLive;
import go.StateAnalysis;
import junit.framework.TestCase;

/**
 * may be "LiveLive" is a better name than ShuangHuo is.
 * 
 * three block in test file: black: (3,13); (19,2); (19,16) white: (3,15);
 * (18,1) and (18,3); (16,18)
 */
public class TestShuangHuo extends TestCase {
	private String root = "doc/Χ���������/˫��/";
	private String name1 = "����˫��1.wjm";
	private String name = "����˫��.wjm";

	public void test1() {
		byte[][] state = StateAnalysis.LoadState(root + name1);
		BothLive bl = new BothLive(state);
		assertTrue(bl.isBothLive(Point.getPoint(3, 13)) > 0);

	}
	public void test() {
		byte[][] state = StateAnalysis.LoadState(root + name);
		BothLive bl = new BothLive(state);
		assertTrue(bl.isBothLive(Point.getPoint(18, 2)) > 0);

	}
}
