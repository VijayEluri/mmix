/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;

import junit.framework.TestCase;

/**
 * <p>
 * TestCountBit.java
 * </p>
 */
public class TestCountBit extends TestCase {
	public void test12() {
		boolean notEqual = false;
		long i = 0;
		long ii = 0;
		for (i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
			if(i<0){
				ii = i + (Integer.MAX_VALUE + 1L)*2;
			}else{
				ii = i;
			}
			if (CountBit.countBit_k1(ii) != CountBit.countBit_k2(ii)) {
				notEqual = true;
				break;
			}
			if (i % 1000000 == 0) {
				System.out.println(i);
			}
		}
		if (notEqual) {
			System.out.println("failure when i=" + i + "; "
					+ CountBit.countBit_k1(ii) + "; " + CountBit.countBit_k2(ii));
		} else {
			System.out.println("success");
		}
	}

	public void test13() {
		boolean notEqual = false;
		long i = 0;
		long ii = 0;
		for (i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
			if(i<0){
				ii = i + (Integer.MAX_VALUE + 1L)*2;
			}else{
				ii = i;
			}
			if (CountBit.countBit_k1(ii) != CountBit.countBit_k3(ii)) {
				notEqual = true;
				break;
			}
			if (i % 1000000 == 0) {
				System.out.println(i);
			}
		}
		if (notEqual) {
			System.out.println("failure when i=" + i + "; "
					+ CountBit.countBit_k1(i) + "; " + CountBit.countBit_k3(i));
		} else {
			System.out.println("success");
		}
	}
	
	public void test13_int() {
		boolean notEqual = false;
		long i = 0;
		for (i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
			if (CountBit.countBit_k1((int)i) != CountBit.countBit_k3((int)i)) {
				notEqual = true;
				break;
			}
			if (i % 1000000 == 0) {
				System.out.println(i);
			}
		}
		if (notEqual) {
			System.out.println("failure when i=" + i + "; "
					+ CountBit.countBit_k1(i) + "; " + CountBit.countBit_k3(i));
		} else {
			System.out.println("success");
		}
	}

}
