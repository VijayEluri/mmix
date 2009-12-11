/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

/**
 * <p>
 * SampleBDDBuilder.java
 * </p>
 */
public class SampleBDDBuilder {
	public static BDD getF4() {
		boolean[] table = new boolean[16];

		for (int i = 0; i < 16; i++) {
			boolean[] a = BooleanUtil.getBooleanInput(i, 4);
			table[i] = (a[0] | a[1]) & (a[2] | a[3]);
		}
		BDD bdd = new BDD(table, 4);
		//System.out.println("before reducing");
		//bdd.printInLevel();
		
		return bdd;
	}

	public static BDD getG4() {
		boolean[] table = new boolean[16];
		for (int i = 0; i < 16; i++) {
			boolean[] a = BooleanUtil.getBooleanInput(i, 4);
			table[i] = (a[0] ^ a[1]) | (a[2] ^ a[3]);
		}
		BDD bdd = new BDD(table, 4);
		//bdd.printInLevel();
		return bdd;
	}
}
