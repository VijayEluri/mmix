/* 
 * Copyright 2008 1fb.net Financial Services.
 *  
 * This document may not be reproduced, distributed or used 
 * in any manner whatsoever without the expressed written 
 * permission of 1st Financial Bank USA.
 */
package util;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>TestMath.java
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
public class TestMath extends TestCase {
	public void test(){
		System.out.println("(int)Math.pow(2, 4)="+(int)Math.pow(2, 4));
		assertEquals(16, (int)Math.pow(2, 4));
	}
	
	public void test2(){
		System.out.println(MathUtil.getOddParity(Long.MAX_VALUE));
		System.out.println(MathUtil.getOddParity(Long.MIN_VALUE));
	}
	
	public void testBitCompositionAndDecomposition(){
		MathUtil m = new MathUtil();
		int a = 127;
		int b = 129;
		int bits = 8;
		int aa[] = m.decomposeToBit(a, bits);
		int bb[] = m.decomposeToBit(b, bits);
		System.out.println(Arrays.toString(aa));
		System.out.println(Arrays.toString(bb));
		a = m.composeBitToInt(aa, bits);
		b = m.composeBitToInt(bb, bits);
		assertEquals(127, a);
		assertEquals(129, b);
		
	}
}
