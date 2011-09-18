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
		 * 先处理最大的眼位
		 */
		List<Block> eyes = new ArrayList<Block>(eyeBlocks);
		Collections.sort(eyes, new BlockSizeComparator());
		Collections.reverse(eyes);
		
		
		
		/**
		 * 处理气短的块，比如处于被打吃状态的。
		 */
		List<Block> list = new ArrayList<Block>(allActiveBlocks.size());
		list.addAll(allActiveBlocks);
		Collections.sort(list, new BlockBreathComparator());
		for(Block block: list){
			
		}
	}
}
