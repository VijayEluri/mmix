package untitled8;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;

import untitled10.QiKuai1;
import untitled3.DsNode;

public class Zhizuodingshi extends Applet {// 可以通过这个程序输入定式
	// 结果保存在定式树文件中。专门输入书本中专家棋手讲解的定式。
	// 较复杂的定式需要通过zhizuobianhua输入，因为涉及到征子是否有利
	// 劫材也可能是要考虑的因素。常规的劫材和引征是否成立等等。
	// 几个类都有保存定式树到文件的代码和从文件生成定式树，考虑封装到
	// 某个类中。
	private static final Logger log = Logger.getLogger(QiKuai1.class);
	final static byte ZKUOHAO = -127;
	final static byte YKUOHAO = 127;
	GoBoardLian1 go;
	DsNode root, temp, work, temp1, temp2;
	boolean YANSHI;// 演示并验证定式。
	boolean KEXIA = true;
	boolean CHONGHUI = true; // 是否重绘。

	DataInputStream in; // 定式树输入文件
	DataOutputStream out; // 定式树输出文件。
	Button yanshi = new Button("演示定式");
	Button store = new Button("储存定式");
	Button huiqi = new Button("悔棋");
	Button shurudingshi = new Button("存入定式文件");
	DsNode[] stackds = new DsNode[25]; // SGF格式用

	public void init() { // 初始化,完成界面
		byte fenzhi = 0; // SGF格式用,与stackds构成堆栈

		setLayout(null);
		add(yanshi);
		add(store);
		add(huiqi);
		add(shurudingshi);
		yanshi.reshape(575, 89, 100, 26);
		store.reshape(575, 115, 100, 26);
		huiqi.reshape(575, 141, 100, 26);
		shurudingshi.reshape(575, 167, 100, 26);
		huiqi.disable();
		store.disable();
		shurudingshi.disable();

		go = new GoBoardLian1();
		try {
			byte lina = 0;
			byte[] dsnode = new byte[5];
			boolean ykhjs = false; // 是否已经有右括号。
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(Constant.DING_SHI_SHU)));
			if (in.available() == 0) {
				return;
			}
			lina = in.readByte();
			if (lina == ZKUOHAO) { // 成型定式树应该第一步有五种以上变化。
				in.read(dsnode); // 左括号不会相连。
				root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
						dsnode[4], dsnode[0]);
				if (log.isDebugEnabled())
					log.debug("定式树的根为：(a=" + work.zba + "," + work.zbb + ")");
				// 整棵树不用括号包围
				// 括号仅表示括号内是父节点的分支；
				fenzhi++;
				stackds[fenzhi] = work; // 右括号结束后新的左括号接到work上。
				if (log.isDebugEnabled())
					log.debug("第一层括号的工作点为：(a=" + stackds[fenzhi].zba + ","
							+ stackds[fenzhi].zbb + ")");
			} else { // 定式树初始时可能只有一种起点。
				in.read(dsnode, 1, 4);
				root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
						dsnode[4], lina);
				if (log.isDebugEnabled())
					log.debug("定式树的根为：(a=" + work.zba + "," + work.zbb + ")");
			}
			while (in.available() != 0) {
				lina = in.readByte();
				if (lina == ZKUOHAO) {
					in.read(dsnode);
					temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
							dsnode[4], dsnode[0]);
					if (log.isDebugEnabled())
						log.debug("分支变化的首节点为:" + temp.zba + "," + temp.zbb
								+ ")");
					if (ykhjs == false) {
						// 新的左括号
						work.left = temp;
						fenzhi++;
						work = temp;
						stackds[fenzhi] = work;
						if (log.isDebugEnabled())
							log.debug("新一层括号的工作点为：(a=" + stackds[fenzhi].zba
									+ "," + stackds[fenzhi].zbb + ")");
					} else { // 右括号结束；并列的作括号
						// 遇到新的作括号，ykhjs失效。
						ykhjs = false;
						work.right = temp;
						if (log.isDebugEnabled())
							log.debug("同一层括号的原工作点为：(a=" + stackds[fenzhi].zba
									+ "," + stackds[fenzhi].zbb + ")");
						work = temp;
						stackds[fenzhi] = work;
						if (log.isDebugEnabled())
							log.debug("同一层新的左括号的工作点为：(a=" + stackds[fenzhi].zba
									+ "," + stackds[fenzhi].zbb + ")");
					}
				} else if (lina == YKUOHAO) {
					if (ykhjs == false) {
						ykhjs = true;
						work = stackds[fenzhi];
						if (log.isDebugEnabled())
							log.debug(work.toString());
					} else {
						stackds[fenzhi--] = null;
						work = stackds[fenzhi];
					}
				} else { // 连续节点
					in.read(dsnode, 1, 4);
					temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
							dsnode[4], lina);
					work.left = temp;
					work = temp;
				}
			}
			in.close();
		} catch (IOException ex) {
			if (log.isDebugEnabled())
				log.debug("打开文件（定式树）遇到问题！");
			if (log.isDebugEnabled())
				log.debug("Exception" + ex.toString());
		}

	}

	public void paint(Graphics g) {
		if (log.isDebugEnabled())
			log.debug("进入方法paint()");
		if (CHONGHUI == true) {
			g.setColor(Color.orange);
			g.fillRect(0, 0, 560, 560);
		} else {
			CHONGHUI = true;
		} // 不想重绘则让CHONGHUI＝false
		g.setColor(Color.black);
		byte kinp = 0;
		for (int i = 1; i <= 19; i++) { // 画线
			g.drawLine(18, 28 * i - 10, 522, 28 * i - 10); // hor
			g.drawLine(28 * i - 10, 18, 28 * i - 10, 522); // ver
		}
		for (int i = 0; i < 3; i++) { // 画星位
			for (int j = 0; j < 3; j++) {
				g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
			}
		}

		for (int i = 1; i <= 19; i++) { // 画着子点
			for (int j = 1; j <= 19; j++) {
				if (go.zb[i][j][0] == 1) {
					g.setColor(Color.black);
					g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
					// if(log.isDebugEnabled())
					// log.debug("//paint the black point.");
				} else if (go.zb[i][j][0] == 2) {
					g.setColor(Color.white);
					g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
					// if(log.isDebugEnabled())
					// log.debug("//paint the white point.");
				}
				if (YANSHI == true) {
					YANSHI = false;
					g.setColor(Color.green);
					for (i = 1; i <= go.shoushu; i++) {
						g.drawString("" + i, 28 * (go.hui[i][25]) - 4,
								28 * (go.hui[i][26]) - 4);
					}
				} else {
					kinp = go.zb[i][j][go.KSYXB];
					if (kinp != 0) { // 输出块号
						g.setColor(Color.green);
						g.drawString("" + kinp, 28 * i - 4, 28 * j - 4);

					}
				}
			}
		}
		if (log.isDebugEnabled())
			log.debug("从方法paint()返回");
	} // else画整个棋盘和棋子

	public boolean mouseDown(Event e, int x, int y) { // 接受鼠标输入
		if (KEXIA == true) {
			// KEXIA=false;//只有机器完成一手,才能继续.
			byte a = (byte) ((x - 4) / 28 + 1); // 完成数气提子等.
			byte b = (byte) ((y - 4) / 28 + 1);
			go.cgcl(a, b);
			go.output();
			if (go.shoushu > 0) {
				huiqi.enable();
				store.enable();
				// shurudingshi.enable();
			}
			CHONGHUI = false; // 效果：提子被画上十字
			repaint();
			if (log.isDebugEnabled())
				log.debug("mousedown");
			return true; // 向容器传播,由Frame处理
		} else {
			return true;
		}
	}

	byte fuzhi1(DsNode root) { // 为各节点的形势赋值
		// 6月13日。
		DsNode temp = null;
		byte max = (byte) -128;
		byte linv;
		if (root.left == null) { // 变化结束
			return root.xingshi1;
		}
		temp = root.left;

		for (; temp != null; temp = temp.right) {
			linv = temp.xingshi1 = fuzhi1(temp);
			if (linv > max) {
				max = linv;
			}
		}
		root.xingshi1 = (byte) (-max);
		return (byte) (-max);
	}

	byte fuzhi2(DsNode root) { // 为各节点的形势赋值
		// 6月13日。
		DsNode temp = null;
		byte max = (byte) -128;
		byte linv;
		if (root.left == null) { // 变化结束
			return root.xingshi2;
		}
		temp = root.left;

		for (; temp != null; temp = temp.right) {
			linv = temp.xingshi2 = fuzhi2(temp);
			if (linv > max) {
				max = linv;
			}
		}
		root.xingshi1 = (byte) (-max);
		return (byte) (-max);
	} // 定式中涉及征子可能不止一个？

	byte fuzhi3(DsNode root) { // 为各节点的形势赋值
		// 6月13日。
		DsNode temp = null;
		byte max = (byte) -1;
		byte linv;
		if (root.left == null) { // 变化结束
			return root.xianhoushou;
		}
		temp = root.left;

		for (; temp != null; temp = temp.right) {
			linv = temp.xianhoushou = fuzhi3(temp);
			if (linv > max) {
				max = linv;

			}
		}
		root.xianhoushou = (byte) (-max);
		return (byte) (-max);
	}

	public void update(Graphics g) { // 585
		paint(g);
	}

	public boolean action(Event e, Object what) {
		short kin1 = 0, i = 0, j = 0, p = 0;
		byte m = 0, n = 0;
		byte yise = 0; // tong se
		byte tongse = 0; // yi se bei ti
		byte lins = 0;
		int ju = 0; // 其实不需要初始化
		/*
		 * if (e.target == yanshi) { work=root; while(work!=null){
		 * 
		 * for(temp=work;temp!=null;temp=temp.right){ if(temp.blbz ==0){
		 * go.cgcl(temp.zba ,temp.zbb) ; temp.blbz==1; } } if(temp==null){
		 * 
		 * }
		 * 
		 * } }
		 */
		if (e.target == shurudingshi) {
			// fuzhi3(root);

			// 输入变化后，将结果和原有定式联系，并修改定式树文件；
			// 将局部的结果存储为SGF文件，需要时动态调入。
			try {
				out = new DataOutputStream(new BufferedOutputStream(
						new FileOutputStream(Constant.DING_SHI_SHU)));
				write(out, root);
				out.close();
			} catch (IOException ex) {
				if (log.isDebugEnabled())
					log.debug("the output meet some trouble!");
				if (log.isDebugEnabled())
					log.debug("Exception" + ex.toString());
			}
			shurudingshi.disable();
		}

		if (e.target == huiqi) {
			if (go.shoushu > 0) {
				CHONGHUI = true;
				go.clhuiqi();
				if (go.shoushu == 0) {
					huiqi.disable();
					store.disable();
				}
				repaint();
			} else {
				if (log.isDebugEnabled())
					log.debug("this is original ju mian");

			}
			return true;
		}
		if (e.target == store) { // 储存定式到树中，并恢复到原始局面
			// 过程中会进行坐标的标准化，树中的坐标都在左上角。
			boolean xds = false; // 是否新定式。
			if (log.isDebugEnabled())
				log.debug("come into method store!");
			work = root;
			byte xuan = 0;
			byte fuan = 0, t = 0;
			for (i = 1; i <= go.shoushu; i++) {
				m = go.hui[i][25];
				n = go.hui[i][26];
				if (xuan == 0) {
					if (n < 10) {
						xuan = 1;
						if (m > 10)
							xuan += 1;
					} else if (n > 10) {
						xuan = 3;
						if (m < 10)
							xuan += 1;
					}
				}// 确保xuan!=0;只在第一步执行
				for (j = xuan; j > 1; j--) {
					t = n;
					n = (byte) (20 - m);
					m = t;
				}
				if (fuan == 0) {// 需要进一步判明
					if (m < n)
						fuan = 1;
					else if (m > n)
						fuan = 2;
				}
				if (fuan == 2) {
					t = m;
					m = n;
					n = t;
				}
				if (log.isDebugEnabled())
					log.debug("处理后节点为：" + m + "," + n);

				if (root == null) {
					xds = true;
					work = root = new DsNode(m, n);
					continue;
				}// 定式树文件为空或不存在的特殊情况。
				if (xds == true) {// 此时work为上级节点
					work.left = new DsNode(m, n);
					work = work.left;
				} else { // work是下一手的老大
					for (temp1 = work; temp1 != null; temp = temp1, temp1 = temp1.right) {
						if (temp1.zba == m && temp1.zbb == n) {
							work = temp1.left;
							break;

						}
					}
					if (temp1 == null) {
						System.out.print("该初始变化尚未列为定式！现在加入");
						temp.right = work = new DsNode(m, n);
						xds = true;
					}
				}
			}

			if (xds == true) {
				work.xianhoushou = -1;
			} else {
				if (log.isDebugEnabled())
					log.debug("该变化在定式树中已经存在");
			}
			store.disable();
			shurudingshi.enable();
			huiqi.disable();
			go = new GoBoardLian1();
			repaint();

		}
		return true;
	}

	void write(DataOutputStream out, DsNode root) {

		DsNode temp;
		DsNode temp1;
		if (root == null) {
			return;
		}
		temp = root.right; // root是表示树的二叉树的根节点
		if (log.isDebugEnabled())
			log.debug("局部root=(" + root.zba + "," + root.zbb + ")");
		if (temp == null) { // 没有兄弟，表示没有分叉
			try {
				out.writeByte(root.xianhoushou);
				out.writeByte(root.zba);
				out.writeByte(root.zbb);
				out.writeByte(root.xingshi1);
				out.writeByte(root.xingshi2);
			} catch (IOException ex) {
				if (log.isDebugEnabled())
					log.debug("the reading of file  meet some trouble!");
				if (log.isDebugEnabled())
					log.debug("Exception" + ex.toString());
			}
			write(out, root.left);
			return;
		} else {

			try {
				out.writeByte(ZKUOHAO);
				out.writeByte(root.xianhoushou);
				out.writeByte(root.zba);
				out.writeByte(root.zbb);
				out.writeByte(root.xingshi1);
				out.writeByte(root.xingshi2);

			} catch (IOException ex) {
				if (log.isDebugEnabled())
					log.debug("the reading of file  meet some trouble!");
				if (log.isDebugEnabled())
					log.debug("Exception" + ex.toString());
			}

			write(out, root.left);

			try {

				out.writeByte(YKUOHAO);
			} catch (IOException ex) {
				if (log.isDebugEnabled())
					log.debug("the reading of file  meet some trouble!");
				if (log.isDebugEnabled())
					log.debug("Exception" + ex.toString());
			}

			for (temp1 = root; temp != null; temp1 = temp, temp = temp.right) {
				try {
					out.writeByte(ZKUOHAO);
					out.writeByte(temp.xianhoushou);
					out.writeByte(temp.zba);
					out.writeByte(temp.zbb);
					out.writeByte(temp.xingshi1);
					out.writeByte(temp.xingshi2);

				} catch (IOException ex) {
					if (log.isDebugEnabled())
						log.debug("the reading of file  meet some trouble!");
					if (log.isDebugEnabled())
						log.debug("Exception" + ex.toString());
				}

				write(out, temp.left);

				try {
					out.writeByte(YKUOHAO);
				} catch (IOException ex) {
					if (log.isDebugEnabled())
						log.debug("the reading of file  meet some trouble!");
					if (log.isDebugEnabled())
						log.debug("Exception" + ex.toString());
				}
			} // for
		} // else
	} // method

} // class
