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
 * �ж��Ƿ����ۻ��塣���ۻ��������������ֹ���棬��������жϷǳ���������Ҫ��<br/>
 * 1. ������⻹��ͦ���ӵģ�Ҫ���ǵ�����Ļ�����ʽ��������ͷ�߻��壬��ʽ�����������ۡ�<br/>
 * 2. ���ж������鱻��������һ���ǲ��������൱��һ�ۣ�����Ҳ�п��ܶԷ��������ӡ�<br/>
 * ������Щ���Ҫͨ�������������ݽṹ�������
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
	 * ����ֻ�����Ƿ��Ѿ������ۣ��������Ƿ���Բ�һ�ֳ�Ϊ���ۡ�
	 * 
	 * @param eye
	 * @return
	 */
	public boolean isRealEye(Point eye, int enemyColor) {
		int a;
		int b;
		int count = 0;
		if (eye.getMinLine() > 1) {// �����м����
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
			// if (eye.getMinLine() > 1) {//���ϵ��ۡ�
			for (int i = 0; i < 4; i++) {
				a = (byte) (eye.getRow() + Constant.szdjd[i][0]);
				b = (byte) (eye.getColumn() + Constant.szdjd[i][1]);
				if (boardPoints[a][b].getColor() == ColorUtil.BLANK
						|| boardPoints[a][b].getColor() == enemyColor) {
					return false;
				}
			}
			return true;
			// } else {//���ϵ��ۡ�==1
			//
			// }
		}
	}

	/**
	 * is the block live, currently only consider the shape of one block.
	 * �ȿ���һ�������۵�������Ƚϼ򵥡�Ȼ�������������� key method!
	 * 
	 * @param point
	 * @return
	 */
	public boolean isLive(Point point) {
		boolean live = isOneBlockTwoEyeLive(point);
		if (live == true) {
			if (log.isWarnEnabled()) {
				log.warn("�򵥵������ۻ��壡");
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
		 * ��λ��Χ�Ŀ���Ҫȫ�����ǽ�����
		 */

		/*
		 * ���ǣ���Щ�ۿ������ڶ����顣��λ����ĳ�����������ֻ��һ����λʱ������λ�Ǽ��ۡ�
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
		 * blocks �е����������һ�ۡ�
		 */
		blocks = allBlocks;
		while (blocks.isEmpty() == false) {
			for (Iterator<Block> it = blocks.iterator(); it.hasNext();) {
				Block block2 = it.next();
				/*
				 * ֻ��һ�������������飬����λҲ�Ǽ��ۡ�
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
				log.warn("�漰�Ĳ���������: " + Arrays.toString(fakeEyes.toArray()));
			}
			for (Iterator<Block> it = blocks.iterator(); it.hasNext();) {
				Block block2 = it.next();
				/*
				 * ����еļ���Ҫɾ������
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
					log.warn("����Ϊ: "
							+ Arrays.toString(temp2.getEyes().toArray()));
				}
			}
		}
		if (blocks.isEmpty()) {
			return false;
		} else if (blocks.contains(block)) {
			if (log.isWarnEnabled()) {
				log.warn("����Ϊ: " + Arrays.toString(block.getEyes().toArray()));
			}
			return true;
		} else {
			/*
			 * TODO: �Ƿ��нӲ�������⡣
			 */
			return true;
		}
	}

	/**
	 * ��鲻������,�����Ǽ���.
	 * 
	 * @param block
	 * @param myColor
	 * @return
	 */
	private Set<Point> getEyes(Block block, int myColor) {
		Set<Point> eyes = new HashSet<Point>();
		/*
		 * ����������Ƿ����ۡ������ӵ��ۿ���һ��������.
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
			log.warn("��������Ϊ: " + Arrays.toString(eyes.toArray()));
		}
		return eyes;
	}

	/**
	 * һ�������۵Ļ�����������վ�״̬����Ϊ������һ�֡�
	 * 
	 * @return true if one block has two real eyes; false otherwise, false does
	 *         not necessarily mean it can not survive.
	 */
	public boolean isOneBlockTwoEyeLive(Point point) {

		Set<Point> eyes = getRealEyes(point);
		if (log.isWarnEnabled()) {
			if (eyes.isEmpty()) {
				log.warn(point + "û������");
			} else {
				log.warn(point + " ӵ�е�����Ϊ: " + Arrays.toString(eyes.toArray()));
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
		 * ����������Ƿ������ۡ�
		 */
		for (Point point2 : block.getAllBreathPoints()) {
			if (isSameBlockEye(block, point2, myColor)) {
				eyes.add(point2);
			}
		}
		return eyes;
	}

	/**
	 * �ж�һ�������Ƿ�Ϊ��λ��������Ϊ���ۣ���Ϊ�������Χ����ͬһ����顣 ���������ݲ����ǡ�
	 * 
	 * @param block
	 * @param point
	 *            ������
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
			 * ����жϰѱ߽������ļ���ƹ�ȥ�ˡ�
			 */
			if (state[m][n] == enemyColor) {// ����
				return false;
			} else if (state[m][n] == myColor) {
				/*
				 * û������һ�顣 not same block
				 */

				if (this.getBlock(Point.getPoint(m, n)) != block)
					return false;
			} else if (state[m][n] == ColorUtil.BLANK) {
				// ���ǵ����ۡ���λΪ���ӻ����ϵ����顣���ﲻ��ʶ��Ϳ��ǡ�
				return false;
			}
		}
		return true;// ������
	}

	/**
	 * �ж�һ�������Ƿ�Ϊ��λ������Ҳ���ԡ���������Ҳ�㡣
	 * 
	 * @param point
	 *            ����
	 * @return
	 */
	public boolean isEye(Block block, Point point, int myColor) {
		int enemyColor = ColorUtil.enemyColor(myColor);
		int m, n;
		for (int i = 0; i < 4; i++) {
			m = point.getRow() + Constant.szld[i][0];
			n = point.getColumn() + Constant.szld[i][1];
			/*
			 * ����жϰѱ߽������ļ���ƹ�ȥ�ˡ�
			 */
			if (state[m][n] == enemyColor) {
				// boolean buruqi = false;
				if (boardPoints[m][n].getBlock().getAllBreathPoints().size() == 1) {
					/*
					 * ��һ������Ƿ�Ϊ�Է��Ĳ������㡣Ч���൱���ۡ� TODO��û�п��ǶԷ��������ӣ�
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
					// ��Ҫ����ڿ���û��ȫ�����������ϡ�
					// if(boardPoints[m][n].getBreaths()==)
					return true;
				} else {
					return false;
				}
			}
		}

		/*
		 * ȷ��������λ�� ����Ƿ��Ǵ�ٵ�����������Ǵ����һ���������֮�Է��������ӻ�ȥ��
		 */
		for (int i = 0; i < 4; i++) {
			m = point.getRow() + Constant.szld[i][0];
			n = point.getColumn() + Constant.szld[i][1];
			Block blockTemp = boardPoints[m][n].getBlock();
			if (blockTemp != null && blockTemp.getAllBreathPoints().size() == 1) {
				if (log.isWarnEnabled()) {
					log.warn("��N��һ��N="
							+ boardPoints[m][n].getBlock()
									.getTotalNumberOfPoint());
				}
				return false;
			}
		}
		return true;// ����
	}
}
