/*
 * Created on 2005-4-22
 *


 */
package eddie.wu.linkedblock;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.domain.Point;
import eddie.wu.util.GBKToUTF8;

/**
 * @author eddie
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TestPoint extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	
	
	public final static void main(String[] args) {
		if(log.isDebugEnabled()) log.debug("test" + args[0]);
	}

	public void testPurePoint() {
		Point a = Point.getPoint(19,4, 3);
	

		Point b = Point.getPoint(19,(short) 42);
		assertEquals(a.getRow(), b.getRow());
		assertEquals(a.getColumn(), b.getColumn());
		assertEquals(a.getOneDimensionCoordinate(),
				b.getOneDimensionCoordinate());

		Point aa = Point.getPoint((byte) 5, (byte) 6);
		Point bb = Point.getPoint((byte) 5, (byte) 6);
		assertEquals(aa, bb);
	}

	public void testIsLeftTop() {
		Point point = Point.getPoint(3, 3);
		assertFalse(!point.isLeftTop());
		assertFalse(point.isLeftBottom());
		assertFalse(point.isRightBottom());
		assertFalse(point.isRightTop());

		point = Point.getPoint(3, 17);
		assertFalse(point.isLeftTop());
		assertFalse(point.isLeftBottom());
		assertFalse(point.isRightBottom());
		assertFalse(!point.isRightTop());

		point = Point.getPoint(17, 3);
		assertFalse(point.isLeftTop());
		assertFalse(!point.isLeftBottom());
		assertFalse(point.isRightBottom());
		assertFalse(point.isRightTop());

		point = Point.getPoint(17, 17);
		assertFalse(point.isLeftTop());
		assertFalse(point.isLeftBottom());
		assertFalse(!point.isRightBottom());
		assertFalse(point.isRightTop());

		point = Point.getPoint(10, 10);
		assertFalse(point.isLeftTop());
		assertFalse(point.isLeftBottom());
		assertFalse(point.isRightBottom());
		assertFalse(point.isRightTop());

		point = Point.getPoint(10, 18);
		assertFalse(point.isLeftTop());
		assertFalse(point.isLeftBottom());
		assertFalse(!point.isRightBottom());
		assertFalse(point.isRightTop());

		point = Point.getPoint(19, 10);
		assertFalse(point.isLeftTop());
		assertFalse(!point.isLeftBottom());
		assertFalse(point.isRightBottom());
		assertFalse(point.isRightTop());

		int a = 0, b = 0, c = 0, d = 0, total = 0;
		for (int i = 1; i <= 19; i++) {
			for (int j = 1; j <= 19; j++) {
				point = Point.getPoint(i, j);
				a += point.isLeftTop() ? 1 : 0;
				b += point.isLeftBottom() ? 1 : 0;
				c += point.isRightTop() ? 1 : 0;
				d += point.isRightBottom() ? 1 : 0;

			}

		}
		total = a + b + c + d;
		assertEquals(360, total);
		assertEquals(90, a);
		assertEquals(90, b);
		assertEquals(90, c);
		assertEquals(90, d);
	}

	public void testArrayOfPoint() {
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				assertEquals(Point.getPoint(i, j), Point.getPoint(i, j));
				if(log.isDebugEnabled()) log.debug(Point.getPoint(i, j).toString()
						+ Point.getPoint(i, j).hashCode());
			}
		}
	}
}
