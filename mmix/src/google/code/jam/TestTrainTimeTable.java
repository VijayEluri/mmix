/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package google.code.jam;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * 
 * <p>
 * TestTrainTimeTable.java
 * </p>
 */
public class TestTrainTimeTable extends TestCase {
	int[] a1 = new int[] { 9, 10, 11, 12, 9 };
	int[] a2 = new int[] { 0, 0, 0, 2, 0 };
	int[] b1 = new int[] { 12, 13, 12, 15, 10 };
	int[] b2 = new int[] { 0, 0, 30, 0, 30 };

	int t = 5;

	// 09:00 12:00
	// 10:00 13:00
	// 11:00 12:30
	// 12:02 15:00
	// 09:00 10:30
	public void test() {
		// List<Integer> la = new ArrayList<Integer>(3);
		int[] la = new int[3];
		// List<Integer> lb = new ArrayList<Integer>(2);
		int[] lb = new int[2];
		for (int i = 0; i < 3; i++) {
			la[i] = (a1[i] * 60 + a2[i]);
		}
		for (int i = 3; i < 5; i++) {
			lb[i - 3] = (b1[i] * 60 + b2[i]);
		}
		int count = new TrainTimeTable().count(la, lb, 5);
		assertEquals(2, count);
		
		
		count += new TrainTimeTable().count(lb, la, 5);
		assertEquals(2, count);
	}
}
