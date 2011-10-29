package eddie.wu.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.linkedblock.ColorUtil;
import eddie.wu.linkedblock.LocalResultOfZhengZi;

/**
 * (zheng zi ji suan hai shi fang zai du li de Class zhong jiao hao.)
 * ���Ӽ��㻹�Ƿ��ڶ��������нϺá����������㷨���п������ԡ� �Ժ�����ƹ� �����еĵж����������������������Ӽ��㡣 2005/08 ��������ȿ�¡������.
 * �����л�ʵ����ȿ�¡Ҳ����������. Decision 1: ��Momentoģʽ��ʵ��״̬ * �Ŀ���. Ҳ��Ч�ʲ����¡,��Ϊ��Ҫ�����ռ������Ϣ.
 * �����ȱ�֤����������и���Ҫ. ���ܿ��������������.
 * 
 * depends on linked.block.GoBoard
 * 
 * @author eddie
 * 
 */
public class ZhengZiCalculate {
	private static final Log log = LogFactory.getLog(ZhengZiCalculate.class);
	static final int SEARTHDEPTH = 120;

	static final int MAX = 1; // �������ӷ�

	static final int MIN = 2; // �������ӷ�

	static final int numberOfNodes = 100000; // ������,��չ���Ľڵ���.

	/**
	 * instance level fields
	 */

	byte zhengzijieguo[][] = new byte[127][2];

	/*
	 * ��ǰ���в�����
	 */
	byte cengshu = 0;

	/*
	 * ��ǰ���о��������š�
	 */
	int lastJumianIndex = 0;

	/**
	 * ��ʼ״̬����0ֻ����һ�����档����ջ�еı����0
	 */
	Controller[] controllers;

	GoBoard[] go = new GoBoard[numberOfNodes];
	/*
	 * ������go��Ӧ�������ߵ�ĺ����ꡣ ������go��Ӧ�������ߵ�������ꡣ <br/> go[0] have no corresponding
	 * za,zb. go[0] move to go[1] with step (za[1,zb[1]). since there are
	 * several options in each state. we can not store the option/step in
	 * starting state.
	 */
	byte[] za = new byte[numberOfNodes];
	byte[] zb = new byte[numberOfNodes];
	GoBoard temp;

	/**
	 * 
	 * @param state
	 *            ��Ҫ�������ӵľ���
	 * @param pointInTargetBlock
	 *            �����ӵĿ��ĳһ��.����ָ�������ӵĿ�.
	 * @return Points[] ���ڱ�ʾ����.
	 */
	public Point[] jisuanzhengziWithClone(BoardColorState state,
			Point pointInTargetBlock) {
		byte[][] result = zhengziCalculate(state, pointInTargetBlock.getRow(),
				pointInTargetBlock.getColumn());
		Point[] points = convertArrayToPoints(result);
		return points;
	}

	public static Point[] convertArrayToPoints(byte[][] result) {
		Point[] points = new Point[result.length];
		for (int i = 0; i < result.length; i++) {

			byte[] temp = result[i];
			if (temp[0] == 0 && temp[1] == 0)
				break;
			points[i] = Point.getPoint(temp[0], temp[1]);
		}
		return points;
	}

	public static byte[][] convertPointsToArray(Point[] points) {
		byte[][] result = new byte[points.length][2];
		for (int i = 0; i < points.length; i++) {
			if (points[i] == null)
				break;
			result[i][0] = points[i].getRow();
			result[i][1] = points[i].getColumn();

		}
		return result;
	}

	/**
	 * 
	 * 
	 * 
	 * @param state
	 *            ��Ҫ�������ӵľ���
	 * @param row
	 *            �����ӵĿ��ĳһ��������
	 * @param column
	 *            �����ӵĿ��ĳһ��������
	 * @return byte[][] ���ڱ�ʾ����.
	 */
	public byte[][] zhengziCalculate(BoardColorState state, byte row,
			byte column) {
		controllers = initControllersMaxMin();
		controllers[0].setIndexForJuMian(0);
		controllers[0].setNumberOfJuMian(1);

		// ��������Ŀ顣
		GoBoard linkedBlockGoBoard = new GoBoard(state);
		linkedBlockGoBoard.generateHighLevelState();

		Block blockToBeEaten = linkedBlockGoBoard.getBlock(row, column);
		if (log.isDebugEnabled()) {
			log.debug("block: " + blockToBeEaten);
		}

		// if (blockToBeEaten.getColor() == ColorUtil.BLACK) {
		// linkedBlockGoBoard.setShoushu(1);// ?
		// if (log.isDebugEnabled()) {
		// log.debug("Ҫ�����ӵ����Ϊ��ɫ���ְ׷����ܷ����ӣ�");
		// }
		// } else if (blockToBeEaten.getColor() == ColorUtil.WHITE) {
		// linkedBlockGoBoard.setShoushu(0);// ?
		// if (log.isDebugEnabled()) {
		// log.debug("Ҫ���ܵ����Ϊ��ɫ���ֺڷ����ܷ����ӣ�");
		// }
		// }

		/*
		 * stack for all the Ju Mian
		 */

		log.debug("clone in index:" + 0);
		go[0] = this.getGoBoardCopy(linkedBlockGoBoard);
		// linkedBlockGoBoard itself will be forwarded.

		/*
		 * 2.��ʼ���㡣 ��һ��ѭ����չ�����һ�����档
		 */

		while (true) {

			if (cengshu >= (SEARTHDEPTH - 1)) {
				if (log.isDebugEnabled()) {
					log.debug("������" + SEARTHDEPTH + "�㣬��û�н�������ز���ȷ���");
				}
				return zhengzijieguo;
			} else {
				temp = (go[lastJumianIndex]).getGoBoardCopy();
				if (cengshu != 0) {
					Point lastPoint = temp.getLastPoint();
					zhengzijieguo[cengshu][0] = (byte) lastPoint.getRow();
					zhengzijieguo[cengshu][1] = (byte) lastPoint.getColumn();
					zhengzijieguo[0][1] = cengshu;
				}
				/*
				 * �²�Ĳ�š� ��ǰ�������ڵĲ㣬�µĲ�(dang qian de gong zuo suo zai de ceng)
				 * ��0��Ԥ�ȶ����(ceng 0 shi yu ding yi de.)
				 */
				cengshu++;
				if (log.isDebugEnabled()) {
					log.debug("\n\n�µĵ�ǰ����Ϊ��" + cengshu);
				}
				/*
				 * �²�Ŀ�ʼ�㡣 ��ǰ��ľ�������￪ʼ���(dang qian ceng de ju mian cong zhe li
				 * kaishi bian hao)
				 */
				controllers[cengshu].setIndexForJuMian(lastJumianIndex + 1);

				if (log.isDebugEnabled()) {
					log.debug("�²�Ŀ�ʼ��������Ϊ��" + (lastJumianIndex + 1));
				}
			}

			log.debug("clone old board/state in index:" + lastJumianIndex);

			// temp.initPoints();
			// Ҫչ���ľ��档
			blockToBeEaten = temp.getBoardPoint(row, column).getBlock();
			// ���ӷ���ѡ����Ϊ
			if (controllers[cengshu].getWhoseTurn() == MIN) {
				if (log.isDebugEnabled()) {
					log.debug("��ǰ������" + cengshu);
					log.debug("��ǰ����˭�ߣ�" + "MIN");
					log.debug("����һ����MAX�ߵõ��ò�");
				}

				LocalResultOfZhengZi result = temp
						.getLocalResultOfZhengZiForMAX(row, column);
				if (log.isDebugEnabled()) {
					log.debug("LocalResultOfZhengZ= " + result);
				}
				if (result.isSelfFail()) {
					// ���أ�һ�������ӷ����ӿ��¡�
					if (log.isDebugEnabled()) {
						log.debug("��Ч��Ϊ0");

					}
					// TODO?st[cengshu][0] = 0;

					if (cengshu == 1) { // ���ӷ�ֱ�����ӿ��£�
						zhengzijieguo[0][0] = -127;
						return zhengzijieguo;
					}

					while (true) {
						cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						if (cengshu == -1) {
							zhengzijieguo[0][0] = -127;
							return zhengzijieguo;
						}
						// for (byte lins = 2; st[lins][0] != 0; lins++) {
						// if (log.isDebugEnabled()) {
						// log.debug("��Ϊ:(" + za[st[lins][0] - 1]
						// + "," + zb[st[lins][0] - 1] + ")");
						// }
						// zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
						// zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];
						//
						// }
						//
						// return zhengzijieguo;
						// }
						controllers[cengshu].decreaseJuMian();
						if (controllers[cengshu].getNumberOfJuMian() != 0) {
							lastJumianIndex = controllers[cengshu]
									.getLastIndexForJumian();
							cleanJuMianAfter(lastJumianIndex, go);
							break;
						}
					}
				} else if (result.isTie()) {// �������Ӽ���

					controllers[cengshu].setNumberOfJuMian(result
							.getNumberOfCandidates());

					int count = 0;
					for (java.util.Iterator<GoBoard> iterator = result
							.getCandidateJuMians().iterator(); iterator
							.hasNext();) {
						count++;
						go[lastJumianIndex + count] = (GoBoard) iterator.next();
						if (go[lastJumianIndex + count] == null) {
							throw new RuntimeException(
									"go[jumianshu+count]==null");
						}
					}
					System.out.println("count=" + count);
					lastJumianIndex += result.getNumberOfCandidates();
					System.out.println("jumianshu=" + lastJumianIndex);
				} else if (result.isSelfSuccess()) {// impossible
					// ���ӳɹ�ֻ���Ǳ����ӷ����ܳ�����
				}
				// if min
			} else if (controllers[cengshu].getWhoseTurn() == MAX) {// �����ӷ���ѡ����
				if (log.isDebugEnabled()) {
					log.debug("��ǰ������" + cengshu);
					log.debug("��ǰ����˭�ߣ�" + "MAX");
					log.debug("��һ����˭�ߣ�" + "MIN");

					log.debug("����һ����MIN�ߵõ��ò�");
				}

				// ���غ�ѡ��.���߷��ؽ��

				LocalResultOfZhengZi result = temp
						.getLocalResultOfZhengZiForMIN(row, column);
				if (log.isDebugEnabled()) {
					log.debug("LocalResultOfZhengZi=" + result);
				}
				if (result.isSelfSuccess()) {
					cengshu -= 1; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
					controllers[cengshu].decreaseJuMian();
					if (controllers[cengshu].getNumberOfJuMian() != 0) {
						lastJumianIndex = controllers[cengshu]
								.getLastIndexForJumian();

					} else {
						while (true) {
							cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
							if (cengshu == -1) {
								zhengzijieguo[0][0] = -127;
								return zhengzijieguo;
							}
							lastJumianIndex = dealWithCeng(lastJumianIndex, cengshu,
									controllers);
						}
					}
				} else if (result.isSelfFail()) {
					while (true) {
						cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						if (cengshu == 0) {
							zhengzijieguo[0][0] = 127;
							//byte[][] hui = go[lastJumianIndex].getStepHistory().hui;
							// for(int j=0;j<512;j++){
							// System.out.println("["+hui[j][0]+","+hui[j][1]+"];");
							// }
							return zhengzijieguo;
						}
						//
						// byte lins = 0;
						// for (lins = 2; st[lins][0] != 0; lins++) {
						// if (log.isDebugEnabled()) {
						// log.debug("��Ϊ:(" + za[st[lins][0] - 1]
						// + "," + zb[st[lins][0] - 1] + ")");
						// } //
						// this.cgcl(za[st[lins][0]-1],zb[st[lins][0]-1]);
						// zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
						// zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];
						//
						// }
						// zhengzijieguo[0][1] = (byte) (lins - 2);
						// return zhengzijieguo;
						// }

						controllers[cengshu]
								.setNumberOfJuMian(controllers[cengshu]
										.getNumberOfJuMian() - 1);
						if (controllers[cengshu].getNumberOfJuMian() != 0) {
							lastJumianIndex = controllers[cengshu]
									.getLastIndexForJumian();
							// st[cengshu - 2][3] = 127;
							this.cleanJuMianAfter(lastJumianIndex, go);
							break;
						}
					}

				} else if (result.isTie()) {
					controllers[cengshu].setNumberOfJuMian(result
							.getNumberOfCandidates());
					int count = 0;
					for (java.util.Iterator<GoBoard> iterator = result
							.getCandidateJuMians().iterator(); iterator
							.hasNext();) {
						count++;
						go[lastJumianIndex + count] = (GoBoard) iterator.next();
						if (go[lastJumianIndex + count] == null) {
							throw new RuntimeException(
									"go[jumianshu+count-1]==null");
						}
					}
					System.out.println("count=" + count);
					lastJumianIndex += result.getNumberOfCandidates();
					System.out.println("jumianshu=" + lastJumianIndex);
				}
			} // max

		} // while
	}

	/**
	 * �����ǰ����֮��ľ���
	 * 
	 * @param jumianshu
	 * @param go
	 */
	void cleanJuMianAfter(int jumianshu, GoBoard[] go) {
		for (int dd = jumianshu + 1; dd < this.numberOfNodes
				&& go[jumianshu] != null; dd++) {
			go[jumianshu] = null;
		}
	}

	protected int dealWithCeng(int jumianshu, byte cengshu,
			Controller[] controllers) {
		controllers[cengshu].setNumberOfJuMian(controllers[cengshu]
				.getNumberOfJuMian() - 1);
		if (controllers[cengshu].getNumberOfJuMian() != 0) {
			jumianshu = controllers[cengshu].getLastIndexForJumian();

		}
		return jumianshu;
	}

	/*
	 * ������������ȡ�
	 */
	Controller[] initControllersMaxMin() {
		Controller[] controllers = new Controller[SEARTHDEPTH];
		Controller controller;

		for (byte i = 0; i < controllers.length; i++) {
			controller = new Controller();
			controller.setWhoseTurn(MAX);
			controllers[i] = controller;
			i++;
			controller = new Controller();
			controller.setWhoseTurn(MIN);
			controllers[i] = controller;

		}
		return controllers;
	}

	Controller[] initControllersMinMax() {
		Controller[] controllers = new Controller[SEARTHDEPTH];
		Controller controller;

		for (byte i = 0; i < controllers.length; i++) {
			controller = new Controller();
			controller.setWhoseTurn(MIN);
			controllers[i] = controller;
			i++;
			controller = new Controller();
			controller.setWhoseTurn(MAX);
			controllers[i] = controller;

		}
		return controllers;
	}

	GoBoard getGoBoardCopy(GoBoard goBoard) {
		GoBoard temp = new GoBoard(goBoard.getBoardColorState());
		temp.generateHighLevelState();
		return temp;
	}
}
