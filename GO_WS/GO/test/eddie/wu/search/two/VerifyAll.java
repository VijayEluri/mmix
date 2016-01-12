package eddie.wu.search.two;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.small.TwoTwoBoardSearch;

/**
 * 11s to verify all states.
 * 
 * @author think
 *
 */
public class VerifyAll extends VerifyAll_IT {
	static {
		Constant.INTERNAL_CHECK = false;
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		Logger.getLogger(TestAllState2.class).setLevel(Level.INFO);
		Logger.getLogger("search.statistic").setLevel(Level.INFO);
		Logger.getLogger("search.verify").setLevel(Level.DEBUG);
	}

	public void testState1011() {
		super.testState2021();
	}
}
