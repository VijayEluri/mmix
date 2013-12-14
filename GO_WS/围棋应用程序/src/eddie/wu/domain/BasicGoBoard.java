package eddie.wu.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.survive.BreathPattern;
import eddie.wu.domain.survive.CoreBreathPattern;

/**
 * 这里主要负责初始化基本的数据结构 <br/>
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
abstract public class BasicGoBoard extends AbsGoBoard {
	private transient static final Logger log = Logger
			.getLogger(BasicGoBoard.class);

	/**
	 * 初始局面
	 */
	protected BoardColorState initColorState;
	protected SymmetryResult initSymmetryResult;

	/**
	 * no double check really used Constructor. ，<br/>
	 * 适用于做题, 局面已经初始化好了.或者让子棋的局面
	 */
	public BasicGoBoard(BoardColorState boardState) {
		this(boardState, 0);

	}

	/**
	 * no double check really used Constructor.
	 * 
	 */
	public BasicGoBoard(BoardColorState boardState, int numberOfSteps) {

		super(boardState.boardSize);
//		super.s
		int matrixSize = boardSize + 2;
		boardPoints = new BoardPoint[matrixSize][matrixSize];
		this.shoushu = numberOfSteps;
		initColorState = boardState;
		initSymmetryResult = initColorState.getSymmetryResult();
		int row, column;
		BoardPoint boardPoint;
		for (row = 1; row <= boardSize; row++) {
			for (column = 1; column <= boardSize; column++) {
				Point point = Point.getPoint(boardSize, row, column);
				boardPoint = new BoardPoint(point);
				this.setBoardPoint(boardPoint);

			}
		}

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
	 * 空枰开局，黑方先行
	 * 
	 * @param boardSize
	 */
	public BasicGoBoard(int boardSize) {
		super(boardSize);
		int matrixSize = boardSize + 2;
		boardPoints = new BoardPoint[matrixSize][matrixSize];
		// initState = new byte[matrixSize][matrixSize];
		initColorState = new BoardColorState(boardSize, Constant.BLACK);
		initSymmetryResult = initColorState.getSymmetryResult();
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

	/**
	 * 得到一块棋的内气，以及内气边的敌子，有时只有一个纯的内眼。<br/>
	 * 尽管通过记住原先纯眼位的位置可以做到，但是仍有必要从头识别，<br/>
	 * 因为有时候只有当前局面，没有历史。
	 * 
	 * @return
	 */
	public CoreBreathPattern getBigEyeBreathPattern(Point target) {
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

	/**
	 * 适用于做题, 局面已经初始化好了.或者让子棋的局面
	 * 
	 * @param board
	 */
	// public BasicGoBoard(byte[][] board, int whoseTurn) {
	// this.boardSize = board.length;
	// int matrixSize = boardSize + 2;
	// boardPoints = new BoardPoint[matrixSize][matrixSize];
	//
	// initState = new byte[board.length][board.length];
	// byte i, j;
	// for (i = 1; i <= boardSize; i++) {
	// for (j = 1; j <= boardSize; j++) {
	// getBoardPoint(i, j).setColor(board[i][j]);
	// this.initState[i][j] = board[i][j];
	// }
	// }
	// generateHighLevelState();
	// // this
	// }

	/**
	 * 暂时决定将相邻点都计入breath shape.<br/>
	 * 觉得此路不通.不够通用,花了很多力气,又没有效果.但是作为搜索的终止节点.<br/>
	 * 也许需要这样短小精悍的只是总结.
	 * 
	 * @param eyeBlock
	 * @return
	 */
	public BreathPattern getBreathPattern(BlankBlock eyeBlock) {
		return this.getBreathPattern(eyeBlock);
	}

	public BreathPattern getBreathPattern(Set<Point> eyeBlock) {
		Shape shape = Shape.getShape(eyeBlock);
		
		Set<Point> extCycle = new HashSet<Point>();
		for(Point point: eyeBlock){
			for(Delta delta: Constant.ADJACENTS){
				Point temp = point.getNeighbour(delta);
				if(temp==null) continue;
				if(eyeBlock.contains(temp)) continue;
				extCycle.add(temp);
			}
		}
		
		byte[][] pattern;
		pattern = new byte[shape.getDeltaX() + 1 + 2][shape.getDeltaY() + 1 + 2];
		// for (Point point : block.getAllPoints()) {
		// pattern[point.getRow() - shape.getMinX()][point.getColumn()
		// - shape.getMinY()] = ColorUtil.BREATH;
		// }
		if(log.isEnabledFor(Level.WARN)) log.warn(shape);
		for (int i = shape.getMinX() - 1; i <= shape.getMaxX() + 1; i++) {
			for (int j = shape.getMinY() - 1; j <= shape.getMaxY() + 1; j++) {
//				if(log.isEnabledFor(Level.WARN)) log.warn("i=" + i);
//				if(log.isEnabledFor(Level.WARN)) log.warn("j=" + j);
				int relativeI = i - (shape.getMinX() - 1);
				int relativeJ = j
						- (shape.getMinY() - 1);
				if (Point.isNotValid(boardSize, i, j)) {
					// pattern[i ][j ] = ColorUtil.OutOfBoard;
					pattern[relativeI][relativeJ] = ColorUtil.OutOfBoard;
				} else if(eyeBlock.contains(Point.getPoint(boardSize, i, j))){
					pattern[relativeI][relativeJ] = (byte) this.getColor(i,
							j);
				} else if(extCycle.contains(Point.getPoint(boardSize, i, j))){
					pattern[relativeI][relativeJ] = (byte) this.getColor(i,
							j);
				}else{
					pattern[relativeI][relativeJ] = ColorUtil.irrelevant;
				}
			}
		}

		return new BreathPattern(pattern);
	}

	public CoreBreathPattern getBreathPattern(Shape shape) {
		BlankBlock block;

		byte[][] key = new byte[shape.getDeltaX() + 1][shape.getDeltaY() + 1];
		// 取决于是否在边角，相关逻辑在小棋盘上需要调整。直三可能minx=1,max=2.
		int matrixA = shape.getDeltaX() + 1;
		int matrixB = shape.getDeltaY() + 1;
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
				pattern[row - startX][column - startY] = (byte) this.getColor(
						row, column);
			}
		}
		// set pattern
		// for (Point point : block.getPoints()) {
		// pattern[point.getRow() - shape.getMinX()][point.getColumn()
		// - shape.getMinY()] = ColorUtil.BREATH;
		// }
		return new CoreBreathPattern(pattern, key);
	}

	public byte[][] getInitState() {
		return this.initColorState.getMatrixState();
	}
	
	public int getInitTurn(){
		return this.initColorState.getWhoseTurn();
	}

	/**
	 * return by parameter state.
	 * 
	 * @param state
	 */
	public void getInitState(byte[][] state) {
		this.initColorState.getMatrixState(state);
	}

	/**
	 * 计算单子眼位周围棋块的最小气数。
	 * 
	 * @param eye
	 * @return
	 */
	public int getMimBreaths(Point eye) {
		int min = 128;
		int breath = 0;
		for (Delta delta : Constant.ADJACENTS) {
			Point point = eye.getNeighbour(delta);
			if (point == null)
				continue;
			breath = this.getBlock(point).getBreaths();
			if (breath < min) {
				min = breath;
			}

		}
		return min;
	}

	/**
	 * 
	 * really used Constructor.
	 * 
	 */
	// public BasicGoBoard(BoardColorState boardState, short numberOfSteps) {
	// boolean isBlackTurn = boardState.getWhoseTurn()==Constant.BLACK;
	// this(boardState, (int) numberOfSteps);
	// if (numberOfSteps % 2 == 0) {
	// if (isBlackTurn) {
	// this.shoushu = numberOfSteps;
	// } else {
	// throw new RuntimeException("whose turn is not right");
	// }
	// } else {
	// if (isBlackTurn) {
	// throw new RuntimeException("whose turn is not right");
	// } else {
	// this.shoushu = numberOfSteps;
	// }
	// }
	//
	// }

	public boolean isValidState() {
		for (Block block : this.getBlackWhiteBlocksOnTheFly()) {
			if (block.getBreaths() == 0)
				return false;
		}
		return true;
	}

	public void output() {
		verifyFlag();
		Set<Block> allActiveBlocks = new HashSet<Block>();
		Set<BlankBlock> breathBlocks = new HashSet<BlankBlock>();
		short count = 0;
		byte i, j;
		if (log.isDebugEnabled()) {
			log.debug("shoushu=" + shoushu);

			for (i = 1; i <= boardSize; i++) {
				for (j = 1; j <= boardSize; j++) {
					count++;
					BoardPoint boardPoint = getBoardPoint(i, j);
					if (boardPoint.getColor() == Constant.BLANK) {
						if (breathBlocks.add(boardPoint.getBlankBlock())) {
							log.debug(boardPoint.getBlankBlock());
						}
					} else if (allActiveBlocks.add(boardPoint.getBlock())) {
						log.debug(boardPoint.getBlock());
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
				log.debug("color: " + myColor + " at point " + point);
			}
			if (myColor == ColorUtil.BLACK) {
				makeBlock(point, new Block(ColorUtil.BLACK));
				blackBlocks.add(getBlock(point));
				if (log.isDebugEnabled()) {
					log.debug("added Black block: " + getBlock(point) + " at "
							+ point);
				}
			} else if (myColor == ColorUtil.WHITE) {
				makeBlock(point, new Block(ColorUtil.WHITE));
				whiteBlocks.add(getBlock(point));
				if (log.isDebugEnabled()) {
					log.debug("added block:" + getBlock(point) + " at " + point);
				}
			} else if (myColor == ColorUtil.BLANK) {
				makeBlock(point, new BlankBlock());
				blankPointBlocks.add(getBlankBlock(point));
				if (log.isDebugEnabled()) {
					log.debug("added block:" + getBlankBlock(point) + " at "
							+ point);
				}
			} else {
				throw new RuntimeException("err generateAllKindsBlocks");
			}

		}
		this.clearFlag();
	}

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
			this.calculateBreath(block);
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
	 * calculate breath and maintain relation ship between blocks
	 * 
	 * @param block
	 */
	protected void calculateBreath(Block block) {
		Set<Point> breaths = block.getBreathPoints();

		verifyFlag();
		if (log.isDebugEnabled()) {
			log.debug("进入方法：计算块气calculateBreath()");
		}
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
					if (log.isDebugEnabled()) {
						log.debug("add breath " + nb + " for block:"
								+ block.getTopLeftPoint());
					}
					block.addBreathPoint(nb);
					block.addBreathBlock_twoWay(this.getBlankBlock(nb));
				}
			}

		}

		// 恢复标志
		for (Point point : block.getBreathPoints()) {
			log.debug("restore flag of" + point);
			this.getBoardPoint(point).setCalculatedFlag(false);
		}
		if (log.isDebugEnabled()) {
			log.debug("块的气数为：" + block.getBreaths());
		}

		if (log.isDebugEnabled()) {
			log.debug("方法jskq：返回");
		}
		if (!breaths.equals(block.getBreathPoints())) {
			if (log.isDebugEnabled())
				log.debug("original breath is" + breaths);
			if (log.isDebugEnabled())
				log.debug("new calculated breath is" + block.getBreathPoints());
			throw new RuntimeException("气数计算有误!");
		}

	} // 2月22日改,原方法虽妙,仍只能忍痛割爱.

	public Point getPoint(int row, int column) {
		return Point.getPoint(boardSize, row, column);
	}

	public SymmetryResult getInitSymmetryResult() {
		return initSymmetryResult;
	}

	/**
	 * The standard rule to calculate the result. suppose all the result are
	 * finalized.<br/>
	 * 判断胜负，但要求全部死子都已经提走。<br/>
	 * 这里只是简单的根据每个点的颜色来决定归属。<br/>
	 * 适合双虚手终局的情形。
	 * 
	 * @return
	 */
	public FinalResult finalResult_deadCleanedUp() {
		int black = 0;
		int white = 0;
		int shared = 0;

		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				if (getColor(i, j) == Constant.BLACK)
					black++;
				else if (getColor(i, j) == Constant.WHITE)
					white++;
				else {
					BlankBlock block = this.getBlankBlock(i, j);
					if (block.isEyeBlock()) {
						if (block.isBlackEye()) {
							black++;
							// black += block.getNumberOfPoint(); BUG
						} else {
							white++;
							// white += block.getNumberOfPoint(); BUG
						}
					} else {
						shared++;
						// shared += block.getNumberOfPoint(); BUG
					}
				}
			}
		}
		if (black + white + shared != boardSize * boardSize)
			throw new RuntimeException("black+white+shared="
					+ (black + white + shared) + "!=boardSize*boardSize");
		FinalResult res = new FinalResult(black, white, shared);
		return res;
	}

}
