package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.Block;

public class GeneralBlockComparator implements Comparator<Block> {
	@Override
	public int compare(Block block1, Block block2) {
		return block2.getPriority() - block1.getPriority();
	}
}
