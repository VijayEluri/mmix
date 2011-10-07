package eddie.wu.domain;

import junit.framework.TestCase;
import eddie.wu.manual.LoadGoManual;
import go.ActionAnalysis;

public class TestActionAnalysis extends TestCase {
	private static final String rootDir = "doc/Î§Æå´òÆ×Èí¼þ/";

	public void test() {
		byte[] temp = new LoadGoManual(rootDir).loadSingleGoManual();
		GoBoard go = new GoBoard();
		for (int i = 0; i < temp.length / 2; i++) {
			Step step = new Step();
			byte color;
			if (i % 2 == 0) {
				color = Constant.BLACK;
			} else {
				color = Constant.WHITE;
			}
			step.setColor(color);
			Point point = Point.getPoint(temp[i * 2], temp[i * 2 + 1]);
			step.setPoint(point);
			String moveName = new ActionAnalysis(go.getBoardColorState()
					.getMatrixState()).moveName(point, color);
			System.out.println(point + " " + moveName);
			go.oneStepForward(step);
		}
	}
}
