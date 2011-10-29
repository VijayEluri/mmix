package eddie.wu.domain.survive;

import junit.framework.TestCase;
import eddie.wu.domain.BoardColorState;
import eddie.wu.linkedblock.ColorUtil;
import go.StateAnalysis;

public class TestSmallEyeKnowledge extends TestCase {
	private String root = "doc/Χ���������/���ۻ�������/";
	private String name1 = "ֱ��.wjm";
	private String name2 = "����.wjm";
	private String name3 = "��ñ��.wjm";
	private String name4 = "�̽�����.wjm";
	private String name5 = "������.wjm";
	private String name6 = "����_������.wjm";
	private String name7 = "����_һ������.wjm";
	private String name8 = "����_��������.wjm";
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
