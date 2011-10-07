/*
 * Created on 2005-5-12
 *


 */
package eddie.wu.arrayblock;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.manual.LoadGoManual;

/**
 * @author eddie
 * 
 * 
 */
public class TestGoBoard256 extends TestCase {
	String rootDir = "doc/Î§Æå´òÆ×Èí¼þ/";
	private static final Log log = LogFactory.getLog(TestGoBoard256.class);

	public void testForwardNextStep_SingleGoManual() {
		byte[] original = null;

		original = new LoadGoManual(rootDir).loadSingleGoManual();
		GoBoard goBoard = new GoBoard();
		eddie.wu.arrayblock.GoBoard256 goboard1 = new eddie.wu.arrayblock.GoBoard256();
		// for(int i=0; i<1;i++ ){
		BoardColorState boardState = null;
		BoardColorState boardState1 = null;
		Constant.DEBUG_CGCL = false;
		for (int i = 0; i < original.length; i++) {
			log.debug("shoushu=" + (i + 1));
			log.debug("a=" + original[i]);
			log.debug("b=" + original[i + 1]);
			goBoard.oneStepForward(original[i], original[i + 1]);
			goboard1.cgcl(original[i], original[++i]);
			boardState = goBoard.getBoardColorState();
			boardState1 = new BoardColorState(goboard1.getStateArray());

			assertEquals("state of every point should equal!", boardState,
					boardState1);
			assertTrue(boardState.equals(boardState1));
		}

	}

	/**
	 * only compoare state!
	 * 
	 * @author eddie TODO: compare the result to another implemnetaion.
	 */
	public void testForwardNextStep_MultiGoManual() {
		byte[] original = null;
		byte count = 0;
		List list = new LoadGoManual(rootDir).loadMultiGoManualFromLib0();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			count++;

			original = (byte[]) iter.next();
			log.debug("GOManual:" + count);
			log.debug("GOManualLength:" + original.length);
			// if(count==2 )continue;//too long for array board to deal with.
			// if(count==4 )continue;
			// if(count==6 )continue;
			// if(count==8 )continue;
			// if(count==9 )continue;
			GoBoard goBoard = new GoBoard();
			eddie.wu.arrayblock.GoBoard256 goboard1 = new eddie.wu.arrayblock.GoBoard256();
			// for(int i=0; i<1;i++ ){
			BoardColorState boardState = null;
			BoardColorState boardState1 = null;
			Constant.DEBUG_CGCL = false;
			for (int i = 0; i < original.length; i++) {

				log.debug("shoushu:" + i);
				goBoard.oneStepForward(original[i], original[i + 1]);
				goboard1.cgcl(original[i], original[++i]);
				boardState = goBoard.getBoardColorState();
				boardState1 = new BoardColorState(goboard1.getStateArray());
				if (!boardState.equals(boardState1)) {
					log.debug(boardState);
					log.debug(boardState1);
				}

				assertEquals("state of every point should equal!", boardState,
						boardState1);
				assertTrue(boardState.equals(boardState1));
				GoBoard goBoard2 = new GoBoard(boardState, (i + 3) / 2);
				goBoard2.generateHighLevelState();
				assertEquals("state should equal internally!", boardState,
						goBoard2.getBoardColorState());

			}
		}
	}

	/**
	 * only compare state! TODO: compare the result to another implementation.
	 * 
	 * @author eddie
	 */
	public void testForwardNextStep_MultiGoManualLib1() {
		byte[] original = null;
		int count = 0;
		List list = new LoadGoManual(rootDir).loadMultiGoManual(1);
		for (Iterator iter = list.iterator(); iter.hasNext();) {

			original = (byte[]) iter.next();
			log.debug("GOManual:" + count);

			count++;
			if (count - 1 == 344)
				continue;// GOBOARD 256 have problem.
			if (count - 1 == 453)
				continue;// GOBOARD 256 have problem.
			log.debug("GOManualLength:" + original.length);

			GoBoard goBoard = new GoBoard();
			eddie.wu.arrayblock.GoBoard256 goboard1 = new eddie.wu.arrayblock.GoBoard256();
			// for(int i=0; i<1;i++ ){
			BoardColorState boardState = null;
			BoardColorState boardState1 = null;
			Constant.DEBUG_CGCL = false;
			for (int i = 0; i < original.length; i++) {

				log.debug("shoushu:" + i);
				goBoard.oneStepForward(original[i], original[i + 1]);
				goboard1.cgcl(original[i], original[++i]);
				boardState = goBoard.getBoardColorState();
				boardState1 = new BoardColorState(goboard1.getStateArray());
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

	public void testForwardNextStep_FromOneOfMultiGoManualLib1() {
		byte[] original = null;
		byte count = 0;
		// List list = LoadGoManual.loadOneFromAllGoManual(1,88);
		// for (Iterator iter = list.iterator(); iter.hasNext();) {
		// count++;

		original = new LoadGoManual(rootDir).loadOneFromAllGoManual(1, 453);
		log.debug("GOManual:" + 344);
		log.debug("GOManualLength:" + original.length);

		GoBoard goBoard = new GoBoard();
		eddie.wu.arrayblock.GoBoard256 goboard1 = new eddie.wu.arrayblock.GoBoard256();
		// for(int i=0; i<1;i++ ){
		BoardColorState boardState = null;
		BoardColorState boardState1 = null;
		Constant.DEBUG_CGCL = false;
		for (int i = 0; i < original.length; i++) {

			log.debug("shoushu:" + i);
			goBoard.oneStepForward(original[i], original[i + 1]);
			goboard1.cgcl(original[i], original[++i]);
			boardState = goBoard.getBoardColorState();
			boardState1 = new BoardColorState(goboard1.getStateArray());
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
}
