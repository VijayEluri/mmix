package eddie.wu.domain.sgf;

import junit.framework.Assert;
import junit.framework.TestCase;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Step;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.SGFGoManual;

public class TestEternalLife extends TestCase {
	public static final String fileName = Constant.rootDir + "围棋规则/长生劫.sgf";

	public void test(){
		GoBoardForward goB = new GoBoardForward(Constant.BOARD_SIZE);
		SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(fileName);
		for(Step step:goManual.getSteps()){
			boolean valid = goB.oneStepForward(step);
			if(valid==false){
				assertEquals(41, goB.getShoushu());
			}
		}
	}
}
