package mmix;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>ITSuite.java
 * </p>
 * will cover the non-complete MMIX program either form the book or modified
 * based on the other program.						
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class ITSuite extends TestCase {
	public static TestSuite suite() {
		TestSuite suite = new TestSuite();
		//suite.addTestSuite(TestHelloMMO.class);
		suite.addTestSuite(TestProgramH.class);
		suite.addTestSuite(TestProgramM.class);
		suite.addTestSuite(TestTestMMO.class);
		
		return suite;
	}
}
