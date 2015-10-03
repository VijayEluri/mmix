package eddie.wu.search.two;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * this will serve as a important check when refactroing.<br/>
 * now all UT/IT for 2*2 board passed. about 20s!
 * 
 * @author think
 *
 */
public class TestSuite2 {

	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTestSuite(TestAllState2_IT.class);
		testSuite.addTestSuite(TestGetCandidate2.class);
		testSuite.addTestSuite(TestListAllState2.class);
		testSuite.addTestSuite(VerifyTwoTwo_IT.class);
		testSuite.addTestSuite(VerifyAll_IT.class);
		return testSuite;
	}
}