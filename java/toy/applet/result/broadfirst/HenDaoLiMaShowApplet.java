package toy.applet.result.broadfirst;

import toy.HenDaoLiMa;
import toy.Search;

public class HenDaoLiMaShowApplet extends BroadFirstManualApplet {
	@Override
	public void init() {
		super.init();
		board = new HenDaoLiMa();

		board.init();
		//solution = Util.getList(TestHenDaoLiMa.fileName);
		Search cen = new Search();

		cen.setInitBoard(board.deepCopy());
		solution = cen.searchSolutionBroadFirst();


	}
}
