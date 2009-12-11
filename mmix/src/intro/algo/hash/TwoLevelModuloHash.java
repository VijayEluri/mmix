/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.hash;

import java.util.List;

/**
 * <p>
 * TwoLevelModuloHash.java
 * </p>
 */
class TwoLevelModuloHash implements Hash{
	int a, b;// a in [1,p-1], b in [0,p-1]
	int p;// first level modulo
	int m;// second level modulo

	public void setA(int a) {
		this.a = a;
	}

	public void setB(int b) {
		this.b = b;
	}

	public TwoLevelModuloHash() {
	}

	public TwoLevelModuloHash(int a, int b) {
		this.a = a;
		this.b = b;
	}

	public void setP(int p) {
		this.p = p;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int hash(int k) {
		return ((a * k + b) % p) % m;
	}

	

	

	

	public static TwoLevelModuloHash getGoodHash(int p, int m, int[] a) {
		TwoLevelModuloHash gh = new TwoLevelModuloHash();
		gh.setP(p);
		gh.setM(m);
		int times=0;
		external: for (int i = 1; i < p; i++) {// a
			for (int j = 0; j < p; j++) {// b
				times++;
				gh.setA(i);
				gh.setB(j);

				if (HashUtil.isFirstLevelGoodHash(gh, a, 2 * a.length + 1)) {
					System.out.println("after try " + times + " times; a=" + i
							+ "; b=" + j + ";");
					break external;
				}// if
			}
		}
		return gh;
	}
}