package eddie.wu.search.four;

import eddie.wu.domain.Constant;
import eddie.wu.search.three.TestAllState3;

public class TestAllState4 extends TestAllState3 {
	/**
	 * the first important improvement is to share the search result for same
	 * state, because it may occurs many times, especially between win and lose
	 * search.
	 */
	public void testInitState() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[_, _, _, _]");
		text[2] = new String("[_, _, _, _]");
		text[3] = new String("[_, _, _, _]");

		testState_internal(text, Constant.BLACK, 1);
	}

	public void testState3091() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[_, B, W, _]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, W, W, _]");
		testState_internal(text, Constant.BLACK, 1);

	}

	public void testState3092() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[_, B, W, _]");
		text[2] = new String("[B, B, W, B]");
		text[3] = new String("[_, W, W, _]");
		testState_internal(text, Constant.WHITE, 1);

	}
}
