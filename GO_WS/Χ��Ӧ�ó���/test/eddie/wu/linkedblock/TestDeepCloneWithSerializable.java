package eddie.wu.linkedblock;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;
import eddie.wu.manual.LoadGMDGoManual;

public class TestDeepCloneWithSerializable extends TestCase{
	Log log = LogFactory.getLog(TestDeepCloneWithSerializable.class);
	public TestDeepCloneWithSerializable(){
		
	}
	public TestDeepCloneWithSerializable(String str){
		super(str);
	}
	/**
	 * @deprecated
	 */
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
		original = new LoadGMDGoManual("doc/Î§Æå´òÆ×Èí¼þ/").loadSingleGoManual();
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
		original = new LoadGMDGoManual("doc/Î§Æå´òÆ×Èí¼þ/").loadSingleGoManual();
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

		BoardColorState boardState = null;
		
		log.debug("original.length=" + original.length);		
		int loopCount=original.length;
		loopCount=18;
		for (int i = 0; i < loopCount; i += 2) {
			log.info("shoushu=" + (i + 3) / 2);
			log.info("a=" + original[i]);
			log.info("b=" + original[i + 1]);
			
			
			
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
		}
	}
}
