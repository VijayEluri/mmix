/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package test;

import junit.framework.TestCase;

/**
 * <p>TestException.java
 * </p>
 */
public class TestException extends TestCase {
	
	public void test(){
		
		try {
			thr();
		} catch (Exception e) {
			System.out.println("fail: " + e);
			e.printStackTrace();
		}

	}
	 
	public void thr(){
		throw new NullPointerException(); 
	}
}
