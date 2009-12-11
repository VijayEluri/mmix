package mmix_port;

import java.math.BigInteger;

import mmix.NumberUtil;

import junit.framework.TestCase;

/**
 * <p>
 * TestFloat.java find two minor bug. int is not converted to long. cause shift
 * error. should shift <<52 instead of 20. after correction, the right answer is
 * o += ((long)e << 52);
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestArithUtil extends TestCase {
	public void testFpack() {
		long x = 0;
		x = new ArithUtil().fpack(1L << 54, 0x3fe, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		System.out.println(Double.longBitsToDouble(x));

		x = new ArithUtil().fpack(1L << 55, 0x7fe, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		System.out.println(Double.longBitsToDouble(x));

		x = new ArithUtil().fpack(1L << 55, 0x7fd, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		System.out.println(Double.longBitsToDouble(x));

		x = new ArithUtil().fpack(1L << 54, 0x1, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		Double d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, 0x1, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 54, -0x1, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, -0x1, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 54, -0x54, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, -0x54, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, -0x55, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 54, -0x54, false,
				mmix.cons.FloatPointConstant.ROUND_UP);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, -0x54, false,
				mmix.cons.FloatPointConstant.ROUND_UP);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, -0x55, false,
				mmix.cons.FloatPointConstant.ROUND_UP);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 54, -0x54, false,
				mmix.cons.FloatPointConstant.ROUND_DOWN);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, -0x54, false,
				mmix.cons.FloatPointConstant.ROUND_DOWN);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, -0x55, false,
				mmix.cons.FloatPointConstant.ROUND_DOWN);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 54, -0x54, false,
				mmix.cons.FloatPointConstant.ROUND_NEAR);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, -0x54, false,
				mmix.cons.FloatPointConstant.ROUND_NEAR);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().fpack(1L << 55, -0x55, false,
				mmix.cons.FloatPointConstant.ROUND_NEAR);
		System.out.println(Long.toHexString(x));
		d = Double.longBitsToDouble(x);
		System.out.println(Double.toHexString(d));
	}

	public void testSfpack() {
		int x = 0;
		java.lang.Float d;
		/*
		 * denomalized value
		 */
		x = new ArithUtil().sfpack(1L << 54, 0x380, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(Double.toHexString(d));

		x = new ArithUtil().sfpack(1L << 55, 0x380, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 54, 0x380 + 253, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 55, 0x380 + 253, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		/*
		 * 
		 */
		x = new ArithUtil().sfpack(1L << 54, 0x380 + 254, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 55, 0x380 + 254, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 54 + 1, 0x380 + 254, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 55 + 1, 0x380 + 254, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		/*
		 * close to 0
		 */
		x = new ArithUtil().sfpack(1L << 54, 0x380 - 1, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 55, 0x380 - 1, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 54, 0x380 - 23, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 55, 0x380 - 23, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 54, 0x380 - 24, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 55, 0x380 - 24, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 54, 0x380 - 25, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 55, 0x380 - 25, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 54, 0x380 - 26, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

		x = new ArithUtil().sfpack(1L << 55, 0x380 - 26, false,
				mmix.cons.FloatPointConstant.ROUND_OFF);
		System.out.println(Integer.toHexString(x));
		d = java.lang.Float.intBitsToFloat(x);
		System.out.println(java.lang.Float.toHexString(d));

	}

	/**
	 *
	 */
	public void testDiv() {
		ArithUtil x = new ArithUtil();
		long t = x.odiv(Long.MAX_VALUE - 1, Long.MAX_VALUE, Long.MAX_VALUE);
		System.out.println(NumberUtil.longToHex(t));
		System.out.println(NumberUtil.longToHex(x.aux));

		BigInteger bigx = BigInteger.valueOf(Long.MAX_VALUE - 1);
		BigInteger bigy = BigInteger.valueOf(Long.MAX_VALUE);
		BigInteger bigz = BigInteger.valueOf(Long.MAX_VALUE);
		BigInteger bigxx = bigx.multiply(NumberUtil.get2Power64()).add(bigy)
				.divide(bigz);
		BigInteger bigxxr = bigx.multiply(NumberUtil.get2Power64()).add(bigy)
				.remainder(bigz);
		System.out.println(Long.toHexString(bigxx.longValue()));
		System.out.println(Long.toHexString(bigxxr.longValue()));
		
		assertEquals(bigxx.longValue(), t);
		assertEquals(bigxxr.longValue(), x.aux);
	}
	
	public void testSignedDiv() {
		ArithUtil x = new ArithUtil();
		long t = x.signed_odiv(9,-4);
		System.out.println(NumberUtil.longToHex(t));
		System.out.println(NumberUtil.longToHex(x.aux));

		
		assertEquals(-3, t);
		assertEquals(-3, x.aux);
	}
	
	public void testDiv2() {
		ArithUtil x = new ArithUtil();
		long t = x.odiv(Integer.MAX_VALUE - 1, Integer.MAX_VALUE, Integer.MAX_VALUE);
		System.out.println(Long.toHexString(t));
		System.out.println(Long.toHexString(x.aux));

		BigInteger bigx = BigInteger.valueOf(Integer.MAX_VALUE - 1);
		BigInteger bigy = BigInteger.valueOf(Integer.MAX_VALUE);
		BigInteger bigz = BigInteger.valueOf(Integer.MAX_VALUE);
		BigInteger bigxx = bigx.multiply(NumberUtil.get2Power64()).add(bigy)
				.divide(bigz);
		BigInteger bigxxr = bigx.multiply(NumberUtil.get2Power64()).add(bigy)
				.remainder(bigz);
		
		System.out.println(Long.toHexString(bigxx.longValue()));
		System.out.println(Long.toHexString(bigxxr.longValue()));
		
		assertEquals(bigxx.longValue(), t);
		assertEquals(bigxxr.longValue(), x.aux);
		// long d =
	}

	public void testDiv1() {
		ArithUtil x = new ArithUtil();
		long t = x.odiv(1 - 1, Long.MAX_VALUE, Long.MAX_VALUE);
		System.out.println(Long.toHexString(t));
		System.out.println(Long.toHexString(x.aux));

		assertEquals(1, t);
		assertEquals(0, x.aux);
	}

	public void testMulti() {
		ArithUtil x = new ArithUtil();
		long t = x.omult(Long.MAX_VALUE, Long.MAX_VALUE);
		System.out.println(Long.toHexString(t));
		System.out.println(NumberUtil.longToHex(x.aux));
		System.out.println();

		BigInteger bigy = BigInteger.valueOf(Long.MAX_VALUE);
		BigInteger bigz = BigInteger.valueOf(Long.MAX_VALUE);
		BigInteger bigx = bigy.multiply(bigz).remainder(
				NumberUtil.get2Power64());
		BigInteger bigx2 = bigy.multiply(bigz).divide(NumberUtil.get2Power64());
		System.out.println(Long.toHexString(bigx.longValue()));
		System.out.println(Long.toHexString(bigx2.longValue()));

		assertEquals(bigx.longValue(), t);
		assertEquals(bigx2.longValue(), x.aux);

	}

	public void testMulti1() {
		ArithUtil x = new ArithUtil();
		long t = x.omult(1, 1);
		System.out.println(Long.toHexString(t));
		System.out.println(Long.toHexString(x.aux));
		System.out.println();

		System.out.println(Long.toHexString(1L));
		System.out.println(Long.toHexString(0L));

		assertEquals(1, t);
		assertEquals(0, x.aux);

		System.out.println(Long.toHexString(16383));
		System.out.println(Long.toHexString(65535));

	}

	public void testSignedMulti1() {
		ArithUtil x = new ArithUtil();
		long t = x.omult(-1, 1);
		System.out.println(Long.toHexString(t));
		System.out.println(Long.toHexString(x.aux));
		System.out.println();

		assertEquals(-1, t);
		assertEquals(0, x.aux);
	}
}
