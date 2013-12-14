package eddie.wu.manual;

import java.util.List;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.SmallGoBoard;

public class TreeGoManual extends AbsGoManual {
	private SearchNode root;
	private SearchNode current;

	public SearchNode getRoot() {
		return root;
	}

	public void setRoot(SearchNode root) {
		assert root.getFather() == null;
		assert root.getStep() == null;
		this.root = root;
		current = root;
	}

	public boolean isEmpty() {
		return root.getChild() == null;
	}

	public String getSGFBodyString() {
		return getSGFBodyString(true);
	}

	public String getSGFBodyString(boolean sgf) {
		StringBuilder sb = new StringBuilder();
		sb.append("INIT variant=" + root.getVariant() + Constant.lineSeparator);
		SearchNode temp = root.getChild();
		if (temp != null) {
			temp.getSGFBodyString(sb, sgf);
		}
		return sb.toString();
	}

	public String getExpandedString() {
		return getExpandedString(true);
	}

	private boolean SGF = false;

	/**
	 * initialize variant as side effect.
	 * 
	 * @return
	 */
	public String getExpandedString(boolean SGF) {
		this.SGF = SGF;
		SearchNode temp = root.getChild();
		StringBuilder sb = new StringBuilder();
		int variant = 0;
		while (temp != null) {
			temp.getExpandedString(sb, SGF);
			variant += temp.variant;
			temp = temp.getBrother();
		}
		root.variant = variant;
		return sb.toString();
	}

	public String getMostExpPath() {
		return getMostExpPath(false, new SimpleGoManual(this.getInitState()));
	}

	public String getMostExpPath(boolean sgf, SimpleGoManual manual) {
		GoBoard goB = null;
		SearchNode farther = root;
		SearchNode temp = null;
		SearchNode max = null;
		StringBuilder sb = new StringBuilder();
		if (sgf == false) {
			goB = new SmallGoBoard(manual.getInitState());
			sb.append(goB.getBoardColorState().getStateString());

		}
		while (farther.getChild() != null) {
			temp = farther.getChild();
			max = temp;
			while (temp != null) {
				if (temp.variant > max.variant) {
					max = temp;
				}
				temp = temp.getBrother();
			}
			if (sgf) {
				sb.append(max.getStep().toSGFString());
			} else {
				sb.append("--");
				sb.append(max.getStep().toNonSGFString() + " (variant="
						+ max.getVariant() + ")" + Constant.lineSeparator);
				sb.append("-->");
				goB.oneStepForward(max.getStep());
				sb.append(goB.getBoardColorState().getStateString());
			}
			if (manual != null) {
				manual.addStep(max.getStep());
			}
			farther = max;
		}
		return sb.toString();
	}

	// public TreeGoManual(SearchNode root) {
	// super(root.getStep().getPoint().boardSize);
	// this.root = root;
	// }

	public TreeGoManual(SearchNode root, int boardSize, int initTurn) {
		super(boardSize, initTurn);
		this.root = root;
		current = root;
	}

	public TreeGoManual(int boardSize, int initTurn) {
		super(boardSize, initTurn);
		this.root = SearchNode.getSpecialRoot();
		current = root;
	}

	public TreeGoManual(BoardColorState state) {
		super(state.getMatrixState(), state.getWhoseTurn());
		this.root = SearchNode.getSpecialRoot();
		current = root;
	}

	public TreeGoManual(List<List<Step>> lists, int boardSize, int initTurn) {
		super(boardSize, initTurn);
		assert lists.isEmpty() == false;
		assert lists.get(0).isEmpty() == false;
		root = SearchNode.getSpecialRoot();
		current = root;
		SearchNode current = root;
		SearchNode child;
		boolean branch = false;
		for (List<Step> list : lists) {
			branch = false;
			current = root;
			for (Step move : list) {
				if (branch == false) {
					child = current.getChild(move);
					if (child != null) {
						current = child;
						continue;
					} else
						branch = true;
				}
				SearchNode node = new SearchNode(move);
				current.addChild(node);
				current = node;
			}
		}

		root = root.getChild();
	}

	public int getVariant() {
		return root.variant;
	}

	public SearchNode getCurrent() {
		return current;
	}

	public void navigateToChild(Step childMove) {
		SearchNode child = current.getChild(childMove);
		if (child != null) {
			current = child;
		} else {
			throw new RuntimeException(current.getStep() + "has no child of "
					+ childMove);
		}
	}

	public SimpleGoManual getMostExpManual() {
		SimpleGoManual manual = new SimpleGoManual(this.getInitState());
		System.out.println(manual.getInitState());
		this.getMostExpPath(true, manual);
		return manual;
	}

}
