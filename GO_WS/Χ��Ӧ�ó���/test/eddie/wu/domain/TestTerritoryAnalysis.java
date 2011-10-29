package eddie.wu.domain;

import junit.framework.TestCase;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.SGFGoManual;
import go.FinalResult;
import go.TerritoryAnalysis;
/**
 * RE : 共235手 黑胜3目<br/>
 * FinalResult [black=120, white=132, shared=109, tiemu=8, whoWin=White, net=20]
 * @author wueddie-wym-wrz
 *
 */

public class TestTerritoryAnalysis extends TestCase {
	public void test() {
		String fileName = Constant.rootDir + "吴清源番棋263局/吴清源番棋002.SGF";

		GoManual manual = SGFGoManual.loadGoManual(fileName);
		
		GoBoard board = new GoBoard();
		for(Point step: manual.getSteps()){
			board.oneStepForward(step);
		}
		TerritoryAnalysis analysis = new TerritoryAnalysis(board.getBoardColorState().getMatrixState());
		FinalResult result = analysis.finalResult();
		System.out.println(result);
		
	}
}
