/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f2.perm;

import java.util.Arrays;

import util.ArrayUtil;
import util.MathUtil;

import junit.framework.TestCase;

/**
 * <p>
 * TestPermutation.java
 * </p>
 */
public class TestPermutation extends TestCase {
	int[][] res;
	int[][] res2;

	public void test() {
		res = new int[2][2];
		PermutationGenerator.perm(2, res);
		ArrayUtil.output2DArray(res);

		res = new int[6][3];
		PermutationGenerator.perm(3, res);
		ArrayUtil.output2DArray(res);

		res = new int[24][4];
		PermutationGenerator.perm(4, res);
		ArrayUtil.output2DArray(res);

		res = new int[120][5];
		PermutationGenerator.perm(5, res);
		ArrayUtil.output2DArray(res);

		res = new int[720][6];
		PermutationGenerator.perm(6, res);
		// ArrayUtil.output2DArray(res);

	}

	public void testPlainChange2() {
		int n = 3;
		int nn = MathUtil.factorial(n);
		res = new int[nn][n];
		PermutationGenerator.permPlainChange(n, res);
		ArrayUtil.output2DArray(res);
	}

	public void testPlainChange() {
		res = new int[2][2];
		PermutationGenerator.permPlainChange(2, res);
		ArrayUtil.output2DArray(res);

		res = new int[6][3];
		PermutationGenerator.permPlainChange(3, res);
		ArrayUtil.output2DArray(res);

		res = new int[24][4];
		PermutationGenerator.permPlainChange(4, res);
		ArrayUtil.output2DArray(res);

		res = new int[120][5];
		PermutationGenerator.permPlainChange(5, res);
		ArrayUtil.output2DArray(res);

		res = new int[720][6];
		PermutationGenerator.permPlainChange(6, res);
		// ArrayUtil.output2DArray(res);

	}

	public void testPlainChangeWithoutSwitchTable() {
		res = new int[2][2];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(2, res);
		ArrayUtil.output2DArray(res);

		res = new int[6][3];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(3, res);
		ArrayUtil.output2DArray(res);

		res = new int[24][4];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(4, res);
		ArrayUtil.output2DArray(res);

		res = new int[120][5];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(5, res);
		ArrayUtil.output2DArray(res);

		res = new int[720][6];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(6, res);
		// ArrayUtil.output2DArray(res);

	}

	public void testTwoPermByComparison() {
		res = new int[2][2];
		res2 = new int[2][2];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(2, res);
		ArrayUtil.output2DArray(res);
		PermutationGenerator.permPlainChange(2, res2);
		for (int i = 0; i < res.length; i++) {
			assertTrue(Arrays.equals(res[i], res2[i]));
		}

		res = new int[6][3];
		res2 = new int[6][3];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(3, res);
		ArrayUtil.output2DArray(res);
		PermutationGenerator.permPlainChange(3, res2);
		ArrayUtil.output2DArray(res2);
		for (int i = 0; i < res.length; i++) {
			assertTrue(Arrays.equals(res[i], res2[i]));
		}

		res = new int[24][4];
		res2 = new int[24][4];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(4, res);
		ArrayUtil.output2DArray(res);
		PermutationGenerator.permPlainChange(4, res2);
		for (int i = 0; i < res.length; i++) {
			assertTrue(Arrays.equals(res[i], res2[i]));
		}

		res = new int[120][5];
		res2 = new int[120][5];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(5, res);
		ArrayUtil.output2DArray(res);
		PermutationGenerator.permPlainChange(5, res2);// break here.
		for (int i = 0; i < res.length; i++) {
			if (!Arrays.equals(res[i], res2[i])) {
				System.out.println("i=" + i);
				System.out.println(Arrays.toString(res[i]));
				System.out.println(Arrays.toString(res2[i]));
				assertTrue(false);
			}
		}

		res = new int[720][6];
		res2 = new int[720][6];
		PermutationGenerator.permPlainChangeWithoutSwitchTable(6, res);
		PermutationGenerator.permPlainChange(6, res2);
		for (int i = 0; i < res.length; i++) {
			assertTrue(Arrays.equals(res[i], res2[i]));
		}

	}

	public void test10() {
		res = new int[MathUtil.factorial(10)][10];
		PermutationGenerator.perm(10, res);
		// ArrayUtil.output2DArray(res);
	}

	public void test9() {
		res = new int[MathUtil.factorial(9)][9];
		PermutationGenerator.perm(9, res);
		// ArrayUtil.output2DArray(res);
	}

	public void testGetSwitchTable() {
		int[] tab = new int[MathUtil.factorial(2)];
		PermutationGenerator.getSwitchTable(2, tab);
		System.out.println(Arrays.toString(tab));

		tab = new int[MathUtil.factorial(3)];
		PermutationGenerator.getSwitchTable(3, tab);
		System.out.println(Arrays.toString(tab));

		tab = new int[MathUtil.factorial(4)];
		PermutationGenerator.getSwitchTable(4, tab);
		System.out.println(Arrays.toString(tab));

		long start, end;
		tab = new int[MathUtil.factorial(10)];
		start = System.currentTimeMillis();		
		PermutationGenerator.getSwitchTable(10, tab);
		end = System.currentTimeMillis();
		System.out.println("it takes: " + (end - start)
				+ " miliseconds to getSwitchTable.");
		for(int i=0; i< 30; i++){
			System.out.print(tab[i]);
		}
		System.out.println();
		//1 9 8 7 6 5 4 3 2 1 9 1 2 3 4 5 6 7 8 9 7 9 8 7 6 5 4 3 2 1
		for(int i=0; i< 30; i++){
			System.out.print(tab[tab.length-1-i]);
		}
	}
	
	public void testForSalesman(){
		res = new int[720][6];
		PermutationGenerator.permPlainChange(6, res,0);
		System.out.println(Arrays.deepToString(res));
		
	}
}
