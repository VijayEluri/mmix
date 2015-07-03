package eddie.wu.search;

import java.util.Comparator;

public class BlankPointBreathComparator implements Comparator<BlankPoint> {

	@Override
	public int compare(BlankPoint o1, BlankPoint o2) {
		return o1.getDirectBreaths() - o2.getDirectBreaths();
	}

}