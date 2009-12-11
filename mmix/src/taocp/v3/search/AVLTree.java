package taocp.v3.search;

/**
 * Hight balanced binary search tree (Not finished yet). Lessons learned. local
 * rotation is enough, the heighe increase will not impact other part besides
 * the subtree itself. so the cases are actually simple. 1)the subtree at the
 * leaf need to be adjusted, the ABCD in the case are all nulls( the
 * generalization is actually unnecessary.). after rotation, the tree is
 * balanced. 2) the subtree at the leaf do not need to balance, but the height
 * will increase 1, we need to back track to the root until we find the subtree
 * we need to rotate or until we get to the root.
 * 
 * 
 * 
 * To summarize, AVL tree is actually easy to maintain blance when inserting.
 * but a little hard when deleting (Not implemented yet). updating the key for
 * one record is same as deleting and adding one.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class AVLTree {
	private Node root = null;

	public boolean add(int a) {
		Node newNode = new Node();
		newNode.value = a;
		newNode.height = 1;// set it temporarily
		newNode.balance = 0;
		if (root == null) {
			root = newNode;
		}

		// There are two different things we need to consider.
		// whether the height is increased. (if so, will impact its parent)
		// whether the balance is broken.(need to rebalance locally)
		Node temp = root;
		while (temp != null) {
			if (temp.value == a) {
				return false;// already exist, can not insert.
			} else if (temp.value < a) {
				if (temp.right == null) {
					// insert here
					temp.right = newNode;
					newNode.father = temp;
					// simplest case: it will not impact the height of tree.
					// it is locally balanced
					if (temp.left != null) {
						temp.balance = 0;
						return true;
					} else {// may need rebalance!
						// temp sub tree's height increased 1.
						temp.balance = 1;
						temp.height = 2;

						rebalance(temp);
						return true;
					}
				} else {
					temp = temp.right;
				}
			} else {
				if (temp.left == null) {
					// insert here
					temp.left = newNode;
					newNode.father = temp;
					// simplest case: it will not impact the height of tree.
					// it is locally balanced
					if (temp.right != null) {
						temp.balance = 0;
						// height is not changed.
						return true;
					} else {
						temp.balance = -1;
						temp.height = 2;
						// may need rebalance!
						rebalance(temp);
						return true;
					}
				} else {
					temp = temp.left;
				}
			}
		}
		;
		return true;
	}

	/**
	 * 
	 * @param temp
	 *            temp's height is increase 1. need to adjust for its parent.
	 *            itself is already balanced.
	 */
	public void rebalance(Node temp) {
		if (temp.father == null)
			return;

		Node father = temp.father;
		Node left = father.left;
		Node right = father.right;

		Node ff = father.father;
		boolean isRoot = father.father == null;
		father.height++;// important.

		int bf;// balance for father
		int bc;// balance for child(temp)
		int leftH = left == null ? 0 : left.height;
		int rightH = right == null ? 0 : right.height;
		bf = rightH - leftH;// balance of father
		bc = temp.balance;// balance of child

		// will not cause imbalance, but the height
		// will increase 1.
		while (bf != 2 && bf != -2) {

			father = father.father;
			left = father.left;
			right = father.right;
			if (father == null)
				return;
			bc = bf;// balance of child
			leftH = left == null ? 0 : left.height;
			rightH = right == null ? 0 : right.height;
			bf = rightH - leftH;// balance of father
			
		}
		isRoot = father.father == null;
		if (bf == -2) {
			if (bc == 1) {// left right case
				// it can be adjusted locally, the height will decrease 1
				// after rebalance. assuming height of father is h+2.
				Node t = left.right;
				Node A = right; // h-1;
				Node B = left.left;// h-1
				Node C = left.right.left;// h-1 or h-2
				Node D = left.right.right;// h-1 or h-2, C,D can not have
				// height(h-2) at the same time
				// A< B< C< D may be null
				t.right = father;
				t.left = left;
				left.right = C;
				father.left = D;
				left.father = t;
				father.father = t;
				if (C != null)
					C.father = left;
				if (D != null)
					D.father = father;
				if (isRoot) {
					root = t;
				} else {
					t.father = ff;
					if (ff.left == father) {
						ff.left = t;
					} else {
						ff.right = t;
					}

				}
				father.height -= 2;
				t.height += 1;
				left.height -= 1;

				t.balance = 0;
				int hd = D == null ? 0 : D.height;
				int ha = A == null ? 0 : A.height;
				father.balance = ha - hd;
				int hb = B == null ? 0 : B.height;
				int hcc = C == null ? 0 : C.height;
				left.balance = hcc - hb;
				// adjust height and balance.
				return;
			} else if (bc == -1) {// left left case
				Node t = left.left;
				Node A = right; // h-1;
				Node B = left.right;// h-1
				Node C = t.left;// h-1 or h-2
				Node D = t.right;// h-1 or h-2, C,D can not have
				// height(h-2) at the same time
				// A< B< C< D may be null
				left.right = father;
				father.left = B;

				father.father = left;
				if (B != null)
					B.father = father;

				if (isRoot) {
					root = left;
				} else {
					left.father = ff;
					if (ff.left == father) {
						ff.left = left;
					} else {
						ff.right = left;
					}

				}

				father.height -= 2;
				// t.height+=1;
				// left.height-=1;
				left.balance = 0;
				int hd = D == null ? 0 : D.height;
				int hcc = C == null ? 0 : C.height;

				t.balance = hcc - hd;
				int hb = B == null ? 0 : B.height;
				int ha = A == null ? 0 : A.height;
				father.balance = ha - hb;
				return;
			}

		} else if (bf == 2) {
			if (bc == -1) {
				// ritht left case, symmetric with left right case.
				// it can be adjusted locally, the height will decrease 1
				// after rebalance. assuming height of father is h+2.
				Node t = right.left;
				Node A = left; // h-1;
				Node B = right.right;// h-1
				Node C = t.left;// h-1 or h-2
				Node D = t.right;// h-1 or h-2, C,D can not have
				// height(h-2) at the same time
				t.left = father;
				t.right = right;
				father.right = C;
				right.left = D;
				right.father = t;
				father.father = t;
				if (C != null)
					C.father = father;
				if (D != null)
					D.father = right;
				if (isRoot) {
					root = t;
				} else {
					t.father = ff;
					if (ff.left == father) {
						ff.left = t;
					} else {
						ff.right = t;
					}
				}

				father.height -= 2;
				t.height += 1;
				right.height -= 1;

				t.balance = 0;

				int hcc = C == null ? 0 : C.height;
				int ha = A == null ? 0 : A.height;
				father.balance = hcc - ha;
				int hb = B == null ? 0 : B.height;
				int hd = D == null ? 0 : D.height;
				right.balance = hb - hd;
				return;
			} else if (bc == 1) {// right right case.
				Node t = right.right;
				Node A = left; // h-1;
				Node B = right.left;// h-1
				Node C = t.left;// h-1 or h-2
				Node D = t.right;// h-1 or h-2, C,D can not have
				// height(h-2) at the same time
				// A< B< C< D may be null
				right.left = father;
				father.right = B;

				father.father = right;
				if (B != null)
					B.father = father;

				if (isRoot) {
					root = right;
				} else {
					right.father = ff;
					if (ff.left == father) {
						ff.left = right;
					} else {
						ff.right = right;
					}

				}

				father.height -= 2;

				right.balance = 0;
				int hd = D == null ? 0 : D.height;
				int hcc = C == null ? 0 : C.height;

				t.balance = hd - hcc;
				int hb = B == null ? 0 : B.height;
				int ha = A == null ? 0 : A.height;
				father.balance = hb - ha;
				return;
			} else
				return;

		}
	}

	public boolean search(int a) {
		Node node = root;
		while (node != null) {
			if (node.value == a) {
				return true;
			} else if (node.value < a) {
				node = node.right;
			} else {
				node = node.left;
			}
		}
		return false;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void print() {
		print(root);
		System.out.println();
	}

	private void print(Node temp) {
		if (temp == null)
			return;
		System.out.print("(");
		System.out.print(temp);
		System.out.print(",");
		System.out.print(temp.value);
		System.out.print(",");
		System.out.print(temp.height);
		System.out.print(",");
		System.out.print(temp.balance);
		System.out.print(")");
		System.out.print("[");
		print(temp.left);
		System.out.print("]");
		System.out.print("[");
		print(temp.right);
		System.out.print("]");

	}
}

class Node {
	int value;
	Node left;
	Node right;
	Node father;

	int height;
	// public int getHeight() {
	// int bl;
	// int br;
	// if (left == null) {
	// bl = 0;
	// } else {
	// bl = left.getHeight();
	// }
	// if (right == null) {
	// br = 0;
	// } else {
	// br = right.getHeight();
	// }
	// if (bl > br) {
	// return bl + 1;
	// } else {
	// return br + 1;
	// }
	// }

	int balance;
	// balance = height of right - height of left
	// public int getBalance() {
	// int bl;
	// int br;
	// if (left == null) {
	// bl = 0;
	// } else {
	// bl = left.getHeight();
	// }
	// if (right == null) {
	// br = 0;
	// } else {
	// br = right.getHeight();
	// }
	// return br - bl;
	// }
}