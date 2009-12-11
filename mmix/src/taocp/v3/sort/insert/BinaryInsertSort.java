/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.insert;

import org.apache.log4j.Logger;

import taocp.v3.sort.IntArrayUtil;
import taocp.v3.sort.Metrics;
import taocp.v3.sort.Sort;

/**
 * <p>
 * BinaryInsertSort.java
 * </p>
 */
public class BinaryInsertSort implements Sort {
	private static Logger log = Logger.getLogger(InsertSort.class);

	Metrics m = new Metrics();
	int comparisons = 0;
	int moves = 0;

	public void sort(int[] inputs) {

		IntArrayUtil.checkArgument(inputs);
		if (inputs.length == 1) {
			return;
		}

		int index = 0;// where to put the elements
		for (int i = 1; i < inputs.length; i++) {
			int key = inputs[i];// we are dealing with the ith element.

			index = binarySearch(inputs, 0, i - 1, key);
			if (index == i) {
				// nothing to do. already in the place.
			} else {
				for (int k = i - 1; k >= index; k--) {
					inputs[k + 1] = inputs[k];
					moves++;
				}
				inputs[index] = key;
			}
			log.debug("index = " + index);
			IntArrayUtil.print(inputs, log);
			// log.debug("comparisons=" + comparisons + ", moves=" + moves);
		}
		m.setCompares(comparisons);
		m.setSwaps(moves);

	}

	/**
	 * the array is partly sorted in the range from start to end(included) find
	 * the last element in array- which is larger than taget value.
	 * 
	 * @param in
	 * @param start
	 * @param end
	 * @param index
	 *            where to put the target value. in array in
	 * @return
	 */
	private int binarySearch(int[] in, int start, int end, int target) {
		int index = 0;
		if (end - start == 0) {// only one elements
			if (in[end] > target)
				index = end;
			else {
				index = end + 1;
			}
			comparisons++;
			return index;

		} else if (end - start == 1) {// only two elements
			comparisons++;
			if (in[end] > target) {
				index = binarySearch(in, start, start, target);
				return index;
			} else {
				index = end + 1;
				return index;
			}

		}

		int media = (start + end) / 2;// 3 and more than three elements.

		if (in[media] > target)
			index = binarySearch(in, start, media - 1, target);
		else {
			index = binarySearch(in, media , end, target);
		}
		comparisons++;
		return index;
	}

	
	public void resetMetrics() {
		m.reset();
		comparisons = 0;
		moves = 0;
	}

	
	public Metrics getMetrics() {
		return m;

	}
}
