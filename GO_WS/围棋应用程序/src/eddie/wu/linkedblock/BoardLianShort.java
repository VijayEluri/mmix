package eddie.wu.linkedblock;

import java.io.*;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardBreathState;

//气块和棋块都存在链表中。
//竟然没有被彻底测试过（利用五千局棋谱库），被测试的是GoBaord！

//bug之一：小飞挂，夹击，白方强行出头，黑方棋子取势，白发
//连接时未能成新块。2004年2月15日。已经修正
/**
 * <p>
 * Title: 每个棋子交叉点对应的块号用short结构。增大范围 并且使悔棋没有错误，<br/>
 * 便于搜索时的返回（现在决定改为拷贝局面数组，<br/>
 * 就是拷贝局面，不在依靠悔棋功能，悔棋功能只在界面程序中使用）。 <br/>
 * 且单点也作为块。增加点就变为新块，悔棋返回时原块仍然可用。<br/>
 * 每个块的子树及其坐标是唯一的。其它信息可能随棋局 进程改变。
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class BoardLianShort implements Cloneable {
	private static final Logger log = Logger.getLogger(BoardLianShort.class);
	public static final boolean DEBUG = true;

	public static final byte QIPANDAXIAO = 19; // 棋盘大小

	public static final byte ZBSX = QIPANDAXIAO + 1; // 棋盘坐标上限（人造边界）;
	public static final byte ZBXX = 0; // 棋盘坐标下限（人造边界）;

	public static final byte[][] szld = {
			// 遍历四周（横轴和纵轴）邻子点,顺序可调.右-下-左-上
			{ 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 1 } };
	public static final byte[][] szdjd = {
			// 遍历四周对角点,顺序可调.右上-右下-左下-左上
			{ 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };

	public byte[][][] zb = new byte[21][21][4];
	// 每个落子点的情况，所有原始信息都在这个数组中。足以代表一个局面。
	// 前两维是棋盘的坐标,数组下标从1到19;
	// 第三维下标0:各点状态:黑1白2;（足以代表一个局面）
	// 第三维下标1:计算气的标志;成气块和棋块时也用作标志。
	// 第三维下标2:气数;
	// 第三维下标3:空白点所在气块的编号。非空白点编号为零。
	// 后三维的意义在于处理规则，理解气和气块、棋块的概念，只有实现这些
	// 基本概念，才能实现规则，自动进行落子后的处理过程。

	public static final byte ZTXB = 0; // 下标0存储状态值（无子、黑子、白子）;
	public final byte BLANK = 0;
	public final byte BLACK = 1; // 1表示黑子;
	public final byte WHITE = 2; // 2表示白子;

	public static final byte SQBZXB = 1; // 下标1存储算气标志;
	public static final byte QSXB = 2; // 下标2存储气数（单子或者棋块的气数）;
	public static final byte QKSYXB = 3; // 下标3存储气块索引（空白点而言）

	public short[][] zbk = new short[21][21]; // 每个点属于的块号。
	// 气块和子块分开，气块是子块的附属。

	public short shoushu = 0; // 当前已有手数,处理之前递增.从1开始;
	// 单子成块，则ki和shoushu等价（考虑到弃权，仍有微妙差别）。

	public byte tiheizishu = 0, tibaizishu = 0; // 黑白被吃子计数
	// 意义不大，不过顺带算出来，形势判断或许有用。

	// 限制之一：棋谱手数不能超过512手。
	public short[][] huik = new short[512][12]; // 成块的块号1－4
	// 提吃的块号5－8，打吃的块号：9－11；
	// 相同性质的子直接连接在一起就是块，所以有黑块和白块，以及气块。
	public byte[][] hui = new byte[512][5];
	// 记录棋局的过程信息,用于悔棋;3-4禁着点，1－2该步落点坐标.
	public ZiKuai1[] zikuai = new ZiKuai1[512];
	// 子块的处理和气块的处理有所不同，子块没法实现现有的块都是存在的
	// 而气块可以做到这一点
	public QiKuai1[] qikuai = new QiKuai1[128];
	public ErJiKuai[] erjikuai = new ErJiKuai[128];
	public short zikuaishu = 0; // 当前已经用到的块号,用前递增;
	public byte qikuaishu = 0;
	public byte erjikuaishu = 0;
	short qikuaizishu = 0; // 起全局联系作用的局部变量。

	public BoardLianShort(BoardLianShort oldgo) {
		super();

		byte i, j, k;
		short t;

		final byte PANWAIDIAN = -1; // 棋盘之外的标志;
		for (i = ZBXX; i <= ZBSX; i++) { // 2月22日加
			zb[ZBXX][i][ZTXB] = PANWAIDIAN;
			zb[ZBSX][i][ZTXB] = PANWAIDIAN;
			zb[i][ZBXX][ZTXB] = PANWAIDIAN;
			zb[i][ZBSX][ZTXB] = PANWAIDIAN;
		} // 2月22日加

		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				for (k = 0; k < 4; k++) {
					zb[i][j][k] = oldgo.zb[i][j][k];
				}
				zbk[i][j] = oldgo.zbk[i][j];
			}

		}
		zikuaishu = oldgo.zikuaishu;
		qikuaishu = oldgo.qikuaishu;
		tibaizishu = oldgo.tibaizishu;
		tiheizishu = oldgo.tiheizishu;
		shoushu = oldgo.shoushu;

		for (i = 1; i <= qikuaishu; i++) {
			// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("qikuai="+i);

			qikuai[i] = new QiKuai1(oldgo.qikuai[i]);
		}
		for (t = 1; t <= zikuaishu; t++) {
			// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("zikuai="+t);
			if (oldgo.zikuai[t] == null) {
				// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("null is zikuai="+t);
			}
			if (oldgo.zikuai[t].active) {
				// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("有效块为："+t);

			} // 只拷贝有用的棋块
			zikuai[t] = new ZiKuai1(oldgo.zikuai[t]);
		}

		for (t = 0; t < 512; t++) {
			for (j = 0; j < 5; j++) {

				hui[t][j] = oldgo.hui[t][j];
			}
		}
		for (t = 0; t < 512; t++) {
			for (j = 0; j < 12; j++) {
				huik[t][j] = oldgo.huik[t][j];
			}
		}

	}

	public BoardLianShort() {

		byte i, j;
		final byte PANWAIDIAN = -1; // 棋盘之外的标志;
		for (i = ZBXX; i <= ZBSX; i++) { // 2月22日加
			zb[ZBXX][i][ZTXB] = PANWAIDIAN;
			zb[ZBSX][i][ZTXB] = PANWAIDIAN;
			zb[i][ZBXX][ZTXB] = PANWAIDIAN;
			zb[i][ZBSX][ZTXB] = PANWAIDIAN;
		} // 2月22日加

		qikuaishu = 1;
		qikuai[qikuaishu] = new QiKuai1();
		qikuai[qikuaishu].color = 0; // ?也许－1更好。
		// qikuai[qikuaishu].zishu = (byte) 361;
		// qikuai[qikuaishu].init();
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				zb[i][j][QKSYXB] = 1;
				qikuai[qikuaishu].addzidian(i, j);
			}
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("qikuai[qikuaishu].zishu="
					+ qikuai[qikuaishu].zishu);

		}
	}

	public void qikuaixinxi(byte kin) {
		byte i = kin;
		DianNode1 temp;
		short zishu = 0;
		byte j = 0;
		byte m, n;
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("输出气块的子数");

		}
		temp = qikuai[i].zichuang;
		zishu = qikuai[i].zishu;
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("块号:" + i + "；气块子数:" + zishu);
		}
		if (zishu > 10) {
			return;
		}
		for (j = 1; j <= qikuai[i].zishu; j++) {
			if (temp == null) {
				return;
			}
			m = temp.a;
			n = temp.b;
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("(" + m + "," + n + ") ");
			}
			temp = temp.next;

		}

	}

	public byte pingfen(byte a, byte b) {
		byte fenshu = 0; // 0表示不确定。
		short zikin = zbk[a][b];

		return fenshu;
	}

	public byte yanxingpanduan(byte kin) {
		// 眼型判断。
		byte yanxing = 0;
		byte i, j;
		byte m, n;
		byte a, b; // 气眼的坐标
		byte color = qikuai[kin].color;
		byte othercolor;
		byte zwzkshu = qikuai[kin].zwzkshu;
		short zishu = qikuai[kin].zishu;
		HaoNode1 temp;
		boolean danduqikuai = true;
		byte yandeweizhi = 0;
		// 1:角眼；2：边眼；3：中腹眼。
		temp = qikuai[kin].zwzkhao;
		for (i = 1; i <= zwzkshu; i++) {

			if (zikuai[temp.hao].zwqkshu > 1) {
				danduqikuai = false;
				break;
			}
			temp = temp.next;
		}
		if (danduqikuai) {
			// 本身是单独气块，可能与其它有眼的块连接，但是不是
			// 通过气块连接。

		} else {
			// 有多个气块相关，用别的算法解决。
			return 0;
		}

		if (zishu == 1) { // 气块的子数。
			yandeweizhi = danyanweizhi(kin); // 单眼的位置。
			color = qikuai[kin].color;
			if (color == CS.BLACK) {
				othercolor = CS.WHITE;
			} else if (color == CS.WHITE) {
				othercolor = CS.BLACK;
			} else {
				othercolor = CS.BLANK;
			}
			a = qikuai[kin].zichuang.a;
			b = qikuai[kin].zichuang.b;
			for (i = 0; i < 4; i++) {
				m = (byte) (a + szdjd[i][0]);
				n = (byte) (b + szdjd[i][1]);
				if (zb[m][n][ZTXB] == othercolor) {

				} else if (zb[m][n][ZTXB] == CS.BLANK) {
					if (shifoukongzhidian(m, n)) {

					}

				}
			}

			switch (zwzkshu) { // 最简单的情况。
			case 1: {
				return CS.ZHENYAN;
			}
			case 2: {
				// 周围块都只有一个气块。
				// 考虑两个子块的连接性。
				if (yandeweizhi == 3) {
					// 有两个非本方棋块的对角点。
				}
			}
			case 3: {
				// 眼的真假并非绝对重要，除非周围的敌子很强。
				// 也就是说只有在涉及封锁后的死活才有意义。
			}
			case 4: {

			}

			default:
				return yanxing;
			}
		} else {
			return yanxing;
		}
	}

	public byte danyanweizhi(byte kin) {
		// 判断单块气眼的位置。
		byte yandeweizhi = 0;
		// 先区别中腹眼位，边角眼位。
		if (qikuai[kin].zwzkshu != 1) {
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("该气块子数不为一：" + kin);
			}
			return 0;
		}
		byte m = qikuai[kin].zichuang.a;
		byte n = qikuai[kin].zichuang.b;
		if (m == 1 || m == 19) {
			if (n == 1 || n == 19) { // 角眼
				yandeweizhi = CS.JIAOYAN;
			} else { // 左右边眼。
				yandeweizhi = CS.BIANYAN;
			}

		} else {
			if (n == 1 || n == 19) { // 上下边眼
				yandeweizhi = CS.BIANYAN;
			} else { // 中腹眼
				yandeweizhi = CS.ZHONGFUYAN;
			}

		}
		return yandeweizhi;

	}

	public byte jskq(short rs) {
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("进入方法：计算块气（jskq）");
			// 算出块的气，并完成块的所有信息：气数和气串。
		}
		byte qishu = 0; // 块的子数和子串已经确定。
		byte a = 0, b = 0;
		byte m, n;
		byte i, j;
		short zishu = zikuai[rs].zishu; // 块的子数
		DianNode1 temp = zikuai[rs].zichuang;
		DianNode1 qich;
		// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("kuaihao:" + rs);
		// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("zishu:" + zishu);
		for (i = 1; i <= zishu; i++) {
			m = temp.a;
			n = temp.b;
			zbk[m][n] = rs; // 块中点的正确设置

			for (j = 0; j < 4; j++) {
				a = (byte) (m + szld[j][0]);
				b = (byte) (n + szld[j][1]);
				if (CS.DEBUG_CGCL) {
					System.out.print("a=" + a);
				}
				if (CS.DEBUG_CGCL) {
					System.out.print("b=" + b);
				}
				if (zb[a][b][ZTXB] == BLANK) {
					// if(CS.DEBUG_CGCL) System.out.print(" kongdian");
				}
				if (zb[a][b][SQBZXB] == 0) {
					// if(CS.DEBUG_CGCL) System.out.print(" weijisuanguo");
				}
				if (zb[a][b][ZTXB] == BLANK && zb[a][b][SQBZXB] == 0) {
					qishu++;
					// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug(" 记为一气。");
					zb[a][b][SQBZXB] = 1;
					// 将气点加入气串。
					qich = new DianNode1(a, b);
					qich.next = zikuai[rs].qichuang;
					zikuai[rs].qichuang = qich;
				}
			}
			temp = temp.next;
		} // for
		zikuai[rs].qishu = qishu;

		qich = zikuai[rs].qichuang;
		for (i = 1; i <= qishu; i++) { // 恢复标志
			a = qich.a;
			b = qich.b;
			zb[a][b][SQBZXB] = 0;
			zb[a][b][QSXB] = qishu;
			// zbk[a][b] = rs;
			qich = qich.next;
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("块的气数为：" + qishu);
		}
		kdq(rs, qishu);
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法jskq：返回");
		}
		return qishu;
	} // 2月22日改,原方法虽妙,仍只能忍痛割爱.

	public void kdq(short kin, byte a) { // 块定气
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法kdq：进入");
		}
		byte m = 0, n = 0;
		short p = 0; // 块减少一气时用。
		short rs = kin;
		p = zikuai[rs].zishu;
		DianNode1 temp = zikuai[rs].zichuang;
		for (byte i = 1; i <= p; i++) {
			m = temp.a;
			n = temp.b;
			zb[m][n][QSXB] = a; // 配合kkhb；
			zbk[m][n] = kin; // 冗余
			temp = temp.next;
		}
		zikuai[rs].qishu = a;
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("块" + rs + "的气数为" + a);
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法kdq：返回");
		}
	}

	public void qkdsy(byte kin) { // 气块定索引。
		// 增加建立周围子块连接的功能
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法qkdsy：进入" + kin);
		}
		byte m = 0, n = 0;
		short p = 0; // 块减少一气时用。
		byte rs = kin;
		p = qikuai[rs].zishu;
		DianNode1 temp = qikuai[rs].zichuang;
		HaoNode1 linshi = qikuai[kin].zwzkhao;
		for (short i = 1; i <= p; i++) {
			m = temp.a;
			n = temp.b;
			zb[m][n][SQBZXB] = 0; // 配合kkhb；
			zb[m][n][QKSYXB] = kin; // 冗余
			temp = temp.next;
		}
		for (byte j = 1; j <= qikuai[kin].zwzkshu; j++) {
			zikuai[linshi.hao].addqikuaihao(kin);
			linshi = linshi.next;
		}
		// zikuai[rs].zishu = a;
		// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("气块" + rs + "的气点为" + a);
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法qkdsy：返回");
		}
	}

	public void kjq(byte r, byte tiao) { // 悔棋时,成块恢复使同色子减气
		short n = 0; // the same color block is eaten
		byte p = 0, q = 0; // 没有自提时,tiao只能是同色.
		short rs = (short) (128 + r);
		n = zikuai[rs].zishu;
		DianNode1 temp = zikuai[rs].zichuang;
		for (byte i = 1; i <= n; i++) {
			p = temp.a;
			q = temp.b;
			zjq(p, q, tiao);
			temp = temp.next;
		}
		zikuai[rs].qishu = 1; // 被提块恢复,气数为1.
		// todo:气串为a,b
	}

	public void kkhb(short rs1, short rs2) { // 8.2并入前块,气数未定
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法kkhb:块块合并");
		}
		DianNode1 temp = zikuai[rs1].zichuang;
		DianNode1 temp1 = zikuai[rs2].zichuang;
		byte m = 0, n = 0;
		for (byte j = 1; j <= zikuai[rs2].zishu; j++) {
			m = temp1.a;
			n = temp1.b;
			zbk[m][n] = rs1;
			temp1 = temp1.next;
		}

		for (byte i = 1; i < zikuai[rs1].zishu; i++) {
			temp = temp.next;
		}
		temp.next = zikuai[rs2].zichuang;
		zikuai[rs1].zishu += zikuai[rs2].zishu;
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法kkhb:块块合并\n");
		}
	}

	public boolean validate(byte a, byte b) { // 落子位置的有效性。
		byte m, n, qi = 0;
		// 在shoushu增加之前调用，yise和tongse的计算有所不同。
		byte tongse = (byte) (shoushu % 2 + 1);
		// yi se=1或2,黑先行为奇数
		byte yise = (byte) ((1 + shoushu) % 2 + 1);
		// tong se=1或2,白后行为偶数
		if (a > ZBXX && a < ZBSX && b > ZBXX && b < ZBSX
				&& zb[a][b][ZTXB] == BLANK) {
			// 下标合法,该点空白
			if (a == hui[shoushu][3] && b == hui[shoushu][4]) { // 是否禁着点
				if (CS.DEBUG_CGCL) {
					System.out.print("这是打劫时的禁着点,请先找劫材!");
				}
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("落点为：a=" + a + ",b=" + b);
				}
				return false;
			} else {
				// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("落点为：a=" + a + ",b=" +
				// b);
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
					if (CS.DEBUG_CGCL) {
						System.out.print("这是自杀的禁着点：");
					}
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("a=" + a + ",b=" + b);
					}
					return false;
				} else {
					if (CS.DEBUG_CGCL) {
						System.out.print("这是合法着点：");
					}
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("a=" + a + ",b=" + b);
					}
					return true;
				}
			}
		} else { // 第一类不合法点.
			if (CS.DEBUG_CGCL) {
				System.out.print("该点不合法,在棋盘之外或者该点已经有子：");
			}
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("a=" + a + ",b=" + b);
			}
			return false;
		}
	}

	public void output() { // 输出每块的信息;
		// todo:周围气块的信息仍然不全面。需要进一步修正。
		DianNode1 temp = null;
		DianNode1 first = null;
		short zishu = 0;
		byte qishu = 0;
		short i = 0;
		byte j = 0;
		byte m, n;
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法 output");
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("1.输出棋块的子数");
		}
		for (i = 1; i <= zikuaishu; i++) {
			if (zikuai[i].active == false) {
				continue;
			}
			temp = zikuai[i].zichuang;
			zishu = zikuai[i].zishu;
			if (CS.DEBUG_CGCL) {
				System.out.print("块号:" + i + "；子数:" + zishu);
			}
			if (zishu > 10) {
				continue;
			}
			for (j = 1; j <= zishu; j++) {
				m = temp.a;
				n = temp.b;
				if (zbk[m][n] != i) {
					if (CS.DEBUG_CGCL) {
						System.out.print("块索引错误！" + i);
					}
				}
				if (CS.DEBUG_CGCL) {
					System.out.print("(" + m + "," + n + ")");
				}
				temp = temp.next;
			}
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("");
			}
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("2.输出棋块的气数");

		}
		for (i = 1; i <= zikuaishu; i++) {
			if (zikuai[i].active == false) {
				continue;
			}
			temp = zikuai[i].qichuang;
			qishu = zikuai[i].qishu;
			if (CS.DEBUG_CGCL) {
				System.out.print("块号:" + i + "；气数:" + qishu);
			}
			if (qishu > 10) {
				continue;
			}
			for (j = 1; j <= qishu; j++) {
				m = temp.a;
				n = temp.b;
				if (CS.DEBUG_CGCL) {
					System.out.print("(" + m + "," + n + ") ");
				}
				temp = temp.next;
			}
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("");
			}
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("3.输出棋块的周围块号");
		}
		for (i = 1; i <= zikuaishu; i++) {
			if (zikuai[i].active == false) {
				continue;
			}
			HaoNode1 temp1 = zikuai[i].zwyskhao;
			short kuaishu = zikuai[i].zwyskshu;
			if (CS.DEBUG_CGCL) {
				System.out.print("块号:" + i + "；周围块数:" + kuaishu);
			}
			for (j = 1; j <= kuaishu; j++) {

				if (CS.DEBUG_CGCL) {
					System.out.print("(" + temp1.hao + ")");
				}
				temp1 = temp1.next;
			}
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("");
			}
		}

		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("4.输出棋块周围的气块号");
		}
		for (i = 1; i <= zikuaishu; i++) {
			if (zikuai[i].active == false) {
				continue;
			}
			HaoNode1 temp1 = zikuai[i].zwqkhao;
			short kuaishu = zikuai[i].zwqkshu;
			if (CS.DEBUG_CGCL) {
				System.out.print("块号:" + i + "；周围气块数:" + kuaishu);
			}
			for (j = 1; j <= kuaishu; j++) {

				if (CS.DEBUG_CGCL) {
					System.out.print("(" + temp1.hao + ")");
				}
				temp1 = temp1.next;
			}
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("");
			}
		}

		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("5.输出气块的子数");

		}
		for (i = 1; i <= qikuaishu; i++) {
			temp = qikuai[i].zichuang;
			zishu = qikuai[i].zishu;
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("块号:" + i + "；气块子数:" + zishu);
			}
			if (zishu > 10) {
				continue;
			}
			for (j = 1; j <= qikuai[i].zishu; j++) {
				if (temp == null) {
					break;
				}
				m = temp.a;
				n = temp.b;
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("(" + m + "," + n + ") ");
				}
				temp = temp.next;

			}

		}

		if (CS.DEBUG_CGCL) {
			System.out.print("zikuaishu=" + zikuaishu + ";shoushu=" + shoushu);
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug(";tibaizishu=" + tibaizishu + ";tiheizishu="
					+ tiheizishu);
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法 output 结束");
		}
	}

	public void zjq(byte a, byte b, byte tiao) { // function 6.1
		// 提子增气函数。
		byte i, m1, j, n1, yiseks = 0;
		short c1 = 0;
		short ysk[] = { // 和被提子相比为异色。
		0, 0, 0, 0 };
		for (i = 0; i < 4; i++) {
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == tiao) {
				c1 = zbk[m1][n1];
				/*
				 * if (c1 == 0) { zb[m1][n1][QSXB] -= 1; if (zb[m1][n1][QSXB] <
				 * 1) { if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("悔棋时气数出错:a=" + m1 +
				 * ",b=" + n1); } }
				 */
				// else {
				for (j = 0; j < yiseks; j++) {
					if (c1 == ysk[j]) {
						break;
					}
				}
				if (j == yiseks) { // 不重复
					ysk[yiseks++] = c1;
					delete(c1, a, b);
					kdq(c1, zikuai[c1 + 128].qishu -= 1);
				}
				// }
			}
		}
	}

	public void zzq(byte a, byte b, byte tiao)
	// 用于悔棋（相当于落子被提）;及正常提子时己方增气;
	// 总之是某子被吃引起对方的增气.tiao指提子方的颜色。
	{
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法zzq()");
		}
		byte i, j, yiseks = 0;
		short c1 = 0;
		short cs;
		byte m1, n1;
		short ysk[] = { 0, 0, 0, 0 };
		for (i = 0; i < 4; i++) {
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == tiao) {
				c1 = zbk[m1][n1];

				// else {
				for (j = 0; j < yiseks; j++) {
					if (c1 == ysk[j]) {
						break;
					}
				}
				if (j == yiseks) { // 不重复
					ysk[yiseks++] = c1;
					cs = c1;
					kdq(c1, zikuai[cs].qishu += 1);
					DianNode1 temp = new DianNode1();
					temp.a = a;
					temp.b = b;
					temp.next = zikuai[cs].qichuang;
					zikuai[cs].qichuang = temp;
				}
				// }
			}
		}
		zb[a][b][ZTXB] = BLANK;
		zb[a][b][QSXB] = 0;
		zbk[a][b] = 0;
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法zzq()");
		}
	}

	public void kzq(short rs, byte tiao) { // 6.2 yi se kuai bei ti
		// 提吃异色块时,同色块气数增加
		short n = 0;
		byte p = 0, q = 0;

		n = zikuai[rs].zishu;
		DianNode1 temp = zikuai[rs].zichuang;
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法kzq()");
		}
		for (byte i = 1; i <= n; i++) {
			p = temp.a;
			q = temp.b;
			zzq(p, q, tiao);
			temp = temp.next;
			// 保留原块信息,主要是子数信息,便于悔棋时恢复
		}
		zikuai[rs].qishu = 0;
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法kzq()");
		}
	}

	public void shanchuqikuaihao(byte kin) {
		byte kuaishu = qikuai[kin].zwzkshu;
		HaoNode1 linshi = qikuai[kin].zwzkhao;
		for (byte i = 1; i <= kuaishu; i++) {
			zikuai[linshi.hao].deleteqikuaihao(kin);

		}
	}

	public byte[][] houxuandian(byte a, byte b) {
		short zkin = zbk[a][b];
		return houxuandian(zkin);
	}

	public byte[][] houxuandian(short zkin) {
		byte[][] fanhui = new byte[40][2];

		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("要做活的块为：" + zkin);

		}
		short qikin;
		byte zijishu = 0;
		byte kuaishu = zikuai[zkin].zwqkshu;
		HaoNode1 linshihao = zikuai[zkin].zwqkhao;

		DianNode1 linshidian;
		for (byte i = 1; i <= kuaishu; i++) {
			qikin = linshihao.hao;

			if (qikuai[qikin].color != 5) {
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("所拥有的气块为：" + qikin);
				}
				linshidian = qikuai[qikin].zichuang;
				for (byte j = 1; j <= qikuai[qikin].zishu; j++) {

					fanhui[++zijishu][0] = linshidian.a;
					fanhui[zijishu][1] = linshidian.b;
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("气点为：" + linshidian.a + "/"
								+ linshidian.b);
					}
					if (zijishu >= 39) {
						fanhui[0][0] = 39;
						return fanhui;
					}
					linshidian = linshidian.next;
				}
			}
			linshihao = linshihao.next;
		}
		fanhui[0][0] = zijishu;
		return fanhui;
	}

	public void cgcl(byte c) {
		byte a = (byte) ((c - 1) % 19 + 1);
		byte b = (byte) ((c - 1) / 19 + 1);
		cgcl(a, b);
	} // 提供一维坐标的接口

	public byte jisuansihuo(byte a, byte b, boolean xianhou) {
		// xianh为true;则为MAXMIN过程、否则为MINMAX过程。
		// 该方法真正用到了搜索算法。
		byte j, k;
		byte sihuo = 0;
		byte MAX = 1; // 代表征子方，征子成立用127标志
		byte MIN = 2; // 代表被征子方，征子不成立用－127表示
		byte turncolor1 = 0; // 计算需要何方轮走。
		byte turncolor2 = 0; // 原始局面原本轮谁走？
		byte m1, n1;
		byte SOUSUOSHENDU = 20;
		int jumianshu = 0; // 当前已有局面号。
		byte cengshu = 0; // 当前已有层数。
		// 自己实现的堆栈。
		// boolean keyiqiquan = false;
		short zkin = zbk[a][b]; // 做活主体的块号。
		byte houxuan[][] = new byte[40][2]; // 候选点
		BoardLianShort[] go = new BoardLianShort[100000];
		int[][] st = new int[SOUSUOSHENDU][5]; // 限制搜索深度为20。
		// 0:该层起始局面号。
		// 1:本层对下一层取max还是min
		// 2:上一层对自己的层取max还是min
		// 3:当前层已经有一个局面确定了。为相应的值。
		// 4:该层还有多少局面。＝0则该层面结束
		st[0][0] = 0;
		st[0][4] = 1;

		if (xianhou) { // 如果做活方（a,b）先手。
			for (byte i = 0; i < SOUSUOSHENDU; i++) {
				// 限制了搜索深度。
				st[i][1] = MAX; // 对下层取MAX；
				// 对下层(走子之后的状态)取MAX；
				st[i][2] = MIN; // 对同一层取MIN
				i++;
				st[i][1] = MIN; // 对下层取MAX；
				st[i][2] = MAX;
			}
		} else { // 如果后手。

			for (byte i = 0; i < SOUSUOSHENDU; i++) {
				st[i][1] = MIN; // 对下层取MAX；
				// 对同一层取MIN
				st[i][2] = MAX;
				i++;
				st[i][1] = MAX; // 对下层取MAX；
				st[i][2] = MIN;

			}
		}

		if (zikuai[zkin].color == CS.BLACK) {
			if (xianhou) {
				turncolor1 = CS.BLACK; // 应该轮谁走。
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("要做活的棋块为黑色，轮黑方走能否做活？");
				}
			} else {
				turncolor1 = CS.WHITE;
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("要做活的棋块为黑色，轮白方走能否杀棋？");

				}
			}
		} else if (zikuai[zkin].color == CS.WHITE) {
			if (xianhou) {
				turncolor1 = CS.WHITE;
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("要做活的棋块为白色，轮白方走能否做活？");

				}
			} else {
				turncolor1 = CS.BLACK;
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("要做活的棋块为白色，轮黑方走能否杀棋？");

				}
			}
		}

		if ((shoushu % 2) == 0) { // 实际上现在轮谁走。
			turncolor2 = CS.BLACK;
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("局面上应该黑走。");

			}
		} else {
			turncolor2 = CS.WHITE;
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("局面上应该白走。");

			}
		}

		if (turncolor1 == turncolor2) {
			go[0] = new BoardLianShort(this);
			// this本身不改变。
		} else {
			go[0] = new BoardLianShort(this);
			go[0].qiquan();
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("需要弃权一步。");

			}
		}

		houxuan = houxuandian(zkin);

		byte youxiaodian = 0; // 不违反落子规则的点。
		byte haodian = 0; // 从有效点中排除可以直接得出结论的点。
		BoardLianShort temp;
		boolean tichi;
		while (true) {
			// 第一层循环：展开最后一个局面。
			cengshu++; // 新层的层号。
			if (cengshu >= SOUSUOSHENDU) {
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("搜索到20层，仍没有结果，返回不精确结果");
				}
			}
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("新的当前层数为：" + cengshu);
			}
			if (st[cengshu][1] == MAX) {
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("当前层轮谁走？" + "MAX");

				}
			} else if (st[cengshu][1] == MIN) {
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("当前层轮谁走？" + "MIN");

				}
			}

			st[cengshu][0] = jumianshu + 1;
			// 新层的开始点。
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("新层的开始局面索引为：" + (jumianshu + 1));

			}
			youxiaodian = 0;
			haodian = 0;
			tichi = false;

			for (byte i = 1; i <= houxuan[0][0]; i++) {
				// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
				// 以后应该进行更细致的处理，根据要展开的局面确定。

				m1 = houxuan[i][0];
				n1 = houxuan[i][1];

				temp = new BoardLianShort(go[jumianshu]);
				// 扩展最后的局面，扩展同一个上级局面。
				if (temp.validate(m1, n1)) { // 判断合法着点
					temp.cgcl(m1, n1);
					youxiaodian++;
					if (st[cengshu][1] == MIN) {
						if (temp.zb[a][b][QSXB] == 1) {
							if (CS.DEBUG_CGCL) {
								if(log.isDebugEnabled()) log.debug("做活主体气数为1");
								// 气数为1，做活主体将被杀。
								// keyiqiquan=true;
							}
							continue;
						} else { // 增加有两个眼的情况处理。
							haodian++;
							if (CS.DEBUG_CGCL) {
								if(log.isDebugEnabled()) log.debug("展开点第几个：" + haodian);
							}
							if (CS.DEBUG_CGCL) {
								if(log.isDebugEnabled()) log.debug("a=" + m1 + "b=" + n1);

							}
							go[jumianshu + haodian] = temp;
						}
					} else if (st[cengshu][1] == MAX) {
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("a=" + m1 + "b=" + n1);
							// 展开得到的层轮MAX下。由min下得到当前层

						}
						if (temp.zbk[a][b] == 0) {
							// 被提吃的块没有块号，也就是zbk[a][b]==0
							if (CS.DEBUG_CGCL) {
								if(log.isDebugEnabled()) log.debug("已经被提吃");
								// 已经被提吃。
							}
							st[cengshu][0] = 0;
							tichi = true;
							break; // 该层已经有确定结果，且足够可以返回上一层。

						} else {
							haodian++;
							if (CS.DEBUG_CGCL) {
								if(log.isDebugEnabled()) log.debug("展开点第几个：" + haodian);
							}
							if (CS.DEBUG_CGCL) {
								if(log.isDebugEnabled()) log.debug("a=" + m1 + "b=" + n1);

							}
							go[jumianshu + haodian] = temp;
						}
					}

					// jumianshu++;//不能递增。因为要展开同一个原始局面。

				} // if is youxiaodian.
			} // for

			if (tichi == true && st[cengshu][1] == MAX) {
				// 该层已经确定
				while (true) {
					st[cengshu - 1][4] -= 1;
					if (st[cengshu - 1][4] == 0) {
						cengshu -= 2;
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("退到层数：" + cengshu);
						}
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("且该层有一个为-127，该层不需继续展开");

						}
						st[cengshu][3] = -127;
						if (cengshu == 0) {
							sihuo = -127;
							return sihuo;
						} else if (cengshu == 1) {
							sihuo = -127;
							return sihuo;
						}
					} else {
						jumianshu = st[cengshu - 1][0] + st[cengshu - 1][4] - 1;

						cengshu--; // important

						// st[cengshu - 2][3] = -127;
						break;
					}
				}
			}

			else if (youxiaodian == 0) {
				// 返回，一般是攻击方无子可下。
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("有效点为0");

					// 递减之后的层数有一个确定状态。
					/*
					 * if (st[cengshu][1] == MIN) { //因为不能填气而没有有效落子点，总之落子后气数为一，
					 * //可能是两眼活棋，也可能是一气被吃。 BoardLianShort lt = new
					 * BoardLianShort(go[jumianshu]); jumianshu++; go[jumianshu]
					 * = lt; lt.qiquan(); st[cengshu][0] = jumianshu;
					 * st[cengshu][4] = 1; }
					 */
				}
				if (st[cengshu][1] == MAX) { // 活棋
					st[cengshu][0] = 0;
					// cengshu--;
					// jumianshu=st[cengshu-1][0]-1;
					// min层有一个活棋。
					// 上一个min层需重新考虑。
					cengshu -= 2; // 倒数两层已经确定。
					if (cengshu == 0) {
						sihuo = 127;
						return sihuo;
					}

					while (true) {
						st[cengshu][4] -= 1;
						if (st[cengshu][4] == 0) {
							cengshu -= 2;
							if (cengshu == 0) {
								sihuo = 127;
								return sihuo;
							} else if (cengshu == -1) {
								sihuo = 127;
								return sihuo;
							}

						} else {
							jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
							// st[cengshu - 2][3] = 127;
							break;
						}
					}
					continue; //
				}
			} else { // 有效点不为零。
				if (haodian == 0) { // 做活方还可以弃权
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("好点为0");
					}
					temp = new BoardLianShort(go[jumianshu]);
					jumianshu++;
					go[jumianshu] = temp;
					temp.qiquan();
					st[cengshu][0] = jumianshu;
					st[cengshu][4] = 1;

				} else {
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("好点个数为：" + haodian);
					}
					st[cengshu][4] = haodian;
					jumianshu += haodian;

				}

			}
		}
	}

	/**
	 * 用线性表来存储征子的结果。因为征子时变化少，这样存储是成立的。 征子有时有两种选择都成立，随意选择一种即可，简化征子结果的表达
	 */
	byte zhengzijieguo[][] = new byte[127][2];

	public byte[][] jiSuanZhengZi(byte a, byte b) {
		// 计算征子，但是不能用于含有劫争的情况。
		// 为MAXMIN过程。
		// 从征子方出发，征子成立为127，征子不成立为-127。
		// 征子方先走。a,b是被征子方棋块中的一个点。因为块号可能改变。
		// 该方法真正用到了搜索算法。

		byte MAX = 1; // 代表征子方
		byte MIN = 2; // 代表被征子方

		byte m1, n1;

		short zkin = zbk[a][b]; // 做活主体的块号。

		byte houxuan[][] = new byte[5][2]; // 候选点〔0〕〔0〕存储子数。
		BoardLianShort[] go = new BoardLianShort[100000];
		byte[] za = new byte[100000]; // 生成与go对应局面所走点的横坐标。
		byte[] zb = new byte[100000]; // 生成与go对应局面所走点的纵坐标。
		int jumianshu = 0; // 当前已有局面号。
		byte SOUSUOSHENDU = 120;
		byte cengshu = 0; // 当前已有层数。
		int[][] st = new int[SOUSUOSHENDU][5]; // 限制搜索深度为100。
		// 0:该层起始局面号。
		// 1:本层对下一层取max还是min
		// 2:上一层对自己的层取max还是min
		// 3:当前层已经有一个局面确定了。为相应的值。
		// 4:该层还有多少局面。＝0则该层面结束

		for (byte i = 0; i < SOUSUOSHENDU; i++) {
			// 限制了搜索深度。
			st[i][1] = MAX; // 对下层取MAX；该层由max下。
			// 对下层(走子之后的状态)取MAX；
			st[i][2] = MIN; // 对同一层取MIN
			i++;
			st[i][1] = MIN; // 对下层取MAX；
			st[i][2] = MAX;
		}

		byte turncolor1 = 0; // 计算计算需要何方轮走。
		byte turncolor2 = 0; // 原始局面原本轮谁走？
		if (zikuai[zkin].color == CS.BLACK) {
			turncolor1 = CS.WHITE;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("要做活的棋块为黑色，轮白方走能否征子？");
			}
		} else if (zikuai[zkin].color == CS.WHITE) {
			turncolor1 = CS.BLACK;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("要做活的棋块为白色，轮黑方走能否征子？");
			}
		}

		if ((shoushu % 2) == 0) { // 实际上现在轮谁走。
			turncolor2 = CS.BLACK;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("局面上应该黑走。");
			}
		} else {
			turncolor2 = CS.WHITE;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("局面上应该白走。");
			}
		}
		go[0] = (BoardLianShort) this.clone();
		if (turncolor1 != turncolor2) {
			go[0].qiquan();
			zhengzijieguo[126][0] = 1;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("需要弃权一步。");
			}
		}
		// 1.初始化
		byte youxiaodian = 0; // 不违反落子规则的点。用于征子方MAX.
		byte haodian = 0; // 用于被征子方。从有效点中排除可以直接得出结论的点。
		BoardLianShort temp;

		st[0][0] = 0;
		st[0][4] = 1;
		jumianshu = 0;

		// 2.开始计算。
		while (true) {
			// 第一层循环：展开最后一个局面。

			if (cengshu >= (SOUSUOSHENDU - 1)) {
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("搜索到100层，仍没有结果，返回不精确结果");
				}
				return zhengzijieguo;
			} else {
				cengshu++; // 新层的层号。
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("新的当前层数为：" + cengshu);
				}
				st[cengshu][0] = jumianshu + 1;
				// 新层的开始点。
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("新层的开始局面索引为：" + (jumianshu + 1));
				}
			}

			youxiaodian = 0;
			haodian = 0;

			temp = (BoardLianShort) (go[jumianshu].clone());
			// 要展开的局面。
			zkin = temp.zbk[a][b];
			if (st[cengshu][1] == MAX) {
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("当前层轮谁走？" + "MAX");
				}
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("上一层轮谁走？" + "MIN");
				}
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("在上一层由MIN走得到该层");

				}
				DianNode1 lili;
				HaoNode1 yskh; // 异色块号
				byte ysks; // 异色块数
				byte tizidianshu = 0;

				ysks = temp.zikuai[zkin].zwyskshu;
				yskh = temp.zikuai[zkin].zwyskhao;
				for (byte jj = 1; jj <= ysks; jj++) { // 加入被打吃的点；
					// 记录周围一气的点。出现错误，打吃一般不适上一步生成。

					if (yskh == null) {
						if (CS.DEBUG_JISUANZHENGZI) {
							if(log.isDebugEnabled()) log.debug("块号错误。");
						}
						break;
					}
					if (temp.zikuai[yskh.hao].qishu == 1) {

						short beidachikuaihao = yskh.hao;
						if (beidachikuaihao > 0) {
							lili = temp.zikuai[beidachikuaihao].qichuang;
							tizidianshu++;
							houxuan[tizidianshu][0] = lili.a;
							houxuan[tizidianshu][1] = lili.b;
						} else {
							if (CS.DEBUG_JISUANZHENGZI) {
								if(log.isDebugEnabled()) log.debug("块号错误。");

							}
						}
					}
					yskh = yskh.next;
				}

				lili = temp.zikuai[zkin].qichuang;
				if (temp.zikuai[zkin].qishu != 1) {
					if (CS.DEBUG_JISUANZHENGZI) {
						if(log.isDebugEnabled()) log.debug("错误：气数不为1。");
					}
					zhengzijieguo[0][0] = -128;
					return zhengzijieguo; // 表示方法失败。
				}

				if (lili == null) {
					if (CS.DEBUG_JISUANZHENGZI) {
						if(log.isDebugEnabled()) log.debug("气数不足1。");
					}
					zhengzijieguo[0][0] = -128;
					return zhengzijieguo; // 表示方法失败。
				}
				tizidianshu++;
				houxuan[tizidianshu][0] = lili.a; // 可能是重复的。
				houxuan[tizidianshu][1] = lili.b;
				houxuan[0][0] = tizidianshu;
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("被征子方候选点数为" + tizidianshu);
				}

				// 被征子方走。
				boolean queding = false;
				for (byte i = 1; i <= houxuan[0][0]; i++) {
					// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
					// 以后应该进行更细致的处理，根据要展开的局面确定。

					m1 = houxuan[i][0];
					n1 = houxuan[i][1];
					temp = new BoardLianShort(go[jumianshu]);
					// 因为temp可能已经被改变，必须重新赋值。
					// 扩展最后的局面，扩展同一个上级局面。
					if (temp.validate(m1, n1)) { // 判断合法着点
						temp.cgcl(m1, n1);
						youxiaodian++;
						if (CS.DEBUG_JISUANZHENGZI) {
							if(log.isDebugEnabled()) log.debug("第" + youxiaodian + "个候选点为:("
									+ m1 + "," + n1 + ")");
						}

						if (temp.zb[a][b][QSXB] == 1) {
							if (CS.DEBUG_JISUANZHENGZI) {
								if(log.isDebugEnabled()) log.debug("落子后被征子方气数为1");
							}
						} else if (temp.zb[a][b][QSXB] == 2) {
							haodian++;
							go[jumianshu + haodian] = temp;
							za[jumianshu + haodian] = m1;
							zb[jumianshu + haodian] = n1;
							if (CS.DEBUG_JISUANZHENGZI) {
								if(log.isDebugEnabled()) log.debug("min走" + "(" + m1 + "," + n1
										+ ")");
							}

						} else if (temp.zb[a][b][QSXB] == 3) {
							if (CS.DEBUG_JISUANZHENGZI) {
								if(log.isDebugEnabled()) log.debug("落子后被征子方气数为3");
							}

							cengshu -= 1; // 倒数两层已经确定。减后的层数需要重新展开

							st[cengshu][4] -= 1;
							if (st[cengshu][4] != 0) {
								jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
								// st[cengshu - 2][3] = 127;
								queding = true;
								break; // 跳出for循环。
							} else {
								while (true) {
									cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
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
				} // for循环结束
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("有效点为:" + youxiaodian);
				}

				if (queding == true) {
					continue;
				} else if (haodian == 0) {
					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu == 0) {
							zhengzijieguo[0][0] = 127;

							byte lins = 0;
							for (lins = 2; st[lins][0] != 0; lins++) {
								if (CS.DEBUG_JISUANZHENGZI) {
									if(log.isDebugEnabled()) log.debug("点为:("
											+ za[st[lins][0] - 1] + ","
											+ zb[st[lins][0] - 1] + ")");
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
			else if (st[cengshu][1] == MIN) {
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("当前层轮谁走？" + "MIN");
				}
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("在上一层由MAX走得到该层");
				}
				DianNode1 lili = temp.zikuai[zkin].qichuang;
				if (temp.zikuai[zkin].qishu != 2) {
					if (CS.DEBUG_JISUANZHENGZI) {
						if(log.isDebugEnabled()) log.debug("错误：气数不为二。");
					}
					zhengzijieguo[0][0] = -127;
					return zhengzijieguo; // 表示方法失败。
				}
				for (byte i = 1; i <= 2; i++) {
					if (lili == null) {
						if (CS.DEBUG_JISUANZHENGZI) {
							if(log.isDebugEnabled()) log.debug("气数不足二。");
						}
						zhengzijieguo[0][0] = -127;
						return zhengzijieguo; // 表示方法失败。
					}
					houxuan[i][0] = lili.a;
					houxuan[i][1] = lili.b;
					lili = lili.next;
				}
				houxuan[0][0] = 2;
				for (byte i = 1; i <= houxuan[0][0]; i++) {
					// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
					// 以后应该进行更细致的处理，根据要展开的局面确定。

					m1 = houxuan[i][0];
					n1 = houxuan[i][1];
					temp = new BoardLianShort(go[jumianshu]);

					// 扩展最后的局面，扩展同一个上级局面。
					if (temp.validate(m1, n1)) { // 判断合法着点
						temp.cgcl(m1, n1);
						youxiaodian++;
						go[jumianshu + youxiaodian] = temp;
						za[jumianshu + youxiaodian] = m1;
						zb[jumianshu + youxiaodian] = n1;
						if (CS.DEBUG_JISUANZHENGZI) {
							if(log.isDebugEnabled()) log.debug("max走" + "(" + m1 + "," + n1
									+ ")");
						}

					}
				}
				if (youxiaodian == 0) {
					// 返回，一般是征子方无子可下。
					if (CS.DEBUG_JISUANZHENGZI) {
						if(log.isDebugEnabled()) log.debug("有效点为0");

					}
					st[cengshu][0] = 0;

					if (cengshu == 1) { // 征子方直接无子可下，
						zhengzijieguo[0][0] = -127;
						return zhengzijieguo;
					}

					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu == -1) {
							zhengzijieguo[0][0] = -127;

							for (byte lins = 2; st[lins][0] != 0; lins++) {
								if (CS.DEBUG_JISUANZHENGZI) {
									if(log.isDebugEnabled()) log.debug("点为:("
											+ za[st[lins][0] - 1] + ","
											+ zb[st[lins][0] - 1] + ")");
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

	public byte[][] jisuanzhengzijiezheng(byte a, byte b) {
		// 计算征子，但是不能用于含有劫争的情况。
		// 为MAXMIN过程。
		// 从征子方出发，征子成立为127，征子不成立为-127。
		// 征子方先走。a,b是被征子方棋块中的一个点。因为块号可能改变。
		// 该方法真正用到了搜索算法。

		byte MAX = 1; // 代表征子方
		byte MIN = 2; // 代表被征子方
		byte JIE = 3;
		byte m1, n1;

		short zkin = zbk[a][b]; // 做活主体的块号。

		byte houxuan[][] = new byte[5][2]; // 候选点〔0〕〔0〕存储子数。
		BoardLianShort[] go = new BoardLianShort[100000];
		byte[] za = new byte[100000]; // 生成与go对应局面所走点的横坐标。
		byte[] zb = new byte[100000]; // 生成与go对应局面所走点的纵坐标。
		byte[] zc = new byte[100000];
		// 标志着该局面已经进入劫争，下一手不能直接提。

		int jumianshu = 0; // 当前已有局面号。
		byte SOUSUOSHENDU = 120;
		byte cengshu = 0; // 当前已有层数。
		int[][] st = new int[SOUSUOSHENDU][5]; // 限制搜索深度为100。
		// 0:该层起始局面号。
		// 1:本层对下一层取max还是min
		// 2:保留
		// 3:当前层已经取得的相应的值。
		// 4:该层还有多少局面。＝0则该层面结束

		for (byte i = 0; i < SOUSUOSHENDU; i++) {
			// 限制了搜索深度。
			st[i][1] = MAX; // 对下层取MAX；在该层局面下由max下。
			// 对下层(走子之后的状态)取MAX；
			st[i][2] = MIN; // 对同一层取MIN
			st[i][3] = -127;
			i++;
			st[i][1] = MIN; // 对下层取MIN；在该层局面下由mIN下。
			// 对下层(走子之后的状态)取MIN；
			st[i][2] = MAX;
			st[i][3] = 127;
		}

		byte turncolor1 = 0; // 计算计算需要何方轮走。
		byte turncolor2 = 0; // 原始局面原本轮谁走？
		if (zikuai[zkin].color == CS.BLACK) {
			turncolor1 = CS.WHITE;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("要做活的棋块为黑色，轮白方走能否征子？");
			}
		} else if (zikuai[zkin].color == CS.WHITE) {
			turncolor1 = CS.BLACK;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("要做活的棋块为白色，轮黑方走能否征子？");
			}
		}

		if ((shoushu % 2) == 0) { // 实际上现在轮谁走。
			turncolor2 = CS.BLACK;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("局面上应该黑走。");
			}
		} else {
			turncolor2 = CS.WHITE;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("局面上应该白走。");
			}
		}
		go[0] = (BoardLianShort) this.clone();
		if (turncolor1 != turncolor2) {
			go[0].qiquan();
			zhengzijieguo[126][0] = 1;
			if (CS.DEBUG_JISUANZHENGZI) {
				if(log.isDebugEnabled()) log.debug("需要弃权一步。");
			}
		}
		// 1.初始化
		byte youxiaodian = 0; // 不违反落子规则的点。用于征子方MAX.
		byte haodian = 0; // 用于被征子方。从有效点中排除可以直接得出结论的点。
		BoardLianShort temp;

		st[0][0] = 0;
		st[0][4] = 1;
		jumianshu = 0;

		// 2.开始计算。
		while (true) {
			// 第一层循环：展开最后一个局面。

			if (cengshu >= (SOUSUOSHENDU - 1)) {
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("搜索到100层，仍没有结果，返回不精确结果");
				}
				return zhengzijieguo;
			} else {
				cengshu++; // 新层的层号。
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("新的当前层数为：" + cengshu);
				}
				st[cengshu][0] = jumianshu + 1;
				// 新层的开始点。
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("新层的开始局面索引为：" + (jumianshu + 1));
				}
			}

			youxiaodian = 0;
			haodian = 0;

			temp = (BoardLianShort) (go[jumianshu].clone());
			// 要展开的局面。
			zkin = temp.zbk[a][b];
			if (st[cengshu][1] == MAX) {
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("当前层轮谁走？" + "MAX");

					if(log.isDebugEnabled()) log.debug("上一层轮谁走？" + "MIN");

					if(log.isDebugEnabled()) log.debug("在上一层由MIN走得到该层");

				}
				DianNode1 lili;
				HaoNode1 yskh; // 异色块号
				byte ysks; // 异色块数
				byte tizidianshu = 0;

				ysks = temp.zikuai[zkin].zwyskshu;
				yskh = temp.zikuai[zkin].zwyskhao;
				for (byte jj = 1; jj <= ysks; jj++) { // 加入被打吃的点；
					// 记录周围一气的点。出现错误，打吃一般不适上一步生成。

					if (yskh == null) {
						if (CS.DEBUG_JISUANZHENGZI) {
							if(log.isDebugEnabled()) log.debug("块号错误。");
						}
						break;
					}
					if (temp.zikuai[yskh.hao].qishu == 1) {

						short beidachikuaihao = yskh.hao;
						if (beidachikuaihao > 0) {
							lili = temp.zikuai[beidachikuaihao].qichuang;
							tizidianshu++;
							houxuan[tizidianshu][0] = lili.a;
							houxuan[tizidianshu][1] = lili.b;
						} else {
							if (CS.DEBUG_JISUANZHENGZI) {
								if(log.isDebugEnabled()) log.debug("块号错误。");

							}
						}
					}
					yskh = yskh.next;
				}

				lili = temp.zikuai[zkin].qichuang;
				if (temp.zikuai[zkin].qishu != 1) {
					if (CS.DEBUG_JISUANZHENGZI) {
						if(log.isDebugEnabled()) log.debug("错误：气数不为1。");
					}
					zhengzijieguo[0][0] = -128;
					return zhengzijieguo; // 表示方法失败。
				}

				if (lili == null) {
					if (CS.DEBUG_JISUANZHENGZI) {
						if(log.isDebugEnabled()) log.debug("气数不足1。");
					}
					zhengzijieguo[0][0] = -128;
					return zhengzijieguo; // 表示方法失败。
				}
				tizidianshu++;
				houxuan[tizidianshu][0] = lili.a; // 可能是重复的。
				houxuan[tizidianshu][1] = lili.b;
				houxuan[0][0] = tizidianshu;
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("被征子方候选点数为" + tizidianshu);
				}

				// 被征子方走。
				boolean queding = false;
				for (byte i = 1; i <= houxuan[0][0]; i++) {
					// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
					// 以后应该进行更细致的处理，根据要展开的局面确定。

					m1 = houxuan[i][0];
					n1 = houxuan[i][1];
					temp = new BoardLianShort(go[jumianshu]);
					// 因为temp可能已经被改变，必须重新赋值。
					// 扩展最后的局面，扩展同一个上级局面。
					if (temp.validate(m1, n1)) { // 判断合法着点
						temp.cgcl(m1, n1);
						youxiaodian++;
						if (CS.DEBUG_JISUANZHENGZI) {
							if(log.isDebugEnabled()) log.debug("第" + youxiaodian + "个候选点为:("
									+ m1 + "," + n1 + ")");
						}

						if (temp.zb[a][b][QSXB] == 1) {
							if (CS.DEBUG_JISUANZHENGZI) {
								if(log.isDebugEnabled()) log.debug("落子后被征子方气数为1");
							}
						} else if (temp.zb[a][b][QSXB] == 2) {
							haodian++;
							go[jumianshu + haodian] = temp;
							za[jumianshu + haodian] = m1;
							zb[jumianshu + haodian] = n1;
							if (CS.DEBUG_JISUANZHENGZI) {
								if(log.isDebugEnabled()) log.debug("min走" + "(" + m1 + "," + n1
										+ ")");
							}
							// cgcl中应该有打劫的信息。
							/*
							 * if (temp.zb[m1][n1][QSXB] == 1) { if
							 * (temp.zikuai[temp.huik[temp.shoushu][5]].zishu ==
							 * 1) { if(log.isDebugEnabled()) log.debug("dajie"); } }
							 */
							if (temp.hui[temp.shoushu][3] != 0
									&& temp.hui[temp.shoushu][4] != 0) { // 将上层的局面排序。
								int shangcengjumianshu = st[cengshu - 1][4];
								if (st[cengshu - 1][4] > 1) {
									BoardLianShort tempjiaohuan;
									tempjiaohuan = go[jumianshu];
									go[jumianshu] = go[jumianshu
											- shangcengjumianshu + 1];
									go[jumianshu - shangcengjumianshu + 1] = tempjiaohuan;
									queding = true;
									break;
									// 重新展开。使不打劫的着法优先展开。

								} else if (st[cengshu - 1][4] == 1) {
									zc[jumianshu + haodian] = JIE;
								} else {

								}
							}
						} else if (temp.zb[a][b][QSXB] == 3) {
							if (CS.DEBUG_JISUANZHENGZI) {
								if(log.isDebugEnabled()) log.debug("落子后被征子方气数为3");
							}
							// 分数的回溯。
							byte yuancengshu = cengshu;
							// 当前层为MAX，递减后的上一层为min；
							// 当前层有一个局面为－127，则上一个层的展开前局面为－127；
							cengshu -= 1; // 倒数两层已经确定。减后的层数需要重新展开

							st[cengshu][4] -= 1;
							if (st[cengshu][4] != 0) {
								jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
								// st[cengshu - 2][3] = 127;
								queding = true;
								break; // 跳出for循环。
							} else { // 该层已经全部展开。
								if (zc[st[cengshu][0] - 1] == JIE) {
									// 虽然等于零，但是由于打劫，其实有一个提劫的变化。
									cengshu--;

									st[cengshu][4] -= 1;
									if (st[cengshu][4] != 0) {
										jumianshu = st[cengshu][0]
												+ st[cengshu][4] - 1;
										// st[cengshu - 2][3] = 127;
										st[cengshu - 1][3] = JIE;
										queding = true;
										break; // 跳出for循环。
									}

								} else {// 非劫的情况。
									while (true) {
										cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
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
					}
				} // for循环结束
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("有效点为:" + youxiaodian);
				}

				if (queding == true) {
					continue;
				} else if (haodian == 0) {
					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu == 0) {
							zhengzijieguo[0][0] = 127;

							byte lins = 0;
							for (lins = 2; st[lins][0] != 0; lins++) {
								if (CS.DEBUG_JISUANZHENGZI) {
									if(log.isDebugEnabled()) log.debug("点为:("
											+ za[st[lins][0] - 1] + ","
											+ zb[st[lins][0] - 1] + ")");
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
			else if (st[cengshu][1] == MIN) {
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("当前层轮谁走？" + "MIN");
				}
				if (CS.DEBUG_JISUANZHENGZI) {
					if(log.isDebugEnabled()) log.debug("在上一层由MAX走得到该层");
				}
				DianNode1 lili = temp.zikuai[zkin].qichuang;
				if (temp.zikuai[zkin].qishu != 2) {
					if (CS.DEBUG_JISUANZHENGZI) {
						if(log.isDebugEnabled()) log.debug("错误：气数不为二。");
					}
					zhengzijieguo[0][0] = -127;
					return zhengzijieguo; // 表示方法失败。
				}
				for (byte i = 1; i <= 2; i++) {
					if (lili == null) {
						if (CS.DEBUG_JISUANZHENGZI) {
							if(log.isDebugEnabled()) log.debug("气数不足二。");
						}
						zhengzijieguo[0][0] = -127;
						return zhengzijieguo; // 表示方法失败。
					}
					houxuan[i][0] = lili.a;
					houxuan[i][1] = lili.b;
					lili = lili.next;
				}
				houxuan[0][0] = 2;
				for (byte i = 1; i <= houxuan[0][0]; i++) {
					// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
					// 以后应该进行更细致的处理，根据要展开的局面确定。

					m1 = houxuan[i][0];
					n1 = houxuan[i][1];
					temp = new BoardLianShort(go[jumianshu]);

					// 扩展最后的局面，扩展同一个上级局面。
					if (temp.validate(m1, n1)) { // 判断合法着点
						temp.cgcl(m1, n1);
						youxiaodian++;
						go[jumianshu + youxiaodian] = temp;
						za[jumianshu + youxiaodian] = m1;
						zb[jumianshu + youxiaodian] = n1;
						if (CS.DEBUG_JISUANZHENGZI) {
							if(log.isDebugEnabled()) log.debug("max走" + "(" + m1 + "," + n1
									+ ")");
						}

					}
				}
				if (youxiaodian == 0) {
					// 返回，一般是征子方无子可下。
					if (CS.DEBUG_JISUANZHENGZI) {
						if(log.isDebugEnabled()) log.debug("有效点为0");

					}
					st[cengshu][0] = 0;

					if (cengshu == 1) { // 征子方直接无子可下，
						zhengzijieguo[0][0] = -127;
						return zhengzijieguo;
					}

					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu == -1) {
							zhengzijieguo[0][0] = -127;

							for (byte lins = 2; st[lins][0] != 0; lins++) {
								if (CS.DEBUG_JISUANZHENGZI) {
									if(log.isDebugEnabled()) log.debug("点为:("
											+ za[st[lins][0] - 1] + ","
											+ zb[st[lins][0] - 1] + ")");
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

	public void delete(short r, byte a, byte b) {
		// 仅从块的气串中删除一口气的坐标。
		// 因为有悔棋，所以不论增气。减气都可能涉及块。
		// 单个调用不能确定最终气数，所以kdq函数确有必要。
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法delete()");

		}
		DianNode1 temp = zikuai[r].qichuang;
		DianNode1 forward = zikuai[r].qichuang;
		byte qishu = zikuai[r].qishu;
		for (byte i = 1; i <= qishu; i++) {
			if (a == temp.a & b == temp.b) {
				if (i == 1) {
					zikuai[r].qichuang = temp.next;

				} else {
					forward.next = temp.next;
				}
			} else {
				forward = temp;
				temp = temp.next;
			}
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("方法delete()");
		}
	}

	public byte yiweidaoerweia(short c) {
		return (byte) ((c - 1) % 19 + 1);
	}

	public byte yiweidaoerweib(short c) {
		return (byte) ((c - 1) / 19 + 1);
	}

	public short erweidaoyiwei(byte a, byte b) {
		return (short) ((b - 1) * 19 + a);
	}

	public void cgcl(byte a, byte b) { // chang gui chu li
		// 命名是一个很难的课题，命名方法的重要性可以
		// 通过名字的历史性来看出，即使可以全局替换，
		// 由于名字可能已经在别处被引用，问题复杂化了。
		// 常规处理，已经判明是合法着点；
		// 可以接受的输入为(a,b)或c;c=b*19+a-19;完成数气提子
		// a是数组的行下标,也是平面的横坐标:1-19
		// b是数组的列下标,也是屏幕的纵坐标:1-19
		// byte c;//a,b的一维表示:1-361;

		byte m1 = a; // (a,b)在方法中不改变
		byte n1 = b; // (m1,n1)为(a,b)的邻点.

		byte yise = 0; // 与落子点异色
		byte tongse = 0; // 与落子点同色

		// 三种点的计数
		byte yisedianshu = 0; // 为(a,b)周围异色点计数
		byte kongbaidianshu = 0; // 为(a,b)周围空白点计数
		byte tongsedianshu = 0; // 为(a,b)周围同色点计数

		byte i = 0, j = 0; // 循环变量
		byte jubutizishu = 0; // 局部提子数(每一步的提子数)

		byte tkd = 0;

		byte[][] zijieqi = new byte[4][2]; // 用于划分气块时用。
		byte yuanshizhijieqishu = 0; // 直接气数：落子点周围直接的气数
		// 不包括提子形成的气数,所以要在提子前调用。

		short kin1 = 0; // a,b周围四点的块索引
		short[] ysk = { 0, 0, 0, 0 }; // 四异色邻子的块索引,同块不重复计算
		byte yiseks = 0; // 四邻异色块数

		byte yuanqikuaisuoyin; // 落子点原先所在的气块的索引。
		short yuanqikuaizishu; // 落子点原先所在的气块的子数。
		short zishujishu; // 落子点四周新气块的子数之和。

		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("进入方法cgcl()");
		}

		if (validate(a, b) == false) { // 1.判断落子点的有效性。
			return;
		}

		shoushu++;
		hui[shoushu][1] = a; // 手数处理前递增,即从1开始计数.与棋谱同.
		hui[shoushu][2] = b; // 记录每步的落点
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("落子点:" + a + "/" + b);
		}

		// 根据手数判断落子点的颜色。这是
		yise = (byte) (shoushu % 2 + 1); // yi se=1或2,黑先行为奇数
		tongse = (byte) ((1 + shoushu) % 2 + 1); // tong se=1或2,白后行为偶数

		zb[a][b][ZTXB] = tongse; // 可以动态一致

		// 2.保留落子点周围的直接气点。

		yuanqikuaisuoyin = zb[a][b][QKSYXB];
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("落子点所在气块索引为：" + yuanqikuaisuoyin);
		}
		qikuaixinxi(yuanqikuaisuoyin);
		zb[a][b][QKSYXB] = 0;
		qikuai[yuanqikuaisuoyin].deletezidian(a, b);
		yuanqikuaizishu = qikuai[yuanqikuaisuoyin].zishu;
		// 已经扣除落子点。
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("落子点所在气块子数现为：" + yuanqikuaizishu);
			// 原气块落子后的子数；
		}

		if (qikuai[yuanqikuaisuoyin].zishu == 0) {
			shanchuqikuaihao(yuanqikuaisuoyin);

		}
		zishujishu = 0; // 判断是否有新气块生成。

		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("零、建立初始新子块。");
		}

		ZiKuai1 linshi = new ZiKuai1();

		linshi.color = zb[a][b][ZTXB];
		linshi.zishu = 1;
		linshi.addzidian(a, b); // 新子块的初始化。
		zikuaishu++;
		zikuai[zikuaishu] = linshi;
		zbk[a][b] = zikuaishu; // count from first block
		huik[shoushu][0] = zikuaishu; // 记录所成块的索引

		if (CS.DEBUG_CGCL) {
			System.out.print("一、记录直接气。直接气点为");
		}
		for (i = 0; i < 4; i++) { // 直接的气，而非提子生成的气。
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == BLANK) { // 2.1the breath of blank
				zijieqi[yuanshizhijieqishu][0] = m1;
				zijieqi[yuanshizhijieqishu][1] = n1;
				yuanshizhijieqishu++;
			}
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("落子点周围的原始直接气点为：" + yuanshizhijieqishu);

		}

		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("二、处理同色邻子");
		}
		for (i = 0; i < 4; i++) { // 再处理同色邻子
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			// 因为kkhb时已经改变了原来块的索引号，所以不会再重复。
			// 算法和以往少有不同。

			kin1 = zbk[m1][n1];

			if (zb[m1][n1][ZTXB] == tongse && kin1 != zikuaishu) { // 3.1
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("同色点：a=" + m1 + ",b=" + n1);
				}
				tongsedianshu++; // 同色点计数
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("kin1＝" + kin1);
				}
				huik[shoushu][tongsedianshu] = kin1;
				HaoNode1 teli = zikuai[kin1].zwyskhao; // 修改周围块号的指针

				if (teli == null) {
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("meiyoulinkuai" + kin1);
					}
				}
				for (byte p = 1; p <= zikuai[kin1].zwyskshu; p++) {
					zikuai[teli.hao].deleteyisekuaihao(kin1);
					zikuai[teli.hao].addyisekuaihao(zikuaishu); // 修改块号
					zikuai[zikuaishu].addyisekuaihao(teli.hao);
					teli = teli.next;
				}

				zikuai[kin1].active = false; // 被合并的块在盘上消失。new
				kkhb(zikuaishu, kin1);

			}
			// 块的气数小于周围气块的最小值是危险的，当然还要考虑
			// 是否有长气的手段。

			/*
			 * if (zb[m1][n1][ZTXB] == tongse) { //3.1 tongsedianshu++; //同色点计数
			 * kin1 = zbk[m1][n1]; for (j = 1; j < tongsedianshu; j++) {
			 * //if(kin1==tsk[j]) break; if (kin1 == huik[shoushu][j]) { break;
			 * } } //变量冲突的惨痛教训 if (j == tongsedianshu) { //不重复 //tsk[ks++]=kin1;
			 * //hui[shoushu][20+ks]=kin1; //kuai[kin1].addkuai(zikuaishu);
			 * huik[shoushu][tongsedianshu] = kin1; HaoNode1 teli =
			 * zikuai[kin1].zwkhao; for (byte p = 1; p <= zikuai[kin1].zwkshu;
			 * p++) { zikuai[teli.hao].deletekuaihao(kin1);
			 * zikuai[teli.hao].addkuaihao(zikuaishu);//修改块号
			 * zikuai[zikuaishu].addkuaihao(teli.hao); teli=teli.next; }
			 * kkhb(zikuaishu, kin1); } else { tongsedianshu--; } // } //成块点 }
			 */
		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("同色点数为：" + tongsedianshu);

		}
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("三、处理异色邻子");
		}
		for (i = 0; i < 4; i++) { // 先处理异色邻子
			// byte bdcds = 0; //被打吃点计数.
			byte bdcks = 0; // 被打吃块计数.
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == yise) { // 1.1右边相邻点
				yisedianshu++; // 异色点计数
				kin1 = zbk[m1][n1]; // 块索引
				// if (kin1==0)为块。
				for (j = 0; j < yiseks; j++) {
					if (kin1 == ysk[j]) {
						break;
					}
				}
				if (j == yiseks) { // 不重复
					ysk[yiseks++] = kin1;
					byte qi = (byte) (zikuai[kin1].qishu - 1);

					if (qi == 0) { // 原来的气。
						// 不进行减气处理是为了悔棋处理方便
						yisedianshu--;
						tkd++; // <=4
						huik[shoushu][4 + tkd] = kin1; // 据此进行二级块处理；

						jubutizishu += zikuai[kin1].zishu; // 实际的提子数
						zikuai[kin1].active = false; // 被吃的棋块不再存在于盘上。
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("块被吃，块号为：" + kin1);
						}
						kzq(kin1, tongse); // 异色块被提,同色子增气.
						// 周围的块的相邻块号链表需要删除该被提的块号

						// 形成新的气块；
						qikuaishu++;
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("形成新的气块；" + qikuaishu);
						}
						qikuai[qikuaishu] = new QiKuai1();
						qikuai[qikuaishu].zichuang = zikuai[kin1].zichuang;
						qikuai[qikuaishu].zishu = zikuai[kin1].zishu;
						qikuai[qikuaishu].color = tongse;
						qkdsy(qikuaishu);
						// 生成新的二级块，周围的块有指针指向该二级块。

						HaoNode1 linh = zikuai[kin1].zwyskhao;
						byte linhs = zikuai[kin1].zwyskshu;
						for (byte tt = 1; tt <= linhs; tt++) {

							zikuai[linh.hao].deleteyisekuaihao(kin1);
							zikuai[linh.hao].addqikuaihao(qikuaishu); // 增加气块号。
							linh = linh.next;
						}

					}

					else if (qi < 0) {
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("气数错误:kin=" + kin1);
						}
						return;
					} else {
						zikuai[kin1].deleteqidian(a, b);
						kdq(kin1, qi);
						zikuai[kin1].addyisekuaihao(zikuaishu);
						zikuai[zikuaishu].addyisekuaihao(kin1); // 要防止重复
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("块" + kin1 + "气数减为"
									+ zb[m1][n1][QSXB]);
						}
						if (qi == 1) {

							huik[shoushu][8 + tkd] = kin1;
							if (CS.DEBUG_CGCL) {
								if(log.isDebugEnabled()) log.debug("块被打吃，块号为：" + kin1);
							}
						}
						DianNode1 dian;
						byte ii = 0;
						for (ii = 1; ii <= zikuai[kin1].qishu; ii++) {
							dian = zikuai[kin1].qichuang;
							m1 = dian.a;
							n1 = dian.b;
							if (zb[m1][n1][QKSYXB] == yuanqikuaisuoyin) {
								break;
							}
							dian = dian.next;
						}
						if (ii > zikuai[kin1].qishu) {
							zikuai[kin1].deleteqikuaihao(yuanqikuaisuoyin);
							if (CS.DEBUG_CGCL) {
								if(log.isDebugEnabled()) log.debug("减少一个相邻气块。"
										+ yuanqikuaisuoyin);
							}
						}
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("方法cgcl()返回");

						}
					}
				} // 非重复块
					// } //if kuai
			} // if==yiseks
		} // 用循环代替

		zb[a][b][QSXB] = 0; // 防止提子时的增气.
		if (shoushu % 2 == BLACK) {
			tiheizishu += jubutizishu;
		} else {
			tibaizishu += jubutizishu; // 将局部提子计入
		}

		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("处理空白邻子");
		}
		for (i = 0; i < 4; i++) { // 再处理空白邻子
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == BLANK) { // 2.1the breath of blank
				kongbaidianshu++; // 气点计数

			}
		}

		if (tongsedianshu == 0) { // 4.1 没有同色邻点？？？
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("没有同色邻点");
			}
			zb[a][b][QSXB] = kongbaidianshu;
			zikuai[zikuaishu].qishu = kongbaidianshu;
			if (kongbaidianshu == 1 && jubutizishu == 1) { // 考虑劫
				hui[shoushu][3] = zikuai[huik[shoushu][5]].zichuang.a; // 打劫禁手
				hui[shoushu][4] = zikuai[huik[shoushu][5]].zichuang.b;
				// 应该是被提之子成为禁手。

			} //

		}

		/*
		 * for (j = 1; j <= tongsedianshu; j++) { //hui[shoushu][20+j]=tsk[j-1];
		 * kkhb(zikuaishu, huik[shoushu][j]);
		 * //kkhb(zikuaishu,tsk[j-1]);//块块合并,气尚未处理. }
		 */
		jskq(zikuaishu);
		// 计算最小的块；
		if (zikuai[zikuaishu].zwyskshu > 1) {
			byte p = zikuai[zikuaishu].zwyskshu;
			HaoNode1 lint = zikuai[zikuaishu].zwyskhao;
			byte qis = 127;
			for (byte wi = 1; wi <= p; wi++) {
				if (zikuai[lint.hao].qishu < qis) {
					qis = zikuai[lint.hao].qishu;
				}
				lint = lint.next;
			}
			zikuai[zikuaishu].minqi = qis; // 最小的气、周围块的minqi也应该修正
		}

		// 11月22日，最后处理气块的生成
		byte m2, n2, m3, n3, m4, n4, x, y;
		switch (yuanshizhijieqishu) {
		case 1: {
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("直接气数为1，没有新块生成。");
			}
			zikuai[zikuaishu].addqikuaihao(yuanqikuaisuoyin);
			break;
		}
		case 2: {
			if (CS.DEBUG_CGCL) {
				System.out.print("直接气数为2，");

			}
			m1 = zijieqi[0][0];
			n1 = zijieqi[0][1];
			m2 = zijieqi[1][0];
			n2 = zijieqi[1][1];
			if (m1 == m2 || n1 == n2) {
				// 各自算。有无新块
				if (CS.DEBUG_CGCL) {
					System.out.print("气点同轴。");

				}
				qikuaizishu = 0;
				qikuaishu++;
				qikuai[qikuaishu] = new QiKuai1();
				short ttt = chengqikuai(m1, n1);
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("chengqikuai()=" + ttt
							+ "yuanqikuaizishu" + yuanqikuaizishu);
				}
				if (ttt == yuanqikuaizishu) {
					// meiyou 形成新块。为了使所有块都是当前局面存在的块
					// 必须使旧块继续使用。
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("但是没有新块生成");

					}
					qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
					qikuai[qikuaishu] = null;
					qikuaishu--; // 与实际存在的块数一致。
					qkdsy(yuanqikuaisuoyin);
				} else {
					qikuaizishu = 0;
					qikuaishu++;
					qikuai[qikuaishu] = new QiKuai1();
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("有新块生成。" + qikuaishu);

					}
					chengqikuai(m2, n2);
					qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
					qkdsy(yuanqikuaisuoyin);
					; // 只是恢复算起标志
					qikuai[qikuaishu] = null;
					qikuaishu--; // 与实际存在的块数一致。

					qkdsy(qikuaishu); // 新块也要定索引

				}
			} else {
				if (CS.DEBUG_CGCL) {
					System.out.print("气点不同轴，");

				}
				x = (byte) (m1 + m2 - a);
				y = (byte) (n1 + n2 - b);
				if (zb[x][y][ZTXB] == BLANK) {
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("对角点为空，没有新块生成。");
					}
				} else if (jgzs(x, y) == 1) {
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("对角点周围空，没有新块生成。");
					}
				} else {

					qikuaizishu = 0;
					qikuaishu++;
					if (CS.DEBUG_CGCL) {
						if(log.isDebugEnabled()) log.debug("以气块数成块：" + qikuaishu);

					}
					qikuai[qikuaishu] = new QiKuai1();

					if (chengqikuai(m1, n1) == yuanqikuaizishu) {
						// meiyou 形成新块。
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("没有新块生成。");

						}
						qikuai[qikuaishu] = null;
						qikuaishu--; // 与实际存在的块数一致。

						qkdsy(yuanqikuaisuoyin);
						; // 只是恢复算起标志

					} else {

						qikuaizishu = 0;
						qikuaishu++;
						if (CS.DEBUG_CGCL) {
							if(log.isDebugEnabled()) log.debug("以气块数成块：" + qikuaishu);
						}
						qikuai[qikuaishu] = new QiKuai1();
						chengqikuai(m2, n2);
						qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
						qikuai[qikuaishu] = null;
						qikuaishu--; // 与实际存在的块数一致。
						qkdsy(qikuaishu);
						qkdsy(yuanqikuaisuoyin); // 将新的气块定为旧的索引

					}

				}
			}
			break;
		}
		case 3: {
			byte lianjieshu = 0;
			m1 = zijieqi[0][0];
			n1 = zijieqi[0][1];
			m2 = zijieqi[1][0];
			n2 = zijieqi[1][1];
			m3 = zijieqi[2][0];
			n3 = zijieqi[2][1];
			if (m1 == m2 || n1 == n2) {

			} else {
				x = (byte) (m1 + m2 - a);
				y = (byte) (n1 + n2 - b);
				if (zb[x][y][ZTXB] == BLANK) {
					lianjieshu++;
				} else if (jgzs(x, y) == 1) {
					lianjieshu++;
				}

			}
			if (m1 == m3 || n1 == n3) {

			} else {
				x = (byte) (m1 + m3 - a);
				y = (byte) (n1 + n3 - b);
				if (zb[x][y][ZTXB] == BLANK) {
					lianjieshu++;

				} else if (jgzs(x, y) == 1) {
					lianjieshu++;

				}

			}
			if (m2 == m3 || n2 == n3) {

			} else {
				x = (byte) (m2 + m3 - a);
				y = (byte) (n2 + n3 - b);
				if (zb[x][y][ZTXB] == BLANK) {
					lianjieshu++;
				} else if (jgzs(x, y) == 1) {
					lianjieshu++;
				}

			}
			if (lianjieshu >= 2) {
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("没有新块生成。");

				}
			} else {
				zishujishu = 0;

				for (byte bianli = 0; bianli < 3; bianli++) {
					m1 = (byte) (zijieqi[bianli][0]);
					n1 = (byte) (zijieqi[bianli][1]);
					qikuaizishu = 0;
					qikuaishu++;
					qikuai[qikuaishu] = new QiKuai1();

					zishujishu += chengqikuai(m1, n1);
					if (zishujishu == yuanqikuaizishu) { // 已经扫描完毕
						if (bianli == 0) {
							// meiyou 形成新块。

							qikuai[qikuaishu] = null;
							qikuaishu--; // 与实际存在的块数一致。
							qkdsy(yuanqikuaisuoyin);
						} else if (bianli == 1) {
							qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
							qkdsy(yuanqikuaisuoyin);
							qikuai[qikuaishu] = null;
							qikuaishu--;
							qkdsy(qikuaishu);
						} else {
							qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
							qkdsy((byte) (yuanqikuaisuoyin));
							qkdsy((byte) (qikuaishu - 2));

							qikuai[qikuaishu] = null;
							qikuaishu--;
							qkdsy(qikuaishu);

						}
						break;

					}
				}

			}
			break;
		}
		case 4: {
			byte lianjieshu = 0;
			for (byte bianli = 0; bianli < 4; bianli++) {
				m1 = (byte) (a + szdjd[bianli][0]);
				n1 = (byte) (b + szdjd[bianli][1]);
				if (zb[m1][n1][ZTXB] == BLANK) {
					lianjieshu++;
					// 通过对角点连接
				} else if (jgzs(m1, n1) == 1) {
					lianjieshu++;
					// 通过九宫连接
				}
			}

			if (lianjieshu >= 3) {
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("直接气数为4，连接数为3，没有新块生成。");
				}
			} else {
				zishujishu = 0;

				for (byte bianli = 0; bianli < 4; bianli++) {
					m1 = (byte) (zijieqi[bianli][0]);
					n1 = (byte) (zijieqi[bianli][1]);
					qikuaizishu = 0;
					qikuaishu++;
					qikuai[qikuaishu] = new QiKuai1();
					zishujishu += chengqikuai(m1, n1);
					if (zishujishu == yuanqikuaizishu) { // 已经扫描完毕
						if (bianli == 0) {
							// meiyou 形成新块。
							// qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
							qikuai[qikuaishu] = null;
							qikuaishu--; // 与实际存在的块数一致。
							qkdsy(yuanqikuaisuoyin);

						} else if (bianli == 1) {
							qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
							qkdsy(yuanqikuaisuoyin);
							qikuai[qikuaishu] = null;
							qikuaishu--;
							qkdsy(qikuaishu);

						} else if (bianli == 2) {
							qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
							qkdsy((byte) (yuanqikuaisuoyin));
							qkdsy((byte) (qikuaishu - 2));

							qikuai[qikuaishu] = null;
							qikuaishu--;
							qkdsy(qikuaishu);

						} else {
							qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
							qkdsy((byte) (yuanqikuaisuoyin));
							qkdsy((byte) (qikuaishu - 3));
							qkdsy((byte) (qikuaishu - 2));

							qikuai[qikuaishu] = null;
							qikuaishu--;
							qkdsy(qikuaishu);
						}

						break;
					}
				}

			}
			break;
		}
		}

		// 新的棋块和原先气块的相邻程度改变
		// output();
	}

	public byte jgzs(byte m, byte n) { // 九宫子数。
		// m,n为九宫中心点。（中心点不计入）
		byte dang = 0; // 气数变量
		byte i, a, b; // 悔棋恢复时，解散块所成单点的气数计算；
		for (i = 0; i < 4; i++) {
			a = (byte) (m + szld[i][0]);
			b = (byte) (n + szld[i][1]);
			if (zb[a][b][ZTXB] != BLANK) { // 2.1the breath of blank
				dang++;
			}
		}
		for (i = 0; i < 4; i++) {
			a = (byte) (m + szdjd[i][0]);
			b = (byte) (n + szdjd[i][1]);
			if (zb[a][b][ZTXB] != BLANK) { // 2.1the breath of blank
				dang++;
			}
		}

		return dang;

	}

	/*
	 * public void clhuiqi() { //是否所有数据结构都能恢复? byte p = 0; byte yise = 0; byte
	 * tongse = 0; //yise is diff color.and 2 same. byte tdzs = 0; byte k0 = 0,
	 * yisedianshu = 0, kongbaidianshu = 0, tongsedianshu = 0, i = 0, j = 0;
	 * //the count for three kinds of point. byte ks = 0, kss = 0; //ks is count
	 * for block,kss for single point byte kin, kin1 = 0, m = 0, n = 0; //the
	 * block index. if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("方法:处理悔棋(clhuiqi)\n");
	 * tongse = (byte) ( (shoushu + 1) % 2 + 1); //tong se yise = (byte)
	 * (shoushu % 2 + 1); m = hui[shoushu][25]; hui[shoushu][25] = 0; n =
	 * hui[shoushu][26]; hui[shoushu][26] = 0; if (m <= 0 || n <= 0) { //弃权的恢复
	 * shoushu--; return; // } zzq(m, n, yise); //悔棋,对方增气,提子直接恢复,不用在此增气
	 * if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("悔棋:" + shoushu); if(CS.DEBUG_CGCL)
	 * if(log.isDebugEnabled()) log.debug("a=" + m + ",b=" + n); kin = hui[shoushu][0]; if (kin
	 * != 0) { //kin是新成的块 zikuai[kin].color = 0; zikuai[kin].qishu = 0;
	 * zikuai[kin].zishu = 0; zikuai[kin].zichuang = null; zikuai[kin].qichuang
	 * = null; zikuaishu = kin; //全局可用块号? zikuaishu--; //xinzeng. for (i = 1; i
	 * <= 4; i++) { if (hui[shoushu][2 * i + 12 - 1] <= 0) { //成新块的点 break; }
	 * else { m = hui[shoushu][12 + 2 * i - 1]; //13-20 n = hui[shoushu][12 + 2
	 * * i]; hui[shoushu][12 + 2 * i - 1] = 0; hui[shoushu][12 + 2 * i] = 0;
	 * zb[m][n][QKSYXB] = 0; zb[m][n][ZTXB] = tongse; //fang wei bian cheng
	 * zb[m][n][QSXB] = jszq(m, n); //计算子的气 if(CS.DEBUG_CGCL)
	 * if(log.isDebugEnabled()) log.debug("//计算成块点的气:" + "a=" + m + ",b" + n); } } //deal with 3
	 * sub for (i = 1; i <= 4; i++) { //是否旧块成新块 kin1 = hui[shoushu][20 + i];
	 * //21-24 hui[shoushu][20 + i] = 0; if (kin1 == 0) { break; } else { p =
	 * zikuai[kin1 + 128].zishu; DianNode1 temp = zikuai[kin1 + 128].zichuang;
	 * for (j = 1; j <= p; j++) { //这个循环关键，避免两块的混淆。 m = temp.a; n = temp.b;
	 * zb[m][n][3] = kin1; //修改块号 //zb[m][n][0]=tongse; zb[m][n][2] =
	 * zikuai[kin1 + 128].qishu; //恢复原块成块时的气 temp = temp.next; } //jskq(kin1);
	 * //cunchukuaiqi; } //else } //for } //if 是否新块 for (i = 1; i <= 4; i++) {
	 * //是否提子 if (hui[shoushu][2 * i - 1] <= 0) { break; } else { m =
	 * hui[shoushu][2 * i - 1]; n = hui[shoushu][2 * i]; hui[shoushu][2 * i - 1]
	 * = 0; hui[shoushu][2 * i] = 0; tdzs = i; //? zb[m][n][ZTXB] = yise;
	 * zb[m][n][QSXB] = 1; zb[m][n][QKSYXB] = 0; zjq(m, n, tongse);
	 * if(CS.DEBUG_CGCL) System.out.print("恢复被提子:"); if(CS.DEBUG_CGCL)
	 * if(log.isDebugEnabled()) log.debug("a=" + m + ",b=" + n); } } //for for (i = 1; i <= 4;
	 * i++) { //是否有被提的块 if (hui[shoushu][8 + i] == 0) { break; } else { kin1 =
	 * hui[shoushu][8 + i]; hui[shoushu][8 + i] = 0; kdq(kin1, (byte) 1);
	 * kjq(kin1, tongse); p = zikuai[kin1 + 128].zishu; DianNode1 temp =
	 * zikuai[kin1 + 128].zichuang; //shiwubiyao for (j = 1; j <= p; j++) { m =
	 * temp.a; n = temp.b; zb[m][n][ZTXB] = yise; zb[m][n][QKSYXB] = kin1; temp
	 * = temp.next; } tdzs += p; } //else } //for if (tongse == BLACK) {
	 * tiheizishu -= tdzs; } if (tongse == WHITE) { tibaizishu -= tdzs; } for (i
	 * = 0; i < 9; i++) { hui[shoushu][27 + i] = 0; //2yue } shoushu--;
	 * if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("方法clhuiqi:处理悔棋\n"); } //clhuiqi
	 */

	public byte jszq(byte m, byte n) { // huiqishiyong.
		byte dang = 0; // 气数变量
		byte i, a, b; // 悔棋恢复时，解散块所成单点的气数计算；
		for (i = 0; i < 4; i++) {
			a = (byte) (m + szld[i][0]);
			b = (byte) (n + szld[i][1]);
			if (zb[a][b][ZTXB] == BLANK) { // 2.1the breath of blank
				dang++;
			}
		}
		return dang;
	}

	public byte bijiaoqishu(short k1, short k2, byte colorxian) {
		// 从同一方的角度出发，先后手差一气。
		// 以 K1方的角度判断；
		byte m1 = zikuai[k1].qishu;
		byte m2 = zikuai[k2].qishu;
		byte cha = (byte) (m1 - m2);
		if (zikuai[k1].color == colorxian) {
			cha += 1;
		}
		// 返回值<=0;就是对杀失败；
		// 一方调用为1：对杀获胜，落后手；从另一方调用就是0：对杀失败，也可
		// 看成弃子，先手利用；
		// 一方调用为2：本方脱先仍然获胜；但是脱先后对方有先手利用；
		// 从另一方调用就是－1：对方脱先仍然失败；
		// 一方调用为3：本方脱先仍可获胜，且脱先后对方没有利用。
		// 另一方调用为－2：对方即使脱先本方也没有先手利用
		// 转换关系为：>0:先减一，再取反。
		// 他们是等价的，只是从不同方面描述。
		// 终于解决了.
		return cha;

	}

	public byte bijiaoqishu2( // 气数比较的结果不仅过程中步步相同
			// 而且双方仅仅差在符号。
			short k1, short k2, byte colorxian) {
		// 以k1的角度出发。
		// 从同一方的角度出发，先后手差2“气”。
		// 以 K1方的角度判断；
		byte m1 = zikuai[k1].qishu;
		byte m2 = zikuai[k2].qishu;
		byte cha = (byte) (2 * (m1 - m2));
		if (zikuai[k1].color == colorxian) {
			cha += 1;
		} else if (zikuai[k2].color == colorxian) {
			cha -= 1;
		}
		// 返回值<=0;就是对杀失败；
		// 一方调用为1：对杀获胜，落后手；从另一方调用就是-1：对杀失败，也可
		// 看成弃子，先手利用；
		// 一方调用为3：本方脱先仍然获胜；但是脱先后对方有先手利用；
		// 从另一方调用就是－1：对方脱先仍然失败；
		// 一方调用为5：本方脱先仍可获胜，且脱先后对方没有利用。
		// 另一方调用为－5：对方即使脱先本方也没有先手利用
		// 转换关系为：取反。
		// 2004年2月16日
		return cha;

	}

	public byte bijiaoqishu3(short k1, short k2, byte gongqi, byte colorxian) {
		// 带公气的对杀
		// 气数比较的结果不仅过程中步步相同
		// 而且双方仅仅差在符号。

		// 以k1的角度出发。
		// 从同一方的角度出发，先后手差2“气”。
		// 以 K1方的角度判断；
		byte m1 = zikuai[k1].qishu;
		byte m2 = zikuai[k2].qishu;
		byte cha = (byte) (2 * (m1 - m2));
		if (zikuai[k1].color == colorxian) {
			cha += 1;
		} else if (zikuai[k2].color == colorxian) {
			cha -= 1;
		}
		cha -= (2 * (gongqi - 1));
		// 返回值<=0;就是对杀失败；
		// 一方调用为1：对杀获胜，落后手；从另一方调用就是-1：对杀失败，也可
		// 看成弃子，先手利用；
		// 一方调用为3：本方脱先仍然获胜；但是脱先后对方有先手利用；
		// 从另一方调用就是－1：对方脱先仍然失败；
		// 一方调用为5：本方脱先仍可获胜，且脱先后对方没有利用。
		// 另一方调用为－5：对方即使脱先本方也没有先手利用
		// 转换关系为：取反。
		// 2004年2月16日
		return cha;

	}

	public byte xingshipanduan(byte colorjiaodu) {
		// 调用的契机，何时调用？对方脱先或者有提子。
		// 首先确定发起调用的地点是落子的局部
		// 同时要考虑对别处的影响，一般是伶近局部的影响
		// 涉及征子才考虑远处的影响。
		// 有的对杀用量表示，不要实际对杀。比如气长2，且短气方没有
		// changqidian.
		byte lingxian = 0;
		return lingxian;
	}

	/**
	 * refacted and move to StateAnalysis
	 */
	public void shengchengjumian() {
		// 从棋谱的位图表示生成kuai和zb数组的相应信息
		// 从链式气块生成局面项目复制来的，需要修改。
		// 该函数应该在初始局面时调用。

		byte i, j;
		byte m, m1, n1, n;
		byte qkxlhzs = 0; // 气块相邻黑子数
		byte qkxlbzs = 0;
		byte color = 0;
		byte othercolor = 0;

		// 第一遍扫描，生成块，（包括子数和子串）
		for (i = 1; i < 20; i++) { // i是纵坐标
			for (j = 1; j < 20; j++) { // j是横坐标,按行处理
				if (zb[j][i][SQBZXB] == 1) {
					continue; // SQBZXB此处相当于处理过的标志.
				}
				// 该点尚未处理。
				qikuaizishu = 0; // 每块子的计数,用于生成气块
				meikuaizishu = 0; // 每块子的计数，用于生成子块
				if (zb[j][i][ZTXB] == BLACK) { // 左.上必为空点或异色子
					zikuaishu++; // 块号递增
					zikuai[zikuaishu] = new ZiKuai1();
					zikuai[zikuaishu].color = BLACK;
					chengkuai(j, i, BLACK); // 判断右.下是否为同色子.
					zikuai[zikuaishu].zishu = meikuaizishu;
					// if(CS.DEBUG_CGCL)
					// System.out.print("zishucuowu:"+zikuaishu);
				} else if (zb[j][i][ZTXB] == WHITE) { // 左.上必为空点或异色子
					zikuaishu++;
					zikuai[zikuaishu] = new ZiKuai1();
					zikuai[zikuaishu].color = WHITE;
					chengkuai(j, i, WHITE); // 判断右.下是否为同色子
					zikuai[zikuaishu].zishu = meikuaizishu;
					// if(CS.DEBUG_CGCL)
					// System.out.print("zishucuowu:"+zikuaishu);
				} else if (zb[j][i][ZTXB] == BLANK) {
					qikuaishu++;
					qikuai[qikuaishu] = new QiKuai1();
					chengqikuai(j, i);
					qikuai[qikuaishu].zishu = qikuaizishu;
					// if(CS.DEBUG_CGCL)
					// System.out.print("zishucuowu:"+qikuaishu) ;
				}
				qikuaizishu = 0;
			}
		} // 生成块,包括棋块。

		for (i = 1; i < 20; i++) { // i是纵坐标
			for (j = 1; j < 20; j++) { // j是横坐标
				zb[j][i][SQBZXB] = 0; // 恢复每个点的算气标志
				// if(zb[j][i][ZTXB]>0 && zb[j][i][KSYXB]==0){
				// zb[j][i][QSXB]=jszq(j,i);
				// }
			}
		}

		// 计算块气，包括点气。
		for (i = 1; i <= zikuaishu; i++) {
			// byte qi=jskq(ki);计算块气过程中直接储存气点.
			jskq(i);
		} // 计算块气

		for (i = 1; i <= qikuaishu; i++) { // 从气块入手，找到强块。
			short meikuaizishu = qikuai[i].zishu;
			if (meikuaizishu == 1) { // 眼位
				// 四周必然有子。
				m = qikuai[qikuaishu].zichuang.a;
				n = qikuai[qikuaishu].zichuang.b;
				for (byte k = 0; k < 4; k++) {
					m1 = (byte) (m + szld[k][0]);
					n1 = (byte) (n + szld[k][1]);

					if (zb[m1][n1][ZTXB] == WHITE) {
						qkxlbzs++;
					} else if (zb[m1][n1][ZTXB] == BLACK) {
						qkxlhzs++;
					}
				}
				if (qkxlbzs == 0) { // 黑方的眼
					qikuai[i].color = CS.BLACK;
					if (qikuaizuixiaoqi(i) > 1) {
						// 所成的眼不会立即被破坏。
						erjikuaishu++;
						erjikuai[erjikuaishu] = new ErJiKuai();
						for (byte k = 0; k < 4; k++) {
							m1 = (byte) (m + szld[k][0]);
							n1 = (byte) (n + szld[k][1]);
							if (zb[m1][n1][ZTXB] == BLACK) {
								erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
							}

						}

					} else {
						// 成气块的棋块本身处于被打吃状态。
					}

				} else if (qkxlhzs == 0) { // 白方的眼，并成一体。
					qikuai[i].color = CS.WHITE;
					if (qikuaizuixiaoqi(i) > 1) {
						erjikuaishu++;
						erjikuai[erjikuaishu] = new ErJiKuai();
						for (byte k = 0; k < 4; k++) {
							m1 = (byte) (m + szld[k][0]);
							n1 = (byte) (n + szld[k][1]);
							if (zb[m1][n1][ZTXB] == BLACK) {
								erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
							}

						}

					} else {
						// todo
					}

				} else { // 双方的公气。
					qikuai[i].color = 5;
				}
				// qikuai[qikuaishu--].zichuang=null;
				// qikuai[qikuaishu].qichuang
				// qikuai[ki--][1][1]=0;
				// zb[j][i][KSYXB]=0;//非块
				// todo:眼位的处理
				// 找出周围块，气数为一且单子，则打劫。两处为一气，
				// 则双提，块为一气，则打多还一、、
			} else if (meikuaizishu > 1) { // 大眼
				// qikuai[qikuaishu].zishu = meikuaizishu;
				DianNode1 tee = qikuai[i].zichuang;
				for (short hh = 1; hh <= meikuaizishu; hh++) {
					m = tee.a;
					n = tee.b;
					for (byte k = 0; k < 4; k++) {
						m1 = (byte) (m + szld[k][0]);
						n1 = (byte) (n + szld[k][1]);

						if (zb[m1][n1][ZTXB] == WHITE) {
							qkxlbzs++;
						} else if (zb[m1][n1][ZTXB] == BLACK) {
							qkxlhzs++;
						}
					}
					tee = tee.next;
				}
				if (qkxlbzs == 0) { // 黑方的大眼
					// 周围的块数，越少越好。
					if (qikuaizuixiaoqi(i) > 1) {
						erjikuaishu++;
						erjikuai[erjikuaishu] = new ErJiKuai();

						for (short hh = 1; hh <= meikuaizishu; hh++) {
							m = tee.a;
							n = tee.b;

							for (byte k = 0; k < 4; k++) {
								m1 = (byte) (m + szld[k][0]);
								n1 = (byte) (n + szld[k][1]);
								if (zb[m1][n1][ZTXB] == BLACK) {
									qikuai[i].addzikuaihao(zbk[m1][n1]);
									erjikuai[erjikuaishu]
											.addkuaihao(zbk[m1][n1]);
								}

							}
						}
					}
				} else if (qkxlhzs == 0) { // 白方的大眼，并成一体。
					if (qikuaizuixiaoqi(i) > 1) {
						erjikuaishu++;
						erjikuai[erjikuaishu] = new ErJiKuai();

						for (short hh = 1; hh <= meikuaizishu; hh++) {
							m = tee.a;
							n = tee.b;

							for (byte k = 0; k < 4; k++) {
								m1 = (byte) (m + szld[k][0]);
								n1 = (byte) (n + szld[k][1]);
								if (zb[m1][n1][ZTXB] == WHITE) {
									qikuai[i].addzikuaihao(zbk[m1][n1]);
									erjikuai[erjikuaishu]
											.addkuaihao(zbk[m1][n1]);
								}

							}
						}
					}

				} else { // 双方的公气。

				}

			} else {
				if (CS.DEBUG_CGCL) {
					if(log.isDebugEnabled()) log.debug("error:zishu<1");

				}
			}

		}

		/*
		 * byte zijishu = 0; byte kuaijishu = 0; for (i = 1; i <= dandianshu;
		 * i++) { zijishu = 0; kuaijishu = 0; m = dandian[i][0]; n =
		 * dandian[i][1]; color = zb[m][n][ZTXB]; if (color == 1) othercolor =
		 * 2; else if (color == 2) othercolor = 1; for (j = 0; j < 4; j++) { m =
		 * (byte) (dandian[i][0] + szld[j][0]); n = (byte) (dandian[i][1] +
		 * szld[j][1]); if (zb[m][n][ZTXB] == othercolor) { if (zb[m][n][KSYXB]
		 * == 0) { zb2[m][n][2 * zijishu] = m; zb2[m][n][2 * zijishu + 1] = n;
		 * zijishu++; } else{//注意防止块的重复。 } } }//for }
		 */
		byte zijishu = 0;
		short kzishu = 0; // 块含有的子数，控制循环
		DianNode1 temp;
		HaoNode1 linhao;
		short zikin1;
		byte zikin2;
		for (i = 1; i <= zikuaishu; i++) {
			color = zikuai[i].color;
			if (color == CS.BLACK) {
				othercolor = CS.WHITE;
			} else if (color == CS.WHITE) {
				othercolor = CS.BLACK;
				// 遍历该块的各个子，得到周围异色块，主意防止重复。
			}
			kzishu = zikuai[i].zishu;

			for (temp = zikuai[i].zichuang; temp != null; temp = temp.next) {
				for (j = 0; j < 4; j++) {

					m = (byte) (temp.a + szld[j][0]);
					n = (byte) (temp.b + szld[j][1]);
					if (zb[m][n][ZTXB] == othercolor) {

						// else if(zb[m][n][SQBZXB]==0){//注意防止块的重复。
						zikin1 = zbk[m][n];
						zikuai[i].addyisekuaihao(zikin1);

					} else if (zb[m][n][ZTXB] == CS.BLANK) {

						// else if(zb[m][n][SQBZXB]==0){//注意防止块的重复。
						zikin2 = zb[m][n][QKSYXB];
						zikuai[i].addqikuaihao(zikin2);

					}

				} // for

			}

		}
	}

	public void chengkuai1(byte a, byte b) { // 不完善
		qikuaishu++;
		qikuai[qikuaishu] = new QiKuai1();
		qikuaizishu = 0;
		if (chengqikuai2(a, b) == false) {
			qikuai[qikuaishu] = null;
			qikuaishu--;
		}
		// qikuaishu的SQBZXB应该清零
	}

	short meikuaizishu;

	public void chengkuai(byte a, byte b, byte color) {
		// 收集信息的过程中,可以令color=BLANK,调用该函数,但是气块的信息
		// 不能驻留在kuai数组内,必须早点调用并清除.
		// byte max=127;
		byte m1, n1;
		byte othercolor = 0;
		DianNode1 temp = new DianNode1();

		if (color == 1) {
			othercolor = 2;
		} else if (color == 2) {
			othercolor = 1;
		}
		temp.a = a;
		temp.b = b;
		temp.next = zikuai[zikuaishu].zichuang; // 块号已经预定
		zikuai[zikuaishu].zichuang = temp;
		zb[a][b][SQBZXB] = 1;
		zbk[a][b] = zikuaishu;
		meikuaizishu++; // 因为现在独子也算块，不必取消块
		for (byte k = 0; k < 4; k++) {
			m1 = (byte) (a + szld[k][0]);
			n1 = (byte) (b + szld[k][1]);
			if (zb[m1][n1][SQBZXB] == 0 && zb[m1][n1][ZTXB] == color) {
				chengkuai(m1, n1, color);
			}
		}
	} // 成块的点SQBZXB==1;

	public short chengqikuai(byte a, byte b) {
		// 因为形势判断调用时无需每次调用都清除标志，所以
		// 函数本身不清除标志。
		byte m1, n1;

		qikuaizishu++;
		// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("气块号为：" + qikuaishu);
		qikuai[qikuaishu].addzidian(a, b); // ?
		if (CS.DEBUG_CGCL) {
			if(log.isDebugEnabled()) log.debug("气块增加点：" + a + "/" + b);
		}
		zb[a][b][SQBZXB] = 1;
		zb[a][b][QKSYXB] = qikuaishu;

		for (byte k = 0; k < 4; k++) {
			m1 = (byte) (a + szld[k][0]);
			n1 = (byte) (b + szld[k][1]);
			if (zb[m1][n1][SQBZXB] != 0) {
				continue;
			}

			if (zb[m1][n1][ZTXB] == BLANK) {
				chengqikuai(m1, n1);

			} else if (zb[m1][n1][ZTXB] == BLACK || zb[m1][n1][ZTXB] == WHITE) {
				qikuai[qikuaishu].addzikuaihao(zbk[m1][n1]);
				// 反向的连接之后再建立，因为气块号可能会变。
			}
		}
		return qikuaizishu;
	} // 成块的点SQBZXB==1;

	public boolean chengqikuai2(byte a, byte b) {
		// 遇到大块就返回
		// 收集信息的过程中,可以令color=BLANK,调用该函数,但是气块的信息
		// 不能驻留在kuai数组内,必须早点调用并清除.
		// 调用该函数前必须将qkzishu清零
		byte m1, n1;
		// byte zishu=0;
		if (qikuaizishu < 10) {
			qikuai[qikuaishu].addzidian(a, b);
			qikuaizishu++;
		} else {
			qikuaizishu++;
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("该块的子数超过10,块号为:" + zikuaishu);
			}
			return false;
		}
		zb[a][b][SQBZXB] = 1;
		zb[a][b][QKSYXB] = qikuaishu;
		// zishu++;
		for (byte k = 0; k < 4; k++) {
			m1 = (byte) (a + szld[k][0]);
			n1 = (byte) (b + szld[k][1]);
			if (zb[m1][n1][SQBZXB] == 0 & zb[m1][n1][ZTXB] == BLANK) {
				if (chengqikuai2(m1, n1) == false) {
					return false;
				}
			}
		}
		return true;
	}

	public void qiquan() {
		shoushu++;
		hui[shoushu][1] = 0;
		hui[shoushu][2] = 0;

	}

	public byte qikuaiyanse(byte kin) {
		// 判断气块的颜色，并且使相邻的棋块指向该气块。
		byte yanse = 0;
		byte i, j;
		byte a, b;
		byte m1, n1;
		short zishu = qikuai[kin].zishu;
		DianNode1 temp = qikuai[kin].zichuang;
		if (zishu > 20) {
			if (CS.DEBUG_CGCL) {
				if(log.isDebugEnabled()) log.debug("气块太大，不可能是单色气块。");
			}
			return 5;
		}
		for (j = 1; j <= zishu; j++) {
			a = temp.a;
			b = temp.b;
			for (i = 0; i < 4; i++) { // 直接的气，而非提子生成的气。

				m1 = (byte) (a + szld[i][0]);
				n1 = (byte) (b + szld[i][1]);
				if (zb[m1][n1][ZTXB] == BLACK) { // 2.1the breath of blank
					// if(CS.DEBUG_CGCL) System.out.print("记录直接气：");
					if (yanse == 0) {
						yanse = BLACK;
					} else if (yanse == WHITE) {
						return 5; // 杂色。
					}

				} else if (zb[m1][n1][ZTXB] == WHITE) { // 2.1the breath of
														// blank
					if (yanse == 0) {
						yanse = WHITE;
					} else if (yanse == BLACK) {
						return 5; // 杂色。
					}

				}
			}
		}
		if (yanse == BLACK || yanse == WHITE) {

		}
		return yanse;
	}

	public byte yisekuaizuixiaoqi(short kin) {
		// 计算棋块周围异色棋子块的最小气。
		byte minqi = 127;
		HaoNode1 linshi = zikuai[kin].zwyskhao;
		byte i = 1;
		byte qishu = 0;
		byte zwyskshu = zikuai[kin].zwyskshu;
		for (; i <= zwyskshu; i++) {
			if (linshi == null) {
				if (CS.DEBUG_CGCL) {
					System.out.print("该块的周围异色棋块链表有误。" + kin);
				}
				break;
			}
			qishu = zikuai[linshi.hao].qishu;
			if (qishu < minqi) {
				minqi = qishu;
			}
			linshi = linshi.next;
		}
		zikuai[kin].minqi = minqi;

		return minqi;
	}

	public byte qikuaizuixiaoqi(byte kin) {
		// 计算气块周围棋子块的最小气。
		byte minqi = 127;
		HaoNode1 linshi = qikuai[kin].zwzkhao;
		byte i = 1;
		byte qishu = 0;
		byte zwzkshu = qikuai[kin].zwzkshu;
		for (; i <= zwzkshu; i++) {
			if (linshi == null) {
				if (CS.DEBUG_CGCL) {
					System.out.print("该块的周围棋块链表有误。" + kin);
				}
				break;
			}
			qishu = zikuai[linshi.hao].qishu;
			if (qishu < minqi) {
				minqi = qishu;
			}
			linshi = linshi.next;
		}
		qikuai[kin].minqi = minqi;

		return minqi;
	}

	// 提出控制点的概念：比如虎口之类。属于强的控制点。控制点有利于识别眼型。
	public byte lianjiexing(short kin1, short kin2) {

		return CS.WEIDINGLIANJIE;

	}

	public void zairujumian(DataInputStream jmin) throws IOException {

		byte a, b, color;
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				zb[i][j][QKSYXB] = 0;
				qikuai[qikuaishu].deletezidian(i, j);
			}
		}
		qikuai[qikuaishu] = null;
		qikuaishu--;
		while (jmin.available() != 0) {
			a = jmin.readByte();
			b = jmin.readByte();
			color = jmin.readByte();
			if (CS.DEBUG_CGCL) {
				System.out.print("i=" + a);
				System.out.print("j=" + b);
				if(log.isDebugEnabled()) log.debug("color=" + color);
			}
			zb[a][b][ZTXB] = color;
			if (a < 1 | a > 19 | b < 1 | b > 19 | color < 1 | color > 2) {
				if (CS.DEBUG_CGCL) {
					System.out.print("载入的数据有误！" + a);
				}
				
			}

		}

		if (CS.DEBUG_CGCL) {
			System.out.print("载入局面");

		}
	}

	public boolean shifoukongzhidian(byte a, byte b) {
		// 调用方保证该点空白。
		if (jszq(a, b) == 1) {

		} else {

		}
		return true;
	}

	public Object clone() {
		BoardLianShort temp = null;
		byte i, j, k;
		short t;
		try {
			temp = (BoardLianShort) (super.clone());
		} catch (CloneNotSupportedException de) {
			de.printStackTrace();
		}

		for (i = 1; i <= qikuaishu; i++) {
			// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("qikuai="+i);
			if (qikuai[i] != null) {
				temp.qikuai[i] = (QiKuai1) qikuai[i].clone();
			}
		}
		for (t = 1; t <= zikuaishu; t++) {
			// if(CS.DEBUG_CGCL) if(log.isDebugEnabled()) log.debug("zikuai="+t);
			if (temp.zikuai[t] != null) {
				temp.zikuai[t] = (ZiKuai1) zikuai[t].clone();
			}

		}
		return temp;

	}

	// 有两个做眼点的识别。比如有三子气块，有两个成气块点。
	// 但是还要涉及所成气块的真假。

	/*
	 * public void duishaxingjubu(ErJiKuai erjikuai){ //对杀型局部的认识。 //相邻棋块的整体,erji
	 * //确定每一块的性质。气能否长？ byte []zikuai =new byte[]; for(byte i=0;i<erjikuai.){ }
	 * }
	 */
	public void shuchujumian(DataOutputStream jmout) throws IOException {
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				if (zb[i][j][ZTXB] != 0) {
					jmout.writeByte((byte) i);
					jmout.writeByte((byte) j);
					jmout.writeByte((byte) zb[i][j][ZTXB]);
					if (CS.DEBUG_CGCL) {
						System.out.print("i=" + i);
						if(log.isDebugEnabled()) log.debug("j=" + j);
					}
				}
			}
		}
		jmout.flush();
		if (CS.DEBUG_CGCL) {
			System.out.print("输出局面");

		}
	}

	public byte chaiershu(byte a, byte b) {// 拆二数
		byte ershu = 0;
		return ershu;
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
				a[i][j] = zb[j][i][ZTXB];

			}
		}
		return a;
	}
}