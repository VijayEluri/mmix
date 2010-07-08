/*
 * Created on 2005-4-21
 *
 * 
 */
package eddie.wu.linkedblock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.api.GoBoardInterface;
import eddie.wu.api.ZhengZiInterface;
import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;

/**
 * this program is refactored from the old program which use array as data
 * structure and did not use any Collection framework. <br/>
 * Χ�����ĺ�����.�����������.
 * 
 * @author eddie
 */
public class GoBoard implements Cloneable, Serializable, GoBoardInterface,
		ZhengZiInterface {
	private transient static final Log log = LogFactory.getLog(GoBoard.class);

	private static final byte[][] szld = Constant.szld;
	
	private static final byte[][] szdjd = Constant.szdjd;
	
	// private byte[][][] zb = new byte[21][21][4];
	// ÿ�����ӵ�����������ԭʼ��Ϣ������������С����Դ���һ�����档
	// ��ά�����̵�����,�����±��1��19;
	private BoardPoint[][] boardPoints = new BoardPoint[Constant.SIZEOFMATRIX][Constant.SIZEOFMATRIX];

	private Point[][] points = new Point[Constant.SIZEOFMATRIX][Constant.SIZEOFMATRIX];

	// ������ӿ�ֿ����������ӿ�ĸ�����
	private short shoushu = 0; // ��ǰ��������,����֮ǰ����.��1��ʼ;

	// ���岻�󣬲���˳��������������жϻ������á�
	private byte numberOfWhitePointEaten = 0;

	private byte numberOfBlackPointEaten = 0; // �ڰױ����Ӽ���

	private StepHistory stepHistory = new StepHistory();

	/**
	 * 
	 * still not finished!
	 * 
	 * @param oldgo
	 */
	// private GoBoard(GoBoard oldgo) {
	// super();
	//
	// byte i, j;
	// short t;
	//
	// for (i = 1; i < 20; i++) {
	// for (j = 1; j < 20; j++) {
	// boardPoints[i][j] = oldgo.boardPoints[i][j];
	// }
	// }
	//
	// numberOfBlackPointEaten = oldgo.numberOfBlackPointEaten;
	// numberOfWhitePointEaten = oldgo.numberOfWhitePointEaten;
	// shoushu = oldgo.shoushu;
	//
	// for (t = 0; t < 512; t++) {
	// for (j = 0; j < 5; j++) {
	//
	// stepHistory.hui[t][j] = oldgo.stepHistory.hui[t][j];
	// }
	// }
	// // for (t = 0; t < 512; t++) {
	// // for (j = 0; j < 12; j++) {
	// // huik[t][j] = oldgo.huik[t][j];
	// // }
	// // }
	//
	// }

	public void initPoints() {
		byte i, j;
		for (i = 1; i < Constant.ZBSX; i++) {
			for (j = 1; j < Constant.ZBSX; j++) {
				points[i][j] = new Point(i, j);

			}
		}
		log.debug("init points");
	}

	/**
	 * 
	 * really used Constructor.
	 * 
	 */
	public GoBoard() {

		byte i, j;
		Block originalBlankBlock = new Block();
		for (i = 1; i < Constant.ZBSX; i++) {
			for (j = 1; j < Constant.ZBSX; j++) {
				points[i][j] = new Point(i, j);
				boardPoints[i][j] = new BoardPoint(points[i][j]);
				boardPoints[i][j].setBlock(originalBlankBlock);
				originalBlankBlock.addPoint(points[i][j]);

			}
		}
		for (i = Constant.ZBXX; i <= Constant.ZBSX; i++) { // 2��22�ռ�
			points[Constant.ZBXX][i] = new Point(Constant.ZBXX, i);
			points[Constant.ZBSX][i] = new Point(Constant.ZBSX, i);
			points[i][Constant.ZBXX] = new Point(i, Constant.ZBXX);
			points[i][Constant.ZBSX] = new Point(i, Constant.ZBSX);
			boardPoints[Constant.ZBXX][i] = new BoardPoint(
					points[Constant.ZBXX][i], (byte) ColorUtil.OutOfBound);
			boardPoints[Constant.ZBSX][i] = new BoardPoint(
					points[Constant.ZBSX][i], (byte) ColorUtil.OutOfBound);
			boardPoints[i][Constant.ZBXX] = new BoardPoint(
					points[i][Constant.ZBXX], (byte) ColorUtil.OutOfBound);
			boardPoints[i][Constant.ZBSX] = new BoardPoint(
					points[i][Constant.ZBSX], (byte) ColorUtil.OutOfBound);
		} // 2��22�ռ�

		if (log.isDebugEnabled()) {
			log.debug("originalBlankBlock" + originalBlankBlock);

		}
	}

	/**
	 * 
	 * really used Constructor.
	 * 
	 */
	GoBoard(BoardColorState boardState, short numberOfSteps, boolean isBlackTurn) {
		this();

		final Set blackPoints = boardState.getBlackPoints();
		final Set whitePoints = boardState.getWhitePoints();
		Point tempPoint = null;
		for (Iterator iter = blackPoints.iterator(); iter.hasNext();) {
			tempPoint = (Point) iter.next();
			boardPoints[tempPoint.getRow()][tempPoint.getColumn()]
					.setColor(ColorUtil.BLACK);
		}
		for (Iterator iter = whitePoints.iterator(); iter.hasNext();) {
			tempPoint = (Point) iter.next();
			boardPoints[tempPoint.getRow()][tempPoint.getColumn()]
					.setColor(ColorUtil.WHITE);
		}
		if (numberOfSteps % 2 == 0) {
			if (isBlackTurn) {
				this.shoushu = numberOfSteps;

			} else {
				throw new RuntimeException("whose turn is not right");
			}
		} else {
			if (isBlackTurn) {

				throw new RuntimeException("whose turn is not right");
			} else {
				this.shoushu = numberOfSteps;
			}
		}
		// now the original block is not synchronized.
	}

	/**
	 * no double check really used Constructor.
	 * 
	 */
	GoBoard(BoardColorState boardState, int numberOfSteps) {
		this();

		final Set blackPoints = boardState.getBlackPoints();
		final Set whitePoints = boardState.getWhitePoints();
		Point tempPoint = null;
		for (Iterator iter = blackPoints.iterator(); iter.hasNext();) {
			tempPoint = (Point) iter.next();
			boardPoints[tempPoint.getRow()][tempPoint.getColumn()]
					.setColor(ColorUtil.BLACK);
		}
		for (Iterator iter = whitePoints.iterator(); iter.hasNext();) {
			tempPoint = (Point) iter.next();
			boardPoints[tempPoint.getRow()][tempPoint.getColumn()]
					.setColor(ColorUtil.WHITE);
		}

		this.shoushu = (short) numberOfSteps;

	}

	/**
	 * no double check really used Constructor.
	 * 
	 */
	GoBoard(BoardColorState boardState) {
		this();

		final Set blackPoints = boardState.getBlackPoints();
		final Set whitePoints = boardState.getWhitePoints();
		Point tempPoint = null;
		for (Iterator iter = blackPoints.iterator(); iter.hasNext();) {
			tempPoint = (Point) iter.next();
			boardPoints[tempPoint.getRow()][tempPoint.getColumn()]
					.setColor(ColorUtil.BLACK);
		}
		for (Iterator iter = whitePoints.iterator(); iter.hasNext();) {
			tempPoint = (Point) iter.next();
			boardPoints[tempPoint.getRow()][tempPoint.getColumn()]
					.setColor(ColorUtil.WHITE);
		}

	}

	GoBoard(byte[][] board) {
		this();
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				boardPoints[i][j].setColor(board[i][j]);
			}
		}
	}

	boolean validate(final Point point) {
		final byte row = point.getRow();
		final byte column = point.getColumn();
		return validate(row, column);
	}

	/**
	 * no side effect �ж�����λ�õ���Ч�ԡ�
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	boolean validate(final byte row, final byte column) {
		byte m, n, qi = 0;
		// ��shoushu����֮ǰ���ã�yise��tongse�ļ���������ͬ��
		byte selfColor = ColorUtil.getNextStepColor(shoushu);
		byte enemyColor = ColorUtil.getNextStepEnemyColor(shoushu);
		// if(log.isDebugEnabled()){
		if (log.isDebugEnabled()) {
			log.debug("row=" + row + ",column=" + column);
		}
		// �±�Ϸ�,�õ�հ�
		if (basicValidate(row, column)) {
			if (boardPoints[row][column].getProhibitStep() == (shoushu + 1)) {
				if (log.isDebugEnabled()) {
					log.debug("���Ǵ��ʱ�Ľ��ŵ�,�����ҽٲ�!");
					log.debug("���Ϊ��a=" + row + ",b=" + column);
				}
				return false;
			} else {
				return validateAccordingBreath(row, column);
			}
		} else { // ��һ�಻�Ϸ���.
			if (log.isDebugEnabled()) {
				log.debug("�õ㲻�Ϸ�,������֮����߸õ��Ѿ����ӣ�");
			}
			return false;
		}
	}

	/**
	 * @param row
	 * @param column
	 * @param qi
	 * @param selfColor
	 * @param enemyColor
	 * @return
	 */
	private boolean validateAccordingBreath(final byte row, final byte column) {
		byte m;
		byte n;
		byte qi = 0;
		byte selfColor = ColorUtil.getNextStepColor(shoushu);
		byte enemyColor = ColorUtil.getNextStepEnemyColor(shoushu);
		for (byte i = 0; i < 4; i++) {
			m = (byte) (row + szld[i][0]);
			n = (byte) (column + szld[i][1]);
			if (boardPoints[m][n].getColor() == ColorUtil.BLANK_POINT) {
				return true;
			} else if (boardPoints[m][n].getColor() == enemyColor) {
				if (boardPoints[m][n].getBreaths() == 1) {
					return true;
				}
			} else if (boardPoints[m][n].getColor() == selfColor) {
				if (boardPoints[m][n].getBreaths() > 1) {
					return true;
				}
			}
		}
		if (qi == 0) {
			if (log.isDebugEnabled()) {
				log.debug("������ɱ�Ľ��ŵ㣺");
			}
			return false;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("���ǺϷ��ŵ㣺");
			}
			return true;
		}
	}

	/**
	 * @param row
	 * @param column
	 * @return
	 */
	private boolean basicValidate(final byte row, final byte column) {
		return row > Constant.ZBXX && row < Constant.ZBSX
				&& column > Constant.ZBXX && column < Constant.ZBSX
				&& boardPoints[row][column].getColor() == ColorUtil.BLANK_POINT;
	}

	/**
	 * Ϊ���жϳ������ѭ��.ֻ������֮������ж�.��ʱû���뵽����Ԥ���жϵ��㷨.
	 * 
	 * @return
	 */

	private boolean isStateSimilar() {
		// stepHistory.hui[shoushu][]
		if (ColorUtil.getCurrentStepColor(shoushu) == ColorUtil.BLACK) {
			if (this.getTotalPoints() < stepHistory
					.getMaxTotalPointsAfterBlack())
				return true;
		} else if (ColorUtil.getCurrentStepColor(shoushu) == ColorUtil.WHITE) {
			if (this.getTotalPoints() < stepHistory
					.getMaxTotalPointsAfterWhite())
				return true;
		}
		// stepHistory.getMaxTotalPoints()
		return false;
	}

	/**
	 * @return
	 */
	private short getTotalPoints() {

		return (short) (shoushu - numberOfBlackPointEaten
				- numberOfWhitePointEaten - getGiveUpSteps());
		// return 0;
	}

	private short giveUpSteps = 0;

	/**
	 * һ����˵,������ֻ������Ȩ.
	 * 
	 * @return
	 */
	private short getGiveUpSteps() {
		return giveUpSteps;
		// return 0;
	}

	public void giveUp() {
		shoushu++;
		giveUpSteps++;
	}

	/**
	 * @return Returns the numberOfBlackPointEaten.
	 */
	private byte getNumberOfBlackPointEaten() {
		return numberOfBlackPointEaten;
	}

	/**
	 * @param numberOfBlackPointEaten
	 *            The numberOfBlackPointEaten to set.
	 */
	private void setNumberOfBlackPointEaten(byte tibaizishu) {
		this.numberOfBlackPointEaten = tibaizishu;
	}

	/**
	 * @return Returns the numberOfWhitePointEaten.
	 */
	private byte getNumberOfWhitePointEaten() {
		return numberOfWhitePointEaten;
	}

	/**
	 * @param numberOfWhitePointEaten
	 *            The numberOfWhitePointEaten to set.
	 */
	private void setNumberOfWhitePointEaten(byte tiheizishu) {
		this.numberOfWhitePointEaten = tiheizishu;
	}

	/**
	 * �����ɫ��ʱ,ͬɫ����������
	 * 
	 * @param eaten
	 */
	private void addBreathAfterEatBlock(Block eaten) {

		for (java.util.Iterator iter = eaten.getAllPoints().iterator(); iter
				.hasNext();) {
			Point point = (Point) iter.next();
			BoardPoint boardPoint = this.getBoardPoint(point);
			addBreathForEatenPoint(boardPoint);
			// sequence is important, must set color after deal with eaten
			// point.
			boardPoint.setColor(ColorUtil.BLANK_POINT);
		}
		eaten.setColor(ColorUtil.BLANK_POINT);
		eaten.initAfterChangeToBlankblock();

	}

	private void addBreathForEatenPoint(BoardPoint point) {
		// ���ڻ��壨�൱�����ӱ��ᣩ;����������ʱ��������;
		// ��֮��ĳ�ӱ�������Է�������.tiaoָ���ӷ�����ɫ��

		if (log.isDebugEnabled()) {
			log.debug("����addBreathForEatenPoint()");
		}
		byte m1, n1;
		for (byte i = 0; i < 4; i++) {
			m1 = (byte) (point.getRow() + szld[i][0]);
			n1 = (byte) (point.getColumn() + szld[i][1]);
			if (boardPoints[m1][n1].getColor() == point.getEnemyColor()) {
				if (log.isDebugEnabled()) {
					log.debug("add breath for block:"
							+ boardPoints[m1][n1].getBlock().getTopLeftPoint());
				}
				boardPoints[m1][n1].getBlock().addBreathPoint(point.getPoint());

			}
		}

	}

	public void output() {
		verifyFlag();
		Set<Block> allActiveBlocks = new HashSet<Block>();

		short count = 0;
		byte i, j;
		if (log.isDebugEnabled()) {
			log.debug("shoushu=" + shoushu);
		}
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				count++;
				if (allActiveBlocks.add(boardPoints[i][j].getBlock())) {
					if (log.isDebugEnabled()) {
						log.debug(boardPoints[i][j].getBlock());
					}
				}

			}

		}
		if (count != Constant.QIPANZONGDIANSHU) {
			if (log.isDebugEnabled()) {
				log.debug("number of points error" + count);
			}
		}

	}

	/**
	 * iterate the point nearby //��������������ɿ��������Ϣ��������������
	 * 
	 * @param block
	 * @return
	 */
	private void calculateBreath(Block block) {
		verifyFlag();
		// short rs = 0;
		if (log.isDebugEnabled()) {
			log.debug("���뷽�������������jskq��");
		}
		// byte tempBreath = 0; //����������Ӵ��Ѿ�ȷ����
		byte a = 0, b = 0;
		byte m, n;
		byte j;

		for (java.util.Iterator iter = block.getAllPoints().iterator(); iter
				.hasNext();) {
			Point point = (Point) iter.next();
			m = point.getRow();
			n = point.getColumn();

			for (j = 0; j < 4; j++) {
				a = (byte) (m + szld[j][0]);
				b = (byte) (n + szld[j][1]);
				if (log.isDebugEnabled()) {
					log.debug("a=" + a);
				}
				if (log.isDebugEnabled()) {
					log.debug("b=" + b);
				}
				if (boardPoints[a][b].getColor() == ColorUtil.BLANK_POINT
						&& boardPoints[a][b].isCalculatedFlag() == false) {
					// tempBreath++;

					if (log.isDebugEnabled())
						log.debug(" ��Ϊһ����");
					boardPoints[a][b].setCalculatedFlag(true);
					if (log.isDebugEnabled()) {
						log.debug("add breath for block:"
								+ block.getTopLeftPoint());
					}
					block.addBreathPoint(points[a][b]);

				}

			}

		} // for
		// block.setBreath(tempBreath);

		// �ָ���־
		for (java.util.Iterator iter = block.getAllBreathPoints().iterator(); iter
				.hasNext();) {

			Point point = (Point) iter.next();
			log.debug("restore flag of" + point);
			this.getBoardPoint(point).setCalculatedFlag(false);
		}
		if (log.isDebugEnabled()) {
			log.debug("�������Ϊ��" + block.getBreaths());
		}

		if (log.isDebugEnabled()) {
			log.debug("����jskq������");
		}

	} // 2��22�ո�,ԭ��������,��ֻ����ʹ�.

	boolean verify1() {
		Set<Block> blocks = new HashSet<Block>();
		byte i;
		byte j;
		short count = 0;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				if (boardPoints[i][j].getBlock() == null) {

				} else if (!blocks.contains(boardPoints[i][j].getBlock())) {
					blocks.add(boardPoints[i][j].getBlock());
					count += boardPoints[i][j].getTotalNumberOfPoint();
					if (log.isDebugEnabled()) {
						log.debug("i=" + i + ",j=" + j + ",count=" + count
								+ ";");
					}
				}
			}
		}
		return count == 361;

	}

	boolean oneStepForward(final Point point) {
		final byte row = point.getRow();
		final byte column = point.getColumn();
		return oneStepForward(row, column);
	}

	public boolean oneStepForward(final Step step) {
		return this.oneStepForward(step.getPoint());
	}

	public boolean oneStepForward(final int row, final int column) {
		return oneStepForward((byte) row, (byte) column);
	}

	/**
	 * accept input and finish all the dealing. ������һ�����ѵĿ��⣬ ������������Ҫ�Կ���
	 * ͨ�����ֵ���ʷ������������ʹ����ȫ���滻�� �������ֿ����Ѿ��ڱ𴦱����ã����⸴�ӻ��ˡ� ���洦���Ѿ������ǺϷ��ŵ㣻
	 * ���Խ��ܵ�����Ϊ(row,column)��c;c=column*19+row-19; �����������
	 * row����������±�,Ҳ��ƽ��ĺ�����:1-19 column����������±�,Ҳ����Ļ��������:1-19 byte c;
	 * a,b��һά��ʾ:1-361;
	 */
	public boolean oneStepForward(final byte row, final byte column) {
		byte enemyColor = 0; // �����ӵ���ɫ
		byte selfColor = 0; // �����ӵ�ͬɫ

		byte jubutizishu = 0; // �ֲ�������(ÿһ����������)
		byte tkd = 0;

		if (log.isDebugEnabled()) {
			log.debug("���뷽��cgcl()");
			log.debug("���ӵ�:" + row + "/" + column);
		}

		if (validate(row, column) == false) { // 1.�ж����ӵ����Ч�ԡ�
			if (log.isDebugEnabled()) {
				log.debug("���ӵ�invalide");
			}
			return false;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("���ӵ���Ч");
			}
		}

		shoushu++;
		enemyColor = ColorUtil.getCurrentStepEnemyColor(shoushu);
		selfColor = ColorUtil.getCurrentStepColor(shoushu);
		stepHistory.addStep(shoushu, row, column);
		// bug fix jgzs() calculate need the point[row][column]shoule be
		// colored, only flaged is not enough.
		boardPoints[row][column].setColor(selfColor);
		dealBlankPoint(row, column);
		// boardPoints[row][column].setColor(selfColor); //���Զ�̬һ��
		Block sameColorBlock = dealSelfPoint(row, column, selfColor);
		jubutizishu = dealEnemyPoint(row, column, enemyColor, sameColorBlock);

		if (shoushu % 2 == ColorUtil.BLACK) {
			numberOfWhitePointEaten += jubutizishu;
		} else {
			numberOfBlackPointEaten += jubutizishu; // ���ֲ����Ӽ���
		}

		this.calculateBreath(sameColorBlock);
		dealJie(row, column, jubutizishu);

		calculateWeakestBlock(sameColorBlock);

		// �µ�����ԭ����������ڳ̶ȸı�
		if (log.isDebugEnabled()) {
			log.debug("�˳�����cgcl()");
		}
		return true;
	}

	/**
	 * @param sameColorBlock
	 */
	private void calculateWeakestBlock(Block sameColorBlock) {
		// TODO Auto-generated method stub

	}

	/**
	 * ���ǽ�
	 * 
	 * @param row
	 * @param column
	 * @param jubutizishu
	 */
	private void dealJie(final byte row, final byte column, byte jubutizishu) {
		if (boardPoints[row][column].getBlock().getBreaths() == 1
				&& jubutizishu == 1
				&& boardPoints[row][column].getBlock().getTotalNumberOfPoint() == 1) {
			Set breathBlocks = boardPoints[row][column].getBlock()
					.getBreathBlocks();
			if (breathBlocks.size() == 1) {
				// ��һ���õ㲻����.
				Point tempPoint = (Point) ((Block) (breathBlocks.iterator()
						.next())).getAllPoints().iterator().next();
				BoardPoint temp = this.getBoardPoint(tempPoint);
				temp.setProhibitStep((short) (shoushu + 1));
				// ����Ϊ��ٵ�������ϵ��һ��.
				this.getBoardPoint(row, column).setTwinForKo(tempPoint);
				temp.setTwinForKo(this.getPoint(row, column));

			} else {
				if (log.isDebugEnabled()) {
					log.debug("error of jie");
				}
				throw new RuntimeException("error of jie");
			}

		}
	}

	/**
	 * @param row
	 * @param column
	 */
	private void dealBlankPoint(final byte row, final byte column) {
		byte m1;
		byte n1;
		byte i;
		// RECORD STEP LOCATION AND NEARBY BLANK POINT.
		// �ж��Ƿ������������ɡ�
		// before forward one step. the original blank point block.
		Block blankPointBlock = boardPoints[row][column].getBlock();
		if (log.isDebugEnabled()) {
			log.debug("���ӵ����ڿհ�����Ϊ��" + blankPointBlock.getTotalNumberOfPoint());
			log.debug(boardPoints[row][column].getBlock().toString());
		}
		boolean removeSuccess = blankPointBlock
				.removePoint(points[row][column]);
		if (log.isDebugEnabled()) {
			log.debug("does fail or success to delete " + removeSuccess);
		}
		// �Ѿ��۳����ӵ㡣
		if (log.isDebugEnabled()) {
			log
					.debug("���ӵ����ڿհ׿�������Ϊ��"
							+ blankPointBlock.getTotalNumberOfPoint());
			log.debug(boardPoints[row][column].getBlock().toString());
			// ԭ�������Ӻ��������
		}
		if (blankPointBlock.getTotalNumberOfPoint() == 0) {
			boardPoints[row][column].setBlock(null);
		} else {
			if (blankPointBlock.getColor() == ColorUtil.BLANK_POINT) {
				if (log.isDebugEnabled()) {
					log.debug("����!---");
				}
			}
			List<Point> blankPoint = new ArrayList<Point>(5);
			blankPoint.add(points[row][column]);
			if (log.isDebugEnabled()) {
				log.debug("һ����¼ֱ������ֱ������Ϊ");
			}
			for (i = 0; i < 4; i++) { // ֱ�ӵ����������������ɵ�����
				m1 = (byte) (row + szld[i][0]);
				n1 = (byte) (column + szld[i][1]);
				if (boardPoints[m1][n1].getColor() == ColorUtil.BLANK_POINT) {
					// 2.1the breath of blank
					blankPoint.add(points[m1][n1]);
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("���ӵ���Χ��ԭʼֱ������Ϊ��" + (blankPoint.size() - 1)
						+ blankPoint);
			}
			if (blankPoint.size() > 1) {
				divideBlankPointBlock(blankPoint);
			}
		}
	}

	/**
	 * bug fix 20050509 ��Ȼ���˼��ϣ�ֱ��ɾ�����㼴�ɡ������ж����ڵ��Ƿ�ͬ�顣 ������ֱ�Ӽ�һ���ķ�ʽ���´��� deal with
	 * points with different color(enemy point)
	 * 
	 * @param row
	 * @param column
	 * @param enemyColor
	 * @param jubutizishu
	 * @param tkd
	 * @param sameColorBlock
	 * @return
	 */
	private byte dealEnemyPoint(final byte row, final byte column,
			byte enemyColor, Block sameColorBlock) {
		byte m1;
		byte n1;
		byte i;
		byte jubutizishu = 0;
		byte tkd = 0;
		if (log.isDebugEnabled()) {
			log.debug("����������ɫ����");
		}
		// Set enemyBlocks = new HashSet(4);
		for (i = 0; i < 4; i++) { // �ȴ�����ɫ����
			m1 = (byte) (row + szld[i][0]);
			n1 = (byte) (column + szld[i][1]);
			if (boardPoints[m1][n1].getColor() == enemyColor) { // 1.1�ұ����ڵ�

				Block tempEnemyBlock = boardPoints[m1][n1].getBlock();
				int qi = subtractBreathOfNeighbourBlock(row, column,
						sameColorBlock, m1, n1);
				// qi=(byte) (boardPoints[m1][n1].getBlock().getBreaths() - 1);
				qi = tempEnemyBlock.getBreaths();

				if (qi == 0) { // ԭ��������
					// �����м���������Ϊ�˻��崦����
					tkd++; // <=4
					jubutizishu += boardPoints[m1][n1].getBlock()
							.getTotalNumberOfPoint(); // ʵ�ʵ�������
					if (log.isDebugEnabled()) {
						log.debug("�鱻�ԣ�behalf point is ��"
								+ boardPoints[m1][n1].getBlock()
										.getTopLeftPoint());
					}
					dealEatenBlock(row, column, m1, n1);
					// TODO:�����µĶ����飬��Χ�Ŀ���ָ��ָ��ö����顣
				} else if (qi < 0) {
					if (log.isDebugEnabled()) {
						log.debug("��������:kin="
								+ boardPoints[m1][n1].getBlock()
										.getTopLeftPoint());
					}
					throw new RuntimeException("��������<0");
				}
			} // ���ظ���
		}
		return jubutizishu;
	}

	/**
	 * @param row
	 * @param column
	 * @param sameColorBlock
	 * @param m1
	 * @param n1
	 * @param qi
	 */
	private int subtractBreathOfNeighbourBlock(final byte row,
			final byte column, Block sameColorBlock, byte m1, byte n1) {
		if (log.isDebugEnabled()) {
			log.debug("remove breath form block:"
					+ boardPoints[m1][n1].getBlock().getTopLeftPoint());
		}
		// TODO:record these bugs
		// boardPoints[m1][n1].getBlock().removeBreathPoint(points[m1][n1]);
		// fix a big bug.--20050807. number 1 gomanual 97th step error.
		boardPoints[m1][n1].getBlock().removeBreathPoint(points[row][column]);
		boardPoints[m1][n1].getBlock().addEnemyBlock(sameColorBlock);
		sameColorBlock.addEnemyBlock(boardPoints[m1][n1].getBlock());
		if (log.isDebugEnabled()) {
			log.debug("��" + boardPoints[m1][n1].getBlock().getTopLeftPoint()
					+ "������Ϊ" + boardPoints[m1][n1].getBlock().getBreaths());
		}
		int qi = boardPoints[m1][n1].getBlock().getBreaths();
		if (qi == 1) {
			if (log.isDebugEnabled()) {
				log.debug("�鱻��ԣ����Ϊ��"
						+ boardPoints[m1][n1].getBlock().getTopLeftPoint());
			}
		}
		return qi;
	}

	/**
	 * @param row
	 * @param column
	 * @param m1
	 * @param n1
	 */
	private void dealEatenBlock(final byte row, final byte column, byte m1,
			byte n1) {
		this.addBreathAfterEatBlock(boardPoints[m1][n1].getBlock());

		if (log.isDebugEnabled()) {
			log.debug("�γ��µ����飻");
		}
		boardPoints[m1][n1].getBlock().changeColorToBlank();
		boardPoints[m1][n1].setColor(ColorUtil.BLANK_POINT);

		boardPoints[row][column].getBlock().addBreathBlock(
				boardPoints[m1][n1].getBlock());
		// this.blankPointBlocks.add(boardPoints[m1][n1].getBlock());
	}

	/**
	 * deal with point with same color.
	 * 
	 * @param row
	 * @param column
	 * @param selfColor
	 * @return
	 */
	private Block dealSelfPoint(final byte row, final byte column,
			final byte selfColor) {
		byte m1;
		byte n1;
		byte i;
		if (log.isDebugEnabled()) {
			log.debug("�㡢������ʼ���ӿ顣");
		}
		// 20050523

		Block sameColorBlock = new Block();

		boardPoints[row][column].setBlock(sameColorBlock);
		sameColorBlock.addPoint(points[row][column]);
		sameColorBlock.setColor(selfColor);
		// if (selfColor == ColorUtil.BLACK) {
		// this.blackBlocks.add(sameColorBlock);
		// } else if (selfColor == ColorUtil.WHITE) {
		// this.whiteBlocks.add(sameColorBlock);
		// }

		if (log.isDebugEnabled()) {
			log.debug("��������ͬɫ����");
		}
		for (i = 0; i < 4; i++) { // �ٴ���ͬɫ����
			m1 = (byte) (row + szld[i][0]);
			n1 = (byte) (column + szld[i][1]);

			if (boardPoints[m1][n1].getColor() == selfColor) { // 3.1
				if (log.isDebugEnabled()) {
					log.debug("ͬɫ�㣺a=" + m1 + ",b=" + n1);
				}

				sameColorBlock.addPoint(boardPoints[m1][n1].getBlock());
				changeColorForAllPoints(boardPoints[m1][n1].getBlock(),
						sameColorBlock);
				boardPoints[m1][n1].getBlock().changeBlockForEnemyBlock(
						sameColorBlock);

			}
			// �������С����Χ�������Сֵ��Σ�յģ���Ȼ��Ҫ����
			// �Ƿ��г������ֶΡ�

		}
		return sameColorBlock;
	}

	/**
	 * verify calculate Flag
	 * 
	 */
	private void verifyFlag() {
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				if (boardPoints[i][j].isCalculatedFlag()) {
					if (log.isDebugEnabled()) {
						log.debug("Flag Error: [" + i + "," + j + "]");
					}
					boardPoints[i][j].setCalculatedFlag(false);
					throw new RuntimeException("Flag Error");
				}
			}
		}
	}

	/**
	 * clear calculate flag
	 * 
	 */
	private void clearFlag() {
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				boardPoints[i][j].setCalculatedFlag(false);
			}
		}
	}

	/**
	 * ���Ӻ���ԭ�ȵĿհ׿���ܷ���.
	 * 
	 * @param blankPoint
	 *            List 0: the original point--step location 1 .... neighbour
	 *            blank point
	 */
	private void divideBlankPointBlock(List blankPoint) {

		byte row = ((Point) blankPoint.get(0)).getRow();
		byte column = ((Point) blankPoint.get(0)).getColumn();
		// necessary!
		boardPoints[row][column].setCalculatedFlag(true);
		// TODO:now is not suitable to access like this
		int numberOfBlankPoints = boardPoints[row][column].getBlock()
				.getTotalNumberOfPoint();
		/* 11��22�գ�first�������������(divide) */
		byte m1, n1, m2, n2, m3, n3, m4, n4, x, y;
		switch ((blankPoint.size() - 1)) {

		case 1: {
			if (log.isDebugEnabled()) {
				log.debug("ֱ������Ϊ1��û���¿����ɡ�");
			}
			// only consider breath block, not other blank point block.
			// zikuai[zikuaishu].addqikuaihao(yuanqikuaisuoyin);
			break;
		}
			/*
		 * 
		 */
		case 2: {
			if (log.isDebugEnabled()) {
				log.debug("ֱ������Ϊ2��");
			}
			m1 = ((Point) blankPoint.get(1)).getRow();
			n1 = ((Point) blankPoint.get(1)).getColumn();
			m2 = ((Point) blankPoint.get(2)).getRow();
			n2 = ((Point) blankPoint.get(2)).getColumn();
			if (m1 == m2 || n1 == n2) {
				// �����㡣�����¿�
				if (log.isDebugEnabled()) {
					log.debug("����ͬ�ᡣ");
				}

			} else {
				if (log.isDebugEnabled()) {
					log.debug("���㲻ͬ�ᣬ");
				}
				x = (byte) (m1 + m2 - row);
				y = (byte) (n1 + n2 - column);
				if (boardPoints[x][y].getColor() == ColorUtil.BLANK_POINT) {
					if (log.isDebugEnabled()) {
						log.debug("�Խǵ�Ϊ�գ�û���¿����ɡ�");
					}
					break;
				} else if (jgzs(x, y) == 1) {
					if (log.isDebugEnabled()) {
						log.debug("�Խǵ���Χ�գ�û���¿����ɡ�");
					}
					break;
				}

			}

			realDivideBlankPointBlock(blankPoint);

			break;
		}
		case 3: {
			byte lianjieshu = 0;
			m1 = ((Point) blankPoint.get(1)).getRow();
			n1 = ((Point) blankPoint.get(1)).getColumn();
			m2 = ((Point) blankPoint.get(2)).getRow();
			n2 = ((Point) blankPoint.get(2)).getColumn();
			m3 = ((Point) blankPoint.get(3)).getRow();
			n3 = ((Point) blankPoint.get(3)).getColumn();
			if (m1 == m2 || n1 == n2) {

			} else {
				x = (byte) (m1 + m2 - row);
				y = (byte) (n1 + n2 - column);
				if (boardPoints[x][y].getColor() == ColorUtil.BLANK_POINT) {
					lianjieshu++;
				} else if (jgzs(x, y) == 1) {
					lianjieshu++;
				}

			}
			if (m1 == m3 || n1 == n3) {

			} else {
				x = (byte) (m1 + m3 - row);
				y = (byte) (n1 + n3 - column);
				if (boardPoints[x][y].getColor() == ColorUtil.BLANK_POINT) {
					lianjieshu++;

				} else if (jgzs(x, y) == 1) {
					lianjieshu++;

				}

			}
			if (m2 == m3 || n2 == n3) {

			} else {
				x = (byte) (m2 + m3 - row);
				y = (byte) (n2 + n3 - column);
				if (boardPoints[x][y].getColor() == ColorUtil.BLANK_POINT) {
					lianjieshu++;
				} else if (jgzs(x, y) == 1) {
					lianjieshu++;
				}

			}
			if (lianjieshu >= 2) {
				if (log.isDebugEnabled()) {
					// log.debug()
					log.debug("û���¿����ɡ�");

				}
				break;
			} else {
				realDivideBlankPointBlock(blankPoint);
			}
			break;
		}
		case 4: {
			byte lianjieshu = 0;
			for (byte bianli = 0; bianli < 4; bianli++) {
				m1 = (byte) (row + szdjd[bianli][0]);
				n1 = (byte) (column + szdjd[bianli][1]);
				if (boardPoints[m1][n1].getColor() == ColorUtil.BLANK_POINT) {
					lianjieshu++;
					// ͨ���Խǵ�����
				} else if (jgzs(m1, n1) == 1) {
					lianjieshu++;
					// ͨ���Ź�����
				}
			}

			if (lianjieshu >= 3) {
				if (log.isDebugEnabled()) {
					log.debug("ֱ������Ϊ4��������Ϊ3��û���¿����ɡ�");
				}
				break;
			} else {
				realDivideBlankPointBlock(blankPoint);

			}
			break;
		}

		}
		clearFlag();
	}

	private byte jgzs(byte m, byte n) {
		return jiugongzishu(m, n);
	}

	/**
	 * calculate will two blank point will connect in an 3*3 part.
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	private byte jiugongzishu(byte m, byte n) { // �Ź�������
		// m,nΪ�Ź����ĵ㡣�����ĵ㲻���룩
		byte dang = 0; // ��������
		byte i, a, b; // ����ָ�ʱ����ɢ�����ɵ�����������㣻
		for (i = 0; i < 4; i++) {
			a = (byte) (m + szld[i][0]);
			b = (byte) (n + szld[i][1]);
			if (boardPoints[a][b].getColor() != ColorUtil.BLANK_POINT) { // 2.1the
				// breath
				// of
				// blank
				dang++;
			}
		}
		for (i = 0; i < 4; i++) {
			a = (byte) (m + szdjd[i][0]);
			b = (byte) (n + szdjd[i][1]);
			if (boardPoints[a][b].getColor() != ColorUtil.BLANK_POINT) { // 2.1the
				// breath
				// of
				// blank
				dang++;
			}
		}

		return dang;

	}

	private short makeBlankPointBlock(byte a, byte b, Block newBlock) {
		// ��Ϊ�����жϵ���ʱ����ÿ�ε��ö������־������
		// �������������־��
		byte m1, n1;

		newBlock.addPoint(points[a][b]);
		// if (log.isDebugEnabled()) {
		// log.debug("�������ӵ㣺" + a + "/" + b);
		// }
		// if (shoushu==145) {
		// log.debug("�������ӵ㣺" + a + "/" + b);
		// }
		boardPoints[a][b].setCalculatedFlag(true);
		boardPoints[a][b].setBlock(newBlock);

		for (byte k = 0; k < 4; k++) {
			m1 = (byte) (a + szld[k][0]);
			n1 = (byte) (b + szld[k][1]);
			if (boardPoints[m1][n1].isCalculatedFlag()) {
				continue;
			}

			if (boardPoints[m1][n1].getColor() == ColorUtil.BLANK_POINT) {
				makeBlankPointBlock(m1, n1, newBlock);

			} else if (boardPoints[m1][n1].getColor() == ColorUtil.BLACK
					|| boardPoints[m1][n1].getColor() == ColorUtil.WHITE) {
				// qikuai[qikuaishu].addzikuaihao(zbk[m1][n1]);
				// ���������֮���ٽ�������Ϊ����ſ��ܻ�䡣
			}
		}
		return newBlock.getTotalNumberOfPoint();
	} // �ɿ�ĵ�SQBZXB==1;

	/**
	 * general make Block
	 * 
	 * @param a
	 * @param b
	 * @param newBlock
	 * @return
	 */
	private short makeBlock(final byte a, final byte b, final byte color,
			Block newBlock) {
		// ��Ϊ�����жϵ���ʱ����ÿ�ε��ö������־������
		// �������������־��
		byte m1, n1;

		newBlock.addPoint(points[a][b]);
		// if (log.isDebugEnabled()) {
		// log.debug("�������ӵ㣺" + a + "/" + b);
		// }
		boardPoints[a][b].setCalculatedFlag(true);
		boardPoints[a][b].setBlock(newBlock);

		for (byte k = 0; k < 4; k++) {
			m1 = (byte) (a + szld[k][0]);
			n1 = (byte) (b + szld[k][1]);

			if (boardPoints[m1][n1].getColor() == color) {
				if (boardPoints[m1][n1].isCalculatedFlag()) {
					continue;
				}
				makeBlock(m1, n1, color, newBlock);
				// bug fix 3; wrong invocation.
				// makeBlankPointBlock(m1, n1, newBlock);
			}
		}
		return newBlock.getTotalNumberOfPoint();
	}

	private Point getPoint(byte row, byte column) {
		return boardPoints[row][column].getPoint();
	}

	public BoardPoint getBoardPoint(byte row, byte column) {
		return boardPoints[row][column];
	}

	private BoardPoint getBoardPoint(Point point) {
		return boardPoints[point.getRow()][point.getColumn()];
	}

	public BoardColorState getBoardColorState() {
		BoardColorState boardState = new BoardColorState();
		// bug fix 20050509
		// for (int i = 1; i < 19; i++) {
		for (int i = 1; i <= 19; i++) {
			// for (int j = 1; j < 19; j++) {
			for (int j = 1; j <= 19; j++) {
				boardState.add(boardPoints[i][j]);
				//

			}
		}
		return boardState;
	}

	private void realDivideBlankPointBlock(List blankPoint) {
		byte row = ((Point) blankPoint.get(0)).getRow();
		byte column = ((Point) blankPoint.get(0)).getColumn();
		byte m1, n1;
		int zishujishu = 0;
		int numberOfBlankPoints = boardPoints[row][column].getBlock()
				.getTotalNumberOfPoint();
		// 20050523 fix bug
		// this.blankPointBlocks.remove(boardPoints[row][column].getBlock());
		Block[] newBlock = new Block[5];
		newBlock[1] = new Block();
		newBlock[2] = new Block();
		newBlock[3] = new Block();
		newBlock[4] = new Block();
		// for (byte bianli = 1; bianli <= blankPoint.size(); bianli++) {
		for (byte bianli = 1; bianli < blankPoint.size(); bianli++) {
			m1 = ((Point) blankPoint.get(bianli)).getRow();
			n1 = ((Point) blankPoint.get(bianli)).getColumn();

			// bug fix for number inconsistent begin!
			if (boardPoints[m1][n1].isCalculatedFlag()) {
				continue;
			}
			// bug fix for number inconsistent end!

			zishujishu += makeBlankPointBlock(m1, n1, newBlock[bianli]);
			// if (zishujishu == numberOfBlankPoints) { // �Ѿ�ɨ�����
			// this.blankPointBlocks.add(newBlock[bianli]);
			// return;
			//
			// }
		}
		if (zishujishu != numberOfBlankPoints) {

			for (int ii = 1; ii < 20; ii++) {
				for (int jj = 1; jj < 20; jj++) {
					if (boardPoints[ii][jj].getColor() == ColorUtil.BLANK_POINT
							&& !newBlock[1].getAllPoints().contains(
									boardPoints[ii][jj])) {
						log.debug("ii=" + ii + ",jj=" + jj + "state="
								+ boardPoints[ii][jj].isCalculatedFlag());
					}
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("shoushu=" + shoushu);
				log.debug("zishujishu=" + zishujishu);
			}
			log.debug("numberOfBlankPoints=" + numberOfBlankPoints);
			throw new RuntimeException(
					"realDivideBlankPointBlock error:shoushu=" + shoushu);
		}
	}

	/**
	 * all black/white/blank blocks. because the blocks will be changed after
	 * they were put into the Set. so can not correctly remove it. so it is
	 * almost useless to collect them into a set.
	 */
	// Set blackAndWhiteBlocks = new HashSet();
	//
	// Set whiteBlocks = new HashSet();
	//
	// Set blackBlocks = new HashSet();
	//
	// Set blankPointBlocks = new HashSet();

	/**
	 * 
	 * @author eddie
	 * 
	 * 
	 *         add function to scan the state! and use it to compare with the
	 *         incremental calculation result.
	 */
	public void generateHighLevelState() {
		// �����׵�λͼ��ʾ����kuai��zb�������Ӧ��Ϣ
		// ����ʽ�������ɾ�����Ŀ�������ģ���Ҫ�޸ġ�
		// �ú���Ӧ���ڳ�ʼ����ʱ���á�

		byte i, j;
		byte m, m1, n1, n;
		byte qkxlhzs = 0; // �������ں�����
		byte qkxlbzs = 0;
		byte color = 0;
		byte othercolor = 0;
		byte qikuaizishu;
		byte meikuaizishu;

		Set<Block> blackBlocks = new HashSet<Block>();
		Set<Block> whiteBlocks = new HashSet<Block>();
		Set<Block> blankPointBlocks = new HashSet<Block>();
		Set<Block> blackAndWhiteBlocks = new HashSet<Block>();

		if (log.isInfoEnabled()) {
			log.info("come into method generateHighLevelState");
		}
		generateAllKindsBlocks(blackBlocks, whiteBlocks, blankPointBlocks);
		blackAndWhiteBlocks.addAll(whiteBlocks);
		blackAndWhiteBlocks.addAll(blackBlocks);

		clearFlag();
		for (Iterator iter = blackAndWhiteBlocks.iterator(); iter.hasNext();) {
			calculateBreath((Block) iter.next());
		}

		/**
		 * for higher level data structure.
		 */

		/*
		 * byte zijishu = 0; short kzishu = 0; //�麬�е�����������ѭ��
		 * 
		 * short zikin1; byte zikin2; Set blackBlock = new HashSet(); for
		 * (Iterator iter = blackBlock.iterator(); iter.hasNext();) { Block
		 * tempBlock = ((Block) (iter.next())); //for (i = 1; i <= zikuaishu;
		 * i++) { color = tempBlock.getColor(); if (color == Constant.BLACK) {
		 * othercolor = Constant.WHITE; } else if (color == Constant.WHITE) {
		 * othercolor = Constant.BLACK; //�����ÿ�ĸ����ӣ��õ���Χ��ɫ�飬�����ֹ�ظ��� } kzishu =
		 * tempBlock.getTotalNumberOfPoint();
		 * 
		 * for (Iterator iter1 = tempBlock.getAllBoardPoints().iterator(); iter
		 * .hasNext();) { // for (temp = zikuai[i].zichuang; temp != null; temp
		 * = // temp.next) { BoardPoint tempBoardPoint = (Point) iter.next();
		 * for (j = 0; j < 4; j++) {
		 * 
		 * m = (byte) (tempBoardPoint.getRow() + szld[j][0]); n = (byte)
		 * (tempBoardPoint.getColumn() + szld[j][1]); if
		 * (boardPoints[m][n].getColor() == othercolor) {
		 * tempBlock.addEnemyBlock(boardPoints[m][n].getBlock()); } else if
		 * (boardPoints[m][n].getColor() == ColorUtil.BLANK_POINT) {
		 * 
		 * //TODO:breath block. } } //for } }
		 */
		if (log.isInfoEnabled()) {
			log.info("return from method generateHighLevelState");
		}
	}

	public void dealStrongAndWeak() {
		// Set blankPointBlock=new HashSet();
		// for(Iterator iter=blankPointBlock.iterator();iter.hasNext();){
		// //for (i = 1; i <= qikuaishu; i++) { //���������֣��ҵ�ǿ�顣
		// short meikuaizishu = qikuai[i].zishu;
		// if (meikuaizishu == 1) { //��λ
		// //���ܱ�Ȼ���ӡ�
		// m = qikuai[qikuaishu].zichuang.a;
		// n = qikuai[qikuaishu].zichuang.b;
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		//
		// if (zb[m1][n1][ZTXB] == WHITE) {
		// qkxlbzs++;
		// }
		// else if (zb[m1][n1][ZTXB] == BLACK) {
		// qkxlhzs++;
		// }
		// }
		// if (qkxlbzs == 0) { //�ڷ�����
		// qikuai[i].color = Constant.BLACK;
		// if (qikuaizuixiaoqi(i) > 1) {
		// //���ɵ��۲����������ƻ���
		// erjikuaishu++;
		// erjikuai[erjikuaishu] = new ErJiKuai();
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		// if (zb[m1][n1][ZTXB] == BLACK) {
		// erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
		// }
		//
		// }
		//
		// }
		// else {
		// //���������鱾���ڱ����״̬��
		// }
		//
		// }
		// else if (qkxlhzs == 0) { //�׷����ۣ�����һ�塣
		// qikuai[i].color = Constant.WHITE;
		// if (qikuaizuixiaoqi(i) > 1) {
		// erjikuaishu++;
		// erjikuai[erjikuaishu] = new ErJiKuai();
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		// if (zb[m1][n1][ZTXB] == BLACK) {
		// erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
		// }
		//
		// }
		//
		// }
		// else {
		// //todo
		// }
		//
		// }
		// else { //˫���Ĺ�����
		// qikuai[i].color = 5;
		// }
		// //qikuai[qikuaishu--].zichuang=null;
		// //qikuai[qikuaishu].qichuang
		// //qikuai[ki--][1][1]=0;
		// //zb[j][i][KSYXB]=0;//�ǿ�
		// //todo:��λ�Ĵ���
		// //�ҳ���Χ�飬����Ϊһ�ҵ��ӣ����١�����Ϊһ����
		// //��˫�ᣬ��Ϊһ�������໹һ����
		// }
		// else if (meikuaizishu > 1) { //����
		// // qikuai[qikuaishu].zishu = meikuaizishu;
		// DianNode1 tee = qikuai[i].zichuang;
		// for (short hh = 1; hh <= meikuaizishu; hh++) {
		// m = tee.a;
		// n = tee.b;
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		//
		// if (zb[m1][n1][ZTXB] == WHITE) {
		// qkxlbzs++;
		// }
		// else if (zb[m1][n1][ZTXB] == BLACK) {
		// qkxlhzs++;
		// }
		// }
		// tee = tee.next;
		// }
		// if (qkxlbzs == 0) { //�ڷ��Ĵ���
		// //��Χ�Ŀ�����Խ��Խ�á�
		// if (qikuaizuixiaoqi(i) > 1) {
		// erjikuaishu++;
		// erjikuai[erjikuaishu] = new ErJiKuai();
		//
		// for (short hh = 1; hh <= meikuaizishu; hh++) {
		// m = tee.a;
		// n = tee.b;
		//
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		// if (zb[m1][n1][ZTXB] == BLACK) {
		// qikuai[i].addzikuaihao(zbk[m1][n1]);
		// erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
		// }
		//
		// }
		// }
		// }
		// }
		// else if (qkxlhzs == 0) { //�׷��Ĵ��ۣ�����һ�塣
		// if (qikuaizuixiaoqi(i) > 1) {
		// erjikuaishu++;
		// erjikuai[erjikuaishu] = new ErJiKuai();
		//
		// for (short hh = 1; hh <= meikuaizishu; hh++) {
		// m = tee.a;
		// n = tee.b;
		//
		// for (byte k = 0; k < 4; k++) {
		// m1 = (byte) (m + szld[k][0]);
		// n1 = (byte) (n + szld[k][1]);
		// if (zb[m1][n1][ZTXB] == WHITE) {
		// qikuai[i].addzikuaihao(zbk[m1][n1]);
		// erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
		// }
		//
		// }
		// }
		// }
		//
		// }
		// else { //˫���Ĺ�����
		//
		// }
		//
		// }
		// else {
		// if (CS.DEBUG_CGCL) {
		// log.debug("error:zishu<1");
		//
		// }
		// }
		//
		// }

		/*
		 * byte zijishu = 0; byte kuaijishu = 0; for (i = 1; i <= dandianshu;
		 * i++) { zijishu = 0; kuaijishu = 0; m = dandian[i][0]; n =
		 * dandian[i][1]; color = zb[m][n][ZTXB]; if (color == 1) othercolor =
		 * 2; else if (color == 2) othercolor = 1; for (j = 0; j < 4; j++) { m =
		 * (byte) (dandian[i][0] + szld[j][0]); n = (byte) (dandian[i][1] +
		 * szld[j][1]); if (zb[m][n][ZTXB] == othercolor) { if (zb[m][n][KSYXB]
		 * == 0) { zb2[m][n][2 * zijishu] = m; zb2[m][n][2 * zijishu + 1] = n;
		 * zijishu++; } else{//ע���ֹ����ظ��� } } }//for }
		 */

	}

	/**
	 * scan the goboard to generate all three kinds of blocks.
	 */
	private void generateAllKindsBlocks(Set<Block> blackBlocks,
			Set<Block> whiteBlocks, Set<Block> blankPointBlocks) {
		if (log.isDebugEnabled()) {
			log.debug("generateAllKindsBlocks");
		}
		byte i;
		byte j;
		// byte qikuaizishu;
		// byte meikuaizishu;
		// ��һ��ɨ�裬���ɿ飬�������������Ӵ���
		this.clearFlag();
		for (i = 1; i < 20; i++) { // i��������
			for (j = 1; j < 20; j++) { // j�Ǻ�����,���д���
				// boardPoints[j][i].isCalculatedFlag()
				if (boardPoints[j][i].isCalculatedFlag()) {
					continue; // SQBZXB�˴��൱�ڴ�����ı�־.
				}
				// �õ���δ����
				// qikuaizishu = 0; // ÿ���ӵļ���,������������
				// meikuaizishu = 0; // ÿ���ӵļ��������������ӿ�
				if (boardPoints[j][i].getColor() == ColorUtil.BLACK) { // ��.�ϱ�Ϊ�յ����ɫ��
					if (log.isDebugEnabled()) {
						log.debug("black:j=" + j + "," + "i=" + i);
					}
					makeBlock(j, i, ColorUtil.BLACK, new Block(ColorUtil.BLACK));
					// log.debug("added block:"+boardPoints[j][i].getBlock());
					// log.debug("hashcode of added
					// block"+boardPoints[j][i].getBlock().hashCode());
					blackBlocks.add(boardPoints[j][i].getBlock());
				} else if (boardPoints[j][i].getColor() == ColorUtil.WHITE) { // ��.�ϱ�Ϊ�յ����ɫ��
					if (log.isDebugEnabled()) {
						log.debug("white:j=" + j + "," + "i=" + i);
					}
					makeBlock(j, i, ColorUtil.WHITE, new Block(ColorUtil.WHITE));
					if (log.isDebugEnabled()) {
						log
								.debug("added block:"
										+ boardPoints[j][i].getBlock());
						log.debug("hashcode of added block:"
								+ boardPoints[j][i].getBlock().hashCode());
					}
					whiteBlocks.add(boardPoints[j][i].getBlock());
				} else if (boardPoints[j][i].getColor() == ColorUtil.BLANK_POINT) {
					if (log.isDebugEnabled()) {
						log.debug("blank:j=" + j + "," + "i=" + i);
					}
					makeBlock(j, i, ColorUtil.BLANK_POINT, new Block(
							ColorUtil.BLANK_POINT));
					if (log.isDebugEnabled()) {
						log
								.debug("added block:"
										+ boardPoints[j][i].getBlock());
						log.debug("hashcode of added block"
								+ boardPoints[j][i].getBlock().hashCode());
					}
					blankPointBlocks.add(boardPoints[j][i].getBlock());
				} else {
					throw new RuntimeException("err generateAllKindsBlocks");
				}
				// qikuaizishu = 0;
			}
		} // ���ɿ�,������顣
	}

	/**
	 * get black blocks form current state structure.
	 * 
	 * @return Set
	 */
	public Set getBlackBlocksFromState() {

		return getSetFromState(ColorUtil.BLACK);
	}

	/**
	 * get blank point blocks form current state structure.
	 * 
	 * @return Set
	 */
	public Set getBlankPointBlocksFromState() {
		return getSetFromState(ColorUtil.BLANK_POINT);
	}

	public Set<Block> getSetFromState(int color) {
		Set<Block> blocks = new HashSet<Block>();
		Block temp = null;
		for (int i = 1; i <= 19; i++) {
			for (int j = 1; j <= 19; j++) {
				if (boardPoints[i][j].getColor() == color) {
					temp = boardPoints[i][j].getBlock();
					blocks.add(temp);
				}
			}
		}
		// log.debug("return from getBlackBlocksFromState");
		return blocks;

	}

	/**
	 * transfer into bidimension array
	 * 
	 * @return
	 */
	public byte[][] getBreathArray() {
		byte[][] a = new byte[21][21];
		byte i, j, k;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				if (boardPoints[i][j].getColor() == ColorUtil.BLACK
						|| boardPoints[i][j].getColor() == ColorUtil.WHITE) {
					a[i][j] = (byte) boardPoints[i][j].getBreaths();
				}
			}
		}
		return a;
	}

	public BoardBreathState getBoardBreathState() {
		return new BoardBreathState(this.getBreathArray());
	}

	/**
	 * get white blocks form current state structure.
	 * 
	 * @return Set
	 */
	public Set getWhiteBlocksFromState() {
		return getSetFromState(ColorUtil.WHITE);
	}

	byte zhengzijieguo[][] = new byte[127][2];

	public byte[][] jiSuanZhengZi(byte a, byte b) {
		return jisuanzhengziWithClone(a, b);
	}

	public byte[][] jiSuanZhengZi(Point point) {
		return jisuanzhengziWithClone(point.getRow(), point.getColumn());
	}

	/**
	 * 
	 * �������ӣ����ǲ������ں��н���������� ΪMAXMIN���̡� �����ӷ����������ӳ���Ϊ127�����Ӳ�����Ϊ-127��
	 * ���ӷ����ߡ�a,b�Ǳ����ӷ�����е�һ���㡣��Ϊ��ſ��ܸı䡣 �÷��������õ��������㷨��
	 * 
	 * @param row
	 * @param column
	 * @return a list of step(Points)
	 */
	public byte[][] jisuanzhengziWithClone(byte row, byte column) {

		byte MAX = 1; // �������ӷ�
		byte MIN = 2; // �������ӷ�

		byte m1, n1;

		// ��������Ŀ顣
		Block blockToBeEaten = boardPoints[row][column].getBlock();
		if (log.isDebugEnabled()) {
			log.debug("block: " + blockToBeEaten);
		}
		byte houxuan[][] = new byte[5][2]; // ��ѡ�㡲0����0���洢������
		GoBoard[] go = new GoBoard[100000];
		byte[] za = new byte[100000]; // ������go��Ӧ�������ߵ�ĺ����ꡣ
		byte[] zb = new byte[100000]; // ������go��Ӧ�������ߵ�������ꡣ
		int jumianshu = 0; // ��ǰ���о���š�
		byte SOUSUOSHENDU = 120;
		byte cengshu = 0; // ��ǰ���в�����

		Controller[] controllers = new Controller[SOUSUOSHENDU];
		int[][] st = new int[SOUSUOSHENDU][5]; // �����������ΪSOUSUOSHENDU��
		// 0:�ò���ʼ����š�
		// 1:�������һ��ȡmax����min
		// 2:��һ����Լ��Ĳ�ȡmax����min
		// 3:��ǰ���Ѿ���һ������ȷ���ˡ�Ϊ��Ӧ��ֵ��
		// 4:�ò㻹�ж��پ��档��0��ò������
		Controller controller = null;
		for (byte i = 0; i < SOUSUOSHENDU; i++) {
			// ������������ȡ�
			st[i][1] = MAX; // ���²�ȡMAX���ò���max�¡�
			// ���²�(����֮���״̬)ȡMAX��
			st[i][2] = MIN; // ��ͬһ��ȡMIN
			controller = new Controller();
			controller.setWhoseTurn(MAX);
			controllers[i] = controller;
			i++;
			st[i][1] = MIN; // ���²�ȡMIN��
			st[i][2] = MAX;
			controller = new Controller();
			controller.setWhoseTurn(MIN);
			controllers[i] = controller;

		}

		byte turncolor1 = 0; // ���������Ҫ�η����ߡ�
		byte turncolor2 = 0; // ԭʼ����ԭ����˭�ߣ�
		if (blockToBeEaten.getColor() == ColorUtil.BLACK) {
			shoushu = 1;
			turncolor1 = ColorUtil.WHITE;
			if (log.isDebugEnabled()) {
				log.debug("Ҫ��������Ϊ��ɫ���ְ׷����ܷ����ӣ�");
			}
		} else if (blockToBeEaten.getColor() == ColorUtil.WHITE) {
			shoushu = 0;
			turncolor1 = ColorUtil.BLACK;
			if (log.isDebugEnabled()) {
				log.debug("Ҫ��������Ϊ��ɫ���ֺڷ����ܷ����ӣ�");
			}
		}

		if ((shoushu % 2) == 0) { // ʵ����������˭�ߡ�
			turncolor2 = ColorUtil.BLACK;
			if (log.isDebugEnabled()) {
				log.debug("������Ӧ�ú��ߡ�");
			}
		} else {
			turncolor2 = ColorUtil.WHITE;
			if (log.isDebugEnabled()) {
				log.debug("������Ӧ�ð��ߡ�");
			}
		}
		log.debug("clone in index:" + 0);
		go[0] = (GoBoard) this.deepClone();

		if (turncolor1 != turncolor2) {
			go[0].giveUp();
			zhengzijieguo[126][0] = 1;
			if (log.isDebugEnabled()) {
				log.debug("��Ҫ��Ȩһ����");
			}
		}

		// 1.��ʼ��
		byte youxiaodian = 0; // ��Υ�����ӹ���ĵ㡣�������ӷ�MAX.
		byte haodian = 0; // ���ڱ����ӷ�������Ч�����ų�����ֱ�ӵó����۵ĵ㡣
		GoBoard temp;

		st[0][0] = 0;
		st[0][4] = 1;
		jumianshu = 0;

		// 2.��ʼ���㡣
		while (true) {
			// ��һ��ѭ����չ�����һ�����档

			if (cengshu >= (SOUSUOSHENDU - 1)) {
				if (log.isDebugEnabled()) {
					log.debug("������100�㣬��û�н�������ز���ȷ���");
				}
				return zhengzijieguo;
			} else {
				cengshu++; // �²�Ĳ�š�
				if (log.isDebugEnabled()) {
					log.debug("�µĵ�ǰ����Ϊ��" + cengshu);
				}
				st[cengshu][0] = jumianshu + 1;
				// �²�Ŀ�ʼ�㡣
				if (log.isDebugEnabled()) {
					log.debug("�²�Ŀ�ʼ��������Ϊ��" + (jumianshu + 1));
				}
			}

			youxiaodian = 0;
			haodian = 0;
			log.debug("clone in index:" + jumianshu);
			temp = (GoBoard) (go[jumianshu].deepClone());
			// temp.initPoints();
			// Ҫչ���ľ��档
			blockToBeEaten = temp.boardPoints[row][column].getBlock();
			if (st[cengshu][1] == MAX) {
				if (log.isDebugEnabled()) {
					log.debug("��ǰ����˭�ߣ�" + "MAX");
				}
				if (log.isDebugEnabled()) {
					log.debug("��һ����˭�ߣ�" + "MIN");
				}
				if (log.isDebugEnabled()) {
					log.debug("����һ����MIN�ߵõ��ò�");

				}
				// DianNode1 lili;
				// HaoNode1 yskh; //��ɫ���
				int ysks; // ��ɫ����
				byte tizidianshu = 0;

				ysks = blockToBeEaten.getEnemyBlocks().size();
				// yskh = zkin.getEnemyBlocks().zwyskhao;
				// for (byte jj = 1; jj <= ysks; jj++) { //���뱻��Եĵ㣻
				for (Iterator iter = blockToBeEaten.getEnemyBlocks().iterator(); iter
						.hasNext();) {
					// ��¼��Χһ���ĵ㡣���ִ��󣬴��һ�㲻����һ�����ɡ�

					Block tempBlock = (Block) iter.next();
					if (tempBlock.getBreaths() == 1) {

						// short beidachikuaihao = yskh.hao;

						Set qi = tempBlock.getAllBreathPoints();
						// lili = temp.zikuai[beidachikuaihao].qichuang;

						Iterator iter2 = tempBlock.getAllBreathPoints()
								.iterator();
						if (iter2.hasNext()) {
							tizidianshu++;
							Point ppp = (Point) iter.next();
							houxuan[tizidianshu][0] = ppp.getRow();
							houxuan[tizidianshu][1] = ppp.getColumn();
						}

					}
					// yskh = yskh.next;
				}

				// lili = temp.zikuai[zkin].qichuang;
				Set temps = blockToBeEaten.getAllBreathPoints();
				if (blockToBeEaten.getBreaths() != 1) {
					if (log.isDebugEnabled()) {
						log.debug("����������Ϊ1��");
					}
					zhengzijieguo[0][0] = -128;
					return zhengzijieguo; // ��ʾ����ʧ�ܡ�
				}

				if (temps == null) {
					if (log.isDebugEnabled()) {
						log.debug("��������1��");
					}
					zhengzijieguo[0][0] = -128;
					return zhengzijieguo; // ��ʾ����ʧ�ܡ�
				}

				Iterator itertemp = temps.iterator();
				if (itertemp.hasNext()) {
					tizidianshu++;
					BoardPoint bp = (BoardPoint) itertemp.next();
					// TODO: two kinds of corrdinate! careful
					houxuan[tizidianshu][0] = bp.getRow(); // �������ظ��ġ�
					houxuan[tizidianshu][1] = bp.getColumn();
				}
				houxuan[0][0] = tizidianshu;
				if (log.isDebugEnabled()) {
					log.debug("�����ӷ���ѡ����Ϊ" + tizidianshu);
				}

				// �����ӷ��ߡ�
				boolean queding = false;
				for (byte i = 1; i <= houxuan[0][0]; i++) {
					// Ŀǰ�������Ǻ�ѡ����֪���������������в���̬�ı�
					// �Ժ�Ӧ�ý��и�ϸ�µĴ�������Ҫչ���ľ���ȷ����

					m1 = houxuan[i][0];
					n1 = houxuan[i][1];
					log.debug("clone in index" + jumianshu);
					temp = (go[jumianshu].deepClone());
					// ��Ϊtemp�����Ѿ����ı䣬�������¸�ֵ��
					// ��չ���ľ��棬��չͬһ���ϼ����档
					if (temp.validate(m1, n1)) { // �жϺϷ��ŵ�
						temp.oneStepForward(m1, n1);
						youxiaodian++;
						if (log.isDebugEnabled()) {
							log.debug("��" + youxiaodian + "����ѡ��Ϊ:(" + m1 + ","
									+ n1 + ")");
						}

						if (temp.boardPoints[row][column].getBreaths() == 1) {
							if (log.isDebugEnabled()) {
								log.debug("���Ӻ����ӷ�����Ϊ1");
							}
						} else if (temp.boardPoints[row][column].getBreaths() == 2) {
							haodian++;
							go[jumianshu + haodian] = temp;
							za[jumianshu + haodian] = m1;
							zb[jumianshu + haodian] = n1;
							if (log.isDebugEnabled()) {
								log.debug("min��" + "(" + m1 + "," + n1 + ")");
							}

						} else if (temp.boardPoints[row][column].getBreaths() == 3) {
							if (log.isDebugEnabled()) {
								log.debug("���Ӻ����ӷ�����Ϊ3");
							}

							cengshu -= 1; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��

							st[cengshu][4] -= 1;
							if (st[cengshu][4] != 0) {
								jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
								// st[cengshu - 2][3] = 127;
								queding = true;
								break; // ����forѭ����
							} else {
								while (true) {
									cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
									if (cengshu == -1) {
										zhengzijieguo[0][0] = -127;
										return zhengzijieguo;
									}

									st[cengshu][4] -= 1;
									if (st[cengshu][4] != 0) {
										jumianshu = st[cengshu][0]
												+ st[cengshu][4] - 1;
										// st[cengshu - 2][3] = 127;
										queding = true;
										break;
									}
								}
							}

						}
					}
				} // forѭ������
				if (log.isDebugEnabled()) {
					log.debug("��Ч��Ϊ:" + youxiaodian);
				}

				if (queding == true) {
					continue;
				} else if (haodian == 0) {
					while (true) {
						cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						if (cengshu == 0) {
							zhengzijieguo[0][0] = 127;

							byte lins = 0;
							for (lins = 2; st[lins][0] != 0; lins++) {
								if (log.isDebugEnabled()) {
									log.debug("��Ϊ:(" + za[st[lins][0] - 1]
											+ "," + zb[st[lins][0] - 1] + ")");
								} // this.cgcl(za[st[lins][0]-1],zb[st[lins][0]-1]);
								zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
								zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];

							}
							zhengzijieguo[0][1] = (byte) (lins - 2);
							return zhengzijieguo;
						}

						st[cengshu][4] -= 1;
						if (st[cengshu][4] != 0) {
							jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
							// st[cengshu - 2][3] = 127;
							break;
						}
					}

				} else {
					st[cengshu][4] = haodian;
					jumianshu += haodian;

				}
			} // max

			// zheng zi fang zou.
			else if (st[cengshu][1] == MIN) {
				if (log.isDebugEnabled()) {
					log.debug("��ǰ����˭�ߣ�" + "MIN");
				}
				if (log.isDebugEnabled()) {
					log.debug("����һ����MAX�ߵõ��ò�");
				}
				// DianNode1 lili = temp.zikuai[zkin].qichuang;
				Set tep = blockToBeEaten.getAllBreathPoints();
				if (blockToBeEaten.getBreaths() != 2) {
					if (log.isDebugEnabled()) {
						log.debug("����������Ϊ����");
					}
					zhengzijieguo[0][0] = -127;
					return zhengzijieguo; // ��ʾ����ʧ�ܡ�
				}
				// for (byte i = 1; i <= 2; i++) {
				int iii = 0;
				for (Iterator iter = tep.iterator(); iter.hasNext();) {
					if (tep.size() == 0) {
						if (log.isDebugEnabled()) {
							log.debug("�����������");
						}
						zhengzijieguo[0][0] = -127;
						return zhengzijieguo; // ��ʾ����ʧ�ܡ�
					}
					iii++;
					Point bpp = (Point) iter.next();
					houxuan[iii][0] = bpp.getRow();
					houxuan[iii][1] = bpp.getColumn();

				}
				houxuan[0][0] = 2;
				for (byte i = 1; i <= houxuan[0][0]; i++) {
					// Ŀǰ�������Ǻ�ѡ����֪���������������в���̬�ı�
					// �Ժ�Ӧ�ý��и�ϸ�µĴ�������Ҫչ���ľ���ȷ����

					m1 = houxuan[i][0];
					n1 = houxuan[i][1];
					log.debug("clone in index" + jumianshu);
					temp = go[jumianshu].deepClone();

					// ��չ���ľ��棬��չͬһ���ϼ����档
					if (temp.validate(m1, n1)) { // �жϺϷ��ŵ�
						temp.oneStepForward(m1, n1);
						youxiaodian++;
						log.debug("set into in index:"
								+ (jumianshu + youxiaodian));
						go[jumianshu + youxiaodian] = temp;
						za[jumianshu + youxiaodian] = m1;
						zb[jumianshu + youxiaodian] = n1;
						if (log.isDebugEnabled()) {
							log.debug("max��" + "(" + m1 + "," + n1 + ")");
						}

					}
				}
				if (youxiaodian == 0) {
					// ���أ�һ�������ӷ����ӿ��¡�
					if (log.isDebugEnabled()) {
						log.debug("��Ч��Ϊ0");

					}
					st[cengshu][0] = 0;

					if (cengshu == 1) { // ���ӷ�ֱ�����ӿ��£�
						zhengzijieguo[0][0] = -127;
						return zhengzijieguo;
					}

					while (true) {
						cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						if (cengshu == -1) {
							zhengzijieguo[0][0] = -127;

							for (byte lins = 2; st[lins][0] != 0; lins++) {
								if (log.isDebugEnabled()) {
									log.debug("��Ϊ:(" + za[st[lins][0] - 1]
											+ "," + zb[st[lins][0] - 1] + ")");
								}
								zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
								zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];

							}

							return zhengzijieguo;
						}

						st[cengshu][4] -= 1;
						if (st[cengshu][4] != 0) {
							jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
							// st[cengshu - 2][3] = 127;
							break;
						}
					}
				} else {

					st[cengshu][4] = youxiaodian;
					jumianshu += youxiaodian;
				}

			} // if min

		} // while

	}

	public void changeColorForAllPoints(Block oldBlock, Block newBlock) {
		for (Iterator iter = oldBlock.getAllPoints().iterator(); iter.hasNext();) {
			this.getBoardPoint(((Point) iter.next())).setBlock(newBlock);
		}
	}

	/**
	 * clone to keep the state in history. avoid the backforward operation.
	 * 
	 * in order to avoid backward. we use the cloned object to store history
	 * data. but maybe it is too difficult to clone the GoBoard because there is
	 * a circle reference.
	 */
	public Object clone() {
		GoBoard temp = null;
		byte i, j, k;
		short t;
		try {
			temp = (GoBoard) (super.clone());
		} catch (CloneNotSupportedException de) {
			de.printStackTrace();
		}
		// temp.boardPoints=(BoardPoint[][])boardPoints.clone();
		boardPoints = new BoardPoint[21][21];
		for (i = Constant.ZBXX; i <= Constant.ZBSX; i++) { // 2��22�ռ�
			// points[Constant.ZBXX][i] = new Point(Constant.ZBXX, i);
			// points[Constant.ZBSX][i] = new Point(Constant.ZBSX, i);
			// points[i][Constant.ZBXX] = new Point(i, Constant.ZBXX);
			// points[i][Constant.ZBSX] = new Point(i, Constant.ZBSX);
			boardPoints[Constant.ZBXX][i] = new BoardPoint(
					points[Constant.ZBXX][i], (byte) ColorUtil.OutOfBound);
			boardPoints[Constant.ZBSX][i] = new BoardPoint(
					points[Constant.ZBSX][i], (byte) ColorUtil.OutOfBound);
			boardPoints[i][Constant.ZBXX] = new BoardPoint(
					points[i][Constant.ZBXX], (byte) ColorUtil.OutOfBound);
			boardPoints[i][Constant.ZBSX] = new BoardPoint(
					points[i][Constant.ZBSX], (byte) ColorUtil.OutOfBound);
		}
		for (i = 1; i <= 19; i++) {
			for (j = 1; j <= 19; j++) {
				temp.boardPoints[i][j] = (BoardPoint) boardPoints[i][j].clone();
			}
		}

		return temp;

	}

	public boolean isBlackTurn() {
		return evenNumberOfPoints(shoushu);
	}

	public boolean possibleVerticalSymmetry() {
		int count = shoushu - pointsInVerticalLine();
		return evenNumberOfPoints(count);
	}

	public boolean possibleHorizontalSymmetry() {
		int count = shoushu - pointsInHorizontalLine();
		return evenNumberOfPoints(count);
	}

	public boolean possibleForwardSlashSymmetry() {
		int count = shoushu - pointsInForwardSlashLine();
		return evenNumberOfPoints(count);
	}

	public boolean possibleBackwardSlashSymmetry() {
		int count = shoushu - pointsInBackwardSlashLine();
		return evenNumberOfPoints(count);
	}

	private boolean evenNumberOfPoints(int count) {
		if (count % 2 == 0)
			return true;
		return false;
	}

	public boolean isWhiteTurn() {
		return !evenNumberOfPoints(shoushu);
	}

	public int pointsInVerticalLine() {
		int count = 0;
		for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
			if (boardPoints[i][Constant.COORDINATEOFTIANYUAN].getColor() != ColorUtil.BLANK_POINT) {
				count++;
			}
		}
		return count;
	}

	public int pointsInForwardSlashLine() {
		int count = 0;
		for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
			if (boardPoints[Constant.SIZEOFBOARD + 1 - i][i].getColor() != ColorUtil.BLANK_POINT) {
				count++;
			}
		}
		return count;
	}

	public int pointsInBackwardSlashLine() {
		int count = 0;
		for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
			if (boardPoints[i][i].getColor() != ColorUtil.BLANK_POINT) {
				count++;
			}
		}
		return count;
	}

	public int pointsInHorizontalLine() {
		int count = 0;
		for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
			if (boardPoints[Constant.COORDINATEOFTIANYUAN][i].getColor() != ColorUtil.BLANK_POINT) {
				count++;
			}
		}
		return count;
	}

	/**
	 * whether the state is symmetry
	 * 
	 * @return
	 */
	public boolean verticalSymmetry() {
		if (possibleVerticalSymmetry()) {
			for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
				for (int j = 1; j < Constant.COORDINATEOFTIANYUAN; j++) {

					if (boardPoints[i][j].getColor() != boardPoints[i][Constant.SIZEOFBOARD
							+ 1 - j].getColor()) {
						if (log.isDebugEnabled()) {
							System.out.println("i="
									+ i
									+ ",j="
									+ j
									+ ",color="
									+ boardPoints[i][j].getColor()
									+ "color="
									+ boardPoints[i][Constant.SIZEOFBOARD + 1
											- j].getColor());
						}
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean horizontalSymmetry() {
		if (possibleHorizontalSymmetry()) {
			for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
				for (int j = 1; j < Constant.COORDINATEOFTIANYUAN; j++) {
					if (boardPoints[j][i].getColor() != boardPoints[Constant.SIZEOFBOARD
							+ 1 - j][i].getColor()) {
						if (log.isDebugEnabled()) {
							System.out
									.println("i="
											+ i
											+ ",j="
											+ j
											+ ",color="
											+ boardPoints[j][i].getColor()
											+ "color="
											+ boardPoints[Constant.SIZEOFBOARD
													+ 1 - j][i].getColor());
						}
						return false;

					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean backwardSlashSymmetry() {
		if (possibleBackwardSlashSymmetry()) {
			for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
				for (int j = i + 1; j <= Constant.SIZEOFBOARD; j++) {
					if (boardPoints[j][i].getColor() != boardPoints[i][j]
							.getColor()) {
						if (log.isDebugEnabled()) {
							System.out.println("i=" + i + ",j=" + j + ",color="
									+ boardPoints[j][i].getColor() + "color="
									+ boardPoints[i][j].getColor());

						}
						return false;
					}

				}
			}
			return true;
		}
		return false;
	}

	public boolean forwardSlashSymmetry() {
		if (possibleForwardSlashSymmetry()) {
			for (int i = 1; i <= Constant.SIZEOFBOARD; i++) {
				for (int j = 1; j < Constant.SIZEOFBOARD + 1 - i; j++) {
					if (boardPoints[20 - j][20 - i].getColor() != boardPoints[i][j]
							.getColor()) {
						if (log.isDebugEnabled()) {
							System.out.println("i=" + i + ",j=" + j + ",color="
									+ boardPoints[20 - j][20 - i].getColor()
									+ "color=" + boardPoints[i][j].getColor());
						}
						return false;

					}

				}
			}
			return true;
		}
		return false;
	}

	public int numberOfSymmetry() {
		int temp = 0;
		temp += this.horizontalSymmetry() ? 1 : 0;
		temp += this.verticalSymmetry() ? 1 : 0;
		temp += this.forwardSlashSymmetry() ? 1 : 0;
		temp += this.backwardSlashSymmetry() ? 1 : 0;

		return temp;

	}

	/**
	 * The clone done by serialization/deserialization
	 * @return
	 */
	public GoBoard deepClone() {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();

			ObjectOutputStream out = new ObjectOutputStream(b);

			out.writeObject(this);
			ByteArrayInputStream bIn = new ByteArrayInputStream(b.toByteArray());

			ObjectInputStream oi = new ObjectInputStream(bIn);

			return ((GoBoard) oi.readObject());
		} catch (Exception e) {
			System.out.println("exception:" + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public Point[][] getPoints() {
		return points;
	}

	public void setPoints(Point[][] points) {
		this.points = points;
	}

	public short getShoushu() {
		return shoushu;
	}

	public void setShoushu(short shoushu) {
		this.shoushu = shoushu;
	}

	public void setShoushu(int shoushu) {
		this.shoushu = (short) shoushu;
	}

	public boolean equals(Object object) {
		if (object instanceof GoBoard) {
			GoBoard goBoard = (GoBoard) object;
			for (int ii = 1; ii < Constant.ZBSX; ii++) {
				for (int j = 1; j < Constant.ZBSX; j++) {
					if (boardPoints[ii][j] == null) {
						if (goBoard.boardPoints[ii][j] != null) {
							return false;
						}
					} else if (!boardPoints[ii][j]
							.equals(goBoard.boardPoints[ii][j])) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}

	}

	/**
	 * �ֱ�����һ����.���۸�����ѡ�㲢���ؽ��.
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public LocalResultOfZhengZi getLocalResultOfZhengZiForMIN(byte row,
			byte column) {
		final Log log = LogFactory.getLog(GoBoard.class.getName() + "Zhengzi");
		LocalResultOfZhengZi result = new LocalResultOfZhengZi();
		// ��������Ŀ顣
		Block blockToBeEaten = boardPoints[row][column].getBlock();
		Set<Point> candidate = new HashSet<Point>(4);
		// 1. ȷ���������ӵĵ�.
		// TODO:�߼����ݽṹ���Ի�û��׼����.
		for (Iterator iter = blockToBeEaten.getEnemyBlocks().iterator(); iter
				.hasNext();) {
			// ��¼��Χһ���ĵ㡣
			// ?���ִ��󣬴��һ�㲻����һ�����ɡ�������ǰһ���γɵ�.

			Block tempBlock = (Block) iter.next();
			if (tempBlock.getBreaths() == 1) {
				Iterator iter2 = tempBlock.getAllBreathPoints().iterator();
				if (iter2.hasNext()) {
					Point ppp = (Point) iter.next();
					candidate.add(ppp);
				}
			}
		}

		// 2. ��������
		if (blockToBeEaten.getBreaths() != 1) {
			if (log.isDebugEnabled()) {
				log.debug("����������Ϊ1��");
			}
			throw new RuntimeException("���󣺱����ӷ�����ʱ������Ϊ1��");
		}
		Set temps = blockToBeEaten.getAllBreathPoints();
		if (temps == null) {
			if (log.isDebugEnabled()) {
				log.debug("��������1��");
			}
			throw new RuntimeException("���󣺱����ӷ�����ʱ��������1��");
		}

		Iterator itertemp = temps.iterator();
		if (itertemp.hasNext()) {
			candidate.add((Point) itertemp.next());
		}

		if (log.isDebugEnabled()) {
			log.debug("�����ӷ���ѡ����Ϊ" + candidate.size());
		}

		// 3. �����ӷ��ߺ��Ч�������ۺ�ѡ�㡣����ܹ�ֱ�ӵõ����(һ��֮��)��
		// ��������ӷ����Ӻ�.���ӷ����ӿ���.������ӷ���ʱ����.

		int youxiaodian = 0;
		// int haodian=0;
		for (Iterator iter = candidate.iterator(); iter.hasNext();) {
			GoBoard temp = this.getGoBoardCopy();
			// Ŀǰ�������Ǻ�ѡ����֪���������������в���̬�ı�
			// �Ժ�Ӧ�ý��и�ϸ�µĴ�������Ҫչ���ľ���ȷ����
			Point tempPoint = (Point) iter.next();

			// ��Ϊtemp�����Ѿ����ı䣬�������¸�ֵ��
			// ��չ���ľ��棬��չͬһ���ϼ����档
			if (temp.validate(tempPoint)) { // �жϺϷ��ŵ�
				temp.oneStepForward(tempPoint);
				youxiaodian++;
				if (log.isDebugEnabled()) {
					log.debug("��" + youxiaodian + "����ѡ��Ϊ:(" + tempPoint + ")");
				}

				if (temp.boardPoints[row][column].getBreaths() == 1) {
					if (log.isDebugEnabled()) {
						log.debug("���Ӻ����ӷ�����Ϊ1");
					}
				} else if (temp.boardPoints[row][column].getBreaths() == 2) {
					if (log.isDebugEnabled()) {
						log.debug("���Ӻ����ӷ�����Ϊ2");
					}
					result.addCandidateJuMianAndPoint(temp, tempPoint);

				} else if (temp.boardPoints[row][column].getBreaths() == 3) {
					if (log.isDebugEnabled()) {
						log.debug("���Ӻ����ӷ�����Ϊ3");
					}
					result.setScore(127);
					return result;
				} else {
					if (log.isDebugEnabled()) {
						log.debug("���Ӻ����ӷ�����Ϊ"
								+ temp.boardPoints[row][column].getBreaths());
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("���ӵ�Ƿ�!");
				}
			}
		} // forѭ������
		// if(result.)
		// TODO how to return result.
		if (result.getNumberOfCandidates() == 0) {
			result.setScore(-127);
		}
		return result;
	}

	public GoBoard getGoBoardCopy() {
		GoBoard temp = new GoBoard(this.getBoardColorState(), this.getShoushu());
		temp.generateHighLevelState();
		return temp;
	}

	/**
	 * ��Max�������ӷ���ʱ���������п��ܵĺ�ѡ�㲢���ų�ĳЩȷ��״̬�ĵ㡣
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public LocalResultOfZhengZi getLocalResultOfZhengZiForMAX(byte row,
			byte column) {
		final Log log = LogFactory.getLog(GoBoard.class.getName() + "Zhengzi");
		LocalResultOfZhengZi result = new LocalResultOfZhengZi();

		Block blockToBeEaten = boardPoints[row][column].getBlock();
		Set tep = blockToBeEaten.getAllBreathPoints();
		if (blockToBeEaten.getBreaths() != 2) {
			if (log.isDebugEnabled()) {
				log.debug("����������Ϊ����" + blockToBeEaten.getBreaths());
			}
			zhengzijieguo[0][0] = -127;
			throw new RuntimeException("�������ӷ�����ǰ������Ϊ����");
			// return zhengzijieguo; // ��ʾ����ʧ�ܡ�
		}

		Set<Point> candidate = new HashSet<Point>(4);
		for (Iterator iter = tep.iterator(); iter.hasNext();) {
			if (tep.size() == 0) {
				if (log.isDebugEnabled()) {
					log.debug("�����������");
				}
				zhengzijieguo[0][0] = -127;
				throw new RuntimeException("�������ӷ�����ǰ������Ϊ����");
			}

			Point bpp = (Point) iter.next();
			candidate.add(bpp);

		}

		byte m1, n1;
		// �����������ۺ�ѡ��,�º�����.��Ҫ����.
		for (Iterator iter = candidate.iterator(); iter.hasNext();) {
			GoBoard temp = this.getGoBoardCopy();
			// Ŀǰ�������Ǻ�ѡ����֪���������������в���̬�ı�
			// �Ժ�Ӧ�ý��и�ϸ�µĴ�������Ҫչ���ľ���ȷ����
			Point tempPoint = (Point) iter.next();

			// ��չ���ľ��棬��չͬһ���ϼ����档
			if (temp.validate(tempPoint)) { // �жϺϷ��ŵ�
				temp.oneStepForward(tempPoint);
				result.addCandidateJuMianAndPoint(temp, tempPoint);
			}
		}
		if (result.getCandidatePoints().size() == 0) {
			// ���أ�һ�������ӷ����ӿ��¡�
			if (log.isDebugEnabled()) {
				log.debug("��Ч��Ϊ0");

			}
			result.setScore(-128);

		}

		return result;
	}

	/**
	 * @return Returns the stepHistory.
	 */
	public StepHistory getStepHistory() {
		return stepHistory;
	}
}