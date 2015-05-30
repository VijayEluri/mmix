package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.TreeGoManual;

/**
 * 这里的search都是由黑方先行。
 */
public class SmallBoardGlobalSearch extends GoBoardSearch {
	private static final Logger log = Logger
			.getLogger(SmallBoardGlobalSearch.class);
	SmallGoBoard goBoard;
	public int initTurn;

	/**
	 * 存储已知的状态的结果，因为不同搜索需要的表示方法不同，放在子类中。<br/>
	 * we may need to extend the result acore from a integer to a scope
	 * [low,high] like [-6, -9]
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

	List<TreeGoManual> manuals = new ArrayList<TreeGoManual>();

	public void outputSearchStatistics(Logger log) {
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {

			log.warn("Black expect: " + getMaxExp() + ", White expect:"
					+ getMinExp());
			log.warn("we calculate steps = " + countSteps);
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

	public SmallBoardGlobalSearch(int boardSize, int highestScore,
			int lowestScore) {
		super(highestScore, lowestScore);
		byte[][] board = new byte[boardSize + 2][boardSize + 2];
		int whoseTurn = Constant.BLACK;
		goBoard = new SmallGoBoard(
				BoardColorState.getInstance(board, whoseTurn));

		this.initLevel(whoseTurn);
	}

	// public SmallBoardGlobalSearch(byte[][] board) {
	// this(board, Constant.BLACK);
	// }

	public SmallBoardGlobalSearch(byte[][] boards, int highestScore,
			int lowestScore) {
		this(boards, Constant.BLACK, highestScore, lowestScore);
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
		this.initLevel(whoseTurn);
	}

	public SmallBoardGlobalSearch(BoardColorState state, int highestScore,
			int lowestScore) {
		super(highestScore, lowestScore);
		goBoard = new SmallGoBoard(state);
		this.initLevel(state.getWhoseTurn());
	}

	@Override
	public SearchLevel getInitLevel() {

		SearchLevel level = new SearchLevel(0, initTurn);

		// level 0: all candidates of original state.
		if (initTurn == Constant.BLACK) {
			level.setMax(true);
			level.setMaxExp(this.getMaxExp());
			level.setTempBestScore(Integer.MIN_VALUE);

		} else {
			level.setMax(false);
			level.setMinExp(this.getMinExp());
			level.setTempBestScore(Integer.MAX_VALUE);
		}
		return level;
	}

	private void initLevel(int whoseTurn) {
		initTurn = whoseTurn; // (brother)
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
		if (goBoard.isDoubleGiveup()) {
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

	@Override
	public GoBoard getGoBoard() {
		return goBoard;
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

	@Override
	public int getScore(BoardColorState boardColorState) {
		BoardColorState boardColorStateN = boardColorState.normalize();
		if (results.containsKey(boardColorStateN))
			return results.get(boardColorStateN).intValue();
		BoardColorState boardColorStateS = boardColorState.blackWhiteSwitch()
				.normalize();
		return 0 - results.get(boardColorStateS).intValue();

	}

}
