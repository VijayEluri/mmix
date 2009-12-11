/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package problem.birthday;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Combination.java
 * </p>
 * finally I got the correct answer: 5*5*5*5 = 5*4*3*2 + 5*4*3*(4*3/(2*1)) +
 * 5*4*(4*3*2/(3*2*1)) + 5*1 + 5*4*(4*3/(2*1))/2.
 * i.e.: 625 = 120 + 360 + 80 + 5 + 60
 */
public class Combination {
	public static void main(String[] args) {
		String[] original = new String[] { "A", "B", "C", "D", "E" };
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < 5; k++) {
					for (int m = 0; m < 5; m++) {
						set.add(original[i] + original[j] + original[k]
								+ original[m]);
					}
				}
			}
		}
		System.out.println(set.size());

		// remove combi of all different char
		boolean removed = false;
		String tmp = null;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (i == j)
					continue;
				for (int k = 0; k < 5; k++) {
					if (k == i || k == j)
						continue;
					for (int m = 0; m < 5; m++) {
						if (m == i || m == j || m == k)
							continue;
						// System.out.println("i=" + i + j + k + m);
						tmp = original[i] + original[j] + original[k]
								+ original[m];
						removed = set.remove(tmp);
						if (!removed)
							System.out.println("tmp=" + tmp + "; removed="
									+ removed + ";");
					}
				}
			}

		}
		System.out.println(set.size());

		// remove combi of two same char and all others are different char
		for (int i = 0; i < 5; i++) {
			int j = i;
			for (int k = 0; k < 5; k++) {
				if (k == i || k == j)
					continue;
				for (int m = 0; m < 5; m++) {
					if (m == i || m == j || m == k)
						continue;
					removed = set.remove(original[i] + original[j]
							+ original[k] + original[m]);
					removed = set.remove(original[i] + original[k]
							+ original[j] + original[m]);
					removed = set.remove(original[i] + original[k]
							+ original[m] + original[j]);
					removed = set.remove(original[k] + original[j]
							+ original[i] + original[m]);
					removed = set.remove(original[k] + original[j]
							+ original[m] + original[i]);
					removed = set.remove(original[k] + original[m]
							+ original[i] + original[j]);

				}
			}

		}
		System.out.println(set.size());

		// remove combi of three same char and all others are different char
		for (int i = 0; i < 5; i++) {
			int j = i;
			int k = j;
			for (int m = 0; m < 5; m++) {
				if (m == i || m == j || m == k)
					continue;
				removed = set.remove(original[i] + original[j] + original[k]
						+ original[m]);

				removed = set.remove(original[i] + original[j] + original[m]
						+ original[k]);
				removed = set.remove(original[i] + original[m] + original[k]
						+ original[j]);
				removed = set.remove(original[m] + original[j] + original[k]
						+ original[i]);
				// System.out.println("removed=" + removed + ";");
			}

		}
		System.out.println(set.size());

		// remove combi of four same char
		for (int i = 0; i < 5; i++) {
			int j = i;
			int k = j;
			int m = k;
			removed = set.remove(original[i] + original[j] + original[k]
					+ original[m]);
			if (!removed)
				System.out.println("removed=" + removed + ";");

		}
		System.out.println(set.size());
		System.out.println(Arrays.toString(set.toArray()));

		for (int i = 0; i < 5; i++) {
			for (int k = 0; k < 5; k++) {
				if (i == k)
					continue;
				int m = k;
				int j = i;
				removed = set.remove(original[i] + original[j] + original[k]
						+ original[m]);
				if (!removed)
					System.out.println("removed=" + removed + ";");
				removed = set.remove(original[i] + original[k] + original[j]
						+ original[m]);
				if (!removed)
					System.out.println("removed=" + removed + ";");
				removed = set.remove(original[i] + original[k] + original[m]
						+ original[j]);
				if (!removed)
					System.out.println("removed=" + removed + ";");
			}
		}

		System.out.println(set.size());
	}

}
