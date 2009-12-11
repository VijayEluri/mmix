/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;


/**
 * <p>
 * LeftestBit.java
 * </p>
 */
public class LeftMostBit {
	private static int lamtab[] = new int[256];
	/**
	 * get log2(x) for [1,255]
	 */
	static {
		for (int i = 0; i < 8; i++) {
			for (int j = (int)((long) 1 << i); j < ((long) 1 << i + 1); j++) {
				lamtab[j] = i;
			}
		}
	}

	/**
	 * get the position of leftest bit 1. (bit is indexed from 0)
	 * 
	 * @param x
	 * @return
	 */
	public static int getLeftMostBitPosition(long x) {
		//negative x will be treated as the positive number x+power(2,d), 
		//here we only care about bit pattern.
		if (x == 0) {
			throw new IllegalArgumentException();
		}

		long y = x;
		int t = 0;
		int lam = 0;
		
		y >>>= 32;		
		if (y > 0) {
			lam = 32;
		} 

		t = lam + 16;
		y = x >>> t;
		if (y > 0) {
			lam = t;
		}

		t = lam + 8;
		y = x >>> t;
		if (y > 0) {
			lam = t;
		}

		y = x >>> lam;
		lam += lamtab[(int) y];

		return lam;
	}

	public static void main(String[] args) {
		System.out.println(LeftMostBit.getLeftMostBitPosition(Long.MIN_VALUE));
		System.out.println(LeftMostBit.getLeftMostBitPosition(1));
		System.out.println(LeftMostBit.getLeftMostBitPosition(128));
		System.out.println(LeftMostBit.getLeftMostBitPosition(Long.MAX_VALUE));
	}
}
