/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package util;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>
 * TestNumberSequence.java
 * </p>
 */
public class TestNumberSequence extends TestCase {
	public void testfabonacii() {
		long[] f = NumberSequence.getFabonacii();
		System.out.println(Arrays.toString(f));
		// 7540113804746346429
		System.out.println("max n=" + (f.length - 1) + "; it is \n"
				+ f[f.length - 1]);
		System.out.println(Long.MAX_VALUE);
		// 9223372036854775807
		for(int i=0;i<10;i++){
			System.out.print(i+"\t");
		}
		System.out.println();
		for(int i=0;i<10;i++){
			System.out.print(f[i]+"\t");
		}
		System.out.println();
	}
	
	public void testfabonacii2() {
		Long[] f = NumberSequence.getFabonacii2();
		System.out.println(java.util.Arrays.toString(f));
		// 7540113804746346429
		System.out.println("max n=" + (f.length - 1) + "; it is \n"
				+ f[f.length - 1]);
		System.out.println(Long.MAX_VALUE);
		// 9223372036854775807
	}

	public void testFactorial() {
		Long[] f = NumberSequence.getFactorial();
		System.out.println(java.util.Arrays.toString(f));
		System.out.println("max n=" + (f.length - 1) + "; it is \n"
				+ f[f.length - 1]);
		System.out.println(Long.MAX_VALUE);

	}

}
