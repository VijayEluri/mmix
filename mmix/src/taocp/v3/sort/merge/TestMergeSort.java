package taocp.v3.sort.merge;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import taocp.v3.sort.IntArrayUtil;
import util.MathUtil;

public class TestMergeSort extends TestCase {
	private static Logger log = Logger.getLogger(MergeSort.class);
	public void test() {
		int[] inputs = { 4, 5, 2, 6, 1, 7 };
		log.setLevel(Level.DEBUG);
		IntArrayUtil.print(inputs, log);
		MergeSort sort=new MergeSort();
		sort.sort(inputs);
		IntArrayUtil.print(inputs, log);
		log.debug(sort.getMetrics());
	}
	
//	public void testSequence() {
////		for(int i = 1; i <=4; i++){
////			testSequence(i);
////		}
//		//int maxsize = 4;
//		testSequence(8);
//		log.setLevel(Level.ERROR);
//	}

	

	
}
