/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.radixsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * RadixSort.java Implement a radix sort by myself. check whether it can beat
 * the quick sort in sun's JDK.
 * </p>
 */
public class RadixSort {
	/**
	 * Assume all the values in array are not negative
	 * 
	 * @param a
	 *            array of int
	 * @param from
	 *            inclusive
	 * @param to
	 *            exclusive
	 */
	public static void sort(int[] a, int from, int to) {
		int[] b = new int[a.length];
		int key;
		int value;

		List<Integer>[] ai = new List[256];// ai[0] should always be 0.
		List<Integer>[] bi = new List[256];

		// apply the idea in SGB football and gb_sort.
		// 1. Partition the gb sorted lists into alt sorted by low-order byte
		for (int i = 0; i < bi.length; i++) {
			bi[i] = new ArrayList<Integer>(a.length >>> 8);
		}
		for (int i = 0; i < a.length; i++) {
			key = a[i] & 0xff;
			bi[key].add(a[i]);
		}
		// to match the original algorithm.
		for (int i = 0; i < bi.length; i++) {
			Collections.reverse(bi[i]);
		}

		// 2. Here we must read from alt sorted from 0 to 255, not from 255 to
		// 0,
		// to get the desired final order. (Each
		// pass reverses the order of the lists; it’s tricky, but it works.)
		for (int i = 0; i < ai.length; i++) {
			ai[i] = new ArrayList<Integer>(a.length >>> 8);
		}
		for (int i = 0; i < bi.length; i++) {
			for (Iterator<Integer> it = bi[i].iterator(); it.hasNext();) {
				value = it.next();
				key = (value >> 8) & 0xff;
				ai[key].add(value);
			}
		}
		// to match the original algorithm.
		for (int i = 0; i < ai.length; i++) {
			Collections.reverse(ai[i]);
		}

		// 3. Partition the gb sorted lists into alt sorted by second-highest
		// byte 10 i
		for (int i = 0; i < bi.length; i++) {
			bi[i].clear();// = new ArrayList<Integer>(a.length>>>8);
		}
		for (int i = ai.length - 1; i >= 0; i--) {
			for (Iterator<Integer> it = ai[i].iterator(); it.hasNext();) {
				value = it.next();
				key = (value >> 16) & 0xff;
				bi[key].add(value);
			}
		}
		// to match the original algorithm.
		for (int i = 0; i < bi.length; i++) {
			Collections.reverse(bi[i]);
		}

		// 4. Partition the alt sorted lists into gb sorted by high-order byte
		for (int i = 0; i < ai.length; i++) {
			ai[i].clear();// = new ArrayList<Integer>(a.length>>>8);
		}
		for (int i = 0; i < bi.length; i++) {
			for (Iterator<Integer> it = bi[i].iterator(); it.hasNext();) {
				value = it.next();
				key = (value >> 24) & 0xff;
				ai[key].add(value);
			}
		}
		int count = 0;
		// get the result into array a.
		for (int i = 0; i < ai.length; i++) {
			for (Iterator<Integer> it = ai[i].iterator(); it.hasNext();) {
				a[count++] = it.next();
			}
		}
		// to match the original algorithm.
		for (int i = 0; i < ai.length; i++) {
			Collections.reverse(ai[i]);
		}

		return;
	}
	
	static List<Integer>[] ai = new List[256];
	static List<Integer>[] bi = new List[256];

	/**
	 * use plain way to implement it.
	 * 
	 * @param a
	 * @param from
	 * @param to
	 */
	public static void sort2(int[] a, int from, int to) {
		int[] b = new int[a.length];
		int key;
		int value;

		

		for (int i = bi.length - 1; i >= 0; i--) {
			bi[i] = new ArrayList<Integer>(a.length >>> 8);
		}
		for (int i = 0; i < ai.length; i++) {
			ai[i] = new ArrayList<Integer>(a.length >>> 8);
		}
		
		long start, end;
		start = System.currentTimeMillis();
		// apply the idea in SGB football and gb_sort.
		// 1. Partition the gb sorted lists into alt sorted by low-order byte
		for (int i = 0; i < a.length; i++) {
			key = a[i] & 0xff;
			bi[key].add(a[i]);
		}

		
		// 2. Here we must read from alt sorted from 0 to 255, not from 255 to
		// 0,
		// to get the desired final order. (Each
		// pass reverses the order of the lists; it’s tricky, but it works.)
		for (int i = bi.length - 1; i >= 0; i--) {
			for (int j = 0; j < bi[i].size(); j++) {
				value = bi[i].get(j);
				key = (value >> 8) & 0xff;
				ai[key].add(value);
			}
		}

		for (int i = 0; i < bi.length; i++) {
			bi[i].clear();
		}
		// 3. Partition the gb sorted lists into alt sorted by second-highest
		// byte 10 i
		for (int i = ai.length - 1; i >= 0; i--) {
			for (int j = 0; j < ai[i].size(); j++) {
				value = ai[i].get(j);
				key = (value >> 16) & 0xff;
				bi[key].add(value);
			}
		}

		for (int i = 0; i < ai.length; i++) {
			ai[i].clear();// = new ArrayList<Integer>(a.length>>>8);
		}
		// 4. Partition the alt sorted lists into gb sorted by high-order byte
		for (int i = bi.length - 1; i >= 0; i--) {

			for (int j = 0; j < bi[i].size(); j++) {
				value = bi[i].get(j);
				key = (value >> 24) & 0xff;
				ai[key].add(value);
			}
		}
		int count = 0;
		for (int i = 0; i < ai.length; i++) {
			count += ai[i].size();
			for (Iterator<Integer> it = ai[i].iterator(); it.hasNext();) {
				a[--count] = it.next();
			}
			count += ai[i].size();
		}
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to sort the list internally.");
		return;
	}
	
	/**
	 * use plain way to implement it.
	 * 
	 * @param a
	 * @param from
	 * @param to
	 */
	public static void sort_array(int[] a, int from, int to) {
		int[] b = new int[a.length];
		int key;
		int value;

		 int[][] ai = new int[256][a.length+1];
		int [][] bi= new int[256][a.length+1];
		for (int i = 0; i < 256; i++) {
			ai[i][0]=1;//the next available index.
			bi[i][0]=1;
		}
		
		// 1. Partition the gb sorted lists into alt sorted by low-order byte
		for (int i = 0; i < a.length; i++) {
			key = a[i] & 0xff;
			bi[key][bi[key][0]++]=a[i];
		}

		
		// 2. Here we must read from alt sorted from 0 to 255, not from 255 to
		// 0,
		// to get the desired final order. (Each
		// pass reverses the order of the lists; it’s tricky, but it works.)
		for (int i = bi.length - 1; i >= 0; i--) {
			for (int j = 1; j < bi[i][0]; j++) {
				value = bi[i][j];
				key = (value >> 8) & 0xff;
				ai[key][ai[key][0]++]=(value);
			}
		}

		for (int i = 0; i < bi.length; i++) {
			bi[i][0]=1;
		}
		// 3. Partition the gb sorted lists into alt sorted by second-highest
		// byte 10 i
		for (int i = ai.length - 1; i >= 0; i--) {
			for (int j = 1; j < ai[i][0]; j++) {
				value = ai[i][j];
				key = (value >> 16) & 0xff;
				bi[key][bi[key][0]++]=(value);
			}
		}

		for (int i = 0; i < ai.length; i++) {
			ai[i][0]=1;// = new ArrayList<Integer>(a.length>>>8);
		}
		// 4. Partition the alt sorted lists into gb sorted by high-order byte
		for (int i = bi.length - 1; i >= 0; i--) {

			for (int j = 1; j < bi[i][0]; j++) {
				value = bi[i][j];
				key = (value >> 24) & 0xff;
				ai[key][ai[key][0]++]=(value);
			}
		}
		int count = 0;
		for (int i = 0; i < ai.length; i++) {
			count += ai[i][0]-1;
			for (int j = 1; j<ai[i][0];j++) {
				a[--count] = ai[i][j];
			}
			count += ai[i][0]-1;
		}

		return;
	}
}
