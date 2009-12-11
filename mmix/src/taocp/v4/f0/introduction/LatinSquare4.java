package taocp.v4.f0.introduction;

import java.util.Arrays;

import util.MathUtil;

/**
 * find solution for Latin Square (16!) = 2004189184 real count=576 it is clear
 * that576*576<16!
 * 
 * we found the solution when i=0; j=13. we can easily find the solution.
 * 
 * 
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class LatinSquare4 {
	public static void main(String[] args) {
		new LatinSquare4().find4();
	}

	public void find4() {

		int[] latinRow = new int[4];
		for (int i = 0; i < 4; i++) {
			latinRow[i] = i;
		}
		int[][] perms = MathUtil.getPermutation(latinRow);

		System.out.println("perms.length=" + perms.length);
		int count = perms.length * perms.length * perms.length * perms.length;
		System.out.println("count=" + count);
		
		int[][][] latin = new int[count][4][4];

		count = 0;
		// get perums between rows
		for (int i = 0; i < perms.length; i++) {
			for (int j = 0; j < perms.length; j++) {
				for (int k = 0; k < perms.length; k++) {
					for (int l = 0; l < perms.length; l++) {

						System.arraycopy(perms[i], 0, latin[count][0], 0, 4);
						System.arraycopy(perms[j], 0, latin[count][1], 0, 4);
						System.arraycopy(perms[k], 0, latin[count][2], 0, 4);
						System.arraycopy(perms[l], 0, latin[count][3], 0, 4);
						boolean valid = true;
						boolean[][] markColumn = new boolean[4][4];
						labelVerify: for (int m = 0; m < 4; m++) {// columns
							for (int n = 0; n < 4; n++) {
								markColumn[m][latin[count][n][m]] = true;
							}
							if (markColumn[m][0] && markColumn[m][1]
									&& markColumn[m][2] && markColumn[m][3]) {

							} else {
								valid = false;
								break labelVerify;
							}
						}

						if (valid) {
							count++;
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
				boolean[][] mark = new boolean[4][4];
				boolean valid = true;
				for (int k = 0; k < 4; k++) {
					for (int l = 0; l < 4; l++) {
						mark[latin[i][k][l]][latin[j][k][l]] = true;
					}
				}
				verify: for (int k = 0; k < 4; k++) {
					for (int l = 0; l < 4; l++) {
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
			boolean mark[] = new boolean[4];
			for (int j = 0; j < 4; j++) {
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
				boolean[][] mark = new boolean[4][4];
				boolean valid = true;
				for (int k = 0; k < 4; k++) {
					for (int l = 0; l < 4; l++) {
						mark[diagonal[i][k][l]][diagonal[j][k][l]] = true;
					}
				}
				verify: for (int k = 0; k < 4; k++) {
					for (int l = 0; l < 4; l++) {
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
