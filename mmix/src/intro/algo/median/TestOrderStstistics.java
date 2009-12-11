/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.median;

import java.util.Arrays;

import junit.framework.TestCase;
import taocp.v3.TAOCPSample;
import taocp.v3.sort.merge.MergeSort;
import util.MathUtil;

/**
 * <p>
 * TestOrderStstistics.java
 * </p>
 */
public class TestOrderStstistics extends TestCase {
	public void test() {
		int[] a = TAOCPSample.getSample_ZeroIndexed();
		System.out.println(Arrays.toString(a));
		int t = new OrderStatistics().getMedian(a);
		System.out.println("median is " + t);

		new MergeSort().sort(a);
		System.out.println(Arrays.toString(a));
		int expect = a[(a.length - 1) / 2];
		assertEquals(expect, t);
	}

	int[] a = new int[] { 39, 11, 41, 34, 32, 37, 6, 34, 36, 18, 33, 29, 33,
			14, 15, 35, 8, 35, 26, 25, 35, 24, 33, 36, 16, 28, 95, 76, 66, 90,
			80, 64, 75, 88, 77, 85, 92, 87, 86, 81, 83, 77, 79, 83, 72, 68, 75,
			89, 73, 63, 85, 84 };

	public void test44() {

		System.out.println(Arrays.toString(a));
		int t = new OrderStatistics().getMedian(a);
		System.out.println("median is " + t);

		new MergeSort().sort(a);
		System.out.println(Arrays.toString(a));
		int expect = a[(a.length - 1) / 2];
		assertEquals(expect, t);
	}

	public void test2() {

		int arraySize = 1 << 8;
		int[] a = MathUtil.getRandomPositiveIntArray(arraySize, 1);
		int[] a2= a.clone();
		int t = new OrderStatistics().getMedian(a);
		System.out.println("median is " + t);

		new MergeSort().sort(a);
		int expect = a[(a.length - 1) / 2];
		System.out.println(Arrays.toString(a));
		System.out.println("median is " + expect);
		assertEquals(expect, t);
		//TODO: failed here!
		expect=new Median().getMedian(a2);
		assertEquals(expect, t);
	}

	public void test3() {
		int arraySize = 1 << 16;

		int[] a = MathUtil.getRandomPositiveIntArray(arraySize, 1);
		int t = new OrderStatistics().getMedian(a);
		System.out.println(Arrays.toString(a));
		System.out.println(Arrays.toString(MathUtil.getHistogram(a)));
		new MergeSort().sort(a);
		int expect = a[(a.length - 1) / 2];
		System.out.println(Arrays.toString(MathUtil.getHistogram(a)));
		System.out.println("median is " + expect);
		for (int i = 0; i < 10; i++) {
			System.out.println("median is " + a[(a.length - 1) / 2 - i]);
			System.out.println("median is " + a[(a.length - 1) / 2 + i]);
		}
		assertEquals(expect, t);
	}
}
