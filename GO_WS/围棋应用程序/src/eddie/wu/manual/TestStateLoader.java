package eddie.wu.manual;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
/**
 * Color orange 	= new Color(255, 200, 0);
 * @author Eddie
 *
 */
public class TestStateLoader extends TestCase{
	private static Logger log = Logger.getLogger(Block.class);
	public void test() {
		String inname = Constant.rootDir
				+ "../围棋程序数据/围棋规则/打三还一/Special State.txt";
		byte[][] stateFromTextFile = StateLoader.LoadStateFromTextFile(inname);
		BoardColorState state = BoardColorState.getInstance(stateFromTextFile,
				Constant.BLACK);
		if(log.isEnabledFor(Level.WARN)) log.warn(state.getStateString());
		
		inname = inname+".ps";
		int boardSize = state.boardSize;
		
		
	}

}
