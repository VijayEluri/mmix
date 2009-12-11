/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f2.perm;

import java.util.Arrays;

import util.MathUtil;

/**
 * <p>
 * PermutationGenerator.java
 * </p>
 * 
 */
public class PermutationGenerator {
	/**
	 * generate all the perms of [1,2,...,n] Home Grow version.
	 * 
	 * @param n
	 * @param res
	 *            n! * n
	 */
	public static void perm(int n, int[][] res) {
		if (n == 2) {
			res[0][0] = 1;
			res[0][1] = 2;
			res[1][0] = 2;
			res[1][1] = 1;
			return;
		}
		int[][] temp = new int[MathUtil.factorial(n - 1)][n - 1];
		perm(n - 1, temp);

		for (int i = 0; i < temp.length; i++) {
			int base = i * n;
			for (int j = 0; j < n; j++) {
				System.arraycopy(temp[i], 0, res[base + j], 0, n - 1 - j);
				System.arraycopy(temp[i], (n - 1 - j), res[base + j], (n - j),
						j);
				res[base + j][(n - 1 - j)] = n;
			}
		}
	}

	/**
	 * get the perms by Adjacent interchange changes
	 * 
	 * @param n
	 * @param res
	 */
	public static void permPlainChangeWithoutSwitchTable(int n, int[][] res) {
		if (n == 2) {
			res[0][0] = 1;
			res[0][1] = 2;
			res[1][0] = 2;
			res[1][1] = 1;
			return;
		}
		int[][] temp = new int[MathUtil.factorial(n - 1)][n - 1];
		permPlainChangeWithoutSwitchTable(n - 1, temp);

		boolean reverse = false;
		for (int i = 0; i < temp.length; i++) {
			int base = i * n;
			if (reverse) {
				int loc = 0;
				for (int j = 0; j < n; j++) {
					loc = base + n - 1 - j;// put into sublist of n elements
											// reversely.
					System.arraycopy(temp[i], 0, res[loc], 0, n - 1 - j);
					System
							.arraycopy(temp[i], (n - 1 - j), res[loc], (n - j),
									j);
					res[loc][(n - 1 - j)] = n;
				}
			} else {
				for (int j = 0; j < n; j++) {
					System.arraycopy(temp[i], 0, res[base + j], 0, n - 1 - j);
					System.arraycopy(temp[i], (n - 1 - j), res[base + j],
							(n - j), j);
					res[base + j][(n - 1 - j)] = n;
				}
			}
			reverse = !reverse;
		}
	}

	/**
	 * get the perms by Adjacent interchange changes
	 * 
	 * @param n
	 * @param res
	 */
	public static void permPlainChange(int n, int[][] res) {
		permPlainChange(n, res, 1);
	}

	/**
	 * get the perms by Adjacent interchange changes
	 * 
	 * @param n
	 * @param res
	 */
	public static void permPlainChange(int n, int[][] res, int indexFrom) {
		int nn = MathUtil.factorial(n);
		int[] tab = new int[nn];
		getSwitchTable(n, tab);
		//System.out.println(Arrays.toString(tab));

		int[] init = new int[n];
		for (int i = 0; i < n; i++) {
			init[i] = i + indexFrom;
		}
		// bug one: forget init res[0]
		System.arraycopy(init, 0, res[0], 0, n);

		int posl = 0;// inclusive
		int posr = 0;// inclusive
		int copyl = 0;
		int copyr = 0;
		int temp;
		for (int i = 0; i < nn - 1; i++) {
			posl = tab[i] - 2;
			posr = tab[i] + 1;
			copyl = posl + 1;
			copyr = n - posr;// (n - 1) - posr + 1;
			// copy left part
			if (copyl > 0) {
				System.arraycopy(init, 0, res[i + 1], 0, copyl);
			}
			// swap when copy;
			res[i + 1][posl + 1] = init[posr - 1];
			res[i + 1][posr - 1] = init[posl + 1];

			// copy right part
			if (copyr > 0) {
				// bug: System.arraycopy(init, posr, res[i + 1], 0, copyr);
				System.arraycopy(init, posr, res[i + 1], posr, copyr);
			}
			System.arraycopy(res[i + 1], 0, init, 0, n);
		}
	}

	/**
	 * get the perms by Adjacent interchange changes
	 * 
	 * @param n
	 * @param res
	 */
	public static void permPlainChange(int n, int indexFrom, Action act) {
		int[] res = new int[n];
		int nn = MathUtil.factorial(n);
		int[] tab = new int[nn];
		getSwitchTable(n, tab);
		// System.out.println(Arrays.toString(tab));

		int[] init = new int[n];
		for (int i = 0; i < n; i++) {
			init[i] = i + indexFrom;
		}
		// bug one: forget init res[0]
		// System.arraycopy(init, 0, res, 0, n);
		act.actOn(init);

		int posl = 0;// inclusive
		int posr = 0;// inclusive
		int copyl = 0;
		int copyr = 0;
		int temp;
		for (int i = 0; i < nn - 1; i++) {
			posl = tab[i] - 2;
			posr = tab[i] + 1;
			copyl = posl + 1;
			copyr = n - posr;// (n - 1) - posr + 1;
			// copy left part
			if (copyl > 0) {
				System.arraycopy(init, 0, res, 0, copyl);
			}
			// swap when copy;
			res[posl + 1] = init[posr - 1];
			res[posr - 1] = init[posl + 1];

			// copy right part
			if (copyr > 0) {
				// bug: System.arraycopy(init, posr, res[i + 1], 0, copyr);
				System.arraycopy(init, posr, res, posr, copyr);
			}
			System.arraycopy(res, 0, init, 0, n);
			act.actOn(res);
			// if(i%10000==0){
			// System.out.println(Arrays.toString(res));
			// }
		}
	}

	/**
	 * 
	 * @param n
	 * @param indexFrom
	 * @param act
	 */
	public static void permPlainChangeOptimize(int n, int indexFrom, Action act) {
		int[] res = new int[n];
		int nn = MathUtil.factorial(n);
		int[] tab = new int[nn];
		getSwitchTable(n, tab);
		// System.out.println(Arrays.toString(tab));

		int[] init = new int[n];
		for (int i = 0; i < n; i++) {
			init[i] = i + indexFrom;
		}
		act.actOn(init);

		int posl = 0;// inclusive
		int posr = 0;// inclusive
		
		int temp;
		for (int i = 0; i < nn - 1; i++) {
			posl = tab[i] - 1;
			posr = tab[i] ;				
			temp = init[posr];
			
			//swap
			init[posr ] = init[posl ];
			init[posl ] = temp;

			act.actOn(init);
		}
	}

	/**
	 * Home Grow version. it is wrong. it will fail when case = 5
	 * 
	 * @deprecated
	 * @param n
	 * @param tab
	 * 
	 *            elements are in the range [1, n-1]; value i means interchange
	 *            i and i+1 in plain change; size of tab is n!. the last element
	 *            is always 1.
	 */
	public static void getSwitchTableHomeGrown(int n, int[] tab) {
		int[] des = new int[n];
		int[] asc = new int[n];
		for (int i = 0; i < n - 1; i++) {
			des[i] = n - 1 - i;
			asc[i] = i + 1;
		}
		des[n - 1] = n - 1;
		asc[n - 1] = 1;

		boolean reverse = false;
		for (int i = 0; i < MathUtil.factorial(n - 1); i++) {
			if (reverse) {
				System.arraycopy(asc, 0, tab, i * n, n);
			} else {
				System.arraycopy(des, 0, tab, i * n, n);
			}
			reverse = !reverse;
		}
	}

	/**
	 * Improved version according to more self-study
	 * 
	 * @param n
	 * @param tab
	 */
	public static void getSwitchTable(int n, int[] tab) {
//		if(n==1){
//			tab[0]=1;
//		}
		if (n == 2) {
			tab[0] = 1;
			tab[1] = 1;
			return;
		}
		int[] des = new int[n];
		int[] asc = new int[n];
		for (int i = 0; i < n - 1; i++) {
			des[i] = n - 1 - i;
			asc[i] = i + 1;
		}
		// Bug:
		// des[n - 1] = n - 1;
		// asc[n - 1] = 1;
		int[] temp = new int[MathUtil.factorial(n - 1)];
		getSwitchTable(n - 1, temp);

		boolean reverse = false;
		for (int i = 0; i < MathUtil.factorial(n - 1); i++) {
			if (reverse) {
				asc[n - 1] = temp[i];
				System.arraycopy(asc, 0, tab, i * n, n);
			} else {
				des[n - 1] = temp[i] + 1;
				System.arraycopy(des, 0, tab, i * n, n);
			}
			reverse = !reverse;
		}
	}

	/**
	 * According to the book.
	 * currently is wrong . not updated yet.
	 * @param n
	 * @param tab
	 */
	public static void getSwitchTableOfficial(int n, int[] tab) {

	}
}
