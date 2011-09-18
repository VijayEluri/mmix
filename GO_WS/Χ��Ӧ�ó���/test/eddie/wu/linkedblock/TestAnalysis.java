package eddie.wu.linkedblock;

import eddie.wu.domain.Point;
import go.StateAnalysis;

import java.util.Set;

import junit.framework.TestCase;

public class TestAnalysis extends TestCase {
	private String inname = "doc/围棋程序数据/死活题局面/围棋天地2003.22-P61-11.wjm";
	byte[][] state;

	public void test() {
		state = StateAnalysis.LoadState(inname);
		BoardColorState bps = new BoardColorState(state);
		Set<Point> blacks = bps.getBlackPoints();
		System.out.println(blacks);
		
//		GoBoard go = new GoBoard(state);
//		Set<Block> black = go.getSetFromState(ColorUtil.BLACK);
//		for(Block block :black){
//			System.out.println(block.toString());
//		}
		
	}

}
