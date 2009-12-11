/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.booleanevaluation;

import junit.framework.TestCase;

/**
 * <p>
 * TestAll.java
 * </p>
 */
public class TestAll extends TestCase {
	FindCost fc = new FindCost();
	FindNormalLength fnl = new FindNormalLength();

	public void testAll() {

	}

	public void test3() {
		int f = 77;
		int x1, x2, x3;
		int index;
		int result;
		for (x1 = 0; x1 <= 1; x1++) {
			for (x2 = 0; x2 <= 1; x2++) {
				for (x3 = 0; x3 <= 1; x3++) {
					index = (x1 << 2) + (x2 << 1) + x3;
					result = (f >>> (8 - 1 - index)) & 1;
					// System.out.println("result="+result);
					// System.out.println("index="+index);
					// System.out.println("(1 << (8 - 1 - index))="+(1 << (8 - 1
					// - index)));
					
					assertEquals(result, (~(~x1 & x2)) & ((x3) | (x1 & ~x2)));
					assertEquals(result, (~(~x1 & x2)) & ((x3) | (x1 ^ x2)));
					assertEquals(result, (~x1 & x2) ^ ((x3) | (x1 ^ x2)));
					assertEquals(result, (x1 & ~x2) | ((x3) & (~(~x1 & x2))));
					assertEquals(result, (x1 & ~x2) | ((x3) & (~(x1 ^ x2))));
					assertEquals(result, (x1 & ~x2) ^ ((x3) & (~(x1 ^ x2))));

					assertEquals(result, (x1 & x3) | ((~(x2)) & (x1 | x3)));

					assertEquals(result, (x1 & x3) | ((~(x2)) & (x1 ^ x3)));

					assertEquals(result, (x1 & x3) ^ ((~(x2)) & (x1 ^ x3)));
					assertEquals(result, (x1 | x3) & (~((x2) & (~(x1 & x3)))));

					assertEquals(result, (x1 | x3) & (~((x2) & (x1 ^ x3))));
					assertEquals(result, (x1 | x3) ^ ((x2) & (x1 ^ x3)));
					assertEquals(result, (~x2 & x3) | ((x1) & (~(x2 & ~x3))));
					assertEquals(result, (~x2 & x3) | ((x1) & (~(x2 ^ x3))));
					assertEquals(result, (~x2 & x3) ^ ((x1) & (~(x2 ^ x3))));
					assertEquals(result, (~(x2 & ~x3)) & ((x1) | (~x2 & x3)));
					assertEquals(result, (~(x2 & ~x3)) & ((x1) | (x2 ^ x3)));
					assertEquals(result, (x2 & ~x3) ^ ((x1) | (x2 ^ x3)));

					assertEquals(result, (x1) ^ ((~(x1 ^ x2)) & (x1 ^ x3)));

					assertEquals(result, (x1) ^ ((~(x1 ^ x2)) & (x2 ^ x3)));
					assertEquals(result, (x1) ^ ((x1 ^ x3) & (x2 ^ x3)));
					assertEquals(result, (x2) ^ ((x1 ^ x2) | (x1 ^ x3)));
					assertEquals(result, (x2) ^ ((x1 ^ x2) | (x2 ^ x3)));
					assertEquals(result, (x2) ^ ((x1 ^ x3) | (x2 ^ x3)));
					assertEquals(result, (x3) ^ ((x1 ^ x2) & (x1 ^ x3)));

					assertEquals(result, (x3) ^ ((x1 ^ x2) & (~(x2 ^ x3))));

					assertEquals(result, (x3) ^ ((x1 ^ x3) & (~(x2 ^ x3))));
				}

			}
		}

	}
}
