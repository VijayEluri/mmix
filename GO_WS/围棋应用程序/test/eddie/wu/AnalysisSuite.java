package eddie.wu;

import junit.framework.Test;
import junit.framework.TestSuite;
import eddie.wu.domain.TestActionAnalysis;
import eddie.wu.domain.TestShuangHuo;
import eddie.wu.domain.TestStateAnalysis;
import eddie.wu.domain.TestStateAnalyze;
import eddie.wu.domain.TestTerritoryAnalysis;

public class AnalysisSuite {
	public static Test suite() {
		TestSuite testSuite = new TestSuite(TestActionAnalysis.class);
		testSuite.addTestSuite(TestShuangHuo.class);
		testSuite.addTestSuite(TestStateAnalysis.class);
		testSuite.addTestSuite(TestStateAnalyze.class);
		testSuite.addTestSuite(TestTerritoryAnalysis.class);
		return testSuite;
	}
}
