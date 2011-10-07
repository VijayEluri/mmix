package go;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eddie.wu.domain.Block;
import eddie.wu.domain.comp.BlockBreathComparator;
import eddie.wu.domain.comp.BlockSizeComparator;
import eddie.wu.domain.survive.BreathPattern;
import eddie.wu.domain.survive.SmallEyeKnowledge;
import eddie.wu.domain.survive.RelativeSurviveResult;

public class OverAllAnalysis extends BothLiveAnalysis{ 
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
		for(Block block: eyes){
			if(block.getTotalNumberOfPoint()>6){
				
			}else{
				BreathPattern bp = BreathPattern.getBreathPattern(block);
				RelativeSurviveResult surviveResult = SmallEyeKnowledge.getResult(bp);
			}
		}
		
		
		
		
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
