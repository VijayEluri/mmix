package eddie.wu.linkedblock;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.Point;

/**
 * 针对当前局势,能够算出的结果以及候选点(意味着分数善不明朗)
 * 或者是无路可走. 或者是胜招.
 * @author eddie
 *
 */
public class LocalResultOfZhengZi {
	/**
	 * 在Controller中暂时采用统一计分法. 不论对哪一方而言.都是用正分表示甲方胜利.所以是
	 * MIN and Max.
	 * 但是在这里采用相对计分法.正分表示本方胜利.Controller需要对结果进行转换.
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
