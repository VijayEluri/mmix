/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>
 * BDDSample.java
 * </p>
 */
public class TestBDD extends TestCase {

	public void testMedianFunction() {
		BDD bdd = BDDBuilder.getBDDForMedian(3);
		bdd.print2();
		bdd.printInLevel();

		boolean[] table = new TruthTable("00010111").getTable();
		boolean[] table2 = new boolean[8];
		for (int i = 0; i < 8; i++) {
			boolean[] a = BooleanUtil.getBooleanInput(i, 3);
			table2[i] = bdd.eval(a);
		}
		System.out.println(Arrays.toString(table2));
		assertEquals(true, Arrays.equals(table, table2));

		// second part
		bdd.initInstrIndex();
		bdd.printInstr();

		// for (int i = 0; i < 8; i++) {
		// boolean[] a = BooleanUtil.getBooleanInput(i, 3);
		// table2[i] = bdd.eval2(a);
		// }
		System.out.println(Arrays.toString(table2));
		assertEquals(true, Arrays.equals(table, table2));
	}

	public void testBDD() {
		String s = "1100100100001111";
		boolean[] table = new TruthTable(s).getTable();
		boolean[] table2 = new boolean[16];
		BDD bdd = new BDD(s, 4);
		bdd.printInLevel();
		for (int i = 0; i < 16; i++) {
			boolean[] a = BooleanUtil.getBooleanInput(i, 4);
			table2[i] = bdd.eval(a);
		}
		System.out.println(Arrays.toString(table2));
		assertEquals(true, Arrays.equals(table, table2));

		// second part
		bdd.initInstrIndex();
		bdd.printInstr();
		bdd.initNumOfOne();
		bdd.printInLevel();
		System.out.println(bdd.root.numOfOne);
	}

	public void testFindSmallestX() {
		BDD bdd = BDDBuilder.getBDDForMedian(3);
		boolean[] res = new boolean[3];

		res = bdd.findSmallestX();

		System.out.println(Arrays.toString(res));
		// assertEquals(true, Arrays.equals(table, table2));
	}

	public void testC6() {
		BDD bdd = BDDBuilder.makeIndependentSetBDD_reduced(6);
		bdd.printInLevel();
		bdd.reduce();
		bdd.printInLevel();
		bdd.initNumOfOne();
		System.out.println(bdd.root.numOfOne);
		System.out.println(TruthTable.numOfOnes(bdd.root.tt));
		bdd.printInLevel();
		assertEquals(TruthTable.numOfOnes(bdd.root.tt), bdd.root.numOfOne);

		boolean[] table2 = new boolean[64];

		System.out.println(Arrays.toString(table2));
		TruthTable tt = new TruthTable(bdd.root.tt);
		boolean[] table = tt.getTable();
		System.out.println(Arrays.toString(table));
		System.out.println(tt.numOfOnes());
		System.out.println(Arrays.toString(tt.differ(table2)));
		assertEquals(true, Arrays.equals(table, table2));

	}

	/**
	 * C_100, 855 kernels
	 */
	public void testC100() {

		BDD bdd = BDDBuilder.makeIndependentSetBDD_reduced(100);
		System.out.println(bdd.numOfNode);
		bdd.printInLevel();

		new BDDReducer().reduce(bdd);
		System.out.println(bdd.numOfNode);
		bdd.printInLevel();
		// bdd.reduce();
		// bdd.printInLevel();

		// bdd.initNumOfOne();
		// System.out.println(bdd.root.numOfOne);
		// System.out.println(TruthTable.numOfOnes(bdd.root.tt));
		// bdd.printInLevel();

	}

	public void testK6() {
		BDD bdd = BDDBuilder.makeMaxIndependentSetBDD_reduced(6);
		bdd.printInLevel();
		bdd.reduce();
		bdd.printInLevel();
		bdd.initNumOfOne();
		System.out.println(bdd.root.numOfOne);
		System.out.println(TruthTable.numOfOnes(bdd.root.tt));
		bdd.printInLevel();
		assertEquals(TruthTable.numOfOnes(bdd.root.tt), bdd.root.numOfOne);

	}

	public void testFullBDD() {
		int numOfVar = 4;
		boolean[] truthTable = new TruthTable("1100100100001111").getTable();
		BDD bdd = BDDBuilder.makeFullBDD(truthTable, numOfVar);
		bdd.printInLevel();

		new BDDReducer().reduce(bdd);
		bdd.printInLevel();
	}
}
