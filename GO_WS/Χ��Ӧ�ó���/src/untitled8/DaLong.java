package untitled8;

/**
 * <p>Title: ���ֶ�ɱ�ı�ʾ����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DaLong {
   byte qishu;
   byte color;
   byte yanxing; //1�����ۣ�2��С�ۣ�3�����ۣ�

   byte jieguo1; //�������ߵĽ��
   byte jieguo2; //�������ߵĽ��;���Ǵӱ���������������
   public static final byte huosheng = 3;
   public static final byte dajie = 2;
   //�Ժ�Ҫ���ǽٵ���ϸ���ࣻ
   public static final byte shibai = -3;
   public static final byte shuanghuo = 0;

   byte xsx; //xian shou xing;������
   //0��ʶ�ٽ磬�����ߵ�һ����ʤ��˫������˭�����йء�
   //1��ʶ�Ѿ���ʤ��˫�2��ʶ������ʤ����û�нٲĵ����á�
   //�����ų���ɱ�Ľٲģ�ֻ��ָ���ı�������˳��Ϊ�ٲģ�
   //��1��ʶ�Ѿ�ʧ�ܣ����ǿ����������ã���ø��õĽ����
//   �����Ǵ�ʧ�ܵ���ʤ��Ҳ���ܴ�ʧ�ܵ�˫�Ҳ���ܴ�˫���ʤ
//   //��2��ʶ�Ѿ�ʧ�ܣ�����û���������á�

   //����Ӳ�ͬ�ĽǶ��������в�ͬ�ı�����ӱ����Ƕȿ����ǽٻ
   //�ӶԷ��Ƕȿ����ǽ�ɱ����֮��Ȼ���ɼ������Ǳ���ͬһ�ġ�
   //��ɱ�Ľ����ʾ��������Ľ����ʾҲ������ͬ������ȴ�Ƚ�΢�
   //���������ֻ��ĳ��������Ĳ�𡣶���ɱ������ġ����������롣


   public byte gongqi(DaLong dl1, DaLong dl2) {
      byte k = 0;
      return k;
   }


   public void duisha(DaLong dl1, DaLong dl2) {
      //��dl1�ĽǶȳ�������ʤ������dl1���ߡ�
      //;�����ĺ�����ԭ����duishas������ͬ��0��ʶ�ٽ磬�����ߵ�һ��
      //��ʤ��1��ʶ�Ѿ���ʤ��2��ʶ������ʤ����û�нٲĵ����á���1��ʶ
      //�Ѿ�ʧ�ܣ����ǿ����������ã���2��ʶ�Ѿ�ʧ�ܣ�����û���������á�
      //��α�ʾ��ɱ�Ľ������Ϊ��ɱ�Ľ������ʤ��֮�⣬���й���������
      //�����������ж�ɱ��ǰ�����Ѿ�û�г������ֶΡ�
      byte gongqishu = gongqi(dl1, dl2);
      byte qi1 = dl1.qishu;
      byte qi2 = dl2.qishu;
      byte yx1 = dl1.yanxing;
      byte yx2 = dl2.yanxing;
      if ( (yx1 != yx2) || (gongqishu == 0) || (yx1 == 1 && gongqishu < 2)) {
         if (yx1 > yx2) { //���ͼ���ͬ�������ܹ���
            qi1 += gongqishu;

         }
         else if (yx1 < yx2) { //�����������ͺõ�һ����
            qi2 += gongqishu;
         }//������ͬ�������ٲ��ܹ���˫��ʱ������˫�����У�
         if (qi1 > qi2) {
            dl1.jieguo1 = huosheng; //��ɱ��ʤ
            dl1.jieguo2 = huosheng;
            dl2.jieguo1 = shibai;
            dl2.jieguo2 = shibai;
            dl1.xsx = (byte) (qi1 - qi2);
            dl2.xsx = (byte) (qi2 - qi1);
         }
         else if (qi1 < qi2) { //��ɱʧ��
            dl1.jieguo1 = shibai; //��ɱʧ��
            dl1.jieguo2 = shibai;
            dl2.jieguo1 = huosheng;
            dl2.jieguo2 = huosheng;

            dl1.xsx = (byte) (qi1 - qi2);
            dl2.xsx = (byte) (qi2 - qi1);
         }
         else if (qi1 == qi2) {
            dl1.jieguo1 = huosheng;
            dl1.jieguo2 = shibai;
            dl2.jieguo2 = shibai;
            dl2.jieguo1 = huosheng;
            dl1.xsx = 0;
            dl1.xsx = 0; //�����������Գƹ�ϵ��
         }

      }
      //if(yx1==yx2);���ͼ�����ͬ
      else {
         //else if (gongqishu >= 2) { //�й���Ŀ���
         if (qi1 > qi2) {
            if (yx1 == 1 ) { //��û����
               qi2 += gongqishu;
               qi2--;
            }else{
               qi2 += gongqishu;
            }
            if (qi1 > qi2) {
               dl1.jieguo1 = huosheng; //��ɱ��ʤ
               dl1.jieguo2=huosheng;
               dl2.jieguo2 = shibai;
               dl2.jieguo2=shibai;
               dl1.xsx = (byte) (qi1 - qi2);
               dl1.xsx = (byte) (qi2 - qi1);
            } //return (byte)(qi1-qi2);
            //Ϊ�����ٽ磻����ʤ��������˫�
            else if (qi1 < qi2) {
               dl1.jieguo1 = shuanghuo; //˫��
               dl1.jieguo2 = shuanghuo;
               dl2.jieguo1 = shuanghuo;
               dl2.jieguo2 = shuanghuo;
               dl1.xsx = (byte) (qi1 - qi2); //�ܸ���
               dl2.xsx = (byte) (qi2 - qi1);//���ÿ�qi2
               //���������������ã���Ҫ��qi1��̫ңԶ�ˡ�
            }
            //return  (byte)(qi1-qi2);
            //Ϊ��1���Ѿ�˫�
            else if (qi1 == qi2) {
               dl1.jieguo1 = huosheng;
               dl1.jieguo2 = shuanghuo;
               dl2.jieguo1=shuanghuo;
               dl2.jieguo2 = shibai;
               dl1.xsx = 0;
               dl2.xsx= 0;
            }

         }

         else if (qi1 < qi2) {
            if (yx1 == 1 ) { //��û����
               qi1 += gongqishu;
               qi1--;
            }else{
               qi1 += gongqishu;
            }
            if (qi1 > qi2) {
               dl1.jieguo1 = shuanghuo; //˫��
               dl1.jieguo2=shuanghuo;
               dl2.jieguo1 =shuanghuo;
               dl2.jieguo2=shuanghuo;
               dl1.xsx = (byte) (qi1 - qi2);
               dl2.xsx = (byte) (qi2 - qi1);
               //��ʵ������Ϊ��˫���ÿһ�����������Ծ���Ҳ������������
            }
            else if(qi1<qi2){
               dl1.jieguo1 = shibai; //˫��
               dl1.jieguo2=shibai;
               dl2.jieguo1 =huosheng;
               dl2.jieguo2=huosheng;
               dl1.xsx = (byte) (qi1 - qi2);
               dl2.xsx = (byte) (qi2 - qi1);
            }
            else if(qi2==qi1){
               dl1.jieguo1 = shuanghuo;
               dl1.jieguo2 = shibai;
               dl2.jieguo1=huosheng;
               dl2.jieguo2 = shuanghuo;
               dl1.xsx = 0;
               dl2.xsx= 0;

            }
         }
         else if(qi1==qi2){
            dl1.jieguo1 =shuanghuo;
            dl1.jieguo2=shuanghuo;
            dl2.jieguo1 =shuanghuo;
            dl2.jieguo2=shuanghuo;
            dl1.xsx=gongqishu;
            dl2.xsx =gongqishu;
         //xsx������������̫��2���ϣ���û��ʵ�������ˣ�
         }

      }

   }






   public DaLong() {
   }

}
/*else if (yx1 == 2 || yx1 == 3) { //����С�ۻ����
         if (qi1 > qi2) {//��ʤ��˫�
            qi2 += gongqishu;
            //qi2--;
            if (qi1 > qi2) {
               dl1.jieguo1 = huosheng; //��ɱ��ʤ
               dl1.jieguo2=huosheng;
               dl2.jieguo2 = shibai;
               dl2.jieguo2=shibai;
               dl1.xsx = (byte) (qi1 - qi2);
               dl1.xsx = (byte) (qi2 - qi1);
            } //return (byte)(qi1-qi2);
            //Ϊ�����ٽ磻����ʤ��������˫�
            else if (qi1 < qi2) {
               dl1.jieguo1 = shuanghuo; //˫��
               dl1.jieguo2 = shuanghuo;
               dl2.jieguo1 = shuanghuo;
               dl2.jieguo2 = shuanghuo;
               dl1.xsx = (byte) (qi1 - qi2); //�ܸ���
               dl2.xsx = (byte) (qi2 - qi1);//���ÿ�qi2
               //���������������ã���Ҫ��qi1��̫ңԶ�ˡ�
            }
            //return  (byte)(qi1-qi2);
            //Ϊ��1���Ѿ�˫�
            else if (qi1 == qi2) {
               dl1.jieguo1 = huosheng;
               dl1.jieguo2 = shuanghuo;
               dl2.jieguo1=shuanghuo;
               dl2.jieguo2 = shibai;
               dl1.xsx = 0;
               dl2.xsx= 0;
            }

         }
         else if (qi1 < qi2) {//ֻ��qi2��ʤ����˫�
            qi1 += gongqishu;
            qi1--;
            if (qi1 > qi2) {
               dl1.jieguo1 = shuanghuo; //˫��
               dl1.jieguo2=shuanghuo;
               dl2.jieguo1 =shuanghuo;
               dl2.jieguo2=shuanghuo;
               dl1.xsx = (byte) (qi1 - qi2);
               dl2.xsx = (byte) (qi2 - qi1);
               //��ʵ������Ϊ��˫���ÿһ�����������Ծ���Ҳ������������
            }
            else if(qi1<qi2){
               dl1.jieguo1 = shibai; //˫��
               dl1.jieguo2=shibai;
               dl2.jieguo1 =huosheng;
               dl2.jieguo2=huosheng;
               dl1.xsx = (byte) (qi1 - qi2);
               dl2.xsx = (byte) (qi2 - qi1);
            }
            else if(qi2==qi1){
               dl1.jieguo1 = shuanghuo;
               dl1.jieguo2 = shibai;
               dl2.jieguo1=huosheng;
               dl2.jieguo2 = shuanghuo;
               dl1.xsx = 0;
               dl2.xsx= 0;

            }
         }
         else if(qi1==qi2){
            dl1.jieguo1 =shuanghuo;
            dl1.jieguo2=shuanghuo;
            dl2.jieguo1 =shuanghuo;
            dl2.jieguo2=shuanghuo;
            dl1.xsx=gongqishu;
            dl2.xsx =gongqishu;
         //xsx������������̫��2���ϣ���û��ʵ�������ˣ�
         }
      }
   }*/
/* public void duishas(DaLong dl1,DaLong dl2,byte color){
       //��dl1�ĽǶȳ�������ʤ����color��ʾ��˭���ߡ�
       byte gongqishu=gongqi(dl1,dl2);
       byte qi1=dl1.qishu;
       byte qi2=dl2.qishu;
       byte yx1=dl1.yanxing ;
       byte yx2=dl2.yanxing ;
       if (yx1>yx2){//���ͼ���ͬ�������ܹ���
          qi1+=gongqishu;
          if(qi1>qi2) return 1;//��ɱ��ʤ
          //��һ����Ϊ�Ѿ����ͣ���������Ϊ�Ѿ�ȷ���Ҹɾ���û�нٲģ�
          else if(qi1<qi2) return 0;//��ɱʧ��
          else if(dl1.color ==color) return 1;
          else return 0;
       }
       else if(yx1<yx2){//�����������ͺõ�һ����
          qi2+=gongqishu;
          if(qi1>qi2) return 1;//��ɱ��ʤ
          else if(qi1<qi2) return 0;//��ɱʧ��
          else if(dl1.color ==color) return 1;
          else return 0;
       }
       //if(yx1==yx2);���ͼ�����ͬ
       else if(yx1==1){//��û����
          if(gongqishu<=1){//�����ܹ���
          }
          else if(gongqishu>2){//�й���Ŀ���
          }
       else if(yx1==2){//����С��
          }
       else if (yx1==3){// ���д���
          }
       }
//��������ֱ���һ�֣���������˭���ߣ�����˵ƽ�۵��Ǿ���
//��һ�֣������ֵ�˭����ȷ�������
    }*/
