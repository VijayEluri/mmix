package eddie.wu.manual;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.linkedblock.GoBoard;
import eddie.wu.linkedblock.SimpleGoBoard;

/**
 * convert between symmetry GoManual
 * though copy code is not a good practice.
 * I will separate the code from GoBoard to simpleGoBoard 
 * 
 * @author eddie
 * 
 */
public class ConvertGoManual {
	private static final int NUMBER_FOR_COMPLETE = Constant.SIZEOFBOARD + 1;

	private static final Log log = LogFactory.getLog(ConvertGoManual.class);

	public byte[] convertFormat(byte[] position) {
		int t = position.length / 2;
		byte[] row = new byte[t];
		byte[] column = new byte[t];
		for (int i = 0; i < t; i++) {
			row[i] = position[2 * i];
			column[i] = position[2 * i + 1];
		}
		return convertFormat(row, column);
	}

	public void display(final byte[] row, final byte[] column) {
		if (log.isDebugEnabled()) {
			for (int i = 0; i < 10; i++) {
				System.out.println("i=" + i + ", [" + row[i] + "," + column[i]
						+ "]");
			}
		}
	}

	/**
	 * final here is useless just to give a tip;
	 * 
	 * @param row
	 * @param column
	 */
	public byte[] convertFormat(final byte[] row, final byte[] column) {
		validateParam(row, column);
		GoBoard goBoard = new GoBoard();
		SimpleGoBoard simpleGoBoard = new SimpleGoBoard();
		/**
		 * standard format, first step in left top and row>column! then can be
		 * easily convert into usual format.
		 */
		int a = 0;
		int b = 0;
		Point point = null;
		int timesOfSymmetry=0;
		for (int i = 0; i < row.length; i++) {
			if (simpleGoBoard.numberOfSymmetry() == 0) {
				goBoard.oneStepForward(row[i], column[i]);
				simpleGoBoard=new SimpleGoBoard(goBoard.getBoardColorState());
				continue;
			} else if (simpleGoBoard.numberOfSymmetry() == 15) {
				timesOfSymmetry++;
				log.debug("i="+i+";timesOfSymmetry="+timesOfSymmetry+
						";numberOfSymmetry="+simpleGoBoard.numberOfSymmetry());
				a = row[i];
				b = column[i];
				point = new Point(a, b);
				if (point.isLeftTop()) {
					if (a < b) {
						backwardSlashLineConvert(row, column, i);
					}
				} else if (point.isLeftBottom()) {// need xuanzhuan.
					this.horizontalLineConvert(row, column, i);
					conditionalBackwardConvert(row, column, i);
				} else if (point.isRightTop()) {
					this.verticalLineConvert(row, column, i);
					conditionalBackwardConvert(row, column, i);
				} else if (point.isRightBottom()) {
					this.forwardSlashLineConvert(row, column, i);
					conditionalBackwardConvert(row, column, i);
				}
				goBoard.oneStepForward(row[i], column[i]);
				simpleGoBoard=new SimpleGoBoard(goBoard.getBoardColorState());
				continue;
			} else if (simpleGoBoard.numberOfSymmetry() == 1||
					simpleGoBoard.numberOfSymmetry() == 2||
					simpleGoBoard.numberOfSymmetry() == 4||
					simpleGoBoard.numberOfSymmetry() == 8) {
				
				timesOfSymmetry++;
//				if(timesOfSymmetry>1){
//					log.error("i="+i+";timesOfSymmetry="+timesOfSymmetry+
//							";numberOfSymmetry="+simpleGoBoard.numberOfSymmetry());
//				}
				log.debug("i="+i+";timesOfSymmetry="+timesOfSymmetry+
						";numberOfSymmetry="+simpleGoBoard.numberOfSymmetry());
				if (simpleGoBoard.verticalSymmetry()) {
					this.verticalLineConvert(row, column, i);
				} else if (simpleGoBoard.horizontalSymmetry()) {
					this.horizontalLineConvert(row, column, i);
				}
				if (simpleGoBoard.forwardSlashSymmetry()) {
					this.forwardSlashLineConvert(row, column, i);
				}
				if (simpleGoBoard.backwardSlashSymmetry()) {
					this.backwardSlashLineConvert(row, column, i);
				}
				goBoard.oneStepForward(row[i], column[i]);
				simpleGoBoard=new SimpleGoBoard(goBoard.getBoardColorState());
				continue;
			} else if(simpleGoBoard.numberOfSymmetry() == 3||
					simpleGoBoard.numberOfSymmetry() == 12) {
				timesOfSymmetry++;
//				log.error("i="+i+";timesOfSymmetry="+timesOfSymmetry+
//						";numberOfSymmetry="+simpleGoBoard.numberOfSymmetry());
//			
				log.debug("timesOfSymmetry="+timesOfSymmetry+
						";numberOfSymmetry="+simpleGoBoard.numberOfSymmetry());
				System.out.println("numberOfSymmetry="
						+ simpleGoBoard.numberOfSymmetry());
				System.out.println(goBoard.getBoardColorState());
				/**
				 * only two kinds of scenarios when the number  of symmetry is 2
				 * one is horizontal and vertical symmetry.
				 * another is slash and back slash symmetry.
				 * horizontal (or vertical) and slash (or back slash) is impossible.
				 */
				if (point.isLeftTop()) {
					if (a < b) {
						if (simpleGoBoard.backwardSlashSymmetry()) {
							backwardSlashLineConvert(row, column, 0);
						}
					}
				} else if (point.isLeftBottom()) {// need xuanzhuan.
					if (simpleGoBoard.horizontalSymmetry()) {
						this.horizontalLineConvert(row, column, i);
						a = row[i];
						b = column[i];
						if (a < b) {
							if (simpleGoBoard.backwardSlashSymmetry()) {//impossible
								backwardSlashLineConvert(row, column, i);
							}
						}
					}else{//salsh symmetry
						
					}
				} else if (point.isRightTop()) {
					if (simpleGoBoard.verticalSymmetry()) {
						this.verticalLineConvert(row, column, i);
						a = row[i];
						b = column[i];
						if (a < b) {//impossible
							if (simpleGoBoard.backwardSlashSymmetry()) {
								backwardSlashLineConvert(row, column, i);
							}
						}
					}else{//salsh symmetry
						if (simpleGoBoard.backwardSlashSymmetry()) {
							backwardSlashLineConvert(row, column, i);
						}
					}
				} else if (point.isRightBottom()) {
					if (simpleGoBoard.forwardSlashSymmetry()) {
						this.forwardSlashLineConvert(row, column, i);
						a = row[i];
						b = column[i];
						if (a < b) {
							if (simpleGoBoard.backwardSlashSymmetry()) {
								backwardSlashLineConvert(row, column, i);
							}
						}
					}else{//horizontal/vertical symmetry
						this.horizontalLineConvert(row,column,i);
						this.verticalLineConvert(row,column,i);
					}
				}
				goBoard.oneStepForward(row[i], column[i]);
				simpleGoBoard=new SimpleGoBoard(goBoard.getBoardColorState());
				

			}
			else{
				/**
				 * It is impossible to be 3 kinds of symmetry. 
				 * if there are three kinds of symmetry
				 * there must be total 4 kinds of symmetry
				 */
				throw new RuntimeException(goBoard.getBoardColorState().toString());
			}
		}
		log.debug("Tatal: timesOfSymmetry="+timesOfSymmetry);
		byte[] temp = new byte[row.length * 2];
		for (int i = 0; i < row.length; i++) {
			temp[2 * i] = row[i];
			temp[2 * i + 1] = column[i];
		}
		return temp;
	}

	private void conditionalBackwardConvert(final byte[] row,
			final byte[] column, int i) {
		int a;
		int b;
		a = row[i];
		b = column[i];
		if (a < b) {
			backwardSlashLineConvert(row, column, i);
		}
	}

	private void validateParam(byte[] row, byte[] column) {
		if (row.length != column.length) {
			throw new RuntimeException("row.length!=column.length");
		}
	}

	/**
	 * convert from index.
	 * 
	 * @param row
	 * @param column
	 * @param index
	 *            inclued
	 */
	public void verticalLineConvert(byte[] row, byte[] column, int index) {
		validateParam(row, column);
		int b = column[index];
		if (NUMBER_FOR_COMPLETE - b > b) {
			return;
		}
		display(row, column);
		for (int i = index; i < row.length; i++) {
			column[i] = (byte) (NUMBER_FOR_COMPLETE - column[i]);
		}
		display(row, column);
	}

	/**
	 * after convert the total value row + column should decrease.
	 * 
	 * @param row
	 * @param column
	 * @param index
	 */
	public void horizontalLineConvert(byte[] row, byte[] column, int index) {
		validateParam(row, column);
		int a = row[index];
		if (NUMBER_FOR_COMPLETE - a > a) {
			return;
		}
		display(row, column);
		for (int i = index; i < row.length; i++) {
			row[i] = (byte) (NUMBER_FOR_COMPLETE - row[i]);
		}
		display(row, column);
	}

	public void backwardSlashLineConvert(byte[] row, byte[] column, int index) {
		validateParam(row, column);
		if (row[index] >= column[index])
			return;
		byte t = 0;
		display(row, column);
		for (int i = index; i < row.length; i++) {
			t = row[i];
			row[i] = column[i];
			column[i] = t;
		}
		display(row, column);
	}

	public void forwardSlashLineConvert(byte[] row, byte[] column, int index) {
		validateParam(row, column);
		byte t = 0;
		int a = row[index];
		int b = column[index];
		if ((NUMBER_FOR_COMPLETE - a + NUMBER_FOR_COMPLETE - b) >= (a + b)) {
			return;
		}
		display(row, column);
		for (int i = index; i < row.length; i++) {
			t = row[i];
			row[i] = (byte) (NUMBER_FOR_COMPLETE - column[i]);
			column[i] = (byte) (NUMBER_FOR_COMPLETE - t);
		}
		display(row, column);
	}
}
