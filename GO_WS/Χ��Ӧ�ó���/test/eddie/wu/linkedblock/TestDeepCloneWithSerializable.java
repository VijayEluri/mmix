package eddie.wu.linkedblock;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.manual.LoadGoManual;

public class TestDeepCloneWithSerializable extends TestCase{
	Log log = LogFactory.getLog(TestDeepCloneWithSerializable.class);
	public TestDeepCloneWithSerializable(){
		
	}
	public TestDeepCloneWithSerializable(String str){
		super(str);
	}
	
	public void testDeepClone() {
		Logger logger = Logger.getLogger(GoBoard.class);
		if (logger.getLevel().isGreaterOrEqual(Level.INFO)) {

		} else {
			logger.setLevel(Level.INFO);
		}
		Logger logger1 = Logger.getLogger(TestDeepCloneWithSerializable.class);
		if (logger1.getLevel().isGreaterOrEqual(Level.INFO)) {
			logger1.setLevel(Level.DEBUG);
		} else {
			logger1.setLevel(Level.DEBUG);
		}
		byte[] original = null;
		original = new LoadGoManual("doc/Î§Æå´òÆ×Èí¼þ/").loadSingleGoManual();
		helperTestMethod(original);
		log.info("success of testDeepClone");
	}
	public void testEquals() {
		Logger logger = Logger.getLogger(GoBoard.class);
		if (logger.getLevel().isGreaterOrEqual(Level.INFO)) {

		} else {
			logger.setLevel(Level.INFO);
		}
		Logger logger1 = Logger.getLogger(TestDeepCloneWithSerializable.class);
		if (logger1.getLevel().isGreaterOrEqual(Level.INFO)) {
			logger1.setLevel(Level.DEBUG);
		} else {
			logger1.setLevel(Level.DEBUG);
		}
		byte[] original = null;
		original = new LoadGoManual("doc/Î§Æå´òÆ×Èí¼þ/").loadSingleGoManual();
		helperTestMethodForEquals(original);
		log.info("success of testDeepClone");
	}
	public void helperTestMethodForEquals(byte[] original) {
		GoBoard goBoard = new GoBoard();		
		GoBoard goBoard2 = null;
		BoardColorState boardState = null;


		
		log.debug("original.length=" + original.length);	
		int loopCount=original.length;
		//loopCount=1;
		for (int i = 0; i <loopCount ; i += 2) {
			log.info("shoushu=" + (i + 3) / 2);
			log.info("a=" + original[i]);
			log.info("b=" + original[i + 1]);
			
			
			goBoard.oneStepForward(original[i], original[i + 1]);			
			boardState = goBoard.getBoardColorState();
			goBoard2 = new GoBoard(boardState, (i + 3) / 2);
			goBoard2.generateHighLevelState();
			
			assertNotSame(goBoard,goBoard2);
			assertEquals(goBoard,goBoard2);
		}
	}
	public void helperTestMethod(byte[] original) {
		GoBoard goBoard = new GoBoard();
		GoBoard goBoardClone = new GoBoard();
		GoBoard goBoard2 = null;
//		GoBoard goBoard4 = null;
//		GoBoard goBoard5 = null;
		BoardColorState boardState = null;
//		BoardColorState boardState2 = null;
//		BoardColorState boardState3 = null;
		
		log.debug("original.length=" + original.length);		
		int loopCount=original.length;
		loopCount=18;
		for (int i = 0; i < loopCount; i += 2) {
			log.info("shoushu=" + (i + 3) / 2);
			log.info("a=" + original[i]);
			log.info("b=" + original[i + 1]);
			
			
			//goBoardClone=goBoard.deepClone();
			//assertNotSame(goBoard,goBoardClone);
			//assertEquals(goBoard,goBoardClone);
			
			goBoard.oneStepForward(original[i], original[i + 1]);
			
			boardState = goBoard.getBoardColorState();

			goBoard2 = new GoBoard(boardState, (i + 3) / 2);
			goBoard2.generateHighLevelState();
			assertNotSame(goBoard,goBoard2);
			assertEquals(goBoard,goBoard2);
			
			goBoardClone=goBoard.deepClone();
			assertNotSame(goBoard,goBoardClone);
			assertEquals(goBoard,goBoardClone);
			log.info("new created goboard clone succes:"+i);
			
//			boardState2 = goBoard2.getBoardColorState();
//			log.debug("boardState:" + boardState);
//			log.debug("boardState2:" + boardState2);
//			assertEquals("state should equal internally!", boardState, goBoard2
//					.getBoardColorState());
//			log.debug("getBlackBlocksFromState:"
//					+ goBoard.getBlackBlocksFromState());
//			log.debug("getBlackBlocksFromState:"
//					+ goBoard2.getBlackBlocksFromState());
//			assertEquals("black block should equal!", goBoard
//					.getBlackBlocksFromState(), goBoard2
//					.getBlackBlocksFromState());
//			log.debug("getWhiteBlocksFromState:"
//					+ goBoard.getWhiteBlocksFromState());
//			log.debug("getWhiteBlocksFromState:"
//					+ goBoard2.getWhiteBlocksFromState());
//			assertEquals("white block should equal!", goBoard
//					.getWhiteBlocksFromState(), goBoard2
//					.getWhiteBlocksFromState());
//
//			// test clone
//			GoBoard goBoard3 = goBoard2.deepClone();
//			log.debug("new created goboard clone succes");
//			goBoard.deepClone();
//			log.debug("after forwardstep goboard clone succes");
//			boardState3 = goBoard3.getBoardColorState();
//			log.debug("boardState:" + boardState2);
//			log.debug("boardState2:" + boardState3);
//			assertEquals("state should equal internally!", boardState2,
//					goBoard3.getBoardColorState());
//
//			assertEquals("black block should equal!", goBoard2
//					.getBlackBlocksFromState(), goBoard3
//					.getBlackBlocksFromState());
//
//			assertEquals("white block should equal!", goBoard2
//					.getWhiteBlocksFromState(), goBoard3
//					.getWhiteBlocksFromState());
//			assertNotNull(goBoard3.getPoints());
//			byte ii, j;
//			for (ii = 1; ii < 20; ii++) {
//				for (j = 1; j < 20; j++) {
//					assertNotNull(goBoard3.getPoints()[ii][j]);
//				}
//			}

//			if (goBoard4 != null) {
//				log.debug("a=" + original[i]);
//				log.debug("b=" + original[i + 1]);
//				goBoard4.forwardOneStep(original[i], original[i + 1]);
//				assertEquals("state should equal internally!", boardState,
//						goBoard4.getBoardColorState());
//
//				assertEquals("black block should equal!", goBoard
//						.getBlackBlocksFromState(), goBoard4
//						.getBlackBlocksFromState());
//
//				assertEquals("white block should equal!", goBoard
//						.getWhiteBlocksFromState(), goBoard4
//						.getWhiteBlocksFromState());
//			} else {
//				goBoard4 = goBoard2;
//			}
//
//			if (goBoard5 != null) {
//				log.debug("a=" + original[i]);
//				log.debug("b=" + original[i + 1]);
//				goBoard5.forwardOneStep(original[i], original[i + 1]);
//				assertEquals("state should equal internally!", boardState,
//						goBoard5.getBoardColorState());
//
//				assertEquals("black block should equal!", goBoard
//						.getBlackBlocksFromState(), goBoard5
//						.getBlackBlocksFromState());
//
//				assertEquals("white block should equal!", goBoard
//						.getWhiteBlocksFromState(), goBoard5
//						.getWhiteBlocksFromState());
//			} else {
//				goBoard5 = goBoard3;
//			}
		}
	}
}
