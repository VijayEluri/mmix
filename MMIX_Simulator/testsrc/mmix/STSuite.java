package mmix;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>STSuite.java
 * </p>
 * will cover all the MMO/MMS in MMIXware.
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class STSuite extends TestCase {
	public static TestSuite suite() {
		TestSuite suite = new TestSuite();
		//suite.addTestSuite(TestHelloMMO.class);
		suite.addTestSuite(TestHelloMMO.class);
		
		suite.addTestSuite(TestCopyMMO.class);
		//please note that CPMMO need read from Stdin, so you need to input something.
		//suite.addTestSuite(TestCPMMO.class);
		return suite;
	}
}
