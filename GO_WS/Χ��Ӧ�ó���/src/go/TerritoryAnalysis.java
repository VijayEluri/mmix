package go;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;

/**
 * �Զ��ж������
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class TerritoryAnalysis extends StateAnalysis {
	public TerritoryAnalysis(byte[][] state) {
		super(state);
	}

	/**
	 * The standard rule to calculate the result. suppose all the result are
	 * finalized. û�����������Ҳ���ǻ��壬���ø÷���֮ǰ��Ҫ�ֹ�������ӡ�
	 * 
	 * @return
	 */
	public FinalResult finalResult() {
		int black = 0;
		int white = 0;
		int shared = 0;
		for (int i = 1; i <= 19; i++) {
			for (int j = 1; j <= 19; j++) {
				if (state[i][j] == Constant.BLACK)
					black++;
				else if (state[i][j] == Constant.WHITE)
					white++;
				else {
					Block block = this.getBlock(i, j);
					if (block.isEyeBlock()) {
						if (block.isBlackEye()) {
							black++;
						} else {
							white++;
						}
					} else {
						shared++;
					}
				}
			}
		}
		FinalResult res = new FinalResult(black, white, shared);
		return res;
	}

	/**
	 * ֻ����ȷ����Ĳż��룬��unkown�����ȫ����������ܵõ������
	 * 
	 * @return
	 */
	public FinalResult finalResult_heuristic() {
		for (int row = 1; row <= 19; row++) {
			for (int column = 1; column <= 19; column++) {
				Block block = this.getBlock(row, column);
				if(block.getEnemyBlocks().isEmpty()==false){
					if(block.getEnemyBlocks().size()>=2){
						Block eye = block.getEnemyBlocks().iterator().next();
						EyeKnowledge
					}
				}
			}
		}
		int black = 0;
		int white = 0;
		int shared = 0;
		FinalResult res = new FinalResult(black, white, shared);
		return res;
	}

}
