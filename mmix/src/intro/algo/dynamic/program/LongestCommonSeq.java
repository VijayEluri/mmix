/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.dynamic.program;

/**
 * <p>
 * LongestCommonSeq.java
 * </p>
 */
public class LongestCommonSeq {
	public static void main(String[] args) {
		char[] X = new char[] { ' ', 'A', 'B', 'C', 'B', 'D', 'A', 'B' };
		char[] Y = new char[] { ' ', 'B', 'D', 'C', 'A', 'B', 'A' };
		int m = X.length;
		int n = Y.length;
		int[][] c = new int[m][n];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (i == 0 || j == 0) {
					c[i][j] = 0;
				} else if (X[i] == Y[j]) {
					c[i][j] = c[i - 1][j - 1] + 1;
				} else {
					int a = c[i - 1][j];
					int b = c[i][j - 1];
					if (a > b) {
						c[i][j] = a;
					} else {
						c[i][j] = b;
					}
				}
			}
		}
		System.out.println(c[m - 1][n - 1]);
	}
}
