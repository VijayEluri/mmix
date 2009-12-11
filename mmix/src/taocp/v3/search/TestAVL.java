package taocp.v3.search;

import junit.framework.TestCase;

public class TestAVL extends TestCase {
	public void test() {
		AVLTree avl = new AVLTree();
		int[] a = new int[] { 16, 3, 7, 11, 9, 26, 18, 14, 15 };
		for (int i = 0; i < a.length; i++) {
			System.out.println("i=" + i);
			avl.add(a[i]);
			avl.print();
			assertEquals(true, avl.search(a[i]));
		}
	}

	public void testLeftRight() {
		AVLTree avl = new AVLTree();
		int[] a = new int[] { 3, 1, 2 };
		for (int i = 0; i < a.length; i++) {
			System.out.println("i=" + i);
			avl.add(a[i]);
			avl.print();
			// assertEquals(true, avl.search(a[i]));
		}
		avl.print();

		a = new int[] { 4, 7, 6 };
		avl = new AVLTree();
		for (int i = 0; i < a.length; i++) {
			System.out.println("i=" + i);
			avl.add(a[i]);
			avl.print();
			// assertEquals(true, avl.search(a[i]));
		}
		avl.print();
		
		//left left case
		a = new int[] { 8, 7, 6 };
		avl = new AVLTree();
		for (int i = 0; i < a.length; i++) {
			System.out.println("i=" + i);
			avl.add(a[i]);
			avl.print();
			// assertEquals(true, avl.search(a[i]));
		}
		avl.print();
		
		//right right case
		a = new int[] { 6, 7, 8 };
		avl = new AVLTree();
		for (int i = 0; i < a.length; i++) {
			System.out.println("i=" + i);
			avl.add(a[i]);
			avl.print();
			// assertEquals(true, avl.search(a[i]));
		}
		avl.print();
	}
	
	public void testLeftRight2() {
		AVLTree avl = new AVLTree();
		int[] a = new int[] { 3, 1, 2, 4};
		for (int i = 0; i < a.length; i++) {
			System.out.println("i=" + i);
			avl.add(a[i]);
			avl.print();
			// assertEquals(true, avl.search(a[i]));
		}
		avl.print();

		a = new int[] { 3, 1, 2, 4, 5 };
		avl = new AVLTree();
		for (int i = 0; i < a.length; i++) {
			System.out.println("i=" + i);
			avl.add(a[i]);
			avl.print();
			// assertEquals(true, avl.search(a[i]));
		}
		avl.print();
		
		//left left case
		a = new int[] { 3, 1, 2, 4, 5, 6 };
		avl = new AVLTree();
		for (int i = 0; i < a.length; i++) {
			System.out.println("i=" + i);
			avl.add(a[i]);
			avl.print();
			// assertEquals(true, avl.search(a[i]));
		}
		avl.print();
		
		//right right case
		a = new int[] { 3, 1, 2, 4, 5, 6, 7, 8 };
		avl = new AVLTree();
		for (int i = 0; i < a.length; i++) {
			System.out.println("i=" + i);
			avl.add(a[i]);
			avl.print();
			// assertEquals(true, avl.search(a[i]));
		}
		avl.print();
	}
}
