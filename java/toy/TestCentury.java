package toy;

import junit.framework.TestCase;

public class TestCentury extends TestCase {
	public void testDeepFirstSearch() {
		Search cen = new Search();

		Board board = new Century();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionDeepFirst();

	}

	public void testBroadFirstSearch() {
		Search cen = new Search();

		Board board = new Century();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionBroadFirst();

	}
}
