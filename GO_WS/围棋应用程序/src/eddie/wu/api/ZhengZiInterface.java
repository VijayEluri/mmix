/*
 * Created on 2005-7-3
 *


 */
package eddie.wu.api;

import eddie.wu.domain.Point;

/**
 * @author eddie
 * 
 */
public interface ZhengZiInterface {
	/**
	 * 
	 * @param point
	 *            the point of attacked block
	 * 
	 * @return the steps to kill/capture/conquer the block.
	 */
	public byte[][] jiSuanZhengZi(Point point);
}
