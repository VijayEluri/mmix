/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.fabonacii.heap;

import junit.framework.TestCase;

/**
 * <p>
 * TestFabonacciHeap.java
 * </p>
 */
public class TestFabonaciiHeap extends TestCase {
	FabonaciiHeap fh = new FabonaciiHeap();

	public void test() {
		Node[] nodes = new Node[10];
		for (int i = 0; i < 10; i++) {
			System.out.println();
			System.out.println("i=" + i);
			nodes[i] = new Node(i);
			fh.insert(nodes[i]);
			fh.printRootList();
			fh.print();
		}

		for (int i = 5; i < 10; i++) {
			System.out.println();
			System.out.println("i=" + i);
			fh.decreaseKey(nodes[i], i - 5);
			fh.printRootList();
			fh.print();
		}

		for (int i = 0; i < 5; i++) {
			System.out.println("minNode=" + fh.extractMin());
			fh.print();
		}

	}
}
