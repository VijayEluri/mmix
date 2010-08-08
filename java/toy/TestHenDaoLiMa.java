package toy;

import junit.framework.TestCase;

public class TestHenDaoLiMa extends TestCase{
	public void testDeepFirstSearch() {
		Search cen = new Search();

		Board board = new HenDaoLiMa();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionDeepFirst();

	}

	public void testBroadFirstSearch() {
		Search cen = new Search();

		Board board = new HenDaoLiMa();
		board.init();

		cen.setInitBoard(board);
		cen.searchSolutionBroadFirst();

	}
}
