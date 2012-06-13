package eddie.wu.domain.analy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Delta;
import eddie.wu.domain.GoBoardLadder;
import eddie.wu.domain.Group;
import eddie.wu.domain.Point;
import eddie.wu.domain.Shape;
import eddie.wu.domain.comp.BlockBreathComparatorDesc;
import eddie.wu.domain.comp.RowColumnComparator;
import eddie.wu.domain.conn.ConnectivityAnalysis;

/**
 * 判断是否两眼活棋。两眼活棋是最基本的终止局面，所以这个判断非常基础和重要。<br/>
 * 1. 这个问题还是挺复杂的，要考虑到特殊的活棋形式，比如两头蛇活棋，形式上是两个假眼。<br/>
 * 2. 还有二子气块被点的情况，一般是不入气，相当于一眼，但是也有可能对方可以提子。<br/>
 * 完整的大眼位被点入后容易成为不入气的局面，应该能直接识别为等价于眼位，而不需要提子。<br/>
 * 看来这些情况要通过高于棋块的数据结构来解决。<br/>
 * 
 * 术语的歧义.比如说不入气,可能有两个不同的意思.<br/>
 * 一是指对方落子后仅有一气,本方可以提吃.(即对方送吃)<br/>
 * 另一个意思是指对方落子后没有气,因而不能落子于该处(禁着不入气点).<br/>
 * 意思相近但是又的确有不同. <br/>
 * 分别称为GiftPoint和SuicidePoint。 <br/>
 * 
 * change history: 考虑劫活，但是只处理最简单的紧劫。<br/>
 * 
 * 原先死活的分析依赖于连接性分析的结果，因此有继承关系，现在好像没有这种依赖关系了。<br/>
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class SurviveAnalysis extends ConnectivityAnalysis {

	private static final Logger log = Logger.getLogger(SurviveAnalysis.class);

	public SurviveAnalysis(int boardSize) {
		super(boardSize);
	}

	public SurviveAnalysis(byte[][] state) {
		super(state);

	}
	
	/**
	 * called if only isLive == false
	 * @return
	 */
	public boolean isDead(){
		return true;
	}

	/**
	 * is the block live, currently only consider the shape of one block.<br/>
	 * 先考虑一块棋两眼的情况，比较简单。然后处理多块棋的情况。 <br/>
	 * key method!
	 * 
	 * @param point
	 *            any point in the target block<br/>
	 *            用块中的任意点来代表块.
	 * @return
	 */
	public boolean isLive(Point point) {
		point = this.getBlock(point).getBehalfPoint();
		log.warn("Is live for status: ");
		this.printState();
		log.warn("for block:");
		log.warn(getBlock(point));

		boolean live;

		live = isOneBlockTwoEyeLive(point);
		if (live == true) {
			if (log.isInfoEnabled()) {
				log.warn("简单的两真眼活棋！" + point);
			}
			return true;
		}

		live = this.isBigEyeLive(point);
		if (live == true) {
			if (log.isInfoEnabled()) {
				log.warn("简单的大眼活棋！" + point);
			}
			return true;
		}

		Block block = getBlock(point);
		int myColor = block.getColor();

		Set<Point> eyes = getEyes(block, myColor);
		if (log.isInfoEnabled()) {
			log.info("块代表点为 " + point + ", 周围眼位气块为 " + eyes);
		}
		if (eyes.size() < 2) {

			return isLiveWithoutTwoEye(point);
			// return false;
		}

		/*
		 * 眼位周围的块需要全部考虑进来。 一下的代码虽然精妙，但是太复杂了。以后再想办法利用。
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

					for (Delta delta : Constant.ADJACENTS) {
						Point nb = eye.getNeighbour(delta);
						if (nb == null)
							continue;

						if (getColor(nb) == myColor) {
							temp = this.getBlock(nb);
							// block2.addEye(eye);
							if (allBlocks.contains(temp) == false
									&& newBlocks.contains(temp) == false) {
								if (log.isInfoEnabled()) {
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
			removed = false;
			for (Iterator<Block> it = blocks.iterator(); it.hasNext();) {
				Block block2 = it.next();

				if (block2.numberOfEyes() == 0) {
					// if(log.isDebugEnabled()) log.debug("");
					if (log.isDebugEnabled())
						log.debug("The block has no eye:");
					if (log.isDebugEnabled())
						log.debug(block2);
					continue;
				}
				/*
				 * 只有一个不入气点的棋块，其眼位也是假眼。
				 */
				if (block2.numberOfEyes() <= 1) {
					Point eyePoint = block2.getEyes().iterator().next();
					fakeEyes.add(eyePoint);
					it.remove();
					removed = true;

					// 虽然是假眼,仍然是可以连接起来的.(work around! actually one step
					// forward!)
					Set<Point> mergedEyes = new HashSet<Point>();
					for (Block blockNeighbor : this.getBlankBlock(eyePoint)
							.getNeighborBlocks()) {
						if (blockNeighbor == block2)
							continue;
						blockNeighbor.removeEye(eyePoint);
						mergedEyes.addAll(blockNeighbor.getEyes());
					}
					for (Block blockNeighbor : this.getBlankBlock(eyePoint)
							.getNeighborBlocks()) {
						if (blockNeighbor == block2)
							continue;
						blockNeighbor.addEyes(mergedEyes);
					}
				}
			}
			if (removed == false) {//
				break;
			}
			if (log.isInfoEnabled()) {
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
			if (log.isInfoEnabled()) {
				for (Block temp2 : blocks) {
					log.warn("Block " + temp2.getBehalfPoint() + "两眼为: "
							+ Arrays.toString(temp2.getEyes().toArray()));
				}
			}
		}
		if (blocks.isEmpty()) {
			return false;
		} else if (blocks.contains(block)) {
			return true;
		} else {
			/*
			 * TODO: 是否有接不归的问题。
			 */
			return true;
		}

	}

	/**
	 * 判断一个单点眼是否为真眼、假眼、或者半眼(后手眼)，通过查看九宫顶点的方式来确定。
	 * 
	 * @param targetBlock
	 * @param eyePoint
	 * @param fakeEyes
	 * @param halfEyes
	 * @param realEyes
	 */
	public void handleSinglePointEye(Block targetBlock, Point eyePoint,
			Set<Point> fakeEyes, Set<Point> halfEyes, Set<Point> realEyes) {
		int friend = 0;
		int enemy = 0;
		int blank = 0;

		int myColor = targetBlock.getColor();
		for (Delta delta : Constant.SHOULDERS) {
			Point shoulder = eyePoint.getNeighbour(delta);
			if (shoulder == null)
				continue;

			if (this.getColor(shoulder) == myColor) {
				friend++;
			} else if (this.getColor(shoulder) == Constant.BLANK) {
				int breaths = this.breathAfterPlay(shoulder,
						targetBlock.getEnemyColor()).size();
				if (breaths <= 1) {// 对方不入气或者自提。
					// TODO 打劫的处理
					friend++;// 空白点由本方控制,计成本方点.
				} else {
					blank++;
				}
			} else {// enemy
				enemy++;
			}
		}

		if (eyePoint.isCenterEye()) {// center： friend + enemy + blank == 4
			if (enemy >= 2) {// TODO 进一步考虑能否攻击卡眼的点.
				fakeEyes.add(eyePoint);
			} else if (enemy == 1) {
				if (blank >= 2) {
					fakeEyes.add(eyePoint);
				} else if (blank == 1) {
					halfEyes.add(eyePoint);
				} else if (blank == 0) {
					realEyes.add(eyePoint);
				}

			} else if (enemy == 0) {

				if (blank >= 4) {
					fakeEyes.add(eyePoint);
				} else if (blank == 3) {
					halfEyes.add(eyePoint);
				} else if (blank == 2) {
					realEyes.add(eyePoint);
				}
			}
		} else if (eyePoint.isBorderEye()) {
			// boarder： friend + enemy + blank == 2
			if (enemy >= 1) {
				fakeEyes.add(eyePoint);
			} else if (blank == 0) {
				realEyes.add(eyePoint);
			} else if (blank == 1) {
				halfEyes.add(eyePoint);
			} else if (blank == 2) {
				fakeEyes.add(eyePoint);
			}
		} else if (eyePoint.isCornerEye()) {
			// corner： friend + enemy + blank == 1
			if (friend == 1) { // 已经成眼
				realEyes.add(eyePoint);
			} else if (enemy == 1) {// 已经破眼
				fakeEyes.add(eyePoint);
			} else if (blank == 1) {// 后手眼
				halfEyes.add(eyePoint);
			}
		}

	}

	/**
	 * 是否双活,无眼活棋情况的一种.长生劫的情况没有考虑.<br/>
	 * 首先是一块棋没有两只眼,但是对方杀不掉.识别出这种情况之后进一步识别双活的情况.<br/>
	 * (这里活棋是指已经活棋,对方杀不掉.)
	 * 
	 * @return
	 */
	public boolean isLiveWithoutTwoEye(Point point) {
		Set<Point> eyes = this.getRealEyes(point, false).getRealEyes();

		Block block = getBlock(point);
		// int giftPoint = 0;// 送吃点。
		// for (Point breath : block.getAllBreathPoints()) {
		// int enemyColor = block.getEnemyColor();
		// NeighborState state2 = this.getNeighborState(breath, enemyColor);
		// if (state2.isGift())
		// giftPoint++;
		// }

		Set<Point> giftPoints = this.getGiftPoints(block);
		if (log.isInfoEnabled())
			log.info("送吃不入气点： " + giftPoints);
		if (eyes.isEmpty()) {// 一个眼也没有。
			if (giftPoints.size() >= 2) {
				log.info("两个或以上送吃不入气点活棋。");
				return true;
			}
		} else if (eyes.size() == 1) {// 已经有一眼。
			if (giftPoints.size() >= 1) {
				log.info("一个或以上送吃不入气点活棋。");
				return true;
			}

		}

		return false;
	}

	/**
	 * 检查不入气点(或者气块),可能是假眼. 调用该方法前，已经判断过没有大眼（子数>=7）。<br/>
	 * 所以这里每个眼块都算一个眼。<br/>
	 * 但是这些稍大一点的眼未必是真眼，比如边上的二子眼（甚至是四子眼）可能是假眼。
	 * 
	 * @param targetBlock
	 * @param myColor
	 * @return
	 */
	private Set<Point> getEyes(Block targetBlock, int myColor) {
		Set<Point> eyes = new HashSet<Point>();
		/*
		 * 检查气点中是否有眼。二三子的眼看成一个不入气.
		 */
		for (BlankBlock blankBlock : targetBlock.getBreathBlocks()) {
			int numberOfPoint = blankBlock.getNumberOfPoint();
			if (blankBlock.isEyeBlock() == false) {// 是一个自提不入气点
				if (numberOfPoint == 1) {
					Point breath = blankBlock.getUniquePoint();
					if (isEye(targetBlock, breath)) {// 不入气点能否成眼
						eyes.add(breath);
						targetBlock.addEye(breath);
						if (log.isInfoEnabled()) {
							log.info("单子公共气块(自提不入气) " + breath);
						}
					}

				}
				continue;
			}

			if (this.isEyeValid(targetBlock, blankBlock) == false) {
				continue;
			}

			if (numberOfPoint > 1) {// 多子眼块

				Point topLeftPoint = blankBlock.getTopLeftPoint();
				eyes.add(topLeftPoint);
				targetBlock.addEye(topLeftPoint);
				if (log.isInfoEnabled()) {
					log.info("多子眼块 " + numberOfPoint + topLeftPoint);
				}

				// block breath block info should already be covered.
			} else { // 单子眼块
				Point breath = blankBlock.getUniquePoint();
				eyes.add(breath);
				targetBlock.addEye(breath);
				if (log.isInfoEnabled()) {
					log.info("单子眼块 " + numberOfPoint + breath);
				}
			}
		}
		if (log.isInfoEnabled()) {
			log.warn("不入气点(或气块)为: " + eyes);
		}
		return eyes;
	}

	/**
	 * 一块棋两眼的活棋情况。是终局状态中最为常见的一种。<br/>
	 * 即使不是简单的两眼活棋,仍有可能是活棋的其他情况.<br/>
	 * 这里的两眼指的是小的两眼
	 * 
	 * @param point
	 *            any point in the target block<br/>
	 * @return true if one block has two real eyes; false otherwise, false does
	 *         not necessarily mean it can not survive.
	 */
	public boolean isOneBlockTwoEyeLive(Point point) {

		EyeResult eyeResult = getRealEyes(point, false);
		Set<Point> realEyes = eyeResult.getRealEyes();
		if (log.isInfoEnabled()) {
			if (realEyes.isEmpty()) {
				log.warn("块" + point + "没有真眼");
			} else {
				log.warn("块" + point + " 拥有的真眼为: " + realEyes);
			}
		}
		if (realEyes.size() >= 2) {
			return true;
		} else if (realEyes.size() == 1) {
			if (eyeResult.getHalfEyes().size() >= 2)
				// 两个后手眼必可以占到一个
				return true;
		} else {
			if (eyeResult.getHalfEyes().size() >= 4)
				// 四个后手眼必可以占到二个
				// TODO:对方一子两用破眼的情况（可能性）。
				return true;
		}
		return false;
	}

	/**
	 * 是否大眼活棋，简化算法（计算双活足够了）。必须大于等于七气(>=7）<br/>
	 * 不要求是成眼的棋子是同一块，但是每块棋需要大于等于三气(>=3)
	 * 
	 * @param target
	 * @return
	 */
	public boolean isBigEyeLive(Point target) {
		Block targetBlock = getBlock(target);

		int breathP = 0;
		for (BlankBlock blankB : targetBlock.getBreathBlocks()) {
			if (blankB.isEyeBlock() == true) {
				breathP += blankB.getNumberOfPoint();
			}
		}
		if (targetBlock.getNumberOfPoint() * 2 < breathP) {
			/**
			 * 初始(刚落一子时)的大眼.成眼棋块的子数还不到眼块子数的一半。
			 */
			return false;
		}

		for (BlankBlock blankB : targetBlock.getBreathBlocks()) {
			// 是否有大眼气块
			if (blankB.isEyeBlock() == false)
				continue;
			boolean flaw = false;
			for (Block blockSurround : blankB.getNeighborBlocks()) {
				if (blockSurround.getBreaths() == 2) {

					for (Point breath : blockSurround.getBreathPoints()) {
						if (this.breathAfterPlay(breath,
								targetBlock.getEnemyColor()).size() >= 2) {
							flaw = true;
							break;
						}
						// return false;
					}

					// if (flaw == true) {
					// if (2 < minBreath)// 当成二气，代表有缺陷的眼位。
					// minBreath = 2;
					// } else {
					// if (3 < minBreath)// 当成三气，代表没有缺陷。
					// minBreath = 3;
					// }
				} else if (blockSurround.getBreaths() == 1)
					flaw = true;
				// minBreathBlock = blockSurr;
			}

			if (blankB.getNumberOfPoint() >= 7) {
				// 是否成眼的棋块没有缺陷。
				if (flaw == false) {
					return true;

				} else {// need further calculation.
					return false;
				}
			}
			if (blankB.getNumberOfPoint() >= 4) {
				/**
				 * 看是否有两个点能两眼活棋，且最少的气大于等于3。
				 */
				// int count = 0;
				// List<Point> candidates = this.getBrokenPoint(blankB);
				// for (Point breath : candidates) {
				// this.oneStepForward(breath, block.getColor());
				// if (this.isLive(target))
				// count++;
				// this.oneStepBackward();
				// if (count >= 2)
				// return true;
				// // if(count>=2) break;
				//
				// }
			}

		}

		return false;
	}

	/**
	 * 得到所有的(狭义的)真眼.处理90%的情况.<br/>
	 * 大眼在这里不算真眼。<br/>
	 * 
	 * 眼位可能还没有定型，比如半只眼是指先手可以有一眼，后手则无眼。<br/>
	 * 因此增加先后手的参数targetFirst。
	 * 
	 * 
	 * @param target
	 *            any point in the target block<br/>
	 * @param targetFirst
	 *            是否轮到目标棋块方走。
	 * @return
	 */
	public EyeResult getRealEyes(Point target, boolean targetFirst) {

		EyeResult es = new EyeResult();
		Set<Point> realEyes = new HashSet<Point>();
		Set<Point> fakeEyes = new HashSet<Point>();
		Set<Point> halfEyes = new HashSet<Point>();
		es.setFakeEyes(fakeEyes);
		es.setHalfEyes(halfEyes);
		es.setRealEyes(realEyes);

		Block block = getBlock(target);
		int myColor = block.getColor();
		Point eyePoint = null;
		for (BlankBlock blankBlock : block.getBreathBlocks()) {
			if (blankBlock.isEyeBlock() == false) {// 公气

				continue;
			}
			if (blankBlock.getNumberOfPoint() != 1)// 大眼
				continue;

			eyePoint = blankBlock.getUniquePoint();
			this.handleSinglePointEye(block, eyePoint, fakeEyes, halfEyes,
					realEyes);
		}

		/**
		 * 检查气点中是否有真眼。
		 */
		// for (Point breathPoint : block.getAllBreathPoints()) {
		// if (isSameBlockEye(block, breathPoint, myColor)) {
		// eyes.add(breathPoint);
		// }
		// }
		return es;
	}

	/**
	 * 判断一个气点是否为真正的眼位。(1.单子眼位,2.围成眼的点属于同块.3.)<br/>
	 * 不可能为假眼，因为气点的周围属于同一个棋块。<br/>
	 * 不入气点暂不考虑。<br/>
	 * Block中的数据结构如果正确维护的话,这里的算法可以简化.当前的实现对block的数据依赖很少.
	 * 
	 * @deprecated
	 * @param targetBlock
	 *            目标棋块
	 * @param breathPoint
	 *            是目标棋块的一个气点
	 * @param myColor
	 * @return
	 */
	private boolean isSameBlockEye(Block targetBlock, Point breathPoint,
			int myColor) {
		int enemyColor = ColorUtil.enemyColor(myColor);
		for (Delta delta : Constant.ADJACENTS) {
			Point nb = breathPoint.getNeighbour(delta);
			if (nb == null)
				continue;

			int color = this.getColor(nb);
			if (color == enemyColor) {// 公气
				return false;
			} else if (color == myColor) {
				/*
				 * 没有连成一块。 not same block
				 */

				if (this.getBlock(nb) != targetBlock)
					return false;
			} else if (color == ColorUtil.BLANK) {
				// 不是单子眼。眼位为二子或以上的气块。这里不予识别和考虑。
				return false;
			}
		}
		return true;// 是真眼
	}

	/**
	 * 判断一个不入气点是否为眼位。<br/>
	 * 这里不入气点指的是对方点入我方眼块的子,只有一气,不能在形式上的公气点落子
	 * 
	 * @param breath
	 *            气点
	 * @return
	 */
	public boolean isEye(Block block, Point breath) {
		int myColor = block.getColor();
		int enemyColor = block.getEnemyColor();

		for (Delta delta : Constant.ADJACENTS) {
			Point nb = breath.getNeighbour(delta);
			if (nb == null)
				continue;

			int color = this.getColor(nb);
			if (color != enemyColor)
				continue;

			// boolean buruqi = false;
			if (getBlock(nb).getBreathPoints().size() == 1) {
				/*
				 * 进一步检查是否为对方的不入气点。效果相当于眼。 TODO：没有考虑对方可能提子！
				 */
				for (Delta delta2 : Constant.ADJACENTS) {
					Point nb2 = breath.getNeighbour(delta2);
					if (nb2 == null)
						continue;
					int color2 = this.getColor(nb2);
					if (color2 == Constant.BLANK) {
						// buruqi = false;
						return false;
					} else if (color2 == myColor) {
						if (getBlock(nb2) != block) {
							return false;
						}
					}
				}

				/**
				 * 不入气点相邻的敌块必须只和本方目标块相邻. 否则其不入气的性质可能改变.
				 */
				for (Block enemyEnemy : getBlock(nb).getEnemyBlocks()) {
					if (enemyEnemy.equals(block) == false)
						return false;
				}
				// 需要检查邻块有没有全部二气或以上。
				// if(boardPoints[m][n].getBreaths()==)
				return true;
			} else {
				return false;
			}

		}
		return false;
	}

	/**
	 * 确定了是眼位(相邻的子全部属于己方)。(TODO: 在初始化时确保正确!不用临时算)<br/>
	 * 检查是否是打劫的情况。或者是打二还一的情况，总之对方可以提子回去。
	 */
	public boolean isEyeValid(Block targetBlock, BlankBlock blankBlock) {
		// int myColor = block.getColor();
		if (targetBlock.getNumberOfPoint() * 2 < blankBlock.getNumberOfPoint()) {
			return false;
		}

		for (Block neighborB : blankBlock.getNeighborBlocks()) {

			// for (Delta delta : Constant.ADJACENTS) {
			// Point nb = breath.getNeighbour(delta);
			// if (nb == null)
			// continue;
			//
			// if (this.getColor(nb) == Constant.BLANK)
			// continue;
			//
			// Block blockTemp = getBlock(nb);
			// 围成breath点所在的眼块的棋块中有气数为一的。
			if (neighborB.getBreaths() == 1) {
				if (log.isInfoEnabled()) {
					log.warn("假眼 －－－ 打N还一（N=" + blankBlock.getNumberOfPoint()
							+ ")");
				}
				return false;
			}
		}
		return true;// 是眼
	}

	public void analyzeAllGroup() {
		for (Group group : groups) {

			for (Block block : group.getBlocks()) {
				if (this.isLive(block.getBehalfPoint())) {

					log.warn("check group " + group);
					log.warn("group is live because its block is live"
							+ block.getBehalfPoint());

					group.setLive(true);
					break;
				}
			}
			if (group.isLive() == false) {
				int eyeAreaSize = this.getEyeArea2(group).size();
				if (eyeAreaSize >= 10) {
					group.setLive(true);
					log.warn("group is live because of big eye area "
							+ eyeAreaSize);
				}
			}
			if (group.isLive() == false) {
				log.warn("check group " + group);
				log.warn("group is not live ");
			}

		}

		for (Group group : groups) {
			if (group.isLive())
				continue;
			log.warn("not live group" + group);
			if (group.hasWeakerGroup()) {
				group.setLive(true);
				log.warn("it becomess live because of weaker neighbor group");
			}
		}

	}

	/**
	 * 处理不了的情况。<br/>
	 * . 块[1,12]没有真眼
	 */
	public void analyzeAllBlock() {
		int toCheck = 7;
		int count = 0;
		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(this.getBlackWhiteBlocks());
		Collections.sort(blocks, new BlockBreathComparatorDesc());
		for (Block block : blocks) {
			count++;
			if (count != toCheck) {
				continue;
			}

			boolean live = this.isLive(block.getBehalfPoint());
			if (log.isDebugEnabled())
				log.debug("check " + block);
			if (log.isDebugEnabled())
				log.debug("live = " + live);

			if (live == false) {
				for (BlankBlock blankBlock : block.getBreathBlocks()) {
					if (blankBlock.isEyeBlock()) {

					}
				}
			}

			if (count == toCheck)
				break;
		}
	}

	/**
	 * 气块周围的黑块或者白块是否有完好的连接,不会出现倒脱靴的情形. (气块周围的子块是否有效的连接起来了.和别处的代码(连接性处理的)稍有重复
	 * 
	 * @return
	 */
	public boolean isBlockConnected(BlankBlock blankBlock) {
		Set<Block> weakB = new HashSet<Block>();
		// 围成眼位的块处于被打吃状态, 如倒扑.
		for (Block block : blankBlock.getNeighborBlocks()) {
			if (block.getBreaths() == 1)
				return false;
			else if (block.getBreaths() == 2) {
				weakB.add(block);
			}
		}
		/**
		 * 围成眼位的块处于可以被断吃的状态, 如倒脱靴.<br/>
		 * 或者对方可以打吃,粘则减少眼位大小,且对方已经点入一子.可能是有力的手段.
		 */

		for (Block block : weakB) {
			GoBoardLadder cal = new GoBoardLadder(this.getBoardColorState());
			byte[][] zhengziCalculate = cal.jiSuanZhengZi(block
					.getBehalfPoint());
			if (zhengziCalculate[0][0] == 127) {
				return false;
			}
			// TODO: return false if ladder succeed. i.e. the block could be
			// eaten by enemy!
		}
		// 每个子块都有三气或以上.
		return true;

	}

	/**
	 * 收集一块棋的送吃不入气点。主要是看有没有双活的情况。这是两眼活棋之外的另外一种终局<br/>
	 * 可能，因为变化没有穷尽，需用动态的计算，不能静态地判定。此时没有自提不入气点的可能。
	 * 
	 * @param targetBlock
	 *            目标棋块
	 * @return
	 */
	public Set<Point> getGiftPoints(Block targetBlock) {
		Set<Point> gifts = new HashSet<Point>();// 对方可能的送礼/吃点。
		Set<Point> breathPoints = new HashSet<Point>();

		Set<Block> enemyBlocks = new HashSet<Block>();// 共享该气块的敌块
		for (BlankBlock breathBlock : targetBlock.getBreathBlocks()) {

			if (breathBlock.isEyeBlock())
				continue;

			if (targetBlock.isBlack())
				enemyBlocks.addAll(breathBlock.getWhiteBlocks());
			else
				enemyBlocks.addAll(breathBlock.getBlackBlocks());
		}

		for (Block enemyBlock : enemyBlocks) {
			if (enemyBlock.getBreaths() > 2)
				continue;
			else if (enemyBlock.getBreaths() < 2) {
				continue;
				// 自提不入气点。
			}
			// enemyBlock.getBreaths() == 2
			// copy to prevent concurrent modification during oneStepForward.
			breathPoints.addAll(enemyBlock.getBreathPoints());

			for (Point breath : breathPoints) {
				Set<Point> breathPoints2 = this.breathAfterPlay(breath,
						enemyBlock.getColor());
				int breaths = breathPoints2.size();
				if (breaths == 1) {
					// 直接落子是自紧一气，有没有其他的长气手段，如提子。
					if (this.canIncreaseBreath(enemyBlock))
						continue;

					log.info("对方尝试送吃 " + breath);
					this.oneStepForward(breath, enemyBlock.getColor());
					// log.info("气数变为： " + enemyBlock);
					// log.info("气数变为： " + enemyBlock.getAllBreathPoints());
					// log.info("本方提子于 " + enemyBlock.getLastBreath());
					// TODO:计算过程中可能块被改变了（生成新块）。
					log.info("气数变为： " + this.getBlock(breath));
					this.oneStepForward(getBlock(breath).getLastBreath(),
							targetBlock.getColor());
					if (this.isLive(targetBlock.getBehalfPoint())) {
						log.info("确定了一个送吃点 " + breath);
						gifts.add(breath);
					}
					this.oneStepBackward();
					
					log.warn(this.getBlock(1, 1));
					log.warn(this.getBlock(1, 3));
					this.oneStepBackward();
					log.warn(this.getBlock(1, 1));
					log.warn(this.getBlock(1, 3));
				}
			}

		}
		if (gifts.isEmpty()) {
			log.warn("there is no gift point for status");
		} else {
			log.warn("gift point " + gifts + " for status");
		}
		this.printState();

		return gifts;
	}

	/**
	 * 
	 * 要送吃的块,原先只有二气,落子后会减少一气.如果两口气都是这种情况,只有送吃一途.<br/>
	 * 但未必不行.若有一个能长气,则也是脱困之道.<br/>
	 * 还有扑的情况.<br>
	 * 简化处理.要求该块的气点除了不入气点,就是送吃点. <br/>
	 * whether the block can increase its breath. <br/>
	 * 要求原块是二气，计算是否能增加到三气，然后可以入气
	 * 
	 * @param block
	 *            该块能否长气。
	 * @return
	 */
	public boolean canIncreaseBreath(Block block) {
		for (Point breath : block.getBreathPoints()) {
			for (Delta delta : Constant.ADJACENTS) {
				Point p = breath.getNeighbour(delta);
				if (p == null)
					continue;
				int breaths = this.breathAfterPlay(breath, block.getColor())
						.size();
				if (breaths > block.getBreaths())
					return true;
			}
		}
		return false;
	}

	/**
	 * 一块棋的潜在眼位大小，估算。不是死活计算。<br/>
	 * 将一块棋沿边境线扩展，到达底线，封闭出一个区域。<br/>
	 * 需要扩展的程度也反映了眼位的成型程度<br/>
	 * 平方距离为1的扩张，只差边线没有围上。<br/>
	 * 平方距离为4的扩张，只差边线和二线没有围上。<br/>
	 * 平方距离为9的扩张，边线二线和三线都没有围上。一般不考虑<br/>
	 */
	public Set<Point> getEyeArea(Block block) {
		Set<Point> eyeArea = new HashSet<Point>();

		for (Point point : block.getPoints()) {
			for (List<Delta> listDelta : Constant.NEIGHBOR_FROM_ADJACENT) {
				for (Delta delta : listDelta) {
					Point temp = point.getNeighbour(delta);
					if (temp == null)
						continue;

					if (this.getColor(temp) == Constant.BLANK) {
						for (Delta deltaR : listDelta) {

						}
					}
				}
			}
		}
		return eyeArea;
	}

	/**
	 * 新的实现。依赖于Connectivity的算法，围成松散眼位的块往往不完整。也不可能象<br/>
	 * 眼块那样通过眼块关联起来。
	 * 
	 * @param group
	 * @return
	 */
	public Set<Point> getEyeArea2(Group group) {
		Set<Point> all = new HashSet<Point>();
		Set<Point> eyeArea = new HashSet<Point>();
		Set<Point> eyeArea_copy = new HashSet<Point>();

		Set<Point> lastLevel = new HashSet<Point>();
		Set<Point> tempLevel;
		Set<Point> nextLevel = new HashSet<Point>();
		for (Block block : group.getBlocks()) {
			lastLevel.addAll(block.getBreathPoints());
		}
		all.addAll(lastLevel);
		if (log.isDebugEnabled()) {
			log.debug("原有气点" + this.getPointList(lastLevel));
		}

		int level = 1;
		int myColor = group.getColor();
		/**
		 * 距离按照row+column来算。最多五线占边。
		 */
		do {
			log.debug("候选点为" + this.getPointList(lastLevel));
			for (Point point : lastLevel) {
				int enemy = 0;
				for (Delta delta : Constant.ADJACENTS) {
					Point temp = point.getNeighbour(delta);

					if (temp == null)
						continue;

					if (this.isBlank(temp)) {
						// if (!lastLevel.contains(temp)
						// && !eyeArea.contains(temp))
						if (!all.contains(temp)) {
							nextLevel.add(temp);
						} else {
							all.add(temp);// ever considered
						}
					} else if (this.getColor(temp) == myColor) {

					} else {
						enemy++;
						break;
					}

				}
				if (enemy == 0) {
					if (this.no_enemy_nearby(point, group.getColor(), level))
						eyeArea.add(point);
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("第" + level + "轮后眼位点为  " + this.getPointList(eyeArea));
				eyeArea.removeAll(eyeArea_copy);
				log.debug("第" + level + "轮后新增眼位点为  "
						+ this.getPointList(eyeArea));
				eyeArea.addAll(eyeArea_copy);
				eyeArea_copy.addAll(eyeArea);

			}

			lastLevel.clear();
			tempLevel = lastLevel;
			lastLevel = nextLevel;
			nextLevel = tempLevel;

		} while (++level <= 4);

		/**
		 * 临界的点不算在眼位内。
		 * 
		 * 
		 */
		for (Point point : eyeArea) {
			this.setColor(point, myColor);
		}
		for (Iterator<Point> pointIter = eyeArea.iterator(); pointIter
				.hasNext();) {
			Point point = pointIter.next();
			for (Delta delta : Constant.ADJACENTS) {
				Point temp = point.getNeighbour(delta);
				if (temp == null)
					continue;
				if (this.isBlank(temp)) {
					pointIter.remove();
					break;
				}
			}

		}
		for (Point point : eyeArea) {
			this.setColor(point, Constant.BLANK);
		}
		if (log.isDebugEnabled()) {
			log.debug("size=" + eyeArea.size());
			log.debug("排除敌子相邻点后眼位点为  " + this.getPointList(eyeArea));
		}
		group.setEyeArea(eyeArea);
		return eyeArea;

	}

	public List<Point> getPointList(Set<Point> eyeArea) {

		List<Point> list = new ArrayList<Point>(eyeArea.size());
		list.addAll(eyeArea);
		Collections.sort(list, new RowColumnComparator());

		return list;
	}

	/**
	 * 在指定距离之内没有敌方之子。
	 * 
	 * @param point2
	 * @param myColor
	 * @param level
	 * @return
	 */
	public boolean no_enemy_nearby(Point point2, int myColor, int level) {
		int levelR = level;
		Set<Point> lastLevel = new HashSet<Point>();
		Set<Point> tempLevel;
		Set<Point> nextLevel = new HashSet<Point>();
		lastLevel.add(point2);
		while (levelR-- >= 1) {
			int enemy = 0;
			for (Point point : lastLevel) {
				for (Delta delta : Constant.ADJACENTS) {
					Point temp = point.getNeighbour(delta);

					if (temp == null)
						continue;
					if (this.isBlank(temp)) {
						nextLevel.add(temp);
					} else if (this.getColor(temp) == myColor) {

					} else {
						enemy++;
						return false;
					}

				}

			}

			lastLevel.clear();
			tempLevel = lastLevel;
			lastLevel = nextLevel;
			nextLevel = tempLevel;

		}

		return true;
	}

	public void controlArea(Group group) {
		Shape shape = group.getShape();

		// if(shape.)
		group.getAllPoints();

	}

	/**
	 * whether each point's liveness is decided. TODO: enhance fake code very
	 * good test case: Exception for count=8331<br/>
	 * [B, _, W]<br/>
	 * [B, _, W]<br/>
	 * [B, B, W]<br/>
	 * 
	 * @return
	 */
	public boolean isFinalState() {
		if (boardSize == 2) {
			if (this.getColor(1, 1) == Constant.BLACK
					&& this.getColor(2, 2) == Constant.BLACK
					&& this.getColor(1, 2) == Constant.BLANK
					&& this.getColor(2, 1) == Constant.BLANK)
				return true;
		}

		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(this.getBlackWhiteBlocks());
		/**
		 * 原始气块,尚未落子.
		 */
		if (blocks.isEmpty())
			return false;

		Collections.sort(blocks, new BlockBreathComparatorDesc());
		for (Block block : blocks) {

			block.setLive(this.isLive(block.getBehalfPoint()));
		}

		for (Block block : blocks) {
			if (block.isLive())
				continue;

			boolean allEnemyLive = true;
			if (block.getEnemyBlocks().isEmpty()) {
				allEnemyLive = false;
			} else {
				for (Block enemyBlock : block.getEnemyBlocks()) {
					if (enemyBlock.isLive() == false) {
						allEnemyLive = false;
						break;
					}
				}
			}
			if (allEnemyLive == true) {
				block.setDead(true);
			}
		}

		boolean unknown = false;
		for (Block block : blocks) {
			if (block.isLive() == false && block.isDead() == false) {
				unknown = true;
				break;
			}
		}

		if (unknown == true) {
			return false;
		} else {
			return true;
		}

		// return false;
	}

	/**
	 * 这里只考虑是否已经是真眼，不考虑是否可以补一手成为真眼。 <br/>
	 * 还是三值逻辑更合适 <br/>
	 * 不够精确,被getRealEyes取代.
	 * 
	 * @param eye
	 * @return
	 */
	public boolean isRealEye_old(Point eye, int enemyColor) {

		int count = 0;// 敌方点或者空白点
		if (eye.isCenterEye()) {// 棋盘中间的眼
			for (Delta delta : Constant.SHOULDERS) {
				Point nb = eye.getNeighbour(delta);
				if (nb == null)
					continue;
				if (this.getColor(nb) == ColorUtil.BLANK
						|| getColor(nb) == enemyColor) {
					count++;
				}
			}

			if (count > 1)
				return false;
			else
				return true;
		} else {// if (eye.getMinLine() == 1) //边上的眼。

			for (Delta delta : Constant.SHOULDERS) {
				Point nb = eye.getNeighbour(delta);
				if (nb == null)
					continue;
				if (this.getColor(nb) == ColorUtil.BLANK
						|| getColor(nb) == enemyColor) {
					return false;
				}
			}

			return true;
			// } else {//角上的眼。==1
			//
			// }
		}
	}

}
