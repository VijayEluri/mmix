/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.booleanbasics;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>
 * TestSelfDual.java
 * </p>
 */
public class TestBooleanAttrUtil extends TestCase {
	public void testSelfDual() {
		for (int i = 0; i < 6; i++) {
			System.out.print("i=" + i + ": ");
			int lengthOfT = 1 << i;
			boolean[] table = new boolean[lengthOfT];
			long count = 0;
			boolean dual = false;
			long countOfFunction = (1L << (1 << i));
			for (long j = 0; j < countOfFunction; j++) {
				// j is the truth table for one fuction of i variable.
				for (int k = 0; k < lengthOfT; k++) {
					table[lengthOfT - 1 - k] = (((j >>> k) & 1) > 0) ? true : false;
				}
				
				dual = BooleanAttrUtil.isSelfDual(table, i);
				if(dual){
					//System.out.println(Arrays.toString(table));
					count ++;
				}
			}
			System.out.println(count + " of " + countOfFunction + " is self-dual" );
		}
	}
	
	public void testCanalizing() {
		for (int i = 0; i < 5; i++) {
			System.out.print("i=" + i + ": ");
			int lengthOfT = 1 << i;
			boolean[] table = new boolean[lengthOfT];
			long count = 0;
			boolean can = false;
			long countOfFunction = (1L << (1 << i));
			for (long j = 0; j < countOfFunction; j++) {
				// j is the truth table for one fuction of i variable.
				for (int k = 0; k < lengthOfT; k++) {
					table[lengthOfT - 1 - k] = (((j >>> k) & 1) > 0) ? true : false;
				}
				
				can = BooleanAttrUtil.isCanalizing(
						table, i);
				if(can){
//					System.out.println();
//					System.out.print(Arrays.toString(table));
					count ++;
				}
			}
			//System.out.println();
			System.out.println(count + " of " + countOfFunction + " is canalizing" );
		}
	}
	
	public void testMonotone() {
		for (int i = 0; i < 6; i++) {
			System.out.print("i=" + i + ": ");
			int lengthOfT = 1 << i;
			boolean[] table = new boolean[lengthOfT];
			long count = 0;
			boolean monotone = false;
			long countOfFunction = (1L << (1 << i));
			for (long j = 0; j < countOfFunction; j++) {
				// j is the truth table for one fuction of i variable.
				for (int k = 0; k < lengthOfT; k++) {
					table[lengthOfT - 1 - k] = (((j >>> k) & 1) > 0) ? true : false;
				}
				
				monotone = BooleanAttrUtil.isMonotone(table, i);
				if(monotone){
					//System.out.println(Arrays.toString(table));
					count ++;
				}
			}
			System.out.println(count + " of " + countOfFunction + " is monotone" );
		}
	}
	
	public void testBothMonotoneAndSelfDual() {
		for (int i = 0; i < 6; i++) {
			System.out.print("i=" + i + ": ");
			int lengthOfT = 1 << i;
			boolean[] table = new boolean[lengthOfT];
			long count = 0;
			boolean monotone = false;
			boolean dual = false;
			long countOfFunction = (1L << (1 << i));
			for (long j = 0; j < countOfFunction; j++) {
				// j is the truth table for one fuction of i variable.
				for (int k = 0; k < lengthOfT; k++) {
					table[lengthOfT - 1 - k] = (((j >>> k) & 1) > 0) ? true : false;
				}
				
				monotone = BooleanAttrUtil.isMonotone(table, i);
				dual = BooleanAttrUtil.isSelfDual(table, i);
				if(monotone && dual){
					//System.out.println(Arrays.toString(table));
					count ++;
				}
			}
			System.out.println(count + " of " + countOfFunction + " is monotone" );
		}
	}
	
	public void testHorn() {
		for (int i = 0; i < 5; i++) {
			System.out.print("i=" + i + ": ");
			int lengthOfT = 1 << i;
			boolean[] table = new boolean[lengthOfT];
			long count = 0;
			boolean horn = false;
			long countOfFunction = (1L << (1 << i));
			for (long j = 0; j < countOfFunction; j++) {
				// j is the truth table for one fuction of i variable.
				for (int k = 0; k < lengthOfT; k++) {
					table[lengthOfT - 1 - k] = (((j >>> k) & 1) > 0) ? true : false;
				}
				
				horn = BooleanAttrUtil.isHorn(table, i);
				if(horn){
					//System.out.println(Arrays.toString(table));
					count ++;
				}
			}
			System.out.println(count + " of " + countOfFunction + " is horn function" );
		}
	}
	
	public void testSymmetry() {
		for (int i = 0; i < 5; i++) {
			System.out.print("i=" + i + ": ");
			int lengthOfT = 1 << i;
			boolean[] table = new boolean[lengthOfT];
			long count = 0;
			boolean horn = false;
			long countOfFunction = (1L << (1 << i));
			for (long j = 0; j < countOfFunction; j++) {
				// j is the truth table for one fuction of i variable.
				for (int k = 0; k < lengthOfT; k++) {
					table[lengthOfT - 1 - k] = (((j >>> k) & 1) > 0) ? true : false;
				}
				
				horn = BooleanAttrUtil.isSymmetric(table, i);
				if(horn){
					//System.out.println(Arrays.toString(table));
					count ++;
				}
			}
			System.out.println(count + " of " + countOfFunction + " is Symmetric" );
		}
	}
	
	public void testSymmetry2() {
		for (int i = 3; i < 5; i++) {
			System.out.print("i=" + i + ": ");
			int lengthOfT = 1 << i;
			boolean[] table = new boolean[lengthOfT];
			long count = 0;
			boolean horn = false;
			long countOfFunction = (1L << (1 << i));
			for (long j = 0; j < countOfFunction; j++) {
				// j is the truth table for one fuction of i variable.
				for (int k = 0; k < lengthOfT; k++) {
					table[lengthOfT - 1 - k] = (((j >>> k) & 1) > 0) ? true : false;
				}
				
				horn = BooleanAttrUtil.isSymmetric2(table, i);
				if(horn){
					//System.out.println(Arrays.toString(table));
					count ++;
				}
			}
			System.out.println(count + " of " + countOfFunction + " is Symmetric2" );
		}
	}
	/**
	 * we know the result from theory. which can check the result.
	 */
	public void testPureThreshold() {
		for (int i = 0; i < 5; i++) {
			System.out.print("i=" + i + ": ");
			int lengthOfT = 1 << i;
			boolean[] table = new boolean[lengthOfT];
			long count = 0;
			boolean horn = false;
			long countOfFunction = (1L << (1 << i));
			for (long j = 0; j < countOfFunction; j++) {
				// j is the truth table for one fuction of i variable.
				for (int k = 0; k < lengthOfT; k++) {
					table[lengthOfT - 1 - k] = (((j >>> k) & 1) > 0) ? true : false;
				}
				
				horn = BooleanAttrUtil.isPureThreshold(table, i);
				if(horn){
					//System.out.println(Arrays.toString(table));
					count ++;
				}
			}
			System.out.println(count + " of " + countOfFunction + " is Pure Threshold" );
		}
	}
}
