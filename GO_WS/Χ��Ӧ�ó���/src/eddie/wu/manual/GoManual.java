package eddie.wu.manual;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;

public class GoManual {

	private String blackName;
	private String whiteName;
	private String result;
	private int id;
	private int boardSize;
	byte[] moves;
	private List<Step> steps = new ArrayList<Step>();
	private Set<Point> initBlacks = new HashSet<Point>();
	private Set<Point> initWhites = new HashSet<Point>();
	
	public GoManual(int boardSize){
		this.boardSize = boardSize;
	}

	public byte[] getMoves() {
		if (moves == null) {
			int count = 0;
			moves = new byte[steps.size() * 2];
			for (Step move : steps) {
				moves[count++] = move.getRow();
				moves[count++] = move.getColumn();
			}

		}
		return moves;

	}

	public void setMoves(byte[] moves) {
		this.moves = moves;
	}

	public int getShouShu() {
		return steps.size();
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void addStep(Point step, int color) {
		this.steps.add(new Step(step, color));
	}

	public void addStep(Point step, int color, int index) {
		this.steps.add(new Step(step, color, index));
	}

	public void addStep(Step step) {
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

	public Step getStep(int index) {
		return steps.get(index);
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

	public BoardColorState getInitSate() {
		BoardColorState state = new BoardColorState(boardSize);
		for (Point point : initBlacks) {
			state.add(point, ColorUtil.BLACK);
		}
		for (Point point : initWhites) {
			state.add(point, ColorUtil.WHITE);
		}
		return state;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String reulst) {
		this.result = reulst;
	}

	/**
	 * 根据棋谱得到最终状态.
	 * 
	 * @return
	 */
	public byte[][] getFinalState() {
		GoBoard go = new GoBoard(this.getInitSate());
		for (Step step : steps) {
			go.oneStepForward(step);
		}
		return go.getMatrixState();

	}

	/**
	 * 根据棋谱得到直到某一手(包括这一手)的状态.
	 * 
	 * @return
	 */
	public byte[][] getState(int shoushu) {
		if (shoushu > steps.size())
			throw new InvalidParameterException("shoushu>steps.size()");
		GoBoard go = new GoBoard(this.getInitSate());
		for (int i = 0; i < shoushu; i++) {
			go.oneStepForward(steps.get(i));
		}
		return go.getMatrixState();

	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}
	
	
}
