package test;

import static mmix.cons.SpecialRegisterConstant.A;

import java.math.BigInteger;

import mmix.NumberUtil;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

/**
 * <p>
 * TestBigInteger.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestBigInteger extends TestCase {
	Logger log = Logger.getLogger(this.getClass());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void testShift() {
		// long specialRegister &= (0x1 << 6);
		int specialRegister = 0;
		specialRegister &= (0x1 << 6);
		log.debug("valueX = " + specialRegister);
		specialRegister = specialRegister & (0x1 << 6);
		log.debug("valueX = " + specialRegister);
	}

	public void test() {
		log.debug("test");
		byte[] a = new byte[8];
		for (int i = 0; i < 8; i++) {
			a[i] = -1;
			log.debug(a[i]);
		}
		BigInteger biga = new BigInteger(a);
		log.debug("test");
		log.debug(biga);
		log.debug(biga.longValue());

	}

	public void test2() {
		log.debug("test2");
		byte[] a = new byte[8];
		for (int i = 0; i < 8; i++) {
			a[i] = -1;
			log.debug("a[" + i + "]=" + a[i]);
		}
		a[0] = (byte) 127;
		log.debug("a[" + 0 + "]=" + a[0]);
		BigInteger biga = new BigInteger(a);
		log.debug("test2: the biggest signed long");
		log.debug(Long.MAX_VALUE);
		log.debug(biga);
		log.debug(biga.longValue());

	}

	public void testc() {
		log.debug("tesct");
		byte[] a = new byte[8];
		for (int i = 0; i < 8; i++) {
			a[i] = 127;
		}
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			b[i] = 127;
		}
		BigInteger biga = new BigInteger(a);
		log.debug(biga);
		BigInteger bigb = new BigInteger(b);
		log.debug(bigb);
		BigInteger bigc = biga.multiply(bigb);
		log.debug(bigc);
		log.debug(bigc.longValue());

	}

	public void testUnsignedLong() {

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
	public void testB(){
		log.setLevel(Level.DEBUG);
		byte[] a;
		a = BigInteger.valueOf(Long.MIN_VALUE).toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);
		}
		a = NumberUtil.getUnsignedLong(Long.MIN_VALUE).toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);
		}
	
		a = NumberUtil.getUnsignedLong(Long.MIN_VALUE+1).toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);
		}
		a = NumberUtil.get2Power64().toByteArray();
		log.debug(a.length);
		for (byte b : a) {
			log.debug(b);
		}
	}
	
}
