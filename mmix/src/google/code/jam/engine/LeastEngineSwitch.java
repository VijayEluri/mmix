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
public class LeastEngineSwitch {
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

	public int getSequence2() {
		int n = queries.length;
		if (n == 0)
			return 0;
		sols = new Solution[n];
		return getEngineSequence(n);
	}

	public int getEngineSequence(int n) {
		if (sols[n - 1] != null) {
			return sols[n - 1].count;
		}
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < engines.length; i++) {
			String eng = engines[i];
			if (eng.equals(queries[n - 1])) {
				continue;
			}
			int j = n - 1;
			for (; j >= 0; j--) {
				if (eng.equals(queries[j])) {
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
			} else {
				int sub = getEngineSequence(j + 1);
				if (sub + 1 < min) {
					min = sub + 1;
					sols[n - 1] = new Solution();
					sols[n - 1].engine = eng;
					sols[n - 1].count = min;
					sols[n - 1].sizeOfSub = j + 1;
				}
			}

		}

		return sols[n - 1].count;
	}
}

class EngineSeq {
	String engine;
	int start;
	int end;// inclusive

	public String toString() {
		return "start=" + start + "; end=" + end + "engine=" + engine;
	}
}

class Solution {
	int count;// of switches.
	int sizeOfSub;
	String engine;// for current seg
}