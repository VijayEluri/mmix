/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.linkedblock;

/**
 * @author eddie
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ColorUtil {

	/**
	 * color for single point 
	 */
	public final static byte BLANK_POINT = 0; //0表示空白点.

	public final static byte BLACK = 1; //1表示黑子;

	public final static byte WHITE = 2; //2表示白子;

	public final static byte Mixture = 3; //3表示普通的空百块.不是气块;

	//或者是没有加以确认.相当于UNKnown
	public final static byte OutOfBound = 10; //2表示白子;

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

		byte tongse = (byte) ((1 + shoushu) % 2 + 1); //tong se=1或2,白后行为偶数
		return tongse;
	}

	/**
	 * 根据手数判断落子点的颜色。手数已经增加。
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getCurrentStepEnemyColor(short shoushu) {
		byte yise = (byte) (shoushu % 2 + 1); //yi se=1或2,黑先行为奇数
		return yise;
	}
}