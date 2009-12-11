package taocp.v3.sort.insert;

import org.apache.log4j.Logger;

import taocp.v3.sort.IntArrayUtil;
import taocp.v3.sort.Metrics;
import taocp.v3.sort.Sort;

public class InsertSort implements Sort {
	private static Logger log = Logger.getLogger(InsertSort.class);

	Metrics m = new Metrics();

	public void sort(int[] inputs) {

		IntArrayUtil.checkArgument(inputs);
		if (inputs.length == 1) {
			return;
		}

		int comparisons = 0;
		int moves = 0;
		for (int i = 1; i < inputs.length; i++) {
			int key = inputs[i];
			if (log.isDebugEnabled()) {
				log.debug("i=" + i + "; key=" + key);
			}
			// find position for key!
			int j = i - 1;
			for (; j >= 0; j--) {
				comparisons++;
				if (inputs[j] > key) {
					moves++;
					inputs[j + 1] = inputs[j];
					// inputs[j] = key;
				} else {
					break;
				}
			}
			moves++;
			inputs[j + 1] = key;
			IntArrayUtil.print(inputs, log);
			log.debug("comparisons=" + comparisons + ", moves=" + moves);
		}
		m.setCompares(comparisons);
		m.setSwaps(moves);
		if (log.isDebugEnabled()) {
			log.debug("comparisons=" + comparisons + ", moves=" + moves);
		}
	}

	

	
	public void resetMetrics() {
		m.reset();

	}

	
	public Metrics getMetrics() {
		return m;

	}
}
