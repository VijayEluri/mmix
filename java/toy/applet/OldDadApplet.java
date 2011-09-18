package toy.applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;

import toy.Block;
import toy.Board;
import toy.Move;
import toy.OldDad;
import toy.Point;

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
		this.setEnabled(true);
		drawBoard(g, board);
		super.paint(g);
	}

	@Override
	protected void processMouseEvent(MouseEvent e) {
		// super.processMouseEvent(e);

	}

	@Override
	public boolean mouseDown(Event evt, int x, int y) {
		int row = y / unit;
		int column = x / unit;

		Point point = Point.getPoint(row, column);
		System.out.println("Click on the poinit " + point);
		switch (state) {
		case Normal:
			Block block = board.getBlock(point);
			int matchCount = 0;
			Move singleMove = null;
			for (Move move : moves) {
				if (board.getBlock(move.getStart()).equals(block)) {
					matchCount++;
					singleMove = move;
					// board.apply(move);
					// break;
				}
			}
			if (matchCount == 0) {
				System.out
						.println("Did not choose any candidate source block!");
			} else if (matchCount == 1) {
				board.apply(singleMove);
			} else {
				chosenBlock = block;
				state = BoardUIState.Chosen;
			}

			break;
		case Chosen:
			if (board.getBlock(point) == null) {
				for (Move move : moves) {
					if (board.getBlock(move.getStart()).equals(chosenBlock)) {
						if (move.getEnd().equals(point)) {
							board.apply(move);
							state = BoardUIState.Normal;
							break;
						}
					}
				}
			} else {
				System.out.println("did not choose any"
						+ " blank point as target block");

			}
			break;
		}

		this.repaint();
		// evt.
		return true;
	}

	List<Move> moves;

	private BoardUIState state = BoardUIState.Normal;
	private Block chosenBlock;

	public void drawBoard(Graphics g, Board board) {
		board.checkInternal();
		g.drawRoundRect(1*unit,1*unit, unit*4, unit*5,
				arcWidth, arcHeight);
		for (Block block : board.getBlockList()) {
			drawBlock(g, block);
		}
		switch (state) {
		case Normal:
			moves = board.getMoves();
			//if (moves.size() > 1) {

				for (Move move : moves) {
					this.markBlockBlue(g, move.getBlock());
				}

				break;
			//} 
//			else {
//				chosenBlock = board.getBlock(moves.get(0).getStart());
//				state = BoardUIState.Chosen;
//			}

		case Chosen:
			this.markBlockRed(g, chosenBlock);
			break;
		}
	}

	private static final int unit = 100; // pixel
	private static final int arcHeight = unit / 10;
	private static final int arcWidth = arcHeight;

	private static final int delta = 3;
	private static final int delta2 = delta * 2;

	public void drawBlock(Graphics g, Block block) {
		int height = block.getHeight() * unit;
		int width = block.getWidth() * unit;
		int y = block.getMinA() * unit;
		int x = block.getMinB() * unit;
		// g.setColor(Color.YELLOW);
		g.drawRoundRect(x + delta, y + delta, width - delta2, height - delta2,
				arcWidth, arcHeight);
		// g.fillRoundRect(y, x, width, height, arcWidth, arcHeight);
		// g.fillRoundRect(x, y, width, height, width, height)
		g.setColor(Color.black);
		g.drawString(block.getName(), x + 10, y + 10 + unit / 2);
		System.out.println("drawing block: " + block.getName());
	}

	/**
	 * 
	 * @param g
	 * @param block
	 */
	public void markBlockBlue(Graphics g, Block block) {
		_markBlock(g, block, Color.BLUE);
	}

	public void markBlockRed(Graphics g, Block block) {
		_markBlock(g, block, Color.RED);
	}

	public void _markBlock(Graphics g, Block block, Color color) {
		int height = block.getHeight() * unit;
		int width = block.getWidth() * unit;
		int y = block.getMinA() * unit;
		int x = block.getMinB() * unit;
		g.setColor(color);
		g.drawRoundRect(x + delta, y + delta, width - delta2, height - delta2,
				arcWidth, arcHeight);
		// g.fillRoundRect(y, x, width, height, arcWidth, arcHeight);
		// g.fillRoundRect(x, y, width, height, width, height)
		g.setColor(Color.black);
		g.drawString(block.getName(), x + 10, y + 10 + unit / 2);
		System.out.println("drawing block: " + block.getName());
	}

	/*
	 * old implementation. mark as red circle
	 */
	public void markBlock(Graphics g, Block block) {
		int height = unit / 5;
		int width = unit / 5;
		int y = block.getRepPoint().getA() * unit;
		int x = block.getRepPoint().getB() * unit;
		g.setColor(Color.RED);
		g.fillOval(x, y, width, height);

	}

}
