package eddie.wu.domain.survive;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.Delta;

/**
 * 当谁先走已经确定时的死活结果。 扩展为通用的结果表达.<br/>
 * Relative means the result is localized, so the delta is relative to the
 * point(minX, minY) some time we get relative result directly; sometime we
 * convert real result to relative result.<br/>
 * logically relative result is result + minX + minY.<br/>
 * consider 四角八边变化, we need to record  the parameter to do the conversion.
 * 
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class RelativeResult {

	// public static final int MAY_lIVE_MAY_DEAD = -1;
	/**
	 * loop live is same as loop dead, so they are only intermediate score!
	 */
	public static final int LOOP_LIVE = 32;
	public static final int LOOP_DEAD = -64;
	public static final int DUAL_LIVE = 64;//DUAL LIVE included
	
	public static final int ALREADY_DEAD = -128;
	public static final int ALREADY_LIVE = 128; 
	public static final int TWO_EYE_LIVE = 128;
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
		if (point != null) {
			steps = new ArrayList<Delta>();
			steps.add(point);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RelativeResult) {
			RelativeResult rr = (RelativeResult) obj;
			return this.score == rr.score;
		} else
			return false;

	}

	public Delta getPoint() {
		return firstStep;
	}

	public int getSurvive() {
		return score;
	}

	public void setPoint(Delta point) {
		this.firstStep = point;
		steps.add(point);
	}

	public void setSurvive(int survive) {
		this.score = survive;
	}

	// public static
}