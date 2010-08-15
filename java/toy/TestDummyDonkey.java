package toy;

import java.util.List;

import junit.framework.TestCase;

public class TestDummyDonkey extends TestCase{
	public static String fileName = "dummydonkey.solution";
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
		List<BasicMove> list = cen.searchSolutionBroadFirst();
		
		Util.storeList(fileName , list);

	}
}
