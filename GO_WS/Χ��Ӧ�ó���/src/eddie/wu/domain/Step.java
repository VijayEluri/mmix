package eddie.wu.domain;


/**
 * better way to express the step.
 * replace of the row,column
 * 
 * how to present the step given up. though it is not common in reality,.
 * @author eddie
 *
 */
public class Step {//implements Step{
	private Point point;

	private byte color;

	public byte getColor() {
		return color;
	}

	public void setColor(byte color) {
		this.color = color;
	}
	public void setColor(int color) {
		this.color = (byte)color;
	}
	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
}
