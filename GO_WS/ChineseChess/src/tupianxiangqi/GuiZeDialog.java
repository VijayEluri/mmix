package tupianxiangqi;

import javax.swing.*;
import java.awt.Choice;

public class GuiZeDialog
  extends JDialog {

  private Frame1 parent;
  JPanel jp;
  Choice guizeshijian = new Choice();
  Choice guizebushu = new Choice();
  Choice guizezhi = new Choice();

  JButton tongyi;
  JButton tuichu;

  JLabel guize_zhi;
  JLabel guize_mei;

  public GuiZeDialog(Frame1 frame) {

    super(frame, "Э�̹���", true);
    parent = frame;
  }

  public void init() {
    setLayout(null);
    jp = (JPanel)this.getContentPane();

    tuichu = new JButton("�˳�");
    tongyi = new JButton("ȷ��");

    guize_zhi = new JLabel("����ִ��");
    guize_mei= new JLabel("ÿ");

    guizeshijian.add("1����");
    guizeshijian.add("5����");
    guizeshijian.add("10����");
    guizeshijian.add("15����");
    guizeshijian.add("25����");
    guizebushu.add("1��");
    guizebushu.add("10��");
    guizebushu.add("15��");
    guizebushu.add("25��");

    guizezhi.add("��");
    guizezhi.add("��");

    jp.setSize(150, 250);
    jp.setLayout(null);
    jp.add(guize_zhi);
    jp.add(guizezhi);
    jp.add(guizeshijian);
    jp.add(guize_mei);
    jp.add(guizebushu);
    jp.add(tongyi);
    jp.add(tuichu);

    this.setSize(250, 150);
    guize_zhi.setBounds(15, 45, 60, 30);
    guizezhi.setBounds(75, 15, 30, 30);
    guizeshijian.setBounds(120, 15, 60, 30);
    guize_mei.setBounds(180, 45, 30, 30);
    guizebushu.setBounds(210, 15, 40, 30);
    tongyi.setBounds(30, 100, 60, 30);
    tongyi.setVisible(true);
    tuichu.setBounds(160, 100, 60, 30);
    tuichu.setVisible(true);

  }
}