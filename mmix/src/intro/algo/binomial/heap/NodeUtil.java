package intro.algo.binomial.heap;

import java.util.ArrayList;
import java.util.List;

public class NodeUtil {
	/**
	 * Treat this as the root of the tree and print out the tree.
	 */
	public static void printTree(LeftChildRightSiblingNode rootNode) {
		/**
		 * auxiliary list for output the tree rooted at this node.
		 */
		List<LeftChildRightSiblingNode> list = new ArrayList<LeftChildRightSiblingNode>();

		List<LeftChildRightSiblingNode> list2 = new ArrayList<LeftChildRightSiblingNode>();

		// resetParam();
		int level = rootNode.getDegree();
		// the depth of Node with (degree) children is (degree+1), labeled from
		// 0 to degree.
		System.out.println("Tree: root degree=" + level);
		System.out.println(rootNode + " -> ");// root have no sibling.
		if (level > 0) {
			list.add(rootNode);
		}
		while (!list.isEmpty()) {// loop on levels
			for (LeftChildRightSiblingNode node : list) {
				if (node.getChild() != null) {// redundant
					printLevel(node.getChild(), list2);
				}
				System.out.print("\t");
			}
			System.out.println();
			list.clear();
			List<LeftChildRightSiblingNode> t = list;
			list = list2;
			list2 = t;
		}
		// resetParam();
	}

	private static void printLevel(LeftChildRightSiblingNode r,
			List<LeftChildRightSiblingNode> list2) {
		LeftChildRightSiblingNode s = r;
		do {
			System.out.print(s + " -> ");
			if (s.getDegree() > 0) {
				list2.add(s);
			}
			s = s.getSibling();
		} while (s != null && s != r);// no matter circle or not.
	}
	
	
}
