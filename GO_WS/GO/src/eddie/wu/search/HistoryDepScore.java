package eddie.wu.search;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import eddie.wu.domain.BoardColorState;
import eddie.wu.search.global.SearchLevel;

/**
 * the score for history dependent state, mostly happened in 2*2 board. simplify
 * the design by assuming only one history sate reachable.
 */

public class HistoryDepScore {
	/**
	 * may has different score with different history state.
	 */
	private Map<BoardColorState, ScopeScore> map = new HashMap<BoardColorState, ScopeScore>();

	public Map<BoardColorState, ScopeScore> getMap() {
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

	public static HistoryDepScore getInstance(BoardColorState state,
			ScopeScore score) {
		HistoryDepScore score2 = new HistoryDepScore();
		score2.map.put(state, score);
		return score2;
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

	public void updateScoreWithSearchLevel(SearchLevel level) {
		BoardColorState key = level.getUniqueDupState();
		if(key==null) return; //history dependent but not reusable.
		if(map.containsKey(key)){
			ScopeScore score = map.get(key);
			System.out.print("contains dependent state: "+key.getStateString());
			if (level.alreadyWin()) {
				score.updateWin(level.getTempBestScore(), level.isMax());
			} else {
				score.updateLose(level.getTempBestScore(), level.isMax());
				score.increaseCount();
			}
		}else{
			map.put(key, level.getScopeScore(key.boardSize));
		}
		
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("HistoryDepScore ");
		for(Entry<BoardColorState,ScopeScore> entry: this.map.entrySet()){
			sb.append(entry.getKey().getStateString());
			sb.append("\t");
			sb.append(entry.getValue().toString());
		}
		return sb.toString();
	}
	
	
}
