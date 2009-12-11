package mmix;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * MMIXTestSuite.java
 * </p>
 * will cover all the tests, it should be run whenever I refactor some code.
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class MMIXTestSuite extends TestCase {
	public static TestSuite suite() {
		TestSuite suite = new TestSuite();
		// suite.addTestSuite(TestSubroutineInstruction.class);
		suite.addTest(UTSuite.suite());
		suite.addTest(ITSuite.suite());
		suite.addTest(STSuite.suite());
		return suite;
	}
}