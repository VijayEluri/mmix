package test;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import mmix.NumberUtil;
import junit.framework.TestCase;

/**
 * <p>
 * TestNumberUtil.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestNumberUtil extends TestCase {
	Logger log = Logger.getLogger(getClass());

	public void testGetSignedWyde() {
		byte high;
		byte low;
		high = 127;
		low = 127;
		log.debug("");
		log.debug(NumberUtil.getSignedWyde(high, low));

		high = -128;
		low = 127;
		log.debug(NumberUtil.getSignedWyde(high, low));
		high = 127;
		low = -128;
		log.debug(NumberUtil.getSignedWyde(high, low));
		high = -128;
		low = -128;
		log.debug(NumberUtil.getSignedWyde(high, low));
	}

	public void testGetUnsignedTetra() {
		byte high;
		byte low;

		byte lhigh;
		byte llow;
		high = 127;
		low = 127;
		lhigh = 127;
		llow = 127;
		log.debug(NumberUtil.getSignedTetra(high, low, lhigh, llow));
		log.debug(NumberUtil.getUnsignedTetra(high, low, lhigh, llow));
		log.debug(NumberUtil.getUnsignedTetra2(high, low, lhigh, llow));
		high = -1;
		low = -1;
		lhigh = -1;
		llow = -1;
		log.debug(NumberUtil.getSignedTetra(high, low, lhigh, llow));
		log.debug(NumberUtil.getUnsignedTetra(high, low, lhigh, llow));
		log.debug(NumberUtil.getUnsignedTetra2(high, low, lhigh, llow));
	}

	public void testBitwise() {
		byte a = 9;
		byte[] result = NumberUtil.bitwiseByte(a);
		for (int i = 0; i < 8; i++) {
			log.debug(result[i]);// result[i]= (byte)(tmp & 1<<( 7-i));
		}
		log.debug(NumberUtil.reverseBitwiseByte(result));// result[i]=
		// (byte)(tmp & 1<<(
		// 7-i));

	}

	public void testIntToHex() {
		int a = -1;
		int b = Integer.MAX_VALUE;
		int c = Integer.MIN_VALUE;
		int d = 132;
		log.debug(NumberUtil.intToHex(a));
		log.debug(NumberUtil.intToHex(b));
		log.debug(NumberUtil.intToHex(c));
		log.debug(NumberUtil.intToHex(d));
		log.debug(Long.toHexString(-1));
		log.debug(Long.toHexString(1));
		assertEquals(8, NumberUtil.intToHex(a).length());
		assertEquals(8, NumberUtil.intToHex(b).length());
		assertEquals(8, NumberUtil.intToHex(c).length());
		assertEquals(8, NumberUtil.intToHex(d).length());
		assertEquals(8, NumberUtil.intToHex(-1).length());
		assertEquals(8, NumberUtil.intToHex(1).length());
		assertEquals(8, NumberUtil.intToHex(0).length());
	}

	public void testLongToHex() {
		log.setLevel(Level.DEBUG);
		int a = -1;
		long b = Long.MAX_VALUE;
		long c = Long.MIN_VALUE;
		int d = 132;
		log.debug(NumberUtil.longToHex(a));
		log.debug(NumberUtil.longToHex(b));
		log.debug(NumberUtil.longToHex(c));
		log.debug(NumberUtil.longToHex(d));
		log.debug(Long.toHexString(-1));
		log.debug(Integer.toHexString(-1));
		log.debug(Long.toHexString(1));
		log.debug(Integer.toHexString(1));
		assertEquals(16, NumberUtil.longToHex(a).length());
		assertEquals(16, NumberUtil.longToHex(b).length());
		assertEquals(16, NumberUtil.longToHex(c).length());
		assertEquals(16, NumberUtil.longToHex(d).length());
		assertEquals(16, NumberUtil.longToHex(-1).length());
		assertEquals(16, NumberUtil.longToHex(1).length());
		assertEquals(16, NumberUtil.longToHex(0).length());
	}
	
	
}
