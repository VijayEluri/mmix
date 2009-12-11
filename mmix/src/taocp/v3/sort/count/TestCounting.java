/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.count;

import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;

/**
 * <p>
 * TestCounting.java
 * </p>
 */
public class TestCounting extends TestCase {
	static Random ran = new Random(0);

	public void test() {
		int[] a = new int[10];
		for (int i = 0; i < 10; i++) {
			a[i] = ran.nextInt(999);
			if (a[i] < 0) {
				a[i] = a[i] >>> 1;
			}
			
		}
		System.out.println(Arrays.toString(a));
		Counting.sort(a);
		System.out.println(Arrays.toString(a));
	}
	
	public void test2() {
		int[] a = new int[20];
		for (int i = 0; i < 10; i++) {
			a[i] = ran.nextInt(999);
			if (a[i] < 0) {
				a[i] = a[i] >>> 1;
			}
			a[i+10]=a[i];
		}
		System.out.println(Arrays.toString(a));
		Counting.sort(a);
		System.out.println(Arrays.toString(a));
	}
	
	public void testSpecialSorting() {
		int[] a = new int[20];
		for (int i = 0; i < 10; i++) {
			a[i] = ran.nextInt(999);
			if (a[i] < 0) {
				a[i] = a[i] >>> 1;
			}
			a[i+10]=a[i];
		}
		System.out.println(Arrays.toString(a));
		int[] b=SpecialSort.sort(a);
		System.out.println(Arrays.toString(b));
	}
}
