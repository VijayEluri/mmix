package eddie.wu.search;

import eddie.wu.domain.BoardColorState;

/**
 * the score for history dependent state, mostly happened in 2*2 board.
 * simplify the design by assuming only one history sate reachable.
 */

public class HistoryDepScore extends ScopeScore {

	/**
	 * current score depends on historical state.
	 */
	BoardColorState state;

	private HistoryDepScore(BoardColorState state) {
		this.state = state;
	}

	public static HistoryDepScore getInstance(BoardColorState state) {
		return new HistoryDepScore(state);
	}

	public BoardColorState getState() {
		return state;
	}
	
	

	// public static ScopeScore getInstance(int score, BoardColorState state) {
	// HistoryDepScore scopeScore = new HistoryDepScore(state);
	// scopeScore.low = score;
	// scopeScore.high = score;
	// // up or not doesn't matter here.
	// return scopeScore;
	// }
}
