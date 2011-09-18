package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.Block;


/**
 * sort by size
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class BlockSizeComparator implements Comparator<Block> {

	@Override
	public int compare(Block block1, Block block2) {
		return block1.getTotalNumberOfPoint() - block2.getTotalNumberOfPoint();
	}

}
