package line.breaking;

public class Glue extends Box {
	private int y; // stretchability
	private int z; // shrinkability

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public Glue(int w, int y, int z) {
		this.setWidth(w);
		this.y = y;
		this.z = z;
		this.setCh('_');
	}

	static Glue betweenWords = new Glue(6, 3, 2);
	static Glue afterComma = new Glue(6, 4, 2);
	static Glue afterSemicolon = new Glue(6, 4, 1);
	static Glue afterPeriod = new Glue(8, 6, 1);

	static Glue getGlue(char perviousCh) {
		if (perviousCh == ',')
			return afterComma;
		else if (perviousCh == ';')
			return afterSemicolon;
		else if (perviousCh == '.')
			return afterPeriod;
		else
			return betweenWords;
	}

	@Override
	public String toString() {
		return "Glue [width=" + getWidth() + ", y=" + y + ", z=" + z + "]";
	}

}
