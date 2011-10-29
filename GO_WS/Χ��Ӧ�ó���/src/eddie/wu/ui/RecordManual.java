package eddie.wu.ui;

import java.awt.Button;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.StepMemo;
import eddie.wu.domain.UIPoint;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.ui.canvas.ReviewManualCanvas;

public class RecordManual extends Frame {
	private static String rootDir = Constant.rootDir; // "doc/围棋打谱软件/";
	private static final Log log = LogFactory.getLog(RecordManual.class);
	private Frame parent = this;
	/*
	 * domain object.
	 */
	private GoBoard go = new GoBoard();
	private List<UIPoint> points = new ArrayList<UIPoint>();
	// private List<Step> steps = new ArrayList<Step>();
	/*
	 * UI elements
	 */
	private ReviewManualCanvas embedCanvas = new ReviewManualCanvas();
	private Button forward = new Button("存储");// 前进
	private Button store = new Button("存储");// 前进
	private Button backward = new Button("悔棋");// 后退

	public static void main(String[] args) {
		if (args.length > 1) {
			rootDir = args[1];
		}

		RecordManual weiqi = new RecordManual();
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
	public RecordManual() {

		embedCanvas.setPoints(points);
		embedCanvas.setVisible(true);

		add(embedCanvas);
		add(forward);
		add(backward);
		add(store);

		// forward.addActionListener(new ForwardActionListener());
		backward.addActionListener(new BackwardActionListener());
		store.addActionListener(new StoreActionListener());
		
		// forward.setVisible(true);
		backward.setVisible(true);
		store.setVisible(true);

		forward.setEnabled(false);
		backward.setEnabled(false);
		setLayout(null);

		embedCanvas.setBounds(30, 30, 560, 560);
		forward.setBounds(600, 100, 100, 30);
		backward.setBounds(600, 160, 100, 30);
		store.setBounds(600, 220, 100, 30);

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
		int row = b;
		int column = a;
		go.oneStepForward(Point.getPoint(row, column));
		points.clear();

		UIPoint uPoint;
		byte[][] matrixState = go.getBoardColorState().getMatrixState();
		int color;
		StepMemo step;
		List<StepMemo> steps2 = go.getStepHistory().getAllSteps();

		for (int i = 0; i < steps2.size(); i++) {
			step = steps2.get(i);
			color = matrixState[step.getCurrentStepPoint().getRow()][step
					.getCurrentStepPoint().getColumn()];
			// 如果这一步（点）没有被提吃的话。
			if (color == Constant.BLACK || color == Constant.WHITE) {
				uPoint = new UIPoint();
				uPoint.setColor(step.getColor());
				uPoint.setMoveNumber(i + 1);
				uPoint.setPoint(step.getCurrentStepPoint());
				points.add(uPoint);
			} else {
				// TODO: better way to display eaten point.
			}
		}

		repaint();
		embedCanvas.repaint();
		return true;
	}

	class StoreActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			FileDialog fd = new FileDialog(parent, "保存局面的位置", FileDialog.SAVE);
			fd.setFile("1.sgf");
			fd.setDirectory(rootDir);
			fd.show();

			String outname = fd.getFile();
			String dir = fd.getDirectory();
			log.debug(outname);
			try {
				DataOutputStream out = new DataOutputStream(
						new BufferedOutputStream(new FileOutputStream(dir
								+ outname)));
				GoManual manual = new GoManual();
				Point step;
				for (StepMemo memo : go.getStepHistory().getAllSteps()) {
					manual.addStep(memo.getCurrentStepPoint());
				}
				SGFGoManual.writeGoManual(out, manual);
				log.debug(out);

				out.flush();
				out.close();
			}

			catch (IOException ex) {
				log.debug("the output meet some trouble!");
				log.debug("Exception" + ex.toString());
				throw new RuntimeException(ex);
			}

			log.debug("保存局面");

		}

	}

	class BackwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			forward.setEnabled(true);
			points.clear();

			go.oneStepBackward();
			if (go.getShoushu() == 0) {
				backward.setEnabled(false);
			}

			UIPoint uPoint;
			List<StepMemo> steps2 = go.getStepHistory().getAllSteps();
			StepMemo step;
			byte[][] matrixState = go.getBoardColorState().getMatrixState();
			int color;
			for (int i = 0; i < steps2.size(); i++) {
				step = steps2.get(i);
				color = matrixState[step.getCurrentStepPoint().getRow()][step
						.getCurrentStepPoint().getColumn()];
				if (color == Constant.BLACK || color == Constant.WHITE) {
					uPoint = new UIPoint();
					uPoint.setColor(step.getColor());
					uPoint.setMoveNumber(i + 1);
					uPoint.setPoint(step.getCurrentStepPoint());
					points.add(uPoint);
				}
			}
			repaint();
			embedCanvas.repaint();

		}
	}
}
