/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.order;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import junit.framework.TestCase;

/**
 * <p>
 * TestOrder.java
 * </p>
 */
public class TestOrder extends TestCase {
	static int N=1 << 20;
	Random random = new Random();
	long start, end;
	int value;
	int[] a=new int[N];

	
	public void setUp() {
		for (int i = 0; i < N; i++) {
			value = random.nextInt();
			if (value < 0) {
				a[i] = (value >>> 1);
			} else {
				a[i] = (value);
			}
		}
	}

	public void test() {
		start = System.currentTimeMillis();
		System.out.println(Order.max(a));
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to get max.");
		
		//setUp();
		start = System.currentTimeMillis();
		System.out.println(Order.min(a));
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to get min.");
		
		//setUp();
		start = System.currentTimeMillis();
		System.out.println(Arrays.toString(Order.max_min(a)));
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to get max and min at the same time");
		
		start = System.currentTimeMillis();
		System.out.println(Arrays.toString(Order.max_min_v2(a)));
		end = System.currentTimeMillis();
		System.out.println("It takes " + (end - start)
				+ " milli seconds to get max and min at the same time");
	}
}
