/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.string.match;

/**
 * <p>
 * MatchUtil.java
 * </p>
 */
public class MatchUtil {
	static NaiveStringMatch nsm = new NaiveStringMatch();
	static RadixStringMatch rsm = new RadixStringMatch();
	static AutomatonStringMatch asm = new AutomatonStringMatch();
	static KPMStringMatch ksm = new KPMStringMatch();
	public static String[] match(String text, String pattern) {
		Integer[] a = nsm.match(text, pattern);
		String[] s = new String[a.length];
		for (int i = 0; i < a.length; i++) {
			s[i] = text.substring(a[i], a[i] + pattern.length());
		}
		return s;
	}
	
	public static String[] match2(String text, String pattern) {
		Integer[] a = rsm.match(text, pattern);
		String[] s = new String[a.length];
		for (int i = 0; i < a.length; i++) {
			s[i] = text.substring(a[i], a[i] + pattern.length());
		}
		return s;
	}
	
	public static String[] match3(String text, String pattern) {
		Integer[] a = asm.match(text, pattern);
		String[] s = new String[a.length];
		
		for (int i = 0; i < a.length; i++) {
			System.out.println("a[i]= "+a[i]);
			s[i] = text.substring(a[i], a[i] + pattern.length());
		}
		return s;
	}
	
	public static String[] match4(String text, String pattern) {
		Integer[] a = ksm.match(text, pattern);
		String[] s = new String[a.length];
		for (int i = 0; i < a.length; i++) {
			s[i] = text.substring(a[i], a[i] + pattern.length());
		}
		return s;
	}

}
