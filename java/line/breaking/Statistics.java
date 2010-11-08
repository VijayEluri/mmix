package line.breaking;

public class Statistics {
	private double rMin;
	private double rMax;
	private int hyphen;

	public double getrMin() {
		return rMin;
	}

	public void setrMin(double rMin) {
		this.rMin = rMin;
	}

	public double getrMax() {
		return rMax;
	}

	public void setrMax(double rMax) {
		this.rMax = rMax;
	}

	public int getHyphen() {
		return hyphen;
	}

	public void setHyphen(int hyphen) {
		this.hyphen = hyphen;
	}

	@Override
	public String toString() {
		return "Statistics [hyphen=" + hyphen + ", rMax=" + rMax + ", rMin="
				+ rMin + "]";
	}

}
