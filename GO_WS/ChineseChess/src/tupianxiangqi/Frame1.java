package tupianxiangqi;

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; //
import java.util.Observer;
import java.util.Observable;

//import
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Frame1
  extends JFrame
  implements Observer {
  byte[] dataout = new byte[1024];
  boolean DANFANG = true;
  String serverIP;
  String roomID;
  String userID;
  String username;
  String pangguanbiaoji;
  String otheruserID; //对手的ID。便于发送信息。

  String hongfyhm = "";
  String heifyhm = "";
  String hongfzwm = "";
  String heifzwm = "";
  String hongtxmc = "Red0.jpg";
  String heitxmc = "Blue0.jpg";
  String hongfdj = "";
  String heifdj = "";
  String hongfdjf = "";
  String heifdjf = "";

  int index; //用户索引。
  //byte mycolor;
  //Image [] yuansu=new Image[16];

  ClsClient myClient;
  JPanel contentPane;

  //Button kaishi;
  Button huiqi;
  Button tuichu;
  Button kaishi;

  Board1 xiang;
  Daojishi djs1, djs2;

  PopList guanzhanzhe;

  Dialog chongxinkaishi; //sheng负判明，询问是否再下？
  Label cxkslabel;
  Button cxksyes;
  Button cxksno;

  Dialog duifangqingqiutuichu; //是否同意对方退出；
  Button tuichuyes;
  Button tuichuno;
  Label tuichulabel;

  Dialog duifangqingqiuhuiqi; //是否同意对方悔棋。
  Label huiqilabel;
  Button huiqiyes;
  Button huiqino;

  Dialog duifangqiangxingtuichu; //显示信息
  Button qiangxingtuichu;
  Label qiangxingtuichul;

  Dialog duifangbutongyihuiqi;
  Button butongyihuiqi;
  Label butongyihuiqil;

  Dialog duifangbutongyituichu;
  Button butongyituichu;
  Label butongyituichul;

  Dialog shanchupangguanzhe; //不能删除
  Button pangguanzhe;
  Label pangguanzhel;

  Dialog benfanghuosheng; //不能删除
  Button bfhsbutton;
  Label bfhslabel;

  Dialog duifanghuosheng; //不能删除
    Button dfhsbutton;
    Label dfhslabel;


  Dialog guize;
  Button tongyi;
  Button guize_tuichu;
  Label guize_zhi;
  Label guize_bu;
  Label guize_mei;
  Choice guizeshijian = new Choice();
  Choice guizebushu = new Choice();
  Choice guizezhi = new Choice();

  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  JPanel gnmb; //功能面板

  /*public Frame1() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
     }*/

  public void update(Observable obs, Object obj) { //接收数据
    if (obj instanceof ClsTCPBaseData) {
      ClsTCPBaseData data = (ClsTCPBaseData) obj;
      //增加代码解释模块
      if (data.err == 0) {
        if (data.data == null || data.data.length < 3) {
          return;
        }
        byte[] datain1 = new byte[data.data.length - 3];
        //截去后用于生成字符串。
        byte xun = 0;
        String[][] khxx;

        System.arraycopy(data.data, 3, datain1, 0, datain1.length);
        System.out.println("\n收到的字节数组截去前三个字节后的字符串为："
                           + new String(datain1)); //为收到的数据
        String fanhui1 = new String(datain1);
        //截去层号。
        byte[] datain = new byte[data.data.length - 1];
        byte[] dataout = new byte[1024];
        System.arraycopy(data.data, 1, datain, 0, datain.length);

        if (datain[0] == CS.QIDONGYOUXI) {
          System.out.println("1:收到游戏启动相关消息."); //为收到的数据

          if (datain[1] == 1) {

            System.out.println("\t1）收到初始登录返回消息:" + fanhui1); //为收到的数据
            // khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            if (fanhui1.equals("OK")) {
              System.out.println("OK"); //为收到的数据

              myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 1,
                (byte) 2,
                "GETI"), 0, 0);

              System.out.println("fachu请求客户消息:"); //为收到的数据

            }
            else {
              System.out.println("NOT OK"); //为收到的数据
              //todo:
            }
          }
          else if (datain[1] == 2) {
            System.out.print("\t2）收到获取登录信息请求的回复:"); //为收到的数据
            if (fanhui1.equals("OK")) {
              System.out.println("OK"); //为收到的数据
              kaishi.setEnabled(true);
              huiqi.setEnabled(true);
              //xiang.qidong(mycolor);
              //请求规则信息。，

              myClient.setAutoSentdata(true); //
              dataout[0] = (byte) 3;
              dataout[1] = (byte) 6;
              dataout[2] = (byte) CS.GUIZE;
              dataout[3] = (byte) CS.QINGQIUGUIZE;
              myClient.sendData(dataout);
              //System.out.println("\nfachu请求客户消息:"); //为收到的数据

            }
            else {
              System.out.println("NOT OK"); //为收到的数据
              //todo:

            }

          }
          else if (datain[1] == 3) {
            System.out.print("\t3）收到请求登录信息的回复:规则信息："); //为收到的数据
            khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            //todo
            System.out.print("\n");
            for (xun = 0; xun < 15; xun++) {
              System.out.print(khxx[0][xun]);
              System.out.print("\t");
              System.out.print(khxx[1][xun]);
            }
            System.out.print("\n");

          }
          else if (datain[1] == 4) {
            System.out.print("4）收到请求登录信息的回复:用户列表："); //为收到的数据
            khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            //todo
            System.out.print("\n");
            for (xun = 0; xun < 15; xun++) {
              System.out.print(khxx[0][xun]);
              System.out.print("\t");
              System.out.print(khxx[1][xun]);
              System.out.print("\n");
            }
            hongfyhm = khxx[0][1];
            heifyhm = khxx[1][1];
            hongfzwm = khxx[0][2];
            heifzwm = khxx[1][2];
            //hongtxmc = "Red0.jpg";
            //heitxmc = "Blue0.jpg";
            hongfdj = khxx[0][10];
            heifdj = khxx[1][10];
            hongfdjf = khxx[0][3];
            heifdjf = khxx[1][3];
            System.out.println("heifangdjf=" + heifdjf);

          }
          else if (datain[1] == 5) {
            System.out.print("\t5）收到请求登录信息的回复:游戏资料："); //为收到的数据
            khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            //todo

          }

          // djs1.init();//?
          //djs1.start();
          /* if (datain[1] == CS.HONGFANG) {
             xiang.qidong(CS.HONGFANG);
             //xiang.qidong( (byte) 0);
             xiang.setEnabled(true);
           }
           else if (datain[1] == CS.HEIFANG) {
             xiang.qidong(CS.HEIFANG);
             xiang.setEnabled(true);
           }*/
        }
        else if (datain[0] == CS.TUICHUYOUXI) {
          if (datain[1] == 1) {
            System.out.print("1）收到请求退出的答复:"); //为收到的数据
            khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            if (khxx[0][0] .equals("OK") ) {
              System.out.println("OK"); //为收到的数据
              System.exit(0);

            }
            else {
              System.out.println("NOT OK"); //为收到的数据
              //todo:
              duifangbutongyituichu.show();
            }
          }
          else if (datain[1] == 2) {
            System.out.print("收到对方强退的通知:"); //为收到的数据
            khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            if (khxx[0][0] == "LOGO") {
              System.out.println("2）LOGO：" + khxx[0][1]); //为收到的数据
              // jieshu
              duifangqiangxingtuichu.show();
              System.exit(0);
            }
            else {
              System.out.println("消息格式错误"); //为收到的数据
              //todo:
            }

          }
          else if (datain[1] == 3) {
            System.out.print("3）收到转发的请求退出："); //为收到的数据
            khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            //todo：对话框
            otheruserID = khxx[0][1];
            System.out.println(khxx[0][1] + "qingqiutuichu");
            duifangqingqiutuichu.setBounds(150, 150, 250, 150);
            duifangqingqiutuichu.show();
          }

        }

        else if (datain[0] == CS.PANGGUANZHE) { //旁观者

          if (datain[1] == 1) {
            System.out.print("1）旁观者退出:"); //为收到的数据
            khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            if (khxx[0][0] == "LOGO") {
              System.out.println(khxx[0][1]); //为收到的数据
              //System.exit(0);
              //todo:从观战列表中删除。
            }
            else {
              System.out.println("GESHICUOWU"); //为收到的数据
              //todo:
            }
          }
          else if (datain[1] == 2) {
            khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            if (khxx[0][0] == "DELE") {
              System.out.print("删除旁观者："); //为收到的数据
              System.out.println(khxx[0][1]); //为收到的数据

            }
            else {
              System.out.println("你不能删除该旁观者"); //为收到的数据
              //todo:
            }

          }

        }
        else if (datain[0] == CS.XIUGAIYONGHUXINXI) { //4
          if (datain[1] == 1) {
            System.out.print("4）收到xinzhengguanzhangzhexinxi");
            //为收到的数据
            khxx = ClsByte.translate(datain1, (byte) '\r', (byte) '\n');
            System.out.print("\n");
            for (xun = 0; xun < 15; xun++) {
              System.out.print(khxx[0][xun]);
              System.out.print("\t");
              System.out.print(khxx[1][xun]);
              System.out.print("\n");
            }
            guanzhanzhe.add(khxx[0][10] + "\t" + khxx[0][3] + "\t"
                            + khxx[0][1] + "\t" + khxx[0][2]); ;

            System.out.println("guanzhanzhewei" + khxx[0][2]);

          }
          else if (datain[1] == 2) {

          }
          else if (datain[1] == 3) {

          }

        }
        else if (datain[0] == CS.BAOLIU) { //5

        }
        else if (datain[0] == CS.YOUXIDINGYI) { //6
          if (datain[1] == CS.CHAOSHISHIBAI) { //本方胜利
            System.out.println("收到胜负信息");
            xiang.bukexia = true;
            if (datain[2] == xiang.mycolor) {//对方获胜
              // chongxinkaishi.cxks
              System.out.println("本方失败");
              duifanghuosheng.show();
            }
            else {
              System.out.println("本方获胜");
              benfanghuosheng.show();
            }

          }
          else if (datain[1] == CS.SHENGLI) { //本方胜利
            xiang.bukexia = true;
            if (datain[2] == xiang.mycolor) {//对方获胜
              // chongxinkaishi.cxks
              System.out.println("本方获胜");
              benfanghuosheng.show();

            }
            else {
              System.out.println("本方失败");
              duifanghuosheng.show();
            }


          }
          else if (datain[1] == CS.QIPU) {
            System.out.println("收到普通棋谱信息");
            xiang.jieshouqipu(datain);
          }
          else if (datain[1] == CS.HUIQI) {
            //if (xiang.mycolor == datain[2]) {
            System.out.println("收到请求悔棋信息");
            duifangqingqiuhuiqi.show();
            // }

          }
          else if (datain[1] == CS.SHUANGFANGHUIQI) {
            System.out.println("收到对方同意悔棋信息");
            if (xiang.mycolor != xiang.turncolor) { //还没有应对，悔一步
              System.out.println("对方还没有应对，悔一步");

              xiang.huiqi();
              xiang.bukexia=false;
              if (xiang.mycolor == CS.HEIFANG) {
                System.out.println("我是黑方"+"djs2.start()");

                djs1.stop();
                djs2.init();
                djs2.start();
              }
              else if (xiang.mycolor == CS.HONGFANG) {
                System.out.println("我是红方"+"djs1.start()");
                djs2.stop();
                djs1.init();
                djs1.start();

              }
            }
            else { //已经作了应对，要悔两步。
              System.out.println("已经作了应对，要悔两步");
              xiang.huiqi();
              xiang.huiqi();
              xiang.bukexia=false;
              if (xiang.mycolor == CS.HEIFANG) {
               System.out.println("我是黑方"+"djs2.start()");

               djs1.stop();
               djs2.init();
               djs2.start();
             }
             else if (xiang.mycolor == CS.HONGFANG) {
               System.out.println("我是红方"+"djs1.start()");
               djs2.stop();
               djs1.init();
               djs1.start();

             }
            }
          }
          else if (datain[1] == CS.HUIQIBUTONGYI) {
            duifangbutongyihuiqi.show();
          }

          else if (datain[1] == CS.GUIZE) {
            System.out.println("jieshoudaoxiangqi规则消息。");

            if (datain[2] == CS.QINGQIUGUIZE) {
              DANFANG = false;
              System.out.println("qidong规则对话框。");
              //guize.setVisible(true);
              guize.show();
              System.out.println("qidong规则对话框。");

            }
            else if (datain[2] == CS.DANFANGGUIZE) {
              DANFANG = true;
              System.out.println("qidong单方规则。");
              if (datain[3] == CS.HEIFANG) {
                guizezhi.select("黑");
              }
              else if (datain[3] == CS.HONGFANG) {
                guizezhi.select("红");
              }
              guizeshijian.select(datain[4] + "");
              guizebushu.select(datain[5] + "");
              //guizebushu.select();
              guize.show();
            }
            else if (datain[2] == CS.SHUANGFANGGUIZE) {
              System.out.println("qidong双方规则。");

              djs1.setbushu(datain[5]);//步数为0；用0对应的用户名
              djs1.setshijian(datain[4]);//时间：分钟
              djs2.setbushu(datain[5]);
              djs2.setshijian(datain[4]);
              djs1.yonghuming = hongfyhm; //默认值
              if (datain[3] == CS.HONGFANG) {
                if (datain[6] == 0) { //索引
                  djs1.yonghuming = hongfyhm;
                  djs1.zhongwenming = hongfzwm;
                  djs1.dengji = hongfdj;
                  djs1.dengjifen = hongfdjf;
                  djs2.yonghuming = heifyhm; //默认值
                  djs2.zhongwenming = heifzwm;
                  djs2.dengji = heifdj;
                  djs2.dengjifen = heifdjf;
                  djs1.repaint();
                  djs2.repaint();
                }
                else if (datain[6] == 1) {
                  djs1.yonghuming = heifyhm;
                  djs1.zhongwenming = heifzwm;
                  djs1.dengji = heifdj;
                  djs1.dengjifen = heifdjf;
                  djs2.yonghuming = hongfyhm; //默认值
                  djs2.zhongwenming = hongfzwm;
                  djs2.dengji = hongfdj;
                  djs2.dengjifen = hongfdjf;
                  djs1.repaint();
                  djs2.repaint();

                }
              }
              else if (datain[3] == CS.HEIFANG) {
                if (datain[6] == 1) {//执黑
                  djs1.yonghuming = hongfyhm;
                  djs1.zhongwenming = hongfzwm;
                  djs1.dengji = hongfdj;
                  djs1.dengjifen = hongfdjf;
                  djs2.yonghuming = heifyhm; //默认值
                  djs2.zhongwenming = heifzwm;
                  djs2.dengji = heifdj;
                  djs2.dengjifen = heifdjf;
                  djs1.repaint();
                  djs2.repaint();
                }
                else if (datain[6] == 0) {
                  djs1.yonghuming = heifyhm;
                  djs1.zhongwenming = heifzwm;
                  djs1.dengji = heifdj;
                  djs1.dengjifen = heifdjf;
                  djs2.yonghuming = hongfyhm; //默认值
                  djs2.zhongwenming = hongfzwm;
                  djs2.dengji = hongfdj;
                  djs2.dengjifen = hongfdjf;
                  djs1.repaint();
                  djs2.repaint();
                }
              }
              //if(datain[3]==CS.HEIFANG){}
              System.out.println("收到确定的规则消息。");
              xiang.qidong(datain[3]);
              xiang.setEnabled(true);
            } else if (datain[2] == CS.DUANXIAN) {
              //断线通知对话框
            } else if (datain[2] == CS.ZHONGJUTUICHU) {
              //todo?对话框
            }
            else if (datain[2] == CS.SHIJIAN) {
              if(datain[3]==CS.HEIFANG){
                System.out.println("客户端黑方时间原为"+xiang.djs2.shengyu+"秒");

                xiang.djs1.setshijian(datain[4],datain[5]);
                System.out.println("客户端黑方时间同步为"+xiang.djs2.shengyu+"秒");


              }else if(datain[3]==CS.HONGFANG){
                System.out.println("客户端红方时间原为"+xiang.djs1.shengyu+"秒");

                xiang.djs1.setshijian(datain[4],datain[5]);
                System.out.println("客户端红方时间同步为"+xiang.djs1.shengyu+"秒");

              }
            }

          }
        }

        /*else if (datain[0] == CS.QQKHXX) { //回复的客户信息。
          System.out.println("收到回复的客户信息");
          byte[] datain2 = new byte[datain.length - 1];
          System.arraycopy(datain, 1, datain2, 0, datain2.length);
          // String[][]khxx=new String[2][6];
          khxx = ClsByte.translate(datain2, (byte) '\r', (byte) '\n');
          hongfyhm = khxx[0][0];
          heifyhm = khxx[1][0];
          hongfzwm = khxx[0][1];
          heifzwm = khxx[1][1];
          //hongtxmc = "Red0.jpg";
          //heitxmc = "Blue0.jpg";
          hongfdj = khxx[0][2];
          heifdj = khxx[1][2];
          hongfdjf = khxx[0][3];
          heifdjf = khxx[1][3];
           System.out.println("heifangdjf="+heifdjf);
          djs1.yonghuming = hongfyhm; //默认值
          djs1.zhongwenming = hongfzwm;
          djs1.dengji = hongfdj;
          djs1.dengjifen = hongfdjf;
          djs2.yonghuming = heifyhm; //默认值
          djs2.zhongwenming = heifzwm;
          djs2.dengji = heifdj;
          djs2.dengjifen = heifdjf;
          djs1.repaint();
          djs2.repaint();
          //kaishi.setEnabled(true);
                 }*/

      }
    }
  }

  //Construct the frame
  public Frame1(String[] arg) {

    serverIP = arg[0];
    roomID = arg[1];
    userID = arg[2];
    username = arg[3];
    pangguanbiaoji = arg[4];

    System.out.println(arg[0]);
    System.out.println(arg[1]);
    System.out.println(arg[2]);
    System.out.println(arg[3]);
    System.out.println(arg[4]);

    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Frame1() {

    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    byte i = 0;
    byte data[] = new byte[1024]; //发出数据。

    String imagename;
    System.out.print("jbinit()");
    tuichu = new Button("退出");
    tuichu.setEnabled(true);
    huiqi = new Button("悔棋");
    huiqi.setEnabled(false);
    kaishi = new Button("开始");
    kaishi.setEnabled(false);

    gnmb = new JPanel(); //功能面板
    contentPane = (JPanel)this.getContentPane();
    titledBorder1 = new TitledBorder("");
    contentPane.setLayout(null);
    this.setSize(new Dimension(800, 546));
    this.setTitle("象棋网络对弈：" + hongfzwm + "－" + heifzwm);
    //statusBar.setText("正常 ");

    //contentPane.add(statusBar, BorderLayout.SOUTH);

    //JPanel zuo = new JPanel();
    // JPanel you = new JPanel();
    //zuo.setLayout(new BorderLayout());
    //you.setLayout(null) ;
    djs1 = new Daojishi(hongfyhm, hongfzwm, hongfdj, hongfdjf, (byte) 0);
    djs2 = new Daojishi(heifyhm, heifzwm, heifdj, heifdjf, (byte) 1);
    //zuo.add(djs1, BorderLayout.NORTH);
    //zuo.add(djs2, BorderLayout.CENTER);
    contentPane.add(djs1);
    contentPane.add(djs2);
    //contentPane.add(zuo, BorderLayout.WEST);
    //contentPane.add(djs2, BorderLayout.WEST);
    djs1.setBounds(0, 0, 150, 256);
    djs2.setBounds(0, 256, 150, 256);

    myClient = new ClsClient(serverIP, 16002, 30000);
    xiang = new Board1(djs1, djs2, myClient);

    xiang.init();
    contentPane.add(xiang);
    xiang.setBounds(150, 0, 461, 512);

    guanzhanzhe = new PopList();
    /*guanzhanzhe.add("加以比1\t" + "yu\t" + "2500");
         guanzhanzhe.add("加以比10");*/

    //guanzhanzhe.add(popup);

    //pm = new JPopupMenu();
    ///pm.add(mi);
    /*ActionListener a1 = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.print("=" + ( (JMenuItem) e.getSource()).getText());
      }
         };*/
    //guanzhanzhe.add(pm);
    //you.setSize(200,512);
    //you.add(kaishi);
    gnmb.setSize(189, 512);
    gnmb.setBackground(Color.orange);
    gnmb.setLayout(null);
    gnmb.add(guanzhanzhe);
    gnmb.add(kaishi);
    gnmb.add(tuichu);
    gnmb.add(huiqi);

    tuichu.setBounds(15, 15, 60, 30);
    tuichu.addActionListener(new TuichuButtonListener(this));
    kaishi.setBounds(15, 55, 60, 30);
    kaishi.addActionListener(new KaishiButtonListener(this));
    huiqi.setBounds(95, 15, 60, 30);
    huiqi.addActionListener(new HuiqiButtonListener(this));
    guanzhanzhe.setBounds(0, 100, 189, 200);

    kaishi.setVisible(true);
    tuichu.setVisible(true);
    huiqi.setVisible(true);
    guanzhanzhe.setVisible(true);
    //contentPane.add(you,BorderLayout.EAST);
    //you.setVisible(true);
    contentPane.add(gnmb);
    gnmb.setBounds(611, 0, 189, 512);
    //xiang.qidong( (byte) 0);
    xiang.setEnabled(false); //
    xiang.setVisible(true);

    guize = new Dialog(this, "协商规则", true);
    guize.setLayout(null);
    guize_tuichu = new Button("退出");
    tongyi = new Button("确定");

    guize_zhi = new Label("本方执：");
    guize_mei = new Label("分钟每");
    guize_bu = new Label("步");

    guizeshijian.add("1");
    guizeshijian.add("5");
    guizeshijian.add("10");
    guizeshijian.add("15");
    guizeshijian.add("25");

    guizebushu.add("1");
    guizebushu.add("10");
    guizebushu.add("15");
    guizebushu.add("25");

    guizezhi.add("红");
    guizezhi.add("黑");

    Listener listener = new Listener();
    guizeshijian.addItemListener(listener);
    guizebushu.addItemListener(listener);
    guizezhi.addItemListener(listener);
    tongyi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String zhihong;
        dataout[0] = 3;
        dataout[1] = 6;
        dataout[2] = CS.GUIZE;
        if (DANFANG == false) {
          dataout[3] = CS.DANFANGGUIZE;
        }
        else {
          dataout[3] = CS.SHUANGFANGGUIZE;
        }
        zhihong = guizezhi.getSelectedItem();
        if (zhihong.equals("黑")) {
          dataout[4] = (byte) 1;
        }
        else if (zhihong.equals("红")) {
          dataout[4] = (byte) 0;
        }

        //=(byte)guizezhi.getSelectedIndex();

        System.out.println("发出dataout[4]" + dataout[4]);
        dataout[5] = (byte) Integer.parseInt(guizeshijian.getSelectedItem());
        System.out.println("发出dataout[5]" + guizeshijian.getSelectedItem());
        dataout[6] = (byte) Integer.parseInt(guizebushu.getSelectedItem());
        System.out.println("发出dataout[6]" + guizebushu.getSelectedItem());

        guize.dispose();
        myClient.sendData(dataout);
        System.out.println("dispose");

        //guize.dispose();
        //发出“再来一盘”的信号
      }
    });
    guize_tuichu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 2, (byte) 2,
                                             "LOGO\r" + userID + "\r\n"), 0,
                          0);

        System.out.println("发出tuichu的信号。");
        guize.dispose();
        System.exit(0);
        //发出“再来一盘”的信号
      }
    });
    guize.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.out.println("window closing.");
        System.out.println("强行退出");
        //System.out.println("就此结束");
        myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 2, (byte) 2,
                                             "LOGO\r" + userID + "\r\n"), 0,
                          0);

        System.out.println("发出强行退出的信号。");
        guize.dispose();
        System.exit(0);

      }

      public void windowClosed(WindowEvent e) {
        //System.out.println("window closed.");
      }
    });
    guize.setSize(320, 150);
    guize.add(guize_zhi);
    guize.add(guizezhi);
    guize.add(guizeshijian);
    guize.add(guize_mei);
    guize.add(guizebushu);
    guize.add(tongyi);
    guize.add(guize_tuichu);

    guize.setBounds(250, 250, 320, 150);
    guize_zhi.setBounds(15, 45, 60, 30);
    guizezhi.setBounds(75, 45, 50, 30);
    guizeshijian.setBounds(125, 45, 50, 30);
    guize_mei.setBounds(175, 45, 60, 30);
    guizebushu.setBounds(235, 45, 50, 30);
    guize_bu.setBounds(285, 45, 25, 30);
    tongyi.setBounds(30, 100, 60, 30);
    guize_tuichu.setBounds(210, 100, 60, 30);

    //shanchupangguanzhe= new JDialog(this, "重新开始", true);;

    //chongxinkaishi.setLocation(150,150);

    //显示信息的对话框。
    duifangqiangxingtuichu = new Dialog(this, "对方强行退出象棋游戏", true);
    duifangqiangxingtuichu.setLayout(null);
    qiangxingtuichul = new Label("对方已经强行退出！请按确定关闭游戏。");
    duifangqiangxingtuichu.add(qiangxingtuichul);
    qiangxingtuichu = new Button("确定");
    duifangqiangxingtuichu.add(qiangxingtuichu);
    duifangqiangxingtuichu.setSize(250, 150);
    qiangxingtuichul.setBounds(15, 50, 220, 30);
    qiangxingtuichu.setBounds(95, 105, 60, 30);
    duifangqiangxingtuichu.setBounds(250,250,250,150);
    qiangxingtuichu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        duifangqiangxingtuichu.dispose();
        System.out.println("duifangqiangtui,jieshu");
        System.exit(0);
        //发出“再来一盘”的信号
      }
    });

    duifangbutongyihuiqi = new Dialog(this, "对方不同意您悔棋", true);
    duifangbutongyihuiqi.setLayout(null);
    duifangbutongyihuiqi.setSize(250, 150);
    butongyihuiqil = new Label("对方不同意您退出！请按确定继续下棋。");
    duifangbutongyihuiqi.add(butongyihuiqil);
    butongyihuiqi = new Button("确定");
    duifangbutongyihuiqi.add(butongyihuiqi);
    butongyihuiqil.setBounds(15, 50, 220, 30);
    butongyihuiqi.setBounds(95, 105, 60, 30);
     duifangbutongyihuiqi.setBounds(250,250,250,150);
    butongyihuiqi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        duifangbutongyihuiqi.dispose();
        System.out.println("duifang不同意悔棋");
        //System.exit(0);
        //发出“再来一盘”的信号
      }
    });

    duifangbutongyituichu = new Dialog(this, "对方不同意您退出象棋游戏", true);
    duifangbutongyituichu.setSize(250, 150);
    duifangbutongyituichu.setLayout(null);
    butongyituichul = new Label("对方不同意您退出！请按确定继续下棋。");
    duifangbutongyituichu.add(butongyituichul);
    butongyituichu = new Button("确定");
    duifangbutongyituichu.add(butongyituichu);
    butongyituichul.setBounds(15, 50, 220, 30);
    butongyituichu.setBounds(95, 105, 60, 30);
    duifangbutongyituichu.setBounds(250,250,250,150);
    butongyituichu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        duifangbutongyituichu.dispose();
        System.out.println("对方不同意您退出");

      }
    });

    shanchupangguanzhe = new Dialog(this, "您不能删除该旁观者", true);
    shanchupangguanzhe.setSize(250, 150);
    pangguanzhel = new Label("您不能删除该旁观者！请按确定继续。");
    shanchupangguanzhe.add(pangguanzhel);
    pangguanzhe = new Button("确定");
    shanchupangguanzhe.add(pangguanzhe);
    shanchupangguanzhe.setLayout(null);
    pangguanzhel.setBounds(15, 50, 220, 30);
    pangguanzhe.setBounds(95, 105, 60, 30);
    shanchupangguanzhe.setBounds(250,250,250,150);
    pangguanzhe.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        shanchupangguanzhe.dispose();
        System.out.println("您无权删除该旁观者");
        //System.exit(0);
        //发出“再来一盘”的信号
      }
    });



    benfanghuosheng = new Dialog(this, "恭喜获胜！", true);
    benfanghuosheng.setLayout(null);
    benfanghuosheng.setSize(250, 150);
    bfhslabel = new Label("恭喜！您已经获胜！！！");
    benfanghuosheng.add(bfhslabel);
    bfhsbutton = new Button("确定");
    benfanghuosheng.add(bfhsbutton);
    bfhslabel.setBounds(15, 50, 220, 30);
    bfhsbutton.setBounds(95, 105, 60, 30);
    bfhsbutton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        benfanghuosheng.dispose();
        System.out.println("本方获胜");
        chongxinkaishi.setBounds(250,250,250,150);
        chongxinkaishi.show();
        //System.exit(0);
        //发出“再来一盘”的信号
      }
    });


    duifanghuosheng = new Dialog(this, "对方获胜", true);
    duifanghuosheng.setLayout(null);
    duifanghuosheng.setSize(250, 150);
    dfhslabel = new Label("您的对手已经获胜。");
    duifanghuosheng.add(dfhslabel);
    dfhsbutton = new Button("确定");
    duifanghuosheng.add(dfhsbutton);
    dfhslabel.setBounds(15, 50, 220, 30);
    dfhsbutton.setBounds(95, 105, 60, 30);
    duifanghuosheng.setBounds(250,250,250,150);
    dfhsbutton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        duifanghuosheng.dispose();
        System.out.println("对方获胜");
        chongxinkaishi.setBounds(250,250,250,150);
        chongxinkaishi.show();
        //System.exit(0);
        //发出“再来一盘”的信号
      }
    });


    cxkslabel = new Label("是否和对手再下一局？");
    cxksyes = new Button("是");
    cxksno = new Button("否");

    chongxinkaishi = new Dialog(this, "重新开始", true);
    chongxinkaishi.setLayout(null);
    chongxinkaishi.add(cxkslabel);
    chongxinkaishi.add(cxksyes);
    chongxinkaishi.add(cxksno);
    chongxinkaishi.setSize(250, 150);
    cxkslabel.setBounds(15, 45, 120, 30);
    cxksyes.setBounds(15, 100, 60, 30);
    cxksno.setBounds(140, 100, 60, 30);
    cxksyes.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chongxinkaishi.dispose();
        System.out.println("发出再来一盘的信号");
        dataout[0] = (byte) 3;
        dataout[1] = (byte) 6;
        dataout[2] = (byte) CS.GUIZE;
        dataout[3] = (byte) CS.QINGQIUGUIZE;
        myClient.sendData(dataout);
        //发出“再来一盘”的信号
      }
    });
    cxksno.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chongxinkaishi.dispose();
        System.out.println("就此结束,FACHU信号");
        dataout[0] = (byte) 3;
        dataout[1] = (byte) 6;
        dataout[2] = (byte) CS.GUIZE;
        dataout[3] = (byte) CS.ZHONGJUTUICHU;

        myClient.sendData(dataout);
        //发出“JIESHU”的信号
      }
    });

    duifangqingqiutuichu = new Dialog(this, "请求退出", true); //是否同意对方退出；
    tuichuyes = new Button("是");
    tuichuno = new Button("否");
    tuichulabel = new Label("您的对手请求退出，您是否同意对手退出？");

    duifangqingqiutuichu.setLayout(null);
    duifangqingqiutuichu.add(tuichulabel);
    duifangqingqiutuichu.add(tuichuyes);
    duifangqingqiutuichu.add(tuichuno);
    duifangqingqiutuichu.setSize(250, 150);
    tuichulabel.setBounds(15, 45, 220, 30);
    tuichuyes.setBounds(30, 100, 60, 30);
    tuichuno.setBounds(160, 100, 60, 30);
    tuichuyes.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        duifangqingqiutuichu.dispose();
        System.out.println("允许对方退出。");
        myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 2, (byte) 3,
                                             "OK\r" + otheruserID + "\r\n"), 0,
                          0);

        System.exit(0);
        //发出“再来一盘”的信号
      }
    });
    tuichuno.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        duifangqingqiutuichu.dispose();
        System.out.println("不同意对方退出");
        // myClient.s
        //发出“再来一盘”的信号
        myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 2, (byte) 3,
                                             "NO\r" + otheruserID + "\r\n"), 0,
                          0);
      }
    });

    duifangqingqiutuichu.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.out.println("window closing.");
        System.out.println("不同意对方退出");
        //System.out.println("就此结束");
        myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 2, (byte) 3,
                                             "NO\r" + otheruserID + "\r\n"), 0,
                          0);
        duifangqingqiutuichu.dispose();
        //System.exit(0);

      }

      public void windowClosed(WindowEvent e) {
        //System.out.println("window closed.");
      }
    });

    //请求悔棋的对话框。
    duifangqingqiuhuiqi = new Dialog(this, "请求悔棋", true); ; //是否同意对方悔棋。
    duifangqingqiuhuiqi.setLayout(null);
    huiqilabel = new Label("对手请求悔棋，是否同意对手悔棋？");
    huiqiyes = new Button("是");
    huiqino = new Button("否");

    duifangqingqiuhuiqi.add(huiqilabel);
    duifangqingqiuhuiqi.add(huiqiyes);
    duifangqingqiuhuiqi.add(huiqino);
    duifangqingqiuhuiqi.setSize(250, 150);
    huiqilabel.setBounds(15, 45, 220, 30);
    huiqiyes.setBounds(30, 100, 60, 30);
    huiqino.setBounds(160, 100, 60, 30);
    huiqiyes.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        duifangqingqiuhuiqi.dispose();
        System.out.println("允许对方悔棋。");
        myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 6,
                                             CS.SHUANGFANGHUIQI, "OK\r\n"), 0,
                          0);

        //对方悔棋，注意计时。
        System.out.println("我方为"+xiang.mycolor);
        System.out.println("轮走："+xiang.turncolor);

        if (xiang.mycolor == xiang.turncolor) { //还没有应对，悔一步
          System.out.println("还没有应对，悔一步");
          xiang.huiqi();
          xiang.bukexia=true;
          if (xiang.mycolor == CS.HEIFANG) {
            System.out.println("我是黑方，轮红方走");

            djs2.stop();
            djs1.init();
            djs1.start();

          }
          else if (xiang.mycolor == CS.HONGFANG) {
            System.out.println("我是红方，轮黑方走");

            djs1.stop();
            djs2.init();
            djs2.start();

          }
        }
        else { //已经作了应对，要悔两步。
          System.out.println("已经作了应对，要悔两步");
          xiang.huiqi();
          xiang.huiqi();
          xiang.bukexia=true;
          if (xiang.mycolor == CS.HEIFANG) {
            System.out.println("我是黑方，轮红方走");

            djs2.stop();
            djs1.init();
            djs1.start();

          }
          else if (xiang.mycolor == CS.HONGFANG) {
            System.out.println("我是红方，轮黑方走");

            djs1.stop();
            djs2.init();
            djs2.start();

          }

        }

      }
    });
    huiqino.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        duifangqingqiuhuiqi.dispose();
        System.out.println("不同意对方悔棋");
        // myClient.s
        //发出“再来一盘”的信号
        myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 6,
                                             CS.HUIQIBUTONGYI,
                                             "NO\r\n"), 0, 0);
      }
    });

    duifangqingqiuhuiqi.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.out.println("window closing.");
        System.out.println("不同意对方悔棋");

        myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 6,
                                             CS.HUIQIBUTONGYI,
                                             "NO\r\n"), 0, 0);
        duifangqingqiuhuiqi.dispose();

      }

      public void windowClosed(WindowEvent e) {
        //System.out.println("window closed.");
      }
    });

    //djs.setSize(150,520);
    djs1.setEnabled(false); //不接收输入。
    djs2.setEnabled(false);
    djs1.init();
    djs2.init();
    //djs1.start();

    myClient.start();
    myClient.addObserver(this);
    myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 1, (byte) 1,
                                         "LOGI\r" + roomID + "\r" + userID +
                                         "\r" + username + "\r\n"), 0, 0);
    //myClient.sendData(data);
    System.out.println("fachu初始登录请求.");

  }

  //File | Exit action performed
  /* public void jMenuFileExit_actionPerformed(ActionEvent e) {
     System.exit(0);
   }*/

  //Help | About action performed
  /* public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
     Frame1_AboutBox dlg = new Frame1_AboutBox(this);
     Dimension dlgSize = dlg.getPreferredSize();
     Dimension frmSize = getSize();
     Point loc = getLocation();
     dlg.setLocation( (frmSize.width - dlgSize.width) / 2 + loc.x,
                     (frmSize.height - dlgSize.height) / 2 + loc.y);
     dlg.setModal(true);
     dlg.pack();
     dlg.show();
   }*/

  //Overridden so we can exit when window is closed

  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {

      //chongxinkaishi.setBounds(150, 150, 250, 150);
      //chongxinkaishi.setC
      //chongxinkaishi.show();
      myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 2, (byte) 2,
                                           "LOGO\r\n"), 0, 0);
      System.out.print("fachu强退信号");
      System.exit(0);
    }
  }

  class Listener
    implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
      DANFANG = false;
    }
  }

  /*public void receivedata(byte[] data) {
    //这个函数需要从外部调用。比如网络处理模块得到
    //服务器传来的数据，再调用该函数。需要进一步细化。
    //接收到的数据如何处理
    byte i, j, k;
    if (data[0] == CS.QIDONG) {*/
  /*hongfyhm =new String(data,1,20);
         //如何从字节数组的到字符串。
         hongfzwm =new String(data,21,20);
         //中文名不能超过10个字符。
         hongfdj =new String(data,41,20);
         heifyhm =new String(data,61,20);
         heifzwm =new String(data,81,20);
         heifdj =new String(data,101,20);*/
  /*   xiang.qidong(data[102]);
   }
   else if (data[0] == CS.QIPU) {
   }
     }*/

}

/*class Frame1_jMenuFileExit_ActionAdapter
  implements ActionListener {
  Frame1 adaptee;
  Frame1_jMenuFileExit_ActionAdapter(Frame1 adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuFileExit_actionPerformed(e);
  }
 }
 class Frame1_jMenuHelpAbout_ActionAdapter
  implements ActionListener {
  Frame1 adaptee;
  Frame1_jMenuHelpAbout_ActionAdapter(Frame1 adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuHelpAbout_actionPerformed(e);
  }
 }
 class KaishiButtonListener
  implements ActionListener {
  private Frame1 fr;
  public KaishiButtonListener(Frame1 frame) {
    fr = frame;
  }
  public void actionPerformed(ActionEvent event) {
    Button source = (Button) event.getSource();
    //System.out.println(source.getLabel() + "jihuo");
    //发出开始的信号。
  }
 }*/

class HuiqiButtonListener
  implements ActionListener {
  private Frame1 fr;
  public HuiqiButtonListener(Frame1 frame) {
    fr = frame;
  }

  public void actionPerformed(ActionEvent event) {
    Button source = (Button) event.getSource();
    //System.out.println(source.getLabel() + "jihuo");
    //发出悔棋的信号
    /*byte[] datahuiqi = new byte[1024];
         datahuiqi[0] = (byte) 3;
         datahuiqi[1] = (byte) 0;
         datahuiqi[2] = (byte) 0;
         datahuiqi[3] = (byte) CS.HUIQI;*/
    fr.myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 6,
                                            (byte) CS.HUIQI,
                                            fr.userID + "\r\n"), 0, 0);

    fr.huiqi.setEnabled(false);
    System.out.println("发出悔棋的信号。");

  }
}

class KaishiButtonListener
  implements ActionListener {
  private Frame1 fr;
  public KaishiButtonListener(Frame1 frame) {
    fr = frame;
  }

  public void actionPerformed(ActionEvent event) {
    Button source = (Button) event.getSource();
    //System.out.println(source.getLabel() + "jihuo");
    //发出开始的信号
    byte[] dataqidong = new byte[1024];
    dataqidong[0] = (byte) 3;
    dataqidong[1] = (byte) 6;
    dataqidong[2] = (byte) CS.JUSHOU;
    //dataqidong[3] = (byte) CS.JUSHOU;
    fr.myClient.sendData(dataqidong);
    fr.kaishi.setEnabled(false);
    System.out.println("发出开始的信号。");
  }
}

class TuichuButtonListener
  implements ActionListener {
  private Frame1 fr;
  public TuichuButtonListener(Frame1 frame) {
    fr = frame;
  }

  public void actionPerformed(ActionEvent event) {
    //Button source = (Button) event.getSource();
    //System.out.println(source.getLabel() + "jihuo");
    //发出开始的信号

    //fr.myClient.sendData(dataqidong);
    fr.myClient.sendData(ClsByte.tranToByte( (byte) 3, (byte) 2, (byte) 1,
                                            "LOGO\r" + fr.userID + "\r\n"), 0,
                         0);
    fr.tuichu.setEnabled(false);
    System.out.println("发出tuichu的信号。");

  }

}

class PopList
  extends List {
  JMenuItem mi = new JMenuItem("踢走");
  JPopupMenu pm;
  PopupMenu popup = new PopupMenu();
  MenuItem popitem1, popitem2, popitem3;
  PopupActionListener popuplistener;
  public PopList() {
    super();
    add(popup);
    popitem1 = new MenuItem("踢走该观战者");
    popitem2 = new MenuItem("邀请该观战者对弈");
    popitem3 = new MenuItem("查看该观战者相关信息");
    popup.add(popitem1);
    popup.add(popitem2);
    popup.add(popitem3);
    popuplistener = new PopupActionListener();
    popitem1.addActionListener(popuplistener);
    popitem2.addActionListener(popuplistener);
    popitem3.addActionListener(popuplistener);

    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        //(MenuItem)e.getSource();
        //super.mousePressed();

        showPopup(e);
      }

      public void mouseClicked(MouseEvent e) {

        showPopup(e);
      }

      public void mouseReleased(MouseEvent e) {
        showPopup(e);
      }

    }
    );

  }

  void showPopup(MouseEvent e) {
    if (e.isPopupTrigger()) {
      popup.show(this, e.getX(), e.getY());
    }
  }

  class PopupActionListener
    implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      MenuItem mi = (MenuItem) e.getSource();
      //System.out.println(mi);
      if (mi.equals(popitem1)) {
        //System.out.println("tizhoupanguanzhe");
        //发出踢走观战者的信号。
      }
      else if (mi.equals(popitem2)) {
        //System.out.println("yaoqingduiyi");
      }
      else if (mi.equals(popitem3)) {
        //System.out.println("chakanxinxi");
      }
      ////System.out.println(mi);
    }
  }

}