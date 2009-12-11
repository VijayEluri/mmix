/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

/**
 * <p>
 * BooleanUtil.java
 * </p>
 */
public class BooleanUtil {
	public static boolean[] getBooleanInput(int boolTableIndex, int numOfVar) {
		boolean[] res = new boolean[numOfVar];
		String s = Integer.toBinaryString(boolTableIndex);
		byte[] cc = s.getBytes();
		for (int i = 0; i < numOfVar - cc.length; i++) {
			res[i] = false;
		}
		for (int i = 0; i < cc.length; i++) {
			res[numOfVar - cc.length + i] = cc[i] == '0' ? false : true;
		}
		return res;
	}
}
