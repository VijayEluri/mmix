package eddie.wu.ui;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;

/**
 * 用于UI的绘图。要显示手数。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class UIPoint {
	private Point point;
	private int color;
	private int mark;// like triangle, rectangle, etc.
	private int moveNumber;
	private boolean eaten;

	public UIPoint(Point point2) {
		this.point = point2;
	}

	public UIPoint() {
	}

	public boolean isEaten() {
		return eaten;
	}

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

	public void toggleDeadMark() {
		if (mark == Constant.MARK_DEAD) {
			mark = 0;
		} else {
			mark = Constant.MARK_DEAD;
		}
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

	public void setEaten(boolean b) {
		this.eaten = b;

	}

	public boolean isBlank() {
		return color == Constant.BLANK;
	}

	public boolean isBlack() {
		return color == Constant.BLACK;
	}

}
