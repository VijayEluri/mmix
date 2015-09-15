package eddie.wu.search;

/**
 * in order to store inaccurate score. <br/>
 * TODO: history dependent score cannot be compared.
 * 
 * @author think
 *
 */
public class ScopeScore {
	private int low;// = 0 - Integer.MAX_VALUE;
	private int high;// = Integer.MAX_VALUE;

	/**
	 * how many times we encounter this state during search. count as one as
	 * terminate state,
	 */
	private transient int count;
	/**
	 * how many times we encounter this state during search, but cannot apply
	 * the score. count as one after state is decided.
	 * 
	 */
	private transient int notAppliedTimes;

	/**
	 * how many times the state is refer as known result, hence avoid searing
	 * the sub-space.
	 */
	private transient int appliedTimes;

	/**
	 * terminal state has accurate score;
	 * 
	 * @param score
	 */
	public ScopeScore(int score) {
		low = score;
		high = score;
	}

	/**
	 * could be initial score.
	 * 
	 * @param low
	 * @param high
	 */
	protected ScopeScore(int low, int high) {
		this.low = low;
		this.high = high;
	}

	public static ScopeScore getInitScore(int boardSize) {
		int high = boardSize * boardSize;
		int low = 0 - high;
		return new ScopeScore(low, high);
	}

	public int getLow() {
		return low;
	}

	public int getHigh() {
		return high;
	}

	public int getAppliedTimes() {
		return appliedTimes;
	}

	public void increaseAppliedTimes() {
		appliedTimes++;
	}

	public int getNotAppliedTimes() {
		return notAppliedTimes;
	}

	public void increaseNotAppliedTimes() {
		notAppliedTimes++;
	}

	public void increaseCount() {
		count++;
	}

	public int getCount() {
		return count;
	}

	// public boolean isInit() {
	// return (high == boardSize * boardSize)
	// && (low == 0 - boardSize * boardSize);
	// }

	public boolean isExact() {
		// if (isInit())
		// return false;
		return low == high;
	}

	public int getExactScore() {
		if (this.isExact())
			return high;
		else {
			throw new RuntimeException("Not accurate Score.");
		}
	}

	/**
	 * accurate score
	 * 
	 * @param score
	 */
	public void updateAccurateScore(int score) {
		if (low <= score && score <= high) {
			low = score;
			high = score;
		} else {
			throw new RuntimeException("Score Conflict：" + score
					+ this.toString());
		}
	}

	/**
	 * 
	 * @param score
	 * @param maxWin
	 */
	public void updateWin(int score, boolean maxWin) {
		if (maxWin) {
			if (score > high) {
				throw new RuntimeException("Conflict: score [" + score + "]"
						+ " > high [" + high + "]" + this.toString());
			}
			if (score > low) {
				low = score;
			}
		} else {
			if (score < low) {
				throw new RuntimeException("Conflict: score [" + score + "]"
						+ " < low[" + low + "]" + this.toString());
			}
			if (score < high) {
				high = score;
			}
		}
	}

	/**
	 * @param max
	 *            who lose? max or min
	 */
	public void updateLose(int score, boolean max) {
		if (max) {
			if (score < low) {
				throw new RuntimeException("Conflict: score [" + score
						+ "] < low [" + low + "]" + this.toString());
			}
			if (score < high) {
				high = score; // reduce hat
			}
		} else {
			if (score > high) {
				throw new RuntimeException("Conflict: score " + "(" + score
						+ ")" + " > high " + "(" + high + ")" + this.toString());
			}
			if (score > low) {
				low = score;
			}
		}
	}

	@Override
	public String toString() {
		if (this.isExact()) {
			return "[score=" + low + ", count=" + count + "]";
		}
		return "ScopeScore [low=" + low + ", high=" + high + ", count=" + count
				+ "]";
	}

	public ScopeScore mirror() {
		int low = 0 - this.high;
		int high = 0 - this.low;
		return new ScopeScore(low, high);
	}

	/**
	 * 
	 * @param mirrorScore
	 *            Not modified
	 */
	public void merge(ScopeScore mirrorScore) {
		if (low < mirrorScore.low) {
			low = mirrorScore.low;
		}
		if (high > mirrorScore.high) {
			high = mirrorScore.high;
		}

		if (low > high) {
			throw new RuntimeException("low = " + low + ", high = " + high);
		}

	}

}
