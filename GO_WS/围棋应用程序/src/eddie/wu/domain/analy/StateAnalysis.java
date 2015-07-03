package eddie.wu.domain.analy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BasicBlock;
import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Delta;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Group;
import eddie.wu.domain.Point;
import eddie.wu.domain.Shape;
import eddie.wu.domain.TigerMouth;
import eddie.wu.domain.comp.BlockRowColumnComparator;
import eddie.wu.domain.survive.CoreBreathPattern;
import eddie.wu.domain.survive.SmallEye;
import eddie.wu.linkedblock.CS;

/**
 * The class hierarchy to present the state reading algorithm.
 * 
 * @author Eddie
 * 
 */
public class StateAnalysis extends GoBoard {
	public static final Logger log = Logger.getLogger(StateAnalysis.class);
	// protected byte[][] state;
	// protected BoardPoint[][] boardPoints = new BoardPoint[21][21];
	/**
	 * black and white blocks.
	 */
	protected Set<Block> blackWhiteBlocks = new HashSet<Block>();
	// add to set only after we calculate breath.
	// List<Block> blocks = new ArrayList<Block>();
	// synonym of allActiveBlocks.
	/*
	 * blank blocks are divided into eye block and shared breath block. note not
	 * all the point in the shared blank block are shared breath.
	 */
	protected Set<BlankBlock> eyeBlocks = new HashSet<BlankBlock>();
	Set<BlankBlock> sharedBlankBlocks = new HashSet<BlankBlock>();

	public StateAnalysis(int boardSize) {
		super(boardSize);
	}
	
	public StateAnalysis(byte[][] state) {
		this(state, Constant.BLACK);
	}

	/**
	 * statically analyze the current board state without the history
	 * information of how we achieve current state (originated from
	 * BoardLianShort, but data structure is new.)
	 * 
	 * @param state
	 */
	public StateAnalysis(byte[][] state, int whoseTurn) {

		// for (int i = Constant.ZBXX; i <= Constant.ZBSX; i++) { // 2月22日加
		//
		// boardPoints[Constant.ZBXX][i] = new BoardPoint(null,
		// (byte) ColorUtil.OutOfBoard);
		// boardPoints[Constant.ZBSX][i] = new BoardPoint(null,
		// (byte) ColorUtil.OutOfBoard);
		// boardPoints[i][Constant.ZBXX] = new BoardPoint(null,
		// (byte) ColorUtil.OutOfBoard);
		// boardPoints[i][Constant.ZBSX] = new BoardPoint(null,
		// (byte) ColorUtil.OutOfBoard);
		// } // 2月22日加
		//
		// makeBlock();// 将棋子聚合成棋块,气点则聚合成气块.
		// generateBlockInfo();
		// generateBreathBlockInfo();
		super(state, whoseTurn);
		// this.state = state;
		// for (int i = 0; i < state.length; i++) {
		// state[i][0] = -1;
		// state[i][boardSize + 1] = -1;
		// state[0][i] = -1;
		// state[boardSize + 1][i] = -1;
		// }

		for (Point point : Point.getAllPoints(boardSize)) {
			BasicBlock basicBlock = this.getBasicBlock(point);
			if (basicBlock.isBlank()) {
				BlankBlock blankBlock = (BlankBlock) basicBlock;
				if (blankBlock.isEyeBlock())
					this.eyeBlocks.add(blankBlock);
				else
					this.sharedBlankBlocks.add(blankBlock);
			} else {
				this.blackWhiteBlocks.add((Block) basicBlock);
			}
			// BoardPoint bp = this.getBoardPoint(point);
			// if(bp.i)
		}
		this.collectSpecialPoint();
	}
	/**
	 * TODO:// init special data
	 * @param colorState
	 */
	public StateAnalysis(BoardColorState colorState) {
		super(colorState);
	}
	

	protected Set<Point> tigerMouth = new HashSet<Point>();

	public void collectSpecialPoint() {
		int i, j;
		Block block;
		for (Point point : Point.getAllPoints(boardSize)) {
			if (this.getColor(point) == ColorUtil.BLANK) {
				TigerMouth tm = this.isTigerMouth_(point);
				if (tm == null)
					continue;
				// TODO: collect tiger mouth.
				TigerMouth tmAdj = this.isTigerMouth_(tm.getPeek());
				if (tmAdj == null) {
					tigerMouth.add(point);
				} else {// 两个虎口相邻，是打劫的形状
					// tiger mouth is 劫 dependent. TODO
				}

			}
		}
	}

	/**
	 * according to the patter of pattern between competitors. whether a point
	 * is a cutting point, it is important since it provide about attacking
	 * point.
	 * 
	 * @param point
	 * @return
	 */

	public boolean isCuttingPoint(Point point) {

		return true;
	}

	/**
	 * whether one point is tiger mouth. it is identified earlier since is
	 * useful information for connectivity checking (already in good shape for
	 * defending).<br/>
	 * 确定一个点是否是虎口.
	 * 
	 * @param point
	 * @return
	 */
	public TigerMouth isTigerMouth_(Point point) {
		int black = 0;
		int white = 0;
		int blank = 0;
		int outOfBound = 0;
		int minBreath = 128;// the adjacent block with minimum breath;
		int tmpBreath;
		TigerMouth tm = new TigerMouth();
		tm.setPoint(point);
		if (this.getBoardPoint(point).getColor() != ColorUtil.BLANK) {
			return null;// return false;
		}
		for (Delta delta : Constant.ADJACENTS) {
			Point neighbourPoint = point.getNeighbour(delta);
			if (neighbourPoint == null) {// Out of board
				outOfBound++;
				continue;
			}
			if (this.getColor(neighbourPoint) == ColorUtil.BLACK) {
				black++;
				tmpBreath = this.getBreaths(neighbourPoint);
				if (tmpBreath < minBreath)
					minBreath = tmpBreath;
			} else if (this.getColor(neighbourPoint) == ColorUtil.WHITE) {
				white++;
				tmpBreath = this.getBreaths(neighbourPoint);
				if (tmpBreath < minBreath)
					minBreath = tmpBreath;
			} else if (this.getColor(neighbourPoint) == ColorUtil.BLANK) {

				tm.setPeek(neighbourPoint);
				blank++;
			}
		}
		if (blank == 1) {
			if (black + outOfBound == 3 && minBreath >= 2) {
				tm.setColor(ColorUtil.BLACK);
				return tm;
			} else if (white + outOfBound == 3 && minBreath >= 2) {
				tm.setColor(ColorUtil.WHITE);
				return tm;
			} else
				return null;// return false;
		} else
			return null;// return false;

	}

	public void generateBlockInfo() {

		Block block;
		byte i, j;
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {

				if (getBoardPoint(i, j).getColor() == ColorUtil.BLACK
						|| getBoardPoint(i, j).getColor() == ColorUtil.WHITE) {
					block = getBoardPoint(i, j).getBlock();
					if (block.isBreathCalculated() == false) {
						this.calculateBreath(block);
						blackWhiteBlocks.add(block);

					}
				}
			}
		}

		/**
		 * add blank block after breath is calculated.
		 */
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {

				if (getBoardPoint(i, j).getColor() == ColorUtil.BLANK) {
					BlankBlock blankblock = getBoardPoint(i, j).getBlankBlock();
					if (sharedBlankBlocks.contains(blankblock))
						continue;
					if (eyeBlocks.contains(blankblock))
						continue;
					this.identifyBlankBlock(blankblock);
				}

			}
		}

		/**
		 * setup enemy Blocks.
		 */
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {

				if (getBoardPoint(i, j).getColor() == ColorUtil.BLACK
						|| getBoardPoint(i, j).getColor() == ColorUtil.WHITE) {
					block = getBoardPoint(i, j).getBlock();
					this.setupEnemyBlock(block);
				}

			}
		}

		for (Block blockTemp : blackWhiteBlocks) {
			if (blockTemp.isBlack())
				if(log.isEnabledFor(Level.WARN)) log.warn(blockTemp);
		}
		for (Block blockTemp : blackWhiteBlocks) {
			if (blockTemp.isWhite())
				if(log.isEnabledFor(Level.WARN)) log.warn(blockTemp);
		}

		for (BlankBlock blockTemp : this.eyeBlocks) {
			if(log.isEnabledFor(Level.WARN)) log.warn(blockTemp);
		}
		for (BlankBlock blockTemp : this.sharedBlankBlocks) {
			if(log.isEnabledFor(Level.WARN)) log.warn(blockTemp);
		}

	}

	/**
	 * 识别眼位气块
	 * 
	 * @param block
	 */
	private void identifyBlankBlock(BlankBlock block) {

		byte a = 0, b = 0;
		byte row, column;
		byte j;
		boolean hasBlack = false;
		boolean hasWhite = false;
		Set<Block> blackBlocks = new HashSet<Block>();
		Set<Block> whiteBlocks = new HashSet<Block>();

		if (block.getNumberOfPoint() > 10) {
			if(log.isEnabledFor(Level.WARN)) log.warn("There is a big blank block with "
					+ block.getNumberOfPoint() + " points");
		}

		for (Point point : block.getPoints()) {
			row = point.getRow();
			column = point.getColumn();

			for (j = 0; j < 4; j++) {
				a = (byte) (row + CS.szld[j][0]);
				b = (byte) (column + CS.szld[j][1]);
				if (log.isDebugEnabled()) {
					log.debug("a=" + a + ", b=" + b);
				}
				if (getColor(a, b) == Constant.WHITE) {
					hasWhite = true;
					whiteBlocks.add(this.getBlock(a, b));
					// getBlock(a, b).addBreathBlock(block);
				} else if (getColor(a, b) == Constant.BLACK) {
					hasBlack = true;
					blackBlocks.add(this.getBlock(a, b));
					// getBlock(a, b).addBreathBlock(block);
				}
			}
		} // for

		if (hasBlack && hasWhite) {// 公气块
			sharedBlankBlocks.add(block);
			block.setEyeBlock(false);
		} else if (hasBlack == false && hasWhite == false) {
			// 棋盘初始状态。
		} else {// 眼位气块
			eyeBlocks.add(block);
			block.setEyeBlock(true);
			block.setBlackEye(hasBlack);
		}

		if (hasWhite) {
			for (Block whiteBlock : whiteBlocks) {
				whiteBlock.addBreathBlock_twoWay(block);
			}
		}
		if (hasBlack) {
			for (Block blackBlock : blackBlocks) {
				blackBlock.addBreathBlock_twoWay(block);
			}
		}
	}

	private void setupEnemyBlock(Block block) {

		byte a = 0, b = 0;
		byte row, column;
		byte j;

		int myColor = block.getColor();
		int enemyColor = ColorUtil.enemyColor(myColor);
		for (Point point : block.getPoints()) {
			
			for (Delta delta : Constant.ADJACENTS) {
				Point nb = point.getNeighbour(delta);
				if (nb == null) {
					continue;
				}
				if (getColor(nb) == enemyColor) {
					block.addEnemyBlock_twoWay(this.getBlock(nb));
				}
			}
//			for (j = 0; j < 4; j++) {
//				a = (byte) (row + CS.szld[j][0]);
//				b = (byte) (column + CS.szld[j][1]);
//				if (log.isDebugEnabled()) {
//					log.debug("a=" + a + ", b=" + b);
//				}
//				if (getColor(a, b) == enemyColor) {
//					block.addEnemyBlock_twoWay(this.getBlock(a, b));
//				}
//			}
		} // for

	}

	public Block getBlock(int a, int b) {
		return getBoardPoint(a, b).getBlock();
	}

	public BlankBlock getBlankBlock(int a, int b) {
		return getBoardPoint(a, b).getBlankBlock();
	}

	/**
	 * 识别出棋块具有的大眼情况。如果气块的子数大于棋块的子数，则不能看成大眼， 此时其实是本方的子被气块环绕。 TODO:
	 */
	private void generateBreathBlockInfo() {

		Set<Block> allActiveBlocks = new HashSet<Block>();
		int row, column;
		int myColor, enemyColor;
		for (Block block : allActiveBlocks) {
			if (block.getColor() == Constant.BLACK
					|| block.getColor() == Constant.WHITE) {
				myColor = block.getColor();
				enemyColor = ColorUtil.enemyColor(myColor);
				for (Point point : block.getPoints()) {
					// for (int i = 0; i < 4; i++) {
					// row = point.getRow() + Constant.szld[i][0];
					// column = point.getRow() + Constant.szld[i][1];
					// if (state[row][column] == ColorUtil.BLANK) {
					//
					// }
					// }
				}
			}
		}

	}

	public static byte[][] LoadState(String inname) {

		byte[][] state = new byte[Constant.BOARD_MATRIX_SIZE][Constant.BOARD_MATRIX_SIZE];
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(inname)));

			byte[] buffer = new byte[1024 * 4];
			int count = in.read(buffer);
			byte a, b, c;
			for (int i = 0; i < count; i++) {
				a = buffer[i++];
				b = buffer[i++];
				c = buffer[i];
				state[a][b] = c;
			}

			in.close();
		}

		catch (IOException ex) {
			log.debug("the input meet some trouble!");
			log.debug("Exception" + ex.toString());
		}
		return state;
	}

	public static void saveState(byte[][] state, String filePath) {
		try {
			DataOutputStream out = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(filePath)));
			// 可能是安全策略的限制，竟然无法写入文件中，字节为始终为0kb。
			// 而且我是用匿名开机的。3月7日。完全错误，代码有问题，没有关闭输出
			// 数据流。
			log.debug(out);

			// goapplet.goboard.outputState(out);
			int boardSize = state.length - 2;
			for (int i = 1; i <= boardSize; i++) {
				for (int j = 1; j <= boardSize; j++) {
					if (state[i][j] != 0) {
						out.writeByte((byte) i);
						out.writeByte((byte) j);
						out.writeByte((byte) state[i][j]);
						log.debug("i=" + i);
						log.debug("j=" + j);
						log.debug("color=" + state[i][j]);
					}
				}
			}
			out.flush();
			out.close();
		}

		catch (IOException ex) {
			log.debug("the output meet some trouble!");
			log.debug("Exception" + ex.toString());
			throw new RuntimeException(ex);
		}

		log.debug("保存局面");

	}

	/**
	 * first pass scan, currently performance is not the key concern. we focus
	 * on clarity first.
	 */
	// public void makeBlock() {
	// byte i, j;
	// // 第一遍扫描，生成块，（包括子数和子串）
	// for (i = 1; i < 20; i++) { // i是纵/行坐标,按行处理
	// for (j = 1; j < 20; j++) { // j是横/列坐标
	// if (getBoardPoint(i, j) != null) {// 此处相当于处理过的标志.
	// continue;
	// }
	// // 左.上必为空点或异色子
	// if (state[i][j] == Constant.BLACK
	// || state[i][j] == Constant.WHITE) {
	// Block block = new Block(state[i][j]);
	// // add to set only after we calculate breath.
	// // blocks.add(block);
	// chengkuai(i, j, state[i][j], block); // 判断右.下是否为同色子.
	//
	// } else if (state[i][j] == Constant.BLANK) { // 左.上必为空点或异色子
	// BlankBlock block = new BlankBlock();
	// // blocks.add(block);
	// chengkuai(i, j, state[i][j], block); // 判断右.下是否为同色子
	// }
	// }
	// } // 生成块,包括棋块。
	// }

	// public void chengkuai(byte a, byte b, byte color, BasicBlock block) {
	// byte m1, n1;
	// BoardPoint bp = new BoardPoint(Point.getPoint(boardSize, a, b), color);
	// bp.setBasicBlock(block);
	// block.addPoint(bp.getPoint());
	// // boardPoints[a][b] = bp;
	// this.setBoardPoint(bp);
	//
	// for (byte k = 0; k < 4; k++) {
	// m1 = (byte) (a + CS.szld[k][0]);
	// n1 = (byte) (b + CS.szld[k][1]);
	// if (getBoardPoint(m1, n1) == null && state[m1][n1] == color) {
	// chengkuai(m1, n1, color, block);
	// }
	// }
	// }
	//
	public byte calculateBreath_only(Block block) {
		if (log.isDebugEnabled()) {
			log.debug("进入方法：计算块气（jskq）");
			// 算出块的气，并完成块的所有信息：气数和气串。
		}

		byte a = 0, b = 0;
		byte m, n;
		byte j;

		for (Point point : block.getPoints()) {
			m = point.getRow();
			n = point.getColumn();

			for (j = 0; j < 4; j++) {
				a = (byte) (m + CS.szld[j][0]);
				b = (byte) (n + CS.szld[j][1]);
				if (log.isDebugEnabled()) {
					log.debug("a=" + a);
					log.debug("b=" + b);
				}
				if (getColor(a, b) == Constant.BLANK) {
					block.addBreathPoint(Point.getPoint(boardSize, a, b));
				}
			}

		} // for

		if (log.isDebugEnabled()) {
			log.debug("块的气数为：" + block.getBreaths());
		}

		if (log.isDebugEnabled()) {
			log.debug("方法jskq：返回");
		}
		block.setBreathCalculated(true);
		return (byte) block.getBreaths();
	} // 2月22日改,原方法虽妙,仍只能忍痛割爱.

	/**
	 * get all the blocks tightly connected by eyes
	 * 
	 * @param block
	 * @return
	 */
	public Set<Block> getGroup(Block block) {
		return null;
	}

	public Block getBlock(Point point) {
		Block block = getBoardPoint(point).getBlock();
		return block;
	}

	public Group getGroup(Point point) {
		Block block = getBoardPoint(point).getBlock();
		return block.getGroup();
	}

	public BlankBlock getBlankBlock(Point point) {
		return getBoardPoint(point).getBlankBlock();

	}

	public int getColor(Point point) {
		return getBoardPoint(point).getColor();
	}

	public int getColor(int row, int column) {

		return getBoardPoint(row, column).getColor();
	}

	/**
	 * 
	 * @param point
	 * @param blank
	 * 
	 * @return
	 */
	public int huKouShu(Point point, Point blank) {
		return huKouShu(point, blank, true);
	}

	/**
	 * 形状的评价指标。
	 * 
	 * @param point
	 *            the block to make two eyes. 代表大眼棋块
	 * @param blank
	 *            the breath point to make hukou/trap? which is the precondition
	 *            of eye. 代表尝试做眼的位置，计算结果也可以为对方破眼做参考。
	 * @param alreadyPlayed
	 *            blank point is already played.
	 * @return
	 */
	public int huKouShu(Point point, Point blank, boolean alreadyPlayed) {
		if (alreadyPlayed == false)
			setColor(blank, getColor(point));

		int a = 0, b = 0, count = 0, breath = 0;
		// 落子点必然参与形成虎口

		for (Delta delta : Constant.ADJACENTS) {
			Point nb = blank.getNeighbour(delta);
			if (nb == null)
				continue;
			// for (int i = 0; i < 4; i++) {
			// a = blank.getRow() + Constant.szld[i][0];
			// b = blank.getRow() + Constant.szld[i][1];
			if (getColor(nb) == Constant.BLANK) {
				breath = 0;
				// 计算潜在虎口点的气数。气数为一才是虎口。
				for (Delta delta2 : Constant.ADJACENTS) {
					Point nb2 = nb.getNeighbour(delta2);
					if (nb == null)
						continue;
					// for (int j = 0; j < 4; j++) {
					// a = blank.getRow() + Constant.szld[j][0];
					// b = blank.getRow() + Constant.szld[j][1];
					if (getColor(nb2) == Constant.BLANK) {

						breath++;
					}
				}
				if (breath == 1) {
					count++;
				}
			}
		}

		if (alreadyPlayed == false)
			setColor(blank.getRow(), blank.getColumn(), Constant.BLANK);
		return count;
	}

	/**
	 * 决定采用生成所有变化的思路，简化应用模式的代码。至少对简单死活，这不是问题。
	 * 
	 * @param targetBlock
	 * @param breathBlock
	 * @return
	 */
	public CoreBreathPattern toBreathPattern(BlankBlock breathBlock) {
		Shape shape = breathBlock.getShape();
		byte[][] pattern = new byte[shape.getDeltaX()][shape.getDeltaY()];
		for (int i = 0; i < shape.getDeltaX(); i++) {
			for (int j = 0; j < shape.getDeltaY(); j++) {
				pattern[i][j] = (byte) getColor(shape.getMinX() + i,
						shape.getMinY() + j);
			}
		}
		int size = breathBlock.getNumberOfPoint();
		if (size == 4 && breathBlock.getEyeName() == SmallEye.T_FOUR_STONE_EYE
				&& breathBlock.isBreathCorner()) {
			// 涉及角上的，存在另外的结构中，不用显式地表达边角，因为拓扑上它们是明确的。
			return new CoreBreathPattern(pattern);
		} else if (size == 6
				&& breathBlock.getEyeName() == SmallEye.MATRIX_SIX_STONE_EYE
				&& breathBlock.isBreathCorner()) {
			return new CoreBreathPattern(pattern);
		} else {
			return new CoreBreathPattern(pattern);
		}
	}

	public List<Block> getBlackWhiteBlocks() {
		/**
		 * blackWhiteBlocks is not maintained when one step forward and backward. so lazy load here.
		 */
		this.blackWhiteBlocks.clear();
		for (Point point : Point.getAllPoints(boardSize)) {
			BasicBlock basicBlock = this.getBasicBlock(point);
			if (basicBlock.isBlank()==false) {			
				this.blackWhiteBlocks.add((Block) basicBlock);
			}
		}
		
		
		List<Block> list = new ArrayList<Block>(this.blackWhiteBlocks.size());
		list.addAll(this.blackWhiteBlocks);
		Collections.sort(list, new BlockRowColumnComparator());
		return list;
	}

	//maybe not up to date
//	public List<BasicBlock> getAllBlocks() {
//		int size = this.blackWhiteBlocks.size() + this.eyeBlocks.size()
//				+ this.sharedBlankBlocks.size();
//		List<BasicBlock> list = new ArrayList<BasicBlock>(size);
//		list.addAll(this.blackWhiteBlocks);
//		list.addAll(this.eyeBlocks);
//		list.addAll(this.sharedBlankBlocks);
//		Collections.sort(list, new BlockRowColumnComparator());
//		return list;
//	}

//	public BoardColorState getBoardColorState() {
//		BoardColorState boardState = new BoardColorState(boardSize);
//		for (int i = 1; i <= boardSize; i++) {
//			for (int j = 1; j <= boardSize; j++) {
//				boardState.add(getBoardPoint(i, j));
//			}
//		}
//		return boardState;
//	}

}
