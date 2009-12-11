package taocp.v3.sort.insert;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import taocp.v3.TAOCPSample;
import taocp.v3.sort.IntArrayUtil;
import util.MathUtil;

public class TestInsertSort extends TestCase {
	private static Logger log = Logger.getLogger(TestInsertSort.class);
	int[] inputs = { 4, 5, 2, 6, 1, 7 };
	
	public void test() {		
		log.setLevel(Level.DEBUG);
		Logger.getLogger(InsertSort.class).setLevel(Level.DEBUG);
		IntArrayUtil.print(inputs, log);
		new InsertSort().sort(inputs);
		IntArrayUtil.print(inputs, log);
		int[] expect=new int[]{1,2,4,5,6,7};
		assertTrue(Arrays.equals(expect, inputs));
		
		inputs = TAOCPSample.getSample_ZeroIndexed();
		IntArrayUtil.print(inputs, log);
		new InsertSort().sort(inputs);
		IntArrayUtil.print(inputs, log);
	}
	
	
	
	

	
	
}
