package eddie.wu.util;

import java.awt.Color;
import java.awt.Graphics;

import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.ui.UIPoint;

/**
 * 
 * @author Eddie
 * 
 */
public class BoardGraphic {
	public static void drawBlankBoard(Graphics g, int boardSize) {

		int diameter = 28;// 交叉点之间的宽度
		int topLeft = diameter / 2 + 4;// 18; // 左上角的坐标
		g.setColor(Color.BLACK);
		int x1, y1, x2, y2;
		for (int i = 1; i <= boardSize; i++) {// 画线
			x1 = topLeft;
			y1 = topLeft + diameter * (i - 1);
			x2 = topLeft + (boardSize - 1) * diameter;
			y2 = y1;
			g.drawLine(x1, y1, x2, y2);// horizontal line
			g.drawLine(y1, x1, y2, x2);// vertical
			// x1 = diameter * (i - 1) + topLeft;
			// y1 = topLeft;
			// x2 = x1;// diameter * i - 10;
			// y2 = topLeft + (boardSize - 1) * diameter;
			// g.drawLine(x1, y1, x2, y2);// vertical
		}
		// log.debug("// paint the ver and hor line.");
		if (boardSize == Constant.BOARD_SIZE) {
			for (int i = 0; i < 3; i++) {// 画星位
				for (int j = 0; j < 3; j++) {
					x1 = (diameter * 6 * i) + (diameter * 3 + topLeft - 3);
					y1 = (diameter * 6 * j) + (diameter * 3 + topLeft - 3);
					g.fillOval(x1, y1, 6, 6);
				}
			}
		} else if (boardSize == Constant.SMALL_BOARD_SIZE) {
			for (int i = 0; i < 2; i++) {// 画两个三三
				for (int j = 0; j < 2; j++) {
					x1 = (diameter * 6 * i) + (diameter * 2 + topLeft - 3);
					y1 = (diameter * 6 * j) + (diameter * 2 + topLeft - 3);
					g.fillOval(x1, y1, 6, 6);
				}
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
	public static void drawPoint(Graphics g, int row, int column, int color,
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

	public static void drawPoint(Graphics g, UIPoint point) {
		int row = point.getPoint().getRow();
		int column = point.getPoint().getColumn();
		int color = point.getColor();
		if (point.isEaten()) {
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
			if (point.getMark() == Constant.MARK_DEAD) {
				g.setColor(Color.RED);
				int[] xPoints = new int[4];
				int[] yPoints = new int[4];
				xPoints[0] = 28 * column - 24 + 7;
				yPoints[0] = 28 * (row + 1) - 24 - 7;

				xPoints[1] = 28 * (column + 1) - 24 - 7 ;
				yPoints[1] = 28 * (row + 1) - 24 - 7;

				xPoints[2] = 28 * column - 24 + (28 / 2);
				yPoints[2] = 28 * (row) - 24 + 7;

				xPoints[3] = xPoints[0];
				yPoints[3] = yPoints[0];

				g.drawPolyline(xPoints, yPoints, 4);

			}
		}

	}

	public static void drawPoints(Graphics g, byte[][] zb) {
		int boardSize = zb.length - 2;

		for (int i = 1; i <= boardSize; i++) {// 画着子点
			for (int j = 1; j <= boardSize; j++) {
				if (zb[i][j] == ColorUtil.BLACK || zb[i][j] == ColorUtil.WHITE)
					drawPoint(g, i, j, zb[i][j], false);

			}
		}// for: paint all the points owned by black and white.

	}
}
