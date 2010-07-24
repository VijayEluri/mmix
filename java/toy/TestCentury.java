package toy;

import junit.framework.TestCase;

public class TestCentury extends TestCase {
	public void test() {
		Search cen = new Search();

		Board board = new OldDad();
		board.init();
		
		cen.setInitBoard(board);
		cen.searchSolution();
		
	}
}
