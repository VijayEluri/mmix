package eddie.wu.linkedblock;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.Point;

/**
 * ��Ե�ǰ����,�ܹ�����Ľ���Լ���ѡ��(��ζ�ŷ����Ʋ�����)
 * ��������·����. ������ʤ��.
 * @author eddie
 *
 */
public class LocalResultOfZhengZi {
	/**
	 * ��Controller����ʱ����ͳһ�Ʒַ�. ���۶���һ������.���������ֱ�ʾ�׷�ʤ��.������
	 * MIN and Max.
	 * ���������������ԼƷַ�.���ֱ�ʾ����ʤ��.Controller��Ҫ�Խ������ת��.
	 */
	private int score = 0;
	/**
	 * set of Point
	 */
	private List<Point> candidatePoints = new ArrayList<Point>(4);
    
	/**
     * set of GoBoard 
	 */    
    private List<GoBoard> candidateJuMians=new ArrayList<GoBoard>(4);

    public List getCandidateJuMians() {
		return candidateJuMians;
	}

	private void setCandidateJuMians(List candidateJuMian) {
		this.candidatePoints = candidateJuMian;
	}
    private void addCandidateJuMian(GoBoard goBaord) {
        this.candidateJuMians.add(goBaord);
    }
    public List getCandidatePoints() {
        return candidatePoints;
    }

    private void setCandidatePoints(List candidatePoint) {
        this.candidatePoints = candidatePoint;
    }
    private void addCandidatePoint(Point point) {
        this.candidatePoints.add(point);
    }
	public int getScore() {
		return score;
	}

    public void addCandidateJuMianAndPoint(GoBoard go,Point point){
        this.addCandidateJuMian(go);
        this.addCandidatePoint(point);
        
    }
    public void setCandidateJuMiansAndPoints(List go,List point){
        this.setCandidateJuMians(go);
        this.setCandidatePoints(point);
        
    }
    
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isSelfSuccess(){
		return score>0;
	}
	public boolean isSelfFail(){
		return score<0;
	}
	public boolean isTie(){//need to continue to research.
		return score==0;
	}
    public int getNumberOfCandidates(){
        return candidatePoints.size();
    }
    public String toString(){
        StringBuffer buffer=new StringBuffer("[score=");
        buffer.append(score);
        buffer.append(",numberOfCandidate=");
        buffer.append(this.getNumberOfCandidates());
        buffer.append(",Points=");
        buffer.append(this.getCandidatePoints());
        return buffer.toString();
    }
}
