/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort.count;

import java.util.BitSet;

/**
 * <p>SpecialSort.java
 * </p>
 */
public class SpecialSort {
	/**
	 * assuming all the elemets are less than 2^20
	 * all the duplicate elements will be removed.
	 * So it is only suitable for special case.
	 * @param a
	 */
	public static int[] sort(int[] a){
		BitSet bs = new BitSet(1<<20);
		for(int i =0; i < a.length;i++){
			bs.set(a[i]);
		}
		int count=0;
		int [] b=new int[bs.cardinality()];
		for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
			b[count++]=i;
		}
		return b;
			
	}
}
