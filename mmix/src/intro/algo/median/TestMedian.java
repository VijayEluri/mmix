/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.median;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import taocp.v3.sort.IntArrayUtil;
import taocp.v3.sort.insert.InsertSort;
import taocp.v3.sort.merge.MergeSort;
import util.MathUtil;

/**
 * <p>
 * TestMedian.java
 * </p>
 */
public class TestMedian extends TestCase {
	private static Logger log = Logger.getLogger(TestMedian.class);
	int[] inputs;

	public void testSortElements() {
		log.setLevel(Level.DEBUG);
		Logger.getLogger(InsertSort.class).setLevel(Level.DEBUG);
		inputs = MathUtil.getRandomPositiveIntArray(27, 1);
		IntArrayUtil.print(inputs, log);
		new Median().sortElements(inputs, 5, 1, 7);
		IntArrayUtil.print(inputs, log);
		log.debug("");

		inputs = MathUtil.getRandomPositiveIntArray(27, 1);
		IntArrayUtil.print(inputs, log);
		new Median().sortElements(inputs, 2, 5, 5);
		IntArrayUtil.print(inputs, log);
		log.debug("");

		inputs = MathUtil.getRandomPositiveIntArray(27, 1);
		IntArrayUtil.print(inputs, log);
		new Median().sortElements(inputs, 15, 5, 5);
		IntArrayUtil.print(inputs, log);
		log.debug("");
	}

	public void testSortElements2() {
		log.setLevel(Level.DEBUG);
		Logger.getLogger(InsertSort.class).setLevel(Level.DEBUG);
		inputs = MathUtil.getRandomPositiveIntArray(32, 1);
		IntArrayUtil.print(inputs, log);
		new Median().sortAllElements(inputs, 0, 1, 5);
		IntArrayUtil.print(inputs, log);
		log.debug("");

		new Median().sortAllElements(inputs, 2, 5, 5);
		IntArrayUtil.print(inputs, log);
		log.debug("");

	}

	public void testGetPivot() {
		log.setLevel(Level.DEBUG);
		Logger.getLogger(InsertSort.class).setLevel(Level.DEBUG);
		inputs = MathUtil.getRandomPositiveIntArray(32, 1);
		IntArrayUtil.print(inputs, log);

		int value = new Median().getPivot(inputs, 0, inputs.length - 1, 5);
		IntArrayUtil.print(inputs, log);
		log.debug("value=" + value);
	}

	public void testGetMedian() {
		log.setLevel(Level.DEBUG);
		Logger.getLogger(InsertSort.class).setLevel(Level.DEBUG);
		inputs = MathUtil.getRandomPositiveIntArray(32, 1);
		int[] inputs2 = inputs.clone();
		IntArrayUtil.print(inputs, log);

		int value = new Median().getMedian(inputs);
		IntArrayUtil.print(inputs, log);
		log.debug("value=" + value);

		new MergeSort().sort(inputs2);
		System.out.println(Arrays.toString(inputs2));
		log.debug("value=" + inputs2[(inputs2.length - 1) / 2]);
	}
}
