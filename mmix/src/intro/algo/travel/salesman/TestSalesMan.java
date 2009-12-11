package intro.algo.travel.salesman;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * the first bug is worng {x, y} for point {2, -2}, it is inputed as {-2,-2} by
 * mistake, and make me suspect the permutation algo. it is poved to be wasting
 * effort.
 * 
 * later i spend 4 more minutes to figure out the bug in test code for distance
 * calculation.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class TestSalesMan extends TestCase {
	int[][] points = new int[][] { { 0, 0 }, { 2, 0 }, { 3, -1 }, { 0, -2 },
			{ 2, -2 }, { 4, -2 }, { -1, -3 }, { 1, -4 } };

	TravellingSales sale = new TravellingSales();

	public void testSalesMan() {
		double dis = sale.getShortestCircle(points);
		System.out.println(dis);
	}

	int[] perm = new int[] { 1, 2, 5, 4, 7, 6, 3 };

	public void testDis() {
		int[][] points2 = new int[perm.length + 1][];
		points2[0] = points[0];
		for (int j = 0; j < perm.length; j++) {
			points2[j + 1] = points[perm[j]];
		}
		System.out.println(Arrays.deepToString(points));
		System.out.println(Arrays.toString(perm));
		System.out.println(Arrays.deepToString(points2));
		System.out.println(sale.getDistance(points2));
	}

	public void testSalesMan2() {
		double dis = sale.getShortestCircle2(points);
		System.out.println(dis);
	}
}
