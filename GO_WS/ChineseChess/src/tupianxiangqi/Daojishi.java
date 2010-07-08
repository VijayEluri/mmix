package tupianxiangqi;

import java.util.Date;
import java.net.URL;
import java.applet.Applet;
import java.awt.HeadlessException;
import java.awt.*;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Daojishi
  extends Canvas
  implements Runnable {
  Thread timer;//计时器线程。
  boolean bt = false;
  private static int MINUTE = 10;
  private static int BUSHU = 25; //步数

  int shengyu = MINUTE * 60; //剩余秒数。
  int bushu = BUSHU; //剩余步数

  int shengyubushu=bushu;
  Date jiu;//新旧计时点
  Date xin;

  String yonghuming = ""; //默认值
  String zhongwenming = "";
  String dengji = "";
  String dengjifen = "";

  Image image1; //背景图像
  Image touxiang; //头像

  byte color; //何方的时钟

  public void setshijian(byte shijian){
    shengyu=shijian*60;
  }
  public void setshijian(byte fen,byte miao){
    shengyu=(byte)(fen*60+miao);
  }

  public void setbushu(byte bushuin){
    bushu=bushuin;
  }
  public void init() {
    this.setSize(150, 256);
    image1 = new ImageIcon(tupianxiangqi.Frame1.class.getResource(
      "pieceTex.jpg")).getImage();
    if (color == CS.HONGFANG) {
      touxiang = new ImageIcon(tupianxiangqi.Frame1.class.getResource(
        "Red0.jpg")).getImage();
    }
    else if (color == CS.HEIFANG) {
      touxiang = new ImageIcon(tupianxiangqi.Frame1.class.getResource(
        "Blue0.jpg")).getImage();
    }
    this.setVisible(true);
    jiu = new Date();
    //start();
    //repaint();

  }

  public void stop() {
    if (timer != null) {
      timer.stop();
      timer = null;
      bt = false;
      ////System.out.println("timer!=null");
    }
    else {
      ////System.out.println("timer=null");
    }
  }

  public void start() {
    if (timer == null) {
      timer = new Thread(this);
      bt = true;//线程进行中的标志
      timer.start();
    }
  }

  public Daojishi(String yhm, String zwm, String dj, byte cl) throws
    HeadlessException {
    //System.getProperties().g
    super();
    yonghuming = yhm;
    zhongwenming = zwm;
    dengji = dj;
    color = cl;

    //jiu = new Date();
  }

  public Daojishi(String yhm, String zwm, String dj, String djf, byte cl) throws
    HeadlessException {
    //System.getProperties().g
    super();
    yonghuming = yhm;
    zhongwenming = zwm;
    dengji = dj;
    dengjifen = djf;
    color = cl;

    //jiu = new Date();
  }

  public void paint(Graphics g) {
    int yiyong = 0; //已经用掉的毫秒数。
    int minute; //剩余分钟数
    int second; //剩余秒数
    Graphics gcontext;
    Image work;

    work = this.createImage(150, 260);

    if (work == null) { //只能放在这里，否则不能正确初始化
      ////System.out.println("work=null");
      gcontext = work.getGraphics();
    }
    else {
      ////System.out.println("work!=null");
      gcontext = work.getGraphics(); //gcontext未能正确赋值。
      if (gcontext == null) {
        return;
      }
      ////System.out.println("gcontext!=null");
    }
    gcontext.drawImage(image1, 0, 0, 150, 256, this);
    gcontext.drawImage(touxiang, 30, 5, 30, 30, this);
    //gcontext.drawString(yonghuming, 30, 80);
    gcontext.drawString("还有 "+shengyubushu+" 步", 30, 80);
    gcontext.drawString(zhongwenming, 30, 110);
    gcontext.drawString(dengji, 30, 140);
    gcontext.drawString(dengjifen, 30, 170);

    if (bt == false) { //非计时当中
      minute = shengyu / 60;
      second = shengyu - minute * 60;
      //gcontext.drawImage(image1,0,0,150,260,this);
      //gcontext.drawImage(touxiang,30,5,30,30,this);
      gcontext.setColor(Color.black);
      gcontext.drawString("剩余 " + (minute < 10 ? "0" : "") + minute + " 分 " +
                          (second < 10 ? "0" : "") + second + " 秒", 30, 50);
      gcontext.drawString("还有 "+shengyubushu+" 步", 30, 80);
      g.drawImage(work, 0, 0, this);
      return;
    }
    if (shengyu > 0) {

      xin = new Date();
      yiyong = (int) (xin.getTime() - jiu.getTime());
      jiu = xin;
      //System.out.print("yiyong=" + yiyong);
      shengyu = shengyu - yiyong / 1000;

      if (shengyu <= 0) {
        bt=false;
        shengyu = 0;
        //timer.destroy();
        timer.stop();
        gcontext.drawString("剩余 " + "00 分 " + "00 秒", 30, 50);
        g.drawImage(work, 0, 0, this);
        return;
      }
      minute = shengyu / 60;
      second = shengyu - minute * 60;
      gcontext.setColor(Color.black);
      gcontext.drawString("剩余 " + (minute < 10 ? "0" : "") + minute + " 分 " +
                          (second < 10 ? "0" : "") + second + " 秒", 30, 50);

    }
    else {
      gcontext.drawString("剩余 " + "00 分 " + "00 秒", 30, 50);
    }

    g.drawImage(work, 0, 0, this);
  }

  /*public void update(Graphics g){
     paint(g);
     }*/
  public void run() {
    jiu=new Date();
    while (timer != null) {

      try {

        Thread.sleep(1000L);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
       repaint();
    }
  }
}