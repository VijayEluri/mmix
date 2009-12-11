package taocp.v4.f0.introduction;

import java.util.Arrays;

import util.MathUtil;

/**
 * find solution for Latin Square 6 since the complexity get huge very quickly,
 * the number may exceeds int or long easily, and it may require differnt
 * apporach for different n so it is not so important to have a general program
 * for all the case.
 * 
 * because the combinatorics algorithm's characteristics.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class LatinSquare6 {
	public static void main(String[] args) {
		LatinSquare6.find6();
	}

	static int N = 6;

	static int[][][] latin = new int[1][N][N];

	static int count = 0;

	private static boolean isValid(int rows) {

		boolean[][] markColumn = new boolean[N][N];
		for (int m = 0; m < N; m++) {// columns
			for (int nn = 0; nn < rows; nn++) {
				if (markColumn[m][latin[0][nn][m]] == false) {
					markColumn[m][latin[0][nn][m]] = true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static void find6() {

		int[] latinRow = new int[N];
		for (int i = 0; i < N; i++) {
			latinRow[i] = i;
		}
		int[][] perms = MathUtil.getPermutation(latinRow);

		System.out.println("perms.length=" + perms.length);

		// for (int i = 0; i < N; i++) {
		// count*=perms.length;
		// }
		// System.out.println("count=" + count);
		// int[][][] latin = new int[count][N][N];

		count = 0;
		// get perums between rows
		ext_i: for (int i = 0; i < perms.length; i++) {
			System.arraycopy(perms[i], 0, latin[0][0], 0, N);
			ext_j: for (int j = 0; j < perms.length; j++) {
				System.arraycopy(perms[j], 0, latin[0][1], 0, N);
				if (!isValid(2))
					continue;
				System.out.println("j=" + j);
				System.out.println("real count=" + count);
				ext_k: for (int k = 0; k < perms.length; k++) {
					System.arraycopy(perms[k], 0, latin[0][2], 0, N);
					if (!isValid(3))
						continue;
					ext_l: for (int l = 0; l < perms.length; l++) {
						System.arraycopy(perms[l], 0, latin[0][3], 0, N);
						if (!isValid(4))
							continue;
						ext_m: for (int m = 0; m < perms.length; m++) {
							System
									.arraycopy(perms[m], 0, latin[0][4], 0,
											N);
							if (!isValid(5))
								continue;
							ext_n: for (int n = 0; n < perms.length; n++) {
								System.arraycopy(perms[n], 0, latin[0][5],
										0, N);
								if (!isValid(6))
									continue;
								else {
									count++;
								}
							}
						}
					}
				}
			}
		}
		System.out.println("real count=" + count);
		System.out.println(Arrays.toString(latin[count - 2][2]));

		// combine two square to check the result;
		combine: for (int i = 0; i < count; i++) {
			System.out.println("i=" + i);
			for (int j = 0; j < count; j++) {
				boolean[][] mark = new boolean[N][N];
				boolean valid = true;
				for (int k = 0; k < N; k++) {
					for (int l = 0; l < N; l++) {
						mark[latin[i][k][l]][latin[j][k][l]] = true;
					}
				}
				verify: for (int k = 0; k < N; k++) {
					for (int l = 0; l < N; l++) {
						if (mark[k][l] == false) {
							valid = false;
							break verify;
						}
					}
				}
				if (valid) {

					System.out.println(Arrays.toString(latin[i][0]));
					System.out.println(Arrays.toString(latin[i][1]));
					System.out.println(Arrays.toString(latin[i][2]));
					System.out.println(Arrays.toString(latin[i][3]));
					System.out.println("j=" + j);
					System.out.println(Arrays.toString(latin[j][0]));
					System.out.println(Arrays.toString(latin[j][1]));
					System.out.println(Arrays.toString(latin[j][2]));
					System.out.println(Arrays.toString(latin[j][3]));
					break combine;
				}
			}
		}

		// find the one with diagonal enumeration.
		int[][][] diagonal = new int[latin.length][][];
		int diagonalC = 0;
		for (int i = 0; i < count; i++) {
			boolean mark[] = new boolean[N];
			for (int j = 0; j < N; j++) {
				mark[latin[i][j][j]] = true;
			}
			if (mark[0] && mark[1] && mark[2] && mark[3]) {
				diagonal[diagonalC] = latin[i];
				diagonalC++;
			}
		}
		System.out.println("diagonalC count=" + diagonalC);
		System.out.println(Arrays.toString(diagonal[diagonalC - 2][2]));

		// combine two square to check the result;
		combine: for (int i = 0; i < diagonalC; i++) {
			System.out.println("i=" + i);
			for (int j = 0; j < diagonalC; j++) {
				boolean[][] mark = new boolean[N][N];
				boolean valid = true;
				for (int k = 0; k < N; k++) {
					for (int l = 0; l < N; l++) {
						mark[diagonal[i][k][l]][diagonal[j][k][l]] = true;
					}
				}
				verify: for (int k = 0; k < N; k++) {
					for (int l = 0; l < N; l++) {
						if (mark[k][l] == false) {
							valid = false;
							break verify;
						}
					}
				}
				if (valid) {

					System.out.println(Arrays.toString(diagonal[i][0]));
					System.out.println(Arrays.toString(diagonal[i][1]));
					System.out.println(Arrays.toString(diagonal[i][2]));
					System.out.println(Arrays.toString(diagonal[i][3]));
					System.out.println("j=" + j);
					System.out.println(Arrays.toString(diagonal[j][0]));
					System.out.println(Arrays.toString(diagonal[j][1]));
					System.out.println(Arrays.toString(diagonal[j][2]));
					System.out.println(Arrays.toString(diagonal[j][3]));
					break combine;
				}
			}
		}

	}

}
