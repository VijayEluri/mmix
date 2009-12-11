/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.string.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * It is much complex than previous one, since we need to touch automata theory.
 * The first challenge is to get familiar with the terms used in this field.
 * <p>
 * Automaton.java
 * </p>
 */
public class AutomatonStringMatch {
	public Integer[] match(String text, String pattern) {
		Integer[] a = new Integer[0];
		List<Integer> ll = new ArrayList<Integer>();

		Automata auto = new Automata();
		auto.initAuto(pattern);
		int m = pattern.length();
		int n = text.length();
		int q = 0;
		for (int i = 0; i < n; i++) {
			q = auto.delta(q, text.charAt(i));
			if (q == m) {
				ll.add(i-m+1);//i is the last char in the string.
			}
		}
		return ll.toArray(a);
	}

}

class Automata {
	Map<Character, Integer> sigmaMap = new HashMap<Character, Integer>();
	int[][] dd;

	/**
	 * 
	 * @param pattern
	 */
	public void initAuto(String pattern) {
		// convert to java code init delta;
		int m = pattern.length();
		Set<Character> sigmaSet = new HashSet<Character>();
		for (int i = 0; i < pattern.length(); i++) {
			sigmaSet.add(pattern.charAt(i));
		}
		char[] sigmaChar = new char[sigmaSet.size()];
		// map the char to index

		int i = 0;
		for (char a : sigmaSet) {
			sigmaMap.put(a, i);
			sigmaChar[i++] = a;

		}
		String sigmaString = String.valueOf(sigmaChar);
		System.out.println("sigma string is: " + sigmaString);

		dd = new int[m+1][sigmaSet.size()];//m+1 because of state 0
		// set up the array.
		for (int q = 0; q <= m; q++) {
			System.out.println("q=" + q);
			for (Iterator<Character> it = sigmaSet.iterator(); it.hasNext();) {
				char tt = it.next();
				System.out.println("tt=" + tt);
				int k = Math.min(m + 1, q + 2);
				String Pk;
				String PqA;
				PqA = pattern.substring(0, q) + tt;
				do {
					k = k - 1;
					Pk = pattern.substring(0, k);
					

				} while ((!PqA.endsWith(Pk)));
				dd[q][sigmaMap.get(tt)]=k;
			}
		}
	}

	int delta(int q, char t) {
		if (sigmaMap.containsKey(t)) {
			return dd[q][sigmaMap.get(t)];
		} else {
			return 0;
		}
	}

}
