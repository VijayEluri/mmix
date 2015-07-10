package eddie.wu.domain.state;

import java.util.Arrays;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardBreathState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;

/**
 * <br/>
 * 防止无效状态触发子类的构造函数(比如ConnectivityAnalysis)。因此放在 StateUtil class 中
 * 
 * @author Eddie
 * 
 */
public class StateUtil {
	private static final Logger log = Logger.getLogger(StateUtil.class);

	/**
	 * state[][] is indexed from 1 .
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isValidState(byte[][] state) {
		GoBoard goBoard = new GoBoard(state);
		return goBoard.isValidState();

	}

	/**
	 * state[][] is indexed from 1 .
	 * 
	 * @param state
	 */
	public static void printState(byte[][] state) {
		char[][] stateString = getDisplayMatrix(state);
		for (int i = 0; i < stateString.length; i++) {
			if (log.isDebugEnabled())
				log.debug(Arrays.toString(stateString[i]));
		}
	}

	public static void printState(byte[][] state, Logger log) {
		char[][] stateString = getDisplayMatrix(state);
		for (int i = 0; i < stateString.length; i++) {
			if (log.isDebugEnabled())
				log.debug(Arrays.toString(stateString[i]));
		}
	}

	/**
	 * state[][] is indexed from 1 . char[][] is indexed from 0 .
	 * 
	 * @param state
	 * @return
	 */
	public static char[][] getDisplayMatrix(byte[][] state) {
		int boardSize = state.length - 2;
		char[][] ret = new char[boardSize][boardSize];

		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				if (state[row][column] == Constant.BLACK) {
					ret[row - 1][column - 1] = ColorUtil.BLACK_STRING;
				} else if (state[row][column] == Constant.WHITE) {
					ret[row - 1][column - 1] = ColorUtil.WHITE_STRING;
				} else {
					ret[row - 1][column - 1] = ColorUtil.BLANK_STRING;
				}
			}

		}
		return ret;
	}

}
