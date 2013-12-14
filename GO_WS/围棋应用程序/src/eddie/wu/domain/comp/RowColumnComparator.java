package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.Point;

public class RowColumnComparator implements Comparator<Point> {
	@Override
	public int compare(Point o1, Point o2) {
		if (o1.getRow() == o2.getRow()) {
			return o1.getColumn() - o2.getColumn();
		} else {
			return o1.getRow() - o2.getRow();
		}

	}

}