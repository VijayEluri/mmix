package eddie.wu.ui;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.conn.ConnectivityAnalysis;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.ui.canvas.EmbedBoardCanvas;

/**
 * 生成某种局面。（按照功能划分）<br/>
 * also implemented in RecordManual, we still keep it here as simple
 * alternative.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class RecordState extends Frame {
	private static final Logger log = Logger.getLogger(RecordState.class);
	private static final String path = "doc/围棋程序数据/死活题局面";
	public byte[][] state = new byte[21][21]; // 0.4k;
	// public final byte[][] state = new byte[13][13]; // 11路小棋盘j
	private byte color = Constant.BLACK;

	EmbedBoardCanvas embedCanvas = new EmbedBoardCanvas();
	Label currentColorL = new Label("当前颜色");// current color
	TextField currentColorT = new TextField("黑");// Black

	Label boardSizeL = new Label("棋盘大小");// current color
	TextField boardSizeT = new TextField("19");// Black

	Choice initTurn = new Choice();
	Button changeColor = new Button("切换黑白");
	Button changeBoardSize = new Button("切换棋盘");
	Button storeState = new Button("保存局面");
	Button loadState = new Button("载入局面");
	Button clearBoard = new Button("清空棋盘");
	Button clearPoint = new Button("擦除棋子");
	TextArea textArea = new TextArea();

	public void clearState() {
		int boardSize = state.length - 2;
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				state[i][j] = Constant.BLANK;
			}
		}
	}

	public static void main(String[] args) {
		RecordState weiqi = new RecordState();
		weiqi.setVisible(true);
		weiqi.setBounds(0, 0, 800, 600);

	}

	public RecordState() {
		embedCanvas.setState(state);
		currentColorT.setColumns(10);
		boardSizeT.setColumns(4);
		changeColor.addActionListener(new ChangeColorActionListener());
		changeBoardSize.addActionListener(new ChangeBoardSizeActionListener());
		// currentColorT.setColumns(8);
		currentColorT.setEditable(false);
		boardSizeT.setEditable(true);
		initTurn.addItem("黑方先行");
		initTurn.addItem("白方先行");

		loadState.addActionListener(new LoadStateActionListener(this));
		storeState.addActionListener(new StoreStateActionListener(this));
		clearBoard.addActionListener(new ClearBoardActionListener());
		clearPoint.addActionListener(new ClearPointActionListener());
		add(embedCanvas);
		add(currentColorL);
		add(currentColorT);

		add(boardSizeL);
		add(boardSizeT);
		add(changeBoardSize);

		add(initTurn);
		add(changeColor);
		add(storeState);
		add(loadState);
		add(clearBoard);
		add(clearPoint);
		add(textArea);

		currentColorL.setVisible(true);
		currentColorT.setVisible(true);

		boardSizeL.setVisible(true);
		boardSizeT.setVisible(true);

		initTurn.setVisible(true);
		embedCanvas.setVisible(true);
		changeColor.setVisible(true);
		changeBoardSize.setVisible(true);

		loadState.setVisible(true);
		clearBoard.setVisible(true);
		textArea.setVisible(true);
		setLayout(null);

		embedCanvas.setBounds(30, 30, 560, 560);
		currentColorL.setBounds(600, 30, 100, 30);
		currentColorT.setBounds(700, 30, 100, 30);

		boardSizeL.setBounds(600, 60, 100, 30);
		boardSizeT.setBounds(700, 60, 100, 30);
		changeColor.setBounds(600, 100, 100, 30);
		loadState.setBounds(600, 160, 100, 30);
		storeState.setBounds(600, 220, 100, 30);

		clearBoard.setBounds(600, 280, 100, 30);
		changeBoardSize.setBounds(700, 280, 100, 30);

		clearPoint.setBounds(600, 280 + 60, 100, 30);
		textArea.setBounds(30, 600, 600, 200);
		initTurn.setBounds(600, 280 + 120, 100, 30);

		// changeBoardSize.setBounds(600, 100, 100, 30);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}

		});
	}

	public boolean mouseDown(Event e, int x, int y) { // 接受鼠标输入
		log.debug("chuan bo dao rong qi");
		x -= 30;
		y -= 30;

		byte a = (byte) ((x - 4) / 28 + 1);// 完成数气提子等.
		byte b = (byte) ((y - 4) / 28 + 1);

		if (Point.isValid(state.length - 2, a, b) == false)
			return true;
		if (log.isDebugEnabled())
			log.debug("weiqiFrame de mousedown");
		// coordinate difference between matrix and plane.

		state[b][a] = color;// vs. state[a][b]=color;
		repaint();
		embedCanvas.repaint();
		return true;
	}

	class ChangeColorActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (color == Constant.BLACK) {
				color = Constant.WHITE;
				currentColorT.setText("白");// WHITE

			} else {
				color = Constant.BLACK;
				currentColorT.setText("黑");// BLACK
			}
		}
	}

	class LoadStateActionListener implements ActionListener { // 载入局面。

		Frame parent;

		public LoadStateActionListener(Frame par) {
			parent = par;
		}

		public void actionPerformed(ActionEvent e) {
			clearState();
			// 载入局面
			FileDialog fd = new FileDialog(parent, "载入局面的位置", FileDialog.LOAD);
			fd.setFile("1.wjm");
			fd.setDirectory(path);
			fd.show();

			String inName = fd.getFile();
			String dir = fd.getDirectory();
			String file = dir + inName;
			if (inName == null || inName.isEmpty()) {
				return;
			} else if (inName.toUpperCase().endsWith("SGF")) {
				SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(file);
				log.debug("载入局面SGF");
				GoBoard go = new GoBoard(goManual.getInitState());
				go.getInitState(state);
				if (goManual.getInitTurn() == Constant.BLACK) {
					initTurn.select(0);
				} else {
					initTurn.select(1);
				}

			}
			log.debug("载入局面");
			ConnectivityAnalysis analysis = new ConnectivityAnalysis(state);
			textArea.setRows(10);
			textArea.setColumns(50);
			String str = analysis.groups.toString();
			textArea.append(str);

			repaint();

		}
	}

	@Override
	public void repaint() {
		super.repaint();
		embedCanvas.repaint();
	}

	class StoreStateActionListener implements ActionListener {
		Frame parent;

		public StoreStateActionListener(Frame par) {
			parent = par;
		}

		public void actionPerformed(ActionEvent e) {

			FileDialog fd = new FileDialog(parent, "保存局面的位置", FileDialog.SAVE);
			fd.setFile("1.wjm");
			fd.setDirectory(path);
			fd.show();

			String outname = fd.getFile();
			String dir = fd.getDirectory();
			log.debug(outname);

			String file = dir + outname;
			if (outname.toUpperCase().endsWith("SGF")) {
				SimpleGoManual goManual = null;
				if (initTurn.getSelectedIndex() == 0) {
					goManual = new SimpleGoManual(state, Constant.BLACK);
				} else {
					goManual = new SimpleGoManual(state, Constant.WHITE);
				}
				SGFGoManual.storeGoManual(file, goManual);
				log.debug("保存局面－SGF格式");
			} else {
				StateAnalysis.saveState(state, dir + outname);
			}
		}

	}

	class ClearBoardActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clearState();
			repaint();
		}

	}

	class ClearPointActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			color = Constant.BLANK;
			// recordState.repaint();
		}

	}

	class ChangeBoardSizeActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int size = Integer.valueOf(boardSizeT.getText());
			state = new byte[size + 2][size + 2];
			embedCanvas.setState(state);
			repaint();
			embedCanvas.repaint();
		}

	}
}
