package go;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Delta;
import eddie.wu.domain.Point;
import eddie.wu.linkedblock.ColorUtil;

/**
 * in current states, judge the name of the move. <br/>
 * 当前局面下，下了某一步，对应的着手名称是什么，比如打扑之类。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class ActionAnalysis extends StateAnalysis {
	private static final Log log = LogFactory.getLog(ActionAnalysis.class);

	public ActionAnalysis(byte[][] state) {
		super(state);
	}

	public String moveName(Point point, int myColor) {
		int row, column;
		int blank = 0; // number of blank neighbor
		int opponent = 0; // number of opponent neighbor
		int connect = 0; // number of friends
		int beat = 0; // number of beats
		int eaten = 0; // number of eaten in blocks.
		Block block;
		Set<Point> breaths = new HashSet<Point>();
		List<Point> opponents = new ArrayList<Point>();
		int opponentColor = ColorUtil.enemyColor(myColor);
		for (int i = 0; i < 4; i++) {
			row = point.getRow() + Constant.szld[i][0];
			column = point.getColumn() + Constant.szld[i][1];
			if (isValid(row, column) == false)
				continue;
			block = this.getBlock(row, column);
			if (block.getColor() == opponentColor) {
				if (block.getBreaths() == 1) {
					eaten++;
				} else if (block.getBreaths() == 2) {
					beat++;
				}
				opponents.add(Point.getPoint(row, column));
			} else if (block.getColor() == myColor) {
				breaths.addAll(block.getAllBreathPoints());
				connect++;
			} else if (block.getColor() == Constant.BLANK) {
				blank++;
			}
		}

		/**
		 * 首先判断提子的情况。
		 */
		if (eaten == 1) {
			return "提";
		} else if (eaten == 2) {
			return "双提";
		} else if (eaten == 3) {
			return "三提";
		} else if (eaten == 4) {
			return "四提";
		}

		int breath = blank + breaths.size();
		if (log.isInfoEnabled()) {
			log.info(point + " 落子后有 " + breath + " 气");
		}
		if (breath == 0) {
			return "自杀";
		} else if (breath == 1) {
			if (beat >= 1) {// 含有打吃才算扑入。
				if (connect == 0) {
					return "单子扑";
				} else {
					return "多子扑";
				}
			} else {
				return "送吃";
			}
		}
		// else if (breath == 2) {
		// return "自杀";
		// }

		/*
		 * 己方气数大于等于二。
		 */
		if (beat == 1) {
			return "单打";
		} else if (beat == 2) {
			return "双打";
		} else if (beat == 3) {
			return "三打";
		}

		/*
		 * 相关块气数都大于等于二。
		 */
		if (opponent == 1) {// 扳，虎，靠，顶，

		} else if (opponent == 2) {
			if (opponents.get(0).isDiagonal(opponents.get(1))) {
				int deltaR = opponents.get(0).getRow() - point.getRow();
				deltaR += (opponents.get(1).getRow() - point.getRow());
				int deltaC = opponents.get(0).getColumn() - point.getColumn();
				deltaC += (opponents.get(1).getColumn() - point.getColumn());
				if (state[point.getRow() + deltaR][point.getColumn() + deltaC] == myColor) {
					return "断";
				} else if (state[point.getRow() + deltaR][point.getColumn()
						+ deltaC] == Constant.BLANK) {
					return "挤/卡";
				} else {
					return "愚形挤/卡";
				}

			} else if (opponents.get(0).isSameline(opponents.get(1))) {
				if (connect == 0) {
					return "挖";
				} else {
					return "冲";
				}
			}
		} else if (opponent == 3) {
			return "挤";
		}

		/*
		 * 没有对方子。
		 */

		if (connect == 1) {
			return "长";
		} else if (connect == 2) {
			return "粘";
		} else if (connect == 3) {
			return "三粘";
		} else if (connect == 4) {
			return "四粘";
		}

		/*
		 * 没有己方子。
		 */
		Block blockBreath = getBlock(point);
		if (blockBreath.isEyeBlock() == true) {
			if (blockBreath.getShape().getMinDelta() > 5) {
				// 初始的大气块。
				;
			} else
				return "点";
		}
		for (List<Delta> list : Constant.NEIGHBOR) {
			for (Delta neighbor : list) {
				row = neighbor.getRowDelta() + point.getRow();
				column = neighbor.getColumnDelta() + point.getColumn();
				if (isValid(row, column) == false)
					continue;
				if (state[row][column] == myColor) {
					if (neighbor.normalize().equals(Constant.DELTA_BIG_KNIGHT)) {
						return "大飞拆";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_ELEPHANT_KNIGHT)) {
						return "象飞拆";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_SMALL_KNIGHT)) {
						return "小飞拆";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_SHOULDER)) {
						return "小尖";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_ONE_DISTANCE)) {
						return "拆一";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_TWO_DISTANCE)) {
						return "拆二";
					}

				} else if (state[row][column] == opponentColor) {
					if (neighbor.normalize().equals(Constant.DELTA_BIG_KNIGHT)) {
						return "大飞挂";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_ELEPHANT_KNIGHT)) {
						return "象飞挂";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_SMALL_KNIGHT)) {
						return "小飞挂";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_SHOULDER)) {
						return "肩冲";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_ONE_DISTANCE)) {
						return "一间挂";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_TWO_DISTANCE)) {
						return "二间挂";
					}
				}
			}
		}

		/*
		 * 附近（大飞范围之内）没有子
		 */
		if (point.Normalize().equals(Point.GAOMU)) {
			return "高目占角";
		} else if (point.Normalize().equals(Point.XIAOMU)) {
			return "小目占角";
		} else if (point.Normalize().equals(Point.MUWAI)) {
			return "目外占角";
		} else if (point.Normalize().equals(Point.SANSAN)) {
			return "三三占角";
		} else if (point.Normalize().equals(Point.XING)) {
			return "星位占角";
		} else if (point.Normalize().nearConer()) {
			return "特殊占角方式";
		} else if (point.Normalize().nearBorder()) {
			return "边上分投";
		} else {
			return "中央分投";
		}

		// return "unknown";
	}

	private boolean isValid(int row, int column) {
		if (row > 19 || row < 1 || column < 1 || column > 19)
			return false;
		else
			return true;
	}
}
