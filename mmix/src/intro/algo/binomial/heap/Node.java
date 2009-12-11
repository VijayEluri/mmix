/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.binomial.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * Why we want to reuse the code? One example is the code for complex data
 * structure; those kind of code is hard to get it correct. It is really hadr
 * and it takes effor to implement it, we do not want to waste effort to
 * reinvent the wheel again and again.
 * 
 * <p>
 * Node.java
 * </p>
 */
class Node {
	public int key;// in reality, should be an Object.

	// int degree;// number of children. only used in concept level
	// Node parent;//if we want to decrease key.
	Node child;

	Node sibling;// singly linked, no cycle

	// public boolean isRoot() {
	// return parent == null;
	// }
	public Node() {

	}

	public Node(int key) {
		this.key = key;
	}

	public boolean isLeaf() {
		return child == null;
	}

	/**
	 * How many children the Node have. based on degree, we can know the tree
	 * size(2^degree)
	 * 
	 * @return
	 */
	public int getDegree() {
		if (isLeaf()) {
			return 0;
		} else {
			int count = 0;
			Node t = child;
			do {
				count++;
				t = t.sibling;
			} while (t != null & t != child);
			return count;
		}
	}

	/**
	 * call this after the status change
	 */
	// public void validate() {
	// if (_getDegree() != degree) {
	// throw new RuntimeException("degree is wrong: degree=" + degree
	// + "; getDegree=" + _getDegree());
	// }
	// }
	public String toString() {
		return "[" + key + "; " + getDegree() + "; c="
				+ (child == null ? "" : "" + child.key) + "s="
				+ (sibling == null ? "" : "" + sibling.key) + "]";
	}

	// private void resetParam() {
	// list.clear();
	// list2.clear();
	// }

	/**
	 * Treat this as the root of the tree and print out the tree.
	 */
	public void printTree() {
		/**
		 * auxiliary list for output the tree rooted at this node.
		 */
		List<Node> list = new ArrayList<Node>();

		List<Node> list2 = new ArrayList<Node>();
		Node rootNode = this;
		// resetParam();
		int level = rootNode.getDegree();
		// the depth of Node with (degree) children is (degree+1), labeled from
		// 0 to degree.
		System.out.println("Tree: root degree=" + level);
		System.out.println(rootNode + " -> ");// root have no sibling.
		if (level > 0) {
			list.add(rootNode);
		}
		while (!list.isEmpty()) {// loop on levels
			for (Node node : list) {
				if (node.child != null) {// redundant
					printLevel(node.child, list2);
				}
				System.out.print("\t");
			}
			System.out.println();
			list.clear();
			List<Node> t = list;
			list = list2;
			list2 = t;
		}
		// resetParam();
	}

	private void printLevel(Node r, List<Node> list2) {
		Node s = r;
		do {
			System.out.print(s + " -> ");
			if (s.getDegree() > 0) {
				list2.add(s);
			}
			s = s.sibling;
		} while (s != null && s != r);
	}
}