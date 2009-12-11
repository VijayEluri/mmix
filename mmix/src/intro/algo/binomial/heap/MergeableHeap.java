/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.binomial.heap;

/**
 * <p>
 * MergeableHeap.java
 * </p>
 */
public interface MergeableHeap {
	void insert(Node x);

	Node min();

	Node extractMin();

	void union(MergeableHeap b);

	void delete(Node x);

	void decreaseKey(Node x, int newKey);

}
