/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.string.match;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * RadixStringMatch.java
 * </p>
 */
public class RadixStringMatch {
	int prime = 2 << 19 - 1;// 31 also works
	int radix = 128; // only consider ASCII (0-127);

	public Integer[] match(String text, String pattern) {

		Integer[] a = new Integer[0];
		List<Integer> ll = new ArrayList<Integer>();

		int p = getNumber(pattern);
		int n = text.length();
		int m = pattern.length();

		int t = getNumber(text.substring(0, m));
		if (t == p) {
			ll.add(0);
		}
		int sub = getSub(m);//
		System.out.println("p=" + p + "; sub=" + sub + "; t=" + t);
		for (int i = 1; i < n - m + 1; i++) {

			t = ((t - (sub * (text.charAt(i - 1)) % prime) + prime)*radix + text
					.charAt(i + m - 1))
					% prime;
			System.out.println("t=" + t);
			if (t == p) {
				ll.add(i);
			}
		}
		return ll.toArray(a);
	}

	private int getNumber(String pattern) {

		int number = 0;
		int mul = 1;
		for (int i = pattern.length() - 1; i >= 0; i--) {
			number += pattern.charAt(i) * mul;
			mul *= radix;
			if (number >= prime) {
				number = number % prime;
			}
			if (mul >= prime) {
				mul %= prime;
			}
		}
		System.out.println("covert " + pattern + " to " + number);
		return number;
	}

	private int getSub(int m) {
		int number = 0;
		int mul = 1;
		for (int i = m - 1; i >= 1; i--) {
			mul *= radix;
			if (mul >= prime) {
				mul %= prime;
			}
		}
		return mul;
	}

	public void verify(String text, String pattern, List<Integer> ll) {
		for (Iterator<Integer> it = ll.iterator(); it.hasNext();) {
			int s = it.next();
			if (pattern.equals(text.substring(s, s + pattern.length()))) {
				// valid
			} else {
				it.remove();
			}
		}
	}
}
