package eddie.wu.linkedblock;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.ZhengZiCalculate;

public class TestZhengZiCalculate extends TestCase {
	public void testConvert() {
		ZhengZiCalculate a = new ZhengZiCalculate();
		byte[][] b = new byte[10][2];
		for (int i = 0; i < b.length; i++) {
			b[i][0] = (byte) (i + 5);
			b[i][1] = (byte) (i + 4);
		}
		Point[] c = a.convertArrayToPoints(b);
		for (int i = 0; i < b.length; i++) {
			assertEquals(c[i].getRow(), (byte) (i + 5));
			assertEquals(c[i].getColumn(), (byte) (i + 4));
		}
	}

	public void testZhentgZi1() {
		String fileName = "doc/征子局面/征子";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");

		logger.setLevel(Level.DEBUG);
		Point point = new Point(16, 14);
		testZhengzi(fileName, point);

	}

	public void testZhengZi2() {
		String fileName = "doc/征子局面/征子2";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");
		logger.setLevel(Level.DEBUG);
		Point point = new Point(14, 14);
		testZhengzi(fileName, point);

	}
	
	/**
	 * 征子和接不归的本质都是一气吃，容易计算，程序中是统一处理的。
	 */
	public void testJieBuGui() {
		String fileName = "doc/征子局面/接不归_3_17.wjm";
		Logger logger = Logger.getLogger(GoBoard.class.getName() + "Zhengzi");
		logger.setLevel(Level.DEBUG);
		//Point point = new Point(3, 17);
		Point point = new Point(17, 3);
		testZhengzi(fileName, point);

	}

	/**
	 * @param fileName
	 */
	private void testZhengzi(String fileName, Point point) {
		BoardColorState state = StateLoader.load(fileName);
		ZhengZiCalculate d = new ZhengZiCalculate();
		Point[] points = d.jisuanzhengziWithClone(state, point);// (16,13)
		for (int j = 0; j < points.length; j++) {
			if (points[j] == null)
				break;
			System.out.println(points[j]);
		}

		//BoardLianShort内部的逻辑没有很好地分离。 
//		BoardLianShort goboard = new BoardLianShort();
//		try {
//			DataInputStream in = new DataInputStream(new BufferedInputStream(
//					new FileInputStream(fileName)));
//
//			goboard.zairujumian(in);
//
//			in.close();
//		}
//
//		catch (IOException ex) {
//			System.out.println("the input meet some trouble!");
//			System.out.println("Exception" + ex.toString());
//		}
//		goboard.shengchengjumian();
//		byte[][] result2;
//		result2 = goboard.jiSuanZhengZi(point.getRow(), point.getColumn());
//		for (int j = 0; j < result2.length; j++) {
//
//			System.out.println(result2[j][0] + "," + result2[j][1]);
//		}
		// this.assertTrue(Arrays.equals(result2 , result));
	}

}
