package eddie.wu.domain;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.domain.analy.ActionAnalysis;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.util.GBKToUTF8;

public class TestActionAnalysis extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	private static final String rootDir = "doc/围棋打谱软件/";

	public void test() {
		//GMDGoManual manual = new LoadGMDGoManual(rootDir).loadSingleGoManual();
		
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);
		GoBoard go = new GoBoard();
		List<Step> steps = manual.getSteps();
			
		for (Step step : steps) {
			String moveName = new ActionAnalysis(go.getBoardColorState()
					.getMatrixState()).moveName(step);
			if(log.isDebugEnabled()) log.debug(step + " : " + moveName);
			go.oneStepForward(step);
		}
//		
//		for (int i = 0; i < temp.length / 2; i++) {
//			Step step = new Step();
//			byte color;
//			if (i % 2 == 0) {
//				color = Constant.BLACK;
//			} else {
//				color = Constant.WHITE;
//			}
//			step.setColor(color);
//			Point point = Point.getPoint(temp[i * 2], temp[i * 2 + 1]);
//			step.setPoint(point);
//			String moveName = new ActionAnalysis(go.getBoardColorState()
//					.getMatrixState()).moveName(point, color);
//			if(log.isDebugEnabled()) log.debug(point + " " + moveName);
//			go.oneStepForward(step);
//		}
	}
}
