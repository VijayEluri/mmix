package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.comp.SymmetryRowColumnComparator;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.CandidateComparator;

/**
 * 原先仅仅考虑奇数路的棋盘,如19路和11路.<br/>
 * 现在增加对偶数路棋盘的支持,比如2路和4路.
 * 
 * @author Eddie
 * 
 * 
 *         TODO: had duplicate class in other package.
 */
public class GoBoardSymmetry extends GoBoard {
	private transient static final Logger log = Logger
			.getLogger(GoBoardSymmetry.class);

	public GoBoardSymmetry() {
		super(Constant.BOARD_SIZE);
	}

	public GoBoardSymmetry(int boardSize) {
		super(boardSize);
	}

	public GoBoardSymmetry(byte[][] state) {
		super(state);
	}

	public int pointsInHorizontalLine() {
		/**
		 * 偶数路棋盘的对称中线不在棋盘上.
		 */
		if (boardSize % 2 == 0)
			return 0;

		int count = 0;
		/**
		 * middleLine = (boardSize+1)/2; to support even number board size.
		 * boardSieze 19 middleLine 10 boardSieze 18 middleLine 10
		 */
		int middleLine = (boardSize + 2) / 2;
		for (int i = 1; i <= boardSize; i++) {
			if (getBoardPoint(middleLine, i).getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	public int pointsInVerticalMiddleLine() {

		/**
		 * 偶数路棋盘的对称中线不在棋盘上.
		 */
		if (boardSize % 2 == 0)
			return 0;
		/**
		 * middleLine = (boardSize+1)/2; to support even number board size.
		 * boardSieze 19 middleLine 10 boardSieze 18 middleLine 10
		 */
		int middleLine = (boardSize + 2) / 2;

		int count = 0;
		for (int i = 1; i <= boardSize; i++) {
			if (getBoardPoint(i, middleLine).getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 不论奇偶路棋盘,斜的对称线总是在棋盘上.
	 * 
	 * @return
	 */
	public int pointsInForwardSlashLine() {
		int count = 0;
		for (int row = 1; row <= boardSize; row++) {
			if (getBoardPoint(boardSize + 1 - row, row).getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 不论奇偶路棋盘,斜的对称线总是在棋盘上.
	 * 
	 * @return
	 */
	public int pointsInBackwardSlashLine() {
		int count = 0;
		for (int row = 1; row <= boardSize; row++) {
			if (getBoardPoint(row, row).getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	/**
	 * whether the state is symmetry<br/>
	 * 棋子是否关于垂直中线对称?
	 * 
	 * @return
	 */
	public boolean verticalSymmetry() {
		if (possibleVerticalSymmetry() == false) {
			return false;
		}
		/**
		 * middleLine = (boardSize+1)/2; to support even number board size.
		 * boardSieze 19 middleLine 10 boardSieze 18 middleLine 10
		 */
		int middleLine = (boardSize + 2) / 2;
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column < middleLine; column++) {
				BoardPoint leftBoardPoint = getBoardPoint(row, column);
				BoardPoint rightBoardPoint = getBoardPoint(row, boardSize + 1
						- column);
				if (leftBoardPoint.getColor() != rightBoardPoint.getColor()) {
					if (log.isDebugEnabled()) {
						log.debug(leftBoardPoint);
						log.debug(rightBoardPoint);
					}
					return false;
				}
			}
		}
		if (log.isDebugEnabled())
			log.debug("verticalSymmetry");
		return true;

	}

	/**
	 * 棋子是否关于水平中线对称?
	 * 
	 * @return
	 */
	public boolean horizontalSymmetry() {
		if (possibleHorizontalSymmetry() == false) {
			return false;
		}
		/**
		 * middleLine = (boardSize+1)/2; to support even number board size.
		 * boardSieze 19 middleLine 10 boardSieze 18 middleLine 10
		 */
		int middleLine = (boardSize + 2) / 2;
		for (int column = 1; column <= boardSize; column++) {
			for (int row = 1; row < middleLine; row++) {
				BoardPoint leftBoardPoint = getBoardPoint(row, column);
				BoardPoint rightBoardPoint = getBoardPoint(boardSize + 1 - row,
						column);
				if (leftBoardPoint.getColor() != rightBoardPoint.getColor()) {
					if (log.isDebugEnabled()) {
						log.debug(leftBoardPoint);
						log.debug(rightBoardPoint);
					}
					return false;
				}
			}
		}
		if (log.isDebugEnabled())
			log.debug("horizontalSymmetry");
		return true;
	}

	public boolean backwardSlashSymmetry() {
		if (possibleBackwardSlashSymmetry() == false) {
			return false;
		}

		for (int row = 1; row <= boardSize; row++) {
			for (int column = row + 1; column <= boardSize; column++) {
				BoardPoint bottomBoardPoint = getBoardPoint(column, row);
				BoardPoint topBoardPoint = getBoardPoint(row, column);
				if (bottomBoardPoint.getColor() != topBoardPoint.getColor()) {
					if (log.isDebugEnabled()) {
						log.debug(topBoardPoint);
						log.debug(bottomBoardPoint);
					}
					return false;
				}

			}
		}
		if (log.isDebugEnabled())
			log.debug("backwardSlashSymmetry");
		return true;

	}

	/**
	 * 是否关于前向斜轴对称.
	 * 
	 * @return
	 */
	public boolean forwardSlashSymmetry() {
		if (possibleForwardSlashSymmetry() == false) {
			return false;
		}

		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column < boardSize + 1 - row; column++) {
				BoardPoint topBoardPoint = getBoardPoint(row, column);
				BoardPoint bottomBoardPoint = getBoardPoint(boardSize + 1
						- column, boardSize + 1 - row);
				if (bottomBoardPoint.getColor() != topBoardPoint.getColor()) {

					if (log.isDebugEnabled()) {
						log.debug(topBoardPoint);
						log.debug(bottomBoardPoint);
					}

					return false;

				}

			}
		}
		if (log.isDebugEnabled())
			log.debug("forwardSlashSymmetry");
		return true;

	}

	public SymmetryResult getSymmetryResult() {
		SymmetryResult result = new SymmetryResult();

		result.setHorizontalSymmetry(this.horizontalSymmetry());
		result.setVerticalSymmetry(this.verticalSymmetry());
		result.setForwardSlashSymmetry(this.forwardSlashSymmetry());
		result.setBackwardSlashSymmetry(this.backwardSlashSymmetry());

		return result;

	}

	/**
	 * 1. reduce candidates by symmetry<br/>
	 * 2. sort by priority.<br/>
	 * 3. avoid fill eye point.<br/>
	 * 4. eat/capture first
	 * 
	 * @return
	 */
	public List<Point> getCandidate(int color) {
		Map<Point, Integer> breathMap = new HashMap<Point, Integer>();

		Set<Point> points = new HashSet<Point>();
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				BoardPoint boardPoint = getBoardPoint(row, column);
				if (boardPoint.getColor() == Constant.BLANK) {
					// 3. avoid fill eye point.
					if (boardPoint.getBlankBlock().isEyeBlock()) {
						if ((color == Constant.BLACK && boardPoint
								.getBlankBlock().isBlackEye())
								|| (color == Constant.WHITE && boardPoint
										.getBlankBlock().isBlackEye() == false))
							continue;

					}

					int breaths = breathAfterPlay(boardPoint.getPoint(), color)
							.size();
					if (breaths > 0) {
						points.add(boardPoint.getPoint());
						breathMap.put(boardPoint.getPoint(), breaths);
					}
				}
			}
		}

		/**
		 * 处理本质上等价的候选棋步.
		 */
		List<Point> can = new ArrayList<Point>();
		SymmetryResult symmetryResult = this.getSymmetryResult();
		if (symmetryResult.getNumberOfSymmetry() != 0) {

			Set<Point> points2 = new HashSet<Point>();
			Set<Point> listAll = new HashSet<Point>();
			for (Iterator<Point> iter = points.iterator(); iter.hasNext();) {
				Point point = iter.next();
				if (listAll.contains(point))
					continue;

				List<Point> listVar = this.normalize(point, symmetryResult);
				listAll.addAll(listVar);
				// only keep one of all the symmetric candidates.
				points2.add(listVar.get(0));

			}
			can.addAll(points2);
		} else {
			can.addAll(points);
		}
		/**
		 * decide sequence by priority.<br/>
		 * capture sequence. <br/>
		 */
		List<Candidate> candidates = new ArrayList<Candidate>();
		for (Point point : can) {
			Candidate candidate = new Candidate();
			candidate.setStep(new Step(point, color));

			NeighborState state = this.getNeighborState(point, color);
			candidate.setEating(state.isCapturing());
			candidate.setBreaths(breathMap.get(point));

			candidates.add(candidate);
		}

		// Collections.sort(can, new LowLineComparator());
		Collections.sort(candidates, new CandidateComparator());

		can.clear();
		for (Candidate candidate : candidates) {
			can.add(candidate.getStep().getPoint());
		}
		return can;
	}

	// public int getIsomorphisms() {
	//
	//
	// SymmetryResult symmetryResult = this.getSymmetryResult();
	// int numberOfSymmetry = symmetryResult.getNumberOfSymmetry();
	// if (numberOfSymmetry >= 3)
	// return 1;
	//
	// List<byte[][]> list = new ArrayList<byte[][]>();
	// //int size = this.boardSize + 2;
	// byte[][] horizontalMirror ;
	// byte[][] verticalMirror ;
	// byte[][] forwardSlashMirror;
	// byte[][] backwardSlashMirror;
	//
	// if (symmetryResult.isHorizontalSymmetry() == false) {
	// horizontalMirror = horizontalMirror(this.getMatrixState());
	// list.add(horizontalMirror);
	// }
	// if (symmetryResult.isVerticalSymmetry()== false) {
	// verticalMirror = verticalMirror(this.getMatrixState());
	// boolean duplicate=false;
	// for(byte[][] temp:list){
	// if(Arrays.deepEquals(temp, verticalMirror)) duplicate=true;
	// }
	// if(duplicate==false)
	// list.add(verticalMirror);
	// }
	// if (symmetryResult.isForwardSlashSymmetry()== false) {
	// forwardSlashMirror = forwardSlashMirror(this.getMatrixState());
	// boolean duplicate=false;
	// for(byte[][] temp:list){
	// if(Arrays.deepEquals(temp, forwardSlashMirror)) duplicate=true;
	// }
	// if(duplicate==false)
	// list.add(forwardSlashMirror);
	// }
	// if (symmetryResult.isBackwardSlashSymmetry()== false) {
	// backwardSlashMirror = backwardSlashMirror(this.getMatrixState());
	// boolean duplicate=false;
	// for(byte[][] temp:list){
	// if(Arrays.deepEquals(temp, backwardSlashMirror)) {
	// duplicate=true;
	// break;
	// }
	// }
	// if(duplicate==false)
	// list.add(backwardSlashMirror);
	// }
	// numberOfSymmetry--;//first round is easy, sure not identical.
	// if(numberOfSymmetry<0) return list.size()+1;
	//
	// /**
	// * apply second non-symmetry.
	// */
	// list.add(this.getMatrixState());
	//
	//
	// numberOfSymmetry--;//first round is easy, sure not identical.
	// if(numberOfSymmetry<0) return list.size();
	//
	// while (numberOfSymmetry-- >0) {
	//
	//
	// /**
	// * get the combination of conversion; second conversion is done. now
	// * first // conversion.
	// */
	// // if (list.size() < 2) {
	// // log.debug(list);
	// // } else {
	// Point pointA = list.get(1);
	// if (symmetryResult.isHorizontalSymmetry()) {
	// list.add(pointA.horizontalMirror());
	// } else if (symmetryResult.isVerticalSymmetry()) {
	// list.add(pointA.verticalMirror());
	// } else if (symmetryResult.isForwardSlashSymmetry()) {
	// list.add(pointA.forwardSlashMirror());
	// } else if (symmetryResult.isBackwardSlashSymmetry()) {
	// list.add(pointA.backwardSlashMirror());
	// }
	// // }
	// list.add(point);
	//
	// } else if (numberOfSymmetry == 1) {
	// if (symmetryResult.isHorizontalSymmetry()) {
	// list.add(horizontalMirror);
	// } else if (symmetryResult.isVerticalSymmetry()) {
	// list.add(verticalMirror);
	// } else if (symmetryResult.isForwardSlashSymmetry()) {
	// list.add(forwardSlashMirror());
	// } else if (symmetryResult.isBackwardSlashSymmetry()) {
	// list.add(backwardSlashMirror());
	// }
	// list.add(point);
	// }
	//
	// }

	private byte[][] verticalMirror(byte[][] original) {
		int size = this.boardSize + 2;
		byte[][] state = new byte[size][size];

		for (Point point : Point.getAllPoints(boardSize)) {
			Point mirror = point.verticalMirror();
			state[mirror.getRow()][mirror.getColumn()] = state[point.getRow()][point
					.getColumn()];
		}

		return state;
	}

	private byte[][] backwardSlashMirror(byte[][] original) {
		int size = this.boardSize + 2;
		byte[][] state = new byte[size][size];

		for (Point point : Point.getAllPoints(boardSize)) {
			Point mirror = point.backwardSlashMirror();
			state[mirror.getRow()][mirror.getColumn()] = state[point.getRow()][point
					.getColumn()];
		}

		return state;
	}

	// for(int row = 1;row<=boardSize;row++){
	// for(int column = 1;column<=boardSize;column++){
	//
	// }
	// }

	private byte[][] horizontalMirror(byte[][] original) {
		int size = this.boardSize + 2;
		byte[][] state = new byte[size][size];

		for (Point point : Point.getAllPoints(boardSize)) {
			Point mirror = point.horizontalMirror();
			state[mirror.getRow()][mirror.getColumn()] = state[point.getRow()][point
					.getColumn()];
		}

		return state;
	}

	private byte[][] forwardSlashMirror(byte[][] original) {
		int size = this.boardSize + 2;
		byte[][] state = new byte[size][size];

		for (Point point : Point.getAllPoints(boardSize)) {
			Point mirror = point.forwardSlashMirror();
			state[mirror.getRow()][mirror.getColumn()] = state[point.getRow()][point
					.getColumn()];
		}

		return state;
	}

	/**
	 * brute force (ugly) implementation. 展开得到所有对称的候选点.
	 * 
	 * @param point
	 * @param symmetryResult
	 * @return all the symmetric points
	 */
	private List<Point> normalize(Point point, SymmetryResult symmetryResult) {
		List<Point> list = new ArrayList<Point>();
		int numberOfSymmetry = symmetryResult.getNumberOfSymmetry();
		Point horizontalMirror = point.horizontalMirror();
		Point verticalMirror = point.verticalMirror();
		if (numberOfSymmetry == 4) {
			list.add(horizontalMirror);
			list.add(verticalMirror);
			list.add(horizontalMirror.verticalMirror());
			list.add(point);
			List<Point> list2 = new ArrayList<Point>();
			list2.addAll(list);
			for (Point temp : list2) {
				// allow duplicates in list.
				list.add(temp.backwardSlashMirror());
				list.add(temp.forwardSlashMirror());
			}
		} else if (numberOfSymmetry == 2) {
			if (symmetryResult.isHorizontalSymmetry()) {
				list.add(horizontalMirror);
			}
			if (symmetryResult.isVerticalSymmetry()) {
				list.add(verticalMirror);
			}
			if (symmetryResult.isForwardSlashSymmetry()) {
				list.add(point.forwardSlashMirror());
			}
			if (symmetryResult.isBackwardSlashSymmetry()) {
				list.add(point.backwardSlashMirror());
			}

			/**
			 * get the combination of conversion; second conversion is done. now
			 * first // conversion.
			 */
			// if (list.size() < 2) {
			// log.debug(list);
			// } else {
			Point pointA = list.get(1);
			if (symmetryResult.isHorizontalSymmetry()) {
				list.add(pointA.horizontalMirror());
			} else if (symmetryResult.isVerticalSymmetry()) {
				list.add(pointA.verticalMirror());
			} else if (symmetryResult.isForwardSlashSymmetry()) {
				list.add(pointA.forwardSlashMirror());
			} else if (symmetryResult.isBackwardSlashSymmetry()) {
				list.add(pointA.backwardSlashMirror());
			}
			// }
			list.add(point);

		} else if (numberOfSymmetry == 1) {
			if (symmetryResult.isHorizontalSymmetry()) {
				list.add(horizontalMirror);
			} else if (symmetryResult.isVerticalSymmetry()) {
				list.add(verticalMirror);
			} else if (symmetryResult.isForwardSlashSymmetry()) {
				list.add(point.forwardSlashMirror());
			} else if (symmetryResult.isBackwardSlashSymmetry()) {
				list.add(point.backwardSlashMirror());
			}
			list.add(point);
		}

		Collections.sort(list, new SymmetryRowColumnComparator());
		return list;
	}

	public boolean possibleVerticalSymmetry() {
		int count = this.getNumberOfPoint() - pointsInVerticalMiddleLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public boolean possibleHorizontalSymmetry() {
		int count = getNumberOfPoint() - pointsInHorizontalLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public boolean possibleForwardSlashSymmetry() {
		int count = getNumberOfPoint() - pointsInForwardSlashLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public boolean possibleBackwardSlashSymmetry() {
		int count = getNumberOfPoint() - pointsInBackwardSlashLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

}
