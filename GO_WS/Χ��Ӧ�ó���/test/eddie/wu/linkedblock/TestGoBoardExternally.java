/*
 * Created on 2005-4-21
 * bug fix 1: when I add a block into the set, it has a hashcode A.
 * then the block is changed and will has another hashcode B.
 * so when i compare two set. it will return false though they are equals in fact.
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

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.manual.LoadGoManual;

/**
 * @author eddie
 * 
 * Test GOBoard class
 */
public class TestGoBoardExternally extends TestCase {
	private static final String rootDir = "doc/Î§Æå´òÆ×Èí¼þ/";
    private static final Log log = LogFactory.getLog(TestGoBoardExternally.class);

    public TestGoBoardExternally(String string) {
    	super(string);		
	}

	public void testValidate() {
        GoBoard goBoard = new GoBoard();
        assertTrue(goBoard.validate((byte) 11, (byte) 12));
        assertTrue(goBoard.verify1());

    }

    /**
     * only compare state! compare the result to another implementation.
     * 
     * @author eddie
     */
    public void testForwardNextStep_SingleGoManual() {
    	Logger logger=Logger.getLogger(GoBoard.class);
    	if(logger.getLevel().isGreaterOrEqual(Level.INFO)){
    		
    	}else{
    		logger.setLevel(Level.INFO);
    	}
    	Logger logger1=Logger.getLogger(TestGoBoardExternally.class);
    	if(logger1.getLevel().isGreaterOrEqual(Level.INFO)){
    		
    	}else{
    		logger1.setLevel(Level.INFO);
    	}
    	//logger.setLevel(Level.DEBUG);
        byte[] original = null;
        original = new LoadGoManual(rootDir).loadSingleGoManual();
        helperTestMethod(original);

    }

	
   

    /**
     * not only compoare state between different implementation! but also
     * compare different algorithm.
     * 
     * @author eddie
     */
    public void testForwardNextStep_MultiGoManual() {
    	Logger logger=Logger.getLogger(GoBoard.class);
    	if(logger.getLevel().isGreaterOrEqual(Level.ERROR)){
    		
    	}else{
    		logger.setLevel(Level.ERROR);
    	}
        byte[] original = null;
        byte count = 0;
        List list = new LoadGoManual(rootDir).loadMultiGoManualFromLib0();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            count++;

            original = (byte[]) iter.next();
            log.debug("GOManual:" + count);
            log.debug("GOManualLength:" + original.length);
            if (count == 2)
                continue;//too long for array board to deal with.
            if (count == 4)
                continue;
            if (count == 6)
                continue;
            if (count == 8)
                continue;
            if (count == 9)
                continue;
            helperTestMethod(original);
        }
    }

    /**
     * java.lang.ArrayIndexOutOfBoundsException: -128
	at eddie.wu.arrayblock.GoBoard.cgcl(GoBoard.java:416)
	at eddie.wu.linkedblock.TestGoBoardExternally.helper(TestGoBoardExternally.java:69)
	at eddie.wu.linkedblock.TestGoBoardExternally.testForwardNextStepFromOneOfMultiGoManualLib1(TestGoBoardExternally.java:165)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:324)
	at junit.framework.TestCase.runTest(TestCase.java:154)
	at junit.framework.TestCase.runBare(TestCase.java:127)
	at junit.framework.TestResult$1.protect(TestResult.java:106)
	at junit.framework.TestResult.runProtected(TestResult.java:124)
	at junit.framework.TestResult.run(TestResult.java:109)
	at junit.framework.TestCase.run(TestCase.java:118)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:478)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:344)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:196)

since Array go Board is not so good.
     *
     */
    public void testForwardNextStep_FromOneOfMultiGoManualLib1() {
        byte[] original = null;
        //byte count = 0;
        //List list = LoadGoManual.loadOneFromAllGoManual(1,88);
        //		for (Iterator iter = list.iterator(); iter.hasNext();) {
        //			count++;
        Logger logger=Logger.getLogger(GoBoard.class);
    	logger.setLevel(Level.INFO);
    	int libId=0;
    	int manualId=4;
    	int shoushu=183;
        original = new LoadGoManual(rootDir).loadOneFromAllGoManual(libId, manualId);
        log.debug("GOManual:"+libId+":" + manualId);
        log.debug("GOManualLength:" + original.length);
        assertEquals(shoushu*2,original.length);
        helperTestMethod(original);        
    }
    /**
     * real test method--shared
     * @param original
     */
    private void helperTestMethod(byte[] original) {
		GoBoard linkedGoBoard = new GoBoard();
		GoBoard linkedGoBoard2 = new GoBoard();
        eddie.wu.arrayblock.GoBoard arrayGoboard = new eddie.wu.arrayblock.GoBoard();
        
        BoardColorState boardStateLinked = null;
        BoardColorState boardStateArrayed = null;
        Constant.DEBUG_CGCL = false;
        
        for (int i = 0; i < original.length; i++) {
            if (log.isInfoEnabled()) {
                log.debug("shoushu=" + (i + 3) / 2);
                log.debug("a=" + original[i]);
                log.debug("b=" + original[i + 1]);
            }
            linkedGoBoard.oneStepForward(original[i], original[i + 1]);
            arrayGoboard.cgcl(original[i], original[++i]);
            
           //1.breath of every point should be equal!
            
            assertEquals("breath of every point should be equal",
            		linkedGoBoard.getBoardBreathState(),
            				arrayGoboard.getBoardBreathState()	);
            
            boardStateLinked = linkedGoBoard.getBoardColorState();
            boardStateArrayed = arrayGoboard.getBoardState();
            
            //2 color of every point should be equal.
            assertEquals("color of every point should be equal!", boardStateLinked,
                    boardStateArrayed);
            assertTrue(boardStateLinked.equals(boardStateArrayed));

            //3.redundant with the code in TEstGoBoardInternally
            linkedGoBoard2 = new GoBoard(boardStateLinked,(i + 3) / 2);
            linkedGoBoard2.generateHighLevelState();
            assertEquals("color state should equal internally!", boardStateLinked,
                    linkedGoBoard2.getBoardColorState());
            assertEquals("breath state should equal internally!", linkedGoBoard.getBoardBreathState(),
                    linkedGoBoard2.getBoardBreathState());
            
            assertEquals("black block should equal!", linkedGoBoard
                    .getBlackBlocksFromState(), linkedGoBoard2
                    .getBlackBlocksFromState());
            assertEquals("white block should equal!", linkedGoBoard
                    .getWhiteBlocksFromState(), linkedGoBoard2
                    .getWhiteBlocksFromState());
            assertEquals("blank block should equal!", linkedGoBoard
                    .getBlankPointBlocksFromState(), linkedGoBoard2.getBlankPointBlocksFromState());

        }
	}

}