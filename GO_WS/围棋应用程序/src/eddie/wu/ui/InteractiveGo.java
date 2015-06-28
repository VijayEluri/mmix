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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardSymmetry;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.SymmetryResult;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.ui.canvas.ReviewManualCanvas;

/**
 * copy from review manual. <br/>
 * intention: to support tree go manual. <br/>
 * 1. to show search result.<br/>
 * a. for positive result, computer play first to justify its result. no matter
 * how player respond, computer have to ensure it reach the target result.<br/>
 * b. for negative result. (cannot reach target for first player) computer play
 * second to show all candidates does not work.<br/>
 * 2. it also support the problem solving for novice; the novice player first,
 * computer try to defeat the novice with all possible tricks.<br/>
 * Note current search result does not support feature 2 yet. in case novice
 * make a wrong play, we don't know how to respond yet. we may search
 * dynamically on the fly.<br/>
 * TODO: initialize the score in each step
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class InteractiveGo extends Frame {
	private static final Logger log = Logger.getLogger(InteractiveGo.class);
	static {
		log.setLevel(Level.WARN);
	}
	private Frame parent = this;
	/**
	 * domain object.
	 */
	private GoBoard go;// for showing current state
	private TreeGoManual manual;// all variant

	private Object immutable = new Object();

	// share data between UI and back end.
	private List<UIPoint> points = new ArrayList<UIPoint>();

	private boolean computerFirst = true;
	private volatile boolean manTurn = false;

	private synchronized void setManTurn(boolean manTurn) {
		this.manTurn = manTurn;
	}

	/**
	 * UI elements
	 */
	private ReviewManualCanvas embedCanvas = new ReviewManualCanvas(
			Constant.BOARD_SIZE);
	private Button load = new Button("载入棋谱");
	private Button forward = new Button("下一步");
	private Button backward = new Button("后退");
	/**
	 * 更加人性化的显示方式，自动以提子为分割，一次显示若干手数。<br/>
	 * 每次不超过100手，这样数字不会超过两位。
	 */
	Button forwardManual = new Button("下一谱");
	Button backwardManual = new Button("后退");

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

		InteractiveGo weiqi = new InteractiveGo();
		weiqi.setVisible(true);
		weiqi.setBounds(0, 0, 800, 600);
		weiqi.manual = SGFGoManual.loadTreeGoManual(fileName).get(0);

		weiqi.blackPlayerV.setText(weiqi.manual.getBlackName());
		weiqi.whitePlayerV.setText(weiqi.manual.getWhiteName());
		weiqi.resultV.setText(weiqi.manual.getResult());
		// weiqi.shoushuV.setText(weiqi.manual.getShouShu() + "");

		weiqi.go = new GoBoard(weiqi.manual.getInitState());

	}

	/**
	 * 
	 * @param state
	 *            the final board status
	 * @param steps
	 *            the history of steps
	 */
	public InteractiveGo() {

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
		backward.addActionListener(new BackwardActionListener());
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

		backwardManual.setEnabled(true);
		backward.setEnabled(true);
		blackPlayerV.setEditable(false);
		whitePlayerV.setEditable(false);
		resultV.setEditable(false);
		shoushuV.setEditable(false);

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

		this.setTitle("交互演示死活题");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}

		});
		// thread.start();
	}

	/**
	 * it seems not easy to get multiple thread right, even just to wait 2
	 * seconds after man's play. so I implement simple version first, just
	 * display man's move and computer's response together. it will be not nice
	 * if some block is eaten in between.<br/>
	 * 接受鼠标输入
	 */
	public boolean mouseDown(Event e, int x, int y) {
		if (manTurn == false) {
			return true;
		}
		x -= 30;
		y -= 30;
		int a = (x - 4) / 28 + 1;
		int b = (y - 4) / 28 + 1;

		// coordinate difference between matrix and plane..
		int row = b;
		int column = a;
		if (Point.isValid(go.boardSize, row, column) == false) {
			return true;
		}

		Point point = Point.getPoint(go.boardSize, row, column);
		boolean validate = go.validate(row, column);
		if (validate == false) {
			log.warn("Invalid step:" + point);
			return true;
		}

		// record symmetry before forwarding
		SymmetryResult symmetryResult = go.getSymmetryResult();

		// for further calculation on the fly.
		BoardColorState oldState = go.getBoardColorState();

		validate = go.oneStepForward(point);
		if (validate == false) {
			log.warn("Invalid step:" + point);
			// should step back internally! we did not do that because we do not
			// want forward class to depend on backward class!
			if (point.equals(go.getLastPoint())) {
				go.oneStepBackward();
				log.warn("One step back at " + point);
				return true;
			}
		}

		log.warn("Man Play " + oldState.getWhoseTurnString() + " at " + point);
		// valid move, response mode.
		this.setManTurn(false);

		// copy since we will convert by symmetry.
		Step childMove = go.getLastStep().getStep().getCopy();

		// reach an final state, need to load its tree
		if (manual.getCurrent().getChild() == null) {
			log.warn("Need to calculate response of :"
					+ childMove.toNonSGFString() + " on the fly.");

			// try another solution: load another file
			if (go.boardSize != 3) {
				return true;// no way to response.
			} else {

				String name = oldState.getStateAsOneLineString();
				String fileName1 = Constant.rootDir + "smallboard/threethree/"
						+ name + "win.sgf";
				String fileName2 = Constant.rootDir + "smallboard/threethree/"
						+ name + "lose.sgf";
				if (computerFirst) {
					if (new File(fileName1).exists()) {
						System.out.println("File name" + fileName1);
						TreeGoManual manual2 = SGFGoManual.loadTreeGoManual(
								fileName1).get(0);
						SearchNode temp = manual2.getRoot().getChild();
						manual.getCurrent().addChild(temp);
						log.warn("Add child " + temp.getStep().toNonSGFString());
						log.warn("Loading known WIN result: " + fileName1);
					} else {
						System.err.println("File not exist " + fileName1);
						return true;
					}
				} else {
					if (new File(fileName2).exists()) {
						System.out.println("File name" + fileName2);
						TreeGoManual manual2 = SGFGoManual.loadTreeGoManual(
								fileName2).get(0);
						SearchNode temp = manual2.getRoot().getChild();
						while (temp != null) {
							manual.getCurrent().addChild(temp);
							log.warn("Add child "
									+ temp.getStep().toNonSGFString());
							temp = temp.getBrother();
						}
						log.warn("Loading known LOSE result: " + fileName2);
					} else {
						System.err.println("File not exist " + fileName1);
						return true;
					}
				}
			}
		}

		if (manual.getCurrent().containsChildMove(childMove)) {
			// short cut path
			manual.navigateToChild(childMove);
			log.warn("Manual contains move " + childMove.toNonSGFString());
		} else {
			log.warn("Manual doesn't contains move "
					+ childMove.toNonSGFString());
			log.warn("Manual contains move "
					+ manual.getCurrent().getChild().getStep().toNonSGFString());
			if (symmetryResult.getNumberOfSymmetry() > 0) {

				if (manual.getCurrent().containsChildMove_mirrorSubTree(
						symmetryResult, childMove.getPoint())) {
					log.warn("After mirroring, Manual contains move "
							+ childMove.toNonSGFString());
					manual.navigateToChild(childMove);
				} else {
					log.warn("Manual does not contain move " + childMove
							+ " Strange!");
					go.oneStepBackward();
					return true;
				}
			}
		}

		SearchNode child = manual.getCurrent().getChild();
		if (child == null) {
			log.error("Computer has no choice; Strange! pass");
			go.giveUp(manual.getCurrent().getStep().getEnemyColor());
		} else {
			Step response = child.getStep();
			manual.navigateToChild(response);
			log.warn("response at " + response.toNonSGFString());
			go.oneStepForward(response);
		}
		go.initUIPoint(points);
		repaint();
		embedCanvas.repaint();
		this.setManTurn(true);
		return true;

	}

	class BackwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (computerFirst && go.getShoushu() == 1) {
				return;// keep computer's first auto play.
			} else {
				go.oneStepBackward();
				go.oneStepBackward();
				manual.up();
				manual.up();
			}
			repaint_complete();
			// embedCanvas.repaint();

		}
	}

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
			fd.setFile("*.sgf");
			fd.setDirectory(Constant.rootDir);
			fd.show();

			String inname = fd.getFile();
			String dir = fd.getDirectory();
			if (inname == null || inname.isEmpty())
				return;
			if (inname.toLowerCase().endsWith("lose.sgf")) {
				computerFirst = false;
				manTurn = true;
			} else {
				computerFirst = true;
			}

			manual = SGFGoManual.loadTreeGoManual(dir + inname).get(0);
			go = new GoBoard(manual.getInitState());

			if (log.isEnabledFor(Level.WARN)) {
				log.info("载入局面");
				log.warn(go.getBoardColorState().getStateString());
				log.warn(manual.getSGFBodyString(false));
			}
			blackPlayerV.setText(manual.getBlackName());
			whitePlayerV.setText(manual.getWhiteName());
			resultV.setText(manual.getResult());

			if (computerFirst == true && manual.isEmpty() == false) {
				Step firstStep = manual.getRoot().getChild().getStep();
				boolean valid = go.oneStepForward(firstStep);
				if (log.isEnabledFor(Level.WARN))
					log.warn("correct color = " + manual.getInitTurn());
				if (valid == false) {
					log.warn("Wrong first step:" + firstStep.toNonSGFString());
				} else {
					log.warn("Computer play at:" + firstStep.toNonSGFString());
				}
				manual.navigateToChild(firstStep);
				if (log.isEnabledFor(Level.WARN))
					log.warn("computer played the first step " + firstStep);
				log.warn("");
				manTurn = true;
			}

			repaint_complete();

		}
	}

	public void repaint_complete() {
		go.initUIPoint(points);
		embedCanvas.setBoardSize(go.boardSize);
		repaint();
		embedCanvas.repaint();
		// TODO Auto-generated method stub

	}
}
// TODO:
// TODO: dynamic code here.
// if (go.boardSize == 3) {
// // need to store result score!
// int score = 4;
// int high = score;
// int low = score;
// if (computerFirst) {
// if (manual.getInitTurn() == Constant.BLACK) {
// low = high - 1;
// }else{
// high = score+1;
// }
// }else{
//
// }
// ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(
// oldState, high, low);
// }