/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

import java.util.HashMap;

/**
 * <p>
 * It synthesis BDD from two or more BDDs.
 * 
 * The idea is to melded the root node, depends on whether the index in node are
 * equal or not, the extention to the next level is updated. recursive in this
 * way, and do some local reducing can ensure we get a Synthesised BDD in lesss
 * then O(B(f) * B(g)). Without local reducing, we will get regular tree with
 * (2^n + 3) nodes, 4 sinks are included.
 * 
 * </p>
 */
public class BDDSynthesiser {
	HashMap<NodePair, Node> beads;
    int n;
    BDD bdd = new BDD();
	/**
	 * a precedes b in the node pair.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public BDD meld(BDD a, BDD b) {
		beads = new HashMap<NodePair, Node>();
		n = a.numOfVar;
		Node ra = a.root;
		Node rb = b.root;
		Node np = meld(ra, rb);
		
		
		bdd.root=np;
		//1 and, 2 or, 3 xor
		reduce(bdd, 1);
		return bdd;
	}

	private void reduce(BDD a, int operation) {
		for(int i=0 ; i<a.numOfNode;i++){
			if(a.nodes[i].i==n+1){
				
			}
		}
		
	}

	private Node meld(Node ra, Node rb) {
		NodePair np = new NodePair();
		Node node;
		np.a = ra;
		np.b = rb;

		if (ra.i == rb.i) {
			np.index = ra.i;
			Node temp = beads.get(np);
			if (temp != null) {
				return temp;
			}
			
			node = new Node();
			bdd.nodes[bdd.numOfNode++]=node;
			if(ra.i==n+1){
				//meaning is changed to accomodate more data.
				node.lo = ra;
				node.hi = rb;
			}else{
				node.lo = meld(ra.lo, rb.lo);
				node.hi = meld(ra.hi, rb.hi);
			}
			// return np;
		} else if (ra.i < rb.i) {
			np.index = ra.i;
			Node temp = beads.get(np);
			if (temp != null) {
				return temp;
			}
			node = new Node();
			bdd.nodes[bdd.numOfNode++]=node;
			node.lo = meld(ra.lo, rb);
			node.hi = meld(ra.hi, rb);
			// return np;
		} else {
			np.index = rb.i;
			Node temp = beads.get(np);
			if (temp != null) {
				return temp;
			}
			node = new Node();
			bdd.nodes[bdd.numOfNode++]=node;
			node.lo = meld(ra, rb.lo);
			node.hi = meld(ra, rb.hi);
			// return np;
		}
		beads.put(np, node);
		return node;
	}
}

/**
 * to help detect duplicate node pairs.
 * 
 * @author eddie.wu
 * 
 */
class NodePair {
	int index;
	Node a;
	Node b;
	NodePair left;
	NodePair right;

	@Override
	public boolean equals(Object pair) {
		if (!(pair instanceof NodePair))
			return false;
		NodePair p = (NodePair) pair;
		return b == p.b && a == p.a;
	}

	@Override
	public int hashCode() {
		return a.hashCode() + b.hashCode();
	}

}
