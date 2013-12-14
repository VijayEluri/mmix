package eddie.wu.domain;

public class TigerMouth {
	Point point;
	int color;// 哪一方的虎口
	Point peek;// 敌方刺的点.

	@Override
	public String toString() {
		return "TigerMouth [point=" + point + ", color=" + color
				+ ", seek point =" + peek + "]";
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

	public Point getPeek() {
		return peek;
	}

	public void setPeek(Point peek) {
		this.peek = peek;
	}

}
