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
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.SymmetryResult;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.small.ThreeThreeBoardSearch;
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
 * TODO: initialize the score in each step<br/>
 * 
 * <br/>
 * for record. get all results of 2*2 board search of initoal status. use this
 * UI to show all results. <br/>
 * 
 * 
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
	private GoBoard goBoard;// for showing current state
	private TreeGoManual manual;// all variant
	private boolean endWithManPlay;

	// private Object immutable = new Object();

	// share data between UI and back end.
	private List<UIPoint> points = new ArrayList<UIPoint>();

	private boolean computerFirst = true;
	private volatile boolean manTurn = false;

	private synchronized void setManTurn(boolean manTurn) {
		this.manTurn = manTurn;
	}

	/**
	 * UI elements. by default, man play first. 所谓练习（做题）模式<br/>
	 * but you can request computer to play first to show the tips. 所谓演示模式
	 */
	private ReviewManualCanvas embedCanvas = new ReviewManualCanvas(
			Constant.BOARD_SIZE);
	private Button load = new Button("载入题目");
	private Button pass = new Button("弃权");
	private Button backward = new Button("后退");

	Label resultLabel = new Label("正确结果");
	Label shoushuLabel = new Label("当前手数");
	TextField resultV = new TextField();
	TextField shoushuV = new TextField();

	public static void main(String[] args) {
		// C:\Users\think\Program\SCM_GIT\mmix\GO_WS\围棋应用程序\doc\围棋程序数据\smallboard\twotwo
		String fileName = Constant.rootDir + "smallboard/twotwo/"
				+ "Black __ __  win.sgf";
		InteractiveGo weiqi = new InteractiveGo();
		weiqi.setBounds(0, 0, 800, 600);
		weiqi.manual = SGFGoManual.loadTreeGoManual(fileName).get(0);
		weiqi.resultV.setText(weiqi.manual.getResult());
		// weiqi.shoushuV.setText(weiqi.manual.getShouShu() + "");

		weiqi.goBoard = new GoBoard(weiqi.manual.getInitState());
		weiqi.loadManual(fileName);
		weiqi.repaint_complete();
		weiqi.setVisible(true);
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
		add(pass);
		add(backward);
		add(resultLabel);
		add(shoushuLabel);
		add(resultV);
		add(shoushuV);
		load.addActionListener(new LoadActionListener());
		pass.addActionListener(new PassActionListener());
		backward.addActionListener(new BackwardActionListener());
		load.setVisible(true);
		pass.setVisible(true);
		backward.setVisible(true);
		resultLabel.setVisible(true);
		resultV.setVisible(true);
		shoushuLabel.setVisible(true);
		shoushuV.setVisible(true);
		backward.setEnabled(true);
		resultV.setEditable(false);
		shoushuV.setEditable(false);

		setLayout(null);

		embedCanvas.setBounds(30, 30, 560, 560);
		load.setBounds(600, 40, 100, 30);
		pass.setBounds(600, 100, 100, 30);
		backward.setBounds(600, 160, 100, 30);

		resultLabel.setBounds(600, 420, 80, 20);
		resultV.setBounds(645, 420, 100, 20);

		shoushuLabel.setBounds(600, 450, 80, 20);
		shoushuV.setBounds(645, 450, 100, 20);

		this.setTitle("交互演示围棋题");

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
		if (goBoard.areBothPass()) {
			System.out.println("Terminated by double pass! cannot move on!");
			return true;
		}
		Point point = null;
		if (x == -1 && y == -1) {
			// OK. pass.
		} else {
			x -= 30;
			y -= 30;
			int a = (x - 4) / 28 + 1;
			int b = (y - 4) / 28 + 1;

			// coordinate difference between matrix and plane..
			int row = b;
			int column = a;
			if (Point.isValid(goBoard.boardSize, row, column) == false) {
				return true;
			}

			point = Point.getPoint(goBoard.boardSize, row, column);

		}
		boolean validate = goBoard.validate(point);
		if (validate == false) {
			log.warn("Invalid step:" + point);
			return true;
		}
		backward.setEnabled(true);
		// record symmetry before forwarding
		SymmetryResult symmetryResult = goBoard.getSymmetryResult();
		log.warn(symmetryResult);

		// for further calculation on the fly.
		BoardColorState oldState = goBoard.getBoardColorState();

		validate = goBoard.oneStepForward(point);
		if (validate == false) {
			throw new RuntimeException(
					"impossible - we already check the state loop");
		}
		log.warn(Constant.lineSeparator);
		log.warn("Man Play " + oldState.getWhoseTurnString() + " at " + point);
		// valid move, response mode.
		this.setManTurn(false);

		// copy since we will convert by symmetry.
		Step childMove = goBoard.getLastStep().getStep().getCopy();

		// reach an final state, need to load its tree
		if (manual.getCurrent().getChild() == null) {
			log.warn("Need to calculate response of :"
					+ childMove.toNonSGFString() + " on the fly.");

			// try another solution: load another file
			if (goBoard.boardSize != 3) {
				goBoard.oneStepBackward();
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

						int lScore = ThreeThreeBoardSearch.getAccurateScore(
								goBoard.getBoardColorState(),
								manual.getResultAsScore());

						goBoard.oneStepBackward();
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
					+ manual.getCurrent().getChildren());
			if (symmetryResult.getNumberOfSymmetry() > 0) {

				if (manual.getCurrent().containsChildMove_mirrorSubTree(
						symmetryResult, childMove)) {
					log.warn("After mirroring, Manual contains move "
							+ childMove.toNonSGFString());
					manual.navigateToChild(childMove);
				} else {
					log.warn("Manual does not contain move " + childMove
							+ " Strange!");
					goBoard.oneStepBackward();
					return true;
				}
			}
		}

		if (goBoard.areBothPass()) {
			pass.setEnabled(false);
			log.warn("Teminated by both pass!");
			endWithManPlay = true;
		} else {

			SearchNode child = manual.getCurrent().getChild();
			if (child == null) {
				log.error("Computer has no choice; Strange! pass");
				// go.giveUp(manual.getCurrent().getStep().getEnemyColor());

				// possible due to that the previous state is an KNOWN state,
				// we need to load its SGF on calculate on the fly.
				int rScore = ThreeThreeBoardSearch
						.getAccurateScore(goBoard.getBoardColorState(),
								manual.getResultAsScore());

			} else {
				log.warn("Manual contains moves "
						+ manual.getCurrent().getChildren());
				child = manual.getCurrent().getLessVisitChild();
				child.increaseVisit();
				Step response = child.getStep();
				manual.navigateToChild(response);				
				goBoard.oneStepForward(response);
				
				log.warn("Computer response at " + response.toNonSGFString());
			}
			if (goBoard.areBothPass()) {
				log.warn("Teminated by both pass!");
				pass.setEnabled(false);
			}
		}
		shoushuV.setText(String.valueOf(goBoard.getShoushu()));
		//goBoard.initUIPoint(points);
		repaint_complete();
		//embedCanvas.repaint();
		this.setManTurn(true);
		return true;

	}

	/**
	 * 后退一步
	 * 
	 * @author think
	 *
	 */
	class BackwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			goBoard.oneStepBackward();
			manual.up();
			if (endWithManPlay == true) {
				endWithManPlay = false;
			} else {
				goBoard.oneStepBackward();
				manual.up();
			}
			if ((computerFirst && goBoard.getShoushu() == 1)
					|| (!computerFirst && goBoard.getShoushu() == 0)) {
				// return;// keep computer's first auto play.
				backward.setEnabled(false);
			}
			pass.setEnabled(true);
			repaint_complete();
		}
	}

	/**
	 * 弃权一手
	 * 
	 * @author think
	 *
	 */
	class PassActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (goBoard.areBothPass()) {
				log.warn("Teminated by both pass!");
			} else {
				mouseDown(null, -1, -1);
			}
		}
	}

	/**
	 * 载入棋谱（题目及研究好的各种变化）
	 * 
	 * @author Eddie
	 * 
	 */
	class LoadActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			FileDialog fd = new FileDialog(parent, "载入局面的位置", FileDialog.LOAD);
			fd.setFile("*.sgf");
			fd.setDirectory(Constant.rootDir);
			fd.setVisible(true);

			String fileName = fd.getFile();
			String dir = fd.getDirectory();
			if (fileName == null || fileName.isEmpty())
				return;
			String fullName = dir + fileName;
			loadManual(fullName);
		}
	}
	
	public void loadManual(String fullName){
		if (fullName.toLowerCase().endsWith("lose.sgf")) {
			computerFirst = false;
			manTurn = true;
		} else { // win.sfg
			computerFirst = true;
		}
		
		manual = SGFGoManual.loadTreeGoManual(fullName).get(0);
		goBoard = new GoBoard(manual.getInitState());

		if (log.isEnabledFor(Level.WARN)) {
			log.info("载入围棋题目" + fullName);
			log.warn(goBoard.getBoardColorState().getStateString());
			log.warn(manual.getSGFBodyString(false));
		}
		resultV.setText(manual.getResult());

		if (computerFirst == true && manual.isEmpty() == false) {
			Step firstStep = manual.getRoot().getChild().getStep();
			boolean valid = goBoard.oneStepForward(firstStep);
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
		pass.setEnabled(true);
		backward.setEnabled(false);
		repaint_complete();
	}

	public void repaint_complete() {
		goBoard.initUIPoint(points);
		embedCanvas.setBoardSize(goBoard.boardSize);
		repaint();
		embedCanvas.repaint();
	}
}