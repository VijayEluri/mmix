/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.introduction;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * <p>
 * TestLatinSquare10.java
 * </p>
 */
public class TestLatinSquare extends TestCase {
	public void test() {
		int[][] M = { { 0, 2, 8, 5, 9, 4, 7, 3, 6, 1 },
				{ 1, 7, 4, 9, 3, 6, 5, 0, 2, 8 },
				{ 2, 5, 6, 4, 8, 7, 0, 1, 9, 3 },
				{ 3, 6, 9, 0, 4, 5, 8, 2, 1, 7 },
				{ 4, 8, 1, 7, 5, 3, 6, 9, 0, 2 },
				{ 5, 1, 7, 8, 0, 2, 9, 4, 3, 6 },
				{ 6, 9, 0, 2, 7, 1, 3, 8, 4, 5 },
				{ 7, 3, 5, 1, 2, 0, 4, 6, 8, 9 },
				{ 8, 0, 2, 3, 6, 9, 1, 7, 5, 4 },
				{ 9, 4, 3, 6, 1, 8, 2, 5, 7, 0 } };
		//boolean valid = LatinSquare.isOrthogonal(M,LatinSquare.L);
		//assertEquals(true, valid);
	}
	
	public void testGetTransversal(){
		List<int[]> list  = LatinSquare.findTran(LatinSquare.Random_L, 0, 10);
		System.out.println(list.size());
		for (int[] tt : list) {
			System.out.println(Arrays.toString(tt));
		}
		//Integer[] res = new Integer[]{0,8,5,9,7,3,4,2,1,6};
		int[] row = new int[]{0,1,2,3,5,6,9,8,4,7};
		assertTrue(Arrays.equals(row, list.get(0)));
	}
	/**
	 * Array is compared by reference, not value.
	 *
	 */
	public void testIntegerArray(){
		Integer[] res = new Integer[]{0,1,2,3,5,6,9,8,4,7};
		Integer[] row = new Integer[]{0,1,2,3,5,6,9,8,4,7};
		assertTrue(Arrays.equals(res, row));
		if(res.equals(row)){
			fail();
		}
	}
	public void testIntArray(){
		int[] res = new int[]{0,1,2,3,5,6,9,8,4,7};
		int[] row = new int[]{0,1,2,3,5,6,9,8,4,7};
		assertTrue(Arrays.equals(res, row));
		if(res.equals(row)){
			fail();
		}
	
		
	}
	
	public void testGetTransversals(){
		List<int[]>[] listA  = LatinSquare.findTrans(LatinSquare.Random_L,  10);
		
		BigInteger bigP = BigInteger.valueOf(1L);
		int[] count = new int[10];

		for (int i = 0; i < 10; i++) {			
			count[i] = (listA[i].size());
			bigP = bigP.multiply(BigInteger.valueOf(count[i]));
		}
		System.out.println(Arrays.toString(count));
		System.out.println(bigP);
		
		int[] res = new int[]{79, 96, 76, 87, 70, 84, 83, 75, 95, 63};
		assertTrue(Arrays.equals(res, count));
	}

//	public void test6() {
//		int[][] L = { { 0, 1, 2, 3, 4, 5 }, { 1, 5, 4, 2, 3, 0 },
//				{ 2, 4, 5, 1, 0, 3 }, { 3, 2, 1, 0, 5, 4 },
//				{ 4, 3, 0, 5, 1, 2 }, { 5, 0, 3, 4, 2, 1 } };
//		System.out.println(new LatinSquare10().findOthogonal(L, 6));
//	}
}
