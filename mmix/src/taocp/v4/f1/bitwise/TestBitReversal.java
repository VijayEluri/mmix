/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;

import java.util.Random;

import junit.framework.TestCase;

/**
 * <p>
 * TestBitReversal.java
 * </p>
 */
public class TestBitReversal extends TestCase {

	private static final int ROUNDS = 10000000;

	public void test() {
		Random r = new Random();
		long x = 0;
		long y = 0;
		for (int i = 0; i < 10; i++) {
			x = r.nextLong();
			y = BitReversal.reverse(x);
			System.out.println(Long.toBinaryString(x));
			System.out.println(Long.toBinaryString(y));
			y = BitReversal.reverseOptimized(y);
			assertEquals(x, y);

		}
	}

	public void testPerformance() {
		Random r = new Random();
		long x = 0;
		long y = 0;
		long start, end;
		start = System.currentTimeMillis();		
		for (int i = 0; i < ROUNDS; i++) {
			x = r.nextLong();
			y = BitReversal.reverse(x);
		}
		end = System.currentTimeMillis();
		double a = (end -start);
		System.out.println("it takes " + a + " milliseconds for reverse");
		
		start = System.currentTimeMillis();		
		for (int i = 0; i < ROUNDS; i++) {
			x = r.nextLong();
			y = BitReversal.reverseOptimized(x);
		}
		end = System.currentTimeMillis();
		double b = (end -start);
		System.out.println("it takes " + b + " milliseconds for reverseOptimized");
		
		System.out.println("Improved " + (a-b)/a);
		
		
		
	}
}
