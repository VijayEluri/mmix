package eddie.wu.domain.survive;

import java.util.List;

import eddie.wu.domain.Delta;

/**
 * 当谁先走已经确定时的死活结果。 扩展为通用的结果表达. ,<br/>
 * Relative means the result is localized, so the delta is relative to the
 * point(minx, miny)
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class RelativeResult {
	/**
	 * 走firstStep 达到结果score
	 */
	int score;
	List<Delta> steps;
	Delta firstStep;

	// 不存储棋子颜色，而是由外面先后手来决定。
	// int color;

	public RelativeResult() {
	}

	public RelativeResult(int survive, Delta point) {
		this.score = survive;
		this.firstStep = point;
		steps.add(point);
	}

	public int getSurvive() {
		return score;
	}

	public void setSurvive(int survive) {
		this.score = survive;
	}

	public Delta getPoint() {
		return firstStep;
	}

	public void setPoint(Delta point) {
		this.firstStep = point;
		steps.add(point);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RelativeResult){
			RelativeResult rr = (RelativeResult)obj;
			return this.score == rr.score;
		}else return false;
		
	}

	// public static
}