package eddie.wu.linkedblock;

import eddie.wu.domain.Constant;

/**
 * wrap the state of breath.
 * 
 * @author eddie
 * 
 */
public class BoardBreathState {
	byte[][] breathState = new byte[Constant.SIZEOFMATRIX][Constant.SIZEOFMATRIX];

	public BoardBreathState() {
	}

	public BoardBreathState(byte[][] breathArray) {
		breathState = breathArray;
	}

	public boolean equals(Object object) {
		if (object instanceof BoardBreathState) {
			BoardBreathState state = (BoardBreathState) object;
			for (int ii = 1; ii < Constant.ZBSX; ii++) {
				for (int j = 1; j < Constant.ZBSX; j++) {
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
}
