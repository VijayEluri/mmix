package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.Point;

/**
 * 三四线优先.
 * @author Eddie
 *
 */
public class LowLineComparator implements Comparator<Point> {

	@Override
	public int compare(Point o1, Point o2) {
		
		return o2.getMinLine() - o1.getMinLine();
	}

}
