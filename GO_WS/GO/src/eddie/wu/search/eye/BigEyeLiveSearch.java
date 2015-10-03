package eddie.wu.search.eye;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.StepMemo;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.SearchLevel;
import eddie.wu.search.global.TerminalState;

/**
 * 二三眼的知识先固化在代码中或者实现输入，这里的搜索从四子的完整眼位开始。<br/>
 * 且二子眼的死活，各种状态都是确定的状态（）<br/>
 * 三子眼的死活，情况比较复杂，尽管技术上只是点眼而已。<br/>
 * 眼位刚刚出现是，很容易知道是大眼死活，一旦对方点入，不再是一个纯的大眼，如何识别呢？<br/>
 * <br/>
 * to make it simple, we only concern pure big eyes here. that means big eyes
 * with only one block. <br/>
 * 只处理最简单的模型. 假设外气都是可紧的, 即由攻击方的活棋控制. <br/>
 * 全局而言,也是先识别活棋,再识别死棋.应为活棋已定.而死棋还有变数. <br/>
 * 追求简单性优先.
 * 
 * @author Eddie
 * 
 */
public class BigEyeLiveSearch extends GoBoardSearch {

	private static final Logger log = Logger.getLogger(BigEyeLiveSearch.class);
	private static boolean liveSearch = true;
	TerritoryAnalysis goBoard;
	Set<Point> candidateScope = new HashSet<Point>();

	private Point target;
	private int targetColor;
	private boolean targetFirst;

	// Shape targetShape;
	private boolean targetLoopSuperior;

	/**
	 * Single Block Eye<br/>
	 * 处理独立大眼的情况.
	 * 
	 * @param state
	 * @param target
	 * @param targetFirst
	 * @param targetLoopSuperior
	 */
	public BigEyeLiveSearch(byte[][] state, Point target, boolean targetFirst,
			boolean targetLoopSuperior) {
		super(RelativeResult.DUAL_LIVE, RelativeResult.ALREADY_DEAD);
		initGoBoard(state, target, targetFirst);
		Block targetBlock = goBoard.getBlock(target);

		if (targetBlock.getBreathBlocks().size() != 1) {
			int count = 0;
			for (BlankBlock bb : targetBlock.getBreathBlocks()) {
				if (bb.isEyeBlock()) {
					count++;
				}
			}
			if (count > 1) {

				if (log.isEnabledFor(Level.WARN)) {
					log.warn("targetBlock has more than one eye blocks"
							+ targetBlock.getBehalfPoint());
					log.warn(goBoard.getStateString());
				}
				throw new RuntimeException(
						"targetBlock has more than one eye blocks");
			}
		}

		Set<Point> eyePoints = targetBlock.getAllPointsInSingleEye();
		init(state, target, eyePoints, targetFirst, targetLoopSuperior);
	}

	/**
	 * suppose only one eye block<br/>
	 * 处理独立大眼的情况.
	 * 
	 * @param state
	 * @param target
	 * @param eyeBlock
	 * @param targetFirst
	 * @param targetLoopSuperior
	 */
	public BigEyeLiveSearch(byte[][] state, Point target, int targetColor,
			BlankBlock eyeBlock, boolean targetFirst, boolean targetLoopSuperior) {
		super(RelativeResult.DUAL_LIVE, RelativeResult.ALREADY_DEAD);
		// bug here. shared Set cause tricky bug!
		// Set<Point> eyePoints = eyeBlock.getPoints();
		initGoBoard(state, target, targetFirst);
		Set<Point> eyePoints = new HashSet<Point>();
		eyePoints.addAll(eyeBlock.getPoints());
		if (eyeBlock.getNeighborBlocks().size() > 1) {
			for (Block block : eyeBlock.getNeighborBlocks()) {
				if (block.getPoints().contains(target)) {
					continue;
				}
				// other block maybe weak and captured.
				eyePoints.addAll(block.getPoints());

				// other shared breath block
				for (BlankBlock blankB : block.getBreathBlocks()) {
					if (blankB == eyeBlock)
						continue;
					eyePoints.addAll(blankB.getPoints());
				}
			}
		}

		init(state, target, eyePoints, targetFirst, targetLoopSuperior);
	}

	/**
	 * for big eyes with enemy filled in.<br/>
	 * 处理大眼中已经有敌子的情况.
	 * 
	 * @param state
	 * @param target
	 * @param eyePoints
	 * @param targetFirst
	 * @param targetLoopSuperior
	 */
	public BigEyeLiveSearch(byte[][] state, Point target, Set<Point> eyePoints,
			boolean targetFirst, boolean targetLoopSuperior) {
		super(RelativeResult.DUAL_LIVE, RelativeResult.ALREADY_DEAD);
		initGoBoard(state, target, targetFirst);
		init(state, target, eyePoints, targetFirst, targetLoopSuperior);
	}

	/**
	 * 处理多个目标棋块围出一个大眼的情况.
	 * 
	 * @param state
	 * @param target
	 *            核心target.
	 * @param eyePoints
	 * @param targetFirst
	 * @param targetLoopSuperior
	 */
	public BigEyeLiveSearch(byte[][] state, Point target,
			Set<Point> multiTarget, Set<Point> eyePoints, boolean targetFirst,
			boolean targetLoopSuperior) {
		super(RelativeResult.DUAL_LIVE, RelativeResult.ALREADY_DEAD);
		initGoBoard(state, target, targetFirst);
		init(state, target, multiTarget, eyePoints, targetFirst,
				targetLoopSuperior);
	}

	private Set<Point> multiTarget;

	private void init(byte[][] state, Point target, Set<Point> multiTarget,
			Set<Point> eyePoints, boolean targetFirst,
			boolean targetLoopSuperior) {
		initGoBoard(state, target, targetFirst);
		init(state, target, eyePoints, targetFirst, targetLoopSuperior);
		this.multiTarget = multiTarget;
	}

	public void initGoBoard(byte[][] state, Point target, boolean targetFirst) {
		this.target = target;
		this.targetFirst = targetFirst;
		// save one unnecessary parameter
		this.targetColor = state[target.getRow()][target.getColumn()];
		if (targetFirst) {
			goBoard = new TerritoryAnalysis(state);
		} else {
			int enemyColor = ColorUtil.enemyColor(targetColor);
			goBoard = new TerritoryAnalysis(state, enemyColor);
		}

	}

	/**
	 * target block has only one big eye block.<br/>
	 * 
	 * 有两种搜索，做眼和破眼。即对于一个局面的先后手结果。
	 * 
	 * @param state
	 * @param target
	 * @param targetFirst
	 *            是否目标方先下（己方做眼）？ 还是目标方后下（对方破眼）？
	 */
	public void init(byte[][] state, Point target, Set<Point> eyePoints,
			boolean targetFirst, boolean targetLoopSuperior) {
		this.targetLoopSuperior = targetLoopSuperior;
		candidateScope.addAll(eyePoints);
	}

	@Override
	public SearchLevel getInitLevel() {
		SearchLevel level;
		if (targetFirst == true) {
			level = new SearchLevel(0, targetColor, null);// 做眼方先下
			level.setMax(true);// 做眼方取最大值。
			level.setExpScore(RelativeResult.ALREADY_LIVE);
		} else {
			int enemyColor = ColorUtil.enemyColor(targetColor);
			level = new SearchLevel(0, enemyColor, null);// 破眼方先下
			level.setMax(false);
			level.setExpScore(RelativeResult.ALREADY_DEAD);
		}
		return level;
	}

	@Override
	protected TerminalState getTerminalState() {
		TerminalState ts = new TerminalState();
		// if (this.isTerminateState()) {
		// ts.setTerminalState(true);
		// ts.setScore(this.getScore());
		// } else {
		// ts.setTerminalState(false);
		// }
		int score = this.getScore();
		if (score != Constant.UNKNOWN) {
			ts.setTerminalState(true);
			ts.setScore(this.getScore());
		} else {
			ts.setTerminalState(false);
		}
		return ts;

	}

	protected boolean isTerminateState() {

		StepMemo lastStep = goBoard.getLastStep();
		// enemy played one stone
		if (lastStep.getColor() == ColorUtil.enemyColor(targetColor)) {

			/**
			 * 处理目标块被提吃的局面。
			 */
			if (goBoard.getBlock(target) == null) {
				if (log.isEnabledFor(org.apache.log4j.Level.WARN))
					log.warn("目标块被提吃 " + target);
				return true;
			} else {
				return false;// goBoard.isDead(target);
			}
		} else { // target played one step.

			if (lastStep.getEatenBlocks().size() == 1) {
				Block eatenBlock = lastStep.getEatenBlocks().iterator().next();
				// can it live after capturing.
				return goBoard.isBigEyeLive_dynamic(goBoard.getBlock(target),
						goBoard.getBlankBlock(eatenBlock.getBehalfPoint()),
						false);

			} else {
				return goBoard.isAlreadyLive_dynamic(target)
						|| goBoard.isDead(target);
			}

		}

	}

	protected int getScore() {
		// when one side give up, we can terminate and count the eyes.

		if (goBoard.areBothPass()) {
			boolean livePotential = goBoard.isDualLivePotential(target,
					candidateScope);
			if (livePotential) {
				/**
				 * only means live without two eyes.
				 */
				return RelativeResult.DUAL_LIVE;
			} else {
				/**
				 * attacker may pass due to lazy for external breath.<br/>
				 * defender may pass to avoid fill the eye.<br/>
				 * that means target does not reach two eyes, so dead.
				 */
				return RelativeResult.ALREADY_DEAD;
			}
		}

		if (goBoard.noStep() == false && goBoard.getLastStep().isPass()) {
			if (this.targetColor == goBoard.getLastStep().getColor()) {
				return Constant.UNKNOWN;// only means without two eyes.
			}
		}

		/**
		 * 处理目标块被提吃的局面。
		 */
		if (multiTarget == null) {
			Block targetBlock = goBoard.getBlock(target);
			if (targetBlock == null
					|| targetBlock.getColor() != this.targetColor) {
				if (log.isEnabledFor(org.apache.log4j.Level.WARN))
					log.warn("目标块被提吃 " + target);
				return RelativeResult.ALREADY_DEAD;
			}
			if (goBoard.isStaticLive(target) == true) {
				return RelativeResult.ALREADY_LIVE;
			} else if (goBoard.potentialEyeLive(target) == false) {
				// potentialEyeLive is not accurate!!
				// return RelativeResult.ALREADY_DEAD;
			}
		} else {
			Set<Point> multiCopy = new HashSet<Point>();
			multiCopy.addAll(multiTarget);
			for (Iterator<Point> iter = multiCopy.iterator(); iter.hasNext();) {
				Point targetT = iter.next();
				Block targetBlockT = goBoard.getBlock(targetT);
				if (targetBlockT == null
						|| targetBlockT.getColor() != this.targetColor) {
					if (log.isEnabledFor(org.apache.log4j.Level.WARN))
						log.warn("目标块被提吃 " + targetT);
					iter.remove();
				}
			}
			if (multiCopy.isEmpty()) {
				// are part are cleaned up.
				return RelativeResult.ALREADY_DEAD;
			}
			if (multiCopy.contains(target)) {

				if (goBoard.isStaticLive(target) == true) {
					return RelativeResult.ALREADY_LIVE;
				}
				if (goBoard.potentialEyeLive(multiCopy) == false) {
					return RelativeResult.ALREADY_DEAD;
				}
			} else {
				Point targetCopy = multiCopy.iterator().next();
				if (goBoard.isStaticLive(targetCopy) == true) {
					return RelativeResult.ALREADY_LIVE;
				}
				if (goBoard.potentialToLive(multiCopy) == false) {
					return RelativeResult.ALREADY_DEAD;
				}
			}
		}

		StepMemo lastStep = goBoard.getLastStep();
		// enemy played one stone
		if (multiTarget == null) {
			Block targetBlock = goBoard.getBlock(target);
			if (lastStep.getColor() == targetColor) {
				// target played one step.
				if (lastStep.getEatenBlocks().size() == 1) {
					Block eatenBlock = lastStep.getEatenBlocks().iterator()
							.next();
					// can it live after capturing.
					BlankBlock eyeBlock = goBoard.getBlankBlock(eatenBlock
							.getBehalfPoint());
					boolean live = goBoard.isBigEyeLive_dynamic(targetBlock,
							eyeBlock, false);
					if (live) {
						return RelativeResult.ALREADY_LIVE;
					}
				}

			}
		}
		// is there enough potential to live.

		return Constant.UNKNOWN;// 仍是未定状态。
	}

	/**
	 * @param color
	 *            whose turn!
	 */
	@Override
	protected void initCandidate(SearchLevel level, int color) {
		int targetColor = goBoard.getColor(target);
		boolean forTarget = color == targetColor;
		List<Candidate> candidates = null;
		if (forTarget) {
			candidates = goBoard.whoseTurn(target, candidateScope, true,
					targetLoopSuperior, liveSearch);
		} else {
			candidates = goBoard.whoseTurn(target, candidateScope, false,
					!targetLoopSuperior, liveSearch);
		}
		level.setCandidates(candidates);
	}

	@Override
	public GoBoard getGoBoard() {
		return goBoard;
	}

	// @Override
	// protected void printState() {
	// if (goBoard.boardSize > 10) {
	// goBoard.printState(targetShape);
	// } else {
	// goBoard.printState();
	// }
	// }

	@Override
	protected void stateDecided(BoardColorState boardColorState, boolean max,
			int score, boolean win) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateFinalizeed(BoardColorState boardColorStateN,
			int scoreTerminator) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public boolean isKnownState(BoardColorState boardColorState) {
	// // TODO Auto-generated method stub
	// return false;
	// }

	// @Override
	// public int getScore(BoardColorState boardColorState) {
	// // TODO Auto-generated method stub
	// return 0;
	// }

	public int getTargetColor() {
		return targetColor;
	}

	public int getEnemyColor() {
		return ColorUtil.enemyColor(targetColor);
	}

	public Point getTarget() {
		return target;
	}

	public boolean isTargetFirst() {
		return targetFirst;
	}
}
