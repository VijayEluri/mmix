package eddie.wu.linkedblock;

public class ZiKuai1
   implements Cloneable {
   //为了实现对象的复制，既使用了构建器方案，也使用了克隆fang'an
   //尽管克隆是更常见的方案，但是由于链表的关系，将导致递归，可能
   //效率反而不高。3.7/2004

   //棋子块的信息。
   public byte color; //块的颜色
   public boolean active; //该棋块是否现在仍在棋盘上。
   //其实用气数＝＝1也可以实现判断，但是不够清晰。

   public DianNode1 zichuang; //块中各子的链表:首点表示。
   public short zishu; //块本身的子数；

   public DianNode1 qichuang; //块的各气的链表；
   public byte qishu; //块本身的气数；

   public HaoNode1 zwqkhao; //气块的号；
   public byte zwqkshu; //周围气块的数目。
   public byte zwdsqkshu; //周围单色气块的数目。

   public HaoNode1 zwyskhao; //周围异色块的号。
   public byte zwyskshu; //周围异色块数；

   public byte minqi; //周围异色块气数的最小值。

   public byte yanxing; //公气，假眼，真眼。？

   //是否要按气数排序？
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
      if (temp == null) { // 复制棋子串
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
      if (temp == null) {} //复制气串
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
      if (qkhtemp == null) {} //复制周围气块
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
      if (yskhtemp == null) {} //复制周围异色块
      else {
         zwyskhao = new HaoNode1(yskhtemp.hao);
         yskhback = zwyskhao;
         for (byte i = 2; i <= zwyskshu; i++) {
            yskhtemp = yskhtemp.next;
            yskhback.next = new HaoNode1(yskhtemp.hao);
            yskhback = yskhback.next;
         }
      }

      //数据结构的复制，为了局面生成之后不再悔棋返回。2月16日、2004年
   }

   public void kuaiqizhuangkuang() {
      switch (qishu) {
         case 1: {
            //计算长出后的气数。能否不实际落子就计算出来。
         }
         case 2: {
            //两个选点长出后的结果，若气数为1，为“扑”，为死
            //气数不变，一般不好。
            //气数能增长1，还有变化余地。
            //气数能增长2，还很有活力。
            //气数能增长3，不是简单的长气，而是连接。
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
            return; //已经有这个块号。
         }
         zwyskshu++;
         HaoNode1 temp = new HaoNode1(kin);
         temp.next = zwyskhao; //块号加在前头。
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
            return; //已经有这个块号。
         }
         zwqkshu++;
         HaoNode1 temp = new HaoNode1(kin);
         temp.next = zwqkhao; //块号加在前头。
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
//周围气块的号，就是眼，可能是假眼，也可能是打劫。
//先用气数判断强弱，再赋予地，根据眼位大小判断强弱。
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
