package eddie.wu.domain.territoty;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Shape;
import eddie.wu.linkedblock.ColorUtil;

/**
 * ������Ϊ���漰�����������ᣬ�൱������Ĳоֽ׶Ρ�<br/>
 * ���ҷ���ΪTerritory��<br/>
 * ����ļ����Ƿǳ������ġ��ڶ�����ܾ�����ѡ��ʱ����Ҫ���ֶ������жϡ�<br/>
 * �����ֶ������Ѷ�����Ҫ���仯�㵽��ͷ��
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class Territory {
	/**
	 * adjustment cause by the move it self, is done outside.<br/>
	 * ���ֱ�����ɵ�Ŀ�����죬��Ҫ�ڵ��õĵط�����˫�����ֵ�һĿ���ӣ�����ļ�����Ϊ3Ŀ��
	 * <br/>�����������Ŀ��
	 * @param myState
	 *            eye belong to its owner. so stateA is adjusted to set the
	 *            color for eye point before calling.<br/>
	 *             �հ׵�����ǵ��٣�Ҳ��������λ������Դ������㷨��Ҫ�����굥�١�
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
