package eddie.wu.domain;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;
import eddie.wu.domain.Point;

public class TestPoint extends TestCase {
	public static Set<Point> xing = new HashSet<Point>();// 星
	public static Set<Point> xiaomu = new HashSet<Point>();// 小目
	public static Set<Point> muwai = new HashSet<Point>();// 目外
	public static Set<Point> gaomu = new HashSet<Point>();// 高目
	public static Set<Point> sansan = new HashSet<Point>();// 三三
	static {
		xing.add(Point.getPoint(4, 4));
		xing.add(Point.getPoint(4, 16));
		xing.add(Point.getPoint(16, 4));
		xing.add(Point.getPoint(16, 16));

		xiaomu.add(Point.getPoint(3, 4));
		xiaomu.add(Point.getPoint(3, 16));
		xiaomu.add(Point.getPoint(17, 4));
		xiaomu.add(Point.getPoint(17, 16));

		xiaomu.add(Point.getPoint(4, 3));
		xiaomu.add(Point.getPoint(16, 3));
		xiaomu.add(Point.getPoint(4, 17));
		xiaomu.add(Point.getPoint(16, 17));

		muwai.add(Point.getPoint(3, 5));
		muwai.add(Point.getPoint(3, 15));
		muwai.add(Point.getPoint(17, 5));
		muwai.add(Point.getPoint(17, 15));
		muwai.add(Point.getPoint(5, 3));
		muwai.add(Point.getPoint(15, 3));
		muwai.add(Point.getPoint(5, 17));
		muwai.add(Point.getPoint(15, 17));

		gaomu.add(Point.getPoint(4, 5));
		gaomu.add(Point.getPoint(4, 15));
		gaomu.add(Point.getPoint(16, 5));
		gaomu.add(Point.getPoint(16, 15));
		gaomu.add(Point.getPoint(5, 4));
		gaomu.add(Point.getPoint(15, 4));
		gaomu.add(Point.getPoint(5, 16));
		gaomu.add(Point.getPoint(15, 16));

		sansan.add(Point.getPoint(3, 3));
		sansan.add(Point.getPoint(3, 17));
		sansan.add(Point.getPoint(17, 3));
		sansan.add(Point.getPoint(17, 17));
	}

	public void testReflection() {
		Set<Point> points;
		points = Point.xings;
		System.out.println(points);
		System.out.println(xing);
		Assert.assertTrue(points.equals(xing));
		
		points = Point.xiaomus;
		System.out.println(points);
		System.out.println(xiaomu);
		Assert.assertTrue(points.equals(xiaomu));
		
		points = Point.gaomus;
		System.out.println(points);
		System.out.println(gaomu);
		Assert.assertTrue(points.equals(gaomu));
		
		points = Point.muwais;
		System.out.println(points);
		System.out.println(muwai);
		Assert.assertTrue(points.equals(muwai));
		
		points = Point.sansans;
		System.out.println(points);
		System.out.println(sansan);
		Assert.assertTrue(points.equals(sansan));
		
	}
}
