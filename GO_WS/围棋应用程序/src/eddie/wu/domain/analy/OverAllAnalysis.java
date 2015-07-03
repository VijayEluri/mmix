package eddie.wu.domain.analy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.comp.BlockBreathComparator;
import eddie.wu.domain.comp.BasicBlockSizeComparator;
import eddie.wu.domain.survive.BreathPattern;
import eddie.wu.domain.survive.RelativeSurviveResult;
import eddie.wu.domain.survive.SmallEyeKnowledge;

public class OverAllAnalysis extends BothLiveAnalysis{ 
	public OverAllAnalysis (byte[][] state){
		super(state);
	}
	
	public void analyze(){
		/**
		 * 先处理最大的眼位
		 */
		List<BlankBlock> eyes = new ArrayList<BlankBlock>(eyeBlocks);
		Collections.sort(eyes, new BasicBlockSizeComparator());
		Collections.reverse(eyes);
		for(BlankBlock block: eyes){
			if(block.getNumberOfPoint()>6){
				
			}else{
				//BreathPattern bp = BreathPattern.getBreathPattern(block);
				//RelativeSurviveResult surviveResult = SmallEyeKnowledge.getResult(bp);
			}
		}
		
		
		
		
		/**
		 * 处理气短的块，比如处于被打吃状态的。
		 */
		List<Block> list = new ArrayList<Block>(blackWhiteBlocks.size());
		list.addAll(blackWhiteBlocks);
		Collections.sort(list, new BlockBreathComparator());
		for(Block block: list){
			
		}
	}
}
