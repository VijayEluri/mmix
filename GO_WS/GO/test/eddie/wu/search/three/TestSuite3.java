package eddie.wu.search.three;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestSuite3 extends TestSuite {
	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTestSuite(TestAllState3.class);
		testSuite.addTestSuite(TestAllState3Colive.class);
		testSuite.addTestSuite(TestAllState3Old.class);
		testSuite.addTestSuite(TestCandidate3.class);
		testSuite.addTestSuite(TestListAllState3.class);
		testSuite.addTestSuite(VerifyThreeThree.class);
		return testSuite;
	}
}
