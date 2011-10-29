package eddie.wu.domain;

/**
 * better way to express the step. replace of the row,column
 * 表达一步棋的下法。
 * How to present the step given up? though it is not common in reality, but it
 * may happen in the very end of the game, current idea is to let point==null.
 * 
 * synonym: Move
 * Chinese: 步
 * @author eddie
 * 
 */
public class Step {// implements Step{
	private Point point;

	private byte color;

	public byte getColor() {
		return color;
	}

	public void setColor(byte color) {
		this.color = color;
	}

	public void setColor(int color) {
		this.color = (byte) color;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
}
