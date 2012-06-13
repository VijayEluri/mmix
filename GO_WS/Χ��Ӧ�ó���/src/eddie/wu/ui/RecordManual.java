package eddie.wu.ui;

import java.awt.Button;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.StepMemo;
import eddie.wu.domain.UIPoint;
import eddie.wu.domain.conn.ConnectivityAnalysis;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.ui.canvas.ReviewManualCanvas;

/**
 * baby存储
 * 
 * baby中国(D6D0 B9FA) baby存储(B4E6 B4A2) 在class 文件中. 瀛樺偍(E5AD98 E582A8)
 * 
 * Run configurations: -Dfile.encoding=UTF-8(default value in my setting.)<br/>
 * cannot display Chinese in button.<br/>
 * -Dfile.encoding=GBK works; do not understand why?<br/>
 * baby
 * 
 * @author Eddie
 * 
 */
public class RecordManual extends Frame {

	private static String rootDir = Constant.rootDir; // "doc/围棋打谱软件/";
	private static final Logger log = Logger.getLogger(RecordManual.class);
	private Frame parent = this;
	/*
	 * domain object.
	 */
//	private GoBoard go = new GoBoard(Constant.SMALL_BOARD_SIZE);
	 private GoBoard go = new GoBoard(Constant.BOARD_SIZE);
	private List<UIPoint> points = new ArrayList<UIPoint>();
	// private List<Step> steps = new ArrayList<Step>();
	/*
	 * UI elements
	 */
	private ReviewManualCanvas embedCanvas = new ReviewManualCanvas(
			go.boardSize);
	private Button forward = new Button("前进");// 前进
	private Button store = new Button("保存");// 存储
	private Button backward = new Button("悔棋");// 悔棋
	private Button giveup = new Button("弃权");// 弃权
	private Button loadState = new Button("载入局面");

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
		add(giveup);
		add(loadState);
		// forward.addActionListener(new ForwardActionListener());
		backward.addActionListener(new BackwardActionListener());
		store.addActionListener(new StoreActionListener());
		giveup.addActionListener(new GiveupActionListener());
		loadState.addActionListener(new LoadStateActionListener(this));
		// forward.setVisible(true);
		backward.setVisible(true);
		store.setVisible(true);
		giveup.setVisible(true);
		loadState.setVisible(true);

		forward.setEnabled(false);
		backward.setEnabled(false);
		setLayout(null);

		embedCanvas.setBounds(30, 30, 560, 560);
		forward.setBounds(600, 100, 100, 30);
		backward.setBounds(600, 160, 100, 30);
		store.setBounds(600, 220, 100, 30);
		giveup.setBounds(600, 280, 100, 30);
		loadState.setBounds(600, 340, 100, 30);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}

		});
		if(log.isDebugEnabled()) log.debug("baby存储");
		Locale local = this.getLocale();
		this.setLocale(Locale.CHINESE);
		local = this.getLocale();
		if(log.isDebugEnabled()) log.debug(local.getCountry());
		if(log.isDebugEnabled()) log.debug(local.toString());
	}

	public boolean mouseDown(Event e, int x, int y) { // 接受鼠标输入
		if (log.isDebugEnabled()) {
			log.debug("chuan bo dao rong qi - forward one step.");
		}
		x -= 30;
		y -= 30;

		byte a = (byte) ((x - 4) / 28 + 1);// 完成数气提子等.
		byte b = (byte) ((y - 4) / 28 + 1);
		if(log.isDebugEnabled()) log.debug("weiqiFrame de mousedown");
		int row = b;
		int column = a;
		if (Point.isNotValid(go.boardSize, row, column))
			return true;

		go.oneStepForward(Point.getPoint(go.boardSize, row, column));
		this.backward.setEnabled(true);
		points.clear();

		getUIPoint();

		repaint();
		embedCanvas.repaint();
		return true;
	}

	public void getUIPoint() {
		UIPoint uPoint;
		byte[][] matrixState = go.getBoardColorState().getMatrixState();
		int color;
		StepMemo step;
		List<StepMemo> steps2 = go.getStepHistory().getAllSteps();
		// 显示初始状态的子
		int boardSize = go.boardSize;
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				color = matrixState[row][column];
				if (go.getInitState() != null
						&& color == go.getInitState()[row][column]) {
					uPoint = new UIPoint();
					uPoint.setColor(color);
					uPoint.setMoveNumber(0);
					uPoint.setPoint(Point.getPoint(boardSize, row, column));
					points.add(uPoint);
				}
			}
		}

		// 显示棋谱中的落子
		for (int i = 0; i < steps2.size(); i++) {
			step = steps2.get(i);
			if (step.isGiveup())
				continue;
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
	}

	class GiveupActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			go.giveUp();

		}

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
				GoManual manual = new GoManual(go.boardSize);
				Point step;
				for (StepMemo memo : go.getStepHistory().getAllSteps()) {
					manual.addStep(memo.getStep());
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
			getUIPoint();
			// UIPoint uPoint;
			// List<StepMemo> steps2 = go.getStepHistory().getAllSteps();
			// StepMemo step;
			// byte[][] matrixState = go.getBoardColorState().getMatrixState();
			// int color;
			// for (int i = 0; i < steps2.size(); i++) {
			// step = steps2.get(i);
			// color = matrixState[step.getCurrentStepPoint().getRow()][step
			// .getCurrentStepPoint().getColumn()];
			// if (color == Constant.BLACK || color == Constant.WHITE) {
			// uPoint = new UIPoint();
			// uPoint.setColor(step.getColor());
			// uPoint.setMoveNumber(i + 1);
			// uPoint.setPoint(step.getCurrentStepPoint());
			// points.add(uPoint);
			// }
			// }
			repaint();
			embedCanvas.repaint();

		}
	}

	class LoadStateActionListener implements ActionListener { // 载入局面。

		Frame parent;

		public LoadStateActionListener(Frame par) {
			parent = par;
		}

		private static final String path = "doc/围棋程序数据/死活题局面";
		public final byte[][] state = new byte[Constant.BOARD_MATRIX_SIZE][Constant.BOARD_MATRIX_SIZE]; // 0.4k;

		public void actionPerformed(ActionEvent e) {

			// 载入局面
			FileDialog fd = new FileDialog(parent, "载入局面的位置", FileDialog.LOAD);
			fd.setFile("1.wjm");
			fd.setDirectory(path);
			fd.show();

			String inname = fd.getFile();
			String dir = fd.getDirectory();

			try {
				DataInputStream in = new DataInputStream(
						new BufferedInputStream(new FileInputStream(dir
								+ inname)));

				byte[] buffer = new byte[1024 * 4];
				int count = in.read(buffer);
				byte a, b, c;
				for (int i = 0; i < count; i++) {
					a = buffer[i++];
					b = buffer[i++];
					c = buffer[i];
					state[a][b] = c;
				}
				in.close();
			}

			catch (IOException ex) {
				log.debug("the input meet some trouble!");
				log.debug("Exception" + ex.toString());
				throw new RuntimeException(ex);
			}

			log.debug("载入局面");
			go = new GoBoard(state);
			points.clear();
			getUIPoint();
			repaint();
			embedCanvas.repaint();

		}
	}

}
