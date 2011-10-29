package eddie.wu.domain.territoty;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Shape;
import eddie.wu.linkedblock.ColorUtil;

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
	 * adjustment cause by the move it self, is done outside.<br/>
	 * 着手本生造成的目数差异，需要在调用的地方处理。双方后手的一目官子，这里的计算结果为3目。
	 * <br/>单官则算成两目。
	 * @param myState
	 *            eye belong to its owner. so stateA is adjusted to set the
	 *            color for eye point before calling.<br/>
	 *             空白点可能是单官，也可能是眼位。区别对待。本算法不要求收完单官。
	 * @param enemyState
	 * @param shape
	 * @param myColor
	 * @return
	 */
	public int compare(byte[][] myState, byte[][] enemyState, Shape shape,
			int myColor) {
		int enemyColor = ColorUtil.enemyColor(myColor);
		int myCount = 0;
		int enemyCount = 0;
		for (int row = shape.getMinX(); row <= shape.getMaxX(); row++) {
			for (int column = shape.getMinY(); column <= shape.getMaxY(); column++) {
				if (myState[row][column] == myColor) {
					if (enemyState[row][column] == enemyColor) {
						myCount+=2;
					}else if (enemyState[row][column] == Constant.BLANK) {
						myCount+=1;
					}
				} else if (myState[row][column] == enemyColor) {
					if (enemyState[row][column] == myColor) {
						enemyCount+=2;
					}else if (enemyState[row][column] == Constant.BLANK) {
						enemyCount+=1;
					}
				}else if (myState[row][column] == Constant.BLANK) {
					if (enemyState[row][column] == enemyColor) {
						myCount+=1;
					}else if (enemyState[row][column] == myColor) {
						enemyCount+=1;
					}
				}
			}
		}
		return myCount - enemyCount;
		
	}
}
