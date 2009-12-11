/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

/**
 * <p>Node.java
 * </p>
 */
public class Node {
	// core data structure
	int i;// variable index, such as i in X_i

	Node lo;// means X_i is set to false;

	Node hi;// means X_i is set to true;

	// auxiliary data
	int k;// Insturction index; I-K is (v_k ? l_k : h_k)

	String tt;// truth table

	long numOfOne;// how many ones are there in the truth table?

	static int _id;
	int id; // to show the link between Nodes.

	boolean removed;// for reducing

	boolean fromLeft;// for C_100 independant set.

	public Node() {
		id = _id++;
	}

	public String toString() {
		// return "i=" + i + "; k=" + k + "; tt= " + tt + "; numOfone=" +
		// numOfOne;
		if (isTerminator()) {
			return "i=" + i + ";id=" + id + "; k=" + k + "; tt= " + tt;
		}
		return "i=" + i + ";id=" + id + " ("
				+ (lo == null ? "null" : lo.i + ":" + lo.id) + ", "
				+ (hi == null ? "null" : hi.i + ":" + hi.id) + ") " + "k=" + k
				+ "; tt= " + tt + "; numOfOne=" + numOfOne;
	}

	public boolean isSymmetric() {
		return TruthTable.isSymmetric(tt);
	}

	/**
	 * there are several ways to detect terminator. 1. whether they are false
	 * node or true node. 2. whtether the node's hi or lo field are null 3.
	 * whtether the node's hi field si equal to lo field. (it may conflict with
	 * the unreduced node.)
	 * 
	 * @return
	 */
	public boolean isTerminator() {
		// TODO Auto-generated method stub
		return lo == null || hi == null;
	}

	public Pair getPair() {
		Pair p = new Pair();
		p.lo = lo;
		p.hi = hi;
		return p;
	}
}
