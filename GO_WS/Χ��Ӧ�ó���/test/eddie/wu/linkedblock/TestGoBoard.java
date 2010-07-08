package eddie.wu.linkedblock;

import junit.framework.TestCase;

public class TestGoBoard extends TestCase{
	public void testSymmetry(){
		GoBoard goBoard=new GoBoard();
		assertTrue(goBoard.isBlackTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalLine());
		assertTrue(goBoard.verticalSymmetry());
		assertTrue(goBoard.horizontalSymmetry());
		
		goBoard.oneStepForward(3,3);
		System.out.println("first Step");
		System.out.println(goBoard.getBoardColorState());
		assertTrue(!goBoard.isBlackTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalLine());
		assertTrue(!goBoard.verticalSymmetry());
		assertTrue(!goBoard.horizontalSymmetry());
		assertTrue(goBoard.backwardSlashSymmetry());
		assertTrue(!goBoard.forwardSlashSymmetry());
		
		goBoard.oneStepForward(3,17);
		System.out.println("second Step");
		System.out.println(goBoard.getBoardColorState());
		assertTrue(goBoard.isBlackTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalLine());
		assertTrue(!goBoard.verticalSymmetry());
		assertTrue(!goBoard.horizontalSymmetry());
		assertTrue(!goBoard.backwardSlashSymmetry());
		assertTrue(!goBoard.forwardSlashSymmetry());
		
		goBoard.oneStepForward(17,3);
		System.out.println("third Step");
		System.out.println(goBoard.getBoardColorState());
		assertTrue(goBoard.isWhiteTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalLine());
		assertTrue(!goBoard.verticalSymmetry());
		assertTrue(!goBoard.horizontalSymmetry());
		assertTrue(!goBoard.backwardSlashSymmetry());
		assertTrue(!goBoard.forwardSlashSymmetry());
		
		goBoard.oneStepForward(17,17);
		System.out.println("the forth Step");
		System.out.println(goBoard.getBoardColorState());
		assertTrue(goBoard.isBlackTurn());
		assertEquals(0,goBoard.pointsInHorizontalLine());
		assertEquals(0,goBoard.pointsInVerticalLine());
		assertTrue(!goBoard.verticalSymmetry());
		assertTrue(goBoard.horizontalSymmetry());
		assertTrue(!goBoard.backwardSlashSymmetry());
		assertTrue(!goBoard.forwardSlashSymmetry());
	}
	
	public void testIsBlankTurn(){
		GoBoard goBoard=new GoBoard();
		assertTrue(goBoard.isBlackTurn());		
	}
	public void testIsWhiteTurn(){
		GoBoard goBoard=new GoBoard();
		assertFalse(goBoard.isWhiteTurn());
	}
}
