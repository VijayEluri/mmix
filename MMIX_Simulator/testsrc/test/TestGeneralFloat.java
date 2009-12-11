package test;

import mmix.NumberUtil;
import junit.framework.TestCase;

/**
 * <p>
 * TestGeneralFloat.java
 * </p>
 * use 1 byte = 8 bits to represent the float - simplify the situation. 1 bit
 * for sign 2 bit for exponent. 5 bit for significant.
 * 
 * double 1 : 11 : 52 float 1 : 8 : 23
 * 
 * E == 0; F == 0 +- 0.0 F != 0 +- 2** (- x) * F E >= 1 and E <=2 +- 2 ** (E-1)
 * * (1 + F / (2 ** 5)) E == 3; F != 0 Not a number(F / 2**5) F == 0 +-infinite
 * 
 * 
 * Note that there are two representation for zero: one is for +0.0(0), another
 * is for -0.0(128) Note that there are many representations for NaN: in out
 * example, there are 32 +NaN (97-127) and -NaN(225-255).
 * 
 * 224: -Infinity 96: +Infinity
 * 
 * now consider the positive signed 8 bits number.
 * [0-32)	[0 - 1) mulitply of 1 / 32
 * [32,64)  [1 - 2) multiply of 1 / 32
 * [64,96)  [2 - 4) multiply of 1 / 16
 * [96,128) 96: +Infinity
 * 			112 standardized NaN.
 * 
 * The closure of these numbers.
 * 0/0 = NaN.
 * ?
 * 
 * There is a mismatch between binary number and decimal number.
 * 				
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestGeneralFloat extends TestCase {
	int[] bits = new int[8];
	int s;
	int e;
	int f;

	public void showNumber() {
		s = bits[0];
		e = bits[1] * 2 + bits[2];
		f = (bits[3] << 4) + (bits[4] << 3) + (bits[5] << 2) + (bits[6] << 1)
				+ bits[7];
		System.out.print("s = " + s + "; e = " + e + "; f = " + f
				+ "; number is " + getNumber());
		System.out.println();
	}

	private String getNumber() {
		if (e == 0) {
			if (f == 0) {
				if (s == 0) {
					return "+0.0";
				} else {
					return "-0.0";
				}
			} else {
				double temp = (double) f / (double) 32;
				if (s == 0) {
					return Double.toString(temp);
				} else {
					return "-" + Double.toString(temp);
				}
			}
		} else if (e == 3) {
			if (f == 0) {
				if (s == 0) {
					return "+Infinity";
				} else {
					return "-Infinity";
				}
			} else {
				if (s == 0) {
					return "+NaN";
				} else {
					return "-NaN";
				}
			}
		} else {// range (0,3)
			double temp = Math.pow(2, (e - 1)) * (1 + f / 32.0);
			if (s == 0) {
				return Double.toString(temp);
			} else {
				return "-" + Double.toString(temp);
			}
		}

	}

	public void test() {
		for (int i = 0; i < 128; i++) {
			bits = NumberUtil.byteToBits(i);
			for (int a : bits) {
				System.out.print(a);
			}
			System.out.println();
			System.out.print("i = " + i + "\t");
			this.showNumber();
		}
		for (int i = 128; i < 256; i++) {
			bits = NumberUtil.byteToBits(i);
			for (int a : bits) {
				System.out.print(a);
			}
			System.out.println();
			System.out.print("i = " + i + "\t");
			this.showNumber();
		}
	}

	public void test2() {
		bits = NumberUtil.byteToBits(255);
		for (int a : bits) {
			System.out.print(a);
		}
		System.out.println();
	}

}
