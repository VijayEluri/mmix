/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package google.code.jam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Address the train time table problem from google code jam. only list the core
 * part of the solution. did not test against the test case yet.
 * <p>
 * TrainTimetable.java
 * </p>
 */
public class TrainTimeTable {
	// public int count(List<Integer> la, List<Integer> lb, int t) {
	// public int count(int[] la, int[] lb, int t) {
	// return countInternal(la, lb, t) + countInternal(lb, la, t);
	// }

	public int count(int[] la, int[] lb, int t) {
		int count = 0;
		int buffer = 0;
		Arrays.sort(la);
		System.out.println(Arrays.toString(la));
		Arrays.sort(lb);
		// List<Integer> lbb = new ArrayList<Integer>(lb.size());
		int[] lbb = lb.clone();
		// Collections.copy(lbb, lb);
		for (int i = 0; i < lbb.length; i++) {
			lbb[i] = lb[i] + t;
		}
		System.out.println(Arrays.toString(lbb));
		boolean empty = false;
		if (lbb.length == 0)
			empty = true;
		for (int i = 0; i < la.length; i++) {
			System.out.println("la[i]" + la[i]);
			if ((!empty) && lbb[buffer] <= la[i]) {
				buffer++;
				if (buffer == lbb.length) {
					empty = true;
				}
			} else {
				System.out.println("count++");
				count++;
			}
		}
		return count;

	}

}
