/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package google.code.jam.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * LeastEngineSwitch.java
 * </p>
 */
public class LeastEngineSwitch2 {
	String[] engines;
	String[] queries;
	Solution[] sols;

	public EngineSeq[] getSequence() {
		int n = queries.length;
		sols = new Solution[n];
		List<EngineSeq> ll = new ArrayList<EngineSeq>();

		int optimal = getEngineSequence(n);
		System.out.println("optimal=" + optimal);

		while (n >= 1) {
			Solution sol = sols[n - 1];

			EngineSeq e = new EngineSeq();
			e.engine = sol.engine;
			e.end = n - 1;
			e.start = sol.sizeOfSub;
			ll.add(e);
			if (sol.sizeOfSub == 0) {
				break;
			} else {
				n = sol.sizeOfSub;
			}
		}
		Collections.reverse(ll);
		return ll.toArray(new EngineSeq[0]);
	}

	public int getEngineSequence(int n) {

		int min = Integer.MAX_VALUE;
		int minI = 0;
		for (int i = 0; i < engines.length; i++) {
			String eng = engines[i];
			if (eng.equals(queries[n - 1])) {
				continue;
			}
			int j = n - 1;
			for (; j >= 0; j--) {
				if (eng.equals(queries[j])) {
					if (j < min) {
						min = j;
						minI = i;
					}
					break;
				}
			}
			if (j == -1) {
				// && sols[n] != null
				sols[n - 1] = new Solution();
				sols[n - 1].engine = eng;
				sols[n - 1].count = 0;
				sols[n - 1].sizeOfSub = 0;
				break;// just one of the optimal solution. other engine may also
				// works.
			}
		}

		int sub = getEngineSequence(min + 1);

		sols[n - 1] = new Solution();
		sols[n - 1].engine = engines[minI];
		sols[n - 1].count = sub + 1;
		sols[n - 1].sizeOfSub = min + 1;

		return sols[n - 1].count;
	}
}
