package eddie.wu.domain;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.util.GBKToUTF8;

public class TestPoint extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	public static Set<Point> xing = new HashSet<Point>();// 星
	public static Set<Point> xiaomu = new HashSet<Point>();// 小目
	public static Set<Point> muwai = new HashSet<Point>();// 目外
	public static Set<Point> gaomu = new HashSet<Point>();// 高目
	public static Set<Point> sansan = new HashSet<Point>();// 三三
	static {
		xing.add(Point.getPoint(Constant.BOARD_SIZE, 4, 4));
		xing.add(Point.getPoint(Constant.BOARD_SIZE, 4, 16));
		xing.add(Point.getPoint(Constant.BOARD_SIZE, 16, 4));
		xing.add(Point.getPoint(Constant.BOARD_SIZE, 16, 16));

		xiaomu.add(Point.getPoint(Constant.BOARD_SIZE, 3, 4));
		xiaomu.add(Point.getPoint(Constant.BOARD_SIZE, 3, 16));
		xiaomu.add(Point.getPoint(Constant.BOARD_SIZE, 17, 4));
		xiaomu.add(Point.getPoint(Constant.BOARD_SIZE, 17, 16));

		xiaomu.add(Point.getPoint(Constant.BOARD_SIZE, 4, 3));
		xiaomu.add(Point.getPoint(Constant.BOARD_SIZE, 16, 3));
		xiaomu.add(Point.getPoint(Constant.BOARD_SIZE, 4, 17));
		xiaomu.add(Point.getPoint(Constant.BOARD_SIZE, 16, 17));

		muwai.add(Point.getPoint(Constant.BOARD_SIZE, 3, 5));
		muwai.add(Point.getPoint(Constant.BOARD_SIZE, 3, 15));
		muwai.add(Point.getPoint(Constant.BOARD_SIZE, 17, 5));
		muwai.add(Point.getPoint(Constant.BOARD_SIZE, 17, 15));
		muwai.add(Point.getPoint(Constant.BOARD_SIZE, 5, 3));
		muwai.add(Point.getPoint(Constant.BOARD_SIZE, 15, 3));
		muwai.add(Point.getPoint(Constant.BOARD_SIZE, 5, 17));
		muwai.add(Point.getPoint(Constant.BOARD_SIZE, 15, 17));

		gaomu.add(Point.getPoint(Constant.BOARD_SIZE, 4, 5));
		gaomu.add(Point.getPoint(Constant.BOARD_SIZE, 4, 15));
		gaomu.add(Point.getPoint(Constant.BOARD_SIZE, 16, 5));
		gaomu.add(Point.getPoint(Constant.BOARD_SIZE, 16, 15));
		gaomu.add(Point.getPoint(Constant.BOARD_SIZE, 5, 4));
		gaomu.add(Point.getPoint(Constant.BOARD_SIZE, 15, 4));
		gaomu.add(Point.getPoint(Constant.BOARD_SIZE, 5, 16));
		gaomu.add(Point.getPoint(Constant.BOARD_SIZE, 15, 16));

		sansan.add(Point.getPoint(Constant.BOARD_SIZE, 3, 3));
		sansan.add(Point.getPoint(Constant.BOARD_SIZE, 3, 17));
		sansan.add(Point.getPoint(Constant.BOARD_SIZE, 17, 3));
		sansan.add(Point.getPoint(Constant.BOARD_SIZE, 17, 17));
	}

	public void testReflection() {
		Set<Point> points;
		points = Point.xings;
		if(log.isDebugEnabled()) log.debug(points);
		if(log.isDebugEnabled()) log.debug(xing);
		assertTrue(points.equals(xing));

		points = Point.xiaomus;
		if(log.isDebugEnabled()) log.debug(points);
		if(log.isDebugEnabled()) log.debug(xiaomu);
		assertTrue(points.equals(xiaomu));

		points = Point.gaomus;
		if(log.isDebugEnabled()) log.debug(points);
		if(log.isDebugEnabled()) log.debug(gaomu);
		assertTrue(points.equals(gaomu));

		points = Point.muwais;
		if(log.isDebugEnabled()) log.debug(points);
		if(log.isDebugEnabled()) log.debug(muwai);
		assertTrue(points.equals(muwai));

		points = Point.sansans;
		if(log.isDebugEnabled()) log.debug(points);
		if(log.isDebugEnabled()) log.debug(sansan);
		assertTrue(points.equals(sansan));

	}
}
