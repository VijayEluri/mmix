package eddie.wu.domain.survive;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Point;
import eddie.wu.domain.survive.RelativeSurviveResult;
import eddie.wu.domain.survive.SurviveResult;
import eddie.wu.search.SurviveCalculate;
import go.StateAnalysis;
import junit.framework.TestCase;

public class TestSurviveCalculate extends TestCase {
	private String root = "doc/Χ���������/���ۻ�������/";
	private String name1 = "ֱ��.wjm";
	private String name2 = "����.wjm";
	private String name3 = "��ñ��.wjm";
	private String name4 = "�̽�����.wjm";
	private String name5 = "������.wjm";
	private String name6 = "����_������.wjm";
	private String name7 = "����_һ������.wjm";
	private String name8 = "����_��������.wjm";

	public void test() {
		byte[][] state = StateAnalysis.LoadState(root + name1);
		BoardColorState bcs = new BoardColorState(state);
		SurviveCalculate survive = new SurviveCalculate();
		SurviveResult result = survive.surviveCalculate(bcs,
				Point.getPoint(3, 16));
		assertFalse(result.isIndependent());
	}

	/**
	 * 0. ���͵�����������Ϊ�ߡ�<br/>
	 * WWWWWW<br/>
	 * WBBBBB<br/>
	 * wBB123<br/>
	 * WBB456<br/>
	 * ����û������������������ֵļ�����˵�Ƿǳ����׺Ϳ��ٵģ����Ƕ��ڼ������˵���������Ҫ ���öࡣ<br/>
	 * ���һ�Ҫ�漰��ٵ�֪ʶ������׷���3λ�ͳ�Ϊ��һ���٣����ܲ������⡣<br/>
	 * 1. ��Ϊû����������ѡ���ֻ�����������ָ��ݻ��۵ľ��飬ֻҪ�����м����㣬��ν������ͬ�������롱
	 * �ڼ�����У��ҵ��뷨��Ҫ�м������и��ߵ����ȼ�������������˵���м�����������¿����γ��������ڣ�
	 * �������ĵ�ֻ��һ�����ڣ�����û�С����Ե�ɱ��һ�����ȿ��������㡣����������ٿ��������ڴ��������������<br/>
	 * 2. �����ϸо����������Ծ����������кܶ�����ķ�����������ѧ�ϵ�����һ���������������������㡣<br/>
	 * 3. 2λ��󣬻��������ѡ�㣬��εõ����ȼ��������γɵĻ�������ͬʱ�������ȣ��ǹ������ȡ�
	 * �����ǣ���������迼�����п����ԣ����ܿ�������̫���ܡ���������ٶ�����ԭ�򣬻ᵼ�¶���ĺܶ��������Ҳ������и�
	 * ���ƣ��������ȼ��ϵ͵ľͲ��ڿ��ǡ���
	 * 4. 5λ�к��ĸ���ѡ�㣬���������Խ�һ������ʣ��������������Ϊ���롣
	 * 5. Ϊ�˿��ټ��㣬���뽫һЩ�������������������絶���塣
	 */
	public void test6() {
		byte[][] state = StateAnalysis.LoadState(root + name6);
		BoardColorState bcs = new BoardColorState(state);
		SurviveCalculate survive = new SurviveCalculate();
		SurviveResult result = survive.surviveCalculate(bcs,
				Point.getPoint(3, 17));
		assertFalse(result.isIndependent());
	}
	
	/**
	 * 0. ���͵�����������Ϊ�ߡ�<br/>
	 * WWWWWW<br/>
	 * WBBBBB<br/>
	 * W B123<br/>
	 * WBB456<br/>
	 * 1. ǰ�����ĺ�ѡ����ͬ��
	 * 2. ���Ĳ�����ɽ١���Ȼ�Է�����������ʼ������  
	 */
	public void test7() {
		byte[][] state = StateAnalysis.LoadState(root + name7);
		BoardColorState bcs = new BoardColorState(state);
		SurviveCalculate survive = new SurviveCalculate();
		SurviveResult result = survive.surviveCalculate(bcs,
				Point.getPoint(3, 16));
		assertFalse(result.isIndependent());
	}
}
