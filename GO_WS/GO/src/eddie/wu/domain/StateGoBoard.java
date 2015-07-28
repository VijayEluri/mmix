package eddie.wu.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.survive.BreathPattern;
import eddie.wu.domain.survive.EyePattern;
import eddie.wu.search.BlankPoint;
import eddie.wu.search.BlankPointBreathComparator;
import eddie.wu.search.eye.BreakEyeComparator;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.MakeEyeComparator;
import eddie.wu.ui.UIPoint;

public abstract class StateGoBoard extends BasicGoBoard {

	private transient static final Logger log = Logger
			.getLogger(StateGoBoard.class);

	public StateGoBoard(int boardSize) {
		super(boardSize);

	}

	/**
	 * 
	 * really used Constructor.
	 * 
	 */
	public StateGoBoard() {
		this(Constant.BOARD_SIZE);
	}

	/**
	 * only maintain breath point, block relationship is not covered.
	 * 
	 * @deprecated
	 * @param block
	 */
	private void calculateBreath_nolink(Block block) {

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
					point = new UIPoint(bPoint.getPoint());
					point.setColor(Constant.BLACK);
				} else if (getBoardPoint(i, j).getColor() == Constant.WHITE) {

				}
			}

		}
		return list;
	}

		

	public EyePattern getEyePattern(BlankBlock block) {
		EyePattern eyePattern = new EyePattern();
		Shape shape = block.getShape();
		int rows = 0, columns = 0;
		int topRow = shape.getMinX();
		int leftColumn = shape.getMinY();
		int bottomRow = shape.getMaxX();
		int rightColumn = shape.getMaxY();
		if (shape.getMinX() != 1) {
			rows += 1;
			topRow -= 1;
		}
		if (shape.getMaxX() != this.boardSize) {
			rows += 1;
			bottomRow += 1;
		}
		if (shape.getMinY() != 1) {
			columns += 1;
			leftColumn -= 1;
		}
		if (shape.getMaxY() != this.boardSize) {
			columns += 1;
			rightColumn += 1;
		}
		byte[][] pattern = new byte[rows][columns];

		Set<Point> selfPoints = new HashSet<Point>();
		for (Point point : block.getPoints()) {
			for (Delta delta : Constant.ADJACENTS) {
				Point neighbor = point.getNeighbour(delta);
				if (neighbor != null)
					selfPoints.add(neighbor);
			}
		}

		eyePattern.addSelfPoints(selfPoints, new Delta(topRow, leftColumn));

		return eyePattern;
	}

	/**
	 * 3. 点眼的选择,点入后气数多为好.
	 * not used now!
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

}
