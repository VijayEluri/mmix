/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import taocp.v3.sort.insert.BinaryInsertSort;
import taocp.v3.sort.insert.InsertSort;
import taocp.v3.sort.merge.MergeSort;
import util.MathUtil;

/**
 * <p>
 * TestSequence.java
 * </p>
 */
public class TestSequence extends TestCase {
	private static Logger log = Logger.getLogger(TestSequence.class);
	Sort[] sorts = new Sort[] { new MergeSort(), new InsertSort(),
			new BinaryInsertSort() };

	public void test(){
		for(Sort sort: sorts){
			_testSequence(sort,8);
		}
	}
	public void _testSequence(Sort sort, int maxsize) {
		System.out.println("Sort by"+sort.getClass().getName());
		int[] res = new int[maxsize];
		for (int i = 1; i < maxsize; i++) {//i elements to sort
			log.debug("i=" + i);
			int[] inputs = MathUtil.getRandomIntArray(i);
			IntArrayUtil.print(inputs, log);
			
			int[][] permutations = new int[MathUtil.factorial(i)][i];
			MathUtil.init(permutations, inputs);
			IntArrayUtil.print(permutations, log);
			
			int max = 0;
			for (int j = 0; j < permutations.length; j++) {
	
				sort.sort(permutations[j]);
				max = sort.getMetrics().getCompares();
				if (max > res[i]) {
					res[i] = max;
				}
				sort.resetMetrics();
			}
			log.debug("max compares=" + res[i]);
			// r[i] = InsertationSort.sort(inputs).getCompares();
			// log.debug("i="+i+", compares="+r);
			// IntArrayUtil.print(inputs);
		}
		for (int i = 0; i < maxsize; i++) {
			log.debug("i=" + i + ", compares=" + res[i]);
			System.out.println("i=" + i + ", compares=" + res[i]);
		}
		// result should be like this
		// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
		// 0 0 1 3 5 7 10 13 16 19 22 26 29 33 37 41 45 49
		// derived from (i=5, compares=8)
	}
}
