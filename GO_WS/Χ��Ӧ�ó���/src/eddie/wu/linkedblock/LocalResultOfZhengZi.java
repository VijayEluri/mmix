package eddie.wu.linkedblock;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;

/**
 * 针对当前局势,能够算出的结果以及候选点(意味着分数善不明朗) 或者是无路可走. 或者是胜招.
 * 
 * @author eddie
 * 
 */
public class LocalResultOfZhengZi {
	/**
	 * 在Controller中暂时采用统一计分法. 不论对哪一方而言.都是用正分表示甲方胜利.所以是 MIN and Max.
	 * 但是在这里采用相对计分法.正分表示本方胜利.Controller需要对结果进行转换.<br/>
	 * 其实只要Controller和Goboard中的计算对分数的见解互相协调就好.死活计算时,self fail
	 * 表示要做活的块死了, self success 表示要做活的块活了.
	 */
	private int score = 0;
	/**
	 * set of Point. one point go to new GoBoard(State)
	 */
	private List<Point> candidatePoints = new ArrayList<Point>(4);

	/**
	 * set of GoBoard (we arrive at the state AFTER move at point.
	 */
	private List<GoBoard> candidateJuMians = new ArrayList<GoBoard>(4);

	public List<GoBoard> getCandidateJuMians() {
		return candidateJuMians;
	}

	private void setCandidateJuMians(List<GoBoard> candidateJuMian) {
		this.candidateJuMians = candidateJuMian;
	}

	private void addCandidateJuMian(GoBoard goBaord) {
		this.candidateJuMians.add(goBaord);
	}

	public List<Point> getCandidatePoints() {
		return candidatePoints;
	}

	private void setCandidatePoints(List<Point> candidatePoint) {
		this.candidatePoints = candidatePoint;
	}

	private void addCandidatePoint(Point point) {
		this.candidatePoints.add(point);
	}

	public int getScore() {
		return score;
	}

	public void addCandidateJuMianAndPoint(GoBoard go, Point point) {
		this.addCandidateJuMian(go);
		this.addCandidatePoint(point);

	}

	public void setCandidateJuMiansAndPoints(List<GoBoard> go, List<Point> point) {
		this.setCandidateJuMians(go);
		this.setCandidatePoints(point);

	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isSelfSuccess() {
		return score > 0;
	}

	public boolean isSelfFail() {
		return score < 0;
	}

	// need to continue the research. kind of unknown status
	public boolean isTie() {
		return score == 0;
	}

	public int getNumberOfCandidates() {
		return candidatePoints.size();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("[score=");
		buffer.append(score);
		buffer.append(",numberOfCandidate=");
		buffer.append(this.getNumberOfCandidates());
		buffer.append(",Points=");
		buffer.append(this.getCandidatePoints());
		return buffer.toString();
	}

	/**
	 * 一锤定音的情况。搜索已经结束。
	 * @param temp
	 * @param tempPoint
	 */
	public void setCandidateJuMianAndPoint(GoBoard temp, Point tempPoint) {
		candidateJuMians.clear();
		candidateJuMians.add(temp);
		this.candidatePoints.clear();
		this.candidatePoints.add(tempPoint);
		
	}
}
