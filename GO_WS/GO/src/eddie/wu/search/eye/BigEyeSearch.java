package eddie.wu.search.eye;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Shape;
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
 * <br/>
 * 
 * @author Eddie
 * 
 */
public class BigEyeSearch extends GoBoardSearch {
	/**
	 * by default, we search for live death. we must cover the potential best
	 * move including PASS.
	 */
	private boolean liveSearch = true;
	/**
	 * when searching for breath, we may need to PASS to ensure longest breath.
	 * 
	 */
	private boolean breathSearch = false;

	private static final Logger log = Logger.getLogger(BigEyeSearch.class);
	TerritoryAnalysis goBoard;
	Set<Point> candidates = new HashSet<Point>();
	Set<Point> enemyCandidates = new HashSet<Point>();

	private Point target;
	private int targetColor;
	private boolean targetFirst;

	Shape targetShape;
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
	public BigEyeSearch(byte[][] state, Point target, int targetColor,
			boolean targetFirst, boolean targetLoopSuperior) {
		super(RelativeResult.DUAL_LIVE, RelativeResult.ALREADY_DEAD);
		initGoBoard(state, target, targetColor, targetFirst);
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
		init(state, target, targetColor, eyePoints, targetFirst,
				targetLoopSuperior);
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
	public BigEyeSearch(byte[][] state, Point target, int targetColor,
			BlankBlock eyeBlock, boolean targetFirst, boolean targetLoopSuperior) {
		super(RelativeResult.DUAL_LIVE, RelativeResult.ALREADY_DEAD);
		// bug here. shared Set cause tricky bug!
		// Set<Point> eyePoints = eyeBlock.getPoints();
		initGoBoard(state, target, targetColor, targetFirst);
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

		init(state, target, targetColor, eyePoints, targetFirst,
				targetLoopSuperior);
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
	public BigEyeSearch(byte[][] state, Point target, int targetColor,
			Set<Point> eyePoints, boolean targetFirst,
			boolean targetLoopSuperior) {
		super(RelativeResult.DUAL_LIVE, RelativeResult.ALREADY_DEAD);
		initGoBoard(state, target, targetColor, targetFirst);
		init(state, target, targetColor, eyePoints, targetFirst,
				targetLoopSuperior);
	}

	/**
	 * latest idea: 2014-03-04 <br/>
	 * No Pass in usual search unless there is no valid candidate. <br/>
	 * if it looks like dual live, we come here. and divide it into two search
	 * with different color play first. <br/>
	 * when there is something looks like a loop. we should also try different
	 * loop parameter to see whether it is really a loop.<br/>
	 * //each
	 * 
	 * @param state
	 * @param target
	 * @param targetColor
	 * @param eyePoints
	 * @return
	 */
	public static int checkDualLive(byte[][] state, Point target,
			int targetColor, Set<Point> eyePoints) {
		BigEyeSearch targetFirst = new BigEyeSearch(state, target, targetColor,
				eyePoints, true, false);
		int first1 = targetFirst.globalSearch();
		BigEyeSearch targetFirst2 = new BigEyeSearch(state, target,
				targetColor, eyePoints, true, true);
		int first2 = targetFirst2.globalSearch();
		BigEyeSearch targetSecond = new BigEyeSearch(state, target,
				targetColor, eyePoints, false, false);
		int second1 = targetSecond.globalSearch();
		BigEyeSearch targetSecond2 = new BigEyeSearch(state, target,
				targetColor, eyePoints, false, true);
		int second2 = targetSecond2.globalSearch();
		if (first1 == RelativeResult.ALREADY_DEAD
				&& first2 == RelativeResult.ALREADY_DEAD) {

			if (second1 == RelativeResult.ALREADY_LIVE
					&& second2 == RelativeResult.ALREADY_LIVE) {
				return RelativeResult.DUAL_LIVE;
			} else if (second1 == RelativeResult.ALREADY_DEAD
					&& second2 == RelativeResult.ALREADY_LIVE) {
				if (true) {
					// only takes 0 times loop back.
					return RelativeResult.ALREADY_DEAD;
				} else {
					// maybe consider 万年劫.
				}
			}
		}
		return Constant.UNKOWN;
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
	public BigEyeSearch(byte[][] state, Point target, int targetColor,
			Set<Point> multiTarget, Set<Point> eyePoints, boolean targetFirst,
			boolean targetLoopSuperior) {
		super(RelativeResult.DUAL_LIVE, RelativeResult.ALREADY_DEAD);
		initGoBoard(state, target, targetColor, targetFirst);
		init(state, target, targetColor, multiTarget, eyePoints, targetFirst,
				targetLoopSuperior);
	}

	private Set<Point> multiTarget;

	private void init(byte[][] state, Point target, int targetColor,
			Set<Point> multiTarget, Set<Point> eyePoints, boolean targetFirst,
			boolean targetLoopSuperior) {
		initGoBoard(state, target, targetColor, targetFirst);
		init(state, target, targetColor, eyePoints, targetFirst,
				targetLoopSuperior);
		this.multiTarget = multiTarget;
	}

	public void initGoBoard(byte[][] state, Point target, int targetColor,
			boolean targetFirst) {
		this.target = target;
		this.targetColor = targetColor;
		int whoseTurn = 0;
		if (targetFirst) {
			whoseTurn = targetColor;
		} else {
			whoseTurn = ColorUtil.enemyColor(targetColor);
		}
		goBoard = new TerritoryAnalysis(state, whoseTurn);
		TestCase.assertEquals(targetColor, goBoard.getColor(target));

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
	public void init(byte[][] state, Point target, int targetColor,
			Set<Point> eyePoints, boolean targetFirst,
			boolean targetLoopSuperior) {
		this.targetLoopSuperior = targetLoopSuperior;
		// this.targetColor = targetColor;
		// if (targetFirst) {
		// goBoard = new TerritoryAnalysis(state, targetColor);
		// } else {
		// goBoard = new TerritoryAnalysis(state,
		// ColorUtil.enemyColor(targetColor));
		//
		// }

		Block targetBlock = goBoard.getBlock(target);

		candidates.addAll(eyePoints);

		// 破眼方在变化过程中可能紧外气。
		// enemyCandidates.addAll(eyePoints);
		enemyCandidates.addAll(targetBlock.getExternalOrSharedBreath());
		enemyCandidates.removeAll(eyePoints);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("init big eye search for state: ");
			log.warn(goBoard.getInitColorState().getStateString());
			log.warn("enemyCandidates = " + enemyCandidates);
		}

		this.target = target;
		this.targetShape = targetBlock.getShape();
		int color = goBoard.getColor(target);// 有眼块的颜色
		targetColor = color;

	}

	@Override
	public SearchLevel getInitLevel() {
		SearchLevel level;
		if (targetFirst == true) {
			level = new SearchLevel(0, targetColor);// 做眼方先下
			level.setMax(true);// 做眼方取最大值。
			level.setMaxExp(RelativeResult.DUAL_LIVE);
			level.setTempBestScore(RelativeResult.ALREADY_DEAD - 1);
		} else {
			int enemyColor = ColorUtil.enemyColor(targetColor);
			level = new SearchLevel(0, enemyColor);// 破眼方先下
			level.setMax(false);
			level.setMinExp(RelativeResult.ALREADY_DEAD);
			level.setTempBestScore(RelativeResult.ALREADY_LIVE + 1);
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
		if (score != Constant.UNKOWN) {
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
			/**
			 * only means live without two eyes.
			 */
			return RelativeResult.DUAL_LIVE;
		}

		if (goBoard.noStep() == false && goBoard.getLastStep().isPass()) {
			if (this.targetColor == goBoard.getLastStep().getColor()) {
				return Constant.UNKOWN;// only means without two eyes.
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
			} else if (goBoard.isRemovable_static(target) == true) {
				return RelativeResult.ALREADY_DEAD;
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
					// cause stack overflow.
					// boolean live = goBoard.isBigEyeLive_dynamic(targetBlock,
					// eyeBlock, false);
					// in case of bigEyeSearch.
					boolean live = goBoard.isStaticLive(target);

					if (live) {
						return RelativeResult.ALREADY_LIVE;
					}
				}

			}
		}
		// is there enough potential to live.

		return Constant.UNKOWN;// 仍是未定状态。
	}

	@Override
	protected List<Candidate> getCandidate(int color) {
		Point targetCopy = target;
		Set<Point> multiCopy = null;
		if (multiTarget != null) {
			multiCopy = new HashSet<Point>();
			multiCopy.addAll(multiTarget);
			for (Iterator<Point> iter = multiCopy.iterator(); iter.hasNext();) {
				Point targetT = iter.next();
				Block targetB = goBoard.getBlock(targetT);
				if (targetB == null || targetB.getColor() != this.targetColor) {
					if (log.isEnabledFor(org.apache.log4j.Level.WARN))
						log.warn("目标块already被提吃 " + targetT);
					iter.remove();
				}
			}
			if (goBoard.getBlock(target) == null) {
				targetCopy = multiCopy.iterator().next();
			}
		}

		Block targetBlock = goBoard.getBlock(targetCopy);
		boolean forTarget = color == targetBlock.getColor();

		// 目标方劫财有利。
		if (this.targetLoopSuperior) {
			if (forTarget)
				return goBoard.getCandidate_forTarget(multiCopy, targetCopy,
						candidates, color, forTarget, forTarget);
			else
				return goBoard.getCandidate_forAttacker(multiCopy, targetCopy,
						candidates, enemyCandidates, color, forTarget,
						forTarget);
		} else {
			if (forTarget)
				return goBoard.getCandidate_forTarget(multiCopy, targetCopy,
						candidates, color, forTarget, !forTarget);
			else
				return goBoard.getCandidate_forAttacker(multiCopy, targetCopy,
						candidates, enemyCandidates, color, forTarget,
						!forTarget);
		}
	}

	// @Override
	// protected int getMinExp() {
	// return RelativeResult.ALREADY_DEAD;
	// }
	//
	// @Override
	// protected int getMaxExp() {
	// return RelativeResult.DUAL_LIVE;
	// }

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
	protected void stateDecided(BoardColorState boardColorState,boolean max, int score,boolean win) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateFinalizeed(BoardColorState boardColorStateN,
			int scoreTerminator) {
		// TODO Auto-generated method stub

	}

//	@Override
//	public boolean isKnownState(BoardColorState boardColorState) {
//		// TODO Auto-generated method stub
//		return false;
//	}

//	@Override
//	public int getScore(BoardColorState boardColorState) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

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
