package eddie.wu.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import util.GBKToUTF8;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.Candidate;

public class TestCandidate extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	public void testRealLifeProblem_20131393_5_six_3() {
		String[] text = new String[6];
		text[0] = new String("[_, _, W, B, B, _]");
		text[1] = new String("[_, _, W, _, B, _]");
		text[2] = new String("[_, B, W, _, B, _]");
		text[3] = new String("[W, W, W, B, B, _]");
		text[4] = new String("[B, B, B, B, B, _]");
		text[5] = new String("[B, _, B, _, B, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		boolean live = false;
		Set<Point> scope = new HashSet<Point>();
		scope.addAll(ta.getBlankBlock(Point.getPoint(6, 1, 1)).getPoints());
		List<Candidate> candidate = ta.getCandidate(null,point, scope,
				Constant.WHITE, true, false);
		if(log.isEnabledFor(Level.WARN)) log.warn(ta.getBoardColorState().getStateString());
		if(log.isEnabledFor(Level.WARN)) log.warn(candidate);

	}

}
