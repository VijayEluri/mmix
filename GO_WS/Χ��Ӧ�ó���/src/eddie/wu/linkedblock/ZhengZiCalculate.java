package eddie.wu.linkedblock;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Block;
import eddie.wu.domain.Point;

/**
 * zheng zi ji suan hai shi fang zai du li de Class zhong jiao hao.
 * 征子计算还是放在独立的类中较好。这样搜索算法才有可重用性。 以后可以推广到所有的敌对搜索，而不仅仅限于征子计算。 2005/08 遇到了深度克隆的问题.
 * 用序列化实现深度克隆也遇到了问题. Decision 1: 用Memento模式来实现状态的拷贝. 也许效率不如克隆,因为需要重新收集棋局信息.
 * 但是先保证程序可以运行更重要. 性能可以留待将来解决.
 * 
 * @author eddie
 * 
 */
public class ZhengZiCalculate {
	private static final Log log = LogFactory.getLog(ZhengZiCalculate.class);

	private final static int MAX = 1; // 代表征子方

	private final static int MIN = 2; // 代表被征子方

	private int numberOfNodes = 100000; // 局面数,即展开的节点数.

	/**
	 * 
	 * @param state
	 *            需要计算征子的局面
	 * @param pointInTargetBlock
	 *            被征子的块的某一点.用于指定被征子的块.
	 * @return Points[] 用于表示正解.
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
     * 注意手数是用来决定某一手的颜色的!
	 * @param state
	 *            需要计算征子的局面
	 * @param row
	 *            被征子的块的某一点行坐标
	 * @param column
	 *            被征子的块的某一点列坐标
	 * @return byte[][] 用于表示正解.
	 */
	public byte[][] jisuanzhengziWithClone(BoardColorState state, byte row,
			byte column) {
		byte zhengzijieguo[][] = new byte[127][2];

		// 做活主体的块。
		GoBoard linkedBlockGoBoard = new GoBoard(state);
		linkedBlockGoBoard.generateHighLevelState();
        
		Block blockToBeEaten = linkedBlockGoBoard.getBoardPoint(row, column)
				.getBlock();
		if (log.isDebugEnabled()) {
			log.debug("block: " + blockToBeEaten);
		}

		int jumianshu = 0; // 当前已有局面号。
		GoBoard[] go = new GoBoard[numberOfNodes];// stack for all the Ju Mian

		byte[] za = new byte[numberOfNodes]; // 生成与go对应局面所走点的横坐标。go[0] have
		// no
		// corresponding za,zb.
		byte[] zb = new byte[numberOfNodes]; // 生成与go对应局面所走点的纵坐标。

		byte SOUSUOSHENDU = 120;
		byte cengshu = 0; // 当前已有层数。

		Controller[] controllers = new Controller[SOUSUOSHENDU];
		initControllers(SOUSUOSHENDU, controllers);

		if (blockToBeEaten.getColor() == ColorUtil.BLACK) {
			linkedBlockGoBoard.setShoushu(1);//?
			if (log.isDebugEnabled()) {
				log.debug("要逃征子的棋块为黑色，轮白方走能否征子？");
			}
		} else if (blockToBeEaten.getColor() == ColorUtil.WHITE) {
			linkedBlockGoBoard.setShoushu(0);//?
			if (log.isDebugEnabled()) {
				log.debug("要taopao的棋块为白色，轮黑方走能否征子？");
			}
		}

		log.debug("clone in index:" + 0);
		go[0] = this.getGoBoardCopy(linkedBlockGoBoard);
		// linkedBlockGoBoard itself will be forwarded.

		// 1.初始化		
		GoBoard temp;

		controllers[0].setIndexForJuMian(0);
		controllers[0].setNumberOfJuMian(1);
		jumianshu = 0;

		// 2.开始计算。
		while (true) {
			// 第一层循环：展开最后一个局面。
			if (cengshu >= (SOUSUOSHENDU - 1)) {
				if (log.isDebugEnabled()) {
					log.debug("搜索到"+SOUSUOSHENDU+"层，仍没有结果，返回不精确结果");
				}
				return zhengzijieguo;
			} else {
				cengshu++; // 新层的层号。dang qian de gong zuo suo zai de ceng
				// ceng 0 shi yu ding yi de.
				if (log.isDebugEnabled()) {
					log.debug("\n\n新的当前层数为：" + cengshu);
				}
				controllers[cengshu].setIndexForJuMian(jumianshu + 1);
				// 新层的开始点。dang qian ceng de ju mian cong zhe li kaishi bian hao

				if (log.isDebugEnabled()) {
					log.debug("新层的开始局面索引为：" + (jumianshu + 1));
				}
			}

			
			
			log.debug("clone in index:" + jumianshu);
			temp =  (go[jumianshu]).getGoBoardCopy();

			// temp.initPoints();
			// 要展开的局面。
			blockToBeEaten = temp.getBoardPoint(row, column).getBlock();
//			 征子方候选点数为
			if (controllers[cengshu].getWhoseTurn() == MIN) {
				if (log.isDebugEnabled()) {
                    log.debug("当前层数？" + cengshu);
					log.debug("当前层轮谁走？" + "MIN");
				}
				if (log.isDebugEnabled()) {
					log.debug("在上一层由MAX走得到该层");
				}

				LocalResultOfZhengZi result = temp
						.getLocalResultOfZhengZiForMAX(row, column);
                if (log.isDebugEnabled()) {
                    log.debug("LocalResultOfZhengZi？" + result);
                }
				if (result.isSelfFail()) {
					// 返回，一般是征子方无子可下。
					if (log.isDebugEnabled()) {
						log.debug("有效点为0");

					}
					// TODO?st[cengshu][0] = 0;

					if (cengshu == 1) { // 征子方直接无子可下，
						zhengzijieguo[0][0] = -127;
						return zhengzijieguo;
					}

					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						 if (cengshu == -1) {
						 zhengzijieguo[0][0] = -127;
                         return zhengzijieguo;
                         }
						// for (byte lins = 2; st[lins][0] != 0; lins++) {
						// if (log.isDebugEnabled()) {
						// log.debug("点为:(" + za[st[lins][0] - 1]
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
			// 被征子方候选点数
			else if (controllers[cengshu].getWhoseTurn() == MAX) {
				if (log.isDebugEnabled()) {
                    log.debug("当前层数？" + cengshu);
					log.debug("当前层轮谁走？" + "MAX");
				}
				if (log.isDebugEnabled()) {
					log.debug("上一层轮谁走？" + "MIN");
				}
				if (log.isDebugEnabled()) {
					log.debug("在上一层由MIN走得到该层");
				}

				// 返回候选点.或者返回结果

				LocalResultOfZhengZi result = temp.getLocalResultOfZhengZiForMIN(row,
						column);
                if (log.isDebugEnabled()) {
                    log.debug("LocalResultOfZhengZi？" + result);
                }
				if (result.isSelfSuccess()) {
					cengshu -= 1; // 倒数两层已经确定。减后的层数需要重新展开
					controllers[cengshu].setNumberOfJuMian(controllers[cengshu]
							.getNumberOfJuMian() - 1);
					if (controllers[cengshu].getNumberOfJuMian() != 0) {
						jumianshu = controllers[cengshu]
								.getLastIndexForJumian();

					} else {
						while (true) {
							cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
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
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
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
						// log.debug("点为:(" + za[st[lins][0] - 1]
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
			// 限制了搜索深度。
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
