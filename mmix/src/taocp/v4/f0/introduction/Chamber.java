/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.introduction;

import java.math.BigInteger;

/**
 * page V: check Chamber's statement.
 * <p>
 * Chamber.java
 * </p>
 */
public class Chamber {
	public static void main(String[] args) {
		getChamber();
		BigInteger n, last, total;//
		n = BigInteger.valueOf(24);
		last = BigInteger.valueOf(1);
		total = BigInteger.valueOf(0);
		for (int i = 0; i < n.longValue(); i++) {
			last = last.multiply(n);
			total = total.add(last);
		}
		System.out.println("n=" + n + "; Sn=" + total);

	}

	private static void getChamber() {
		long n, last, total;//
		n = 5;
		last = 1;
		total = 0;
		for (int i = 0; i < n; i++) {
			last *= n;
			total += last;
		}
		System.out.println("n=" + n + "; Sn=" + total);

		n = 10;
		last = 1;
		total = 0;
		for (int i = 0; i < n; i++) {
			last *= n;
			total += last;
		}
		System.out.println("n=" + n + "; Sn=" + total);
	}
}
