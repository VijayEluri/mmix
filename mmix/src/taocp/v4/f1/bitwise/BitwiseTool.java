/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;
/**
 * <p>
 * BitwiseTool.java
 * </p>
 */
public class BitwiseTool {
	protected static final long m0 = 0x5555555555555555L;

	protected static final long m1 = 0x3333333333333333L;

	protected static final long m2 = 0x0f0f0f0f0f0f0f0fL;

	protected static final long m3 = 0x00ff00ff00ff00ffL;

	protected static final long m4 = 0x0000ffff0000ffffL;

	protected static final long m5 = 0x00000000ffffffffL;

	protected static long[] m = new long[6];

	protected static int[] n = new int[6];
	static {
		m[0] = m0;
		m[1] = m1;
		m[2] = m2;
		m[3] = m3;
		m[4] = m4;
		m[5] = m5;

		n[0] = 1;
		n[1] = 2;
		n[2] = 4;
		n[3] = 8;
		n[4] = 16;
		n[5] = 32;
		// n[6]=6;
	}

	public static void swapBit(long x, int i, int j){
		BitSwapping.swapBit(x, i, j);
	}
	public static int getLeftMostBitPosition(long x) {
		return LeftMostBit.getLeftMostBitPosition(x);
	}
	public static int getRightMostBitPosition(long x) {
		return RightMostBit.getRightMostBitPosition(x);
	}
	public static int countBit_k1(long xx) {
		return CountBit.countBit_k1(xx);
	}
	/**
	 * the method to test the feature
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(LeftMostBit.getLeftMostBitPosition(Long.MIN_VALUE));
		System.out.println(LeftMostBit.getLeftMostBitPosition(1));
		System.out.println(LeftMostBit.getLeftMostBitPosition(128));
		System.out.println(LeftMostBit.getLeftMostBitPosition(Long.MAX_VALUE));		
		
		System.out.println(RightMostBit.getRightMostBitPosition(Long.MAX_VALUE));
		System.out.println(RightMostBit.getRightMostBitPosition(Long.MIN_VALUE));
		System.out.println(RightMostBit.getRightMostBitPosition(0));
	}
	
}
