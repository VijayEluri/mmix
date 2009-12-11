package intro.algo.dynamic.program;

import java.util.Arrays;

public class OptimalBinarySearchTree {
	public static void main(String[] args) {
		int n = 5;
		// probobility is converted to integer of percentage
		int[] p = new int[] { 0, 15, 10, 5, 10, 20 };// p[0] is not used.
		int[] q = new int[] { 5, 10, 5, 5, 5, 10 };

		int[][] e = new int[n + 2][n + 1];// e[0] is not used
		int[][] root = new int[n + 2][n + 1];// e[0] is not used
		int[][] w = new int[n + 2][n + 1];// w[0] is not used

		//
		for (int i = 1; i <= n + 1; i++) {
			e[i][i - 1] = q[i - 1];
			w[i][i - 1] = q[i - 1];
		}

		for (int L = 1; L <= n; L++) {// number of diagonals to fill
			for (int i = 1; i <= n - L + 1; i++) {// cover all the rows. row
													// will decrease
				int j = i + L - 1;
				e[i][j] = Integer.MAX_VALUE;
				w[i][j] = w[i][j - 1] + p[j] + q[j];
				for (int r = i; r <= j; r++) {
					int t = e[i][r - 1] + e[r + 1][j] + w[i][j];
					if (t < e[i][j]) {
						e[i][j] = t;
						root[i][j] = r;
					}

				}
			}
		}
		System.out.println(Arrays.deepToString(e));
		System.out.println(Arrays.deepToString(w));
		System.out.println("cost is " + e[1][n]);
	}
}
