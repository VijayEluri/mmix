/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f2.perm;

import java.util.Arrays;

/**
 * <p>
 * Alphamatics.java
 * </p>
 * applications of permutation in a sample alphamatics. 1534 + 1723 = 3257
 * 1,2,3,4,5,7 is map to A,B,C,D,E,F so the puzzle is AECD + AFBC = CBEF
 * 
 */
public class Alphamatics implements Action {
	// int[] x = new int[]{2000,-90,-989,1,90,99};
	static int[] x = new int[6];
	static {
		x[0] = 2000;
		x[1] = -90;
		x[2] = -989;
		x[3] = 1;
		x[4] = 90;
		x[5] = 99;
		int count = x[0] + 2*x[1] + 3*x[2]+4*x[3]+5*x[4]+7*x[5];
		if(count!=0){
			System.out.println("init error: count="+count);
		}
	}

	int[] y = new int[10];

	public static void main(String[] args) {

		// int[][] comb = new int[MathUtil.factorial(10)][10];
		PermutationGenerator.permPlainChange(10, 0, new Alphamatics());

	}

	
	public void actOn(int[] perm) {
		if(perm[0]==0 || perm[2]==0){
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
