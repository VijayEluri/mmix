package go;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardPoint;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.linkedblock.CS;
import eddie.wu.ui.RecordState;

public class StateAnalysis {
	private static final Log log = LogFactory.getLog(RecordState.class);
	protected byte[][] state;
	protected BoardPoint[][] boardPoints = new BoardPoint[21][21];
	/**
	 * black and white blocks.
	 */
	Set<Block> allActiveBlocks = new HashSet<Block>();
	// add to set only after we calculate breath.
	// List<Block> blocks = new ArrayList<Block>();
	// synonym of allActiveBlocks.
	/*
	 * blank blocks are divided into eye block and shared breath block. note not
	 * all the point in the shared blank block are shared breath.
	 */
	Set<Block> eyeBlocks = new HashSet<Block>();
	Set<Block> sharedBlankBlocks = new HashSet<Block>();

	/**
	 * statically analyze the current board state without the history
	 * information of how we achieve current state (originated from
	 * BoardLianShort, but data structure is new.)
	 * 
	 * @param state
	 */
	public StateAnalysis(byte[][] state) {
		this.state = state;
		for (int i = 0; i < 21; i++) {
			state[i][0] = -1;
			state[i][20] = -1;
			state[0][i] = -1;
			state[20][i] = -1;
		}
		for (int i = Constant.ZBXX; i <= Constant.ZBSX; i++) { // 2月22日加

			boardPoints[Constant.ZBXX][i] = new BoardPoint(Point.getPoint(
					Constant.ZBXX, i), (byte) ColorUtil.OutOfBound);
			boardPoints[Constant.ZBSX][i] = new BoardPoint(Point.getPoint(
					Constant.ZBSX, i), (byte) ColorUtil.OutOfBound);
			boardPoints[i][Constant.ZBXX] = new BoardPoint(Point.getPoint(i,
					Constant.ZBXX), (byte) ColorUtil.OutOfBound);
			boardPoints[i][Constant.ZBSX] = new BoardPoint(Point.getPoint(i,
					Constant.ZBSX), (byte) ColorUtil.OutOfBound);
		} // 2月22日加

		makeBlock();
		generateBlockInfo();
		generateBreathBlockInfo();
	}
	
	public void generateBlockInfo() {

		Block block;
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				block = boardPoints[i][j].getBlock();
				if (block.getColor() == ColorUtil.BLACK
						|| block.getColor() == ColorUtil.WHITE) {
					if (block.isBreathCalculated() == false) {
						this.calculateBreath(block);
						allActiveBlocks.add(block);

					}
				}
			}
		}

		/**
		 * add block after breath is calculated.
		 */
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				block = boardPoints[i][j].getBlock();
				if (block.getColor() == ColorUtil.BLANK_POINT) {					
					this.identifyBlankBlock(block);
				}

			}
		}
		
		/**
		 * setup enemy Blocks.
		 */
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				block = boardPoints[i][j].getBlock();
				if (block.getColor() == ColorUtil.BLACK
						|| block.getColor() == ColorUtil.WHITE) {					
					this.setupEnemyBlock(block);
				}

			}
		}

		if (log.isWarnEnabled()) {
			for (Block blockTemp : allActiveBlocks) {
				log.warn(blockTemp);
			}
		}

	}

	/**
	 * 识别眼位气块
	 * 
	 * @param block
	 */
	private void identifyBlankBlock(Block block) {

		byte a = 0, b = 0;
		byte row, column;
		byte j;
		boolean hasBlack = false;
		boolean hasWhite = false;
		Set<Block> blackBlocks = new HashSet<Block>();
		Set<Block> whiteBlocks = new HashSet<Block>();
		if (sharedBlankBlocks.contains(block))
			return;
		if (eyeBlocks.contains(block))
			return;
		if (block.getTotalNumberOfPoint() > 10) {
			if (log.isWarnEnabled()) {
				log.warn("There is a big blank block with "
						+ block.getTotalNumberOfPoint() + " points");
			}
		}

		for (Point point : block.getAllPoints()) {
			row = point.getRow();
			column = point.getColumn();

			for (j = 0; j < 4; j++) {
				a = (byte) (row + CS.szld[j][0]);
				b = (byte) (column + CS.szld[j][1]);
				if (log.isDebugEnabled()) {
					log.debug("a=" + a + ", b=" + b);
				}
				if (state[a][b] == Constant.WHITE) {
					hasWhite = true;
					whiteBlocks.add(this.getBlock(a, b));
					// getBlock(a, b).addBreathBlock(block);
				} else if (state[a][b] == Constant.BLACK) {
					hasBlack = true;
					blackBlocks.add(this.getBlock(a, b));
					// getBlock(a, b).addBreathBlock(block);
				}
			}
		} // for

		if (hasBlack && hasWhite) {// 公气块
			sharedBlankBlocks.add(block);
		} else if (hasBlack == false && hasWhite == false) {// 公气块
			// 棋盘初始状态。
		} else {// 眼位气块
			eyeBlocks.add(block);
		}

		if (hasWhite) {
			for (Block whiteBlock : whiteBlocks) {
				whiteBlock.addBreathBlock(block);
			}
		}
		if (hasBlack) {
			for (Block blackBlock : blackBlocks) {
				blackBlock.addBreathBlock(block);
			}
		}
	}
	
	private void setupEnemyBlock(Block block) {

		byte a = 0, b = 0;
		byte row, column;
		byte j;
		

		int myColor = block.getColor();
		int enemyColor = ColorUtil.enemyColor(myColor);
		for (Point point : block.getAllPoints()) {
			row = point.getRow();
			column = point.getColumn();

			for (j = 0; j < 4; j++) {
				a = (byte) (row + CS.szld[j][0]);
				b = (byte) (column + CS.szld[j][1]);
				if (log.isDebugEnabled()) {
					log.debug("a=" + a + ", b=" + b);
				}
				if (state[a][b] == enemyColor) {
					block.addEnemyBlock(this.getBlock(a,b));
				} 
			}
		} // for
		
	}

	private Block getBlock(byte a, byte b) {
		return boardPoints[a][b].getBlock();
	}

	

	/**
	 * 识别出棋块具有的大眼情况。如果气块的子数大于棋块的子数，则不能看成大眼， 此时其实是本方的子被气块环绕。
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
				for (Point point : block.getAllPoints()) {
					for (int i = 0; i < 4; i++) {
						row = point.getRow() + Constant.szld[i][0];
						column = point.getRow() + Constant.szld[i][1];
						if (state[row][column] == ColorUtil.BLANK_POINT) {

						}
					}
				}
			}
		}

	}

	public static byte[][] LoadState(String inname) {

		byte[][] state = new byte[21][21];
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

	/**
	 * first pass scan, currently performance is not the key concern. we focus
	 * on clarity first.
	 */
	public void makeBlock() {
		byte i, j;
		// 第一遍扫描，生成块，（包括子数和子串）
		for (i = 1; i < 20; i++) { // i是纵/行坐标,按行处理
			for (j = 1; j < 20; j++) { // j是横/列坐标
				if (boardPoints[i][j] != null) {// 此处相当于处理过的标志.
					continue;
				}
				// 左.上必为空点或异色子
				if (state[i][j] == Constant.BLACK
						|| state[i][j] == Constant.WHITE) {
					Block block = new Block(state[i][j]);
					// add to set only after we calculate breath.
					// blocks.add(block);
					chengkuai(i, j, state[i][j], block); // 判断右.下是否为同色子.

				} else if (state[i][j] == Constant.BLANK) { // 左.上必为空点或异色子
					Block block = new Block(state[i][j]);
					// blocks.add(block);
					chengkuai(i, j, state[i][j], block); // 判断右.下是否为同色子
				}
			}
		} // 生成块,包括棋块。
	}

	public void chengkuai(byte a, byte b, byte color, Block block) {
		byte m1, n1;
		BoardPoint bp = new BoardPoint(Point.getPoint(a, b), color);
		bp.setBlock(block);
		block.addPoint(bp.getPoint());
		boardPoints[a][b] = bp;

		for (byte k = 0; k < 4; k++) {
			m1 = (byte) (a + CS.szld[k][0]);
			n1 = (byte) (b + CS.szld[k][1]);
			if (boardPoints[m1][n1] == null && state[m1][n1] == color) {
				chengkuai(m1, n1, color, block);
			}
		}
	}

	public byte calculateBreath(Block block) {
		if (log.isDebugEnabled()) {
			log.debug("进入方法：计算块气（jskq）");
			// 算出块的气，并完成块的所有信息：气数和气串。
		}

		byte a = 0, b = 0;
		byte m, n;
		byte j;

		for (Point point : block.getAllPoints()) {
			m = point.getRow();
			n = point.getColumn();

			for (j = 0; j < 4; j++) {
				a = (byte) (m + CS.szld[j][0]);
				b = (byte) (n + CS.szld[j][1]);
				if (log.isDebugEnabled()) {
					log.debug("a=" + a);
					log.debug("b=" + b);
				}
				if (state[a][b] == Constant.BLANK) {
					block.addBreathPoint(Point.getPoint(a, b));
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
		Block block = boardPoints[point.getRow()][point.getColumn()].getBlock();
		return block;
	}

	public int getColor(Point point) {
		return boardPoints[point.getRow()][point.getColumn()].getColor();
	}
}
