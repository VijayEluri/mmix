package eddie.wu.domain;

/**
 * 当谁先走已经确定时的死活结果。
 * @author wueddie-wym-wrz
 *
 */
public class Result{	
	int survive;
	Point point;
	//不存储棋子颜色，而是由外面先后手来决定。
	//int color;
	
	
	public Result(){}
	public Result(int survive, Point point){
		this.survive = survive;
		this.point = point;
	}


	public int getSurvive() {
		return survive;
	}


	public void setSurvive(int survive) {
		this.survive = survive;
	}


	public Point getPoint() {
		return point;
	}


	public void setPoint(Point point) {
		this.point = point;
	} 
	
//	public static 
}