package eddie.wu.linkedblock;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;

/**
 * ��Ե�ǰ����,�ܹ�����Ľ���Լ���ѡ��(��ζ�ŷ����Ʋ�����) ��������·����. ������ʤ��.
 * 
 * @author eddie
 * 
 */
public class LocalResultOfZhengZi {
	/**
	 * ��Controller����ʱ����ͳһ�Ʒַ�. ���۶���һ������.���������ֱ�ʾ�׷�ʤ��.������ MIN and Max.
	 * ���������������ԼƷַ�.���ֱ�ʾ����ʤ��.Controller��Ҫ�Խ������ת��.<br/>
	 * ��ʵֻҪController��Goboard�еļ���Է����ļ��⻥��Э���ͺ�.�������ʱ,self fail
	 * ��ʾҪ����Ŀ�����, self success ��ʾҪ����Ŀ����.
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
	 * һ������������������Ѿ�������
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
