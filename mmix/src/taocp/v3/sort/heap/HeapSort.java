package taocp.v3.sort.heap;

import util.MathUtil;

public class HeapSort {
	// static PriorityQueue pq;

	public static void sort(int[] a) {
		Heap h = new Heap(a.length);
		int count = MathUtil.getSwapCount();
		for (int i = 0; i < a.length; i++) {
			h.add(a[i]);
		}
		System.out.println("swap " + (MathUtil.getSwapCount() - count)
				+ " times to make heap!");
		for (int i = 0; i < a.length; i++) {
			a[i] = h.removeRoot();
		}
		System.out.println("swap total " + (MathUtil.getSwapCount() - count)
				+ " times!");
	}

}
