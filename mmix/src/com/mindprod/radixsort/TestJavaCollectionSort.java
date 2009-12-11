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

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * compare embedded Java Collections sorting with Radix sorting. Radix is better
 * but not that much.
 * 
 * Start Java collections sorting items=1000000 Java collections sorting
 * Elapsed:3639 Radix sort Elapsed:3265
 * 
 * @author Roedy Green, Canadian Mind Products
 * @since 1996
 * @version 1.6 2008-01-01 - add generics to Comparator
 */

public class TestJavaCollectionSort {
	// --------------------------- main() method ---------------------------

	// Test RadixSort by sorting N random Strings

	public static void main(String[] args) {
		final int N = 1000000;

		String[] anArray = new String[N];
		String[] bnArray = new String[N];
		Random wheel = new Random(149);
		for (int i = 0; i < anArray.length; i++) {
			// keys of form A9999
			anArray[i] = "A" + ((wheel.nextInt() & Integer.MAX_VALUE) % 10000);
			bnArray[i] = anArray[i];
		}
		System.out.println("Start Java collections sorting items=" + N);
		long start, stop;

		start = System.currentTimeMillis();
		Collections.sort(Arrays.asList(anArray), new Latin1Comparator());
		stop = System.currentTimeMillis();
		System.out
				.println("Java collections sorting Elapsed:" + (stop - start));

		start = System.currentTimeMillis();
		RadixSort.sort(bnArray, new Latin1Comparator(), 5);
		stop = System.currentTimeMillis();
		System.out.println("Radix sort Elapsed:" + (stop - start));

	}// end main
}// end class TestRadixSort

