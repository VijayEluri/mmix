package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.BasicBlock;


/**
 * sort by size
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class BasicBlockSizeComparator implements Comparator<BasicBlock> {

	@Override
	public int compare(BasicBlock block1, BasicBlock block2) {
		return block1.getNumberOfPoint() - block2.getNumberOfPoint();
	}

}
