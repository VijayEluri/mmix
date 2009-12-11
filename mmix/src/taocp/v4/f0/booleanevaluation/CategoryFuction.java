/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.booleanevaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taocp.v4.f0.booleanbasics.Monotone;
import taocp.v4.f1.bitwise.CountBit;

/**
 * <p>
 * CategoryFuction.java
 * </p>
 */
public class CategoryFuction {
	private static final int N = 4;// keep it fixed because it takes much time
	// for 5.
	// categories
	int[] cat = new int[(int) (1 << ((1 << N) - 1))];// only consider
	int classIndex = 0;
	// normal fuction
	private int c = cat.length - N - 1;// count of un-touched normal

	// function after
	Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();

	public int[] categoryFunction() {

		cat[0] = classIndex++;
		map.put(cat[0], new ArrayList<Integer>(1));
		map.get(cat[0]).add(0);

		int i = 1;
		while (i < cat.length && c < 0) {
			int min = i;
			List<Integer> list = new ArrayList<Integer>();
			
			long[] aa = Monotone.getMonotoneSequence(4,CountBit.countBit_k1(i));	
			
			
			map.put(min, list);
			i = getNextFunction(i);			
		}

		return cat;

	}

	private int getNextFunction(int i) {
		for (int j = i + 1; j < cat.length; j++) {
			if (cat[j] != 0) {
				return j;
			}
		}
		return cat.length;
	}
	
	
	
}
