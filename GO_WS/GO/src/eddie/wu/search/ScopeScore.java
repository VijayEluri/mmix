package eddie.wu.search;


/**
 * in order to store inaccurate score. <br/>
 * TODO: history dependent score cannot be compared.
 * 
 * @author think
 *
 */
public class ScopeScore {
	private int low = 0 - Integer.MAX_VALUE;
	private int high = Integer.MAX_VALUE;
	public int getLow() {
		return low;
	}

	public int getHigh() {
		return high;
	}

	private boolean up;
	public boolean isUp() {
		return up;
	}

//	public void setUp(boolean up) {
//		this.up = up;
//	}

	/**
	 * how many times we encounter this state during search.
	 */
	private transient int count;
	
	public void increaseCount(){
		count++;
	}

	protected ScopeScore() {
	}

	public static ScopeScore getInstance() {
		return new ScopeScore();
	}

	public static ScopeScore getInstance(int score) {
		ScopeScore scopeScore = new ScopeScore();
		scopeScore.low = score;
		scopeScore.high = score;
		// up or not doesn't matter here.
		return scopeScore;
	}

	public boolean isInit() {
		return (low == 0 - Integer.MAX_VALUE) && (high == Integer.MAX_VALUE);
	}

	public boolean isExact() {
		if (isInit())
			return false;
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
	 * @param score
	 */
	public void updateAccurateScore(int score, boolean max) {
		if (isInit()) {
			low = score;
			high = score;
			up = max;
		} else if (low <= score && score <= high) {
			low = score;
			high = score;
		} else {
			throw new RuntimeException("Score Conflict");
		}
	}

	/**
	 * 
	 * @param score
	 * @param maxWin
	 */
	public void updateWin(int score, boolean maxWin) {
		if (isInit()) {
			if (maxWin) {
				low = score;
				up = true;
			} else {
				high = score;
				up = false;
			}
		} else { // already has scores.
			if (maxWin) {
				if (score > high) {
					throw new RuntimeException("Conflict: score > high");
				}
				if (score > low) {
					low = score;
					assert up = true;
				}
			} else {
				if (score < low) {
					throw new RuntimeException("Conflict: score < low");
				}
				if (score < high) {
					high = score;
					assert up = false;
				}
			}
		}
	}

	public void updateLose(int score, boolean maxLose) {
		if (this.isInit()) {
			if (maxLose) {
				high = score; // hat value
				up = true;
			} else {
				low = score;
				up = false;
			}
		} else {// already has scores.
			if (maxLose) {
				if (score < low) {
					throw new RuntimeException("Conflict: score < low");
				}
				if (score < high) {
					high = score; // reduce hat
					assert up = true;
				}
			} else {
				if (score > high) {
					throw new RuntimeException("Conflict: score > high");
				}
				if (score > low) {
					low = score;
					assert up = false;
				}
			}
			
			
		}
	}
	
//	public boolean alreadyWin(boolean max, int expScore){
//		
//	}

}
