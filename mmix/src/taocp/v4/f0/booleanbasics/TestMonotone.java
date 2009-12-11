/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.booleanbasics;

import java.util.Arrays;

import taocp.v4.f1.bitwise.TestBitUtil;

import junit.framework.TestCase;

/**
 * <p>
 * TestMonotone.java
 * </p>
 */
public class TestMonotone extends TestCase {
	public void test3() {
		System.out.println(Arrays.toString(Monotone.getMonotoneSequenceByNumberOfOne(3)));
	}

	public void test4() {
		System.out.println(Arrays.toString(Monotone.getMonotoneSequenceByNumberOfOne(4)));
	}

	public void test5() {
		System.out.println(Arrays.toString(Monotone.getMonotoneSequenceByNumberOfOne(5)));
	}
	
	public void test7() {
		System.out.println(Arrays.toString(Monotone.getMonotoneSequenceByNumberOfOne(7)));
	}
	
	public void test7_16() {
		for (int i = 7; i <= 16; i++) {
			long [] a = Monotone.getMonotoneSequenceByNumberOfOne(i);
			long count=0;
			for (int j = 0; j < (1<<i); j++) {
				
				count+=a[j];
			}
			long expected = (1L<<i)*((1L<<i)-1)/2;
			assertEquals(expected, count);
		}
	}

	public void test6() {
		long[] a = Monotone.getMonotoneSequenceByNumberOfOne(6);
		System.out.println(Arrays.toString(a));
		assertEquals(1<<6, a.length);
		assertFalse(0 == a[a.length-1]);
	}

	public void test2() {
		System.out.println(Arrays.toString(Monotone.getMonotoneSequenceByNumberOfOne(3)));
		int num = 3;
		boolean[] truthTable = TestBitUtil.testTruthTableForS2();
		this.assertEquals(true, Monotone.isMonotone(truthTable, num));
	}
}
