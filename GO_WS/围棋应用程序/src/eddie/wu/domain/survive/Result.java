package eddie.wu.domain.survive;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.Step;
import eddie.wu.manual.SearchNode;

/**
 * 每一步都是针对目前的实际局面的.<br/>
 * Together with current state, we can calculate all the isomorphism.
 * 
 * and each could be map to relative result, with known delta
 * 
 * @author Eddie
 * 
 */
public class Result {
	/**
	 * 走firstStep 达到结果score
	 */
	int score;
	Step firstStep;
	List<Step> steps = new ArrayList<Step>();
	SearchNode tree;

	// 不存储棋子颜色，而是由外面先后手来决定。
	// int color;

	public Result() {
	}

	public Result(int survive, List<Step> steps) {
		this.score = survive;
		this.firstStep = steps.get(0);
		this.steps = steps;
	}

	public int getSurvive() {
		return score;
	}
	
	public boolean isLive(){
		return score == RelativeResult.ALREADY_LIVE;
	}

	public void setSurvive(int survive) {
		this.score = survive;
	}

	public Step getPoint() {
		return firstStep;
	}

	public void setPoint(Step point) {
		this.firstStep = point;
		steps.add(point);
	}

	public SearchNode getTree() {
		return tree;
	}

	public void setTree(SearchNode tree) {
		this.tree = tree;
	}
}
