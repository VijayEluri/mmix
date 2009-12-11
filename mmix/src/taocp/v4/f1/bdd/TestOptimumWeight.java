/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

import util.MathUtil;
import junit.framework.TestCase;

/**
 * <p>Find Optimum weight using  
 * </p>
 */
public class TestOptimumWeight extends TestCase{
	static int n = 100;
	/*
	 * w_j = (-1)^{vj}
	 */
	public void test(){
		int[] a = new int[n+1];
		for(int i=1;i<=n;i++){
			a[i]=MathUtil.getOddParity(i)? -1 : 1;
			if(a[i]==1){
				System.out.println((i)+",");
			}
		}
	}

}
