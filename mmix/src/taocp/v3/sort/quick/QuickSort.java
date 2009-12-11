package taocp.v3.sort.quick;

import java.util.Random;

import taocp.v3.sort.Metrics;
import taocp.v3.sort.Sort;
import util.MathUtil;

/**
 * Re-implement quick sort with the strategy -- burn the candle from both sides.
 * 
 * @author eddie.wu
 * 
 */
public class QuickSort implements Sort {
	Metrics m = new Metrics();

	/**
	 * sort the elements into ascending order.
	 * 
	 * @param a
	 *            must be not null.
	 */
	public void sort(int[] a) {
		if (a.length <= 1)
			return;
		int i = 0;
		int j = a.length - 1;
		sort(a, i, j);
	}

	/**
	 * 
	 * @param a
	 *            must be not null.
	 * @param ii
	 * @param jj
	 *            must be large than ii. inclusive
	 */
	public void sort(int[] a, int start, int end) {

		if (start == end) {
			return;
		}
		int jj = partition(a, start, end);

		if (jj > start) {
			sort(a, start, jj - 1);
		}
		if (jj < end) {
			sort(a, jj + 1, end);
		}
	}

	public void randomized_sort(int[] a, int start, int end) {

		if (start == end) {
			return;
		}
		int jj = randomized_partition(a, start, end);

		if (jj > start) {
			randomized_sort(a, start, jj - 1);
		}
		if (jj < end) {
			randomized_sort(a, jj + 1, end);
		}
	}

	/**
	 * The first element is choose as the pivot
	 * 
	 * @param a
	 * @param start
	 * @param end
	 *            end must large than start
	 * @return
	 */
	public int partition(int[] a, int start, int end, int pivotIndex) {
		MathUtil.swap(a, start, pivotIndex);
		int ii = start + 1;
		int jj = end;
		while (ii <= jj) {
			int i = ii;
			for (; i <= jj; i++) {
				if (a[i] > a[start]) {
					ii = i;
					break;
				}
			}
			if (i == jj + 1) {// all are less than pivot
				// MathUtil.swap(a, a;, b)
				break;
			}
			// now ii point to the first element large than pivot
			i = jj;
			for (; i > ii; i--) {
				if (a[i] <= a[start]) {
					jj = i;
					break;
				}
			}
			if (i == ii) {// no one less then pivot
				jj = ii - 1;
				break;
			}

			MathUtil.swap(a, ii, jj);
			ii++;
			jj--;

		}
		MathUtil.swap(a, start, jj);
		return jj;

	}

	public int partition(int[] a, int start, int end) {
		return partition(a, start, end, start);
	}

	public int randomized_partition(int[] a, int start, int end) {
		Random r = new Random();
		return partition(a, start, end, r.nextInt() % (end - start + 1) + start);

	}

	
	public Metrics getMetrics() {
		return m;
	}

	
	public void resetMetrics() {
		m.reset();

	}

}
