package eddie.wu.manual;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.Point;

public class GoManual {

	byte[] moves;

	public byte[] getMoves() {
		if (moves == null) {
			int count = 0;
			moves = new byte[steps.size() * 2];
			for (Point move : steps) {
				moves[count++] = move.getRow();
				moves[count++] = move.getColumn();
			}

		}
		return moves;

	}

	public void setMoves(byte[] moves) {
		this.moves = moves;
	}

	private List<Point> steps = new ArrayList<Point>();
	private String blackName;
	private String whiteName;
	private int id;

	public int getShouShu() {
		return steps.size();
	}

	public List<Point> getSteps() {
		return steps;
	}

	public void addStep(Point step) {
		this.steps.add(step);
	}

	public String getBlackName() {
		return blackName;
	}

	public void setBlackName(String blackName) {
		this.blackName = blackName;
	}

	public String getWhiteName() {
		return whiteName;
	}

	public void setWhiteName(String whiteName) {
		this.whiteName = whiteName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "GMDGoManual [" + " id=" + id + ", moves=" + steps.size()
				+ ", blackName=" + blackName + ", whiteName=" + whiteName + "]";
	}
}
