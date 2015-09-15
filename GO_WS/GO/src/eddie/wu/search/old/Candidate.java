package eddie.wu.search.old;

import java.util.Comparator;

import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;

/**
 * different with global.search.Candidate. this one is almost obsolete.
 * @deprecated
 * @author Eddie
 * 
 */
public class Candidate {
	private GoBoard goBoard;
	/**
	 * move at point arrive at state of goBoard.
	 */
	private Point point;
	/**
	 * higher, better; control the search sequence. but it is not the final
	 * result.
	 */
	private int priority;

	public GoBoard getGoBoard() {
		return goBoard;
	}

	public void setGoBoard(GoBoard goBoard) {
		this.goBoard = goBoard;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	static class CandidateComparator implements Comparator<Candidate> {

		@Override
		public int compare(Candidate o1, Candidate o2) {
			return o1.getPriority() - o2.getPriority();
		}

	}
}
