/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.count;

import java.util.Collections;
import java.util.Random;

import junit.framework.TestCase;

/**
 * <p>
 * CompareSorting.java
 * </p>
 * To sort 1000 000 mobile phone number(11digits). see which algorithm can win.
 * when N = 10000 It takes method testSpecial() 15 milli seconds to sort the
 * phone list. It takes method test() 554 milli seconds to sort the phone list.
 * 
 * when N = 100000 It takes method testSpecial() 31 milli seconds to sort the
 * phone list. It takes method test() 57733 milli seconds to sort the phone
 * list.
 * 
 * amazing
 */
public class CompareSorting extends TestCase {
	static int N = 100000;
	static Random ran = new Random(0);
	static int[] phones;

	public void prepareData() {
		phones = new int[N];
		for (int i = 0; i < N; i++) {
			phones[i] = ran.nextInt();
		}
	}

	public void prepareDataSpecial() {
		phones = new int[N];
		for (int i = 0; i < N; i++) {
			phones[i] = ran.nextInt() & (0x11111);
		}
	}

	public void test() {

		long start, end;
		prepareData();
		start = System.currentTimeMillis();
		Counting.sort(phones);
		end = System.currentTimeMillis();
		System.out.println("It takes method test() " + (end - start)
				+ " milli seconds to sort the phone list.");
	}

	public void testSpecial() {
		long start, end;
		prepareDataSpecial();
		start = System.currentTimeMillis();
		SpecialSort.sort(phones);
		end = System.currentTimeMillis();
		System.out.println("It takes method testSpecial() " + (end - start)
				+ " milli seconds to sort the phone list.");
	}
}
