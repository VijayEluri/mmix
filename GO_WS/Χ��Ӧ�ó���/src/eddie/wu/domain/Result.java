package eddie.wu.domain;

/**
 * ��˭�����Ѿ�ȷ��ʱ����������
 * @author wueddie-wym-wrz
 *
 */
public class Result{	
	int survive;
	Point point;
	//���洢������ɫ�������������Ⱥ�����������
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