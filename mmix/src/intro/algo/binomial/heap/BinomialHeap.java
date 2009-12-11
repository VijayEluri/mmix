/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.binomial.heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * binomial heap is a colletion of binomial trees.
 * 
 * all the binomial Tree are represented by a Node. so a Node may point to the
 * root of a Binomial Tree or the root of binomial heap. In one binomial Heap,
 * only one Node point to root of the Heap. there are many Node point to the
 * root of Binomial Tree or SubTree.
 * 
 * 
 * <p>
 * BinomialHeap.java
 * </p>
 */
public class BinomialHeap {// implements MergeableHeap {
	Node root = null;

	int size;// number of Nodes in the forests(heap).

	// knuth did not implement it in SGB, because it does not
	// provide better performance for requeuing than binary heap
	// also it require a parent field in each Node
	//
	public void decreaseKey(Node x, int newKey) {
		if (newKey > x.key) {
			throw new RuntimeException(
					"new key is greater than current key; current key" + x.key
							+ "; new key=" + newKey);
		}
		x.key = newKey;
		Node y = x;
		// Node z=y.parent;
	}

	/**
	 * 
	 */
	public void delete(Node x) {
		decreaseKey(x, Integer.MIN_VALUE);
		extractMin();
	}

	/**
	 * according to Introduction to Algorithm.
	 */
	public Node extractMin() {
		if (isEmpty()) {
			return null;
		}
		int min = Integer.MAX_VALUE;
		Node minNode = null;
		for (Node r = root; r != null; r = r.sibling) {
			if (r.key < min) {
				min = r.key;
				minNode = r;
			}
		}
		System.out.println("minNode=" + minNode);
		Node pre = null;
		for (Node r = root; r != null && r != minNode; r = r.sibling) {
			pre = r;
		}
		size -= (int) Math.pow(2, minNode.getDegree());
		if (pre == null) {// easy case.
			root = minNode.sibling;
			if (minNode.getDegree() == 0) {
				return minNode;
			}
		} else {
			pre.sibling = minNode.sibling;// delete(minNode);
		}
		System.out.println("after delete minNode Tree, heap become");
		print();

		// reorganize the childs in minNode to be a new binomial Heap
		BinomialHeap bh = new BinomialHeap();
		bh.size = (int) Math.pow(2, minNode.getDegree()) - 1;

		List<Node> ll = new ArrayList<Node>();
		Node t = minNode.child;

		System.out.println("the children list of minNode");
		for (; t != null; t = t.sibling) {
			ll.add(t);
			System.out.println(t);
		}
		Collections.reverse(ll);

		Node pp = new Node();
		bh.root = pp;
		for (Node d : ll) {
			pp.sibling = d;
			pp = d;
			d.sibling = null;
		}
		bh.root = bh.root.sibling;
		System.out.println("Heap of minNode Tree");
		bh.print();

		union(bh);
		System.out.println("after Union, heap become");
		print();

		return minNode;
	}

	/**
	 * 
	 * @param x
	 *            only Key is set up by caller
	 */
	public void insert(Node x) {
		BinomialHeap b = new BinomialHeap();
		b.root = x;
		b.size = 1;
		union(b);

	}

	public Node min() {
		if (isEmpty()) {
			return null;
		}
		int min = 0;
		Node minNode = null;
		for (Node r = root; r != null; r = r.sibling) {

			if (r.key < min) {
				min = r.key;
				minNode = r;
			}
		}
		return minNode;
	}

	/**
	 * the folowing parameter is not real field, they are used to ease the mthod
	 * call.
	 */
	transient int k = 1;

	transient Node p = null;

	transient Node c = null;

	Node q = null;

	Node qq = null;

	Node r = null;

	Node rr = null;

	int key = 0;

	public void resetParam() {
		k = 1;
		p = null;
		c = null;
		q = null;
		qq = null;
		r = null;
		rr = null;
		key = 0;
	}

	/**
	 * bb is not null
	 */

	public void union(BinomialHeap b) {
		//BinomialHeap b = (BinomialHeap) bb;
		int total = size + b.size;
		q = root;
		qq = b.root;

		k = 1;

		while (size != 0) {
			if ((size & k) == 0) {//blank in this position for this instance.
				if ((b.size & k) != 0) { // qq goes into the merged list
					merge(qq);				
					b.size -= k;
					if (b.size != 0) {
						qq = qq.sibling;
					}
					p.sibling=null;
				}
			} else if ((b.size & k) == 0) { // q goes into the merged list
				merge(q);				
				size -= k;
				if (size != 0) {
					q = q.sibling;
				}
				p.sibling=null;
			} else {// need to merge q and qq.until find a blank position
				mergeQ_QQ(b);
			}
			k <<= 1;
		}// while
		if (b.size != 0) {
			merge(qq);
		} else {
			p.sibling = null;// terminate the loop
		}
		size = total;
		resetParam();
		return;

	}

	public void mergeQ_QQ(BinomialHeap b) {

		size -= k;
		if (size != 0) {
			r = q.sibling;
		}
		b.size -= k;
		if (b.size != 0) {
			rr = qq.sibling;
		}
		// Set c to the combination of q and qq 47 i;
		combineQ_QQ();
		k <<= 1;
		q = r;
		qq = rr;
		while (((size | b.size) & k) != 0) {// already have a carry
			if ((size & k) == 0) {
				// h Merge qq into c and advance qq 49 i
				mergeQQ_C(b);
			} else {
				// h Merge q into c and advance q 48 i;
				mergeQ_C();
				if ((b.size & k) != 0) {// the third one is put into current
					// rank.
					merge(qq);
					// p = qq;
					b.size -= k;
					if (b.size != 0) {
						qq = qq.sibling;
					}

				}
			}
			k <<= 1;
		}
		merge(c);//
		p.sibling=null;
	}

	private void combineQ_QQ() {
		System.out.println("method combineQ_QQ; k="+k);
		if (q.key < qq.key) {
			c = q;
			key = q.key;
			q = qq;
		} else {
			c = qq;
			key = qq.key;
		}// c is the one with smaller root
		if (k == 1) {
			c.child = q;
			c.sibling = null;
			q.sibling = null;
		} else {
			qq = c.child;

			c.child = q;
			c.sibling = null;
			// if (k == 2) {
			// q.sibling = qq;
			// } else {
			// q.sibling = qq.sibling;
			// }
			q.sibling = qq;
			// qq.sibling = q;//circle is not necessary!
		}
		c.printTree();
	}

	private void mergeQ_C() {// c.s min = key
		System.out.println("method mergeQ_C; k="+k);
		size -= k;
		if (size != 0) {
			r = q.sibling;
		}

		if (q.key < key) {
			rr = c;// reuse rr, since now qq is same as rr.
			c = q;
			key = q.key;
			q = rr;
		}
		rr = c.child;
		c.child = q;
		// if (k == 2) {
		// q.sibling = rr;
		// } else {
		// q.sibling = rr.sibling;
		// }
		// rr.sibling = q;
		q.sibling = rr;
		q = r;
		c.printTree();
	}

	private void mergeQQ_C(BinomialHeap b) {// c.s min = key
		System.out.println("method mergeQQ_C; k="+k);
		b.size -= k;
		if (b.size != 0) {
			r = qq.sibling;
		}

		if (qq.key < key) {
			rr = c;// reuse rr, since now qq is same as rr.
			c = qq;
			key = qq.key;
			qq = rr;
		}
		rr = c.child;
		c.child = qq;
		// if (k == 2) {
		// qq.sibling = rr;
		// } // only single link
		// else {
		// qq.sibling = rr.sibling;
		// }
		// rr.sibling = qq;
		// TODO: what is the benefit of circle list.
		qq.sibling = rr;
		qq = r;
		c.printTree();
	}

	/**
	 * 
	 * @param qq
	 */
	private void merge(Node qq) {
		if (p == null) {
			p = qq;
			root = p;
		} else {
			p.sibling = qq;
			p = qq;
		}
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void print() {
		System.out.println("binomial heap m=" + size);
		Node r = root;
		while (r != null) {
			r.printTree();
			r = r.sibling;
		}
	}

}
