package math;

import java.math.BigInteger;

/**
 * Log2 = 1 - 1/2 + 1/3 - 1/4 + 1/5 + ...
 * 
 * @author think
 *
 */
public class Log2 {// **
	/**
	 * converge slowly
	 * 
	 * @return
	 */
	public static double getLog2() {
		double res = 0.0D;
		for (int i = 1; i <= 100000 * 100000; i++) {
			if ((i % 2) == 0) {
				res -= (1.0D / i);
			} else {
				res += (1.0D / i);
			}
		}
		return res;
	}

	public static double getNaturalBase() {
		double res = 1.0D;
		double temp;
		BigInteger one = new BigInteger(String.valueOf(1), 10);
		BigInteger val = null;
		BigInteger factorial = one;

		for (int i = 1; i <= 10; i++) {
			val = new BigInteger(String.valueOf(i), 10);
			factorial = factorial.multiply(val);
			System.out.println(factorial.doubleValue());
			temp = (1.0D/factorial.doubleValue());
			System.out.println(temp);
			res += temp;
		}
		return res;
	}

	public static void main(String[] args) {
		System.out.println(Log2.getNaturalBase());
		// System.out.println(Log2.getLog2());

		// System.out.println(Math.log(2));
		// System.out.println(4%2);
		// System.out.println(3%2);
	}
}
