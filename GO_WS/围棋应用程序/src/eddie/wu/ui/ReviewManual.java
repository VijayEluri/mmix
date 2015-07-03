package eddie.wu.ui;

import java.awt.Button;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.api.GoBoardInterface;
import eddie.wu.domain.Block;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.ui.canvas.ReviewManualCanvas;

/**
 * 简单的打谱功能，只能展示线性的常规棋谱，不能展示变化；变化也许可以在注释中描述。<br/>
 * button 1: 前进一步。<br/>
 * button 2: 后退一步。<br/>
 * 默认显示数字<br/>
 * TODO：左右方向键可以后退和前进。（如果不喜欢鼠标操作的话，可以放松一下） 2011.10.6 play game<br/>
 * 吴瑞泽想学习编程，想编个世界上最伟大的愤怒小鸟。 好好学习，天天向上。 <br/>
 * change 1: display real number instead of always starting from 1 in each page.
 * change 2:
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class ReviewManual extends Frame {
	private static final Logger log = Logger.getLogger(ReviewManual.class);

	/**
	 * domain object.
	 */
	private GoBoardInterface go;
	// private GoBoardInterface go = new ArrayGoBoard();

	private List<UIPoint> points = new ArrayList<UIPoint>();

	private SimpleGoManual manual;
	private int moves = 0;//
	private int startMove = 0;//
	/**
	 * 每一谱的起始手数.
	 */
	private List<Integer> sectionStart = new ArrayList<Integer>();

	public void init() {
		moves = 0;
		startMove = 0;
		sectionStart.clear();
	}

	/**
	 * UI elements
	 */
	private ReviewManualCanvas embedCanvas = new ReviewManualCanvas(
			Constant.BOARD_SIZE);
	private Button load = new Button("载入棋谱");
	private Button forward = new Button("下一步");
	private Button backward = new Button("上一步");
	/**
	 * 更加人性化的显示方式，自动以提子为分割，一次显示若干手数。<br/>
	 * 每次不超过100手，这样数字不会超过两位。
	 */
	Button forwardManual = new Button("下一谱");
	Button backwardManual = new Button("上一谱");

	/**
	 * 显示对局信息
	 */
	Label blackPlayer = new Label("黑方");
	Label whitePlayer = new Label("白方");
	Label result = new Label("结果");
	Label shoushu = new Label("手数");

	TextField blackPlayerV = new TextField();
	TextField whitePlayerV = new TextField();
	TextField resultV = new TextField();
	TextField shoushuV = new TextField();

	public static void main(String[] args) {
		// if (args.length > 1) {
		// rootDir = args[1];
		// }
		// GMDGoManual manual = new
		// LoadGMDGoManual(rootDir).loadSingleGoManual();
		String fileName = Constant.rootDir + "吴清源番棋263局/吴清源番棋001.SGF";

		ReviewManual weiqi = new ReviewManual();
		weiqi.setVisible(true);
		weiqi.setBounds(0, 0, 800, 600);
		weiqi.manual = SGFGoManual.loadSimpleGoManual(fileName);

		weiqi.blackPlayerV.setText(weiqi.manual.getBlackName());
		weiqi.whitePlayerV.setText(weiqi.manual.getWhiteName());
		weiqi.resultV.setText(weiqi.manual.getResult());
		weiqi.shoushuV.setText(weiqi.manual.getShouShu() + "");

		weiqi.go = new GoBoard(weiqi.manual.getInitState());

		int count = 0;
		List<Step> steps = weiqi.manual.getSteps();
		for (Step step : steps) {
			count++;
			if (count >= 163)
				break;
			weiqi.go.oneStepForward(step);
		}

	}

	/**
	 * 
	 * @param state
	 *            the final board status
	 * @param steps
	 *            the history of steps
	 */
	public ReviewManual() {

		embedCanvas.setPoints(points);
		embedCanvas.setVisible(true);

		add(embedCanvas);
		add(load);
		add(forward);
		add(backward);
		add(forwardManual);
		add(backwardManual);
		add(blackPlayer);
		add(whitePlayer);
		add(result);
		add(shoushu);
		add(blackPlayerV);
		add(whitePlayerV);
		add(resultV);
		add(shoushuV);
		load.addActionListener(new LoadActionListener());
		forward.addActionListener(new ForwardActionListener());
		backward.addActionListener(new BackwardActionListener());
		forwardManual.addActionListener(new ForwardManualActionListener());
		backwardManual.addActionListener(new BackwardManualActionListener());
		load.setVisible(true);
		forward.setVisible(true);
		backward.setVisible(true);
		forwardManual.setVisible(true);
		backwardManual.setVisible(true);
		blackPlayer.setVisible(true);
		whitePlayer.setVisible(true);
		result.setVisible(true);
		blackPlayerV.setVisible(true);
		whitePlayerV.setVisible(true);
		resultV.setVisible(true);
		shoushu.setVisible(true);
		shoushuV.setVisible(true);

		backwardManual.setEnabled(false);
		blackPlayerV.setEditable(false);
		whitePlayerV.setEditable(false);
		resultV.setEditable(false);
		shoushuV.setEditable(false);

		backward.setEnabled(false);
		setLayout(null);

		embedCanvas.setBounds(30, 30, 560, 560);
		load.setBounds(600, 40, 100, 30);
		forward.setBounds(600, 100, 100, 30);
		backward.setBounds(600, 160, 100, 30);
		forwardManual.setBounds(600, 220, 100, 30);
		backwardManual.setBounds(600, 280, 100, 30);

		blackPlayer.setBounds(600, 360, 40, 20);
		blackPlayerV.setBounds(645, 360, 100, 20);

		whitePlayer.setBounds(600, 390, 40, 20);
		whitePlayerV.setBounds(645, 390, 100, 20);

		result.setBounds(600, 420, 40, 20);
		resultV.setBounds(645, 420, 100, 20);

		shoushu.setBounds(600, 450, 40, 20);
		shoushuV.setBounds(645, 450, 100, 20);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}

		});
	}

	public boolean mouseDown(Event e, int x, int y) { // 接受鼠标输入
		if (log.isDebugEnabled()) {
			log.debug("chuan bo dao rong qi - forward one step.");
		}
		x -= 30;
		y -= 30;

		byte a = (byte) ((x - 4) / 28 + 1);// 完成数气提子等.
		byte b = (byte) ((y - 4) / 28 + 1);
		if (log.isDebugEnabled())
			log.debug("weiqiFrame de mousedown");
		// coordinate difference between matrix and plane.

		repaint();
		embedCanvas.repaint();
		return true;
	}

	void populateUIPoints_oneStep() {
		points.clear();
		handleInitState();// should happen first.
		UIPoint uPoint;
		byte[][] matrixState = go.getMatrixState();
		int color;
		List<Step> steps = manual.getSteps();
		for (int i = 0; i < moves; i++) {
			Step step = steps.get(i);
			if (step.getPoint() == null)
				continue;
			color = matrixState[step.getPoint().getRow()][step.getPoint()
					.getColumn()];
			// 如果这一步（点）没有被提吃的话。
			if (color == Constant.BLACK || color == Constant.WHITE) {
				uPoint = new UIPoint(step.getPoint());
				uPoint.setColor(step.getColor());
				uPoint.setMoveNumber(i + 1);
				points.add(uPoint);
			} else {
				// TODO: better way to display eaten point.
			}
		}

	}

	void handleInitState() {
		// handle the handicap
		int color;
		UIPoint uPoint;
		byte[][] matrixState = go.getMatrixState();
		for (Point black : manual.getInitBlacks()) {
			color = matrixState[black.getRow()][black.getColumn()];
			// 如果这一步（点）没有被提吃的话。
			if (color == Constant.BLACK) {
				uPoint = new UIPoint();
				uPoint.setColor(ColorUtil.BLACK);
				uPoint.setMoveNumber(0);
				uPoint.setPoint(black);
				points.add(uPoint);
			}
		}

		for (Point white : manual.getInitWhites()) {
			color = matrixState[white.getRow()][white.getColumn()];
			// 如果这一步（点）没有被提吃的话。
			if (color == Constant.WHITE) {
				uPoint = new UIPoint();
				uPoint.setColor(ColorUtil.WHITE);
				uPoint.setMoveNumber(0);
				uPoint.setPoint(white);
				points.add(uPoint);
			}
		}
	}

	/**
	 * 
	 * @author Eddie
	 * 
	 */
	class ForwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			List<Step> steps = manual.getSteps();
			backward.setEnabled(true);
			points.clear();
			Step step = steps.get(moves++);
			go.oneStepForward(step);
			if (moves == steps.size()) {
				forward.setEnabled(false);
			}
			populateUIPoints_oneStep();
			repaint();
			embedCanvas.repaint();
		}
	}

	class BackwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			List<Step> steps = manual.getSteps();
			forward.setEnabled(true);
			points.clear();
			Step step = steps.get(--moves);
			go.oneStepBackward();
			if (moves == 0) {
				backward.setEnabled(false);
			}

			populateUIPoints_oneStep();

			repaint();
			embedCanvas.repaint();

		}
	}

	void populateUIPoints_multistep(Set<Point> eatenPoints) {
		UIPoint uPoint;
		byte[][] matrixState = go.getMatrixState();
		int color;
		int eatenColor;
		List<Step> steps = manual.getSteps();
		for (int i = 0; i < moves; i++) {
			Step step = steps.get(i);
			color = matrixState[step.getPoint().getRow()][step.getPoint()
					.getColumn()];
			// 如果这一步（点）没有被提吃的话。(显示其到目前为止最终的颜色)
			if (color == Constant.BLACK || color == Constant.WHITE) {
				uPoint = new UIPoint();
				uPoint.setColor(step.getColor());
				// 前谱的棋子不再显示数字
				if (i >= startMove) {
					// uPoint.setMoveNumber(i + 1 - startMove);
					uPoint.setMoveNumber(i + 1);
				}
				uPoint.setPoint(step.getPoint());
				points.add(uPoint);
			} else if (eatenPoints.contains(step.getPoint())) {// 如果过程中有提子的话,提子需要特别显示.
				// 在当前谱被提吃.
				uPoint = new UIPoint();
				uPoint.setColor(step.getColor());
				// 前谱的棋子不再显示数字
				if (i >= startMove) {
					// uPoint.setMoveNumber(i + 1 - startMove);
					uPoint.setMoveNumber(i + 1);
				}
				uPoint.setPoint(step.getPoint());
				uPoint.setEaten(true);
				points.add(uPoint);
			}
		}
		handleInitState();
	}

	/**
	 * // 前进到有提子的情况出现。 实验另一种策略. 前进到落子于被提子块的局面.
	 * 
	 * @author Eddie
	 * 
	 */
	class ForwardManualActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			List<Step> steps = manual.getSteps();
			points.clear();
			Step step;

			int count = 1;
			startMove = moves;
			sectionStart.add(moves);
			backwardManual.setEnabled(true);
			Set<Block> eatenBlocks = null;
			Set<Point> eatenPoints = new HashSet<Point>();

			while (moves < steps.size()
					&& count < Constant.MAX_STEPS_IN_ONE_MANUAL_SHOW) {
				step = steps.get(moves++);
				if (eatenPoints.isEmpty() == false
						&& eatenPoints.contains(step.getPoint())) {
					moves--;
					break;
				}
				go.oneStepForward(step);
				count++;
				// eatenBlocks = go.getStepHistory().getStep(moves - 1)
				// .getEatenBlocks();
				// if (eatenBlocks.isEmpty() == false) {
				// for (Block eatenBlock : eatenBlocks) {
				// eatenPoints.addAll(eatenBlock.getAllPoints());
				// }
				// }
				Set<Point> eatenPoints2 = go.getEatenPoints();
				if (eatenPoints2 != null)
					eatenPoints.addAll(eatenPoints2);

			}
			if (moves == steps.size()) {
				forwardManual.setEnabled(false);
				forward.setEnabled(false);
			}

			populateUIPoints_multistep(eatenPoints);

			repaint();
			embedCanvas.repaint();
		}
	}

	/**
	 * @deprecated
	 * @author Eddie
	 * 
	 */
	class ForwardManualActionListener2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			List<Step> steps = manual.getSteps();
			points.clear();
			Step step;

			int count = 1;
			startMove = moves;
			sectionStart.add(moves);
			backwardManual.setEnabled(true);
			Set<Block> eatenBlocks = null;
			while (moves < steps.size()
					&& count < Constant.MAX_STEPS_IN_ONE_MANUAL_SHOW) {
				step = steps.get(moves++);
				go.oneStepForward(step);
				count++;
				// eatenBlocks = go.getStepHistory().getStep(moves - 1)
				// .getEatenBlocks();
				// if (eatenBlocks.isEmpty() == false) {
				// break;
				// }
				if (go.getEatenPoints() != null)
					break;
			}
			if (moves == steps.size()) {
				forwardManual.setEnabled(false);
				forward.setEnabled(false);
			}
			UIPoint uPoint;
			byte[][] matrixState = go.getMatrixState();
			int color;
			int eatenColor;

			// 如果最后一步有提子的话,提子需要特别显示.
			Set<Point> eatenPoints = new HashSet<Point>();
			for (Block eatenBlock : eatenBlocks) {
				eatenPoints.addAll(eatenBlock.getPoints());
				eatenColor = eatenBlock.getColor();
			}
			// for (int i = 0; i < moves; i++) {
			// step = steps.get(i);
			// color = matrixState[step.getPoint().getRow()][step.getPoint()
			// .getColumn()];
			// // 如果这一步（点）没有被提吃的话。
			// if (color == Constant.BLACK || color == Constant.WHITE) {
			// uPoint = new UIPoint();
			// uPoint.setColor(step.getColor());
			// // 前谱的棋子不再显示数字
			// if (i >= startMove) {
			// // uPoint.setMoveNumber(i + 1 - startMove);
			// uPoint.setMoveNumber(i + 1);
			// }
			// uPoint.setPoint(step.getPoint());
			// points.add(uPoint);
			// } else if (eatenPoints.contains(step.getPoint())) {
			// // 在当前谱被提吃.
			// uPoint = new UIPoint();
			// uPoint.setColor(step.getColor());
			// // 前谱的棋子不再显示数字
			// if (i >= startMove) {
			// // uPoint.setMoveNumber(i + 1 - startMove);
			// uPoint.setMoveNumber(i + 1);
			// }
			// uPoint.setPoint(step.getPoint());
			// uPoint.setEaten(true);
			// points.add(uPoint);
			// }
			// }
			populateUIPoints_multistep(eatenPoints);
			repaint();
			embedCanvas.repaint();
		}
	}

	/**
	 * TODO:
	 * 
	 * @author wueddie-wym-wrz
	 * 
	 */
	class BackwardManualActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			List<Step> steps = manual.getSteps();
			points.clear();
			forwardManual.setEnabled(true);
			forward.setEnabled(true);
			Step step;
			// 后退到上一谱。
			int count = 1;
			startMove = sectionStart.remove(sectionStart.size() - 1);
			if (sectionStart.isEmpty()) {
				backwardManual.setEnabled(false);
			}
			int endMove = moves;// inclusive
			while (moves > startMove) {
				step = steps.get(--moves);
				go.oneStepBackward();
				count++;
			}
			if (log.isInfoEnabled())
				log.info("back ward " + count + "steps.");

			UIPoint uPoint;
			byte[][] matrixState = go.getMatrixState();
			int color;

			for (int i = 0; i < endMove; i++) {
				step = steps.get(i);
				color = matrixState[step.getPoint().getRow()][step.getPoint()
						.getColumn()];
				// 如果这一步（点）没有被提吃的话。
				if (color == Constant.BLACK || color == Constant.WHITE) {
					uPoint = new UIPoint();
					uPoint.setColor(step.getColor());
					// 前谱的棋子不再显示数字
					if (i >= startMove) {
						uPoint.setMoveNumber(i + 1 - startMove);
					}
					uPoint.setPoint(step.getPoint());
					points.add(uPoint);
				}
			}

			repaint();
			embedCanvas.repaint();
		}
	}

	private Frame parent = this;

	/**
	 * 载入棋谱
	 * 
	 * @author Eddie
	 * 
	 */
	class LoadActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// 载入局面
			FileDialog fd = new FileDialog(parent, "载入局面的位置", FileDialog.LOAD);
			fd.setFile("1.wjm");
			fd.setDirectory(Constant.rootDir);
			fd.show();

			String inname = fd.getFile();
			String dir = fd.getDirectory();
			if (inname == null || inname.isEmpty())
				return;
			manual = SGFGoManual.loadSimpleGoManual(dir + inname);
			go = new GoBoard(manual.getInitState());
			embedCanvas.setBoardSize(manual.getBoardSize());
			init();
			blackPlayerV.setText(manual.getBlackName());
			whitePlayerV.setText(manual.getWhiteName());
			resultV.setText(manual.getResult());
			shoushuV.setText(manual.getShouShu() + "");

			points.clear();
			populateUIPoints_oneStep();

			// go = new
			// GoBoard256(manual.getInitBlacks(),manual.getInitWhites());
			// steps.clear();
			// int color = Constant.BLACK;
			// for (Point point : manual.getSteps()) {
			// Step step = new Step();
			// step.setColor(color);
			// if (color == Constant.BLACK)
			// color = Constant.WHITE;
			// else if (color == Constant.WHITE)
			// color = Constant.BLACK;
			// step.setPoint(point);
			// steps.add(step);
			// }

			log.debug("载入局面");
			repaint();
			// have to repaint canvas!
			embedCanvas.repaint();

		}
	}
}
