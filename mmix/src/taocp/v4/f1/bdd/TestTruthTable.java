/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

import junit.framework.TestCase;

/**
 * <p>
 * TestTruthTable.java
 * </p>
 */
public class TestTruthTable extends TestCase {
	public void test() {
		System.out.println(TruthTable.getString(new boolean[] { false }));
		System.out.println(TruthTable.getString(new boolean[] { true }));
	}
}
