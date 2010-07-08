package untitled8;

import java.applet.Applet;
import java.awt.*;

//项目10：用GoAppletLian测试GoboardLian1
//所有源代码在同一目录下，确保类的通信。
//仍有很多错误，未细致检查。
public class GoAppletLian1
   extends Applet {
   public final boolean DEBUG = true;
   boolean KEXIA = true;
   boolean CHONGHUI = true; //重绘标志
   boolean ZZ = false; //征子成立标志
   JisuanBoardLian goji;
   Button houtui=new Button("后退");
   Button zhzi = new Button("征子计算");
   TextField zzwb = new TextField(); //征子块索引
   GoBoardLian1 goboard = new GoBoardLian1();
   public void update(Graphics g) {
      paint(g);
   }

   public void init() {
      System.out.println("进入方法init()");
      add(zzwb);
      add(zhzi);
      add(houtui);
      zzwb.reshape(580, 130, 80, 25);
      zhzi.reshape(580, 100, 80, 25);
      houtui.reshape(580,250,80,25);
      System.out.println("从方法init()返回");
   }

   public boolean action(Event e, Object what) {
      System.out.println("进入方法action()");
      if (e.target == zhzi) {

         goji = new JisuanBoardLian(goboard);
         //System.out.println("方法getText()");
         byte kkk = (byte) Integer.parseInt(zzwb.getText().toString());
         System.out.println("被征子的块为："+kkk);
         boolean acti = goji.yiqichi(kkk);
         //System.out.println("方法getText()");
         if (acti == true) {
            System.out.println("征子成立");
         }
         System.out.println("从方法action()返回");
         ZZ = true;
         CHONGHUI = true;
         repaint();
      }
      else if(e.target==houtui){
       goboard.clhuiqi();
       repaint();

      }
      return true;
   }

   public void paint(Graphics g) {
      System.out.println("进入方法paint()");
      if (CHONGHUI == true) {
         g.setColor(Color.orange);
         g.fillRect(0, 0, 560, 560);
      }
      CHONGHUI = true;
      if (ZZ == true) {
         ZZ = false;
         g.setColor(Color.black);
         for (int i = 1; i <= 19; i++) { //画线
            g.drawLine(18, 28 * i - 10, 522, 28 * i - 10); //hor
            g.drawLine(28 * i - 10, 18, 28 * i - 10, 522); //ver
         }
         //  System.out.println("// paint the ver and hor line.");
         for (int i = 0; i < 3; i++) { //画星位
            for (int j = 0; j < 3; j++) {
               g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
            }
         }
         // System.out.println("//paint the star point.");
         for (int i = 1; i <= 19; i++) { //画着子点
            for (int j = 1; j <= 19; j++) {
               if (goji.zb[i][j][0] == 1) {
                  g.setColor(Color.black);
                  g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
                  //System.out.println("//paint the black point.");
               }
               else if (goji.zb[i][j][0] == 2) {
                  g.setColor(Color.white);
                  g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
                  // System.out.println("//paint the white point.");
               }
            }
         }

      }

      g.setColor(Color.black);
      byte kinp = 0;
      for (int i = 1; i <= 19; i++) { //画线
         g.drawLine(18, 28 * i - 10, 522, 28 * i - 10); //hor
         g.drawLine(28 * i - 10, 18, 28 * i - 10, 522); //ver
      }
      for (int i = 0; i < 3; i++) { //画星位
         for (int j = 0; j < 3; j++) {
            g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
         }
      }

      for (int i = 1; i <= 19; i++) { //画着子点
         for (int j = 1; j <= 19; j++) {
            if (goboard.zb[i][j][0] == 1) {
               g.setColor(Color.black);
               g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
               //System.out.println("//paint the black point.");
            }
            else if (goboard.zb[i][j][0] == 2) {
               g.setColor(Color.white);
               g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
               // System.out.println("//paint the white point.");
            }
            kinp = goboard.zb[i][j][goboard.KSYXB];
            if (kinp != 0 & DEBUG == true) { //输出块号
               g.setColor(Color.green);
               g.drawString("" + kinp, 28 * i - 4, 28 * j - 4);

            }
         }
      }
      System.out.println("从方法paint()返回");
   } //else画整个棋盘和棋子

   public boolean mouseDown(Event e, int x, int y) { //接受鼠标输入
      if (KEXIA == true) {
         //KEXIA=false;//只有机器完成一手,才能继续.
         byte a = (byte) ( (x - 4) / 28 + 1); //完成数气提子等.
         byte b = (byte) ( (y - 4) / 28 + 1);
         goboard.cgcl(a, b);
         goboard.output();
         CHONGHUI = false;
         repaint();
         System.out.println("mousedown");
         return true; //向容器传播,由Frame处理
      }
      else {
         return true;
      }
   }

   public boolean handleEvent(Event evt) {
      if (evt.id == Event.MOUSE_DOWN) {
         return mouseDown(evt, evt.x, evt.y);
      }
      else {
         return super.handleEvent(evt);
      }
   }

   public GoAppletLian1() {
      super();
   }
}
