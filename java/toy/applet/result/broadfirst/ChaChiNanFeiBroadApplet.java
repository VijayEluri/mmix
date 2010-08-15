package toy.applet.result.broadfirst;

import toy.ChaChiNanFei;
import toy.Search;

public class ChaChiNanFeiBroadApplet extends BroadFirstManualApplet {
	@Override
	public void init() {
		super.init();
		board = new ChaChiNanFei();

		board.init();

		Search cen = new Search();

		cen.setInitBoard(board.deepCopy());
		solution = cen.searchSolutionBroadFirst();

	}
}
