/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;


/**
 * <p>
 * SwapBit.java
 * </p>
 */
public class BitSwapping {
	private static long[] mask = new long[64];
	private static long[][] mm = new long[64][64];

	static {
		for (int i = 0; i < 64; i++) {
			mask[i] = 1 << i;
		}
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				mm[i][j] = ~((mask[i]) | (mask[j]));
			}
		}
	}

	/**
	 * simple and stupid solution by intuition swap the j and j bit in x
	 * 
	 * for this specific feature, it looks ok.
	 * the problem is that it is not scalable. e.g. consider the broadword computing.
	 * @param x
	 * @param i indexed from right, start from 0;
	 * @param j
	 */
	public static long swapBit_Stupid(long x, int i, int j) {
		long tempi = x & mask[i];
		long tempj = x & mask[j];
		if (i == j) {
			return x;
		}
		if (tempi == 0) {
			if (tempj == 0) {
				// both bits are 0. no need swap.
			} else {
				x = x | mask[i];
				x = x ^ mask[j];
			}
		} else {
			if (tempj == 0) {
				x = x ^ mask[i];
				x = x | mask[j];
			} else {
				// both bits are 1. no need swap.
			}
		}
		return x;
	}

	/**
	 * swap in a clever and efficient way.
	 * 
	 * @param x
	 * @param i
	 * @param j
	 */
	public static long swapBit(long x, int i, int j) {
		if (i == j) {
			return x;
		} else if (i < j) {
			int temp = i;
			i = j;
			j = temp;
		}
		return _swapBit(x, i, j);

	}

	/**
	 * 
	 * @param x
	 * @param i
	 * @param j
	 * @return
	 */
	public static long _swapBit(long x, int i, int j) {
		int d = i - j;
		/**
		 * consider two bits bi and bj. 
		 * after step 1: for bj in y. it is bi in x, other bits are zero
		 * after step 2: for bi in z. it is bj in x, other bits are zero
		 * in (x & mm[i][j]), only bi and bj are clear, other bits are same as x.
		 * 
		 * in final returned value. bi comes from z, bj comes from y, other bits comes from 
		 * (x & mm[i][j])
		 */
		long y = (x >>> d) & (mask[j]);
		long z = (x & (mask[j])) << d;
		return (x & mm[i][j]) | y | z;
	}

	/**
	 * How it works.
	 * 
	 * @param x
	 * @param i
	 * @param j
	 * @return
	 */
	public static long swapBit1(long x, int i, int j) {
		if (i == j) {
			return x;
		} else if (i < j) {
			int temp = i;
			i = j;
			j = temp;
		}
		return _swapBit1(x, i, j);

	}

	public static long _swapBit1(long x, int i, int j) {
		int d = i - j;
		/**
		 * consider two bits bi and bj. after step 1: for bj in y. it is bj ^ bi
		 * & 1 = bi ^ bj other bits in y is 0;
		 * 
		 * after step 2: for bi in x in return. it is bi ^ 0 ^ (bi ^ bj) = bj
		 * for bj in x in return. it is bj ^ (bi ^ bj) ^ (0) = bi fot other bit
		 * such as bk, it is bk ^ 0 ^ 0 = bk
		 * 
		 * That's it.
		 */
		long y = (x ^ (x >>> d)) & (mask[j]);
		return x ^ y ^ (y << d);
	}
	
	
}
