package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.Point;

public class SymmetryRowColumnComparator implements Comparator<Point> {
	@Override
	public int compare(Point o1, Point o2) {
		int sum1 = o1.getRow() + o1.getColumn();
		int sum2 = o2.getRow() + o2.getColumn();
		if (sum1 != sum2) {
			return sum1 - sum2;
		}

		return o1.getRow() - o2.getRow();

	}

}