/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

import junit.framework.TestCase;

/**
 * <p>
 * TestBDDBuilder.java
 * </p>
 */
public class TestBDDBuilder extends TestCase {
	public void testK6() {
		BDD bdd = BDDBuilder.makeMaxIndependentSetBDD_reduced(6);
		bdd.printInLevel();
		System.out.println();

		bdd.reduce();
		bdd.printInLevel();

		bdd.initNumOfOne();
		System.out.println("numOfOne = " + bdd.root.numOfOne);
		bdd.printInLevel();

		System.out.println(TruthTable.numOfOnes(bdd.root.tt));
		bdd.printInLevel();
		assertEquals(5, TruthTable.numOfOnes(bdd.root.tt));
		assertEquals(5, bdd.root.numOfOne);

	}

	public void testK100() {
		BDD bdd = BDDBuilder.makeMaxIndependentSetBDD_reduced(100);
		// bdd.printInLevel();
		System.out.println(bdd.numOfNode);

		new BDDReducer().reduce(bdd);
		this.assertEquals(855, bdd.numOfNode);
		bdd.printInLevel();
		//		
		bdd.initNumOfOne();
		System.out.println(bdd.root.numOfOne);
		assertEquals(1630580875002L, bdd.root.numOfOne);
		// bdd.printInLevel();
		//		
		// System.out.println(TruthTable.numOfOnes(bdd.root.tt));
		// bdd.printInLevel();
		// assertEquals(5, TruthTable.numOfOnes(bdd.root.tt));
		// assertEquals(5, bdd.root.numOfOne);

	}
}
