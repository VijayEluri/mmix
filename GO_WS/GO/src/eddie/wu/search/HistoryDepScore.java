package eddie.wu.search;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.SymmetryResult;
import eddie.wu.search.global.SearchLevel;

/**
 * the score for history dependent state, mostly happened in 2*2 board. simplify
 * the design by assuming only one history sate reachable.
 */

public class HistoryDepScore {
	/**
	 * may has different score with different history state.
	 */
	private Map<BoardColorState, ScoreWithManual> map = new HashMap<BoardColorState, ScoreWithManual>();

	public Map<BoardColorState, ScoreWithManual> getMap() {
		return map;
	}

	/**
	 * current score depends on historical state.
	 */
	// BoardColorState state;

	// private HistoryDepScore(BoardColorState state) {
	// super(0 - state.boardSize * state.boardSize, state.boardSize
	// * state.boardSize);
	// this.state = state;
	// }

	/**
	 * 
	 * @param state
	 * @param score depends on the state (duplicated and blocked)
	 * @return
	 */
	public static HistoryDepScore getInstance(BoardColorState state,
			ScoreWithManual score) {
		HistoryDepScore depScore = new HistoryDepScore();
		depScore.map.put(state, score);
		return depScore;
	}

	// public BoardColorState getState() {
	// return state;
	// }

	// public static ScopeScore getInstance(int score, BoardColorState state) {
	// HistoryDepScore scopeScore = new HistoryDepScore(state);
	// scopeScore.low = score;
	// scopeScore.high = score;
	// // up or not doesn't matter here.
	// return scopeScore;
	// }

	public void updateScoreWithSearchLevel(SearchLevel level, SymmetryResult symmetry, boolean mirror) {
		BoardColorState key = level.getUniqueDupState().convert(symmetry);
		if (key == null)
			return; // history dependent but not reusable.
		ScoreWithManual newScore = level.getScopeScore_(key, symmetry);
		if (map.containsKey(key)) {
			ScoreWithManual score = map.get(key);
			score.merge(newScore);
		} else {
			map.put(key, newScore);
		}

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("HistoryDepScore ");
		for (Entry<BoardColorState, ScoreWithManual> entry : this.map.entrySet()) {
			sb.append(entry.getKey().getStateString());
			sb.append("\t");
			sb.append(entry.getValue().toString());
		}
		return sb.toString();
	}

	/**
	 * generally newStates has less entries unless the first few rounds.
	 * 
	 * @param oldStates
	 * @param newStates
	 */
	public static void merge(Map<BoardColorState, HistoryDepScore> oldStates,
			Map<BoardColorState, HistoryDepScore> newStates) {
		for (Entry<BoardColorState, HistoryDepScore> newEntry : newStates
				.entrySet()) {
			if (oldStates.containsKey(newEntry.getKey())) {
				oldStates.get(newEntry.getKey()).merge(newEntry.getValue());
			} else {
				oldStates.put(newEntry.getKey(), newEntry.getValue());
			}
		}
	}

	/**
	 * when the same state has different dependencies.
	 * 
	 * @param newScore
	 */
	public void merge(HistoryDepScore newScore) {
		ScopeScore.merge(map, newScore.getMap());
		// for(Entry<BoardColorState, ScopeScore> newEntry :
		// newScore.getMap().entrySet()){
		// if(map.containsKey(newEntry.getKey())){
		// map.get(newEntry.getKey()).merge(newEntry.getValue());
		// }else{
		// map.put(newEntry.getKey(), newEntry.getValue());
		// }
		// }
	}
}
