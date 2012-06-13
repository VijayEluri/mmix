package eddie.wu.domain.survive;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BasicGoBoard;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Group;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.analy.SurviveAnalysis;

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
	private BasicGoBoard survive = new BasicGoBoard(Constant.BOARD_SIZE);
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
		boolean live = survive.isLive(target);
		assertEquals(result, live);

	}

	/**
	 * 看起来可以满足基本需要.
	 */
	public void testGetEyeArea() {
		String fileName;
//		fileName = "doc/围棋程序数据/死活题局面/死活一月通/Page_140_diagram_21.wjm";
//		this.testGetEyeArea1(fileName);
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

		if(log.isDebugEnabled()) log.debug("fileName: " + inname1);
		state = StateAnalysis.LoadState(inname1);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(survive.getPoint(2, 16));
		assertTrue(live);

	}

	public void test2() {

		if(log.isDebugEnabled()) log.debug("fileName: " + inname2);
		state = StateAnalysis.LoadState(inname2);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(survive.getPoint(2, 16));
		assertTrue(live);

	}

	public void test3() {

		if(log.isDebugEnabled()) log.debug("fileName: " + inname3);
		state = StateAnalysis.LoadState(inname3);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(survive.getPoint(2, 17));
		assertTrue(live);

	}

	public void test4() {

		if(log.isDebugEnabled()) log.debug("fileName: " + inname4);
		state = StateAnalysis.LoadState(inname4);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(survive.getPoint(3, 17));
		assertTrue(live);

	}

	public void test5() {

		if(log.isDebugEnabled()) log.debug("fileName: " + inname5);
		state = StateAnalysis.LoadState(inname5);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(survive.getPoint(5, 15));
		assertTrue(live);

	}
}
