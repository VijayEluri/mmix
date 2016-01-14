package eddie.wu.search.three;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.small.ThreeThreeBoardSearch;

public class TestAllState3_UT extends TestAllState3 {
	static {
		Constant.INTERNAL_CHECK = false;
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j.xml";
		System.setProperty(key, value);
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		// Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.INFO);
		Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.INFO);
		Logger.getLogger(TestAllState3.class).setLevel(Level.WARN);
		Logger.getLogger("search.terminal.state.add").setLevel(Level.WARN);
		Logger.getLogger("search.state.apply").setLevel(Level.DEBUG);
		Logger.getLogger("search.state.add").setLevel(Level.DEBUG);
		Logger.getLogger("search.statistic").setLevel(Level.DEBUG);
		// Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.WARN);
	}
	

	public void testDemo() {
		//Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		 testState_V11();
	}
}
