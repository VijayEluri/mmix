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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.StepMemo;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.TreeGoManual;
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
 * <br/>
 * also support store variants.
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
	// private GoBoard go = new GoBoard(4);
	// private GoBoard go = new GoBoard(Constant.SMALL_BOARD_SIZE);
	private GoBoard go = new GoBoard(Constant.BOARD_SIZE);
	private UIBoard board;// = new UIBoard(Constant.BOARD_SIZE);
	private List<UIPoint> points = new ArrayList<UIPoint>();
	// private List<Step> steps = new ArrayList<Step>();
	/*
	 * UI elements
	 */
	private ReviewManualCanvas embedCanvas = new ReviewManualCanvas(
			go.boardSize);
	private Button forward = new Button("前进");// 前进
	private Button store = new Button("保存");// 存储
	private Button backward = new Button("后退");// 悔棋
	private Label backLable1 = new Label("到");
	private TextField step = new TextField();
	private Label backLable2 = new Label("步");
	private Button giveup = new Button("弃权");// 弃权
	private Button loadState = new Button("载入局面");
	private Button markDead = new Button("标记死子");
	private Button countScore = new Button("点目");
	/**
	 * 将当前局面保存起来。
	 */
	private Button saveState = new Button("保存当前局面");

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
	/**
	 * 
	 */
	public RecordManual() {

		embedCanvas.setPoints(points);
		embedCanvas.setVisible(true);

		add(embedCanvas);
		add(forward);
		add(backward);
		add(backLable1);
		add(step);
		add(backLable2);
		add(store);
		add(giveup);
		add(loadState);
		add(markDead);
		add(saveState);
		add(countScore);
		// forward.addActionListener(new ForwardActionListener());
		backward.addActionListener(new BackwardActionListener());
		store.addActionListener(new StoreActionListener());
		giveup.addActionListener(new GiveupActionListener());
		loadState.addActionListener(new LoadStateActionListener(this));
		markDead.addActionListener(new MarkDeadActionListener());
		saveState.addActionListener(new SaveStateActionListener(this));
		countScore.addActionListener(new CountScoreActionListener());
		// forward.setVisible(true);
		backward.setVisible(true);
		backLable1.setVisible(true);
		step.setVisible(true);
		backLable2.setVisible(true);
		store.setVisible(true);
		giveup.setVisible(true);
		loadState.setVisible(true);
		saveState.setVisible(true);

		forward.setEnabled(false);
		backward.setEnabled(false);
		setLayout(null);

		embedCanvas.setBounds(30, 30, 560, 560);
		forward.setBounds(600, 100, 100, 30);
		backward.setBounds(600, 160, 100, 30);
		backLable1.setBounds(700, 160, 25, 30);
		step.setBounds(730, 160, 30, 30);
		backLable2.setBounds(780, 160, 25, 30);
		store.setBounds(600, 220, 100, 30);
		giveup.setBounds(600, 280, 100, 30);
		loadState.setBounds(600, 340, 100, 30);
		saveState.setBounds(600, 400, 100, 30);
		markDead.setBounds(600, 460, 100, 30);
		countScore.setBounds(600, 520, 100, 30);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}

		});
		if (log.isDebugEnabled())
			log.debug("baby存储");
		Locale local = this.getLocale();
		this.setLocale(Locale.CHINESE);
		local = this.getLocale();
		if (log.isDebugEnabled())
			log.debug(local.getCountry());
		if (log.isDebugEnabled())
			log.debug(local.toString());
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
		int row = b;
		int column = a;
		if (Point.isNotValid(go.boardSize, row, column))
			return true;
		Point pointTemp = Point.getPoint(go.boardSize, row, column);

		switch (context) {
		case 0:
			if (go.getColor(pointTemp) == Constant.BLANK) {
				boolean success = go.oneStepForward(pointTemp);
				if (success == false) {
					System.err.println("Invalid step!");
					go.oneStepBackward();
					return true;
				}
				this.backward.setEnabled(true);

				SearchNode var = go.getCurrent().getChild();
				if (var != null) {
					if(log.isEnabledFor(Level.WARN)) log.warn("after play at " + pointTemp
							+ ", known candidate:");
					while (var != null) {
						if(log.isEnabledFor(Level.WARN)) log.warn(var.getStep());
						var = var.getBrother();
					}
				}
				// only can back to last step.
				step.setText((go.getLastStep().getStep().getIndex() - 1) + "");
			} else {// restore to the specified step.
				Step lastStep = go.getStepHistory().getLastStep(pointTemp);
				if (lastStep != null) {
					step.setText(lastStep.getIndex() + "");
				}
			}
			break;
		case Constant.MARK_DEAD:
			Block block = go.getBlock(pointTemp);
			if (block != null) {
				for (Point point : block.getPoints()) {
					board.getUIPoint(point).toggleDeadMark();
				}
				board.addDeadStones(block.getPoints());
			}
		}

		points.clear();
		getUIPoint();

		repaint();
		embedCanvas.repaint();
		return true;
	}

	public void getUIPoint(UIBoard board) {
		int boardSize = board.boardSize;
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				UIPoint point = board.getUIPoint(row, column);
				points.add(point);
				// if(point.isBlank()){
				//
				// }else {
				// points.add(point);
				// }
			}
		}
	}

	public void getUIPoint() {
		if (board != null) {
			getUIPoint(board);
			return;
		}
		go.initUIPoint(points);
	}

	class GiveupActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			go.giveUp();
			step.setText((go.getLastStep().getStep().getIndex() - 1) + "");

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
			TreeGoManual manualT = go.getTreeGoManual();
			if(log.isEnabledFor(Level.WARN)) log.warn(manualT.getSGFBodyString());
			if(log.isEnabledFor(Level.WARN)) log.warn(manualT.getExpandedString());

			try {
				DataOutputStream out = new DataOutputStream(
						new BufferedOutputStream(new FileOutputStream(dir
								+ outname)));
				SimpleGoManual manual = go.getSimpleGoManual();
				SGFGoManual.writeGoManual(out, manual);
				log.debug(out);

				out.flush();
				out.close();

				// store whole tree of the manual.
				if (outname.toLowerCase().contains("complex")) {
					out = new DataOutputStream(new BufferedOutputStream(
							new FileOutputStream(dir + outname)));
				} else {
					out = new DataOutputStream(new BufferedOutputStream(
							new FileOutputStream(dir + outname + ".complex")));
				}
				TreeGoManual manual2 = go.getTreeGoManual();

				SGFGoManual.writeGoManual(out, manual2);
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
			if (go.getShoushu() == 0) {
				// should not happen!
				backward.setEnabled(false);
				return;
			}

			forward.setEnabled(true);
			points.clear();
			for (int i = go.getShoushu(); i > Integer.valueOf(step.getText())
					.intValue(); i--) {
				go.oneStepBackward();
			}
			if (go.hasStep()) {
				step.setText((go.getLastStep().getStep().getIndex() - 1) + "");
			}
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
			if (dir == null || inname == null) {
				return;
			}
			String file = dir + inname;
			if (inname.toUpperCase().endsWith("SGF")) {
				SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(file);
				if(log.isEnabledFor(Level.WARN)) log.warn("载入局面SGF");
				if (go.boardSize != goManual.getBoardSize()) {
					go = new GoBoard(goManual.getInitState());
					/**
					 * We should not create new embedCanvas, because old
					 * instance is registered into Swing. new one is unknown to
					 * Swing! root cause of strange behavior.
					 */
					embedCanvas.setBoardSize(goManual.getBoardSize());
					if(log.isEnabledFor(Level.WARN)) log.warn("canvas size="
							+ embedCanvas.getBoardSize());
				} else {
					go = new GoBoard(goManual.getInitState());
					embedCanvas.setBoardSize(goManual.getBoardSize());
					if(log.isEnabledFor(Level.WARN)) log.warn("canvas size="
							+ embedCanvas.getBoardSize());
					if(log.isEnabledFor(Level.WARN)) log.warn("canvas size=" + go.boardSize);
				}
			} else if (inname.toUpperCase().endsWith("SGF.COMPLEX")) {
				TreeGoManual goManual = SGFGoManual.loadTreeGoManual(file).get(
						0);
				log.debug("载入局面SGF.complex");
				if (go.boardSize != goManual.getBoardSize()) {
					go = new GoBoard(goManual);
					embedCanvas = new ReviewManualCanvas(
							goManual.getBoardSize());
				} else {
					go = new GoBoard(goManual);
				}

			} else {

				try {
					DataInputStream in = new DataInputStream(
							new BufferedInputStream(new FileInputStream(file)));

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
			}

			points.clear();
			getUIPoint();
			repaint();
			embedCanvas.repaint();
			// embedCanvas.paint(g);
		}
	}

	/**
	 * 数目（点目）的界面设计如下：<br/>
	 * 1.自动标记死子。<br/>
	 * 2.如果自动标记有误，可以人工修改标记。<br/>
	 * 3.基于标记算目。<br/>
	 * 
	 * @author Eddie
	 * 
	 */
	private int context;

	class MarkDeadActionListener implements ActionListener { // 手动标记死子
		public void actionPerformed(ActionEvent e) {
			context = Constant.MARK_DEAD;
			board = new UIBoard(go);
		}
	}

	class SaveStateActionListener implements ActionListener {
		Frame parent;
		String path = "doc/围棋程序数据/死活题局面";

		public SaveStateActionListener(Frame par) {
			parent = par;
		}

		public void actionPerformed(ActionEvent e) {

			FileDialog fd = new FileDialog(parent, "保存局面的位置", FileDialog.SAVE);
			fd.setFile("1.wjm");
			fd.setDirectory(path);
			fd.show();

			String dir = fd.getDirectory();
			String outname = fd.getFile();
			if(log.isEnabledFor(Level.WARN)) log.warn(outname);
			if (dir == null || outname == null)
				return;

			String file = dir + outname;
			if (outname.toUpperCase().endsWith("SGF")) {
				SimpleGoManual goManual = go.getSimpleGoManual();
				SGFGoManual.storeGoManual(file, goManual);
				log.debug("保存局面－SGF格式");
			} else {
				StateAnalysis.saveState(go.getMatrixState(), dir + outname);
			}
		}

	}

	class CountScoreActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// go.cleanupDeadStone(board.getDeadStoneReps());
			BoardColorState colorState = go.getBoardColorState().getCopy();
			colorState.updateWithDeadStone(board.getDeadStoneReps());
			GoBoard goEnd = new GoBoard(colorState);
			if (log.isEnabledFor(Level.WARN))
				log.warn("net=" + goEnd.simpleCountScore());
			board = null;
			points.clear();
			getUIPoint();
			repaint();
			embedCanvas.repaint();
		}

	}

}
