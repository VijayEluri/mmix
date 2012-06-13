package untitled6;

import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import org.apache.log4j.Logger;

import untitled10.QiKuai1;

public class SiHuoZhiZuo extends Applet {
	private static final Logger log = Logger.getLogger(QiKuai1.class);
	int ju = 0;
	// boolean HUIQICH=false;// dan bu hui qi shi chong hua
	// boolean ZHENGCHANGCH=false;//cong wen jian da kai shi chong hua
	// int guize=0;chinese regular;
	int bhs = 0; // bian hua de shu mu变化数目
	int linss = 0; // chushi ju mian de shou shu初始局面的手数,确定能否悔棋
	int shoushu = 0; // dang qian shou shu,caution:it increase before deal
	// 当前手数,处理之前递增.
	int a = -1; // a is the row subscript of the point.
	// a是数组的行下标,也是平面的横坐标
	int b = -1; // b is the column subscript of the point.
	// b是数组的列下标,也是屏幕的纵坐标
	int ki = 0; // kuai shu块数?
	int ktm = -1, ktn = -1; // and position for points eaten.禁提点的坐标(打劫用)
	int ktb = 0, ktw = 0; // the count for black and white point eaten.
	// 黑白被吃子计数
	int[][][] zb = new int[19][19][4]; // 0:state;2:breath;3:blockindex
	// 棋盘的坐标数组,下标从0到18;
	int[][][] kuai = new int[100][50][2]; // mei yi kuai de ge zi zuo biao
	// 存储每块的详细信息
	int[][] hui = new int[400][27]; // 0=zi ti yi kuai,1~8four single point
	// eaten,9~12 kuai suo ying of fou block eaten.13~24is same ,25,26a,b
	// 棋局的过程信息,用于悔棋
	int[][] huitemp = new int[50][2];
	boolean fzhudong = false; // 是否主动
	boolean fhuiqi = false; // 是否悔棋
	boolean fqianjin = false; // 是否前进
	// int[][][]tbzb = new int[19][19][4];
	// int[][][]tbkuai = new int[100][50][2];
	// int [][] tbhui=new int[400][27];
	int[][][] bianhua = new int[50][100][2]; // 可存储50个变化
	int[][] jsweizhi = new int[50][2]; // 解说位置,最多50句,[0]为第几个变化,
	// [1]为第几手
	int jss; // 解说数目
	String[] tempstr = new String[50]; // 字符串数组,存储解说
	DataOutputStream out; // qi pu shu chu wen jain棋谱输出文件
	DataOutputStream out2;
	BufferedWriter jsout; // jie shuo shu chu wen jian解说输出文件
	// DataInputStream in;
	Button store = new Button("储存变化");
	Button huiqi = new Button("悔棋");
	// Button dakai=new Button("从文件打开");
	Button conglai = new Button("新变化");
	Button pass = new Button("放弃一手");
	Button wenjian = new Button("存为文件");
	TextArea jieshuo = new TextArea(20, 120);
	Button jiarujieshuo = new Button("加入解说");

	/*
	 * public static void main(String args[]){ StarterCombinedFrame app= new
	 * StarterCombinedFrame("死活练习"); app.setSize(800,600); app.show();
	 * if(log.isDebugEnabled()) log.debug("shisuo:main "); }
	 */

	/*
	 * static class WL extends WindowAdapter{ public void
	 * windowClosing(WindowEvent e){ System.exit(0); } } public static void
	 * main(String[] args){ Untitled1 applet=new Untitled1(); Frame aframe = new
	 * Frame("Shihuo"); aframe.addWindowListener(new WL());
	 * aframe.add(applet,BorderLayout.CENTER ); aframe.setSize(800,560);
	 * applet.init() ; applet.start() ; aframe.setVisible(true); }
	 */
	public void init() { // 初始化,完成界面
		int i, j;
		setLayout(null);
		add(store);
		add(pass);
		add(huiqi);
		// add(dakai);
		add(wenjian);
		add(conglai);
		add(jieshuo);
		add(jiarujieshuo);
		store.setBounds(575, 115, 60, 26);
		// reshape(575,115,60,26);
		// huiqi.setBounds(635,15,60,26);
		wenjian.setBounds(635, 115, 60, 26);
		huiqi.setBounds(575, 141, 60, 26);
		conglai.setBounds(635, 141, 60, 26);
		// wenjian.setBounds(635,41,60,26);

		pass.setBounds(575, 167, 60, 26);
		// conglai.setBounds(635,67,60,26);
		jieshuo.setBounds(575, 220, 180, 150);
		jiarujieshuo.setBounds(575, 390, 60, 26);
		conglai.setEnabled(false);
		huiqi.setEnabled(false);
		store.setEnabled(false);
		wenjian.setEnabled(false);
		for (i = 1; i < 400; i++) {
			for (j = 1; j <= 8; j++) {
				hui[i][j] = -1;
			}
			for (j = 13; j <= 20; j++) {
				hui[i][j] = -1;
			}
			hui[i][25] = -1;
			hui[i][26] = -1;
		}
		for (j = 0; j < 50; j++) {
			huitemp[j][0] = -1;
			huitemp[j][1] = -1;
		}
		/*
		 * try{ URL base=this.getCodeBase(); URL dbase=this.getDocumentBase() ;
		 * if(log.isDebugEnabled()) log.debug("CodeBase="+base);
		 * if(log.isDebugEnabled()) log.debug("documentBase="+dbase); URL
		 * urlbase=new URL( base+"go"); if(log.isDebugEnabled())
		 * log.debug("UrlBase="+urlbase); URLConnection
		 * uconn=urlbase.openConnection(); InputStream
		 * uc=uconn.getInputStream(); BufferedReader buffin= new
		 * BufferedReader(new InputStreamReader(uc)); String s;
		 * //a=buffin.readInt while((s=buffin.readLine())!=null){
		 * jieshuo.append(s); if(log.isDebugEnabled()) log.debug("s="+s); }
		 * }catch(Exception e){ e.printStackTrace() ; }
		 */
	}

	public void update(Graphics g) { // 585
		paint(g);
	}

	public boolean mouseDown(Event e, int x, int y) { // 接受鼠标输入
		a = (x - 4) / 28; // 完成数气提子等
		b = (y - 4) / 28;
		// showStatus("a="+a+", b="+b);
		if (a >= 0 && a <= 18 && b >= 0 && b <= 18 && zb[a][b][2] == 0) { // 下标合法,该点空白
			if (a == ktm && b == ktn) { // decide is it the prohibtted point?
				// 是否禁着点
				// showStatus("this point is not permmitted,try another point!");
			} else {
				if (log.isDebugEnabled())
					log.debug("a=" + a);
				if (log.isDebugEnabled())
					log.debug("b=" + b);
				fzhudong = true; // 与显示有关
				fqianjin = true;
				cgcl(); // chang gui chu li真正完成数气提子
				if (shoushu > linss) {
					huiqi.setEnabled(true);
					// enable() ;
					conglai.setEnabled(true);
					// .setEnabled(true);
					store.setEnabled(true);

				}
				repaint();
			}
		} else {
			// showStatus("it is not the right point,please play again!");
		}
		a = -1;
		b = -1;
		return true;
	}

	public void cgcl() { // chang gui chu li qi zi de qi
		// 完成数气提子
		int p = 0;
		int linzishu = 0; // 邻子数
		int yise = 0; // 异色
		int tongse = 0; // yise is diff color.and 2 same.同色
		int k0 = 0, k1 = 0, k2 = 0, k3 = 0, i = 0, j = 0; // the count for three
															// kinds of point.
		// 三种点的计数,k1为异色点计数,k2为气点计数,k3为同色点计数
		int dang = 0; // dang is breath of block.块的气数
		int ktz = 0; // ,kq=0,p=0,q=0; //dang is breath of block.
		int ks = 0, kss = 0; // ks is count for block,kss for single point
		// 相邻的成块点数和独立单点数
		int kin1 = 0, kin2 = 0, kin3 = 0, kin4 = 0, m = 0, n = 0; // the block
																	// index.
		// a,b周围四点的块索引
		int gq = 0;
		int[] u = { 0, 0, 0, 0, 0 }; // position
		int[] v = { 0, 0, 0, 0, 0 }; // 四个邻子的坐标
		int[] k = { 0, 0, 0, 0, 0 }; // array for block index.四邻子的块索引
		int tzd = 0, tkd = 0; // the count for single pointeaten andblock eaten.
		// 吃的点数和块数
		// if(log.isDebugEnabled())
		// log.debug("//come into method jhcl.jiaohu chu li");
		shoushu++; // 手数处理前递增,即从1开始计数.与棋谱同.
		hui[shoushu][25] = a; // 记录每步的落点
		hui[shoushu][26] = b;
		yise = shoushu % 2 + 1; // yi se=1或2,黑先行为奇数
		tongse = (1 + shoushu) % 2 + 1; // tong se=1或2,白后行为偶数
		zb[a][b][0] = tongse; // 可以动态一致

		if ((a + 1) <= 18) { // 计算邻子数2月20日增加
			linzishu++;
		}
		if ((a - 1) >= 0) {
			linzishu++;
		}
		if ((b + 1) <= 18) {
			linzishu++;
		}
		if ((b - 1) >= 0) {
			linzishu++;
		} // 计算邻子数2月20日增加

		if ((a + 1) <= 18 && zb[a + 1][b][0] == yise) { // 1.1右边相邻点
			k1++; // the count of diffrent color.异色点计数
			kin1 = zb[a + 1][b][3]; // the block index for the point.66
			if (kin1 == 0) { // not a block.不是块
				zb[a + 1][b][2] -= 1;
				if (zb[a + 1][b][2] <= 0) { // eat the diff point
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a + 1;
					hui[shoushu][tzd * 2] = b;
					if (log.isDebugEnabled())
						log.debug("ti zi 1 xing");
					ktz++; // single point eaten was count
					zzq(a + 1, b, tongse); // zi zhen qi
				}
			} else { // if (kin1==0)
				int qi = kuai[kin1][0][0] - 1;
				kdq(kin1, qi);
				if (kuai[kin1][0][0] <= 0) {
					k1--;
					tkd++;
					hui[shoushu][8 + tkd] = kin1;
					ktz += kuai[kin1][0][1];
					kzq(kin1, tongse); // increase the breath of point surround
				}
			}
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == yise) { // 1.2
			k1++;
			kin2 = zb[a - 1][b][3];
			if (kin2 == 0) { // 99
				zb[a - 1][b][2] -= 1;
				if (zb[a - 1][b][2] <= 0) {
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a - 1;
					hui[shoushu][tzd * 2] = b;
					if (log.isDebugEnabled())
						log.debug("ti zi 2 xing");
					ktz++;
					zzq(a - 1, b, tongse); // zi zhen qi
				}
			} else if (kin2 != kin1) {
				int qi = kuai[kin2][0][0] - 1;
				kdq(kin2, qi);
				// kuai[kin2][0][0]-=1;
				if (kuai[kin2][0][0] <= 0) {
					k1--;
					tkd++;
					hui[shoushu][8 + tkd] = kin2;
					ktz += kuai[kin2][0][1];
					kzq(kin2, tongse); // kuai zheng qi
				}
			} // overpaint

		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == yise) { // 1.3
			k1++;
			kin3 = zb[a][b + 1][3];
			if (kin3 == 0) {
				zb[a][b + 1][2] -= 1; // 132
				if (zb[a][b + 1][2] <= 0) {
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a;
					hui[shoushu][tzd * 2] = b + 1;
					if (log.isDebugEnabled())
						log.debug("ti zi 3 xing");
					ktz++;
					zzq(a, (b + 1), tongse); // zi zhen qi
				}
			} else if (kin3 != kin2 && kin3 != kin1) {
				int qi = kuai[kin3][0][0] - 1;
				kdq(kin3, qi);
				// kuai[kin3][0][0]-=1;
				if (kuai[kin3][0][0] <= 0) {
					k1--;
					tkd++;
					hui[shoushu][8 + tkd] = kin3;
					ktz += kuai[kin3][0][1];
					kzq(kin3, tongse); // kuai zheng qi
				}
			}
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == yise) { // 1.4
			k1++;
			kin4 = zb[a][b - 1][3]; // the subscipt 3 mean the block index
			if (kin4 == 0) {
				zb[a][b - 1][2] -= 1;
				if (zb[a][b - 1][2] <= 0) { // 165
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a;
					hui[shoushu][tzd * 2] = b - 1;
					if (log.isDebugEnabled())
						log.debug("ti zi 4 xing");
					ktz++;
					zzq(a, (b - 1), tongse); // zi zhen qi
				}
			} else if (kin4 != kin3 && kin4 != kin2 && kin4 != kin1) {
				int qi = kuai[kin4][0][0] - 1;
				kdq(kin4, qi);
				// kuai[kin4][0][0]-=1;
				if (kuai[kin4][0][0] <= 0) {
					k1--;
					tkd++;
					hui[shoushu][8 + tkd] = kin4;
					ktz += kuai[kin4][0][1];
					kzq(kin4, tongse); // kuai zheng qi
				}
			} // overpain
		}

		k0 = k1; // k0 is count for diff point.
		zb[a][b][2] = 0; // return the breath to zero.
		if (shoushu % 2 == 1) {
			ktb += ktz;
		} else {
			ktw += ktz; // 将局部提子计入

		}
		if ((a + 1) <= 18 && zb[a + 1][b][0] == 0) { // 2.1the breath of blank
			// 考虑相邻四点是否为气
			k2++; // 气点计数
			u[k0 + k2] = a + 1;
			v[k0 + k2] = b; // 198
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == 0) { // 2.2
			k2++;
			u[k0 + k2] = a - 1;
			v[k0 + k2] = b;
		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == 0) { // 2.3
			k2++;
			u[k0 + k2] = a;
			v[k0 + k2] = b + 1;
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == 0) { // 2.4
			k2++;
			u[k0 + k2] = a;
			v[k0 + k2] = b - 1;
		}

		k0 += k2; // k0 is the total points of diff and blank.
		// k0为异色点和气点的总数
		dang = k2; // 点四邻的气
		if ((a + 1) <= 18 && zb[a + 1][b][0] == tongse) { // 3.1
			k3++; // 同色点计数
			kin1 = zb[a + 1][b][3];
			if (kin1 == 0) { // 独立点
				kss++; // same color single point.独立点计数
				dang += zb[a + 1][b][2];
				dang--; // current point close one breath of surr point.
				u[k0 + kss] = a + 1; // u[0] not used
				v[k0 + kss] = b; // deal with single point.
			} else { // 231成块点
				dang += kuai[kin1][0][0];
				dang--;
				u[4 - ks] = a + 1; // deal with block.
				v[4 - ks] = b;
				ks++;
				k[ks] = kin1; //
			}

		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == tongse) { // 3.2
			k3++;
			kin2 = zb[a - 1][b][3];
			if (kin2 == 0) {
				kss++;
				dang += zb[a - 1][b][2];
				dang--;
				u[k0 + kss] = a - 1;
				v[k0 + kss] = b;
			} else if (kin2 != kin1) {
				dang += kuai[kin2][0][0];
				dang--;
				u[4 - ks] = a - 1;
				v[4 - ks] = b;
				ks++;
				k[ks] = kin2;
			} // ks biao shi you ji ge bu tong de kuai shu

		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == tongse) { // 3.3
			k3++;
			kin3 = zb[a][b + 1][3];
			if (kin3 == 0) {
				kss++;
				dang += zb[a][b + 1][2]; // 264
				dang--;
				u[k0 + kss] = a;
				v[k0 + kss] = b + 1;
			} else if (kin3 != kin2 && kin3 != kin1) {
				dang += kuai[kin3][0][0];
				dang--;
				u[4 - ks] = a;
				v[4 - ks] = b + 1;
				ks++;
				k[ks] = kin3;
			}

		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == tongse) { // 3.4
			k3++;
			kin4 = zb[a][b - 1][3];
			if (kin4 == 0) {
				kss++;
				dang += zb[a][b - 1][2];
				dang--;
				u[k0 + kss] = a;
				v[k0 + kss] = b - 1; // kss is single point.
			} else if (kin4 != kin3 && kin4 != kin2 && kin4 != kin1) {
				dang += kuai[kin4][0][0];
				dang--;
				u[4 - ks] = a;
				v[4 - ks] = b - 1;
				ks++;
				k[ks] = kin4; // ks is block.
			}

		} // 297
		if (dang > 0) { // dang可能大于真正的气数,但不可能为0.
			ktm = -1; // 原来的禁着点实效,因为已经寻劫
			ktn = -1;
		}
		if (dang == 0) { // ?仅仅考虑了单点,如果是块呢?没关系,都是虚的计算.
			// showStatus("//this point is prohibited,try again!");
			zzq(a, b, yise); // 相当于落子和提子一步完成,本函数完成提子
			hui[shoushu][25] = -1;
			hui[shoushu][26] = -1;
			shoushu--;
			zb[a][b][0] = 0;
			return;
		} // 不允许自杀
			// showStatus("qing="+dang+a+b);
		if (k3 == 0) { // 4.1 no same color point surround没有同色邻点
			if (log.isDebugEnabled())
				log.debug("//k3=0");
			zb[a][b][2] = dang;
			if (dang == 1 && ktz == 1) { // 考虑劫
				ktm = u[linzishu]; // 因为先处理异色子,再空白点,又无同色点.
				ktn = v[linzishu]; // 必为最后一点?如果是角上的劫呢?由4改为linzishu
			} // not conform to so. en.

			return;
		}
		if (ks == 0) { // 4.2 only single point surr.有同色点,但都为独立点
			if (log.isDebugEnabled())
				log.debug("//ks=0");
			gq = 0;
			for (i = 1; i <= kss; i++) { // 4.1 deal surr point处理相邻独立点
				hui[shoushu][12 + i * 2 - 1] = u[k0 + i]; // 记录合并成块的独立点
				hui[shoushu][12 + i * 2] = v[k0 + i]; // 从13到20
				for (j = 1; j <= (kss - i); j++) { // 计算点之间的共气
					gq += dd(u[k0 + i], v[k0 + i], u[k0 + i + j], v[k0 + i + j]);
				}
			}
			zb[a][b][2] = dang - gq;
			// zb[a][b][0]=tongse;

			zb[a][b][3] = ++ki; // count from first block
			hui[shoushu][0] = ki; // 记录所成块的索引
			kuai[ki][0][0] = zb[a][b][2];
			kuai[ki][0][1] = k3 + 1;
			kuai[ki][k3 + 1][0] = a; // 最后一点为a,b
			kuai[ki][k3 + 1][1] = b;
			for (i = 1; i <= k3; i++) {
				m = u[k0 + i];
				n = v[k0 + i];
				kuai[ki][i][0] = m;
				kuai[ki][i][1] = n;
				zb[m][n][2] = zb[a][b][2];
				zb[m][n][3] = ki;
			}
			/*
			 * if(zb[a][b][2]==0){ ktm=-1; ktn=-1; kzq(ki,yise); }
			 */
			// ci chu zi ti yi kuai
			// ci shi de de gong qi jin jin she ji dian
			// hen hao chu li.jian qu gong qi ji ke . jian qu gong qi
		}
		if (ks > 0) { // 相邻有块
			if (log.isDebugEnabled())
				log.debug("//ks>0");
			ki++;
			hui[shoushu][0] = ki;
			kuai[ki][0][1] = 1; // 建立临时块
			kuai[ki][1][0] = a; // a,b存在首位
			kuai[ki][1][1] = b;
			zb[a][b][3] = ki;
			for (i = 1; i <= kss; i++) { // 330
				hui[shoushu][12 + i * 2 - 1] = u[k0 + i];
				hui[shoushu][12 + i * 2] = v[k0 + i];
				dkhb(u[k0 + i], v[k0 + i], ki); // 相邻点并入临时块
			}
			// dkhb(a,b,k[1]);
			for (j = 1; j <= ks; j++) {
				hui[shoushu][20 + j] = k[j];
				kkhb(ki, k[j]); // not deal with breath块块合并,气尚未处理.
			}
			// hui[shoushu][0]=ki;
			// zb[a][b][2]=tongse;
			// kuai[k[1]][0][0]=zb[a][b][2];//? need deal with breath.
			dang = jskq(ki);
			kdq(ki, dang);
			/*
			 * if(dang==0){ hui[shoushu][0]=ki; kzq(ki,yise); ktm=-1; ktn=-1;
			 * hui[shoushu][0]=ki; // return; }
			 */
		}
	}

	public void paint(Graphics g) {
		int tongse;
		int xun;
		int m, n;
		int kin1;
		int p;
		int i = 0, j = 0; // the count for three kinds of point.
		// if(log.isDebugEnabled()) log.debug("//come into method paint");
		if (fzhudong == true) {
			if (fhuiqi == true) {
				m = huitemp[0][0];
				n = huitemp[0][1];
				huitemp[0][0] = -1;
				huitemp[0][1] = -1;
				xz(g, m, n);
				i = 1;
				while (huitemp[i][0] >= 0) {
					m = huitemp[i][0];
					n = huitemp[i][1];
					huitemp[i][0] = -1;
					huitemp[i][1] = -1;
					tongse = shoushu % 2 + 1;
					if (tongse == 1) {
						g.setColor(Color.white);
					} else {
						g.setColor(Color.black);
					}
					g.fillOval(28 * m + 4, 28 * n + 4, 28, 28);
					i++;
				}
				fhuiqi = false;
			}
			if (fqianjin == true) {
				m = hui[shoushu][25];
				n = hui[shoushu][26];
				tongse = (shoushu + 1) % 2 + 1;
				if (tongse == 1) {
					g.setColor(Color.black);
				} else {
					g.setColor(Color.white);
				}
				if (log.isDebugEnabled())
					log.debug("color=" + tongse);
				g.fillOval(28 * m + 4, 28 * n + 4, 28, 28);
				for (i = 1; i <= 4; i++) {
					if (hui[shoushu][2 * i - 1] < 0) {
						break;
					} else {
						m = hui[shoushu][2 * i - 1];
						n = hui[shoushu][2 * i];
						xz(g, m, n);
					}
				}
				for (i = 1; i <= 4; i++) {
					kin1 = hui[shoushu][8 + i];
					if (kin1 < 0) {
						break;
					} else {
						p = kuai[kin1][0][1];
						for (j = 1; j <= p; j++) {

							m = kuai[kin1][j][0];
							n = kuai[kin1][j][1];
							xz(g, m, n);
						}
					} // else
				} // for
				fqianjin = false;
			} // else
			fzhudong = false;
		} // if
		else {
			g.setColor(Color.white);
			g.fillRect(540, 0, 200, 540);
			g.setColor(Color.orange);
			g.fillRect(0, 0, 540, 540); // 33
			g.setColor(Color.black);
			for (i = 0; i < 19; i++) {
				g.drawLine(18, 18 + 28 * i, 522, 18 + 28 * i); // hor
				g.drawLine(18 + 28 * i, 18, 18 + 28 * i, 522); // ver
			}
			// if(log.isDebugEnabled())
			// log.debug("// paint the ver and hor line.");
			for (i = 0; i < 3; i++) {
				for (j = 0; j < 3; j++) {
					g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
				}
			}
			// if(log.isDebugEnabled()) log.debug("//paint the star point.");
			for (i = 0; i <= 18; i++) {
				for (j = 0; j <= 18; j++) {
					if (zb[i][j][0] == 1) {
						g.setColor(Color.black);
						g.fillOval(28 * i + 4, 28 * j + 4, 28, 28);
						// if(log.isDebugEnabled())
						// log.debug("//paint the black point.");
					} else if (zb[i][j][0] == 2) {
						g.setColor(Color.white);
						g.fillOval(28 * i + 4, 28 * j + 4, 28, 28);
						// if(log.isDebugEnabled())
						// log.debug("//paint the white point.");
					}
				}
			} // paint all the points owned by black and white.
		} // else
		g.setColor(Color.black);
		for (i = 10; i < 19; i++) {
			for (j = 0; j < 10; j++) {
				// g.drawString(""+zb[j][i][2],545+15*j,15*i+50);
				// g.drawString(""+zb[j][i][0],545+15*j,15*i+210);
				if (zb[j][i][2] > 0) {
					if (log.isDebugEnabled())
						log.debug("zb[" + j + "][" + i + "][2]=" + zb[j][i][2]);
				}
			}
		}

		for (xun = linss + 1; xun <= shoushu; xun++) {
			tongse = (1 + xun) % 2 + 1;
			m = hui[xun][25];
			n = hui[xun][26];
			if (m >= 0 && n >= 0) {
				if (zb[m][n][2] > 0) {
					if (tongse == 1) {
						g.setColor(Color.black);
					} else {
						g.setColor(Color.white);
					}
					g.fillOval(28 * m + 4, 28 * n + 4, 28, 28); // bixu fu gai
																// ,fou ze shu
																// zi chong he
					if (tongse == 2) {
						g.setColor(Color.black);
					} else {
						g.setColor(Color.white);
					}
					if (xun >= linss + 10) {
						g.drawString("" + (xun - linss), 28 * m + 11,
								28 * n + 23);
					} else {
						g.drawString("" + (xun - linss), 28 * m + 14,
								28 * n + 23);
					}
				}
			}
		}
		// if(log.isDebugEnabled())
		// log.debug("//come into the end of method paint");
	}

	public boolean action(Event e, Object what) {
		int kin1 = 0, i = 0, j = 0, m = 0, n = 0, p = 0;
		int yise = 0; // tong se
		int tongse = 0; // yi se bei ti
		int lins = 0;
		String s;
		if (e.target == pass) {
			shoushu++;
			hui[shoushu][25] = -1;
			hui[shoushu][26] = -1;
			return true;
		}
		if (e.target == wenjian) {

			try {
				DataInputStream fnin = new DataInputStream(
						new BufferedInputStream(new FileInputStream(
								"F:\\JBuilder项目\\演示死活题\\classes\\" + "qipushu")));
				ju = fnin.readInt();
				if (log.isDebugEnabled())
					log.debug("ju=" + ju);
				ju++;
				fnin.close();
				DataOutputStream fnout = new DataOutputStream(
						new BufferedOutputStream(new FileOutputStream(
								"H:\\JBuilder6\\演示死活题\\classes\\" + "qipushu")));
				fnout.writeInt(ju);
				fnout.close();
			} catch (IOException ex) {
				if (log.isDebugEnabled())
					log.debug("the reading of file  meet some trouble!");
				if (log.isDebugEnabled())
					log.debug("Exception" + ex.toString());
			}

			try {
				out = new DataOutputStream(new BufferedOutputStream(
						new FileOutputStream("H:\\JBuilder6\\演示死活题\\classes\\"
								+ "qipu" + ju)));
				if (log.isDebugEnabled())
					log.debug("bhs=" + bhs);
				for (i = 0; i < bhs; i++) {
					p = bianhua[i][0][0];
					if (log.isDebugEnabled())
						log.debug("shoushu=" + p);
					for (j = 0; j <= p; j++) {
						out.writeByte((byte) bianhua[i][j][0]);
						out.writeByte((byte) bianhua[i][j][1]);
					}
					out.writeByte((byte) 90);
				}
				out.writeByte((byte) 95);
				// if(log.isDebugEnabled()) log.debug("lins="+shoushu);
				for (i = 0; i < jss; i++) {
					out.writeByte((byte) jsweizhi[i][0]);
					out.writeByte((byte) jsweizhi[i][1]);
				}
				out.writeByte((byte) 100);
				out.close();
			}
			/*
			 * while(lins<shoushu){ lins++; out.writeByte((byte)hui[lins][25]);
			 * out.writeByte((byte)hui[lins][26]); if(log.isDebugEnabled())
			 * log.debug("a="+hui[lins][25]); } out.writeByte(100); out.close();
			 * }
			 */
			catch (IOException ex) {
				if (log.isDebugEnabled())
					log.debug("the output meet some trouble!");
				if (log.isDebugEnabled())
					log.debug("Exception" + ex.toString());
			}
			/*
			 * for(i=1;i<=lins;i++){ shoushu++; a=hui[shoushu][25];
			 * b=hui[shoushu][26]; System.out.print("a="+a);
			 * System.out.print("b="+b); System.out.print("lins="+i);
			 * cqpchuli(); } repaint(); return true;
			 */
			try {
				DataOutputStream out2 = new DataOutputStream(
						new BufferedOutputStream(new FileOutputStream(
								"H:\\JBuilder6\\演示死活题\\classes\\" + "jieshuo"
										+ ju)));

				jsout = new BufferedWriter(new OutputStreamWriter(out2));
				// if(log.isDebugEnabled()) log.debug("jsout="+jsout);
				for (i = 0; i < jss; i++) {
					s = tempstr[i] + "\n";
					if (s == null) {
						if (log.isDebugEnabled())
							log.debug("eror");
					}
					jsout.write(s, 0, s.length());

				}
				jsout.close();
				out2.close();

			} catch (IOException ex) {
				if (log.isDebugEnabled())
					log.debug("the output meet some trouble!");
				if (log.isDebugEnabled())
					log.debug("Exception" + ex.toString());
			}
			wenjian.setEnabled(false);
		}
		if (e.target == conglai) {
			while (shoushu != linss) {
				clhuiqi();
			}
			conglai.setEnabled(false);
			huiqi.setEnabled(false);
			store.setEnabled(false);
			for (i = 0; i < 50; i++) {
				huitemp[i][0] = -1;
				huitemp[i][1] = -1;
			}
			repaint();
			return true;
		}

		if (e.target == jiarujieshuo) {
			s = jieshuo.getText();
			if (log.isDebugEnabled())
				log.debug("jshuo[" + bhs + "][" + (shoushu - linss) + "]=\""
						+ s + "\";");
			jsweizhi[jss][0] = bhs;
			jsweizhi[jss][1] = shoushu - linss;
			tempstr[jss] = s;
			jss++;
			jieshuo.setText("");
		}
		if (e.target == huiqi) {
			if (shoushu > linss) {
				fzhudong = true;
				fhuiqi = true;
				clhuiqi();
				if (shoushu == linss) {
					huiqi.setEnabled(false);
					conglai.setEnabled(false);
					store.setEnabled(false);
				}
				repaint();
			} else {
				if (log.isDebugEnabled())
					log.debug("this is original ju mian");
				// huiqi.disable();
			}
			return true;
		}
		if (e.target == store) { // if(log.isDebugEnabled())
									// log.debug("come into method store!");
			if (bhs == 0) {
				// if(log.isDebugEnabled()) log.debug("lins="+shoushu);
				linss = shoushu; // que ding yuan zhi jumian de shou shu
				for (i = 1; i <= shoushu; i++) {
					m = hui[i][25];
					n = hui[i][26];
					if (log.isDebugEnabled())
						log.debug("bianhua[0][" + i + "][0]=" + m + ";");
					if (log.isDebugEnabled())
						log.debug("bianhua[0][" + i + "][1]=" + n + ";");
					bianhua[bhs][i][0] = m;
					bianhua[bhs][i][1] = n;
				}
				if (log.isDebugEnabled())
					log.debug("bianhua[" + bhs + "][0][0]=" + shoushu + ";");
				bianhua[bhs][0][0] = linss;
				// bianhua[bhs][0][1]=bhs;
				// bhs++;
				pass.setEnabled(false);
			} else {
				for (i = 1; i <= shoushu - linss; i++) {
					m = hui[i + linss][25];
					n = hui[i + linss][26];
					if (log.isDebugEnabled())
						log.debug("bianhua[" + bhs + "][" + i + "][0]=" + m
								+ ";");
					if (log.isDebugEnabled())
						log.debug("bianhua[" + bhs + "][" + i + "][1]=" + n
								+ ";");
					bianhua[bhs][i][0] = m;
					bianhua[bhs][i][1] = n;
				}
				if (log.isDebugEnabled())
					log.debug("bianhua[" + bhs + "][0][0]=" + (shoushu - linss)
							+ ";");
				bianhua[bhs][0][0] = shoushu - linss;
			}
			if (log.isDebugEnabled())
				log.debug("bianhua[0][0][1]=" + bhs + ";");
			bianhua[0][0][1] = bhs;
			bhs += 1;
			store.setEnabled(false);
			wenjian.setEnabled(true);
			repaint();

		}

		return true;
	}

	void chushihua() {
		int i, j, k;
		a = -1;
		b = -1;
		shoushu = 0;
		ktm = -1;
		ktn = -1;
		ki = 0;
		ktw = 0;
		ktb = 0;
		for (i = 0; i <= 18; i++) {
			for (j = 0; j <= 18; j++) {
				zb[i][j][0] = 0;
				zb[i][j][2] = 0;
				zb[i][j][3] = 0;
			}
		}
		for (i = 0; i < 400; i++) {
			for (j = 0; j < 27; j++) {
				hui[i][j] = 0;
			}
			for (j = 1; j < 9; j++) {
				hui[i][j] = -1;
			}
			for (j = 13; j < 21; j++) {
				hui[i][j] = -1;
			}
			hui[i][25] = -1;
			hui[i][26] = -1;
		}
		for (k = 1; k < 100; k++) {
			kuai[k][0][0] = 0;
			kuai[k][0][1] = 0;
			for (i = 1; i < 50; i++) {
				kuai[k][i][0] = 0;
				kuai[k][i][1] = 0;
			}
		}

	}

	int jszq(int a, int b) {
		int dang = 0;
		if ((a + 1) <= 18 && zb[a + 1][b][0] == 0) { // 2.1the breath of blank
			dang++;
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == 0) { // 2.2
			dang++;
		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == 0) { // 2.3
			dang++;
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == 0) { // 2.4
			dang++;
		}
		return dang;

	}

	// one point was eaten
	void zzq(int a, int b, int tiao) { // function 6.1
		int c1 = 0, c2 = 0, c3 = 0, c4 = 0, i;
		if ((a + 1) <= 18 && zb[a + 1][b][0] == tiao) {
			c1 = zb[a + 1][b][3];
			if (c1 == 0) {
				zb[a + 1][b][2] += 1;
			} else {
				int qi;
				qi = kuai[c1][0][0] += 1;
				kdq(c1, qi);
			}
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == tiao) {
			c2 = zb[a - 1][b][3];
			if (c2 == 0) {
				zb[a - 1][b][2] += 1;
			} else if (c2 != c1) {
				int qi;
				qi = kuai[c2][0][0] += 1;
				kdq(c2, qi);
			}
		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == tiao) {
			c3 = zb[a][b + 1][3];
			if (c3 == 0) {
				zb[a][b + 1][2] += 1;
			} else if (c3 != c2 && c3 != c1) {
				int qi;
				qi = kuai[c3][0][0] += 1;
				kdq(c3, qi);
			} // 400
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == tiao) {
			c4 = zb[a][b - 1][3];
			if (c4 == 0) {
				zb[a][b - 1][2] += 1;
			} else if (c4 != c3 && c4 != c2 && c4 != c1) {
				int qi;
				qi = kuai[c4][0][0] += 1;
				kdq(c4, qi);
			}
		}
		zb[a][b][0] = 0;
		zb[a][b][2] = 0;
		zb[a][b][3] = 0;
	}

	public void kzq(int r, int tiao) { // 6.2 tong se kuai bei ti
		int n = 0; // the same color block is eaten
		int p = 0, q = 0;
		n = kuai[r][0][1];
		for (int i = 1; i <= n; i++) {
			p = kuai[r][i][0];
			q = kuai[r][i][1];
			zzq(p, q, tiao);
			// kuai[r][i][0]=0;
			// kuai[r][i][1]=0;
		}
		kuai[r][0][0] = 0;
		// kuai[r][0][1]=0;
	}

	void zjq(int a, int b, int tiao) { // function 6.1
		int c1 = 0, c2 = 0, c3 = 0, c4 = 0, i;
		if ((a + 1) <= 18 && zb[a + 1][b][0] == tiao) {
			c1 = zb[a + 1][b][3];
			if (c1 == 0) {
				zb[a + 1][b][2] -= 1;
			} else {
				int qi;
				qi = kuai[c1][0][0] -= 1;
				kdq(c1, qi);
			}
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == tiao) {
			c2 = zb[a - 1][b][3];
			if (c2 == 0) {
				zb[a - 1][b][2] -= 1;
			} else if (c2 != c1) {
				int qi;
				qi = kuai[c2][0][0] -= 1;
				kdq(c2, qi);
			}
		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == tiao) {
			c3 = zb[a][b + 1][3];
			if (c3 == 0) {
				zb[a][b + 1][2] -= 1;
			} else if (c3 != c2 && c3 != c1) {
				int qi;
				qi = kuai[c3][0][0] -= 1;
				kdq(c3, qi);
			} // 400
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == tiao) {
			c4 = zb[a][b - 1][3];
			if (c4 == 0) {
				zb[a][b - 1][2] -= 1;
			} else if (c4 != c3 && c4 != c2 && c4 != c1) {
				int qi;
				qi = kuai[c4][0][0] -= 1;
				kdq(c4, qi);
			}
		}

	}

	public void kjq(int r, int tiao) { // 6.2 tong se kuai bei ti
		int n = 0; // the same color block is eaten
		int p = 0, q = 0;
		n = kuai[r][0][1];
		for (int i = 1; i <= n; i++) {
			p = kuai[r][i][0];
			q = kuai[r][i][1];
			zjq(p, q, tiao);
			// kuai[r][i][0]=0;
			// kuai[r][i][1]=0;
		}
		kuai[r][0][0] = 1;
		// kuai[r][0][1]=0;
	}

	public int dd(int a, int b, int c, int d) { // 7.1diang diang gong qi
		int gq = 0; // consider four points only.
		if (zb[a][d][0] == 0) {
			gq++;
		}
		if (zb[c][b][0] == 0) {
			gq++;
		}
		return gq;
	}

	// 6.7diang kuai he bing
	public void dkhb(int p, int q, int r) { // 8.1
		int ss = kuai[r][0][1] + 1;
		kuai[r][ss][0] = p;
		kuai[r][ss][1] = q;
		kuai[r][0][1] = ss;
		zb[p][q][3] = r;
		// zb[p][q][2]=zb[a][b][2];
	}

	// 6.8 ss1 shi zu yao kuai!
	public void kkhb(int r1, int r2) { // 8.2
		int ss1 = kuai[r1][0][1];
		int ss2 = kuai[r2][0][1];
		int m = 0, n = 0;
		for (int i = 1; i <= ss2; i++) {
			m = 0;
			n = 0; // 476
			m = kuai[r2][i][0];
			n = kuai[r2][i][1];
			zb[m][n][3] = r1;
			// zb[m][n][2]=zb[a][b][2];

			// kuai[r2][i][0]=0;
			// kuai[r2][i][1]=0;
			kuai[r1][ss1 + i][0] = m;
			kuai[r1][ss1 + i][1] = n;
		}

		/*
		 * for (int i=1;i<=ss1;i++){ m=0; n=0; //476 m=kuai[r1][i][0];
		 * n=kuai[r1][i][1]; zb[m][n][2]=zb[a][b][2]; }leave breath decision
		 */

		kuai[r1][0][1] = ss1 + ss2;
		// kuai[r2][0][1]=0;
		// kuai[r2][0][0]=0;
	}

	public int xl(int a, int b, int r) { // 9.1dian kuai xiang ling
		int gq = 0; // 6.12
		if ((a + 1) <= 18 && zb[a + 1][b][3] == r) {
			gq++;
		}
		if ((a - 1) >= 0 && zb[a - 1][b][3] == r) {
			gq++;
		}
		if ((b + 1) <= 18 && zb[a][b + 1][3] == r) {
			gq++;
		}
		if ((b - 1) >= 0 && zb[a][b - 1][3] == r) {
			gq++;
		}
		return gq;
	}

	// 10.1ji suan kuai qi.
	public int jskq(int r2) {
		int gq = 0; // the breath of the block
		int k1 = 0; // xiang ling cheng du wei 2;
		int k2 = 0; // xiang ling cheng du wei 3;
		int k3 = 0; // xiang ling cheng du wei 4;
		int a = 0, b = 0, p = 0, q = 0;
		q = kuai[r2][0][1];
		for (int i = 1; i <= q; i++) {
			a = kuai[r2][i][0];
			b = kuai[r2][i][1];
			if ((a + 1) <= 18 && zb[a + 1][b][0] == 0) {
				gq++;
				if (xl((a + 1), b, r2) == 2) {
					k1++;
				}
				if (xl((a + 1), b, r2) == 3) {
					k2++;
				}
				if (xl((a + 1), b, r2) == 4) {
					k3++;
				}
			}
			if ((a - 1) >= 0 && zb[a - 1][b][0] == 0) {
				gq++;
				if (xl((a - 1), b, r2) == 2) {
					k1++;
				}
				if (xl((a - 1), b, r2) == 3) {
					k2++;
				}
				if (xl((a - 1), b, r2) == 4) {
					k3++;
				}
			}
			if ((b + 1) <= 18 && zb[a][b + 1][0] == 0) {
				gq++;
				if (xl(a, (b + 1), r2) == 2) {
					k1++;
				}
				if (xl(a, (b + 1), r2) == 3) {
					k2++;
				}
				if (xl(a, (b + 1), r2) == 4) {
					k3++;
				}
			} // 573
			if ((b - 1) >= 0 && zb[a][b - 1][0] == 0) {
				gq++;
				if (xl(a, (b - 1), r2) == 2) {
					k1++;
				}
				if (xl(a, (b - 1), r2) == 3) {
					k2++;
				}
				if (xl(a, (b - 1), r2) == 4) {
					k3++;
				}
			}
		} // 574
		gq -= k1 / 2;
		gq -= 2 * k2 / 3;
		gq -= 3 * k3 / 4;
		return gq;
	}

	public void kdq(int kin, int a) { // 11.1 kuai ding qi
		int m = 0, n = 0, p = 0;
		p = kuai[kin][0][1];
		for (int i = 1; i <= p; i++) {
			m = kuai[kin][i][0];
			n = kuai[kin][i][1];
			zb[m][n][2] = a;
		}
		kuai[kin][0][0] = a;
	}

	public void xz(Graphics g, int a, int b) { // 12.1
		g.setColor(Color.orange); // deal with image only.
		g.fillOval(28 * a + 4, 28 * b + 4, 28, 28);
		g.setColor(Color.black);
		if (a == 0) {
			g.drawLine(18, 28 * b + 18, 32, 28 * b + 18);
		} else if (a == 18) {
			g.drawLine(28 * a + 4, 28 * b + 18, 28 * a + 18, 28 * b + 18);
		} else {
			g.drawLine(28 * a + 4, 28 * b + 18, 28 * a + 32, 28 * b + 18);
		}
		if (b == 0) {
			g.drawLine(28 * a + 18, 18, 28 * a + 18, 32);
		} else if (b == 18) {
			g.drawLine(28 * a + 18, 28 * b + 4, 28 * a + 18, 28 * b + 18);
		} else {
			g.drawLine(28 * a + 18, 28 * b + 4, 28 * a + 18, 28 * b + 32);
		}
		if ((a - 3) % 6 == 0 && (b - 3) % 6 == 0) {
			g.fillOval(28 * a + 15, 28 * b + 15, 6, 6);
		} // draw the star point if necessary.
			// zb[a][b][0]=0;
			// zb[a][b][3]=0;
			// zb[a][b][2]=0;
	}

	public void clhuiqi() {
		int p = 0;
		int yise = 0;
		int tdzs = 0;
		int tongse = 0; // yise is diff color.and 2 same.
		int k0 = 0, k1 = 0, k2 = 0, k3 = 0, i = 0, j = 0; // the count for three
															// kinds of point.
		// int dang=0,ktz=0;//,kq=0,p=0,q=0; //dang is breath of block.
		int ks = 0, kss = 0; // ks is count for block,kss for single point
		int kin, kin1 = 0, m = 0, n = 0; // the block index.

		// count for single pointeaten andblock eaten.

		tongse = (shoushu + 1) % 2 + 1; // tong se
		yise = shoushu % 2 + 1;
		m = hui[shoushu][25];
		hui[shoushu][25] = -1;
		n = hui[shoushu][26];
		hui[shoushu][26] = -1;
		if (m < 0 || n < 0) {
			shoushu--;
			return;
		}
		huitemp[0][0] = m;
		huitemp[0][1] = n;
		fhuiqi = true;
		zzq(m, n, yise);
		if (log.isDebugEnabled())
			log.debug("//regret step:" + shoushu);
		if (log.isDebugEnabled())
			log.debug("a=" + m);
		if (log.isDebugEnabled())
			log.debug("b=" + n);

		kin = hui[shoushu][0];
		if (kin > 0) {
			for (i = 0; i < 50; i++) {
				kuai[kin][i][0] = 0;
				kuai[kin][i][1] = 0;
			}
			ki = kin;
			for (i = 1; i <= 4; i++) {
				if (hui[shoushu][2 * i + 12 - 1] < 0) {
					break;
				} else {
					m = hui[shoushu][12 + 2 * i - 1];
					n = hui[shoushu][12 + 2 * i];
					hui[shoushu][12 + 2 * i - 1] = -1;
					hui[shoushu][12 + 2 * i] = -1;
					zb[m][n][3] = 0;
					zb[m][n][0] = tongse; // fang wei bian cheng
					zb[m][n][2] = jszq(m, n);
					if (log.isDebugEnabled())
						log.debug("//ji suan zi de qi");
				}
			} // deal with 3 sub
			for (i = 1; i <= 4; i++) {
				kin1 = hui[shoushu][20 + i];
				hui[shoushu][20 + i] = 0;
				if (kin1 == 0) {
					break;
				} else {
					p = kuai[kin1][0][1];
					for (j = 1; j <= p; j++) {
						m = kuai[kin1][j][0];
						n = kuai[kin1][j][1];
						zb[m][n][3] = kin1;
						// zb[m][n][0]=tongse;
						zb[m][n][2] = kuai[kin1][0][0];
					}
				}
			} // for
		}
		for (i = 1; i <= 4; i++) {
			if (hui[shoushu][2 * i - 1] < 0) {
				break;
			} else {
				m = hui[shoushu][2 * i - 1];
				n = hui[shoushu][2 * i];
				hui[shoushu][2 * i - 1] = -1;
				hui[shoushu][2 * i] = -1;
				huitemp[i][0] = m;
				huitemp[i][1] = n;
				tdzs = i;
				zb[m][n][0] = yise;
				zb[m][n][2] = 1;
				zb[m][n][3] = 0;
				zjq(m, n, tongse);
				System.out.print("hui fu bei ti de zi");
				System.out.print("a=" + m);
				System.out.print("b=" + n);
			}
		}

		for (i = 1; i <= 4; i++) {
			if (hui[shoushu][8 + i] <= 0) {
				break;
			} else {
				kin1 = hui[shoushu][8 + i];
				hui[shoushu][8 + i] = 0;
				kdq(kin1, 1);
				kjq(kin1, tongse);
				p = kuai[kin1][0][1];
				for (j = 1; j <= p; j++) {
					m = kuai[kin1][j][0];
					n = kuai[kin1][j][1];
					huitemp[j + tdzs][0] = m;
					huitemp[j + tdzs][1] = n;
					zb[m][n][0] = yise;
					zb[m][n][3] = kin1;

				}
				tdzs += p;
			}
		}
		shoushu--;
	} // if

}
/*
 * class StarterCombinedFrame extends Frame{ public StarterCombinedFrame(String
 * frametitle){ super(frametitle); Untitled1 applet=new Untitled1();
 * applet.start(); add(applet,"Center"); addWindowListener(new WindowAdapter(){
 * public void windowClosing(WindowEvent event){ dispose(); System.exit(0); }
 * }); } }
 */
