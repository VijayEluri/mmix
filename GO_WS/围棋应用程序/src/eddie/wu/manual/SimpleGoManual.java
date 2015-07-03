package eddie.wu.manual;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.BoardPoint;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.SymmetryResult;

/**
 * This is the simplest Go Manual, only for record the manual in tournament.
 * 
 * @author Eddie
 * 
 */
public class SimpleGoManual extends AbsGoManual {
	private byte[] moves;
	protected List<Step> steps = new ArrayList<Step>();

	public SimpleGoManual(int boardSize, int initTurn) {
		super(boardSize, initTurn);
	}

	public SimpleGoManual(byte[][] state, int initTurn) {
		super(state, initTurn);
	}

	public SimpleGoManual(BoardColorState stateB) {
		super(stateB.getMatrixState(), stateB.getWhoseTurn());
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

	@Override
	public String toString() {
		return "GoManual [" + " id=" + id + ", moves=" + steps.size()
				+ ", blackName=" + blackName + ", whiteName=" + whiteName + "]";
	}

	public Step getStep(int index) {
		return steps.get(index);
	}

	/**
	 * 根据棋谱得到最终状态.
	 * 
	 * @return
	 */
	public byte[][] getFinalState() {
		GoBoard go = new GoBoard(this.getInitState());
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
		GoBoard go = new GoBoard(this.getInitState());
		for (int i = 0; i < shoushu; i++) {
			go.oneStepForward(steps.get(i));
		}
		return go.getMatrixState();

	}

	public SymmetryStatistic getSymStatistics(int manualIndex) {
		List<StepSymmetry> sym = this.getStepSymmetry();
		SymmetryStatistic stat = new SymmetryStatistic(manualIndex, sym);
		return stat;
	}

	public List<StepSymmetry> getStepSymmetry() {
		List<StepSymmetry> sym = new ArrayList<StepSymmetry>();
		GoBoard goBoard = new GoBoard(this.getInitState());
		for (int i = 0; i < steps.size(); i++) {
			SymmetryResult symmetryResult = goBoard.getSymmetryResult();
			int symmetries = symmetryResult.getNumberOfSymmetry();
			if (symmetries != 0) {
				sym.add(new StepSymmetry(i, symmetryResult));
			}
			goBoard.oneStepForward(steps.get(i));

		}
		return sym;
	}

	public String getBodySGFString() {
		return getBodySGFString(steps);
	}

	public static String getBodySGFString(List<Step> steps) {
		StringBuilder sb = new StringBuilder();
		for (Step step : steps) {
			sb.append(step.toSGFString());
		}
		return sb.toString();
	}

	public static String getBodyNonSGFString(List<Step> steps) {
		StringBuilder sb = new StringBuilder();
		for (Step step : steps) {
			sb.append(step.toNonSGFString());
		}
		return sb.toString();
	}

	// public String toSGFString(){
	// StringBuilder out = new StringBuilder();
	// // byte[] temps = new byte[];
	// out.append( '(');
	// out.append( ';');
	// out.append("SZ");
	// out.append( '[');
	// out.append(getBoardSize());
	// out.append(']');
	//
	// // if (manual.getBlackName() == null || manual.getBlackName().isEmpty())
	// {
	// // } else {
	// //
	// // out.write("PB".getBytes());
	// // out.writeByte((byte) '[');
	// // out.write(manual.getBlackName().getBytes());
	// // out.writeByte((byte) ']');
	// // }
	// //
	// // if (manual.getWhiteName() == null || manual.getWhiteName().isEmpty())
	// {
	// // } else {
	// // out.write("PW".getBytes());
	// // out.writeByte((byte) '[');
	// // out.write(manual.getWhiteName().getBytes());
	// // out.writeByte((byte) ']');
	// // }
	// //
	// // // output init state;
	// // if (manual.getInitBlacks().isEmpty() == false) {
	// // out.write("AB".getBytes());//Add Black
	// // for (Point pointTemp : manual.getInitBlacks()) {
	// // out.writeByte((byte) '[');
	// // writePoint(out,pointTemp);
	// // out.writeByte((byte) ']');
	// // }
	// // }
	// //
	// // if (manual.getInitWhites().isEmpty() == false) {
	// // out.write("AW".getBytes());//Add White
	// // for (Point pointTemp : manual.getInitWhites()) {
	// // out.writeByte((byte) '[');
	// // writePoint(out,pointTemp);
	// // out.writeByte((byte) ']');
	// // }
	// // }
	// //
	// // // black and white play in turn.
	// // // boolean black = true;
	// // for (Step step : manual.getSteps()) {
	// // // TODO: how to express 弃权.
	// // if (step.isGiveUp())
	// // continue;
	// // out.writeByte((byte) ';');
	// //
	// // if (step.isBlack()) {
	// // out.write(BLACK.getBytes());
	// // // black = false;
	// // } else if (step.isWhite()) {
	// // out.write(WHITE.getBytes());
	// // // black = true;
	// // }
	// // out.writeByte((byte) '[');
	// // writePoint(out, step.getPoint());
	// // out.writeByte((byte) ']');
	// // }
	// //
	// // out.writeByte((byte) ')');
	// return null;
	//
	// //}
	// }

}
