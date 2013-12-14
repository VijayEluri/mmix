package eddie.wu.search.global;

import java.util.List;

import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardLadder;
import eddie.wu.domain.Point;
import eddie.wu.domain.StepHistory;
import eddie.wu.domain.analy.TerritoryAnalysis;

/**
 * 新写的的征子计算，准确的说是一气吃的计算。
 * 
 * @author Eddie
 * 
 */
public class CaptureSearch extends GoBoardSearch {

	private static final Logger log = Logger.getLogger(CaptureSearch.class);

	GoBoardLadder goBoard;
	public static final int CAPTURE_SUCCESS = 128;
	public static final int CAPTURE_FAILURE = -128;
	public static final int UNKNOWN = 0;

	private Point target;

	private int targetColor;

	public CaptureSearch(byte[][] state, Point target, int targetColor,
			boolean targetFirst) {
		this.target = target;
		this.targetColor = targetColor;
		int whoseTurn;
		if (targetFirst == true) {
			whoseTurn = targetColor;
		} else {
			whoseTurn = ColorUtil.enemyColor(targetColor);
		}
		// int enemyColr = ColorUtil.enemyColor(targetColor);

		goBoard = new GoBoardLadder(BoardColorState.getInstance(state,
				whoseTurn));
		Block targetBlock = goBoard.getBlock(target);
		// level 0: all candidates of original state.
		SearchLevel level;
		List<Candidate> candidates;
		level = new SearchLevel(0, whoseTurn);
		if (targetFirst == false) {
			level.setWhoseTurn(Constant.MAX);// 征子方取最大值。
			level.setHighestExp(this.CAPTURE_SUCCESS);
			level.setTempBestScore(Integer.MIN_VALUE);
		} else {
			level.setWhoseTurn(Constant.MIN);// 被征子方取最小值
			level.setLowestExp(this.CAPTURE_FAILURE);
			level.setTempBestScore(Integer.MAX_VALUE);
		}
		candidates = getCandidate(whoseTurn);
		level.setCandidates(candidates, whoseTurn);
		levels.add(level);
	}

	

	@Override
	protected List<Candidate> getCandidate(int color) {

		return goBoard.getCaptureCandidate(target, color);

	}

	@Override
	protected TerminalState getTerminalState() {
		TerminalState ts = new TerminalState();
		boolean finalState= this.goBoard.isFinalState_capture(target, targetColor);
		ts.setTerminalState(finalState);
		if(finalState){
			ts.setScore(this.goBoard.getScore_capture(target, targetColor));
		}
		return ts;
	}
	

	@Override
	protected int getLowestExp() {
		return CAPTURE_FAILURE;
	}

	@Override
	protected int getHighestExp() {
		return this.CAPTURE_SUCCESS;
	}

	@Override
	public GoBoard getGoBoard() {
		return goBoard;
	}

	public boolean isDoubleGiveup() {

		return goBoard.isDoubleGiveup();
	}

	@Override
	void stateDecided(BoardColorState boardColorState, int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateFinalizeed(BoardColorState boardColorStateN,
			int scoreTerminator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isKnownState(BoardColorState boardColorState) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getScore(BoardColorState boardColorState) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	protected void printKnownResult() {
		// TODO Auto-generated method stub
		
	}
}
