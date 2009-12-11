/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.insert;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import taocp.v3.TAOCPSample;
import taocp.v3.sort.IntArrayUtil;

/**
 * <p>TestBinaryInsertSort.java
 * </p>
 */
public class TestBinaryInsertSort extends TestCase{
	private static Logger log = Logger.getLogger(TestBinaryInsertSort.class);
	int[] inputs = { 4, 5, 2, 6, 1, 7 };
	
	
	public void testBinaryInsertSort() {
		log.setLevel(Level.DEBUG);
		IntArrayUtil.print(inputs, log);		
		BinaryInsertSort sort=new BinaryInsertSort();
		sort.sort(inputs);
		IntArrayUtil.print(inputs, log);
		int[] expect=new int[]{1,2,4,5,6,7};
		assertTrue(Arrays.equals(expect, inputs));
		
		log.debug(sort.getMetrics());
		
		sort.resetMetrics();
		inputs = TAOCPSample.getSample_ZeroIndexed();
		IntArrayUtil.print(inputs, log);
		sort.sort(inputs);
		IntArrayUtil.print(inputs, log);
		log.debug(sort.getMetrics());
	}
	

}
