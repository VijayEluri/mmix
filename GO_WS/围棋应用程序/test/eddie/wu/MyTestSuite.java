/*
 * Created on 2005-4-23
 *


 */
package eddie.wu;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.arrayblock.TestArrayGoBoardExternally;
import eddie.wu.domain.GoBoard;
import eddie.wu.linkedblock.TestBlock;
import eddie.wu.linkedblock.TestBoardPoint;
import eddie.wu.linkedblock.TestBoardColorState;
import eddie.wu.linkedblock.TestColorUtil;
import eddie.wu.linkedblock.TestGoBoardInternally;
import eddie.wu.linkedblock.TestLoadGoManual;
import eddie.wu.linkedblock.TestPoint;
import eddie.wu.other.TestLog4j;

/**
 * @author eddie only the class in linkedblock package are well tested. TODO To
 *         change the template for this generated type comment go to
 */
public class MyTestSuite {
	public MyTestSuite() {

	}

	public MyTestSuite(String name) {
		// super(name);
	}

	/**
	 * note the method signature!
	 * 
	 * @return
	 */
	public static Test suite() {
		TestSuite testSuite = new TestSuite(TestBlock.class);
		testSuite.addTestSuite(TestBoardPoint.class);
		testSuite.addTestSuite(TestBoardColorState.class);
		testSuite.addTestSuite(TestColorUtil.class);

		testSuite.addTestSuite(TestLoadGoManual.class);
		testSuite.addTestSuite(TestLog4j.class);
		testSuite.addTestSuite(TestPoint.class);

		// only test a simple method because of the complexity/time consuming.
		// testSuite.addTestSuite(TestGoBoardExternally.class);
		// testSuite.addTestSuite(TestGoBoard256.class);
		Logger log = Logger.getLogger(GoBoard.class);
		log.setLevel(Level.ERROR);
		Logger logger1 = Logger.getLogger(TestArrayGoBoardExternally.class);
		logger1.setLevel(Level.ERROR);
		testSuite.addTest(new TestArrayGoBoardExternally(
				"testForwardNextStepSingleGoManual"));

		Logger logger2 = Logger.getLogger(TestGoBoardInternally.class);
		logger2.setLevel(Level.ERROR);
		testSuite.addTest(new TestGoBoardInternally(
				"testForwardNextStepSingleGoManualInternally"));
		return testSuite;
	}
	
	
		
	
}
