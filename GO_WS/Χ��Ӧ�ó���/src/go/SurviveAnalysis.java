package go;

import eddie.wu.domain.Block;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.BlockElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 判断是否两眼活棋。两眼活棋是最基本的终止局面，所以这个判断非常基础和重要。<br/>
 * 1. 这个问题还是挺复杂的，要考虑到特殊的活棋形式，比如两头蛇活棋，形式上是两个假眼。<br/>
 * 2. 还有二子气块被点的情况，一般是不入气，相当于一眼，但是也有可能对方可以提子。<br/>
 * 看来这些情况要通过高于棋块的数据结构来解决。
 * 
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class SurviveAnalysis extends StateAnalysis {

	private static final Log log = LogFactory.getLog(SurviveAnalysis.class);

	public SurviveAnalysis(byte[][] state) {
		super(state);

	}

	/**
	 * 这里只考虑是否已经是真眼，不考虑是否可以补一手成为真眼。
	 * 
	 * @param eye
	 * @return
	 */
	public boolean isRealEye(Point eye, int enemyColor) {
		int a;
		int b;
		int count = 0;
		if (eye.getMinLine() > 1) {// 棋盘中间的眼
			for (int i = 0; i < 4; i++) {
				a = (byte) (eye.getRow() + Constant.szdjd[i][0]);
				b = (byte) (eye.getColumn() + Constant.szdjd[i][1]);
				if (boardPoints[a][b].getColor() == ColorUtil.BLANK
						|| boardPoints[a][b].getColor() == enemyColor) {
					count++;
				}
			}
			if (count > 1)
				return false;
			else
				return true;
		} else {// if (eye.getMinLine() == 1) {
			// if (eye.getMinLine() > 1) {//边上的眼。
			for (int i = 0; i < 4; i++) {
				a = (byte) (eye.getRow() + Constant.szdjd[i][0]);
				b = (byte) (eye.getColumn() + Constant.szdjd[i][1]);
				if (boardPoints[a][b].getColor() == ColorUtil.BLANK
						|| boardPoints[a][b].getColor() == enemyColor) {
					return false;
				}
			}
			return true;
			// } else {//角上的眼。==1
			//
			// }
		}
	}

	/**
	 * is the block live, currently only consider the shape of one block.
	 * 先考虑一块棋两眼的情况，比较简单。然后处理多块其的情况。 key method!
	 * 
	 * @param point
	 * @return
	 */
	public boolean isLive(Point point) {
		boolean live = isOneBlockTwoEyeLive(point);
		if (live == true) {
			if (log.isWarnEnabled()) {
				log.warn("简单的两真眼活棋！");
			}
			return true;
		}
		Block block = getBlock(point);
		int myColor = block.getColor();

		Set<Point> eyes = getEyes(block, myColor);
		if (eyes.size() < 2) {
			return false;
		}

		/*
		 * 眼位周围的块需要全部考虑进来。
		 */

		/*
		 * 但是，这些眼可能属于多个棋块。眼位属于某个棋块而该棋块只有一个眼位时，该眼位是假眼。
		 */
		Set<Block> blocks = new HashSet<Block>();
		Set<Block> newBlocks = new HashSet<Block>();
		Set<Block> allBlocks = new HashSet<Block>();
		blocks.add(block);
		allBlocks.add(block);
		Block temp;
		boolean extended = true;
		while (extended == true) {
			extended = false;
			for (Block block2 : blocks) {
				for (Point eye : getEyes(block2, myColor)) {
					int m, n;
					for (int i = 0; i < 4; i++) {
						m = eye.getRow() + Constant.szld[i][0];
						n = eye.getColumn() + Constant.szld[i][1];
						// almost always true except out of board.
						if (boardPoints[m][n].getColor() == myColor) {
							temp = boardPoints[m][n].getBlock();
							// block2.addEye(eye);
							if (allBlocks.contains(temp) == false
									&& newBlocks.contains(temp) == false) {
								if (log.isWarnEnabled()) {
									log.warn("extend to the block " + temp);
								}
								newBlocks.add(temp);
								extended = true;
							}
						}
					}
				}
			}
			allBlocks.addAll(newBlocks);
			blocks.clear();
			blocks.addAll(newBlocks);
			newBlocks.clear();
		}
		;

		Set<Point> fakeEyes = new HashSet<Point>();
		boolean removed = false;
		/*
		 * blocks 中的棋块至少有一眼。
		 */
		blocks = allBlocks;
		while (blocks.isEmpty() == false) {
			for (Iterator<Block> it = blocks.iterator(); it.hasNext();) {
				Block block2 = it.next();
				/*
				 * 只有一个不入气点的棋块，其眼位也是假眼。
				 */
				if (block2.numberOfEyes() <= 1) {
					fakeEyes.add(block2.getEyes().iterator().next());
					it.remove();
					removed = true;
				}
			}
			if (removed == false) {
				//
				break;
			}
			if (log.isWarnEnabled()) {
				log.warn("涉及的不入气点有: " + Arrays.toString(fakeEyes.toArray()));
			}
			for (Iterator<Block> it = blocks.iterator(); it.hasNext();) {
				Block block2 = it.next();
				/*
				 * 棋块中的假眼要删除掉。
				 */
				for (Iterator<Point> itp = block2.getEyes().iterator(); itp
						.hasNext();) {
					Point point2 = itp.next();
					if (fakeEyes.contains(point2))
						itp.remove();
				}
			}

			fakeEyes = new HashSet<Point>();
		}
		if (blocks.isEmpty() == false) {
			if (log.isWarnEnabled()) {
				for (Block temp2 : blocks) {
					log.warn("两眼为: "
							+ Arrays.toString(temp2.getEyes().toArray()));
				}
			}
		}
		if (blocks.isEmpty()) {
			return false;
		} else if (blocks.contains(block)) {
			if (log.isWarnEnabled()) {
				log.warn("两眼为: " + Arrays.toString(block.getEyes().toArray()));
			}
			return true;
		} else {
			/*
			 * TODO: 是否有接不归的问题。
			 */
			return true;
		}
	}

	/**
	 * 检查不入气点,可能是假眼.
	 * 
	 * @param block
	 * @param myColor
	 * @return
	 */
	private Set<Point> getEyes(Block block, int myColor) {
		Set<Point> eyes = new HashSet<Point>();
		/*
		 * 检查气点中是否有眼。二三子的眼看成一个不入气.
		 */
		for (Point breath : block.getAllBreathPoints()) {
			if (this.getBlock(breath).getTotalNumberOfPoint() > 1) {
				eyes.add(getBlock(breath).getTopLeftPoint());
				// block breath block info should already covered.
			} else if (isEye(block, breath, myColor)) {
				eyes.add(breath);
				block.addEye(breath);
			}
		}
		if (log.isWarnEnabled()) {
			log.warn("不入气点为: " + Arrays.toString(eyes.toArray()));
		}
		return eyes;
	}

	/**
	 * 一块棋两眼的活棋情况。是终局状态中最为常见的一种。
	 * 
	 * @return true if one block has two real eyes; false otherwise, false does
	 *         not necessarily mean it can not survive.
	 */
	public boolean isOneBlockTwoEyeLive(Point point) {

		Set<Point> eyes = getRealEyes(point);
		if (log.isWarnEnabled()) {
			if (eyes.isEmpty()) {
				log.warn(point + "没有真眼");
			} else {
				log.warn(point + " 拥有的真眼为: " + Arrays.toString(eyes.toArray()));
			}
		}
		if (eyes.size() < 2) {
			return false;
		} else {
			return true;
		}
	}

	public Set<Point> getRealEyes(Point point) {
		Block block = getBlock(point);
		int myColor = block.getColor();
		Set<Point> eyes = new HashSet<Point>();
		/*
		 * 检查气点中是否有真眼。
		 */
		for (Point point2 : block.getAllBreathPoints()) {
			if (isSameBlockEye(block, point2, myColor)) {
				eyes.add(point2);
			}
		}
		return eyes;
	}

	/**
	 * 判断一个气点是否为眼位。不可能为假眼，因为气点的周围属于同一个棋块。 不入气点暂不考虑。
	 * 
	 * @param block
	 * @param point
	 *            是气点
	 * @param myColor
	 * @return
	 */
	private boolean isSameBlockEye(Block block, Point point, int myColor) {
		int enemyColor = ColorUtil.enemyColor(myColor);
		int m, n;
		for (int i = 0; i < 4; i++) {
			m = point.getRow() + Constant.szld[i][0];
			n = point.getColumn() + Constant.szld[i][1];
			/*
			 * 这个判断把边界条件的检查绕过去了。
			 */
			if (state[m][n] == enemyColor) {// 公气
				return false;
			} else if (state[m][n] == myColor) {
				/*
				 * 没有连成一块。 not same block
				 */

				if (this.getBlock(Point.getPoint(m, n)) != block)
					return false;
			} else if (state[m][n] == ColorUtil.BLANK) {
				// 不是单子眼。眼位为二子或以上的气块。这里不予识别和考虑。
				return false;
			}
		}
		return true;// 是真眼
	}

	/**
	 * 判断一个气点是否为眼位。假眼也可以。不入气点也算。
	 * 
	 * @param point
	 *            气点
	 * @return
	 */
	public boolean isEye(Block block, Point point, int myColor) {
		int enemyColor = ColorUtil.enemyColor(myColor);
		int m, n;
		for (int i = 0; i < 4; i++) {
			m = point.getRow() + Constant.szld[i][0];
			n = point.getColumn() + Constant.szld[i][1];
			/*
			 * 这个判断把边界条件的检查绕过去了。
			 */
			if (state[m][n] == enemyColor) {
				// boolean buruqi = false;
				if (boardPoints[m][n].getBlock().getAllBreathPoints().size() == 1) {
					/*
					 * 进一步检查是否为对方的不入气点。效果相当于眼。 TODO：没有考虑对方可能提子！
					 */
					for (int j = 0; j < 4; j++) {
						m = point.getRow() + Constant.szld[j][0];
						n = point.getColumn() + Constant.szld[j][1];
						if (state[m][n] == Constant.BLANK) {
							// buruqi = false;
							return false;
						} else if (state[m][n] == myColor) {
							if (boardPoints[m][n].getBlock() != block) {
								return false;
							}
						}
					}
					// 需要检查邻块有没有全部二气或以上。
					// if(boardPoints[m][n].getBreaths()==)
					return true;
				} else {
					return false;
				}
			}
		}

		/*
		 * 确定了是眼位。 检查是否是打劫的情况。或者是打二还一的情况，总之对方可以提子回去。
		 */
		for (int i = 0; i < 4; i++) {
			m = point.getRow() + Constant.szld[i][0];
			n = point.getColumn() + Constant.szld[i][1];
			Block blockTemp = boardPoints[m][n].getBlock();
			if (blockTemp != null && blockTemp.getAllBreathPoints().size() == 1) {
				if (log.isWarnEnabled()) {
					log.warn("打N还一：N="
							+ boardPoints[m][n].getBlock()
									.getTotalNumberOfPoint());
				}
				return false;
			}
		}
		return true;// 是眼
	}
}
