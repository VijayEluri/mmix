/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.median;

import java.util.Arrays;

import taocp.v3.sort.Metrics;
import taocp.v3.sort.merge.MergeSort;
import taocp.v3.sort.quick.QuickSort;

/**
 * <p>
 * Median.java
 * </p>
 */
public class Median {
	MergeSort sort = new MergeSort();
	Metrics m = new Metrics();
	QuickSort quick = new QuickSort();
	int comp = 0;
	int move = 0;
	
	public int getMedian(int[] a){
		return getMedian(a,0,a.length-1,(a.length-1)/2);
	}
			

	/**
	 * in place algorithm, do not depends on extra array of median of median. do
	 * not recurse when get the median of median.
	 * 
	 * @param a
	 * @param start
	 * @param end
	 * @param expectedIndex
	 * @return
	 */
	public int getMedian(int[] a, int start, int end, int expectedIndex) {
		// pseudo code
		/**
		 * sort each 5 element group, start at 0, (i=groups) step is 5:1(group
		 * step vs element step) next level start at 2 (5*0+2), step is 25:5
		 * next level sart at 12(5*2+2), step is 125:25 need customized sort
		 * routine to handle non-continuous elements.
		 */
		if (end - start + 1 <= 5 * 2) {// there are no more than 10 elements.
			sort.resetMetrics();
			sort.sort(a, start, end);
			m.add(sort.getMetrics());
//			System.out.println(Arrays.toString(Arrays.copyOfRange(a, start,
//					end + 1)));
			return a[expectedIndex];
		}
		System.out.println();
		System.out.println("work on start=" + start + "; end=" + end
				+ "; expectedIndex=" + expectedIndex);
//		System.out.println(Arrays.toString(Arrays
//				.copyOfRange(a, start, end + 1)));
		
		int medianIndex = getPivot(a, start, end, 5);

		
		quick.resetMetrics();
		int par = 0;
		par=quick.partition(a, start, end, medianIndex);
		m.add(quick.getMetrics());
		System.out.println("par=" + par + "; a[par]" + a[par]);

		// System.out.println(Arrays.toString(a));
		if (par == expectedIndex) {
			return a[par];
		} else if (par < expectedIndex) {
			return getMedian(a, par + 1, end, expectedIndex);
		} else {
			return getMedian(a, start, par - 1, expectedIndex);
		}

	}

	/**
	 * there may be less than 5 elements
	 * 
	 * @param inputs
	 * @param start
	 *            index of first element
	 * @param step
	 *            distance between two elements
	 * @param elements
	 *            how many elements to sort.
	 */
	public void sortElements(int[] inputs, int start, int step, int elements) {
		int key;
		for (int i = 1; i < elements && (start + i * step < inputs.length); i++) {
			key = inputs[start + i * step];
			int j = i - 1;
			for (; j >= 0; j--) {
				if (key < inputs[start + j * step]) {
					inputs[start + (j + 1) * step] = inputs[start + j * step];
				} else {
					break;
				}
			}
			inputs[start + (j + 1) * step] = key;
		}
		//
		System.out.print("[");
		for (int i = 0; i < elements && (start + i * step < inputs.length); i++) {
			System.out.print(inputs[start + i * step] + ",");
		}
		System.out.print("]");
		System.out.println();
	}

	/**
	 * Ignore the right left residues.
	 * 
	 * @param inputs
	 * @param start
	 * @param step
	 * @param elements
	 *            the elements in one group
	 */
	public void sortAllElements(int[] inputs, int start, int step, int elements) {
		int count = (inputs.length - start) / (elements * step);
		if (count < 1) {
			System.out.println("less than one group");
			sortElements(inputs, start, step, elements);
		}
		for (int j = 0; j < count; j++) {
			sortElements(inputs, start + j * (step * elements), step, elements);
		}
	}

	public int getPivot(int[] a, int originalStart, int end, int elements) {

		// pseudo code
		/**
		 * sort each 5 element group, start at 0, (i=groups) step is 5:1(group
		 * step vs element step) next level start at 2 (5*0+2), step is 25:5
		 * next level sart at 12(5*2+2), step is 125:25 need customized sort
		 * routine to handle non-continuous elements.
		 */

		// int elements = 5;
		int step = 1;
		int start = originalStart;
		int last = start;
		while (start < a.length) {
			System.out.println("start=" + start);
			sortAllElements(a, start, step, elements);
			step = step * elements;
			last = start;
			start = originalStart + (step / 2);
			System.out.println(Arrays.toString(a));
		}
		System.out.println("start - (step / 2)=" + (start - (step / 2)));
		System.out.println("start=" + (start));
		System.out.println("last=" + (last));
		System.out.println("(step / 2)=" + ((step / 2)));
		//return a[last];
		return last;

	}
}
