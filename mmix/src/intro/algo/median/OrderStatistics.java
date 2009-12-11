package intro.algo.median;

import java.util.Arrays;

import taocp.v3.sort.Metrics;
import taocp.v3.sort.merge.MergeSort;
import taocp.v3.sort.quick.QuickSort;

public class OrderStatistics {
	MergeSort sort = new MergeSort();// used to sort small array and get median.
	QuickSort quick = new QuickSort();
	Metrics m = new Metrics();
	int comp = 0;
	int move = 0;

	public int getMedian(int[] a) {
		return getMedian(a, 0, a.length - 1, (a.length - 1) / 2);
	}

	/**
	 * get the median of the array, if a.length is even, then the median is
	 * defined as the lower one.
	 * 
	 * @param a
	 * @return median value
	 */
	public int getMedian(int[] a, int start, int end, int expectedIndex) {
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
		int c = (end - start + 1) % 5;//

		// divided into b sub groups of 5 elements. there may be one extra
		// group with less than 5 elements.
		int b = (end - start + 1) / 5;

		// the array of medians in each sub group of 5 elements.
		int[] meds;//
		int[] medIndex;// we need to know the index of the median value
		if (c != 0) {
			meds = new int[b + 1];
			medIndex = new int[b + 1];
		} else {
			meds = new int[b];
			medIndex = new int[b];
		}

		for (int i = 0; i < b; i++) {
			sort.resetMetrics();
			sort.sort(a, start + i * 5, start + (i + 1) * 5 - 1);
			m.add(sort.getMetrics());
			meds[i] = a[start + 5 * i + 2];
			medIndex[i] = start + 5 * i + 2;
		}
		if (c != 0) {
			sort.resetMetrics();
			sort.sort(a, start + b * 5, end);
			m.add(sort.getMetrics());
			meds[b] = a[start + 5 * b + (c - 1) / 2];// get lower one when even
			medIndex[b] = start + 5 * b + (c - 1) / 2;
		}
		System.out.println("After sort for each 5 elements group.");
		System.out.println(Arrays.toString(a));
		System.out.println("meds=" + Arrays.toString(meds));
		System.out.println("medIndex=" + Arrays.toString(medIndex));

		// clone it because sort will
		// change the array.
		int median = getMedian(meds.clone());
		int medianIndex = 0;
		for (int i = 0; i < medIndex.length; i++) {
			comp++;
			if (meds[i] == median) {
				medianIndex = medIndex[i];
				break;
			}
		}
		System.out.println("medianIndex=" + medianIndex);
		int[] meds2 = meds.clone();
		sort.sort(meds2);
		int median2 = meds2[(meds.length - 1) / 2];
		if (median != median2) {
			System.out.println(Arrays.toString(meds));
			System.out.println(Arrays.toString(meds2));
			throw new RuntimeException("median=" + median + "!=median2="
					+ median2);
		}
		System.out.println("median=" + median + "median2=" + median2);
		System.out.println("when work on start=" + start + "; end=" + end);
		// partition
		quick.resetMetrics();
		int par = quick.partition(a, start, end, medianIndex);
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

	
	
}
