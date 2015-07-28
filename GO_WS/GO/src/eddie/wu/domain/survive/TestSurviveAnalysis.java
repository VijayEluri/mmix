package eddie.wu.domain.survive;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Group;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.GoBoardSearch;

public class TestSurviveAnalysis extends TestCase {
	private String inname = "doc/围棋程序数据/死活题局面/边上的假眼.wjm";
	private String inname0 = "doc/围棋程序数据/死活题局面/一路假眼.wjm";
	private String inname00 = "doc/围棋程序数据/死活题局面/一路真眼.wjm";
	private String inname000 = "doc/围棋程序数据/双活/基本双活.wjm";
	// C:\scm\git\git-hub\mmix\GO_WS\围棋应用程序\doc\围棋程序数据\死活题局面
	byte[][] state;

	private String inname1 = "doc/围棋程序数据/死活题局面/两真眼活棋.wjm";
	private String inname2 = "doc/围棋程序数据/死活题局面/边上三块两假眼活棋.wjm";
	private String inname3 = "doc/围棋程序数据/死活题局面/角上两块两假眼活棋.wjm";
	private String inname4 = "doc/围棋程序数据/死活题局面/两块两假眼活棋.wjm";
	private String inname5 = "doc/围棋程序数据/死活题局面/两头蛇假眼活棋.wjm";

	private static final Logger log = Logger.getLogger(SurviveAnalysis.class);
	static {
		log.setLevel(Level.DEBUG);
	}
	private GoBoard survive = new GoBoard(Constant.BOARD_SIZE);

	public void test() {

		_test0(inname, survive.getPoint(2, 16), false);

	}

	public void test0() {
		_test0(inname0, survive.getPoint(18, 2), false);
	}

	public void test00() {
		_test0(inname00, survive.getPoint(18, 2), true);
	}

	public void test000() {
		_test0(inname000, survive.getPoint(18, 1), true);
		// _test0(inname000, survive.getPoint(18, 2), true);
		// _test0(inname000, survive.getPoint(18, 3), true);
	}

	public void _test0(String fileName, Point target, boolean result) {
		state = StateAnalysis.LoadState(fileName);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isAlreadyLive_dynamic(target);
		assertEquals(result, live);

	}

	/**
	 * [_, B, B] <br/>
	 * [B, B, B] <br/>
	 * [W, W, _
	 */
	public void testFinalState() {
		String[] text = new String[3];
		text[0] = new String("[_, B, B]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[W, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);
		boolean finalState = sa.isFinalState_deadExist();
		if(log.isEnabledFor(Level.WARN)) log.warn("finalState=" + finalState);
	}

	/**
	 * [W, W, _]<br/>
	 * [W, B, B]<br/>
	 * [_, B, _]
	 */

	public void testLiveState() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, B, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(3, 2, 2);
		boolean live = sa.isAlreadyLive_dynamic(point);
		if(log.isEnabledFor(Level.WARN)) log.warn( point + " isLive= " + live);
		
		live = sa.isLiveWithoutTwoEye(point);//(point);
		if(log.isEnabledFor(Level.WARN)) log.warn( point + " isLive= " + live);
		
		boolean finalState = sa.isFinalState_deadExist();
		if(log.isEnabledFor(Level.WARN)) log.warn("finalState=" + finalState);
		//

		FinalResult finalResult = sa.finalResult_deadExist();
		if(log.isEnabledFor(Level.WARN)) log.warn(finalResult);
		int score = finalResult.getScore();
		this.assertEquals(9, score);
		if(log.isEnabledFor(Level.WARN)) log.warn("finalResult: score=" + score);

		// score = sa.finalResult_noCandidate().getScore();
		// if(log.isEnabledFor(Level.WARN)) log.warn("finalResult_noCandidate(): score=" + score);
	}



	/**
	 * 看起来可以满足基本需要.
	 */
	public void testGetEyeArea() {
		String fileName;
		// fileName = "doc/围棋程序数据/死活题局面/死活一月通/Page_140_diagram_21.wjm";
		// this.testGetEyeArea1(fileName);
		fileName = "doc/围棋程序数据/死活题局面/死活一月通/Page_117_diagram_16.wjm";
		this.testGetEyeArea2(fileName);
	}

	public void testGetEyeArea1(String fileName) {

		state = StateAnalysis.LoadState(fileName);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		group.addBlock(survive.getBlock(survive.getPoint(16, 18)));
		group.addBlock(survive.getBlock(survive.getPoint(17, 16)));
		// survive.generateBlockInfo();
		survive.getEyeArea2(group);
		// assertEquals(result, live);

	}

	public void testGetEyeArea2(String fileName) {

		state = StateAnalysis.LoadState(fileName);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = survive.getGroup(survive.getPoint(17, 17));

		survive.getEyeArea2(group);

	}

	public void test1() {

		if (log.isDebugEnabled())
			log.debug("fileName: " + inname1);
		state = StateAnalysis.LoadState(inname1);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isAlreadyLive_dynamic(survive.getPoint(2, 16));
		assertTrue(live);

	}

	public void test2() {

		if (log.isDebugEnabled())
			log.debug("fileName: " + inname2);
		state = StateAnalysis.LoadState(inname2);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isAlreadyLive_dynamic(survive.getPoint(2, 16));
		assertTrue(live);

	}

	public void test3() {

		if (log.isDebugEnabled())
			log.debug("fileName: " + inname3);
		state = StateAnalysis.LoadState(inname3);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isAlreadyLive_dynamic(survive.getPoint(2, 17));
		assertTrue(live);

	}

	public void test4() {

		if (log.isDebugEnabled())
			log.debug("fileName: " + inname4);
		state = StateAnalysis.LoadState(inname4);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isAlreadyLive_dynamic(survive.getPoint(3, 17));
		assertTrue(live);

	}

	public void test5() {

		if (log.isDebugEnabled())
			log.debug("fileName: " + inname5);
		state = StateAnalysis.LoadState(inname5);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isAlreadyLive_dynamic(survive.getPoint(5, 15));
		assertTrue(live);

	}
	
	
	public void testMultipleBlock(){
		String[] text = new String[6];
		text[0] = new String("[W, W, W, W, W, B]");
		text[1] = new String("[_, W, B, B, W, B]");
		text[2] = new String("[_, W, B, _, B, _]");
		text[3] = new String("[_, W, B, B, _, B]");
		text[4] = new String("[_, W, _, B, B, W]");
		text[5] = new String("[_, W, W, W, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 4);

		boolean live= ta.isAlreadyLive_dynamic(point);
		if(log.isEnabledFor(Level.WARN)) log.warn("live=" + live);
		assertTrue(live);
	}
	
	public void testMultipleBlock2(){
		String[] text = new String[6];
		text[0] = new String("[W, W, W, W, W, B]");
		text[1] = new String("[_, W, B, B, W, _]");
		text[2] = new String("[_, W, B, _, B, W]");
		text[3] = new String("[_, W, B, B, _, B]");
		text[4] = new String("[_, W, _, B, B, W]");
		text[5] = new String("[_, W, W, W, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 4);

		Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		boolean live= ta.isAlreadyLive_dynamic(point);
		if(log.isEnabledFor(Level.WARN)) log.warn("live=" + live);
		assertTrue(live);
	}

}
