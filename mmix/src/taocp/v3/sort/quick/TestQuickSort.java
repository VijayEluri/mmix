/** 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.quick;

import java.util.Arrays;

import junit.framework.TestCase;
import taocp.v3.TAOCPSample;
import util.MathUtil;

/**
 * <p>
 * TestQuickSort.java
 * </p>
 */

public class TestQuickSort extends TestCase {
	public void test() {
		int[] a = TAOCPSample.getSample_ZeroIndexed();
		int count = MathUtil.getSwapCount();
		System.out.println(Arrays.toString(a));
		new QuickSort().sort(a);
		System.out.println(Arrays.toString(a));
		System.out.println("swap total " + (MathUtil.getSwapCount() - count)
				+ " times!");
		
		a = TAOCPSample.getSample_ZeroIndexed();
		count = MathUtil.getSwapCount();
		System.out.println(Arrays.toString(a));
		new QuickSort().sort(a);
		System.out.println(Arrays.toString(a));
		System.out.println("swap total " + (MathUtil.getSwapCount() - count)
				+ " times!");
	}
}
