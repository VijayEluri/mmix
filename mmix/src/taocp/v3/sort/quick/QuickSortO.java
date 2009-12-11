/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.quick;

import taocp.v3.sort.insert.InsertSort;

/**
 * Design Decision: do not use static method, use instance method instead.
 * because we can not make interface for static method.
 * 
 * instance method give us more flexibility.
 * <p>
 * QuickSortO.java
 * </p>
 */
public class QuickSortO {
	InsertSort insertSort = new InsertSort();	

	/**
	 * short cut version!
	 * 
	 * @param a
	 */
	public void sort(int[] a) {
		sort(a, 0, a.length);
	}

	/**
	 * mixed sorting strategy - use insert sort when less than 5 elements.
	 * 
	 * @param a
	 * @param start
	 * @param end
	 */
	public void sort(int[] a, int start, int end) {
		sort_not_complete(a, start, end);
		// sort by insert
		insertSort.sort(a);
	}

	

	public  void sort_not_complete(int[] a, int start, int end) {
		int jj = partition(a, start, end);
		if (jj != 0) {
			sort_not_complete(a, start, jj - 1);
			sort_not_complete(a, jj + 1, end);
		}
	}

	public static int partition(int[] a, int start, int end) {
		if (end - start < 5)
			return 0;
		// partition as order statistics.
		// ensure > 3/10N was splited.
		return 0;
	}
}
