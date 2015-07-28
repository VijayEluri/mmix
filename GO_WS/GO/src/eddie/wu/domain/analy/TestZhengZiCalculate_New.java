package eddie.wu.domain.analy;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.CaptureSearch;

public class TestZhengZiCalculate_New extends TestCase {
	private static final Logger log = Logger
			.getLogger(TestZhengZiCalculate_New.class);

	public void testZhentgZi1() {
		String fileName = "doc/征子局面/征子";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");

		logger.setLevel(Level.DEBUG);
		Point point = Point.getPoint(14, 16);
		Point firstMove = Point.getPoint(14, 15);
		testZhengZi_success(fileName, point, Constant.WHITE);

	}

	public void testZhengZi2() {
		String fileName = "doc/征子局面/征子2";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");
		logger.setLevel(Level.DEBUG);
		Point point = Point.getPoint(14, 14);
		Point firstMove = Point.getPoint(13, 14);
		testZhengZi_success(fileName, point, Constant.WHITE);

	}

	public void testWangLei() {
		String fileName = "doc/征子局面/王磊－曹熏铉";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");
		logger.setLevel(Level.DEBUG);
		Point point = Point.getPoint(16, 16);
		Point firstMove = Point.getPoint(16, 15);
		testZhengZi_fail(fileName, point, Constant.WHITE);

	}

	public void testShuangDaoPu() {
		String fileName = "doc/围棋程序数据/死活题局面/双倒扑_1.wjm";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");
		logger.setLevel(Level.DEBUG);
		Point point = Point.getPoint(17, 17);
		Point firstMove = Point.getPoint(16, 15);
		testZhengZi_success(fileName, point, Constant.BLACK);
		point = Point.getPoint(18, 18);
		testZhengZi_success(fileName, point, Constant.BLACK);
	}

	/**
	 * 征子和接不归的本质都是一气吃，容易计算，程序中是统一处理的。
	 */
	public void testJieBuGui() {
		String fileName = "doc/征子局面/接不归_3_17.wjm";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");
		logger.setLevel(Level.DEBUG);
		// Point point = new Point(3, 17);
		Point point = Point.getPoint(3, 17);
		Point firstMove = Point.getPoint(4, 16);
		testZhengZi_success(fileName, point, Constant.WHITE);

	}

	/**
	 * @param fileName
	 * @param firstMove
	 *            正解的第一步
	 */
	private void testZhengZi_success(String fileName, Point point,
			int targetColor) {
		BoardColorState state = StateLoader.load(fileName);
		CaptureSearch cs = new CaptureSearch(state.getMatrixState(), point,
				targetColor, false);
		assertEquals(CaptureSearch.CAPTURE_SUCCESS, cs.globalSearch());

	}

	private void testZhengZi_fail(String fileName, Point point, int targetColor) {
		BoardColorState state = StateLoader.load(fileName);
		CaptureSearch cs = new CaptureSearch(state.getMatrixState(), point,
				targetColor, false);
		assertEquals(CaptureSearch.CAPTURE_FAILURE, cs.globalSearch());

	}
}
