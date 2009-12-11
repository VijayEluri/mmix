/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.binomial.heap;

import java.util.Arrays;
import java.util.Random;

import util.MathUtil;

import junit.framework.TestCase;

/**
 * <p>
 * TestBinomialHeap.java
 * </p>
 */
public class TestBinomialHeap extends TestCase {
	public void test() {
		BinomialHeap b = new BinomialHeap();
		Node x = null;
		Node[] xx = new Node[10];
		for (int i = 0; i < 10; i++) {
			System.out.println("i=" + i);
			x = new Node();
			xx[i] = x;
			x.key = i + 1;
			b.insert(x);
			b.print();
			System.out.println();
		}		
	}

	public void testExtract() {
		BinomialHeap b = new BinomialHeap();
		Node x = null;
		Node[] xx = new Node[10];
		for (int i = 0; i < 10; i++) {
			x = new Node();
			xx[i] = x;
			x.key = i + 1;
			b.insert(x);
		}
		System.out.println("after build the heap");
		b.print();

		Node min = b.extractMin();
		System.out.println("min=" + min);
		b.print();

		for (int i = 0; i < 10; i++) {
			System.out.println();
			System.out.println("i=" + i);
			min = b.extractMin();
			System.out.println("min=" + min);
			b.print();
			
		}
	}

	public void testRandom() {
		Random r = new Random();
		int arraySize = 10;
		int[] a = MathUtil.getRandomPositiveIntArray(arraySize,1);
		System.out.println(Arrays.toString(a));
		Node x;
		BinomialHeap b = new BinomialHeap();
		for (int i = 0; i < 10; i++) {
			x = new Node();			
			x.key =a[i];
			b.insert(x);
		}
		System.out.println("after build the heap");
		b.print();

		Node min = b.extractMin();
		System.out.println("min=" + min);
		b.print();

		for (int i = 0; i < 10; i++) {
			System.out.println();
			System.out.println("i=" + i);
			min = b.extractMin();
			System.out.println("min=" + min);
			b.print();
			
		}
	}
}
