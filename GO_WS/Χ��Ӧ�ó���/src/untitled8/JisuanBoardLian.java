package untitled8;

/**
 * <p>Title:�������� </p>
 */
public class JisuanBoardLian
   extends GoBoardLian1 {
   public JisuanBoardLian(GoBoardLian1 goboard) {

      super();
      short i, j;
      for (i = 1; i <= goboard.shoushu; i++) {
         cgcl(goboard.hui[i][25], goboard.hui[i][26]);
      }
      System.out.println("zb[0][0][0]="+zb[0][0][0]);
      System.out.println("����JisuanBoardLian()");
      /* for ( i = ZBXX; i <= ZBSX; i++) {
         for ( j = ZBXX; j <= ZBSX; j++) {
           zb[i][j][ZTXB] = goboard.zb[i][j][ZTXB];
           zb[i][j][SQBZXB] = goboard.zb[i][j][SQBZXB];
           zb[i][j][QSXB] = goboard.zb[i][j][QSXB];
           zb[i][j][KSYXB] = goboard.zb[i][j][KSYXB];
         }
       } //zb����ĳ�ʼ��
       shoushu = goboard.shoushu;
       ki = goboard.ki;
       System.out.println("ki="+ki);
       for ( i = 0; i <=(short)( ki + 128); i++) {
         System.out.println("kuaidedian()");
         KuaiBiaoXiang linshi=new KuaiBiaoXiang();
         linshi.qishu = goboard.kuai[i].qishu;
         System.out.println("kuaidedian()");
         linshi.zishu = goboard.kuai[i].zishu;
         System.out.println("kuaidedian()");
         linshi.qichuang = goboard.kuai[i].qichuang;
         System.out.println("kuaidechuagn()");
         linshi.zichuang = goboard.kuai[i].zichuang;
         kuai[i]=linshi;
       } //��ԭ��������飿���໥Ӱ�졣
       for (i = 1;i<= shoushu; i++) {
         for (j = 0; j < 38; j++) {
           hui[i][j] = goboard.hui[i][j];
         }
       }
       ktb = goboard.ktb;
       ktw = goboard.ktw;*/
      System.out.println("����JisuanBoardLian()");
   }

   /*public boolean yiqichi(byte ydm,byte ydn){//���ش�Ե��һά����
      //����0�鴢����Ϣ,ͳһ������yiqichi(int kin)
      boolean zzcl=false;
      kuai[0][0][0]=2;
      kuai[0][0][1]=1;
      kuai[0][1][0]=ydm;
      kuai[0][1][1]=ydn;
      kuai[0][50][0]=zb[ydm][ydn][4] ;
      kuai[0][50][1]=zb[ydm][ydn][5];
      kuai[0][51][0]=zb[ydm][ydn][6];
      kuai[0][51][1]=zb[ydm][ydn][7];
      zzcl= yiqichi((byte)0);
      for(byte i=0;i<70;i++){
         kuai[0][i][0]=0;
         kuai[0][i][1]=0;
      }//��ʱ���������;
      return zzcl;
       }*/
   public boolean yiqichi(byte kint) { //���Ӽ�����Ԥ��,Ӧ�þ��лָ���ԭ�����
      //����,���߸���ԭ����,�ø������м���.����ǰ�߸���,��Ϊ����ʱҲҪ����
      //kin�Ǳ����ӵĿ������.
      short kin = (short) (kint + 128);
      byte i = 0, j = 0; //ѭ������.
      byte m1 = 0, n1 = 0, m2 = 0, n2 = 0; //m1,n1Ϊ����֮һ,m2,n2Ϊ����֮��.
      byte a, b; //���ڴ���cgcl()����������λ��.
      byte cq1 = 0, cq2 = 0; //�������º�׷�����,�жϳ����ŵ����>=2;��==1��ʾδ��ȷ
      byte q1 = 0, q2 = 0; //q1,q2Ϊ�������ֺ��ҷ�����.
      byte beitia = 0, beitib = 0, tizia = 0, tizib = 0; //���ӷ����й�,ǰΪ������Ϊ���ӵ�.
      boolean huitui = false; //����,����̽���¾���,���ǻص�ԭ��������µ�ѡ��.
      boolean fanda = false; //����
      boolean first=true;//��ʼ��Ų������Ŀ�š�
      byte[][] hou = new byte[40][8]; //hou xuan qi bu,��ѡ�岽����,��1����
      //0:��һ��(�˴�1����);1-4Ϊ����,5Ϊ�����־.40��ָ�����40��������ѡ��.
      byte ch = 0; //chΪ��ѡ�岽����.
      short ss = shoushu; //��¼��������
      byte qi = 0;
      byte m, n;
      byte bzzfyanse = kuai[kin].color; //�����ӷ���ɫ
      byte zzfyanse; //���ӷ�����ɫ
      if (bzzfyanse == 1) {
         zzfyanse = 2;
      }
      else {
         zzfyanse = 1;
         //ע���ʾģ��,�����������.
      }


      System.out.println("����yiqichi()");
      while (true) { //����֪�������������
         if (huitui == true) { //�����Ѿ��洢����һ·ѡ��
            huitui = false; //����Ļ�
            for (i = (byte) (shoushu - ss); i >= hou[ch][0]; i--) {
               clhuiqi();
            }
            if (hou[ch][5] == 1) {
               fanda = true;
               tizia = hou[ch][6];
               tizib = hou[ch][7];
               hou[ch][6] = 0;
               hou[ch][7] = 0;
            }
            else {
               fanda = false; //�ָ�����
            }
            m1 = hou[ch][1]; //����ȫ�ֱ���
            n1 = hou[ch][2]; //?
            m2 = hou[ch][3]; //����ȫ�ֱ���
            n2 = hou[ch][4];
            ch--;

            q2 = 2; //�൱��ֻ��һ����ѡ��,���Ժϲ�����
            q1 = 3; //�򻯣������¾���Ĵ���
         }

         else { //��ͷ̽���µľ���
            System.out.println("��ͷ̽���µľ���") ;
            if(first==true){
               System.out.println("Ҫ���ԵĿ��Ϊ:"+(kin-128)) ;
               m1 = kuai[kin].qichuang.a; //���ӵ�a������,Ҳ��kin�������
               n1 = kuai[kin].qichuang.b; //50~69�洢������Ϣ.
               m2 = kuai[kin].qichuang.next.a; //���ӵ�b������,Ҳ��kin�������
               n2 = kuai[kin].qichuang.next.b;
               first=false;
            }
            else{
               System.out.println("Ҫ���ԵĿ��Ϊ:"+ki) ;
               m1 = kuai[ki+128].qichuang.a; //���ӵ�a������,Ҳ��kin�������
               n1 = kuai[ki+128].qichuang.b; //50~69�洢������Ϣ.
               m2 = kuai[ki+128].qichuang.next.a; //���ӵ�b������,Ҳ��kin�������
               n2 = kuai[ki+128].qichuang.next.b;

            }
            System.out.println("��ѡ���Ե������Ϊ��") ;
            System.out.println("m1="+m1+";n1="+n1) ;
            System.out.println("m2="+m2+";n2="+n2) ;
            cq1 = zzddq(m1, n1); //������m1,n1��õ�����ڿ������
            cq2 = zzddq(m2, n2);

            if (cq1 >= 1) {
               q2 = jhhdq(m2, n2, m1, n1); //������m1,n1.��m2,n2������õ�����ڿ������
            }
            else {
               q2 = 3;
            }
            if (cq2 >= 1) {
               q1 = jhhdq(m1, n1, m2, n2); //q1<2,�����������Խ���.
            }
            else {
               q1 = 3;
            }
         } //else huitui==false

         if (q1 > 2 && q2 > 2) { //kzzd=0;û�п������ӵĵ�
            if (ch == 0) {
               System.out.println("û�к�ѡ��");
               return false;
            }
            else {
               huitui = true;
               continue;
            }
         }
         else if (q1 >= 2 && q2 >= 2) { //����һ������Ҫ̽��
            //��Ҫ̽��,������ֱ�ӻ��Ǽ��;����huitui���
            System.out.println("����һ������Ҫ̽��");
            if (q1 == 2 && q2 == 2) { //ͬʱΪ2
               System.out.println("����ͬʱ��Ҫ̽��");
               if (fanda == true) {
                  hou[++ch][5] = 1;
                  hou[ch][6] = tizia;
                  hou[ch][7] = tizib;
               }
               else {
                  hou[++ch][5] = 0;
               }
               hou[ch][1] = m2; //���ӵ�
               hou[ch][2] = n2;
               hou[ch][3] = m1;
               hou[ch][4] = n1;
               hou[ch][0] = (byte) (shoushu + 1 - ss); //�ڼ��ֵı仯
            }

            if (q1 == 2 && q2 > 2) {
               System.out.println("ֻ��һ������Ҫ̽��");
               byte t1 = m1;
               byte t2 = n1;
               m1 = m2;
               n1 = n2;
               m2 = t1;
               n2 = t2; //���ӵ�Ψһʱ,ʼ����m1,n1 ��ʾ��
               q1 = q2;
               q2 = 2;
            }
            System.out.print("ѡ���ĵ�Ϊ:") ;
            System.out.println("m1="+m1+";n1="+n1) ;
         }
         else if (q2 < 2 || q1 < 2) { //��ĳ���ɾֲ���������kzzd=0
            System.out.println("����һ������������");
            if (q1 < 2 && q2 < 2) { //ͬʱΪ2
               System.out.println("����ͬʱ��������");
               if (fanda == true) {
                  hou[++ch][5] = 1;
                  hou[ch][6] = tizia;
                  hou[ch][7] = tizib;
               }
               else {
                  hou[++ch][5] = 0;
               }
               hou[ch][1] = m2; //���ӵ�
               hou[ch][2] = n2;
               hou[ch][3] = m1;
               hou[ch][4] = n1;
               hou[ch][0] = (byte) (shoushu + 1 - ss); //�ڼ��ֵı仯
            }
            else if (q1 < 2) { //�Ƿ���Ӧ����q2==2?
               System.out.println("ֻ��һ������������");
               if (q2 == 2) {
                  if (fanda == true) {
                     hou[++ch][5] = 1;
                     hou[ch][6] = tizia;
                     hou[ch][7] = tizib;
                  }
                  else {
                     hou[++ch][5] = 0;
                  }

                  hou[ch][1] = m1; //���ӵ�
                  hou[ch][2] = n1;
                  hou[ch][3] = m2;
                  hou[ch][4] = n2;
                  hou[ch][0] = (byte) (shoushu + 1 - ss); //�ڼ��ֵı仯
               }
               byte t1 = m1;
               byte t2 = n1;
               m1 = m2;
               n1 = n2;
               m2 = t1;
               n2 = t2;

            }
            else if (q2 < 2) {
               System.out.println("ֻ��һ������������");
               if (q1 == 2) {
                  if (fanda == true) {
                     hou[++ch][5] = 1;
                     hou[ch][6] = tizia;
                     hou[ch][7] = tizib;
                  }
                  else {
                     hou[++ch][5] = 0;
                  }

                  hou[ch][1] = m2; //���ӵ�
                  hou[ch][2] = n2;
                  hou[ch][3] = m1;
                  hou[ch][4] = n1;
                  hou[ch][0] = (byte) (shoushu + 1 - ss); //�ڼ��ֵı仯

               }
            }
            System.out.print("ѡ���ĵ�Ϊ��") ;
            System.out.println("m1="+m1+";n1="+n1) ;
         }

         a = m1; //һ�����ӵ����ѡ���������е�һ��
         b = n1;
         cgcl(a, b);

         if (zb[a][b][2] == 1) { //Ҫ���Ƿ��������,
            System.out.println("Ҫ���Ƿ��������");
            byte ksyi = zb[a][b][KSYXB]; //���ܿ鱻��?���ǿ�Ĵ�С
            if (fanda == true) {
               if (ch == 0) {
                  return false;
               }
               else {
                  huitui = true;
                  continue;
               }
            }
            else if (ksyi != 0 && kuai[ksyi + 128].zishu > 4) {
               if (ch == 0) {
                  return false;
               }
               else {
                  huitui = true;
                  continue;
               }
            }
            fanda = true;
            if (ksyi == 0) {
               tizia = zb[a][b][4];//?
               tizib = zb[a][b][5];
               beitia = a;
               beitib = b;
            }
            else {
               tizia = kuai[ksyi + 128].qichuang.a;
               tizib = kuai[ksyi + 128].qichuang.b;
               beitia = 0; //��ʾ�鱻��
               beitib = 0; //�������ѡ��
            }
         }

         //��ʼѡ��ڶ���
         if (q1 >= 2 && q2 >= 2) { //����ͬʱ>2
            a = m2;
            b = n2;
            cgcl(a, b);
            if (kuai[ki + 128].qishu < 2) { //������Ҫ�жϽ������
               clhuiqi(); //��>=2�򲻻������һ��if��
               q2 = 1;
            }
         } //���н������ܣ���ʱȷ��

         if (q2 < 2 || q1 < 2) { //��ĳ���ɾֲ���������kzzd=0
            System.out.println("�����Ƿ���ȷʵ�ܽ���");
            if (fanda == false) {
               return true;
            }
            else {
               System.out.println("tizia="+tizia+",tizib="+tizib);
               if (ltdd(tizia, tizib,zzfyanse) == true) {
                  if (ch == 0) {
                     return false;
                  }
                  else {
                     huitui = true;
                     continue;
                  }
               }
               a = tizia; //�ڷ�������
               b = tizib; //�Ѿ�����û���������?
               fanda = false;

               cgcl(a, b);
               //   if(jszq(m2,n2)<=1) return 0;//�Ƿ�?������
               //������,fanda=trueʱ,��С��1,��=3;
               if (beitia == 0) { //����һ��
                  //  if(zb[kin][0][0]<2) return true;   //�����ж�
                  if (kuai[ki + 128].qishu > 2) { //���ؿ���q1,ͬ������
                     if (ch == 0) {
                        return false;
                     }
                     else {
                        huitui = true;
                        continue;
                     }
                  } //else
                  else { //����ǿ飬��������ѡ�񣬴���һ�ּ�����
                     continue;
                  }
               }
               if(tizia==m2&&tizib==n2){
                  tizia = 0;
                  tizib = 0;
                  continue;
               }
               tizia = 0;
               tizib = 0;
               a = m2; //��һ���򶵳�
               b = n2;
               cgcl(a, b);
               if (zb[a][b][ZTXB] < 2) {
                  if (ch == 0) {
                     return false;
                  }
                  else {
                     huitui = true;
                     continue;
                  }
               }
               a = beitia; //ճ��
               b = beitib;
               beitia = 0;
               beitib = 0;
               cgcl(a, b);
            } //else
         }

         //��������
         if (kuai[ki + 128].qishu < 2) {//����ճ����������迼�Ƿ���
            return true; //������������ж�
         }
         else if (kuai[ki + 128].qishu > 2) { //���ؿ���q1,ͬ������
            if (ch == 0) {
               return false;
            }
            else {
               huitui = true;
               continue;
            }
         } //else
         else if (kuai[ki + 128].qishu == 2) {
            System.out.println("kuai:"+ki+"������Ϊ2") ;
            if (hui[shoushu][27] > 0) { //���һ��
               if (fanda == true) {

                  if (ch == 0) {
                     return false;
                  }
                  else {
                     huitui = true;
                     continue;
                  }
               }
               else { //�Ƿ����������

                  fanda = true;
                  beitia = hui[shoushu][27];
                  beitib = hui[shoushu][28];
                  System.out.println("���һ��:a="+beitia+"b="+beitib) ;
                  for (j = 0; j < 4; j++) {
                     tizia = (byte) (beitia + szld[j][0]);
                     tizib = (byte) (beitib + szld[j][1]);
                     if (zb[tizia][tizib][ZTXB] == BLANK) break;

                  }

                  //tizia = zb[beitia][beitib][4];
                  //tizib = zb[beitia][beitib][5];

                  //������η������飿
                  /* if ( ltdd(tizia, tizib, zzfyanse)) {
                     if (ch == 0) {
                       return false;
                     }
                     else {
                       huitui = true; //���˵�ǰһ�ֲ�
                       continue;
                     }
                   }*/
                  continue;
               }
            }
            else if (hui[shoushu][33] != 0) { //��Կ�,����ʧ��
               System.out.println("��Կ飺"+hui[shoushu][33]) ;
               if (ch == 0) { //��ǰ��Ĵ�Կ鲻ͬ��������Ϊ2��
                  return false;
               }
               else {
                  huitui = true;
                  continue;
               }
            }
            //�϶�ִ��(������Ǹò��γɵĴ��)
            if (fanda == true && ltdd(tizia, tizib, zzfyanse)) {
               if (ch == 0) {
                  return false;
               }
               else {
                  huitui = true; //���˵�ǰһ�ֲ�
                  continue;
               }
            }

         }
      } //do
   } //yiqichi

   public short xiayishou() { //������һ�ֵ�һά��ʾ
      byte a, b;
      while (true) {
         a = (byte) (Math.random() * 19 + 1);
         b = (byte) (Math.random() * 19 + 1);
         if (zb[a][b][ZTXB] == 0) {
            return (short) (b * 19 + a - 19);
         }
      }
   }

   public boolean ltdd(byte m1, byte n1, byte zzfys) { //m1,n1Ϊ���ӵ�
      byte tongse = zzfys; //���ӷ���ɫ
      byte m, n,jishu=0;
      System.out.println("����ltdd()");
      for (byte i = 0; i < 4; i++) {
         m = (byte) (m1 + szld[i][0]);
         n = (byte) (n1 + szld[i][1]);
         if (zb[m][n][ZTXB] == zzfys && zb[m][n][QSXB] <= 2) {
            jishu++;
         }
      } //ltdd
      System.out.println("����ltdd()");
      if(jishu==1)return false;
      else return true;

   }

   public byte jhhdq(byte m1, byte n1, byte m2, byte n2) {
      //���������,����m2,n2,m1,n1�ܳ�Ϊ����;m2,n2>=1���ŵ��øú���
      //����1��ʾֻ������һ��ѡ��
      byte qi = 0; //������д�µĺ���,��Ϊ������������(�����ж�,��ʡ����)
      byte tongse = (byte) ((shoushu+1) % 2 + 1);
      byte m, n;
      System.out.println("����jhhdq()");
      for (byte i = 0; i < 4; i++) {
         m = (byte) (m1 + szld[i][0]);
         n = (byte) (n1 + szld[i][1]);
         if (zb[m][n][ZTXB] == BLANK) { //2.1the breath of blank
            if (m == m2 && n == n2) { //m1+1����ܲ�������.���㻥��Ϊ��.
               // if(jszq(m2,n2)==1) qi++;//������������??==1δ����һ��
               //����������!ԭ��>=2;
               //���m2,n2����,��������һ��
            }
            else {
               qi++;
            }
         }
      }
      if (qi == 3) {
         System.out.println("qi=3");
         return 3; //�÷���������Ѿ������ܳ���
      }
      //  else  return 0;//�޸�,2��22��,ֻ����հ״�,���´���û��
      for (byte i = 0; i < 4; i++) {
         m = (byte) (m1 + szld[i][0]);
         n = (byte) (n1 + szld[i][1]);
         if (zb[m][n][ZTXB] == tongse&&zb[m][n][KSYXB]!=ki) { //2.1the breath of blank
            qi += zb[m][n][QSXB];
            qi--;
         } //������Χ����Ϊͬһ��,����������.
      }
      if (qi > 1) {
         System.out.println("qi=2");
         return 2; //2�ǲ�ȷ����,����Ҫʵ�ʼ���
      }
      else {
         System.out.println("qi="+qi);
         return qi; //0����ʵ��,1����Ч�õ�(1����Ϊ0)
      }

   }

   public byte zzddq(byte m1, byte n1) { //Ԥ�����ӵ����
      byte qi = 0; //��������
      byte tongse = (byte) (shoushu % 2 + 1); //��ʱ����û�е���
      byte m, n;
      System.out.println("����zzddq()");
      for (byte i = 0; i < 4; i++) {
         m = (byte) (m1 + szld[i][0]);
         n = (byte) (n1 + szld[i][1]);
         if (zb[m][n][ZTXB] == BLANK) { //2.1the breath of blank
            qi++;
         }
      } //ֻ����ֱ�ӵ���,�򵥺ܶ�,���Ҵﵽ��Ԥ���Ŀ��,��Ϊ��������������������
      if (qi >= 1) {
         System.out.println("���ӵ����="+qi);
         return qi; //qi==3,Ӧ����>=2��;qi==2,Ӧ����ܱ�����
      }
      //qi==1;�Ѿ����ڷ���״̬(����, ���ܳɿ�,ֻ�ܱ�����һ��ֱ�ӵ���).
      //qi==1ֻ��ʾ��Ҫ��һ������,ʵ��>=1��.qi==0��ʾ���ŵ�.
      else if (qi == 0) {
         for (byte i = 0; i < 4; i++) {
            m = (byte) (m1 + szld[i][0]);
            n = (byte) (n1 + szld[i][1]);
            if (zb[m][n][ZTXB] == tongse) {
               qi += zb[m][n][QSXB];
               qi--;
            }
         }
         if (qi == 0) {
            byte yise = (byte) ( (shoushu + 1) % 2 + 1);
            for (byte i = 0; i < 4; i++) {
               m = (byte) (m1 + szld[i][0]);
               n = (byte) (n1 + szld[i][1]);
               if (zb[m][n][ZTXB] == yise && zb[m][n][QSXB] == 1) {
                  System.out.println("���ӵ����="+qi);
                 return 1; //��Ҫ��һ���ж�
               }
            }
            System.out.println("���ӵ����="+qi);
            return 0;
         }
         else {
           System.out.println("���ӵ����="+qi);
            return 1; //��ʾ��Ҫ��һ������.
         }
      }
      return 1; //������������,û����һ��Ͳ���ͨ��
   } //ֻ��������û����.����û�п��Ǵ��(��Ȼû����,�������Ӻ������һ��).
} //return boolean ���� ,���ӹ��̱�����hui������.
