package taocp.v3.sort.select;

import util.MathUtil;

public class StraightSelectionSort {
	public static void sort_max(int[] a) {
		if (a.length <= 1)
			return;
		for (int i = a.length - 1; i >= 1; i--) {
			int max = 0;
			int index = 0;
			for (int j = i; j >= 0; j--) {
				if (a[j] > max) {
					max = a[j];
					index = j;
				}
			}
			MathUtil.swap(a,index ,i);
		}
	}

	public static void sort_min(int[] a) {

	}
}
