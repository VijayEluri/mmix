package go;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Block;
import eddie.wu.domain.Point;
import eddie.wu.domain.comp.BlockBreathComparator;
import eddie.wu.ui.RecordState;

/*
 * 计算目标棋块是否有双活的可能。
 * 这里只是识别最终状态，需要计算变化的状态不在此列。
 */
public class BothLiveAnalysis extends  SurviveAnalysis{
	
	private static final Log log = LogFactory.getLog(BothLiveAnalysis.class);

	public BothLiveAnalysis(byte[][] state) {
		super(state);

	}
	
	public int duiSha(Block me,Block enemy){
		
		return 0;
	}

	/**
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
			log.info("Block " + minBreathEnemy.getTopLeftPoint()
					+ " has min breath: " + minBreathEnemy.getBreaths());
		}

		Set<Block> breathBlocks = block.getBreathBlocks();
		Set<Block> breathBlocks2 = minBreathEnemy.getBreathBlocks();
		int count = 0;
		for (Block blankBlock : breathBlocks) {
			
			if (breathBlocks2.contains(blankBlock)) {
				count++;
				if (blankBlock.getTotalNumberOfPoint() <= 1) {
					/*
					 * 有眼时才能单气双活。
					 */
					Set<Point> eyes = this.getRealEyes(block.getTopLeftPoint());
					Set<Point> enemyEyes = this.getRealEyes(minBreathEnemy.getTopLeftPoint());
					if(eyes.size()==0){
						continue; // impossible to coexist lively with one breath.
					}if(eyes.size()==1){
						if(enemyEyes.size()==1){
							/*
							 * time to compare the breaths. 这里不考虑先走可杀对方，后走则双活的情况。
							 */
							if (block.getBreaths() >= minBreathEnemy.getBreaths()) {
								return 16;// 已经双活或者可以杀死对方
							}
							int deltaBreath = minBreathEnemy.getBreaths()
									- block.getBreaths();
							if (deltaBreath <= (blankBlock.getTotalNumberOfPoint() - 1)) {
								return 8;// 已经双活
							} else if (deltaBreath <= (blankBlock
									.getTotalNumberOfPoint() - 2)) {
								return 1;// 后手双活
							}
						}
					}else{//					
						throw new RuntimeException("should not happen since block is live already."+block);
					}
				}
				
				if (log.isInfoEnabled()) {
					log.info("shared block " + blankBlock.getTopLeftPoint()
							+ " with min breath block "
							+ minBreathEnemy.getTopLeftPoint());
				}
				/*
				 * 是否气块所有的气 为两棋块共享。
				 */
				boolean completeShared = true;
				for (Point point : blankBlock.getAllPoints()) {
					if (block.getAllBreathPoints().contains(point)
							&& minBreathEnemy.getAllBreathPoints().contains(
									point)) {
						//shared breath.
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
					
					/*
					 * time to compare the breaths. 这里不考虑先走可杀对方，后走则双活的情况。
					 */
					if (block.getBreaths() >= minBreathEnemy.getBreaths()) {
						return 16;// 已经双活或者可以杀死对方
					}
					int deltaBreath = minBreathEnemy.getBreaths()
							- block.getBreaths();
					if (deltaBreath <= (blankBlock.getTotalNumberOfPoint() - 2)) {
						return 8;// 已经双活
					} else if (deltaBreath <= (blankBlock
							.getTotalNumberOfPoint() - 3)) {
						return 1;// 后手双活
					}
				} else {
					return 0;
				}
			}
		}
		if (log.isInfoEnabled()) {
			log.info("coexist with " + count + " blocks.");
		}
		return 0;
	}
}
