package untitled10;

public class ZiKuai1 {
  public byte color; //�����ɫ
  public byte zishu; //���������
  public byte qishu; //���������
  public DianNode1 zichuang; //���и��ӵ�����:�׵��ʾ��
  public DianNode1 qichuang; //��ĸ���������

  public byte yanxing; //���������ۣ����ۡ�
  public HaoNode1 qkhao;//����ĺţ�
  public byte zwqkshu;//�������Ŀ��

  public byte minqi; //��Χ����������Сֵ��
  public HaoNode1 zwkhao; //��Χ��ɫ��ĺš�
  public byte zwyskshu;//��Χ������
  //�Ƿ�Ҫ����������



  public void addkuaihao(short kin) {
    if (zwkhao == null) {
      zwkhao = new HaoNode1(kin);
      zwyskshu = 1;
    }
    else {
      HaoNode1 linshi=zwkhao;
      byte i=1;
      for(;i<=zwyskshu;i++){
         if(linshi.hao==kin) break;
         linshi=linshi.next ;
      }
      if(i<=zwyskshu) return;//�Ѿ��������š�
      zwyskshu++;
      HaoNode1 temp = new HaoNode1(kin);
      temp.next = zwkhao;//��ż���ǰͷ��
      zwkhao = temp;
    }

  }

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

  public void addqidian(byte m1, byte n1) {
    if (qichuang == null) {
      qichuang = new DianNode1(m1, n1);
    }
    else {
      qishu++;
      DianNode1 temp = new DianNode1(m1, n1);
      temp.next = qichuang;
      qichuang = temp;
    }
  }

  public void deletezidian(byte m1, byte n1) {
    DianNode1 temp = zichuang;
    DianNode1 forward = zichuang;

    for (byte i = 1; i <= zishu; i++) {
      if (m1 == temp.a & n1 == temp.b) {
        if (i == 1) {
          zichuang = temp.next;

        }
        else {
          forward.next = temp.next;
        }
         zishu--;
      }
      else {
        forward = temp;
        temp = temp.next;
      }
    }

  }

  public void deletekuaihao(short kin) {
    HaoNode1 temp = zwkhao;
    HaoNode1 forward = zwkhao;

    for (byte i = 1; i <= zwyskshu; i++) {
      if (kin == temp.hao) {
        if (i == 1) {
          zwkhao = temp.next;

        }
        else {
          forward.next = temp.next;
        }
        zwyskshu--;
        break;
      }
      else {
        forward = temp;
        temp = temp.next;
      }
    }

  }

  public void deleteqidian(byte m1, byte n1) {
    DianNode1 temp = qichuang;
    DianNode1 forward = qichuang;

    for (byte i = 1; i <= qishu; i++) {
      if (m1 == temp.a & n1 == temp.b) {
        if (i == 1) {
          qichuang = temp.next;

        }
        else {
          forward.next = temp.next;
        }
        qishu--;
      }
      else {
        forward = temp;
        temp = temp.next;
      }
    }

  }

}
//��Χ����ĺţ������ۣ������Ǽ��ۣ�Ҳ�����Ǵ�١�
  //���������ж�ǿ�����ٸ���أ�������λ��С�ж�ǿ����
  /*void addkuaihao( byte kin1){
             linhao =new HaoNode1();
             linhao.hao=kin1;
             linhao.next =zwkhao;
             zwkh=linhao;
      }*/

  /*public ZiKuai1(a,b) {
     // color=zb[a][b][ZTXB];
   zishu=1;
     // qishu=zb[a][b][QSXB];
    zwkhao = new HaoLian();
    qkhao = new HaoLian();
    color = 0;
    minqi = 0;
    zishu = 0;
    qishu = 0;
    zichuang = null;
    qichuang = null;
     }*/
