package test;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * <p>TestHEX2Object.java
 * </p>
						
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class TestHEX2Object extends TestCase {
	public void testProgramM() throws IOException{
		String [] args = new String[1];
		args[0] = "test/ProgramM";
		HEX2Object.main(args);
	}
}
