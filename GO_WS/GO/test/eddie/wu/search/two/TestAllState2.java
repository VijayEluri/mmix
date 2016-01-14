package eddie.wu.search.two;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.StateLoader;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.HistoryDepScore;
import eddie.wu.search.ScopeScore;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.ListAllState;
import eddie.wu.search.small.SmallBoardGlobalSearch;
import eddie.wu.search.small.ThreeThreeBoardSearch;
import eddie.wu.search.small.TwoTwoBoardSearch;

/**
 * 1 second 5/4/2013<br/>
 * It doesn't depends on the dynamic live/death identification for 2*2 board.<br/>
 * but for 3*3 board, live/death identification is already quite important in
 * performance point of view.<br/>
 * 
 * @author Eddie
 * 
 */
public class TestAllState2 extends TestAllState2_IT {

	/**
	 * 貌似子类的static块后执行。
	 */
	static {
		Constant.INTERNAL_CHECK = false;
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.INFO);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		Logger.getLogger(TestAllState2.class).setLevel(Level.INFO);
		Logger.getLogger("search.statistic").setLevel(Level.DEBUG);
		Logger.getLogger("search.verify").setLevel(Level.DEBUG);
		;

		 Logger.getLogger("search.state.apply").setLevel(Level.DEBUG);
		 Logger.getLogger("search.state.add").setLevel(Level.DEBUG);
		 Logger.getLogger("search.termial.state.add").setLevel(Level.DEBUG);
		 Logger.getLogger("search.process").setLevel(Level.DEBUG);
	}

	public void testDemo() {
		testState2();
	}

	public void testDemo2() {
		testState1A_blackFirst();
	}
	
	public void testState1A_whiteFirst(){
		super.testState1A_whiteFirst();
	}
	
	public void testState1A_blackFirst(){
		super.testState1A_blackFirst();
	}

	/**
	 * exclude them from IT　for performance reason.
	 */

	public void testState1_blackFirst_lose_4_4() {
		testState1_blackFirst_lose(2, 4, -4);// 112s
	}

	/**
	 * why it takes more time. it will suicide in case of<br/>
	 * W_<br/>
	 * _W<br/>
	 * by play at W[1,2]. this step is nonsense if you just want to get one
	 * effective move. but if you want to get all best moves, you need to verify
	 * that this move is NOT one of them.
	 */
	public void testState1_blackFirst_lose_5_5() {
		testState1_blackFirst_lose(2, 5, -5);//
	}

	public void testState_blackFirst_lose_9_8() {
		testState1_blackFirst_lose(3, 9, 8);// 5s
	}
	
	public void testState2_AA1(){
		super.testState2_AA1();
	}
}
