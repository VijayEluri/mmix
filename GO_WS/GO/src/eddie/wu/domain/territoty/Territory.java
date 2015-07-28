package eddie.wu.domain.territoty;

import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Shape;

/**
 * 官子因为是涉及地域的最后争夺，相当于象棋的残局阶段。<br/>
 * 暂且翻译为Territory。<br/>
 * 地域的计算是非常基本的。在多个可能局面中选择时，需要这种定量的判断。<br/>
 * 而这种定量的难度在于要将变化算到尽头。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class Territory {
	/**
	 * adjustment of difference caused by the move itself, is done outside.<br/>
	 * 着手本生造成的目数差异，需要在调用的地方处理。双方后手的一目官子，这里的计算结果为3目。 <br/>
	 * 单官则算成两目。
	 * 
	 * @param stateA
	 *            The state when I play first.<br/>
	 *            since eye belong to its owner. so stateA is adjusted to set
	 *            the color for eye point before calling.<br/>
	 *            空白点可能是单官，也可能是眼位。区别对待。本算法不要求收完单官。
	 * @param stateB
	 *            the state when enemyState play first.
	 * @param shape
	 *            only consider the point in the scope specified by shape.
	 * @param myColor
	 *            以哪一方为出发点.
	 * @return the difference between I play first and enemy play first.
	 */
	public int compare(byte[][] stateA, byte[][] stateB, Shape shape,
			int myColor) {
		int enemyColor = ColorUtil.enemyColor(myColor);
		int myCount = 0;
		int enemyCount = 0;
		for (int row = shape.getMinX(); row <= shape.getMaxX(); row++) {
			for (int column = shape.getMinY(); column <= shape.getMaxY(); column++) {
				if (stateA[row][column] == myColor) {
					if (stateB[row][column] == enemyColor) {
						myCount += 2;
					} else if (stateB[row][column] == Constant.BLANK) {
						myCount += 1;
					}
				} else if (stateA[row][column] == enemyColor) {
					if (stateB[row][column] == myColor) {
						enemyCount += 2;
					} else if (stateB[row][column] == Constant.BLANK) {
						enemyCount += 1;
					}
				} else if (stateA[row][column] == Constant.BLANK) {
					if (stateB[row][column] == enemyColor) {
						myCount += 1;
					} else if (stateB[row][column] == myColor) {
						enemyCount += 1;
					}
				}
			}
		}
		return myCount - enemyCount;

	}

	/**
	 * 计算最终局面的胜负,不考虑贴子/让目,
	 * 
	 * @param state
	 *            之前已经处理过,死子已经提走,眼位已经设成所属棋块的颜色<br/>
	 *            双活的点仍是空白点
	 * @return
	 */
	public static int count(byte[][] state) { 
		int boardSize = state.length-2;
		int blank = 0;
		int white = 0;
		int black = 0;
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				if (state[row][column] == Constant.BLACK)
					black++;
				else if (state[row][column] == Constant.WHITE)
					white++;
				else if (state[row][column] == Constant.BLANK)
					blank++;
			}
		}
		if (black + white + blank != 361)
			throw new RuntimeException("black+white+blank = "
					+ (black + white + blank));

		return black - white;
	}
}
