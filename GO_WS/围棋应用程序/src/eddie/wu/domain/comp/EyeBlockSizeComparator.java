package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.BlankBlock;

public class EyeBlockSizeComparator implements Comparator<BlankBlock> {
	@Override
	public int compare(BlankBlock block1, BlankBlock block2) {
		int size2 = Math.abs(block2.getNumberOfPoint() - 4);
		int size1 = Math.abs(block1.getNumberOfPoint() - 4);
		if (size1 != size2) {
			return size1 - size2;
		}
		size2 = block2.getBiggestNeighborBlock().getNumberOfPoint();
		size1 = block1.getBiggestNeighborBlock().getNumberOfPoint();
		return size2 - size1;
	}

}
