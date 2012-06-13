/*
 * Created on 2005-4-21
 * 现在有3000行代码.其实都是围绕有限的数据结构展开的,只是实现的功能不同.为了<br/>
 * 将代码尽量分散开。决定将前进后退的功能和根据局面初始化的功能放在单独的类中<br/>
 * 死活判断以及其他的形式分析也是这样处理。所形成的类层次结构不是为了代码复用<br/>
 * 而是为了让一个类的代码不要过于膨胀<br/>
 */
package eddie.wu.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.api.GoBoardInterface;
import eddie.wu.domain.survive.BreathPattern;
import eddie.wu.search.BlankPoint;
import eddie.wu.search.BlankPointBreathComparator;

/**
 * this program is refactored from the old program which use array as data
 * structure and did not use any Collection framework. <br/>
 * 围棋程序的核心类.完成数气提子.
 * 
 * @author eddie
 */
public class GoBoard extends GoBoardBackward implements Cloneable,
		Serializable, GoBoardInterface {
	private transient static final Logger log = Logger.getLogger(GoBoard.class);
	private byte[][] initState;

	public GoBoard(int boardSize) {
		super(boardSize);

	}

	/**
	 * 
	 * really used Constructor.
	 * 
	 */
	public GoBoard() {
		this(Constant.BOARD_SIZE);
	}

	/**
	 * 
	 * really used Constructor.
	 * 
	 */
	GoBoard(BoardColorState boardState, short numberOfSteps, boolean isBlackTurn) {
		this(boardState, (int) numberOfSteps);
		if (numberOfSteps % 2 == 0) {
			if (isBlackTurn) {
				this.shoushu = numberOfSteps;
			} else {
				throw new RuntimeException("whose turn is not right");
			}
		} else {
			if (isBlackTurn) {
				throw new RuntimeException("whose turn is not right");
			} else {
				this.shoushu = numberOfSteps;
			}
		}

	}

	/**
	 * no double check really used Constructor.
	 * 
	 */
	public GoBoard(BoardColorState boardState, int numberOfSteps) {
		this(boardState.boardSize);
		final Set<Point> blackPoints = boardState.getBlackPoints();
		final Set<Point> whitePoints = boardState.getWhitePoints();

		// Here we only set color, block is not up to date yet.
		for (Point tempPoint : blackPoints) {
			setColor(tempPoint, ColorUtil.BLACK);
		}
		for (Point tempPoint : whitePoints) {
			setColor(tempPoint, ColorUtil.WHITE);
		}

		this.shoushu = numberOfSteps;
		generateHighLevelState();

	}

	/**
	 * no double check really used Constructor. ，<br/>
	 * 适用于做题, 局面已经初始化好了.或者让子棋的局面
	 */
	public GoBoard(BoardColorState boardState) {
		this(boardState, 0);

	}

	/**
	 * 适用于做题, 局面已经初始化好了.或者让子棋的局面
	 * 
	 * @param board
	 */
	public GoBoard(byte[][] board) {
		super(board.length - 2);

		initState = new byte[board.length][board.length];
		byte i, j;
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				getBoardPoint(i, j).setColor(board[i][j]);
				this.initState[i][j] = board[i][j];
			}
		}
		generateHighLevelState();
	}

	public void output() {
		verifyFlag();
		Set<Block> allActiveBlocks = new HashSet<Block>();

		short count = 0;
		byte i, j;
		if (log.isDebugEnabled()) {
			log.debug("shoushu=" + shoushu);
		}
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				count++;
				if (allActiveBlocks.add(getBoardPoint(i, j).getBlock())) {
					if (log.isDebugEnabled()) {
						log.debug(getBoardPoint(i, j).getBlock());
					}
				}

			}

		}
		if (count != boardSize * boardSize) {
			if (log.isDebugEnabled()) {
				log.debug("number of points error" + count);
			}
		}

	}

	/**
	 * only maintain breath point, block relationship is not covered.
	 * 
	 * @deprecated
	 * @param block
	 */
	private void calculateBreath(Block block) {

		verifyFlag();
		block.clearBreath();
		for (Point point : block.getPoints()) {
			for (Delta delta : Constant.ADJACENTS) {
				Point nb = point.getNeighbour(delta);
				if (nb == null)
					continue;
				BoardPoint bp = this.getBoardPoint(nb);
				if (bp.getColor() == ColorUtil.BLANK
						&& bp.isCalculatedFlag() == false) {
					bp.setCalculatedFlag(true);
					block.addBreathPoint(nb);
				}
			}
		}

		// 恢复标志
		for (Point point : block.getBreathPoints()) {
			this.getBoardPoint(point).setCalculatedFlag(false);
		}
	}

	public boolean verify1() {
		Set<Block> blocks = new HashSet<Block>();
		byte i;
		byte j;
		short count = 0;
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				if (getBoardPoint(i, j).getBlock() == null) {

				} else if (!blocks.contains(getBoardPoint(i, j).getBlock())) {
					blocks.add(getBoardPoint(i, j).getBlock());
					count += getBoardPoint(i, j).getTotalNumberOfPoint();
					if (log.isDebugEnabled()) {
						log.debug("i=" + i + ",j=" + j + ",count=" + count
								+ ";");
					}
				}
			}
		}
		return count == boardSize * boardSize;

	}

	/**
	 * It is better to specify the color from caller side, instead of decide
	 * color according to the 手数。 since for calculation, we may not follow the
	 * principle --- black and white comes one after the other.
	 * 
	 * @param row
	 * @param column
	 * @param color
	 * @return
	 */
	// public boolean oneStepForward(final int row, final int column, int color)
	// {
	//
	// }

	/**
	 * general make Block // 因为形势判断调用时无需每次调用都清除标志，所以 // 函数本身不清除标志。
	 * 
	 * @param a
	 * @param b
	 * @param newBlock
	 * @return
	 */
	private void makeBlock(Point point, BasicBlock newBlock) {
		final int color = newBlock.getColor();
		BoardPoint bp = this.getBoardPoint(point);
		bp.setCalculatedFlag(true);
		bp.setBasicBlock(newBlock);
		// newBlock.addPoint(point);

		for (Delta delta : Constant.ADJACENTS) {
			Point neighbourPoint = point.getNeighbour(delta);
			if (neighbourPoint == null)
				continue;

			bp = this.getBoardPoint(neighbourPoint);
			if (bp.getColor() == color) {
				if (bp.isCalculatedFlag()) {
					continue;
				}
				makeBlock(neighbourPoint, newBlock);
			}
		}

	}

	/**
	 * @deprecated
	 * @param original
	 * @param blankPoints
	 */
	private void realDivideBlankPointBlock(Point original,
			List<Point> blankPoints) {
		Set<Point> points = new HashSet<Point>();
		points.addAll(blankPoints);

		// realDivideBlankPointBlock(original, points);
	}

	/**
	 * all black/white/blank blocks. because the blocks will be changed after
	 * they were put into the Set. so can not correctly remove it. so it is
	 * almost useless to collect them into a set.
	 * ----------------------------------------------------------------------
	 * the situation changed: now I adopt the reference equality strategy. the
	 * block contnet change will not impact is SET operation.
	 * 
	 */
	// Set blackAndWhiteBlocks = new HashSet();
	// Set whiteBlocks = new HashSet();
	// Set blackBlocks = new HashSet();
	// Set blankPointBlocks = new HashSet();

	/**
	 * 
	 * 从链式气块生成局面项目复制来的，已经修改。 <br/>
	 * 从棋谱的位图表示生成黑白块(Block)和空白块以及他们之间相邻关系的信息<br/>
	 * 该函数应该在构造函数中调用(初始化局面)。 <br/>
	 * add function to scan the state! and use it to compare with * the
	 * incremental calculation result.
	 * 
	 * @author eddie
	 * 
	 * 
	 */
	private void generateHighLevelState() {
		this.verifyFlag();
		Set<Block> blackBlocks = new HashSet<Block>();
		Set<Block> whiteBlocks = new HashSet<Block>();
		Set<BlankBlock> blankPointBlocks = new HashSet<BlankBlock>();
		Set<Block> blackAndWhiteBlocks = new HashSet<Block>();

		if (log.isInfoEnabled()) {
			log.info("come into method generateHighLevelState");

		}
		generateAllKindsBlocks(blackBlocks, whiteBlocks, blankPointBlocks);
		blackAndWhiteBlocks.addAll(whiteBlocks);
		blackAndWhiteBlocks.addAll(blackBlocks);

		clearFlag();
		for (Block block : blackAndWhiteBlocks) {
			verifyBreath(block);
		}

		this.clearFlag();

		/**
		 * for higher level data structure.
		 */
		Point nb;
		BoardPoint bp;
		// 遍历所有黑白块.生成相邻关系
		for (Block block : blackAndWhiteBlocks) {
			// 遍历块中的点
			for (Point point : block.getPoints()) {
				// if (point == Point.getPoint(8, 3)) {
				// if(log.isDebugEnabled()) log.debug("");
				// }
				for (Delta delta : Constant.ADJACENTS) {
					nb = point.getNeighbour(delta);
					if (nb == null)
						continue;
					bp = this.getBoardPoint(nb);
					// if (bp.isCalculatedFlag())
					// continue;

					if (getColor(nb) == block.getEnemyColor()) {
						block.addEnemyBlock_twoWay(getBlock(nb));
					} else if (getColor(nb) == Constant.BLANK) {
						block.addBreathPoint(nb);
						block.addBreathBlock_twoWay(this.getBlankBlock(nb));
					}

				}
			}

		}

		if (log.isInfoEnabled()) {
			log.info("return from method generateHighLevelState");
		}
		// this.clearFlag();
	}

	/**
	 * 第一遍扫描，生成块,包括棋块和气块<br/>
	 * scan the goboard to generate all three kinds of blocks.
	 * 
	 * precondition: the color of board point is set already. result: the point
	 * in the same block has point to same Block.
	 */
	private void generateAllKindsBlocks(Set<Block> blackBlocks,
			Set<Block> whiteBlocks, Set<BlankBlock> blankPointBlocks) {
		this.verifyFlag();

		if (log.isDebugEnabled()) {
			log.debug("generateAllKindsBlocks");
		}
		int myColor;
		BoardPoint bp;

		for (Point point : Point.getAllPoints(boardSize)) {
			bp = this.getBoardPoint(point);

			if (bp.isCalculatedFlag()) {// 处理过的标志.
				continue;
			} else {
				bp.setCalculatedFlag(true);
			}

			myColor = bp.getColor();
			if (log.isDebugEnabled()) {
				log.debug("color: " + myColor + "point " + point);
			}
			if (myColor == ColorUtil.BLACK) {
				makeBlock(point, new Block(ColorUtil.BLACK));
				blackBlocks.add(getBlock(point));
			} else if (myColor == ColorUtil.WHITE) {
				makeBlock(point, new Block(ColorUtil.WHITE));
				whiteBlocks.add(getBlock(point));
				if (log.isDebugEnabled()) {
					log.debug("added block:" + getBlock(point));
				}
			} else if (myColor == ColorUtil.BLANK) {
				makeBlock(point, new BlankBlock());
				blankPointBlocks.add(getBlankBlock(point));
				if (log.isDebugEnabled()) {
					log.debug("added block:" + getBlock(point));
				}
			} else {
				throw new RuntimeException("err generateAllKindsBlocks");
			}

		}
		this.clearFlag();
	}

	public void changeColorForAllPoints(Block oldBlock, Block newBlock) {

	}

	/**
	 * clone to keep the state in history. avoid the backforward operation.
	 * 
	 * in order to avoid backward. we use the cloned object to store history
	 * data. but maybe it is too difficult to clone the GoBoard because there is
	 * a circle reference.
	 */
	// public Object clone() {
	// GoBoard temp = null;
	// byte i, j, k;
	// short t;
	// try {
	// temp = (GoBoard) (super.clone());
	// } catch (CloneNotSupportedException de) {
	// de.printStackTrace();
	// }
	// // temp.boardPoints=(BoardPoint[][])boardPoints.clone();
	// boardPoints = new BoardPoint[21][21];
	// for (i = Constant.ZBXX; i <= Constant.ZBSX; i++) { // 2月22日加
	// // points[Constant.ZBXX][i] = new Point(Constant.ZBXX, i);
	// // points[Constant.ZBSX][i] = new Point(Constant.ZBSX, i);
	// // points[i][Constant.ZBXX] = new Point(i, Constant.ZBXX);
	// // points[i][Constant.ZBSX] = new Point(i, Constant.ZBSX);
	// boardPoints[Constant.ZBXX][i] = new BoardPoint(null,
	// (byte) ColorUtil.OutOfBoard);
	// boardPoints[Constant.ZBSX][i] = new BoardPoint(null,
	// (byte) ColorUtil.OutOfBoard);
	// boardPoints[i][Constant.ZBXX] = new BoardPoint(null,
	// (byte) ColorUtil.OutOfBoard);
	// boardPoints[i][Constant.ZBSX] = new BoardPoint(null,
	// (byte) ColorUtil.OutOfBoard);
	// }
	// for (i = 1; i <= Constant.BOARD_SIZE; i++) {
	// for (j = 1; j <= Constant.BOARD_SIZE; j++) {
	// temp.getBoardPoint(i,j) = (BoardPoint) getBoardPoint(i,j).clone();
	// }
	// }
	//
	// return temp;
	//
	// }

	/**
	 * The clone done by serialization/deserialization
	 * 原先的想法是通过clone来复制局面，避免重新生成局面的数据结构。<br/>
	 * 现在觉得这样性能未必好（通用序列化框架实现带来性能开销），而且代码复杂，不好维护。
	 * 
	 * @deprecated during clone, the Block of boradPoint is initial
	 * @return
	 */
	public GoBoard deepClone() {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();

			ObjectOutputStream out = new ObjectOutputStream(b);

			out.writeObject(this);
			ByteArrayInputStream bIn = new ByteArrayInputStream(b.toByteArray());

			ObjectInputStream oi = new ObjectInputStream(bIn);

			return ((GoBoard) oi.readObject());
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("exception:" + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public boolean equals(Object object) {
		if (object instanceof GoBoard) {
			GoBoard goBoard = (GoBoard) object;
			for (int row = 1; row <= boardSize; row++) {
				for (int column = 1; column <= boardSize; column++) {
					if (getBoardPoint(row, column) == null) {
						if (goBoard.getBoardPoint(row, column) != null) {
							if (log.isDebugEnabled())
								log.debug("null==false at [row=" + row
										+ ", column=" + column + "]");
							return false;
						}
					} else if (!getBoardPoint(row, column).equals(
							goBoard.getBoardPoint(row, column))) {
						if (log.isDebugEnabled())
							log.debug("equal==false at [row=" + row
									+ ", column=" + column + "]");
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}

	}

	// public GoBoard getGoBoardCopy() {
	// GoBoard temp = new GoBoard(this.getBoardColorState(), this.getShoushu());
	// temp.setStepHistory(this.getStepHistory().getCopy());
	// temp.generateHighLevelState();
	// temp.lastPoint = this.lastPoint;
	// return temp;
	// }

	/**
	 * option 1: UI display is decided by GoBoard.
	 * 
	 * @TODO
	 * @return
	 */
	public List<UIPoint> getUIPoints() {
		List<UIPoint> list = new ArrayList<UIPoint>();
		UIPoint point;
		BoardPoint bPoint;
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				bPoint = getBoardPoint(i, j);
				if (bPoint.getColor() == Constant.BLACK) {
					point = new UIPoint();
					point.setColor(Constant.BLACK);
					point.setPoint(bPoint.getPoint());
				} else if (getBoardPoint(i, j).getColor() == Constant.WHITE) {

				}
			}

		}
		return list;
	}

	/**
	 * 判断一个点是否是虎口。
	 * 
	 * @param point
	 * @return
	 */
	public boolean isTigerMouth(Point point) {
		int black = 0;
		int white = 0;
		int blank = 0;
		int outOfBound = 0;
		if (this.getBoardPoint(point).getColor() == ColorUtil.BLANK) {
			for (Delta delta : Constant.ADJACENTS) {
				Point neighbourPoint = point.getNeighbour(delta);
				if (neighbourPoint == null) {
					outOfBound++;
					continue;
				}

				if (this.getColor(neighbourPoint) == ColorUtil.BLACK)
					black++;
				else if (this.getColor(neighbourPoint) == ColorUtil.WHITE)
					white++;
				else if (this.getColor(neighbourPoint) == ColorUtil.BLANK)
					blank++;
				else
					outOfBound++;
			}
			if (blank == 1) {
				if (black + outOfBound == 3) {
					return true;
				} else if (white + outOfBound == 3) {
					return true;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * 仅仅能够识别单子眼，多子眼位会被识别成虎口！ （大眼死活计算可以够用！）
	 * 
	 * @param point
	 * @return
	 */
	public boolean isEye(Point point) {
		int black = 0;
		int white = 0;
		int blank = 0;
		int outOfBound = 0;
		if (this.getBoardPoint(point).getColor() == ColorUtil.BLANK) {
			for (Delta delta : Constant.ADJACENTS) {
				Point neighbourPoint = point.getNeighbour(delta);
				if (neighbourPoint == null) {
					outOfBound++;
					continue;
				}

				if (this.getColor(neighbourPoint) == ColorUtil.BLACK)
					black++;
				else if (this.getColor(neighbourPoint) == ColorUtil.WHITE)
					white++;
				else if (this.getColor(neighbourPoint) == ColorUtil.BLANK) {
					blank++;
					break;
				} else
					outOfBound++;
			}
			if (blank == 0) {
				if (black + outOfBound == 4) {
					return true;
				} else if (white + outOfBound == 4) {
					return true;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * 已知不会提子。<br/>
	 * 事先判断某处落子是否可以形成虎口。
	 * 
	 * @param original
	 * @param color
	 * @return
	 */
	public Set<Point> tigerMouthAfterPlay(Point original, int color) {
		if (this.getColor(original) != Constant.BLANK) {
			throw new RuntimeException("getColor(original)!=Constant.BLANK");
		}
		this.setColor(original, color);
		Set<Point> tigerMouth = new HashSet<Point>();
		for (Delta delta : Constant.ADJACENTS) {
			Point neighbourPoint = original.getNeighbour(delta);
			if (neighbourPoint == null) {
				continue;
			}
			if (this.getColor(neighbourPoint) != Constant.BLANK)
				continue;
			if (this.isTigerMouth(neighbourPoint)) {
				tigerMouth.add(neighbourPoint);
			}
		}
		this.setColor(original, Constant.BLANK);
		return tigerMouth;
	}

	

	/**
	 * 已知不会提子。<br/>
	 * 事先判断某处落子是否可以眼，及其数量。
	 * 
	 * @param original
	 * @param color
	 * @return
	 */
	public Set<Point> EyesAfterPlay(Point original, int color) {
		if (this.getColor(original) != Constant.BLANK) {
			throw new RuntimeException("getColor(original)!=Constant.BLANK");
		}
		this.setColor(original, color);
		Set<Point> eyes = new HashSet<Point>();
		for (Delta delta : Constant.ADJACENTS) {
			Point neighbourPoint = original.getNeighbour(delta);
			if (neighbourPoint == null) {
				continue;
			}
			if (this.getColor(neighbourPoint) != Constant.BLANK)
				continue;
			if (this.isEye(neighbourPoint)) {
				eyes.add(neighbourPoint);
			}
		}
		this.setColor(original, Constant.BLANK);
		return eyes;
	}


	/**
	 * 暂时决定将相邻点都计入breath shape.<br/>
	 * 觉得此路不通.不够通用,花了很多力气,又没有效果.但是作为搜索的终止节点.<br/>
	 * 也许需要这样短小精悍的只是总结.
	 * 
	 * @param block
	 * @return
	 */
	public BreathPattern getBreathPattern(BlankBlock block) {
		Shape shape = block.getShape();
		byte[][] pattern;
		pattern = new byte[shape.getDeltaX() + 2][shape.getDeltaY() + 2];
		// for (Point point : block.getAllPoints()) {
		// pattern[point.getRow() - shape.getMinX()][point.getColumn()
		// - shape.getMinY()] = ColorUtil.BREATH;
		// }
		for (int i = shape.getMinX() - 1; i <= shape.getMaxX() + 1; i++) {
			for (int j = shape.getMinY() - 1; j <= shape.getMaxY() + 1; j++) {
				if (Point.isNotValid(boardSize, i, j)) {
					pattern[i - shape.getMinX() - 1][j - shape.getMinY() - 1] = ColorUtil.OutOfBoard;
				} else {
					pattern[i - shape.getMinX() - 1][j - shape.getMinY() - 1] = (byte) this
							.getColor(i, j);
				}
			}
		}

		return new BreathPattern(pattern);
	}

	public byte[][] getInitState() {
		return initState;
	}

	/**
	 * 3. 点眼的选择,点入后气数多为好.
	 */
	public List<Point> getBrokenPoint(BlankBlock eyeBlock) {
		List<Point> candidates = new ArrayList<Point>();

		List<BlankPoint> points = new ArrayList<BlankPoint>();
		for (Point point : eyeBlock.getPoints()) {
			int breath = 0;
			for (Delta delta : Constant.ADJACENTS) {
				Point nb = point.getNeighbour(delta);
				if (nb == null)
					continue;
				if (this.getColor(nb) == ColorUtil.BLANK) {
					breath++;
				}
			}
			points.add(new BlankPoint(point, breath));

		}
		// sort by breath;
		Collections.sort(points, new BlankPointBreathComparator());
		Collections.reverse(points);
		for (BlankPoint point : points) {
			candidates.add(point.getPoint());
		}
		return candidates;
	}

	/**
	 * get all the eaten points in this step.
	 */
	@Override
	public Set<Point> getEatenPoints() {
		Set<Block> eatenBlocks = getStepHistory().getStep(shoushu - 1)
				.getEatenBlocks();
		// if (eatenBlocks.isEmpty() == false) {
		// return null;
		// }
		Set<Point> points = new HashSet<Point>();
		for (Block block : eatenBlocks) {
			points.addAll(block.allPoints);

		}

		return points;

	}

	public boolean isValidState() {
		for (Block block : this.getBlackWhiteBlock()) {
			if (block.getBreaths() == 0)
				return false;
		}
		return true;
	}
}
