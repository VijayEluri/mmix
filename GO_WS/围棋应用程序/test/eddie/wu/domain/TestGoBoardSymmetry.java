package eddie.wu.domain;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import util.GBKToUTF8;

public class TestGoBoardSymmetry extends TestCase{
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	
	public void testSymmetry(){
		GoBoard goBoard=new GoBoard(Constant.BOARD_SIZE);
//		assertTrue(goBoard.isBlackTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalMiddleLine());
		assertTrue(goBoard.verticalSymmetry());
		assertTrue(goBoard.horizontalSymmetry());
		
		goBoard.oneStepForward(3,3);
		if(log.isDebugEnabled()) log.debug("first Step");
		if(log.isDebugEnabled()) log.debug(goBoard.getBoardColorState());
//		assertTrue(!goBoard.isBlackTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalMiddleLine());
		assertTrue(!goBoard.verticalSymmetry());
		assertTrue(!goBoard.horizontalSymmetry());
		assertTrue(goBoard.backwardSlashSymmetry());
		assertTrue(!goBoard.forwardSlashSymmetry());
		
		goBoard.oneStepForward(3,17);
		if(log.isDebugEnabled()) log.debug("second Step");
		if(log.isDebugEnabled()) log.debug(goBoard.getBoardColorState());
//		assertTrue(goBoard.isBlackTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalMiddleLine());
		assertTrue(!goBoard.verticalSymmetry());
		assertTrue(!goBoard.horizontalSymmetry());
		assertTrue(!goBoard.backwardSlashSymmetry());
		assertTrue(!goBoard.forwardSlashSymmetry());
		
		goBoard.oneStepForward(17,3);
		if(log.isDebugEnabled()) log.debug("third Step");
		if(log.isDebugEnabled()) log.debug(goBoard.getBoardColorState());
//		assertTrue(goBoard.isWhiteTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalMiddleLine());
		assertTrue(!goBoard.verticalSymmetry());
		assertTrue(!goBoard.horizontalSymmetry());
		assertTrue(!goBoard.backwardSlashSymmetry());
		assertTrue(!goBoard.forwardSlashSymmetry());
		
		goBoard.oneStepForward(17,17);
		if(log.isDebugEnabled()) log.debug("the forth Step");
		if(log.isDebugEnabled()) log.debug(goBoard.getBoardColorState());
//		assertTrue(goBoard.isBlackTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalMiddleLine());
		assertTrue(!goBoard.verticalSymmetry());
		assertTrue(goBoard.horizontalSymmetry());
		assertTrue(!goBoard.backwardSlashSymmetry());
		assertTrue(!goBoard.forwardSlashSymmetry());
	}
	
	public void testIsBlankTurn(){
		GoBoard goBoard=new GoBoard();
//		assertTrue(goBoard.isBlackTurn());		
	}
	public void testIsWhiteTurn(){
		GoBoard goBoard=new GoBoard();
//		assertFalse(goBoard.isWhiteTurn());
	}
}
