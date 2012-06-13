package eddie.wu.domain.sgf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BasicBlock;
import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.comp.RowColumnComparator;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.SGFGoManual;

public class TestGoBoardBackward extends TestCase {
	private static Logger log = Logger.getLogger(TestGoBoardForward.class);

	public void _testOne_backward(int manualIndex) {
		String fileName = SGFGoManual.getFileName(manualIndex);
		if(log.isDebugEnabled()) log.debug(fileName);
		GoManual manual = SGFGoManual.loadGoManual(fileName);
		GoBoard goBoard = new GoBoard();
		GoBoard goBoard2 = new GoBoard();
		for (int i = 0; i < manual.getShouShu() - 1; i++) {
			if(log.isDebugEnabled()) log.debug("message");
			try {
				goBoard.oneStepForward(manual.getStep(i));
				goBoard.oneStepForward(manual.getStep(i + 1));
				goBoard.oneStepBackward(manual.getStep(i + 1).getPoint());
				goBoard2.oneStepForward(manual.getStep(i));
			} catch (RuntimeException e) {
				if(log.isDebugEnabled()) log.debug("shoushu=" + (i + 1) + " error at step "
						+ manual.getStep(i));
				throw e;
			}
			// assert goBoard = goBoard2.
			boolean result = goBoard.getBoardColorState().equals(
					goBoard2.getBoardColorState());
			if (result == false) {
				BoardColorState.showDiff(goBoard2.getBoardColorState(),
						goBoard.getBoardColorState());
				if(log.isDebugEnabled()) log.debug("shoushu=" + (i + 1) + " error at step "
						+ manual.getStep(i));
			}

			assertTrue("state should equal internally!", result);

			for (Point point : Point.getAllPoints(goBoard.boardSize)) {
				boolean equals = BasicBlock.equals(
						goBoard2.getBasicBlock(point),
						goBoard.getBasicBlock(point));
				if (equals == false) {
					if(log.isDebugEnabled()) log.debug("shoushu=" + (i + 1) + " error at step "
							+ manual.getStep(i));
					if(log.isDebugEnabled()) log.debug("correct result: one step forward!");
					if(log.isDebugEnabled()) log.debug(goBoard2.getBasicBlock(point));
					System.out
							.println("suspectable result: two step forward, one step backward");
					if(log.isDebugEnabled()) log.debug(goBoard.getBasicBlock(point));
				}
				assertTrue("all block should be equal internally shoushu = "
						+ (i + 1) + ", manual index=" + manualIndex, equals);
			}
		}

	}

	public void _testOne_fullBackForth(int manualIndex) {
		String fileName = SGFGoManual.getFileName(manualIndex);
		if(log.isDebugEnabled()) log.debug(fileName);
		GoManual manual = SGFGoManual.loadGoManual(fileName);
		GoBoard goBoard = new GoBoard();
		for (Step step : manual.getSteps()) {
			goBoard.oneStepForward(step);
		}
		for (Step step : manual.getSteps()) {
			goBoard.oneStepBackward();
		}

		BlankBlock blankBlock = goBoard.getBlankBlock(1, 1);

		assertEquals(361, blankBlock.getNumberOfPoint());
	}

	public void testAll_backward() {
		log.setLevel(Level.ERROR);
		for (int i = 1; i <= 263; i++) {
			_testOne_backward(i);
			_testOne_fullBackForth(i);
		}
	}

	public void testOne() {
		 _testOne_backward(1);
		//_testOne_fullBackForth(1);
	}
}
