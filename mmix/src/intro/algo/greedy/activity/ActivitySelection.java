/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.greedy.activity;

/**
 * <p>
 * ActivitySelection.java choose the as many activities as possible.
 * 
 * brute force: each activity, either choosen or not choosen. 2^n case to
 * consider.
 * 
 * Dynamic programming method
 * 
 * Greedy method:
 * </p>
 */
public class ActivitySelection {
	int[] s = new int[] { 1, 3, 0, 5, 3, 5, 6, 8, 8, 2, 12, };// start time
	int[] f = new int[] { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, };// finish time
	
	public int getMaxSize(){
		boolean [] include = new boolean[f.length];
		for(int i=0; i< include.length;i++){
			include[i]=true;
		}
		return 0;
	}
}
