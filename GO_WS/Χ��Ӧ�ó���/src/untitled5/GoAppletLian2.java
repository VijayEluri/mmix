//package untitled5;
//
//import java.applet.Applet;
//import java.awt.Color;
//import java.awt.Event;
//import java.awt.Graphics;
//
//import untitled8.GoBoardLian1;
////��Ŀ10����GoAppletLian����GoboardLian1
////����Դ������ͬһĿ¼�£�ȷ�����ͨ�š�
////���кܶ����δϸ�¼�顣
//public class GoAppletLian2 extends Applet {
//   public final boolean DEBUG=true;
//   boolean KEXIA=true;
//   boolean CHONGHUI=true;
//   GoBoardLian1 go=new GoBoardLian1();
//   GoBoardLian2 goboard=new GoBoardLian2(go);
//   public void update (Graphics g){
//      paint(g);
//   }
//   public void init(){
//
//
//   }
//   public void  paint(Graphics g){
//      if(CHONGHUI==true){
//         g.setColor(Color.orange);
//         g.fillRect(0,0,560,560);
//      }
//      CHONGHUI=true;
//      g.setColor(Color.black) ;
//      byte kinp=0;
//      for (int i=1; i<=19; i++)//����
//      {
//         g.drawLine(18,28*i-10,522,28*i-10);//hor
//         g.drawLine(28*i-10,18,28*i-10,522);//ver
//      }
//      for (int i=0; i<3;i++){  //����λ
//         for ( int j=0; j<3;j++){
//            g.fillOval(168*i+99,168*j+99,6,6);
//         }
//      }
//
//      for(int i=1;i<=19;i++){//�����ӵ�
//         for(int j=1;j<=19;j++){
//            if(goboard.zb[i][j][0]==1) {
//               g.setColor (Color.black);
//               g.fillOval(28*i-24,28*j-24,28,28);
//               //System.out.println("//paint the black point.");
//            }
//            else  if(goboard.zb[i][j][0]==2){
//               g.setColor(Color.white);
//               g.fillOval(28*i-24,28*j-24,28,28);
//               // System.out.println("//paint the white point.");
//            }
//            kinp=goboard.zb[i][j][goboard.KSYXB];
//            if(kinp!=0&DEBUG==true){//������
//                g.setColor(Color.green);
//                g.drawString(""+kinp,28*i-4,28*j-4) ;
//
//            }
//         }
//      }
//   }//else���������̺�����
//
//   public boolean mouseDown(Event e,int x,int y){//�����������
//      if(KEXIA==true){
//         //KEXIA=false;//ֻ�л������һ��,���ܼ���.
//         byte a=(byte)((x-4)/28+1);//����������ӵ�.
//         byte b=(byte)((y-4)/28+1);
//         goboard.cgcl(a,b);
//         goboard.output();
//         CHONGHUI=false;
//         repaint();
//         System.out.println("Gocanvas de mousedown");
//         return true;//����������,��Frame����
//      }
//      else  return true;
//   }
//
//  public boolean handleEvent(Event evt){
//      if(evt.id==Event.MOUSE_DOWN)  return mouseDown(evt,evt.x,evt.y);
//      else return super.handleEvent(evt);
//   }
//
//   public GoAppletLian2() {
//      super();
//   }
//}
//
//

