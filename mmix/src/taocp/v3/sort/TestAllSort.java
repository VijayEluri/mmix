/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import taocp.v3.sort.heap.TestHeapSort;
import taocp.v3.sort.insert.TestBinaryInsertSort;
import taocp.v3.sort.insert.TestInsertSort;
import taocp.v3.sort.merge.TestMergeSort;

/**
 * <p>TestAllSort.java
 * </p>
 */
public class TestAllSort extends TestCase {
	public static TestSuite suite() {
		TestSuite suite = new TestSuite();
		
		suite.addTestSuite(TestInsertSort.class);
		suite.addTestSuite(TestBinaryInsertSort.class);
		suite.addTestSuite(TestMergeSort.class);
		suite.addTestSuite(TestHeapSort.class);
		
		suite.addTestSuite(TestSequence.class);
		return suite;
	}
}
