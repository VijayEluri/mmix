/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.hash;

import java.util.Arrays;

import util.MathUtil;

/**
 * <p>
 * VectorHash.java
 * </p>
 */
class VectorHash implements Hash {
	int[] a;// the parameter of hash, used in dot product.
	int m;// modulo parameter.

	public VectorHash(int[] a, int m) {
		this.a = a;
		this.m = m;
	}

	public VectorHash(int bits, int m) {
		a = new int[bits];
		this.m = m;
	}

	public VectorHash(int m) {
		this.m = m;
	}

	public int hash(int x) {
		int b[] = MathUtil.intToVector(x, m);
		int count = (b.length > a.length) ? a.length : b.length;

		int product = 0;
		for (int i = 0; i < count; i++) {
			product += (a[i] * b[i]);
		}
		return product;
	}

	/**
	 * 
	 * @param bits
	 * @param m
	 * @param a
	 * @return null if can not get satisfying hash
	 */
	public static VectorHash getGoodHash(int bits, int m, int[] a) {
		VectorHash gh = new VectorHash(bits, m);

		boolean success = false;
		int times = 0;
		for (int i = 0; i < Math.pow(m, bits); i++) {// a

			times++;
			gh.a = MathUtil.intToVector(i, m);

			if (HashUtil.isFirstLevelGoodHash(gh, a, 2 * a.length + 1)) {
				System.out.println("after try " + times + " times; a="
						+ Arrays.toString(a));
				success = true;
				break;
			}// if
		}
		if (success) {
			return gh;
		} else {
			return null;
		}
	}

	public static VectorHash getPerfectHash(int bits, int m, int size, int[] a) {
		VectorHash gh = new VectorHash(bits,m);

		boolean success = false;
		int times = 0;
		for (int i = 0; i < Math.pow(m, bits); i++) {// a

			times++;
			gh.a = MathUtil.intToVector(i, m);

			if (HashUtil.isPerfectHash(gh, a)) {
				System.out.println("after try " + times + " times; a="
						+ Arrays.toString(a));
				success = true;
				break;
			}// if
		}
		if (success) {
			return gh;
		} else {
			return null;
		}
	}
}
