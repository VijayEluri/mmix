/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package test;

import junit.framework.TestCase;

/**
 * <p>TestScreenShot.java
 * </p>
 */
public class TestScreenShot extends TestCase {
	public void test(){
		String fileName = Constant.pathPrefix + "firstI.png";
		try {
			TakeScreenShot.captureScreen(fileName );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
