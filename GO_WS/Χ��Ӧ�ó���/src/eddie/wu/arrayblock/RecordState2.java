package eddie.wu.arrayblock;

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
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.ui.applet.GoApplet;

public class RecordState2
   extends Frame {
	private static final Log log=LogFactory.getLog(RecordState2.class);
   //private int id;
   //GoBoard的主要缺陷是棋块数有限制，只有128块。但是用于计算死活题足够了。
   //数组棋块的好处是易于复制数据；
   GoApplet goapplet = new GoApplet();
   Button shuangfanglunxia = new Button("双方轮下");
   Button sihuojisuan = new Button("死活计算");
   //Button baocunqipu = new Button("保存棋谱");
   Button renjiduixia = new Button("人机对下");
   Button baichujumian = new Button("摆出局面");
   Button baocunjumian = new Button("保存局面");
   Button zairujumian = new Button("载入局面");
   Label zuobiao = new Label("输入做活棋块的坐标点");
   TextField zuobiaoa = new TextField();
   TextField zuobiaob = new TextField();

   boolean SHUANGFANGLUNXIA = true;
   boolean RENJIDUIXIA;
   boolean BAICHUJUMIAN;

   public RecordState2() {
      shuangfanglunxia.addActionListener(new LunxiaActionListener());
      renjiduixia.addActionListener(new DuixiaActionListener());
      
      sihuojisuan.addActionListener(new SihuoActionListener());
    
      add(goapplet);
      
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

   public boolean mouseDown(Event e, int x, int y) { //接受鼠标输入
      log.debug("chuanbodaorongqi");
      if (SHUANGFANGLUNXIA == true) {
         byte a = (byte) ( (x - 4) / 28 + 1); //完成数气提子等.
         byte b = (byte) ( (y - 4) / 28 + 1);
         goapplet.KEXIA = true;
      }
      if (RENJIDUIXIA == true) {
         //zuochuyingdui
      }
      if (BAICHUJUMIAN == true) {
         goapplet.goboard.qiquan();
         goapplet.KEXIA = true;
      }
      return true;
   }

   public static void main(String[] args) {
      RecordState2 weiqi = new RecordState2();

      weiqi.setVisible(true);
   }

   class LunxiaActionListener
      implements ActionListener {

      public void actionPerformed(ActionEvent e) {
         //点击后重新开局。
         goapplet.goboard = new GoBoard();
         goapplet.KEXIA = true;

         SHUANGFANGLUNXIA = true;
         BAICHUJUMIAN = false;
         RENJIDUIXIA = false;
         log.debug("SHUANGFANGLUNXIA=" + SHUANGFANGLUNXIA);
      }

   }

   class SihuoActionListener
      implements ActionListener {

      public void actionPerformed(ActionEvent e) {
         //计算死活
         //byte i, j, k;
         byte houxuan[][] = new byte[40][2];
         houxuan = goapplet.goboard.houxuandian(
            (byte) Integer.parseInt(zuobiaoa.getText()),
            (byte) Integer.parseInt(zuobiaob.getText()));
         GoBoard[] go = new GoBoard[100000];
         Stack<GoBoard> stack = new Stack<GoBoard> ();
         stack.add(go[0]);
         while (!stack.isEmpty()) {
            //for(i=1;i<=

         }
         //每步10种变化计算，最多算10步(主要是内存限制)。
         //假设其中一方每次都能先计算好点。
         //做活方能做活的话，只需能找到最佳步骤即可。
         //做活方不能做活，必须验证所有点，包括弃权才能下结论
         //攻击方能杀棋，只需正解即可
         //攻击方不能成功，需要全部验证。
         go[0] = goapplet.goboard;
         log.debug("死活计算的结果为：");
      }

   }

   class DuixiaActionListener
      implements ActionListener {

      public void actionPerformed(ActionEvent e) {
         goapplet.goboard = new GoBoard();
         goapplet.KEXIA = true;
         SHUANGFANGLUNXIA = false;
         BAICHUJUMIAN = false;
         RENJIDUIXIA = true;
         log.debug("RENJIDUIXIA=" + RENJIDUIXIA);

      }

   }

   

}