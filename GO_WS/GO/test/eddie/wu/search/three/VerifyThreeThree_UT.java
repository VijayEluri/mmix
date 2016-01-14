package eddie.wu.search.three;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class VerifyThreeThree_UT extends VerifyThreeThree {
	static {
//		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
//		String value = "log4j.xml";
//		System.setProperty(key, value);
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		// Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.ERROR);
		// Logger.getLogger(TestAllState3.class).setLevel(Level.WARN);
		// Logger.getLogger(ThreeThreeBoardSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(SmallBoardGlobalSearch.class).setLevel(Level.WARN);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.WARN);
		Logger.getLogger("search.state.add").setLevel(Level.DEBUG);
		Logger.getLogger("search.state.apply").setLevel(Level.INFO);
		Logger.getLogger("search.statistic").setLevel(Level.DEBUG);
		// Logger.getLogger("search.terminal.state.add").setLevel(Level.DEBUG);
	}
	public void testDemo(){
		
	}
}
