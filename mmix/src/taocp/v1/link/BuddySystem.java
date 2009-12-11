/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v1.link;

/**
 * <p>
 * BuddySystem.java
 * </p>
 */
public class BuddySystem {
	public static final int K = 1024;// 1K
	public static final int M = 1024 * 1024;// 1M
	public static final int PageSize = 4 * 1024;// 4K
	public static final int BigPageSize = 4 * M;// 4M

	// we can not always use the memory space itself to manage the memory.
	// just like the Page struct used in Kernel should be in seperate memory.
	// because they exist even when the memory is allocated.
	// Here we only compare the algorithm, do not need to care the
	// implementation
	// detail, we even do not need to really manage the memory.

	byte[] pages = new byte[4 * K];//mean 16M memory

	/**
	 * if only need to know whether the page is availabe, one bitset is enough.
	 * but it may requrei sequencial scanning when we want to find the match.
	 */
	public byte[] getMem(int length) {

		return null;
	}
}
