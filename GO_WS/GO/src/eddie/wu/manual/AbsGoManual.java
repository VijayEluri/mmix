package eddie.wu.manual;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.SymmetryResult;

public abstract class AbsGoManual {
	protected String blackName = "";
	protected String whiteName = "";
	protected String result = "";
	protected int id;
	protected int boardSize = Constant.BOARD_SIZE;
	protected Set<Point> initBlacks = new HashSet<Point>();
	protected Set<Point> initWhites = new HashSet<Point>();
	protected int initTurn = Constant.BLACK;

	public AbsGoManual(int size, int initTurn) {
 		this.boardSize = size;
 		this.setInitTurn(initTurn);
	}

	public AbsGoManual(byte[][] state, int initTurn) {
		this.boardSize = state.length - 2;
		setInitState(state);
		this.setInitTurn(initTurn);
	}

	public void setInitState(byte[][] state) {
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				if (state[row][column] == Constant.BLACK) {
					this.addInitBlack(Point.getPoint(boardSize, row, column));
				} else if (state[row][column] == Constant.WHITE) {
					this.addInitWhite(Point.getPoint(boardSize, row, column));
				}
			}
		}
	}
	
	public void setInitState(BoardColorState stateIn) {
		byte[][] state = stateIn.getMatrixState();
		setInitState(state);
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

	public Set<Point> getInitBlacks() {
		return initBlacks;
	}

	public void addInitBlack(Point initBlack) {
		this.initBlacks.add(initBlack);
	}

	public Set<Point> getInitWhites() {
		return initWhites;
	}

	public void addInitWhite(Point initWhite) {
		this.initWhites.add(initWhite);
	}

	public BoardColorState getInitState() {
		int length = boardSize + 2;
		byte[][] matrix = new byte[length][length];
		for (Point point : initBlacks) {
			matrix[point.getRow()][point.getColumn()] = Constant.BLACK;
		}
		for (Point point : initWhites) {
			matrix[point.getRow()][point.getColumn()] = Constant.WHITE;
		}
		return BoardColorState.getInstance(matrix, initTurn);
	}

	public String getResult() {
		return result;
	}
	public int getResultAsScore() {
		return Integer.valueOf(result).intValue();
	}

	public void setResult(String reulst) {
		this.result = reulst;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public int getInitTurn() {
		return initTurn;
	}

	public void setInitTurn(int initTurn) {
		this.initTurn = initTurn;
	}

}
