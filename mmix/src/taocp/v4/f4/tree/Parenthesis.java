/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f4.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import taocp.v4.f1.bdd.TruthTable;

/**
 * <p>
 * generating all the trees. matched parenthesis example.
 * 
 * </p>
 */
public class Parenthesis {
	/**
	 * left parenthesis is treated as 1;
	 * 
	 * right parenthesis is treated as 0;
	 * 
	 * how to generate the sequence from 10101010 to 11110000 following
	 * lexicographic order, taken n=4 as example.
	 * 
	 * The idea is like this. the first zero from right, which have 1 on its
	 * right, will be changed to 1 to increase the number. at the same time,
	 * choose one of the 1s in its right and change it to zero. the resulted
	 * right part may be shuffle to get lowest possible increasement.
	 * 
	 * (Otherwise, there is no way to find a 1 and change it to 0 and ensure the
	 * result is increased.)
	 * 
	 * @param n
	 *            number of pairs of parenthesis
	 */
	public String[] getAllMatched(int n) {
		List<boolean[]> list = new ArrayList<boolean[]>();

		boolean[] b = new boolean[2 * n];
		for (int i = 0; i < 2 * n; i += 2) {
			b[i] = true;
			b[i + 1] = false;
		}

		list.add(b.clone());
		boolean[] temp = b;
		while (true) {
			temp = getNext(temp);
			if (temp == null)
				break;
			list.add(temp.clone());
		}

		String[] res = new String[list.size()];
		int count = 0;
		for (boolean[] t : list) {
			res[count++] = TruthTable.getString(t, '(', ')');
		}
		return res;
	}

	public static boolean[] getNext(boolean[] b) {
		int firstOneIndex = 0;
		for (int i = b.length - 1; i >= 0; i--) {
			if (b[i] == true) {
				firstOneIndex = i;
				break;
			}
		}

		int firstZeroIndex = firstOneIndex - 1;
		for (int i = firstZeroIndex; i >= 0; i--, firstZeroIndex--) {
			if (b[i] == false) {
				firstZeroIndex = i;
				break;
			}
		}
		if (firstZeroIndex == -1) {
			return null;
		}
		swap(b, firstZeroIndex, firstZeroIndex + 1);
		int count = 0;
		for (int i = firstOneIndex; i > firstZeroIndex + 1; i--) {
			swap(b, i, b.length - 2 - count * 2);
			count++;
		}
		System.out.println(Arrays.toString(b));
		return b;
	}

	private static void swap(boolean[] b, int i, int j) {
		boolean temp = b[i];
		b[i] = b[j];
		b[j] = temp;
	}
}
