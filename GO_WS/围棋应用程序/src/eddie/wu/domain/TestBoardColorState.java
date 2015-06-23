package eddie.wu.domain;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.manual.StateLoader;
import eddie.wu.util.GBKToUTF8;

public class TestBoardColorState extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	public void test() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.INFO);
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState normalize = BoardColorState.getInstance(state,
				Constant.BLACK).normalize();
		if(log.isEnabledFor(Level.WARN)) log.warn("Normalized:");
		if(log.isEnabledFor(Level.WARN)) log.warn(normalize.getStateString());

		// if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(normalize.getDisplayMatrixState()));
	}

	public void testTwoTwo() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.INFO);
		String[] text = new String[2];
		text[0] = new String("[W, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState normalize = BoardColorState.getInstance(state,
				Constant.BLACK).normalize();
		if(log.isEnabledFor(Level.WARN)) log.warn("Normalized:");
		if(log.isEnabledFor(Level.WARN)) log.warn(normalize.getStateString());

		// if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(normalize.getDisplayMatrixState()));
	}

	public void testTwoTwo_second() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.INFO);
		String[] text = new String[2];
		text[0] = new String("[_, W]");
		text[1] = new String("[W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState normalize = BoardColorState.getInstance(state,
				Constant.BLACK).normalize();
		if(log.isEnabledFor(Level.WARN)) log.warn("Normalized:");
		if(log.isEnabledFor(Level.WARN)) log.warn(normalize.getStateString());

	}

	public void test2() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.INFO);
		String[] text = new String[3];
		text[0] = new String("[B, W, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState normalize = BoardColorState.getInstance(state,
				Constant.BLACK).normalize();
		if(log.isEnabledFor(Level.WARN)) log.warn(normalize.getStateString());

	}

	public void test3() {
		Logger.getLogger(BoardColorState.class).setLevel(Level.INFO);
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, _, _]");
		text[2] = new String("[_, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState normalize = BoardColorState.getInstance(state,
				Constant.BLACK).normalize();
		if(log.isEnabledFor(Level.WARN)) log.warn(normalize.getStateString());

		// if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(normalize.getDisplayMatrixState()));
	}
}
