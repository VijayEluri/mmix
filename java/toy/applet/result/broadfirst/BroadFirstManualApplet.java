package toy.applet.result.broadfirst;

import java.awt.Event;
import java.util.List;

import toy.BasicMove;
import toy.Move;
import toy.Search;
import toy.applet.OldDadApplet;

public class BroadFirstManualApplet extends OldDadApplet {
	int step = 0;
	List<BasicMove> solution;

	@Override
	public void init() {
		super.init();
		Search cen = new Search();

		cen.setInitBoard(board.deepCopy());
		solution = cen.searchSolutionBroadFirst();

	}

	/**
	 * trigger next step in the manual.
	 */
	@Override
	public boolean mouseDown(Event evt, int x, int y) {
		if (step < solution.size()) {
			BasicMove move = solution.get(step++);
			Move move2 = new Move(move);
			move2.setBoard(board);
			board.apply(move2);
			repaint();
		}
		return true;
	}
}
