/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.fabonacii.heap;

import java.io.OutputStream;

/**
 * <p>
 * DoubleLinkedList.java
 * </p>
 */
public class DoubleLinkedList {
	Node head;

	/**
	 * 
	 * @param head
	 *            Not null
	 */
	public DoubleLinkedList(Node head) {
		this.head = head;
	}

	/**
	 * 
	 * @param head
	 *            head of the list -Not null
	 * @param x
	 *            Node to insert - Not Null
	 */
	public DoubleLinkedList addNode(Node x) {
		// suppose there are two or more than two nodes in list.
		// after get the code, verify it works when there is only one node
		Node u = head.left_sibling;
		x.right_sibling = head;
		x.left_sibling = u;
		head.left_sibling = x;
		u.right_sibling = x;
		return this;
	}

	public void print() {// OutputStream out
		Node t = head;
		System.out.print("[");
		do {
			System.out.print(t.key + ",");
			t = t.right_sibling;
		} while (t != head);
		System.out.println("]");
	}

	public void printReverse() {// OutputStream out
		Node t = head;
		System.out.print("[");
		do {
			System.out.print(t.key + ",");
			t = t.left_sibling;
		} while (t != head);
		System.out.println("]");
	}
}
