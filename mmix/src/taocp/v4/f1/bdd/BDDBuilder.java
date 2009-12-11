package taocp.v4.f1.bdd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Not Thread safe.
 * @author eddie.wu
 *
 */
public class BDDBuilder {
	/**
	 * The following act as temporary global variable, share state between
	 * method call.
	 */
	static BDD bdd;
	static Node falseNode;
	static Node trueNode;

	static Node[][] levelList;

	/**
	 * max independent set of n elements in a circle. 1) if any two adjacent
	 * elements are choose, the set is not independent. 2) if there are three
	 * consecutive 0s, it is not maximum, because the cetral 0 can be change to
	 * 1 without broken independent characteristics. <br />
	 * 
	 * so it is a n bit boolean function. but we should not try to get the truth
	 * table for it, since it will be too long to store if n is big, say 100.
	 * 
	 * the resulted BDD may not be a reduced one, we need to reduce it in
	 * separate method invocation.
	 */
	public static BDD makeMaxIndependentSetBDD_reduced(int n) {
		bdd = new BDD(n);
		falseNode = bdd.falseNode;
		trueNode = bdd.trueNode;

		// manuall extends the first three level;
		// first level
		int level = 1;
		Node root = new Node();
		root.i = level;
		bdd.nodes[bdd.numOfNode++] = root;
		bdd.root = root;

		// second level
		level = 2;
		Node n21 = new Node();
		n21.i = level;
		bdd.nodes[bdd.numOfNode++] = n21;
		Node n22 = new Node();
		n22.i = level;
		bdd.nodes[bdd.numOfNode++] = n22;
		root.lo = n21;
		root.hi = n22;

		// third level
		level = 3;
		Node n31 = new Node();
		n31.i = level;
		bdd.nodes[bdd.numOfNode++] = n31;
		Node n32 = new Node();
		n32.i = level;
		bdd.nodes[bdd.numOfNode++] = n32;
		Node n33 = new Node();
		n33.i = level;
		bdd.nodes[bdd.numOfNode++] = n33;
		n21.lo = n31;
		n21.hi = n32;
		n22.lo = n33;
		n22.hi = falseNode;

		// extend first branch
		levelList = new Node[n][3];
		n31.lo = falseNode;
		n31.hi = makeChild_reduced(n31, false, level + 1, 2, 0);

		// extend second branch
		levelList = new Node[n][3];
		n32.lo = makeChild_reduced(n32, true, level + 1, 1, 1);
		;
		n32.hi = falseNode;

		// extend third branch
		levelList = new Node[n][3];
		n33.lo = makeChild_reduced(n33, true, level + 1, 0, 2);
		;
		n33.hi = makeChild_reduced(n33, false, level + 1, 0, 0);

		return bdd;
	}

	/**
	 * ll is used to judge whther conflict with first two element. ll=0, in
	 * right branch; ll=1, in left-right branch; ll=2, in left-left branch.
	 * 
	 * numOfZeros is used to track how many 0s we have choosen. ll is the number
	 * of zeros in from the start point in the cricle.
	 * 
	 * number of zeros is similar to ll, but it is for current node. level >=4;
	 */
	static Node makeChild_reduced(Node parent, boolean left, int level, int ll,
			int numOfZeros) {
		Node node;

		// special handling for last level.
		if (level == bdd.numOfVar) {
			node = new Node();
			bdd.nodes[bdd.numOfNode++] = node;
			node.i = level;
			System.out.println(node);
			if (left) {// previous step is left
				// set lo
				if (numOfZeros >= 2) {
					System.out.println("number of zeros = 3 : false");
					node.lo = falseNode;
				} else if (numOfZeros == 1) {
					if (ll >= 1) {
						node.lo = falseNode;
					} else {
						node.lo = trueNode;
					}
				} else {
					System.out.println("Impossilbe");
				}
				// set hi
				if (ll >= 1) {// left is at the beginning.
					node.hi = trueNode;
				} else {
					node.hi = falseNode;
				}
			} else {// previous step is right
				// set lo
				if (ll >= 2) {
					System.out.println("number of ll= 2 : false");
					node.lo = falseNode;
				} else {
					node.lo = trueNode;
				}
				// set hi
				node.hi = falseNode;
			}

			return node;
		}

		Node temp = levelList[level - 1][numOfZeros];
		if (temp != null) {
			return temp;
		}

		// now we need to create node.
		node = new Node();
		bdd.nodes[bdd.numOfNode++] = node;
		node.i = level;

		if (left) {
			if (numOfZeros >= 2) {
				System.out.println("number of zeros = 3 : false");
				node.lo = falseNode;
			} else {
				node.lo = makeChild_reduced(node, true, level + 1, ll,
						numOfZeros + 1);
			}
			node.hi = makeChild_reduced(node, false, level + 1, ll, 0);
			levelList[level - 1][numOfZeros] = node;
		} else {
			node.hi = falseNode;
			node.lo = makeChild_reduced(node, true, level + 1, ll, 1);
			levelList[level - 1][numOfZeros] = node;
		}

		return node;
	}

	static Node[][] nds;

	/**
	 * Independent set of n elements in a circle if any two adjacent elements
	 * are choosen, the set is not independent. so it is a n bit boolean
	 * function. but we should not try to get the truth table for it, since it
	 * will be too long to store if n is big, say 100.
	 * 
	 * if n is big, we can not build the BDD in an normal way, if we do not
	 * reduce it during the building process, it will become very big. if we
	 * keep reducing in the process, the resulted unreduced BDD should have a
	 * reasonable size, for example, n * 4. Then we can reduce it.
	 * 
	 * Note the returned BDD may be not completely reduced.
	 */
	public static BDD makeIndependentSetBDD_reduced(int n) {
		List<Node>[] headList = new ArrayList[n];

		bdd = new BDD(n);
		/**
		 * 0: level 1: left branch or right branch from root
		 * 
		 */
		nds = new Node[n][2];
		int level = 1;
		Node root = new Node();
		bdd.root = root;
		bdd.nodes[bdd.numOfNode++] = root;
		root.i = level;

		// headList[0] = new ArrayList<Node>();
		// headList[0].add(root);

		root.lo = makeChild_reduced(root, true, level + 1, true);
		root.hi = makeChild_reduced(root, false, level + 1, false);

		// bdd.reduce();
		// (suppose n>3, the case is simple for n=1 and n=2;)
		nds=null;
		return bdd;
	}

	/**
	 * ll is used to judge whther conflict with first element.
	 */
	static Node makeChild_reduced(Node parent, boolean left, int level,
			boolean ll) {
		Node node;

		if (level == 2 || level == 3) {
			node = new Node();
			node.i = level;
			if (left) {
				node.lo = makeChild_reduced(node, true, level + 1, ll);
				node.hi = makeChild_reduced(node, false, level + 1, ll);
			} else {
				node.hi = falseNode;
				node.lo = makeChild_reduced(node, true, level + 1, ll);
			}
			bdd.nodes[bdd.numOfNode++] = node;
			return node;
		} else if (level == bdd.numOfVar) {
			node = new Node();
			node.i = level;

			if (left) {
				// set lo
				node.lo = trueNode;
				// set hi
				if (ll) {
					node.hi = trueNode;
				} else {
					node.hi = falseNode;
				}
			} else {// previous step is right
				// set lo
				node.lo = trueNode;
				// set hi
				node.hi = falseNode;
			}

			bdd.nodes[bdd.numOfNode++] = node;
			return node;
		}

		if (left) {
			int lli = ll ? 0 : 1;
			if (nds[level - 1][lli] != null) {
				return nds[level - 1][lli];
			} else {
				node = new Node();
				node.i = level;
				node.lo = makeChild_reduced(node, true, level + 1, ll);
				node.hi = makeChild_reduced(node, false, level + 1, ll);
				nds[level - 1][lli] = node;
			}
		} else {
			node = new Node();
			node.i = level;
			node.hi = falseNode;
			node.lo = makeChild_reduced(node, true, level + 1, ll);
		}
		bdd.nodes[bdd.numOfNode++] = node;
		return node;
	}

	static BDD getBDDForMedian(int numOfVar) {
		BDD bdd = new BDD(numOfVar);
		Node node1 = new Node();
		Node node21 = new Node();
		Node node22 = new Node();
		Node node3 = new Node();
		bdd.numOfNode = 6;
		bdd.root = node1;

		node1.i = 1;
		node1.lo = node21;
		node1.hi = node22;

		node21.i = 2;
		node21.lo = bdd.falseNode;
		node21.hi = node3;

		node22.i = 2;
		node22.lo = node3;
		node22.hi = bdd.trueNode;

		node3.i = 3;
		node3.lo = bdd.falseNode;
		node3.hi = bdd.trueNode;

		return bdd;
	}

	public static BDD makeBDDForFalse(int numOfVar) {
		BDD bdd = new BDD(numOfVar);
		bdd.root = bdd.falseNode;
		return bdd;
	}

	public static BDD makeBDDForTrue(int numOfVar) {
		BDD bdd = new BDD(numOfVar);
		bdd.root = bdd.trueNode;
		return bdd;
	}
	
	public static BDD makeFullBDD(boolean[] truthTable, int numOfVar) {
		bdd = new BDD(numOfVar);
		int level = 1;
		Node root = new Node();
		bdd.root = root;
		bdd.nodes[bdd.numOfNode++] = root;

		root.i = level;

		int n = truthTable.length;
		boolean[] leftT = Arrays.copyOfRange(truthTable, 0, n / 2);
		boolean[] rightT = Arrays.copyOfRange(truthTable, n / 2, n);
		makeFullBDD(bdd.root, leftT, true, 2);
		makeFullBDD(bdd.root, rightT, false, 2);
		return bdd;
	}

	static private void makeFullBDD(Node parent, boolean[] truthTable, boolean left,
			int level) {
		System.out.print("for Level = " + level + "; truth table is ");
		System.out.println(new TruthTable(truthTable).toString());

		if (level >= bdd.numOfVar + 1) {
			if (left) {
				if (truthTable[0]) {
					parent.lo = trueNode;
				} else {
					parent.lo = falseNode;
				}
			} else {
				if (truthTable[0]) {
					parent.hi = trueNode;
				} else {
					parent.hi = falseNode;
				}
			}
			return;
		}

		Node temp = new Node();
		temp.i = level;
		temp.tt = TruthTable.getString(truthTable);
		bdd.nodes[bdd.numOfNode++] = temp;
		if (left == true) {
			parent.lo = temp;
		} else {
			parent.hi = temp;
		}

		int n = truthTable.length;
		boolean[] leftT = Arrays.copyOfRange(truthTable, 0, n / 2);
		boolean[] rightT = Arrays.copyOfRange(truthTable, n / 2, n);
		makeFullBDD(temp, leftT, true, level + 1);
		makeFullBDD(temp, rightT, false, level + 1);

	}
}
