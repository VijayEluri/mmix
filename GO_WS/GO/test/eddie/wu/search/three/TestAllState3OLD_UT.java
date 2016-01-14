package eddie.wu.search.three;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.search.global.GoBoardSearch;

public class TestAllState3OLD_UT extends TestAllState3Old {
	static {
		Constant.INTERNAL_CHECK = false;
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.INFO);
//		Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
//		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.DEBUG);
	}
}
