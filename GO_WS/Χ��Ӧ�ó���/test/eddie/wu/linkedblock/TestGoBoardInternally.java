/*
 * Created on 2005-6-25
 *


 */
package eddie.wu.linkedblock;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.manual.LoadGoManual;

/**
 * @author eddie
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestGoBoardInternally extends TestCase {
	Log log = LogFactory.getLog(TestGoBoardInternally.class);
	private static final String rootDir = "doc/Î§Æå´òÆ×Èí¼þ/";
	public TestGoBoardInternally(String string) {
		super(string);
	}

	/**
	 * test internally--is data structure consistent for first go manual.
	 */
	public void testForwardNextStepSingleGoManualInternally() {
		Logger logger = Logger.getLogger(GoBoard.class);
		if (logger.getLevel().isGreaterOrEqual(Level.INFO)) {

		} else {
			logger.setLevel(Level.INFO);
		}
		byte[] original = null;
		original =new LoadGoManual(rootDir).loadSingleGoManual();
		helperTestMethod(original);
		log.info("success of testForwardNextStepSingleGoManualInternally");
	}

	public void helperTestMethodCountTime(byte[] original) {
		GoBoard goBoard = new GoBoard();
		GoBoard goBoard2 = null;
		GoBoard goBoard4 = null;
		GoBoard goBoard5 = null;
		BoardColorState boardState = null;
		BoardColorState boardState2 = null;
		BoardColorState boardState3 = null;
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
			oldTime = System.currentTimeMillis();
			goBoard.oneStepForward(original[i], original[i + 1]);
			newTime = System.currentTimeMillis();
			log.error("forwardOneStep cost: " + (newTime - oldTime) + "ms");
			boardState = goBoard.getBoardColorState();

			oldTime = System.currentTimeMillis();
			goBoard2 = new GoBoard(boardState, (i + 3) / 2);
			newTime = System.currentTimeMillis();
			log.error("new GoBoard(boardState) cost: " + (newTime - oldTime)
					+ "ms");

			oldTime = System.currentTimeMillis();
			goBoard2.generateHighLevelState();
			newTime = System.currentTimeMillis();
			log.error("goBoard2.generateHighLevelState() cost: "
					+ (newTime - oldTime) + "ms");
			boardState2 = goBoard2.getBoardColorState();
			log.debug("boardState:" + boardState);
			log.debug("boardState2:" + boardState2);
			assertEquals("state should equal internally!", boardState, goBoard2
					.getBoardColorState());
			log.debug("getBlackBlocksFromState:"
					+ goBoard.getBlackBlocksFromState());
			log.debug("getBlackBlocksFromState:"
					+ goBoard2.getBlackBlocksFromState());
			assertEquals("black block should equal!", goBoard
					.getBlackBlocksFromState(), goBoard2
					.getBlackBlocksFromState());
			log.debug("getWhiteBlocksFromState:"
					+ goBoard.getWhiteBlocksFromState());
			log.debug("getWhiteBlocksFromState:"
					+ goBoard2.getWhiteBlocksFromState());
			assertEquals("white block should equal!", goBoard
					.getWhiteBlocksFromState(), goBoard2
					.getWhiteBlocksFromState());

			// test clone
			GoBoard goBoard3 = goBoard2.deepClone();
			boardState3 = goBoard3.getBoardColorState();
			log.debug("boardState:" + boardState2);
			log.debug("boardState2:" + boardState3);
			assertEquals("state should equal internally!", boardState2,
					goBoard3.getBoardColorState());

			assertEquals("black block should equal!", goBoard2
					.getBlackBlocksFromState(), goBoard3
					.getBlackBlocksFromState());

			assertEquals("white block should equal!", goBoard2
					.getWhiteBlocksFromState(), goBoard3
					.getWhiteBlocksFromState());
			assertNotNull(goBoard3.getPoints());
			byte ii, j;
			for (ii = 1; ii < 20; ii++) {
				for (j = 1; j < 20; j++) {
					assertNotNull(goBoard3.getPoints()[ii][j]);
				}
			}

			if (goBoard4 != null) {
				log.debug("a=" + original[i]);
				log.debug("b=" + original[i + 1]);
				goBoard4.oneStepForward(original[i], original[i + 1]);
				assertEquals("state should equal internally!", boardState,
						goBoard4.getBoardColorState());

				assertEquals("black block should equal!", goBoard
						.getBlackBlocksFromState(), goBoard4
						.getBlackBlocksFromState());

				assertEquals("white block should equal!", goBoard
						.getWhiteBlocksFromState(), goBoard4
						.getWhiteBlocksFromState());
			} else {
				goBoard4 = goBoard2;
			}

			if (goBoard5 != null) {
				log.debug("a=" + original[i]);
				log.debug("b=" + original[i + 1]);
				goBoard5.oneStepForward(original[i], original[i + 1]);
				assertEquals("state should equal internally!", boardState,
						goBoard5.getBoardColorState());

				assertEquals("black block should equal!", goBoard
						.getBlackBlocksFromState(), goBoard5
						.getBlackBlocksFromState());

				assertEquals("white block should equal!", goBoard
						.getWhiteBlocksFromState(), goBoard5
						.getWhiteBlocksFromState());
			} else {
				goBoard5 = goBoard3;
			}
		}
	}

	public void helperTestMethod(byte[] original) {
		GoBoard goBoard = new GoBoard();
		GoBoard goBoard2 = null;
		GoBoard goBoard4 = null;
		GoBoard goBoard5 = null;
		BoardColorState boardState = null;
		BoardColorState boardState2 = null;
		BoardColorState boardState3 = null;
		// /long oldTime=0;
		// long newTime=0;
		log.debug("original.length=" + original.length);
		for (int i = 0; i < original.length; i += 2) {
			if (log.isDebugEnabled()) {
				log.debug("[a=" + original[i] + ",b=" + original[i + 1] + "]");
			}
		}
		for (int i = 0; i < original.length; i += 2) {
			log.info("shoushu=" + (i + 3) / 2);
			log.info("a=" + original[i]);
			log.info("b=" + original[i + 1]);
			
			
			goBoard.deepClone();
			log.info("new created goboard clone succes:"+i);
			goBoard.oneStepForward(original[i], original[i + 1]);
			
			boardState = goBoard.getBoardColorState();

			goBoard2 = new GoBoard(boardState, (i + 3) / 2);

			goBoard2.generateHighLevelState();
			boardState2 = goBoard2.getBoardColorState();
			log.debug("boardState:" + boardState);
			log.debug("boardState2:" + boardState2);
			assertEquals("state should equal internally!", boardState, goBoard2
					.getBoardColorState());
			log.debug("getBlackBlocksFromState:"
					+ goBoard.getBlackBlocksFromState());
			log.debug("getBlackBlocksFromState:"
					+ goBoard2.getBlackBlocksFromState());
			assertEquals("black block should equal!", goBoard
					.getBlackBlocksFromState(), goBoard2
					.getBlackBlocksFromState());
			log.debug("getWhiteBlocksFromState:"
					+ goBoard.getWhiteBlocksFromState());
			log.debug("getWhiteBlocksFromState:"
					+ goBoard2.getWhiteBlocksFromState());
			assertEquals("white block should equal!", goBoard
					.getWhiteBlocksFromState(), goBoard2
					.getWhiteBlocksFromState());

			// test clone
			GoBoard goBoard3 = goBoard2.deepClone();
			log.debug("new created goboard clone succes");
			goBoard.deepClone();
			log.debug("after forwardstep goboard clone succes");
			boardState3 = goBoard3.getBoardColorState();
			log.debug("boardState:" + boardState2);
			log.debug("boardState2:" + boardState3);
			assertEquals("state should equal internally!", boardState2,
					goBoard3.getBoardColorState());

			assertEquals("black block should equal!", goBoard2
					.getBlackBlocksFromState(), goBoard3
					.getBlackBlocksFromState());

			assertEquals("white block should equal!", goBoard2
					.getWhiteBlocksFromState(), goBoard3
					.getWhiteBlocksFromState());
			assertNotNull(goBoard3.getPoints());
			byte ii, j;
			for (ii = 1; ii < 20; ii++) {
				for (j = 1; j < 20; j++) {
					assertNotNull(goBoard3.getPoints()[ii][j]);
				}
			}

			if (goBoard4 != null) {
				log.debug("a=" + original[i]);
				log.debug("b=" + original[i + 1]);
				goBoard4.oneStepForward(original[i], original[i + 1]);
				assertEquals("state should equal internally!", boardState,
						goBoard4.getBoardColorState());

				assertEquals("black block should equal!", goBoard
						.getBlackBlocksFromState(), goBoard4
						.getBlackBlocksFromState());

				assertEquals("white block should equal!", goBoard
						.getWhiteBlocksFromState(), goBoard4
						.getWhiteBlocksFromState());
			} else {
				goBoard4 = goBoard2;
			}

			if (goBoard5 != null) {
				log.debug("a=" + original[i]);
				log.debug("b=" + original[i + 1]);
				goBoard5.oneStepForward(original[i], original[i + 1]);
				assertEquals("state should equal internally!", boardState,
						goBoard5.getBoardColorState());

				assertEquals("black block should equal!", goBoard
						.getBlackBlocksFromState(), goBoard5
						.getBlackBlocksFromState());

				assertEquals("white block should equal!", goBoard
						.getWhiteBlocksFromState(), goBoard5
						.getWhiteBlocksFromState());
			} else {
				goBoard5 = goBoard3;
			}
		}
	}

	/**
	 * test internally--is data structure consistent for first go manual.
	 */
	public void testForwardNextStepFirstLibGoManualInternally() {

		byte count = 0;
		List list =new LoadGoManual(rootDir).loadMultiGoManualFromLib0();
		byte[] original = null;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			count++;
			original = (byte[]) iter.next();
			log.debug("GOManual:" + count);
			log.debug("GOManualLength:" + original.length);
			this.helperTestMethod(original);

		}
		log.info("success of testForwardNextStepFirstLibGoManualInternally");
	}

	/**
	 * test internally--is data structure consistent for first go manual. for
	 * all Go Manuals
	 */
	public void testForwardNextStepForAllGoManualInternally() {

		int count = 0;
		List list =new LoadGoManual(rootDir).loadMultiGoManualFromLib0();

		byte[] original = null;
		// for (Iterator iter = list.iterator(); iter.hasNext();) {
		// count++;
		// original = (byte[]) iter.next();
		// if (log.isInfoEnabled()) {
		// log.debug("GOManual:" + count);
		// log.debug("GOManualLength:" + original.length);
		// }
		// this.helperTestMethod(original);
		//
		// }
		for (int j = 1; j < 2; j++) {
			log.info("j=" + j);
			list = new LoadGoManual(rootDir).loadMultiGoManual(j);
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				count++;
				original = (byte[]) iter.next();
				if (log.isInfoEnabled()) {
					log.info("GOManual:" + count);
					log.info("GOManualLength:" + original.length);
				}
				this.helperTestMethod(original);
			}
		}
		log.info("success of testForwardNextStepForAllGoManualInternally");
	}
}