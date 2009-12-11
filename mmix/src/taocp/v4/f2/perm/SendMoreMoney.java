/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f2.perm;

import java.util.Arrays;

import util.MathUtil;

/**
 * <p>
 * SEND + MORE = MONEY
 * </p>
 * sequence of char: S, E, N, D, M, O, R, Y consider expression SEND + MORE -
 * MONEY
 * 
 * pure solution. they are map to 9, 5, 6, 7, 1, 0, 8, 2, 9567 + 1085 = 10652
 */
public class SendMoreMoney implements Action {
	// int[] x = new int[]{2000,-90,-989,1,90,99};
	static int[] x = new int[8];
	static {
		x[0] = 1000;
		x[1] = 91;
		x[2] = -90;
		x[3] = 1;
		x[4] = -9000;
		x[5] = -900;
		x[6] = 10;
		x[7] = -1;
		int count = x[0] * 9 + 5 * x[1] + 6 * x[2] + 7 * x[3] + 1 * x[4] + 0
				* x[5] + 8 * x[6] + 2 * x[7];
		if (count != 0) {
			System.out.println("init error: count=" + count);
		}
		int[] tt = new int[] { 9, 3, 4, 7, 1, 0, 8, 5, 2, 6 };
		for (int ii = 0; ii < x.length; ii++) {
			count += tt[ii] * x[ii];
		}
		if (count != 0) {
			System.out.println("init error: count=" + count);
		}
	}

	int[] y = new int[10];

	/**
	 * it takes: 1000 miliseconds. v.s. it takes: 266 miliseconds v.s. it takes:
	 * 265 miliseconds.. so 1) maintaining a list of all permutation is not a
	 * good idea. we 'd better be able to generate on the fly. 2) funciton call
	 * overhead is not existed at all in Java.
	 * 
	 * in C real 0m0.21s user 0m0.20s sys 0m0.01s
	 * It seems the Java is catching up on the performance, how could it be?
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long start;
		long end;

		start = System.currentTimeMillis();
		PermutationGenerator.permPlainChange(10, 0, new SendMoreMoney());
		end = System.currentTimeMillis();
		System.out.println("it takes: " + (end - start) + " miliseconds.");

		start = System.currentTimeMillis();
		PermutationGenerator
				.permPlainChangeOptimize(10, 0, new SendMoreMoney());
		end = System.currentTimeMillis();
		System.out.println("it takes: " + (end - start) + " miliseconds.");

		// embeded: seems no use. it show java did a good job in
		// method invocation optimization;
		start = System.currentTimeMillis();
		new SendMoreMoney().embed(10, 0);
		end = System.currentTimeMillis();
		System.out.println("it takes: " + (end - start) + " miliseconds.");
	}

	public void embed(int n, int indexFrom) {

		int nn = MathUtil.factorial(n);
		int[] tab = new int[nn];
		PermutationGenerator.getSwitchTable(n, tab);
		// System.out.println(Arrays.toString(tab));

		int[] init = new int[n];
		for (int i = 0; i < n; i++) {
			init[i] = i + indexFrom;
		}

		int count;
		if (init[0] == 0 || init[4] == 0) {

		} else {
			count = 0;
			for (int ii = 0; ii < x.length; ii++) {
				count += init[ii] * x[ii];
			}
			if (count == 0) {
				System.out.println("answer found:");
				System.out.println(Arrays.toString(init));
			}
		}

		int posl = 0;// inclusive
		int posr = 0;// inclusive

		int temp;
		for (int i = 0; i < nn - 1; i++) {
			posl = tab[i] - 1;
			posr = tab[i];
			temp = init[posr];

			// swap
			init[posr] = init[posl];
			init[posl] = temp;

			if (init[0] == 0 || init[4] == 0) {
				continue;
			}
			count = 0;
			for (int ii = 0; ii < x.length; ii++) {
				count += init[ii] * x[ii];
			}
			if (count == 0) {
				System.out.println("answer found:");
				System.out.println(Arrays.toString(init));
			}
		}
	}

	
	public void actOn(int[] perm) {
		if (perm[0] == 0 || perm[4] == 0) {
			return;
		}

		int count = 0;
		for (int i = 0; i < x.length; i++) {
			count += perm[i] * x[i];
		}
		if (count == 0) {
			System.out.println("answer found:");
			System.out.println(Arrays.toString(perm));
		}

	}

}
