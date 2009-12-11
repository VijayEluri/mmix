/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.fabonacii.heap;

import junit.framework.TestCase;

/**
 * <p>
 * TestDoubleLinkedList.java
 * </p>
 */
public class TestDoubleLinkedList extends TestCase {
	public void testAdd() {
		DoubleLinkedList ll = new DoubleLinkedList(new Node(3));
		ll.print();
		for (int i = 0; i < 10; i++) {
			ll.addNode(new Node(i));
			ll.print();
		}
	}
}
