/*
 * Created on 2005-5-12
 *eddie.wu.arrayblock.GoBoard can not accomodate exceeding 128 block
 *so change some code to improve it.
 *normal block form 0-255
 *sub 128 before store into zb array 
 *add 128 after get from zb array
 *
 *initialized value should be -128
 *
 *but the huiqi() etc is out of synchronized,
 *so we should alos keep and use the GoBoard class.
 *
 *
 * 
 */
package eddie.wu.arrayblock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.api.GoBoardInterface;
import eddie.wu.domain.BoardBreathState;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;

/**
 * 1. using Log4J to Debug <br/>
 * 2. compare between different implementation --good and close begin to compare
 * internally. <br/>
 * 3. exercise refactoring<br/>
 * 区别在于能支持256块！GoBoard本身只能支持128块。
 * 
 * @author eddie resolve the array out of bound problem of blocks.
 * 
 */
public class GoBoard256 implements GoBoardInterface {
	private static final Logger log = Logger.getLogger(GoBoard256.class);

	private final int boardSize;
	public final byte[][] szld = { { 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 1 } };

	private static final int MAX_BYTE = 128;

	// 遍历四周邻子点,顺序可调.右-下-左-上
	public final byte ZBSX = 20; // 棋盘坐标上限;

	public final byte ZBXX = 0; // 棋盘坐标下限;

	public final byte BLANK = 0;

	public final byte BLACK = 1; // 1表示黑子;

	public final byte WHITE = 2; // 2表示白子;

	public final byte ZTXB = 0; // 下标0存储状态值;

	public final byte SQBZXB = 1; // 下标1存储算气标志;

	public final byte QSXB = 2; // 下标2存储气数;

	public final byte KSYXB = 3; // 下标3存储块索引

	/**
	 * 0->255; -128 when store into zb<br/>
	 * add 128 after get from zb.<br/>
	 * no change when access<br/>
	 */
	public short ki = 0; // kuai shu块数?当前已经用到的块号;

	/**
	 * 127->0 <br/>
	 * add 128 before store into zb array <br/>
	 * sub 128 after get from zb array. <br/>
	 * add 128 when access
	 */
	public byte qiki = 127; // qikuai的索引从高到底。

	public short shoushu = 0; // 当前手数,处理之前递增.从1开始;

	public byte ktb = 0, ktw = 0; // 黑白被吃子计数

	/**
	 * 前两维是棋盘的坐标,数组下标从1到19;<br/>
	 * 0:state;各点状态:黑1白2;<br/>
	 * 1:计算气的标志;<br/>
	 * 2:breath;气数;<br/>
	 * 3:block index块索引;<br/>
	 * 4-7存储气点.<br/>
	 * 
	 * 
	 */
	public byte[][][] zb;

	/**
	 * mei yi kuai de ge zi zuo biao <br/>
	 * 存储每块的详细信息;50-69为气点.1-49为子点;[x][0][0]为气数;<br/>
	 * [x][0][1]为子数;多于49子,不存储,多于20气,不具体存储;<br/>
	 * 
	 * design change:<br/〉
	 * 1--〉为棋子点.100-->
	 * 为气点.只要相加不超过99都能处理.
	 */
	public byte[][][] kuai = new byte[256][100][2]; // 17k;

	/**
	 * 0=新块索引;<br/>
	 * 1~8four single point eaten,<br/>
	 * <br/>
	 * 9~12 kuai suo ying of fou block eaten.<br/>
	 * 13~24is same ,25,26a,b 记录棋局的过程信息,用于悔棋;<br/>
	 * 1-8为被吃单子的坐标;9-12为被吃块的索引;<br/>
	 * <br/>
	 * 13-20为成块子的坐标,21-24为成新块的旧块索引.<br/>
	 * 27-32为打吃点,33-35为打吃的块 索引; <br/>
	 * 36.37为禁着点.25.26为该步落点坐标.
	 */

	public byte[][] hui = new byte[512][38]; // 19k;

	// DataOutputStream out;//qi pu shu chu wen jain棋谱输出文件
	// DataInputStream in;//直接从文件生成局面.
	public void qiquan() { // 为了不与常规处理的0.0混淆，用此函数处理弃权，
		// 坐标保存为0.0
		shoushu++;
		hui[shoushu][25] = 0;
		hui[shoushu][26] = 0;
	}

	public void shuchujumian(DataOutputStream jmout) throws IOException {
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				if (zb[i][j][ZTXB] != 0) {
					jmout.writeByte((byte) i);
					jmout.writeByte((byte) j);
					jmout.writeByte((byte) zb[i][j][ZTXB]);
					log.debug("i=" + i);
					log.debug("j=" + j);
				}
			}
		}
		jmout.flush();
		log.debug("输出局面");

	}

	public void shuchuqipu(DataOutputStream jmout) throws IOException {
		short i, j;
		for (i = 1; i <= shoushu; i++) {

			jmout.writeByte(hui[i][25]);
			jmout.writeByte(hui[i][26]);

		}
		jmout.flush();
		log.debug("输出棋谱");

	}

	public void zairujumian(DataInputStream jmin) throws IOException {

		byte a, b, color;
		while (jmin.available() != 0) {
			a = jmin.readByte();
			b = jmin.readByte();
			color = jmin.readByte();
			zb[a][b][ZTXB] = color;
			if (a < 1 | a > 19 | b < 1 | b > 19 | color < 1 | color > 2) {
				log.debug("载入的数据有误！" + a);
				log.debug("i=" + a);
				log.debug("j=" + b);
				log.debug("color=" + color);
			}

		}

		log.debug("载入局面");

	}

	/**
	 * transfer into bidimension array
	 * 
	 * @return
	 */
	public byte[][] getStateArray() {
		byte[][] a = new byte[21][21];
		byte i, j, k;
		for (i = 0; i < 21; i++) {
			for (j = 0; j < 21; j++) {
				a[i][j] = zb[i][j][ZTXB];

			}
		}
		return a;
	}

	byte neighbourPoints = 0; // 邻子数
	byte yise = 0; // 异色
	byte tongse = 0; // yise is diff color.and 2 same.同色
	byte k0 = 0, numberOfEnemyPoint = 0;
	byte numberOfBlankPoint = 0, numberOfSelfPoint = 0;
	// the count for three kinds of point.
	// 三种点的计数
	byte i = 0, j = 0;
	byte dang = 0; // dang is breath of block.块的气数
	byte ktz = 0; // 提子计数,局部
	byte numberOfNeighbourBlock = 0, numberOfNeighbourPoint = 0; // ks is count
																	// for
																	// block,kss
																	// for
																	// single
																	// point
	// 相邻的成块点数和独立单点数
	short kin1 = 0;
	byte m = 0, n = 0; // the block index.
	// a,b周围四点的块索引
	byte gq = 0; // 共气.
	byte yiseks = 0; // 四邻异色块数
	byte tzd = 0, tkd = 0; // the count for single pointeaten andblock
							// eaten.
	// 吃的点数和块数
	byte ktm = hui[shoushu][36]; // 禁着点在上一步产生.
	byte ktn = hui[shoushu][37]; // 禁着点的坐标(打劫产生的禁着点)

	public void cgcl(final byte row, final byte column) {
		cgcl(row, column);
	}

	/**
	 * chang gui chu li qi zi de qi<br/>
	 * 常规处理子的气
	 */

	public void cgcl(final byte row, final byte column, int color) {

		// byte m1 = row; //a,b在方法中不改变
		// byte n1 = column; //m1,n1为a,b的邻点.
		neighbourPoints = 0; // 邻子数
		yise = 0; // 异色
		tongse = 0; // yise is diff color.and 2 same.同色
		k0 = 0;
		numberOfEnemyPoint = 0;
		numberOfBlankPoint = 0;
		numberOfSelfPoint = 0;
		// the count for three kinds of point.
		// 三种点的计数
		i = 0;
		j = 0;
		dang = 0; // dang is breath of block.块的气数
		ktz = 0; // 提子计数,局部
		numberOfNeighbourBlock = 0;
		numberOfNeighbourPoint = 0; // ks is count for block,kss for single
									// point
		// 相邻的成块点数和独立单点数
		// short kin1 = 0;
		m = 0;
		n = 0; // the block index.
		// a,b周围四点的块索引
		gq = 0; // 共气.
		byte[] neighbourPointRow = { 0, 0, 0, 0, 0 }; // position0不用
		byte[] neighbourPointColumn = { 0, 0, 0, 0, 0 }; // 四个邻子的坐标

		short[] neighbourSelfBlock = { 0, 0, 0, 0 }; // array for block
														// index.四同色邻子的块索引
		short[] neighbourEnemyBlock = { 0, 0, 0, 0 }; // 四异色邻子的块索引,同块不重复计算
		yiseks = 0; // 四邻异色块数
		tzd = 0;
		tkd = 0; // the count for single pointeaten andblock
					// eaten.
		// 吃的点数和块数
		byte ktm = hui[shoushu][36]; // 禁着点在上一步产生.
		byte ktn = hui[shoushu][37]; // 禁着点的坐标(打劫产生的禁着点)

		log.debug("come into method cgcl()");
		if (row > ZBXX && row < ZBSX && column > ZBXX && column < ZBSX
				&& zb[row][column][ZTXB] == BLANK) {
			// 下标合法,该点空白
			if (row == ktm && column == ktn) { // 是否禁着点
				log.debug("这是打劫时的禁着点,请先找劫材!");
				return;
			} else {
				log.debug("a=" + row + ",b=" + column);
			} // 仍有可能是无气禁着点!
		} else { // 第一类不合法点.
			log.debug("该点不合法,在棋盘之外或者该点已经有子.");
			log.debug("a=" + row + ",b=" + column);
			return;
		}

		hui[++shoushu][25] = row; // 手数处理前递增,即从1开始计数.与棋谱同.
		hui[shoushu][26] = column; // 记录每步的落点
		if (color != 0) {
			tongse = (byte) color;
			yise = (byte) ColorUtil.enemyColor(color);
		} else {
			yise = (byte) (shoushu % 2 + 1); // yi se=1或2,黑先行为奇数
			tongse = (byte) ((1 + shoushu) % 2 + 1); // tong se=1或2,白后行为偶数
		}
		zb[row][column][ZTXB] = tongse; // 可以动态一致

		dealWithEnemyPoint(row, column, neighbourEnemyBlock);

		k0 = numberOfEnemyPoint; // k0 is count for diff point.
		zb[row][column][QSXB] = 0; // return the breath to zero.防止提子时的增气.
		if (shoushu % 2 == BLACK) {
			ktb += ktz;
		} else {
			ktw += ktz; // 将局部提子计入
		}

		dealWithBlankPoint(row, column, neighbourPointRow, neighbourPointColumn);

		k0 += numberOfBlankPoint; // k0 is the total points of diff and blank.
		// k0现在为异色点和气点的总数
		dang = numberOfBlankPoint; // 落子点四邻的直接气

		dealWithSelfPoint(row, column, neighbourPointRow, neighbourPointColumn,
				neighbourSelfBlock);

		if (dang == 0) { // ?仅仅考虑了单点,如果是块呢?没关系,都是虚的计算.
			// showStatus("//this point is prohibited,try again!");
			zzq(row, column, yise); // 相当于落子和提子一步完成,本函数完成提子
			for (i = 25; i <= 35; i++) {
				hui[shoushu][i] = 0;
			}
			shoushu--;
			zb[row][column][ZTXB] = BLANK;
			return;
		} // 不允许自杀showStatus("qing="+dang+a+b);

		if (numberOfSelfPoint == 0) { // 4.1 no same color pobyte surround没有同色邻点
			log.debug("//k3=0");
			dealWithNoSelfPoint(row, column, neighbourPointRow,
					neighbourPointColumn);
			return;
		}

		if (numberOfNeighbourBlock == 0) { // 4.2 only single pobyte
											// surr.有同色点,但都为独立点
			log.debug("//ks=0");
			dealWithNoSelfBlock(row, column, neighbourPointRow,
					neighbourPointColumn);
		}

		if (numberOfNeighbourBlock > 0) { // 相邻有块
			log.debug("//ks>0" + numberOfNeighbourBlock);
			dealWithMultiSelfBlock(row, column, neighbourPointRow,
					neighbourPointColumn, neighbourSelfBlock);
			/*
			 * if(dang==0){ hui[shoushu][0]=ki; kzq(ki,yise); ktm=-1; ktn=-1;
			 * hui[shoushu][0]=ki; // return; }
			 */
		}
	}

	/**
	 * @param row
	 * @param column
	 * @param neighbourPointRow
	 * @param neighbourPointColumn
	 * @param neighbourSelfBlock
	 */
	private void dealWithMultiSelfBlock(final byte row, final byte column,
			byte[] neighbourPointRow, byte[] neighbourPointColumn,
			short[] neighbourSelfBlock) {
		ki++;
		// TODO:change code of huiqi()
		hui[shoushu][0] = (byte) (ki - MAX_BYTE);
		kuai[ki][0][1] = 1; // 建立临时块
		kuai[ki][1][0] = row; // a,b存在首位
		kuai[ki][1][1] = column;
		zb[row][column][KSYXB] = (byte) (ki - MAX_BYTE);
		for (i = 1; i <= numberOfNeighbourPoint; i++) { // 点合并入块
			hui[shoushu][12 + i * 2 - 1] = neighbourPointRow[k0 + i];
			hui[shoushu][12 + i * 2] = neighbourPointColumn[k0 + i];
			dkhb(neighbourPointRow[k0 + i], neighbourPointColumn[k0 + i], ki); // 相邻点并入临时块
		}
		// dkhb(a,b,k[1]);
		for (j = 1; j <= numberOfNeighbourBlock; j++) {
			hui[shoushu][20 + j] = (byte) (neighbourSelfBlock[j - 1] - MAX_BYTE);
			kkhb(ki, neighbourSelfBlock[j - 1]); // not deal with
													// breath块块合并,气尚未处理.
		}
		// hui[shoushu][0]=ki;
		// zb[a][b][2]=tongse;
		// kuai[k[1]][0][0]=zb[a][b][2];//? need deal with breath.
		dang = jskq(ki);
		kdq(ki, dang);
	}

	/**
	 * @param row
	 * @param column
	 * @param neighbourPointRow
	 * @param neighbourPointColumn
	 */
	private void dealWithNoSelfBlock(final byte row, final byte column,
			byte[] neighbourPointRow, byte[] neighbourPointColumn) {
		gq = 0;
		for (i = 1; i <= numberOfNeighbourPoint; i++) { // 4.1 deal surr
														// point处理相邻独立点
			hui[shoushu][12 + i * 2 - 1] = neighbourPointRow[k0 + i]; // 记录合并成块的独立点
			hui[shoushu][12 + i * 2] = neighbourPointColumn[k0 + i]; // 从13到20
			for (j = 1; j <= (numberOfNeighbourPoint - i); j++) { // 计算点之间的共气
				gq += dd(neighbourPointRow[k0 + i],
						neighbourPointColumn[k0 + i], neighbourPointRow[k0 + i
								+ j], neighbourPointColumn[k0 + i + j]);
			}
		}
		zb[row][column][QSXB] = (byte) (dang - gq);
		zb[row][column][KSYXB] = (byte) (++ki - MAX_BYTE); // count from first
															// block
		// TODO:change code of huiqi()
		hui[shoushu][0] = (byte) (ki - MAX_BYTE); // 记录所成块的索引
		kuai[ki][0][0] = zb[row][column][QSXB];
		kuai[ki][0][1] = (byte) (numberOfSelfPoint + 1);
		kuai[ki][numberOfSelfPoint + 1][0] = row; // 最后一点为a,b
		kuai[ki][numberOfSelfPoint + 1][1] = column;
		for (i = 1; i <= numberOfSelfPoint; i++) { // 将周围点合并到块中
			m = neighbourPointRow[k0 + i];
			n = neighbourPointColumn[k0 + i];
			kuai[ki][i][0] = m;
			kuai[ki][i][1] = n;
			zb[m][n][4] = 0; // 块的气信息不在点中存储
			zb[m][n][5] = 0;
			zb[m][n][6] = 0;
			zb[m][n][7] = 0;
			zb[m][n][QSXB] = zb[row][column][QSXB];
			zb[m][n][KSYXB] = (byte) (ki - MAX_BYTE);
		}
		if (zb[row][column][QSXB] != jskq(ki)) {
			log.debug("error of breath");
		}
	}

	/**
	 * @param row
	 * @param column
	 * @param neighbourPointRow
	 * @param neighbourPointColumn
	 */
	private void dealWithNoSelfPoint(final byte row, final byte column,
			byte[] neighbourPointRow, byte[] neighbourPointColumn) {
		byte ktm;
		byte ktn;
		zb[row][column][QSXB] = dang;
		// defensive
		zb[row][column][KSYXB] = -MAX_BYTE;
		if (dang == 1 && ktz == 1) { // 考虑劫
			ktm = neighbourPointRow[neighbourPoints]; // 因为先处理异色子,再空白点,又无同色点.
			ktn = neighbourPointColumn[neighbourPoints]; // 必为最后一点?如果是角上的劫呢?由4改为linzishu
			hui[shoushu][36] = ktm; // 空白点即为打劫的禁着点.
			hui[shoushu][37] = ktn; // 2yue23日增
		} // not conform to so. en.
		if (dang >= 2) { // 为征子计算提供信息
			zb[row][column][4] = neighbourPointRow[numberOfEnemyPoint + 1];
			zb[row][column][5] = neighbourPointColumn[numberOfEnemyPoint + 1];
			zb[row][column][6] = neighbourPointRow[numberOfEnemyPoint + 2];
			zb[row][column][7] = neighbourPointColumn[numberOfEnemyPoint + 2];
		} else { // dang==1
			zb[row][column][4] = neighbourPointRow[numberOfEnemyPoint + 1];
			zb[row][column][5] = neighbourPointColumn[numberOfEnemyPoint + 1];
		}
	}

	/**
	 * @param row
	 * @param column
	 * @param neighbourPointRow
	 * @param neighbourPointColumn
	 * @param neighbourSelfBlock
	 */
	private void dealWithSelfPoint(final byte row, final byte column,
			byte[] neighbourPointRow, byte[] neighbourPointColumn,
			short[] neighbourSelfBlock) {
		byte m1;
		byte n1;
		short kin1;
		// 根据成块算法统计子数,看是否生成新块
		for (i = 0; i < 4; i++) { // 再处理同色邻子
			m1 = (byte) (row + szld[i][0]);
			n1 = (byte) (column + szld[i][1]);
			if (zb[m1][n1][ZTXB] == tongse) { // 3.1
				numberOfSelfPoint++; // 同色点计数
				kin1 = (short) (zb[m1][n1][KSYXB] + MAX_BYTE);
				if (kin1 == 0) { // 独立点
					numberOfNeighbourPoint++; // same color single point.独立点计数
					dang += zb[m1][n1][QSXB];
					dang--; // current pobyte close one breath of surr point.
					neighbourPointRow[k0 + numberOfNeighbourPoint] = m1; // u[0]
																			// not
																			// used
					neighbourPointColumn[k0 + numberOfNeighbourPoint] = n1; // deal
																			// with
																			// single
																			// point.
				} else { // 231成块点
					for (j = 0; j < numberOfNeighbourBlock; j++) {
						if (kin1 == neighbourSelfBlock[j]) {
							break;
						}
					}
					if (j == numberOfNeighbourBlock) { // 不重复
						dang += kuai[kin1][0][0]; // 此为气数
						dang--;
						neighbourPointRow[neighbourPoints
								- numberOfNeighbourBlock] = m1; // deal with
																// block.
						neighbourPointColumn[neighbourPoints
								- numberOfNeighbourBlock] = n1;
						neighbourSelfBlock[numberOfNeighbourBlock++] = kin1; //
					}
				} // 成块点
			}
		}
	}

	/**
	 * @param row
	 * @param column
	 * @param neighbourPointRow
	 * @param neighbourPointColumn
	 */
	private void dealWithBlankPoint(final byte row, final byte column,
			byte[] neighbourPointRow, byte[] neighbourPointColumn) {
		byte m1;
		byte n1;
		for (i = 0; i < 4; i++) { // 再处理空白邻子
			m1 = (byte) (row + szld[i][0]);
			n1 = (byte) (column + szld[i][1]);
			if (zb[m1][n1][ZTXB] >= 0) {
				neighbourPoints++;
				if (zb[m1][n1][ZTXB] == BLANK) { // 2.1the breath of blank
					// 考虑相邻四点是否为气
					numberOfBlankPoint++; // 气点计数
					neighbourPointRow[k0 + numberOfBlankPoint] = m1;
					neighbourPointColumn[k0 + numberOfBlankPoint] = n1; // 存到每点的信息中
				}
			}
		}
	}

	/**
	 * @param row
	 * @param column
	 * @param neighbourEnemyBlock
	 */
	private void dealWithEnemyPoint(final byte row, final byte column,
			short[] neighbourEnemyBlock) {
		byte m1;
		byte n1;
		short kin1;
		for (i = 0; i < 4; i++) { // 先处理异色邻子
			byte bdcds = 0; // 被打吃点计数.
			byte bdcks = 0; // 被打吃块计数.
			m1 = (byte) (row + szld[i][0]);
			n1 = (byte) (column + szld[i][1]);
			if (zb[m1][n1][ZTXB] == yise) { // 1.1右边相邻点
				numberOfEnemyPoint++; // the count of diffrent color.异色点计数
				kin1 = (short) (zb[m1][n1][KSYXB] + MAX_BYTE); // the block
																// index for the
																// point.66
				if (kin1 == 0) { // not a block.不是块
					zb[m1][n1][QSXB] -= 1;
					if (zb[m1][n1][QSXB] == 0) { // eat the diff point
						numberOfEnemyPoint--; // 被提点要减去
						tzd++;
						hui[shoushu][tzd * 2 - 1] = m1;
						hui[shoushu][tzd * 2] = n1;
						log.debug("提子:a=" + m1 + ",b=" + n1);
						ktz++; // single pobyte eaten was count
						zzq(m1, n1, tongse); // zi zhen qi同色子将增气.
					} else if (zb[m1][n1][QSXB] == 1) {
						hui[shoushu][27 + bdcds++] = m1;
						hui[shoushu][27 + bdcds++] = n1;
					} else if (zb[m1][n1][QSXB] < 0) {
						log.debug("气数错误:a=" + m1 + ",b=" + n1);
						throw new RuntimeException("气数错误:kin=" + kin1);
					}
				} else { // if (kin1==0)
					for (j = 0; j < yiseks; j++) {
						if (kin1 == neighbourEnemyBlock[j]) {
							break;
						}
					}
					if (j == yiseks) { // 不重复
						neighbourEnemyBlock[yiseks++] = kin1;
						byte qi = (byte) (kuai[kin1][0][0] - 1);
						kdq(kin1, qi);
						if (kuai[kin1][0][0] == 0) {
							numberOfEnemyPoint--;
							tkd++; // <=4
							hui[shoushu][8 + tkd] = (byte) (kin1 - MAX_BYTE);
							ktz += kuai[kin1][(byte) 0][1]; // 实际的提子数
							kzq(kin1, tongse); // increase the breath of pobyte
												// surround
							// 异色块被提,同色子增气.
						} else if (kuai[kin1][0][0] == 1) {
							hui[shoushu][32 + bdcks++] = (byte) (kin1 - MAX_BYTE);
						} else if (kuai[kin1][(byte) 0][(byte) 0] < 0) {
							log.debug("气数错误:kin=" + kin1);
							throw new RuntimeException("气数错误:kin=" + kin1);
						}
					} // i==yiseks
				}
			}
		} // 用循环代替
	}

	// one point was eaten
	public void zzq(byte a, byte b, byte tiao) { // function 6.1总之是某子被吃引起的增气.
		short c1 = 0;
		byte i, j, yiseks = 0;
		byte m1, n1;
		short ysk[] = { 0, 0, 0, 0 };
		for (i = 0; i < 4; i++) {
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == tiao) {
				c1 = (short) (zb[m1][n1][KSYXB] + MAX_BYTE);
				if (c1 == 0) {
					zb[m1][n1][QSXB] += 1;
					if (zb[m1][n1][QSXB] == 2) {
						zb[m1][n1][6] = a;
						zb[m1][n1][7] = b;
					}

				} else {
					for (j = 0; j < yiseks; j++) {
						if (c1 == ysk[j]) {
							break;
						}
					}
					if (j == yiseks) { // 不重复
						ysk[yiseks++] = c1;
						kdq(c1, kuai[c1][0][0] += 1);
					}

				}
			}
		}
		zb[a][b][ZTXB] = BLANK;
		zb[a][b][QSXB] = 0;
		zb[a][b][KSYXB] = -MAX_BYTE;
		zb[a][b][4] = 0;
		zb[a][b][5] = 0;
		zb[a][b][6] = 0;
		zb[a][b][7] = 0;
	}

	public void kzq(short r, byte tiao) { // 6.2 yi se kuai bei ti
		// 提吃异色块时,同色块气数增加
		byte n = 0;
		byte p = 0, q = 0;
		n = kuai[r][0][1];
		for (byte i = 1; i <= n; i++) {
			p = kuai[r][i][0];
			q = kuai[r][i][1];
			zzq(p, q, tiao);
			// 保留原块信息,主要是子数信息,便于悔棋时恢复
		}
		// change for -128
		kuai[r][0][0] = 0;

	}

	public void zjq(byte a, byte b, byte tiao) { // function 6.1
		byte c1 = 0, i, m1, j, n1, yiseks = 0;
		byte ysk[] = { 0, 0, 0, 0 };
		for (i = 0; i < 4; i++) {
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == tiao) {
				c1 = zb[m1][n1][KSYXB];
				if (c1 == 0) {
					zb[m1][n1][QSXB] -= 1;
					if (zb[m1][n1][QSXB] < 1) {
						log.debug("悔棋时气数出错:a=" + m1 + ",b=" + n1);
					}
				} else {
					for (j = 0; j < yiseks; j++) {
						if (c1 == ysk[j]) {
							break;
						}
					}
					if (j == yiseks) { // 不重复
						ysk[yiseks++] = c1;
						kdq(c1, kuai[c1][0][0] -= 1);
					}
				}
			}
		}

	}

	public void kjq(byte r, byte tiao) { // 悔棋时,成块恢复使同色子减气
		byte n = 0; // the same color block is eaten
		byte p = 0, q = 0; // 没有自提时,tiao只能是同色.
		n = kuai[r][0][1];
		for (byte i = 1; i <= n; i++) {
			p = kuai[r][i][0];
			q = kuai[r][i][1];
			zjq(p, q, tiao);
		}
		kuai[r][0][0] = 1; // 被提块恢复,气数为1.
	}

	public byte dd(byte a, byte b, byte c, byte d) { // 7.1diang diang gong qi
		byte gq = 0; // consider four points only.
		if (zb[a][d][0] == 0) { // 点共气只有两种可能,相对位置为肩冲和一间跳
			gq++; // 后者不必考虑,由当前着点连接在中间,不会重复计入气数.
		}
		if (zb[c][b][0] == 0) {
			gq++;
		}
		log.debug("方法dd,计算共气=" + gq + "\n");
		return gq;
	}

	// 6.7diang kuai he bing

	public void dkhb(byte p, byte q, short r) { // 8.1
		byte ss = (byte) (kuai[r][0][1] + 1); // 块的子数增1;
		kuai[r][ss][0] = p;
		kuai[r][ss][1] = q;
		kuai[r][0][1] = ss;
		zb[p][q][KSYXB] = (byte) (r - MAX_BYTE);
		zb[p][q][4] = 0;
		zb[p][q][5] = 0;
		zb[p][q][6] = 0;
		zb[p][q][7] = 0;
		log.debug("方法dkhb:点块合并\n");
	}

	/**
	 * 6.8 ss1 shi zu yao kuai!
	 * 
	 * @param r1
	 *            是主要块,
	 * @param r2
	 *            是次要块,他的棋子合并到R1中
	 */
	public void kkhb(short r1, short r2) { // 8.2并入前块,气数未定
		byte ss1 = kuai[r1][0][1];
		byte ss2 = kuai[r2][0][1];
		byte m = 0, n = 0;
		log.debug("shoushu" + shoushu);
		log.debug("主要块的子数SS1 = " + ss1);
		log.debug("次要块的子数ss2 = " + ss2);

		for (byte i = 1; i <= ss2; i++) {
			m = kuai[r2][i][0];
			n = kuai[r2][i][1];
			zb[m][n][KSYXB] = (byte) (r1 - MAX_BYTE);
			// 保留原块信息

			kuai[r1][ss1 + i][0] = m;
			kuai[r1][ss1 + i][1] = n;
		}
		kuai[r1][0][1] = (byte) (ss1 + ss2);
		log.debug("方法kkhb:块块合并");
	}

	public byte jskq(short r2) {
		byte qishu = 0; // the breath of the block
		byte a = 0, b = 0;
		byte m, n;
		byte zishu = kuai[r2][0][1]; // 块的手数
		byte i, j;
		for (i = 1; i <= zishu; i++) {
			m = kuai[r2][i][0];
			n = kuai[r2][i][1];
			for (j = 0; j < 4; j++) {
				a = (byte) (m + szld[j][0]);
				b = (byte) (n + szld[j][1]);
				if (zb[a][b][ZTXB] == BLANK && zb[a][b][SQBZXB] == 0) {
					qishu++;
					zb[a][b][1] = 1;
					kuai[r2][100 - qishu][0] = a; // 存储气点下标50开始
					kuai[r2][100 - qishu][1] = b;
				}
			}
		} // for

		for (i = 1; i <= qishu; i++) { // 恢复标志
			a = kuai[r2][100 - i][0];
			b = kuai[r2][100 - i][1];
			zb[a][b][SQBZXB] = 0;
		}
		int ss1 = kuai[r2][0][0];
		int ss2 = kuai[r2][0][1];
		if (ss1 + ss2 > 99) {
			throw new RuntimeException("子数 = " + ss1 + "气数 = " + ss2 + "; 存储不了");
		}
		return qishu;
	} // 2月22日改,原方法虽妙,仍只能忍痛割爱.

	// 10.1ji suan kuai qi.

	public void kdq(short kin, byte a) { // 11.1 kuai ding qi块定气
		byte m = 0, n = 0, p = 0;
		p = kuai[kin][0][1];
		for (byte i = 1; i <= p; i++) {
			m = kuai[kin][i][0];
			n = kuai[kin][i][1];
			zb[m][n][QSXB] = a;
		}
		kuai[kin][0][0] = a;
	}

	public void clhuiqi() { // 是否所有数据结构都能恢复?
		byte p = 0;
		byte yise = 0;
		byte tongse = 0; // yise is diff color.and 2 same.
		byte tdzs = 0;
		// byte k0 = 0, k1 = 0, k2 = 0, k3 = 0;
		byte i = 0, j = 0; // the count for
							// three kinds of
							// point.
		// byte ks = 0, kss = 0; //ks is count for block,kss for single point
		byte kin, kin1 = 0, m = 0, n = 0; // the block index.

		tongse = (byte) ((shoushu + 1) % 2 + 1); // tong se
		yise = (byte) (shoushu % 2 + 1);
		m = hui[shoushu][25];
		hui[shoushu][25] = 0;
		n = hui[shoushu][26];
		hui[shoushu][26] = 0;
		if (m <= 0 || n <= 0) { // 弃权的恢复
			shoushu--;
			return; //
		}
		zzq(m, n, yise); // 悔棋,对方增气,提子直接恢复,不用在此增气
		log.debug("悔棋:" + shoushu);
		log.debug("a=" + m + ",b=" + n);
		kin = hui[shoushu][0];
		if (kin > 0) { // 是否成新块,自此错误缩进
			for (i = 0; i < 70; i++) {
				kuai[kin][i][0] = 0;
				kuai[kin][i][1] = 0;
			}
			ki = kin; // 全局可用块号?
			for (i = 1; i <= 4; i++) {
				if (hui[shoushu][2 * i + 12 - 1] < 0) { // 成新块的点
					break;
				} else {
					m = hui[shoushu][12 + 2 * i - 1]; // 13-20
					n = hui[shoushu][12 + 2 * i];
					hui[shoushu][12 + 2 * i - 1] = 0;
					hui[shoushu][12 + 2 * i] = 0;
					zb[m][n][KSYXB] = 0;
					zb[m][n][0] = tongse; // fang wei bian cheng
					zb[m][n][QSXB] = jszq(m, n); // 计算子的气
					log.debug("//计算成块点的气:" + "a=" + m + ",b" + n);
				}
			} // deal with 3 sub
			for (i = 1; i <= 4; i++) { // 是否旧块成新块
				kin1 = hui[shoushu][20 + i]; // 21-24
				hui[shoushu][20 + i] = 0;
				if (kin1 == 0) {
					break;
				} else {
					p = kuai[kin1][0][1];
					for (j = 1; j <= p; j++) {
						m = kuai[kin1][j][0];
						n = kuai[kin1][j][1];
						zb[m][n][KSYXB] = kin1; // 修改块号
						// zb[m][n][0]=tongse;
						zb[m][n][QSXB] = kuai[kin1][0][0]; // 恢复原块成块时的气
					}
				} // else
			} // for
		} // if 是否新块
		for (i = 1; i <= 4; i++) { // 是否提子
			if (hui[shoushu][2 * i - 1] <= 0) {
				break;
			} else {
				m = hui[shoushu][2 * i - 1];
				n = hui[shoushu][2 * i];
				hui[shoushu][2 * i - 1] = 0;
				hui[shoushu][2 * i] = 0;
				tdzs = i; // ?
				zb[m][n][ZTXB] = yise;
				zb[m][n][QSXB] = 1;
				zb[m][n][KSYXB] = -MAX_BYTE;
				zjq(m, n, tongse);
				log.debug("恢复被提子:");
				log.debug("a=" + m + ",b=" + n);
			}
		} // for

		for (i = 1; i <= 4; i++) { // 是否有被提的块
			if (hui[shoushu][8 + i] <= 0) {
				break;
			} else {
				kin1 = hui[shoushu][8 + i];
				hui[shoushu][8 + i] = 0;
				kdq(kin1, (byte) 1);
				kjq(kin1, tongse);
				p = kuai[kin1][0][1];
				for (j = 1; j <= p; j++) {
					m = kuai[kin1][j][0];
					n = kuai[kin1][j][1];
					zb[m][n][0] = yise;
					zb[m][n][KSYXB] = kin1;
				}
				tdzs += p;
			} // else
		} // for
		if (tongse == BLACK) {
			ktb -= tdzs;
		}
		if (tongse == WHITE) {
			ktw -= tdzs;
		}
		for (i = 0; i < 9; i++) {
			hui[shoushu][27 + i] = 0; // 2yue
		}
		shoushu--;
		log.debug("方法clhuiqi:处理悔棋\n");
	} // clhuiqi

	public void shengchengjumian() {
		// 从棋谱的位图表示生成kuai和zb数组的相应信息
		byte i, j;
		for (i = 1; i < 20; i++) { // i是纵坐标
			for (j = 1; j < 20; j++) { // j是横坐标.平行扫描
				if (zb[j][i][SQBZXB] == 1) {
					continue; // SQBZXB此处相当于处理过的标志.
				}
				zishu = 0;
				if (zb[j][i][ZTXB] == BLACK) { // 左.上必为空点或异色子
					ki++;
					chengkuai(j, i, BLACK); // 判断右.下是否为同色子.
				} else if (zb[j][i][ZTXB] == WHITE) { // 左.上必为空点或异色子
					ki++;
					chengkuai(j, i, WHITE); // 判断右.下是否为同色子
				} else { // 气块
					ki++; // 因为成块函数中 统一用ki。
					chengkuai(j, i, BLANK);
					/*
					 * if(zishu==1){//眼位 kuai[ki][1][0]=0; kuai[ki--][1][1]=0;
					 * zb[j][i][KSYXB]=0;//非块 //todo:眼位的处理 }
					 */
					// else if(zishu>1){
					kuai[ki][0][1] = zishu;
					zishu = 0;
					for (byte p = 0; p < 70; p++) {
						kuai[qiki + MAX_BYTE][p][0] = kuai[ki][p][0];
						kuai[ki][p][0] = 0;
						kuai[qiki + MAX_BYTE][p][1] = kuai[ki][p][1];
						kuai[ki][p][1] = 0;
					}
					qiki--;
					ki--;
					// }
					// else log.debug("error:zishu<1");

					continue;
				}
				if (zishu == 1) {
					// ki--;
					kuai[ki][1][0] = 0;
					kuai[ki--][1][1] = 0;
					zb[j][i][KSYXB] = -MAX_BYTE; // 非块
				} else if (zishu > 1) {
					kuai[ki][0][1] = zishu;
					zishu = 0;
				} else {
					log.debug("error:zishu<1");
				}
			}
		} // 生成块
		for (i = 1; i < 20; i++) { // i是纵坐标
			for (j = 1; j < 20; j++) { // j是横坐标
				zb[j][i][SQBZXB] = 0; // 恢复每个点的算气标志
				if (zb[j][i][ZTXB] > 0 & zb[j][i][KSYXB] == -MAX_BYTE) {
					zb[j][i][QSXB] = jszq(j, i);
				}
			}
		} // 计算点气
			// biaojihuifu();//算气之前恢复标记。
		for (i = 1; i <= ki; i++) {
			// byte qi=jskq(ki);计算块气过程中直接储存气点.
			kuai[ki][0][0] = jskq(ki);
			output(i);
		} // 计算块气

	}

	byte zishu;

	public void chengkuai(byte a, byte b, byte color) {
		// 收集信息的过程中,可以令color=BLANK,调用该函数,但是气块的信息
		// 不能驻留在kuai数组内,必须早点调用并清除.
		byte m1, n1;
		// byte zishu=0;
		if (zishu < 49) { // 防止子数太多。
			kuai[ki][++zishu][0] = a;
			kuai[ki][zishu][1] = b;
		} else {

			log.debug("该块的子数超过49,块号为:" + ki);
		}
		zb[a][b][SQBZXB] = 1;
		zb[a][b][KSYXB] = (byte) (ki - MAX_BYTE);
		// zishu++;
		for (byte k = 0; k < 4; k++) {
			m1 = (byte) (a + szld[k][0]);
			n1 = (byte) (b + szld[k][1]);
			if (zb[m1][n1][SQBZXB] == 0 & zb[m1][n1][ZTXB] == color) {
				chengkuai(m1, n1, color);
			}
		}
	} // 成块的点SQBZXB==1;

	public void output(byte kin) { // 打印棋块的子数，各点坐标
		// 以及气数和气点坐标。
		byte i, p;
		p = kuai[kin][0][1];
		byte qishu = kuai[kin][0][0];
		log.debug("\n块号为：" + kin);
		for (i = 1; i <= p; i++) {
			log.debug("" + kuai[kin][i][0] + "," + kuai[kin][i][1] + "\t");
			if (i == 5) {
				log.debug("");
			}
		}
		log.debug("\n气数为：" + qishu);
		for (i = 1; i <= qishu; i++) {
			log.debug("" + kuai[kin][100 - i][0] + "," + kuai[kin][100 - i][1]
					+ "\t");
			if (i == 5) {
				log.debug("");

			}
		}

	}

	public void biaojihuifu() {
		// 用于成块后的标记恢复。因为每块的不交叉性，只需最后统一恢复一次。
		byte i, j;
		for (i = 0; i < 21; i++) {
			for (j = 0; j < 21; j++) {
				zb[i][j][SQBZXB] = 0;
			}
		}
	}

	public GoBoard256(int boardSize) {
		this.boardSize = boardSize;
		int length = boardSize + 2;
		zb = new byte[length][length][8]; // 1.6k;
		byte i, j;
		final byte PANWAIDIAN = -1; // 棋盘之外的标志;
		for (i = 0; i < length; i++) { // 2月22日加
			zb[0][i][0] = PANWAIDIAN;
			zb[boardSize + 1][i][0] = PANWAIDIAN;
			zb[i][0][0] = PANWAIDIAN;
			zb[i][boardSize + 1][0] = PANWAIDIAN;
		} // 2月22日加
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				zb[i][j][KSYXB] = -MAX_BYTE;
			}
		}
	}

	public GoBoard256(byte[][] initState) {
		this(initState.length - 2);
		// int length = initState.length;
		// boardSize = length -2;

		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				zb[i][j][0] = initState[i][j];
			}
		}

	}

	/**
	 * 
	 * @param initBlack
	 *            should not be empty
	 * @param initWhite
	 */
	public GoBoard256(Set<Point> initBlack, Set<Point> initWhite) {
		this(initBlack.iterator().next().boardSize);
		for (Point point : initBlack) {
			zb[point.getRow()][point.getColumn()][0] = BLACK;
		}
		for (Point point : initWhite) {
			zb[point.getRow()][point.getColumn()][0] = WHITE;
		}
	}

	public byte jszq(byte m, byte n) {
		byte dang = 0; // 气数变量
		byte i, a, b;
		for (i = 0; i < 4; i++) {
			a = (byte) (m + szld[i][0]);
			b = (byte) (n + szld[i][1]);
			if (zb[a][b][ZTXB] == BLANK) { // 2.1the breath of blank
				dang++;
			}
		}
		return dang;
	}

	public byte dingdianshu(byte m1, byte n1) {
		byte[][] dingdian = { { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };
		// 右上-右下-左下-左上
		byte i, dds = 0;
		byte m, n;
		for (i = 0; i < 4; i++) {
			m = (byte) (m1 + dingdian[i][0]);
			n = (byte) (n1 + dingdian[i][1]);
			if (zb[m][n][ZTXB] == BLACK || zb[m][n][ZTXB] == WHITE) {
				dds++;
			}
		}
		return dds;
	}

	public GoBoard256(GoBoard256 goold) {
		this.boardSize = goold.boardSize;
		int length = this.boardSize + 2;

		byte i, j, k;
		for (i = 0; i < length; i++) {
			for (j = 0; j < length; j++) {
				for (k = 0; k < 4; k++) {
					zb[i][j][k] = goold.zb[i][j][k];
				}
			}
		}
		for (i = 0; i < 128; i++) {
			for (j = 0; j < 70; j++) {
				for (k = 0; k < 2; k++) {
					kuai[i][j][k] = goold.kuai[i][j][k];
				}
			}
		}
		for (i = 0; i < 512; i++) {
			for (j = 0; j < 38; j++) {
				hui[i][j] = goold.hui[i][j];
			}
		}

	}

	public byte[][] houxuandian(byte a, byte b) {
		// 根据活棋主体的块号。（不妥，还是坐标好）确定候选点
		// 先按大眼死活来解决，没有外气，只有眼位候选点

		// 靠近对方强棋（铁棒型），走后没有直接的气，无效；如果还有先手利用
		// 另当别论。

		// 1.做活方强子横纵扩展，遇到对方强子结束。
		// 2.从己方的气出发。不求全面。
		// 3.局部用模式识别来评分。
		// 4.做眼，限于直接气和二次间接气。
		byte[][] fanhui = new byte[40][2];
		byte qishu, i, j, kc;
		byte m, n, m1, n1;
		short kin = (short) (zb[a][b][KSYXB] + MAX_BYTE);

		qishu = kuai[kin][0][0]; // 气数
		kc = 0;
		for (j = 1; j <= qishu; j++) { // 直接气
			kc++;
			m = fanhui[kc][0] = kuai[kin][100 - j][0];
			n = fanhui[kc][1] = kuai[kin][100 - j][1];
			zb[m][n][SQBZXB] = 1; // 做上标记

			for (i = 0; i < 4; i++) { // 间接气
				m1 = (byte) (m + szld[i][0]);
				n1 = (byte) (n + szld[i][1]);
				if (zb[m1][n1][ZTXB] == BLANK && zb[m1][n1][SQBZXB] == 0) { // 1.1右边相邻点
					kc++;
					zb[m1][n1][SQBZXB] = 1;
					fanhui[kc][0] = m1;
					fanhui[kc][1] = n1;
				}
			}
		}
		fanhui[0][0] = kc;
		for (j = 1; j <= kc; j++) {
			zb[fanhui[j][0]][fanhui[j][1]][SQBZXB] = 0; // 恢复标记
		}
		return fanhui; // 随着落子和提子动态增补和删减
	}

	public boolean validate(byte a, byte b) {
		// 从shortLian拷贝而来，可能不兼容。？
		// 落子位置的有效性。
		byte m, n, qi = 0;
		// 在shoushu增加之前调用，yise和tongse的计算有所不同。
		byte tongse = (byte) (shoushu % 2 + 1); // yi se=1或2,黑先行为奇数
		byte yise = (byte) ((1 + shoushu) % 2 + 1); // tong se=1或2,白后行为偶数
		if (a > ZBXX && a < ZBSX && b > ZBXX && b < ZBSX
				&& zb[a][b][ZTXB] == BLANK) {
			// 下标合法,该点空白
			if (a == hui[shoushu][2] && b == hui[shoushu][3]) { // 是否禁着点
				log.debug("这是打劫时的禁着点,请先找劫材!");
				log.debug("落点为：a=" + a + ",b=" + b);
				return false;
			} else {
				// log.debug("落点为：a=" + a + ",b=" + b);
				for (byte i = 0; i < 4; i++) {
					m = (byte) (a + szld[i][0]);
					n = (byte) (b + szld[i][1]);
					if (zb[m][n][ZTXB] == BLANK) {
						return true;
					} else if (zb[m][n][ZTXB] == yise) {
						if (zb[m][n][QSXB] == 1) {
							return true; // todo
						}
					} else if (zb[m][n][ZTXB] == tongse) {
						if (zb[m][n][QSXB] > 1) {
							return true;
						} else {
							qi += zb[m][n][QSXB]; // 不论成块与否都成立
							qi--;
						}
					}
				}
				if (qi == 0) {
					log.debug("这是自杀的禁着点：");
					log.debug("a=" + a + ",b=" + b);
					return false;
				} else {
					log.debug("这是合法着点：");
					log.debug("a=" + a + ",b=" + b);
					return true;
				}
			}
		} else { // 第一类不合法点.
			log.debug("该点不合法,在棋盘之外或者该点已经有子：");
			log.debug("a=" + a + ",b=" + b);
			return false;
		}
	} // 从shortLian拷贝而来，可能不兼容。？

	@Override
	public boolean oneStepForward(Step step) {

		this.cgcl((byte) (step.getRow()), (byte) step.getColumn(),
				step.getColor());
		return true;
	}

	@Override
	public boolean oneStepBackward() {
		this.clhuiqi();
		return true;
	}

	@Override
	public byte[][] getMatrixState() {
		int length = this.boardSize + 2;
		byte[][] a = new byte[length][length];
		for (int i = 1; i <= boardSize; i++)
			for (int j = 1; j <= boardSize; j++) {
				a[i][j] = zb[i][j][0];
			}
		return a;
	}

	@Override
	public Set<Point> getEatenPoints() {
		Set<Point> points = new HashSet<Point>();
		// 当前一步提吃的单子
		for (int i = 0; i < 4; i++) {
			int m = hui[shoushu][i * 2 + 1];
			int n = hui[shoushu][i * 2 + 2];
			if (m != 0 && n != 0)
				points.add(Point.getPoint(this.boardSize,m, n));
			else
				break;
		}
		// 当前一步提吃的气块
		for (int i = 0; i < 4; i++) {
			int kin = hui[shoushu][9 + i];
			if (kin != 0) {
				for (int k = 1; k <= kuai[kin][0][1]; k++)
					points.add(Point.getPoint(boardSize, kuai[kin][k][0],
							kuai[kin][k][1]));

			} else
				break;
		}
		return points;
	}

	public BoardColorState getBoardState() {
		int color = ColorUtil.getCurrentStepColor(shoushu);
		BoardColorState boardColorState = new BoardColorState(this.getStateArray(),color);
//		boardColorState.setShoushu(shoushu);
		return boardColorState;
	}

	public BoardBreathState getBoardBreathState() {
		return new BoardBreathState(this.getBreathArray());
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
				a[i][j] = zb[i][j][QSXB];

			}
		}
		return a;
	}
	/*
	 * public byte qiangruo(){ //根据强弱判断出做活的主体。返回块号。 byte i, j, k; byte zishu;
	 * for (i = 1; i < ki; i++) { zishu = kuai[i][0][1]; } }
	 */
	/*
	 * public byte zhankai(byte a,byte b){ //拆二余地，空点连着四个。 }
	 */
}
