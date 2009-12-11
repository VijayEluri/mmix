/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * many things can be done in O(n) after we build the BDD. The only concern is
 * that the upper bound of BDD size is O(2^n). Fortunately, most of boolean
 * function in reality DOES have a BDD with small size. To some extent, the
 * truth table for them are sparse.
 * 
 * although truth table and BDD are equivalent logically to represent a boolean
 * function. They have very different characteristics. For example, to get the
 * number of independent set and maximum independent set of a circle of n
 * elements. The BDD is better way when n is big, say, 100. Since we do not need
 * to have a truth table of length 2^100 explicitly.
 * 
 * To make it simple, we exclude the constant function 0 and 1 in our BDD
 * implementation. or we can add these two case into it with special case.
 * 
 * For the convinience of testing, we need a good way to show the internal
 * strutcure of BDD. Think of how to do it. a). print out the node to show the
 * structure. b). draw the BDD with the language such as metapost. the program
 * print out MetaPost code. Currently I can only try solution a), because b) is
 * actually complex and depends on many other things.
 * 
 * There are way too many redundant information in the BDD, they can be
 * converted to each other.
 * 
 * 1) BDD can be reduced. (Normal v.s. Redcued)
 * 
 * 2) In each node, the truth table can be populated.
 * 
 * 3) BDD with node structure can be converted to Instruction List.
 * <p>
 * The Home Grown implementation of Binary Dicision Diagram (BDD). Just to get
 * some hands on experience on BDD.
 * </p>
 */
public class BDD {

	// core data structure
	Node root;

	int numOfVar; // number of boolean variables in the function

	// auxiliary data structure
	// mapping between truth table snippet and BDD Node.
	Map<String, Node> beads = new HashMap<String, Node>();

	// for debug
	Node[] nodes = new Node[1000];

	// number of Node in the BDD. it may vary depends whether we reduce it.
	int numOfNode;

	// we do not make it static, because the value i of the node may be
	// different for different BDD.
	Node falseNode = new Node();

	Node trueNode = new Node();

	BDD() {
	}

	protected BDD(int numOfVar) {
		this.numOfVar = numOfVar;
		falseNode.tt = "0";
		trueNode.tt = "1";
		falseNode.i = numOfVar + 1;
		trueNode.i = numOfVar + 1;
		nodes[numOfNode++] = falseNode;
		nodes[numOfNode++] = trueNode;
		beads.put(falseNode.tt, falseNode);
		beads.put(trueNode.tt, trueNode);
	}

	/**
	 * 
	 * @param truthTableS
	 * @param numOfVar
	 */
	public BDD(String truthTableS, int numOfVar) {
		this(new TruthTable(truthTableS).getTable(), numOfVar);
	}

	/**
	 * not scalable, because the truth table will become very big quickly
	 * 
	 * it can only be used to show some small cases.
	 * 
	 * @param truthTable
	 *            not all true or all false.
	 * @param numOfVar
	 */
	public BDD(boolean[] truthTable, int numOfVar) {

		this(numOfVar);

		root = new Node();
		root.i = 1;
		root.tt = TruthTable.getString(truthTable);
		beads.put(root.tt, root);
		nodes[numOfNode++] = root;
		int n = truthTable.length;
		boolean[] leftT = Arrays.copyOfRange(truthTable, 0, n / 2);
		boolean[] rightT = Arrays.copyOfRange(truthTable, n / 2, n);
		if (Arrays.equals(leftT, rightT)) {
			int cc = 0;
			while (true) {
				if (Arrays.equals(leftT, rightT)) {
					cc++;
					truthTable = leftT;
					leftT = Arrays.copyOfRange(truthTable, 0, n / 2);
					rightT = Arrays.copyOfRange(truthTable, n / 2, n);
				} else {
					break;
				}
			}

			makeBDD(root, leftT, true, 2 + cc);
			root.removed=true;
			numOfNode--;
			root = root.lo;
			// root.hi = root.lo;
		} else {
			makeBDD(root, leftT, true, 2);
			makeBDD(root, rightT, false, 2);
		}
	}

	// private int getUniqueTruthTable(boolean[] truthTable, int level) {
	// int n = truthTable.length;
	// int count = 0;
	// boolean[] leftT = Arrays.copyOfRange(truthTable, 0, n / 2);
	// boolean[] rightT = Arrays.copyOfRange(truthTable, n / 2, n);
	// while (true) {
	// if (Arrays.equals(leftT, rightT)) {
	// count++;
	// truthTable = leftT;
	// leftT = Arrays.copyOfRange(truthTable, 0, n / 2);
	// rightT = Arrays.copyOfRange(truthTable, n / 2, n);
	// } else {
	// break;
	// }
	// }
	// return count + level + 1;
	// }

	private void makeBDD(Node parent, boolean[] truthTable, boolean left,
			int level) {
		Node e = beads.get(TruthTable.getString(truthTable));
		if (e != null) {
			if (left == true) {
				parent.lo = e;
			} else {
				parent.hi = e;
			}
			return;
		}

		int n = truthTable.length;

		if (TruthTable.allFalse(truthTable)) {
			if (left == true) {
				parent.lo = falseNode;
			} else {
				parent.hi = falseNode;
			}
			beads.put(TruthTable.getString(truthTable), falseNode);
			return;
		} else if (TruthTable.allTrue(truthTable)) {
			if (left == true) {
				parent.lo = trueNode;
			} else {
				parent.hi = trueNode;
			}
			beads.put(TruthTable.getString(truthTable), trueNode);
			return;
		}

		boolean[] leftT = Arrays.copyOfRange(truthTable, 0, n / 2);
		boolean[] rightT = Arrays.copyOfRange(truthTable, n / 2, n);
		Node temp = new Node();
		temp.i = level;
		temp.tt = TruthTable.getString(truthTable);
		beads.put(temp.tt, temp);
		nodes[numOfNode++] = temp;
		if (left == true) {
			parent.lo = temp;
		} else {
			parent.hi = temp;
		}

		if (Arrays.equals(leftT, rightT)) {
			int cc = 0;
			while (true) {
				if (Arrays.equals(leftT, rightT)) {
					cc++;
					truthTable = leftT;
					leftT = Arrays.copyOfRange(truthTable, 0, n / 2);
					rightT = Arrays.copyOfRange(truthTable, n / 2, n);
				} else {
					break;
				}
			}

			makeBDD(temp, leftT, true, level + 1 + cc);
			if (left == true) {
				parent.lo =  temp.lo;
				temp.removed=true;
				numOfNode--;
			} else {
				parent.hi =  temp.lo;
				temp.removed=true;
				numOfNode--;
			}
			
			// temp.hi = root.lo;
		} else {
			makeBDD(temp, leftT, true, level + 1);
			makeBDD(temp, rightT, false, level + 1);
		}
	}

	

	/**
	 * Evaluate the function based on the input. consider the i (index) in the
	 * nodes are not consecutive.
	 */
	public boolean eval(boolean[] input) {
		Node temp = root;
		for (int i = 0; i < numOfVar && temp != falseNode && temp != trueNode; i++) {
			if (input[temp.i - 1] == true) {// (index) in the nodes may be not
				// consecutive.
				temp = temp.hi;
			} else {
				temp = temp.lo;
			}
		}
		if (temp == falseNode) {
			return false;
		}
		return true;
	}

	// /**
	// * Evaluate the function based on the input. consider the i (index) in the
	// * nodes are not consecutive.
	// */
	// public boolean eval2(boolean[] input) {
	// Node temp = root;
	// for (int i = 0; i < numOfVar && temp != falseNode && temp != trueNode;
	// i++) {
	// if (temp.lo == null || temp.hi == null) {
	// System.out.println(temp);
	// }
	//
	// if (temp.lo.isTerminator()) {
	// if (input[temp.i - 1] == false) {
	// temp = temp.lo;
	// continue;
	// }
	// } else {//bug still depends on temp.i-1.
	// if (input[temp.lo.i - 2] == false) {// (index) in the nodes may
	// // be not
	// // consecutive.
	// temp = temp.lo;
	// continue;
	// }
	// }
	//
	// if (temp.hi.isTerminator()) {
	// if (input[temp.i - 1] == true) {
	// temp = temp.hi;
	// }
	// } else {
	// if (input[temp.hi.i - 2] == true) {// (index) in the nodes may
	// // be not
	// // consecutive.
	// temp = temp.hi;
	// }
	// }
	//
	// }
	// if (temp == falseNode) {
	// return false;
	// }
	// return true;
	// }

	/**
	 * To find the smallest X which can satisfy the boolean function.
	 */
	public boolean[] findSmallestX() {
		boolean[] res = new boolean[numOfVar];
		Node temp = root;
		for (int i = 0; i < numOfVar; i++) {
			if (temp.lo != falseNode) {
				res[i] = false;
				temp = temp.lo;
			} else {
				res[i] = true;
				temp = temp.hi;
			}
		}
		return res;
	}

	// public void print() {
	// System.out.println("in print(); count=" + count);
	// for (int i = 0; i < nodes.length && nodes[i] != null; i++) {
	// Node node = nodes[i];
	// System.out.println(node);
	// }
	// }

	public void print2() {
		print2(root);
	}

	public void printInLevel() {
		System.out.println("in printInLevel(); count=" + numOfNode);
		Node node;
		for (int ll = 1; ll <= numOfVar + 1; ll++) {
			System.out.println("level " + ll);
			for (int i = 0; i < nodes.length && nodes[i] != null; i++) {
				node = nodes[i];
				if (node.i == ll && node.removed == false) {
					System.out.println(node);
				}
			}
		}
	}

	public void print2(Node node) {
		if (node.lo != null) {
			print2(node.lo);
		}
		System.out.println(node.i);
		if (node.hi != null) {
			print2(node.hi);
		}
	}

	

	// List<Instruction> insts = new ArrayList<Instruction>();
	Instruction[] insts;

	/**
	 * initialize Instruction Index for each node
	 * 
	 */
	void initInstrIndex() {
		int countBack = numOfNode;
		insts = new Instruction[numOfNode];
		insts[0] = buildI0();
		insts[1] = buildI1();
		numOfNode--;
		initInstrIndex(root);
		numOfNode = countBack;
	}

	private Instruction buildI0() {
		return buildI0OrI1(0);
	}

	private Instruction buildI1() {
		return buildI0OrI1(1);
	}

	private Instruction buildI0OrI1(int index) {
		Instruction instr = new Instruction();
		instr.index = index;
		instr.v = numOfVar + 1;
		instr.l = index;
		instr.h = index;
		return instr;
	}

	void initInstrIndex(Node node) {

		if (node.isTerminator() == true) {
			// should not happen. we use pre-check strategy
		}

		Instruction instr = new Instruction();
		int k = numOfNode;
		insts[k] = instr;
		instr.index = k;
		instr.v = node.i;
		node.k = k;

		if (node.lo.isTerminator() == false) {
			instr.l = --numOfNode;
			initInstrIndex(node.lo);
		} else if (node.lo == falseNode) {
			instr.l = 0;
		} else {
			instr.l = 1;
		}
		if (node.hi.isTerminator() == false) {
			instr.h = --numOfNode;
			initInstrIndex(node.hi);
		} else if (node.hi == falseNode) {
			instr.h = 0;
		} else {
			instr.h = 1;
		}

	}

	void printInstr() {
		for (int i = numOfNode - 1; i >= 0; i--) {
			System.out.println(insts[i]);
		}
	}

	/**
	 * count the number of ones in the truth table, also update each node to
	 * maintain intermediante result.
	 * 
	 */
	void initNumOfOne() {
		initNumOfOne(root);
	}

	void initNumOfOne(Node node) {

		if (node.lo.isTerminator() == false && node.lo.removed == false) {
			initNumOfOne(node.lo);
		}
		if (node.hi.isTerminator() == false && node.hi.removed == false) {
			initNumOfOne(node.hi);
		}

		long numOfOne = 0;
		if (node.lo.isTerminator()) {
			if (node.lo == trueNode) {
				numOfOne += Math.pow(2, numOfVar - node.i);
			}
		} else {
			numOfOne += node.lo.numOfOne * Math.pow(2, node.lo.i - 1 - node.i);
		}
		if (node.hi.isTerminator()) {
			if (node.hi == trueNode) {
				numOfOne += Math.pow(2, numOfVar - node.i);
			}
		} else {
			numOfOne += node.hi.numOfOne * Math.pow(2, node.hi.i - 1 - node.i);
		}
		node.numOfOne = numOfOne;
		node.removed = true;// mark it to prevent duplicate call.
		if (node.i % 10 == 0) {
			System.out.println(node);
		}
	}

	



	

	

	

	/**
	 * the idea is to populate the truth table first. then judge whether we have
	 * duplicate sub-tree by truth table.
	 * @deprecated naive implementation.
	 */
	public void reduce2() {
		populateTruthTable(root);
		getBeads(root);
		System.out.println("there are " + beads.size() + " beads");
		for (Entry<String, Node> entry : beads.entrySet()) {
			System.out.println(entry.getValue());
		}
		realReduce();
	}
	
	/**
	 * 
	 */
	public void reduce() {
		new BDDReducer().reduce(this);
	}

	/**
	 * @deprecated not tested yet.
	 */
	private void realReduce() {
		// for (int j = numOfVar; j > 0; j--) {
		//
		// }
		List<Node> list = new ArrayList<Node>();
		for (int i = 0; i < numOfNode; i++) {

			Node node = nodes[i];
			if (node.isTerminator()) {
				list.add(node);
				continue;
			}
			// make all the beads node having correct link.
			if (node == beads.get(node.tt)) {
				list.add(node);
				if (node.lo.isSymmetric()) {
					node.lo = followLink(node.lo);
				} else {
					node.lo = beads.get(node.lo.tt);
				}
				// System.out.println("it should be symmetric"
				// + beads.get(node.tt).isSymmetric());
				if (node.hi.isSymmetric()) {
					node.hi = followLink(node.lo);
				} else {
					node.hi = beads.get(node.hi.tt);
				}
			}
		}
		// second round.
		for (int i = 0; i < numOfNode; i++) {
			Node node = nodes[i];
			if (null == beads.get(node.tt)) {
				nodes[i] = null;
			}
		}

		nodes = list.toArray(new Node[0]);
		numOfNode = nodes.length;

	}

	private Node followLink(Node node) {
		Node temp = node.lo;
		while (temp.isSymmetric()) {
			temp = temp.lo;
		}
		return beads.get(temp.tt);
	}

	private void realReduce(Node node) {
		if (node.isSymmetric()) {

		}
	}

	private void getBeads(Node node) {
		if (TruthTable.isSymmetric(node.tt) == false) {
			beads.put(node.tt, node);
		}
		if (node.lo.isTerminator() == false) {
			getBeads(node.lo);
		}
		if (node.hi.isTerminator() == false) {
			getBeads(node.hi);
		}
		beads.put("0", falseNode);
		beads.put("1", trueNode);

	}

	private void populateTruthTable(Node node) {
		String left;
		// if(node.lo==null){
		// System.out.println(node);
		// }
		if (node.lo.isTerminator() == false) {
			populateTruthTable(node.lo);
			left = node.lo.tt;
		} else {
			if (node.lo == falseNode) {
				left = TruthTable.repeatString(false, 1 << (numOfVar - node.i));
			} else {
				left = TruthTable.repeatString(true, 1 << (numOfVar - node.i));
			}
		}

		String right;
		if (node.hi.isTerminator() == false) {
			populateTruthTable(node.hi);
			right = node.hi.tt;
		} else {
			if (node.hi == falseNode) {
				right = TruthTable
						.repeatString(false, 1 << (numOfVar - node.i));
			} else {
				right = TruthTable.repeatString(true, 1 << (numOfVar - node.i));
			}
		}
		node.tt = left + right;
	}

}

