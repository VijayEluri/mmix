package taocp.v3.sort.merge;

import org.apache.log4j.Logger;

import taocp.v3.sort.IntArrayUtil;
import taocp.v3.sort.Metrics;
import taocp.v3.sort.Sort;

public class MergeSort implements Sort{
	private static Logger log = Logger.getLogger(MergeSort.class);
	Metrics m = new Metrics();

	

	public  void sort(int[] inputs) {		
		sort(inputs, 0, inputs.length - 1);		
	}

	/**
	 * 
	 * @param inputs
	 * @param start
	 * @param end
	 *            (included)
	 */
	public void sort(int[] inputs, int start, int end) {

		IntArrayUtil.checkArgument(inputs, start, end);
		if (start == end) {
			return;
		}
		int comparisons = 0;
		int moves = 0;
		int middle = (start + end) / 2;

		MergeSort aa = new MergeSort();
		MergeSort bb = new MergeSort();
		aa.sort(inputs, start, middle);
		bb.sort(inputs, middle + 1, end);
		Metrics a = aa.m;
		Metrics b = bb.m;

		int i = start;
		int j = middle + 1;
		int[] out = new int[end - start + 1];
		int count = 0;
		while (i <= middle && j <= end) {
			comparisons++;
			if (inputs[i] < inputs[j]) {
				out[count++] = inputs[i];
				i++;
			} else {
				out[count++] = inputs[j];
				j++;
			}

		}
		// copy the left segment.
		if (i <= middle) {
			for (int k = i; k <= middle; k++) {
				out[count++] = inputs[k];
			}
		} else if (j <= end) {
			for (int k = j; k <= end; k++) {
				out[count++] = inputs[k];
			}
		}
		System.arraycopy(out, 0, inputs, start, out.length);
		m.setCompares(comparisons + a.getCompares() + b.getCompares());

		m.setSwaps(out.length + a.getSwaps() + b.getSwaps());
		IntArrayUtil.print(inputs, log);
		log.debug("sorts=" + m.getCompares() + ", moves=" + m.getSwaps());
	}

	
	public void resetMetrics() {
		m.reset();
		
	}
	
	public Metrics getMetrics() {
		return m;

	}
}
