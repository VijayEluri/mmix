package toy.applet.result.broadfirst;

import toy.CengCengSheFang;
import toy.Search;
import toy.applet.OldDadApplet;

public class CengCengSheFangBroadApplet extends BroadFirstManualApplet {
	@Override
	public void init() {
		super.init();
		board = new CengCengSheFang();

		board.init();

		Search cen = new Search();

		cen.setInitBoard(board.deepCopy());
		solution = cen.searchSolutionBroadFirst();

	}
}
