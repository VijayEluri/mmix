package eddie.wu.manual;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.SmallGoBoard;

public class TreeGoManual extends AbsGoManual {
	private SearchNode root;
	private SearchNode current;
	private ExpectScore expScore;

	public void setExpScore(ExpectScore expScore) {
		this.expScore = expScore;
	}

	// public TreeGoManual(SearchNode root){
	//
	// }
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
		return getSGFBodyString(true, true);
	}

	public String getSGFBodyString(boolean sgf) {
		return getSGFBodyString(sgf, true);
	}

	public void cleanupBadMoveForBoth() {
		getRoot().cleanupBadMoveForBoth();
		getRoot().initVariant();
	}
	public String getSGFBodyString(boolean sgf, boolean inMemoryFormat) {
		StringBuilder sb = new StringBuilder();
		if (sgf == false) {
			sb.append(root.toString() + Constant.lineSeparator);
		}
		SearchNode temp = root.getChild();
		if (temp != null) {
			temp.getSGFBodyString(sb, sgf, inMemoryFormat);
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

	/**
	 * initialize the score assuming only terminator has score assigned.
	 */
	public int initScore() {
		int score = this.root.initScore(expScore);
		if (result == null || result.isEmpty())
			result = String.valueOf(score);
		return score;
	}

	public String getMostExpPath() {
		this.initVariant();
		return getMostExpPath(false, new SimpleGoManual(this.getInitState()));
	}

	/**
	 * for killed target, we check the longest breath path.
	 * 
	 * @return
	 */
	public SimpleGoManual getLongestBreathPath() {
		SimpleGoManual manual = new SimpleGoManual(this.getInitState());
		SearchNode temp = root;
		while (temp != null) {

		}

		return manual;

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
			sb.append(" (variant=" + root.getVariant() + ")");
			sb.append(Constant.lineSeparator);

		}
		List<Point> candidate = new ArrayList<Point>();
		while (farther.getChild() != null) {
			temp = farther.getChild();
			max = temp;
			while (temp != null) {
				if (temp.variant > max.variant) {
					max = temp;
				}
				candidate.add(temp.getStep().getPoint());
				sb.append(temp.getStep().toNonSGFString());
				sb.append("(variant=" + temp.getVariant() + ") ");
				temp = temp.getBrother();
			}
			if (sgf) {
				sb.append(max.getStep().toSGFString());
			} else {
				sb.append(Constant.lineSeparator + "choose step: --"
						+ max.getStep().toNonSGFString() + "-->");
				sb.append(Constant.lineSeparator);
				goB.oneStepForward(max.getStep());
				sb.append(goB.getBoardColorState().getStateString());
				sb.append(" (variant=" + max.getVariant() + ")");
				sb.append(Constant.lineSeparator);
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
		this.root = SearchNode.createSpecialRoot();
		current = root;
	}

	public TreeGoManual(BoardColorState state) {
		super(state.getMatrixState(), state.getWhoseTurn());
		this.root = SearchNode.createSpecialRoot();
		current = root;
	}

	public TreeGoManual(List<List<Step>> lists, int boardSize, int initTurn) {
		super(boardSize, initTurn);
		assert lists.isEmpty() == false;
		assert lists.get(0).isEmpty() == false;
		root = SearchNode.createSpecialRoot();
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

	// public void cleanupBadMove_firstWin(int whoseTurn, int expectedScore) {
	// root.initScore();
	// this.setResult(String.valueOf(root.getScore()));
	// root.cleanupBadMove_firstWin(whoseTurn, expectedScore);
	// }
	//
	// public void cleanupBadMove_firstLose(int whoseTurn, int expectedScore) {
	// root.initScore();
	// SearchNode brother = root.getChild();
	// while (brother != null) {
	// brother.cleanupBadMove_firstLose(whoseTurn, expectedScore);
	// brother = brother.brother;
	// }
	// }
	public void backToRoot(){
		this.current = root;
	}
	
	public void up() {
		if (current != root)
			this.current = current.farther;
		// this.current = current.farther;

	}

	public void blackWhiteSwitch() {
		BoardColorState state = this.getInitState().blackWhiteSwitch();
		this.setInitState(state.getMatrixState());
		this.setInitTurn(state.getWhoseTurn());
		this.root.blackWhiteSwitch();
		int resultI = 0 - root.getScore();
		this.setResult(String.valueOf(resultI));

	}

	public void mergeWith(TreeGoManual goManual) {
		SearchNode temp = goManual.getRoot().getChild();
		while (temp != null) {
			getCurrent().addChild(temp);
			// log.warn("Add child "+temp.getStep().toNonSGFString());
			temp = temp.getBrother();
		}
	}

	public void cleanupBadMoveForWinner(boolean maxWin) {
		getRoot().cleanupBadMoveForWinner(maxWin);
		getRoot().initVariant();

	}

	public void chooseBestMoveForWinner(boolean maxWin) {
		getRoot().chooseBestMoveForWinner(maxWin);
		getRoot().initVariant();
	}

	public int initVariant() {
		return getRoot().initVariant();
	}

	/**
	 * @deprecated copy maybe expensive when tree is large
	 * @return
	 */
	public TreeGoManual copy() {
		return new TreeGoManual(this.getRoot().copySubTree(), boardSize,
				initTurn);
	}

	public TreeGoManual getMaxWinResult() {
		TreeGoManual maxWin = new TreeGoManual(boardSize, initTurn);
		maxWin.setRoot(this.getRoot().getMaxWinResult());
		maxWin.setInitState(this.getInitState());
		maxWin.initVariant();
		return maxWin;
	}

	public TreeGoManual getMinWinResult() {
		TreeGoManual minWin = new TreeGoManual(boardSize, initTurn);
		minWin.setRoot(this.getRoot().getMinWinResult());
		minWin.setInitState(this.getInitState());
		minWin.initVariant();
		return minWin;
	}
}
