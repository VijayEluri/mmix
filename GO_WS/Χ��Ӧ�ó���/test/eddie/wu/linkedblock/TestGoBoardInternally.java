/*
 * Created on 2005-6-25
 *


 */
package eddie.wu.linkedblock;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.manual.GMDGoManual;
import eddie.wu.manual.LoadGMDGoManual;

/**
 * "Internally" means check the internal status instead of comparison with other
 * implementation.
 * 
 * @author eddie
 * 
 * 
 */
public class TestGoBoardInternally extends TestCase {
	Log log = LogFactory.getLog(TestGoBoardInternally.class);
	private static final String rootDir = Constant.rootDir;// "doc/Î§Æå´òÆ×Èí¼þ/";

	public TestGoBoardInternally(String string) {
		super(string);
	}

	public void testBackward() {
		GMDGoManual manual = GMDGoManual
				.loadGoManual(Constant.GLOBAL_MANUAL, 0);
		if (log.isDebugEnabled())
			log.debug(manual);
		_testBackward(manual);
	}

	public void _testBackward(GMDGoManual manual) {
		GoBoard goBoard = new GoBoard();
		int row = 0;
		int column = 0;
		byte[] original = manual.getMoves();

		for (int i = 0; i < original.length; i += 2) {
			row = original[i];
			column = original[i + 1];
			if (log.isDebugEnabled()) {
				log.debug("shoushu=" + (i + 3) / 2);
				log.debug("a=" + row);
				log.debug("b=" + column);
			}
			goBoard.oneStepForward(row, column);
		}

		for (int i = 0; i < original.length; i += 2) {
			column = original[(original.length - 1) - i];
			row = original[(original.length - 1) - (i + 1)];
			if (log.isDebugEnabled()) {
				log.debug("shoushu=" + (manual.getShouShu() - i / 2));
				log.debug("a=" + row);
				log.debug("b=" + column);
			}
			goBoard.oneStepBackward(row, column);

		}
		if (log.isInfoEnabled()) {
			log.info(goBoard.getBlock(1, 1));
			Set<Point> originalB = goBoard.getBlock(1, 1).getAllPoints();
			for (row = 1; row < Constant.ZBSX; row++) {
				for (column = 1; column < Constant.ZBSX; column++) {
					Point point = Point.getPoint(row, column);

					if (originalB.contains(point) == false)
						log.debug(point);
				}
			}
		}
	}

	/**
	 * test internally--is data structure consistent for first go manual.
	 */
	public void testForwardNextStep_SingleGoManualInternally() {
		Logger logger = Logger.getLogger(GoBoard.class);
		if (logger.getLevel().isGreaterOrEqual(Level.INFO)) {

		} else {
			logger.setLevel(Level.INFO);
		}
		byte[] original = null;
		original = new LoadGMDGoManual(rootDir).loadSingleGoManual();
		helperTestMethod(original);
		// helperTestMethod_CountTime(original);
		log.debug("success of testForwardNextStepSingleGoManualInternally");
	}

	public void helperTestMethod_CountTime(byte[] original) {
		GoBoard goBoard = new GoBoard();
		GoBoard goBoard2 = null;
		BoardColorState boardState = null;
		BoardColorState boardState2 = null;

		long oldTime = 0;
		long newTime = 0;
		log.debug("original.length=" + original.length);
		for (int i = 0; i < original.length; i += 2) {
			if (log.isDebugEnabled()) {
				log.debug("[a=" + original[i] + ",b=" + original[i + 1] + "]");
			}
		}
		for (int i = 0; i < original.length; i += 2) {
			log.debug("shoushu=" + (i + 3) / 2);
			log.debug("a=" + original[i]);
			log.debug("b=" + original[i + 1]);

			oldTime = System.nanoTime();
			goBoard.oneStepForward(original[i], original[i + 1]);
			newTime = System.nanoTime();
			log.error("forwardOneStep cost: " + (newTime - oldTime) + "ns");

			boardState = goBoard.getBoardColorState();

			oldTime = System.nanoTime();
			goBoard2 = new GoBoard(boardState, (i + 3) / 2);
			newTime = System.nanoTime();
			log.error("new GoBoard(boardState) cost: " + (newTime - oldTime)
					+ "ns");

			oldTime = System.nanoTime();
			goBoard2.generateHighLevelState();
			newTime = System.nanoTime();
			log.error("goBoard2.generateHighLevelState() cost: "
					+ (newTime - oldTime) + "ns");

			boardState2 = goBoard2.getBoardColorState();
			log.debug("boardState:" + boardState);
			log.debug("boardState2:" + boardState2);
			assertEquals("state should equal internally!", boardState,
					goBoard2.getBoardColorState());

			log.debug("getBlackBlocksFromState:" + goBoard.getBlackBlocks());
			log.debug("getBlackBlocksFromState:" + goBoard2.getBlackBlocks());
			assertEquals("black block should equal!", goBoard.getBlackBlocks(),
					goBoard2.getBlackBlocks());

			log.debug("getWhiteBlocksFromState:" + goBoard.getWhiteBlocks());
			log.debug("getWhiteBlocksFromState:" + goBoard2.getWhiteBlocks());
			assertEquals("white block should equal!", goBoard.getWhiteBlocks(),
					goBoard2.getWhiteBlocks());

		}
	}

	/**
	 * test one manual!<br/>
	 * Now clone does not work!
	 * 
	 * @param original
	 */
	public void helperTestMethod(byte[] original) {
		GoBoard goBoard = new GoBoard();
		GoBoard goBoard2 = null;

		BoardColorState boardState = null;
		BoardColorState boardState2 = null;

		log.debug("original.length=" + original.length);
		for (int i = 0; i < original.length; i += 2) {
			if (log.isDebugEnabled()) {
				log.debug("[a=" + original[i] + ",b=" + original[i + 1] + "]");
			}
		}
		for (int i = 0; i < original.length; i += 2) {
			log.debug("shoushu=" + (i + 3) / 2);
			log.debug("a=" + original[i]);
			log.debug("b=" + original[i + 1]);

			goBoard.oneStepForward(original[i], original[i + 1]);

			boardState = goBoard.getBoardColorState();
			goBoard2 = new GoBoard(boardState, (i + 3) / 2);
			goBoard2.generateHighLevelState();
			boardState2 = goBoard2.getBoardColorState();

			log.debug("boardState:" + boardState);
			log.debug("boardState2:" + boardState2);
			assertEquals("state should equal internally!", boardState,
					goBoard2.getBoardColorState());

			log.debug("getBlackBlocksFromState:" + goBoard.getBlackBlocks());
			log.debug("getBlackBlocksFromState:" + goBoard2.getBlackBlocks());
			assertEquals("black block should equal!", goBoard.getBlackBlocks(),
					goBoard2.getBlackBlocks());

			log.debug("getWhiteBlocksFromState:" + goBoard.getWhiteBlocks());
			log.debug("getWhiteBlocksFromState:" + goBoard2.getWhiteBlocks());
			assertEquals("white block should equal!", goBoard.getWhiteBlocks(),
					goBoard2.getWhiteBlocks());

			assertEquals("white block should equal!", goBoard.getBlankBlocks(),
					goBoard2.getBlankBlocks());

		}

	}

	/**
	 * test internally--is data structure consistent for first go manual.
	 */
	public void testForwardNextStep_FirstLibGoManualInternally() {

		byte count = 0;
		List<GMDGoManual> list = new LoadGMDGoManual(rootDir)
				.loadMultiGoManualFromLib0();
		for (GMDGoManual manual : list) {
			count++;
			byte[] original = manual.getMoves();
			log.debug("GOManual:" + count);
			log.debug("GOManualLength:" + original.length);
			this.helperTestMethod(original);

		}
		log.debug("success of testForwardNextStepFirstLibGoManualInternally");
	}

	/**
	 * test internally--is data structure consistent for first go manual. for
	 * all Go Manuals
	 */
	public void testForwardNextStep_ForAllGoManualInternally() {

		int count = 0;
		List<GMDGoManual> list = new LoadGMDGoManual(rootDir)
				.loadMultiGoManualFromLib0();

		for (int j = 1; j < 2; j++) {
			log.debug("j=" + j);
			list = new LoadGMDGoManual(rootDir).loadMultiGoManual(j);
			for (GMDGoManual manual : list) {
				byte[] original = manual.getMoves();
				count++;

				if (log.isDebugEnabled()) {
					log.debug("GOManual:" + count);
					log.debug("GOManualLength:" + original.length);
				}
				this.helperTestMethod(original);
			}
		}
		log.debug("success of testForwardNextStepForAllGoManualInternally");
	}
}