/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;

import java.util.Random;

import junit.framework.TestCase;

/**
 * <p>TestBitSwapping.java
 * </p>
 */
public class TestBitSwapping extends TestCase {
	private static int ROUNDS= 10000;
	
	public void test(){
		long a = 0xf;
		System.out.println(Long.toBinaryString(a));
		System.out.println(Long.toBinaryString(BitSwapping.swapBit_Stupid(a, 0, 6)));
		System.out.println(Long.toBinaryString(BitSwapping.swapBit(a, 0, 6)));
		System.out.println(Long.toBinaryString(BitSwapping.swapBit1(a, 0, 6)));
		
		a = 0x1;
		System.out.println(Long.toBinaryString(a));
		System.out.println(Long.toBinaryString(BitSwapping.swapBit_Stupid(a, 1, 0)));
		System.out.println(Long.toBinaryString(BitSwapping.swapBit(a, 1, 1)));
		System.out.println(Long.toBinaryString(BitSwapping.swapBit1(a, 1, 0)));
	}
	
	public void testPerformance() {
		Random r = new Random();
		long x = 0;
		long y = 0;
		long start, end;
		start = System.currentTimeMillis();		
		
		for (int i = 0; i < ROUNDS; i++) {
			x = r.nextLong();
			for (int j = 0; j < 64; j++) {
				for (int k = 0; k < 64; k++) {
					BitSwapping.swapBit_Stupid(x, j, k);
				}
			}			
		}
		end = System.currentTimeMillis();
		double a = (end -start);
		System.out.println("it takes " + a + " milliseconds for swapBit_Stupid");
		
		start = System.currentTimeMillis();		
		for (int i = 0; i < ROUNDS; i++) {
			x = r.nextLong();
			for (int j = 0; j < 64; j++) {
				for (int k = 0; k < 64; k++) {
					BitSwapping.swapBit1(x, j, k);
				}
			}			
		}
		end = System.currentTimeMillis();
		double b = (end -start);
		System.out.println("it takes " + b + " milliseconds for swapBit");
		
		System.out.println("Improved " + (a-b)/a);
		
		
		
	}
}
