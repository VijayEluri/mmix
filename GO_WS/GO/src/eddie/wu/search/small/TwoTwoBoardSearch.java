package eddie.wu.search.small;

import java.util.List;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.SearchLevel;

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
	private static final Logger log = Logger.getLogger(TwoTwoBoardSearch.class);

	public TwoTwoBoardSearch(BoardColorState state, int expScore) {
		super(state, expScore);
		// initKnownState();
	}

	public TwoTwoBoardSearch(BoardColorState state, int maxExp, int minExp) {
		super(state, maxExp, minExp);
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
		// String[] text = new String[2];
		// text[0] = new String("[B, _]");
		// text[1] = new String("[_, B]");
		// byte[][] state = StateLoader.LoadStateFromText(text);
		// this.stateFinalizeed(new BoardColorState(state, Constant.WHITE), 4);

		// this.results.put(, 4);
		// Not necessary, since only normalized one will be stored!
		// text[0] = new String("[_, B]");
		// text[1] = new String("[B, _]");
		// state = StateLoader.LoadStateFromText(text);
		// this.stateFinalizeed(new BoardColorState(state, Constant.WHITE), 4);
	}

	/**
	 * 同型禁现导致状态不足以决定结果,还要考虑到达该状态的历史过程.<br/>
	 * 对称的不同候选点可能一个是同型再现,而另外一个不是.这就有了差异.<br/>
	 * 解决方法是仅仅考虑初始的对称性。仅仅是当前局面有对称性，而历史没有对称性，是不够的。
	 */
	@Override
	protected void initCandidate(SearchLevel level, int color) {
		int expectedScore;
		if (color == Constant.BLACK) {
			expectedScore = getMaxExp();
		} else {
			expectedScore = getMinExp();
		}
		if (goBoard.getInitSymmetryResult().getNumberOfSymmetry() == 0) {
			level.setCandidates(goBoard.getCandidate(level, false, expectedScore));
		} else if (goBoard.getStepHistory().getAllSteps().isEmpty()) {
			// first move
			level.setCandidates(goBoard.getCandidate(level, true, expectedScore));
		} else if (goBoard.getStepHistory().noRealStep()) {
			// first real move
			level.setCandidates(goBoard.getCandidate(level, true, expectedScore));
		} else if (goBoard.getLastStep().getSymmetry().getNumberOfSymmetry() != 0) {
			level.setCandidates( goBoard.getCandidate(level, true, expectedScore));
		} else {
			level.setCandidates( goBoard.getCandidate(level, false, expectedScore));
		}
	}
}
