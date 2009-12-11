/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package problem.birthday;

import util.MathUtil;

/**
 * <p>
 * Birthday.java
 * </p>
 */
public class Birthday {
	static int N = 6;

	public static void main(String[] args) {
		long enumerator;
		long denominator;
		System.out
				.println("The possibility of at least two people having same birthday!");
		for (int i = 1; i < N; i++) {
			enumerator = 1;
			denominator = 1;
			for (int j = 0; j < i; j++) {
				enumerator = enumerator * ((365 - j));
				denominator *= 365;
				
			}
			enumerator = denominator - enumerator;
			System.out.println("i=" + i + "; percent="
					+ ((double) enumerator / denominator) + "(" + enumerator
					+ "/" + denominator + ");");
		}

		System.out
				.println("The possibility of exact two people having same birthday!");
		for (int i = 1; i < N; i++) {
			enumerator = 1;
			denominator = 1;
			for (int j = 0; j < i; j++) {
				enumerator = enumerator * ((365 - j));
				denominator *= 365;
			}
			enumerator = enumerator * (i * (i - 1) / 2) / (365 - i + 1);
			System.out.println("i=" + i + "; percent="
					+ ((double) enumerator / denominator) + "(" + enumerator
					+ "/" + denominator + ";");
		}

		System.out.println("another way to calculate --- double check!");

		for (int i = 2; i < N; i++) {
			long count = 0;
			// consdier the possibility of 2, 3 n people have same birthday and
			// sum them.
			denominator=1;
			for (int j = 0; j < i; j++) {				
				denominator *= 365;				
			}
			for (int k = 2; k <= i; k++) {
				int combination = MathUtil.factorial(i)
						/ MathUtil.factorial(i - k) / MathUtil.factorial(k);

				enumerator = 1;				
				for (int j = 0; j < i - (k - 1); j++) {
					enumerator = enumerator * (365 - j);					
				}
				enumerator = enumerator * (combination);
				count += enumerator;
				System.out.println("k="+k+"; enumerator="+enumerator+"; count="+count);
			}
			System.out.println("i=" + i + "; percent="
					+ ((double) count / denominator) + "(" + count
					+ "/" + denominator + ";");
		}

	}

	public static void floatM() {
		double percent;
		System.out
				.println("The possibility of at least two people having same birthday!");
		for (int i = 1; i < N; i++) {
			percent = 1.0f;
			for (int j = 0; j < i; j++) {
				percent = percent * ((365 - j) / (float) 365);
			}
			percent = 1.0f - percent;
			System.out.println("i=" + i + "; percent=" + percent + ";");
		}

		System.out
				.println("The possibility of exact two people having same birthday!");
		for (int i = 1; i < N; i++) {
			percent = 1.0f;
			for (int j = 0; j < i; j++) {
				percent = percent * ((365 - j) / (float) 365);
			}
			percent = percent * (i * (i - 1) / 2) / (365 - i + 1);
			System.out.println("i=" + i + "; percent=" + percent + ";");
		}

		System.out.println("another way to calculate --- double check!");

		for (int i = 2; i < N; i++) {
			float count = 0;
			// consdier the possibility of 2, 3 n people have same birthday and
			// sum them.
			for (int k = 2; k <= i; k++) {
				int combination = MathUtil.factorial(i)
						/ MathUtil.factorial(i - k) / MathUtil.factorial(k);

				percent = 1.0f;
				for (int j = 0; j < i - (k - 1); j++) {
					percent = percent * (365 - j);
				}
				percent = percent * (combination) / Math.pow(365, i);
				count += percent;
			}
			// count = 1 - count;
			System.out.println("i=" + i + "; percent=" + count + ";");
		}

	}

}
/**
 * i=23; percent=0.5072974; i=30; percent=0.7063163; i=47; percent=0.95477444;
 * i=57; percent=0.99012244; it means if there are 23 or more people, the
 * oppotunity of two people having same birthday is more than 50%. a good bet.
 * 47 and more people, 95% sure. 57 and more people, 99% sure.
 * 
 * i=2; percent=0.0027397275; i=3; percent=0.008204162;
 */
