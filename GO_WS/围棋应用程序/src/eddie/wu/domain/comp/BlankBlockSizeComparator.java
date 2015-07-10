package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.BlankBlock;

public class BlankBlockSizeComparator implements Comparator<BlankBlock> {
	@Override
	public int compare(BlankBlock block1, BlankBlock block2) {
		return block1.getNumberOfPoint() - block2.getNumberOfPoint();
	}

}
