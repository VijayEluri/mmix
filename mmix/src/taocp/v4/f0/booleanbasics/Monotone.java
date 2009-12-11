/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.booleanbasics;

import taocp.v4.f1.bitwise.BitSwapping;

/**
 * <p>
 * Monotone.java
 * </p>
 */
public class Monotone {
	/**
	 * get the sequence suach as. [0, 1, 2, 4, 3, 5, 6, 7]
	 * [000,001,010,100,011,101,110,111] here we want to get the permutation of
	 * {1,0,0} in a different way.
	 * 
	 * have not get a general solution such as the gray code change sequence.
	 * 
	 * @param nvar
	 * @return
	 */
	public static long[] getMonotoneSequenceByNumberOfOne(int nvar) {
		long[] a = new long[1 << nvar];
		int index = 0;// index for populating the array a. a[index] is
		// available.

		int i0 = 0; // index of left outer 0 from right
		int i0a = 0;// index of right inner 0 from right

		if (nvar > 3) {
			a[0] = 0;
			index = 1;

			for (int i = 1; i < nvar; i++) {
				long[] tempa = getMonotoneSequence(nvar , i);
				for (int k = 0; k < tempa.length; k++) {
					a[index++] = tempa[k];
				}				
			}

			a[a.length - 1] = (1 << nvar) - 1;
			index = a.length;
			return a;
		}

		a[0] = 0;
		index = 1;

		for (int i = 1; i <= nvar; i++) {// i means number of 1s.
			a[index++] = (1 << i) - 1;
			System.out.println(Long.toBinaryString(a[index - 1]));
			i0 = i;
			while (i0 != (nvar)) {
				long temp = BitSwapping.swapBit(a[index - 1], i0, i0 - 1);
				a[index++] = temp;
				System.out.println(Long.toBinaryString(a[index - 1]));

				i0a = i0 - 1;
				while ((i0a) > i0 - i) {
					temp = BitSwapping.swapBit(a[index - 1], i0a, i0a - 1);
					a[index++] = temp;
					System.out.println(Long.toBinaryString(a[index - 1]));
					i0a--;
				}
				i0++;
			}
		}
		// if(index != a.length){
		// throw new RuntimeException("index != a.length");
		// }
		return a;
	}

	public static long[] getMonotoneSequence(int nvar, int n1) {
		int index = 0;
		if(n1>nvar){
			return new long[]{};
		}
		
		if (nvar > 3) {

			long[] tempa = getMonotoneSequence(nvar - 1, n1);
			long[] tempb = getMonotoneSequence(nvar - 1, n1 - 1);

			long[] a = new long[tempa.length + tempb.length];
			for (int k = 0; k < tempa.length; k++) {
				a[index++] = tempa[k];
			}

			for (int k = 0; k < tempb.length; k++) {
				a[index++] = tempb[k] + (1 << (nvar-1));
			}
			return a;

		}
		if (n1 < 0) {
			return new long[] {  };
		} else
		if (n1 == 0) {
			return new long[] { 0 };
		} else if (n1 == 1) {
			return new long[] { 1, 1 << 1, 1 << 2 };
		} else if (n1 == 2) {
			return new long[] { (1 << 2) - 1, 5, 6 };
		} else if (n1 == 3) {
			return new long[] { (1 << 3) - 1 };
		}
		return null;
	}

	/**
	 * truth table is not a good way to express a function since it is exponent
	 * complexity. better way is to generate on the fly with some tech. such as
	 * BDD.
	 * 
	 * @param truthTable
	 * @return
	 */
	public static boolean isMonotone(boolean[] truthTable, int num) {
		long[] a = getMonotoneSequenceByNumberOfOne(num);
		boolean changed = false;
		for (int i = 0; i < (1 << num); i++) {
			if (changed) {
				if (truthTable[(int) a[i]] == false) {
					return false;
				}
			} else {
				if (truthTable[(int) a[i]]) {
					changed = true;
				}
			}
		}
		return true;
	}
}
