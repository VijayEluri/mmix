package eddie.wu.linkedblock;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Block;
import eddie.wu.domain.Point;

/**
 * zheng zi ji suan hai shi fang zai du li de Class zhong jiao hao.
 * ���Ӽ��㻹�Ƿ��ڶ��������нϺá����������㷨���п������ԡ� �Ժ�����ƹ㵽���еĵж����������������������Ӽ��㡣 2005/08 ��������ȿ�¡������.
 * �����л�ʵ����ȿ�¡Ҳ����������. Decision 1: ��Mementoģʽ��ʵ��״̬�Ŀ���. Ҳ��Ч�ʲ����¡,��Ϊ��Ҫ�����ռ������Ϣ.
 * �����ȱ�֤����������и���Ҫ. ���ܿ��������������.
 * 
 * @author eddie
 * 
 */
public class ZhengZiCalculate {
	private static final Log log = LogFactory.getLog(ZhengZiCalculate.class);

	private final static int MAX = 1; // �������ӷ�

	private final static int MIN = 2; // �������ӷ�

	private int numberOfNodes = 100000; // ������,��չ���Ľڵ���.

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
		byte[][] result = jisuanzhengziWithClone(state, pointInTargetBlock
				.getRow(), pointInTargetBlock.getColumn());
		Point[] points = convertArrayToPoints(result);
		return points;
	}

	protected Point[] convertArrayToPoints(byte[][] result) {
		Point[] points = new Point[result.length];
		for (int i = 0; i < result.length; i++) {
			byte[] temp = result[i];
			points[i] = new Point(temp[0], temp[1]);
		}
		return points;
	}

	/**
	 * 
     * ע����������������ĳһ�ֵ���ɫ��!
	 * @param state
	 *            ��Ҫ�������ӵľ���
	 * @param row
	 *            �����ӵĿ��ĳһ��������
	 * @param column
	 *            �����ӵĿ��ĳһ��������
	 * @return byte[][] ���ڱ�ʾ����.
	 */
	public byte[][] jisuanzhengziWithClone(BoardColorState state, byte row,
			byte column) {
		byte zhengzijieguo[][] = new byte[127][2];

		// ��������Ŀ顣
		GoBoard linkedBlockGoBoard = new GoBoard(state);
		linkedBlockGoBoard.generateHighLevelState();
        
		Block blockToBeEaten = linkedBlockGoBoard.getBoardPoint(row, column)
				.getBlock();
		if (log.isDebugEnabled()) {
			log.debug("block: " + blockToBeEaten);
		}

		int jumianshu = 0; // ��ǰ���о���š�
		GoBoard[] go = new GoBoard[numberOfNodes];// stack for all the Ju Mian

		byte[] za = new byte[numberOfNodes]; // ������go��Ӧ�������ߵ�ĺ����ꡣgo[0] have
		// no
		// corresponding za,zb.
		byte[] zb = new byte[numberOfNodes]; // ������go��Ӧ�������ߵ�������ꡣ

		byte SOUSUOSHENDU = 120;
		byte cengshu = 0; // ��ǰ���в�����

		Controller[] controllers = new Controller[SOUSUOSHENDU];
		initControllers(SOUSUOSHENDU, controllers);

		if (blockToBeEaten.getColor() == ColorUtil.BLACK) {
			linkedBlockGoBoard.setShoushu(1);//?
			if (log.isDebugEnabled()) {
				log.debug("Ҫ�����ӵ����Ϊ��ɫ���ְ׷����ܷ����ӣ�");
			}
		} else if (blockToBeEaten.getColor() == ColorUtil.WHITE) {
			linkedBlockGoBoard.setShoushu(0);//?
			if (log.isDebugEnabled()) {
				log.debug("Ҫtaopao�����Ϊ��ɫ���ֺڷ����ܷ����ӣ�");
			}
		}

		log.debug("clone in index:" + 0);
		go[0] = this.getGoBoardCopy(linkedBlockGoBoard);
		// linkedBlockGoBoard itself will be forwarded.

		// 1.��ʼ��		
		GoBoard temp;

		controllers[0].setIndexForJuMian(0);
		controllers[0].setNumberOfJuMian(1);
		jumianshu = 0;

		// 2.��ʼ���㡣
		while (true) {
			// ��һ��ѭ����չ�����һ�����档
			if (cengshu >= (SOUSUOSHENDU - 1)) {
				if (log.isDebugEnabled()) {
					log.debug("������"+SOUSUOSHENDU+"�㣬��û�н�������ز���ȷ���");
				}
				return zhengzijieguo;
			} else {
				cengshu++; // �²�Ĳ�š�dang qian de gong zuo suo zai de ceng
				// ceng 0 shi yu ding yi de.
				if (log.isDebugEnabled()) {
					log.debug("\n\n�µĵ�ǰ����Ϊ��" + cengshu);
				}
				controllers[cengshu].setIndexForJuMian(jumianshu + 1);
				// �²�Ŀ�ʼ�㡣dang qian ceng de ju mian cong zhe li kaishi bian hao

				if (log.isDebugEnabled()) {
					log.debug("�²�Ŀ�ʼ��������Ϊ��" + (jumianshu + 1));
				}
			}

			
			
			log.debug("clone in index:" + jumianshu);
			temp =  (go[jumianshu]).getGoBoardCopy();

			// temp.initPoints();
			// Ҫչ���ľ��档
			blockToBeEaten = temp.getBoardPoint(row, column).getBlock();
//			 ���ӷ���ѡ����Ϊ
			if (controllers[cengshu].getWhoseTurn() == MIN) {
				if (log.isDebugEnabled()) {
                    log.debug("��ǰ������" + cengshu);
					log.debug("��ǰ����˭�ߣ�" + "MIN");
				}
				if (log.isDebugEnabled()) {
					log.debug("����һ����MAX�ߵõ��ò�");
				}

				LocalResultOfZhengZi result = temp
						.getLocalResultOfZhengZiForMAX(row, column);
                if (log.isDebugEnabled()) {
                    log.debug("LocalResultOfZhengZi��" + result);
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
						controllers[cengshu]
								.setNumberOfJuMian(controllers[cengshu]
										.getNumberOfJuMian() - 1);
						if (controllers[cengshu].getNumberOfJuMian() != 0) {
							jumianshu = controllers[cengshu]
									.getLastIndexForJumian();
                            cleanJuMian(jumianshu, go);
							break;
						}
					}
				} else if (result.isTie()) {

					controllers[cengshu].setNumberOfJuMian(result.getNumberOfCandidates());
					
                    int count=0;
                    for(java.util.Iterator iterator=result.getCandidateJuMians().iterator();iterator.hasNext();){
                        count++;
                        go[jumianshu+count]=(GoBoard)iterator.next();
                        if(go[jumianshu+count]==null){
                            throw new RuntimeException("go[jumianshu+count-1]==null");
                        }
                    }
                    System.out.println("count="+count);
                    jumianshu += result.getNumberOfCandidates();
                    System.out.println("jumianshu="+jumianshu);
				} else if (result.isSelfSuccess()) {//impossible

				}

			} // if min
			// �����ӷ���ѡ����
			else if (controllers[cengshu].getWhoseTurn() == MAX) {
				if (log.isDebugEnabled()) {
                    log.debug("��ǰ������" + cengshu);
					log.debug("��ǰ����˭�ߣ�" + "MAX");
				}
				if (log.isDebugEnabled()) {
					log.debug("��һ����˭�ߣ�" + "MIN");
				}
				if (log.isDebugEnabled()) {
					log.debug("����һ����MIN�ߵõ��ò�");
				}

				// ���غ�ѡ��.���߷��ؽ��

				LocalResultOfZhengZi result = temp.getLocalResultOfZhengZiForMIN(row,
						column);
                if (log.isDebugEnabled()) {
                    log.debug("LocalResultOfZhengZi��" + result);
                }
				if (result.isSelfSuccess()) {
					cengshu -= 1; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
					controllers[cengshu].setNumberOfJuMian(controllers[cengshu]
							.getNumberOfJuMian() - 1);
					if (controllers[cengshu].getNumberOfJuMian() != 0) {
						jumianshu = controllers[cengshu]
								.getLastIndexForJumian();

					} else {
						while (true) {
							cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
							if (cengshu == -1) {
								zhengzijieguo[0][0] = -127;
								return zhengzijieguo;
							}
							jumianshu = dealWithCeng(jumianshu, cengshu,
									controllers);
						}
					}
				} else if (result.isSelfFail()) {
					while (true) {
						cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						 if (cengshu == 0) {
						     zhengzijieguo[0][0] = 127;
                             byte [][] hui=go[jumianshu].getStepHistory().hui;
//                             for(int j=0;j<512;j++){
//                                 System.out.println("["+hui[j][0]+","+hui[j][1]+"];");
//                             }
                             return zhengzijieguo;
                         }
						//
						// byte lins = 0;
						// for (lins = 2; st[lins][0] != 0; lins++) {
						// if (log.isDebugEnabled()) {
						// log.debug("��Ϊ:(" + za[st[lins][0] - 1]
						// + "," + zb[st[lins][0] - 1] + ")");
						// } // this.cgcl(za[st[lins][0]-1],zb[st[lins][0]-1]);
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
							jumianshu = controllers[cengshu]
									.getLastIndexForJumian();
							// st[cengshu - 2][3] = 127;
                            this.cleanJuMian(jumianshu,go);
							break;
						}
					}

				} else if (result.isTie()) {
					controllers[cengshu].setNumberOfJuMian(result.getNumberOfCandidates());
                    int count=0;
                    for(java.util.Iterator iterator=result.getCandidateJuMians().iterator();iterator.hasNext();){
                        count++;
                        go[jumianshu+count]=(GoBoard)iterator.next();
                        if(go[jumianshu+count]==null){
                            throw new RuntimeException("go[jumianshu+count-1]==null");
                        }
                    }
                    System.out.println("count="+count);
                    jumianshu += result.getNumberOfCandidates();
                    System.out.println("jumianshu="+jumianshu);
				}
			} // max

			

		} // while

	}

    /**
     * @param jumianshu
     * @param go
     */
    private void cleanJuMian(int jumianshu, GoBoard[] go) {
        for(int dd=jumianshu+1;dd<this.numberOfNodes&&go[jumianshu]!=null;dd++){
            go[jumianshu]=null;
        }
    }

	private int dealWithCeng(int jumianshu, byte cengshu,
			Controller[] controllers) {
		controllers[cengshu].setNumberOfJuMian(controllers[cengshu]
				.getNumberOfJuMian() - 1);
		if (controllers[cengshu].getNumberOfJuMian() != 0) {
			jumianshu = controllers[cengshu].getLastIndexForJumian();

		}
		return jumianshu;
	}

	private void initControllers(byte SOUSUOSHENDU, Controller[] controllers) {
		Controller controller;
		for (byte i = 0; i < SOUSUOSHENDU; i++) {
			// ������������ȡ�
			controller = new Controller();
			controller.setWhoseTurn(MAX);
			controllers[i] = controller;
			i++;
			controller = new Controller();
			controller.setWhoseTurn(MIN);
			controllers[i] = controller;

		}
	}

	private GoBoard getGoBoardCopy(GoBoard goBoard) {
		GoBoard temp = new GoBoard(goBoard.getBoardColorState());
		temp.generateHighLevelState();
		return temp;
	}
}
