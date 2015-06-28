package eddie.wu.manual;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardSymmetry;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.SymmetryResult;

/**
 * result of search tree; left is child, right is brother.<br/>
 * Design Change, there is an dummy root node (without step)
 * 
 * @author Eddie Wu
 * 
 */
public class SearchNode {
	SearchNode child;
	SearchNode brother;
	SearchNode farther;

	private int score;
	private boolean max;
	private Step step;
	private String jieshuo;
	int variant;// 走完该步所致状态所拥有变化的数目

	public SearchNode(Step step) {
		this.step = step;
	}

	public static SearchNode getSpecialRoot() {
		// root has no step (to it)
		return new SearchNode();
	}

	private SearchNode() {

	}

	public SearchNode getCopy(SymmetryResult sym) {
		Step step2 = null;
		SearchNode copy = null;
		if (step == null) {
			copy = SearchNode.getSpecialRoot();
		} else {
			step2 = step.getCopy();
			step2.convert(sym);
			copy = new SearchNode(step2);
		}
		copy.score = this.score;
		copy.max = this.max;
//		System.out.println("before Copy " + step.toNonSGFString());
//		System.out.println("after Copy " + step2.toNonSGFString());
		return copy;
	}

	public SearchNode getChild() {
		return child;
	}

	public void setChild(SearchNode child) {
		this.child = child;
	}

	public SearchNode getBrother() {
		return brother;
	}

	public void setBrother(SearchNode brother) {
		this.brother = brother;
	}

	public SearchNode getFather() {
		return farther;
	}

	public void setFather(SearchNode father) {
		this.farther = father;
	}

	public String getJieshuo() {
		return jieshuo;
	}

	public void setJieshuo(String jieshuo) {
		this.jieshuo = jieshuo;
	}

	/**
	 * the methods below define the data structure of the tree.
	 * 
	 * @param child
	 */
	public void addChild(SearchNode child) {
		if (this.child == null) {
			this.child = child;
			child.farther = this;
			return;
		}
		this.child.addBrother(child);
	}

	public void addBrother(SearchNode brother) {
		SearchNode temp = this;
		while (temp.brother != null) {
			temp = temp.brother;
		}
		temp.brother = brother;
		brother.farther = this.farther;
	}

	public void addChildren(List<SearchNode> list) {
		assert this.child == null;
		assert list.isEmpty() == false;
		for (int i = 0; i < list.size() - 1; i++) {
			list.get(i).brother = list.get(i + 1);
			list.get(i + 1).farther = this.farther;
		}
		this.child = list.get(0);
		list.get(0).farther = this.farther;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isMax() {
		return max;
	}

	public void setMax(boolean max) {
		this.max = max;
	}

	public Step getStep() {
		return step;
	}

	@Override
	public String toString() {
		if (this.step == null) {
			return "ROOT" + " score = " + score;
		}
		return this.step.toString() + " score = " + score;
	}

	/**
	 * (;B[qe]VW[aa:sj]FG[257:Dia. 1]MN[1];W[re];B[qf];W[rf];B[qg];W[pb];B[ob]
	 * ;W[qb]LB[rg:a]N[Diagram 1]) <br/>
	 * use "(" when branching
	 * 
	 * @return
	 */
	int depth = 0;

	public void getSGFBodyString(StringBuilder sb, boolean sgf) {
		if (this.brother == null) {
			// no variant
			if (sgf) {
				sb.append(this.getStep().toSGFString());
			} else {
				sb.append(this.getStep().toNonSGFString() + ", score=" + score
						+ ", max=" + max);
			}
			if (this.child != null) {
				this.child.setDepth(depth);
				child.getSGFBodyString(sb, sgf);
			}
		} else {
			// branching
			sb.append(Constant.lineSeparator);
			for (int i = 1; i <= depth; i++) {
				sb.append("\t");
			}
			sb.append("(");
			depth++;
			if (sgf) {
				sb.append(this.getStep().toSGFString());
			} else {
				sb.append(this.getStep().toNonSGFString());
				sb.append(" (variant=" + this.getVariant() + ", score=" + score
						+ ", max=" + max + ") ");
			}

			if (this.child != null) {
				this.child.setDepth(depth);
				this.child.getSGFBodyString(sb, sgf);
			}
			sb.append(")");
			depth--;

			// this.brother.setDepth(depth);
			// sb.append(brother.toSGFBodyString());
			SearchNode brother = this.brother;
			while (brother != null) {
				sb.append(Constant.lineSeparator);
				for (int i = 1; i <= depth; i++) {
					sb.append("\t");
				}
				depth++;

				sb.append("(");
				if (sgf) {
					sb.append(brother.getStep().toSGFString());
				} else {
					sb.append(brother.getStep().toNonSGFString());
					sb.append(" (variant=" + brother.getVariant() + ", score="
							+ brother.getScore() + ", max=" + brother.isMax()
							+ ") ");
				}
				// sb.append("variant=" + brother.getVariant());
				if (brother.child != null) {
					brother.child.setDepth(depth);
					brother.child.getSGFBodyString(sb, sgf);
				}
				sb.append(")");
				depth--;
				// brother.child.setDepth(depth);
				brother = brother.brother;
			}

		}

		// if (this.child == null) {
		//
		// }else if(this.child.brother==null){
		// sb.append(this.getStep().toSGFString());
		// sb.append(this.child.toSGFBodyString());
		// }else{
		//
		// }
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getVariant() {
		return variant;
	}

	public List<Step> getPathFromRoot() {
		assert this.step != null && this.child == null;
		List<Step> list = new ArrayList<Step>();
		SearchNode temp = this;
		while (temp != null && temp.step != null) {
			list.add(temp.step);
			temp = temp.farther;
		}
		java.util.Collections.reverse(list);
		return list;
	}

	public String getSingleManualStringToRoot(boolean sgf) {
		if (step == null) {
			return "INIT";
		}
		List<Step> list = getPathFromRoot();
		if (sgf) {
			return SimpleGoManual.getBodySGFString(list);
		} else {
			return SimpleGoManual.getBodyNonSGFString(list);
		}
	}

	public String getSingleManualString() {
		assert this.child == null;
		return step.toSGFString();
	}

	private static int countPasses;
	private static SearchNode longestBreath;

	public List<Step> getLongestBreathPath() {
		assert step == null;
		countPasses = 0;
		longestBreath = null;
		getLongestBreathPath_internal();
		System.out.println("longest path passes = " + countPasses);
		return longestBreath.getPathFromRoot();
	}

	private void getLongestBreathPath_internal() {
		if (this.child == null) {
			List<Step> list = this.getPathFromRoot();
			int temp = Step.getPasses(list);
			if (temp > countPasses) {
				countPasses = temp;
				longestBreath = this;
			}
			return;
		}

		SearchNode temp = child;
		while (temp != null) {
			temp.getLongestBreathPath_internal();
			temp = temp.brother;
		}
	}

	/**
	 * the variant after this move.
	 * 
	 * @return
	 */
	int getExpandedString(StringBuilder sb, boolean sgf) {
		if (this.child == null) {
			this.variant = 1;
			sb.append(this.getSingleManualStringToRoot(sgf));
			sb.append(Constant.lineSeparator);
			return 1;

		}

		if (this.child.brother == null) {
			this.variant = this.child.getExpandedString(sb, sgf);
			return variant;
		}

		SearchNode temp = child;
		int count = 0;
		while (temp != null) {
			count += temp.getExpandedString(sb, sgf);
			// temp.variant = count;
			temp = temp.brother;
		}
		this.variant = count;
		return count;

	}

	/**
	 * whether next move/step is already included in current sub-tree.
	 * 
	 * @param move
	 * @return
	 */
	public boolean containsChildMove(Step move) {
		return this.getChild(move) != null;
	}

	public SearchNode getChild(Step move) {
		if (child == null)
			return null;
		if (child.getStep().getPoint() == null) {
			if (move.getPoint() == null)
				return child;
		} else if (child.getStep().equals(move))
			return child;
		if (child.brother == null)
			return null;
		SearchNode temp = child.brother;
		while (temp != null) {
			if (temp.getStep().getPoint() == null) {
				if (move.getPoint() == null)
					return child;
			} else if (temp.getStep().equals(move)) {
				return temp;
			}
			temp = temp.brother;
		}
		return null;
	}

	/**
	 * initialize the score assuming only terminator has score assigned.
	 */
	public int initScore() {
		if (this.child == null)
			return this.score;
		SearchNode temp = this.child.brother;
		if (this.isMax()) {
			int max = child.initScore();
			while (temp != null) {
				if (temp.initScore() > max) {
					max = temp.getScore();
				}
				temp = temp.brother;
			}
			this.score = max;
			return max;
		} else {
			int min = child.initScore();
			while (temp != null) {
				if (temp.initScore() < min) {
					min = temp.getScore();
				}
				temp = temp.brother;
			}
			this.score = min;
			return min;
		}
	}

	public int countSteps() {
		int count = 0;
		SearchNode temp = this;
		while (temp != null) {
			count++;
			temp = temp.farther;
		}
		return count - 1;
	}

	public void cleanupBadMove_firstWin(int initTurn, int expectedScore) {

		SearchNode child = getChild();
		if (child == null) {
			return;
		}

		SearchNode brother = child;// .getBrother();
		while (brother != null) {
			if ((initTurn == Constant.BLACK && brother.getScore() >= expectedScore)
					|| (initTurn == Constant.WHITE && brother.getScore() <= expectedScore)) {
				brother.farther.child = brother;
				brother.brother = null;
				break;
			} else {
				brother = brother.brother;
			}
		}
		if (brother == null) {
			System.out.println("fail to reach score " + expectedScore
					+ " from root at " + getStep());
			brother = child;
			System.out.println(this);
			while (brother != null) {
				System.out.println(brother);
				brother = brother.brother;
			}
			return;
		}
		if (brother.getChild() != null) {
			brother = brother.getChild();
			while (brother != null) {
				brother.cleanupBadMove_firstWin(initTurn, expectedScore);
				brother = brother.brother;
			}
		}
		// }

		// }else if(whoseTurn== Constant.WHITE){//min
		//
		// }

	}

	/**
	 * 
	 * @param initTurn
	 * @param expectedScore
	 *            score for initTurn
	 */
	public void cleanupBadMove_firstLose(int initTurn, int expectedScore) {

		SearchNode child = getChild();
		if (child == null) {
			return;
		}

		SearchNode brother = child;// .getBrother();
		while (brother != null) {
			if ((initTurn == Constant.BLACK && brother.getScore() < expectedScore)
					|| (initTurn == Constant.WHITE && brother.getScore() > expectedScore)) {
				brother.farther.child = brother;
				brother.brother = null;
				break;
			} else {
				brother = brother.brother;
			}
		}
		if (brother == null) {
			System.out.println("fail to reach score " + expectedScore
					+ " from root at " + getStep());
			brother = child;
			System.out.println(this);
			while (brother != null) {
				System.out.println(brother);
				brother = brother.brother;
			}
			return;
		}
		if (brother.getChild() != null) {
			brother = brother.getChild();
			while (brother != null) {
				brother.cleanupBadMove_firstLose(initTurn, expectedScore);
				brother = brother.brother;
			}
		}
		// }

		// }else if(whoseTurn== Constant.WHITE){//min
		//
		// }

	}

	public void blackWhiteSwitch() {
		if (step != null) {
			this.getStep().switchColor();
		}
		if (child != null) {
			child.blackWhiteSwitch();
		}

		SearchNode temp = brother;
		while (temp != null) {
			temp.blackWhiteSwitch();
			temp = temp.brother;
		}
	}

	/**
	 * mirror subtree on the fly when checking whether man's move is covered by
	 * manual
	 * 
	 * @param sym
	 *            before the move is taken!
	 * @param move
	 */
	public boolean containsChildMove_mirrorSubTree(SymmetryResult sym,
			Point move) {
		SymmetryResult normalizeOperation = GoBoardSymmetry
				.getNormalizeOperation(move, sym);
		Point moveNorm = move.normalize();
		SymmetryResult normalizeManual = null;

		boolean found = false;
		SearchNode temp = child;
		while (temp != null) {
			if(temp.getStep().isGiveUp()) continue;
			if (temp.getStep().getPoint().normalize().equals(moveNorm)) {
				System.out.println(temp.getStep().getPoint());
				normalizeManual = GoBoardSymmetry.getNormalizeOperation(temp
						.getStep().getPoint(), sym);
				System.out.println(normalizeManual);
				found = true;
				break;
			}
			temp = temp.brother;
		}
		if (found == false)
			return false;

		// mirror the child inclusive.
		System.out.println(normalizeOperation);
		
		normalizeOperation.cascaseOperation(normalizeManual);
		
		System.out.println(normalizeOperation);
		SearchNode copy = temp.mirrorSubTree_internal(normalizeOperation);
		this.addChild(copy);
		return true;
		// log.warn(normalizeOperation);
		// manual.getCurrent().
	}

	/**
	 * in case of symmetric state, original manual only store one sub-tree, in
	 * UI we'd better to have its mirror at hand. <br/>
	 * current node is not mirrored.<br/>
	 * actually we mirror only current node's children!
	 * @deprecated may copy wrong child. only one is matched.
	 */
	public void mirrorSubTree(SymmetryResult sym) {
		if (child == null) {
			return;
		}
		List<SearchNode> mirrors = new ArrayList<SearchNode>();
		SearchNode temp = child;
		while (temp != null) {
			SearchNode tempChild = temp.mirrorSubTree_internal(sym);
			mirrors.add(tempChild);

			temp = temp.brother;
		}
		// add child later to avoid conflict.
		for (SearchNode tempChild : mirrors) {
			this.addChild(tempChild);
		}
	}

	/**
	 * will not copy current node!
	 * 
	 * @return
	 */
	public SearchNode copySubTree() {
		// fake one
		SymmetryResult sym = new SymmetryResult();
		SearchNode node = mirrorSubTree_internal(sym);
		node.step = null;
		return node;
	}

	/**
	 * this node itself is copied and form sub tree.
	 * 
	 * @param sym
	 * @return
	 */
	private SearchNode mirrorSubTree_internal(SymmetryResult sym) {
		SearchNode copy = this.getCopy(sym);
		if (child == null) {
			return copy;
		}

		List<SearchNode> mirrors = new ArrayList<SearchNode>();
		SearchNode temp2 = child.mirrorSubTree_internal(sym);
		mirrors.add(temp2);

		SearchNode brother = child.brother;
		while (brother != null) {
			temp2 = brother.mirrorSubTree_internal(sym);
			mirrors.add(temp2);
			brother = brother.brother;
		}
		// add child later to avoid conflict.
		for (SearchNode tempChild : mirrors) {
			copy.addChild(tempChild);
		}
		return copy;
	}

}
