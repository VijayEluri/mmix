package test;

import java.math.BigInteger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

/**
 * <p>
 * TestFloat.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestFloat extends TestCase {
	Logger log = Logger.getLogger(this.getClass());

	public void test() {
		log.setLevel(Level.DEBUG);
		double a = 33.1415;
		double b = 33.7;
		double c = 31.1;
		log.debug("a = " + a + "; fix integer a = "
				+ Double.valueOf(a).intValue());
		log.debug("a = " + a + "; fix integer a = "
				+ Double.valueOf(a).longValue());
		log.debug("(long)a = " + (long) a);
		log.debug("(long)a = " + (long) Double.NaN);
		log.debug("(long)a = " + (long) Double.NEGATIVE_INFINITY);
		log.debug("(long)a = " + (long) Double.MAX_VALUE);
		log.debug("(long)a = " + (long) Double.MIN_VALUE);
		//log.debug("(long)a = " + (long) Double.MIN_NORMAL);
		log.debug("(long)a = " + (long) Long.MAX_VALUE);
		log.debug("(long)a = " + (long) Long.MIN_VALUE);
		log.debug("(long)a = " + (long) Double.POSITIVE_INFINITY);
		log.debug("(long)a = " + (long) Double.NEGATIVE_INFINITY);
		log.debug("(long)a = "
				+ Double.valueOf("+0.0").compareTo(Double.valueOf("-0.0")));
		log.debug("(long)a = "
				+ Double.valueOf("-0.0").compareTo(Double.valueOf("+0.0")));
		log.debug("(+0.0 > -0.0)? " + (+0.0 > -0.0));
		log.debug("(+0.0 == -0.0)? " + (+0.0 == -0.0));
		log.debug("(3.7 )? " + (b) + ":" + Double.toHexString(b) + ":"
				+ Double.toHexString(c));
		log.debug("(3.7 )? " + (b) + ":" + Double.toString(b) + ":"
				+ Double.toString(c));
		log.debug("(1.1)? " + (c) + ":"
				+ Long.toHexString((Double.doubleToRawLongBits(c))) + ":"
				+ Long.toHexString((Double.doubleToLongBits(c))));
		log.debug("(1.1)? " + (c) + ":"
				+ Long.toBinaryString((Double.doubleToRawLongBits(c))) + ":"
				+ Long.toBinaryString((Double.doubleToLongBits(c))));
		log.debug("(33.7 - 31.1)? " + (33.7 - 31.1));
		log.debug("(33.7 - 21.1)? " + (33.7 - 21.1));
		log.debug("(3.7 - 1.1)? " + (3.7 - 1.1));
		log.debug("(3.7 - 1.1)? " + (0.0));
		log.debug("(3.7 - 1.1)? " + (-0.0));
		log.debug("(3.7 - 1.1)? " + (0.0 + 0.0));
		log.debug("(3.7 - 1.1)? " + (-0.0 + -0.0));
		log.debug("(3.7 - 1.1)? " + (0.0 + -0.0));
		// log.debug("(3.7 - 1.1)? " + (-0.0));
		log.debug("Double.valueOf(Long.MAX_VALUE)? " + ((Long.MAX_VALUE)));
		log.debug("(Long.MAX_VALUE)? " + ((Long.MAX_VALUE - 1)));
		log.debug("(Long.MAX_VALUE)? " + ((Long.MAX_VALUE) + 1)
				* ((Long.MAX_VALUE) + 1));
		log.debug("(Long.MAX_VALUE)? "
				+ (1 + ((Long.MAX_VALUE) + 1) * ((Long.MAX_VALUE) + 1)));
	}

	public void testBigInteger() {
		log.setLevel(Level.DEBUG);
		byte[] a = BigInteger.valueOf(0xffff0000).toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);
		}
		a = BigInteger.valueOf(0xffff0000L).toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);
		}
		a = BigInteger.valueOf(0xff).toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);
		}
		a = BigInteger.valueOf(0x7f).toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);

		}
		a = BigInteger.valueOf(0x80).toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);
		}
		a = BigInteger.valueOf(0x81).toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);
		}
	}

	public void testFF() {
		float a;
		a = 3.2f;
		System.out.print((int) a);
		a = -3.2f;
		System.out.print((int) a);
	}
}
