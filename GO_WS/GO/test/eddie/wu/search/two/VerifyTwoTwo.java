package eddie.wu.search.two;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.small.TwoTwoBoardSearch;

/**
 * for debugging one case
 * 
 * @author think
 *
 */
public class VerifyTwoTwo extends VerifyTwoTwo_IT {
	static {
//		Logger.getLogger(TwoTwoBoardSearch.class).setLevel(Level.WARN);
//		Logger.getLogger(GoBoardSearch.class).setLevel(Level.DEBUG);
//		Logger.getLogger("search.state.apply").setLevel(Level.DEBUG);
		Logger.getLogger("search.state.add").setLevel(Level.DEBUG);
//		Logger.getLogger("search.termial.state.add").setLevel(Level.DEBUG);
//		Logger.getLogger("search.process").setLevel(Level.DEBUG);
//		Logger.getLogger("search.statistic").setLevel(Level.DEBUG);
		Logger.getLogger("search.verify").setLevel(Level.DEBUG);
	}
	
	public void testInitState_white_turn() {
		super.testInitState_white_turn();
		GoBoardSearch.debugStateReused();
	}
	
	public void testInitState() {
		super.testInitState();
	}
}
