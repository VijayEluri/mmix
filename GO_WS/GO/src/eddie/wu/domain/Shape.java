package eddie.wu.domain;

import java.util.Set;

/**
 * 描述一块棋的轮廓形状，所有的棋子都在（minX,minY）到（maxX,maxY）的矩形内。 这里的X,Y分别指行和列。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class Shape {

	private int minX;
	private int maxX;
	private int minY;
	private int maxY;

	/**
	 * normal shape, deltaX is less than or equal to deltaY. landscape vs.
	 * portrait. return true when deltaX is bigger than deltaY.
	 * 
	 */
	public boolean isLandscape() {
		int deltaX = maxX - minX;
		int deltaY = maxY - minY;
		return deltaX > deltaY;
	}

	public int getDeltaX() {
		return maxX - minX;
	}

	public int getDeltaY() {
		return maxY - minY;
	}

	public int getMinDelta() {
		int deltaX = maxX - minX;
		int deltaY = maxY - minY;
		if (deltaX > deltaY)
			return deltaY;
		else
			return deltaX;
	}

	public int getMaxDelta() {
		int deltaX = maxX - minX;
		int deltaY = maxY - minY;
		if (deltaX > deltaY)
			return deltaX;
		else
			return deltaY;
	}

	/**
	 * plain old boring method is put at the bottom of the class.
	 * 
	 * @return
	 */
	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	/**
	 * 根据一块棋中的所有点来判断棋块的形状。 <br/>
	 * 决定top left和bottom right 点。
	 * 
	 * @param allPoints
	 * @return
	 */
	public static Shape getShape(Set<Point> allPoints) {
		Point temp = allPoints.iterator().next();
		int minX = temp.getRow(), minY = temp.getColumn();
		int maxX = temp.getRow(), maxY = temp.getColumn();
		for (Point point : allPoints) {
			if (point.getRow() < minX) {
				minX = point.getRow();
			} else if (point.getRow() > maxX) {
				maxX = point.getRow();
			}
			if (point.getColumn() < minY) {
				minY = point.getColumn();
			} else if (point.getColumn() > maxY) {
				maxY = point.getColumn();
			}
		}
		Shape shape = new Shape();
		shape.setMaxX(maxX);
		shape.setMaxY(maxY);
		shape.setMinX(minX);
		shape.setMinY(minY);

		return shape;

	}

	/**
	 * 注意等号在边角上成立。
	 * 
	 * @param shape
	 * @return
	 */
	public boolean include(Shape shape) {
		return this.minX <= shape.minX && this.minY <= shape.minY
				&& this.maxX >= shape.maxX && this.maxY >= shape.maxY;
	}

	@Override
	public String toString() {
		return "Shape [minX=" + minX + ", maxX=" + maxX + ", minY=" + minY
				+ ", maxY=" + maxY +", DeltaX="+this.getDeltaX()+", deltaY="+this.getDeltaY()+ "]";
	}

	// public
}
