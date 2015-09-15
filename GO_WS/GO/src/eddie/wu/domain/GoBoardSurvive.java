package eddie.wu.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.search.old.Candidate;
import eddie.wu.search.old.LocalResult;

/**
 * 死活计算。
 */
public class GoBoardSurvive extends GoBoard {
	private transient static final Logger log = Logger
			.getLogger(GoBoardSurvive.class);

	public GoBoardSurvive(BoardColorState boardColorState, int shoushu) {
		super(boardColorState);
	}

	public GoBoardSurvive(BoardColorState state) {
		super(state);
	}

	public GoBoardSurvive(byte[][] state) {
		super(state);
	}

	/**
	 * 
	 * @param toSurvive
	 * @return
	 */
	public LocalResult getLocalResultSelfFirst(Point toSurvive) {
		LocalResult result = new LocalResult();

		Block blockToSurvive = getBlock(toSurvive);
		// breath point may not cover all the candidates.eg. 板六。
		Set<BlankBlock> breathBlocks = blockToSurvive.getBreathBlocks();
		Set<Point> candidates = new HashSet<Point>();
		for (BlankBlock tempBlock : breathBlocks) {
			if (tempBlock.isEyeBlock()) {
				candidates.addAll(tempBlock.getPoints());
			} else {// 点入的子也会和目标快形成公气，但是又与原先的外气（也很可能是公气）不同。

			}
		}
		if (log.isInfoEnabled()) {
			log.info("考虑的可能下法有:" + candidates);
		}

		int myColor = blockToSurvive.getColor();
		// 遍历并且评价候选点,事后评价.需要落子.
		for (Point tempPoint : candidates) {
			GoBoardSurvive temp = this.getGoBoardSurviveCopy();
			// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
			// 以后应该进行更细致的处理，根据要展开的局面确定。

			// 扩展最后的局面，扩展同一个上级局面。
			if (temp.validate(tempPoint, myColor)) { // 判断合法着点
				temp.oneStepForward(tempPoint, myColor);
				byte[][] matrixState = temp.getBoardColorState()
						.getMatrixState();
				SurviveAnalysis survive = new SurviveAnalysis(matrixState);
				if (survive.isAlreadyLive_dynamic(tempPoint)) {
					result.setScore(127);//
					Candidate candidate = new Candidate();
					candidate.setGoBoard(temp);
					candidate.setPoint(tempPoint);
					result.setCandidate(candidate);
					return result;
				} else {
					int huKouShu = new StateAnalysis(matrixState).huKouShu(
							toSurvive, tempPoint);
					Candidate candidate = new Candidate();
					candidate.setGoBoard(temp);
					candidate.setPoint(tempPoint);
					result.addCandidate(candidate);
				}
			}
		}
		// 返回，一般是做活方无子可下。
		if (result.countCandicates() == 0) {
			if (log.isDebugEnabled()) {
				log.debug("有效点为0");
			}
			result.setScore(-128);
		}

		return result;
	}

	/**
	 * 破眼方有两个方面来考察候选点的优先级。 1. 点眼之子的气数。<br/>
	 * 2. 考虑做眼一方下于同处所形成的虎口数目。<br/>
	 * 后者可能更反映出着手的本质。
	 * 
	 * @param toSurvive
	 *            有眼的一方.
	 * @return
	 */
	public LocalResult getLocalResultEnemyFirst(Point toSurvive) {
		LocalResult result = new LocalResult();

		Block blockToSurvive = getBlock(toSurvive);
		// breath point may not cover all the candidates.eg. 板六。
		Set<BlankBlock> breathBlocks = blockToSurvive.getBreathBlocks();
		Set<Point> candidates = new HashSet<Point>();
		for (BlankBlock tempBlock : breathBlocks) {
			candidates.addAll(tempBlock.getPoints());
		}
		if (log.isInfoEnabled()) {
			log.info("考虑的可能下法有:" + candidates);
		}

		int myColor = blockToSurvive.getColor();
		int enemyColor = ColorUtil.enemyColor(myColor);

		// 遍历并且评价候选点,事后评价.需要落子.
		for (Point moveCandidate : candidates) {
			GoBoardSurvive temp = this.getGoBoardSurviveCopy();
			// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
			// 以后应该进行更细致的处理，根据要展开的局面确定。

			// 扩展最后的局面，扩展同一个上级局面。
			if (temp.validate(moveCandidate, enemyColor)) { // 判断合法着点
				temp.oneStepForward(moveCandidate, enemyColor);
				byte[][] matrixState = temp.getBoardColorState()
						.getMatrixState();
				SurviveAnalysis survive = new SurviveAnalysis(matrixState);
				if (survive.isAlreadyLive_dynamic(toSurvive)) {
					result.setScore(127);// live
					Candidate candidate = new Candidate();
					candidate.setGoBoard(temp);
					candidate.setPoint(moveCandidate);
					result.setCandidate(candidate);
					return result;
				} else {
					// option 1: get the number of hukou, and eyes after play
					// one step.
					// if(this.getBlock(tempPoint).getBreaths())

					// option 2:
					int huKouShu = new StateAnalysis(matrixState).huKouShu(
							toSurvive, moveCandidate);
					Candidate candidate = new Candidate();
					candidate.setGoBoard(temp);
					candidate.setPoint(moveCandidate);
					candidate.setPriority(huKouShu);
					result.addCandidate(candidate);
				}
			}
		}
		// 返回，一般是破眼方无子可下。
		if (result.getCandidates().size() == 0) {
			if (log.isDebugEnabled()) {
				log.debug("有效点为0");
			}
			result.setScore(127);// live
		}

		return result;
	}

	public GoBoardSurvive getGoBoardSurviveCopy() {
		GoBoardSurvive temp = new GoBoardSurvive(this.getBoardColorState(),
				this.getShoushu());
		temp.setStepHistory(this.getStepHistory().getCopy());
		// temp.generateHighLevelState();
		// temp.lastPoint = this.lastPoint;
		return temp;
	}

}
