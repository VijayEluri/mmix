package eddie.wu.domain;

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
}
