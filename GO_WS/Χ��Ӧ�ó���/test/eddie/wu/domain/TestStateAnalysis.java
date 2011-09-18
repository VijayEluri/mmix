package eddie.wu.domain;

import eddie.wu.linkedblock.BoardColorState;
import eddie.wu.search.SurviveCalculate;
import go.StateAnalysis;
import junit.framework.TestCase;

public class TestStateAnalysis extends TestCase {
	private String root = "doc/围棋程序数据/大眼基本死活/";
	private String name1 = "直三.wjm";
//	private String name2 = "方四.wjm";
//	private String name3 = "笠帽四.wjm";
//	private String name4 = "盘角曲四.wjm";
//	private String name5 = "刀把五.wjm";
//	private String name6 = "板六_无外气.wjm";
//	private String name7 = "板六_一口外气.wjm";
//	private String name8 = "板六_两口外气.wjm";

	public void test() {
		byte [][] state = StateAnalysis.LoadState(root+name1);
		new StateAnalysis(state);		
		
	}
}
