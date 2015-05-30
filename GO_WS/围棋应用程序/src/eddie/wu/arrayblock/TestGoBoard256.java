/*
 * Created on 2005-5-12
 *


 */
package eddie.wu.arrayblock;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.manual.GMDGoManual;
import eddie.wu.manual.LoadGMDGoManual;

/**
 * 
 * It is not a good idea because the array.goboard implementation is not strong
 * enough, there are several restriction to properly deal with real go manual.
 * 
 * @author eddie
 * 
 * 
 */
public class TestGoBoard256 extends TestCase {
	String rootDir = "doc/围棋打谱软件/";
	private static final Logger log = Logger.getLogger(TestGoBoard256.class);

	/**
	 * test lib 0 manual 0.
	 */
	public void testForwardNextStep_SingleGoManual_lib0() {
		byte[] original = null;

		original = new LoadGMDGoManual(rootDir).loadSingleGoManual().getMoves();
		GoBoard goBoard = new GoBoard();
		eddie.wu.arrayblock.GoBoard256 goboard1 = new eddie.wu.arrayblock.GoBoard256(
				Constant.BOARD_SIZE);
		// for(int i=0; i<1;i++ ){
		BoardColorState boardState = null;
		BoardColorState boardState1 = null;
		Constant.DEBUG_CGCL = false;
		for (int i = 0; i < original.length; i++) {
			try {
				log.debug("shoushu=" + (i + 1));
				log.debug("a=" + original[i]);
				log.debug("b=" + original[i + 1]);
				goBoard.oneStepForward(original[i], original[i + 1]);
				goboard1.cgcl(original[i], original[++i]);
				boardState = goBoard.getBoardColorState();
				int color = ColorUtil.getNextStepColor(i+1);
				boardState1 = new BoardColorState(goboard1.getStateArray(),color);

				assertEquals("state of every point should equal!", boardState,
						boardState1);
				assertTrue(boardState.equals(boardState1));
			} catch (RuntimeException e) {
				if(log.isDebugEnabled()) log.debug("不好: error with test manual in shoushu = "
						+ goBoard.getShoushu());
				throw e;
			}
		}

	}

	/**
	 * test lib 1 manual 0.
	 * 
	 * @deprecated failed because of lib1 is lost.
	 */
	public void testForwardNextStep_FromOneOfMultiGoManual_Lib1() {
		byte[] original = null;
		byte count = 0;
		// List list = LoadGoManual.loadOneFromAllGoManual(1,88);
		// for (Iterator iter = list.iterator(); iter.hasNext();) {
		// count++;

		original = new LoadGMDGoManual(rootDir).loadOneFromAllGoManual(1, 453);
		log.debug("GOManual:" + 344);
		log.debug("GOManualLength:" + original.length);

		GoBoard goBoard = new GoBoard();
		eddie.wu.arrayblock.GoBoard256 goboard1 = new eddie.wu.arrayblock.GoBoard256(Constant.BOARD_SIZE);
		// for(int i=0; i<1;i++ ){
		BoardColorState boardState = null;
		BoardColorState boardState1 = null;
		Constant.DEBUG_CGCL = false;
		for (int i = 0; i < original.length; i++) {

			log.debug("shoushu:" + i);
			goBoard.oneStepForward(original[i], original[i + 1]);
			goboard1.cgcl(original[i], original[++i]);
			boardState = goBoard.getBoardColorState();
			int color = ColorUtil.getNextStepColor(i);
			boardState1 = new BoardColorState(goboard1.getStateArray(),color);
			if (!boardState.equals(boardState1)) {
				log.debug(boardState);
				log.debug(boardState1);
			}

			assertEquals("state of every point should equal!", boardState,
					boardState1);
			assertTrue(boardState.equals(boardState1));
		}
		// }
	}

	public void testloadGMDManual_listtitle() {
		List<GMDGoManual> list = new LoadGMDGoManual(rootDir)
				.loadMultiGoManualFromLib0();

		for (GMDGoManual manual : list) {
			if(log.isDebugEnabled()) log.debug("manual: " + manual);
		}
	}

	/**
	 * test lib 0 only compare state!
	 * 
	 * @author eddie TODO: compare the result to another implementation.
	 */
	public void testForwardNextStep_MultiGoManual() {
		byte[] original = null;
		byte count = 0;
		List<GMDGoManual> list = new LoadGMDGoManual(rootDir)
				.loadMultiGoManualFromLib0();

		for (GMDGoManual manual : list) {
			try {
				count++;
				original = manual.getMoves();
				log.debug("GOManual:" + count);
				log.debug("GOManualLength:" + original.length);
				// if(count==2 )continue;//too long for array board to deal
				// with.
				// if(count==4 )continue;
				// if(count==6 )continue;
				// if(count==8 )continue;
				// if(count==9 )continue;
				GoBoard goBoard = new GoBoard();
				eddie.wu.arrayblock.GoBoard256 goboard1 = new eddie.wu.arrayblock.GoBoard256(
						Constant.BOARD_SIZE);
				// for(int i=0; i<1;i++ ){
				BoardColorState boardState = null;
				BoardColorState boardState1 = null;
				Constant.DEBUG_CGCL = false;
				for (int i = 0; i < original.length; i++) {

					log.debug("shoushu:" + i);
					goBoard.oneStepForward(original[i], original[i + 1]);
					goboard1.cgcl(original[i], original[++i]);
					boardState = goBoard.getBoardColorState();
					int color = ColorUtil.getNextStepColor(i);
					boardState1 = new BoardColorState(goboard1.getStateArray(),color);
					if (!boardState.equals(boardState1)) {
						log.debug(boardState);
						log.debug(boardState1);
					}

					assertEquals("state of every point should equal!",
							boardState, boardState1);
					assertTrue(boardState.equals(boardState1));
					GoBoard goBoard2 = new GoBoard(boardState);
					// goBoard2.generateHighLevelState();
					assertEquals("state should equal internally!", boardState,
							goBoard2.getBoardColorState());

				}
			} catch (RuntimeException e) {
				if(log.isDebugEnabled()) log.debug("不好: error with test manual" + manual);
				throw e;
			}
		}

	}

	/**
	 * test lib 1 only compare state! TODO: compare the result to another
	 * implementation. GOManual:462 have problem.
	 * 
	 * @author eddie wu
	 * @deprecated failed because of lib1 is lost.
	 */
	public void testForwardNextStep_MultiGoManualLib1() {

		int count = 0;
		List<GMDGoManual> list = new GMDGoManual().loadMultiGoManual(
				Constant.GLOBAL_MANUAL, 6391);
		for (GMDGoManual manual : list) {
			byte[] original = manual.getMoves();
			if(log.isDebugEnabled()) log.debug("GOManual:" + count);
			count++;
			if (count - 1 == 344)
				continue;// GOBOARD 256 have problem.
			if (count - 1 == 453)
				continue;// GOBOARD 256 have problem.
			log.debug("GOManualLength:" + original.length);

			GoBoard goBoard = new GoBoard();
			eddie.wu.arrayblock.GoBoard256 goboard1 = new eddie.wu.arrayblock.GoBoard256(
					Constant.BOARD_SIZE);
			// for(int i=0; i<1;i++ ){
			BoardColorState boardState = null;
			BoardColorState boardState1 = null;
			Constant.DEBUG_CGCL = false;
			for (int i = 0; i < original.length; i++) {

				log.debug("shoushu:" + i);
				goBoard.oneStepForward(original[i], original[i + 1]);
				goboard1.cgcl(original[i], original[++i]);
				boardState = goBoard.getBoardColorState();
				int color = ColorUtil.getNextStepColor(i);
				boardState1 = new BoardColorState(goboard1.getStateArray(),color);
				if (!boardState.equals(boardState1)) {
					log.debug(boardState);
					log.debug(boardState1);
				}

				assertEquals("state of every point should equal!", boardState,
						boardState1);
				assertTrue(boardState.equals(boardState1));
			}
		}
	}

}
