package eddie.wu.domain.analy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.Point;
import eddie.wu.domain.comp.BlockBreathComparator;

/**
 * 计算目标棋块是否有双活的可能。 这里只是识别最终状态，需要计算变化的状态不在此列。
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
}
