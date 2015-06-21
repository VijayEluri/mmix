package eddie.wu.search.global;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;

public class TestSpecialState3 extends TestCase {
	private static Logger log = Logger.getLogger(TestSpecialState3.class);
	static {
		log.setLevel(Level.INFO);
		Logger.getLogger(TerritoryAnalysis.class).setLevel(Level.INFO);
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		Logger.getLogger(BigEyeSearch.class).setLevel(Level.INFO);
	}

	public void testcodead2() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		if (log.isEnabledFor(Level.WARN)) {
			sa.printState(log);
		}

		Point point = Point.getPoint(3, 3, 1);
		assertFalse(sa.isAlreadyLive_dynamic(point));
		assertFalse(sa.isAlreadyDead_dynamic(point));

		point = Point.getPoint(3, 1, 2);
		assertFalse(sa.isAlreadyLive_dynamic(point));
		assertFalse(sa.isAlreadyDead_dynamic(point));

		assertFalse(sa.isFinalState_deadExist());
	}

	public void testKnifeHandle5() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(3, 2, 3);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(3, 1, 2);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		assertFalse(sa.isFinalState_deadExist());
		// FinalResult finalResult = sa.finalResult();
		// if(log.isEnabledFor(Level.WARN)) log.warn(finalResult);
		// int score = finalResult.getScore();
		// if(log.isEnabledFor(Level.WARN)) log.warn("finalResult: score=" +
		// score);
		// assertEquals(0, score);
	}

	public void testKnifeHandle5_2() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(3, 3, 1);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		assertFalse(sa.isFinalState_deadExist());
	}

	
}
