package eddie.wu.search.global;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;
import eddie.wu.domain.Constant;
import eddie.wu.manual.StateLoader;

public class TestAllState2 extends TestCase {
	/**
	 * final path:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Score=0<br/>
	 */
	public void testState1() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		// System.out.println(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(0, score);
	}

	/**
	 * Step [point=null, color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Score=0<br/>
	 */
	public void testState2() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(0, score);
	}

	/**
	 * final path:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Score=0<br/>
	 */
	public void testState3() {
		String[] text = new String[2];
		text[0] = new String("[_, W]");
		text[1] = new String("[W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		System.out.println(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK);
		int score = goS.globalSearch();
		System.out.println("Score=" + score);
		Assert.assertEquals(0, score);
	}
}
