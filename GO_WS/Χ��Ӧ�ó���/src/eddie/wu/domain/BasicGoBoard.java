package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.comp.BlockRowColumnComparator;

/**
 * since there are many code share the simple data structure, current idea is to
 * distribute the code to class hierarchy. so make data protected to ease
 * sharing.<br/>
 * design change: internally the matrix is indexed from 0. but on the surface,
 * it is indexed from 1 to be compatible with human convention when talking
 * about the position of each stone(Point).<br/>
 * 
 * @author Eddie
 * 
 */
public class BasicGoBoard {
	private transient static final Logger log = Logger
			.getLogger(BasicGoBoard.class);

	public final int boardSize;
	// public final int middleLine;// keep it same as index from 1.

	/**
	 * 两维是棋盘的坐标,数组下标从1到19; 气块和子块分开，气块是子块的附属。
	 */
	private BoardPoint[][] boardPoints;
	/**
	 * 当前已有手数,处理之前递增.从1开始;
	 */
	protected int shoushu = 0;

	public BasicGoBoard(int boardSize) {
		this.boardSize = boardSize;
		/**
		 * change to be same as board size
		 */
		int matrixSize = boardSize + 2;

		boardPoints = new BoardPoint[matrixSize][matrixSize];

		byte row, column;
		BoardPoint boardPoint;
		BlankBlock originalBlankBlock = new BlankBlock();
		// 初始局面就是一个361点的空白块.
		for (row = 1; row <= boardSize; row++) {
			for (column = 1; column <= boardSize; column++) {
				Point point = Point.getPoint(boardSize, row, column);
				boardPoint = new BoardPoint(point);
				this.setBoardPoint(boardPoint);
				boardPoint.setBlankBlock(originalBlankBlock);

			}
		}
		if (log.isDebugEnabled()) {
			log.debug("originalBlankBlock" + originalBlankBlock);
		}
	}

	protected int getBreaths(Point point) {
		return this.getBoardPoint(point).getBreaths();

	}

	/**
	 * 
	 * @param row
	 * @param column
	 * @return null if the point in blank
	 */
	public Block getBlock(int row, int column) {
		return getBoardPoint(row, column).getBlock();
	}

	public BlankBlock getBlankBlock(int row, int column) {
		return getBoardPoint(row, column).getBlankBlock();
	}

	public Block getBlock(Point point) {
		return this.getBoardPoint(point).getBlock();
	}

	public BlankBlock getBlankBlock(Point point) {
		return this.getBoardPoint(point).getBlankBlock();
	}

	public BasicBlock getBasicBlock(Point point) {
		return this.getBoardPoint(point).getBasicBlock();
	}

	public void setBlock(Point point, Block block) {
		getBoardPoint(point).setBlock(block);
	}

	public void setBlock(Point point, BlankBlock block) {
		getBoardPoint(point).setBlankBlock(block);
	}

	public BlankBlock getBreathBlock(Point point) {
		return this.getBoardPoint(point).getBlankBlock();
	}

	public BoardColorState getBoardColorState() {
		BoardColorState boardState = new BoardColorState(boardSize);
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				boardState.add(getBoardPoint(i, j));
			}
		}
		return boardState;
	}

	/**
	 * get black blocks form current state structure.
	 * 
	 * @return Set
	 */
	public Set<Block> getBlackBlocks() {

		return getSetFromState(ColorUtil.BLACK);
	}

	public Set<Block> getBlackWhiteBlock() {
		Set<Block> blocks = new HashSet<Block>();
		blocks.addAll(this.getSetFromState(Constant.BLACK));
		blocks.addAll(this.getSetFromState(Constant.WHITE));
		return blocks;
	}

	/**
	 * get blank point blocks form current state structure.
	 * 
	 * @return Set
	 */
	public Set<BlankBlock> getBlankBlocks() {
		return getBlankSetFromState();
	}

	public Set<Block> getSetFromState(int color) {
		Set<Block> blocks = new HashSet<Block>();
		Block temp = null;
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				if (getBoardPoint(i, j).getColor() == color) {
					temp = getBoardPoint(i, j).getBlock();
					if (temp.getPoints().isEmpty()) {
						if (log.isDebugEnabled())
							log.debug("debug! temp.getPoints().isEmpty()");
					}
					blocks.add(temp);
				}
			}
		}
		// log.debug("return from getBlackBlocksFromState");
		return blocks;

	}

	public Set<BlankBlock> getBlankSetFromState() {
		Set<BlankBlock> blocks = new HashSet<BlankBlock>();
		BlankBlock temp = null;
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				if (getColor(i, j) == ColorUtil.BLANK) {
					temp = getBoardPoint(i, j).getBlankBlock();
					if (temp.getPoints().isEmpty()) {
						if (log.isDebugEnabled())
							log.debug("empty blank block --- debug!");
					}
					blocks.add(temp);
				}
			}
		}
		// log.debug("return from getBlackBlocksFromState");
		return blocks;

	}

	/**
	 * transfer into bi-dimension array
	 * 
	 * @return
	 */
	public byte[][] getBreathArray() {
		int matrixSize = this.boardSize + 2;
		byte[][] a = new byte[matrixSize][matrixSize];
		byte i, j, k;
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				if (getBoardPoint(i, j).getColor() == ColorUtil.BLACK
						|| getBoardPoint(i, j).getColor() == ColorUtil.WHITE) {
					a[i][j] = (byte) getBoardPoint(i, j).getBreaths();
				}
			}
		}
		return a;
	}

	public BoardBreathState getBoardBreathState() {
		return new BoardBreathState(this.getBreathArray());
	}

	/**
	 * get white blocks form current state structure.
	 * 
	 * @return Set
	 */
	public Set<Block> getWhiteBlocks() {
		return getSetFromState(ColorUtil.WHITE);
	}

	public int getShoushu() {
		return shoushu;
	}

	public void setShoushu(int shoushu) {
		this.shoushu = shoushu;
	}

	public int getColor(Point point) {
		return this.getBoardPoint(point).getColor();
	}

	public int getColor(int row, int column) {
		return getBoardPoint(row, column).getColor();
	}

	public void setColor(Point point, int color) {
		getBoardPoint(point).setColor(color);
	}

	public void setColor(int row, int column, int color) {
		getBoardPoint(row, column).setColor(color);
	}

	public boolean validate(final byte row, final byte column) {
		return validate(Point.getPoint(boardSize, row, column), 0);

	}

	/**
	 * 判断落子位置的有效性。没有副作用 <br/>
	 * 
	 * no side effect
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean validate(Point original, int color) {

		// 下标合法,该点空白
		if (this.getColor(original) == ColorUtil.BLANK) {
			if (this.getBoardPoint(original).getProhibitStep() == (shoushu + 1)) {

				log.warn("这是打劫时的禁着点,请先找劫材!");
				log.warn("落点为：a=" + original);
				return false;
			} else {
				int selfColor = ColorUtil.getNextStepColor(shoushu);
				if (color != 0) {
					selfColor = (byte) color;
				}

				return this.getNeighborState(original, selfColor).isValid();

			}
		} else { // 第一类不合法点.
			if (log.isDebugEnabled()) {
				log.debug("该点不合法,在棋盘之外或者该点已经有子：");
			}
			return false;
		}
	}

	/**
	 * 不仅计算落子点周围的空白点数.还计算气的增加情况. 可以提供Candidate需要的信息.
	 * 
	 * @param original
	 * @param color
	 * @return
	 */
	public NeighborState getNeighborState(Point original, int color) {
		NeighborState state = new NeighborState();
		state.setOriginal(original);

		int enemyColor = ColorUtil.enemyColor(color);
		state.setFriendColor(color);
		state.setEnemyColor(enemyColor);
		state.setOriginalBlankBlock(this.getBlankBlock(original));

		int enemy = 0;
		int friend = 0;
		int blank = 0;
		for (Delta delta : Constant.ADJACENTS) {
			Point nb = original.getNeighbour(delta);
			if (nb == null) {
				continue;
			} else if (this.getColor(nb) == ColorUtil.BLANK) {// 落子点周围有空白点
				blank++;
				state.addBlankPoint(nb);
				state.setValid(true);
			} else if (getColor(nb) == enemyColor) {
				enemy++;
				state.addEnemyBlock(getBlock(nb));
				if (this.getBreaths(nb) == 1) {// 落子点周围有敌子可提.
					state.setCapturing(true);
					state.setValid(true);
				}
			} else if (getColor(nb) == color) {
				friend++;
				state.addFriendBlock(getBlock(nb));
				if (getBreaths(nb) > 1) {
					state.setValid(true);
				}
			}
		}
		state.setBlank(blank);
		state.setEnemy(enemy);
		state.setFriend(friend);

		if (state.isValid()) {
			if (state.isCapturing()) { // 有提子
				if (blank == 0) {
					if (state.getEnemyBlocks().size() == 1) {

						if (state.getEnemyBlocks().iterator().next()
								.getNumberOfPoint() == 1) {
							if (friend == 0)
								log.info("提劫 " + original);
							else
								log.info("提一子 " + original);
						} else {
							log.info("提一块棋 - 两子或以上 " + original);
						}
					} else {
						log.info("提两块棋或以上 " + original);
					}
					// 被倒扑的识别.
				} else {
					// 两气或者两气以上
				}
			} else { // 没有提子
				if (friend > 0) {
					Set<Point> breaths = new HashSet<Point>();
					for (Block block : state.getFriendBlocks()) {
						breaths.addAll(block.getBreathPoints());
						/**
						 * 考虑相邻子能否长气,如果可以就不必送吃了.
						 */

					}
					breaths.remove(original);
					breaths.addAll(state.getBlankPoints());
					if (breaths.size() == 1) {
						/**
						 * 多子送吃,仍有可能是破眼.<br/>
						 * 要考虑对方是否必须送吃.
						 */
						state.setGift(true);
						log.info("多子送吃 " + original);
					}
				} else if (blank == 1) {
					/**
					 * 单子送吃,未必不利,比如倒扑.即使不是倒扑,也可能是破眼.(
					 */
					state.setGift(true);
					log.info("单子送吃 " + original);
				}
				//
			}
		} else {// 自提不入气点
			if (log.isInfoEnabled()) {
				log.info("自提不入气点: " + original);
			}
		}

		return state;
	}

	/**
	 * 
	 * @param step
	 * @return
	 */
	public Set<Point> breathAfterPlay(Point original, int color) {
		Set<Point> breaths = new HashSet<Point>();
		int enemyColor = ColorUtil.enemyColor(color);
		for (Delta delta : Constant.ADJACENTS) {
			Point nb = original.getNeighbour(delta);
			if (nb == null) {
				continue;
			}

			BoardPoint bp = this.getBoardPoint(nb);
			if (bp.getColor() == ColorUtil.BLANK) {// 落子点周围有空白点
				breaths.add(nb);
			} else if (bp.getColor() == enemyColor) {
				if (this.getBreaths(nb) == 1) {// 落子点周围有敌子可提.
					// 送吃的情况下。原有两气，有提子不可能变成一气。
					breaths.addAll(this.getBlock(nb).getPoints());
					// breaths.remove(original);
					// return 2;
				}
			} else if (bp.getColor() == color) {
				breaths.addAll(bp.getBlock().getBreathPoints());
				breaths.remove(original);
			}
		}
		return breaths;
	}
	
	

	/**
	 * now it is same as getDisplaymatrix
	 * 
	 * @return
	 */
	public byte[][] getMatrixState() {
		return getBoardColorState().getMatrixState();
	}

	/**
	 * index from 0
	 * 
	 * @return
	 */
	public char[][] getDisplayMatrixState() {
		return getBoardColorState().getDisplayMatrixState();
	}

	public void printState() {
		BoardColorState colorState = getBoardColorState();
		char[][] state = colorState.getDisplayMatrixState();
		for (int i = 0; i < state.length; i++) {
			// if(log.isDebugEnabled()) log.debug(Arrays.toString(state[i]));
			log.warn(Arrays.toString(state[i]));
		}

		// byte[][] color = colorState.getMatrixState();
	}

	/**
	 * 
	 * @param state
	 *            index from 1.
	 */
	public static void printGoBoard(byte[][] state) {
		for (int i = 1; i < state.length - 1; i++) {
			byte[] temp = Arrays.copyOfRange(state[i], 1, state.length - 1);
			if (log.isDebugEnabled())
				log.debug(Arrays.toString(temp));
		}
	}

	public List<BasicBlock> getAllBlocks() {
		Set<BasicBlock> blocks = new HashSet<BasicBlock>();
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				blocks.add(this.getBasicBlock(i, j));
			}
		}
		List<BasicBlock> list = new ArrayList<BasicBlock>(blocks.size());
		Collections.sort(list, new BlockRowColumnComparator());

		return list;
	}

	private BasicBlock getBasicBlock(int i, int j) {
		return getBoardPoint(i, j).getBasicBlock();
	}

	public boolean isBlack(Point point) {
		return this.getBoardPoint(point).getColor() == Constant.BLACK;
	}

	public boolean isBlank(Point point) {
		return this.getBoardPoint(point).getColor() == Constant.BLANK;
	}

	public boolean isWhite(Point point) {
		return this.getBoardPoint(point).getColor() == Constant.WHITE;
	}

	public Point getPoint(int row, int column) {
		return Point.getPoint(boardSize, row, column);
	}

	public int getNumberOfPoint() {
		int count = 0;
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				if (getBoardPoint(row, column).getColor() == Constant.BLACK
						|| getBoardPoint(row, column).getColor() == Constant.WHITE) {
					count++;
				}
			}
		}
		return count;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		char[][] cs = this.getDisplayMatrixState();
		for (int i = 0; i < boardSize; i++) {
			sb.append(Arrays.toString(cs[i]));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	/**
	 * index conversion related logic
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public BoardPoint getBoardPoint(int row, int column) {
		return boardPoints[row][column];
	}

	/**
	 * only internal structure will index from 0. in api level. it always index
	 * from 1 to make life easier.
	 * 
	 * @param point
	 * @return
	 */
	protected BoardPoint getBoardPoint(Point point) {
		return boardPoints[point.getRow()][point.getColumn()];
	}

	protected void setBoardPoint(BoardPoint boardPoint) {
		boardPoints[boardPoint.getRow()][boardPoint.getColumn()] = boardPoint;
	}

	/**
	 * 得到一块棋的内气，以及内气边的敌子，有时只有一个纯的内眼。<br/>
	 * 尽管通过记住原先纯眼位的位置可以做到，但是仍有必要从头识别，<br/>
	 * 因为有时候只有当前局面，没有历史。
	 * 
	 * @return
	 */
	public BreathPattern getBigEyeBreathPattern(Point target) {
		/**
		 * 大眼涉及的点，如果已经被点入，则对方的点入之子也计入。
		 */
		Set<Point> points = new HashSet<Point>();
		Block block = this.getBlock(target);
		Shape shape = block.getShape();// 成眼块的范围
		int numberOfEyeBlock = block.getNumberOfEyeBlock();
		if (numberOfEyeBlock > 1) {
			throw new RuntimeException("眼块数大于1：" + numberOfEyeBlock);
		}

		for (BlankBlock breathBlock : block.getBreathBlocks()) {
			if (breathBlock.isEyeBlock()) {
				if (shape.include(breathBlock.getShape()) == false) {
					continue;// 外眼，极其特殊情况下才有，如中央拔花之型，盘上无敌子。
				} else {
					return this.getBreathPattern(breathBlock.getShape());
					// return null;
				}
			}
			// 处理共享气块的情况。
			boolean inner = true;
			for (Block enemy : breathBlock.getNeighborBlocks()) {
				if (enemy == block)
					continue;
				if (shape.include(enemy.getShape())) {

				} else {
					inner = false;
					break;
				}
			}
			if (inner == true) {
				points.addAll(breathBlock.getPoints());
				// 将共享气块周边的敌子也计入。
				for (Block enemy : breathBlock.getNeighborBlocks()) {
					if (enemy == block)
						continue;
					points.addAll(enemy.getPoints());
				}
			}
		}

		return this.getBreathPattern(Shape.getShape(points));
		// for (Point breath : block.getBreathPoints()) {
		//
		// }
		// return null;
	}

	public BreathPattern getBreathPattern(Shape shape) {
		BlankBlock block;

		byte[][] key = new byte[shape.getDeltaX()+1][shape.getDeltaY()+1];
		// 取决于是否在边角，相关逻辑在小棋盘上需要调整。直三可能minx=1,max=2.
		int matrixA = shape.getDeltaX()+1;
		int matrixB = shape.getDeltaY()+1;
		int startX = shape.getMinX();
		int startY = shape.getMinY();
		int endX = shape.getMaxX();
		int endY = shape.getMaxY();
		if (shape.getMinX() != 1) {
			matrixA++;
			startX--;
		}
		if (shape.getMaxX() != this.boardSize) {
			matrixA++;
			endX++;
		}
		if (shape.getMinY() != 1) {
			matrixB++;
			startY--;
		}
		if (shape.getMaxY() != this.boardSize) {
			matrixB++;
			endY++;
		}
		byte[][] pattern = new byte[matrixA][matrixB];

		// set key
		for (int row = shape.getMinX(); row <= shape.getMaxX(); row++) {
			for (int column = shape.getMinY(); column <= shape.getMaxY(); column++) {
				key[row - shape.getMinX()][column - shape.getMinY()] = (byte) this
						.getColor(row, column);
			}
		}
		/**
		 * 同样是曲四，盘角曲四和中间的曲四的结果就不一样，这时光用key是不够的。
		 */

		for (int row = startX; row <= endX; row++) {
			for (int column = startY; column <= endY; column++) {
				pattern[row - startX][column - startY] = (byte) this.getColor(row,
						column);
			}
		}
		// set pattern
		// for (Point point : block.getPoints()) {
		// pattern[point.getRow() - shape.getMinX()][point.getColumn()
		// - shape.getMinY()] = ColorUtil.BREATH;
		// }
		return new BreathPattern(pattern, key);
	}
}
