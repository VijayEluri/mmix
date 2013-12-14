package eddie.wu.domain.comp;

import java.util.Comparator;
import eddie.wu.domain.Block;

/**
 * sort by breath first, the size second.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class BreathSizeComparator implements Comparator<Block> {
	@Override
	public int compare(Block block1, Block block2) {
		int delta = block1.getBreaths() - block2.getBreaths();
		if (delta != 0)
			return delta;
		else {
			return block1.getNumberOfPoint()
					- block2.getNumberOfPoint();
		}
	}

}