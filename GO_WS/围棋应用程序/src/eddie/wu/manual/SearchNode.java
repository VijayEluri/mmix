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
 * Design Change, there is an dummy root node (without step/move)<br/>
 * reason is map to the initial state. each note stands for the state it reach
 * after the move is taken.
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

	/**
	 * auxiliary field. in case of multiple best move, in order to show all of
	 * them we check which move is traversed less and choose it in current
	 * computer's turn. in case of tie, the first one is chosen.
	 */

	private int visitedTimes = 0;

	public SearchNode(Step step) {
		this.step = step;
	}

	public static SearchNode createSpecialRoot() {
		// root has no step (to it)
		return new SearchNode(null);
	}

	/**
	 * make a safe copy
	 * 
	 * @return
	 */
	public SearchNode getCopy() {
		Step stepCopy = (step == null ? null : step.getCopy());
		SearchNode nodeCopy = new SearchNode(stepCopy);
		nodeCopy.score = this.score;
		nodeCopy.max = this.max;
		return nodeCopy;
	}

	public SearchNode getCopy(SymmetryResult sym) {
		Step step2 = null;
		SearchNode copy = null;
		if (step == null) {
			copy = SearchNode.createSpecialRoot();
		} else {
			step2 = step.getCopy();
			step2.convert(sym);
			copy = new SearchNode(step2);
		}
		copy.score = this.score;
		copy.max = this.max;
		return copy;
	}

	public SearchNode getChild() {
		return child;
	}
	
	public void increaseVisit(){
		
		visitedTimes+=1;
		System.out.println(step.toNonSGFString()+this.visitedTimes);
	}

	public SearchNode getLessVisitChild() {
		SearchNode temp = child;
		int visit = Integer.MAX_VALUE;
		SearchNode current = temp;
		while (temp != null) {
			System.out.println("visitedTimes"+temp.visitedTimes);
			if (temp.visitedTimes < visit) {
				visit = temp.visitedTimes ;
				current = temp;
			}
			temp = temp.brother;
		}
		return current;
	}

	public List<Point> getChildren() {
		if (child == null)
			return null;
		List<Point> list = new ArrayList<Point>();
		SearchNode temp = child;
		while (temp != null) {
			list.add(temp.getStep().getPoint());
			temp = temp.brother;
		}
		return list;
	}

	public SearchNode getBrother() {
		return brother;
	}

	public SearchNode getFather() {
		return farther;
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
		assert child != null;
		child.brother = null;// safer
		if (this.child == null) {
			this.child = child;
			child.farther = this;
			return;
		}
		this.child.addBrother(child);
	}

	private void addBrother(SearchNode brother) {
		brother.brother = null;// safer
		SearchNode temp = this;
		while (temp.brother != null) {
			temp = temp.brother;
		}
		temp.brother = brother;
		brother.farther = this.farther;

	}

	/**
	 * @deprecated
	 * @param list
	 */
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

	public boolean isMin() {
		return !max;
	}

	public void setMax(boolean max) {
		this.max = max;
	}

	public Step getStep() {
		return step;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.step == null) {
			sb.append("INIT");
		} else {
			sb.append(step.toNonSGFString());
		}
		sb.append(" (variant=" + getVariant() + ", score = " + score);
		if (isMax()) {
			sb.append(", Max) ");
		} else {
			sb.append(", Min) ");
		}
		return sb.toString();
	}

	/**
	 * (;B[qe]VW[aa:sj]FG[257:Dia. 1]MN[1];W[re];B[qf];W[rf];B[qg];W[pb];B[ob]
	 * ;W[qb]LB[rg:a]N[Diagram 1]) <br/>
	 * use "(" when branching
	 * 
	 * @return
	 */
	int depth = 0;

	public void getSGFBodyString(StringBuilder sb, boolean sgf,
			boolean inMemoryFormat) {
		if (this.brother == null) {
			// no variant
			if (sgf) {
				sb.append(this.getStep().toSGFString());
			} else {
				if (inMemoryFormat) {
					sb.append(this.toString());
				} else {
					sb.append(this.getStep().toNonSGFString());
				}
			}
			if (this.child != null) {
				this.child.setDepth(depth);
				child.getSGFBodyString(sb, sgf, inMemoryFormat);
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
				if (inMemoryFormat) {
					sb.append(this.toString());
				} else {
					sb.append(this.getStep().toNonSGFString());
				}
			}

			if (this.child != null) {
				this.child.setDepth(depth);
				this.child.getSGFBodyString(sb, sgf, inMemoryFormat);
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
					if (inMemoryFormat) {
						sb.append(brother.toString());
					} else {
						sb.append(brother.getStep().toNonSGFString());
					}
					// sb.append(brother.getStep().toNonSGFString());
					// sb.append(" (variant=" + brother.getVariant() +
					// ", score="
					// + brother.getScore() + ", max=" + brother.isMax()
					// + ") ");
				}
				// sb.append("variant=" + brother.getVariant());
				if (brother.child != null) {
					brother.child.setDepth(depth);
					brother.child.getSGFBodyString(sb, sgf, inMemoryFormat);
				}
				sb.append(")");
				depth--;
				// brother.child.setDepth(depth);
				brother = brother.brother;
			}

		}
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
		SearchNode temp = child;
		while (temp != null) {
			if (temp.getStep().equals(move))
				return temp;
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
			int max = child.initScore();// proper initial value
			while (temp != null) {
				if (temp.initScore() > max) {
					max = temp.getScore();
				}
				temp = temp.brother;
			}
			this.score = max;
			return max;
		} else {
			int min = child.initScore();// proper initial value
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

	/**
	 * initialize the score assuming only terminator has score assigned.
	 */
	public int initVariant() {
		if (this.child == null) {
			this.variant = 1;
			return this.variant;
		}
		SearchNode temp = this.child;
		this.variant = 0;
		while (temp != null) {
			this.variant += temp.initVariant();
			temp = temp.brother;
		}
		return this.variant;
	}

	/**
	 * current move is # of steps in the search.
	 * 
	 * @return
	 */
	public int countSteps() {
		int count = 0;
		SearchNode temp = this;
		while (temp != null) {
			count++;
			temp = temp.farther;
		}
		return count - 1;
	}

	/**
	 * suppose we have the complete search tree, for example the two*two board.
	 * we want to get the result which ensure Max will win no matter how Min
	 * played.<br/>
	 * since want to get MinWin at the same time, we will get a copy of tree and
	 * keep the original tree unchanged.
	 * 
	 * @return
	 */
	public SearchNode getMaxWinResult() {
		return getWinResult(true);
	}

	public SearchNode getMinWinResult() {
		return getWinResult(false);
	}

	public SearchNode getWinResult(boolean maxWin) {
		SearchNode copy = this.getCopy();
		if (child == null) {
			return copy;
		}
		SearchNode temp = child;
		SearchNode current = null;

		boolean minWin = !maxWin;
		while (temp != null) {
			if ((maxWin && this.isMax() && temp.getScore() < this.getScore())
					|| (minWin && this.isMin() && temp.getScore() > this
							.getScore())) {
				// ignore not effective move.
				temp = temp.brother;
			} else {
				// rebuild with valid child.
				current = temp;
				temp = temp.brother;
				copy.addChild(current.getWinResult(maxWin));
			}
		}
		return copy;
	}

	/**
	 * new idea with after thought - simplify the algorithm. <br/>
	 * should be called from root first. <br/>
	 * did not do it in initScore because we may need raw search data for
	 * checking.<br/>
	 * winner only need one effective move to win.<br/>
	 * if the search tree is complete, we ensure the best result in each context<br/>
	 * not only just to win<br/>
	 */
	public void cleanupBadMoveForWinner(boolean maxWin) {
		if (child == null)
			return;
		SearchNode temp = child;
		SearchNode current = null;
		child = null; // Detach children
		boolean minWin = !maxWin;
		while (temp != null) {
			if ((maxWin && this.isMax() && temp.getScore() < this.getScore())
					|| (minWin && this.isMin() && temp.getScore() > this
							.getScore())) {
				// ignore not effective move.
				temp = temp.brother;
			} else {
				// rebuild with valid child.
				current = temp;
				temp = temp.brother;
				this.addChild(current);
				current.cleanupBadMoveForWinner(maxWin);
			}
		}
	}

	/**
	 * if search with [2,0] get result 1. we can get best results for both side.
	 * not so good move is filtered.
	 */
	public void cleanupBadMoveForBoth() {
		if (child == null)
			return;
		SearchNode temp = child;
		SearchNode current = null;
		child = null; // Detach children
		while (temp != null) {
			if ((this.isMax() && temp.getScore() < this.getScore())
					|| (this.isMin() && temp.getScore() > this.getScore())) {
				// ignore not effective move.
				temp = temp.brother;
			} else {
				// rebuild with valid child.
				current = temp;
				temp = temp.brother;
				this.addChild(current);
				current.cleanupBadMoveForBoth();
			}
		}
	}

	/**
	 * depends on variant is up to date.
	 * 
	 * @param maxWin
	 */
	public void chooseBestMoveForWinner(boolean maxWin) {
		if (child == null) {
			this.variant = 1;
			return;
		}
		boolean minWin = !maxWin;
		SearchNode temp = child;
		SearchNode current = null;
		if (maxWin && this.isMax() || minWin && this.isMin()) {
			child = null; // Detach children
			int variant = this.variant + 1;
			while (temp != null) {
				// choose the one with less variants - decide sub_tree first
				temp.chooseBestMoveForWinner(maxWin);
				if (temp.variant < variant) {
					current = temp;
					variant = temp.variant;
				}
				temp = temp.brother;
			}
			this.addChild(current);
			this.variant = current.variant;
		} else {
			int variant = 0;
			while (temp != null) {
				temp.chooseBestMoveForWinner(maxWin);
				variant += temp.variant;
				temp = temp.brother;
			}
			this.variant = variant;
		}
	}

	// public void cleanupBadMove_firstWin(int initTurn, int expectedScore) {
	//
	// SearchNode child = getChild();
	// if (child == null) {
	// return;
	// }
	//
	// SearchNode brother = child;// .getBrother();
	// while (brother != null) {
	// if ((initTurn == Constant.BLACK && brother.getScore() >= expectedScore)
	// || (initTurn == Constant.WHITE && brother.getScore() <= expectedScore)) {
	// brother.farther.child = brother;
	// brother.brother = null;
	// break;
	// } else {
	// brother = brother.brother;
	// }
	// }
	// if (brother == null) {
	// System.out.println("fail to reach score " + expectedScore
	// + " from root at " + getStep());
	// brother = child;
	// System.out.println(this);
	// while (brother != null) {
	// System.out.println(brother);
	// brother = brother.brother;
	// }
	// return;
	// }
	// if (brother.getChild() != null) {
	// brother = brother.getChild();
	// while (brother != null) {
	// brother.cleanupBadMove_firstWin(initTurn, expectedScore);
	// brother = brother.brother;
	// }
	// }
	// // }
	//
	// // }else if(whoseTurn== Constant.WHITE){//min
	// //
	// // }
	//
	// }
	//
	// /**
	// *
	// * @param initTurn
	// * @param expectedScore
	// * score for initTurn
	// */
	// public void cleanupBadMove_firstLose(int initTurn, int expectedScore) {
	//
	// SearchNode child = getChild();
	// if (child == null) {
	// return;
	// }
	//
	// SearchNode brother = child;// .getBrother();
	// while (brother != null) {
	// if ((initTurn == Constant.BLACK && brother.getScore() < expectedScore)
	// || (initTurn == Constant.WHITE && brother.getScore() > expectedScore)) {
	// brother.farther.child = brother;
	// brother.brother = null;
	// break;
	// } else {
	// brother = brother.brother;
	// }
	// }
	// if (brother == null) {
	// System.out.println("fail to reach score " + expectedScore
	// + " from root at " + getStep());
	// brother = child;
	// System.out.println(this);
	// while (brother != null) {
	// System.out.println(brother);
	// brother = brother.brother;
	// }
	// return;
	// }
	// if (brother.getChild() != null) {
	// brother = brother.getChild();
	// while (brother != null) {
	// brother.cleanupBadMove_firstLose(initTurn, expectedScore);
	// brother = brother.brother;
	// }
	// }
	// // }
	//
	// // }else if(whoseTurn== Constant.WHITE){//min
	// //
	// // }
	//
	// }

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
	public boolean containsChildMove_mirrorSubTree(SymmetryResult sym, Step step) {
		Point move = step.getPoint();
		SymmetryResult normalizeOperation = GoBoardSymmetry
				.getNormalizeOperation(move, sym);
		Point moveNorm = move.normalize(sym);
		SymmetryResult normalizeManual = null;

		boolean found = false;
		SearchNode temp = child;
		while (temp != null) {
			if (temp.getStep().isPass()) {
				temp = temp.brother;
				continue;
			}
			if (temp.getStep().getPoint().normalize(sym).equals(moveNorm)) {
				System.out.println(temp.getStep().getPoint());
				normalizeManual = GoBoardSymmetry.getNormalizeOperation(temp
						.getStep().getPoint(), sym);
				System.out.println(normalizeManual);
				found = true;
				break;
			} else {
				temp = temp.brother;
			}

		}
		if (found == false)
			return false;

		// mirror the child inclusive.
		System.out.println(normalizeOperation);
		normalizeOperation.cascaseOperation(normalizeManual);
		System.out.println(normalizeOperation);

		SearchNode copy = temp.mirrorSubTree_internal(normalizeOperation);
		this.addChild(copy);
		if (this.containsChildMove(step) == false) {
			throw new RuntimeException("not contains child: " + move);
		}
		return true;
	}

	/**
	 * in case of symmetric state, original manual only store one sub-tree, in
	 * UI we'd better to have its mirror at hand. <br/>
	 * current node is not mirrored.<br/>
	 * actually we mirror only current node's children!
	 * 
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
