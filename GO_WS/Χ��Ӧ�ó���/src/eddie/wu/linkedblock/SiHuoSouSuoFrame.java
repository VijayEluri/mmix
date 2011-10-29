package eddie.wu.linkedblock;

import java.awt.Button;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SiHuoSouSuoFrame extends Frame {

	// private int id;
	// GoBoard����Ҫȱ��������������ƣ�ֻ��128�顣�������ڼ����������㹻�ˡ�
	// �������ĺô������ڸ������ݣ�
	ShortLianApplet1 goapplet = new ShortLianApplet1();
	Button shuangfanglunxia = new Button("˫������");
	Button sihuojisuan = new Button("�������");
	// Button baocunqipu = new Button("��������");
	Button renjiduixia = new Button("�˻�����");
	Button baichujumian = new Button("�ڳ�����");
	Button baocunjumian = new Button("�������");
	Button zairujumian = new Button("�������");
	Label zuobiao = new Label("�����������������");
	TextField zuobiaoa = new TextField();
	TextField zuobiaob = new TextField();

	boolean SHUANGFANGLUNXIA = true;
	boolean RENJIDUIXIA;
	boolean BAICHUJUMIAN;

	public SiHuoSouSuoFrame() {
		shuangfanglunxia.addActionListener(new LunxiaActionListener());
		renjiduixia.addActionListener(new DuixiaActionListener());
		baichujumian.addActionListener(new JumianActionListener());
		sihuojisuan.addActionListener(new SihuoActionListener());
		baocunjumian.addActionListener(new BaocunjumianActionListener(this));
		zairujumian.addActionListener(new ZairujumianActionListener(this));

		add(goapplet);
		add(shuangfanglunxia);
		add(sihuojisuan);
		add(renjiduixia);
		add(baichujumian);
		add(baocunjumian);
		add(zairujumian);
		add(zuobiao);
		add(zuobiaoa);
		add(zuobiaob);

		goapplet.setVisible(true);
		shuangfanglunxia.setVisible(true);
		sihuojisuan.setVisible(true);
		renjiduixia.setVisible(true);
		baichujumian.setVisible(true);
		zairujumian.setVisible(true);
		setLayout(null);
		goapplet.setBounds(30, 30, 560, 560);
		shuangfanglunxia.setBounds(600, 100, 100, 30);
		renjiduixia.setBounds(600, 130, 100, 30);

		baichujumian.setBounds(600, 160, 100, 30);
		zairujumian.setBounds(700, 100, 100, 30);
		sihuojisuan.setBounds(700, 130, 100, 30);
		baocunjumian.setBounds(700, 160, 100, 30);
		zuobiao.setBounds(600, 190, 150, 30);
		zuobiaoa.setBounds(600, 220, 100, 30);
		zuobiaob.setBounds(700, 220, 100, 30);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}

		});
	}

	public boolean mouseDown(Event e, int x, int y) { // �����������
		System.out.print("chuanbodaorongqi");
		if (SHUANGFANGLUNXIA == true) {
			byte a = (byte) ((x - 4) / 28 + 1); // ����������ӵ�.
			byte b = (byte) ((y - 4) / 28 + 1);
			goapplet.KEXIA = true;
		}
		if (RENJIDUIXIA == true) {
			// zuochuyingdui
		}
		if (BAICHUJUMIAN == true) {
			goapplet.goboard.qiquan();
			goapplet.KEXIA = true;
		}
		return true;
	}

	public static void main(String[] args) {
		SiHuoSouSuoFrame weiqi = new SiHuoSouSuoFrame();

		weiqi.setVisible(true);
	}

	class LunxiaActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// ��������¿��֡�
			goapplet.goboard = new BoardLianShort();
			goapplet.KEXIA = true;

			SHUANGFANGLUNXIA = true;
			BAICHUJUMIAN = false;
			RENJIDUIXIA = false;
			System.out.println("SHUANGFANGLUNXIA=" + SHUANGFANGLUNXIA);
		}

	}

	class ZairujumianActionListener implements ActionListener { // ������档
		Frame parent;

		public ZairujumianActionListener(Frame par) {
			parent = par;
		}

		public void actionPerformed(ActionEvent e) {
			// �������
			FileDialog fd = new FileDialog(parent, "��������λ��", FileDialog.LOAD);
			fd.setFile("1.wjm");
			fd.setDirectory(".");
			fd.show();

			String inname = fd.getFile();
			String dir = fd.getDirectory();

			try {
				DataInputStream in = new DataInputStream(
						new BufferedInputStream(new FileInputStream(dir
								+ inname)));

				goapplet.goboard.zairujumian(in);
				goapplet.goboard.shengchengjumian();
				goapplet.goboard.output();
				in.close();
			}

			catch (IOException ex) {
				System.out.println("the input meet some trouble!");
				System.out.println("Exception" + ex.toString());
			}

			System.out.println("�������");

		}
	}

	class SihuoActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// ��������
			byte m1, n1, result;
			m1 = (byte) Integer.parseInt(zuobiaoa.getText());
			n1 = (byte) Integer.parseInt(zuobiaob.getText());
			result = goapplet.goboard.jisuansihuo(m1, n1, true);

			// for(i=1;i<=

			// ÿ��10�ֱ仯���㣬�����10��(��Ҫ���ڴ�����)��
			// ��������һ��ÿ�ζ����ȼ���õ㡣
			// ���������Ļ���ֻ�����ҵ���Ѳ��輴�ɡ�
			// ����������������֤���е㣬������Ȩ�����½���
			// ��������ɱ�壬ֻ�����⼴��
			// ���������ܳɹ�����Ҫȫ����֤��

			System.out.println("�������Ľ��Ϊ��" + result);
		}

	}

	class DuixiaActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			goapplet.goboard = new BoardLianShort();
			goapplet.KEXIA = true;
			SHUANGFANGLUNXIA = false;
			BAICHUJUMIAN = false;
			RENJIDUIXIA = true;
			System.out.println("RENJIDUIXIA=" + RENJIDUIXIA);

		}

	}

	class JumianActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (BAICHUJUMIAN == true) {
				goapplet.goboard.qiquan();
			} else {
				goapplet.goboard = new BoardLianShort();
				goapplet.KEXIA = true;

				SHUANGFANGLUNXIA = false;
				BAICHUJUMIAN = true;
				RENJIDUIXIA = false;
			}

			System.out.println("BAICHUJUMIAN=" + BAICHUJUMIAN);
		}
	}

	class BaocunjumianActionListener implements ActionListener {
		Frame parent;

		public BaocunjumianActionListener(Frame par) {
			parent = par;
		}

		public void actionPerformed(ActionEvent e) {
			if (true) { // �������
				FileDialog fd = new FileDialog(parent, "��������λ��",
						FileDialog.SAVE);
				fd.setFile("1.wjm");
				fd.setDirectory(".");
				fd.show();

				String outname = fd.getFile();
				String dir = fd.getDirectory();
				System.out.print(outname);
				try {
					DataOutputStream out = new DataOutputStream(
							new BufferedOutputStream(new FileOutputStream(dir
									+ outname)));
					System.out.print(out);

					// goapplet.goboard.outputjumian(out);

				}

				catch (IOException ex) {
					System.out.println("the output meet some trouble!");
					System.out.println("Exception" + ex.toString());
				}

				System.out.println("�������");

			} else { // ��������

				System.out.println("��������");

			}

		}
	}

}
