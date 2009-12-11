/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package util;

import java.util.Arrays;

/**
 * <p>
 * ArrayUtil.java
 * </p>
 */
public class ArrayUtil {
	public static void output2DArray(int[][] res) {
		for(int i = 0; i < res.length; i++){
			System.out.println(Arrays.toString(res[i]));
		}
	}
}
