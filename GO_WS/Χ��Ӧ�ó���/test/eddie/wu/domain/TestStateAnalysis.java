package eddie.wu.domain;

import eddie.wu.linkedblock.BoardColorState;
import eddie.wu.search.SurviveCalculate;
import go.StateAnalysis;
import junit.framework.TestCase;

public class TestStateAnalysis extends TestCase {
	private String root = "doc/Χ���������/���ۻ�������/";
	private String name1 = "ֱ��.wjm";
//	private String name2 = "����.wjm";
//	private String name3 = "��ñ��.wjm";
//	private String name4 = "�̽�����.wjm";
//	private String name5 = "������.wjm";
//	private String name6 = "����_������.wjm";
//	private String name7 = "����_һ������.wjm";
//	private String name8 = "����_��������.wjm";

	public void test() {
		byte [][] state = StateAnalysis.LoadState(root+name1);
		new StateAnalysis(state);		
		
	}
}
