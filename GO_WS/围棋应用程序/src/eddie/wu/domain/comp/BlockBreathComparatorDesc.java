package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.Block;

public class BlockBreathComparatorDesc implements Comparator<Block> {

	@Override
	public int compare(Block block1, Block block2) {
		int blocks = block2.getNumberOfEyeBlock()
				- block1.getNumberOfEyeBlock();
		if (blocks != 0)
			return blocks;

		int size = block2.getNumberOfPoint() - block1.getNumberOfPoint();
		if (size != 0)
			return size;
		return block2.getBreaths() - block1.getBreaths();
	}

}
