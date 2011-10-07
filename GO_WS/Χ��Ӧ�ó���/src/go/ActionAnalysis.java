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
 * ��ǰ�����£�����ĳһ������Ӧ������������ʲô���������֮�ࡣ
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
		 * �����ж����ӵ������
		 */
		if (eaten == 1) {
			return "��";
		} else if (eaten == 2) {
			return "˫��";
		} else if (eaten == 3) {
			return "����";
		} else if (eaten == 4) {
			return "����";
		}

		int breath = blank + breaths.size();
		if (log.isInfoEnabled()) {
			log.info(point + " ���Ӻ��� " + breath + " ��");
		}
		if (breath == 0) {
			return "��ɱ";
		} else if (breath == 1) {
			if (beat >= 1) {// ���д�Բ������롣
				if (connect == 0) {
					return "������";
				} else {
					return "������";
				}
			} else {
				return "�ͳ�";
			}
		}
		// else if (breath == 2) {
		// return "��ɱ";
		// }

		/*
		 * �����������ڵ��ڶ���
		 */
		if (beat == 1) {
			return "����";
		} else if (beat == 2) {
			return "˫��";
		} else if (beat == 3) {
			return "����";
		}

		/*
		 * ��ؿ����������ڵ��ڶ���
		 */
		if (opponent == 1) {// �⣬������������

		} else if (opponent == 2) {
			if (opponents.get(0).isDiagonal(opponents.get(1))) {
				int deltaR = opponents.get(0).getRow() - point.getRow();
				deltaR += (opponents.get(1).getRow() - point.getRow());
				int deltaC = opponents.get(0).getColumn() - point.getColumn();
				deltaC += (opponents.get(1).getColumn() - point.getColumn());
				if (state[point.getRow() + deltaR][point.getColumn() + deltaC] == myColor) {
					return "��";
				} else if (state[point.getRow() + deltaR][point.getColumn()
						+ deltaC] == Constant.BLANK) {
					return "��/��";
				} else {
					return "���μ�/��";
				}

			} else if (opponents.get(0).isSameline(opponents.get(1))) {
				if (connect == 0) {
					return "��";
				} else {
					return "��";
				}
			}
		} else if (opponent == 3) {
			return "��";
		}

		/*
		 * û�жԷ��ӡ�
		 */

		if (connect == 1) {
			return "��";
		} else if (connect == 2) {
			return "ճ";
		} else if (connect == 3) {
			return "��ճ";
		} else if (connect == 4) {
			return "��ճ";
		}

		/*
		 * û�м����ӡ�
		 */
		Block blockBreath = getBlock(point);
		if (blockBreath.isEyeBlock() == true) {
			if (blockBreath.getShape().getMinDelta() > 5) {
				// ��ʼ�Ĵ����顣
				;
			} else
				return "��";
		}
		for (List<Delta> list : Constant.NEIGHBOR) {
			for (Delta neighbor : list) {
				row = neighbor.getRowDelta() + point.getRow();
				column = neighbor.getColumnDelta() + point.getColumn();
				if (isValid(row, column) == false)
					continue;
				if (state[row][column] == myColor) {
					if (neighbor.normalize().equals(Constant.DELTA_BIG_KNIGHT)) {
						return "��ɲ�";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_ELEPHANT_KNIGHT)) {
						return "��ɲ�";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_SMALL_KNIGHT)) {
						return "С�ɲ�";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_SHOULDER)) {
						return "С��";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_ONE_DISTANCE)) {
						return "��һ";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_TWO_DISTANCE)) {
						return "���";
					}

				} else if (state[row][column] == opponentColor) {
					if (neighbor.normalize().equals(Constant.DELTA_BIG_KNIGHT)) {
						return "��ɹ�";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_ELEPHANT_KNIGHT)) {
						return "��ɹ�";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_SMALL_KNIGHT)) {
						return "С�ɹ�";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_SHOULDER)) {
						return "���";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_ONE_DISTANCE)) {
						return "һ���";
					} else if (neighbor.normalize().equals(
							Constant.DELTA_TWO_DISTANCE)) {
						return "�����";
					}
				}
			}
		}

		/*
		 * ��������ɷ�Χ֮�ڣ�û����
		 */
		if (point.Normalize().equals(Point.GAOMU)) {
			return "��Ŀռ��";
		} else if (point.Normalize().equals(Point.XIAOMU)) {
			return "СĿռ��";
		} else if (point.Normalize().equals(Point.MUWAI)) {
			return "Ŀ��ռ��";
		} else if (point.Normalize().equals(Point.SANSAN)) {
			return "����ռ��";
		} else if (point.Normalize().equals(Point.XING)) {
			return "��λռ��";
		} else if (point.Normalize().nearConer()) {
			return "����ռ�Ƿ�ʽ";
		} else if (point.Normalize().nearBorder()) {
			return "���Ϸ�Ͷ";
		} else {
			return "�����Ͷ";
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
