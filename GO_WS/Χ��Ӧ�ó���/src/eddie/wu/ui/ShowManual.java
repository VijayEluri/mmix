package eddie.wu.ui;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.ui.canvas.EmbedBoardCanvas;

/**
 * 静态地显示一张棋谱。有点象棋谱打印的功能
 * statically show a manual to specified moves.
 * @author wueddie-wym-wrz
 *
 */
public class ShowManual extends Frame {
	/*
	 * UI elements
	 */
	EmbedBoardCanvas embedCanvas = new EmbedBoardCanvas();
	
	public static void main(String[] args) {
		
		String fileName = Constant.rootDir+"吴清源番棋263局/吴清源番棋002.SGF";;
		//GoManual manual = GMDGoManual.loadGoManual(Constant.GLOBAL_MANUAL,0);
		GoManual manual = SGFGoManual.loadGoManual(fileName);
		 byte[] stepArray= manual.getMoves();
		List<Step> steps = new ArrayList<Step>();
		GoBoard go = new GoBoard();
		int color = 0;
		// for (int i = 0; i < stepArray.length / 2; i++) {
		for (int i = 0; i < manual.getShouShu(); i++) {
			if (i % 2 == 0) {
				color = Constant.BLACK;
			} else {
				color = Constant.WHITE;
			}
			Step step = new Step();
			step.setColor(color);
			step.setPoint(Point
					.getPoint(stepArray[2 * i], stepArray[2 * i + 1]));
			go.oneStepForward(step);
			steps.add(step);
		}
		byte[][] state = go.getBoardColorState().getMatrixState();
		ShowManual weiqi = new ShowManual(state, steps);
		weiqi.setVisible(true);
		weiqi.setBounds(0, 0, 800, 600);

	}
	
	public ShowManual(byte[][] state, List<Step> steps) {

		embedCanvas.setState(state);
		embedCanvas.setSteps(steps);
		
		embedCanvas.setVisible(true);

		add(embedCanvas);	
		setLayout(null);

		embedCanvas.setBounds(30, 30, 560, 560);		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}

		});
	}
}
