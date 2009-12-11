/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.introduction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * For a random choosed 10*10 latin square, check whether there is a solution.
 * Based on idea of Transversals, we can get the answer in several minutes. good
 * ideas really make sense!
 * <p>
 * The result we found is M = <br>
 * [0, 2, 8, 5, 9, 4, 7, 3, 6, 1]<br>
 * [1, 7, 4, 9, 3, 6, 5, 0, 2, 8]<br>
 * [2, 5, 6, 4, 8, 7, 0, 1, 9, 3]<br>
 * [3, 6, 9, 0, 4, 5, 8, 2, 1, 7]<br>
 * [4, 8, 1, 7, 5, 3, 6, 9, 0, 2]<br>
 * [5, 1, 7, 8, 0, 2, 9, 4, 3, 6]<br>
 * [6, 9, 0, 2, 7, 1, 3, 8, 4, 5]<br>
 * [7, 3, 5, 1, 2, 0, 4, 6, 8, 9]<br>
 * [8, 0, 2, 3, 6, 9, 1, 7, 5, 4]<br>
 * [9, 4, 3, 6, 1, 8, 2, 5, 7, 0]
 * </p>
 * When I come back to have a look on the code, I find it is very very hard to
 * understand. original code is messed up. I need to read the book again and try
 * to decrept the algorithm. sign!
 * 
 * The code need to be re-organized, because we also want to use it for n=6. and
 * verify that all the latin suares of n = 6 do not have a mate (orthogonal
 * square).
 */
public class LatinSquare {
	/**
	 * A random choosed 10*10 latin square is used to check whether we can find
	 * a orthogonal square for it.
	 */
	public static int[][] Random_L = new int[][] {
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, { 1, 8, 3, 2, 5, 4, 7, 6, 9, 0 },
			{ 2, 9, 5, 6, 3, 0, 8, 4, 7, 1 }, { 3, 7, 0, 9, 8, 6, 1, 5, 2, 4 },
			{ 4, 6, 7, 5, 2, 9, 0, 8, 1, 3 }, { 5, 0, 9, 4, 7, 8, 3, 1, 6, 2 },
			{ 6, 5, 4, 7, 1, 3, 2, 9, 0, 8 }, { 7, 4, 1, 8, 0, 2, 9, 3, 5, 6 },
			{ 8, 3, 6, 0, 9, 1, 5, 2, 4, 7 }, { 9, 2, 8, 1, 6, 7, 4, 0, 3, 5 } };

	// int[][] L;

	private static boolean debug = true;

	// private int N = 10;
	//
	// private int[][] M = new int[N][N];
	//
	// private List<List<Integer[]>> ll = new ArrayList<List<Integer[]>>(N);
	//
	// int[] indexInList = new int[N];// for each level
	//
	// // List<Integer[]> indexList = new ArrayList<Integer[]>();
	// int[] indexInListCopy = new int[N];
	//
	// boolean solution = false;

	// private int listIndex = 0;

	// public LatinSquare() {
	// M = new int[N][N];
	// indexInList = new int[N];// for each level
	// indexInListCopy = new int[N];
	// ll = new ArrayList<List<Integer[]>>(N);
	// // listIndex = 0;
	// L = Random_L;
	// }

	public static void main(String[] args) {
		LatinSquare.findOthogonal(Random_L, 10);

	}

	// public boolean findOthogonal(int[][] l, int nn) {
	// N = nn;
	// indexInList = new int[nn];// for each level
	// indexInListCopy = new int[nn];
	// L = l;
	// // listIndex = 0;
	// ll = new ArrayList<List<Integer[]>>(nn);
	// M = new int[nn][nn];
	// return findOthogonal10();
	// }

	/**
	 * return null when there is no orthogonal square for l
	 */
	private static int[][] findOthogonal(int[][] latinS, int n) {
		if (debug)
			System.out.println("Valid? " + isSquare(latinS));

		int[][] M = new int[n][n];
		// record the choice for each level
		int[] indexInList = new int[n];
		int[] indexInListCopy = new int[n];

		List<int[]>[] ll = findTrans(latinS, n);

		// short cut for special cases. useful for case n = 6;
		for (int j = 0; j < n; j++) {
			if (ll[j].size() == 0) {
				return null;
			}
		}

		// experiment: can we filter invalid option earlier.

		combineList(ll[0], ll[1]);
		combineList(ll[0], ll[1], ll[2]);
		// useless!
		// for (int i = 0; i < n; i++) {
		// System.out.println("original size for list " + i + " is "
		// + ll[i].size());
		// filterInvalid(ll, i);
		// }
		//System.exit(0);

		// iterate the list to find a solution.
		boolean[][] mark = new boolean[n][n];
		iterateList(ll, indexInList, indexInListCopy, mark, 0, n);

		if (debug) {
			System.out.println(Arrays.toString(indexInListCopy));
			System.out.println(Arrays.toString(indexInList));
			System.out.println("M=\n");
		}
		for (int j = 0; j < n; j++) {
			int[] temp = ll[j].get(indexInListCopy[j]);
			if (debug) {
				System.out.println(Arrays.toString(temp));
			}
			for (int k = 0; k < n; k++) {
				M[temp[k]][k] = j;
			}
		}

		if (debug) {
			for (int j = 0; j < n; j++) {
				System.out.println(Arrays.toString(M[j]));
			}
			System.out.println("M valid?" + isOrthogonal(M, latinS));
		}

		return M;
	}

	private static void filterInvalid(List<int[]>[] ll, int k) {
		List<int[]> l = ll[k];
		int[] tb;
		exter: for (Iterator<int[]> it = l.iterator(); it.hasNext();) {
			int[] ta = (int[]) it.next();
			mate: for (int i = 1; i != k && i < ll.length; i++) {
				List<int[]> r = ll[i];
				// boolean conflict = false;
				for (int j = 0; j < r.size(); j++) {
					tb = r.get(j);
					if (noConflict(ta, tb)) {
						continue mate;
					}
				}
				it.remove();
				continue exter;
			}
		}
		System.out.println("new size for list " + k + " is " + l.size());
	}

	private static void combineList(List<int[]> l1, List<int[]> l2) {
		int[] t1;
		int[] t2;
		int count = 0;
		for (int i = 0; i < l1.size(); i++) {
			t1 = l1.get(i);
			for (int j = 0; j < l2.size(); j++) {
				t2 = l2.get(j);
				if (noConflict(t1, t2)) {
					count++;
				}
			}
		}
		System.out.println("original count=" + l1.size() * l2.size());
		System.out.println("no conflict count=" + count);
	}

	private static void combineList(List<int[]> l1, List<int[]> l2,
			List<int[]> l3) {
		int[] t1;
		int[] t2;
		int[] t3;
		int count = 0;
		for (int i = 0; i < l1.size(); i++) {
			t1 = l1.get(i);
			for (int j = 0; j < l2.size(); j++) {
				t2 = l2.get(j);
				if (noConflict(t1, t2)) {
					for (int k = 0; k < l3.size(); k++) {
						t3 = l3.get(k);
						if (noConflict(t1, t2, t3)) {
							count++;
						}
					}
				}

			}
		}
		System.out.println("original count=" + l1.size() * l2.size()
				* l3.size());
		System.out.println("no conflict count=" + count);

	}

	private static boolean noConflict(int[] t1, int[] t2, int[] t3) {
		int n = t1.length;
		boolean[][] mark = new boolean[n][n];
		int row, column;
		for (int j = 0; j < n; j++) {
			row = t1[j];
			column = j;
			mark[row][column] = true;
		}
		for (int j = 0; j < n; j++) {
			row = t2[j];
			column = j;
			if (mark[row][column] == false) {
				mark[row][column] = true;
			} else {
				return false;
			}
		}
		for (int j = 0; j < n; j++) {
			row = t3[j];
			column = j;
			if (mark[row][column] == false) {
				mark[row][column] = true;
			} else {
				return false;
			}
		}
		return true;
	}

	private static boolean noConflict(int[] t1, int[] t2) {
		int n = t1.length;
		boolean[][] mark = new boolean[n][n];
		int row, column;
		for (int j = 0; j < n; j++) {
			row = t1[j];
			column = j;
			mark[row][column] = true;
		}
		for (int j = 0; j < n; j++) {
			row = t2[j];
			column = j;
			if (mark[row][column] == false) {
				mark[row][column] = true;
			} else {
				return false;
			}
		}

		return true;
	}

	private static void iterateList(List<int[]>[] ll, int[] indexInList,
			int[] indexInListCopy, boolean[][] mark, int level, int N) {
		int column;
		int row;
		boolean[][] mark1;

		List<int[]> list = ll[level];
		int count = 0;
		// try each choice in the level
		external: for (int[] temp : list) {
			indexInList[level] = count++;
			// initialize the mark array.
			mark1 = new boolean[N][N];
			for (int j = 0; j < N; j++) {
				System.arraycopy(mark[j], 0, mark1[j], 0, N);
			}

			for (int j = 0; j < N; j++) {
				row = temp[j];
				column = j;
				// M[row][column] will be set to 'level'
				if (mark1[row][column] == false) {
					mark1[row][column] = true;
				} else {
					continue external;
				}
			}

			// no confilict, go to next level
			if (level == N - 1) {
				// output result
				if (debug)
					System.out.println("we have solution!");
				// solution = true;
				for (int jj = 0; jj < N; jj++) {
					indexInListCopy[jj] = indexInList[jj];
				}
			} else {
				iterateList(ll, indexInList, indexInListCopy, mark1, level + 1,
						N);
			}
		}
	}

	/**
	 * 
	 * @param latinS
	 * 
	 * @param n
	 *            number of column/rows of the latin square.
	 * @return
	 */
	public static List<int[]>[] findTrans(int[][] latinS, int n) {

		List<int[]>[] list = new List[n];
		// long is not enough to hold the number
		BigInteger bigP = BigInteger.valueOf(1L);
		int[] count = new int[n];

		for (int i = 0; i < n; i++) {
			list[i] = findTran(latinS, i, n);
			count[i] = (list[i].size());
			bigP = bigP.multiply(BigInteger.valueOf(count[i]));
		}

		return list;
	}

	static List<int[]> findTran(int[][] latinS, int index, int n) {
		List<int[]> list = new ArrayList<int[]>();

		List<Integer> row;
		// corresponding value in L for the chosen row numbers
		List<Integer> value;

		row = new ArrayList<Integer>();
		value = new ArrayList<Integer>();
		row.add(index);
		value.add(index);

		findTransversal(latinS, row, value, 1, n, list);

		return list;
	}

	// private void findTransversals() {
	// // the row number has been chosen, column number is fixed increasing
	// // sequence.
	// List<Integer> row;
	// // corresponding value in L for the chosen row numbers
	// List<Integer> value;
	// // column.add(0);
	//
	// // long is not enough to hold the number
	// BigInteger bigP = BigInteger.valueOf(1L);
	// int[] count = new int[N];
	//
	// for (int i = 0; i < 1; i++) {
	// // for (int i = 0; i < 1; i++) {
	// // listIndex = i;
	// ll.add(new ArrayList<Integer[]>());
	// row = new ArrayList<Integer>();
	// value = new ArrayList<Integer>();
	// row.add(i);
	// value.add(i);
	// // internal(row, column, 1);
	// findTransversal(row, value, 1, ll.get(i));
	//
	// if (debug) {
	// System.out.println(ll.get(i).size());
	// for (Integer[] tt : ll.get(i)) {
	// System.out.println(Arrays.toString(tt));
	// }
	// }
	// count[i] = (ll.get(i).size());
	// bigP = bigP.multiply(BigInteger.valueOf(count[i]));
	//
	// }
	//
	// if (debug) {
	// System.out.println("correct perms=" + bigP);
	// System.out.println("count [] is " + Arrays.toString(count));
	// }
	//
	// }

	/**
	 * 
	 * @param row
	 *            the resulted row.
	 * @param value
	 * @param level
	 *            which column we are trying to fill in?
	 */
	// void findTransversal(List<Integer> row, List<Integer> value, int level,
	// List<Integer[]> list) {
	//
	// if (debug)
	// System.out.println("level==" + level);
	// if (debug)
	// System.out.println(Arrays.toString(row.toArray()));
	// if (debug)
	// System.out.println(Arrays.toString(value.toArray()));
	//
	// boolean suc = false;
	// for (int j = 0; j < N; j++) {
	// List<Integer> row1 = new ArrayList<Integer>();
	//
	// row1.addAll(row);
	// List<Integer> value1 = new ArrayList<Integer>();
	// value1.addAll(value);
	// // continue when j is already in the row, or when
	// if (row.contains(j)) {
	// System.out.println("row.contains(j); j=" + j);
	// continue;
	// // } else if (j == L[row.get(0)][level]) {
	// // System.out.println("j == L[row.get(0)][level] =
	// // "+L[row.get(0)][level]);
	// // continue;
	// } else if (value.contains((L[j][level]))) {
	// System.out.println("value.contains((L[j][level])");
	// Integer[] empty = new Integer[0];
	// System.out.print(Arrays.toString(row.toArray(empty)));
	// System.out.print(Arrays.toString(value.toArray(empty)));
	// continue;
	// } else {
	// suc = true;
	// // column1.add(level);
	// row1.add(j);
	// value1.add((L[j][level]));
	// if (level == N - 1) {
	// Integer[] a = new Integer[N];
	// Integer[] b = new Integer[N];
	// list.add(row1.toArray(a));
	// if (debug)
	// System.out.print(Arrays.toString(a));
	// value1.toArray(b);
	// // BUG: value1.toArray(a);
	// if (debug)
	// System.out.println(Arrays.toString(a));
	// } else {
	// findTransversal(row1, value1, level + 1, list);
	// }
	// }
	// }
	// if (suc == false) {
	// if (debug)
	// System.out.println("level=" + level);
	// if (debug)
	// System.out.println(Arrays.toString(row.toArray()));
	// if (debug)
	// System.out.println(Arrays.toString(value.toArray()));
	// }
	//
	// }
	/**
	 * 
	 * @param row
	 *            the resulted row.
	 * @param value
	 * @param level
	 *            which column we are trying to fill in?
	 */
	static void findTransversal(int[][] latinS, List<Integer> row,
			List<Integer> value, int level, int n, List<int[]> list) {

		for (int j = 0; j < n; j++) {
			List<Integer> row1 = new ArrayList<Integer>();
			row1.addAll(row);
			List<Integer> value1 = new ArrayList<Integer>();
			value1.addAll(value);

			// continue when j is already in the row, we can not have k,
			// example, 0
			// in the same rows.
			if (row.contains(j)) {
				continue;
			} else if (value.contains((latinS[j][level]))) {
				// the corresponding value (latinS[j][level]) is duplicate
				// with existing values, so j is not right choice.
				continue;
			} else {

				row1.add(j);
				value1.add((latinS[j][level]));
				if (level == n - 1) {
					int[] a = new int[n];
					int[] b = new int[n];
					int count = 0;
					for (int tt : row1) {
						a[count++] = tt;
					}
					count = 0;
					for (int tt : value1) {
						b[count++] = tt;
					}
					list.add((a));
					// value1.toArray(b);
				} else {
					findTransversal(latinS, row1, value1, level + 1, n, list);
				}
			}
		}

	}

	/**
	 * To avoid typo in initialization.
	 * 
	 * @param rows
	 * @return
	 */
	public static boolean isSquare(int[][] L) {
		int N = L.length;

		// check rows
		for (int m = 0; m < N; m++) {// rows
			boolean[] markRow = new boolean[N];
			for (int nn = 0; nn < N; nn++) {
				if (markRow[L[m][nn]] == false) {
					markRow[L[m][nn]] = true;
				} else {
					return false;
				}
			}
		}

		// check columns
		for (int m = 0; m < N; m++) {// columns
			boolean[] markColumn = new boolean[N];
			for (int nn = 0; nn < N; nn++) {
				if (markColumn[L[nn][m]] == false) {
					markColumn[L[nn][m]] = true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * verify whether M is orthogonal with L.
	 * 
	 * @return
	 */
	static boolean isOrthogonal(int[][] M, int[][] L) {
		if (!(isSquare(M) && isSquare(L))) {
			return false;
		}

		int n = M.length;
		boolean[][] mark = new boolean[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (mark[L[i][j]][M[i][j]] == false) {
					mark[L[i][j]][M[i][j]] = true;
				} else {
					if (debug)
						System.out.println("i=" + i + "; j=" + j);
					return false;
				}
			}
		}
		return true;

	}

}
