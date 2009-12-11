/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package misc.twentyfour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * <p>
 * TwentyFour.java
 * </p>
 */
public class TwentyFour {
	boolean debug = false;
	char oper[] = new char[] { '+', '-', '*', '/' };

	public static void main(String[] args) {
		int[] a = new int[] { 1, 5, 5, 5 };
		new TwentyFour().cal(a);
	}

	public String cal(int[] a) {
		// if(a.length==4){
		// return cal4(a);
		// }
		Level level = new Level();
		level.init(a);
		Stack<Level> stack = new Stack<Level>();
		stack.add(level);

		while (!stack.isEmpty()) {
			level = stack.pop();
			if (debug)
				System.out.println(level.toString());
			Integer[] temp = level.activeIndex.toArray(new Integer[0]);
			if (temp.length == 2) {
				if (level.rawValue[temp[0]] * level.rawValue[temp[1]] == 24) {
					// if (debug)
					System.out.println("(" + level.rawS[temp[0]] + ")*("
							+ level.rawS[temp[1]] + ")");
				}
				if (level.rawValue[temp[0]] / level.rawValue[temp[1]] == 24) {
					// if (debug)
					System.out.println("(" + level.rawS[temp[0]] + ")/("
							+ level.rawS[temp[1]] + ")");
				}
				if (level.rawValue[temp[0]] + level.rawValue[temp[1]] == 24) {
					// if (debug)
					System.out.println("(" + level.rawS[temp[0]] + ")+("
							+ level.rawS[temp[1]] + ")");
				}
				if (level.rawValue[temp[0]] - level.rawValue[temp[1]] == 24) {
					// if (debug)
					System.out.println("(" + level.rawS[temp[0]] + ")-("
							+ level.rawS[temp[1]] + ")");
				}
			} else {
				int size = temp.length;
				for (int i = 0; i < size - 1; i++) {
					for (int j = i + 1; j < size; j++) {
						// choose i and j
						Level lev = level.clone();
						lev.rawValue[lev.nextI] = level.rawValue[temp[i]]
								* level.rawValue[temp[j]];
						lev.rawS[lev.nextI] = "(" + level.rawValue[temp[i]]
								+ "*" + level.rawValue[temp[j]] + ")";
						if (debug)
							System.out.println(lev.rawS[lev.nextI]);

						lev.activeIndex.remove(temp[i]);
						lev.activeIndex.remove(temp[j]);
						lev.activeIndex.add(lev.nextI);
						lev.nextI++;
						stack.push(lev);

						lev = level.clone();
						lev.rawValue[lev.nextI] = level.rawValue[temp[i]]
								+ level.rawValue[temp[j]];
						lev.rawS[lev.nextI] = "(" + level.rawValue[temp[i]]
								+ "+" + level.rawValue[temp[j]] + ")";
						if (debug)
							System.out.println(lev.rawS[lev.nextI]);

						lev.activeIndex.remove(temp[i]);
						lev.activeIndex.remove(temp[j]);
						lev.activeIndex.add(lev.nextI);
						lev.nextI++;
						stack.push(lev);

						lev = level.clone();
						lev.rawValue[lev.nextI] = level.rawValue[temp[i]]
								- level.rawValue[temp[j]];
						lev.rawS[lev.nextI] = "(" + level.rawValue[temp[i]]
								+ "-" + level.rawValue[temp[j]] + ")";
						if (debug)
							System.out.println(lev.rawS[lev.nextI]);

						lev.activeIndex.remove(temp[i]);
						lev.activeIndex.remove(temp[j]);
						lev.activeIndex.add(lev.nextI);
						lev.nextI++;
						stack.push(lev);

						lev = level.clone();
						lev.rawValue[lev.nextI] = level.rawValue[temp[i]]
								/ level.rawValue[temp[j]];
						lev.rawS[lev.nextI] = "(" + level.rawValue[temp[i]]
								+ "/" + level.rawValue[temp[j]] + ")";
						if (debug)
							System.out.println(lev.rawS[lev.nextI]);

						lev.activeIndex.remove(temp[i]);
						lev.activeIndex.remove(temp[j]);
						lev.activeIndex.add(lev.nextI);
						lev.nextI++;
						stack.push(lev);
					}
				}
			}
		}
		return "";
	}
}

class Level {
	// int level;

	double[] rawValue = new double[10];
	int nextI = 0;
	Set<Integer> activeIndex = new HashSet<Integer>();
	// the index to available data in raw.
	// List<Choice> seq = new ArrayList<Choice>();
	String[] rawS = new String[10];

	void init(int[] a) {
		for (int i = 0; i < a.length; i++) {
			activeIndex.add(i);// =a;
			rawValue[i] = a[i];
			rawS[i] = "" + a[i];
			nextI = a.length;
		}
	}

	public Level clone() {
		Level lev = new Level();
		lev.rawValue = this.rawValue.clone();
		lev.nextI = nextI;
		lev.activeIndex.addAll(this.activeIndex);
		lev.rawS = rawS.clone();
		return lev;
	}

	// format, index oper index
	public String toString() {

		return Arrays.toString(rawValue) + "; size of index="
				+ activeIndex.size()
				+ Arrays.toString(activeIndex.toArray(new Integer[0]))
				+ "; nextI" + nextI;
	}
}

class Choice {
	int first;
	char oper;
	int second;
}