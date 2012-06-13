/*
 * Created on 2005-4-23
 *


 */
package eddie.wu.linkedblock;
import java.util.HashSet;
import java.util.Set;

import eddie.wu.domain.BoardPoint;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Point;

import junit.framework.TestCase;

/**
 * @author eddie
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestBoardPoint extends TestCase {
	public void testEquals() {
		
		
		Point a=Point.getPoint(19,4,3);
		
		
		Point b=Point.getPoint(19,42);
		assertEquals(a.getRow(),b.getRow());
		assertEquals(a.getColumn(),b.getColumn());
		assertEquals(a.getOneDimensionCoordinate(),b.getOneDimensionCoordinate());
	
		Point aa = Point.getPoint((byte) 5, (byte) 6);
		Point bb = Point.getPoint((byte) 5, (byte) 6);		
		BoardPoint pointa=new BoardPoint();
		BoardPoint pointb=new BoardPoint();
		pointa.setPoint(aa);
		pointb.setPoint(bb);
		assertEquals(aa,bb);
		assertEquals(pointa.getColor(),pointb.getColor());
		assertEquals(pointa,pointb);
		
		Set<BoardPoint> setOfBoardPoint=new HashSet<BoardPoint>();
		setOfBoardPoint.add(pointa);
		assertEquals(1,setOfBoardPoint.size());
		setOfBoardPoint.remove(pointb);
		assertEquals(0,setOfBoardPoint.size());
		setOfBoardPoint.add(pointa);
		setOfBoardPoint.add(pointb);
		assertEquals(1,setOfBoardPoint.size());
		
		pointb.setColor(ColorUtil.Mixture);
		pointb.setCalculatedFlag(true);
		setOfBoardPoint.remove(pointb);
		assertEquals(1,setOfBoardPoint.size());
		
	}
	
}