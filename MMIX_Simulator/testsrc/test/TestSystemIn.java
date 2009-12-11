package test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

/**
 * <p>
 * TestSystemIn.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestSystemIn extends TestCase {
	Logger log = Logger.getLogger(this.getClass());
	public void test() {
		byte[] tt = new byte[4];
		StringBuffer buffer = new StringBuffer();
		try {
			int temp = 0;
			while ((temp = System.in.read(tt)) == 4) {
				buffer.append(new String(tt));
				log.debug(new String(tt) + " -- temp= " + temp
						+ "tt[3]=" + tt[3]);
			}
			buffer.append(new String(tt));
			log.debug(new String(tt) + " -- temp= " + temp
					+ "tt[3]=" + tt[3]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug(buffer);
	}
	
	public void test2(){
		
		byte[] tt = new byte[40];
		StringBuffer buffer = new StringBuffer();
		InputStream inf = (InputStream )System.in;
		try {
			int temp = 0;
			while ((temp = inf.read(tt)) == 40) {
				buffer.append(new String(tt));
				log.debug(new String(tt) + " -- temp= " + temp
						+ "tt[3]=" + tt[39]);
			}
			buffer.append(new String(tt));
			log.debug(new String(tt) + " -- temp= " + temp
					+ "tt[3]=" + tt[39]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug(buffer);
		
	}
}
