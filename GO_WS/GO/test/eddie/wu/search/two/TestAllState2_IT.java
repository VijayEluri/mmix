package eddie.wu.search.two;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.search.global.GoBoardSearch;

public class TestAllState2_IT extends TestAllState2 {
	static {
		Constant.INTERNAL_CHECK = false;
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		Logger.getLogger(TestAllState2.class).setLevel(Level.ERROR);
	}
}
