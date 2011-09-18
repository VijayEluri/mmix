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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Constant;
import eddie.wu.ui.applet.EmbedBoardCanvas;

/**
 * 生成某种局面。（按照功能划分）
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class RecordState extends Frame {
	private static final Log log = LogFactory.getLog(RecordState.class);
	private static final String path = "doc/围棋程序数据/死活题局面";
	public final byte[][] state = new byte[21][21]; // 0.4k;
	private byte color = Constant.BLACK;

	EmbedBoardCanvas embedApplet = new EmbedBoardCanvas();
	Label currentColor = new Label("current color");
	TextField currentColorT = new TextField("Black");

	Button changeColor = new Button("切换黑白");
	Button storeState = new Button("保存局面");
	Button loadState = new Button("载入局面");
	Button clearBoard = new Button("清空棋盘");
	Button clearPoint = new Button("擦除棋子");

	public void clearState() {
		for (int i = 1; i <= 19; i++) {
			for (int j = 1; j <= 19; j++) {
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
		embedApplet.setState(state);
		embedApplet.setRepaint(true);

		changeColor.addActionListener(new ChangeColorActionListener());
		loadState.addActionListener(new LoadStateActionListener(this));
		storeState.addActionListener(new StoreStateActionListener(this));
		clearBoard.addActionListener(new ClearBoardActionListener());
		clearPoint.addActionListener(new ClearPointActionListener());
		add(embedApplet);
		add(currentColor);
		add(currentColorT);
		add(changeColor);
		add(storeState);
		add(loadState);
		add(clearBoard);
		add(clearPoint);

		currentColor.setVisible(true);
		currentColorT.setVisible(true);
		embedApplet.setVisible(true);
		changeColor.setVisible(true);
		loadState.setVisible(true);
		clearBoard.setVisible(true);
		setLayout(null);

		embedApplet.setBounds(30, 30, 560, 560);
		currentColor.setBounds(600, 60, 100, 30);
		currentColorT.setBounds(700, 60, 30, 30);
		changeColor.setBounds(600, 100, 100, 30);
		loadState.setBounds(600, 160, 100, 30);
		storeState.setBounds(600, 220, 100, 30);
		clearBoard.setBounds(600, 280, 100, 30);
		clearPoint.setBounds(600, 280 + 60, 100, 30);

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
		System.out.println("weiqiFrame de mousedown");
		// coordinate difference between matrix and plane.

		state[b][a] = color;// vs. state[a][b]=color;
		repaint();
		embedApplet.repaint();
		return true;
	}

	class ChangeColorActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (color == Constant.BLACK) {
				color = Constant.WHITE;
				currentColorT.setText("WHITE");

			} else {
				color = Constant.BLACK;
				currentColorT.setText("BLACK");
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
			repaint();

		}
	}

	@Override
	public void repaint() {
		super.repaint();
		embedApplet.repaint();
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
			try {
				DataOutputStream out = new DataOutputStream(
						new BufferedOutputStream(new FileOutputStream(dir
								+ outname)));
				// 可能是安全策略的限制，竟然无法写入文件中，字节为始终为0kb。
				// 而且我是用匿名开机的。3月7日。完全错误，代码有问题，没有关闭输出
				// 数据流。
				log.debug(out);

				// goapplet.goboard.outputState(out);
				for (int i = 1; i < 20; i++) {
					for (int j = 1; j < 20; j++) {
						if (state[i][j] != 0) {
							out.writeByte((byte) i);
							out.writeByte((byte) j);
							out.writeByte((byte) state[i][j]);
							log.debug("i=" + i);
							log.debug("j=" + j);
							log.debug("color=" + state[i][j]);
						}
					}
				}
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
}
