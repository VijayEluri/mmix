package eddie.wu.manual;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Step;

/**
 * result of search tree; left is child, right is brother.<br/>
 * Design Change, there is an dummy root node (without step) * @author Eddie
 * 
 */
public class SearchNode {
	private SearchNode child;
	private SearchNode brother;
	private SearchNode father;

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
		return father;
	}

	public void setFather(SearchNode father) {
		this.father = father;
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
			child.father = this;
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
		brother.father = this.father;
	}

	public void addChildren(List<SearchNode> list) {
		assert this.child == null;
		assert list.isEmpty() == false;
		for (int i = 0; i < list.size() - 1; i++) {
			list.get(i).brother = list.get(i + 1);
			list.get(i + 1).father = this.father;
		}
		this.child = list.get(0);
		list.get(0).father = this.father;
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
		return this.step.toString();
	}

	/**
	 * (;B[qe]VW[aa:sj]FG[257:Dia. 1]MN[1];W[re];B[qf];W[rf];B[qg];W[pb];B[ob]
	 * ;W[qb]LB[rg:a]N[Diagram 1]) <br/>
	 * use "(" when branching
	 * 
	 * @return
	 */
	int depth = 0;

	public void getSGFBodyString(StringBuilder sb,boolean sgf) {
		if (this.brother == null) {
			// no variant
			if(sgf){
				sb.append(this.getStep().toSGFString());
			}else{
				sb.append(this.getStep().toNonSGFString());	
			}
			if (this.child != null) {
				this.child.setDepth(depth);
				child.getSGFBodyString(sb,sgf);
			}
		} else {
			// branching
			sb.append(Constant.lineSeparator);
			for (int i = 1; i <= depth; i++) {
				sb.append("\t");
			}
			sb.append("(");
			depth++;
			if(sgf){
				sb.append(this.getStep().toSGFString());
			}else{
				sb.append(this.getStep().toNonSGFString());
				sb.append(" (variant=" + this.getVariant()+") ");
			}

			if (this.child != null) {
				this.child.setDepth(depth);
				this.child.getSGFBodyString(sb,sgf);
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
				if(sgf){
				sb.append(brother.getStep().toSGFString());
				}else{
					sb.append(brother.getStep().toNonSGFString());
					sb.append(" (variant=" + brother.getVariant()+") ");
				}
				// sb.append("variant=" + brother.getVariant());
				if (brother.child != null) {
					brother.child.setDepth(depth);
					brother.child.getSGFBodyString(sb,sgf);
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
	
	
	public String getSingleManualStringToRoot(boolean sgf) {
		assert this.child == null;
		int boardSize = 0;
		if (step.getPoint() != null)
			boardSize = step.getPoint().boardSize;
		List<Step> list = new ArrayList<Step>();
		SearchNode temp = this;
		while (temp != null && temp.step != null) {
			list.add(temp.step);
			if (boardSize == 0 && temp.step.getPoint() != null) {
				boardSize = temp.step.getPoint().boardSize;
			}
			temp = temp.father;
		}
		java.util.Collections.reverse(list);

//		SimpleGoManual simple = new SimpleGoManual(boardSize);
//		simple.steps = list;
		if(sgf){
		return SimpleGoManual.getBodySGFString(list);
		}else{
			return SimpleGoManual.getBodyNonSGFString(list);	
		}
	}

	public String getSingleManualString() {
		assert this.child == null;
		return step.toSGFString();
	}

	/**
	 * the variant after this move.
	 * 
	 * @return
	 */
	int getExpandedString(StringBuilder sb,boolean sgf) {
		if (this.child == null) {
			this.variant = 1;
			sb.append(this.getSingleManualStringToRoot(sgf));
			sb.append(Constant.lineSeparator);
			return 1;

		}

		if (this.child.brother == null) {
			this.variant = this.child.getExpandedString(sb,sgf);
			return variant;
		}

		SearchNode temp = child;
		int count = 0;
		while (temp != null) {
			count += temp.getExpandedString(sb,sgf);
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
}
