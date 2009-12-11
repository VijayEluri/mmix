package taocp.v4.f0.booleanevaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * AlgoL_R1.java: the cost may be less than the length of formula. to get the
 * C(f) by revising AlgoL
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class FindCost {
	static boolean debug = true;
	private static final int N = 4;
	private static final int NN = (1 << N);
	private static final int NNN = 1 << (NN - 1);
	// only consider normal function.
	private int[] U = new int[NNN];

	private int c = 0;// count of untouched(untagged) function.

	int f = 0;// function
	int r = 0;// cost

	// each element[r] is a list of fuctions with cost r.
	List<List<Integer>> listOfList = new ArrayList<List<Integer>>();

	public int[] getCost(){
		return U;
	}
	
	public int[] findCost() {

		int k = 0;
		init();

		List<Integer> listJ = null;
		List<Integer> listK = null;
		r = 1;

		labeled: while (c != 0 && r < 7) {
			common = false;
			r++;
			listOfList.add(r, new LinkedList<Integer>());
			System.out.println("how many function was left: c = " + c);
			for (int i = 0; i < r; i++) {
				System.out.println("r=" + i + "; size="
						+ listOfList.get(i).size() * 2);
			}

			System.out.println("new r = " + r);

			for (int j = (r - 1) / 2; j >= 0; j--) {
				k = r - 1 - j;
				listJ = listOfList.get(j);
				listK = listOfList.get(k);
				System.out.println("j=" + j + "; k=" + k);
				if(r==7){
					for (int i = 0; i < r; i++) {
						System.out.println("r=" + i + "; size="
								+ listOfList.get(i).size() * 2);
					}
				}
				if (j == k) {//
					for (int ti = 0; ti < listJ.size(); ti++) {
						for (int tj = ti + 1; tj < listJ.size(); tj++) {
							int g = listJ.get(ti);
							int h = listJ.get(tj);
							combineGH(g, h);
							
							//it take me two afternoons to figure out the problem.
//							if (c == 0)
//								break labeled;
						}
					}
				} else {
					for (int g : listJ) {
						for (int h : listK) {
							combineGH(g, h);
//							if (c == 0)
//								break labeled;
						}
					}
				}
			}
			// collect_middle();
		}
		return collect();
	}

	int u = 0;
	int v = 0;

	boolean common = false;

	private void combineGH(int g, int h) {
		if ((footprint[g] & footprint[h]) != 0) {
			if (common == false) {
				System.out.println("common element when r=" + r);
				common = true;
			}
			u = r - 1;
			v = footprint[g] & footprint[h];
		} else {
			u = r;
			v = footprint[g] | footprint[h];
		}

		f = g & h;
		if (judgef(g, h)) {
			if (!lili.containsKey(f)) {
				lili.put(f, new LinkedList<String>());
			}
//			for (String a : lili.get(g)) {
//				for (String b : lili.get(h)) {
//					addf("(" + a + ")&(" + b + ")");
//				}
//			}

		}
		f = ~g & h;
		if (judgef(g, h)) {
			if (!lili.containsKey(f)) {
				lili.put(f, new LinkedList<String>());
			}
//			for (String a : lili.get(g)) {
//				for (String b : lili.get(h)) {
//					addf("(~(" + a + "))&(" + b + ")");
//				}
//			}

		}
		f = g & ~h;
		if (judgef(g, h)) {
			if (!lili.containsKey(f)) {
				lili.put(f, new LinkedList<String>());
			}
//			for (String a : lili.get(g)) {
//				for (String b : lili.get(h)) {
//					addf("(" + a + ")&(~(" + b + "))");
//				}
//			}

		}
		f = g | h;
		if (judgef(g, h)) {
			if (!lili.containsKey(f)) {
				lili.put(f, new LinkedList<String>());
			}
			// for (String a : lili.get(g)) {
			// for (String b : lili.get(h)) {
			// addf("(" + a + ")|(" + b + ")");
			// }
			// }

		}
		f = g ^ h;
		if (judgef(g, h)) {
			if (!lili.containsKey(f)) {
				lili.put(f, new LinkedList<String>());
			}
//			for (String a : lili.get(g)) {
//				for (String b : lili.get(h)) {
//					addf("(" + a + ")^(" + b + ")");
//				}
//			}

		}

	}

	private void addf(String s) {
		lili.get(f).add(s);
	}

	/**
	 * return whether the combination is valid and added.
	 */
	private boolean judgef(int g, int h) {
		if (U[f] == -1) {
			U[f] = u;
			footprint[f] = v;
			c--;
			listOfList.get(u).add(f);
//			if (u == r - 1) {
//				System.out.println("r = " + r + " cost decrease one for f=" + f
//						+ "; u=" + u);
//			}
			return true;
		} else {
			if (u == U[f]) {
				footprint[f] |= v;
				return true;
			} else if (u < U[f]) {
//				System.out.println("u < U[f]");
//				System.out.println("r = " + r + " cost decrease one for f=" + f
//						+ "; u=" + u);
				listOfList.get(U[f]).remove(Integer.valueOf(f));
				lili.remove(f);
				U[f] = u;
				listOfList.get(u).add(f);

				footprint[f] = v;
				return true;
			} else {
				return false;
			}

		}

	}

	// each element is a bit vector of 5 * N * (N - 1) / 2 fuctions of cost 1
	private int[] footprint = new int[NNN];

	// if N = 5;
	// private long[] footprint = new int[NNN];

	void init() {
		init0();
		init1();
	}

	/**
	 * init for cost = 0;
	 */
	void init0() {
		U[0] = 0;
		lili.put(0, new ArrayList<String>());
		lili.get(0).add("0");

		for (int i = 1; i < U.length; i++) {
			U[i] = -1;
		}

		int xk = 0;
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= N; i++) {
			xk = ((int) Math.pow(2, Math.pow(2, N)) - 1)
					/ ((int) Math.pow(2, Math.pow(2, N - i)) + 1);
			U[xk] = 0;
			lili.put(xk, new ArrayList<String>());
			lili.get(xk).add("x" + i);
			if (debug) {
				System.out.println("k = " + i + "; x(k) = "
						+ Integer.toBinaryString(xk));
			}
			list.add(xk);
		}
		listOfList.add(0, list);
		c = U.length - N - 1;
		System.out.println("c = " + c);
		System.out.println("size of list for cost 0: "
				+ listOfList.get(0).size());
	}

	int vectorLength = N * (N - 1) / 2;
	String[] footP = new String[5 * vectorLength];

	/**
	 * init for cost = 1;
	 */
	void init1() {
		// initialize delta for cost=1

		int cc = 0;

		List<Integer> list = listOfList.get(0);
		listOfList.add(1, new LinkedList<Integer>());

		for (int ti = 0; ti < list.size(); ti++) {
			for (int tj = ti + 1; tj < list.size(); tj++) {

				int g = list.get(ti);
				int h = list.get(tj);
				f = g & h;
				U[f] = 1;
				lili.put(f, new ArrayList<String>());
				lili.get(f).add("x" + (ti + 1) + "&" + "x" + (tj + 1));
				footprint[f] = 1 << (vectorLength * 0 + cc);
				footP[((vectorLength * 0)) + cc] = "x" + (ti + 1) + "&" + "x"
						+ (tj + 1);
				listOfList.get(1).add(f);

				f = ~g & h;
				U[f] = 1;
				lili.put(f, new ArrayList<String>());
				lili.get(f).add("~x" + (ti + 1) + "&" + "x" + (tj + 1));
				footprint[f] = 1 << (vectorLength * 1 + cc);
				footP[((vectorLength * 1)) + cc] = "~x" + (ti + 1) + "&" + "x"
						+ (tj + 1);
				listOfList.get(1).add(f);

				f = g & ~h;
				U[f] = 1;
				lili.put(f, new ArrayList<String>());
				lili.get(f).add("x" + (ti + 1) + "&" + "~x" + (tj + 1));
				footprint[f] = 1 << (vectorLength * 2 + cc);
				footP[((vectorLength * 2)) + cc] = "x" + (ti + 1) + "&" + "~x"
						+ (tj + 1);
				listOfList.get(1).add(f);

				f = g | h;
				U[f] = 1;
				lili.put(f, new ArrayList<String>());
				lili.get(f).add("x" + (ti + 1) + "|" + "x" + (tj + 1));
				footprint[f] = 1 << (vectorLength * 3 + cc);
				footP[((vectorLength * 3)) + cc] = "x" + (ti + 1) + "|" + "x"
						+ (tj + 1);
				listOfList.get(1).add(f);

				f = g ^ h;
				U[f] = 1;
				lili.put(f, new ArrayList<String>());
				lili.get(f).add("x" + (ti + 1) + "^" + "x" + (tj + 1));
				footprint[f] = 1 << (vectorLength * 4 + cc);
				footP[((vectorLength * 4)) + cc] = "x" + (ti + 1) + "^" + "x"
						+ (tj + 1);
				listOfList.get(1).add(f);

				cc++;
			}
		}

		c = c - listOfList.get(1).size();
		System.out.println("c = " + c);
		System.out.println("size of list for cost 1: "
				+ listOfList.get(1).size());
		r = 1;
		System.out.println("footP+++++++++++++");

		int sc = 0;
		for (String temp : footP) {
			System.out.println("footP[" + sc + "]=" + temp);
			sc++;
		}

		// this.collect_middle();

	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		new FindCost().findCost();
		// new FindCost().init();
		long end = System.currentTimeMillis();
		System.out.println("seconds = " + (end - start));
	}

	private int[] collect() {
		int[] count = new int[r + 1];
		for (int i = 0; i < U.length; i++) {
			if (U[i] == -1) {
				// throw new RuntimeException("L[i] == -1; i=" + i);
				// System.out.println("U[i] == -1; i="+i);
			} else {
				count[U[i]] += 1;
			}

		}

		for (int i = 0; i < count.length; i++) {
			System.out.println("cf = " + i + "; functions = " + count[i] * 2
					+ " -- " + count[i]);
		}
		return count;
	}

	// private String[] formula = new String[NNN];
	// each element is list of formulas for same function.
	Map<Integer, List<String>> lili = new HashMap<Integer, List<String>>();

	private void collect_middle() {
		int[] count = new int[r + 1];

		// for(int i = 0; i < r+1; i++){
		// lili.add(new LinkedList<String>());
		// }

		for (int i = 0; i < U.length; i++) {
			if (U[i] == -1) {

			} else {
				count[U[i]] += 1;
				// lili.get(i).add(formula[U[i]]);
			}

		}

		System.out.println();
		System.out.println("r================" + r);
		for (int i = 0; i < count.length; i++) {
			System.out.println("cf = " + i + "; functions = " + count[i] * 2
					+ " -- " + count[i]);
			for (Integer intg : listOfList.get(i)) {
				System.out.println("function for f = " + intg + " total "
						+ lili.get(intg).size() + " formula");
				for (String temp : lili.get(intg)) {
					System.out.println(temp);
				}
				footprint(intg);
			}

		}
	}

	private void footprint(int f) {
		int temp = footprint[f];
		System.out.print("foot print is as the following:" + temp + ": ");
		for (int i = 0; i < footP.length; i++) {
			if ((temp & (1 << i)) != 0) {
				System.out.print(footP[i] + "\t");
			}
		}
		System.out.println();
	}
}
