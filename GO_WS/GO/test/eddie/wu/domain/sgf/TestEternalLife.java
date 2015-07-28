package eddie.wu.domain.sgf;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SimpleGoManual;
/**
 * 长生劫的测试
 * @author think
 *
 */
public class TestEternalLife extends TestCase {
	public static final String fileName = Constant.rootDir + "围棋规则/长生劫.sgf";
	private static final Logger log = Logger.getLogger(TestEternalLife.class);
	static{
		log.setLevel(Level.INFO);
	}
	public void test(){
		GoBoardForward goB = new GoBoardForward(Constant.BOARD_SIZE);
		SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(fileName);
		log.warn(goManual.toString());
	
		
		for(Step step:goManual.getSteps()){
			log.warn("step= "+ step);
			boolean valid = goB.oneStepForward(step);
			goB.printState(log);
			if(valid==false){
				assertEquals(41, goB.getShoushu());
			}
		}
	}
}
