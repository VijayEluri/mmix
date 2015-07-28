package eddie.wu.domain.analy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Delta;
import eddie.wu.domain.Point;
import eddie.wu.domain.comp.BlockBreathComparator;

/**
 * 计算目标棋块是否有双活的可能。 这里只是识别最终状态，需要计算变化的状态不在此列。<br/>
 * 双活的棋块所处的状态并非一般意义上的终止状态。双方都有合法的候选点。但是走了反而不利。<br/>
 * 也许还是直接计算候选点导致的不利结果更好处理一点。<br/>
 * 还有一个问题就是何时开始计算双活的可能性呢？
 * 
 * @deprecated Not Complete.
 */
public class BothLiveAnalysis extends SurviveAnalysis {

	private static final Logger log = Logger.getLogger(BothLiveAnalysis.class);

	public BothLiveAnalysis(byte[][] state) {
		super(state);

	}

	public int duiSha(Block me, Block enemy) {

		return 0;
	}

	/**
	 * 目标棋块能否以双活的形式生存,
	 * 
	 * @param pointIn
	 * @return 0 for unknown
	 */
	public int isBothLive(Point pointIn) {
		Block block = getBlock(pointIn);
		Set<Block> enemys = block.getEnemyBlocks();
		List<Block> enemyBlocks = new ArrayList<Block>(enemys.size());
		enemyBlocks.addAll(enemys);
		Collections.sort(enemyBlocks, new BlockBreathComparator());
		Block minBreathEnemy = enemyBlocks.get(0);
		if (log.isInfoEnabled()) {
			log.info("Enemy Block " + minBreathEnemy.getTopLeftPoint()
					+ " has min breath : " + minBreathEnemy.getBreaths());
		}

		Set<BlankBlock> breathBlocks = block.getBreathBlocks();
		Set<BlankBlock> breathBlocks2 = minBreathEnemy.getBreathBlocks();
		int count = 0;
		for (BlankBlock blankBlock : breathBlocks) {

			if (breathBlocks2.contains(blankBlock) == false)
				continue;
			count++;
			// 仅仅共享一气.
			if (blankBlock.getNumberOfPoint() <= 1) {
				/*
				 * 有眼时才能单气双活。
				 */
				Set<Point> eyes = this.getRealEyes(block.getTopLeftPoint(),
						false).getRealEyes();
				Set<Point> enemyEyes = this.getRealEyes(
						minBreathEnemy.getTopLeftPoint(), false).getRealEyes();
				if (log.isInfoEnabled()) {
					log.info("self block eyes " + eyes);
					log.info("enemy block eyes " + enemyEyes);
				}
				if (eyes.size() == 0) {
					continue; // impossible to coexist lively with one breath.
				}
				if (eyes.size() == 1) {
					if (enemyEyes.size() == 1) {
						/*
						 * time to compare the breaths. 这里不考虑先走可杀对方，后走则双活的情况。
						 */
						if (block.getBreaths() >= minBreathEnemy.getBreaths()) {
							return 16;// 已经双活或者可以杀死对方
						}
						int deltaBreath = minBreathEnemy.getBreaths()
								- block.getBreaths();
						if (deltaBreath <= (blankBlock.getNumberOfPoint() - 1)) {
							return 8;// 已经双活
						} else if (deltaBreath <= (blankBlock
								.getNumberOfPoint() - 2)) {
							return 1;// 后手双活
						}
					}
				} else {//
					throw new RuntimeException(
							"should not happen since block is live already."
									+ block);
				}
			}

			if (log.isInfoEnabled()) {
				log.info("shared block " + blankBlock.getTopLeftPoint()
						+ " of breath " + blankBlock.getNumberOfPoint()
						+ " with min breath block "
						+ minBreathEnemy.getTopLeftPoint());
			}
			/**
			 * 共享两气或者两气以上<br/>
			 * 是否气块所有的气均为两棋块共享。
			 */
			boolean completeShared = true;
			for (Point point : blankBlock.getPoints()) {
				if (block.getBreathPoints().contains(point)
						&& minBreathEnemy.getBreathPoints().contains(point)) {
					// shared breath.
				} else {
					if (log.isInfoEnabled()) {
						log.info("point " + point
								+ " in shared breath block is not shared.");
					}

					completeShared = false;
					break;
				}
			}
			if (completeShared) {

				/**
				 * time to compare the breaths. 这里不考虑先走可杀对方，后走则双活的情况。
				 */
				if (block.getBreaths() >= minBreathEnemy.getBreaths()) {
					return 16;// 已经双活或者可以杀死对方
				} else {
					int deltaBreath = minBreathEnemy.getBreaths()
							- block.getBreaths();
					if (deltaBreath <= (blankBlock.getNumberOfPoint() - 2)) {
						return 8;// 已经双活
					} else if (deltaBreath <= (blankBlock.getNumberOfPoint() - 3)) {
						return 1;// 后手双活
					}
				}
			} else {
				return 0; // 变化未尽.
			}
		}// for

		if (log.isInfoEnabled()) {
			log.info("coexist with " + count + " blocks.");
		}
		return 0;
	}

	/**
	 * 双活中对方的送礼点，凑我方提子，因此需要判定他有没有可能长到四气（我方紧气后即为三气），<br/>
	 * 因此有机会紧气而不送吃。<br/>
	 * 常见的长气手段有提子，连接。（如何识别出连接的手段？似乎还要考虑断开的变化。） <br/>
	 * BBBWWW<br/>
	 * B WB <br/>
	 * 
	 * BBBWWW<br/>
	 * B W <br/>
	 * 
	 * @param point
	 *            the enemyBlock
	 * @param enemyColor
	 * @return
	 */
	public boolean extendToThree(Block enemyBlock) {
		
		Block enemyEnemy = enemyBlock.getMinBreathEnemyBlock();		
		if (enemyEnemy.getBreaths() == 1) {

		}
		for(Point point: enemyBlock.getPoints()){
		for (Delta delta : Constant.ADJACENTS) {
			Point nb = point.getNeighbour(delta);
			if (nb == null)
				continue;
			if (this.getColor(nb) == Constant.BLANK) {// try to find friend block to connect with.
				int breath = this.breathAfterPlay(nb, enemyBlock.getColor()).size();
				 if(breath>=4){
					 return true;
				 }else if(breath>=2){
					 
				 }
			}
		}
		}
		return false;
	}
}
