package eddie.wu.manual;

import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.BoardPoint;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;

/**
 * this Go board is used for display and for go manual conversion. the data
 * structure is too simple to deal with forward one step.
 * 
 * It will normalize the Go Manual. because of symmetry, the Go manual may have  
 * several variant for same one.
 * @deprecated refer to another GoBoardSymmetry
 * @author eddie
 * 
 */
public class GoBoardSymmetry {
	private static final Logger log = Logger.getLogger(GoBoardSymmetry.class);

	// 每个落子点的情况，所有原始信息都在这个数组中。足以代表一个局面。
	// 两维是棋盘的坐标,数组下标从1到19;
	private BoardPoint[][] boardPoints = new BoardPoint[Constant.BOARD_MATRIX_SIZE][Constant.BOARD_MATRIX_SIZE];

	// private Point[][] points = new
	// Point[Constant.BOARD_MATRIX_SIZE][Constant.BOARD_MATRIX_SIZE];

	// 气块和子块分开，气块是子块的附属。
	private short numberOfPointsExisted = 0; // 当前已有手数,处理之前递增.从1开始;

	private final int boardSize;
	private final int middleLine;

	public GoBoardSymmetry() {
		this(Constant.BOARD_SIZE);
	}

	public GoBoardSymmetry(int boardSize) {
		this.boardSize = boardSize;
		middleLine = (boardSize + 1) / 2;
		byte i, j;
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {

				boardPoints[i][j] = new BoardPoint(Point.getPoint(boardSize, i,
						j));

			}
		}
		// for (i = Constant.ZBXX; i <= Constant.ZBSX; i++) { // 2月22日加
		// // points[Constant.ZBXX][i] = new Point(Constant.ZBXX, i);
		// // points[Constant.ZBSX][i] = new Point(Constant.ZBSX, i);
		// // points[i][Constant.ZBXX] = new Point(i, Constant.ZBXX);
		// // points[i][Constant.ZBSX] = new Point(i, Constant.ZBSX);
		// boardPoints[Constant.ZBXX][i] = new BoardPoint(
		// points[Constant.ZBXX][i], (byte) ColorUtil.OutOfBoard);
		// boardPoints[Constant.ZBSX][i] = new BoardPoint(
		// points[Constant.ZBSX][i], (byte) ColorUtil.OutOfBoard);
		// boardPoints[i][Constant.ZBXX] = new BoardPoint(
		// points[i][Constant.ZBXX], (byte) ColorUtil.OutOfBoard);
		// boardPoints[i][Constant.ZBSX] = new BoardPoint(
		// points[i][Constant.ZBSX], (byte) ColorUtil.OutOfBoard);
		// } // 2月22日加

	}

	/**
	 * no double check really used Constructor.
	 * 
	 */
	public GoBoardSymmetry(BoardColorState boardState) {
		this(boardState.boardSize);

		final Set<Point> blackPoints = boardState.getBlackPoints();
		final Set<Point> whitePoints = boardState.getWhitePoints();

		for (Point tempPoint : blackPoints) {
			boardPoints[tempPoint.getRow()][tempPoint.getColumn()]
					.setColor(ColorUtil.BLACK);
		}
		for (Point tempPoint : whitePoints) {
			boardPoints[tempPoint.getRow()][tempPoint.getColumn()]
					.setColor(ColorUtil.WHITE);
		}

	}

	public boolean isBlackTurn() {
		return evenNumberOfPoints(numberOfPointsExisted);
	}

	public boolean possibleVerticalSymmetry() {
		int count = numberOfPointsExisted - pointsInVerticalLine();
		return evenNumberOfPoints(count);
	}

	public boolean possibleHorizontalSymmetry() {
		int count = numberOfPointsExisted - pointsInHorizontalLine();
		return evenNumberOfPoints(count);
	}

	public boolean possibleForwardSlashSymmetry() {
		int count = numberOfPointsExisted - pointsInForwardSlashLine();
		return evenNumberOfPoints(count);
	}

	public boolean possibleBackwardSlashSymmetry() {
		int count = numberOfPointsExisted - pointsInBackwardSlashLine();
		return evenNumberOfPoints(count);
	}

	// whether is even (ou shu) or not
	private boolean evenNumberOfPoints(int count) {
		if (count % 2 == 0)
			return true;
		return false;
	}

	public boolean isWhiteTurn() {
		return !evenNumberOfPoints(numberOfPointsExisted);
	}

	public int pointsInVerticalLine() {
		int count = 0;
		for (int i = 1; i <= boardSize; i++) {
			if (boardPoints[i][middleLine].getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	public int pointsInForwardSlashLine() {
		int count = 0;
		for (int i = 1; i <= boardSize; i++) {
			if (boardPoints[boardSize + 1 - i][i].getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	public int pointsInBackwardSlashLine() {
		int count = 0;
		for (int i = 1; i <= boardSize; i++) {
			if (boardPoints[i][i].getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	public int pointsInHorizontalLine() {
		int count = 0;
		for (int i = 1; i <= boardSize; i++) {
			if (boardPoints[middleLine][i].getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	/**
	 * whether the state is vertical symmetry
	 * 
	 * @return
	 */
	public boolean verticalSymmetry() {
		if (possibleVerticalSymmetry()) {
			for (int i = 1; i <= boardSize; i++) {
				for (int j = 1; j < middleLine; j++) {

					if (boardPoints[i][j].getColor() != boardPoints[i][boardSize
							+ 1 - j].getColor()) {
						if (log.isDebugEnabled()) {
							if(log.isDebugEnabled()) log.debug("i="
									+ i
									+ ",j="
									+ j
									+ ",color="
									+ boardPoints[i][j].getColor()
									+ "color="
									+ boardPoints[i][boardSize + 1 - j]
											.getColor());
						}
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean horizontalSymmetry() {
		if (possibleHorizontalSymmetry()) {
			for (int i = 1; i <= boardSize; i++) {
				for (int j = 1; j < middleLine; j++) {
					if (boardPoints[j][i].getColor() != boardPoints[boardSize
							+ 1 - j][i].getColor()) {
						if (log.isDebugEnabled()) {
							if(log.isDebugEnabled()) log.debug("i="
									+ i
									+ ",j="
									+ j
									+ ",color="
									+ boardPoints[j][i].getColor()
									+ "color="
									+ boardPoints[boardSize + 1 - j][i]
											.getColor());
						}
						return false;

					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean backwardSlashSymmetry() {
		if (possibleBackwardSlashSymmetry()) {
			for (int i = 1; i <= boardSize; i++) {
				for (int j = i + 1; j <= boardSize; j++) {
					if (boardPoints[j][i].getColor() != boardPoints[i][j]
							.getColor()) {
						if (log.isDebugEnabled()) {
							if(log.isDebugEnabled()) log.debug("i=" + i + ",j=" + j + ",color="
									+ boardPoints[j][i].getColor() + "color="
									+ boardPoints[i][j].getColor());

						}
						return false;
					}

				}
			}
			return true;
		}
		return false;
	}

	public boolean forwardSlashSymmetry() {
		if (possibleForwardSlashSymmetry()) {
			for (int i = 1; i <= boardSize; i++) {
				for (int j = 1; j < boardSize + 1 - i; j++) {
					if (boardPoints[boardSize+1 - j][boardSize+1 - i].getColor() != boardPoints[i][j]
							.getColor()) {
						if (log.isDebugEnabled()) {
							if(log.isDebugEnabled()) log.debug("i=" + i + ",j=" + j + ",color="
									+ boardPoints[boardSize+1 - j][boardSize+1 - i].getColor()
									+ "color=" + boardPoints[i][j].getColor());
						}
						return false;

					}

				}
			}
			return true;
		}
		return false;
	}

	public int numberOfSymmetry() {
		int temp = 0;
		temp += this.horizontalSymmetry() ? 1 : 0;
		temp += this.verticalSymmetry() ? 2 : 0;
		temp += this.forwardSlashSymmetry() ? 4 : 0;
		temp += this.backwardSlashSymmetry() ? 8 : 0;

		return temp;

	}
}
