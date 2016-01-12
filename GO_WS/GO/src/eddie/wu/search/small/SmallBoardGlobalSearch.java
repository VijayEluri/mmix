package eddie.wu.search.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.ExpectScore;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.HistoryDepScore;
import eddie.wu.search.ScopeScore;
import eddie.wu.search.ScoreWithManual;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.SearchLevel;
import eddie.wu.search.global.TerminalState;
import eddie.wu.search.global.VerifySearchResult;

/**
 * 1. only for small board (3<=size<=?).<br/>
 * 这里的search都是由黑方先行。
 */
public class SmallBoardGlobalSearch extends GoBoardSearch {
	private static final Logger logStatistic = Logger
			.getLogger("search.statistic");
	private static final Logger log = Logger
			.getLogger(SmallBoardGlobalSearch.class);
	SmallGoBoard goBoard;
	public int initTurn;

	/**
	 * 存储已知的状态的结果，因为不同搜索需要的表示方法不同，放在子类中。<br/>
	 * we may need to extend the result score from a integer to a scope
	 * [low,high] like [-6, -9]<br/>
	 * another idea is to know max or min by whose turn. then the score means
	 * either >= score or <=score.
	 */
	protected Map<BoardColorState, ExpectScore> historyIndependentResult = new HashMap<BoardColorState, ExpectScore>();

	/**
	 * store terminate state result. not that useful
	 */
	protected Map<BoardColorState, Integer> terminalResults = new HashMap<BoardColorState, Integer>();

	List<TreeGoManual> manuals = new ArrayList<TreeGoManual>();

	public SmallBoardGlobalSearch(BoardColorState state, int highestScore,
			int lowestScore) {
		super(highestScore, lowestScore);
		goBoard = new SmallGoBoard(state);
		this.initTurn = state.getWhoseTurn(); // (brother)
	}

	/**
	 * caller provide state and estimated score, search will decide the score.
	 * if it's Black's turn, expScore v.s. expScore - 1<br/>
	 * otherwise, expScore + 1 v.s. expScore <br.>
	 * expScore is for the current player of the state<br/>
	 * if the score returned in search equals expScore, the current turn's
	 * player win.
	 * 
	 * @param state
	 * @param expScore
	 */
	public SmallBoardGlobalSearch(BoardColorState state, int expScore) {
		super(state.isBlackTurn() ? expScore : expScore + 1, state
				.isBlackTurn() ? expScore - 1 : expScore);
		int maxScore = state.boardSize * state.boardSize;
		if (state.isBlackTurn() && expScore == -maxScore) {
			throw new RuntimeException("Black: expScore == -maxScore");
		} else if (state.isWhiteTurn() && expScore == maxScore) {
			throw new RuntimeException("White: expScore == maxScore");
		}
		goBoard = new SmallGoBoard(state);
		this.initTurn = state.getWhoseTurn(); // (brother)
	}

	/**
	 * @deprecated it may cause too much calculation, since all the branch need
	 *             to be considered
	 * @param boards
	 * @param whoseTurn
	 */
	// public SmallBoardGlobalSearch(byte[][] boards, int whoseTurn) {
	//
	// int boardSize = boards.length - 2;
	// super(boardSize * boardSize,boardSize * boardSize);
	// this.maxExpScore = boardSize * boardSize;
	// this.minExpScore = 0 - maxExpScore;
	// goBoard = new SmallGoBoard(BoardColorState.getInstance(boards,
	// whoseTurn));
	// this.initLevel(whoseTurn);
	// }

	public SmallBoardGlobalSearch(byte[][] boards, int whoseTurn,
			int highestScore, int lowestScore) {
		super(highestScore, lowestScore);
		goBoard = new SmallGoBoard(BoardColorState.getInstance(boards,
				whoseTurn));
		this.initTurn = whoseTurn; // (brother)
	}

	public SmallBoardGlobalSearch(int boardSize, int highestScore,
			int lowestScore) {
		super(highestScore, lowestScore);
		byte[][] board = new byte[boardSize + 2][boardSize + 2];
		int whoseTurn = Constant.BLACK;
		goBoard = new SmallGoBoard(
				BoardColorState.getInstance(board, whoseTurn));

		this.initTurn = whoseTurn; // (brother)
	}

	@Override
	protected void initCandidate(SearchLevel level, int color) {
		List<Candidate> candidates = goBoard.getCandidate(color, true);
		level.setCandidates(candidates);
	}

	@Override
	public GoBoard getGoBoard() {
		return goBoard;
	}

	// public SmallBoardGlobalSearch(byte[][] board) {
	// this(board, Constant.BLACK);
	// }

	@Override
	public SearchLevel getInitLevel() {
		SearchLevel level = new SearchLevel(0, initTurn, null);
		// level 0: all candidates of original state.
		if (initTurn == Constant.BLACK) {
			level.setMax(true);			
		} else {
			level.setMax(false);
		}
		level.setExpScore(this.getExpScore());
		return level;
	}

	// @Override
	// public int getScore(BoardColorState boardColorState) {
	// BoardColorState boardColorStateN = boardColorState.normalize();
	// if (terminalResults.containsKey(boardColorStateN)) {
	// return terminalResults.get(boardColorStateN).intValue();
	// }
	// BoardColorState boardColorStateS = boardColorState.blackWhiteSwitch()
	// .normalize();
	// if (terminalResults.containsKey(boardColorStateS)) {
	// return 0 - terminalResults.get(boardColorStateS).intValue();
	// }
	// if (this.historyIndependentResult.containsKey(boardColorStateN)) {
	// if (boardColorState.isBlackTurn()) {
	// return historyIndependentResult.get(boardColorStateN)
	// .getLowExp();
	// } else {
	// return historyIndependentResult.get(boardColorStateN)
	// .getHighExp();
	// }
	// }
	// if (this.historyIndependentResult.containsKey(boardColorStateS)) {
	// if (boardColorState.isBlackTurn()) {
	// return 0 - historyIndependentResult.get(boardColorStateS)
	// .getHighExp();
	// } else {
	// return 0 - historyIndependentResult.get(boardColorStateS)
	// .getLowExp();
	// }
	// }
	// return 0;
	// }

	/**
	 * two two board optimization
	 */
	@Override
	protected TerminalState getTerminalState() {
		TerminalState ts = new TerminalState();
		if (goBoard.areBothPass()) {
			ts.setBothPass(true);
			ts.setTerminalState(true);
			ts.setFinalResult(goBoard.finalResult_doublePass());

		} else if (terminalResults.containsKey(this.getGoBoard()
				.getBoardColorState().normalize())) {
			ts.setTerminalState(true);
			// ???
			ts.setScore(terminalResults.get(
					this.getGoBoard().getBoardColorState().normalize())
					.intValue());

		} else if (terminalResults.containsKey(this.getGoBoard()
				.getBoardColorState().normalize().blackWhiteSwitch())) {
			ts.setTerminalState(true);
			// ???
			ts.setScore(0 - terminalResults.get(
					this.getGoBoard().getBoardColorState().normalize())
					.intValue());

		}

		return ts;
	}

	/**
	 * only normalized state is stored! for efficiency.
	 */
	// @Override
	// public boolean isKnownState(BoardColorState boardColorState) {
	// BoardColorState boardColorStateN = boardColorState.normalize();
	// if (terminalResults.containsKey(boardColorStateN))
	// return true;
	// else if (this.historyIndependentResult.containsKey(boardColorStateN))
	// return true;
	// // Black is not symmetry with White due to different expecting score
	// // like [-8,-9]
	// BoardColorState boardColorStateS = boardColorState.blackWhiteSwitch()
	// .normalize();
	// if (terminalResults.containsKey(boardColorStateS))
	// return true;
	// else if (this.historyIndependentResult.containsKey(boardColorStateS))
	// return true;
	// return false;
	// }

	public void outputSearchStatistics() {
		{// ensure win/lose manual is ready!
			TreeGoManual manual = getTreeGoManual();
			int initScore = manual.initScore();
			boolean maxWin = false;
//			if(this.initTurn==Constant.BLACK)
			if (initScore >= this.getExpScore().getHighExp())
				maxWin = true;
			manual.cleanupBadMoveForWinner(maxWin);
			manual.getMostExpManual().print(logStatistic);
		}
		if (logStatistic.isInfoEnabled() == false) {
			return;
		}
		logStatistic.info(this.getExpScore());
		logStatistic.info("we calculate steps = " + countSteps);
		long forwardMoves = goBoard.getForwardMoves();
		logStatistic.info("forwardMoves = " + forwardMoves);
		long backwardMoves = goBoard.getBackwardMoves();
		logStatistic.info("backwardMoves = " + backwardMoves);
		// because we will go back to initial state in the end of the
		// search!
		// TestCase.assertEquals(forwardMoves,backwardMoves);
		logStatistic.info("we know the result = " + terminalResults.size());
		for (Entry<BoardColorState, Integer> entry : terminalResults.entrySet()) {
			if (entry.getKey().getWhoseTurn() == Constant.WHITE)
				continue;
			// logStatistic.info(entry.getKey().getStateString() +
			// " the score is = "
			// + entry.getValue());

		}
		this.logHistoryDepStateReached();
		String fileName = Constant.DYNAMIC_DATA
				+ "small_board/three_three/decided/" + "all_state.sgf";
		SGFGoManual.storeGoManual(fileName, manuals);

		int count = getSearchProcess().size();
		int bothPass = 0;
		int exhaust = 0;
		for (String list : getSearchProcess()) {
			if (count < 2000) {
				logStatistic.info(list);
			}
			if (list.endsWith(DB_PASS + ")")) {
				bothPass++;
			} else if (list.endsWith((EXHAUST + ")"))) {
				exhaust++;
			}
		}

		logStatistic.info("searched path = " + getSearchProcess().size());
		logStatistic.info("Pure searched path = " + (count - exhaust));

		TreeGoManual manual = getTreeGoManual();
		logStatistic.info("raw data: ");
		logStatistic.info(manual.getSGFBodyString(false));
		int initScore = manual.initScore();

		logStatistic.info("Before Cleanup ");
		logStatistic.info("Init score = " + initScore);
		logStatistic.info(manual.getSGFBodyString(false));
		boolean maxWin = false;
		if (initScore >= this.getExpScore().getHighExp())
			maxWin = true;
		manual.cleanupBadMoveForWinner(maxWin);
		logStatistic.info("After Cleanup " + this.getExpScore());
		logStatistic.info(manual.getSGFBodyString(false));
		manual.getMostExpManual().print(logStatistic);

	}

	public void printKnownResult() {
		for (Map.Entry<BoardColorState, Integer> entry : terminalResults
				.entrySet()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(entry.getKey().getStateString());
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(entry.getValue());
		}
	}

	/**
	 * the state is decided because of dual give up.<br/>
	 * Commented out: the state is decided by dual give up, might not be the
	 * exact value.<br/>
	 * even the double-pass state is accurate, when it level up the intermediate
	 * state is not accurate.<br/>
	 * fix in two ways: 1. user score scope like [4,6]<br/>
	 * 2. double pass is filtered (should not be treated as terminal state).
	 */
	@Override
	public void stateDecided(BoardColorState boardColorStateN, boolean max,
			int score, boolean win) {
		BoardColorState boardColorState = boardColorStateN.normalize();
		int boardSize = boardColorState.boardSize;
		int low, high;
		if (max) {
			if (win) {
				low = score;
				high = boardSize * boardSize;
			} else {
				high = score;
				low = 0 - boardSize * boardSize;
			}
		} else {
			if (win) {
				low = 0 - boardSize * boardSize;
				high = score;
			} else {
				low = score;
				high = boardSize * boardSize;
			}
		}
		ExpectScore expScore = new ExpectScore(low, high);
		this.historyIndependentResult.put(boardColorState, expScore);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("add non-final state with score = " + score);
			log.warn(boardColorState.getStateString());
		}

		BoardColorState colorStateSwitch = boardColorState.blackWhiteSwitch()
				.normalize();
		ExpectScore expScore2 = new ExpectScore(0 - high, 0 - low);
		this.historyIndependentResult.put(colorStateSwitch, expScore2);

		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("add reverse non-final state with score = " + (-score));
			log.warn(colorStateSwitch.getStateString());
		}
	}

	/**
	 * 先后手无关的终局状态。评分确定。
	 */
	@Override
	public void stateFinalizeed(BoardColorState boardColorStateN,
			int scoreTerminator) {
		terminalResults.put(boardColorStateN, scoreTerminator);
		terminalResults.put(boardColorStateN.getReverseTurnCopy(),
				scoreTerminator);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("we add 2 final state with score = " + scoreTerminator
					+ "; state is: ");
			log.warn(boardColorStateN.getStateString());
		}

		BoardColorState boardColorStateS = boardColorStateN.blackWhiteSwitch()
				.normalize();
		terminalResults.put(boardColorStateS, -scoreTerminator);
		terminalResults.put(boardColorStateS.getReverseTurnCopy(),
				-scoreTerminator);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {

			log.warn("we add 2 reverse final state with score = "
					+ (-scoreTerminator) + "; state is: ");
			log.warn(boardColorStateS.getStateString());
		}
	}

	public static int getAccurateScore(BoardColorState state) {
		int expScore = state.getScore_assumeAllLive();
		return getAccurateScore(state, expScore);
	}

	/**
	 * utility method for better external use.
	 * 
	 * @param state
	 * @param expScore
	 *            is the estimation of right score, but not guaranteed.
	 * @return
	 */

	public static int getAccurateScore(BoardColorState state, int expScore) {
		ScopeScore scopeScore = ScopeScore.getInitScore(state.boardSize);
		int high = 1;
		int low = 0;
		int score = 0;
		int dir = 0;// direction to check further
		int maxScore = state.boardSize * state.boardSize;
		// int bestScore = 0;
		SmallBoardGlobalSearch goS;

		assert (expScore <= maxScore);
		assert (expScore >= 0 - maxScore);
		if (expScore > maxScore)
			throw new RuntimeException("expScore > maxScore");
		if (expScore < -maxScore) {
			throw new RuntimeException("expScore < -maxScore");
		}

		// check whether current player can reach the expScore.
		if (state.isBlackTurn()) {
			if (expScore == -maxScore) {
				// Black must win, simulate the result with searching
				dir = 1;
				// bestScore = expScore;
				high = expScore + 1;
			} else {
				high = expScore;
			}
			low = high - 1;
		} else { // white's turn
			if (expScore == maxScore) {
				// White must win, simulate the result with searching
				dir = -1;
				// bestScore = expScore;
				low = expScore - 1;
			} else {
				low = expScore;
			}
			high = low + 1;
		}

		TreeGoManual win = null;
		TreeGoManual lose = null;
		do {
			if (state.boardSize == 2) {
				goS = new TwoTwoBoardSearch(state, state.isBlackTurn() ? high
						: low);
			} else if (state.boardSize == 3) {
				goS = new ThreeThreeBoardSearch(state,
						state.isBlackTurn() ? high : low);
			} else {
				throw new RuntimeException("boardSize=" + state.boardSize);
			}
			score = goS.globalSearch();
			state.setVariant(goS.getSearchProcess().size());
			goS.logStateReached();
			goS.logHistoryDepStateReached();
			goS.outputSearchStatistics();
			
			//check whether the state collected in this search is compatible with existing searchResult.
			Map<BoardColorState, ScoreWithManual> stateReached2 = goS.getStateReached();
			ScopeScore.merge(stateReached_,stateReached2);
			HistoryDepScore.merge(hisDepState_, goS.getStateHisDepReached());
			if (score >= high) {
				if (state.isBlackTurn()) {
					log.warn("Black Play First: search with high = " + high
							+ " succeed with score = " + score);
					scopeScore.updateWin(score, true);
					win = goS.getTreeGoManual();

				} else {
					log.warn("White Play First: search with low = " + low
							+ " fail with score = " + score);
					scopeScore.updateLose(score, false);
					lose = goS.getTreeGoManual();
				}
				if (scopeScore.isExact())
					break;
				high = score + 1;
				low = high - 1;
				// if (dir == 0) {
				// dir = 1;
				// //bestScore = score;
				// } else if (dir == -1) {
				// //assert (score == bestScore);
				// TestCase.assertEquals(score, bestScore);
				// return score;
				// } else {
				// bestScore = score;
				// continue;
				// }
			} else {
				if (state.isBlackTurn()) {
					log.warn("Black Play First: search with high = " + high
							+ " fail with score = " + score);
					scopeScore.updateLose(score, true);
					lose = goS.getTreeGoManual();
				} else {
					log.warn("White Play First: search with low = " + low
							+ " succeed with score = " + score);
					scopeScore.updateWin(score, false);
					win = goS.getTreeGoManual();
				}
				if (scopeScore.isExact())
					break;
				low = score - 1;
				high = low + 1;
				// if (dir == 0) {
				// dir = -1;
				// bestScore = score;
				// } else if (dir == 1) {
				// assert (score == bestScore);
				// TestCase.assertEquals(score, bestScore);
				// return score;
				// } else {
				// bestScore = score;
				// continue;
				// }
			}
		} while (high <= maxScore && low > -maxScore);
		score = scopeScore.getExactScore();
		if (state.isBlackTurn()) {
			if (score == -maxScore) {
				VerifySearchResult.verifyBetterImpossible(state, score, lose);
			} else if (score == maxScore) {
				VerifySearchResult.VerifyWin(state, score, win);
			} else {
				VerifySearchResult.VerifyWin(state, score, win);
				VerifySearchResult.verifyBetterImpossible(state, score, lose);
			}
		} else if (state.isWhiteTurn()) {
			if (score == -maxScore) {
				VerifySearchResult.VerifyWin(state, score, win);
			} else if (score == maxScore) {
				VerifySearchResult.verifyBetterImpossible(state, score, lose);
			} else {
				VerifySearchResult.VerifyWin(state, score, win);
				VerifySearchResult.verifyBetterImpossible(state, score, lose);
			}
		}

		return score;
	}
	
	

}
