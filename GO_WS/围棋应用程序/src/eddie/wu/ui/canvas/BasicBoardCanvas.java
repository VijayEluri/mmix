package eddie.wu.ui.canvas;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.util.BoardGraphic;

/**
 * basic class: only have shared method, no data at all.
 * 
 * @author Eddie
 * 
 */
public abstract class BasicBoardCanvas extends Canvas {
	protected int boardSize;

	public void update(Graphics g) { // 585
		paint(g);
	}

	protected void drawBlankBoard(Graphics g) {
		BoardGraphic.drawBlankBoard(g, boardSize);
	}

	protected void drawPoint(Graphics g, int row, int column, int color) {
		BoardGraphic.drawPoint(g, row, column, color, false);
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
		BoardGraphic.drawPoint(g, point.getRow(), point.getColumn(),
				Constant.BLACK, iseaten);
	}

	protected void drawWhitePoint(Graphics g, Point point, boolean iseaten) {
		BoardGraphic.drawPoint(g, point.getRow(), point.getColumn(),
				Constant.WHITE, iseaten);
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

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

}
