/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.string.match;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>
 * TestMatch.java
 * </p>
 */
public class TestMatch extends TestCase {

	String text = "ababaabbbaa";
	String pattern = "aba";

	public void test() {

		String[] t = MatchUtil.match(text, pattern);
		assertEquals(2, t.length);
		for (String temp : t) {
			assertEquals(pattern, temp);
			System.out.println(temp);

		}
	}

	public void test2() {

		String[] t = MatchUtil.match2(text, pattern);
		assertEquals(2, t.length);
		for (String temp : t) {
			assertEquals(pattern, temp);
			System.out.println(temp);

		}
	}

	public void testAutoMataMatch() {
		text = "abababacaba";
		pattern = "ababaca";
		String[] t = MatchUtil.match3(text, pattern);
		assertEquals(1, t.length);
		for (String temp : t) {
			// assertEquals(pattern, temp);
			System.out.println(temp);

		}
	}

	public void testAutoMataMatch2() {

		String[] t = MatchUtil.match3(text, pattern);
		assertEquals(2, t.length);
		for (String temp : t) {
			assertEquals(pattern, temp);
			System.out.println(temp);

		}
	}

	public void testKMPMatch() {
		text = "cdadabdaababababcaabcdedbac";
		pattern = "ababababca";
		String[] t = MatchUtil.match3(text, pattern);
		assertEquals(1, t.length);
		for (String temp : t) {
			// assertEquals(pattern, temp);
			System.out.println(temp);

		}
	}

	public void testKMPInit() {
		KPMStringMatch ksm = new KPMStringMatch();
		pattern = "ababababca";
		ksm.init(pattern);
		System.out.println(Arrays.toString(ksm.pi));

	}

	public void testKMPMatch2() {

		String[] t = MatchUtil.match3(text, pattern);
		assertEquals(2, t.length);
		for (String temp : t) {
			assertEquals(pattern, temp);
			System.out.println(temp);

		}
	}
}
