package untitled7;

/*
 * <p>Title:围棋死活题小程序：不能运行 </p>
 * <p>Description: 用于死活题训练:shihuo 基础上加入布局的代码</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SE</p>
 * @author wueddie
 * @version 1.0
 * 感觉上java对于围棋程序未必合适,因为与一般应用程序不同,不容易提出对象
 */
import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.TextArea;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import untitled10.QiKuai1;

//import java.net.*;
public class GoBoard2 extends Applet {
	private static final Logger log = Logger.getLogger(QiKuai1.class);
	boolean fdangbu = false;// hui qi shi shi fou dang bu.
	boolean fshuzi = false;// shi fou biao chu shu zi.
	boolean fbuhelidian = false;// zhi zuo shi wei yu kao lv de dian.
	boolean ftishi = false;// shi fou ti shi .
	boolean fzhudong = false;// shi fou xu yao quan bu zhong hua
	boolean fdonghua = false;
	int czhengjie = 0;// zheng jie de biao zhi?
	int linss;// 原始局面的手数。
	int chuiqi = 0;// hui qi de bu shu.<3
	int cqianjin = 0;// xiang qian de bu shu.<3
	int[][][] huitemp = new int[2][50][2];// yong yu paint de xie tiao
	int tsa = -1, tsb = -1;// ti shi de wei zhi.yong yu shang chu ti shi biao
							// zhi

	Tree gotree = new Tree();// cun chu bian hua de dong tai jie gou.
	TreeNode gotemp;// dan qian jie dian.
	TreeNode old = null;// 当前节点的父节点。

	boolean isStandalone = false;
	int ki = 0;// dangqian kuaishu
	int ktm = -1, ktn = -1;// position for points eaten.
	int ktb = 0, ktw = 0;// white point eaten.
	int shoushu = 0;// dang qian shou shu,caution:it increase before deal
	int a = -1;// a is the row subscript of the point.
	int b = -1;// b is the column subscript of the point
	int hda = -1, hdb = -1;
	// int hdse=0;
	int[][][] zb = new int[19][19][4];// 0:state;2:breath;3:blockindex
	int[][][] kuai = new int[100][50][2];// mei yi kuai de ge zi zuo biao
	int[][] hui = new int[400][29];// 0=zi ti yi kuai,1~8four single point
	// eaten,9~12 kuai suo ying of fou block eaten.13~24is same ,25,26a,b

	int qpdx = 28;// qipangdaxiao;
	int qpbj = qpdx / 2;//
	int xbj = 3;// xing dian de ban jing.
	int tyzj = 28;// tuo yuan zhi jing;
	int bjjx = 4;// qi pang bian jie jian xi.
	// tu xing yong hu jie mian
	Button tishi = new Button("提示");
	Button huiqi = new Button("悔棋");
	Button conglai = new Button("重做");
	Button yanshi = new Button("演示");
	TextArea jieshuo = new TextArea(5, 10);

	public void zhengzi() {

	}

	public int jieduan() {
		return 0;// dummy
	}

	public int buju() {// 布局
		int jiao[][] = new int[5][2];
		jiao[0][0] = 2;
		jiao[0][1] = 2;
		jiao[1][0] = 3;
		jiao[1][1] = 3;
		jiao[2][0] = 2;
		jiao[2][1] = 3;
		jiao[3][0] = 2;
		jiao[3][1] = 4;
		jiao[4][0] = 3;
		jiao[4][1] = 4;
		int dummy = 0;
		return dummy;

	}

	int[][] dingdian1 = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };// 每个点的直接相邻点
	int[][] dingdian2 = { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };// 每个点的肩冲点
	private int[][] jiao;

	public int jiejinfangshi(int m, int n) {// 接近方式:靠还是肩冲
		for (int i = 0; i < 4; i++) {
			int u = zb[m + dingdian1[i][0]][n + dingdian1[i][1]][0];
			if (u != 0)
				return 1;// 紧紧相邻
		}
		for (int i = 0; i < 4; i++) {
			int u = zb[m + dingdian2[i][0]][n + dingdian2[i][1]][0];
			if (u != 0)
				return 2;// 肩冲位置
		}
		return 3;
	}

	public boolean kecaiyi(int m, int n) {
		if (m < 2)
			return false;
		if (n < 2)
			return false;// 三线或三线以上
		if (zb[m][n][0] != 0)
			return false;
		if (jiejinfangshi(m, n) > 2)
			return true;
		return false;
	}

	public int chaiyigeshu(int m, int n) {// 计算拆一个数
		// 主要用于角部，应事前检查参数,m,n为落子点坐标。
		int chai = 0;
		if ((m < 2 || m > 3) & (n < 2 || n > 3)) {
			System.out.print("输入位置错误，太高或太低");
			return -1;
		}
		if (m > 2 & m < 3) {
			if (kecaiyi(m + 2, n))
				chai++;
			if (kecaiyi(m - 2, n))
				chai++;
		}
		if (n > 2 & n < 3) {
			if (kecaiyi(m, n + 2))
				chai++;
			if (kecaiyi(m, n - 2))
				chai++;
		}
		return chai;
	}

	public boolean iskongjiao(int m, int n) {
		// 对方不正常占角，是否使角不为空
		for (int i = 0; i < 8; i++) {
			int u = jiao[i][0];
			int v = jiao[i][1];
			if (chaiyigeshu(u, v) >= 2)
				return true;

		}
		return false;
	}

	public void init() {
		int i, j = -1;
		int p = 0;// bian hua de shu mu
		int pos;
		int[][][] bianhua = new int[50][100][2];// ge zhong bian hua de cun chu.
		String[][] jshuo = new String[50][100];// cun chu jie shuo.
		byte[][] jsweizhi = new byte[50][2];// jie shuo xianzhi zai 50 shou.
		byte[] qipubh = new byte[1000];
		String qipuwjm;
		String jieshuowjm;

		if (getParameter("qipangdaxiao") != null) {
			qpdx = Integer.parseInt(getParameter("qipangdaxiao"));
			qpbj = qpdx / 2;
			tyzj = (qpdx / 2) * 2;
		}
		if (getParameter("xingbanjing") != null)
			xbj = Integer.parseInt(getParameter("xingbanjing"));
		if (getParameter("bianjiejianxi") != null)
			bjjx = Integer.parseInt(getParameter("bianjiejianxi"));
		// if(log.isDebugEnabled()) log.debug("qipuwenjianming="+qipuwjm);
		qipuwjm = getParameter("qipuwenjianming");
		jieshuowjm = getParameter("jieshuowenjianming");
		// if(log.isDebugEnabled()) log.debug("jieshuowenjinaming="+jieshuowjm);
		try {
			URL base = this.getCodeBase();
			URL dbase = this.getDocumentBase();
			String sdbase = dbase.toString();
			int index = sdbase.lastIndexOf("/");
			sdbase = sdbase.substring(0, index + 1);
			if (log.isDebugEnabled())
				log.debug("CodeBase=" + base);
			if (log.isDebugEnabled())
				log.debug("documentBase=" + sdbase);
			URL urlbase = new URL(base + qipuwjm);
			// URL urlbase=new URL( sdbase+qipuwjm);
			URLConnection uconn = urlbase.openConnection();
			InputStream uc = uconn.getInputStream();
			uc.read(qipubh);
			// if(log.isDebugEnabled()) log.debug("ss="+1) ;
			boolean qipujieshu = false;
			// int [][]jsweizhi=new int[30][2];
			j = -1;
			for (i = 0; i < 500; i++) {
				j++;
				// if((int)bianhua[i]==-1) break;
				if (log.isDebugEnabled())
					log.debug("a=" + qipubh[i]);
				if (qipujieshu == true) {
					if (qipubh[i] == 100)
						break;
					jsweizhi[j][0] = qipubh[i];
					if (log.isDebugEnabled())
						log.debug("jieshuoweizhia=" + qipubh[i]);
					jsweizhi[j][1] = qipubh[++i];
					if (log.isDebugEnabled())
						log.debug("jieshuoweizhib=" + qipubh[i]);
					continue;
				}
				if (qipubh[i] == 100)
					break;
				else if (qipubh[i] == 90) {
					p++;// xia yi ge bian hua.
					j = -1;
					continue;
				} else if (qipubh[i] == 95) {
					qipujieshu = true;
					j = -1;

				} else {
					bianhua[p][j][0] = qipubh[i];
					bianhua[p][j][1] = qipubh[++i];
					if (log.isDebugEnabled())
						log.debug("b=" + qipubh[i]);
				}

			}
			uc.close();

			URL urlbasejs = new URL(base + jieshuowjm);
			URLConnection uconnjs = urlbasejs.openConnection();
			InputStream ucjs = uconnjs.getInputStream();
			DataInputStream js = new DataInputStream(new BufferedInputStream(
					ucjs));
			if (log.isDebugEnabled())
				log.debug("js=" + js);
			BufferedReader buffin = new BufferedReader(
					new InputStreamReader(js));
			String s = new String();
			String[] tempstr = new String[50];
			j = 0;
			while ((s = buffin.readLine()) != null) {
				// ta.append(s);
				// int c;

				jshuo[jsweizhi[j][0]][jsweizhi[j][1]] = s;
				tempstr[j] = s;
				j++;
				if (log.isDebugEnabled())
					log.debug(s);
			}
			buffin.close();

		} catch (Exception e) {
			e.printStackTrace();

		}
		gotree.insert(bianhua, jshuo);
		gotemp = gotree.getTreeNode();
		if (gotemp == null)
			if (log.isDebugEnabled())
				log.debug("gotemp is null");
		/*
		 * else{ while(gotemp!=null){ if(log.isDebugEnabled())
		 * log.debug("a="+gotemp.zba); if(log.isDebugEnabled())
		 * log.debug("b="+gotemp.zbb); gotemp=gotemp.left; }
		 * gotemp=gotree.getTreeNode(); }
		 */
		setLayout(null);
		add(tishi);
		add(huiqi);
		add(conglai);
		add(yanshi);
		add(jieshuo);
		pos = qpdx * 19 + 2 * bjjx + 10;
		huiqi.setBounds(pos, 60, 60, 26);
		conglai.setBounds(pos, 110, 60, 26);
		tishi.setBounds(pos + 90, 60, 60, 26);
		yanshi.setBounds(pos + 90, 110, 60, 26);
		jieshuo.setBounds(pos, 160, 150, 100);
		jieshuo.setText("\"提示\"按钮可以逐手给出提示\n\"重做\"按钮可以恢复到初始局面\n\"演示\"按钮可以演示正解\n如果走错，直接点另一点即可");
		huiqi.setEnabled(false);
		conglai.setEnabled(false);
		for (i = 1; i < 400; i++) {
			for (j = 1; j <= 8; j++) {
				hui[i][j] = -1;
			}
			for (j = 13; j <= 20; j++) {
				hui[i][j] = -1;
			}
			hui[i][25] = -1;
			hui[i][26] = -1;
			hui[i][27] = -1;// jin zhao dian.6.30
			hui[i][28] = -1;
		}
		for (i = 0; i < 2; i++) {
			for (j = 0; j < 50; j++) {
				huitemp[i][j][0] = -1;
				huitemp[i][j][1] = -1;
			}
		}
		linss = bianhua[0][0][0];
		for (i = 1; i <= linss; i++) {
			a = bianhua[0][i][0];
			b = bianhua[0][i][1];
			System.out.print("a=" + a);
			System.out.print(",b=" + b);
			if (log.isDebugEnabled())
				log.debug(",shoushu=" + i);
			if (a < 0 || b < 0) {
				shoushu++;
				hui[shoushu][25] = -1;
				hui[shoushu][26] = -1;
				System.out.print("dian:a=" + a + ",b=" + (b + 1)
						+ "=qi quan 1 shou");
			} else {
				cgcl();
			}
		}
		a = -1;
		b = -1;
	}

	public void update(Graphics g) { // 585
		paint(g);
	}

	public boolean mouseDown(Event e, int x, int y) {
		a = (x - 4) / qpdx;
		b = (y - 4) / qpdx;
		int i, j;
		TreeNode work;
		String s;
		fshuzi = false;
		chuiqi = 0;
		cqianjin = 0;
		showStatus("a=" + a + ", b=" + b);
		if (log.isDebugEnabled())
			log.debug("a=" + a + ",b=" + b);
		if (a >= 0 && a <= 18 && b >= 0 && b <= 18 && zb[a][b][2] == 0) {
			if (a == ktm && b == ktn) {// decide is it the prohibtted point?
				if (log.isDebugEnabled())
					log.debug("this point is not permmitted,try another point!");
				showStatus("this point is not permmitted,try another point!");
			} else {
				work = gotemp;
				if (gotemp == null) {
					if (czhengjie == 0) {
						// jieshuo.setText("你已经赢了!");
						jieshuo.setText("变化已经结束，\n请单击\"重做\"按钮");
					} else {
						// jieshuo.setText("你已经失败了!");
						jieshuo.setText("变化已经结束，\n请单击\"重做\"按钮");
					}
					return true;
				}// xia mian gotemp!=null
				huiqi.setEnabled(true);
				conglai.setEnabled(true);
				fzhudong = true;
				// ftishi=false;
				if (fbuhelidian == true) {
					clhuiqi();
					chuiqi++;// ?
					fbuhelidian = false;
				}
				if (a == gotemp.zba && b == gotemp.zbb) {
					czhengjie -= 1;// ?wei le pei he hou mian suan fa .bu hao li
									// jie
				}
				while (gotemp != null) {
					if (a == gotemp.zba && b == gotemp.zbb) {
						a = gotemp.zba;
						b = gotemp.zbb;
						s = gotemp.jieshuo;
						jieshuo.setText(s);
						if (log.isDebugEnabled())
							log.debug("a=" + a + ",b=" + b);
						if (log.isDebugEnabled())
							log.debug("s=" + s);
						cgcl();
						cqianjin++;
						a = -1;
						b = -1;
						break;
					} else {
						gotemp = gotemp.right;
					}
				}// while
				if (gotemp == null) {
					fbuhelidian = true;
					cgcl();
					cqianjin++;
					gotemp = work;
					jieshuo.setText("再想想，有没有更好的解法");
					// shi yi fei zheng jie ;
				} else {
					czhengjie += 1;
					old = gotemp;
					gotemp = gotemp.left;
					ftishi = false;
					if (gotemp == null) {
						// jieshuo.setText("恭喜你，你已经赢了");
						fdangbu = true;
						tishi.setEnabled(false);
					} else {
						s = gotemp.jieshuo;
						a = gotemp.zba;
						b = gotemp.zbb;
						if (log.isDebugEnabled())
							log.debug("a=" + a + ",b=" + b);
						cgcl();
						cqianjin++;
						if (s != null) {
							if (jieshuo.getText() != null)
								s = jieshuo.getText() + "\n" + s;
							jieshuo.setText(s);
						}
						old = gotemp;
						gotemp = gotemp.left;
						if (gotemp == null) {
							tishi.setEnabled(false);
						}

					}
				}
				repaint();// else
			}// else
		}// if
		else {
			showStatus("it is not the right point,please play again!");
		}
		a = -1;
		b = -1;
		return true;
	}

	public void cgcl() {// chang gui chu li qi zi de qi
		int p = 0;
		int yise = 0;
		int tongse = 0;// yise is diff color.and 2 same.
		int k0 = 0, k1 = 0, k2 = 0, k3 = 0;// the count for three kinds of
											// point.
		int i = 0, j = 0;//
		int dang = 0, ktz = 0;// ,kq=0,p=0,q=0; //dang is breath of block.
		int ks = 0, kss = 0;// ks is count for block,kss for single point
		int kin1 = 0, kin2 = 0, kin3 = 0, kin4 = 0;
		int gq = 0, m = 0, n = 0;// the block index.
		int[] u = { 0, 0, 0, 0, 0 };// position
		int[] v = { 0, 0, 0, 0, 0 };
		int[] k = { 0, 0, 0, 0, 0 };// array for block index.
		int tzd = 0, tkd = 0;// the count for single pointeaten andblock eaten.
		if (log.isDebugEnabled())
			log.debug("//come into method cgcl()");
		shoushu++;
		hui[shoushu][25] = a;
		hui[shoushu][26] = b;
		yise = shoushu % 2 + 1;// yi se:tiao=2
		tongse = (1 + shoushu) % 2 + 1;// tong se:tiao=1
		zb[a][b][0] = tongse;
		if ((a + 1) <= 18 && zb[a + 1][b][0] == yise) {// 1.1
			k1++;// the count of diffrent color.
			kin1 = zb[a + 1][b][3];// the block index for the point.66
			if (kin1 == 0) { // not a block.
				zb[a + 1][b][2] -= 1;
				if (log.isDebugEnabled())
					log.debug("dian:a=" + (a + 1) + ",b=" + b + "jian 1 qi");
				if (zb[a + 1][b][2] <= 0) {// eat the diff point
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a + 1;
					hui[shoushu][tzd * 2] = b;
					if (log.isDebugEnabled())
						log.debug("ti zi:a=" + (a + 1) + ",b=" + b);
					ktz++; // single point eaten was count
					zzq(a + 1, b, tongse);// zi zhen qi
				}
			} else {
				int qi = kuai[kin1][0][0] - 1;
				System.out.print("dian:a=" + (a + 1) + ",b=" + b);
				if (log.isDebugEnabled())
					log.debug(";suo zai kuai:" + kin1 + " jian 1 qi");
				kdq(kin1, qi);
				if (kuai[kin1][0][0] <= 0) {
					k1--;
					tkd++;
					hui[shoushu][8 + tkd] = kin1;
					if (log.isDebugEnabled())
						log.debug("ti kuai:ki=" + kin1);
					ktz += kuai[kin1][0][1];
					kzq(kin1, tongse); // increase the breath of point surround
				}
			}
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == yise) {// 1.2
			k1++;
			kin2 = zb[a - 1][b][3];
			if (kin2 == 0) {// 99
				zb[a - 1][b][2] -= 1;
				if (log.isDebugEnabled())
					log.debug("dian:a=" + (a - 1) + ",b=" + b + "jian 1 qi");
				if (zb[a - 1][b][2] <= 0) {
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a - 1;
					hui[shoushu][tzd * 2] = b;
					if (log.isDebugEnabled())
						log.debug("ti zi:a=" + (a - 1) + ",b=" + b);
					ktz++;
					zzq(a - 1, b, tongse);// zi zhen qi
				}
			} else if (kin2 != kin1) {
				int qi = kuai[kin2][0][0] - 1;
				System.out.print("dian:a=" + (a - 1) + ",b=" + b);
				if (log.isDebugEnabled())
					log.debug(";suo zai kuai:" + kin2 + " jian 1 qi");
				kdq(kin2, qi);
				// kuai[kin2][0][0]-=1;
				if (kuai[kin2][0][0] <= 0) {
					k1--;
					tkd++;
					hui[shoushu][8 + tkd] = kin2;
					if (log.isDebugEnabled())
						log.debug("ti kuai:ki=" + kin2);
					ktz += kuai[kin2][0][1];
					kzq(kin2, tongse); // kuai zheng qi
				}
			} // overpaint

		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == yise) {// 1.3
			k1++;
			kin3 = zb[a][b + 1][3];
			if (kin3 == 0) {
				zb[a][b + 1][2] -= 1;// 132
				if (log.isDebugEnabled())
					log.debug("dian:a=" + a + ",b=" + (b + 1) + "jian 1 qi");
				if (zb[a][b + 1][2] <= 0) {
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a;
					hui[shoushu][tzd * 2] = b + 1;
					if (log.isDebugEnabled())
						log.debug("ti zi:a=" + a + ",b=" + (b + 1));
					ktz++;
					zzq(a, (b + 1), tongse);// zi zhen qi
				}
			} else if (kin3 != kin2 && kin3 != kin1) {
				int qi = kuai[kin3][0][0] - 1;
				System.out.print("dian:a=" + a + ",b=" + (b + 1));
				if (log.isDebugEnabled())
					log.debug(";suo zai kuai:" + kin3 + " jian 1 qi");
				kdq(kin3, qi);
				if (kuai[kin3][0][0] <= 0) {
					k1--;
					tkd++;
					hui[shoushu][8 + tkd] = kin3;
					if (log.isDebugEnabled())
						log.debug("ti kuai:ki=" + kin3);
					ktz += kuai[kin3][0][1];
					kzq(kin3, tongse); // kuai zheng qi
				}
			}
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == yise) {// 1.4
			k1++;
			kin4 = zb[a][b - 1][3];// the subscipt 3 mean the block index
			if (kin4 == 0) {
				zb[a][b - 1][2] -= 1;
				if (log.isDebugEnabled())
					log.debug("dian:a=" + a + ",b=" + (b - 1) + "jian 1 qi");
				if (zb[a][b - 1][2] <= 0) {// 165
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a;
					hui[shoushu][tzd * 2] = b - 1;
					if (log.isDebugEnabled())
						log.debug("ti zi:a=" + a + ",b=" + (b - 1));
					ktz++;
					zzq(a, (b - 1), tongse);// zi zhen qi
				}
			} else if (kin4 != kin3 && kin4 != kin2 && kin4 != kin1) {
				int qi = kuai[kin4][0][0] - 1;
				System.out.print("dian:a=" + a + ",b=" + (b - 1));
				if (log.isDebugEnabled())
					log.debug(";suo zai kuai:" + kin4 + " jian 1 qi");
				kdq(kin4, qi);
				if (kuai[kin4][0][0] <= 0) {
					k1--;
					tkd++;
					hui[shoushu][8 + tkd] = kin4;
					if (log.isDebugEnabled())
						log.debug("ti kuai:ki=" + kin4);
					ktz += kuai[kin4][0][1];
					kzq(kin4, tongse); // kuai zheng qi
				}
			} // overpain
		}
		k0 = k1;// k0 is count for diff color point.
		zb[a][b][2] = 0;// return the breath to zero.
		/*
		 * if(shoushu%2==1) ktb+=ktz; else ktw+=ktz;
		 */

		if ((a + 1) <= 18 && zb[a + 1][b][0] == 0) {// 2.1the breath of blank
			k2++;
			u[k0 + k2] = a + 1;
			v[k0 + k2] = b;// 198
			if (log.isDebugEnabled())
				log.debug("di " + k2 + " qi:a=" + (a + 1) + ",b=" + b);
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == 0) {// 2.2
			k2++;
			u[k0 + k2] = a - 1;
			v[k0 + k2] = b;
			if (log.isDebugEnabled())
				log.debug("di " + k2 + " qi:a=" + (a - 1) + ",b=" + b);
		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == 0) {// 2.3
			k2++;
			u[k0 + k2] = a;
			v[k0 + k2] = b + 1;
			if (log.isDebugEnabled())
				log.debug("di " + k2 + " qi:a=" + a + ",b=" + (b + 1));
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == 0) {// 2.4
			k2++;
			u[k0 + k2] = a;
			v[k0 + k2] = b - 1;
			if (log.isDebugEnabled())
				log.debug("di " + k2 + " qi:a=" + a + ",b=" + (b - 1));
		}
		k0 += k2;// k0 is the total points of diff and blank.
		dang = k2;// kong bai dian jiu shi qi.
		if ((a + 1) <= 18 && zb[a + 1][b][0] == tongse) {// 3.1
			k3++;
			kin1 = zb[a + 1][b][3];
			if (kin1 == 0) {
				kss++; // same color single point.
				dang += zb[a + 1][b][2];
				dang--;// current point close one breath of surr point.
				u[k0 + kss] = a + 1;// u[0] not used
				v[k0 + kss] = b; // deal with single point.
				System.out.print("dian:a=" + (a + 1) + ",b=" + b + "zeng qi:");
				if (log.isDebugEnabled())
					log.debug("" + (zb[a + 1][b][2] - 1));
			} else {// 231
				dang += kuai[kin1][0][0];
				dang--;
				System.out.print("dian:a=" + (a + 1) + ",b=" + b
						+ "suo zai kuai:" + kin1);
				if (log.isDebugEnabled())
					log.debug(" zeng qi:" + (kuai[kin1][0][0] - 1));
				u[4 - ks] = a + 1;// deal with block.
				v[4 - ks] = b;
				ks++;
				k[ks] = kin1;//
			}

		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == tongse) {// 3/.2
			k3++;
			kin2 = zb[a - 1][b][3];
			if (kin2 == 0) {
				kss++;
				dang += zb[a - 1][b][2];
				dang--;
				u[k0 + kss] = a - 1;
				v[k0 + kss] = b;
				System.out.print("dian:a=" + (a - 1) + ",b=" + b + "zeng qi:");
				if (log.isDebugEnabled())
					log.debug("" + (zb[a - 1][b][2] - 1));
			} else if (kin2 != kin1) {
				dang += kuai[kin2][0][0];
				dang--;
				u[4 - ks] = a - 1;
				v[4 - ks] = b;
				ks++;
				k[ks] = kin2;
				System.out.print("dian:a=" + (a - 1) + ",b=" + b
						+ "suo zai kuai:" + kin2);
				if (log.isDebugEnabled())
					log.debug(" zeng qi:" + (kuai[kin2][0][0] - 1));
			}// ks biao shi you ji ge bu tong de kuai shu

		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == tongse) {// 3.3
			k3++;
			kin3 = zb[a][b + 1][3];
			if (kin3 == 0) {
				kss++;
				dang += zb[a][b + 1][2];// 264
				dang--;
				u[k0 + kss] = a;
				v[k0 + kss] = b + 1;
				System.out.print("dian:a=" + a + ",b=" + (b + 1) + "zeng qi:");
				if (log.isDebugEnabled())
					log.debug("" + (zb[a][b + 1][2] - 1));
			} else if (kin3 != kin2 && kin3 != kin1) {
				dang += kuai[kin3][0][0];
				dang--;
				u[4 - ks] = a;
				v[4 - ks] = b + 1;
				ks++;
				k[ks] = kin3;
				System.out.print("dian:a=" + a + ",b=" + (b + 1)
						+ "suo zai kuai:" + kin3);
				if (log.isDebugEnabled())
					log.debug(" zeng qi:" + (kuai[kin3][0][0] - 1));
			}

		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == tongse) {// 3.4
			k3++;
			kin4 = zb[a][b - 1][3];
			if (kin4 == 0) {
				kss++;
				dang += zb[a][b - 1][2];
				dang--;
				u[k0 + kss] = a;
				v[k0 + kss] = b - 1;// kss is single point.
				System.out.print("dian:a=" + a + ",b=" + (b - 1) + "zeng qi:");
				if (log.isDebugEnabled())
					log.debug("" + (zb[a][b - 1][2] - 1));
			} else if (kin4 != kin3 && kin4 != kin2 && kin4 != kin1) {
				dang += kuai[kin4][0][0];
				dang--;
				u[4 - ks] = a;
				v[4 - ks] = b - 1;
				ks++;
				k[ks] = kin4; // ks is block.
				System.out.print("dian:a=" + a + ",b=" + (b - 1)
						+ "suo zai kuai:" + kin4);
				if (log.isDebugEnabled())
					log.debug(" zeng qi:" + (kuai[kin4][0][0] - 1));
			}

		} // 297
		if (dang > 0) {
			ktm = -1;
			ktn = -1;
			if (log.isDebugEnabled())
				log.debug(" ke yi ti jie le.");
		}
		if (dang == 0) {
			showStatus("this point is prohibited,try again!");
			if (log.isDebugEnabled())
				log.debug("this point is prohibited,try again!");
			zzq(a, b, yise);
			hui[shoushu][25] = -1;
			hui[shoushu][26] = -1;
			shoushu--;
			zb[a][b][0] = 0;
			return;
		}
		showStatus("qing=" + dang + ";  a=" + a + ",b=" + b + ";");
		if (k3 == 0) {// 4.1 no same color point surround
			if (log.isDebugEnabled())
				log.debug("//k3=0");
			zb[a][b][2] = dang;
			if (dang == 1 & ktz == 1) {
				ktm = u[k0];// k0==2,3,4.
				ktn = v[k0];
				hui[shoushu + 1][27] = ktm;// 6.30
				hui[shoushu + 1][28] = ktn;
			}// not conform to so. en.
				// else if(dang>1&&ktz==1){
				// wyydj++;
				// wyyd[wyydj][0]=hui[shoushu][1];
				// wyyd[wyydj][1]=hui[shoushu][2];
				// }
			return;
		}
		if (ks == 0) {// 4.2 only single point surr.
			if (log.isDebugEnabled())
				log.debug("ks=0");
			gq = 0;
			for (i = 1; i <= kss; i++) {// 4.1 deal surr point
				hui[shoushu][12 + i * 2 - 1] = u[k0 + i];
				hui[shoushu][12 + i * 2] = v[k0 + i];
				for (j = 1; j <= (kss - i); j++) {
					gq += dd(u[k0 + i], v[k0 + i], u[k0 + i + j], v[k0 + i + j]);
					if (log.isDebugEnabled())
						log.debug("gq=" + gq);
				}
			}
			zb[a][b][2] = dang - gq;
			// zb[a][b][0]=tongse;
			zb[a][b][3] = ++ki;// count from first block
			if (log.isDebugEnabled())
				log.debug("ki=" + ki);
			hui[shoushu][0] = ki;
			kuai[ki][0][0] = zb[a][b][2];
			kuai[ki][0][1] = k3 + 1;
			kuai[ki][k3 + 1][0] = a;
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
			// ci shi de de gong qi jin jin she ji dian
			// hen hao chu li.jian qu gong qi ji ke . jian qu gong qi
		}
		if (ks > 0) {
			if (log.isDebugEnabled())
				log.debug("ks>0");
			ki++;
			if (log.isDebugEnabled())
				log.debug("ki=" + ki);
			hui[shoushu][0] = ki;
			kuai[ki][0][1] = 1;
			kuai[ki][1][0] = a;
			kuai[ki][1][1] = b;
			zb[a][b][3] = ki;
			for (i = 1; i <= kss; i++) {// 330
				hui[shoushu][12 + i * 2 - 1] = u[k0 + i];
				hui[shoushu][12 + i * 2] = v[k0 + i];
				dkhb(u[k0 + i], v[k0 + i], ki);
			}
			// dkhb(a,b,k[1]);
			for (j = 1; j <= ks; j++) {
				hui[shoushu][20 + j] = k[j];
				if (log.isDebugEnabled())
					log.debug("kin=" + k[j] + " ");
				kkhb(ki, k[j]);// not deal with breath
			}
			if (log.isDebugEnabled())
				log.debug("xiao shi.");
			// zb[a][b][2]=tongse;
			// kuai[k[1]][0][0]=zb[a][b][2];//? need deal with breath.
			dang = jskq(ki);
			kdq(ki, dang);
			/*
			 * if(dang==0){ hui[shoushu][0]=ki; kzq(ki,yise); ktm=-1; ktn=-1;
			 * //hui[shoushu][0]=ki; // return; }
			 */
		}
	}

	public void paint(Graphics g) {
		int p = 0;// kuai de zi shu .
		int tongse = 0;// yise is diff color.and 2 same.
		int i = 0, j = 0, k;
		int kin1 = 0, m = 0, n = 0;// the block index.
		int xun;
		int yanchi;
		if (log.isDebugEnabled())
			log.debug("//come into method paint");
		if (fzhudong == true) {
			if (tsa >= 0 && tsb >= 0) {
				xz(g, tsa, tsb);
				tsa = -1;
				tsb = -1;
			}
			for (i = 0; i < chuiqi; i++) {
				xun = shoushu - i - cqianjin + chuiqi;
				tongse = (xun + 1) % 2 + 1;// tong se
				m = huitemp[i][0][0];
				n = huitemp[i][0][1];
				huitemp[i][0][0] = -1;
				huitemp[i][0][1] = -1;
				xz(g, m, n);
				if (tongse == 2)
					g.setColor(Color.black);
				else
					g.setColor(Color.white);
				j = 1;
				while (huitemp[i][j][0] >= 0) {
					m = huitemp[i][j][0];
					n = huitemp[i][j][1];
					huitemp[i][j][0] = -1;
					huitemp[i][j][1] = -1;
					g.fillOval(qpdx * m + bjjx, qpdx * n + bjjx, tyzj, tyzj);
					j++;
				}
			}
			chuiqi = 0;
			for (i = 0; i < cqianjin; i++) {
				xun = shoushu + 1 - cqianjin + i;
				tongse = (xun + 1) % 2 + 1;
				m = hui[xun][25];
				n = hui[xun][26];

				if ((hui[xun][1] >= 0 || hui[xun][9] > 0) && i == 1) {
					try {
						Thread.currentThread().sleep(800);
					} catch (InterruptedException e) {
					}
					/*
					 * yanchi=1000000; while(yanchi>0)yanchi--;
					 */
				}
				if (tongse == 1)
					g.setColor(Color.black);
				else
					g.setColor(Color.white);
				g.fillOval(qpdx * m + bjjx, qpdx * n + bjjx, tyzj, tyzj);
				for (j = 1; j < 5; j++) {
					if (hui[xun][2 * j - 1] < 0)
						break;
					else {
						m = hui[xun][2 * j - 1];
						n = hui[xun][2 * j];
						xz(g, m, n);
					}

				}
				for (j = 1; j < 5; j++) {
					kin1 = hui[xun][8 + j];
					if (kin1 <= 0)
						break;
					else {
						p = kuai[kin1][0][1];
						for (k = 1; k <= p; k++) {
							m = kuai[kin1][k][0];
							n = kuai[kin1][k][1];
							xz(g, m, n);
						}
					}
				}// for
			}// for
			cqianjin = 0;

			if (ftishi == true) {
				g.setColor(Color.red);
				m = gotemp.zba;
				n = gotemp.zbb;
				tsa = m;
				tsb = n;
				g.drawString("A", qpdx * m + qpbj + 1, qpdx * n + qpbj + bjjx
						+ 5);
				// a=-1;
				// b=-1;
			}
			g.setColor(Color.white);
			g.fillRect(500, 0, 270, 530);
			g.fillRect(0, 340, 700, 190);
			g.setColor(Color.black);
			for (i = 10; i < 19; i++) {
				for (j = 0; j < 10; j++) {
					// g.drawString(" ",545+15*j,15*i+60);
					// g.drawString(" ",545+15*j,15*i+230);
					// g.drawString(""+zb[j+9][i][3],500+25*j,15*i-90);
					kin1 = zb[j + 9][i][3];
					// if (kin1>0){
					// g.drawString(""+kuai[kin1][0][0],30+25*j,15*i+230) ;
					// }
					// g.drawString(""+zb[j+9][i][2],500+25*j,15*i+60);
					// g.drawString(""+zb[j+9][i][0],500+25*j,15*i+230);
				}
			}
			fzhudong = false;
		} else {
			// g.setColor(Color.white);
			// g.fillRect(qpdx*19+1+2*bjjx,0,200,qpdx*19+2*bjjx);
			g.setColor(Color.white);
			g.fillRect(500, 0, 270, 530);
			g.fillRect(0, 340, 700, 190);
			g.setColor(Color.orange);
			g.fillRect(0, 0, qpdx * 19 + 2 * bjjx, qpdx * 19 + 2 * bjjx);// 33
			g.setColor(Color.black);
			for (i = 0; i < 19; i++) {
				g.drawLine(qpbj + bjjx, qpbj + bjjx + qpdx * i, 18 * qpdx
						+ bjjx + qpbj, qpbj + bjjx + qpdx * i);// hor
				g.drawLine(qpbj + bjjx + qpdx * i, qpbj + bjjx, qpbj + bjjx
						+ qpdx * i, qpdx * 18 + bjjx + qpbj);// ver
			}// paint the ver and hor line.
			for (i = 0; i < 3; i++) {
				for (j = 0; j < 3; j++) {
					g.fillOval(qpdx * 6 * i + 3 * qpdx + bjjx + qpbj - xbj,
							qpdx * 6 * j + 3 * qpdx + bjjx + qpbj - xbj,
							xbj * 2 + 1, xbj * 2 + 1);
				}
			}// paint the star point.

			// paint all the points owned by black and white.

			// biao shang shu zi
			// ke yi zhu bu DA KAI
			if (fdonghua == true && fshuzi == true) {
				for (xun = 1; xun <= linss; xun++) {
					tongse = (xun + 1) % 2 + 1;
					m = hui[xun][25];
					n = hui[xun][26];
					if (m < 0 || n < 0)
						continue;
					if (tongse == 1)
						g.setColor(Color.black);
					else
						g.setColor(Color.white);
					g.fillOval(qpdx * m + bjjx, qpdx * n + bjjx, tyzj, tyzj);

					for (j = 1; j < 5; j++) {
						if (hui[xun][2 * j - 1] < 0)
							break;
						else {
							m = hui[xun][2 * j - 1];
							n = hui[xun][2 * j];
							xz(g, m, n);
						}
					}
					for (j = 1; j < 5; j++) {
						kin1 = hui[xun][8 + j];
						if (kin1 <= 0)
							break;
						else {
							p = kuai[kin1][0][1];
							for (k = 1; k <= p; k++) {
								m = kuai[kin1][k][0];
								n = kuai[kin1][k][1];
								xz(g, m, n);
							}
						}
					}// for
				}// for
				for (xun = linss + 1; xun <= shoushu; xun++) {
					tongse = (xun + 1) % 2 + 1;
					m = hui[xun][25];
					n = hui[xun][26];
					if (xun > linss + 1) {
						try {
							Thread.currentThread().sleep(800);
						} catch (InterruptedException e) {
						}
					}
					if (tongse == 1)
						g.setColor(Color.black);
					else
						g.setColor(Color.white);
					g.fillOval(qpdx * m + bjjx, qpdx * n + bjjx, tyzj, tyzj);
					if (tongse == 2)
						g.setColor(Color.black);
					else
						g.setColor(Color.white);
					if (xun >= linss + 10)
						g.drawString("" + (xun - linss), qpdx * m + bjjx + qpbj
								- 7, qpdx * n + bjjx + qpbj + 5);
					else
						g.drawString("" + (xun - linss), qpdx * m + bjjx + qpbj
								- 4, qpdx * n + bjjx + qpbj + 5);
					for (j = 1; j < 5; j++) {
						if (hui[xun][2 * j - 1] < 0)
							break;
						else {
							m = hui[xun][2 * j - 1];
							n = hui[xun][2 * j];
							xz(g, m, n);
						}
					}
					for (j = 1; j < 5; j++) {
						kin1 = hui[xun][8 + j];
						if (kin1 <= 0)
							break;
						else {
							p = kuai[kin1][0][1];
							for (k = 1; k <= p; k++) {
								m = kuai[kin1][k][0];
								n = kuai[kin1][k][1];
								xz(g, m, n);
							}
						}
					}// for
				}// for
				fdonghua = false;
			}// if fdonghua==true
			else {
				for (i = 0; i <= 18; i++) {
					for (j = 0; j <= 18; j++) {
						if (zb[i][j][0] == 1) {
							g.setColor(Color.black);
							g.fillOval(qpdx * i + bjjx, qpdx * j + bjjx, tyzj,
									tyzj);
						} else if (zb[i][j][0] == 2) {
							g.setColor(Color.white);
							g.fillOval(qpdx * i + bjjx, qpdx * j + bjjx, tyzj,
									tyzj);
						}
					}// for j
				}// for i
				if (fshuzi == true) {
					for (xun = linss + 1; xun <= shoushu; xun++) {
						tongse = (1 + xun) % 2 + 1;
						m = hui[xun][25];
						n = hui[xun][26];
						if (m < 0 || n < 0) {
							if (log.isDebugEnabled())
								log.debug("zheng jie zhong you qi quan.");
							break;
						}
						if (zb[m][n][2] > 0) {
							if (tongse == 1)
								g.setColor(Color.black);
							else
								g.setColor(Color.white);
							g.fillOval(qpdx * m + bjjx, qpdx * n + bjjx, tyzj,
									tyzj);
							if (tongse == 2)
								g.setColor(Color.black);
							else
								g.setColor(Color.white);
							if (xun >= linss + 10)
								g.drawString("" + (xun - linss), qpdx * m
										+ bjjx + qpbj - 7, qpdx * n + bjjx
										+ qpbj + 5);
							else
								g.drawString("" + (xun - linss), qpdx * m
										+ bjjx + qpbj - 4, qpdx * n + bjjx
										+ qpbj + 5);
						}// if
					}// for
				}
				if (ftishi == true) {
					g.setColor(Color.red);
					m = gotemp.zba;
					n = gotemp.zbb;
					g.drawString("A", qpdx * m + qpbj + 1, qpdx * n + qpbj
							+ bjjx + 5);
					a = -1;
					b = -1;
				}
			}// else
		}// else fzhudong==false

		g.setColor(Color.black);
		for (i = 10; i < 19; i++) {
			for (j = 0; j < 10; j++) {
				// g.drawString(""+zb[j+9][i][3],500+25*j,15*i-90);
				kin1 = zb[j + 9][i][3];
				if (kin1 > 0) {
					// g.drawString(""+kuai[kin1][0][0],30+25*j,15*i+230) ;
				}
				// g.drawString(""+zb[j+9][i][2],500+25*j,15*i+60);
				// g.drawString(""+zb[j+9][i][0],500+25*j,15*i+230);
			}
		}// ti gong ge dian xin xi.
		if (fshuzi == false && shoushu > linss) {
			if (hda > 0 && hdb > 0 && zb[hda][hdb][0] > 0) {
				if (zb[hda][hdb][0] == 1)
					g.setColor(Color.black);
				else if (zb[hda][hdb][0] == 2)
					g.setColor(Color.white);
				g.fillOval(qpdx * hda + bjjx + qpbj - xbj, qpdx * hdb + bjjx
						+ qpbj - xbj, xbj * 2 + 1, xbj * 2 + 1);
			}
			g.setColor(Color.red);
			m = hui[shoushu][25];
			n = hui[shoushu][26];
			hda = m;
			hdb = n;
			// hdse=(1+shoushu)%2+1;
			g.fillOval(qpdx * m + bjjx + qpbj - xbj, qpdx * n + bjjx + qpbj
					- xbj, xbj * 2 + 1, xbj * 2 + 1);
		}// hua dang qian hong dian.
	}

	public boolean action(Event e, Object what) {
		int kin1 = 0, i = 0, j = 0, m = 0, n = 0, p = 0;
		// int yise=0;//tong se
		int tongse = 0;// yi se bei ti
		int lins = 0;
		chuiqi = 0;// ?
		fzhudong = false;
		fshuzi = false;
		if (e.target == huiqi) {
			chuiqi = 0;
			fzhudong = true;
			jieshuo.setText("");
			ftishi = false;
			fshuzi = false;
			hda = -1;
			hdb = -1;// 6.30
			if (shoushu > linss) {
				tishi.setEnabled(true);// ?
				if (fbuhelidian == true) {
					fbuhelidian = false;
					clhuiqi();// wu ying zhao de huiqi
					chuiqi++;// ?
					if (shoushu == linss) {
						huiqi.setEnabled(false);
						conglai.setEnabled(false);
					}
					// } else if(czhengjie==0&&gotemp==null)
				} else if (fdangbu == true)// 6.30
				{
					clhuiqi();
					chuiqi++;// ?
					gotemp = old;
					fdangbu = false;
					if (shoushu == linss) {
						huiqi.setEnabled(false);
						conglai.setEnabled(false);
					}
					/*
					 * if(gotemp.father!=null&&gotemp!=gotemp.father.left) {//?
					 * czhengjie-=1; }
					 * while(gotemp.father!=null&&gotemp!=gotemp.father.left) {
					 * gotemp=gotemp.father; }//?
					 */
					old = gotemp.father;
				} else {
					clhuiqi();
					chuiqi++;// ?
					clhuiqi();
					chuiqi++;// ?
					if (shoushu == linss) {
						huiqi.setEnabled(false);
						conglai.setEnabled(false);
					}
					gotemp = old.father;
					if (gotemp.father != null && gotemp != gotemp.father.left) {
						czhengjie -= 1;
					}
					while (gotemp.father != null
							&& gotemp != gotemp.father.left) {
						gotemp = gotemp.father;
					}
					old = gotemp.father;
					// if(old==null) huiqi.setEnabled(false);
				} // bian hua de hui qi
				repaint();
			} else {
				if (log.isDebugEnabled())
					log.debug("//this is original ju mian");
				jieshuo.setText("已经是原始局面，请选择着点");

			}
			return true;
		}
		if (e.target == tishi) {
			if (log.isDebugEnabled())
				log.debug("come into method tishi!");
			if (gotemp != null) {
				// a=gotemp.zba;
				// b=gotemp.zbb;
				ftishi = true;
				fzhudong = true;
				repaint();
			}// ti shi dang qian shou
			else {
				jieshuo.setText("变化已经结束，请选择\"重来\"按钮");
			}
			return true;
		}
		if (e.target == conglai) {
			fshuzi = false;
			hda = -1;
			hdb = -1;
			if (log.isDebugEnabled())
				log.debug("//come into method conglai!");
			while (shoushu != linss) {
				clhuiqi();
			}
			fdangbu = false;
			ktm = -1;//
			ktn = -1;// 6.30
			for (i = 0; i < 50; i++) {
				huitemp[0][i][0] = -1;
				huitemp[0][i][1] = -1;
				huitemp[1][i][0] = -1;
				huitemp[1][i][1] = -1;
			}
			huiqi.setEnabled(false);
			conglai.setEnabled(false);
			tishi.setEnabled(true);
			yanshi.setEnabled(true);
			jieshuo.setText("\"提示\"按钮可以逐手给出提示；\n\"重来\"按钮可以恢复到初始局面；\n\"演示\"按钮可以显示正解\n如果走错，直接点另一点即可");
			ftishi = false;
			fbuhelidian = false;
			czhengjie = 0;
			fshuzi = false;
			gotemp = gotree.getTreeNode();
			repaint();
			return true;
		}
		if (e.target == yanshi) {
			// =0;//?
			fdonghua = true;
			hda = -1;
			hdb = -1;
			fdangbu = false;
			for (i = 0; i < 50; i++) {
				huitemp[0][i][0] = -1;
				huitemp[0][i][1] = -1;
				huitemp[1][i][0] = -1;
				huitemp[1][i][1] = -1;
			}
			jieshuo.setText("");
			while (shoushu != linss) {
				clhuiqi();
			}
			ktm = -1;
			ktn = -1;
			fbuhelidian = false;
			ftishi = false;
			fshuzi = true;
			gotemp = gotree.getTreeNode();
			while (gotemp != null) {
				a = gotemp.zba;
				b = gotemp.zbb;
				if (a < 0 || b < 0) {
					if (log.isDebugEnabled())
						log.debug("qipuzhengjie zhong you qiquan.");
					break;
				}
				cgcl();
				gotemp = gotemp.left;
			}
			tishi.setEnabled(false);
			huiqi.setEnabled(false);
			yanshi.setEnabled(false);
			conglai.setEnabled(true);
			// gotemp=gotree.getTreeNode();
			repaint();
			return true;
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
		if ((a + 1) <= 18 && zb[a + 1][b][0] == 0) {// 2.1the breath of blank
			dang++;
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == 0) {// 2.2
			dang++;
		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == 0) {// 2.3
			dang++;
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == 0) {// 2.4
			dang++;
		}
		return dang;

	}

	// one point was eaten
	void zzq(int a, int b, int tiao) {// function 6.1
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
			}// 400
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

	public void kzq(int r, int tiao) {// 6.2 tong se kuai bei ti
		int n = 0;// the same color block is eaten
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

	void zjq(int a, int b, int tiao) {// function 6.1
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
			}// 400
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

	public void kjq(int r, int tiao) {// 6.2 tong se kuai bei ti
		int n = 0;// the same color block is eaten
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

	public int dd(int a, int b, int c, int d) {// 7.1diang diang gong qi
		int gq = 0;// consider four points only.
		if (zb[a][d][0] == 0)
			gq++;
		if (zb[c][b][0] == 0)
			gq++;
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
	public void kkhb(int r1, int r2) {// 8.2
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

	public int xl(int a, int b, int r) {// 9.1dian kuai xiang ling
		int gq = 0;// 6.12
		if ((a + 1) <= 18 && zb[a + 1][b][3] == r)
			gq++;
		if ((a - 1) >= 0 && zb[a - 1][b][3] == r)
			gq++;
		if ((b + 1) <= 18 && zb[a][b + 1][3] == r)
			gq++;
		if ((b - 1) >= 0 && zb[a][b - 1][3] == r)
			gq++;
		return gq;
	}

	// 10.1ji suan kuai qi.
	public int jskq(int r2) {
		int gq = 0;// the breath of the block
		int k1 = 0;// xiang ling cheng du wei 2;
		int k2 = 0;// xiang ling cheng du wei 3;
		int k3 = 0;
		int a = 0, b = 0, p = 0, q = 0;

		q = kuai[r2][0][1];
		for (int i = 1; i <= q; i++) {
			a = kuai[r2][i][0];
			b = kuai[r2][i][1];

			if ((a + 1) <= 18 && zb[a + 1][b][0] == 0) {
				gq++;
				if (xl((a + 1), b, r2) == 2)
					k1++;
				if (xl((a + 1), b, r2) == 3) {
					k2++;
				}
				if (xl((a + 1), b, r2) == 4) {
					k3++;
				}
			}
			if ((a - 1) >= 0 && zb[a - 1][b][0] == 0) {
				gq++;
				if (xl((a - 1), b, r2) == 2)
					k1++;
				if (xl((a - 1), b, r2) == 3) {
					k2++;
				}
				if (xl((a - 1), b, r2) == 4) {
					k3++;
				}
			}
			if ((b + 1) <= 18 && zb[a][b + 1][0] == 0) {
				gq++;
				if (xl(a, (b + 1), r2) == 2)
					k1++;
				if (xl(a, (b + 1), r2) == 3) {
					k2++;
				}
				if (xl(a, (b + 1), r2) == 4) {
					k3++;
				}
			} // 573
			if ((b - 1) >= 0 && zb[a][b - 1][0] == 0) {
				gq++;
				if (xl(a, (b - 1), r2) == 2)
					k1++;
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

	public void kdq(int kin, int a) {// 11.1 kuai ding qi
		int m = 0, n = 0, p = 0;
		p = kuai[kin][0][1];
		for (int i = 1; i <= p; i++) {
			m = kuai[kin][i][0];
			n = kuai[kin][i][1];
			zb[m][n][2] = a;
		}
		kuai[kin][0][0] = a;
	}

	public void xz(Graphics g, int a, int b) {// 12.1

		g.setColor(Color.orange);// deal with image only.
		g.fillOval(qpdx * a + bjjx, qpdx * b + bjjx, tyzj, tyzj);
		g.setColor(Color.black);
		if (a == 0) {
			g.drawLine(bjjx + qpbj, qpdx * b + bjjx + qpbj, bjjx + qpdx, qpdx
					* b + bjjx + qpbj);
		} else if (a == 18) {
			g.drawLine(qpdx * a + bjjx, qpdx * b + qpbj + bjjx, qpdx * a + qpbj
					+ bjjx, qpdx * b + qpbj + bjjx);
		} else {
			g.drawLine(qpdx * a + bjjx, qpdx * b + qpbj + bjjx, qpdx * a + qpdx
					+ bjjx, qpdx * b + qpbj + bjjx);
		}
		if (b == 0) {
			g.drawLine(qpdx * a + qpbj + bjjx, qpbj + bjjx, qpdx * a + qpbj
					+ bjjx, bjjx + qpdx);
		} else if (b == 18) {
			g.drawLine(qpdx * a + qpbj + bjjx, qpdx * b + bjjx, qpdx * a + qpbj
					+ bjjx, qpdx * b + qpbj + bjjx);
		} else {
			g.drawLine(qpdx * a + qpbj + bjjx, qpdx * b + bjjx, qpdx * a + qpbj
					+ bjjx, qpdx * b + bjjx + qpdx);
		}
		if ((a - 3) % 6 == 0 && (b - 3) % 6 == 0) {
			g.fillOval(qpdx * a + bjjx + qpbj - xbj, qpdx * b + bjjx + qpbj
					- xbj, xbj * 2 + 1, xbj * 2 + 1);
		}// draw the star point if necessary.
	}

	public void clhuiqi() {
		int p = 0;
		int yise = 0;
		int tongse = 0;// yise is diff color.and 2 same.
		int i = 0, j = 0;// the count for three kinds of point.
		int kin1 = 0;
		int m = 0, n = 0;// the block index.
		int tzs = 0;
		tongse = (shoushu + 1) % 2 + 1;// tong se
		yise = shoushu % 2 + 1;
		m = hui[shoushu][25];
		hui[shoushu][25] = -1;
		n = hui[shoushu][26];
		hui[shoushu][26] = -1;
		huitemp[chuiqi][0][0] = m;
		huitemp[chuiqi][0][1] = n;
		zzq(m, n, yise);
		if (log.isDebugEnabled())
			log.debug("//regret the first step:" + shoushu);
		if (log.isDebugEnabled())
			log.debug(";a=" + m + ",b=" + n);
		if (hui[shoushu][0] > 0) {// you xin kuai sheng cheng.
			ki = hui[shoushu][0];
			for (i = 0; i < 50; i++) {
				kuai[ki][i][0] = 0;
				kuai[ki][i][1] = 0;
			}
			ki--;// zhe shi dang qian you xiao de kuai;
			if (log.isDebugEnabled())
				log.debug("hui fu ki=" + ki);
			for (i = 1; i <= 4; i++) {// xing cheng xin kuai de fan chu li
				if (hui[shoushu][2 * i + 12 - 1] < 0) {
					break;
				} else {
					m = hui[shoushu][12 + 2 * i - 1];
					hui[shoushu][12 + 2 * i - 1] = -1;
					n = hui[shoushu][12 + 2 * i];
					hui[shoushu][12 + 2 * i] = -1;
					zb[m][n][3] = 0;
					// zb[m][n][0]=tongse;
					zb[m][n][2] = jszq(m, n);
					if (log.isDebugEnabled())
						log.debug("//ji suan zi de qi wei" + zb[m][n][2]);
				}
			}// deal with 3 sub
			for (i = 1; i <= 4; i++) {
				kin1 = hui[shoushu][20 + i];
				if (kin1 == 0)
					break;
				else {
					hui[shoushu][20 + i] = 0;
					p = kuai[kin1][0][1];
					for (j = 1; j <= p; j++) {
						m = kuai[kin1][j][0];
						n = kuai[kin1][j][1];
						zb[m][n][3] = kin1;
						zb[m][n][2] = kuai[kin1][0][0];// hui fu yuan lai de qi
					}
				}
			}// for
		}
		for (i = 1; i <= 4; i++) {// chu li ti zi.
			if (hui[shoushu][2 * i - 1] < 0)
				break;
			else {
				m = hui[shoushu][2 * i - 1];
				hui[shoushu][2 * i - 1] = -1;
				n = hui[shoushu][2 * i];
				hui[shoushu][2 * i] = -1;
				tzs++;
				if (log.isDebugEnabled())
					log.debug("hui fu bei ti zi:a=" + m + ",b=" + n);
				huitemp[chuiqi][tzs][0] = m;
				huitemp[chuiqi][tzs][1] = n;
				zb[m][n][0] = yise;
				zb[m][n][2] = 1;
				zb[m][n][3] = 0;
				zjq(m, n, tongse);
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
					tzs++;
					huitemp[chuiqi][tzs][0] = m;
					huitemp[chuiqi][tzs][1] = n;
					if (log.isDebugEnabled())
						log.debug("hui fu bei ti zi:a=" + m + ",b=" + n);
					zb[m][n][0] = yise;
					zb[m][n][3] = kin1;
				}
			}
		}// kuai de xun huan jie shu.
		ktm = hui[shoushu][27];
		ktn = hui[shoushu][28];
		shoushu--;
	}// clhuiqi end
}

class TreeNode {
	TreeNode left;
	int zba;
	int zbb;
	TreeNode father;
	String jieshuo;
	TreeNode right;

	public TreeNode(int a, int b, String s) {
		left = null;
		right = null;
		father = null;
		jieshuo = s;
		zba = a;
		zbb = b;
	}
}

class Tree {
	private TreeNode root;

	public TreeNode getTreeNode() {
		return root;
	}
	private static final Logger log = Logger.getLogger(QiKuai1.class);
	public void insert(int[][][] a, String[][] s) {
		int i, j, k, h, p;
		int m, n;
		String str;
		TreeNode temp = null, old = null;
		TreeNode work = null;
		h = a[0][0][1];// bian hua de shu mu,bu bao kuo zheng jie
		for (i = 1; i <= h; i++) {

			p = a[i][0][0];// bian hua de shou shu
			temp = root;
			old = null;
			for (j = 1; j <= p; j++) {
				str = s[i][j];
				m = a[i][j][0];
				n = a[i][j][1];
				if (temp == null) {
					if (old != null) {
						temp = new TreeNode(m, n, str);
						if (log.isDebugEnabled())
							log.debug("cha ru di yi ge bianhua");
						if (log.isDebugEnabled())
							log.debug("a=" + temp.zba + ",b=" + temp.zbb);
						if (log.isDebugEnabled())
							log.debug("str=" + str);
						old.left = temp;
						temp.father = old;
						old = temp;
						temp = temp.left;
					} else {
						root = new TreeNode(m, n, str);
						if (log.isDebugEnabled())
							log.debug("cha ru gen jie dian/zhengjie");
						if (log.isDebugEnabled())
							log.debug("a=" + m + ",b=" + n);
						if (log.isDebugEnabled())
							log.debug("str=" + str);
						old = root;
						temp = root.left;
					}
				} else if (temp.zba == m && temp.zbb == n) {
					temp = temp.left;

				} else {
					work = temp.right;
					while (work != null) {
						if (work.zba == m && work.zbb == n)
							break;
						else {
							temp = work;
							work = work.right;
						}
					}
					if (work == null) {
						work = new TreeNode(m, n, str);
						if (log.isDebugEnabled())
							log.debug("cha ru fei zheng jie");
						if (log.isDebugEnabled())
							log.debug("a=" + m + ",b=" + n);
						// if(log.isDebugEnabled()) log.debug("b="+n);
						if (log.isDebugEnabled())
							log.debug("str=" + str);
						temp.right = work;
						work.father = temp;
					}
					old = work;
					temp = work.left;
				}
			}// for1
		}// for2,bian hua shu

	}

	public Tree() {
		root = null;
	}
}
