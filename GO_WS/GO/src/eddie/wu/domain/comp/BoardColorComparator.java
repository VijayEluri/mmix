package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Point;

public class BoardColorComparator implements Comparator<BoardColorState> {

	@Override
	public int compare(BoardColorState o1, BoardColorState o2) {
		if (o1.getWhoseTurn() != o2.getWhoseTurn())
			return o1.getWhoseTurn() - o2.getWhoseTurn();
		if (o1.getBlackPoints().equals(o2.getBlackPoints())) {
			if (o1.getWhitePoints().equals(o2.getWhitePoints())) {
				return 0;
			} else if (o1.getWhitePoints().size() != o2.getWhitePoints().size()) {
				return o1.getWhitePoints().size() - o2.getWhitePoints().size();
			} else {
				int count1 = 0, count2 = 0;
				for (Point point : o1.getWhitePoints()) {
					count1 += point.getOneDimensionCoordinate();
				}
				for (Point point : o2.getWhitePoints()) {
					count2 += point.getOneDimensionCoordinate();
				}
				return count1 - count2;
			}
		} else {
			if (o1.getBlackPoints().size() != o2.getBlackPoints().size()) {
				return o1.getBlackPoints().size() - o2.getBlackPoints().size();
			} else {
				int count1 = 0, count2 = 0;
				for (Point point : o1.getBlackPoints()) {
					count1 += point.getOneDimensionCoordinate();
				}
				for (Point point : o2.getBlackPoints()) {
					count2 += point.getOneDimensionCoordinate();
				}
				return count1 - count2;
			}
		}
		// return 0;
	}

}
