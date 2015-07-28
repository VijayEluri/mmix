package eddie.wu.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.api.GoBoardInterface;
import eddie.wu.ui.UIPoint;

/**
 * 这里主要实现后退一步（悔棋）的功能，要点是保证数据结构的一致性。<br/>
 * 处理后退一步的算法
 */
public class GoBoardBackward extends GoBoardForward implements GoBoardInterface {
	//count how many moves we have backward.
	private long backwardMoves = 0;
	public GoBoardBackward(int boardSize) {
		super(boardSize);
	}

	public GoBoardBackward(BoardColorState colorState) {
		super(colorState);
	}

	private transient static final Logger log = Logger
			.getLogger(GoBoardBackward.class);

	/**
	 * 要能够处理弃权的情况。
	 * 
	 * @return
	 */
	public boolean oneStepBackward() {
		if (current.getFather() == null || current.getStep() == null) {
			log.warn("already in root");
			return false;
		}
		String prefix = "Backward: " + current.getStep().toSGFString();
		this.current = current.getFather();
		if (current.getStep() == null) {
			log.warn(prefix + "-->INIT");
		} else {
			log.warn(prefix + "-->" + current.getStep().toSGFString());
		}

		// int index = getStepHistory().getAllSteps().size() - 1;
		StepMemo memo = this.getLastStep();
		backwardMoves++;//put it in central entrance.
		if (memo.isPass()) {
			this.getStepHistory().removeStep(shoushu--);
			
			return true;
		}
		return oneStepBackward(memo.getCurrentStepPoint());
	}

	/**
	 * 打谱用的。后退一步<br/>
	 * 尚未处理后来增加的数据结构,包括棋块的breathBlocks和气块的blackBlocks 和whiteBlocks 算法概况:<br/>
	 * 先将原先分裂的气块恢复为旧的气块（其中有到原先旧的相邻块的连接） 再将合并后的棋块恢复为原先的棋块（其中有到原气块的连接） 被提子也恢复为原棋块。 <br/>
	 * 该算法分成两步，第一步先恢复为正确的棋块/气块。<br/>
	 * 第二步再处理恢复相邻关系。
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	private boolean oneStepBackward(Point original) {
		if (original == null) {
			log.error("backward when Give up with original==null.");
			this.getStepHistory().removeStep(shoushu--);
			return true;
		}
		BoardColorState copy;
		if (this.getStepHistory().getAllSteps().size() <= 1) {
			copy = this.getInitColorState();
		} else {// if(this.getStepHistory().getAllSteps().size()>=2)
			copy = this.getSecondLastStep().getColorState();
		}
		// = this.getBoardColorState();

		BoardPoint boardPoint = this.getBoardPoint(original);
		// Point twinForKo = boardPoint.getTwinForKo();
		// if (twinForKo != null&&) {
		// BoardPoint twinBoardPoint = getBoardPoint(twinForKo);
		// twinBoardPoint.clearProhibitStep();
		// twinBoardPoint.clearTwinForKo();
		// boardPoint.clearTwinForKo();
		// }

		BoardPoint neighborP;
		BoardPoint enemyNeighborP;
		int myColor = boardPoint.getColor();
		if (myColor == 0) {
			// 该子被提子后没有在之前的步骤中恢复.
			String errorMessage = "Color of " + original
					+ " is Blank. 该子被提子后没有在之前的步骤中恢复  ";
			if (log.isEnabledFor(Level.WARN))
				log.warn(this.getBoardColorState().getStateString());
			for (StepMemo step : this.getStepHistory().getAllSteps()) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(step.getStep());
			}
			if (log.isDebugEnabled())
				log.debug(errorMessage);
			throw new RuntimeException(errorMessage);
		}
		int enemyColor = ColorUtil.enemyColor(myColor);
		int m1, n1;
		Set<Block> recal = new HashSet<Block>();

		StepMemo memo = this.getStepHistory().removeStep(shoushu);
		shoushu--;
		if (log.isInfoEnabled()) {
			log.info("\r\n悔棋 last step = " + memo);
		}

		/**
		 * 先彻底删除新生成的块。
		 */
		if (log.isInfoEnabled()) {
			log.info("先彻底删除新生成的块。");
			log.info("1.删除被提吃形成的气块");
		}
		for (Block eatenBlock : memo.getEatenBlocks()) {
			BlankBlock blankBlock = this.getBlankBlock(eatenBlock
					.getBehalfPoint());
			// 解除新的空白块和周围块的关系.
			blankBlock.removeNeighborBlocks_twoWay();
			blankBlock.setActive(false);
		}

		if (memo.getMergedBlocks().isEmpty() == false) {
			// remove the merged block from its neighbors.
			Block originalBlock = this.getBlock(original);
			if (log.isInfoEnabled()) {
				log.info("将之前被合并的棋块从邻块和气块中删除。" + originalBlock);
			}
			originalBlock.removeEnemyBlocks_TwoWay();
			originalBlock.removeBreathBlocks_TwoWay();
			originalBlock.setActive(false);
		} else {
			Block originalBlock = this.getBlock(original);
			if (originalBlock.getNumberOfPoint() == 1) {
				originalBlock.removeEnemyBlocks_TwoWay();
				originalBlock.removeBreathBlocks_TwoWay();
				originalBlock.setActive(false);
				// recal.add(originalBlock);
				if (log.isInfoEnabled()) {
					log.info("删除落子后形成的单点块");
				}
			}
		}

		// 彻底删除这些新的气块
		if (memo.getNeighborState().isOriginalBlankBlockDivided()) {
			// BlankBlock dividedBlankBlock = memo.getDividedBlock();
			for (BlankBlock newBlankBlock : memo.getNewBlankBlocks()) {
				newBlankBlock.removeNeighborBlocks_twoWay();
				newBlankBlock.setActive(false);
			}
		}
		// memo.getNewBlankBlocks()

		boolean reomvePointImpact = false;
		/**
		 * 处理被提吃的棋块。
		 */
		if (log.isInfoEnabled()) {
			log.info("恢复旧块");
			log.info("1.悔棋时处理被提吃的棋块");
		}
		SimpleNeighborState state = memo.getNeighborState();
		if (state.isEating()) {
			for (Block eatenBlock : memo.getEatenBlocks()) {
				eatenBlock.setActive(true);
				for (Point point : eatenBlock.getPoints()) {
					setColor(point, eatenBlock.getColor());
					setBlock(point, eatenBlock);

				}

				eatenBlock.attachToBreath();
				eatenBlock.attachToEnemy();
				// recover breath later.
			}
		}

		/**
		 * 被合并的棋块在悔棋时需要分裂（恢复成原先的小块）
		 * 
		 * 
		 */
		if (log.isInfoEnabled()) {
			log.info("2.悔棋时处理相邻的同色棋块");
		}
		Block originalBlock = this.getBlock(original);
		if (state.isFriendBlockMerged()) {
			if (log.isInfoEnabled()) {
				log.info("恢复之前被合并的棋块 at " + original);
			}
			for (Block mergedBlock : memo.getMergedBlocks()) {
				mergedBlock.setActive(true);
				if (log.isInfoEnabled()) {
					log.info("恢复之前被合并的棋块" + mergedBlock);
				}
				for (Point point : mergedBlock.getPoints()) {
					this.getBoardPoint(point).setBlock(mergedBlock);
				}

				mergedBlock.attachToBreath();
				mergedBlock.attachToEnemy();

				// 恢复和原先棋块/气块的相邻关系.
				// for (BlankBlock blankBlock : mergedBlock.getBreathBlocks()) {
				// blankBlock.addNeighborBlock_oneWay(mergedBlock);
				// }
				// for (Block enemyBlock : mergedBlock.getEnemyBlocks()) {
				// enemyBlock.addEnemyBlock_oneWay(mergedBlock);
				// }
			}
			// maintain bi-direction relationship later.

		} else if (state.isNewSinglePointBlock()) {

			TestCase.assertTrue(originalBlock.getNumberOfPoint() == 1);
			originalBlock.removeEnemyBlocks_TwoWay();
			originalBlock.removeBreathBlocks_TwoWay();
			originalBlock.setActive(false);
			// recal.add(originalBlock);
			if (log.isInfoEnabled()) {
				log.info("删除落子后形成的单点块");
			}

		} else {// 块没有变，但是需要减少一子。
			// maintain impact of one stone change later.
			originalBlock.removePoint(original);
			if (log.isInfoEnabled()) {
				log.info("原棋块没有变，但是减少一子。" + original);
			}

			// originalBlock.removeBreathPoint(point);

			// need to maintain breath here. even blocks are not
			// recal.add(originalBlock);
			// reomvePointImpact = true;
			// 和敌块可能不再相邻.现在即可处理.
			for (Block enemyBlock : state.getConnectedEnemyBlocks()) {
				// 不再相邻的棋块恰好被提吃了。
				if (state.getEatenBlocks().contains(enemyBlock)) {
					originalBlock.removeBreathPoints(enemyBlock.getPoints());
					originalBlock.removeEnemyBlock_oneWay(enemyBlock);
				} else {
					enemyBlock.removeEnemyBlocks_TwoWay(originalBlock);

				}

			}
			// 气数减少。（原先通过扩展一子获得的气数）
			originalBlock.removeBreathPoints(state.getAddedBreaths());
			originalBlock.addBreathPoint(original);
		}

		/**
		 * 气块在落子时分裂过,悔棋时先将他们合并.
		 */

		if (log.isInfoEnabled()) {
			log.info("3.悔棋时处理相邻的气块");
		}
		this.setColor(original, ColorUtil.BLANK);

		if (state.isOriginalBlankBlockDivided()) {// 气块曾经分裂
			BlankBlock dividedBlankBlock = memo.getOriginalBlankBlock();
			dividedBlankBlock.attachToNeighbor();
			dividedBlankBlock.setActive(true);

			for (Point point : dividedBlankBlock.getPoints()) {
				this.setBlock(point, dividedBlankBlock);
			}

			// for (Block blackBlock : dividedBlankBlock.getNeighborBlocks()) {
			// blackBlock.addBreathBlock_oneWay(dividedBlankBlock);
			// }
			if (log.isInfoEnabled()) {
				log.info("合并之前被分裂的气块.");
			}
		}

		else if (state.isOriginalBlankBlockDisappear()) {
			BlankBlock originalBlankBlock = memo.getOriginalBlankBlock();
			TestCase.assertEquals(1, originalBlankBlock.getNumberOfPoint());
			originalBlankBlock.attachToNeighbor();
			originalBlankBlock.setActive(true);
			if (log.isInfoEnabled()) {
				log.info("恢复单点气块,之前曾消失.");
			}
			// for (Block enemyBlock :
			// originalBlankBlock.getNeighborBlocks()) {
			// enemyBlock.addBreathBlock_oneWay(originalBlankBlock);
			// }

			this.setBlock(original, originalBlankBlock); // will add one stone.

			// if (log.isInfoEnabled()) {
			// if (originalBlankBlock.getNumberOfPoint() == 0) {
			// log.info("恢复单点气块,之前曾消失.");
			// if (memo.getMergedBlocks().isEmpty() == false) {
			// boolean remove = originalBlankBlock.getNeighborBlocks()
			// .remove(originalBlock);
			// }
			// } else {
			// if (log.isInfoEnabled()) {
			// }
			// }
		} else {
			BlankBlock originalBlankBlock = memo.getOriginalBlankBlock();

			this.setBlock(original, originalBlankBlock); // will add one stone.
			if (log.isInfoEnabled()) {
				log.info("恢复多点气块,增加气点" + original);
			}

			// 恢复曾经消失的敌块相邻关系。
			for (Block enemyBlock : state.getDisconnectedEnemyBlocks()) {
				originalBlankBlock.addNeighborBlock_twoWay(enemyBlock);
			}
		}
		/**
		 * 先处理异色邻子,增加一气即可。
		 */
		if (log.isInfoEnabled()) {
			log.info("4.悔棋时处理异色邻子");
		}

		for (Block enemyBlock : state.getEnemyBlocks()) {
			enemyBlock.addBreathPoint(original);
		}
		for (Block enemyBlock : state.getFriendBlocks()) {
			enemyBlock.addBreathPoint(original);
		}
		// for (Delta delta : Constant.ADJACENTS) {
		// Point tmp = original.getNeighbour(delta);
		// if (tmp == null)
		// continue;
		// neighborP = this.getBoardPoint(tmp);
		// if (neighborP.getColor() == enemyColor) {
		// neighborP.getBlock().addBreathPoint(original);
		// // neighborP.getBlock().addBreathBlock_twoWay(
		// // this.getBlankBlock(original));
		// } else if (neighborP.getColor() == myColor) {
		// neighborP.getBlock().addBreathPoint(original);
		// }
		// }

		//
		// recal.addAll(memo.getEatenBlocks());
		// recal.addAll(memo.getMergedBlocks());
		//
		/**
		 * 进一步处理被提吃的棋块。
		 */
		for (Block eatenBlock : memo.getEatenBlocks()) {
			for (Point eatenP : eatenBlock.allPoints) {
				this.getBoardPoint(eatenP).setColor(enemyColor);
			}
			for (Block enemyBlock : eatenBlock.getEnemyBlocks()) {
				enemyBlock.addEnemyBlock_oneWay(eatenBlock);
			}

			// 建立和落子点空白块的相邻关系
			eatenBlock.addBreathBlock_twoWay(this.getBlankBlock(original));
			// below does not work because the breath block is empty for eaten
			// block
			for (BlankBlock blankBlock : eatenBlock.getBreathBlocks()) {
				blankBlock.addNeighborBlock_oneWay(eatenBlock);
			}

			// 建立(恢复)和周围敌块的相邻关系
			for (Point eatenP : eatenBlock.getPoints()) {
				for (Delta delta : Constant.ADJACENTS) {
					Point nb = eatenP.getNeighbour(delta);
					if (nb == null)
						continue;
					if (getColor(nb) == myColor) {
						this.getBlock(nb).addEnemyBlock_twoWay(eatenBlock);
						this.getBlock(nb).removeBreathPoint(eatenP);
					}
					// recal.add(getBlock(nb));
				}
			}
		}
		// this.getBoardPoint(eatenP).setBlock(eatenB);
		// recal.add(getBlock(eatenP));

		if (reomvePointImpact == true) {

			for (Iterator<Block> iter = originalBlock.getEnemyBlocks()
					.iterator(); iter.hasNext();) {
				Block block = iter.next();
				boolean connected = false;
				for (Point point : block.getPoints()) {
					for (Delta delta : Constant.ADJACENTS) {
						Point nb = point.getNeighbour(delta);
						if (nb == null)
							continue;
						if (this.getBlock(nb) == originalBlock) {
							// still connected,
							connected = true;
							break;
						}
					}
					if (connected == true)
						break;
				}
				if (connected == true)
					continue;
				else {
					// not connected. update relationship with
					block.removeEnemyBlock_oneWay(originalBlock);
					iter.remove();
					if (log.isInfoEnabled()) {
						log.info("block " + block.getBehalfPoint()
								+ "no longer connected to blankBlock "
								+ originalBlock.getBehalfPoint());
					}
				}
			}

		}
		if (Constant.INTERNAL_CHECK == true) {
			// verify breath
			for (Block block : this.getBlackWhiteBlocksOnTheFly()) {
				this.verifyBreath(block);
			}

			String temp = "after backward from step" + original;
			check_internal(temp);
			this.backward_check(temp);
			if (log.isEnabledFor(Level.WARN))
				log.warn("backward check succede");

			this.debugIngernalDataStructure(copy, this.getBoardColorState());
		}
		return true;
	}
	public long getBackwardMoves(){
		return backwardMoves;
	}

	public void debugIngernalDataStructure(BoardColorState copy,
			BoardColorState dynamic) {
		// assert goBoard = goBoard2.
		boolean result = copy.equals(dynamic);
		if (result == false) {
			BoardColorState.showDiff(dynamic, copy);

		}

		TestCase.assertTrue("state should equal internally!", result);
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

	public void backward_check(String temp) {
		// GoBoardForward goBoard = this;
		BoardColorState boardColorState = this.getBoardColorState();

		GoBoard goBoard2 = new GoBoard(boardColorState);
		BoardColorState boardColorState2 = goBoard2.getBoardColorState();
		goBoard2.setShoushu(shoushu);

		boolean result = boardColorState.equals(boardColorState2);
		if (result == false) {
			BoardColorState.showDiff(boardColorState2, boardColorState);

			if (log.isEnabledFor(Level.WARN))
				log.warn("expeced new state whose turn = "
						+ boardColorState2.getWhoseTurnString());
			goBoard2.printState();
			if (log.isEnabledFor(Level.WARN))
				log.warn("curernt state whose turn = "
						+ boardColorState.getWhoseTurnString());
			this.printState();
			TestCase.assertTrue("state should equal internally!", result);
		}

		for (BasicBlock basicBlock : this.getAllBlocks()) {
			// for (Point point : Point.getAllPoints(goBoard.boardSize)) {
			Point point = basicBlock.getBehalfPoint();

			BasicBlock oldBlock = this.getBasicBlock(point);
			BasicBlock newBlock = goBoard2.getBasicBlock(point);
			boolean equals = BasicBlock.equals(newBlock, oldBlock);
			if (equals == false) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(temp);
				if (log.isEnabledFor(Level.WARN))
					log.warn("correct result for" + point
							+ ": after steps below!");
				for (StepMemo stepMemo : this.getStepHistory().getAllSteps()) {
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

	/**
	 * get all the eaten points in this step.
	 */
	@Override
	public Set<Point> getEatenPoints() {
		Set<Block> eatenBlocks = getStepHistory().getStep(shoushu - 1)
				.getEatenBlocks();
		// if (eatenBlocks.isEmpty() == false) {
		// return null;
		// }
		Set<Point> points = new HashSet<Point>();
		for (Block block : eatenBlocks) {
			points.addAll(block.allPoints);

		}

		return points;

	}

	public void initUIPoint(List<UIPoint> points) {
		UIPoint uPoint;
		byte[][] matrixState = getBoardColorState().getMatrixState();
		int color;
		StepMemo step;
		points.clear();
		// 显示初始状态的子
		// int boardSize = go.boardSize;
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				color = matrixState[row][column];
				if (getInitState() != null
						&& color == getInitState()[row][column]) {
					uPoint = new UIPoint(Point.getPoint(boardSize, row, column));
					uPoint.setColor(color);
					uPoint.setMoveNumber(0);

					points.add(uPoint);
				}
			}
		}

		// 显示棋谱中的落子
		List<StepMemo> steps2 = getStepHistory().getAllSteps();
		for (int i = 0; i < steps2.size(); i++) {
			step = steps2.get(i);
			if (step.isPass())
				continue;
			color = matrixState[step.getCurrentStepPoint().getRow()][step
					.getCurrentStepPoint().getColumn()];
			// 如果这一步（点）没有被提吃的话。
			if (color == Constant.BLACK || color == Constant.WHITE) {
				uPoint = new UIPoint(step.getCurrentStepPoint());
				uPoint.setColor(step.getColor());
				uPoint.setMoveNumber(i + 1);
				points.add(uPoint);
			} else {
				// TODO: better way to display eaten point.
			}
		}

	}
}
