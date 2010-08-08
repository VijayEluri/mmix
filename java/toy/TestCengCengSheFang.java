package toy;

import junit.framework.TestCase;

public class TestCengCengSheFang extends TestCase {
	public void testDeepFirstSearch() {
		Search cen = new Search();

		Board board = new CengCengSheFang();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionDeepFirst();

	}

	/**
	 * Find the Solution: 77 steps
	 */
	public void testBroadFirstSearch() {
		Search cen = new Search();

		Board board = new CengCengSheFang();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionBroadFirst();

	}
}
