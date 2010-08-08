package toy.applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Set;

import toy.Block;
import toy.Board;
import toy.Move;
import toy.OldDad;
import toy.Point;
import static toy.Constant.Debug;

/**
 * 1. it will show the candidate.<br/>
 * 2. click on candidate will choose the corresponding candidate..<br/>
 * 3. if there are two way to move for one candidate. it is decided internally..<br/>
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class OldDadApplet extends Applet {
	protected Board board = new OldDad();

	@Override
	public void init() {
		super.init();
		board.init();
	}

	@Override
	public void paint(Graphics g) {
		drawBoard(g, board);
		super.paint(g);
	}

	@Override
	protected void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);

	}

	@Override
	public boolean mouseDown(Event evt, int x, int y) {
		int row = y / unit;
		int column = x / unit;

		System.out
				.println("Click on the poinit " + Point.getPoint(row, column));
		for (Move move : moves) {
			if (move.getStart().equals(Point.getPoint(row, column))) {
				board.apply(move);
				break;
			}
		}
		this.repaint();
		// evt.
		return true;
	}

	Set<Move> moves;

	public void drawBoard(Graphics g, Board board) {
		board.checkInternal();
		for (Block block : board.getBlockList()) {
			drawBlock(g, block);
		}
		moves = board.getMoves();
		for (Move move : moves) {
			this.markBlock(g, move.getBlock());
		}
	}

	private static final int unit = 100; // pixcel
	private static final int arcHeight = unit / 10;
	private static final int arcWidth = arcHeight;

	public void drawBlock(Graphics g, Block block) {
		int height = block.getHeight() * unit;
		int width = block.getWidth() * unit;
		int y = block.getMinA() * unit;
		int x = block.getMinB() * unit;
		// g.setColor(Color.YELLOW);
		g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
		// g.fillRoundRect(y, x, width, height, arcWidth, arcHeight);
		// g.fillRoundRect(x, y, width, height, width, height)
		g.setColor(Color.black);
		g.drawString(block.getName(), x + 10, y + 10 + unit / 2);
		System.out.println("drawing block: " + block.getName());
	}

	public void markBlock(Graphics g, Block block) {
		int height = unit / 2;
		int width = unit / 2;
		int y = block.getPositions()[0].getA() * unit;
		int x = block.getPositions()[0].getB() * unit;
		g.setColor(Color.RED);
		g.fillOval(x, y, width, height);

	}

}
