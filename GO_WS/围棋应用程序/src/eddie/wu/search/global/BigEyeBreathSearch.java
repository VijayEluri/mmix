package eddie.wu.search.global;

import eddie.wu.domain.Point;

/**
 * if a block is kill-able, we need to know how many steps we need to kill it.
 * this information is useful in breath race (liberty race). <br/>
 * since we already have the complete search tree in case the target is dead, we
 * can get the max breath easily from the search Tree afterward.<br/>
 * currently we just go through the search tree result and check how many passes
 * are there. it is not always correct. To ensure its correctness, we have to
 * consider PASS for defender. but it is still not enough because we did not
 * prefer the failure move which has longer path/breath.
 * <br/>
 * New Idea: do breath search only when target is dead. then the search target is 
 * to max-min the breath! 
 * 
 * @author Eddie
 * 
 */
public class BigEyeBreathSearch extends BigEyeSearch {

	public BigEyeBreathSearch(byte[][] state, Point target, int targetColor,
			boolean targetFirst, boolean targetLoopSuperior) {
		super(state, target, targetColor, targetFirst, targetLoopSuperior);
		// TODO Auto-generated constructor stub
	}

}
