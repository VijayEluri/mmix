package eddie.wu.search.global;

import java.util.List;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;

/**
 * 将2*2的小棋盘的特殊处理应用于3*3的小棋盘。<br/>
 * 
 * 
 * @author Eddie Wu
 * 
 */
public class ThreeThreeBoardSearch extends SmallBoardGlobalSearch {

	public ThreeThreeBoardSearch(byte[][] boards, int whoseTurn) {
		super(boards, whoseTurn);
		initKnownState();
	}

	public ThreeThreeBoardSearch(byte[][] boards, int whoseTurn,
			int highestScore, int lowestScore) {
		super(boards, whoseTurn, highestScore, lowestScore);
		initKnownState();
	}

	/**
	 * the state is decided because of dual give up.
	 */
	// @Override
	// public void stateDecided(BoardColorState boardColorState, int score) {
	//
	// }

	private void initKnownState() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		results.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		results.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 9);

		text[0] = new String("[_, B, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		results.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		results.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 3);
		text = new String[3];
		text[0] = new String("[B, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");

		state = StateLoader.LoadStateFromText(text);
		results.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		results.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), -9);

		text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		results.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		results.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 3);

		text = new String[3];
		text[0] = new String("[B, B, B]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		results.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		results.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), -9);
		
		text = new String[3];		
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, B, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		results.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		results.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 9);
		// for (BoardColorState bcs : new ListAllState().getFinalState(3)) {
		//
		// }
	}

	/**
	 * three three board optimization
	 */

	@Override
	protected TerminalState getTerminalState() {
		boolean knownState = false;
		TerminalState ts = new TerminalState();
		if (this.isDoubleGiveup()) {
			ts.setTerminalState(true);
			ts.setFinalResult(goBoard
					.finalResult(TerritoryAnalysis.DEAD_CLEANED_UP));
		} else if (results.containsKey(this.getGoBoard().getBoardColorState())) {
			knownState = true;
			ts.setTerminalState(true);
			Integer score = results.get(this.getGoBoard().getBoardColorState());
			ts.setScore(score);
		} else if (goBoard.isFinalState_deadCleanedUp()) {
			ts.setTerminalState(true);
			ts.setFinalResult(goBoard.finalResult_deadCleanedUp());
		} else if (goBoard.isFinalState_deadExist()) {
			if (goBoard.boardSize < 5) {
				ts.setTerminalState(false);
			} else {
				ts.setTerminalState(true);
				ts.setFinalResult(goBoard.finalResult_deadExist());
			}
		} else {
			ts.setTerminalState(false);
		}

		if (this.isDoubleGiveup() == false && ts.isTerminalState() == true
				&& knownState == false) {
			BoardColorState boardColorStateN = this.getGoBoard()
					.getBoardColorState();
			int scoreTerminator = ts.getScore();
			this.stateFinalizeed(boardColorStateN, scoreTerminator);
		}
		return ts;
	}

	/**
	 * 同型禁现导致状态不足以决定结果,还要考虑到达该状态的历史过程.<br/>
	 * 对称的不同候选点可能一个是同型再现,而另外一个不是.这就有了差异.
	 */
	@Override
	protected List<Candidate> getCandidate(int color) {
		int expectedScore;
		if (boardSize <= 3) {
			if (color == Constant.BLACK) {
				expectedScore = this.highestScore;
			} else {
				expectedScore = this.lowestScore;
			}
		} else {
			if (color == Constant.BLACK) {
				expectedScore = boardSize * boardSize;
			} else {
				expectedScore = 0 - boardSize * boardSize;
			}
		}
		// not necessary
		// if(this.boardSize>=4){
		// return goBoard.getCandidate(color, true, expectedScore);
		// }

		if (goBoard.getInitSymmetryResult().getNumberOfSymmetry() == 0) {
			return goBoard.getCandidate(color, false, expectedScore);
		} else if (goBoard.getStepHistory().getAllSteps().isEmpty()) {
			// first move
			return goBoard.getCandidate(color, true, expectedScore);
		} else if (goBoard.getStepHistory().noRealStep()) {
			// first real move
			return goBoard.getCandidate(color, true, expectedScore);
		} else if (goBoard.getLastStep().getSymmetry().getNumberOfSymmetry() != 0) {
			return goBoard.getCandidate(color, true, expectedScore);
		} else {
			return goBoard.getCandidate(color, false, expectedScore);
		}

		// if (goBoard.getStepHistory().getAllSteps().isEmpty()) {
		// return goBoard.getCandidate(color, true);
		// } else if (goBoard.getStepHistory().noRealStep()) {
		// return goBoard.getCandidate(color, true);
		// } else {
		// return goBoard.getCandidate(color, false);
		// }
	}

	public static int getAccurateScore_blackTurn(BoardColorState state) {
		int high = 1;
		int low = 0;
		int score = 0;
		int dir = 0;// direction to check further
		do {
			GoBoardSearch goS = new ThreeThreeBoardSearch(
					state.getMatrixState(), state.getWhoseTurn(), high, low);
			score = goS.globalSearch();
			state.setVariant(goS.getSearchProcess().size());
			if (score >= 4) {
				return score;
			}
			if (score > high) {
				high = score + 1;
				high = score;
				if (dir == 0)
					dir = 1;
				else if (dir == -1)
					return score;
			} else if (score == high) {
				high++;
				low++;
				if (dir == 0)
					dir = 1;
				else if (dir == -1)
					return score;
				// else break;
			} else {
				high--;
				low--;
				if (dir == 0)
					dir = -1;
				else if (dir == 1)
					return score;
			}
		} while (high <= 4 && high > -4);
		return score;
	}

	public static int getAccurateScore_whiteTurn(BoardColorState state) {
		int high = 0;
		int low = -1;
		int score = 0;
		int dir = 0;
		do {
			GoBoardSearch goS = new ThreeThreeBoardSearch(
					state.getMatrixState(), state.getWhoseTurn(), high, low);
			score = goS.globalSearch();
			state.setVariant(goS.getSearchProcess().size());
			if (score <= -4) {
				return score;
			}
			if (score < low) {
				low = score - 1;
				high = score;
				if (dir == 0)
					dir = -1;
				else if (dir == 1)
					return score;
			} else if (score == low) {
				high--;
				low--;
				if (dir == 0)
					dir = -1;
				else if (dir == 1)
					return score;
			} else {
				high++;
				low++;
				if (dir == 0)
					dir = 1;
				else if (dir == -1)
					return score;
			}
		} while (high <= 4 && high > -4);
		return score;
	}

}
