/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;


/**
 * <p>
 * BitReversal.java
 * </p>
 */
public class BitReversal extends BitwiseTool {
	/**
	 * 0000 0011 0000 0000 - 1100 0000 0011 0000 - 0011 0000 0011 0000 - 1100
	 * 0011 0000 0011
	 * 2-bit swap  8 times 
	 */
	private static final long r1 = 0x0300c0303030c303L;
	/**
	 * 0000 0000 1100 0000 - 0011 0000 0000 1100 - 0000 0011 1111 0000 - 0000
	 * 0000 0011 1111
	 * 6- bit swap - 2
	 * 2 -bit swap - 2
	 */
	private static final long r2 = 0x00c0300c03f0003fL;
	/**
	 * 0000 0000 0000 0000 - 0000 1111 1111 0011 - 0000 0000 0000 0000 - 0011
	 * 1111 1111 1111
	 * 14-bit swap - 1
	 * 8-bit swap - 2
	 * 2-bit swap - 2
	 */
	private static final long r3 = 0x00000ffc00003fffL;

	public static long reverse(long x) {
		long y = 0;
		long z = 0;

		for (int i = 0; i < 6; i++) {
			y = (x >>> n[i]) & m[i];
			z = (x & m[i]) << n[i];
			x = y | z;
		}

		return x;
	}

	/**
	 * same implementation. but the loop code is extended
	 * 
	 * @param x
	 * @return
	 */
	public static long reverseOptimized(long x) {
		long y = 0;
		long z = 0;

		y = (x >>> 1) & m[0];
		z = (x & m[0]) << 1;
		x = y | z;

		y = (x >>> 2) & m[1];
		z = (x & m[1]) << 2;
		x = y | z;

		y = (x >>> 4) & m[2];
		z = (x & m[2]) << 4;
		x = y | z;

		y = (x >>> 8) & m[3];
		z = (x & m[3]) << 8;
		x = y | z;

		y = (x >>> 16) & m[4];
		z = (x & m[4]) << 16;
		x = y | z;

		return (x >>> 32) | (x << 32);
	}

	/**
	 * reverse the bits by swapping bits
	 * It is complex, without background theory knowledge, we can not understand it.
	 * @param x
	 * @return
	 */
	public static long reverse2(long x) {
		long y = 0;
		long z = 0;

		y = (x >>> 1) & m[0];
		z = (x & m[0]) << 1;
		x = y | z;

		y = (x >>> 2) & m[1];
		z = (x & m[1]) << 2;
		x = y | z;

		y = (x >>> 4) & m[2];
		z = (x & m[2]) << 4;
		x = y | z;

		y = (x >>> 8) & m[3];
		z = (x & m[3]) << 8;
		x = y | z;

		y = (x >>> 16) & m[4];
		z = (x & m[4]) << 16;
		x = y | z;

		return (x >>> 32) | (x << 32);
	}
}
