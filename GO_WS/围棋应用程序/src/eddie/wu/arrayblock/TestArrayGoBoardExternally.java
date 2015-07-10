/*
 * Created on 2005-4-21
 * bug fix 1: when I add a block into the set, it has a hashcode A.
 * then the block is changed and will has another hashcode B.
 * so when i compare two set. it will return false though they are equals in fact.
 * 
 */
package eddie.wu.arrayblock;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.manual.GMDGoManual;
import eddie.wu.manual.LoadGMDGoManual;

/**
 * 
 * Test domain.GoBoard class vs. arrayblock.GoBoard
 * 
 * @author eddie
 * 
 * 
 */
public class TestArrayGoBoardExternally extends TestCase {
	private static final String rootDir = Constant.rootDir;

	private static final Logger log = Logger
			.getLogger(TestArrayGoBoardExternally.class);

	public TestArrayGoBoardExternally(String string) {
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
		Logger logger = Logger.getLogger(GoBoard.class);
		if (logger.getLevel().isGreaterOrEqual(Level.INFO)) {

		} else {
			logger.setLevel(Level.INFO);
		}
		Logger logger1 = Logger.getLogger(TestArrayGoBoardExternally.class);
		if (logger1.getLevel().isGreaterOrEqual(Level.INFO)) {

		} else {
			logger1.setLevel(Level.INFO);
		}
		// logger.setLevel(Level.DEBUG);
		byte[] original = null;
		original = new LoadGMDGoManual(rootDir).loadSingleGoManual().getMoves();
		helperTestMethod(original);

	}

	/**
	 * not only compoare state between different implementation! but also
	 * compare different algorithm.
	 * 
	 * @author eddie
	 */
	public void testForwardNextStep_MultiGoManual() {
		Logger logger = Logger.getLogger(GoBoard.class);
		if (logger.getLevel().isGreaterOrEqual(Level.ERROR)) {

		} else {
			logger.setLevel(Level.ERROR);
		}

		byte count = 0;
		List<GMDGoManual> list = new LoadGMDGoManual(rootDir)
				.loadMultiGoManualFromLib0();
		for (GMDGoManual manual : list) {
			byte[] original = manual.getMoves();
			count++;
			log.debug("GOManual:" + count);
			log.debug("GOManualLength:" + original.length);
			if (count == 2)
				continue;// too long for array board to deal with.
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
	 * java.lang.ArrayIndexOutOfBoundsException: -128 at
	 * eddie.wu.arrayblock.GoBoard.cgcl(GoBoard.java:416) at
	 * eddie.wu.linkedblock
	 * .TestGoBoardExternally.helper(TestGoBoardExternally.java:69) at
	 * eddie.wu.linkedblock
	 * .TestGoBoardExternally.testForwardNextStepFromOneOfMultiGoManualLib1
	 * (TestGoBoardExternally.java:165) at
	 * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) at
	 * sun.reflect
	 * .NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39) at
	 * sun. reflect.DelegatingMethodAccessorImpl.invoke(
	 * DelegatingMethodAccessorImpl .java:25) at
	 * java.lang.reflect.Method.invoke(Method.java:324) at
	 * junit.framework.TestCase.runTest(TestCase.java:154) at
	 * junit.framework.TestCase.runBare(TestCase.java:127) at
	 * junit.framework.TestResult$1.protect(TestResult.java:106) at
	 * junit.framework.TestResult.runProtected(TestResult.java:124) at
	 * junit.framework.TestResult.run(TestResult.java:109) at
	 * junit.framework.TestCase.run(TestCase.java:118) at
	 * org.eclipse.jdt.internal
	 * .junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:478) at
	 * org.
	 * eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner
	 * .java:344) at
	 * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(
	 * RemoteTestRunner.java:196)
	 * 
	 * since Array go Board is not so good.
	 * 
	 */
	public void testForwardNextStep_FromOneOfMultiGoManualLib1() {
		byte[] original = null;
		// byte count = 0;
		// List list = LoadGoManual.loadOneFromAllGoManual(1,88);
		// for (Iterator iter = list.iterator(); iter.hasNext();) {
		// count++;
		Logger logger = Logger.getLogger(GoBoard.class);
		logger.setLevel(Level.INFO);
		int libId = 0;
		int manualId = 4;
		int shoushu = 183;
		original = new LoadGMDGoManual(rootDir).loadOneFromAllGoManual(libId,
				manualId);
		log.debug("GOManual:" + libId + ":" + manualId);
		log.debug("GOManualLength:" + original.length);
		assertEquals(shoushu * 2, original.length);
		helperTestMethod(original);
	}

	private void helperTestMethod(byte[] original) {
		helperTestMethod_External(original);
		helperTestMethod_Internal(original);
	}

	/**
	 * real test method--shared
	 * 
	 * arrayBlock.GoBoard vs. domain.GoBoard
	 * 
	 * @param original
	 */
	private void helperTestMethod_External(byte[] original) {
		GoBoard linkedGoBoard = new GoBoard();
		eddie.wu.arrayblock.ArrayGoBoard arrayGoBoard = new eddie.wu.arrayblock.ArrayGoBoard(
				Constant.BOARD_SIZE);

		BoardColorState colorStateLinked = null;
		BoardColorState colorStateArrayed = null;
		Constant.DEBUG_CGCL = false;

		for (int i = 0; i < original.length; i += 2) {
			if (log.isInfoEnabled()) {
				log.debug("shoushu=" + (i + 3) / 2);
				log.debug("a=" + original[i]);
				log.debug("b=" + original[i + 1]);
			}
			linkedGoBoard.oneStepForward(original[i], original[i + 1]);
			arrayGoBoard.cgcl(original[i], original[i + 1]);

			// 1.breath of every point should be equal!
			assertEquals("breath of every point should be equal",
					linkedGoBoard.getBoardBreathState(),
					arrayGoBoard.getBoardBreathState());

			colorStateLinked = linkedGoBoard.getBoardColorState();
			colorStateArrayed = arrayGoBoard.getBoardState();

			// 2 color of every point should be equal.
			assertEquals("color of every point should be equal!",
					colorStateLinked, colorStateArrayed);
			assertTrue(colorStateLinked.equals(colorStateArrayed));

		}
	}

	/**
	 * TODO: not done yet. domain.GoBoard accumulate states vs. state generated
	 * in one step.
	 * 
	 * @param original
	 */
	private void helperTestMethod_Internal(byte[] original) {
		GoBoard goBoard = new GoBoard();
		GoBoard goBoard2;
		BoardColorState boardColorState;
		Constant.DEBUG_CGCL = false;

		for (int i = 0; i < original.length; i += 2) {
			if(log.isEnabledFor(Level.WARN)) log.warn("shoushu=" + (i + 3) / 2);
			if(log.isEnabledFor(Level.WARN)) log.warn("a=" + original[i]);
			if(log.isEnabledFor(Level.WARN)) log.warn("b=" + original[i + 1]);
			goBoard.oneStepForward(original[i], original[i + 1]);

			// 3.redundant with the code in TEstGoBoardInternally
			boardColorState = goBoard.getBoardColorState();
			goBoard2 = new GoBoard(boardColorState);
			// goBoard2.generateHighLevelState();

			assertEquals("color state should equal internally!",
					boardColorState, goBoard2.getBoardColorState());
			assertEquals("breath state should equal internally!",
					goBoard.getBoardBreathState(),
					goBoard2.getBoardBreathState());

			Set<Block> blackBlocks = goBoard.getBlackBlocksOnTheFly();
			Set<Block> blackBlocks2 = goBoard2.getBlackBlocksOnTheFly();
			// Block blackBlock = blackBlocksFromState.iterator().next();
			// if(log.isDebugEnabled()) log.debug(blackBlock.hashCode());
			// Block blackBlock2 = blackBlocksFromState2.iterator().next();
			// if(log.isDebugEnabled()) log.debug(blackBlock2.hashCode());
			// found a bug because of fly weight pattern of class Point is
			// broken.
			// assertTrue(blackBlock.equals(blackBlock2));
			// assertTrue(blackBlocksFromState.contains(blackBlock2));
			// assertTrue(blackBlocksFromState.containsAll(blackBlocksFromState2));
			// assertTrue(blackBlocksFromState.equals(blackBlocksFromState2));
			assertEquals("black block should equal!", blackBlocks, blackBlocks2);

			Set<Block> whiteBlocks = goBoard.getWhiteBlocks();
			Set<Block> whiteBlocks2 = goBoard2.getWhiteBlocks();
			assertTrue(whiteBlocks.equals(whiteBlocks2));
			assertEquals("white block should equal!", whiteBlocks, whiteBlocks2);

			Set<BlankBlock> blankBlocks = goBoard.getBlankBlocksOnTheFly();
			Set<BlankBlock> blankBlocks2 = goBoard2.getBlankBlocksOnTheFly();
			assertEquals("blank block should equal!", blankBlocks, blankBlocks2);

		}
	}

}