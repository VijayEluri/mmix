/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.fabonacii.heap;

import intro.algo.binomial.heap.LeftChildRightSiblingNode;
import intro.algo.binomial.heap.NodeUtil;

/**
 * <p>
 * Node.java
 * </p>
 */
public class Node implements LeftChildRightSiblingNode {
	int key;

	int rank;// # of childer.

	boolean tagged;// critical one(r_j==j-2) will be tagged

	Node parent;

	Node child;

	Node left_sibling;

	Node right_sibling;

	public Node(int key) {
		this.key = key;
		left_sibling = this;
		right_sibling = this;
		parent = null;
		child = null;
		rank = 0;
		tagged = false;
	}

	/**
	 * 
	 * @param x
	 *            x is not the only child
	 */
	public void removeFromFamiliy(Node p) {
		Node u = this.left_sibling;
		Node w = this.right_sibling;
		u.right_sibling = w;
		w.left_sibling = u;
		if (p.child == this) {
			p.child = w;
		}
	}

	/**
	 * if this is the root of double linked list
	 * 
	 * @param x
	 */
	public void addNode(Node x) {
		// suppose there are two or more than two nodes in list.
		// after get the code, verify it works when there is only one node
		Node u = this.left_sibling;
		x.right_sibling = this;
		x.left_sibling = u;
		this.left_sibling = x;
		u.right_sibling = x;

		x.parent = null;
	}

	public LeftChildRightSiblingNode getChild() {
		return child;
	}
	
	public void printTree(){
		NodeUtil.printTree(this);
	} 

	/**
	 * How many children the Node have. based on degree, we can know the tree
	 * size(2^degree)
	 * 
	 * @return
	 */
	public int getDegree() {
		if (child == null) {
			return 0;
		} else {
			int count = 0;
			Node t = child;
			do {
				count++;
				t = t.right_sibling;
			} while (t != null & t != child);
			return count;
		}
	}

	public LeftChildRightSiblingNode getSibling() {
		return this.right_sibling;
	}
	
	public String toString() {
		return "[" + key + "; " + getDegree() + "; c="
				+ (child == null ? "" : "" + child.key) + "s="
				+ (right_sibling == null ? "" : "" + right_sibling.key) + "]";
	}
}
