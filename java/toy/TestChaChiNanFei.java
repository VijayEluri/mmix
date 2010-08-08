package toy;

import junit.framework.TestCase;

public class TestChaChiNanFei extends TestCase {
	/**
	 * out of memory
	 */
	public void testDeepFirstSearch() {
		Search cen = new Search();

		Board board = new ChaChiNanFei();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionDeepFirst();

	}

	/**
	 * out of memory
	 */
	public void testBroadFirstSearch() {
		Search cen = new Search();

		Board board = new ChaChiNanFei();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionBroadFirst();

	}
}
