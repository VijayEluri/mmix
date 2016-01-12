package eddie.wu.manual;

public class ExpectScore {
	private int highExp;
	private int lowExp;

	public ExpectScore(int highExp, int lowExp) {
		this.highExp = highExp;
		this.lowExp = lowExp;
	}

	public int getHighExp() {
		return highExp;
	}

	// public void setHighExp(int highExp) {
	// this.highExp = highExp;
	// }

	public int getLowExp() {
		return lowExp;
	}

	// public void setLowExp(int lowExp) {
	// this.lowExp = lowExp;
	// }

	@Override
	public String toString() {
		return "ExpectScore [highExp=" + highExp + ", lowExp=" + lowExp + "]";
	}

	/**
	 * get low_high, "-1_0" as example.
	 * 
	 * @return
	 */
	public String toFileName() {
		return lowExp + "_" + highExp;
	}

}
