package perf;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * count=10000<br/>
 * 100 field access, 14,806<br/>
 * count=10000<br/>
 * 100 method access, 20,393<br/>
 * count=10000<br/>
 * 100 map access, 66,489<br/>
 * count=10000<br/>
 * 100 reflection field access, 620,190<br/>
 * count=10000<br/>
 * 100 reflection method access, 1,832,356<br/>
 * 
 * count=100000000 <br/>
 * 10000 field access, 855,416 <br/>
 * count=100000000 <br/>
 * 10000 method access, 1,034,489<br/>
 * count=100000000 <br/>
 * 10000 map access, 2,357,562 <br/>
 * count=100000000 <br/>
 * 10000 reflection field access, 17,541,336 <br/>
 * count=100000000 <br/>
 * 10000 reflection method access, 9,833,652<br/>
 * 
 * count=10000000000 100000 field access, 2,938,362<br/>
 * count=10000000000 100000 method access, 3,039,772 <br/>
 * count=10000000000 100000 map access, 10,784,052 <br/>
 * count=10000000000 100000 reflection field access, 144,489,034 <br/>
 * count=10000000000 100000 reflection method access, 37,525,719 <br/>
 * 
 * @author Wu Jianfeng
 * 
 */
public class TestDataAccess extends TestCase {
	private static final int n = 100000;
	private static final long expectedCount = n * (long)n;
	Point[] points = new Point[n];
	Map<String, Integer>[] map = new HashMap[n];
	long start, end;
	long count;

	public void test() {
		fieldAccess();
		methodAccess();
		mapAccess();
		methodAccessThreadLocal();
		//compareThreadLocal();
		try {
			reflectionFieldAccess();
			reflectionMethodAccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reflectionFieldAccess() throws NoSuchFieldException,
			IllegalAccessException {
		Field x = Point.class.getDeclaredField("x");
		Field y = Point.class.getDeclaredField("y");
		start = System.nanoTime();
		count = 0;
		for (int i = 0; i < n; i++) {
			count += x.getInt(points[i]);
			count += y.getInt(points[i]);
		}
		end = System.nanoTime();
		System.out.println("count=" + count);
		assertEquals(expectedCount, count);
		System.out.printf(n + " reflection field access, %,d %n", end - start);
	}

	private void reflectionMethodAccess() throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Method getX = Point.class.getDeclaredMethod("getX");
		Method getY = Point.class.getDeclaredMethod("getY");

		start = System.nanoTime();
		count = 0;
		for (int i = 0; i < n; i++) {
			count += (Integer) getX.invoke(points[i]);
			count += (Integer) getY.invoke(points[i]);
		}
		end = System.nanoTime();
		System.out.println("count=" + count);
		assertEquals(expectedCount, count);
		System.out.printf(n + " reflection method access, %,d %n", end - start);
	}

	private void fieldAccess() {
		start = System.nanoTime();
		count = 0;
		for (int i = 0; i < n; i++) {
			count += points[i].x;
			count += points[i].y;
		}
		end = System.nanoTime();
		System.out.println("count=" + count);
		assertEquals(expectedCount, count);
		System.out.printf(n + " field access, %,d %n", end - start);
	}

	private void methodAccess() {
		start = System.nanoTime();
		count = 0;
		for (int i = 0; i < n; i++) {
			count += points[i].getX();
			count += points[i].getY();
		}
		end = System.nanoTime();
		System.out.println("count=" + count);
		assertEquals(expectedCount, count);
		System.out.printf(n + " method access, %,d %n", end - start);
	}

	private void methodAccessThreadLocal() {
		start = System.nanoTime();
		count = 0;

		for (int i = 0; i < n; i++) {
			count += local[i].get().getX();
			count += local[i].get().getY();
		}
		end = System.nanoTime();
		System.out.println("count=" + count);
		assertEquals(expectedCount, count);
		System.out.printf(n + " method access thread local, %,d %n", end
				- start);
	}

	private void mapAccess() {
		start = System.nanoTime();
		count = 0;
		for (int i = 0; i < n; i++) {
			count += map[i].get("x");
			count += map[i].get("y");
		}
		end = System.nanoTime();
		System.out.println("count=" + count);
		assertEquals(expectedCount, count);
		System.out.printf(n + " map access, %,d %n", end - start);
	}

	ThreadLocal<Point>[] local = new ThreadLocal[n];

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		for (int i = 0; i < n; i++) {
			points[i] = new Point(i, i + 1);
			map[i] = new HashMap<String, Integer>();
			map[i].put("x", i);
			map[i].put("y", i + 1);

			local[i] = new ThreadLocal<Point>();
			local[i].set(points[i]);
		}

	}

	void compareThreadLocal() {
		start = System.nanoTime();
		count = 0;
		for (int i = 0; i < n; i++) {
			count += points[i].getX();
			count += points[i].getY();
		}
		end = System.nanoTime();
		System.out.println("count=" + count);
		assertEquals(expectedCount, count);
		System.out.printf(n + " method access, %,d %n", end - start);

		start = System.nanoTime();
		count = 0;
		for (int i = 0; i < n; i++) {
			count += local[i].get().getX();
			count += local[i].get().getY();
		}
		end = System.nanoTime();
		System.out.println("count=" + count);
		assertEquals(expectedCount, count);
		System.out.printf(n + " method thread local access, %,d %n", end
				- start);
	}

}
