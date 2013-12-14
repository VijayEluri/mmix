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
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.ui.canvas.ReviewManualCanvas;

/**
 * copy from review manual. <br/>
 * intention: to support tree go manual. <br/>
 * 1. to show search result, computer play first to justify its result. no
 * matter how player respond, computer have to ensure it reach the target
 * result.<br/>
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
	private Frame parent = this;
	/**
	 * domain object.
	 */
	private GoBoard go;// for showing current state
	private TreeGoManual manual;// all variant

	// share data between UI and back end.
	private List<UIPoint> points = new ArrayList<UIPoint>();

	private boolean computerFirst = true;
	private boolean manTurn = false;

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
		// coordinate difference between matrix and plane..
		Point point = Point.getPoint(go.boardSize, b, a);
		if (manTurn) {
			boolean valid = go.oneStepForward(point);
			if (valid == false) {
				System.out.print("invalid step:" + point);
				return true;
			}
			go.initUIPoint(points);
			repaint();
			embedCanvas.repaint();

			Step childMove = go.getLastStep().getStep();
			if (manual.getCurrent().containsChildMove(childMove)) {
				manual.navigateToChild(childMove);
			} else {
				System.out.print("Need to calculate respond of :" + point);
				return true;
				//TODO: dynamic code here.
			}

			// computer respond one move in a second.
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			SearchNode child = manual.getCurrent().getChild();
			if(child == null){
				System.out.print("Computer has no choise; pass");
				return true;
			}
			Step response = child.getStep();
			go.oneStepForward(response);
			manual.navigateToChild(response);
			
			go.initUIPoint(points);
			repaint();
			embedCanvas.repaint();

		}
		return true;
	}

	class BackwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			go.oneStepBackward();

			repaint();
			embedCanvas.repaint();

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
			fd.setFile("1.wjm");
			fd.setDirectory(Constant.rootDir);
			fd.show();

			String inname = fd.getFile();
			String dir = fd.getDirectory();
			if (inname == null || inname.isEmpty())
				return;
			manual = SGFGoManual.loadTreeGoManual(dir + inname).get(0);
			go = new GoBoard(manual.getInitState());
			blackPlayerV.setText(manual.getBlackName());
			whitePlayerV.setText(manual.getWhiteName());
			resultV.setText(manual.getResult());

			if (computerFirst == true && manual.isEmpty() == false) {
				Step firstStep = manual.getRoot().getChild().getStep();
				boolean valid = go.oneStepForward(firstStep);
				if(log.isEnabledFor(Level.WARN)) log.warn("correct color = "+manual.getInitTurn());
				if(valid==false){
					System.err.println("Wrong first step:"+firstStep);
					
				}
				manual.navigateToChild(firstStep);
				if(log.isEnabledFor(Level.WARN)) log.warn("computer played the first step "
						+ firstStep);
				manTurn = true;
			}

			log.debug("载入局面");
			if(log.isEnabledFor(Level.WARN)) log.warn(go.getBoardColorState().getStateString());
			if(log.isEnabledFor(Level.WARN)) log.warn(manual.getSGFBodyString());
			go.initUIPoint(points);
			embedCanvas.setBoardSize(go.boardSize);
			repaint();
			embedCanvas.repaint();

		}
	}
}
