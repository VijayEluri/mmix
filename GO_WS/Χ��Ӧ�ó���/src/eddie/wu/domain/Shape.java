package eddie.wu.domain;

import java.util.Set;

/**
 * ����һ�����������״�����е����Ӷ��ڣ�minX,minY������maxX,maxY���ľ����ڡ� �����X,Y�ֱ�ָ�к��С�
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
	 * normal shape, deltaX is less than or equal to deltaY.
	 * landscape vs. portrait. 
	 * return true when deltaX is bigger than deltaY.
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
	 * ����һ�����е����е����ж�������״��
	 * @param allPoints
	 * @return
	 */
	public static Shape getShape(Set<Point> allPoints) {
		int minX = 20, minY = 20, maxX = 0, maxY = 0;
		for (Point point : allPoints) {
			if (point.getRow() < minX)
				minX = point.getRow();

			if (point.getRow() > maxX)
				maxX = point.getRow();

			if (point.getColumn() < minY)
				minY = point.getColumn();
			if (point.getColumn() > maxY)
				maxY = point.getColumn();
		}
		Shape shape = new Shape();
		shape.setMaxX(maxX);
		shape.setMaxY(maxY);
		shape.setMinX(minX);
		shape.setMinY(minY);

		return shape;
		
	}
}
