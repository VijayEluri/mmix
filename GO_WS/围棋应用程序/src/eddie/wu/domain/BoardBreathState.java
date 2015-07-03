package eddie.wu.domain;

import org.apache.log4j.Logger;

import eddie.wu.arrayblock.GoApplet;

/**
 * wrap the state of breath. but it could be derived from color state, so it is
 * not used yet.
 * 
 * @author eddie
 * 
 */
public class BoardBreathState {
	
	private static final Logger log = Logger.getLogger(BoardBreathState.class);
	byte[][] breathState;//
	private int boardSize;

//	public BoardBreathState() {
//		boardSize = Constant.BOARD_SIZE;
//		breathState = new byte[Constant.BOARD_MATRIX_SIZE][Constant.BOARD_MATRIX_SIZE];
//	}

	public BoardBreathState(byte[][] breathArray) {
		boardSize = breathArray.length - 2;
		breathState = breathArray;
	}

	public boolean equals(Object object) {
		if (object instanceof BoardBreathState) {
			BoardBreathState state = (BoardBreathState) object;
			for (int ii = 1; ii <= boardSize; ii++) {
				for (int j = 1; j <= boardSize; j++) {
					if (state.breathState[ii][j] != breathState[ii][j]) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static void showDiff(BoardBreathState breathState,
			BoardBreathState breathState2) {
		if (breathState.boardSize != breathState2.boardSize) {
			if(log.isDebugEnabled()) log.debug("board Size diff:" + breathState.boardSize
					+ " v.s. " + breathState2.boardSize);
		}
		for (int ii = 1; ii <= breathState.boardSize; ii++) {
			for (int j = 1; j <= breathState.boardSize; j++) {
				byte b = breathState.breathState[ii][j];
				byte c = breathState2.breathState[ii][j];
				if (b != c) {
					if(log.isDebugEnabled()) log.debug("breath: " + b + " v.s. " + c + " at ["
							+ ii + "," + j + "]");
				}
			}
		}

	}
}
