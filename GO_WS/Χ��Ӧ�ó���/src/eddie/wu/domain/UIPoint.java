package eddie.wu.domain;
/**
 * ����UI�Ļ�ͼ��Ҫ��ʾ������
 * @author wueddie-wym-wrz
 *
 */
public class UIPoint {
	private Point point;
	private int color;
	private int mark;//like triangle, rectangle, etc.
	private int moveNumber;
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public int getMoveNumber() {
		return moveNumber;
	}
	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}       
	
}
