package test;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

/**
 * <p>TestShift.java
 * </p>
 
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class TestShift extends TestCase {
	Logger log = Logger.getLogger(this.getClass());
	public void testShift(){
		long value = 4611686018427387944L;
		log.debug("value >> 24 & 0x000000ff: "+(0x00000000000000ffL));
		log.debug("value >> 24 & 0x000000ff: "+(value & 0x00000000000000ffL));
		log.debug("value >> 24 & 0x000000ff:\n"
				+ (Long.toHexString(value) + ":\n" + Long.toBinaryString(value)));
		log.debug("value >> 24 & 0x000000ff: "+(value >> 24 & 0x000000ff));
		log.debug("value >> 24 & 0x000000ff: "+(value  & 0x000000ff));
		log.debug("value >> 24 & 0x000000ff: "+(value  & 0x00000000000000ffL));
	}
}
