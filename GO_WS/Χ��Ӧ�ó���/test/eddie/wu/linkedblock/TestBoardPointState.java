/*
 * Created on 2005-4-23
 *


 */
package eddie.wu.linkedblock;
import java.util.HashSet;
import java.util.Set;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Point;

import junit.framework.TestCase;
/**
 * @author eddie
 *
 * TODO To change the template for this generated type comment go to

 */
public class TestBoardPointState extends TestCase {
	public void testBoardState(){
		BoardColorState state=new BoardColorState();
		state.add(new BoardPoint(new Point((byte)1,(byte)1),ColorUtil.BLACK));
		state.add(new BoardPoint(new Point((byte)4,(byte)4),ColorUtil.WHITE));
		
		Set blackPoints=new HashSet();
		blackPoints.add(new Point((byte)1,(byte)1));
		Set whitePoints=new HashSet();
		whitePoints.add(new Point((byte)4,(byte)4));
		assertTrue(blackPoints.equals(state.getBlackPoints()));
		assertTrue(whitePoints.equals(state.getWhitePoints()));
		//assert in another way.--equivalent
		assertEquals(state.getBlackPoints(),blackPoints);
		assertEquals(state.getWhitePoints(),whitePoints);
		
		state.add(new BoardPoint(new Point((byte)1,(byte)19),ColorUtil.BLACK));
		state.add(new BoardPoint(new Point((byte)2,(byte)19),ColorUtil.WHITE));
		blackPoints.add(new Point((byte)1,(byte)19));
		whitePoints.add(new Point((byte)2,(byte)19));
		assertTrue(blackPoints.equals(state.getBlackPoints()));
		assertTrue(whitePoints.equals(state.getWhitePoints()));
	}
}
