package eddie.wu.ui;

import java.awt.Button;
import java.awt.Event;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.UIPoint;
import eddie.wu.manual.LoadGoManual;
import eddie.wu.ui.canvas.ReviewManualCanvas;

/**
 * 简单的打谱功能，只能展示线性的常规棋谱，不能展示变化；变化也许可以在注释中描述。<br/>
 * button 1: 前进一步。<br/>
 * button 2: 后退一步。<br/>
 * 默认显示数字<br/>
 * TODO：左右方向键可以后退和前进。（如果不喜欢鼠标操作的话，可以放松一下） 2011.10.6 play game<br/>
 * 吴瑞泽想学习编程，想编个世界上最伟大的愤怒小鸟。 好好学习，天天向上。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class ReviewManual extends Frame {
	private static String rootDir = Constant.rootDir; // "doc/围棋打谱软件/";
	private static final Log log = LogFactory.getLog(ReviewManual.class);

	/*
	 * domain object.
	 */
	private GoBoard go = new GoBoard();
	private List<UIPoint> points = new ArrayList<UIPoint>();
	private List<Step> steps = new ArrayList<Step>();
	private int moves = 0;//
	private int startMove = 0;//
	/*
	 * UI elements
	 */
	private ReviewManualCanvas embedCanvas = new ReviewManualCanvas();
	private Button forward = new Button("下一步");// 前进
	private Button backward = new Button("上一步");// 后退
	/**
	 * 更加人性化的显示方式，自动以提子为分割，一次显示若干手数。 每次不超过100手，这样数字不会超过两位。
	 */
	Button forwardManual = new Button("下一谱");// 前进
	Button backwardManual = new Button("上一谱");// 后退

	public static void main(String[] args) {
		if (args.length > 1) {
			rootDir = args[1];
		}
		byte[] stepArray = new LoadGoManual(rootDir).loadSingleGoManual();
		List<Step> steps = new ArrayList<Step>();
		int color = 0;
		for (int i = 0; i < stepArray.length / 2; i++) {
			if (i % 2 == 0) {
				color = Constant.BLACK;
			} else {
				color = Constant.WHITE;
			}
			Step step = new Step();
			step.setColor(color);
			step.setPoint(Point
					.getPoint(stepArray[2 * i], stepArray[2 * i + 1]));
			steps.add(step);
		}

		ReviewManual weiqi = new ReviewManual(steps);
		weiqi.setVisible(true);
		weiqi.setBounds(0, 0, 800, 600);

	}

	/**
	 * 
	 * @param state
	 *            the final board status
	 * @param steps
	 *            the history of steps
	 */
	public ReviewManual(List<Step> steps) {
		this.steps = steps;

		embedCanvas.setPoints(points);
		embedCanvas.setVisible(true);

		add(embedCanvas);
		add(forward);
		add(backward);
		add(forwardManual);
		add(backwardManual);
		forward.addActionListener(new ForwardActionListener());
		backward.addActionListener(new BackwardActionListener());
		forwardManual.addActionListener(new ForwardManualActionListener());
		backwardManual.addActionListener(new BackwardManualActionListener());
		forward.setVisible(true);
		backward.setVisible(true);
		forwardManual.setVisible(true);
		backwardManual.setVisible(true);
		setLayout(null);

		embedCanvas.setBounds(30, 30, 560, 560);
		forward.setBounds(600, 100, 100, 30);
		backward.setBounds(600, 160, 100, 30);
		forwardManual.setBounds(600, 220, 100, 30);
		backwardManual.setBounds(600, 280, 100, 30);
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
		System.out.println("weiqiFrame de mousedown");
		// coordinate difference between matrix and plane.

		repaint();
		embedCanvas.repaint();
		return true;
	}

	class ForwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			points.clear();
			Step step = steps.get(moves++);
			go.oneStepForward(step);
			UIPoint uPoint;
			byte[][] matrixState = go.getBoardColorState().getMatrixState();
			int color;
			for (int i = 0; i < moves; i++) {
				step = steps.get(i);
				color = matrixState[step.getPoint().getRow()][step.getPoint()
						.getColumn()];
				// 如果这一步（点）没有被提吃的话。
				if (color == Constant.BLACK || color == Constant.WHITE) {
					uPoint = new UIPoint();
					uPoint.setColor(step.getColor());
					uPoint.setMoveNumber(i + 1);
					uPoint.setPoint(step.getPoint());
					points.add(uPoint);
				}
			}
			repaint();
			embedCanvas.repaint();
		}
	}

	class BackwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			points.clear();
			Step step = steps.get(--moves);
			go.oneStepBackward(step.getPoint());
			UIPoint uPoint;
			byte[][] matrixState = go.getBoardColorState().getMatrixState();
			int color;
			for (int i = 0; i < moves; i++) {
				step = steps.get(i);
				color = matrixState[step.getPoint().getRow()][step.getPoint()
						.getColumn()];
				if (color == Constant.BLACK || color == Constant.WHITE) {
					uPoint = new UIPoint();
					uPoint.setColor(step.getColor());
					uPoint.setMoveNumber(i + 1);
					uPoint.setPoint(step.getPoint());
					points.add(uPoint);
				}
			}
			repaint();
			embedCanvas.repaint();

		}
	}

	class ForwardManualActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			points.clear();
			Step step;
			// 前进到有提子的情况出现。
			int count = 1;
			startMove = moves;
			while (moves < steps.size()
					&& count < Constant.MAX_STEPS_IN_ONE_MANUAL_SHOW) {
				step = steps.get(moves++);
				go.oneStepForward(step);
				count++;
				if (go.getStepHistory().getStep(moves-1).getEatenBlocks()
						.isEmpty() == false) {
					break;
				}
			}
			if(moves==steps.size()){
				forwardManual.setEnabled(false);
			}
			UIPoint uPoint;
			byte[][] matrixState = go.getBoardColorState().getMatrixState();
			int color;

			for (int i = 0; i < moves; i++) {
				step = steps.get(i);
				color = matrixState[step.getPoint().getRow()][step.getPoint()
						.getColumn()];
				// 如果这一步（点）没有被提吃的话。
				if (color == Constant.BLACK || color == Constant.WHITE) {
					uPoint = new UIPoint();
					uPoint.setColor(step.getColor());
					//前谱的棋子不再显示数字
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

	/**
	 * TODO: 
	 * @author wueddie-wym-wrz
	 *
	 */
	class BackwardManualActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}
}
