package eddie.wu.domain.survive;

import eddie.wu.domain.Point;
import go.StateAnalysis;
import go.SurviveAnalysis;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestSurvice extends TestCase {
	private String inname = "doc/Χ���������/���������/���ϵļ���.wjm";
	// C:\scm\git\git-hub\mmix\GO_WS\Χ��Ӧ�ó���\doc\Χ���������\���������
	byte[][] state;

	private String inname1 = "doc/Χ���������/���������/�����ۻ���.wjm";
	private String inname2 = "doc/Χ���������/���������/�������������ۻ���.wjm";
	private String inname3 = "doc/Χ���������/���������/�������������ۻ���.wjm";
	private String inname4 = "doc/Χ���������/���������/���������ۻ���.wjm";
	private String inname5 = "doc/Χ���������/���������/��ͷ�߼��ۻ���.wjm";

	public void test() {
		state = StateAnalysis.LoadState(inname);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(Point.getPoint(2, 16));
		assertFalse(live);
	}

	public void test1() {

		System.out.println("fileName: " + inname1);
		state = StateAnalysis.LoadState(inname1);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(Point.getPoint(2, 16));
		assertTrue(live);

	}

	public void test2() {

		System.out.println("fileName: " + inname2);
		state = StateAnalysis.LoadState(inname2);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(Point.getPoint(2, 16));
		assertTrue(live);

	}

	public void test3() {

		System.out.println("fileName: " + inname3);
		state = StateAnalysis.LoadState(inname3);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(Point.getPoint(2, 17));
		assertTrue(live);

	}

	public void test4() {

		System.out.println("fileName: " + inname4);
		state = StateAnalysis.LoadState(inname4);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(Point.getPoint(3, 17));
		assertTrue(live);

	}

	public void test5() {

		System.out.println("fileName: " + inname5);
		state = StateAnalysis.LoadState(inname5);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		survive.generateBlockInfo();
		boolean live = survive.isLive(Point.getPoint(5, 15));
		assertTrue(live);

	}
}
