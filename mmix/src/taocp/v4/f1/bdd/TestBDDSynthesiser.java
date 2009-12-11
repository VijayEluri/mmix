/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

import junit.framework.TestCase;

/**
 * <p>
 * TestBDDSynthesiser.java
 * </p>
 */
public class TestBDDSynthesiser extends TestCase {
	public void testMeld() {
		BDD f = SampleBDDBuilder.getF4();
		new BDDReducer().reduce(f);
		f.printInLevel();
		
		BDD g = SampleBDDBuilder.getG4();
		new BDDReducer().reduce(g);
		g.printInLevel();
		
		BDDSynthesiser bs = new BDDSynthesiser();
		BDD res = bs.meld(f, g);
		res.reduce();
		res.printInLevel();

	}
}
