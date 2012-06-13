package eddie.wu.ui;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.manual.GMDGoManual;
import eddie.wu.manual.LoadGMDGoManual;

public class TestShowManual extends TestCase {
	public void test_lib0() {
		String fileName = Constant.rootDir + "吴清源番棋263局/吴清源番棋002.SGF";
		;
		// GoManual manual = GMDGoManual.loadGoManual(Constant.GLOBAL_MANUAL,0);
		byte count = 2;
		List<GMDGoManual> list = new LoadGMDGoManual(Constant.rootDir)
				.loadMultiGoManualFromLib0();
		// for (GMDGoManual manual : list) {
		// count++;
		// byte[] original = list.get(count).getMoves();

		GMDGoManual manual = list.get(count);
		List<Step> steps = manual.getSteps();
		GoBoard go = new GoBoard();		
		for (Step step : steps) {
			go.oneStepForward(step);
		}
		byte[][] state = go.getBoardColorState().getMatrixState();

		ShowManual weiqi = new ShowManual(state, steps);
		weiqi.setVisible(true);
		weiqi.setBounds(0, 0, 800, 600);

	}
}
