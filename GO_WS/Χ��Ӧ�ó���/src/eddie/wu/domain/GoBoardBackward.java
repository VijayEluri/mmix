package eddie.wu.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 *  处理后退一步的算法
 */
class GoBoardBackward extends GoBoardForward {
	
	public GoBoardBackward(int boardSize) {
		super(boardSize);
	}

	private transient static final Logger log = Logger
			.getLogger(GoBoardBackward.class);
	
	public boolean oneStepBackward() {
		// int index = getStepHistory().getAllSteps().size() - 1;
		StepMemo memo = this.getLastStep();
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
	public boolean oneStepBackward(Point original) {

		BoardPoint boardPoint = this.getBoardPoint(original);
		BoardPoint neighborP;
		BoardPoint enemyNeighborP;
		int myColor = boardPoint.getColor();
		if (myColor == 0) {
			// 该子被提子后没有在之前的步骤中恢复.
			String errorMessage = "Color of " + original
					+ " is Blank. 该子被提子后没有在之前的步骤中恢复  ";
			if(log.isDebugEnabled()) log.debug(errorMessage);
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
		boolean reomvePointImpact = false;
		/**
		 * 处理被提吃的棋块。
		 */
		if (log.isInfoEnabled()) {
			log.info("1.悔棋时处理被提吃的棋块");
		}
		for (Block eatenBlock : memo.getEatenBlocks()) {
			BlankBlock blankBlock = this.getBlankBlock(eatenBlock
					.getBehalfPoint());
			// 解除和周围块的关系.
			blankBlock.removeNeighborBlocks_twoWay();

			for (Point point : eatenBlock.getPoints()) {
				this.setColor(point, eatenBlock.getColor());
				setBlock(point, eatenBlock);
			}
			// recover breath later.
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
		if (memo.getMergedBlocks().isEmpty() == true) {

			if (originalBlock.getNumberOfPoint() == 1) {
				originalBlock.removeEnemyBlocks_TwoWay();
				originalBlock.removeBreathBlocks_TwoWay();
				// recal.add(originalBlock);
				if (log.isInfoEnabled()) {
					log.info("删除落子后形成的单点块");
				}
			} else {// 块没有变，但是需要减少一子。
					// maintain impact of one stone change later.
				originalBlock.removePoint(original);
				if (log.isInfoEnabled()) {
					log.info("块没有变，但是减少一子。" + original);
				}

				// need to maintain breath here. even blocks are not
				// recal.add(originalBlock);
				reomvePointImpact = true;
				// 和敌块可能不再相邻.现在即可处理.

			}
		} else {
			// remove the merged block from its neighbors.
			// Block originalBlock = this.getBlock(original);
			originalBlock.removeEnemyBlocks_TwoWay();
			originalBlock.removeBreathBlocks_TwoWay();

			for (Block mergedBlock : memo.getMergedBlocks()) {
				for (Point point : mergedBlock.getPoints()) {
					this.getBoardPoint(point).setBlock(mergedBlock);
				}
				// 恢复和原先棋块/气块的相邻关系.
				for (BlankBlock blankBlock : mergedBlock.getBreathBlocks()) {
					blankBlock.addNeighborBlock_oneWay(mergedBlock);
				}
				for (Block enemyBlock : mergedBlock.getEnemyBlocks()) {
					enemyBlock.addEnemyBlock_oneWay(mergedBlock);
				}
			}
			// maintain bi-direction relationship later.
			if (log.isInfoEnabled()) {
				log.info("恢复之前被合并的棋块" + original);
			}
		}

		/**
		 * 气块在落子时分裂过,悔棋时先将他们合并.
		 */
		if (log.isInfoEnabled()) {
			log.info("3.悔棋时处理相邻的气块");
		}
		this.setColor(original, ColorUtil.BLANK);
		BlankBlock dividedBlankBlock = memo.getDividedBlock();
		if (dividedBlankBlock == null) {
			BlankBlock originalBlankBlock = memo.getOriginalBlankBlock();
			if (originalBlankBlock.getNumberOfPoint() == 0) {
				for (Block enemyBlock : originalBlankBlock.getNeighborBlocks()) {
					enemyBlock.addBreathBlock_oneWay(originalBlankBlock);
				}
			}
			// will add one stone.
			this.setBlock(original, originalBlankBlock);

			if (log.isInfoEnabled()) {
				if (originalBlankBlock.getNumberOfPoint() == 0) {
					log.info("恢复单点气块,之前曾消失.");
				} else {
					log.info("恢复多点气块,增加" + original);
				}
			}
		} else {

			for (BlankBlock newBlankBlock : memo.getNewBlankBlocks()) {
				newBlankBlock.removeNeighborBlocks_twoWay();
			}

			for (Point point : dividedBlankBlock.getPoints()) {
				this.setBlock(point, dividedBlankBlock);
			}
			for (Block blackBlock : dividedBlankBlock.getNeighborBlocks()) {
				blackBlock.addBreathBlock_oneWay(dividedBlankBlock);
			}
			if (log.isInfoEnabled()) {
				log.info("合并之前被分裂的气块.");
			}
		}

		/**
		 * 先处理异色邻子,增加一气即可。
		 */
		if (log.isInfoEnabled()) {
			log.info("4.悔棋时处理异色邻子");
		}
		for (Delta delta : Constant.ADJACENTS) {
			Point tmp = original.getNeighbour(delta);
			if (tmp == null)
				continue;
			neighborP = this.getBoardPoint(tmp);
			if (neighborP.getColor() == enemyColor) {
				neighborP.getBlock().addBreathPoint(boardPoint.getPoint());
				neighborP.getBlock().addBreathBlock_twoWay(
						this.getBlankBlock(original));
			}
		}

		//
		// recal.addAll(memo.getEatenBlocks());
		// recal.addAll(memo.getMergedBlocks());
		//
		/**
		 * 处理被提吃的棋块。
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
		
		//verify breath
		for(Block block:this.getBlackWhiteBlock()){
			this.verifyBreath(block);
		}

		return true;
	}

}
