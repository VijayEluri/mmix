package eddie.wu.domain;

import org.apache.log4j.Logger;

/**
 * wrap the color state array, not sure of its usage yet.
 * 
 * @author eddie
 * 
 */
public class MatrixColorState {

	private static final Logger log = Logger.getLogger(MatrixColorState.class);
	byte[][] colorState;//
	private int boardSize;

	private MatrixColorState(int boardSize) {
		this.boardSize = boardSize;
		colorState = new byte[boardSize][boardSize];
	}

	public static MatrixColorState getInitBoard(int boardSize) {
		return new MatrixColorState(boardSize);
	}

	public MatrixColorState(byte[][] breathArray) {
		boardSize = breathArray.length - 2;
		colorState = breathArray;
	}

	public boolean equals(Object object) {
		if (object instanceof MatrixColorState == false) {
			return false;
		}
		MatrixColorState state = (MatrixColorState) object;
		if (state.boardSize != this.boardSize) {
			return false;
		}

		for (int ii = 1; ii <= boardSize; ii++) {
			for (int j = 1; j <= boardSize; j++) {
				if (state.colorState[ii][j] != colorState[ii][j]) {
					return false;
				}
			}
		}
		return true;

	}

	public int getColor(Point point) {
		return this.colorState[point.getRow()][point.getColumn()];
	}

	public static void showDiff(MatrixColorState breathState,
			MatrixColorState breathState2) {
		if (breathState.boardSize != breathState2.boardSize) {
			if (log.isDebugEnabled())
				log.debug("board Size diff:" + breathState.boardSize + " v.s. "
						+ breathState2.boardSize);
		}
		for (int ii = 1; ii <= breathState.boardSize; ii++) {
			for (int j = 1; j <= breathState.boardSize; j++) {
				byte b = breathState.colorState[ii][j];
				byte c = breathState2.colorState[ii][j];
				if (b != c) {
					if (log.isDebugEnabled())
						log.debug("breath: " + b + " v.s. " + c + " at [" + ii
								+ "," + j + "]");
				}
			}
		}

	}
}
