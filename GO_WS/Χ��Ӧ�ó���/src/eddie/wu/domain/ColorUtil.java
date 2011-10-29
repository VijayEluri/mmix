/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.domain;

import com.sun.corba.se.impl.orbutil.closure.Constant;

/**
 * @author eddie
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ColorUtil {

	/**
	 * color for single point
	 */
	public final static byte BLANK = eddie.wu.domain.Constant.BLANK; // 0表示空白点.

	public final static byte BLACK = eddie.wu.domain.Constant.BLACK; // 1表示黑子;

	public final static byte WHITE = eddie.wu.domain.Constant.WHITE; // 2表示白子;

	public final static byte Mixture = 3; // 3表示普通的空百块.不是气块;

	//public final static byte BREATH = 4; // 大眼死活模式识别时表示提子形成的气块

	// 或者是没有加以确认.相当于UNKnown
	public final static byte OutOfBound = 10; // 2表示白子;

	/**
	 * 在shoushu增加之前调用，yise和tongse的计算有所不同。
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getNextStepColor(short shoushu) {

		byte tongse = (byte) (shoushu % 2 + 1);
		return tongse;
	}

	public static int enemyColor(int myColor) {

		if (myColor == BLACK) {
			return WHITE;
		} else if (myColor == WHITE) {
			return BLACK;
		}
		throw new RuntimeException("my color = " + myColor + " is invalid");
	}

	/**
	 * 在shoushu增加之前调用，yise和tongse的计算有所不同。
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getNextStepEnemyColor(short shoushu) {
		byte yise = (byte) ((1 + shoushu) % 2 + 1);
		return yise;
	}

	/**
	 * 根据手数判断落子点的颜色。手数已经增加。
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getCurrentStepColor(short shoushu) {

		byte tongse = (byte) ((1 + shoushu) % 2 + 1); // tong se=1或2,白后行为偶数
		return tongse;
	}

	/**
	 * 根据手数判断落子点的颜色。手数已经增加。
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getCurrentStepEnemyColor(short shoushu) {
		byte yise = (byte) (shoushu % 2 + 1); // yi se=1或2,黑先行为奇数
		return yise;
	}
}