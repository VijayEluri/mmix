package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.TerritoryAnalysis;

/**
 * 1. only for small board (3<=size<=?).<br/>
 * 2. no copy of board, use backward/forward feature.
 * 
 * @author Eddie
 * 
 */
public class GoBoardSearch {
	private static final Logger log = Logger.getLogger(GoBoardSearch.class);

	TerritoryAnalysis goBoard;
	List<Level> levels = new ArrayList<Level>();
	// init state is in level 0
	private int levelIndex = 0;

	

	public GoBoardSearch(int boardSize) {
		goBoard = new TerritoryAnalysis(boardSize);

		// level 0: all candidates of original state.
		Level level = new Level(0, Constant.BLACK);
		level.setWhoseTurn(Constant.MAX);
		level.setHighestExp(9);
		level.setTempBestScore(Integer.MIN_VALUE);
		int color = Constant.BLACK;
		List<Point> candidates = getCandidate(color);
		level.setCandidates(candidates, color);
		levels.add(level);

	}

	/**
	 * reuse same structure to do life-death search. first we try big eye.
	 * target is whether the complete block with big eye can survive.
	 * 
	 * @param state
	 * @param boardSize
	 */
	public GoBoardSearch(byte[][] state) {
		goBoard = new TerritoryAnalysis(state);
	}

	// store candidates

	/**
	 * 禁止全局同型再现
	 */
	// Set<BoardColorState> knownState = new HashSet<BoardColorState>();

	public void globalSearch() {

		/**
		 * 2.开始计算。 第一层循环：展开最后一个局面。 <br/>
		 * decide to completely redo it.<br/>
		 * 有未确定状态才需要继续搜索.
		 */

		while (true) {
			Level level = levels.get(levelIndex);
			if (level.alreadyWin()) {
				int score = level.getTempBestScore();
				levels.remove(levelIndex--);
				level = levels.get(levelIndex);
				level.getChildScore(score);
				goBoard.oneStepBackward();
				continue;
			}

			Step step = level.getNextCandidate();

			if (step == null) {// all candidates are handled
				if (levelIndex == 0) {
					if (log.isDebugEnabled())
						log.debug("final score: " + level.getTempBestScore());
					break;
				} else {
					int score = level.getTempBestScore();
					levels.remove(levelIndex--);
					level = levels.get(levelIndex);
					level.getChildScore(score);
					goBoard.oneStepBackward();
					continue;
				}
			}

			// goBoard is in state of level above.
			if (log.isDebugEnabled())
				log.debug("mes");
			log.warn("state:");
			goBoard.printState();
			log.warn("choose step " + step + "at level " + level.getLevel());
			List<Step> candidates = level.getCandidates();
			log.warn("from " + candidates.size() + " candidate: ");
			for (Step candidate : candidates) {
				log.warn(candidate);
			}

			// 前进一步,进入新的level
			boolean valid = goBoard.oneStepForward(step);
			if (valid == false) {
				log.warn("invalid Step");
				// TODO 如全局再现.已经走了.需要回退.
				goBoard.oneStepBackward();
				continue;// ignore invalid candidate.666
			}
			/**
			 * 解决最终状态识别的问题.
			 * 
			 */
			boolean isTerminateState = isTerminateState();// goBoard.isFinalState();
			if (isTerminateState) {
				// FinalResult finalResult = goBoard.finalResult();
				level.getNeighborScore(getScore());

				log.warn("final state");
				goBoard.printState();
				goBoard.oneStepBackward();
				continue;
			}

			/**
			 * continue expanding
			 */
			int color = ColorUtil.enemyColor(level.getColor());
			List<Point> candidate = getCandidate(color);
			if (candidate.size() == 1
					&& candidate.contains(Point.getPoint(3, 3, 3))) {
				if (log.isDebugEnabled())
					log.debug("");
			}
			if (candidate.isEmpty()) {
				log.warn(color + " turn: no candidate");
				goBoard.printState();

				candidate = getCandidate(color);// ?
				// FinalResult finalResult = goBoard.finalResult_noCandidate();
				level.getNeighborScore(getScore());

				log.warn("final state");
				goBoard.printState();
				goBoard.oneStepBackward();

				continue;
				// continue;
				// throw new RuntimeException(color + ": no candidate"
				// + Arrays.deepToString(goBoard.getMatrixState()));
			}

			Level newLevel = new Level(++levelIndex, color);
			if (color == Constant.BLACK) {
				newLevel.setWhoseTurn(Constant.MAX);
				newLevel.setHighestExp(9);
				newLevel.setTempBestScore(Integer.MIN_VALUE);
			} else if (color == Constant.WHITE) {
				newLevel.setHighestExp(5);
				newLevel.setWhoseTurn(Constant.MIN);
				newLevel.setTempBestScore(Integer.MAX_VALUE);
			}
			newLevel.setCandidates(candidate, color);

			levels.add(newLevel);

		} // while

	}

	protected List<Point> getCandidate(int color) {
		return goBoard.getCandidate(color);
	}

	protected boolean isTerminateState() {
		return goBoard.isFinalState();
	}

	protected int getScore() {
		FinalResult finalResult = goBoard.finalResult();
		return finalResult.getScore();
	}
}
