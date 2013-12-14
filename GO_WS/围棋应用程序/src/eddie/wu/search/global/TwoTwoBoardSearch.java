package eddie.wu.search.global;

import java.util.List;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;

/**
 * 2*2的小棋盘真正的终局状态很少，变化中有很多循环。状态对应的结果和达到该状态的过程相关。<br/>
 * 需要特殊处理。<br/>
 * 围棋中常有殊途同归的情况,一般认为相同的局面(包括先后手也相同)具有相同的结果.<br/>
 * 但是在极端情况下,这一点是不能保证的.比如这个局面如果是出现在循环中,那么在不同的上下文中<br/>
 * 得到的结果是不一样的.所以不能简单的加以利用.<br/>
 * 这也会破坏各个选点之间的对称性,就是说对称的选点可以因为其中一个导致循环而不再等价.<br/>
 * 所以final state还要看是否是全局的final state,即可以在任何上下文中重用.<br/>
 * 小棋盘状态重用的可能小很多,因为大都涉及到循环.基本没有真正的活棋. <br/>
 * 
 * @author Eddie Wu
 * 
 */
public class TwoTwoBoardSearch extends SmallBoardGlobalSearch {

	public TwoTwoBoardSearch(byte[][] boards, int whoseTurn) {
		super(boards, whoseTurn);
		initKnownState();
	}

	public TwoTwoBoardSearch(byte[][] boards, int whoseTurn, int highestScore,
			int lowestScore) {
		super(boards, whoseTurn, highestScore, lowestScore);
		initKnownState();
	}

	/**
	 * the state is decided because of dual give up.
	 */
	@Override
	public void stateDecided(BoardColorState boardColorState, int score) {

	}

	private void initKnownState() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		this.stateFinalizeed(new BoardColorState(state, Constant.WHITE), 4);
		// this.results.put(, 4);
		// Not necessary, since only normalized one will be stored!
		// text[0] = new String("[_, B]");
		// text[1] = new String("[B, _]");
		// state = StateLoader.LoadStateFromText(text);
		// this.stateFinalizeed(new BoardColorState(state, Constant.WHITE), 4);
	}



	/**
	 * 同型禁现导致状态不足以决定结果,还要考虑到达该状态的历史过程.<br/>
	 * 对称的不同候选点可能一个是同型再现,而另外一个不是.这就有了差异.
	 */
	@Override
	protected List<Candidate> getCandidate(int color) {
		int expectedScore;
		if (color == Constant.BLACK) {
			expectedScore = this.highestScore;
		} else {
			expectedScore = this.lowestScore;
		}
		if (goBoard.getInitSymmetryResult().getNumberOfSymmetry() == 0) {
			return goBoard.getCandidate_smallBoard(color, false, expectedScore);
		} else if (goBoard.getStepHistory().getAllSteps().isEmpty()) {
			// first move
			return goBoard.getCandidate_smallBoard(color, true, expectedScore);
		} else if (goBoard.getStepHistory().noRealStep()) {
			// first real move
			return goBoard.getCandidate_smallBoard(color, true, expectedScore);
		} else if (goBoard.getLastStep().getSymmetry().getNumberOfSymmetry() != 0) {
			return goBoard.getCandidate_smallBoard(color, true, expectedScore);
		} else {
			return goBoard.getCandidate_smallBoard(color, false, expectedScore);
		}
	}

	public static int getAccurateScore_blackTurn(BoardColorState state) {
		int high = 1;
		int low = 0;
		int score = 0;
		int dir = 0;// direction to check further
		do {
			GoBoardSearch goS = new TwoTwoBoardSearch(state.getMatrixState(),
					state.getWhoseTurn(), high, low);
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
			GoBoardSearch goS = new TwoTwoBoardSearch(state.getMatrixState(),
					state.getWhoseTurn(), high, low);
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
