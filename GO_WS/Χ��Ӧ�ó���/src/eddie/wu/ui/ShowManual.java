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
import eddie.wu.manual.GMDGoManual;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.LoadGMDGoManual;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.ui.canvas.EmbedBoardCanvas;

/**
 * 静态地显示一张棋谱。有点象棋谱打印的功能 statically show a manual to specified moves.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class ShowManual extends Frame {
	/**
	 * UI elements
	 */
	EmbedBoardCanvas embedCanvas = new EmbedBoardCanvas();

	public static void main(String[] args) {
		 test_wuqingyuan();
//		test_lib0();
	}

	public static void test_wuqingyuan() {
		String fileName = Constant.currentManual ;
		;
		// GoManual manual = GMDGoManual.loadGoManual(Constant.GLOBAL_MANUAL,0);
		GoManual manual = SGFGoManual.loadGoManual(fileName);

		List<Step> steps = manual.getSteps();
		GoBoard go = new GoBoard();		
		for (Step step : steps) {
			go.oneStepForward(step);
		}
		byte[][] state = go.getBoardColorState().getMatrixState();
		
		//only show the state, without step number
		ShowManual weiqi = new ShowManual(state, null);
		
		//ShowManual weiqi = new ShowManual(state, steps);
		weiqi.setVisible(true);
		weiqi.setBounds(0, 0, 800, 600);
	}

	public static void test_lib0() {

		byte count = 5;
		List<GMDGoManual> list = new LoadGMDGoManual(Constant.rootDir)
				.loadMultiGoManualFromLib0();
		// for (GMDGoManual manual : list) {
		// count++;
		// byte[] original = list.get(count).getMoves();

		GMDGoManual manual = list.get(count);
		List<Step> steps = manual.getSteps();
		GoBoard go = new GoBoard();		
		for (Step step : steps) {
			go.oneStepForward(step);
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
