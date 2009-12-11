/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.count;

import java.util.Arrays;

/**
 * takes time to get it correct, even for such a simple algorithm. I have made
 * at least about 10 mistakes.
 * <p>
 * Counting.java
 * </p>
 */
public class Counting {
	public static void sort(int[] a) {
		int[] count = new int[a.length];
		int[] b = new int[a.length];
		for (int i = 1; i < a.length; i++) {
			for (int j = 0; j < i; j++) {
				if (a[j] <= a[i]) {
					count[i]++;
				} else {
					count[j]++;
				}
			}
			//System.out.println(Arrays.toString(count));
		}
		for (int i = 0; i < count.length; i++) {
			b[count[i]] = a[i];
		}
		for (int i = 0; i < a.length; i++) {
			a[i] = b[i];
		}
	}
}
