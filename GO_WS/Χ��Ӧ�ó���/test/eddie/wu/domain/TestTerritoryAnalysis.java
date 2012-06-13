package eddie.wu.domain;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import util.GBKToUTF8;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.SGFGoManual;

/**
 * RE : 共235手 黑胜3目<br/>
 * FinalResult [black=120, white=132, shared=109, tiemu=8, whoWin=White, net=20]
 * 
 * @author wueddie-wym-wrz
 * 
 */

public class TestTerritoryAnalysis extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);

	public void test() {
		// String fileName = Constant.rootDir + "吴清源番棋263局/吴清源番棋002.SGF";

		GoManual manual = SGFGoManual.loadGoManual(Constant.currentManual);

		GoBoard board = new GoBoard();
		for (Step step : manual.getSteps()) {
			board.oneStepForward(step);
		}
		TerritoryAnalysis analysis = new TerritoryAnalysis(board
				.getBoardColorState().getMatrixState());
		FinalResult result = analysis.finalResult();
		if (log.isDebugEnabled())
			log.debug(result);

	}
}
