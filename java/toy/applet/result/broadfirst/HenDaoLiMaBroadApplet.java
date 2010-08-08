package toy.applet.result.broadfirst;

import toy.HenDaoLiMa;
import toy.Search;

public class HenDaoLiMaBroadApplet extends BroadFirstManualApplet {
	@Override
	public void init() {
		super.init();
		board = new HenDaoLiMa();

		board.init();

		Search cen = new Search();

		cen.setInitBoard(board.deepCopy());
		solution = cen.searchSolutionBroadFirst();

	}
}
