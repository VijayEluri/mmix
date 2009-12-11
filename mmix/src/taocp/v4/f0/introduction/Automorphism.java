/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.introduction;

import java.util.Arrays;

import taocp.v4.f2.perm.PermutationGenerator;
import util.MathUtil;

/**
 * try to find automorphism of regulat 3-cube. 
 * <p>
 * Automorphism.java
 * </p>
 */
public class Automorphism {
	public static void main(String[] args) {
		int count1 = new Automorphism().countAutomorphism();
		int count2 = new Automorphism().countAutomorphism_old();
		System.out.println(count1);
		System.out.println(count2);
		if (count1 != count2) {
			throw new RuntimeException();
		}
	}

	int[][] r3 = new int[][] { { 1, 2, 4, 5 }, { 2, 1, 3, 6 }, { 3, 2, 4, 7 },
			{ 4, 1, 3, 8 }, { 5, 1, 6, 8 }, { 6, 2, 5, 7 }, { 7, 3, 6, 8 },
			{ 8, 4, 5, 7 } };

	private boolean existEdge(int u, int v) {
		u = u - 1;
		int[] t = r3[u];
		for (int i = 1; i < t.length; i++) {
			if (t[i] == v)
				return true;
		}
		return false;
	}

	/**
	 * if u-v and p(u)-p(v). the P is valid automorphism because of the internal
	 * symmetries.
	 * 
	 * @return
	 */
	public int countAutomorphism() {
		int count = 1;// count ifself.

		int tab[][] = new int[MathUtil.factorial(8)][8];
		PermutationGenerator.perm(8, tab);

		//System.out.println(Arrays.toString(tab[1]));
		// [1, 2, 3, 4, 5, 6, 8, 7]

		int u, v;
		exo: for (int i = 1; i < tab.length; i++) {
			// the mapping is tab[i].
			for (int k = 0; k < r3.length; k++) {
				u = tab[i][r3[k][0] - 1];
				for (int m = 1; m < r3[0].length; m++) {
					v = tab[i][r3[k][m] - 1];
					if (!existEdge(u, v)) {
						continue exo;
					}
				}
			}
			//System.out.println("i=" + i + "; " + Arrays.toString(tab[i]));
			count++;
		}
		return count;

	}

	/**
	 * The idea is corrent, but there are some flaws in original implementation.
	 * The flaw has been fixed after the success of another new implemenation.
	 *  
	 * This kind of change is equal to the permutation
	 * of the vertices. each is a isophism, but may also be also a automorphism
	 * if it have same adjacent matrix.
	 * 	
	 * @return
	 */
	public int countAutomorphism_old() {
		int count = 1;

		int tab[][] = new int[MathUtil.factorial(8)][8];
		PermutationGenerator.perm(8, tab);

		System.out.println(Arrays.toString(tab[40319]));

		int[][] temp = new int[8][4];
		int[][] temp2 = new int[8][4];
		outer: for (int i = 1; i < tab.length; i++) {
			// the mapping is tab[i].
			for (int k = 0; k < r3.length; k++) {
				for (int m = 0; m < r3[0].length; m++) {
					temp[k][m] = tab[i][r3[k][m] - 1];
				}
			}
			// sorting accordint to first element.
			for (int k = 0; k < r3.length; k++) {
				Arrays.sort(temp[k], 1, temp[k].length);
				temp2[temp[k][0] - 1] = temp[k];

				if (i == 40319) {
					System.out.println(Arrays.toString(temp[k]));
					System.out.println(Arrays.toString(temp2[k]));
				}
			}

			for (int k = 0; k < r3.length; k++) {
				for (int m = 0; m < r3[0].length; m++) {
					if (temp2[k][m] != r3[k][m]) {
						continue outer;
					}
				}
			}

			count++;
		}
		return count;
	}
}
