/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f0.booleanevaluation;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * TruthTableExtension.java
 * </p>
 * 
 * get the equivalent truth table. consider the variable name is exchangeable.
 */
public class TruthTableExtension {
	/**
	 * 
	 * @param t
	 *            truth table for n<=5 normal function; so t >=0;
	 * @return
	 */
	public static List<Integer> extendTable(int t, int n) {
		int nn= 1<<n;
		List<Integer> list = new ArrayList<Integer>();
		int[] x = new int[nn];
		for(int i=0; i <nn; i++){
			x[i]=(t>>>(nn-1-i))&1;
		}
		
		return list;
	}
	
	public static List<Integer> extendTableN4(int t) {
		int n = 4;
		int nn= 1<<n;
		List<Integer> list = new ArrayList<Integer>();
		int[] x = new int[nn];
		for(int i=0; i <nn; i++){
			x[i]=(t>>>(nn-1-i))&1;
		}
		
		int[][] yy = new int[48][nn];
		for (int i=0; i <nn; i++){
			
		}
		
		return list;
	}
}
