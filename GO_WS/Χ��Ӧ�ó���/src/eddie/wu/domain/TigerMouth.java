package eddie.wu.domain;

public class TigerMouth {
	Point point;
	int color;
	Point seek;// 刺的点.

	@Override
	public String toString() {
		return "TigerMouth [point=" + point + ", color=" + color
				+ ", seek point =" + seek + "]";
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

	public Point getSeek() {
		return seek;
	}

	public void setSeek(Point seek) {
		this.seek = seek;
	}

}
