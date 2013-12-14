package eddie.wu.domain.sgf;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BasicGoBoard;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardBackward;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.manual.StateLoader;

public class TestGoBoardBackwardSpecialStep extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);
	public void test_31() {

		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, _, B]");
		text[2] = new String("[_, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state);
		go.output();

		Step step = new Step(Point.getPoint(3, 2, 2), Constant.BLACK);
		go.oneStepForward(step);

		go.oneStepBackward();
		go.output();
	}

	public void test_32() {

		String[] text = new String[3];
		text[0] = new String("[B, _, B]");
		text[1] = new String("[B, W, B]");
		text[2] = new String("[_, B, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state);
		go.output();
		if(log.isEnabledFor(Level.WARN)) log.warn("");

		Step step = new Step(Point.getPoint(3, 1, 2), Constant.BLACK);
		go.oneStepForward(step);

		go.oneStepBackward();
		go.output();
	}
	
	public void test_33() {

		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, B, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state);
		go.output();
		if(log.isEnabledFor(Level.WARN)) log.warn("");

		Step step = new Step(Point.getPoint(3, 2, 1), Constant.BLACK);
		go.oneStepForward(step);

		go.oneStepBackward();
		go.output();
	}

	/**
	 * [B, _, B, _] <br/>
	 * [B, B, B, B] <br/>
	 * [B, B, W, _] <br/>
	 * [W, _, B, B] <br/>
	 * -----------------><br/>
	 * [B, _, B, _] <br/>
	 * [B, B, B, B] <br/>
	 * [B, B, _, B] <br/>
	 * [W, _, B, B] <br/>
	 * ------------------->
	 */
	public void test_4() {

		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[W, _, B, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state);
		go.output();
		if(log.isEnabledFor(Level.WARN)) log.warn("");

		Step step = new Step(Point.getPoint(4, 3, 4), Constant.BLACK);
		go.oneStepForward(step);
		go.output();

		go.oneStepBackward();
		go.output();
	}

	/**
	 * ------------->target<br/>
	 * [W, W, W, W] <br/>
	 * [W, B, _, W] <br/>
	 * [_, W, W, W] <br/>
	 * [_, W, W, _]<br/>
	 */

	public void test_42() {
		
		String[] text = new String[4];
		text[0] = new String("[W, W, W, W]");
		text[1] = new String("[_, B, _, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[B, W, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state);
		go.output();

		if(log.isEnabledFor(Level.WARN)) log.warn("\r\n");
		if(log.isEnabledFor(Level.WARN)) log.warn("");

		Step step = new Step(Point.getPoint(4, 2, 1), Constant.WHITE);
		go.oneStepForward(step);

		if(log.isEnabledFor(Level.WARN)) log.warn("\r\n");
		if(log.isEnabledFor(Level.WARN)) log.warn("");

		go.oneStepBackward();

		if(log.isEnabledFor(Level.WARN)) log.warn("");
	}

	
	static {
		Logger.getLogger(Block.class).setLevel(Level.DEBUG);
		Logger.getLogger(Block.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoard.class).setLevel(Level.DEBUG);
		Logger.getLogger(BasicGoBoard.class).setLevel(Level.DEBUG);
		Logger.getLogger(Block.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardBackward.class).setLevel(Level.DEBUG);
		Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);
	}

	public void test_43() {

		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, W]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state);
		go.output();

		if(log.isEnabledFor(Level.WARN)) log.warn("\r\n");
		if(log.isEnabledFor(Level.WARN)) log.warn("");

		Step step = new Step(Point.getPoint(4, 1, 3), Constant.WHITE);
		go.oneStepForward(step);

		if(log.isEnabledFor(Level.WARN)) log.warn("\r\n");
		if(log.isEnabledFor(Level.WARN)) log.warn("");

		go.oneStepBackward();
		if(log.isEnabledFor(Level.WARN)) log.warn("");
	}

	public void test_44() {

		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, W, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state);
		go.output();

		if(log.isEnabledFor(Level.WARN)) log.warn("\r\n");
		if(log.isEnabledFor(Level.WARN)) log.warn("");

		Step step = new Step(Point.getPoint(4, 4, 4), Constant.WHITE);
		go.oneStepForward(step);

		if(log.isEnabledFor(Level.WARN)) log.warn("\r\n");
		if(log.isEnabledFor(Level.WARN)) log.warn("");

		go.oneStepBackward();
		if(log.isEnabledFor(Level.WARN)) log.warn("");
	}

	public void testSteps() {
		List<Step> steps = new ArrayList<Step>();
		Step step = null;
		step = new Step(Point.getPoint(19, 4, 16), 1);
		steps.add(step);
		step = new Step(Point.getPoint(19, 4, 4), 2);
		steps.add(step);
		step = new Step(Point.getPoint(19, 16, 17), 1);
		steps.add(step);
		step = new Step(Point.getPoint(19, 17, 4), 2);
		steps.add(step);

		step = new Step(Point.getPoint(19, 17, 15), 1);
		steps.add(step);
		step = new Step(Point.getPoint(19, 10, 17), 2);
		steps.add(step);
		step = new Step(Point.getPoint(19, 6, 3), 1);
		steps.add(step);
		step = new Step(Point.getPoint(19, 3, 6), 2);
		steps.add(step);

		step = new Step(Point.getPoint(19, 15, 3), 1);
		steps.add(step);
		step = new Step(Point.getPoint(19, 11, 3), 2);
		steps.add(step);
		step = new Step(Point.getPoint(19, 16, 4), 1);
		steps.add(step);
		step = new Step(Point.getPoint(19, 17, 3), 2);
		steps.add(step);

		step = new Step(Point.getPoint(19, 17, 5), 1);
		steps.add(step);
		step = new Step(Point.getPoint(19, 16, 5), 2);
		steps.add(step);
		step = new Step(Point.getPoint(19, 18, 5), 1);
		steps.add(step);
		step = new Step(Point.getPoint(19, 15, 5), 2);
		steps.add(step);

		step = new Step(Point.getPoint(19, 16, 3), 1);
		steps.add(step);
		step = new Step(Point.getPoint(19, 17, 2), 2);
		steps.add(step);
		step = new Step(Point.getPoint(19, 14, 4), 1);
		steps.add(step);
		// step = new Step(Point.getPoint(19,14,5),2);steps.add(step);

		GoBoard goBoard = new GoBoard();
		for (int i = 0; i < steps.size(); i++) {
			goBoard.oneStepForward(steps.get(i));
		}
		// 2012-08-12 19:39:16,805 WARN [AWT-EventQueue-0] domain.GoBoardForward
		// (GoBoardForward.java:262) - after one step forward[18,4]
		// 2012-08-12 19:39:16,821 WARN [AWT-EventQueue-0] domain.GoBoardForward
		// (GoBoardForward.java:356) - Internal check succede.
		// 2012-08-12 19:39:24,567 WARN [AWT-EventQueue-0] domain.GoBoardForward
		// (GoBoardForward.java:262) - after one step forward[13,4]
		// 2012-08-12 19:39:24,567 WARN [AWT-EventQueue-0] domain.GoBoardForward
		// (GoBoardForward.java:356) - Internal check succede.
		// 2012-08-12 19:39:25,381 WARN [AWT-EventQueue-0] domain.GoBoardForward
		// (GoBoardForward.java:262) - after one step forward[13,3]
		// 2012-08-12 19:39:25,396 WARN [AWT-EventQueue-0] domain.GoBoardForward
		// (GoBoardForward.java:356) - Internal check succede.
	}
}
