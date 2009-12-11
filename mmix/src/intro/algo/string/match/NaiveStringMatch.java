/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.string.match;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * NaiveStringMatch.java
 * </p>
 */
public class NaiveStringMatch {
	/**
	 * 
	 * @param text
	 * it may be very long in real case, say 1000 or 1M
	 * @param pattern
	 *            is a literal
	 * @return int[] a a[i] is the increment of start index of matched string
	 */
	public Integer[] match(String text, String pattern) {
		Integer[] a = new Integer[0];
		List<Integer> ll = new ArrayList<Integer>();
		int n = text.length();
		int m = pattern.length();
		for (int i = 0; i < n - m + 1; i++) {
			String temp = text.substring(i, i + m);
			if (temp.equals(pattern)) {
				ll.add(i);
			}
		}
		return ll.toArray(a);
	}

}
