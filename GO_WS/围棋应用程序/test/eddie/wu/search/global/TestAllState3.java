package eddie.wu.search.global;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;

public class TestAllState3 extends TestCase {

	static {
		Constant.INTERNAL_CHECK = false;
	}

	public void testState_specialColive1() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		GoBoardSearch goS = new SmallBoardGlobalSearch(state);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(1, score);
	}

	public void testState_specialColive2() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[W, _, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state,
				Constant.BLACK);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(0, score);
		goS.outputSearchStatistics();
	}

	public void testState_specialColive3() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state,
				Constant.BLACK);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(4, score);
		goS.outputSearchStatistics();
	}

	public void testState_specialColive4() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state,
				Constant.BLACK);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(3, score);
		goS.outputSearchStatistics();
	}

	/** 198 seconds */
	public void testState() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(4, score);
		goS.outputSearchStatistics();
	}

	public void testState_1() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		GoBoardSearch goS = new SmallBoardGlobalSearch(state);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(4, score);
	}

	public void testState_311() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[_, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));
		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertTrue(finalState);
	}

	public void testState_312() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);

		finalState = new TerritoryAnalysis(state).isFinalState();
		Assert.assertTrue(finalState);
	}

	public void testState_313() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);

		finalState = new TerritoryAnalysis(state).isFinalState();
		Assert.assertTrue(finalState);
	}

	/**
	 * 虽然是结果确定的状态（先后手结果相同）<br
	 * /〉 但是只看一步没有办法判断，先手黑两眼活<br
	 * /〉 计算过程中有重复的局面<br
	 * /〉 简化终局的判断周后反而速度快了，避免了没有意义的isLive调用<br
	 * /〉
	 */
	public void testState_314() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);

		GoBoardSearch goS = new SmallBoardGlobalSearch(state, Constant.WHITE);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(9, score);

		goS = new SmallBoardGlobalSearch(state, Constant.BLACK);
		score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(9, score);

		// finalState = new TerritoryAnalysis(state).isFinalState();
		// Assert.assertFalse(finalState);
	}

	public void testState_316() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new SmallBoardGlobalSearch(state, Constant.BLACK);
		score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(9, score);

		goS = new SmallBoardGlobalSearch(state, Constant.WHITE);
		score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(3, score);

		// finalState = new TerritoryAnalysis(state).isFinalState();
		// Assert.assertFalse(finalState);
	}

	public void testState_317() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new SmallBoardGlobalSearch(state, Constant.BLACK);
		score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(9, score);

		goS = new SmallBoardGlobalSearch(state, Constant.WHITE);
		score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(9, score);

		// finalState = new TerritoryAnalysis(state).isFinalState();
		// Assert.assertFalse(finalState);
	}

	public void testState_318() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new SmallBoardGlobalSearch(state, Constant.WHITE);
		score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(4, score);

	}

	public void testState_3110() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new SmallBoardGlobalSearch(state, Constant.BLACK);
		score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(9, score);

	}

	public void testState_3111() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[W, B, _]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SurviveAnalysis sa = new SurviveAnalysis(state);
		Assert.assertFalse(sa.isLive(Point.getPoint(3, 2, 1)));

	}

	/**
	 * 8 seconds.
	 */
	public void testState_319() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);
		GoBoardSearch goS;
		int score;
		goS = new SmallBoardGlobalSearch(state, Constant.BLACK);
		score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(-3, score);

	}

	/**
	 * we calculate steps = 2491 <br/>
	 * we know the result = 1575<br/>
	 * 8 seconds.--> 12 seconds
	 */
	public void testState_3191() {
		// Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		// Constant.INTERNAL_CHECK=true;
		String[] text = new String[3];
		text[0] = new String("[B, B, B]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);
		SmallBoardGlobalSearch goS;
		int score;
		goS = new SmallBoardGlobalSearch(state, Constant.WHITE);
		score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(-9, score);
		goS.outputSearchStatistics();
	}

	public void testState_31911() {
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, W, B]");
		text[2] = new String("[_, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		Point point = Point.getPoint(3, 1, 2);
		boolean live = new SurviveAnalysis(state).isLive_Special(point,
				Constant.BLACK);
		Assert.assertFalse(live);

		point = Point.getPoint(3, 2, 1);
		live = new SurviveAnalysis(state).isLive(point);
		Assert.assertFalse(live);

		point = Point.getPoint(3, 2, 3);
		live = new SurviveAnalysis(state).isLive(point);
		Assert.assertFalse(live);

	}

	public void testState_315() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		boolean finalState = new TerritoryAnalysis(state)
				.isFinalState_static1();
		Assert.assertFalse(finalState);

		finalState = new TerritoryAnalysis(state).isFinalState();
		Assert.assertTrue(finalState);
	}

	/**
	 * [B, B, B]<br/>
	 * [_, _, _]
	 */
	public void testState_11() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		GoBoardSearch goS = new SmallBoardGlobalSearch(state);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(9, score);
	}

	public void testState_2() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		SmallBoardGlobalSearch goS = new SmallBoardGlobalSearch(state);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(9, score);
		goS.outputSearchStatistics();
	}

}
