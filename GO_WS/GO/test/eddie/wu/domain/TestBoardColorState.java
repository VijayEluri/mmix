package eddie.wu.domain;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.manual.StateLoader;

public class TestBoardColorState extends TestCase {
	private static final Logger log = Logger.getLogger(TestBoardColorState.class);

	static {
		Logger.getLogger(TestBoardColorState.class).setLevel(Level.INFO);
	}

	public void _test_internal(BoardColorState original) {
//		BoardColorState normalize = original.normalize();
//		log.warn("Normalized:");
//		log.warn(normalize.getStateString());
//
//		SymmetryResult oper = BoardColorState.getSymmetryOper(normalize, original);
//		log.warn("Original :");
//		BoardColorState convert = normalize.convert(oper);
//		log.warn(convert.getStateString());
//		TestCase.assertEquals(original, convert);
	}

	public void test() {

		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState original = BoardColorState.getInstance(state, Constant.BLACK);
		this._test_internal(original);
	}

	public void testNormalize1() {

		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState original = BoardColorState.getInstance(state, Constant.BLACK);
		this._test_internal(original);
	}

	public void testNormalize2() {

		String[] text = new String[3];
		text[0] = new String("[B, _, B]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState original = BoardColorState.getInstance(state, Constant.BLACK);
		this._test_internal(original);

	}

	public void testTwoTwo() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.INFO);
		String[] text = new String[2];
		text[0] = new String("[W, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState original = BoardColorState.getInstance(state, Constant.BLACK);
		this._test_internal(original);
	}

	public void testTwoTwo_second() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.INFO);
		String[] text = new String[2];
		text[0] = new String("[_, W]");
		text[1] = new String("[W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState original = BoardColorState.getInstance(state, Constant.BLACK);
		this._test_internal(original);
	}

	public void test2() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.INFO);
		String[] text = new String[3];
		text[0] = new String("[B, W, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState original = BoardColorState.getInstance(state, Constant.BLACK);
		this._test_internal(original);
	}

	public void test3() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.INFO);
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState original = BoardColorState.getInstance(state, Constant.BLACK);
		this._test_internal(original);
	}
}
