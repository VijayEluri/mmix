package eddie.wu.ui.canvas;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;

/**
 * basic class: only have shared method, no data at all.
 * 
 * @author Eddie
 * 
 */
public abstract class BasicBoardCanvas extends Canvas {
	int boardSize;

	public void update(Graphics g) { // 585
		paint(g);
	}

	protected void drawBlankBoard(Graphics g) {
		int diameter = 28;//交叉点之间的宽度
		int topLeft = 18; //左上角的坐标
		g.setColor(Color.BLACK);
		for (int i = 1; i <= boardSize; i++) {// 画线

			g.drawLine(topLeft, diameter * i - 10, topLeft + (boardSize - 1)
					* diameter, diameter * i - 10);// horizontal
			g.drawLine(diameter * i - 10, topLeft, diameter * i - 10, topLeft
					+ (boardSize - 1) * diameter);// vertical
		}
		// log.debug("// paint the ver and hor line.");
		if (boardSize == Constant.BOARD_SIZE) {
			for (int i = 0; i < 3; i++) {// 画星位
				for (int j = 0; j < 3; j++) {
					g.fillOval((diameter * 6 * i)
							+ (diameter * 3 + topLeft - 3), (diameter * 6 * j)
							+ (diameter * 3 + topLeft - 3), 6, 6);
				}
			}
		} else if (boardSize == Constant.SMALL_BOARD_SIZE) {
			for (int i = 0; i < 2; i++) {// 画两个三三
				for (int j = 0; j < 2; j++) {
					g.fillOval((diameter * 6 * i)
							+ (diameter * 2 + topLeft - 3), (diameter * 6 * j)
							+ (diameter * 2 + topLeft - 3), 6, 6);
				}
			}
		}
		// log.debug("//paint the star point.");
	}

	protected void drawPoint(Graphics g, int row, int column, int color) {
		drawPoint(g, row, column, color, false);
	}

	/**
	 * Note coordinate difference between matrix and plane.
	 * 
	 * @param g
	 * @param row
	 * @param column
	 */
	private void drawPoint(Graphics g, int row, int column, int color,
			boolean isEaten) {

		if (isEaten) {
			g.setColor(Color.RED);
			g.fillOval(28 * column - 24, 28 * row - 24, 28, 28);
			if (color == Constant.BLACK)
				g.setColor(Color.BLACK);
			else if (color == Constant.WHITE)
				g.setColor(Color.WHITE);
			g.fillOval(28 * column - 24 + 2, 28 * row - 24 + 2, 28 - 4, 28 - 4);

		} else {
			if (color == Constant.BLACK)
				g.setColor(Color.BLACK);
			else if (color == Constant.WHITE)
				g.setColor(Color.WHITE);
			g.fillOval(28 * column - 24, 28 * row - 24, 28, 28);
		}

	}

	/**
	 * default font is approximately 4*8.
	 * 
	 * @param g
	 * @param row
	 * @param column
	 * @param moveNumber
	 */
	protected void drawMoveNumber_OneDigit(Graphics g, int row, int column,
			int moveNumber) {

		g.drawString("" + moveNumber, 28 * column - 13, 28 * row - 5);
	}

	protected void drawMoveNumber_TwoDigit(Graphics g, int row, int column,
			int moveNumber) {

		g.drawString("" + moveNumber, 28 * column - 17, 28 * row - 5);
	}

	protected void drawMoveNumber_ThreeDigit(Graphics g, int row, int column,
			int moveNumber) {

		g.drawString("" + moveNumber, 28 * column - 21, 28 * row - 5);
	}

	protected void drawBackgroud(Graphics g) {
		g.setColor(Color.orange);
		g.fillRect(0, 0, 560, 560);
	}

	public boolean mouseDown(Event e, int x, int y) {// 接受鼠标输入

		return false;// 向容器传播,由Frame处理

	}

	protected void drawBlackPoint(Graphics g, Point point, boolean iseaten) {
		drawPoint(g, point.getRow(), point.getColumn(), Constant.BLACK, iseaten);
	}

	protected void drawWhitePoint(Graphics g, Point point, boolean iseaten) {
		drawPoint(g, point.getRow(), point.getColumn(), Constant.WHITE, iseaten);
	}

	// protected void drawBlackPoint(Graphics g, int row, int column) {
	// drawBlackPoint(g, row, column, false);
	// }

	// protected void drawBlackPoint(Graphics g, int row, int column,
	// boolean isEaten) {
	// g.setColor(Color.black);
	// drawPoint(g, row, column, isEaten);
	// }

	// protected void drawWhitePoint(Graphics g, int row, int column) {
	// drawWhitePoint(g, row, column, false);
	// }
	//
	// protected void drawWhitePoint(Graphics g, int row, int column,
	// boolean isEaten) {
	// g.setColor(Color.white);
	// drawPoint(g, row, column, isEaten);
	// }

	protected void drawMoveNumber(Graphics g, Point point, int stepCount) {
		g.setColor(Color.green);
		_drawMoveNumber(g, point, stepCount);
	}

	/**
	 * 反显手数。
	 * 
	 * @param g
	 * @param point
	 * @param stepCount
	 * @param color
	 */
	protected void drawMoveNumber(Graphics g, Point point, int stepCount,
			int color) {
		if (color == Constant.BLACK)
			g.setColor(Color.white);
		else if (color == Constant.WHITE)
			g.setColor(Color.black);
		_drawMoveNumber(g, point, stepCount);
	}

	protected void _drawMoveNumber(Graphics g, Point point, int stepCount) {
		if (stepCount < 10) {
			drawMoveNumber_OneDigit(g, point.getRow(), point.getColumn(),
					stepCount);
		} else if (stepCount < 100) {
			drawMoveNumber_TwoDigit(g, point.getRow(), point.getColumn(),
					stepCount);
		} else {
			drawMoveNumber_ThreeDigit(g, point.getRow(), point.getColumn(),
					stepCount);
		}
	}

}
