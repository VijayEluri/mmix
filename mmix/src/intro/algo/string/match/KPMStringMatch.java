/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.string.match;

import java.util.ArrayList;
import java.util.List;

/**
 * The Algorithm invented by Knuth, Morris, and Pakin at the same time. It
 * improve the preprocessing performance from $m|\Sigma|$ to $m$, and keep the
 * runtime complexity to $O(n).$
 * 
 * <p>
 * KPMStringMatch.java
 * </p>
 */
public class KPMStringMatch {
	/**
     *
     */
	public Integer[] match(String text, String pattern) {
		init(pattern);
		
		List<Integer> ll = new ArrayList<Integer>();
		
		int m = pattern.length();
		int n = text.length();
		int q = 0;

		char[] p = pattern.toCharArray();
		char[] t = text.toCharArray();
		for (int i = 0; i < n; i++) {
			while (q > 0 && p[q] != t[i]) {
				q = pi[q];
			}
			// two exit case: 1. q=0. follow
			// case 2. q>0 and p[q] = t[i].
			if (p[q] == t[i]) {
				q++;
			}// else i increment.
			if (q == m) {
				ll.add(i - m + 1);// i is the last char in the string.
				q = pi[q];
			}
		}
		return ll.toArray(new Integer[0]);
	}

	int[] pi;

	/**
	 * init the pi array. the meaning of pi[i]=j means that string p[1..j] is
	 * the suffix of string p[1..i].
	 * 
	 * when used in match, p[1..i] match the current input string and p[i+1]
	 * does not match the input, in this situation, we know that the next valid
	 * shift is decided by the pi[i]. e.g. if pi[i]=0, it means we should start
	 * the next possible match from postion p[i+1].
	 */
	void init(String p) {
		int m = p.length();
		char[] pattern = p.toCharArray();
		// note we add 1 because indexing from 1. note m[0] is not used.
		pi = new int[m + 1];

		pi[1] = 0;// pi[i]<i, so pi[1] has to be 0 since it is impossible to be
		// negative.
		int k = 0;// will be assign to pi[q] after correctly set. k count the
		// number of chars in p.
		for (int q = 2; q <= m; q++) {
			// while (k > 0 && pattern[k+1 ] != pattern[q]) {
			while (k > 0 && pattern[k] != pattern[q - 1]) {
				// if change to k>=0 may cause endless loop.
				k = pi[k];
			}
			// two exit case: 1 k=0, we check again.
			// k>0 and matched. case follows.

			// if (pattern[k + 1] == pattern[q]) {
			// we are indexed from 0;
			if (pattern[k] == pattern[q - 1]) {
				// normally it work since k and p bother increased.
				// and the corresponding string are incremented at the right
				// end.
				// if always matched then pi[q]=q-1;
				k = k + 1;// one more char matched.
			}
			pi[q] = k;// how many chars in p[1-q] match its end.
			System.out.println("q= " + q + "; pi[q] = " + k);
		}
	}
}
