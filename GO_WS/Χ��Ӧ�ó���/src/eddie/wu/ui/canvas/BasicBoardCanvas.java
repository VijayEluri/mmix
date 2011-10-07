package eddie.wu.ui.canvas;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;

import eddie.wu.domain.Point;

public class BasicBoardCanvas extends Canvas {
	public void update(Graphics g) { // 585
		paint(g);
	}

	protected void drawBlankBoard(Graphics g) {
		g.setColor(Color.black);
		for (int i = 1; i <= 19; i++)// 画线
		{
			g.drawLine(18, 28 * i - 10, 522, 28 * i - 10);// horizontal
			g.drawLine(28 * i - 10, 18, 28 * i - 10, 522);// vertical
		}
		// log.debug("// paint the ver and hor line.");
		for (int i = 0; i < 3; i++) {// 画星位
			for (int j = 0; j < 3; j++) {
				g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
			}
		}
		// log.debug("//paint the star point.");
	}

	/**
	 * Note coordinate difference between matrix and plane.
	 * 
	 * @param g
	 * @param row
	 * @param column
	 */
	private void drawPoint(Graphics g, int row, int column) {
		g.fillOval(28 * column - 24, 28 * row - 24, 28, 28);
	}

	/**
	 * default font is approximately 4*8.
	 * @param g
	 * @param row
	 * @param column
	 * @param moveNumber
	 */
	protected void drawMoveNumber_OneDigit(Graphics g, int row, int column,
			int moveNumber) {
		g.setColor(Color.green);
		g.drawString("" + moveNumber, 28 * column - 13, 28 * row - 5);
	}

	protected void drawMoveNumber_TwoDigit(Graphics g, int row, int column,
			int moveNumber) {
		g.setColor(Color.green);
		g.drawString("" + moveNumber, 28 * column - 17, 28 * row - 5);
	}
	
	protected void drawMoveNumber_ThreeDigit(Graphics g, int row, int column,
			int moveNumber) {
		g.setColor(Color.green);
		g.drawString("" + moveNumber, 28 * column - 21, 28 * row - 5);
	}

	protected void drawBackgroud(Graphics g) {
		g.setColor(Color.orange);
		g.fillRect(0, 0, 560, 560);
	}
	
	

	public boolean mouseDown(Event e, int x, int y) {// 接受鼠标输入

		return false;// 向容器传播,由Frame处理

	}

	protected void drawBlackPoint(Graphics g, Point point) {
		drawBlackPoint(g, point.getRow(), point.getColumn());
	}

	protected void drawWhitePoint(Graphics g, Point point) {
		drawWhitePoint(g, point.getRow(), point.getColumn());
	}

	protected void drawBlackPoint(Graphics g, int row, int column) {
		g.setColor(Color.black);
		drawPoint(g, row, column);
	}

	protected void drawWhitePoint(Graphics g, int row, int column) {
		g.setColor(Color.white);
		drawPoint(g, row, column);
	}

	protected void drawMoveNumber(Graphics g, Point point, int stepCount) {
		if (stepCount < 10) {
			drawMoveNumber_OneDigit(g, point.getRow(), point.getColumn(),
					stepCount);
		} else if (stepCount < 100) {
			drawMoveNumber_TwoDigit(g, point.getRow(), point.getColumn(),
					stepCount);
		}
	}

}
