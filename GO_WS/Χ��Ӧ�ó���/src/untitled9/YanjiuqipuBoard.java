package untitled9;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 * ƭ��Ҳ�����ã���Ϊƭ��֮��ı仯Ҳ�Ƿǳ���ģ��������������ʱ
 * ȫ�����ƿ����ʺ�ʹ��ƭ�š���ʱ�͸���Ч�ˡ�Ψһ�������ǣ���ƭ�ž�
 * ��Ϊ�˱ܿ���Ҷ���Ϥ�Ķ�ʽ��·������������Ѷ���ƭ�ŵ��ռ���Խ
 * ��Խ�ߣ�Ч����̫���룬������Լ����о��ĵ���Ϊƭ�ţ���Ϊ����һ��
 * ���������ȸպ�Ҳ�о�����Ҫ����ÿ����ʽ���߲��ֶ���������һ������
 * ������
 * �����׵�ѧϰ�о��ǻ����͵ģ�������Ϊ���������֮�⣬��ʼ����ǰ��
 * �Ƿ���ȷ��Ҫʵ��������ܣ�������������жϣ�ͨ��ǰ������ƶԱ���
 * �ó����ۡ���Ϊ�仯Խ���󣬽���Խ׼ȷ����򵥵ľ���û�����Է��ĳ�
 * ���ֶ���Ҫ���塣
 */
import java.io.*;
import java.math.*;
import untitled3.DsNode;
import untitled8.GoBoardLian1;

public class YanjiuqipuBoard extends untitled8.GoBoardLian1 {
   DsNode root, temp, work, temp1, temp2;
   DsNode[] stackds = new DsNode[25]; //SGF��ʽ��
   short zshoushu;//���׵���������
   byte[][] zuijin=new byte[21][21];//ÿ�����ؿ�š�
   byte[] zuijinkh=new byte[4];//���������Ŀ�š�
   byte[] zuijinfs=new byte[4] ;//�����Ľӽ���ʽ��
   byte dangqiankh;
   final static byte ZKUOHAO = -127;
   final static byte YKUOHAO = 127;
   byte xgkjs;//��ؿ������

   public YanjiuqipuBoard(String qipuwenjianming) {
      //�������ļ��������ײ���ʼ��hui����,zshoushu�ĸ�ֵ
      //�ɰ�����ͬ�����ļ���ת����

     //��һ�ֿ϶�Ϊ�����㣬��Ӧ�����Ͻǡ�
     if (hui[1][0] > 10 || hui[1][1] > 10) {
        System.out.println("���׸�ʽ����");
        trans(); //��Ϊ��׼��
      }


   }
   byte julia(byte a,byte b,byte c,byte d){//�����ľ���
      return (byte)(Math.abs(a-c)+Math.abs(b-d));
      //��һ�ֶ��壺��������ֵ�ĺ͡�
   }
   byte julib(byte a,byte b,byte c,byte d){//����ĵڶ��ֶ���
      byte m=(byte)(Math.abs(a-c));
      byte n=(byte)(Math.abs(b-d));
      byte t=0;
      if(m>n){
         t=n;
         n=m;
         m=t;
      }//���������ֵ��
         return n;
   }

   byte juli(byte a,byte b,byte c,byte d){//����ĵڶ��ֶ���
      byte m=(byte)(Math.abs(a-c));
      byte n=(byte)(Math.abs(b-d));
      //byte t=(byte)Math.sqrt(m*m+n*n);
      //return t;     //�����ľ���
      int t=m*m+n*n;
      if(t>127) return (byte)(-1);//��1��ʾ����̫Զ��
      else return (byte)t;        //�������ؾ����ƽ����
  }

   void trans(){
   //������תΪ��׼�͡�
   }

    public void init() { //��ʼ��,��ɽ���
     byte fenzhi = 0; //SGF��ʽ��,��stackds���ɶ�ջ

     try {
        byte lina = 0;
        byte[] dsnode = new byte[5];
        boolean ykhjs = false; //�Ƿ��Ѿ��������š�
        DataInputStream in = new DataInputStream(
           new BufferedInputStream(
           new FileInputStream("H:\\weiqidata\\" + "��ʽ��")));
        if (in.available() == 0) {
           return;
        }
        lina = in.readByte();
        if (lina == ZKUOHAO) { //���Ͷ�ʽ��Ӧ�õ�һ�����������ϱ仯��
           in.read(dsnode); //�����Ų���������
           root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                    dsnode[4], dsnode[0]);
           System.out.println("��ʽ���ĸ�Ϊ��(a="+
                              work.zba+","+work.zbb+")" );
           //�������������Ű�Χ
           //���Ž���ʾ�������Ǹ��ڵ�ķ�֧��
           fenzhi++;
           stackds[fenzhi] = work; //�����Ž������µ������Žӵ�work�ϡ�
           System.out.println("��һ�����ŵĹ�����Ϊ��(a="+
               stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
        }
        else { //��ʽ����ʼʱ����ֻ��һ����㡣
           in.read(dsnode, 1, 4);
           root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                    dsnode[4], lina);
           System.out.println("��ʽ���ĸ�Ϊ��(a="+
                              work.zba+","+work.zbb+")" );
        }
        while (in.available() != 0) {
           lina = in.readByte();
           if (lina == ZKUOHAO) {
              in.read(dsnode);
              temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                dsnode[4], dsnode[0]);
              System.out.println("��֧�仯���׽ڵ�Ϊ:"+ temp.zba
                                 +","+temp.zbb+")");
              if (ykhjs == false) {
                 //�µ�������
                 work.left = temp;
                 fenzhi++;
                 work = temp;
                 stackds[fenzhi] = work;
                 System.out.println("��һ�����ŵĹ�����Ϊ��(a="+
               stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
              }
              else { //�����Ž��������е�������
                 //�����µ������ţ�ykhjsʧЧ��
                 ykhjs=false;
                 work.right = temp;
                 System.out.println("ͬһ�����ŵ�ԭ������Ϊ��(a="+
               stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
                 work = temp;
                 stackds[fenzhi] = work;
                 System.out.println("ͬһ���µ������ŵĹ�����Ϊ��(a="+
               stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
              }
           }
           else if (lina == YKUOHAO) {
              if (ykhjs == false) {
                 ykhjs = true;
                 work = stackds[fenzhi];
                 System.out.println(work.toString() );
              }
              else {
                 stackds[fenzhi--] = null;
                 work = stackds[fenzhi];
              }
           }
           else { //�����ڵ�
              in.read(dsnode, 1, 4);
              temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                dsnode[4], lina);
              work.left = temp;
              work = temp;
           }
        }
        in.close();
     }
     catch (IOException ex) {
        System.out.println("���ļ�����ʽ�����������⣡");
        System.out.println("Exception" + ex.toString());
     }
     //���Ӳ������ĳ�ʼ����
  }

   void pipeidingshi(byte m,byte n){

      for(DsNode linshi=root;linshi!=null;linshi=linshi.right){
         if(linshi.zba ==m&&linshi.zbb==n){

         }
      }


   }
   //ͳ��ÿ�����ߣ������ӣ����塢�����ӵĸ�������Ϊ����Ĳο���׼
   void read(){//�������л�ȡ֪ʶ���洢�ڲ������У��õ�һ��׼��
      byte [][]juli={{0,0},{1,0},{1,1},{2,0},{2,1},{2,2},{3,0},
         {3,1},{3,2},{4,0},{4,1},{3,3},{4,2},{5,0},{4,3},{5,1}};
      //������ȵ���ͬ���Ϳ�����ת�ͷ�ת�õ���
      //��ͬ����������ʶ��ͬ���ͣ�ͬʱҲ���ֲ�ͬ�ľ���Զ����
      DsNode [] dingshiwork=new DsNode[4];
      //ƥ���Ľ�ʱ��ͬ�ĵ�ǰ�����㡣
      byte []othercolor=new byte[4];//��ؿ�����߷���ɫ��
      byte dingshishu=0;//�Ѿ��ҵ��Ķ�ʽ����
      byte syshoum=0;
      byte ssyshoum=0;
      byte syshoun=0;
      byte ssyshoun=0;
      byte julim=0;//�����ĺ�����֮�
      byte julin=0;//������������֮�
      byte zbm=0;  //������ĺ����ꣻ
      byte zbn=0;
      byte kongjiaoshu =4;//ʣ��Ŀս�����
      byte dangqianm,dangqiann;//���������е�ǰ��ĺ������ꣻ
      byte zjkjs=0;//�����ļ�����ͬһ�鲻�ܷ������롣
      byte t;
      short i;
      boolean tongkuai=false;
      byte j,k;
      for(i=1;i<=zshoushu;i++){//�������ס�
         //�򵥵İ����״��������У��ȱ��ĳ�����ø����ӵ�������ʽ
         //Ҳ���Լ򵥵�ת��Ϊһ�����顣
         dangqianm=hui[i][25];
         dangqiann=hui[i][26];
         cgcl(hui[i][25],hui[i][26]);

         if(i==1){//��һ�ֿ϶�Ϊ�����㣬��Ӧ�����Ͻǡ�
            if(dangqianm>10||dangqiann>10){
               System.out.println("���׸�ʽ����");
               //trans();��Ϊ
            }


         }else{//���ж��Ƿ�Ϊ������
            for(j=1;j<=11;j++){//������ɨ����㣻
               julim=juli[i][0];
               julin=juli[i][1];
               if(julim==julin||julin==0){//���ĸ��ԳƵ�
                 // ת����(a,b)��ɣ���b��a��
                 for(k=0;k<4;k++){
                     t=julim;
                     julim=(byte)(-julin);
                     julin=t;
                     zbm=(byte)(hui[i][25]+julim);
                     zbn=(byte)(hui[i][26]+julin);
                     if(zbm>0&&zbm<20&&zbn>0&&zbn<20){
                        if(zb[zbm][zbn][0]>0) {
                           tongkuai=false;
                           for(byte tkt=0;tkt<zjkjs;tkt++){
                              if(zuijin[zbm][zbn]==zuijinkh[tkt])
                                 tongkuai=true;
                           }
                           if(tongkuai=false){
                              zuijinkh[zjkjs]=zuijin[zbm][zbn];
                              zuijinfs[zjkjs++]=j;
                           }
                        }
                     }

                  }
               }
               else{//�а˸��ԳƵ㡣
                  //ͬ��ת��������
                  for(k=0;k<4;k++){
                     t=julim;
                     julim=(byte)(-julin);
                     julin=t;
                     zbm=(byte)(hui[i][25]+julim);
                     zbn=(byte)(hui[i][26]+julin);
                     if(zbm>0&&zbm<20&&zbn>0&&zbn<20){
                        if(zb[zbm][zbn][0]>0) {
                           tongkuai=false;
                           for(byte tkt=0;tkt<zjkjs;tkt++){
                              if(zuijin[zbm][zbn]==zuijinkh[tkt])
                                 tongkuai=true;
                           }
                           if(tongkuai=false){
                              zuijinkh[zjkjs]=zuijin[zbm][zbn];
                              zuijinfs[zjkjs++]=j;
                           }

                        }
                     }
                 }  //ѭ����julim��julin�ص�ԭֵ��
                 t=julim;
                 julim=julin;
                 julin=t;
                 for(k=0;k<4;k++){
                     t=julim;
                     julim=(byte)(-julin);
                     julin=t;
                     zbm=(byte)(hui[i][25]+julim);
                     zbn=(byte)(hui[i][26]+julin);
                     if(zbm>0&&zbm<20&&zbn>0&&zbn<20){
                        if(zb[zbm][zbn][0]>0) {
                           tongkuai=false;
                           for(byte tkt=0;tkt<zjkjs;tkt++){
                              if(zuijin[zbm][zbn]==zuijinkh[tkt])
                                 tongkuai=true;
                           }
                           if(tongkuai=false){
                              zuijinkh[zjkjs]=zuijin[zbm][zbn];
                              zuijinfs[zjkjs++]=j;
                           }

                        }
                     }
                 }
               }
            }//for j;
            if(zjkjs==0){//�������ӡ�
               xgkjs++;//�µ���ؿ顢
               zuijin[dangqianm][dangqiann]=xgkjs;
               //ƥ�䶨ʽ����һ��
               dangqiankh=xgkjs;
               pipeidingshi(dangqianm,dangqiann);
            }
            else if(zjkjs==1){
               if (zuijinkh[1]==dangqiankh){//û������
               zuijin[dangqianm][dangqiann]=dangqiankh;
               }//ǰ�����������ؿ�
               else{//��������
                  dangqiankh=zuijinkh[1];
                  if(othercolor[dangqiankh]==
                     zb[dangqianm][dangqiann][0]){//������ʽ

                  }else{//���ȵĶ�ʽ

                  }
               }
            }else {//������������ϡ�
               if (zuijinkh[1]==dangqiankh){//û������
                   //����ԭ�ȶ����ؿ�Ŀ�ţ��γ��µĿ飻

               }//ǰ�����������ؿ�
               else{


               }

            }

            /*else{ //������ٿսǻ�ߡ����������
               //��֮���ڼ���ĳ���ı仯�������������֡�
               boolean tuoxian=true;
               for(byte w=1;w<=zjkjs;w++){
                  if (zuijinkh[w]==dangqiankh){
                     //�����ദ��أ����ܳ�Ϊ��ʽ������ȫ�������
                     //���Կ�����Ϊ���ֶ�ʽ������λ���ļ�С�
                     tuoxian=false;
                     break;
                  }else{

                  }
               }
               if(tuoxian==true){
                 //���е���ؿ���ж�ʽƥ�䡣
                 dangqiankh=zuijinkh[1];
                 //�Ƿ��ͷ������һ���Ķ�ʽ�����������ı仯
               }
               else{
               //���������ı仯��
               //�Ƿ��뵱ǰ�鲻���������һ����������ȣ��������뵱ǰ��
               //������ԡ�
               }
            }*/
            byte julis=juli(hui[i][25],hui[i][26],
                             hui[i-1][25],hui[i-1][26]);
            byte juliss=juli(hui[i][25],hui[i][26],
                              hui[i-2][25],hui[i-2][26]);
            if(julis<juliss){

            }

         }//else
      }//for

   }//method
}
//9��14�գ�
//���������ϵĵ���Է���Ϊ��
//�����㣺��Χû���ӣ�����ͬɫ������ɫ��
//����롢���𡢷�Ͷ��������
//Ҫ���ֿ���ʹ��룻
//�ɿ�㣺���Ѿ��еļ����ӳɿ飬Ҳ����ͬʱ����ɫ�����ڡ�
//�糤���塢����ճ��
//�����㣺���Ѿ��еĶԷ������ڣ�����������û��ͬɫ�����ڡ�
//���ڡ���������