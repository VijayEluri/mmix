package eddie.wu.domain.survive;

import java.util.List;

import eddie.wu.domain.Point;

public class Result {
	/**
	 * ��firstStep �ﵽ���score
	 */
	int score;
	List<Point> steps;
	Point firstStep;
	//���洢������ɫ�������������Ⱥ�����������
	//int color;
	
	
	public Result(){}
	public Result(int survive, Point point){
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


	public Point getPoint() {
		return firstStep;
	}


	public void setPoint(Point point) {
		this.firstStep = point;
		steps.add(point);
	} 
}
