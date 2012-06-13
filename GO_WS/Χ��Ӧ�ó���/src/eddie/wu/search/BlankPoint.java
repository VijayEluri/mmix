package eddie.wu.search;


import eddie.wu.domain.Point;

public class BlankPoint {
	private Point point;

	/**
	 * 点的直接相邻气数.临时生成,只在局部算法中用到.不需要维护.用前初始化.
	 */
	private int directBreaths = 0;

	public BlankPoint(Point point2, int breath) {
		point = point2;
		this.directBreaths = breath;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getDirectBreaths() {
		return directBreaths;
	}

	public void setDirectBreaths(int directBreaths) {
		this.directBreaths = directBreaths;
	}

}
