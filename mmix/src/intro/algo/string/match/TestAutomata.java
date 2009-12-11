/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.string.match;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>TestAutomata.java
 * </p>
 */
public class TestAutomata extends TestCase {
	String pattern="ababaca";
	public void test(){
		Automata a = new Automata();
		a.initAuto(pattern);
		for(int i =0; i< a.dd.length;i++){
			System.out.println(Arrays.toString(a.dd[i]));
		}		
	}
}
