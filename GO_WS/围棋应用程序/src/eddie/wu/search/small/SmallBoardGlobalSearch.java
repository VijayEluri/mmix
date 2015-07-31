package eddie.wu.search.small;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.SearchLevel;
import eddie.wu.search.global.TerminalState;

/**
 * 1. only for small board (3<=size<=?).<br/>
 * 这里的search都是由黑方先行。
 */
public class SmallBoardGlobalSearch extends GoBoardSearch {
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
	protected Map<BoardColorState, Integer> results = new HashMap<BoardColorState, Integer>();

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
	 * if the score returned in search equals expScore, the current turn's player
	 * win.
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

	protected List<Candidate> getCandidate(int color) {
		return goBoard.getCandidate(color, true);
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

		SearchLevel level = new SearchLevel(0, initTurn);

		// level 0: all candidates of original state.
		if (initTurn == Constant.BLACK) {
			level.setMax(true);
			level.setMaxExp(this.getMaxExp());
		} else {
			level.setMax(false);
			level.setMinExp(this.getMinExp());
		}

		level.initTempBestScore();
		return level;
	}

	@Override
	public int getScore(BoardColorState boardColorState) {
		BoardColorState boardColorStateN = boardColorState.normalize();
		if (results.containsKey(boardColorStateN))
			return results.get(boardColorStateN).intValue();
		BoardColorState boardColorStateS = boardColorState.blackWhiteSwitch()
				.normalize();
		return 0 - results.get(boardColorStateS).intValue();

	}

	/**
	 * two two board optimization
	 */
	@Override
	protected TerminalState getTerminalState() {
		TerminalState ts = new TerminalState();
		if (goBoard.areBothPass()) {
			ts.setTerminalState(true);
			ts.setFinalResult(goBoard.finalResult_doublePass());

		} else if (results.containsKey(this.getGoBoard().getBoardColorState())) {
			ts.setTerminalState(true);
			// ???
			ts.setScore(results.get(this.getGoBoard().getBoardColorState())
					.intValue());

		}

		return ts;
	}

	/**
	 * only normalized state is stored! for efficiency.
	 */
	@Override
	public boolean isKnownState(BoardColorState boardColorState) {
		BoardColorState boardColorStateN = boardColorState.normalize();
		if (results.containsKey(boardColorStateN))
			return true;
		// Black is not symmetry with White due to different expecting score
		// like [-8,-9]
		// BoardColorState boardColorStateS = boardColorState.blackWhiteSwitch()
		// .normalize();
		// if (results.containsKey(boardColorStateS))
		// return true;
		return false;
	}

	public void outputSearchStatistics() {
		outputSearchStatistics(log);
	}

	// protected TerminalState getTerminalState() {
	// TerminalState ts = new TerminalState();
	// // boolean state = goBoard.isFinalState(1);
	// int state = goBoard.finalStateType();
	// if (state <= 0) {
	// ts.setTerminalState(false);
	// } else {
	// ts.setTerminalState(true);
	// ts.setFinalResult(goBoard.finalResult(state));
	// // ts.setScore(this)
	// }
	// return ts;
	// }

	// protected int getScore() {
	// FinalResult finalResult = goBoard.finalResult();
	// if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
	// log.warn(goBoard.getStateString().toString());
	// log.warn(finalResult.toString());
	// }
	// return finalResult.getScore();
	// }

	public void outputSearchStatistics(Logger log) {
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("Black expect: " + getMaxExp() + ", White expect:"
					+ getMinExp());
			log.warn("we calculate steps = " + countSteps);
			long forwardMoves = goBoard.getForwardMoves();
			log.warn("forwardMoves = " + forwardMoves);
			long backwardMoves = goBoard.getBackwardMoves();
			log.warn("backwardMoves = " + backwardMoves);
			//because we will go back to initial state in the end of the search!
//			TestCase.assertEquals(forwardMoves,backwardMoves);
			log.warn("we know the result = " + results.size());
			for (Entry<BoardColorState, Integer> entry : results.entrySet()) {
				if (entry.getKey().getWhoseTurn() == Constant.WHITE)
					continue;
				// log.warn(entry.getKey().getStateString() + " the score is = "
				// + entry.getValue());

			}
			String fileName = Constant.rootDir
					+ "smallboard/threethree/decided/" + "all_state.sgf";
			SGFGoManual.storeGoManual(fileName, manuals);

			int count = 0;
			int bothPass = 0;
			int exhaust = 0;
			for (String list : getSearchProcess()) {
				count++;
				log.warn(list);
//				if (count % 100 == 0)
//					log.warn("count=" + count);
				if (list.endsWith(DB_PASS + ")")) {
					bothPass++;
				} else if (list.endsWith((EXHAUST + ")"))) {
					exhaust++;
				}
			}

			log.warn("searched path = " + getSearchProcess().size());
			log.warn("Pure searched path = "
					+ (getSearchProcess().size() - exhaust));
		}
	}

	public void printKnownResult() {
		for (Map.Entry<BoardColorState, Integer> entry : results.entrySet()) {
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
	 * state is not accurate.
	 */
	@Override
	public void stateDecided(BoardColorState boardColorStateN, int score) {
		// BoardColorState boardColorState = boardColorStateN.normalize();
		// this.results.put(boardColorState, score);
		// if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
		// log.warn("add non-final state with score = " + score);
		// log.warn(boardColorState.getStateString());
		// }
		//
		// BoardColorState colorStateSwitch = boardColorState.blackWhiteSwitch()
		// .normalize();
		// this.results.put(colorStateSwitch, -score);
		//
		// if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
		// log.warn("add reverse non-final state with score = " + (-score));
		// log.warn(colorStateSwitch.getStateString());
		// }
	}

	/**
	 * 先后手无关的终局状态。
	 */
	@Override
	public void stateFinalizeed(BoardColorState boardColorStateN,
			int scoreTerminator) {
		results.put(boardColorStateN, scoreTerminator);
		results.put(boardColorStateN.getReverseTurnCopy(), scoreTerminator);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("we add 2 final state with score = " + scoreTerminator
					+ "; state is: ");
			log.warn(boardColorStateN.getStateString());
		}

		BoardColorState boardColorStateS = boardColorStateN.blackWhiteSwitch()
				.normalize();
		results.put(boardColorStateS, -scoreTerminator);
		results.put(boardColorStateS.getReverseTurnCopy(), -scoreTerminator);
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
	 * @return
	 */

	public static int getAccurateScore(BoardColorState state, int expScore) {

		int high = 1;
		int low = 0;
		int score = 0;
		int dir = 0;// direction to check further
		int maxScore = state.boardSize * state.boardSize;
		int bestScore = 0;
		GoBoardSearch goS;

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
				bestScore = expScore;
				high = expScore + 1;
			} else {
				high = expScore;
			}
			low = high - 1;
		} else { // white's turn
			if (expScore == maxScore) {
				// White must win, simulate the result with searching
				dir = -1;
				bestScore = expScore;
				low = expScore - 1;
			} else {
				low = expScore;
			}
			high = low + 1;
		}

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

			if (score >= high) {
				if (state.isBlackTurn()) {
					log.error("Black Play First: search with high = " + high
							+ " succeed with score = " + score);
				} else {
					log.error("White Play First: search with low = " + low
							+ " fail with score = " + score);
				}
				high = score + 1;
				low = high - 1;
				if (dir == 0) {
					dir = 1;
					bestScore = score;
				} else if (dir == -1) {
					assert (score == bestScore);
					TestCase.assertEquals(score, bestScore);
					return score;
				} else {
					bestScore = score;
					continue;
				}
			} else {
				if (state.isBlackTurn()) {
					log.error("Black Play First: search with high = " + high
							+ " fail with score = " + score);
				} else {
					log.error("White Play First: search with low = " + low
							+ " succeed with score = " + score);
				}
				low = score - 1;
				high = low + 1;
				if (dir == 0) {
					dir = -1;
					bestScore = score;
				} else if (dir == 1) {
					assert (score == bestScore);
					TestCase.assertEquals(score, bestScore);
					return score;
				} else {
					bestScore = score;
					continue;
				}
			}
		} while (high <= maxScore && low > -maxScore);
		return score;
	}
}
