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
		 * �ȴ���������λ
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
		 * �������̵Ŀ飬���紦�ڱ����״̬�ġ�
		 */
		List<Block> list = new ArrayList<Block>(allActiveBlocks.size());
		list.addAll(allActiveBlocks);
		Collections.sort(list, new BlockBreathComparator());
		for(Block block: list){
			
		}
	}
}
