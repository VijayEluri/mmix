package intro.algo.travel.salesman;

import java.util.Arrays;

import taocp.v4.f2.perm.Action;
import taocp.v4.f2.perm.PermutationGenerator;
import util.MathUtil;

public class TravellingSales {
	static boolean debug = false;

	double minDis = Integer.MAX_VALUE;

	double dis = 0;

	public double getShortestCircle(final int[][] points) {
		final int size = points.length;
		Action act = new Action() {
			public void actOn(int[] perm) {
				int[][] points2 = new int[size][];
				for (int i = 0; i < perm.length; i++) {
					// points2[perm[i]]=points[i];
					points2[i] = points[perm[i]];
				}

				dis = getDistance(points2);
				if (dis < minDis) {
					minDis = dis;
					if (debug)
						System.out.println(Arrays.deepToString(points2));
					if (debug)
						System.out.println("minDis=" + minDis);

				} else {
					// if(debug) System.out.println("worse(larger) distance");
				}
			}
		};

		PermutationGenerator.permPlainChange(size, 0, act);

		if (debug)
			System.out.println("minDis=" + minDis);
		return minDis;
	}

	public double getShortestCircle2(final int[][] points) {
		int n = points.length;
		int[][] res = new int[MathUtil.factorial(n - 1)][n - 1];
		int[][] points2 = new int[n][];
		int count = 0;
		PermutationGenerator.permPlainChange(n - 1, res, 1);
		if (debug)
			System.out.println("res.length=" + res.length);
		points2[0] = points[0];
		dis = 0;
		for (int i = 0; i < res.length; i++) {
			int[] perm = res[i];
			boolean special = false;
			if (Arrays.equals(perm, new int[] { 1, 2, 5, 4, 7, 6, 3 })) {
				if (debug)
					System.out.println("special" + this.getDistance(points));
				if (debug)
					System.out.println(Arrays.toString(perm));
				special = true;
			}

			for (int j = 0; j < perm.length; j++) {
				points2[j + 1] = points[perm[j]];
			}
			dis = getDistance(points2);

			if (dis < minDis || special) {
				minDis = dis;
				if (debug)
					System.out.println(Arrays.toString(perm));
				if (debug)
					System.out.println(Arrays.deepToString(points2));
				if (debug)
					System.out.println("minDis=" + minDis);

			} else {
				count++;

			}
		}
		if (debug)
			System.out.println("worse(larger) distance= " + count);
		if (debug)
			System.out.println(Arrays.deepToString(points));
		if (debug)
			System.out.println("minDis=" + minDis);
		return minDis;
	}

	/**
	 * 
	 * @param points
	 *            more than one elements
	 * @return
	 */
	static double getDistance(int[][] points) {
		int x = points[0][0];
		int y = points[0][1];
		int tx = 0;
		int ty = 0;

		double dis = 0;
		double temp = 0;
		for (int i = 1; i < points.length; i++) {
			tx = points[i][0];
			ty = points[i][1];
			if (debug)
				System.out.println("x=" + x + "; y=" + y + "; tx=" + tx
						+ "; ty=" + ty);
			temp = (Math.sqrt(Math.pow(tx - x, 2) + Math.pow(ty - y, 2)));
			dis += temp;
			if (debug)
				System.out.println("distance=" + temp);
			x = tx;
			y = ty;

		}
		temp = (Math.sqrt(Math.pow(x - points[0][0], 2)
				+ Math.pow(y - points[0][1], 2)));
		dis += temp;
		if (debug)
			System.out.println("distance=" + temp + "; total=" + dis);
		return dis;
	}
}
