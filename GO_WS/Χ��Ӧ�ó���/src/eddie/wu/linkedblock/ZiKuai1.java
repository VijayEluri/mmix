package eddie.wu.linkedblock;

public class ZiKuai1
   implements Cloneable {
   //Ϊ��ʵ�ֶ���ĸ��ƣ���ʹ���˹�����������Ҳʹ���˿�¡fang'an
   //���ܿ�¡�Ǹ������ķ�����������������Ĺ�ϵ�������µݹ飬����
   //Ч�ʷ������ߡ�3.7/2004

   //���ӿ����Ϣ��
   public byte color; //�����ɫ
   public boolean active; //������Ƿ��������������ϡ�
   //��ʵ����������1Ҳ����ʵ���жϣ����ǲ���������

   public DianNode1 zichuang; //���и��ӵ�����:�׵��ʾ��
   public short zishu; //�鱾���������

   public DianNode1 qichuang; //��ĸ���������
   public byte qishu; //�鱾���������

   public HaoNode1 zwqkhao; //����ĺţ�
   public byte zwqkshu; //��Χ�������Ŀ��
   public byte zwdsqkshu; //��Χ��ɫ�������Ŀ��

   public HaoNode1 zwyskhao; //��Χ��ɫ��ĺš�
   public byte zwyskshu; //��Χ��ɫ������

   public byte minqi; //��Χ��ɫ����������Сֵ��

   public byte yanxing; //���������ۣ����ۡ���

   //�Ƿ�Ҫ����������
   public Object clone() {
      ZiKuai1 o = null;

      try {
         o = (ZiKuai1)super.clone();

      }
      catch (CloneNotSupportedException de) {
         de.printStackTrace();
      }

      if (zichuang != null) {
         o.zichuang = (DianNode1) (zichuang.clone());
      }
      if (zwqkhao != null) {
         o.zwqkhao = (HaoNode1) (zwqkhao.clone());
      }
      if (zwyskhao != null) {
         o.zwyskhao = (HaoNode1) (zwyskhao.clone());
      }
      if (qichuang != null) {
         o.qichuang = (DianNode1) (qichuang.clone());
      }

      return o;

   }

   public ZiKuai1() {
      active = true;
   }

   public ZiKuai1(ZiKuai1 old) {
      super();
      if (old == null) {
         return;
      }
      color = old.color;
      zishu = old.zishu;
      minqi = old.minqi;
      zwyskshu = old.zwyskshu;
      zwqkshu = old.zwqkshu;
      qishu = old.qishu;
      yanxing = old.yanxing;
      active = old.active;
      DianNode1 temp;
      DianNode1 back;
      temp = old.zichuang;
      if (temp == null) { // �������Ӵ�
      }
      else {
         zichuang = new DianNode1(temp.a, temp.b);
         back = zichuang;
         for (short i = 2; i <= zishu; i++) {
            temp = temp.next;
            back.next = new DianNode1(temp.a, temp.b);
            back = back.next;
         }
      }
      temp = old.qichuang;
      if (temp == null) {} //��������
      else {
         qichuang = new DianNode1(temp.a, temp.b);
         back = qichuang;
         for (byte i = 2; i <= qishu; i++) {
            temp = temp.next;
            back.next = new DianNode1(temp.a, temp.b);
            back = back.next;
         }
      }
      HaoNode1 qkhtemp;
      HaoNode1 qkhback;
      qkhtemp = old.zwqkhao;
      if (qkhtemp == null) {} //������Χ����
      else {
         zwqkhao = new HaoNode1(qkhtemp.hao);
         qkhback = zwqkhao;
         for (byte i = 2; i <= zwqkshu; i++) {
            qkhtemp = qkhtemp.next;
            qkhback.next = new HaoNode1(qkhtemp.hao);
            qkhback = qkhback.next;
         }
      }

      HaoNode1 yskhtemp;
      HaoNode1 yskhback;
      yskhtemp = old.zwyskhao;
      if (yskhtemp == null) {} //������Χ��ɫ��
      else {
         zwyskhao = new HaoNode1(yskhtemp.hao);
         yskhback = zwyskhao;
         for (byte i = 2; i <= zwyskshu; i++) {
            yskhtemp = yskhtemp.next;
            yskhback.next = new HaoNode1(yskhtemp.hao);
            yskhback = yskhback.next;
         }
      }

      //���ݽṹ�ĸ��ƣ�Ϊ�˾�������֮���ٻ��巵�ء�2��16�ա�2004��
   }

   public void kuaiqizhuangkuang() {
      switch (qishu) {
         case 1: {
            //���㳤������������ܷ�ʵ�����Ӿͼ��������
         }
         case 2: {
            //����ѡ�㳤����Ľ����������Ϊ1��Ϊ���ˡ���Ϊ��
            //�������䣬һ�㲻�á�
            //����������1�����б仯��ء�
            //����������2�������л�����
            //����������3�����Ǽ򵥵ĳ������������ӡ�
         }
      }
   }

   public void addyisekuaihao(short kin) {
      if (zwyskhao == null) {
         zwyskhao = new HaoNode1(kin);
         zwyskshu = 1;
      }
      else {
         HaoNode1 linshi = zwyskhao;
         byte i = 1;
         for (; i <= zwyskshu; i++) {
            if (linshi.hao == kin) {
               break;
            }
            linshi = linshi.next;
         }
         if (i <= zwyskshu) {
            return; //�Ѿ��������š�
         }
         zwyskshu++;
         HaoNode1 temp = new HaoNode1(kin);
         temp.next = zwyskhao; //��ż���ǰͷ��
         zwyskhao = temp;
      }

   }

   public void addqikuaihao(short kin) {
      if (zwqkhao == null) {
         zwqkhao = new HaoNode1(kin);
         zwqkshu = 1;
      }
      else {
         HaoNode1 linshi = zwqkhao;
         byte i = 1;
         for (; i <= zwqkshu; i++) {
            if (linshi.hao == kin) {
               break;
            }
            linshi = linshi.next;
         }
         if (i <= zwqkshu) {
            return; //�Ѿ��������š�
         }
         zwqkshu++;
         HaoNode1 temp = new HaoNode1(kin);
         temp.next = zwqkhao; //��ż���ǰͷ��
         zwqkhao = temp;
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

   public void deleteyisekuaihao(short kin) {
      HaoNode1 temp = zwyskhao;
      HaoNode1 forward = zwyskhao;

      for (byte i = 1; i <= zwyskshu; i++) {
         if (kin == temp.hao) {
            if (i == 1) {
               zwyskhao = temp.next;

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

   public void deleteqikuaihao(short kin) {
      HaoNode1 temp = zwqkhao;
      HaoNode1 forward = zwqkhao;

      for (byte i = 1; i <= zwqkshu; i++) {
         if (kin == temp.hao) {
            if (i == 1) {
               zwqkhao = temp.next;

            }
            else {
               forward.next = temp.next;
            }
            zwqkshu--;
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
           linhao.next =zwyskhao;
           zwkh=linhao;
    }*/
/*public ZiKuai1(a,b) {
   // color=zb[a][b][ZTXB];
 zishu=1;
   // qishu=zb[a][b][QSXB];
  zwyskhao = new HaoLian();
  zwqkhao = new HaoLian();
  color = 0;
  minqi = 0;
  zishu = 0;
  qishu = 0;
  zichuang = null;
  qichuang = null;
   }*/
