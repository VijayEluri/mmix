package eddie.wu.manual;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.linkedblock.BoardPoint;
import eddie.wu.linkedblock.ColorUtil;

/**
 * this Go board is used for display and for go manual conversion. the data
 * structure is too simple to deal with forward one step.
 * 
 * It will normalize the Go Manual. because of symmetry, the Go manual may have
 * several variant for same one.
 * 
 * @author eddie
 * 
 */
public class GoBoardSymmetry {
	private static final Log log = LogFactory.getLog(GoBoardSymmetry.class);

	// ÿ�����ӵ�����������ԭʼ��Ϣ������������С����Դ���һ�����档
	// ��ά�����̵�����,�����±��1��19;
	private BoardPoint[][] boardPoints = new BoardPoint[Constant.SIZEOFMATRIX][Constant.SIZEOFMATRIX];

	private Point[][] points = new Point[Constant.SIZEOFMATRIX][Constant.SIZEOFMATRIX];

	// ������ӿ�ֿ����������ӿ�ĸ�����
	private short numberOfPointsExisted = 0; // ��ǰ��������,����֮ǰ����.��1��ʼ;

	public GoBoardSymmetry() {

		byte i, j;
		for (i = 1; i < Constant.ZBSX; i++) {
			for (j = 1; j < Constant.ZBSX; j++) {
				points[i][j] = Point.getPoint(i,j);//new Point(i, j);
				boardPoints[i][j] = new BoardPoint(points[i][j]);
				boardPoints[i][j].setBlock(null);// no need to use block here

			}
		}
		for (i = Constant.ZBXX; i <= Constant.ZBSX; i++) { // 2��22�ռ�
			points[Constant.ZBXX][i] = new Point(Constant.ZBXX, i);
			points[Constant.ZBSX][i] = new Point(Constant.ZBSX, i);
			points[i][Constant.ZBXX] = new Point(i, Constant.ZBXX);
			points[i][Constant.ZBSX] = new Point(i, Constant.ZBSX);
			boardPoints[Constant.ZBXX][i] = new BoardPoint(
					points[Constant.ZBXX][i], (byte) ColorUtil.OutOfBound);
			boardPoints[Constant.ZBSX][i] = new BoardPoint(
					points[Constant.ZBSX][i], (byte) ColorUtil.OutOfBound);
			boardPoints[i][Constant.ZBXX] = new BoardPoint(
					points[i][Constant.ZBXX], (byte) ColorUtil.OutOfBound);
			boardPoints[i][Constant.ZBSX] = new BoardPoint(
					points[i][Constant.ZBSX], (byte) ColorUtil.OutOfBound);
		} // 2��22�ռ�

	}

	/**
	 * no double check really used Constructor.
	 * 
	 */
	public GoBoardSymmetry(BoardColorState boardState) {
		this();

		final Set<Point> blackPoints = boardState.getBlackPoints();
		final Set<Point> whitePoints = boardState.getWhitePoints();
		 
		for (Point tempPoint: blackPoints) {		
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
		for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
			if (boardPoints[i][Constant.COORDINATEOFTIANYUAN].getColor() != ColorUtil.BLANK_POINT) {
				count++;
			}
		}
		return count;
	}

	public int pointsInForwardSlashLine() {
		int count = 0;
		for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
			if (boardPoints[Constant.SIZEOFBOARD + 1 - i][i].getColor() != ColorUtil.BLANK_POINT) {
				count++;
			}
		}
		return count;
	}

	public int pointsInBackwardSlashLine() {
		int count = 0;
		for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
			if (boardPoints[i][i].getColor() != ColorUtil.BLANK_POINT) {
				count++;
			}
		}
		return count;
	}

	public int pointsInHorizontalLine() {
		int count = 0;
		for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
			if (boardPoints[Constant.COORDINATEOFTIANYUAN][i].getColor() != ColorUtil.BLANK_POINT) {
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
			for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
				for (int j = 1; j < Constant.COORDINATEOFTIANYUAN; j++) {

					if (boardPoints[i][j].getColor() != boardPoints[i][Constant.SIZEOFBOARD
							+ 1 - j].getColor()) {
						if (log.isDebugEnabled()) {
							System.out.println("i="
									+ i
									+ ",j="
									+ j
									+ ",color="
									+ boardPoints[i][j].getColor()
									+ "color="
									+ boardPoints[i][Constant.SIZEOFBOARD + 1
											- j].getColor());
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
			for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
				for (int j = 1; j < Constant.COORDINATEOFTIANYUAN; j++) {
					if (boardPoints[j][i].getColor() != boardPoints[Constant.SIZEOFBOARD
							+ 1 - j][i].getColor()) {
						if (log.isDebugEnabled()) {
							System.out
									.println("i="
											+ i
											+ ",j="
											+ j
											+ ",color="
											+ boardPoints[j][i].getColor()
											+ "color="
											+ boardPoints[Constant.SIZEOFBOARD
													+ 1 - j][i].getColor());
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
			for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
				for (int j = i + 1; j <= Constant.SIZEOFBOARD; j++) {
					if (boardPoints[j][i].getColor() != boardPoints[i][j]
							.getColor()) {
						if (log.isDebugEnabled()) {
							System.out.println("i=" + i + ",j=" + j + ",color="
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
			for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
				for (int j = 1; j < Constant.SIZEOFBOARD + 1 - i; j++) {
					if (boardPoints[20 - j][20 - i].getColor() != boardPoints[i][j]
							.getColor()) {
						if (log.isDebugEnabled()) {
							System.out.println("i=" + i + ",j=" + j + ",color="
									+ boardPoints[20 - j][20 - i].getColor()
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
