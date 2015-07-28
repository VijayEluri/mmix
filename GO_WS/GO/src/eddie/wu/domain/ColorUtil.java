/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.domain;

/**
 * @author eddie
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ColorUtil {

	public final static char BLACK_STRING = 'B';
	public final static char WHITE_STRING = 'W';
	public static final char BLANK_STRING = '_';

	public final static String BLACK_LONG_STRING = "Black";
	public final static String WHITE_LONG_STRING = "White";
	public final static String BLANK_LONG_STRING = "Blank";
	/**
	 * color for single point
	 */
	public final static byte BLANK = eddie.wu.domain.Constant.BLANK; // 0表示空白点.

	public final static byte BLACK = eddie.wu.domain.Constant.BLACK; // 1表示黑子;

	public final static byte WHITE = eddie.wu.domain.Constant.WHITE; // 2表示白子;

	public final static byte Mixture = 3; // 3表示普通的空百块.不是气块;

	/**
	 * @deprecated
	 */
	public final static byte BREATH = 4; // 大眼死活模式识别时表示提子形成的气块

	// 或者是没有加以确认.相当于UNKnown
	public final static byte OutOfBoard = 8; // 2表示白子;
	public final static byte irrelevant = 4; // 2表示白子;

	/**
	 * 在shoushu增加之前调用
	 * 
	 * @param shoushu
	 * @return
	 */
	public static int getNextStepColor(int shoushu) {
		return (shoushu % 2 + 1);
	}

	/**
	 * 在shoushu增加之前调用
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getNextStepEnemyColor(short shoushu) {
		byte yise = (byte) ((1 + shoushu) % 2 + 1);
		return yise;
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
	 * 根据手数判断落子点的颜色。手数已经增加。
	 * 
	 * @param shoushu
	 *            =1 for first play/move.
	 * @return
	 */
	public static int getCurrentStepColor(int shoushu) {
		return ((1 + shoushu) % 2 + 1); // 白后行为偶数
	}

	/**
	 * 根据手数判断落子点的颜色。手数已经增加。
	 * 
	 * @param shoushu
	 * @return
	 */
	public static int getCurrentStepEnemyColor(short shoushu) {
		return (shoushu % 2 + 1); // 黑先行为奇数

	}

	public boolean isBlackTurn(int shoushu) {
		return evenNumberOfPoints(shoushu);
	}

	public boolean isWhiteTurn(int shoushu) {
		return !evenNumberOfPoints(shoushu);
	}

	public static boolean evenNumberOfPoints(int count) {
		if (count % 2 == 0)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param a
	 *            index from 0
	 * @param boardSize
	 * @return state index from 1
	 */
	public static byte[][] initState(String a, int boardSize) {
		byte[][] state = new byte[boardSize + 2][boardSize + 2];
		char color;
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				int endIndex = (row - 1) * boardSize + column - 1;
				color = a.charAt(endIndex);
				if (color == ColorUtil.BLACK_STRING) {
					state[row][column] = Constant.BLACK;
				} else if (color == ColorUtil.WHITE_STRING) {
					state[row][column] = Constant.WHITE;
				}
			}
		}
		return state;
	}

	public static String getColorText(int color) {
		if (color == Constant.BLACK)
			return BLACK_LONG_STRING;
		else if (color == Constant.WHITE)
			return WHITE_LONG_STRING;
		else if (color == Constant.BLANK)
			return BLANK_LONG_STRING;
		else
			return null;
	}

}