package eddie.wu.search.global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.domain.analy.TerritoryAnalysis;

/**
 * 这里的search都是由黑方先行。
 */
public class SmallBoardGlobalSearch extends GoBoardSearch {
	private static final Logger log = Logger
			.getLogger(SmallBoardGlobalSearch.class);
	SmallGoBoard goBoard;
	int boardSize;
	int lowestScore;
	int highestScore;

	/**
	 * 存储已知的状态的结果，因为不同搜索需要的表示方法不同，放在子类中。
	 */
	protected Map<BoardColorState, Integer> results = new HashMap<BoardColorState, Integer>();

	public void printKnownResult() {
		for (Map.Entry<BoardColorState, Integer> entry : results.entrySet()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(entry.getKey().getStateString());
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(entry.getValue());
		}
	}

	public void outputSearchStatistics(Logger log) {
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
//			for (Entry<BoardColorState, Integer> entry : results.entrySet()) {
//				log.warn(entry.getKey().getStateString() + " the score is = "
//						+ entry.getValue());
//
//			}
			log.warn("we calculate steps = " + countSteps);
			log.warn("we know the result = " + results.size());
			log.warn("Black expect: "+ this.highestScore+", White expect:"+this.lowestScore);
		}
	}
	public void outputSearchStatistics() {
		outputSearchStatistics(log);
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

	public SmallBoardGlobalSearch(int boardSize) {
		this.boardSize = boardSize;
		byte[][] board = new byte[boardSize + 2][boardSize + 2];
		int whoseTurn = Constant.BLACK;
		goBoard = new SmallGoBoard(
				BoardColorState.getInstance(board, whoseTurn));

		this.initLevel(whoseTurn);
	}

	public SmallBoardGlobalSearch(byte[][] board) {
		this(board, Constant.BLACK);
	}

	public SmallBoardGlobalSearch(byte[][] boards, int highestScore,
			int lowestScore) {
		this(boards, Constant.BLACK, highestScore, lowestScore);
	}

	/**
	 * @deprecated
	 * it may cause too much calculation, since all the branch need to be considered
	 * @param boards
	 * @param whoseTurn
	 */
	public SmallBoardGlobalSearch(byte[][] boards, int whoseTurn) {
		this.boardSize = boards.length - 2;
		this.highestScore = boardSize * boardSize;
		this.lowestScore = 0 - highestScore;
		goBoard = new SmallGoBoard(BoardColorState.getInstance(boards,
				whoseTurn));
		this.initLevel(whoseTurn);
	}

	public SmallBoardGlobalSearch(byte[][] boards, int whoseTurn,
			int highestScore, int lowestScore) {
		this.boardSize = boards.length - 2;
		goBoard = new SmallGoBoard(BoardColorState.getInstance(boards,
				whoseTurn));
		this.highestScore = highestScore;
		this.lowestScore = lowestScore;
		this.initLevel(whoseTurn);
	}

	private void initLevel(int whoseTurn) {
		SearchLevel level = new SearchLevel(0, whoseTurn);
		// level 0: all candidates of original state.
		if (whoseTurn == Constant.BLACK) {
			level.setWhoseTurn(Constant.MAX);
			level.setHighestExp(this.getHighestExp());
			level.setTempBestScore(Integer.MIN_VALUE);

		} else {
			level.setWhoseTurn(Constant.MIN);
			level.setLowestExp(this.getLowestExp());
			level.setTempBestScore(Integer.MAX_VALUE);
		}
		List<Candidate> candidates = getCandidate(whoseTurn);
		level.setCandidates(candidates, whoseTurn);
		levels.add(level);
	}

	protected List<Candidate> getCandidate(int color) {
		return goBoard.getCandidate(color, true);
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

	/**
	 * two two board optimization
	 */
	@Override
	protected TerminalState getTerminalState() {
		TerminalState ts = new TerminalState();
		if (this.isDoubleGiveup()) {
			ts.setTerminalState(true);
			ts.setFinalResult(goBoard
					.finalResult(TerritoryAnalysis.DEAD_CLEANED_UP));

		} else if (results.containsKey(this.getGoBoard().getBoardColorState())) {
			ts.setTerminalState(true);
			ts.setFinalResult(goBoard
					.finalResult(TerritoryAnalysis.DEAD_CLEANED_UP));
			if (ts.getScore() != results.get(
					this.getGoBoard().getBoardColorState()).intValue()) {
				throw new RuntimeException("score "
						+ ts.getScore()
						+ " not match for state "
						+ this.getGoBoard().getBoardColorState()
								.getStateString());
			}
		}

		return ts;
	}

	protected int getLowestExp() {
		// return 0 - boardSize * boardSize;
		return this.lowestScore;
	}

	@Override
	protected int getHighestExp() {
		// return boardSize * boardSize;
		return this.highestScore;
	}

	@Override
	public GoBoard getGoBoard() {
		return goBoard;
	}

	@Override
	protected boolean isDoubleGiveup() {
		return goBoard.isDoubleGiveup();
	}

	/**
	 * only normalized state is stored! for efficiency.
	 */
	@Override
	public boolean isKnownState(BoardColorState boardColorState) {
		BoardColorState boardColorStateN = boardColorState.normalize();
		if (results.containsKey(boardColorStateN))
			return true;
		BoardColorState boardColorStateS = boardColorState.blackWhiteSwitch()
				.normalize();
		if (results.containsKey(boardColorStateS))
			return true;
		return false;
	}

	@Override
	public int getScore(BoardColorState boardColorState) {
		BoardColorState boardColorStateN = boardColorState.normalize();
		if (results.containsKey(boardColorStateN))
			return results.get(boardColorStateN).intValue();
		BoardColorState boardColorStateS = boardColorState.blackWhiteSwitch()
				.normalize();
		return 0-results.get(boardColorStateS).intValue();

	}

}
