/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.booleanevaluation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * MinimumMemoryChain.java
 * </p>
 */
public class MinimumMemoryChain {
	private boolean debug;
	/**
	 * 
	 * @param n
	 *            number of boolean variables
	 */
	public void showTruthTable3() {
		int nn = 1 << 3;
		int x1, x2, x3;
		for (int i = 0; i < nn; i++) {
			x3 = i & 1;
			x2 = (i >>> 1) & 1;
			x1 = (i >>> 2) & 1;

			x1 = x1 ^ x2;
			x3 = x3 & x1;
			x2 = x2 & (~x1);
			x3 = x3 | x2;
			if(debug) System.out.println(Integer.toBinaryString(i) + ":\t" + x3);
		}

	}

	/**
	 * 
	 * @param n
	 *            number of boolean variables
	 */
	public void showTruthTable3_1() {
		int nn = 1 << 3;
		int x1, x2, x3;
		for (int i = 0; i < nn; i++) {
			x3 = i & 1;
			x2 = (i >>> 1) & 1;
			x1 = (i >>> 2) & 1;

			int x5 = x1 ^ x2;
			int x6 = x3 & x5;
			int x7 = x2 & (~x5);
			int x8 = x6 | x7;
			if(debug) System.out.println(Integer.toBinaryString(i) + ":\t" + x8);
		}

	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	public Set<Integer> convertTruthTable4(int t) {
		if(debug) System.out.println("t=" + t + "; " + Integer.toBinaryString(t));
		Set<Integer> set = new HashSet<Integer>();
		int[] tx = new int[16];
		int[] ty = new int[16];
		int tt = 0;
		// get decomposed truth table.
		for (int i = 0; i < 16; i++) {
			tx[15 - i] = (t >>> i) & 1;
		}

		// combination of xi=xi&xj;
		for (int ii = 0; ii < 4; ii++) {
			// bug: for (int j = i + 1; j <= 4; j++) {
			for (int jj = ((ii + 1) % 4); jj != ii; jj = (jj + 1) % 4) {
				int i = ii + 1;
				int j = jj + 1;
				if(debug) System.out.println("i=" + i + "; j=" + j);

				int[] x = new int[5];
				// kk is the index in t.
				for (int kk = 0; kk < 16; kk++) {
					// decompse each kk.
					for (int k = 0; k < 4; k++) {
						x[4 - k] = (kk >>> k) & 1;
					}
					x[i] = x[i] & x[j];
					int tempc = 0;
					for (int ki = 1; ki < 5; ki++) {
						tempc += (x[ki] << (4 - ki));
					}
					ty[kk] = tx[tempc];
				}
				tt=0;
				for (int ki = 0; ki < 16; ki++) {
					tt += (ty[ki] << (15 - ki));
				}
				set.add(tt);
				if(debug) System.out.println("tt=" + tt + "; "
						+ Integer.toBinaryString(tt));

				x = new int[5];
				// kk is the index in t.
				for (int kk = 0; kk < 16; kk++) {
					// decompse each kk.
					for (int k = 0; k < 4; k++) {
						x[4 - k] = (kk >>> k) & 1;
					}
					x[i] = (~x[i]) & x[j];
					int tempc = 0;
					for (int ki = 1; ki < 5; ki++) {
						tempc += (x[ki] << (4 - ki));
					}
					ty[kk] = tx[tempc];
				}
				tt=0;
				for (int ki = 0; ki < 16; ki++) {
					tt += (ty[ki] << (15 - ki));
				}
				set.add(tt);
				if(debug) System.out.println("tt=" + tt + "; "
						+ Integer.toBinaryString(tt));
				x = new int[5];
				// kk is the index in t.
				for (int kk = 0; kk < 16; kk++) {
					// decompse each kk.
					for (int k = 0; k < 4; k++) {
						x[4 - k] = (kk >>> k) & 1;
					}
					x[i] = x[i] & (~x[j]);
					int tempc = 0;
					for (int ki = 1; ki < 5; ki++) {
						tempc += (x[ki] << (4 - ki));
					}
					ty[kk] = tx[tempc];
				}
				tt=0;
				for (int ki = 0; ki < 16; ki++) {
					tt += (ty[ki] << (15 - ki));
				}
				set.add(tt);
				if(debug) System.out.println("tt=" + tt + "; "
						+ Integer.toBinaryString(tt));
				
				x = new int[5];
				// kk is the index in t.
				for (int kk = 0; kk < 16; kk++) {
					// decompse each kk.
					for (int k = 0; k < 4; k++) {
						x[4 - k] = (kk >>> k) & 1;
					}
					x[i] = x[i] ^ x[j];
					int tempc = 0;
					for (int ki = 1; ki < 5; ki++) {
						tempc += (x[ki] << (4 - ki));
					}
					ty[kk] = tx[tempc];
				}
				tt=0;
				for (int ki = 0; ki < 16; ki++) {
					tt += (ty[ki] << (15 - ki));
				}				
				set.add(tt);
				if(debug) System.out.println("tt=" + tt + "; "
						+ Integer.toBinaryString(tt));

				x = new int[5];
				// kk is the index in t.
				for (int kk = 0; kk < 16; kk++) {
					// decompse each kk.
					for (int k = 0; k < 4; k++) {
						x[4 - k] = (kk >>> k) & 1;
					}
					x[i] = x[i] | x[j];
					int tempc = 0;
					for (int ki = 1; ki < 5; ki++) {
						tempc += (x[ki] << (4 - ki));
					}
					ty[kk] = tx[tempc];
				}
				tt=0;
				for (int ki = 0; ki < 16; ki++) {
					tt += (ty[ki] << (15 - ki));
				}
				set.add(tt);
				if(debug) System.out.println("tt=" + tt + "; "
						+ Integer.toBinaryString(tt));
			}
		}
		return set;

	}

	public static void main(String[] args) {
		new MinimumMemoryChain().showTruthTable3();
		 System.out.println();
		new MinimumMemoryChain().showTruthTable3_1();
		 System.out.println();

		Set<Integer> s = new MinimumMemoryChain().convertTruthTable4((1 << 16) - 1);
		 System.out.println(s);
		s = new MinimumMemoryChain().convertTruthTable4(0);
		 System.out.println(s);
	}
}
