package eddie.wu.search.small;

import java.util.Map.Entry;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.SearchLevel;
import eddie.wu.search.global.TerminalState;

/**
 * 将2*2的小棋盘的特殊处理应用于3*3的小棋盘。<br/>
 * 
 * 
 * @author Eddie Wu
 * 
 */
public class ThreeThreeBoardSearch extends SmallBoardGlobalSearch {
	private static Logger log = Logger.getLogger(ThreeThreeBoardSearch.class);
	/**
	 * use it when verifying whether the terminal state static judgment is
	 * correct.
	 */
	private boolean tillBothPass = false;

	// public ThreeThreeBoardSearch(byte[][] boards, int whoseTurn) {
	// super(boards, whoseTurn);
	// initKnownState();
	// }

	public void setTillBothPass(boolean tillBothPass) {
		this.tillBothPass = tillBothPass;
	}

	public ThreeThreeBoardSearch(byte[][] boards, int whoseTurn,
			int highestScore, int lowestScore) {
		super(boards, whoseTurn, highestScore, lowestScore);
		// initKnownState();
	}

	public ThreeThreeBoardSearch(BoardColorState state, int expScore) {
		super(state, expScore);
		// initKnownState();
	}

	public ThreeThreeBoardSearch(BoardColorState state, int low, int high) {
		super(state, low, high);
		// initKnownState();
	}

	/**
	 * the state is decided because of dual give up.
	 */
	// @Override
	// public void stateDecided(BoardColorState boardColorState, int score) {
	//
	// }

	private void initKnownState() {
		/**
		 * one point big eye!
		 */
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 9);

		text[0] = new String("[_, B, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 3);

		text = new String[3];
		text[0] = new String("[B, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), -9);

		/**
		 * two points big eye!
		 */
		text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 3);
		text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, B, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 9);
		/**
		 * three points big eye!
		 */
		text = new String[3];
		text[0] = new String("[B, B, B]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), -9);

		text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[_, _, _]");
		state = StateLoader.LoadStateFromText(text);
		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 9);
		/**
		 * four points big eye!
		 */
		text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");
		state = StateLoader.LoadStateFromText(text);
		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 4);

		text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, B]");
		state = StateLoader.LoadStateFromText(text);
		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 4);

		text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		state = StateLoader.LoadStateFromText(text);
		terminalResults.put(BoardColorState.getInstance(state, Constant.BLACK)
				.normalize(), 9);
		terminalResults.put(BoardColorState.getInstance(state, Constant.WHITE)
				.normalize(), 4);
		shownInitKnownState();
		// for (BoardColorState bcs : new ListAllState().getFinalState(3)) {
		//
		// }

	}

	public void shownInitKnownState() {
		for (Entry<BoardColorState, Integer> result : terminalResults
				.entrySet()) {
			System.out.print(result.getKey().getStateAsOneLineString());
			System.out.println("Score = " + result.getValue());
		}
	}

	/**
	 * three three board optimization
	 */
	@Override
	protected TerminalState getTerminalState() {
		TerminalState ts = new TerminalState();
		if (tillBothPass) {
			/**
			 * special strategy in global search, will end only if all dead
			 * stone is cleaned up.
			 */
			if (goBoard.areBothPass()) {
				ts.setBothPass(true);
				ts.setTerminalState(true);
				ts.setFinalResult(goBoard.finalResult_doublePass());
			} else {
				ts.setTerminalState(false);
			}
			return ts;
		}

		boolean knownState = false;

		if (goBoard.areBothPass()) {
			ts.setTerminalState(true);
			ts.setBothPass(true);
			ts.setFinalResult(goBoard.finalResult_doublePass());
		} else if (terminalResults.containsKey(this.getGoBoard()
				.getBoardColorState().normalize())) {
			knownState = true;
			ts.setTerminalState(true);
			Integer score = terminalResults.get(this.getGoBoard()
					.getBoardColorState().normalize());
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

		if (goBoard.areBothPass() == false && ts.isTerminalState() == true
				&& knownState == false) {
			BoardColorState boardColorStateN = this.getGoBoard()
					.getBoardColorState();
			int scoreTerminator = ts.getScore();
			if (scoreTerminator > 9 || scoreTerminator < -9) {
				throw new RuntimeException("scoreTerminator" + scoreTerminator);
			}
			this.stateFinalizeed(boardColorStateN, scoreTerminator);
		}
		return ts;
	}

	/**
	 * 同型禁现导致状态不足以决定结果,还要考虑到达该状态的历史过程.<br/>
	 * 对称的不同候选点可能一个是同型再现,而另外一个不是.这就有了差异.
	 */
	@Override
	protected void initCandidate(SearchLevel level, int color) {
		int expectedScore;
		int boardSize = goBoard.boardSize;
		if (boardSize <= 3) {
			if (color == Constant.BLACK) {
				expectedScore = this.getExpScore().getHighExp();
			} else {
				expectedScore = this.getExpScore().getLowExp();
			}
		} else {
			if (color == Constant.BLACK) {
				expectedScore = boardSize * boardSize;
			} else {
				expectedScore = 0 - boardSize * boardSize;
			}
		}

		level.setCandidates(goBoard.getCandidate(level, true, expectedScore));

		// if (goBoard.getInitSymmetryResult().getNumberOfSymmetry() == 0) {
		// return goBoard.getCandidate(color, false, expectedScore);
		// } else if (goBoard.getStepHistory().getAllSteps().isEmpty()) {
		// // first move
		// return goBoard.getCandidate(color, true, expectedScore);
		// } else if (goBoard.getStepHistory().noRealStep()) {
		// // first real move
		// return goBoard.getCandidate(color, true, expectedScore);
		// } else {
		// SymmetryResult symmetry = goBoard.getLastStep().getSymmetry();
		// //will let symmetry = null for 3*3 board.
		// if (symmetry != null && symmetry.getNumberOfSymmetry() != 0) {
		// return goBoard.getCandidate(color, true, expectedScore);
		// } else {
		// return goBoard.getCandidate(color, false, expectedScore);
		// }
		// }
	}

	/**
	 * 1. ensure the state is not history dependent; if it has ever been reached
	 * by some variants. <br/>
	 * 
	 * 2. double give up is not accurate result<br/>
	 * 
	 * 3. the meaning of the result depends on whose turn it is, if it is
	 * black's turn (White play to reach current state), the result is <= the
	 * result score; because white in the future phase did not do its best since
	 * it only try to reach target score!<br/>
	 */
	// @Override
	// public void stateDecided(BoardColorState boardColorStateN, int score) {
	// if (score < -9 || score > 9) {
	// return;
	// // throw new RuntimeException("scoreTerminator=" + score);
	// }
	// String name = boardColorStateN.getStateAsOneLineString();
	// String fileName = Constant.rootDir + "smallboard/threethree/decided/"
	// + name + "win.sgf";
	// // SearchNode root = SearchNode.getSpecialRoot();
	// // root.addChild(this.getGoBoard().getCurrent());
	// SearchNode root = this.getGoBoard().getCurrent().copySubTree();
	// TreeGoManual manual = new TreeGoManual(boardColorStateN);
	// manual.setRoot(root);
	// manual.setResult(String.valueOf(score));
	//
	// manuals.add(manual);
	// if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
	// log.warn("add non-final (but decided) state with score = " + score);
	// String history = getGoBoard().getCurrent()
	// .getSingleManualStringToRoot(false);
	// log.warn(history);
	// log.warn(boardColorStateN.getStateString());
	// }
	//
	// BoardColorState boardColorState = boardColorStateN.normalize();
	// this.terminalResults.put(boardColorState, score);
	// // DO not add reverse color version. because score expectation are
	// // different.
	// // +6 means good enough in [-8,-9], but -6 is NOT good enough in
	// // [-8,-9].
	// // be careful of the in-symmetry.
	// // BoardColorState colorStateSwitch = boardColorState.blackWhiteSwitch()
	// // .normalize();
	// // this.results.put(colorStateSwitch, -score);
	// // do not log reverse state!
	// // if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
	// // log.warn("add reverse non-final((but decided)) state with score = " +
	// // (-score));
	// // log.warn(colorStateSwitch.getStateString());
	// // }
	//
	// }

}
