/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.booleanbasics;

import util.MathUtil;

/**
 * horn, krom, and threshold is harder to judge!
 * 
 * <p>
 * PropertyUtil.java
 * </p>
 */
public class BooleanAttrUtil {
	/**
	 * judge whether the function is self dual according to it truth table.
	 * 
	 * @return
	 */
	public static boolean isSelfDual(boolean[] table, int nvar) {
		long length = table.length >>> 1;
		if (length == 0)
			return false;
		for (int i = 0; i < length; i++) {
			// System.out.println(i + " vs " + ((~i) & nvar));
			if (table[i] == (table[(~i) & ((1 << nvar) - 1)])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * point is to get the permutation of the n variable
	 * 
	 * @param table
	 * @param nvar
	 * @return
	 */
	public static boolean isSymmetric(boolean[] table, int nvar) {
		if (nvar == 0) {
			return true;
		}
		boolean[] temp = new boolean[nvar + 1];// boolean value when there are n
		// 1s;
		boolean[] change = new boolean[nvar + 1];

		temp[0] = table[0];
		change[0] = false;

		temp[nvar] = table[table.length-1];
		change[nvar] = false;

		boolean chan = false;
		exter: for (int i = 1; i < nvar; i++) {
			int[] inputs = new int[nvar];
			for (int j = 0; j < i; j++) {
				inputs[j] = 1;
			}
			int[][] perm = MathUtil.getPermutation(inputs);

			int var = 0;
			for (int kk = 0; kk < inputs.length; kk++) {
				var += (inputs[kk] << nvar - 1 - kk);
			}
			boolean tt = table[var];
			temp[i] = tt;

			for (int k = 0; k < perm.length; k++) {
				var = 0;
				for (int kk = 0; kk < perm[k].length; kk++) {
					var += (perm[k][kk] << nvar - 1 - kk);
				}
				if (table[var] != tt) {
					change[i] = true;
					chan = true;
					break exter;
				}
			}

		}

		// the value chagne between combination. ie. not symmetry
		if (chan == true) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isSymmetric2(boolean[] table, int nvar) {
		if (nvar == 0) {
			return true;
		}
		boolean[] temp = new boolean[nvar + 1];// boolean value when there are n
		// 1s;
		boolean[] change = new boolean[nvar + 1];

		temp[0] = table[0];
		change[0] = false;

		temp[nvar] = table[table.length-1];
		change[nvar] = false;

		boolean chan = false;
		exter: for (int i = 1; i < nvar; i++) {
			
			long[] aa = Monotone.getMonotoneSequence(nvar,i);			
			boolean tt = table[(int)aa[0]];
			temp[i] = tt;

			for (int k = 0; k < aa.length; k++) {				
				if (table[(int)aa[k]] != tt) {
					change[i] = true;
					chan = true;
					break exter;
				}
			}

		}

		// the value chagne between combination. ie. not symmetry
		if (chan == true) {
			return false;
		}
		
		return true;
	}

	public static boolean isPureThreshold(boolean[] table, int nvar) {
		if (nvar == 0) {
			return true;
		}
		boolean[] temp = new boolean[nvar + 1];// boolean value when there are n
		// 1s;
		boolean[] change = new boolean[nvar + 1];

		temp[0] = table[0];
		change[0] = false;

		temp[nvar] = table[table.length-1];
		change[nvar] = false;

		boolean chan = false;
		exter: for (int i = 1; i < nvar; i++) {
			int[] inputs = new int[nvar];
			for (int j = 0; j < i; j++) {
				inputs[j] = 1;
			}
			int[][] perm = MathUtil.getPermutation(inputs);

			int var = 0;
			for (int kk = 0; kk < inputs.length; kk++) {
				var += (inputs[kk] << nvar - 1 - kk);
			}
			boolean tt = table[var];
			temp[i] = tt;

			for (int k = 0; k < perm.length; k++) {
				var = 0;
				for (int kk = 0; kk < perm[k].length; kk++) {
					var += (perm[k][kk] << nvar - 1 - kk);
				}
				if (table[var] != tt) {
					change[i] = true;
					chan = true;
					break exter;
				}
			}

		}

		// the value chagne between combination. ie. not symmetry
		if (chan == true) {
			return false;
		}

		if (temp[0] == true) {
			for (int i = 0; i < nvar + 1; i++) {
				if (temp[i] == false)
					return false;
			}
			return true;
		}

		for (int i = 0; i < nvar + 1; i++) {
			if (chan == false) {
				if (temp[i] == true) {
					chan = true;
				}
			} else {
				if (temp[i] == false) {
					return false;
				}
			}

		}
		return true;
	}

	public static boolean isCanalizing(boolean[] table, int nvar) {
		if (nvar == 0) {
			return true;
		}
		boolean[] temp0 = new boolean[nvar];
		boolean[] temp1 = new boolean[nvar];
		boolean[] chan0 = new boolean[nvar];
		boolean[] chan1 = new boolean[nvar];
		for (int j = 0; j < nvar; j++) {
			temp0[j] = table[0];
			temp1[j] = table[table.length - 1];
			chan0[j] = false;
			chan1[j] = false;
		}

		for (int i = 0; i < table.length; i++) {

			for (int j = 0; j < nvar; j++) {
				if (chan0[j] && chan1[j]) {
					continue;
				}
				if ((i & (1 << j)) == 0) {
					if ((chan0[j] == false) && (temp0[j] != table[i])) {
						chan0[j] = true;
					}
				} else {
					if ((chan1[j] == false) && (temp1[j] != table[i])) {
						chan1[j] = true;
					}
				}

			}

		}

		for (int j = 0; j < nvar; j++) {
			if (chan0[j] == false || chan1[j] == false) {
				return true;
			}
		}
		return false;

	}

	public static boolean isMonotone(boolean[] table, int nvar) {
		int temp;
		if (nvar == 0) {
			return true;
		}
		for (int i = 0; i < table.length; i++) {
			if (table[i]) {
				for (int j = 0; j < nvar; j++) {
					if ((i & (1 << j)) == 0) {
						temp = i ^ (1 << j);
						if (table[temp] == false) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public static boolean isHorn(boolean[] table, int nvar) {
		int[] temp = new int[table.length];
		int count = 0;
		if (nvar <= 1) {
			return true;
		}

		for (int i = 0; i < table.length; i++) {
			if (table[i] == true) {
				temp[count++] = i;
			}
		}
		if (count <= 1) {
			return true;
		}

		for (int i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++) {
				if (table[temp[i] & temp[j]] == false) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean isKorm() {
		return false;
	}

	public static boolean isThreshold() {
		return false;
	}
}
