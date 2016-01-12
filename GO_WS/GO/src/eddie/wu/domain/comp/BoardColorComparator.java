package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;

public class BoardColorComparator implements Comparator<BoardColorState> {
	public static int compare_(BoardColorState o1, BoardColorState o2) {
		if (o1.getWhoseTurn() != o2.getWhoseTurn()) {
			//black precedes white
			return (o1.getWhoseTurn() == Constant.BLACK) ? -1 : 1;
		}

		long[] black1 = o1.getBlackLongArray();
		long[] black2 = o2.getBlackLongArray();
		if (black1.length != black2.length) {
			return black1.length - black2.length;
		} else {
			for (int i = 0; i < black1.length; i++) {
				if (black1[i] != black2[i]) {
					return (black1[i] > black2[i]) ? 1 : -1;
				}
			}
		}
		// if black stones are exactly same
		long[] white1 = o1.getWhiteLongArray();
		long[] white2 = o2.getWhiteLongArray();
		if (white1.length != white2.length) {
			return white1.length - white2.length;
		} else {
			for (int i = 0; i < white1.length; i++) {
				if (white1[i] != white2[i]) {
					return (white1[i] > white2[i]) ? 1 : -1;
				}
			}
		}
		//when state are same!
		return 0;
	}

	@Override
	public int compare(BoardColorState o1, BoardColorState o2) {
		int compareRes = compare_(o1, o2);
		if (compareRes == 0) {
			// if white is also exactly same
			System.out.println(o1.getStateString());
			System.out.println(o2.getStateString());
			System.err.println(o1.equals(o2));
			throw new RuntimeException("return 0");
		}
		return compareRes;
	}

}
