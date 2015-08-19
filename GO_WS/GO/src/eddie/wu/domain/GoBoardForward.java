package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.TreeGoManual;

/**
 * 这里主要实现前进一步的功能，要点是保证数据结构的一致性。<br/>
 * the code of one step forward feature.<br/>
 * 类层次结构中，所有的数据初始化都在GoBoard中。父类只负责操作这些数据。
 * 
 * @author Eddie
 * 
 */
public class GoBoardForward extends GoBoardSymmetry {

	private transient static final Logger log = Logger
			.getLogger(GoBoardForward.class);
	protected Point lastPoint;
	private StepHistory stepHistory = new StepHistory();
	private boolean checkForward = false;
	private BoardStatistic statistic = new BoardStatistic();

	// count how many moved we have forwarded, used in search
	private long forwardMoves = 0;

	public GoBoardForward(int boardSize) {
		super(boardSize);
		this.stepHistory.colorStates.add(initColorState);
	}

	public GoBoardForward(BoardColorState colorState) {
		super(colorState);

		this.stepHistory.colorStates.add(colorState);
		// this.stepHistory.setColorState(initColorState);
	}

	/**
	 * statistic should be updated in each step (forward or backward).
	 * 
	 * @return
	 */
	public BoardStatistic getStatistic() {
		// BoardStatistic statistic = new BoardStatistic();

		return statistic;
	}

	public void setStepHistory(StepHistory stepHistory) {
		this.stepHistory = stepHistory;
	}

	/**
	 * natural color decision. each side make one move in turn.
	 * 
	 * @return
	 */
	public int decideColor() {
		if (initColorState != null
				&& initColorState.getWhoseTurn() == Constant.WHITE) {
			return ColorUtil.getNextStepColor(shoushu + 1);
		} else {
			return ColorUtil.getNextStepColor(shoushu);
		}
	}

	public boolean oneStepForward(final Point point) {
		int color = this.decideColor();
		return oneStepForward(point, color);
	}

	/**
	 * BUG fix: not depend on lastPoint, which is not set properly when one step
	 * backward.
	 * 
	 * @return
	 */
	public Point getLastPoint() {
		if (noStep())
			return null;
		return this.getLastStep().getStep().getPoint();
		// return lastPoint;
	}

	public boolean oneStepForward(Point original, int selfColor) {
		lastPoint = original;
		Step step = new Step(original, selfColor);
		return this.oneStepForward(step);
	}

	/**
	 * @deprecated
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean oneStepForward(final int row, final int column) {
		int selfColor = ColorUtil.getCurrentStepColor(shoushu + 1);
		lastPoint = Point.getPoint(boardSize, row, column);
		return oneStepForward(lastPoint, selfColor);
	}

	/**
	 * accept input and finish all the dealing. <br/>
	 * 命名是一个很难的课题， 命名方法的重要性可以 通过名字的历史性来看出，<br/>
	 * 即使可以全局替换， 由于名字可能已经在别处被引用，问题复杂化了。 常规处理，已经判明是合法着点；
	 * 可以接受的输入为(row,column)或c;c=column*19+row-19;<br/>
	 * 完成数气提子 row是数组的行下标,也是平面的横坐标:1-19 <br/>
	 * column是数组的列下标,也是屏幕的纵坐标:1-19 <br/>
	 * byte c; a,b的一维表示:1-361;
	 */
	public boolean oneStepForward(final Step step) {
		// maintain all the variants.

		Point original = step.getPoint();
		int selfColor = step.getColor();

		if (log.isInfoEnabled()) {
			log.info("进入方法oneStepForward() 落子点:" + step);
		}

		if (validate(step) == false) { // 1.判断落子点的有效性。
			if (log.isDebugEnabled()) {
				log.debug("落子点无效:invalid");
			}
			return false;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("落子点有效:valid");
			}
		}

		String prefix = "Forward: ";
		if (current.getStep() == null) {
			prefix += "INIT";
		} else {
			prefix += current.getStep().toNonSGFString();
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(prefix + "-->" + step.toNonSGFString());

		SearchNode child = current.getChild(step);
		if (child == null) {

			SearchNode temp = new SearchNode(step);
			current.addChild(temp);
			current = temp;

		} else {
			current = child;
		}

		forwardMoves++;
		if (step.isPass()) {
			this.pass(step.getColor());
			return true;
		}
		// record oldState for duplicates
		BoardColorState oldState = this.getBoardColorState();

		byte jubutizishu = 0; // 局部提子数(每一步的提子数)
		shoushu++;
		step.setIndex(shoushu);

		lastPoint = original;
		stepHistory.setStep(shoushu, original, selfColor);

		// get state first before change color for "original"
		SimpleNeighborState neighborState = getNeighborState(original,
				selfColor);

		// 先改变该点的颜色,但是Block仍旧没有改变.
		this.setColor(original, selfColor);

		BlankBlock blankBlock = neighborState.getOriginalBlankBlock();
		this.isConnectedWithoutPoint(neighborState, blankBlock, original);

		this.getLastStep().setNeighborState(neighborState);
		if (log.isEnabledFor(Level.WARN))
			log.warn(neighborState.toString());
		/**
		 * 先处理将要消失的气块和棋块，将他们的相邻关系改为单向。 <br/>
		 * (顺序应该没有影响。)
		 */
		if (neighborState.isEating()) {
			for (Block block : neighborState.getEatenBlocks()) {
				if (log.isDebugEnabled()) {
					log.debug("detach eatenBlock" + block.getBehalfPoint()
							+ "from its enemy");
				}
				block.dettachFromEnemy();
				if (log.isDebugEnabled()) {
					log.debug("detach eatenBlock" + block.getBehalfPoint()
							+ "from its breath");
				}
				block.dettachFromBreath();
				block.setActive(false);
			}
		}

		if (neighborState.isFriendBlockMerged()) {
			for (Block block : neighborState.getFriendBlocks()) {
				if (log.isDebugEnabled()) {
					log.debug("dettach mergedBlock" + block.getBehalfPoint()
							+ "from enemy");
				}
				block.dettachFromEnemy();
				if (log.isDebugEnabled()) {
					log.debug("detach mergedBlock" + block.getBehalfPoint()
							+ "from breath block");
				}
				block.dettachFromBreath();
				block.setActive(false);

			}
		}

		if (neighborState.isOriginalBlankBlockDisappear()) {
			BlankBlock originalBlankBlock = neighborState
					.getOriginalBlankBlock();
			if (log.isDebugEnabled()) {
				log.debug("detach disappear blank Block"
						+ originalBlankBlock.getBehalfPoint() + "from neighbor");
			}
			originalBlankBlock.dettachFromNeighbor();
			originalBlankBlock.setActive(false);
		}

		/**
		 * 生成新的棋块，比如将邻块并入，但是气数尚未计算<br/>
		 * (现在直接维护相邻关系,已经无需计算气数)。
		 */
		Block sameColorBlock = dealSelfPoint(neighborState);

		/**
		 * 空白块可能分裂成多个气块。比如成为眼。
		 */
		dealBlankPoint(neighborState);

		sameColorBlock.removeBreathPoint(original);

		// 记录真正增加的气数。
		if (!neighborState.isFriendBlockMerged()
				&& !neighborState.isNewSinglePointBlock()) {
			for (Point breath : neighborState.getBlankPoints()) {
				if (!sameColorBlock.getBreathPoints().contains(breath)) {
					neighborState.getAddedBreaths().add(breath);
				} else {
					if (log.isInfoEnabled())
						log.info("original block already has breath " + breath);
				}
			}
		}
		sameColorBlock.getBreathPoints().addAll(neighborState.getBlankPoints());

		// boardPoints[row][column].setColor(selfColor); //可以动态一致

		/**
		 * 处理周围的敌子
		 */
		jubutizishu = dealEnemyPoint(neighborState, sameColorBlock);

		// 将局部提子计入
		if (shoushu % 2 == ColorUtil.BLACK) {
			statistic.addNumberOfWhitePointEaten(jubutizishu);
		} else {
			statistic.addNumberOfBlackPointEaten(jubutizishu);
		}

		this.verifyBreath(sameColorBlock);

		/**
		 * 计算sameColorBlock的breathBlock情况.<br/>
		 * 似无必要，若块分裂，则已经有相邻信息，若未分裂，则原棋块和气块已经相邻，除非是新生成的棋块<br/>
		 * 
		 */

		for (Delta delta : Constant.ADJACENTS) {
			Point tmp = original.getNeighbour(delta);
			if (tmp == null)
				continue;
			if (this.getColor(tmp) == ColorUtil.BLANK) {
				// bi-direction relationship is maintained.
				sameColorBlock.addBreathBlock_twoWay(this.getBlankBlock(tmp));
			}
		}

		neighborStateStatistic(neighborState);

		dealJie(original, jubutizishu, stepHistory.getLastStep());

		calculateWeakestBlock(sameColorBlock);

		if (step.isLoopSuperior()) {
			// fix a bug here. we may repeat all the steps.
			this.getLastStep().getStep().setLoopSuperior(step.isLoopSuperior());
			if (log.isEnabledFor(Level.WARN))
				log.warn("模拟找了绝对劫材之后提回。避开全局同型检查。");
		} else {
			// prevent duplicate state.
			boolean valid = stepHistory.setColorState(getBoardColorState());
			if (valid == false) {
				// ensure previous state is identified as loop relevant.
				stepHistory.setColorState(oldState);
				if (log.isEnabledFor(Level.WARN)) {
					log.warn("落子点无效:全局同型再现!" + this.getLastStep().getStep());
					log.warn(this.getStateString());
					for (StepMemo stepMemo : this.getStepHistory().allSteps) {
						log.warn(stepMemo);
					}
				}
				// external code to roll back one step.
				// stepHistory.removeStep(shoushu--);
				// this.on
				return false;
			}
		}

		/**
		 * 对称性的改变<br/>
		 * I believe it is only necessary for 2*2 board. but I cannot prove it.
		 * let's see.
		 */
		if (this.boardSize == 2) {
			if (this.initSymmetryResult.getNumberOfSymmetry() != 0) {
				int steps = this.getStepHistory().getAllSteps().size();
				if (steps == 1) {
					// first move
					this.getLastStep().setSymmetry(
							this.getSymmetryResult().getCopy());
					this.getLastStep().getSymmetry().and(initSymmetryResult);
				} else if (steps == 0) {
					if (log.isEnabledFor(Level.WARN))
						log.warn("steps=" + steps);
					throw new RuntimeException("steps =0  after one step!");
				} else {

					StepMemo secondLastStep = this.getSecondLastStep();

					if (secondLastStep.getSymmetry() == null) {
						if (log.isEnabledFor(Level.WARN))
							log.warn("steps=" + steps);
						for (StepMemo memo : this.getStepHistory()
								.getAllSteps()) {
							if (log.isEnabledFor(Level.WARN)) {
								if (memo.getSymmetry() != null)
									log.warn(memo.getStep()
											+ memo.getSymmetry().toString());
								else {
									log.warn(memo.getStep() + "null");
								}
							}
						}
						throw new RuntimeException(
								"(secondLastStep.getSymmetry()==null)");
					} else if (secondLastStep.getSymmetry()
							.getNumberOfSymmetry() != 0) {
						this.getLastStep()
								.setSymmetry(this.getSymmetryResult());
						this.getLastStep().getSymmetry()
								.and(secondLastStep.getSymmetry().getCopy());
					} else {// already no symmetry!
						// safe copy is necessary to prevent shared change.
						this.getLastStep().setSymmetry(
								secondLastStep.getSymmetry().getCopy());
					}
					log.warn(getSecondLastStep().getStep()
							+ getSecondLastStep().getSymmetry().toString());
					log.warn(getLastStep().getStep()
							+ getLastStep().getSymmetry().toString());
				}
			} else {
				this.getLastStep().setSymmetry(initSymmetryResult.getCopy());
			}
		}
		// 新的棋块和原先气块的相邻程度改变
		if (log.isInfoEnabled()) {
			log.info("退出方法oneStepForward().");
			log.info(this.getLastStep() + "\r\n");
			this.output();

			if (neighborState.isOriginalBlankBlockDisappear()) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(neighborState.getOriginalBlankBlock());
			}
			if (neighborState.isEating()) {
				if (log.isEnabledFor(Level.WARN))
					log.warn("eatenBlock");
				for (Block block : neighborState.getEatenBlocks()) {
					if (log.isEnabledFor(Level.WARN))
						log.warn(block);
				}
			}
			if (neighborState.isFriendBlockMerged()) {
				if (log.isEnabledFor(Level.WARN))
					log.warn("mergedBlock");
				for (Block block : neighborState.getFriendBlocks()) {
					if (log.isEnabledFor(Level.WARN))
						log.warn(block);
				}
			}
		}
		String message = "after one step forward" + original;
		if (Constant.INTERNAL_CHECK == true) {
			check_internal(message);
			if (checkForward == true) {

				check(message);
			}
		}
		return true;
	}

	public long getForwardMoves() {
		return forwardMoves;
	}

	int[] caseCount = new int[18];
	int[] selfStoneCaseCount = new int[3];
	int[] blankStoneCaseCount = new int[3];
	int[] enemyStoneCaseCount = new int[2];

	private void neighborStateStatistic(SimpleNeighborState neighborState) {
		selfStoneCaseCount[neighborState.getSelfSonteCaseNumber()] += 1;
		blankStoneCaseCount[neighborState.getBlankStoneCaseNumber()] += 1;
		enemyStoneCaseCount[neighborState.getEnemyStoneCaseNumber()] += 1;
		caseCount[neighborState.getCaseNumber()] += 1;
	}

	public void printNeighborStateStatistic() {
		if (log.isEnabledFor(Level.WARN))
			log.warn("self Stone " + Arrays.toString(selfStoneCaseCount));
		if (log.isEnabledFor(Level.WARN))
			log.warn("blank Stone " + Arrays.toString(blankStoneCaseCount));
		if (log.isEnabledFor(Level.WARN))
			log.warn("enemy Stone " + Arrays.toString(enemyStoneCaseCount));
		if (log.isEnabledFor(Level.WARN))
			log.warn("overall" + Arrays.toString(caseCount));
	}

	public int[] getCaseCount() {
		return caseCount;
	}

	/**
	 * ensure the consistency internally.
	 */
	protected void check_internal(String messageIn) {
		if (Constant.INTERNAL_CHECK == false)
			return;
		if (log.isEnabledFor(Level.WARN))
			log.warn(messageIn);
		for (BasicBlock basicBlock : this.getAllBlocks()) {
			if (basicBlock.isActive() == false) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(basicBlock);
				TestCase.assertTrue(
						"all block should be active: "
								+ basicBlock.getBehalfPoint(),
						basicBlock.active);
			}
			/**
			 * 块中的点确实指向该块
			 */
			for (Point point : basicBlock.allPoints) {
				TestCase.assertEquals("check point" + point,
						this.getBasicBlock(point), basicBlock);
			}

			if (basicBlock.isBlank()) {
				BlankBlock blankBlock = (BlankBlock) basicBlock;

				// 防止相邻敌块的数据是旧的块
				for (Block neighborBlock : blankBlock.getNeighborBlocks()) {
					if (neighborBlock.isActive() == false) {

						boolean result = true;
						for (Point point : neighborBlock.allPoints) {
							if (neighborBlock != this.getBasicBlock(point)) {
								result = false;
								if (result == false) {
									if (log.isEnabledFor(Level.WARN))
										log.warn("check block: " + blankBlock);
									if (log.isEnabledFor(Level.WARN))
										log.warn("its neighorBlock"
												+ neighborBlock
												+ " is wrong for point" + point);
									if (log.isEnabledFor(Level.WARN))
										log.warn("correct one"
												+ this.getBasicBlock(point));
								}
								TestCase.assertEquals(
										"check "
												+ basicBlock.getBehalfPoint()
												+ " neighborBlock: wrong at point"
												+ point, getBasicBlock(point),
										neighborBlock);
								break;
							}

						}
						TestCase.assertTrue(
								"all neighbor block should be active: ",
								neighborBlock.active);
					}

				}

			} else {// 黑白块
				Block block = ((Block) basicBlock);
				Set<BlankBlock> breathBlocks = new HashSet<BlankBlock>();
				breathBlocks.addAll(block.getBreathBlocks());
				for (Point breath : block.getBreathPoints()) {
					breathBlocks.remove(this.getBasicBlock(breath));
					if (block.getBreathBlocks().contains(
							this.getBasicBlock(breath)) == false) {
						String message = "no breath block for breath" + breath
								+ "at block" + block;
						if (log.isEnabledFor(Level.WARN))
							log.warn(message);
						throw new RuntimeException(message);
					}
				}
				if (breathBlocks.isEmpty() == false) {
					// 防止breathBlocks中有多余的块。
					if (log.isEnabledFor(Level.WARN))
						log.warn(breathBlocks);
					throw new RuntimeException("Extra blank block "
							+ breathBlocks.iterator().next() + "at Block "
							+ basicBlock.getBehalfPoint());
				}
				// 防止相邻敌块的数据是旧的块
				for (Block enemyBlock : block.getEnemyBlocks()) {
					boolean result = true;
					for (Point point : enemyBlock.allPoints) {
						if (enemyBlock != this.getBasicBlock(point)) {
							result = false;
							if (result == false) {
								if (log.isEnabledFor(Level.WARN))
									log.warn("check block: " + block);
								if (log.isEnabledFor(Level.WARN))
									log.warn("its enemyBlock" + enemyBlock
											+ " is wrong for point" + point);
							}
							TestCase.assertEquals("check enemyBlock's point"
									+ point, enemyBlock, getBasicBlock(point));
							break;
						}

					}

				}
			}
		}
		debugIngernalDataStructure_beforeForward(this.getBoardColorState());
		if (log.isEnabledFor(Level.WARN))
			log.warn("Internal check succede.");
	}

	public void debugIngernalDataStructure_beforeForward(BoardColorState copy) {
		GoBoardForward goBoard = new GoBoardForward(copy);
		for (Point point : Point.getAllPoints(boardSize)) {
			boolean equals = BasicBlock.equals(this.getBasicBlock(point),
					goBoard.getBasicBlock(point));
			if (equals == false) {
				log.error(goBoard.getBoardColorState().getStateString());
				log.error(goBoard.getBasicBlock(point));
				log.error("Point=:" + point);
				log.error(this.getBoardColorState().getStateString());
				log.error(this.getBasicBlock(point));

			}
			TestCase.assertTrue(equals);
		}
	}

	@Override
	public BoardColorState getBoardColorState() {
		if (this.stepHistory.getAllSteps().isEmpty()) {
			return this.initColorState;
		}

		BoardColorState boardState;
		int color = this.getLastStep().getColor();
		int whoseTurn = ColorUtil.enemyColor(color);
		boardState = BoardColorState.getInstance(this.getColorArray(),
				whoseTurn);
		return boardState;
	}

	public BoardColorState getInitColorState() {
		return initColorState;
	}

	/**
	 * 打劫时的虚拟劫材应对。 注意悔棋时需要对应处理。
	 * 
	 * @deprecated not used yet!
	 * @return
	 */
	public boolean oneVirtualStepInLoop(int color) {
		shoushu++;
		// statistic.increaseGiveUpSteps();
		StepMemo step = new StepMemo(null, color);
		// step.setStep(step2);
		this.stepHistory.addStep(step);
		BoardColorState colorState = this.getBoardColorState();
		this.stepHistory.increateVirtualStepInLoop();
		this.stepHistory.setColorState(colorState);
		return true;
	}

	/**
	 * 落子于(row,column)后处理该点原先所在的气块. <br/>
	 * RECORD STEP LOCATION AND NEARBY BLANK POINT. <br/>
	 * 判断是否有新气块生成。 <br
	 * /. before forward one step. the original blank point block.<br/>
	 * 先将气块分裂.并更新和相邻块的相邻关系. 这些相邻块即使将来需要合并也很简单. ,<br/>
	 * (被分裂的气块尽量不变的保存下来,以利于将来悔棋/后退.)
	 * 
	 * @param origianl
	 *            原落子点
	 */
	private void dealBlankPoint(SimpleNeighborState state) {
		Point original = state.getOriginal();
		BlankBlock blankBlock = state.getOriginalBlankBlock();
		if (state.isOriginalBlankBlockDivided() == false) {
			this.getLastStep().setOriginalBlankBlock(blankBlock);

			if (state.isOriginalBlankBlockDisappear()) {
				TestCase.assertTrue(blankBlock.getPoints().size() == 1);
				if (log.isInfoEnabled())
					log.info("原气块仅有一点,落子后气块消失. ");
				// state.isOriginalBlankBlockDisappear();
				for (Block neighbourBlock : blankBlock.getNeighborBlocks()) {
					// remove the breath from neighbor.
					neighbourBlock.removeBreathPoint(original);

				}
				// 消失的气块没有一个点，显示时需要特殊处理。??
				// 消失的气块尽量保持原样。
				// blankBlock.getPoints().remove(original);
				return;
			}
			blankBlock.removePoint(original);
			// 气块没有分裂,且尚有余子,则必须动态维护空白点.

			// for (Delta delta : Constant.ADJACENTS) {
			// Point nb = original.getNeighbour(delta);
			// if (nb == null)
			// continue;
			// // 原先通过着子点和空白块相邻的棋块/敌块 现在可能不再相连.
			// if (getColor(nb) == state.getEnemyColor()) {
			// removeBreathPoint(this.getBlock(nb), state);
			// }
			// }
			return;
		}

		/**
		 * 气块将发生分裂.先从相邻棋块中删除原始棋块.在气块分裂的过程中,会建立和相邻棋块的新的关系.
		 */

		// else {
		boolean removed = false;
		// do not remove the point from the disappeared block.
		// boolean removeSuccess = blankBlock.removePoint(original);
		this.realDivideBlankPointBlock(state);
		this.stepHistory.getLastStep().setOriginalBlankBlock(blankBlock);

	}

	/**
	 * bug fix 20050509 虽然用了集合，直接删除气点即可。无须判断相邻点是否同块。 <br/>
	 * 但是用直接减一气的方式导致错误。<br/>
	 * deal with points with different color(enemy point)
	 * 
	 * @param row
	 * @param column
	 * @param enemyColor
	 * @param jubutizishu
	 * @param tkd
	 * @param sameColorBlock
	 * @return
	 */
	private byte dealEnemyPoint(SimpleNeighborState state, Block sameColorBlock) {
		if (log.isInfoEnabled()) {
			log.info("三、处理异色邻子");
		}

		byte jubutizishu = 0;
		Point point = state.getOriginal();
		for (Block enemyBlock : state.getEnemyBlocks()) {
			if (!state.isFriendBlockMerged() && !state.isNewSinglePointBlock()) {
				if (!sameColorBlock.getEnemyBlocks().contains(enemyBlock)) {
					state.getConnectedEnemyBlocks().add(enemyBlock);
				} else {
					if (log.isInfoEnabled())
						log.info("original block already neighbor to"
								+ enemyBlock.getBehalfPoint());
				}
			}
			// reuse same method for friend block.
			removeBreathPointFromEnemyBlock(enemyBlock, state);

			int enemyBreath = enemyBlock.getBreaths();
			if (enemyBreath == 0) {
				Block eatenBlock = enemyBlock;
				jubutizishu += eatenBlock.getNumberOfPoint(); // 实际的提子数
				// if (log.isInfoEnabled()) {
				// log.info("块被吃，behalf point is ："
				// + eatenBlock.getTopLeftPoint());
				// }
				this.getStepHistory().getStep(shoushu - 1)
						.addEatenBlock(eatenBlock);
				dealEatenBlock(point, eatenBlock);
				// TODO:生成新的二级块，周围的块有指针指向该二级块。
			} else if (enemyBreath < 0) {

				log.error("气数错误:kin=" + enemyBlock.getTopLeftPoint());

				throw new RuntimeException("气数错误<0");
			} else {

				sameColorBlock.addEnemyBlock_twoWay(enemyBlock);
			}

		}

		return jubutizishu;
	}

	/**
	 * @param row
	 * @param column
	 * @param m1
	 * @param n1
	 */
	private void dealEatenBlock(Point original, Block eatenBlock) {

		for (Block block : eatenBlock.getEnemyBlocks()) {
			// keep the single direction from eaten block to its enemy.
			block.removeEnemyBlock_oneWay(eatenBlock);
		}

		BlankBlock blankBlock = new BlankBlock();
		blankBlock.setEyeBlock(true);
		blankBlock.getPoints().addAll(eatenBlock.getPoints());
		for (Point point : eatenBlock.getPoints()) {
			this.setBlock(point, blankBlock);
			setColor(point, ColorUtil.BLANK);
		}
		if (log.isDebugEnabled()) {
			log.debug("吃子后形成新的气块；" + eatenBlock.getBehalfPoint());
		}
		// boardPoints[m1][n1].getBlock().changeColorToBlank();
		this.getBlock(original).addBreathBlock_twoWay(blankBlock);

		for (Point blankPoint : blankBlock.getPoints()) {
			for (Delta delta : Constant.ADJACENTS) {
				Point neighbor = blankPoint.getNeighbour(delta);
				if (neighbor == null)
					continue;
				BoardPoint neighborBP = this.getBoardPoint(neighbor);
				if (neighborBP.getColor() == eatenBlock.getEnemyColor()) {
					if (log.isDebugEnabled()) {
						log.debug("add breath"
								+ blankPoint
								+ "for "
								+ (neighborBP.getBlock().isActive() ? "Active"
										: "Inactive") + "block:"
								+ neighborBP.getBlock().getTopLeftPoint());
					}
					neighborBP.getBlock().addBreathPoint(blankPoint);
					neighborBP.getBlock().addBreathBlock_twoWay(blankBlock);

				}
			}
		}
	}

	/**
	 * deal with point with same color.<br/>
	 * 处理同色邻子<br/>
	 * 1. 如果没有同色邻子,则形成单子新块.<br/>
	 * 2. 如果只有一个邻块,则将落子点并入.<br/>
	 * 3. 否则多个邻块合并成新块(此时的旧块尽量原样保存,仍旧指向他们原先的邻块,但是原先的邻块<br/>
	 * 不再指向他们.<br/>
	 * .
	 * 
	 * @param row
	 * @param column
	 * @param selfColor
	 * @return
	 */
	private Block dealSelfPoint(SimpleNeighborState neighborState) {
		if (log.isInfoEnabled()) {
			log.info("一、处理同色邻子");
		}
		Point point = neighborState.getOriginal();
		final int selfColor = neighborState.getFriendColor();

		int friendBlocks = neighborState.getFriendBlockNumber();
		Block sameColorBlock = null;

		if (neighborState.isFriendBlockMerged()) {
			if (log.isInfoEnabled()) {
				log.info("一.1 建立初始新子块, " + friendBlocks + " 块被合并。");

			}
			sameColorBlock = new Block(selfColor);
			this.setBlock(point, sameColorBlock);

			for (Block friendBlock : neighborState.getFriendBlocks()) {
				log.info("Merge Block " + friendBlock.getBehalfPoint());
				this.getLastStep().addMergedBlock(friendBlock);
				// 将旧块从周围气块中脱出.
				// for (BlankBlock breathBlock : friendBlock.getBreathBlocks())
				// {
				// if (breathBlock.removeNeighborBlock_oneWay(friendBlock) ==
				// false)
				// throw new RuntimeException(
				// "Return false when remove block"
				// + friendBlock.getBehalfPoint()
				// + "from breathblock");
				// }
				// 将旧块从周围敌块中脱出
				// for (Block enemyBlock : friendBlock.getEnemyBlocks()) {
				// enemyBlock.removeEnemyBlock_oneWay(friendBlock);
				// }

				// change Color For AllPoints in friendBlock
				for (Point pointTemp : friendBlock.getPoints()) {
					this.getBoardPoint(pointTemp).setBlock(sameColorBlock);
					sameColorBlock.allPoints.addAll(friendBlock.allPoints);
					sameColorBlock.getBreathPoints().addAll(
							friendBlock.getBreathPoints());
				}

				friendBlock.changeEnemyBlockTo(sameColorBlock);
				// 我们仍旧有单向指向消失块的气块。
				for (BlankBlock bb : friendBlock.getBreathBlocks()) {
					if (neighborState.isOriginalBlankBlockDisappear()
							&& bb == neighborState.getOriginalBlankBlock()) {
						continue;
					}
					sameColorBlock.addBreathBlock_twoWay(bb);
				}

			}
			return sameColorBlock;
			// 块的气数小于周围气块的最小值是危险的，当然还要考虑
			// 是否有长气的手段。
		} else {
			if (friendBlocks == 1) {
				/**
				 * ** 仅在唯一的同色块中增加一子,不会增加相邻气块,<br>
				 * 因为已经和该气块相邻(或者该气块消失)
				 * 
				 * 可能增加相邻敌子块.
				 */
				sameColorBlock = neighborState.getUniqueFriendBlock();
				this.setBlock(point, sameColorBlock);
				if (log.isInfoEnabled()) {
					log.info("一.2 合并到唯一的相邻子块中。"
							+ sameColorBlock.getBehalfPoint());
				}
				// return sameColorBlock;

			} else if (friendBlocks == 0) {
				sameColorBlock = new Block(selfColor);
				this.setBlock(point, sameColorBlock);
				if (log.isInfoEnabled()) {
					log.info("一.3 建立单子新子块。");
				}
				sameColorBlock.getBreathPoints().addAll(
						neighborState.getBlankPoints());
				// return sameColorBlock;
			}

			// 增加新块的相邻敌子块.处理敌块是有这一步。
			// for (Block enemyBlock : neighborState.getEnemyBlocks()) {
			// enemyBlock.addEnemyBlock_twoWay(sameColorBlock);
			// }
			// 如果气块不分裂，则相邻关系已经有。如果分裂，在后面的气块分裂算法中维护相邻关系。

			return sameColorBlock;//
		}
	}

	/**
	 * @return Returns the stepHistory.
	 */
	public StepHistory getStepHistory() {
		return stepHistory;
	}

	/**
	 * 过程中有打劫
	 * 
	 * @return
	 */
	public boolean hasLoopInHistory() {
		for (StepMemo memo : stepHistory.allSteps) {
			if (memo.getProhibittedPoint() != null)
				return true;
		}
		return false;
	}

	/**
	 * @deprecated only used in old code path.
	 */
	public void giveUp() {
		if (this.noStep()) {
			if (this.getInitColorState().getWhoseTurn() == Constant.BLACK)
				pass(Constant.BLACK);
			else {
				pass(Constant.WHITE);
			}
		} else {
			pass(ColorUtil.enemyColor(this.getLastStep().getColor()));
		}
	}

	/**
	 * fix a bug here: forget to store state after pass, so failed to check same
	 * state happens again.(2013/04/28)
	 * 
	 * @param color
	 */
	public void pass(int color) {
		shoushu++;
		statistic.increaseGiveUpSteps();
		StepMemo step = new StepMemo(null, color, shoushu);
		step.getStep().setIndex(shoushu);
		if (this.boardSize == 2) {
			if (noStep()) {// first move
				step.setSymmetry(this.initSymmetryResult.getCopy());
			} else {
				if (this.getLastStep().getSymmetry() == null) {

					for (StepMemo memo : this.getStepHistory().getAllSteps()) {
						if (log.isEnabledFor(Level.WARN)) {
							if (memo.getSymmetry() != null)
								log.warn(memo.getStep()
										+ memo.getSymmetry().toString());
							else {
								log.warn(memo.getStep() + "null");
							}
						}
					}
					throw new RuntimeException("(LastStep.getSymmetry()==null)");
				} else {
					step.setSymmetry(this.getLastStep().getSymmetry().getCopy());
				}
			}
		}
		// step.setStep(step2);
		this.stepHistory.addStep(step);
		// prevent other way to the current state..
		// TODO:
		boolean valid = stepHistory.setColorState(getBoardColorState());
		if (valid == false) {
			if (log.isEnabledFor(Level.WARN)) {
				log.warn("允许弃权导致的全局同型再现!");
				log.warn(this.getStateString());
				for (StepMemo stepMemo : this.getStepHistory().allSteps) {
					log.warn(stepMemo);
				}
			}

		}
		//
		// SearchNode child = current.getChild(step);
		// if (child == null) {
		//
		// SearchNode temp = new SearchNode(step);
		// current.addChild(temp);
		// current = temp;
		//
		// } else {
		// current = child;
		// }
	}

	/**
	 * 为了判断出局面的循环.只有下了之后才能判断.暂时没有想到可以预先判断的算法.
	 * 
	 * @return
	 */

	private boolean isStateSimilar() {
		// stepHistory.hui[shoushu][]
		if (ColorUtil.getCurrentStepColor(shoushu) == ColorUtil.BLACK) {
			if (this.getTotalPoints() < stepHistory
					.getMaxTotalPointsAfterBlack())
				return true;
		} else if (ColorUtil.getCurrentStepColor(shoushu) == ColorUtil.WHITE) {
			if (this.getTotalPoints() < stepHistory
					.getMaxTotalPointsAfterWhite())
				return true;
		}
		// stepHistory.getMaxTotalPoints()
		return false;
	}

	/**
	 * @return
	 */
	private short getTotalPoints() {

		return (short) (shoushu - statistic.getNumberOfBlackPointEaten()
				- statistic.getNumberOfWhitePointEaten() - statistic
					.getGiveUpSteps());
	}

	/**
	 * 考虑劫 (将打劫的处理结果记录于history，便于利用)
	 * 
	 * @param row
	 * @param column
	 * @param jubutizishu
	 */
	private void dealJie(Point point, byte jubutizishu, StepMemo step) {
		if (getBlock(point).getBreaths() == 1 && jubutizishu == 1
				&& getBlock(point).getNumberOfPoint() == 1) {
			Set<BlankBlock> breathBlocks = getBlock(point).getBreathBlocks();
			if (breathBlocks.size() == 1) {
				// 下一步该点不能下，即打劫的禁着点，目前是气点.
				Point tempBreathPoint = ((breathBlocks.iterator().next()))
						.getUniquePoint();
				step.setProhibittedPoint(tempBreathPoint);
				step.setProhibittedColor(ColorUtil.enemyColor(getColor(point)));
				// BoardPoint temp = this.getBoardPoint(tempBreathPoint);
				// temp.setProhibitStep((shoushu + 1),
				// ColorUtil.enemyColor(getColor(point)));
				// // 将互为打劫的两点联系在一起.
				// this.getBoardPoint(point).setTwinForKo(tempBreathPoint);
				// temp.setTwinForKo(point);

			} else {
				if (log.isDebugEnabled()) {
					log.debug("error of jie");
				}
				throw new RuntimeException("error of jie");
			}

		}
	}

	public void dealStrongAndWeak() {
		// Set blankPointBlock=new HashSet();
		// for(Iterator iter=blankPointBlock.iterator();iter.hasNext();){
		// //for (i = 1; i <= qikuaishu; i++) { //从气块入手，找到强块。
		// short meikuaizishu = qikuai[i].zishu;
		// if (meikuaizishu == 1) { //眼位
		// //四周必然有子。
		// m = qikuai[qikuaishu].zichuang.a;
		// n = qikuai[qikuaishu].zichuang.b;
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		//
		// if (zb[m1][n1][ZTXB] == WHITE) {
		// qkxlbzs++;
		// }
		// else if (zb[m1][n1][ZTXB] == BLACK) {
		// qkxlhzs++;
		// }
		// }
		// if (qkxlbzs == 0) { //黑方的眼
		// qikuai[i].color = Constant.BLACK;
		// if (qikuaizuixiaoqi(i) > 1) {
		// //所成的眼不会立即被破坏。
		// erjikuaishu++;
		// erjikuai[erjikuaishu] = new ErJiKuai();
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		// if (zb[m1][n1][ZTXB] == BLACK) {
		// erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
		// }
		//
		// }
		//
		// }
		// else {
		// //成气块的棋块本身处于被打吃状态。
		// }
		//
		// }
		// else if (qkxlhzs == 0) { //白方的眼，并成一体。
		// qikuai[i].color = Constant.WHITE;
		// if (qikuaizuixiaoqi(i) > 1) {
		// erjikuaishu++;
		// erjikuai[erjikuaishu] = new ErJiKuai();
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		// if (zb[m1][n1][ZTXB] == BLACK) {
		// erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
		// }
		//
		// }
		//
		// }
		// else {
		// //todo
		// }
		//
		// }
		// else { //双方的公气。
		// qikuai[i].color = 5;
		// }
		// //qikuai[qikuaishu--].zichuang=null;
		// //qikuai[qikuaishu].qichuang
		// //qikuai[ki--][1][1]=0;
		// //zb[j][i][KSYXB]=0;//非块
		// //todo:眼位的处理
		// //找出周围块，气数为一且单子，则打劫。两处为一气，
		// //则双提，块为一气，则打多还一、、
		// }
		// else if (meikuaizishu > 1) { //大眼
		// // qikuai[qikuaishu].zishu = meikuaizishu;
		// DianNode1 tee = qikuai[i].zichuang;
		// for (short hh = 1; hh <= meikuaizishu; hh++) {
		// m = tee.a;
		// n = tee.b;
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		//
		// if (zb[m1][n1][ZTXB] == WHITE) {
		// qkxlbzs++;
		// }
		// else if (zb[m1][n1][ZTXB] == BLACK) {
		// qkxlhzs++;
		// }
		// }
		// tee = tee.next;
		// }
		// if (qkxlbzs == 0) { //黑方的大眼
		// //周围的块数，越少越好。
		// if (qikuaizuixiaoqi(i) > 1) {
		// erjikuaishu++;
		// erjikuai[erjikuaishu] = new ErJiKuai();
		//
		// for (short hh = 1; hh <= meikuaizishu; hh++) {
		// m = tee.a;
		// n = tee.b;
		//
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		// if (zb[m1][n1][ZTXB] == BLACK) {
		// qikuai[i].addzikuaihao(zbk[m1][n1]);
		// erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
		// }
		//
		// }
		// }
		// }
		// }
		// else if (qkxlhzs == 0) { //白方的大眼，并成一体。
		// if (qikuaizuixiaoqi(i) > 1) {
		// erjikuaishu++;
		// erjikuai[erjikuaishu] = new ErJiKuai();
		//
		// for (short hh = 1; hh <= meikuaizishu; hh++) {
		// m = tee.a;
		// n = tee.b;
		//
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		// if (zb[m1][n1][ZTXB] == WHITE) {
		// qikuai[i].addzikuaihao(zbk[m1][n1]);
		// erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
		// }
		//
		// }
		// }
		// }
		//
		// }
		// else { //双方的公气。
		//
		// }
		//
		// }
		// else {
		// if (CS.DEBUG_CGCL) {
		// log.debug("error:zishu<1");
		//
		// }
		// }
		//
		// }

	}

	public StepMemo getLastStep() {
		return this.getStepHistory().getLastStep();
	}

	public StepMemo getSecondLastStep() {
		return this.getStepHistory().getSecondLastStep();
	}

	/**
	 * @param sameColorBlock
	 */
	private void calculateWeakestBlock(Block sameColorBlock) {
		// TODO Auto-generated method stub

	}

	/**
	 * iterate the point nearby <br>
	 * 算出块的气，并完成块的所有信息：气数和气串。 现在气数可以从棋子的相邻关系直接达到.<br/>
	 * 算气的方法仅用于验证而已.
	 * 
	 * @deprecated
	 * @param block
	 * @return
	 */
	protected void verifyBreath(Block block) {
		Set<Point> breaths = new HashSet<Point>();
		breaths.addAll(block.getBreathPoints());

		verifyFlag();
		if (log.isDebugEnabled()) {
			log.debug("进入方法：验证块气verifyBreath()");
			log.debug(block);

		}
		block.clearBreath();
		for (Point point : block.getPoints()) {
			for (Delta delta : Constant.ADJACENTS) {
				Point nb = point.getNeighbour(delta);
				if (nb == null)
					continue;
				BoardPoint bp = this.getBoardPoint(nb);
				if (bp.getColor() == ColorUtil.BLANK
						&& bp.isCalculatedFlag() == false) {
					bp.setCalculatedFlag(true);
					if (log.isDebugEnabled()) {
						log.debug("add breath " + nb + "for block:"
								+ block.getTopLeftPoint());
					}
					block.addBreathPoint(nb);
					// block.addBreathBlock_twoWay(this.getBlankBlock(nb));
				}
			}

		}

		// 恢复标志
		for (Point point : block.getBreathPoints()) {
			log.debug("restore flag of" + point);
			this.getBoardPoint(point).setCalculatedFlag(false);
		}
		if (log.isDebugEnabled()) {
			log.debug("块" + block.getBehalfPoint() + "的气数为："
					+ block.getBreaths());
			log.debug("方法jskq：返回");
		}
		if (!breaths.equals(block.getBreathPoints())) {
			this.errorState();
			if (log.isEnabledFor(Level.WARN)) {
				log.warn(block);
				log.warn("original breath of " + block.getBehalfPoint()
						+ " is " + breaths);
				log.warn("new calculated breath is" + block.getBreathPoints());
			}
			throw new RuntimeException("气数计算有误!");
		}

	} // 2月22日改,原方法虽妙,仍只能忍痛割爱.

	/**
	 * 落子后导致原先的棋块分裂.
	 * 
	 * @param original
	 * @param blankPoints
	 *            at most four point.
	 */
	private void realDivideBlankPointBlock(SimpleNeighborState state) {
		this.verifyFlag();
		int countPoint = 0;
		Point original = state.getOriginal();
		Set<Point> blankPoints = state.getBlankPoints();
		BlankBlock originalBlankBlock = state.getOriginalBlankBlock();
		int numberOfBlankPoints = originalBlankBlock.getNumberOfPoint();

		BlankBlock newBlank = null;
		int countNewBlank = 0;
		for (Point blankPoint : blankPoints) {
			BoardPoint bp = this.getBoardPoint(blankPoint);
			// bug fix for number inconsistent begin!
			if (bp.isCalculatedFlag()) {
				continue;
			}// bug fix for number inconsistent end!

			newBlank = new BlankBlock();
			this.getLastStep().addNewBlankBlock(newBlank);
			countNewBlank++;
			makeBlankPointBlock(blankPoint, newBlank);

			countPoint += newBlank.getNumberOfPoint();
			// maintain another direction relationship after blank block
			// collected all the points.
			// for (Block block : newBlank.getAllBlocks()) {
			// block.getBreathBlocks().add(newBlank);
			// }

			if (countPoint == numberOfBlankPoints - 1) {
				break;
			}
		}

		if (countNewBlank > 1) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("New blank block generated:"
						+ newBlank.getTopLeftPoint());
		} else if (countPoint != numberOfBlankPoints - 1) {
			this.printState();
			for (int ii = 1; ii <= boardSize; ii++) {
				for (int jj = 1; jj <= boardSize; jj++) {
					if (getBoardPoint(ii, jj).getColor() == ColorUtil.BLANK) {
						log.debug("ii=" + ii + ",jj=" + jj + "state="
								+ getBoardPoint(ii, jj).isCalculatedFlag());
					}
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("shoushu=" + shoushu);
				log.debug("zishujishu=" + countPoint);
			}
			log.debug("numberOfBlankPoints=" + numberOfBlankPoints);

			for (Point tmp : originalBlankBlock.allPoints) {
				if (newBlank.getPoints().contains(tmp) == false) {
					if (log.isDebugEnabled())
						log.debug("Not included: " + tmp + " played at point "
								+ original);
				}
			}
			throw new RuntimeException(
					"realDivideBlankPointBlock error:shoushu=" + shoushu
							+ " zishujishu=" + countPoint
							+ " numberOfBlankPoints=" + numberOfBlankPoints);
		}
		this.clearFlag();
	}

	/**
	 * Whether the block will be disconnected if the point is occupied. put in
	 * another way, whether the blank block will be divided if next play is at
	 * point.
	 * 
	 * old logic(divideBlankPointBlock) may be merged to improve performance in
	 * case of big blank block.
	 * 
	 * @param point
	 * @return
	 */
	public boolean isConnectedWithoutPoint(SimpleNeighborState state,
			BlankBlock blankBlock, Point point) {
		if (state.getBlankPoints().isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("直接气数为0，没有新气块生成。旧气块将消失。");
			}
			state.setOriginalBlankBlockDisappear(true);
			return true;
		} else if (state.getBlankPoints().size() == 1) {
			if (log.isDebugEnabled()) {
				log.debug("直接气数为1，没有新块生成。");
			}
			return true;
		} else if (this.isBlankBlockConnected(state)) {
			return true;
		}

		this.verifyFlag();
		BoardPoint bp = this.getBoardPoint(point);
		// because the color has already be changed.
		// bp.setCalculatedFlag(true);// mark it out.

		int count = 0;

		// if (blankBlock.getAllPoints().size() <= 2)
		// return true;

		// for (Point blankP : blankBlock.getPoints()) {
		// // prevent the first point happen to the played point.
		// if (blankP.equals(point) == false) {
		// blankPoint = blankP;
		// this.getBoardPoint(blankPoint).setCalculatedFlag(true);
		// count++;
		// break;
		// }
		// }

		// count += this._countBlankBlock(blankPoint);
		// use played point is wrong, you cause the result that the blank block
		// is never devided.
		// bp = this.getBoardPoint(point);
		// bp.setCalculatedFlag(true);// mark it out.

		// 取任一空白点来尝试生成块
		Point blankPoint = state.getBlankPoints().iterator().next();
		this.getBoardPoint(blankPoint).setCalculatedFlag(true);
		count++;
		count += this._countBlankBlock(blankPoint);
		if (log.isEnabledFor(Level.WARN))
			log.warn("check breath block connnectivity count = " + count
					+ "; expected = " + (blankBlock.getNumberOfPoint() - 1));
		this.clearFlag();
		if (count == blankBlock.getNumberOfPoint() - 1) {
			return true;
		} else {
			state.setOriginalBlankBlockDisappear(true);
			state.setOriginalBlankBlockDivided(true);
			return false;
		}
	}

	/**
	 * 落子后,落子点周围的敌块是否仍和原气块相邻
	 * 
	 * @param block
	 *            落子点周围的棋块.可以是同色己方块（不可能，）,亦可以是异色敌方块.
	 * @param original
	 *            落子点
	 * @return
	 */
	private void removeBreathPointFromEnemyBlock(Block block,
			SimpleNeighborState state) {
		// 落子点原来所在的气块.
		Point original = state.getOriginal();
		BlankBlock blankBlock = state.getOriginalBlankBlock();
		block.removeBreathPoint(original);

		if (state.isOriginalBlankBlockDivided()) {
			return;
		} else if (state.isOriginalBlankBlockDisappear()) {
			TestCase.assertTrue(blankBlock.getNumberOfPoint() == 1);
			return; // neighbor relationship is already handled in blank point
		}

		for (Point point : block.getBreathPoints()) {
			if (this.getBlankBlock(point) == blankBlock) {
				return; // still connected, need not update relationship.
			}
		}

		state.getDisconnectedEnemyBlocks().add(block);
		// not connected. update relationship with
		if (blankBlock.isActive() && block.isActive()) {
			blankBlock.removeNeighborBlock_twoWay(block);
			if (log.isInfoEnabled()) {
				log.info("block " + block.getBehalfPoint()
						+ "no longer connected to blankBlock "
						+ blankBlock.getBehalfPoint());
			}
		}
	}

	/**
	 * 落子后导致原先的空白块可能分裂.<br/>
	 * 复杂的处理主要是为了避免不必要的分裂棋块的尝试。尤其在开局有一个非常大的原始棋块的情况下。<br/>
	 * 但是可能处理的过头了，细节过多。
	 * 
	 * 
	 * @param blankPoint
	 *            original: the original point--step location<br/>
	 *            List[0...n-1]: .... neighbors breath of original point. blank
	 *            point
	 * @return whether new block is created true if connected, false is not sure
	 *         / unknown
	 */
	private boolean isBlankBlockConnected(SimpleNeighborState state) {

		Point original = state.getOriginal();
		List<Point> blankPoint = new ArrayList<Point>(4);
		blankPoint.addAll(state.getBlankPoints());

		byte row = original.getRow();
		byte column = original.getColumn();

		/* 11月22日，first处理气块的生成(divide) */
		byte m1, n1, m2, n2, m3, n3, x, y;

		// caller ensure its size >=2
		switch ((blankPoint.size())) {
		case 2: {
			if (log.isDebugEnabled()) {
				log.debug("直接气数为2，");
			}
			m1 = blankPoint.get(1).getRow();
			n1 = ((Point) blankPoint.get(1)).getColumn();
			m2 = ((Point) blankPoint.get(0)).getRow();
			n2 = ((Point) blankPoint.get(0)).getColumn();
			if (m1 == m2 || n1 == n2) { // 各自算。有无新块

				if (log.isDebugEnabled()) {
					log.debug("气点同轴。");
				}
				return false;
			} else {
				if (log.isDebugEnabled()) {
					log.debug("气点不同轴，");
				}
				x = (byte) (m1 + m2 - row);
				y = (byte) (n1 + n2 - column);
				if (getBoardPoint(x, y).getColor() == ColorUtil.BLANK) {
					if (log.isDebugEnabled()) {
						log.debug("对角点为空，没有新块生成。");
					}
					return true;
				} else if (jgzs(x, y) == 8) {
					if (log.isDebugEnabled()) {
						log.debug("对角点周围空，没有新块生成。");
					}
					return true;
				}
				return false;
			}

		}
		case 3: {
			byte lianjieshu = 0;
			m1 = ((Point) blankPoint.get(1)).getRow();
			n1 = ((Point) blankPoint.get(1)).getColumn();
			m2 = ((Point) blankPoint.get(2)).getRow();
			n2 = ((Point) blankPoint.get(2)).getColumn();
			m3 = ((Point) blankPoint.get(0)).getRow();
			n3 = ((Point) blankPoint.get(0)).getColumn();
			if (m1 == m2 || n1 == n2) {

			} else {
				x = (byte) (m1 + m2 - row);
				y = (byte) (n1 + n2 - column);
				if (getBoardPoint(x, y).getColor() == ColorUtil.BLANK) {
					lianjieshu++;
				} else if (jgzs(x, y) == 8) {
					lianjieshu++;
				}

			}
			if (m1 == m3 || n1 == n3) {

			} else {
				x = (byte) (m1 + m3 - row);
				y = (byte) (n1 + n3 - column);
				if (getBoardPoint(x, y).getColor() == ColorUtil.BLANK) {
					lianjieshu++;

				} else if (jgzs(x, y) == 8) {
					lianjieshu++;

				}

			}
			if (m2 == m3 || n2 == n3) {

			} else {
				x = (byte) (m2 + m3 - row);
				y = (byte) (n2 + n3 - column);
				if (getBoardPoint(x, y).getColor() == ColorUtil.BLANK) {
					lianjieshu++;
				} else if (jgzs(x, y) == 8) {
					lianjieshu++;
				}

			}
			if (lianjieshu >= 2) {
				if (log.isDebugEnabled()) {
					// log.debug()
					log.debug("没有新气块生成。");

				}
				return true;
			} else {
				return false;
			}

		}
		case 4: {
			byte lianjieshu = 0;
			for (Delta delta : Constant.SHOULDERS) {
				Point nb = original.getNeighbour(delta);
				if (nb == null)
					continue;
				else if (this.getColor(nb) == ColorUtil.BLANK) {
					lianjieshu++;
					// 通过对角点连接
				} else if (jgzs(nb.getRow(), nb.getColumn()) == 8) {
					lianjieshu++;
					// 通过九宫连接
				}
			}

			if (lianjieshu >= 3) {
				if (log.isDebugEnabled()) {
					log.debug("直接气数为4，连接数为3，没有新块生成。");
				}
				return true;
			} else {
				return false;
			}

		}

		}
		return true;

	}

	private byte jgzs(int m, int n) {
		return jiugongzishu(Point.getPoint(boardSize, m, n));
	}

	/**
	 * calculate whether two blank point will connect in an 3*3 part. (how many
	 * breath a point have?)
	 * 
	 * @param m
	 * @return
	 */
	private byte jiugongzishu(Point original) { // 九宫子数。
		// m,n为九宫中心点。（中心点不计入）
		byte blankCount = 0; // 气数变量
		// byte i, a, b; // 悔棋恢复时，解散块所成单点的气数计算；

		for (Delta delta : Constant.ADJACENTS) {
			Point nb = original.getNeighbour(delta);
			if (nb == null)
				continue;
			if (this.getColor(nb) != ColorUtil.BLANK)
				blankCount++;
		}

		for (Delta delta : Constant.SHOULDERS) {
			Point nb = original.getNeighbour(delta);
			if (nb == null)
				continue;
			if (this.getColor(nb) != ColorUtil.BLANK) {
				blankCount++;
			}
		}

		return blankCount;

	}

	/**
	 * 因为形势判断调用时无需每次调用都清除标志，所以 函数本身不清除标志。
	 * 
	 * @param blankPoint
	 * @param newBlock
	 */
	private void makeBlankPointBlock(Point blankPoint, BlankBlock newBlock) {

		BoardPoint bp = this.getBoardPoint(blankPoint);
		bp.setCalculatedFlag(true);
		bp.setBlankBlock(newBlock);
		// newBlock.addPoint(blankPoint);
		for (Delta delta : Constant.ADJACENTS) {
			Point neighbourPoint = blankPoint.getNeighbour(delta);
			if (neighbourPoint == null)
				continue;
			bp = this.getBoardPoint(neighbourPoint);
			if (bp.isCalculatedFlag())
				continue;

			if (bp.getColor() == ColorUtil.BLANK) {
				makeBlankPointBlock(neighbourPoint, newBlock);

			} else if (bp.getColor() == ColorUtil.BLACK
					|| bp.getColor() == ColorUtil.WHITE) {

				// newBlock.addNeighborBlock_oneWay(bp.getBlock());
				bp.getBlock().addBreathBlock_twoWay(newBlock);
			}
		}

	}

	/**
	 * 
	 * @param point
	 *            itself is alreay counted.
	 * @return
	 */
	private int _countBlankBlock(Point blankPoint) {
		BoardPoint bp = null;
		int count = 0;
		for (Delta delta : Constant.ADJACENTS) {
			Point neighbourPoint = blankPoint.getNeighbour(delta);
			if (neighbourPoint == null)
				continue;
			bp = this.getBoardPoint(neighbourPoint);
			if (bp.isCalculatedFlag())
				continue;

			if (bp.getColor() == ColorUtil.BLANK) {
				bp.setCalculatedFlag(true);
				count++;
				count += _countBlankBlock(neighbourPoint);
			}
		}
		return count;
	}

	public void check(String temp) {
		if (Constant.INTERNAL_CHECK == false)
			return;
		if (this.getStepHistory().getAllSteps().size() == 0)
			return;
		GoBoardForward goBoard = this;
		// Set<Block> whiteBlocks = goBoard.getWhiteBlocks();
		// int whoseTurn = this.stepHistory.getLastStep().getColor();
		GoBoard goBoard2 = new GoBoard(this.getInitColorState());
		goBoard2.setCheckForward(false);// avoid stack overflow.
		try {
			for (StepMemo stepMemo : this.getStepHistory().getAllSteps()) {
				goBoard2.oneStepForward(stepMemo.getStep());

			}
		} catch (RuntimeException e) {
			this.printState();
			for (StepMemo stepMemo : this.getStepHistory().getAllSteps()) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(stepMemo.getStep());
			}
			throw e;
		}

		boolean result = goBoard.getBoardColorState().equals(
				goBoard2.getBoardColorState());
		if (result == false) {
			for (StepMemo stepMemo : this.getStepHistory().getAllSteps()) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(stepMemo.getStep());
			}
			result = goBoard.getBoardColorState().equals(
					goBoard2.getBoardColorState());
			BoardColorState.showDiff(goBoard2.getBoardColorState(),
					goBoard.getBoardColorState());

			if (log.isEnabledFor(Level.WARN))
				log.warn("expeced new state");
			goBoard2.printState();
			if (log.isEnabledFor(Level.WARN))
				log.warn("curernt state");
			this.printState();
			TestCase.assertTrue("state should equal internally!", result);
		}

		for (BasicBlock basicBlock : goBoard.getAllBlocks()) {
			// for (Point point : Point.getAllPoints(goBoard.boardSize)) {
			Point point = basicBlock.getBehalfPoint();

			BasicBlock oldBlock = goBoard.getBasicBlock(point);
			BasicBlock newBlock = goBoard2.getBasicBlock(point);
			boolean equals = BasicBlock.equals(newBlock, oldBlock);
			if (equals == false) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(temp);
				if (log.isEnabledFor(Level.WARN))
					log.warn("correct result for" + point
							+ ": after steps below!");
				for (StepMemo stepMemo : goBoard.getStepHistory().getAllSteps()) {
					if (log.isEnabledFor(Level.WARN))
						log.warn(stepMemo.getStep());
				}
				if (log.isEnabledFor(Level.WARN))
					log.warn("new expected block:");
				if (log.isEnabledFor(Level.WARN))
					log.warn(newBlock);
				if (log.isEnabledFor(Level.WARN))
					log.warn("old block");
				if (log.isEnabledFor(Level.WARN))
					log.warn(oldBlock);
				this.printState();

				outputManualWithIssue();
			}
			TestCase.assertTrue(
					"all block should be equal internally shoushu = ", equals);
		}

	}

	protected void outputManualWithIssue() {
		SimpleGoManual goManual = new SimpleGoManual(this.boardSize,
				this.getInitTurn());
		goManual.setBlackName("EGO - Black");
		goManual.setWhiteName("EGO - White");
		goManual.setId(new Random().nextInt());
		for (StepMemo stepMemo : getStepHistory().getAllSteps()) {
			goManual.addStep(stepMemo.getStep());
		}
		String fileName = Constant.rootDir + "/调试出错的棋谱/" + goManual.getId()
				+ ".sgf";
		SGFGoManual.storeGoManual(fileName, goManual);

	}

	public boolean isCheckForward() {
		return checkForward;
	}

	public void setCheckForward(boolean checkForward) {
		this.checkForward = checkForward;
	}

	public boolean hasStep() {
		return this.stepHistory.getAllSteps().isEmpty() == false;
	}

	public boolean noStep() {
		return this.stepHistory.getAllSteps().isEmpty();
	}

	/**
	 * 双虚手终局
	 * 
	 * @return
	 */
	public boolean areBothPass() {
		return this.getStepHistory().areBothPass();
	}

	public boolean validate(Step step) {
		if (step.isPass())
			return true;

		Point original = step.getPoint();
		int color = step.getColor();

		// 下标合法,该点空白
		if (this.getColor(original) != ColorUtil.BLANK) { // 第一类不合法点.
			if (log.isDebugEnabled()) {
				log.debug("该点不合法,在棋盘之外或者该点已经有子：");
			}
			return false;
		} else {
			if (this.noStep() != true
					&& this.getLastStep().getProhibittedPoint() == step
							.getPoint()
					&& this.getLastStep().getProhibittedColor() == step
							.getColor()) {

				// }
				// BoardPoint boardPoint = this.getBoardPoint(original);
				// if (boardPoint.getProhibitStep() == (shoushu + 1)
				// && boardPoint.getProhibitColor() == color) {
				if (step.isLoopSuperior()) {
					if (log.isEnabledFor(Level.WARN))
						log.warn("模拟找了绝对劫材之后提回。避开打劫检查。");
					return true;
				} else {
					if (log.isEnabledFor(Level.WARN))
						log.warn("这是打劫时的禁着点,请先找劫材!");
					if (log.isEnabledFor(Level.WARN))
						log.warn("落点为：a=" + original + " shoushu="
								+ (shoushu + 1));
					BoardColorState oldState = this.getBoardColorState();
					// ensure it is recorded as duplicate state.
					stepHistory.setColorState(oldState);
					// in this case, last step should always exist! but seond
					// last may not exist
					if (stepHistory.getSecondLastStep() != null) {
						BoardColorState preState = stepHistory
								.getSecondLastStep().getColorState();
						stepHistory.setColorState(preState);
					} else {
						stepHistory.setColorState(this.getInitColorState());
					}
					return false;
				}
			} else {
				int selfColor = ColorUtil.getNextStepColor(shoushu);
				if (color != 0) {
					selfColor = (byte) color;
				}

				if (this.getNeighborState(original, selfColor).isValid() == false)
					return false;
				if (globalDuplicate(step) == true)
					return false;
				return true;
			}
		}

	}

	public boolean globalDuplicate(Step step) {
		if (step.isPass())
			return false;
		SimpleNeighborState neighborState = getNeighborState(step);
		// make safe copy!
		byte[][] state = this.getColorArray();
		int row, column;
		// clear dead/eaten point
		for (Block block : neighborState.getEatenBlocks()) {
			for (Point point : block.getPoints()) {
				row = point.getRow();
				column = point.getColumn();
				state[row][column] = 0;
			}
		}
		// step is not played yet. play the step in state copy.
		row = step.getPoint().getRow();
		column = step.getPoint().getColumn();
		state[row][column] = (byte) step.getColor();

		int whoseTurn = ColorUtil.enemyColor(step.getColor());
		BoardColorState boardState = BoardColorState.getInstance(state,
				whoseTurn);
		if (stepHistory.containState(boardState)) {
			if (log.isEnabledFor(Level.WARN)) {
				log.warn("落子点无效:全局同型再现!候选点排除。" + step);
			}
			return true;
		}
		return false;
	}

	public boolean validate(final int row, final int column) {
		return validate(Point.getPoint(boardSize, row, column));
	}

	public boolean validate(Point point) {
		return validate(point, this.decideColor());
	}

	/**
	 * 判断落子位置的有效性。没有副作用 <br/>
	 * 
	 * no side effect
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean validate(Point original, int color) {
		Step step = new Step(original, color);
		return validate(step);
	}

	protected SearchNode rootParent = SearchNode.createSpecialRoot();
	protected SearchNode current = rootParent;

	public SearchNode getCurrent() {
		return current;
	}

	// public SearchNode getRoot() {
	// return rootParent.getChild();
	// }

	public TreeGoManual getTreeGoManual() {
		TreeGoManual manual = new TreeGoManual(rootParent, boardSize,
				getInitTurn());
		manual.setInitState(getInitState());
		return manual;
	}

	public void initTree(TreeGoManual treeGoManual) {
		rootParent = treeGoManual.getRoot();
	}
}
