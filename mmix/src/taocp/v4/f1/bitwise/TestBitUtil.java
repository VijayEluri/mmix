/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;

import junit.framework.TestCase;

/**
 * <p>
 * TestBitUtil.java
 * </p>
 */
public class TestBitUtil extends TestCase {
	public void test() {
		getTruthTable2();
	}

	/**
	 * for fomula 27 in page 8 of scetion 7.1.1
	 */
	public static void getTruthTable2() {
		boolean x, y, z;

		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1) > 0 ? true : false;
			y = (i >>> 1 & 1) > 0 ? true : false;
			x = (i >>> 2 & 1) > 0 ? true : false;
			System.out.print(((x & (!y) & z) | (y & z) | ((!x) & y & (!z))) ? 1
					: 0);
		}
		System.out.println();

		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1) > 0 ? true : false;
			y = (i >>> 1 & 1) > 0 ? true : false;
			x = (i >>> 2 & 1) > 0 ? true : false;
			System.out.print((((!x) & y) | (x & z)) ? 1 : 0);
		}
		System.out.println();

		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1) > 0 ? true : false;
			y = (i >>> 1 & 1) > 0 ? true : false;
			x = (i >>> 2 & 1) > 0 ? true : false;
			System.out.print(((x & z) | (y & z) | ((!x) & y)) ? 1 : 0);
		}

		System.out.println();
		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1) > 0 ? true : false;
			y = (i >>> 1 & 1) > 0 ? true : false;
			x = (i >>> 2 & 1) > 0 ? true : false;
			System.out.println((x ? 1 : 0) + "" + (y ? 1 : 0) + (z ? 1 : 0)
					+ "\t" + (((x & z) | (y & z) | ((!x) & y)) ? 1 : 0));
		}

	}

	/**
	 * for fomula 19 in page 6 of scetion 7.1.1
	 */
	public static void testTruthTable() {
		boolean x, y, z;

		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1) > 0 ? true : false;
			y = (i >>> 1 & 1) > 0 ? true : false;
			x = (i >>> 2 & 1) > 0 ? true : false;
			System.out
					.print((true ^ (x & y) ^ (x & z) ^ (y & z) ^ (x & y & z)) ? 1
							: 0);
		}
		System.out.println();
		getTruthTable();
	}

	public static void getTruthTable() {
		int x, y, z;
		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1);
			y = (i >>> 1 & 1);
			x = (i >>> 2 & 1);
			System.out
					.print((1 + (x * y) + (x * z) + (y * z) + (x * y * z)) & 1);
		}
		System.out.println();
	}

	/**
	 * for S2
	 */
	public static boolean[] testTruthTableForS2() {
		int x, y, z;
		boolean [] bb = new boolean[8];
		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1);
			y = (i >>> 1 & 1);
			x = (i >>> 2 & 1);
			System.out.print((x + y + z) >= 2 ? 1 : 0);
		}
		System.out.println();
		System.out.println();
		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1);
			y = (i >>> 1 & 1);
			x = (i >>> 2 & 1);
			bb[i] = (x + y + z) >= 2;
			System.out
					.println(x + "" + y + "" + z + "\t" + (((x + y + z) >= 2) ? 1
							: 0));
		}
		return bb;
	}
	/**
	 * 
	 * @return
	 */
	public static boolean[] testTruthTableForS2_G() {
		int x, y, z;
		boolean [] bb = new boolean[8];
		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1);
			y = (i >>> 1 & 1);
			x = (i >>> 2 & 1);
			System.out.print((x + y + z) >= 2 ? 1 : 0);
		}
		System.out.println();
		System.out.println();
		for (int i = 0; i < (1 << 3); i++) {
			z = (i & 1);
			y = (i >>> 1 & 1);
			x = (i >>> 2 & 1);
			bb[i] = (x + y + z) >= 2;
			System.out
					.println(x + "" + y + "" + z + "\t" + (((x + y + z) >= 2) ? 1
							: 0));
		}
		return bb;
	}

}
