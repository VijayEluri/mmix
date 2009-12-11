
package mmix.cons;

/**
 * <p>ArithmeticExceptionConstant.java
 * </p>
 * 
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class ArithmeticExceptionConstant {
	
	public static final int D_BIT_ENABLE = 7 + 8;

	public static final int V_BIT_ENABLE = 6 + 8;

	public static final int W_BIT_ENABLE = 5 + 8;

	public static final int I_BIT_ENABLE = 4 + 8;

	public static final int O_BIT_ENABLE = 3 + 8;

	public static final int U_BIT_ENABLE = 2 + 8;

	public static final int Z_BIT_ENABLE = 1 + 8;

	public static final int X_BIT_ENABLE = 0 + 8;

	public static final int D_BIT_SHIFT = 7;// integer divide check

	public static final int V_BIT_SHIFT = 6;//integer overflow

	public static final int W_BIT_SHIFT = 5;//float-to-fix overflow

	public static final int I_BIT_SHIFT = 4;//invalid floating operation

	public static final int O_BIT_SHIFT = 3;//floating overflow

	public static final int U_BIT_SHIFT = 2;//floating underflow

	public static final int Z_BIT_SHIFT = 1;//floating division by zero

	public static final int X_BIT_SHIFT = 0;//floating inexact
}
