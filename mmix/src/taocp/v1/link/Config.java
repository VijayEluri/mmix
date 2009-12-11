/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v1.link;

import java.util.Random;

/**
 * <p>
 * Config.java
 * </p>
 */
public class Config {
	Random r = new Random();
	public int[] getSizeSeries() {
		int[] a = new int[BuddySystem.M];
		
		return a;
	}
	public int[] getRandomSizeSeries() {
		
		int[] a = new int[1500];
		for(int i = 0; i<a.length; i++){
			a[i]=r.nextInt(BuddySystem.K);
		}
		return a;
	}
}
