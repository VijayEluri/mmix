package eddie.wu.domain.survive;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.analy.StateAnalysis;
import junit.framework.TestCase;

public class TestSmallEyeKnowledge extends TestCase {
	private String root = "doc/围棋程序数据/大眼基本死活/";
	private String name1 = "直三.wjm";
	private String name2 = "方四.wjm";
	private String name3 = "笠帽四.wjm";
	private String name4 = "盘角曲四.wjm";
	private String name5 = "刀把五.wjm";
	private String name6 = "板六_无外气.wjm";
	private String name7 = "板六_一口外气.wjm";
	private String name8 = "板六_两口外气.wjm";
	public void test1() {
		byte[][] state = StateAnalysis.LoadState(root + name1);
		BoardColorState bcs = new BoardColorState(state);
		byte[][] matrixState = bcs.getMatrixState();
		StateAnalysis sa = new StateAnalysis(matrixState);
		
		byte[][] pattern = new byte[1][2];
		 for (int i = 0; i < pattern[0].length; i++) {
			 pattern[0][i] = ColorUtil.BLANK;
		 }
		 pattern[1][1]=ColorUtil.BLACK;
		BreathPattern bp = new BreathPattern(pattern);
	}
}
