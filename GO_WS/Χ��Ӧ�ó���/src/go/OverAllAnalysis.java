package go;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eddie.wu.domain.Block;
import eddie.wu.domain.comp.BlockBreathComparator;
import eddie.wu.domain.comp.BlockSizeComparator;

public class OverAllAnalysis extends BothLive{ 
	public OverAllAnalysis (byte[][] state){
		super(state);
	}
	
	public void analyze(){
		/**
		 * �ȴ���������λ
		 */
		List<Block> eyes = new ArrayList<Block>(eyeBlocks);
		Collections.sort(eyes, new BlockSizeComparator());
		Collections.reverse(eyes);
		
		
		
		/**
		 * �������̵Ŀ飬���紦�ڱ����״̬�ġ�
		 */
		List<Block> list = new ArrayList<Block>(allActiveBlocks.size());
		list.addAll(allActiveBlocks);
		Collections.sort(list, new BlockBreathComparator());
		for(Block block: list){
			
		}
	}
}
