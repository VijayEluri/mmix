package util;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import taocp.v3.sort.IntArrayUtil;

/**
 * int can only accomodate 12! about 0.5G
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class TestMathUtil extends TestCase {
	private static Logger log = Logger.getLogger(MathUtil.class);

	public void test() {
		log.setLevel(Level.DEBUG);
		for (int i = 1; i < 20; i++) {
			log.debug("(" + i + "!) = " + MathUtil.factorial(i));
			log.debug("(" + i + "!) = " + MathUtil.factorial((long) i));
		}
	}

	public void testPermutation() {
		int[] inputs = MathUtil.getRandomIntArray(4);
		IntArrayUtil.print(inputs, log);
		int[][] permutations = new int[MathUtil.factorial(4)][4];
		log.setLevel(Level.ERROR);
		MathUtil.init(permutations, inputs);
		log.setLevel(Level.DEBUG);
		IntArrayUtil.print(permutations, log);
	}

	public void testVH() {
		int a = 75;
		System.out.println(Arrays.toString(MathUtil.intToVector(a, 7)));
		a = 908;
		System.out.println(Arrays.toString(MathUtil.intToVector(a, 16)));
		
	}
	
}
