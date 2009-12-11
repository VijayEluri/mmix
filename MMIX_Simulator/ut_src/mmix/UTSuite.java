package mmix;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>UTSuite.java
 * </p>
						
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class UTSuite extends TestCase {
	public static TestSuite suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestArithmeticInstruction.class);
		suite.addTestSuite(TestBitwiseInstruction.class);
		suite.addTestSuite(TestBytewiseInstruction.class);
		suite.addTestSuite(TestLoadingInstruction.class);
		suite.addTestSuite(TestStoringInstruction.class);
		suite.addTestSuite(TestSubroutineInstruction.class);
		suite.addTestSuite(TestWydeImmediateInstruction.class);
		return suite;
	}
}
