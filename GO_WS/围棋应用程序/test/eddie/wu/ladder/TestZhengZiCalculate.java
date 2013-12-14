package eddie.wu.ladder;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import util.GBKToUTF8;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.ZhengZiCalculate;

public class TestZhengZiCalculate extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	public void testConvert() {
		ZhengZiCalculate a = new ZhengZiCalculate();
		byte[][] b = new byte[10][2];
		for (int i = 0; i < b.length; i++) {
			b[i][0] = (byte) (i + 5);
			b[i][1] = (byte) (i + 4);
		}
		Point[] c = a.convertArrayToPoints(Constant.BOARD_SIZE,b);
		for (int i = 0; i < b.length - 1; i++) {
			assertEquals(c[i].getRow(), (byte) (i + 5 + 1));
			assertEquals(c[i].getColumn(), (byte) (i + 4 + 1));
		}
	}

	public void testZhentgZi1() {
		String fileName = "doc/征子局面/征子";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");

		logger.setLevel(Level.DEBUG);
		Point point = Point.getPoint(14, 16);
		Point firstMove = Point.getPoint(14, 15);
		testZhengZi(fileName, point, firstMove);

	}

	public void testZhengZi2() {
		String fileName = "doc/征子局面/征子2";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");
		logger.setLevel(Level.DEBUG);
		Point point = Point.getPoint(14, 14);
		Point firstMove = Point.getPoint(13, 14);
		testZhengZi(fileName, point, firstMove);

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
		testZhengZi(fileName, point, firstMove);

	}

	/**
	 * @param fileName
	 * @param firstMove
	 *            正解的第一步
	 */
	private void testZhengZi(String fileName, Point point, Point firstMove) {
		BoardColorState state = StateLoader.load(fileName);
		ZhengZiCalculate d = new ZhengZiCalculate();
		Point[] points = d.jisuanzhengziWithClone(state, point);// (16,13)
		this.assertEquals(firstMove, points[0]);
		for (int j = 0; j < points.length; j++) {
			if (points[j] == null)
				break;
			if(log.isDebugEnabled()) log.debug(points[j]);
		}

	}
}
