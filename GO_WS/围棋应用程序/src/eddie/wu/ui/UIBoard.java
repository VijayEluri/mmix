package eddie.wu.ui;

import java.util.HashSet;
import java.util.Set;

import eddie.wu.domain.BasicGoBoard;
import eddie.wu.domain.Point;

public class UIBoard {
	public int boardSize;// = 19;
	private UIPoint[][] points;
	private Set<Point> deadStoneReps = new HashSet<Point>();

	public void addDeadStoneRep(Point rep) {
		deadStoneReps.add(rep);
	}

	public void addDeadStones(Set<Point> reps) {
		deadStoneReps.addAll(reps);
	}

	public Set<Point> getDeadStoneReps() {
		return deadStoneReps;
	}

	public UIBoard(int boardSize) {
		this.boardSize = boardSize;
		points = new UIPoint[boardSize][boardSize];
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				UIPoint uiPoint = new UIPoint(Point.getPoint(boardSize, row,
						column));
				points[row - 1][column - 1] = uiPoint;
			}
		}
	}

	public UIBoard(BasicGoBoard go) {
		this.boardSize = go.boardSize;
		points = new UIPoint[boardSize][boardSize];
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				UIPoint uiPoint = new UIPoint(Point.getPoint(boardSize, row,
						column));
				points[row - 1][column - 1] = uiPoint;
				uiPoint.setColor(go.getColor(row, column));
			}
		}
	}

	public UIPoint getUIPoint(int row, int column) {
		return points[row - 1][column - 1];
	}

	public UIPoint getUIPoint(Point point) {
		return points[point.getRow() - 1][point.getColumn() - 1];
	}
}
