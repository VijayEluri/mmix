package taocp.v4.f0.introduction;

import java.util.Arrays;

import util.MathUtil;

/**
 * verify that no solution for orthogonal latin square 6.
 * 
 * suppose there is a solution. then we can permulate on columns to let the
 * first row be 0-6 for latin square. similarly, we can permulate on rows from 1
 * to 5 to let the first column be 0-6. it sweep out a lot of calculation.
 * 
 * Then for all of these possiblities, we check one by one whethere there is a
 * othogonal grake square bu using the same strategy in case 10 for latin
 * square.
 * 
 * @author wueddie-wym-wrz
 * 
 */
/**
 * @author eddie.wu
 * 
 */
public class LatinSquare6_v2 {
	private static boolean debug = false;

	public static void main(String[] args) {
		LatinSquare6_v2.find6();
	}

	static int N = 6;

	static int[][][] latinTemp = new int[1][N][N];
	static int[][][] latin = new int[9408][N][N];
	static int count = 0;

	private static boolean isPartialValid(int rows) {

		boolean[][] markColumn = new boolean[N][N];
		for (int m = 1; m < N; m++) {// columns
			for (int nn = 0; nn < rows; nn++) {
				if (markColumn[m][latinTemp[0][nn][m]] == false) {
					markColumn[m][latinTemp[0][nn][m]] = true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static void find6() {
		// the number sequence to set in the row.
		int[][] latinRow = new int[N - 1][N - 1];
		int[][][] perms = new int[N - 1][][];
		for (int i = 0; i < N - 1; i++) {
			int count = 0;
			for (int j = 0; j < N; j++) {
				if (j == i + 1) {
					continue;
				} else {
					latinRow[i][count++] = j;
				}
			}
			if (debug)
				System.out.println(Arrays.toString(latinRow[i]));
			perms[i] = MathUtil.getPermutation(latinRow[i]);
		}
		if (debug)
			System.out.println("perms.length=" + perms[0].length);

		// partial initialize latin array. top row and left column
		for (int i = 0; i < N; i++) {
			latinTemp[0][0][i] = i;
			latinTemp[0][i][0] = i;
		}

		count = 0;
		// get perums between rows
		// ext_i: for (int i = 0; i < perms.length; i++) {
		// System.arraycopy(perms[i], 0, latin[0][0], 0, N);
		ext_j: for (int j = 0; j < perms[0].length; j++) {
			System.arraycopy(perms[0][j], 0, latinTemp[0][1], 1, N - 1);
			if (!isPartialValid(2))
				continue;

			ext_k: for (int k = 0; k < perms[1].length; k++) {
				System.arraycopy(perms[1][k], 0, latinTemp[0][2], 1, N - 1);
				if (!isPartialValid(3))
					continue;
				// if(debug)System.out.println("k=" + k);
				// if(debug)System.out.println("real count=" + count);
				ext_l: for (int l = 0; l < perms[2].length; l++) {
					System.arraycopy(perms[2][l], 0, latinTemp[0][3], 1, N - 1);
					if (!isPartialValid(4))
						continue;
					ext_m: for (int m = 0; m < perms[3].length; m++) {
						System.arraycopy(perms[3][m], 0, latinTemp[0][4], 1,
								N - 1);
						if (!isPartialValid(5))
							continue;
						ext_n: for (int n = 0; n < perms[4].length; n++) {
							System.arraycopy(perms[4][n], 0, latinTemp[0][5],
									1, N - 1);
							if (!isPartialValid(6))
								continue;
							else {
								for (int oo = 0; oo < N; oo++) {
									System.arraycopy(latinTemp[0][oo], 0,
											latin[count][oo], 0, N);
								}
								count++;

							}
						}
					}
				}
			}
			// }
		}
		if (debug)
			System.out.println("real count=" + count);
		for (int i = 0; i < N; i++) {
			if (debug)
				System.out.println(Arrays.toString(latin[(count - 2)][i]));
		}
//		for (int i = 0; i < count; i++) {
//			System.out.println("i=" + i + "result:"
//					+ new LatinSquare(6).findOthogonal(latin[i], 6));
//		}
	}
	// // combine two square to check the result;
	// combine: for (int i = 0; i < count; i++) {
	// System.out.println("i=" + i);
	// for (int j = 0; j < count; j++) {
	// boolean[][] mark = new boolean[N][N];
	// boolean valid = true;
	// for (int k = 0; k < N; k++) {
	// for (int l = 0; l < N; l++) {
	// mark[latin[i][k][l]][latin[j][k][l]] = true;
	// }
	// }
	// verify: for (int k = 0; k < N; k++) {
	// for (int l = 0; l < N; l++) {
	// if (mark[k][l] == false) {
	// valid = false;
	// break verify;
	// }
	// }
	// }
	// if (valid) {
	//
	// System.out.println(Arrays.toString(latin[i][0]));
	// System.out.println(Arrays.toString(latin[i][1]));
	// System.out.println(Arrays.toString(latin[i][2]));
	// System.out.println(Arrays.toString(latin[i][3]));
	// System.out.println("j=" + j);
	// System.out.println(Arrays.toString(latin[j][0]));
	// System.out.println(Arrays.toString(latin[j][1]));
	// System.out.println(Arrays.toString(latin[j][2]));
	// System.out.println(Arrays.toString(latin[j][3]));
	// break combine;
	// }
	// }
	// }
	//
	// // find the one with diagonal enumeration.
	// int[][][] diagonal = new int[latin.length][][];
	// int diagonalC = 0;
	// for (int i = 0; i < count; i++) {
	// boolean mark[] = new boolean[N];
	// for (int j = 0; j < N; j++) {
	// mark[latin[i][j][j]] = true;
	// }
	// if (mark[0] && mark[1] && mark[2] && mark[3]) {
	// diagonal[diagonalC] = latin[i];
	// diagonalC++;
	// }
	// }
	// System.out.println("diagonalC count=" + diagonalC);
	// System.out.println(Arrays.toString(diagonal[diagonalC - 2][2]));
	//
	// // combine two square to check the result;
	// combine: for (int i = 0; i < diagonalC; i++) {
	// System.out.println("i=" + i);
	// for (int j = 0; j < diagonalC; j++) {
	// boolean[][] mark = new boolean[N][N];
	// boolean valid = true;
	// for (int k = 0; k < N; k++) {
	// for (int l = 0; l < N; l++) {
	// mark[diagonal[i][k][l]][diagonal[j][k][l]] = true;
	// }
	// }
	// verify: for (int k = 0; k < N; k++) {
	// for (int l = 0; l < N; l++) {
	// if (mark[k][l] == false) {
	// valid = false;
	// break verify;
	// }
	// }
	// }
	// if (valid) {
	//
	// System.out.println(Arrays.toString(diagonal[i][0]));
	// System.out.println(Arrays.toString(diagonal[i][1]));
	// System.out.println(Arrays.toString(diagonal[i][2]));
	// System.out.println(Arrays.toString(diagonal[i][3]));
	// System.out.println("j=" + j);
	// System.out.println(Arrays.toString(diagonal[j][0]));
	// System.out.println(Arrays.toString(diagonal[j][1]));
	// System.out.println(Arrays.toString(diagonal[j][2]));
	// System.out.println(Arrays.toString(diagonal[j][3]));
	// break combine;
	// }
	// }
	// }
	//
	// }
	/**
	 * 
	 [0, 1, 2, 3, 4, 5] [1, 5, 4, 2, 3, 0] [2, 4, 5, 1, 0, 3] [3, 2, 1, 0, 5,
	 * 4] [4, 3, 0, 5, 1, 2] [5, 0, 3, 4, 2, 1]
	 * 
	 * 
	 */
	
	

}
