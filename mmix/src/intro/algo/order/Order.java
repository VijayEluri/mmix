/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.order;

/**
 * <p>
 * Order.java
 * </p>
 */
public class Order {
	/**
	 * 
	 * @param a
	 *            having at least one elements.
	 * @return
	 */
	public static int max(int[] a) {

		int max = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * 
	 * @param a
	 *            having at least one elements.
	 * @return
	 */
	public static int min(int[] a) {

		int min = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Solution 1: no optimization
	 * worst case is O(2n);
	 * average performance is O(1.5n)
	 * bease case is O(n)
	 * 
	 * @param a
	 *            having at least one elements.
	 * @return
	 */
	public static int[] max_min(int[] a) {
		int[] res = new int[2];

		int max = a[0];
		int min = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > max)
				max = a[i];
			else if (a[i] < min)
				min = a[i];
		}
		res[0] = max;
		res[1] = min;
		return res;
	}

	/**
	 * worst case performance is better. O(1.5n)
	 * average case is O(1.25n);
	 * best case is also O(1n)
	 * @param a
	 *            having at least one elements.
	 * @return
	 */
	public static int[] max_min_v2(int[] a) {
		int[] res = new int[2];
		int max;// = a[0];
		int min;// = a[0];
		if (a.length % 2 == 0) {
			if (a[0] > a[1]) {
				max = a[0];
				min = a[1];
			} else {
				max = a[1];
				min = a[0];
			}
			max_min_internal(a, res, 2, max, min);
		} else {
			max = a[0];
			min = a[0];
			max_min_internal(a, res, 1, max, min);
		}
		return res;
	}

	private static void max_min_internal(int[] a, int[] res, int start,
			int max, int min) {
		for (int i = start; i < a.length; i += 2) {
			if (a[start] > a[start + 1]) {
				if (a[start] > max) {
					max = a[start];
				} else if (a[start + 1] < min) {
					min = a[start + 1];
				}
			} else {
				if (a[start + 1] > max) {
					max = a[start + 1];
				} else if (a[start] < min) {
					min = a[start];
				}
			}

		}
		res[0] = max;
		res[1] = min;

	}
}
