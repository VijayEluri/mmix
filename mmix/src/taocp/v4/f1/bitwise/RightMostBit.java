/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;

/**
 * <p>
 * RightMostBit.java
 * </p>
 */
public class RightMostBit extends BitwiseTool{

	

	/**
	 * count the number of trailing zeros, it also means the position of the
	 * first bit 1 looking leftward (bit is indexed from 0). 
	 * it is also known as ruler funciton.
	 * 
	 * @param x
	 * @return
	 */
	public static int getRightMostBitPosition(long x) {
		int count = 0;
		long y = x & (-x);
		long q = 0;

		for (int i = 5; i >= 0; i--) {
			q = y & m[i];
			if (q == 0) {
				count += n[i];
			}
		}
		return count;
	}

	public static void main(String[] args) {
		System.out.println(RightMostBit.getRightMostBitPosition(Long.MAX_VALUE));
		System.out.println(RightMostBit.getRightMostBitPosition(Long.MIN_VALUE));
		System.out.println(RightMostBit.getRightMostBitPosition(0));
	}
}
