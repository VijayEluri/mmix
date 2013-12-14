package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.BasicBlock;
import eddie.wu.domain.Point;

/**
 * 块的排序通过他们的代表点(最左上点)决定.
 * 
 * @author Eddie
 * 
 */
public class BlockRowColumnComparator implements Comparator<BasicBlock> {

	@Override
	public int compare(BasicBlock b1, BasicBlock b2) {
		Point o1, o2;
		o1 = b1.getBehalfPoint();
		o2 = b2.getBehalfPoint();
		if (o1.getRow() == o2.getRow()) {
			return o1.getColumn() - o2.getColumn();
		} else {
			return o1.getRow() - o2.getRow();
		}

	}

}
