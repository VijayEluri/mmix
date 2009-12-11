/* 
 * Copyright 2008 1fb.net Financial Services.
 *  
 * This document may not be reproduced, distributed or used 
 * in any manner whatsoever without the expressed written 
 * permission of 1st Financial Bank USA.
 */
package test;

import junit.framework.TestCase;

/**
 * <p>
 * TestProfile.java
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: 1FB USA
 * </p>
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestProfile extends TestCase {
	private static final int TIMES = 10000000;

	public void test() {
		Measure m = new Measure();
		for (int i = 0; i < 10; i++) {
			tInt(m);
		}

		System.out.println("" + m.empty / 10 + " " + m.add / 10 + " " + m.sub
				/ 10 + " " + m.mul / 10 + " " + m.div / 10);
		
		for (int i = 0; i < 10; i++) {
			tFloat(m);
		}

		System.out.println("" + m.empty / 10 + " " + m.add / 10 + " " + m.sub
				/ 10 + " " + m.mul / 10 + " " + m.div / 10);

	}

	public void tInt(Measure m) {
		long start, end;
		long average = 0;
		long averageEmpty = 0;
		long count = 0;
		start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {

		}
		end = System.currentTimeMillis();
		average = (end - start);
		averageEmpty = average;
		m.empty += averageEmpty;
		System.out.println("empty loop takes " + average + " milliseconds ("
				+ "start = " + start + "; end= " + end);
		System.out.println();

		start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {
			count += i;
		}
		end = System.currentTimeMillis();
		average = (end - start) - averageEmpty;
		m.add += average;
		System.out.println("count = " + count);
		System.out.println("add takes " + average + " milliseconds("
				+ "start = " + start + "; end= " + end);
		System.out.println();

		start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {
			count -= i;
		}
		end = System.currentTimeMillis();
		average = (end - start) - averageEmpty;
		m.sub += average;
		System.out.println("count = " + count);
		System.out.println("sub takes " + average + " milliseconds("
				+ "start = " + start + "; end= " + end);
		System.out.println();

		count = 100;
		start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {
			count *= i;
		}
		end = System.currentTimeMillis();
		average = (end - start) - averageEmpty;
		m.mul += average;
		System.out.println("count = " + count);
		System.out.println("mul takes " + average + " milliseconds("
				+ "start = " + start + "; end= " + end);
		System.out.println();

		count = Long.MAX_VALUE;
		start = System.currentTimeMillis();
		for (int i = 1; i < TIMES; i++) {
			count /= i;
		}
		end = System.currentTimeMillis();
		average = (end - start) - averageEmpty;
		m.div += average;
		System.out.println("count = " + count);
		System.out.println("div takes " + average + " milliseconds("
				+ "start = " + start + "; end= " + end);
		System.out.println();
	}

	public void tFloat(Measure m) {
		long start, end;
		long average = 0;
		long averageEmpty = 0;
		long count = 0;
		start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {

		}
		end = System.currentTimeMillis();
		average = (end - start);
		averageEmpty = average;
		m.empty += averageEmpty;
		System.out.println("empty loop takes " + average + " milliseconds ("
				+ "start = " + start + "; end= " + end);
		System.out.println();

		start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {
			count += 1.1;
		}
		end = System.currentTimeMillis();
		average = (end - start) - averageEmpty;
		m.add += average;
		System.out.println("count = " + count);
		System.out.println("add takes " + average + " milliseconds("
				+ "start = " + start + "; end= " + end);
		System.out.println();

		start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {
			count -= 1.1;
		}
		end = System.currentTimeMillis();
		average = (end - start) - averageEmpty;
		m.sub += average;
		System.out.println("count = " + count);
		System.out.println("sub takes " + average + " milliseconds("
				+ "start = " + start + "; end= " + end);
		System.out.println();

		count = 100;
		start = System.currentTimeMillis();
		for (int i = 0; i < TIMES; i++) {
			count *= 1.1;
		}
		end = System.currentTimeMillis();
		average = (end - start) - averageEmpty;
		m.mul += average;
		System.out.println("count = " + count);
		System.out.println("mul takes " + average + " milliseconds("
				+ "start = " + start + "; end= " + end);
		System.out.println();

		count = Long.MAX_VALUE;
		start = System.currentTimeMillis();
		for (int i = 1; i < TIMES; i++) {
			count /= 1.1;
		}
		end = System.currentTimeMillis();
		average = (end - start) - averageEmpty;
		m.div += average;
		System.out.println("count = " + count);
		System.out.println("div takes " + average + " milliseconds("
				+ "start = " + start + "; end= " + end);
		System.out.println();
	}
}
