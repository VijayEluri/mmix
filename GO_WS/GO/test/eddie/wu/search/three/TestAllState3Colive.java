package eddie.wu.search.three;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.small.SmallBoardGlobalSearch;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * 处理3*3小棋盘的死活题。比较有意思，很多意外的结果。
 * 
 * @author think
 *
 */
public class TestAllState3Colive extends TestCase {
	private static Logger log = Logger.getLogger(TestAllState3Colive.class);
	static {
		Constant.INTERNAL_CHECK = false;
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		log.setLevel(Level.WARN);
	}

	public void testState_specialColive1() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state,
				Constant.BLACK, 1, 0);
		goS.setDepth(22);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("Score=" + score);
			goS.outputSearchStatistics(log);
		}
		// assertEquals(1, score);
	}

	public void testState_specialColive2() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[W, _, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state,
				Constant.BLACK, 4, 3);
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN)) {
			log.warn(goS.getGoBoard().getInitColorState().toString());
			log.warn("Score=" + score);
		}
		goS.outputSearchStatistics(log);
		assertEquals(4, score);
	}

	/**
	 * 处理被点入的眼(直三)
	 */
	public void testState_specialColive21() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		SurviveAnalysis sa = new SurviveAnalysis(state);
		Point point = Point.getPoint(3, 2, 1);
		assertFalse(sa.isAlreadyLive_dynamic(point));

		// SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state,
		// Constant.BLACK);
		// int score = goS.globalSearch();
		// if(log.isEnabledFor(Level.WARN)) log.warn("Score=" + score);
		// goS.outputSearchStatistics();
	}

	/**
	 * 处理被点入的眼(直三)
	 */
	public void testState_specialColive22() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		SurviveAnalysis sa = new SurviveAnalysis(state);
		Point point = Point.getPoint(3, 2, 1);
		assertTrue(sa.isAlreadyLive_dynamic(point));

	}

	public void testState_specialColive3() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state,
				Constant.BLACK, 4, 3);
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);
		assertEquals(4, score);
		goS.outputSearchStatistics(log);
	}

	public void testState_specialColive4() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state,
				Constant.BLACK, -3, -4);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);
		assertEquals(-3, score);
		goS.outputSearchStatistics(log);
	}

}
