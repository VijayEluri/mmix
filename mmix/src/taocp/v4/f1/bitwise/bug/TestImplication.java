/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise.bug;

import junit.framework.TestCase;

/**
 * <p>
 * TestImplication.java
 * </p>
 */
public class TestImplication extends TestCase {
	boolean[] al = new boolean[] { false, false, true };
	boolean[] ar = new boolean[] { false, true, true };

	boolean xl, xr, yl, yr, zl, zr;

	/**
	 * 0 --> 00 * --> 01 1 --> 11 Get the truth table for original expression
	 * ((xr^yr)&~((~xl)&yr))(xl&(~yr))
	 */
	public void testOriginal() {

		for (int i = 0; i < 3; i++) {
			xl = al[i];
			xr = ar[i];
			for (int j = 0; j < 3; j++) {
				yl = al[j];
				yr = ar[j];
				zl = (xr ^ yr) & (!((!xl) & yr));
				zr = xl & (!yr);
				System.out.print((zl ? 1 : 0) + "" + (zr ? 1 : 0) + "\t");
			}
			System.out.println();
		}

	}

	/**
	 * still hard to find the correct answer manually
	 */
	public void testDefinedTruthTable() {

	}

	/**
	 * Get the truth table for new expression
	 * 
	 */
	public void atestNewZL() {
		for (int i = 0; i < 3; i++) {
			xl = al[i];
			xr = ar[i];
			for (int j = 0; j < 3; j++) {
				yl = al[j];
				yr = ar[j];
				zl = ((!(xr^yl)|(!xl)&yr));
				System.out.print((zl ? 1 : 0) + "" + "\t");
			}
			System.out.println();
		}
		System.out.println();	

	}

	public void atestNewZR() {
		for (int i = 0; i < 3; i++) {
			xl = al[i];
			xr = ar[i];
			for (int j = 0; j < 3; j++) {
				yl = al[j];
				yr = ar[j];
				zr = (!xl) | (yr);
				System.out.print((zr ? 1 : 0) + "" + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	public void testNew() {
		atestNewZL();
		atestNewZR();
		for (int i = 0; i < 3; i++) {
			xl = al[i];
			xr = ar[i];
			for (int j = 0; j < 3; j++) {
				yl = al[j];
				yr = ar[j];
				zl = ((!(xr^yl)|(!xl)&yr));
				zr = (!xl) | (yr);
				System.out.print((zl ? 1 : 0) + ""+(zr ? 1 : 0) + "" + "\t");
			}
			System.out.println();
		}
	}

	
}
