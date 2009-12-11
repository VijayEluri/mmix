/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f4.tree;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>
 * TestParenthesis.java
 * </p>
 */
public class TestParenthesis extends TestCase {
	public void test4() {
		Parenthesis p = new Parenthesis();
		int n = 4;
		String[] s = p.getAllMatched(n);
		System.out.println(s.length);
		System.out.println(Arrays.toString(s));

	}

	public void test8() {
		Parenthesis p = new Parenthesis();
		int n = 8;
		String[] s = p.getAllMatched(n);
		System.out.println(s.length);

	}
}
