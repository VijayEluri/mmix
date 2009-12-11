/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * BDDReducer.java
 * </p>
 */
public class BDDReducer {
	static Node[] empty = new Node[0];

	/**
	 * maintain list for each level (variable index) hh[0] is the list for level
	 * 1. hh[n-1] is the list for level n;
	 */
	List<Node>[] hh;

	Map<Node, Node> beads;

	Map<Pair, Node> bb;

	public void close() {
		hh = null;
		beads = null;
		bb = null;
	}

	/**
	 * Try to implement one before I really understand the algorithm
	 * description.
	 * 
	 * @param bdd
	 * @return
	 */
	public void reduce(BDD bdd) {
		int n = bdd.numOfVar;

		// each node has a link to bead, several nodes may share same bead.
		hh = new ArrayList[n];
		beads = new HashMap<Node, Node>();
		bb = new HashMap<Pair, Node>();

		initHeads(bdd);

		for (int i = n - 1; i >= 0; i--) {
			System.out.println("for level=" + (i + 1));

			List<Node> head = hh[i];
			// 1. update hi lo link to point to bead.
			updateLoHI(head);
			// 2. second loop to find node (lo==hi) and
			for (Iterator<Node> it = head.iterator(); it.hasNext();) {
				Node node = it.next();
				if (node.lo == node.hi) {
					beads.put(node, node.lo);
					if (node == bdd.root) {
						bdd.root = node.lo;
					}
					System.out.println("add " + node + " to beads for lo==hi");
					node.removed = true;
					it.remove();// still in the BDD.
				} else {
					// decide bead for equivalent node.
					bb.put(node.getPair(), node);
					System.out.println("add  " + node + "to pair map.");
				}
			}
			// update the
			for (Iterator<Node> it = head.iterator(); it.hasNext();) {
				Node node = it.next();
				Node bead = bb.get(node.getPair());
				beads.put(node, bead);
				System.out.println("bead = " + bead);
				System.out.println("node = " + node);
				if (node != bead) {
					node.removed = true;
					it.remove();
					System.out.println("remove duplicate node: " + node);
				} else {
					System.out.println("a bead" + node);
				}
			}
			// then the node is possible to be equivalent to ohter nodes in
			// same level. depends on whether they have same lo and hi.
		}
		bdd.numOfNode = getNumOfNode(bdd);
		System.out.println("number of node= " + bdd.numOfNode);
		close();
		// return bdd;
	}

	private void updateLoHI(List<Node> head) {
		for (Node node : head) {
			if (node.lo.removed) {
				node.lo = beads.get(node.lo);
			}
			if (node.hi.removed) {
				node.hi = beads.get(node.hi);
			}
		}
	}

	private int getNumOfNode(BDD bdd) {

		int count = 0;
		for (int i = 0; i < hh.length; i++) {
			for (Node node : hh[i]) {
				if (node.removed == false) {
					count++;
				}
			}
		}

		List<Node> list = new ArrayList<Node>(count + 2);
		for (int i = 0; i < bdd.nodes.length; i++) {
			if (bdd.nodes[i] == null) {
				break;
			} else if (bdd.nodes[i].removed == true) {

			} else {
				list.add(bdd.nodes[i]);
			}
		}
		bdd.nodes = list.toArray(empty);

		return count + 2;// false and true node should be included.
	}

	public void initHeads(BDD bdd) {
		for (int i = 0; i < hh.length; i++) {
			hh[i] = new ArrayList<Node>();
		}
		traverse(bdd.root);
		// reset removed tag.
		for (int i = 0; i < hh.length; i++) {
			for (Node node : hh[i]) {
				node.removed = false;
			}
		}
	}

	private void traverse(Node node) {
		if (node.isTerminator() || node.removed) {
			return;
		}

		hh[node.i - 1].add(node);
		node.removed = true;
		System.out.println("level = " + node.i + "; " + node.toString());

		traverse(node.lo);
		traverse(node.hi);
	}
}

class Pair {
	Node hi;
	Node lo;

	@Override
	public boolean equals(Object pair) {
		if (!(pair instanceof Pair))
			return false;
		Pair p = (Pair) pair;
		return lo == p.lo && hi == p.hi;
	}

	@Override
	public int hashCode() {
		return hi.hashCode() + lo.hashCode();
	}

}
