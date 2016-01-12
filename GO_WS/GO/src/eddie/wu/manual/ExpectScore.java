package eddie.wu.manual;

/**
 * simplex class to represent both max and min's expect score
 * 
 * @author think
 * @immutable
 * 
 */
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

	public int getLowExp() {
		return lowExp;
	}

	@Override
	public String toString() {
		return "[" + lowExp + ", " + highExp + "]";
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
