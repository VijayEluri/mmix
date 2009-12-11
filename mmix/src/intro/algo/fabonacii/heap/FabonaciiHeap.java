/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.fabonacii.heap;

/**
 * not finished yet.
 * <p>
 * FabonaciiHeap.java
 * </p>
 */
public class FabonaciiHeap {
	Node root;// point to the smallest one

	/**
	 * only key in x is set up already
	 * 
	 * @param x
	 */
	public void insert(Node x) {
		if (root == null) {
			root = x;
		} else {
			root.addNode(x);
		}
		if (x.key < root.key) {
			root = x;
		}
	}

	/**
	 * 
	 * @param v
	 * @param newKey
	 *            less than or equall to original key
	 */
	public void decreaseKey(Node v, int newKey) {
		v.key = newKey;
		Node p = v.parent;
		if (p == null) {// in root list
			if (root.key > newKey) {
				root = v;
			}
		} else if (p.key > newKey) {
			while (true) {
				if (p.rank >= 2) {
					v.removeFromFamiliy(p);
				} else {
					// no need t to remove from family.
					p.child = null;
				}
				root.addNode(v);
				if (v.key < root.key) {
					root = v;
				}
				// case 1: remove child from root.
				Node pp = p.parent;
				if (pp == null) {
					p.rank -= 1;
					break;
				}
				// case 2: remove from untagged parent, just remove it and tag
				// it.
				if (p.tagged == false) {
					p.rank -= 1;
					p.tagged = true;
					break;
				} else {// case 3: tagged parent, need recursive removing.
					p.rank -= 1;
					v = p;
					p = pp;
				}

			}
		}
	}

	public Node extractMin() {
		if (root == null) {
			return null;
		}
		Node res = root;
		Node v, w;
		if (root.rank < 1) {// no child
			v = root.right_sibling;
		} else {
			w = root.child;
			v = w.right_sibling;
			w.right_sibling = root.right_sibling;//tricky, the middle state.
			for (w = v; w != root.right_sibling; w = w.right_sibling) {
				w.parent = null;
			}
			while(v!=root){
				w=v.right_sibling;
				//addNodeToA(v);
				v=w;
				
			}

		}
		return res;
	}

	/**
	 * 
	 * @param head
	 */
	public void printRootList() {// OutputStream out
		if (root == null) {
			return;
		}
		Node t = root;
		System.out.print("Tree: [");
		do {
			System.out.print(t.key + ",");
			t = t.right_sibling;
		} while (t != root);
		System.out.println("]");
	}

	public void print() {
		if (root == null)
			return;
		Node t = root;
		do {
			printTree(t);
			t = t.right_sibling;
		} while (t != root);
		// for(Node t=root; t)
	}

	public void printTree(Node head) {// OutputStream out
		head.printTree();
	}
}
