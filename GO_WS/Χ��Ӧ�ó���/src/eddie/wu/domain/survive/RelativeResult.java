package eddie.wu.domain.survive;

import java.util.List;

import eddie.wu.domain.Delta;

/**
 * ��˭�����Ѿ�ȷ��ʱ����������
 * ��չΪͨ�õĽ�����. 
 * Relative means the result is localized, so the delta is relative to the point(minx, miny)
 * @author wueddie-wym-wrz
 *
 */
public class RelativeResult{	
	/**
	 * ��firstStep �ﵽ���score
	 */
	int score;
	List<Delta> steps;
	Delta firstStep;
	//���洢������ɫ�������������Ⱥ�����������
	//int color;
	
	
	public RelativeResult(){}
	public RelativeResult(int survive, Delta point){
		this.score = survive;
		this.firstStep = point;
		steps.add(point);
	}


	public int getSurvive() {
		return score;
	}


	public void setSurvive(int survive) {
		this.score = survive;
	}


	public Delta getPoint() {
		return firstStep;
	}


	public void setPoint(Delta point) {
		this.firstStep = point;
		steps.add(point);
	} 
	
//	public static 
}