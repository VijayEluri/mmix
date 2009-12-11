package mmix.cons;


/**
 * <p>
 * SpecialRegisterConstant.java
 * </p>
 * 
 * public static int ([^= ]+) = ((\d)+); specialRegister\ [$2\] = "$1";
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class SpecialRegisterConstant {
	/**
	 * index for special register
	 */
	public static int A = 21;

	public static int B = 0;

	public static int C = 8;

	public static int D = 1;

	public static int E = 2;

	public static int F = 22;

	public static int G = 19;

	public static int H = 3;

	public static int I = 12;

	public static int J = 4;

	public static int K = 15;

	public static int L = 20;

	public static int M = 5;

	public static int N = 9;

	public static int O = 10;// register stack Offset

	public static int P = 23;

	public static int Q = 16;

	public static int R = 6;

	public static int S = 11;// register stack point

	public static int T = 13;// address of trap handler

	public static int U = 17;

	public static int V = 18;

	public static int W = 24;

	public static int X = 25;

	public static int Y = 26;

	public static int Z = 27;

	public static int BB = 7;// bootstrap register

	public static int TT = 14;// used when rK & rQ != 0; for dynamic traps.

	public static int WW = 28;// where interrupted

	public static int XX = 29;// execution register

	public static int YY = 30;// Y operand register

	public static int ZZ = 31;// Z operand register

	private static String[] specialRegister = new String[32];
	static {
		specialRegister[21] = "A";

		specialRegister[0] = "B";

		specialRegister[8] = "C";

		specialRegister[1] = "D";

		specialRegister[2] = "E";

		specialRegister[22] = "F";

		specialRegister[19] = "G";

		specialRegister[3] = "H";

		specialRegister[12] = "I";

		specialRegister[4] = "J";

		specialRegister[15] = "K";

		specialRegister[20] = "L";

		specialRegister[5] = "M";

		specialRegister[9] = "N";

		specialRegister[10] = "O";// register stack Offset

		specialRegister[23] = "P";

		specialRegister[16] = "Q";

		specialRegister[6] = "R";

		specialRegister[11] = "S";// register stack point

		specialRegister[13] = "T";// address of trap handler

		specialRegister[17] = "U";

		specialRegister[18] = "V";

		specialRegister[24] = "W";

		specialRegister[25] = "X";

		specialRegister[26] = "Y";

		specialRegister[27] = "Z";

		specialRegister[7] = "BB";// bootstrap register

		specialRegister[14] = "TT";// used when rK & rQ != 0; for dynamic traps.

		specialRegister[28] = "WW";// where interrupted

		specialRegister[29] = "XX";// execution register

		specialRegister[30] = "YY";// Y operand register

		specialRegister[31] = "ZZ";// Z operand register
	}

	public static String mapToString(int index) {
		return specialRegister[index];
	}
}
