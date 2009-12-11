/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.dynamic.program;

import java.util.Arrays;

/**
 * The reason we can use dynamic programming just because we have a sequence
 * choosing progress. many combination of sequence does not make sense and can
 * be filtered out locally.
 * <p>
 * AssemblyLine.java
 * </p>
 */
public class AssemblyLine {
	double entryTime;
	double exitTime;

	public static Station[] fastPath(AssemblyLine[] assemblys) {

		return null;
	}

	public static void main(String[] args) {
		int[] a1 = new int[] { 7, 9, 3, 4, 8, 4 };// assembley time
		int[] a2 = new int[] { 8, 5, 6, 4, 5, 7 };
		int[] t1 = new int[] { 2, 3, 1, 3, 4 };// tranfer time
		int[] t2 = new int[] { 2, 1, 2, 2, 1 };
		int[] e1 = new int[] { 2, 3 };// entry and exit time
		int[] e2 = new int[] { 4, 2 };

		// for this kind of problem, seem OO does not make any sense.
		//
		AssemblyLine al1 = new AssemblyLine();
		AssemblyLine al2 = new AssemblyLine();

		int n = a1.length;// 6
		int[] f1 = new int[n];
		int[] f2 = new int[n];
		f1[0] = e1[0] + a1[0];
		f2[0] = e2[0] + a2[0];
		int[] path = new int[n - 1];
		for (int i = 1; i < f1.length; i++) {
			// int ta = f1[i - 1] + a1[i];
			// int tb = f2[i - 1] + t2[i - 1] + a1[i];
			// if (ta<tb){
			//				
			// }
			// need consider that ta=tb.
			f1[i] = Math.min(f1[i - 1] + a1[i], f2[i - 1] + t2[i - 1] + a1[i]);
			f2[i] = Math.min(f2[i - 1] + a2[i], f1[i - 1] + t1[i - 1] + a2[i]);
		}
		int fa = Math.min(f1[n - 1] + e1[1], f2[n - 1] + e2[1]);
		System.out.println(Arrays.toString(f1));
		System.out.println(Arrays.toString(f2));
		System.out.println(fa);
	}
}

class Station {
	int lineId;
	int id;
	double assemblyTime;
	double transferTime;// to the next Line

	public String toString() {
		return "Station " + id + " in Line" + lineId;
	}
}
