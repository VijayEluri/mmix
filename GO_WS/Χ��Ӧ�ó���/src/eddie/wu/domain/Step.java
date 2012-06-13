package eddie.wu.domain;

/**
 * Better way to express the step. replace of the row,column 表达一步棋的下法。<br/>
 * How to present the step given up? though it is not common in reality, but it
 * may happen in the very end of the game, current idea is to let point==null.<br/>
 * it has same data structure as BoardPoint but the meaning is different.
 * 
 * synonym: Move Chinese: 步
 * 
 * @author eddie
 * 
 */
public class Step {// implements Step{
	private Point point;
	/**
	 * start from 1;
	 */
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private byte color;

	public Step(Point point, int color) {
		this.point = point;
		this.color = (byte) color;
	}

	public Step(Point point, int color, int index) {
		this.point = point;
		this.color = (byte) color;
		this.index = index;
	}

	public byte getColor() {
		return color;
	}

	public void setColor(byte color) {
		this.color = color;
	}

	// public void setColor(int color) {
	// this.color = (byte) color;
	// }

	public Point getPoint() {
		return point;
	}

	// public void setPoint(Point point) {
	// this.point = point;
	// }

	public boolean isGiveUp() {
		return point == null;
	}

	@Override
	public String toString() {
		return "Step [point=" + point + ", color=" + color + ", index=" + index
				+ "]";
	}

	public byte getRow() {

		return point.getRow();
	}

	public byte getColumn() {
		return point.getColumn();
	}

	public boolean isBlack() {

		return color == Constant.BLACK;
	}

	public boolean isWhite() {

		return color == Constant.WHITE;
	}

	public void setTime(long l) {
		this.time = l;

	}

	private long time;

	public long getTime() {
		return time;
	}
}
