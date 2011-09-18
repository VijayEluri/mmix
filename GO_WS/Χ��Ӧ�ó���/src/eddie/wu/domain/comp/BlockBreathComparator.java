package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.Block;

/**
 * sort by breath
 * 
 */
public class BlockBreathComparator implements Comparator<Block> {

	@Override
	public int compare(Block block1, Block block2) {
		return block1.getBreaths() - block2.getBreaths();
	}

}

