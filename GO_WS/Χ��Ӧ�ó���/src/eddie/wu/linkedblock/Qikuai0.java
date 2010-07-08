package eddie.wu.linkedblock;

public class Qikuai0 {
  //最原始的气块，只有构成气块的点坐标及点数。
  //假设气块是由单块棋子拥有，对其眼型做出判断。

  public DianNode1 zichuang; //气块中各子的链表。
  public short zishu; //气块的子数；
  public byte color; //也许多余，因为他从属于某个块,但是方便。
  //0表示公气，反正周围的块有黑和白。这样的块简单处理
  public byte[] yanxingpanduan(){
  //先考虑最简单的情形。由单块构成，外型没有缺陷，只是知识的存储
  //返回0：已死；4：可活
  //8:已活
  byte yisi=0;
  byte kehuo=4;
  byte yihuo=8;

  //先将各点坐标放在数组中。
  byte [] jieguo=new byte[5];
  //0：死活结果；1：气数；
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

        jieguo[0]=yisi;//就是单眼
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
      case 4:{//先判断出形状、
         //四个气点连接只有四种情况
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
             //笠帽四
             jieguo[0]=kehuo;
           }

         }else {
           if(ca==cb){
             jieguo[0]= yisi;
           }
           else{
             jieguo[0]= yihuo;
             //仍可能是盘角曲四。
           }
         }
        // return jieguo;
      }
      case 5:{
        //  棋型     死活结论   ca，cb值  max－min
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
  public byte dingdianshu; //单眼而言。
  public byte yanxing; //公气，假眼，真眼。
  //直接读入局面时，只能先统一生成气块，再决定yanxing；
  public byte minqi; //形成眼的周围块气数的最小值。
  //如果小于等于2，就可能被打吃，眼位就没有了。
  //当然有打劫的抵抗。
  //如果等于1，就有反提或者劫争；
  public HaoNode1 zwkhao; //周围形成气块的棋块的号。
  //HaoNode1 qkhao; //气块的号，就是眼，可能是假眼，也可能是打劫。
  //先用气数判断强弱，再赋予地，根据眼位大小判断强弱。
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

  public void init() { //用于棋盘的最初完整气块。
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