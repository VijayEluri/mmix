/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bitwise;

/**
 * <p>
 * CountBit.java
 * </p>
 */
public class CountBit {
	

	

	

	public static void main(String[] args) {
		
		boolean notEqual = false;
		long i = 0;
		for( i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++){
			if(CountBit.countBit_k1((int)i) != CountBit.countBit_k2((int)i)){
				notEqual = true;
				break;
			}
			if(i % 1000000 ==0){
				System.out.println(i);
			}
		}
		if(notEqual){
			System.out.println("failure when i=" + i + "; " +CountBit.countBit_k1((int)i) + "; " +CountBit.countBit_k2((int)i));
		}else{
			System.out.println("success");
		}
	}

	/**
	 * initial version in MMIXWare easy to understand if you know the Bitwise
	 * trick. total 19v
	 * 
	 * @param xx
	 * @return
	 */
	public static int countBit_k1(int xx) {
		xx = (xx & 0x55555555) + ((xx >>> 1) & 0x55555555);// 4v
		xx = (xx & 0x33333333) + ((xx >>> 2) & 0x33333333);// 4v
		xx = (xx & 0x0f0f0f0f) + ((xx >>> 4) & 0x0f0f0f0f);// 4v
		xx = (xx & 0x00ff00ff) + ((xx >>> 8) & 0x00ff00ff);// 4v
		return (xx & 0x0000ffff) + (xx >>> 16);// 3v

	}

	/**
	 * 23v. good scalability.?
	 * 
	 * @param xx
	 * @return
	 */
	public static int countBit_k1(long xx) {
		xx = (xx & 0x5555555555555555L) + ((xx >>> 1) & 0x5555555555555555L);// 4v
		xx = (xx & 0x3333333333333333L) + ((xx >>> 2) & 0x3333333333333333L);// 4v
		xx = (xx & 0x0f0f0f0f0f0f0f0fL) + ((xx >>> 4) & 0x0f0f0f0f0f0f0f0fL);// 4v
		xx = (xx & 0x00ff00ff00ff00ffL) + ((xx >>> 8) & 0x00ff00ff00ff00ffL);// 4v
		xx = (xx & 0x0000ffff0000ffffL) + ((xx >>> 16) & 0x0000ffff0000ffffL);// 4v
		return (int) (xx & 0x00000000ffffffff + (xx >>> 32));// 3v

	}

	/**
	 * Revised version from MMIXWare more complex, but it save 4v 
	 * (total 15v)
	 * 
	 * @param xx
	 * @return
	 */
	public static int countBit_k2(int xx) {
		xx = xx - ((xx >>> 1) & 0x55555555);// 3v
		xx = (xx & 0x33333333) + ((xx >>> 2) & 0x33333333);// 4v
		xx = (xx + (xx >>> 4)) & 0x0f0f0f0f;// 3v
		xx = xx + (xx >>> 8);// 2v
		return (xx + (xx >>> 16)) & 0xff;// 3v
	}

	/**
	 * Revised version from MMIXWare more complex, but it save 4v (total 14v)
	 * total 17v
	 * 
	 * @param xx
	 * @return
	 */
	public static int countBit_k2(long xx) {
		xx = xx - ((xx >>> 1) & 0x5555555555555555L);// 3v
		xx = (xx & 0x3333333333333333L) + ((xx >>> 2) & 0x3333333333333333L);// 4v
		xx = (xx + (xx >>> 4)) & 0x0f0f0f0f0f0f0f0fL;// 3v
		xx = xx + (xx >>> 8);// 2v
		xx = xx + (xx >>> 16);// 2v
		return (int) ((xx + (xx >>> 32)) & 0xff);// 3v
	}

	private static final long a = 0x0101010101010101L;
	private static final int aa = 0x01010101;

	/**
	 * Revised version from TAOCP volume 4 - bitwise tricks and techniques total
	 * 13v
	 * 
	 * @param xx
	 * @return
	 */
	public static int countBit_k3(long xx) {
		xx = xx - ((xx >>> 1) & 0x5555555555555555L);// 3v
		xx = (xx & 0x3333333333333333L) + ((xx >>> 2) & 0x3333333333333333L);// 4v
		xx = (xx + (xx >>> 4)) & 0x0f0f0f0f0f0f0f0fL;// 4v		
		return (int) ((xx * a) >>> 56);// 3v
	}
	
	public static int countBit_k3(int xx) {
		xx = xx - ((xx >>> 1) & 0x55555555);// 3v
		xx = (xx & 0x33333333) + ((xx >>> 2) & 0x33333333);// 4v
		xx = (xx + (xx >>> 4)) & 0x0f0f0f0f;// 4v		
		return (int) ((xx * aa) >>> 24);// 3v
	}
}
