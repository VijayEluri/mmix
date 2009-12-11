/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.booleanevaluation;

import junit.framework.TestCase;

/**
 * <p>
 * TestAlgoL.java
 * </p>
 */
public class TestFindNormalLength extends TestCase {
	public void test() {
		int count[]=new FindNormalLength().findNormalLength();
		 
		int count2[] = new int[] { 10, 60, 456, 2474, 10624, 24184, 24640, 3088 };
		for (int i = 0; i < count.length; i++) {
			assertEquals(count2[i], 2 * count[i]);
		}

	}
}
