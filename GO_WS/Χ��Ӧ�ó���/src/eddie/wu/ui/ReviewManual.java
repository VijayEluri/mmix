package eddie.wu.ui;

import java.awt.Button;
import java.awt.Event;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.UIPoint;
import eddie.wu.manual.LoadGoManual;
import eddie.wu.ui.canvas.ReviewManualCanvas;

/**
 * �򵥵Ĵ��׹��ܣ�ֻ��չʾ���Եĳ������ף�����չʾ�仯���仯Ҳ�������ע����������<br/>
 * button 1: ǰ��һ����<br/>
 * button 2: ����һ����<br/>
 * Ĭ����ʾ����<br/>
 * TODO�����ҷ�������Ժ��˺�ǰ�����������ϲ���������Ļ������Է���һ�£� 2011.10.6 play game<br/>
 * ��������ѧϰ��̣�������������ΰ��ķ�ŭС�� �ú�ѧϰ���������ϡ�
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class ReviewManual extends Frame {
	private static String rootDir = Constant.rootDir; // "doc/Χ��������/";
	private static final Log log = LogFactory.getLog(ReviewManual.class);

	/*
	 * domain object.
	 */
	private GoBoard go = new GoBoard();
	private List<UIPoint> points = new ArrayList<UIPoint>();
	private List<Step> steps = new ArrayList<Step>();
	private int moves = 0;//
	private int startMove = 0;//
	/*
	 * UI elements
	 */
	private ReviewManualCanvas embedCanvas = new ReviewManualCanvas();
	private Button forward = new Button("��һ��");// ǰ��
	private Button backward = new Button("��һ��");// ����
	/**
	 * �������Ի�����ʾ��ʽ���Զ�������Ϊ�ָһ����ʾ���������� ÿ�β�����100�֣��������ֲ��ᳬ����λ��
	 */
	Button forwardManual = new Button("��һ��");// ǰ��
	Button backwardManual = new Button("��һ��");// ����

	public static void main(String[] args) {
		if (args.length > 1) {
			rootDir = args[1];
		}
		byte[] stepArray = new LoadGoManual(rootDir).loadSingleGoManual();
		List<Step> steps = new ArrayList<Step>();
		int color = 0;
		for (int i = 0; i < stepArray.length / 2; i++) {
			if (i % 2 == 0) {
				color = Constant.BLACK;
			} else {
				color = Constant.WHITE;
			}
			Step step = new Step();
			step.setColor(color);
			step.setPoint(Point
					.getPoint(stepArray[2 * i], stepArray[2 * i + 1]));
			steps.add(step);
		}

		ReviewManual weiqi = new ReviewManual(steps);
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
	public ReviewManual(List<Step> steps) {
		this.steps = steps;

		embedCanvas.setPoints(points);
		embedCanvas.setVisible(true);

		add(embedCanvas);
		add(forward);
		add(backward);
		add(forwardManual);
		add(backwardManual);
		forward.addActionListener(new ForwardActionListener());
		backward.addActionListener(new BackwardActionListener());
		forwardManual.addActionListener(new ForwardManualActionListener());
		backwardManual.addActionListener(new BackwardManualActionListener());
		forward.setVisible(true);
		backward.setVisible(true);
		forwardManual.setVisible(true);
		backwardManual.setVisible(true);
		setLayout(null);

		embedCanvas.setBounds(30, 30, 560, 560);
		forward.setBounds(600, 100, 100, 30);
		backward.setBounds(600, 160, 100, 30);
		forwardManual.setBounds(600, 220, 100, 30);
		backwardManual.setBounds(600, 280, 100, 30);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}

		});
	}

	public boolean mouseDown(Event e, int x, int y) { // �����������
		if (log.isDebugEnabled()) {
			log.debug("chuan bo dao rong qi - forward one step.");
		}
		x -= 30;
		y -= 30;

		byte a = (byte) ((x - 4) / 28 + 1);// ����������ӵ�.
		byte b = (byte) ((y - 4) / 28 + 1);
		System.out.println("weiqiFrame de mousedown");
		// coordinate difference between matrix and plane.

		repaint();
		embedCanvas.repaint();
		return true;
	}

	class ForwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			points.clear();
			Step step = steps.get(moves++);
			go.oneStepForward(step);
			UIPoint uPoint;
			byte[][] matrixState = go.getBoardColorState().getMatrixState();
			int color;
			for (int i = 0; i < moves; i++) {
				step = steps.get(i);
				color = matrixState[step.getPoint().getRow()][step.getPoint()
						.getColumn()];
				// �����һ�����㣩û�б���ԵĻ���
				if (color == Constant.BLACK || color == Constant.WHITE) {
					uPoint = new UIPoint();
					uPoint.setColor(step.getColor());
					uPoint.setMoveNumber(i + 1);
					uPoint.setPoint(step.getPoint());
					points.add(uPoint);
				}
			}
			repaint();
			embedCanvas.repaint();
		}
	}

	class BackwardActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			points.clear();
			Step step = steps.get(--moves);
			go.oneStepBackward(step.getPoint());
			UIPoint uPoint;
			byte[][] matrixState = go.getBoardColorState().getMatrixState();
			int color;
			for (int i = 0; i < moves; i++) {
				step = steps.get(i);
				color = matrixState[step.getPoint().getRow()][step.getPoint()
						.getColumn()];
				if (color == Constant.BLACK || color == Constant.WHITE) {
					uPoint = new UIPoint();
					uPoint.setColor(step.getColor());
					uPoint.setMoveNumber(i + 1);
					uPoint.setPoint(step.getPoint());
					points.add(uPoint);
				}
			}
			repaint();
			embedCanvas.repaint();

		}
	}

	class ForwardManualActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			points.clear();
			Step step;
			// ǰ���������ӵ�������֡�
			int count = 1;
			startMove = moves;
			while (moves < steps.size()
					&& count < Constant.MAX_STEPS_IN_ONE_MANUAL_SHOW) {
				step = steps.get(moves++);
				go.oneStepForward(step);
				count++;
				if (go.getStepHistory().getStep(moves-1).getEatenBlocks()
						.isEmpty() == false) {
					break;
				}
			}
			if(moves==steps.size()){
				forwardManual.setEnabled(false);
			}
			UIPoint uPoint;
			byte[][] matrixState = go.getBoardColorState().getMatrixState();
			int color;

			for (int i = 0; i < moves; i++) {
				step = steps.get(i);
				color = matrixState[step.getPoint().getRow()][step.getPoint()
						.getColumn()];
				// �����һ�����㣩û�б���ԵĻ���
				if (color == Constant.BLACK || color == Constant.WHITE) {
					uPoint = new UIPoint();
					uPoint.setColor(step.getColor());
					//ǰ�׵����Ӳ�����ʾ����
					if (i >= startMove) {
						uPoint.setMoveNumber(i + 1 - startMove);
					}
					uPoint.setPoint(step.getPoint());
					points.add(uPoint);
				}
			}

			repaint();
			embedCanvas.repaint();
		}
	}

	/**
	 * TODO: 
	 * @author wueddie-wym-wrz
	 *
	 */
	class BackwardManualActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}
}
