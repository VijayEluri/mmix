package eddie.wu.domain.survive;

import java.util.List;

import eddie.wu.domain.Point;

/**
 * 每一步都是针对目前的实际局面的.
 * @author Eddie
 *
 */
public class Result {
	/**
	 * 走firstStep 达到结果score
	 */
	int score;
	List<Point> steps;
	Point firstStep;
	//不存储棋子颜色，而是由外面先后手来决定。
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
