package eddie.wu.search.two;

import junit.framework.TestCase;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.small.TwoTwoBoardSearch;

public class VerifyAll_IT extends TestCase {
	public void testState0() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_whiteTurn);
	}

	public void testState1011() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(4, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_whiteTurn);
	}

	public void testState1012() {
		String[] text = new String[2];
		text[0] = new String("[W, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-4, score_whiteTurn);
	}

	public void testState2011() {
		String[] text = new String[2];
		text[0] = new String("[B, B]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_whiteTurn);
	}

	public void testState2012() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_whiteTurn);
	}

	public void testState2021() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(4, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(4, score_whiteTurn);
	}

	public void testState2022() {
		String[] text = new String[2];
		text[0] = new String("[W, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-4, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-4, score_whiteTurn);
	}

	public void testState2031() {
		String[] text = new String[2];
		text[0] = new String("[B, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(4, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-4, score_whiteTurn);
	}

	public void testState2041() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_whiteTurn);
	}

	public void testState3011() {
		String[] text = new String[2];
		text[0] = new String("[B, B]");
		text[1] = new String("[B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_whiteTurn);
	}

	public void testState3012() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_whiteTurn);
	}

	public void testState3021() {
		String[] text = new String[2];
		text[0] = new String("[B, B]");
		text[1] = new String("[W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_whiteTurn);
	}

	public void testState3022() {
		String[] text = new String[2];
		text[0] = new String("[W, B]");
		text[1] = new String("[W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_blackTurn);

		stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_whiteTurn);
	}


}
