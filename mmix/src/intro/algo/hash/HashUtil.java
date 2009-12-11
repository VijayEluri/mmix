/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.hash;

import java.util.List;

/**
 * Util class to share the reusable code. no instance variable.
 * <p>
 * HashUtil.java
 * </p>
 */
public class HashUtil {
	/**
	 * whether we can get a hash, which map array a into m slots, and the total
	 * storage (sum of mj^2) is not large than maxStorage
	 * 
	 * caller need to ensure the m in hash is same as the m in parameter.
	 * 
	 * @param h
	 * @param a
	 * @param m
	 * @param maxStorage
	 * @return
	 */
	public static boolean isFirstLevelGoodHash(Hash h, int[] a, int m,
			int maxStorage) {
		int[] counts = new int[m];
		int count = 0;
		for (int k = 0; k < a.length; k++) {
			counts[h.hash(a[k])]++;
		}
		for (int k = 0; k < m; k++) {
			count += counts[k] * counts[k];
		}
		if (count > maxStorage) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * suppose the table size is a.length.
	 * 
	 * caller need to ensure the m in hash is same as the a.length in parameter.
	 * 
	 * @param h
	 * @param a
	 * @param maxStorage
	 * @return
	 */
	public static boolean isFirstLevelGoodHash(Hash h, int[] a, int maxStorage) {
		return isFirstLevelGoodHash(h, a, a.length, maxStorage);
	}

	/**
	 * check whether we can get a no collission hash with n=m^2 slots.
	 * 
	 * @param h
	 * @param list
	 * @return
	 */
	public static boolean isPerfectHash(Hash h, List<Integer> list) {
		int[] counts = new int[list.size() * list.size()];
		for (int k : list) {
			if (counts[h.hash(k)] != 0) {
				return false;
			} else {
			}
		}
		return true;
	}

	/**
	 * check whether we can get a no collission hash with .
	 * 
	 * @param h
	 * @param a
	 *            keys to hash
	 * @param size
	 *            the size of the table to hold the elements.
	 * @return
	 */
	public static boolean isPerfectHash(Hash h, int[] a, int size) {
		int[] counts = new int[size];
		for (int k : a) {
			if (counts[h.hash(k)] != 0) {
				return false;
			} else {
			}
		}
		return true;
	}

	/**
	 * check whether we can get a no collission hash with n=m^2 slots.
	 * 
	 * @param h
	 * @param a
	 * @return
	 */
	public static boolean isPerfectHash(Hash h, int[] a) {
		int[] counts = new int[a.length * a.length];
		for (int k : a) {
			if (counts[h.hash(k)] != 0) {
				return false;
			} else {
			}
		}
		return true;
	}
}
