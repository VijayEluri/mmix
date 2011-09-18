package go;

import java.util.HashSet;
import java.util.Set;

import eddie.wu.domain.Block;
import eddie.wu.domain.Point;

public class Board {
	public static final int BOARD_SIZE = 19;
	int[][] state = new int[BOARD_SIZE + 2][BOARD_SIZE + 2];

	public void play(int row, int column) {

	}

	private Set<Block> blocks = new HashSet<Block>();
    
	Set<Point> zhanJiao ; //占角的点
	Set<Point> guaJiao; // 挂角的点
	/**
	 * 是否两边都有拆二的余地。
	 * @return
	 */
//	public boolean shuangChaiEr(){//
//		 
//	}
	
	public Set<Point> getCandidateInBuJu() {//布局的候选点
		//如果每个点都有拆二的余地，可以认为占角成立。
		for(Point point : Point.xing){
			
		}
		
		return null;
	}
	public Set<Point> getCandidate() {
		
		
		
//		for (Block block : blocks) {
//			if (block.inGroup() == false && block.isSingle() == true) {
//				if (block.getBreath() == 1) {
//					return block.getBreathPoint();
//				}
//			}
//		}
		return null;
	}
}
