package untitled4;

/**
 * <p>Title:演示死活的。变化的内容是硬编码存储的。 </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SE</p>
 * @author wueddie
 * @version 1.0
 */
import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.io.*;

import org.apache.log4j.Logger;

import untitled10.QiKuai1;

public class Shihuo extends Applet {
	private static final Logger log = Logger.getLogger(QiKuai1.class);

	// int [][] group=new int[100][30];// ge zhong fen san de kuai.ke
	// rong 10 kuai 10 zi ;ju li wei 1.2.3fen kai cun fang
	// int [][]tbgroup=new int[100][30];
	// int ju=0;
	int fzhengjie = 0;
	boolean fyanshi = false;
	boolean HUIQICH = false;// hui qi shi chong hua
	boolean ZHENGCHANGCH = false;// cong wen jian da kai shi chong hua
	// int guize=0;//0:my regular;1:chinese regular;2:japanese regular;
	// 3:south korea regular; 4:ying's regular
	// int [][] wyyd=new int [40][2];
	// int wyydj=0;
	boolean fbuhelidian = false;
	boolean ftishi = false;
	int[][][] bianhua = new int[50][50][2];
	Tree gotree = new Tree();
	TreeNode gotemp;
	TreeNode old = null;
	String[][] jshuo = new String[50][50];
	int linss;
	boolean isStandalone = false;
	int shoushu = 0;// dang qian shou shu,caution:it increase before deal
	int a = -1;// a is the row subscript of the point.
	// int tbshoushu=0;
	int b = -1;// b is the column subscript of the point.
	int ki = 0, tbki = 0;
	int ktm = -1, ktn = -1, tbktm = -1, tbktn = -1;// and position for points
													// eaten.
	int ktb = 0, ktw = 0, tbktb = 0, tbktw = 0; // the count for black and white
												// point eaten.
	int[][][] zb = new int[19][19][4];// 0:state;2:breath;3:blockindex
	int[][][] kuai = new int[100][50][2];// mei yi kuai de ge zi zuo biao
	int[][] hui = new int[400][27];// 0=zi ti yi kuai,1~8four single point
	// eaten,9~12 kuai suo ying of fou block eaten.13~24is same ,25,26a,b
	int[][][] tbzb = new int[19][19][4];
	int[][][] tbkuai = new int[100][50][2];
	int[][] tbhui = new int[400][27];
	DataOutputStream out;
	DataInputStream in;
	Button tishi = new Button("脤谩脢戮");
	Button huiqi = new Button("禄脷脝氓");
	Button conglai = new Button("脰脴脌麓");
	Button yanshi = new Button("脩脻脢戮");
	TextArea jieshuo = new TextArea(5, 10);

	public void init() {
		int i, j;
		// ju++;

		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		bianhua[0][1][0] = -1;
		bianhua[0][1][1] = -1;
		bianhua[0][2][0] = 3;
		bianhua[0][2][1] = 15;
		bianhua[0][3][0] = -1;
		bianhua[0][3][1] = -1;
		bianhua[0][4][0] = 6;
		bianhua[0][4][1] = 16;
		bianhua[0][5][0] = -1;
		bianhua[0][5][1] = -1;
		bianhua[0][6][0] = 2;
		bianhua[0][6][1] = 12;
		bianhua[0][7][0] = 2;
		bianhua[0][7][1] = 16;
		bianhua[0][8][0] = 3;
		bianhua[0][8][1] = 16;
		bianhua[0][9][0] = 2;
		bianhua[0][9][1] = 15;
		bianhua[0][10][0] = 2;
		bianhua[0][10][1] = 14;
		bianhua[0][11][0] = 1;
		bianhua[0][11][1] = 14;
		bianhua[0][12][0] = 1;
		bianhua[0][12][1] = 13;
		bianhua[0][13][0] = 3;
		bianhua[0][13][1] = 14;
		bianhua[0][14][0] = 2;
		bianhua[0][14][1] = 13;
		bianhua[0][15][0] = 3;
		bianhua[0][15][1] = 17;
		bianhua[0][16][0] = 4;
		bianhua[0][16][1] = 17;
		bianhua[0][0][0] = 16;
		bianhua[0][0][1] = 0;
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		jshuo[1][1] = "脰禄麓脣脪禄脢脰隆拢";
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		jshuo[1][3] = "脮芒脢脟戮禄禄卯碌脛鹿脴录眉隆拢";
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		jshuo[1][7] = "脮芒脢脟脥镁脨虏掳脳脥芒脦搂碌脛脧脠脢脰隆拢";
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		bianhua[1][1][0] = 1;
		bianhua[1][1][1] = 17;
		bianhua[1][2][0] = 3;
		bianhua[1][2][1] = 18;
		bianhua[1][3][0] = 0;
		bianhua[1][3][1] = 15;
		bianhua[1][4][0] = 1;
		bianhua[1][4][1] = 18;
		bianhua[1][5][0] = 2;
		bianhua[1][5][1] = 17;
		bianhua[1][6][0] = 2;
		bianhua[1][6][1] = 18;
		bianhua[1][7][0] = 0;
		bianhua[1][7][1] = 17;
		bianhua[1][8][0] = 5;
		bianhua[1][8][1] = 15;
		bianhua[1][9][0] = 1;
		bianhua[1][9][1] = 16;
		bianhua[1][0][0] = 9;
		bianhua[0][0][1] = 1;
		// regret the first step:25
		// regret the first step:24
		// regret the first step:23
		// regret the first step:22
		// ji suan zi de qi
		// ji suan zi de qi
		// regret the first step:21
		// ji suan zi de qi
		// ji suan zi de qi
		// regret the first step:20
		// regret the first step:19
		// regret the first step:18
		// regret the first step:17
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		jshuo[2][1] = "虏禄脢脟脧脠脢脰拢卢露脭掳脳脥芒脦搂脙禄脫脨脥镁脨虏隆拢";
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ti zi 1 xing
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		jshuo[2][5] = "虏垄脙禄脫脨脝氓拢卢麓鹿脣脌脮玫脭煤露酶脪脩隆拢";
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		bianhua[2][1][0] = 2;
		bianhua[2][1][1] = 17;
		bianhua[2][2][0] = 1;
		bianhua[2][2][1] = 15;
		bianhua[2][3][0] = 1;
		bianhua[2][3][1] = 16;
		bianhua[2][4][0] = 0;
		bianhua[2][4][1] = 14;
		bianhua[2][5][0] = 4;
		bianhua[2][5][1] = 16;
		bianhua[2][6][0] = 4;
		bianhua[2][6][1] = 15;
		bianhua[2][7][0] = 5;
		bianhua[2][7][1] = 16;
		bianhua[2][8][0] = 5;
		bianhua[2][8][1] = 17;
		bianhua[2][9][0] = 5;
		bianhua[2][9][1] = 15;
		bianhua[2][10][0] = 4;
		bianhua[2][10][1] = 14;
		bianhua[2][0][0] = 10;
		bianhua[0][0][1] = 2;
		// regret the first step:26
		// regret the first step:25
		// regret the first step:24
		// ji suan zi de qi
		// regret the first step:23
		// ji suan zi de qi
		// regret the first step:22
		// regret the first step:21
		// regret the first step:20
		// hui fu bei ti de zia=1b=14//regret the first step:19
		// regret the first step:18
		// regret the first step:17
		// ji suan zi de qi
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		jshuo[3][3] = "麓貌陆脵虏禄脢脟脮媒陆芒拢卢碌芦脢脟脢碌脮陆禄貌脨铆驴脡脪脭脫脙拢卢脫脷脠芦戮脰陆脵虏脛脫脨鹿脴隆拢";
		// come into method jhcl.jiaohu chu li
		// ti zi 1 xing
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		bianhua[3][1][0] = 1;
		bianhua[3][1][1] = 17;
		bianhua[3][2][0] = 3;
		bianhua[3][2][1] = 18;
		bianhua[3][3][0] = 2;
		bianhua[3][3][1] = 18;
		bianhua[3][4][0] = 2;
		bianhua[3][4][1] = 17;
		bianhua[3][0][0] = 4;
		bianhua[0][0][1] = 3;
		// regret the first step:20
		// hui fu bei ti de zia=3b=17//regret the first step:19
		// regret the first step:18
		// regret the first step:17
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		jshuo[4][1] = "陆脛脨脪脰庐脳脜隆拢";
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks>0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// ks=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// regret the first step:23
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		// come into method jhcl.jiaohu chu li
		// k3=0
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint
		bianhua[4][1][0] = 1;
		bianhua[4][1][1] = 15;
		bianhua[4][2][0] = 1;
		bianhua[4][2][1] = 17;
		bianhua[4][3][0] = 2;
		bianhua[4][3][1] = 17;
		bianhua[4][4][0] = 3;
		bianhua[4][4][1] = 18;
		bianhua[4][5][0] = 2;
		bianhua[4][5][1] = 18;
		bianhua[4][6][0] = 0;
		bianhua[4][6][1] = 17;
		bianhua[4][7][0] = 0;
		bianhua[4][7][1] = 16;
		bianhua[4][8][0] = 0;
		bianhua[4][8][1] = 14;
		bianhua[4][0][0] = 8;
		bianhua[0][0][1] = 4;
		// come into method paint
		// paint the ver and hor line.
		// paint the star point.
		// come into the end of method paint

		gotree.insert(bianhua, jshuo);
		gotemp = gotree.getTreeNode();
		if (gotemp == null)
			if (log.isDebugEnabled())
				log.debug("gotemp is null");
			else {
				while (gotemp != null) {
					if (log.isDebugEnabled())
						log.debug("a=" + gotemp.zba);
					if (log.isDebugEnabled())
						log.debug("b=" + gotemp.zbb);
					gotemp = gotemp.left;
				}

				gotemp = gotree.getTreeNode();
			}
		setLayout(null);
		add(tishi);
		add(huiqi);
		add(conglai);
		add(yanshi);
		add(jieshuo);
		tishi.reshape(550, 15, 60, 26);
		huiqi.reshape(550, 60, 60, 26);
		conglai.reshape(550, 110, 60, 26);
		yanshi.reshape(640, 110, 60, 26);
		jieshuo.reshape(550, 160, 180, 120);
		jieshuo.setText("脤谩脢戮掳麓脜楼驴脡脪脭脰冒脢脰赂酶鲁枚脤谩脢戮\n脰脴脌麓掳麓脜楼驴脡脪脭禄脰赂麓碌陆鲁玫脢录戮脰脙忙\n脠莽鹿没脳脽麓铆拢卢脰卤陆脫碌茫脕铆脪禄碌茫录麓驴脡");

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
		linss = bianhua[0][0][0];
		for (i = 1; i <= linss; i++) {

			a = bianhua[0][i][0];
			b = bianhua[0][i][1];
			System.out.print("a=" + a);
			System.out.print("b=" + b);
			System.out.print("lins=" + i);
			if (a < 0 || b < 0) {
				shoushu++;
				hui[shoushu][25] = -1;
				hui[shoushu][26] = -1;
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
		a = (x - 4) / 28;
		b = (y - 4) / 28;
		TreeNode work;
		String s;
		showStatus("a=" + a + ", b=" + b);
		if (a >= 0 && a <= 18 && b >= 0 && b <= 18 && zb[a][b][2] == 0) {
			if (a == ktm && b == ktn) {// decide is it the prohibtted point?
				showStatus("this point is not permmitted,try another point!");
			} else {
				work = gotemp;
				if (gotemp == null) {
					if (fzhengjie == 0) {
						jieshuo.setText("脛茫脪脩戮颅脫庐脕脣");
					} else {
						jieshuo.setText("脛茫脪脩戮颅脢搂掳脺脕脣");
					}
					return true;
				}
				if (fbuhelidian == true) {
					clhuiqi();
					fbuhelidian = false;
				}

				if (a == gotemp.zba && b == gotemp.zbb) {
					fzhengjie -= 1;
				}
				while (gotemp != null) {
					if (a == gotemp.zba && b == gotemp.zbb) {

						a = gotemp.zba;
						b = gotemp.zbb;
						s = gotemp.jieshuo;
						jieshuo.setText(s);
						if (log.isDebugEnabled())
							log.debug("s=" + s);
						cgcl();
						a = -1;
						b = -1;
						break;
					} else {
						gotemp = gotemp.right;

					}

				}// while
				if (gotemp == null) {
					fbuhelidian = true;
					// fzhengjie=true;
					cgcl();
					gotemp = work;
					jieshuo.setText("脭脵脧毛脧毛拢卢脫脨脙禄脫脨赂眉潞脙碌脛陆芒路篓");
					// shi yi fei zheng jie ;
				} else {
					fzhengjie += 1;
					old = gotemp;
					gotemp = gotemp.left;

					if (gotemp == null) {
						jieshuo.setText("鹿搂脧虏脛茫拢卢脛茫脪脩戮颅脫庐脕脣");
					} else {
						s = gotemp.jieshuo;
						a = gotemp.zba;
						b = gotemp.zbb;
						if (s != null) {
							s = jieshuo.getText() + "\n" + s;
							jieshuo.setText(s);

						}
						old = gotemp;
						gotemp = gotemp.left;
						cgcl();

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
		int k0 = 0, k1 = 0, k2 = 0, k3 = 0, i = 0, j = 0;// the count for three
															// kinds of point.
		int dang = 0, ktz = 0;// ,kq=0,p=0,q=0; //dang is breath of block.
		int ks = 0, kss = 0;// ks is count for block,kss for single point
		int kin1 = 0, kin2 = 0, kin3 = 0, kin4 = 0, gq = 0, m = 0, n = 0;// the
																			// block
																			// index.
		int[] u = { 0, 0, 0, 0, 0 };// position
		int[] v = { 0, 0, 0, 0, 0 };
		int[] k = { 0, 0, 0, 0, 0 };// array for block index.
		int tzd = 0, tkd = 0;// the count for single pointeaten andblock eaten.
		if (log.isDebugEnabled())
			log.debug("come into method jhcl.jiaohu chu li");
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
				if (zb[a + 1][b][2] <= 0) {// eat the diff point
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a + 1;
					hui[shoushu][tzd * 2] = b;
					ktz++; // single point eaten was count
					zzq(a + 1, b, tongse);// zi zhen qi
				}
			} else {
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
		if ((a - 1) >= 0 && zb[a - 1][b][0] == yise) {// 1.2
			k1++;
			kin2 = zb[a - 1][b][3];
			if (kin2 == 0) {// 99
				zb[a - 1][b][2] -= 1;
				if (zb[a - 1][b][2] <= 0) {
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a - 1;
					hui[shoushu][tzd * 2] = b;
					ktz++;
					zzq(a - 1, b, tongse);// zi zhen qi
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
		if ((b + 1) <= 18 && zb[a][b + 1][0] == yise) {// 1.3
			k1++;
			kin3 = zb[a][b + 1][3];
			if (kin3 == 0) {
				zb[a][b + 1][2] -= 1;// 132
				if (zb[a][b + 1][2] <= 0) {
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a;
					hui[shoushu][tzd * 2] = b + 1;
					ktz++;
					zzq(a, (b + 1), tongse);// zi zhen qi
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
		if ((b - 1) >= 0 && zb[a][b - 1][0] == yise) {// 1.4
			k1++;
			kin4 = zb[a][b - 1][3];// the subscipt 3 mean the block index
			if (kin4 == 0) {
				zb[a][b - 1][2] -= 1;
				if (zb[a][b - 1][2] <= 0) {// 165
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a;
					hui[shoushu][tzd * 2] = b - 1;
					ktz++;
					zzq(a, (b - 1), tongse);// zi zhen qi
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
		k0 = k1;// k0 is count for diff point.
		zb[a][b][2] = 0;// return the breath to zero.
		if (shoushu % 2 == 1)
			ktb += ktz;
		else
			ktw += ktz;

		if ((a + 1) <= 18 && zb[a + 1][b][0] == 0) {// 2.1the breath of blank

			k2++;
			u[k0 + k2] = a + 1;
			v[k0 + k2] = b;// 198
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == 0) {// 2.2

			k2++;
			u[k0 + k2] = a - 1;
			v[k0 + k2] = b;
		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == 0) {// 2.3
			k2++;
			u[k0 + k2] = a;
			v[k0 + k2] = b + 1;
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == 0) {// 2.4
			k2++;
			u[k0 + k2] = a;
			v[k0 + k2] = b - 1;
		}
		k0 += k2;// k0 is the total points of diff and blank.
		dang = k2;
		if ((a + 1) <= 18 && zb[a + 1][b][0] == tongse) {// 3.1
			k3++;
			kin1 = zb[a + 1][b][3];
			if (kin1 == 0) {
				kss++; // same color single point.
				dang += zb[a + 1][b][2];
				dang--;// current point close one breath of surr point.
				u[k0 + kss] = a + 1;// u[0] not used
				v[k0 + kss] = b; // deal with single point.
			} else {// 231
				dang += kuai[kin1][0][0];
				dang--;
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
			} else if (kin2 != kin1) {
				dang += kuai[kin2][0][0];
				dang--;
				u[4 - ks] = a - 1;
				v[4 - ks] = b;
				ks++;
				k[ks] = kin2;
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
			} else if (kin3 != kin2 && kin3 != kin1) {
				dang += kuai[kin3][0][0];
				dang--;
				u[4 - ks] = a;
				v[4 - ks] = b + 1;
				ks++;
				k[ks] = kin3;
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
			} else if (kin4 != kin3 && kin4 != kin2 && kin4 != kin1) {
				dang += kuai[kin4][0][0];
				dang--;
				u[4 - ks] = a;
				v[4 - ks] = b - 1;
				ks++;
				k[ks] = kin4; // ks is block.
			}

		} // 297
		if (dang > 0) {
			ktm = -1;
			ktn = -1;
		}
		if (dang == 0) {

			showStatus("this point is prohibited,try again!");
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
				ktm = u[4];
				ktn = v[4];
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
				}
			}
			zb[a][b][2] = dang - gq;
			// zb[a][b][0]=tongse;

			zb[a][b][3] = ++ki;// count from first block
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
				kkhb(ki, k[j]);// not deal with breath
			}
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
		int p = 0;
		int yise = 0;
		int tongse = 0;// yise is diff color.and 2 same.
		int k0 = 0, k1 = 0, k2 = 0, k3 = 0, i = 0, j = 0;// the count for three
															// kinds of point.
		int dang = 0, ktz = 0;// ,kq=0,p=0,q=0; //dang is breath of block.
		int ks = 0, kss = 0;// ks is count for block,kss for single point
		int kin1 = 0, kin2 = 0, kin3 = 0, kin4 = 0, gq = 0, m = 0, n = 0;// the
																			// block
																			// index.
		int[] u = { 0, 0, 0, 0, 0 };// position
		int[] v = { 0, 0, 0, 0, 0 };
		int[] k = { 0, 0, 0, 0, 0 };// array for block index.
		int tzd = 0, tkd = 0;// the count for single pointeaten andblock eaten.
		int xun;
		if (log.isDebugEnabled())
			log.debug("come into method paint");

		/*
		 * if(fbuhelidian==true){ if(shoushu%2==0) g.setColor(Color.black); else
		 * g.setColor(Color.white); g.fillOval(28*a+4,28*b+4,28,28); return; }
		 */
		if (HUIQICH == true) {
			xun = shoushu + 1;
			tongse = (shoushu + 1) % 2 + 1;// tong se
			// yise=shoushu%2+1;
			m = hui[xun][25];
			n = hui[xun][26];
			xz(g, m, n);
			if (tongse == 1)
				g.setColor(Color.black);
			else
				g.setColor(Color.white);
			if (hui[xun][0] != 0) {// zi ti yi kuai//
				kin1 = hui[xun][0];
				p = kuai[kin1][0][1];
				for (i = 1; i <= p; i++) {
					m = kuai[kin1][i][0];
					n = kuai[kin1][i][1];
					g.fillOval(28 * m + 4, 28 * n + 4, 28, 28);
				}

				if (log.isDebugEnabled())
					log.debug("deal with self eaten in regret");

			} else {//
				if (tongse == 2)
					g.setColor(Color.black);
				else
					g.setColor(Color.white);
				for (i = 1; i <= 4; i++) {
					if (hui[xun][2 * i - 1] < 0)
						break;
					else {
						m = hui[xun][2 * i - 1];
						n = hui[xun][2 * i];
						g.fillOval(28 * m + 4, 28 * n + 4, 28, 28);
					}
				}
				for (i = 1; i <= 4; i++) {
					if (hui[shoushu][8 + i] <= 0) {
						break;
					} else {
						kin1 = hui[shoushu][8 + i];

						p = kuai[kin1][0][1];
						for (j = 1; j <= p; j++) {
							m = kuai[kin1][j][0];
							n = kuai[kin1][j][1];
							g.fillOval(28 * m + 4, 28 * n + 4, 28, 28);

						}

					}
				}

			}// else
			HUIQICH = false;
			return;

		}
		g.setColor(Color.white);
		g.fillRect(540, 0, 200, 540);
		g.setColor(Color.orange);
		g.fillRect(0, 0, 540, 540);// 33
		g.setColor(Color.black);
		for (i = 0; i < 19; i++) {
			g.drawLine(18, 18 + 28 * i, 522, 18 + 28 * i);// hor
			g.drawLine(18 + 28 * i, 18, 18 + 28 * i, 522);// ver
		}// paint the ver and hor line.
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 3; j++) {
				g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
			}
		}// paint the star point.
		for (i = 0; i <= 18; i++) {
			for (j = 0; j <= 18; j++) {
				if (zb[i][j][0] == 1) {
					g.setColor(Color.black);
					g.fillOval(28 * i + 4, 28 * j + 4, 28, 28);
				} else if (zb[i][j][0] == 2) {
					g.setColor(Color.white);
					g.fillOval(28 * i + 4, 28 * j + 4, 28, 28);
				}
			}
		}// paint all the points owned by black and white.
		g.setColor(Color.black);
		for (i = 10; i < 19; i++) {
			// g.drawString(""+kuai[i][0][0],600,300+15*i);
			// g.drawString(""+kuai[i][0][1],650,300+15*i);
			for (j = 0; j < 10; j++) {
				g.drawString("" + zb[j][i][2], 545 + 15 * j, 15 * i + 150);
			}
		}

		if (fyanshi == true) {

			// ke yi zhu bu DA KAI
			for (xun = linss + 1; xun <= shoushu; xun++) {

				tongse = (1 + xun) % 2 + 1;
				if (tongse == 1)
					g.setColor(Color.black);
				else
					g.setColor(Color.white);
				m = hui[xun][25];
				n = hui[xun][26];
				g.fillOval(28 * m + 4, 28 * n + 4, 28, 28);
				if (tongse == 2)
					g.setColor(Color.black);
				else
					g.setColor(Color.white);
				if (xun >= linss + 10)
					g.drawString("" + (xun - linss), 28 * m + 11, 28 * n + 23);
				else
					g.drawString("" + (xun - linss), 28 * m + 14, 28 * n + 23);
				// hua zhao zi dian
				/*
				 * if(hui[xun][0]!=0){//hua zi ti dian kin1=hui[xun][0];
				 * p=kuai[kin1][0][1]; for (i=1;i<=p;i++) { m=kuai[kin1][i][0];
				 * n=kuai[kin1][i][1]; xz(g,m,n); } }
				 */
				// else {
				for (i = 1; i <= 4; i++) {
					if (hui[xun][2 * i - 1] < 0)
						break;
					else {
						m = hui[xun][2 * i - 1];
						n = hui[xun][2 * i];
						xz(g, m, n);
					}

				}
				for (i = 1; i <= 4; i++) {
					if (hui[xun][8 + i] <= 0) {
						break;
					} else {
						kin1 = hui[xun][8 + i];
						p = kuai[kin1][0][1];
						for (j = 1; j <= p; j++) {
							m = kuai[kin1][j][0];
							n = kuai[kin1][j][1];
							xz(g, m, n);
						}
					}
				}// }for
				// }//else
			}
			return;
		}

		if (ftishi == true) {
			g.setColor(Color.red);
			g.drawString("A", 28 * a + 14, 28 * b + 23);
			a = -1;
			b = -1;
			// return;
		}
		/*
		 * g.setColor(Color.black); for (i=10;i<19;i++){ //
		 * g.drawString(""+kuai[i][0][0],600,300+15*i); //
		 * g.drawString(""+kuai[i][0][1],650,300+15*i); for ( j=0;j<10;j++){
		 * g.drawString(""+zb[j][i][2],545+15*j,15*i+150); } }
		 */
	}

	public boolean action(Event e, Object what) {
		int kin1 = 0, i = 0, j = 0, m = 0, n = 0, p = 0;
		int yise = 0;// tong se
		int tongse = 0;// yi se bei ti
		int lins = 0;
		if (e.target == huiqi) {
			jieshuo.setText("");
			ftishi = false;
			fyanshi = false;
			if (shoushu > linss) {

				if (fbuhelidian == true) {
					fbuhelidian = false;
					clhuiqi();// wu ying zhao de huiqi
				} else if (fzhengjie == 0 && gotemp == null) {
					clhuiqi();
					gotemp = old;
					if (gotemp.father != null && gotemp != gotemp.father.left) {
						fzhengjie -= 1;
					}
					while (gotemp.father != null
							&& gotemp != gotemp.father.left) {

						gotemp = gotemp.father;
					}
					old = gotemp.father;
				} else {
					clhuiqi();
					clhuiqi();
					gotemp = old.father;
					if (gotemp.father != null && gotemp != gotemp.father.left) {
						fzhengjie -= 1;
					}
					while (gotemp.father != null
							&& gotemp != gotemp.father.left) {

						gotemp = gotemp.father;
					}

					old = gotemp.father;
				} // bian hua de hui qi
				repaint();
			} else {
				if (log.isDebugEnabled())
					log.debug("//this is original ju mian");
				jieshuo.setText("脪脩戮颅脢脟脭颅脢录戮脰脙忙拢卢脟毛脩隆脭帽脳脜碌茫");
				// huiqi.disable();
			}
			return true;
		}
		if (e.target == tishi) {
			if (log.isDebugEnabled())
				log.debug("come into method tishi!");
			if (gotemp != null) {
				a = gotemp.zba;
				b = gotemp.zbb;
				ftishi = true;
				repaint();
			}// ti shi dang qian shou
			else {
				jieshuo.setText("脛茫脪脩戮颅脫庐脕脣");
			}

			return true;
		}
		if (e.target == conglai) {
			if (log.isDebugEnabled())
				log.debug("come into method conglai!");
			while (shoushu != linss) {
				clhuiqi();
			}
			jieshuo.setText("脤谩脢戮掳麓脜楼驴脡脪脭脰冒脢脰赂酶鲁枚脤谩脢戮\n脰脴脌麓掳麓脜楼驴脡脪脭禄脰赂麓碌陆鲁玫脢录戮脰脙忙\n脩脻脢戮掳麓脜楼驴脡脪脭脧脭脢戮脮媒陆芒\n脠莽鹿没脳脽麓铆拢卢脰卤陆脫碌茫脕铆脪禄碌茫录麓驴脡");

			ftishi = false;
			fbuhelidian = false;
			fzhengjie = 0;
			gotemp = gotree.getTreeNode();

			repaint();
			return true;
		}
		if (e.target == yanshi) {
			jieshuo.setText("");
			while (shoushu != linss) {
				clhuiqi();
			}
			fbuhelidian = false;
			ftishi = false;
			fyanshi = true;
			gotemp = gotree.getTreeNode();
			while (gotemp != null) {
				a = gotemp.zba;
				b = gotemp.zbb;
				cgcl();
				gotemp = gotemp.left;
			}
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
			}
			if ((a - 1) >= 0 && zb[a - 1][b][0] == 0) {
				gq++;
				if (xl((a - 1), b, r2) == 2)
					k1++;
				if (xl((a - 1), b, r2) == 3) {
					k2++;
				}
			}
			if ((b + 1) <= 18 && zb[a][b + 1][0] == 0) {
				gq++;
				if (xl(a, (b + 1), r2) == 2)
					k1++;
				if (xl(a, (b + 1), r2) == 3) {
					k2++;
				}
			} // 573
			if ((b - 1) >= 0 && zb[a][b - 1][0] == 0) {
				gq++;
				if (xl(a, (b - 1), r2) == 2)
					k1++;
				if (xl(a, (b - 1), r2) == 3) {
					k2++;
				}
			}
		} // 574
		gq -= k1 / 2;
		gq -= 2 * k2 / 3;
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
		}// draw the star point if necessary.
		zb[a][b][0] = 0;
		zb[a][b][3] = 0;
		zb[a][b][2] = 0;
	}

	public void cqpchuli() {
		int p = 0;
		int yise = 0;
		int tongse = 0;// yise is diff color.and 2 same.
		int k0 = 0, k1 = 0, k2 = 0, k3 = 0, i = 0, j = 0;// the count for three
															// kinds of point.
		int dang = 0, ktz = 0;// ,kq=0,p=0,q=0; //dang is breath of block.
		int ks = 0, kss = 0;// ks is count for block,kss for single point
		int kin1 = 0, kin2 = 0, kin3 = 0, kin4 = 0, gq = 0, m = 0, n = 0;// the
																			// block
																			// index.
		int[] u = { 0, 0, 0, 0, 0 };// position
		int[] v = { 0, 0, 0, 0, 0 };
		int[] k = { 0, 0, 0, 0, 0 };// array for block index.
		int tzd = 0, tkd = 0;// the count for single pointeaten andblock eaten.
		// zhuang=false;
		if (log.isDebugEnabled())
			log.debug("hello");
		yise = shoushu % 2 + 1;// yi se:tiao=2
		tongse = (1 + shoushu) % 2 + 1;// tong se:tiao=1
		zb[a][b][0] = tongse;
		if ((a + 1) <= 18 && zb[a + 1][b][0] == yise) {// 1.1
			k1++;// the count of diffrent color.
			kin1 = zb[a + 1][b][3];// the block index for the point.66
			if (kin1 == 0) { // not a block.
				zb[a + 1][b][2] -= 1;
				if (zb[a + 1][b][2] <= 0) {// eat the diff point
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a + 1;
					hui[shoushu][tzd * 2] = b;
					ktz++; // single point eaten was count
					// ktm=a+1;//the point just be eaten.
					// ktn=b;
					// g.setColor(Color.orange);
					// g.fillOval(28*(a+1)+4,28*b+4,28,28);
					// xz(g,a+1,b);
					zzq(a + 1, b, tongse);// zi zhen qi
				}
			} else {
				int qi = kuai[kin1][0][0] - 1;
				kdq(kin1, qi);
				if (kuai[kin1][0][0] <= 0) {
					k1--;
					tkd++;
					hui[shoushu][8 + tkd] = kin1;
					ktz += kuai[kin1][0][1];
					// xk(g,kin1);//eaten the block.only paint
					kzq(kin1, tongse); // increase the breath of point surround
				}
			}
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == yise) {// 1.2
			k1++;
			kin2 = zb[a - 1][b][3];
			if (kin2 == 0) {// 99
				zb[a - 1][b][2] -= 1;
				if (zb[a - 1][b][2] <= 0) {
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a - 1;
					hui[shoushu][tzd * 2] = b;
					ktz++;

					// xz(g,a-1,b);
					zzq(a - 1, b, tongse);// zi zhen qi
					// g.drawString("zb[a][b][2]",600,30);
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
					// xk(g,kin2);
					kzq(kin2, tongse); // kuai zheng qi
				}
			} // overpaint

		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == yise) {// 1.3
			k1++;
			kin3 = zb[a][b + 1][3];
			if (kin3 == 0) {
				zb[a][b + 1][2] -= 1;// 132
				if (zb[a][b + 1][2] <= 0) {
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a;
					hui[shoushu][tzd * 2] = b + 1;
					ktz++;
					// xz(g,a,b+1);
					zzq(a, (b + 1), tongse);// zi zhen qi
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
					// xk(g,kin3);
					kzq(kin3, tongse); // kuai zheng qi
				}
			}
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == yise) {// 1.4
			k1++;
			kin4 = zb[a][b - 1][3];// the subscipt 3 mean the block index
			if (kin4 == 0) {
				zb[a][b - 1][2] -= 1;
				if (zb[a][b - 1][2] <= 0) {// 165
					k1--;
					tzd++;
					hui[shoushu][tzd * 2 - 1] = a;
					hui[shoushu][tzd * 2] = b - 1;
					ktz++;
					// xz(g,a,b-1);
					zzq(a, (b - 1), tongse);// zi zhen qi
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
					// xk(g,kin4);
					kzq(kin4, tongse); // kuai zheng qi
				}
			} // overpain
		}
		k0 = k1;// k0 is count for diff point.
		zb[a][b][2] = 0;// return the breath to zero.
		if (shoushu % 2 == 1)
			ktb += ktz;// black eat white
		else
			ktw += ktz;

		if ((a + 1) <= 18 && zb[a + 1][b][0] == 0) {// 2.1the breath of blank
			// dang++;
			k2++;
			u[k0 + k2] = a + 1;
			v[k0 + k2] = b;// 198
		}
		if ((a - 1) >= 0 && zb[a - 1][b][0] == 0) {// 2.2
			// dang++;
			k2++;
			u[k0 + k2] = a - 1;
			v[k0 + k2] = b;
		}
		if ((b + 1) <= 18 && zb[a][b + 1][0] == 0) {// 2.3
			// dang++;
			k2++;
			u[k0 + k2] = a;
			v[k0 + k2] = b + 1;
		}
		if ((b - 1) >= 0 && zb[a][b - 1][0] == 0) {// 2.4
			// dang++;
			k2++;
			u[k0 + k2] = a;
			v[k0 + k2] = b - 1;
		}
		dang = k2;
		k0 += k2;// k0 is the total points of diff and blank.

		if ((a + 1) <= 18 && zb[a + 1][b][0] == tongse) {// 3.1
			k3++;
			kin1 = zb[a + 1][b][3];
			if (kin1 == 0) {
				kss++; // same color single point.
				dang += zb[a + 1][b][2];
				dang--;// current point close one breath of surr point.
				u[k0 + kss] = a + 1;// u[0] not used
				v[k0 + kss] = b; // deal with single point.
			} else {// 231
				dang += kuai[kin1][0][0];
				dang--;
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
			} else if (kin2 != kin1) {
				dang += kuai[kin2][0][0];
				dang--;
				u[4 - ks] = a - 1;
				v[4 - ks] = b;
				ks++;
				k[ks] = kin2;
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
			} else if (kin3 != kin2 && kin3 != kin1) {
				dang += kuai[kin3][0][0];
				dang--;
				u[4 - ks] = a;
				v[4 - ks] = b + 1;
				ks++;
				k[ks] = kin3;
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
			} else if (kin4 != kin3 && kin4 != kin2 && kin4 != kin1) {
				dang += kuai[kin4][0][0];
				dang--;
				u[4 - ks] = a;
				v[4 - ks] = b - 1;
				ks++;
				k[ks] = kin4; // ks is block.
			}

		} // 297
		if (dang > 0) {
			ktm = -1;
			ktn = -1;
		}
		/*
		 * if(dang==0){//check the file is right or not
		 * if(guize==1|guize==2|guize==3){
		 * showStatus("this point is prohibited,give up one hand!"); xz(g,a,b);
		 * zzq(a,b,yise); hui[shoushu][25]=-1; hui[shoushu][26]=-1; shoushu--;
		 * zb[a][b][0]=0; a=-1; b=-1; return; } }
		 */
		showStatus("qing=" + dang + a + b);
		if (k3 == 0) {// 4.1 no same color point surround
			if (log.isDebugEnabled())
				log.debug("k3=0");
			zb[a][b][2] = dang;
			if (dang == 1 & ktz == 1) {
				ktm = u[4];
				ktn = v[4];
			}
			a = -1;
			b = -1;
			return;
		}
		if (ks == 0) {// 4.2 only single point surr.
			if (log.isDebugEnabled())
				log.debug("ks=0");
			gq = 0;
			for (i = 1; i <= kss; i++) {// 4.1 deal surr point
				hui[shoushu][12 + i * 2 - 1] = u[kss];
				hui[shoushu][12 + i * 2] = v[kss];
				for (j = 1; j <= (kss - i); j++) {
					gq += dd(u[k0 + i], v[k0 + i], u[k0 + i + j], v[k0 + i + j]);
				}
			}
			zb[a][b][2] = dang - gq;
			// zb[a][b][0]=tongse;

			zb[a][b][3] = ++ki;// count from first block
			kuai[ki][0][0] = zb[a][b][2];
			kuai[ki][0][1] = k3 + 1;
			kuai[ki][k3 + 1][0] = a;
			kuai[ki][k3 + 1][1] = b;
			for (i = 1; i <= k3; i++) {
				m = u[k0 + i];
				n = v[k0 + i];
				kuai[ki][i][0] = m;
				hui[shoushu][12 + i * 2 - 1] = m;
				kuai[ki][i][1] = n;
				hui[shoushu][12 + i * 2] = n;
				zb[m][n][2] = zb[a][b][2];
				zb[m][n][3] = ki;
			}
			if (zb[a][b][2] == 0) {
				ktm = -1;
				ktn = -1;
				hui[shoushu][0] = ki;
				// xk(g,ki);
				kzq(ki, yise);
			}
			// ci shi de de gong qi jin jin she ji dian
			// hen hao chu li.jian qu gong qi ji ke . jian qu gong qi
		}
		if (ks > 0) {
			if (log.isDebugEnabled())
				log.debug("ks>0");
			for (i = 1; i <= ks; i++) {

				hui[shoushu][20 + i] = k[ks];
			}
			ki++;
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
				kkhb(ki, k[j]);// not deal with breath
			}
			// zb[a][b][2]=tongse;
			// kuai[k[1]][0][0]=zb[a][b][2];//? need deal with breath.
			dang = jskq(ki);
			kdq(ki, dang);
			if (dang == 0) {
				hui[shoushu][0] = ki;
				kzq(ki, yise);
				a = -1;
				b = -1;
				ktm = -1;
				ktn = -1;
			}
		}
		a = -1;
		b = -1;
	}

	public void clhuiqi() {
		int p = 0;
		int yise = 0;
		int tongse = 0;// yise is diff color.and 2 same.
		int k0 = 0, k1 = 0, k2 = 0, k3 = 0, i = 0, j = 0;// the count for three
															// kinds of point.
		int dang = 0, ktz = 0;// ,kq=0,p=0,q=0; //dang is breath of block.
		int ks = 0, kss = 0;// ks is count for block,kss for single point
		int kin1 = 0, kin2 = 0, kin3 = 0, kin4 = 0, gq = 0, m = 0, n = 0;// the
																			// block
																			// index.

		int tzd = 0, tkd = 0;// the count for single pointeaten andblock eaten.

		tongse = (shoushu + 1) % 2 + 1;// tong se
		yise = shoushu % 2 + 1;
		/*
		 * if(hui[shoushu][0]!=0){//zi ti yi kuai,qie ci kuai zai zhi qian xing
		 * cheng / kin1=hui[shoushu][0]; kjq(kin1,yise); p=kuai[kin1][0][1];
		 * 
		 * for (i=1;i<=p;i++) { m=kuai[kin1][i][0]; n=kuai[kin1][i][1];
		 * zb[m][n][2]=1; zb[m][n][0]=tongse; } m=hui[shoushu][25];
		 * n=hui[shoushu][26]; zzq(m,n,yise); zb[m][n][0]=0; zb[m][n][3]=0;
		 * zb[m][n][2]=0; //deal with zb 0 and 2 subscipt // kuai[kin1][0][0]=0;
		 * // kuai[kin1][0][1]=0; for(i=1;i<=4;i++){
		 * if(hui[shoushu][2*i+12-1]<0){ break; } else{
		 * m=hui[shoushu][12+2*i-1]; n=hui[shoushu][12+2*i]; zb[m][n][3]=0; }
		 * }//deal with 3 sub for(i=1;i<=4;i++){ kin1=hui[shoushu][20+i];
		 * if(kin1==0) break; else{ p=kuai[kin1][0][1]; for(j=1;j<=p;j++){
		 * m=kuai[kin1][j][0]; n=kuai[kin1][j][1]; zb[m][n][3]=kin1;
		 * kuai[kin1][0][0]=1; } }
		 * 
		 * } if(log.isDebugEnabled())
		 * log.debug("deal with self eaten in regret");
		 * 
		 * }
		 */
		// else {//fei zi ti yi kuai,dan gai kuai zhi qian wei xing cheng
		m = hui[shoushu][25];
		hui[shoushu][25] = -1;
		n = hui[shoushu][26];
		hui[shoushu][26] = -1;
		zzq(m, n, yise);
		if (log.isDebugEnabled())
			log.debug("//regret the first step:" + shoushu);

		for (i = 1; i <= 4; i++) {// chu li ti zi.
			if (hui[shoushu][2 * i - 1] < 0)
				break;
			else {
				m = hui[shoushu][2 * i - 1];
				hui[shoushu][2 * i - 1] = -1;
				n = hui[shoushu][2 * i];
				hui[shoushu][2 * i] = -1;
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
					zb[m][n][0] = yise;
					zb[m][n][3] = kin1;

				}

			}
		}
		if (hui[shoushu][0] > 0) {
			ki = hui[shoushu][0];
			for (i = 0; i < 50; i++) {
				kuai[ki][i][0] = 0;
				kuai[ki][i][1] = 0;
			}
			for (i = 1; i <= 4; i++) {// mei you zi ti, xing cheng xin kuai de
										// fan chu li
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
						log.debug("//ji suan zi de qi");
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
						// zb[m][n][0]=tongse;
						zb[m][n][2] = kuai[kin1][0][0];
					}
				}
			}// for
		}
		shoushu--;
	}// method cgcl()

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
	private static final Logger log = Logger.getLogger(QiKuai1.class);
	private TreeNode root;

	public TreeNode getTreeNode() {
		return root;
	}

	public void insert(int[][][] a, String[][] s) {
		int i, j, k, h, p;
		int m, n;
		String str;
		TreeNode temp = null, old = null;
		TreeNode work = null;
		h = a[0][0][1];
		for (i = 1; i <= h; i++) {

			p = a[i][0][0];
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
							log.debug("a=" + temp.zba);
						if (log.isDebugEnabled())
							log.debug("b=" + temp.zbb);
						if (log.isDebugEnabled())
							log.debug("str=" + str);
						old.left = temp;
						temp.father = old;
						old = temp;
						temp = temp.left;
					} else {
						root = new TreeNode(m, n, str);
						if (log.isDebugEnabled())
							log.debug("cha ru gen jie dian");
						if (log.isDebugEnabled())
							log.debug("a=" + m);
						if (log.isDebugEnabled())
							log.debug("b=" + n);
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
							log.debug("a=" + m);
						if (log.isDebugEnabled())
							log.debug("b=" + n);
						if (log.isDebugEnabled())
							log.debug("str=" + str);
						temp.right = work;
						work.father = temp;

					}
					old = work;
					temp = work.left;

				}
			}// for1
			temp = root;
			while (temp != null) {
				m = temp.zba;
				n = temp.zbb;
				if (log.isDebugEnabled())
					log.debug("a=" + m);
				if (log.isDebugEnabled())
					log.debug("b=" + n);

				temp = temp.left;

			}
		}// for2
		temp = root;
		/*
		 * while (temp!=null){ m=temp.zba; n=temp.zbb; if(log.isDebugEnabled())
		 * log.debug("a="+m); if(log.isDebugEnabled()) log.debug("b="+n);
		 * temp=temp.left;
		 * 
		 * }
		 */
	}

	public Tree() {
		root = null;
	}
}