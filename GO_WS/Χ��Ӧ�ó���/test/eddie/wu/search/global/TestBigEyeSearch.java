package eddie.wu.search.global;

import junit.framework.TestCase;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;

public class TestBigEyeSearch extends TestCase{
	public void testBent4() {
		
		String inname = "doc/围棋程序数据/大眼基本死活/梅花六.wjm";
		byte[][] state = StateAnalysis.LoadState(inname);
		new GoBoard(state).getBigEyeBreathPattern(Point.getPoint(2, 15)).printPattern();
		
		
		BigEyeSearch search = new BigEyeSearch(state, Point.getPoint(2, 15),true);
		search.globalSearch();
	}
}
