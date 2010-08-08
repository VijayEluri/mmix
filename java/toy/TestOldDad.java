package toy;

import junit.framework.TestCase;

public class TestOldDad extends TestCase {
	public void testDeepFirstSearch() {
		Search cen = new Search();

		Board board = new OldDad();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionDeepFirst();

	}

	public void testBroadFirstSearch() {
		Search cen = new Search();

		Board board = new OldDad();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionBroadFirst();

	}
}
