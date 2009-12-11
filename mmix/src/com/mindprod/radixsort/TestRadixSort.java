/*
 * @(#)TestRadixSort.java
 *
 * Summary: Demonstrate how to use the RadixSort clas.
 *
 * Copyright: (c) 1996-2009 Roedy Green, Canadian Mind Products, http://mindprod.com
 *
 * Licence: This software may be copied and used freely for any purpose but military.
 *          see http://mindprod.com/contact/nonmil.html
 *
 * Requires: JDK 1.5+
 *
 * Created with: IntelliJ IDEA IDE.
 *
 * Version History:
 *  1.6 2008-01-01 - add generics to Comparator
 */

package com.mindprod.radixsort;

import java.util.Random;

/**
 * Demonstrate how to use the RadixSort clas.
 * 
 * 
 * @author Roedy Green, Canadian Mind Products
 * @since 1996
 * @version 1.6 2008-01-01 - add generics to Comparator
 */

public class TestRadixSort {
	// --------------------------- main() method ---------------------------

	// Test RadixSort by sorting N random Strings

	public static void main(String[] args) {
		final int N = 1000000;

		String[] anArray = new String[N];
		Random wheel = new Random(149);
		for (int i = 0; i < anArray.length; i++) {
			// keys of form A9999
			anArray[i] = "A" + ((wheel.nextInt() & Integer.MAX_VALUE) % 10000);
		}
		System.out.println("Start RadixSort items=" + N);
		long start = System.currentTimeMillis();

		RadixSort.sort(anArray, new Latin1Comparator(), 5);

		long stop = System.currentTimeMillis();
		System.out.println("Elapsed:" + (stop - start));

		// waste a little time to let user admire the results
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
	}// end main
}// end class TestRadixSort

// Callback delegate to describe collating sequence
/**
 * Start RadixSort items=100000 Elapsed:269
 */
// to RadixSort
class Latin1Comparator implements RadixComparator<String> {
	// -------------------------- PUBLIC INSTANCE METHODS
	// --------------------------

	// Comparator two Strings. Callback for sort.
	// effectively returns a-b;
	// e.g. +1 (or any +ve number) if a > b
	// 0 if a == b
	// -1 (or any -ve number) if a < b

	public final int compare(String a, String b) {
		return a.compareTo(b);
	}// end compare

	/* compare comparators */
	public final boolean equals(Object a, Object b) {
		return (a == b);
	}// end equals

	public final int getKeyByteAt(String a, int offset) {
		if (offset >= a.length()) {
			return 0;
		}
		// only use low order 8 bits in sort key
		return a.charAt(offset) & 0xff;
	}// end getKeyByteAt
}// end class Latin1Comparator
