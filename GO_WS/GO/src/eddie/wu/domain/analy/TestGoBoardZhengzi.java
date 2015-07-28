package eddie.wu.domain.analy;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardLadder;
import eddie.wu.domain.Point;
import eddie.wu.manual.StateLoader;
import eddie.wu.util.GBKToUTF8;

/**
 * case failed bacause of issue in Cloning a Complex instance.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class TestGoBoardZhengzi extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);

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

	public void testZhentgZi1() {
		String fileName = "doc/征子局面/征子";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");

		logger.setLevel(Level.DEBUG);
		Point point = Point.getPoint(14, 16);
		Point firstMove = Point.getPoint(14, 15);
		testZhengZi(fileName, point, firstMove);

	}

	public void testZhengZi(String fileName, Point point, Point firstMove) {
		// 计算死活
		byte m1, n1;
		byte[][] result;

		BoardColorState state = StateLoader.load(fileName);
		// new LoadExercise().loadZhengZi();
		if(log.isDebugEnabled()) log.debug("state" + state);
		GoBoardLadder linkedBlockGoBoard = new GoBoardLadder(state);

		// linkedBlockGoBoard.generateHighLevelState();
		if(log.isDebugEnabled()) log.debug("black Block" + linkedBlockGoBoard.getBlackBlocksOnTheFly());
		if(log.isDebugEnabled()) log.debug("white Block" + linkedBlockGoBoard.getWhiteBlocks());
		result = linkedBlockGoBoard.jiSuanZhengZi(point);
		if (result[126][0] == 1) {
			// goapplet.goboard.qiquan();
		}
		for (byte i = 1; i <= result[0][1]; i++) {
			// goapplet.goboard.cgcl(result[i][0], result[i][1]);
			if(log.isDebugEnabled()) log.debug("[" + result[i][0] + "," + result[i][1] + "]");
		}

		if(log.isDebugEnabled()) log.debug("征子计算的结果为：" + result[0][0]);
		this.assertEquals(127, result[0][0]);
		this.assertEquals(firstMove, Point.getPoint(result[1][0], result[1][1]));
	}
}
