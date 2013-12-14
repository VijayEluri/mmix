package eddie.wu.domain.eye;

import junit.framework.TestCase;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;

public class TestBreathPattern extends TestCase {
	//C:\Users\Eddie\scm\mmix\GO_WS\围棋应用程序\doc\围棋程序数据\
	String inname = "doc/围棋程序数据/大眼基本死活/曲四一口外气.wjm";
	public void testBreathPattern() {
		byte[][] loadState = StateAnalysis.LoadState(inname);
		new GoBoard(loadState).getBigEyeBreathPattern(Point.getPoint(1, 17)).printPattern();
		
		inname = "doc/围棋程序数据/大眼基本死活/直三.wjm";
		loadState = StateAnalysis.LoadState(inname);
		new GoBoard(loadState).getBigEyeBreathPattern(Point.getPoint(1, 17)).printPattern();
		
	}
}
