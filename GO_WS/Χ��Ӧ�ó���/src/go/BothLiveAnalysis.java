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
 * ����Ŀ������Ƿ���˫��Ŀ��ܡ�
 * ����ֻ��ʶ������״̬����Ҫ����仯��״̬���ڴ��С�
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
					 * ����ʱ���ܵ���˫�
					 */
					Set<Point> eyes = this.getRealEyes(block.getTopLeftPoint());
					Set<Point> enemyEyes = this.getRealEyes(minBreathEnemy.getTopLeftPoint());
					if(eyes.size()==0){
						continue; // impossible to coexist lively with one breath.
					}if(eyes.size()==1){
						if(enemyEyes.size()==1){
							/*
							 * time to compare the breaths. ���ﲻ�������߿�ɱ�Է���������˫��������
							 */
							if (block.getBreaths() >= minBreathEnemy.getBreaths()) {
								return 16;// �Ѿ�˫����߿���ɱ���Է�
							}
							int deltaBreath = minBreathEnemy.getBreaths()
									- block.getBreaths();
							if (deltaBreath <= (blankBlock.getTotalNumberOfPoint() - 1)) {
								return 8;// �Ѿ�˫��
							} else if (deltaBreath <= (blankBlock
									.getTotalNumberOfPoint() - 2)) {
								return 1;// ����˫��
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
				 * �Ƿ��������е��� Ϊ����鹲��
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
					 * time to compare the breaths. ���ﲻ�������߿�ɱ�Է���������˫��������
					 */
					if (block.getBreaths() >= minBreathEnemy.getBreaths()) {
						return 16;// �Ѿ�˫����߿���ɱ���Է�
					}
					int deltaBreath = minBreathEnemy.getBreaths()
							- block.getBreaths();
					if (deltaBreath <= (blankBlock.getTotalNumberOfPoint() - 2)) {
						return 8;// �Ѿ�˫��
					} else if (deltaBreath <= (blankBlock
							.getTotalNumberOfPoint() - 3)) {
						return 1;// ����˫��
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
