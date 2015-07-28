package eddie.wu.linkedblock;

import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.util.GBKToUTF8;

public class TestAnalysis extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	
	private String inname = "doc/围棋程序数据/死活题局面/围棋天地2003.22-P61-11.wjm";
	byte[][] state;

	public void test() {
		state = StateAnalysis.LoadState(inname);
		BoardColorState bps = new BoardColorState(state,Constant.BLACK);
		Set<Point> blacks = bps.getBlackPoints();
		if(log.isDebugEnabled()) log.debug(blacks);
		
//		GoBoard go = new GoBoard(state);
//		Set<Block> black = go.getSetFromState(ColorUtil.BLACK);
//		for(Block block :black){
//			if(log.isDebugEnabled()) log.debug(block.toString());
//		}
		
	}

}
