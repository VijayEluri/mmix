package eddie.wu.linkedblock;

public class Qikuai0 {
  //��ԭʼ�����飬ֻ�й�������ĵ����꼰������
  //�����������ɵ�������ӵ�У��������������жϡ�

  public DianNode1 zichuang; //�����и��ӵ�����
  public short zishu; //�����������
  public byte color; //Ҳ����࣬��Ϊ��������ĳ����,���Ƿ��㡣
  //0��ʾ������������Χ�Ŀ��кںͰס������Ŀ�򵥴���
  public byte[] yanxingpanduan(){
  //�ȿ�����򵥵����Ρ��ɵ��鹹�ɣ�����û��ȱ�ݣ�ֻ��֪ʶ�Ĵ洢
  //����0��������4���ɻ�
  //8:�ѻ�
  byte yisi=0;
  byte kehuo=4;
  byte yihuo=8;

  //�Ƚ�����������������С�
  byte [] jieguo=new byte[5];
  //0����������1��������
  byte [][] qidian=new byte[10][2];
  DianNode1 temp=zichuang;
  for(byte i=0;i<zishu&&i<10;i++){
    qidian[i][0]=temp.a;
    qidian[i][1]=temp.b;
    temp=zichuang.next;
  }
    switch (zishu){
      case 1:
      {

        jieguo[0]=yisi;//���ǵ���
        jieguo[1]=1;
      }
      case 2:{
        jieguo[0]=yisi;
        jieguo[1]=2;
      }
      case 3:{
         jieguo[0]=kehuo;
         jieguo[1]=3;
      }
      case 4:{//���жϳ���״��
         //�ĸ���������ֻ���������
         // 1:AAAA   2:AAA   3:AAA   4:AA   5:AA
         //            A        A       AA    AA
         //ca:  2      1 3      0       0     2
         //cb:  0      1 3      1 3     2     2
         byte i=0;
         byte ca=0;
         byte cb=0;
         for(i=0;i<4;i++){
           ca+=qidian[i][0];
           cb+=qidian[i][1];
         }
         ca%=4;
         cb%=4;
         if(ca*cb==0){
           if((ca+cb)==2){
             //
             jieguo[0]=yihuo;
           }else{
             //��ñ��
             jieguo[0]=kehuo;
           }

         }else {
           if(ca==cb){
             jieguo[0]= yisi;
           }
           else{
             jieguo[0]= yihuo;
             //�Կ������̽����ġ�
           }
         }
        // return jieguo;
      }
      case 5:{
        //  ����     �������   ca��cbֵ  max��min
        // AAAAA
        byte i=0;
        byte chang=0;
        byte kuan=0;

        byte maxa=qidian[0][0];
        byte mina=qidian[0][0];
        byte maxb=qidian[0][1];
        byte minb=qidian[0][1];
        for(i=1;i<5;i++){
          if(qidian[i][0]>maxa)
            maxa=qidian[i][0];
          if(qidian[i][0]<mina)
            mina=qidian[i][0];
          if(qidian[i][1]>maxb)
           maxb=qidian[i][1];
          if(qidian[i][1]<minb)
           minb=qidian[i][1];

         }
         chang=(byte)(maxa-mina);
         kuan=(byte)(maxb-minb);
         switch (Math.abs(chang-kuan)){
           case 2:
           case 4: {
             jieguo[0]=yihuo;
             //jieguo[
           }
            case 1:
         }
      }
      case 6:{

     }
     case 7:{

      }
      default :{

      }

    }
    return jieguo;
  }
  public byte dingdianshu; //���۶��ԡ�
  public byte yanxing; //���������ۣ����ۡ�
  //ֱ�Ӷ������ʱ��ֻ����ͳһ�������飬�پ���yanxing��
  public byte minqi; //�γ��۵���Χ����������Сֵ��
  //���С�ڵ���2���Ϳ��ܱ���ԣ���λ��û���ˡ�
  //��Ȼ�д�ٵĵֿ���
  //�������1�����з�����߽�����
  public HaoNode1 zwkhao; //��Χ�γ���������ĺš�
  //HaoNode1 qkhao; //����ĺţ������ۣ������Ǽ��ۣ�Ҳ�����Ǵ�١�
  //���������ж�ǿ�����ٸ���أ�������λ��С�ж�ǿ����
  public void addzidian(byte m1, byte n1) {
    if (zichuang == null) {
      zichuang = new DianNode1(m1, n1);
      zishu = 1;
    }
    else {
      zishu++;
      DianNode1 temp = new DianNode1(m1, n1);
      temp.next = zichuang;
      zichuang = temp;
    }
  }

  public void init() { //�������̵�����������顣
    zishu = 0;
    DianNode1 temp;
    byte i, j;
    for (i = 1; i < 20; i++) {
      for (j = 1; j < 20; j++) {
        zishu++;
        temp = new DianNode1(i, j);
        temp.next = zichuang;
        zichuang = temp;

      }

    }
    System.out.println("zishju=" + zishu);
  }

  public void deletezidian(byte m1, byte n1) {
    DianNode1 temp = zichuang;
    DianNode1 forward = zichuang;
    System.out.println("deletezidian:zishju=" + zishu);
    for (short i = 1; i <= zishu; i++) {
      if (m1 == temp.a & n1 == temp.b) {
        if (i == 1) {
          zichuang = temp.next;

        }
        else {
          forward.next = temp.next;
        }

      }
      else {
        forward = temp;
        temp = temp.next;
      }
    }
    zishu--;
    System.out.println("deletezidian:zishju=" + zishu);
  }

}