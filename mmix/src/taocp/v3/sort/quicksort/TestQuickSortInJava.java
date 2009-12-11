/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.quicksort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import taocp.v3.sort.radixsort.RadixSort;

/**
 * <p>
 * TestQuickSortInJava.java
 * </p>
 */
public class TestQuickSortInJava extends TestCase {
	Random random = new Random();
	long start, end;
	int value;

	public void testJavaCollSort() {
		List<Integer> list = new ArrayList<Integer>(1 << 20);
		for (int i = 0; i < 1 << 20; i++) {
			value = random.nextInt();
			if (value < 0) {
				list.add(value >>> 1);
			} else {
				list.add(value);
			}
		}

		start = System.currentTimeMillis();
		Collections.sort(list);
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to sort the list in java collection.");
	}

	public void testQuickSortInJava() {
		System.out.println("Quick Sort in Java");
		int[] a = new int[1 << 20];
		start = System.currentTimeMillis();
		for (int i = 0; i < a.length; i++) {
			a[i] = random.nextInt();
		}
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to init.");

		start = System.currentTimeMillis();
		Arrays.sort(a);
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to sort.");
	}

	public void testRadixSort() {
		System.out.println("Radix Sort");
		int[] a = new int[1 << 20];
		start = System.currentTimeMillis();
		for (int i = 0; i < a.length; i++) {
			a[i] = random.nextInt();
		}
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to init.");

		start = System.currentTimeMillis();
		RadixSort.sort(a, 0, a.length);
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to sort.");
	}

	/**
	 * in current problem size (n=1M), radix sort can not beat quick sort in
	 * array , which is ten times faster.
	 */
	public void testCompare() {

		int[] a = new int[1 << 12];
		int[] b = new int[1 << 12];
		int[] c = new int[1 << 12];
		start = System.currentTimeMillis();
		for (int i = 0; i < a.length; i++) {
			a[i] = random.nextInt();
			if (a[i] < 0) {
				a[i] = a[i] >>> 1;
			}
			c[i]= b[i] = a[i];
		}
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to init.");

		System.out.println("quick Sort");
		start = System.currentTimeMillis();
		Arrays.sort(b);
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to sort.");

		System.out.println("Radix Sort");
		start = System.currentTimeMillis();
		RadixSort.sort_array(a, 0, a.length);
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to sort.");

//		System.out.println(Arrays.toString(Arrays..copyOfRange(a, 0, 10)));
//		System.out.println(Arrays.toString(Arrays.copyOfRange(b, 0, 10)));

		

	}

	public void testCompare2() {
		System.out.println("Radix Sort");
		int[] a = new int[60];
		int[] b = new int[60];
		int[] c = new int[60];
		start = System.currentTimeMillis();
		for (int i = 0; i < a.length; i++) {
			a[i] = 1 << (i / 2);
			c[i]=b[i] = a[i];
		}
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to init.");

		Arrays.sort(b);

		start = System.currentTimeMillis();
		RadixSort.sort2(a, 0, a.length);
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to sort.");

//		System.out.println(Arrays.toString(Arrays.copyOfRange(a, 0, 60)));
//		System.out.println(Arrays.toString(Arrays.copyOfRange(b, 0, 60)));

		this.assertTrue(Arrays.equals(a, b));
		start = System.currentTimeMillis();
		RadixSort.sort_array(c, 0, c.length);
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to sort.");

//		System.out.println(Arrays.toString(Arrays.copyOfRange(a, 0, 60)));
//		System.out.println(Arrays.toString(Arrays.copyOfRange(b, 0, 60)));

		this.assertTrue(Arrays.equals(c, b));

	}
	/**
	 * sample out put It takes 63 milli seconds to init. quick Sort It takes 328
	 * milli seconds to sort. Radix Sort It takes 2574 milli seconds to sort.
	 */

}
