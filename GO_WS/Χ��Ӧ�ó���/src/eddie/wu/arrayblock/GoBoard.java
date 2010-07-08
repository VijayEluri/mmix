package eddie.wu.arrayblock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.linkedblock.BoardBreathState;
import eddie.wu.linkedblock.BoardColorState;

/**
 * <p>
 * Title:������̹��ܵ���:������� ��Ӧ�ó���ĺ�����
 * </p>
 * ��������һ���о��õĽ��湤�ߣ����������ף��;��档 ��Ȼ��ʵ�����壬��Ϊ����������������ٶȣ�
 * �˷�һ���� ����ֵ�õġ�
 * 2003��ʮ�����գ�������Χ�����Ϣ��
 * <p>
 * Description:����,��������. a,b��ʾ�������ӵ�ĺ������꣬����a��bͬʱ��Ϊ��������ʹ�ã�
 * ������ʵ�� ������һ��ӳ�䡣
 * </p>
 * <p>ԭ�������128���壬������չ���������256���塣</p>
 */

public class GoBoard {
	private static final Log log=LogFactory.getLog(GoBoard.class);
	
	//�����������ӵ�,˳��ɵ�.��-��-��-��
	public final byte[][] szld = { { 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 1 } };
	
	public final byte ZBSX = 20; //������������;

	public final byte ZBXX = 0; //������������;

	public final byte BLANK = 0; //�հ׵�

	public final byte BLACK = 1; //1��ʾ����;

	public final byte WHITE = 2; //2��ʾ����;

	public final byte ZTXB = 0; //�±�0�洢״ֵ̬;

	public final byte SQBZXB = 1; //�±�1�洢������־;

	public final byte QSXB = 2; //�±�2�洢����;

	public final byte KSYXB = 3; //�±�3�洢������

	public short ki = 0; //kuai index ����?��ǰ�Ѿ��õ��Ŀ��;

	public short qiki = 255; //qikuai�������Ӹߵ��ס�

	public short shoushu = 0; //��ǰ����,����֮ǰ����.��1��ʼ;

	public byte ktb = 0, ktw = 0; //�ڰױ����Ӽ���

	//0:state;����״̬:��1��2;
	//1:�������ı�־;
	//2:breath;����;
	//3:block index������;
	//4-7�洢����.
	public byte[][][] zb = new byte[21][21][8]; //1.6k;

	
	//ǰ��ά�����̵�����,�����±��1��19;
	public byte[][][] kuai = new byte[256][70][2]; //17k;

	//mei yi kuai de ge zi zuo biao
	//�洢ÿ�����ϸ��Ϣ;50-69Ϊ����.1-49Ϊ�ӵ�;[x][0][0]Ϊ����;
	//[x][0][1]Ϊ����;����49��,���洢,����20��,������洢;
	public byte[][] hui = new byte[512][38]; //19k;

	//0=�¿�����;1~8four single point
	//eaten,9~12 kuai suo ying of fou block eaten.13~24is same ,25,26a,b
	//��¼��ֵĹ�����Ϣ,���ڻ���;1-8Ϊ���Ե��ӵ�����;9-12Ϊ���Կ������;
	//13-20Ϊ�ɿ��ӵ�����,21-24Ϊ���¿�ľɿ�����.27-32Ϊ��Ե�,33-35Ϊ��ԵĿ�
	//����;36.37Ϊ���ŵ�.25.26Ϊ�ò��������.
	//DataOutputStream out;//qi pu shu chu wen jain��������ļ�
	//DataInputStream in;//ֱ�Ӵ��ļ����ɾ���.
	public void qiquan() { //Ϊ�˲��볣�洦���0.0�������ô˺���������Ȩ��
		//���걣��Ϊ0.0
		shoushu++;
		hui[shoushu][25] = 0;
		hui[shoushu][26] = 0;
	}

	public void shuchujumian(DataOutputStream jmout) throws IOException {
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				if (zb[i][j][ZTXB] != 0) {
					jmout.writeByte((byte) i);
					jmout.writeByte((byte) j);
					jmout.writeByte((byte) zb[i][j][ZTXB]);
					log.debug("i=" + i);
					log.debug("j=" + j);
				}
			}
		}
		jmout.flush();
		log.debug("�������");

	}

	public void shuchuqipu(DataOutputStream jmout) throws IOException {
		short i, j;
		for (i = 1; i <= shoushu; i++) {

			jmout.writeByte(hui[i][25]);
			jmout.writeByte(hui[i][26]);

		}
		jmout.flush();
		log.debug("�������");

	}

	public void zairujumian(DataInputStream jmin) throws IOException {

		byte a, b, color;
		while (jmin.available() != 0) {
			a = jmin.readByte();
			b = jmin.readByte();
			color = jmin.readByte();
			zb[a][b][ZTXB] = color;
			if (a < 1 | a > 19 | b < 1 | b > 19 | color < 1 | color > 2) {
				log.debug("�������������" + a);
				log.debug("i=" + a);
				log.debug("j=" + b);
				log.debug("color=" + color);
			}

		}

		log.debug("�������");

	}

	/**
	 * transfer into bidimension array
	 * 
	 * @return
	 */
	public byte[][] getStateArray() {
		byte[][] a = new byte[21][21];
		byte i, j, k;
		for (i = 0; i < 21; i++) {
			for (j = 0; j < 21; j++) {
				a[i][j] = zb[i][j][ZTXB];

			}
		}
		return a;
	}
	public BoardColorState getBoardState() {	
		return new BoardColorState(this.getStateArray());
	}
	
	public BoardBreathState getBoardBreathState() {	
		return new BoardBreathState(this.getBreathArray());
	}
	/**
	 * transfer into bidimension array
	 * 
	 * @return
	 */
	public byte[][] getBreathArray() {
		byte[][] a = new byte[21][21];
		byte i, j, k;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				a[i][j] = zb[i][j][QSXB];

			}
		}
		return a;
	}

	public void cgcl(byte a, byte b) { //chang gui chu li qi zi de qi
		//���Խ��ܵ�����Ϊ(a,b)��c;c=b*19+a-19;�����������
		//a is the row subscript of the point.
		//a����������±�,Ҳ��ƽ��ĺ�����:0-18
		//b is the column subscript of the point.
		//b����������±�,Ҳ����Ļ��������:0-18
		//byte c;//a,b��һά��ʾ:0-360;
		byte m1 = a; //a,b�ڷ����в��ı�
		byte n1 = b; //m1,n1Ϊa,b���ڵ�.
		byte linzishu = 0; //������
		byte yise = 0; //��ɫ
		byte tongse = 0; //yise is diff color.and 2 same.ͬɫ
		byte k0 = 0, k1 = 0, k2 = 0, k3 = 0; //the count for three kinds of
											 // point.
		//���ֵ�ļ���,k1Ϊ��ɫ�����,k2Ϊ�������,k3Ϊͬɫ�����
		byte i = 0, j = 0;
		byte dang = 0; //dang is breath of block.�������
		byte ktz = 0; //���Ӽ���,�ֲ�
		byte ks = 0, kss = 0; //ks is count for block,kss for single point
		//���ڵĳɿ�����Ͷ���������
		byte kin1 = 0, m = 0, n = 0; //the block index.
		//a,b��Χ�ĵ�Ŀ�����
		byte gq = 0; //����.
		byte[] u = { 0, 0, 0, 0, 0 }; //position0����
		byte[] v = { 0, 0, 0, 0, 0 }; //�ĸ����ӵ�����
		byte[] tsk = { 0, 0, 0, 0 }; //array for block index.��ͬɫ���ӵĿ�����
		byte[] ysk = { 0, 0, 0, 0 }; //����ɫ���ӵĿ�����,ͬ�鲻�ظ�����
		byte yiseks = 0; //������ɫ����
		byte tzd = 0, tkd = 0; //the count for single pointeaten andblock
							   // eaten.
		//�Եĵ����Ϳ���
		byte ktm = hui[shoushu][36]; //���ŵ�����һ������.
		byte ktn = hui[shoushu][37]; //���ŵ������(��ٲ����Ľ��ŵ�)

		log.debug("come into method cgcl()");
		if (a > ZBXX && a < ZBSX && b > ZBXX && b < ZBSX
				&& zb[a][b][ZTXB] == BLANK) {
			//�±�Ϸ�,�õ�հ�
			if (a == ktm && b == ktn) { //�Ƿ���ŵ�
				log.debug("���Ǵ��ʱ�Ľ��ŵ�,�����ҽٲ�!");
				return;
			} else {
				log.debug("a=" + a + ",b=" + b);
			} //���п������������ŵ�!
		} else { //��һ�಻�Ϸ���.
			log.debug("�õ㲻�Ϸ�,������֮����߸õ��Ѿ�����.");
			log.debug("a=" + a + ",b=" + b);
			return;
		}

		hui[++shoushu][25] = a; //��������ǰ����,����1��ʼ����.������ͬ.
		hui[shoushu][26] = b; //��¼ÿ�������
		yise = (byte) (shoushu % 2 + 1); //yi se=1��2,������Ϊ����
		tongse = (byte) ((1 + shoushu) % 2 + 1); //tong se=1��2,�׺���Ϊż��
		zb[a][b][ZTXB] = tongse; //���Զ�̬һ��

		for (i = 0; i < 4; i++) { //�ȴ�����ɫ����
			byte bdcds = 0; //����Ե����.
			byte bdcks = 0; //����Կ����.
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == yise) { //1.1�ұ����ڵ�
				k1++; //the count of diffrent color.��ɫ�����
				kin1 = zb[m1][n1][KSYXB]; //the block index for the point.66
				if (kin1 == 0) { //not a block.���ǿ�
					zb[m1][n1][QSXB] -= 1;
					if (zb[m1][n1][QSXB] == 0) { //eat the diff point
						k1--; //�����Ҫ��ȥ
						tzd++;
						hui[shoushu][tzd * 2 - 1] = m1;
						hui[shoushu][tzd * 2] = n1;
						log.debug("����:a=" + m1 + ",b=" + n1);
						ktz++; //single pobyte eaten was count
						zzq(m1, n1, tongse); //zi zhen qiͬɫ�ӽ�����.
					} else if (zb[m1][n1][QSXB] == 1) {
						hui[shoushu][27 + bdcds++] = m1;
						hui[shoushu][27 + bdcds++] = n1;
					} else if (zb[m1][n1][QSXB] < 0) {
						log.debug("��������:a=" + m1 + ",b=" + n1);
						return;
					}
				} else { //if (kin1==0)
					for (j = 0; j < yiseks; j++) {
						if (kin1 == ysk[j]) {
							break;
						}
					}
					if (j == yiseks) { //���ظ�
						ysk[yiseks++] = kin1;
						byte qi = (byte) (kuai[kin1][0][0] - 1);
						kdq(kin1, qi);
						if (kuai[kin1][0][0] == 0) {
							k1--;
							tkd++; //<=4
							hui[shoushu][8 + tkd] = kin1;
							ktz += kuai[kin1][(byte) 0][1]; //ʵ�ʵ�������
							kzq(kin1, tongse); //increase the breath of pobyte
											   // surround
							//��ɫ�鱻��,ͬɫ������.
						} else if (kuai[kin1][0][0] == 1) {
							hui[shoushu][32 + bdcks++] = kin1;
						} else if (kuai[kin1][(byte) 0][(byte) 0] < 0) {
							log.debug("��������:kin=" + kin1);
							return;
						}
					} // i==yiseks
				}
			}
		} //��ѭ������

		k0 = k1; //k0 is count for diff point.
		zb[a][b][2] = 0; //return the breath to zero.��ֹ����ʱ������.
		if (shoushu % 2 == BLACK) {
			ktb += ktz;
		} else {
			ktw += ktz; //���ֲ����Ӽ���

		}
		for (i = 0; i < 4; i++) { //�ٴ���հ�����
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] >= 0) {
				linzishu++;
				if (zb[m1][n1][ZTXB] == BLANK) { //2.1the breath of blank
					//���������ĵ��Ƿ�Ϊ��
					k2++; //�������
					u[k0 + k2] = m1;
					v[k0 + k2] = n1; //�浽ÿ�����Ϣ��
				}
			}
		}

		k0 += k2; //k0 is the total points of diff and blank.
		//k0����Ϊ��ɫ������������
		dang = k2; //���ӵ����ڵ�ֱ����
		/*
		 * byte dds=dingdianshu(a,b); byte qikuaishu=0; byte qikuaidian[][]=new
		 * byte[4][2]; if(k2==2){
		 * if((u[k0+k2]-u[k0+1])*(v[k0+k2]-v[k0+1])==1){//б�Խ�
		 * if(zb[u[k0+k2]][v[k0+1]][ZTXB]==BLANK||
		 * zb[u[k0+1]][v[k0+k2]][ZTXB]==BLANK){//���Ƿ�Ͽ�
		 * qikuaidian[qikuaishu][0]=u[k0+k2];
		 * qikuaidian[qikuaishu++][1]=v[k0+k2]; //qikuaishu=1; } else{
		 * qikuaidian[qikuaishu][0]=u[k0+1]; qikuaidian[qikuaishu++][1]=v[k0+1];
		 * qikuaidian[qikuaishu][0]=u[k0+k2];
		 * qikuaidian[qikuaishu++][1]=v[k0+k2]; //qikuaishu=2; } } else {
		 * //qikuaishu=2; qikuaidian[qikuaishu][0]=u[k0+1];
		 * qikuaidian[qikuaishu++][1]=v[k0+1];
		 * qikuaidian[qikuaishu][0]=u[k0+k2];
		 * qikuaidian[qikuaishu++][1]=v[k0+k2]; } } else if(k2==3){
		 * if(zb[u[k0+2]][v[k0+1]][ZTXB]==BLANK||
		 * zb[u[k0+1]][v[k0+2]][ZTXB]==BLANK){ //qikuaishu=1;
		 * qikuaidian[qikuaishu][0]=u[k0+1]; qikuaidian[qikuaishu++][1]=v[k0+1]; }
		 * else{ qikuaidian[qikuaishu][0]=u[k0+1];
		 * qikuaidian[qikuaishu++][1]=v[k0+1]; qikuaidian[qikuaishu][0]=u[k0+2];
		 * qikuaidian[qikuaishu++][1]=v[k0+2]; //qikuaishu=2; }
		 * if(zb[u[k0+2]][v[k0+3]][ZTXB]==BLANK||
		 * zb[u[k0+3]][v[k0+2]][ZTXB]==BLANK){ log.debug("����ȫ����"); }
		 * else{ qikuaidian[qikuaishu][0]=u[k0+k3];
		 * qikuaidian[qikuaishu++][1]=v[k0+k3]; //qikuaishu++; } } else
		 * if(k2==4){ if(dds==2){ } if(dds==3){ } if(dds==4){ //todo:ȷ���������ԭʼ��. }
		 * }//4��18�� for(i=0;i <4;i++){ m1=qikuaidian[i][0]; n1=qikuaidian[i][1];
		 * if(m1!=0){ zishu=0; chengkuai(m1,n1,BLANK);
		 * if(zishu==kuai[zb[a][b][KSYXB]][0][1]-1){
		 * kuai[zb[a][b][KSYXB]][0][1]-=1; break; } else{ } } else break; }
		 */
		//���ݳɿ��㷨ͳ������,���Ƿ������¿�
		for (i = 0; i < 4; i++) { //�ٴ���ͬɫ����
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == tongse) { //3.1
				k3++; //ͬɫ�����
				kin1 = zb[m1][n1][KSYXB];
				if (kin1 == 0) { //������
					kss++; //same color single point.���������
					dang += zb[m1][n1][QSXB];
					dang--; //current pobyte close one breath of surr point.
					u[k0 + kss] = m1; //u[0] not used
					v[k0 + kss] = n1; //deal with single point.
				} else { //231�ɿ��
					for (j = 0; j < ks; j++) {
						if (kin1 == tsk[j]) {
							break;
						}
					}
					if (j == ks) { //���ظ�
						dang += kuai[kin1][0][0]; //��Ϊ����
						dang--;
						u[linzishu - ks] = m1; //deal with block.
						v[linzishu - ks] = n1;
						tsk[ks++] = kin1; //
					}
				} //�ɿ��
			}
		}
		/*
		 * if(dang>0){//dang���ܴ�������������,��������Ϊ0. ktm=0;//ԭ���Ľ��ŵ�ʵЧ,��Ϊ�Ѿ�Ѱ�� ktn=0;
		 * byte [] tsk={0,0,0,0};//����ɫ���ӵĿ�����,ͬ�鲻�ظ����� byte tsks=0;//������ɫ���� byte
		 * lin1=0,lin2=0;//2��23�� for(i=0;i <4;i++){ m1=(byte)(a+szld[i][0]);
		 * n1=(byte)(b+szld[i][1]); if
		 * (zb[m1][n1][ZTXB]==yise&&zb[m1][n1][QSXB]==1) { //1.1�ұ����ڵ��Ƿ񱻴��
		 * kin1=zb[m1][n1][KSYXB];//the block index for the point.66 if
		 * (kin1==0){ //not a block.���ǿ� lin1++; hui[shoushu][26+lin1*2-1]=m1;
		 * hui[shoushu][26+lin1*2]=n1; } else { for(i=0;i <tsks;i++){
		 * if(kin1==tsk[i]) break; } if(i==tsks){ lin2++;
		 * hui[shoushu][32+lin2]=kin1; } } } } }//if dang>0
		 */
		if (dang == 0) { //?���������˵���,����ǿ���?û��ϵ,������ļ���.
			//showStatus("//this point is prohibited,try again!");
			zzq(a, b, yise); //�൱�����Ӻ�����һ�����,�������������
			for (i = 25; i <= 35; i++) {
				hui[shoushu][i] = 0;
			}
			shoushu--;
			zb[a][b][ZTXB] = BLANK;
			return;
		} //��������ɱshowStatus("qing="+dang+a+b);

		if (k3 == 0) { //4.1 no same color pobyte surroundû��ͬɫ�ڵ�
			log.debug("//k3=0");
			zb[a][b][2] = dang;
			if (dang == 1 && ktz == 1) { //���ǽ�
				ktm = u[linzishu]; //��Ϊ�ȴ�����ɫ��,�ٿհ׵�,����ͬɫ��.
				ktn = v[linzishu]; //��Ϊ���һ��?����ǽ��ϵĽ���?��4��Ϊlinzishu
				hui[shoushu][36] = ktm; //�հ׵㼴Ϊ��ٵĽ��ŵ�.
				hui[shoushu][37] = ktn; //2yue23����
			} //not conform to so. en.
			if (dang >= 2) { //Ϊ���Ӽ����ṩ��Ϣ
				zb[a][b][4] = u[k1 + 1];
				zb[a][b][5] = v[k1 + 1];
				zb[a][b][6] = u[k1 + 2];
				zb[a][b][7] = v[k1 + 2];
			} else { //dang==1
				zb[a][b][4] = u[k1 + 1];
				zb[a][b][5] = v[k1 + 1];
			}
			return;
		}

		if (ks == 0) { //4.2 only single pobyte surr.��ͬɫ��,����Ϊ������
			log.debug("//ks=0");
			gq = 0;
			for (i = 1; i <= kss; i++) { //4.1 deal surr point�������ڶ�����
				hui[shoushu][12 + i * 2 - 1] = u[k0 + i]; //��¼�ϲ��ɿ�Ķ�����
				hui[shoushu][12 + i * 2] = v[k0 + i]; //��13��20
				for (j = 1; j <= (kss - i); j++) { //�����֮��Ĺ���
					gq += dd(u[k0 + i], v[k0 + i], u[k0 + i + j], v[k0 + i + j]);
				}
			}
			zb[a][b][QSXB] = (byte) (dang - gq);
			zb[a][b][KSYXB] = (byte)++ki; //count from first block
			hui[shoushu][0] = (byte)ki; //��¼���ɿ������
			kuai[ki][0][0] = zb[a][b][2];
			kuai[ki][0][1] = (byte) (k3 + 1);
			kuai[ki][k3 + 1][0] = a; //���һ��Ϊa,b
			kuai[ki][k3 + 1][1] = b;
			for (i = 1; i <= k3; i++) { //����Χ��ϲ�������
				m = u[k0 + i];
				n = v[k0 + i];
				kuai[ki][i][0] = m;
				kuai[ki][i][1] = n;
				zb[m][n][4] = 0; //�������Ϣ���ڵ��д洢
				zb[m][n][5] = 0;
				zb[m][n][6] = 0;
				zb[m][n][7] = 0;
				zb[m][n][QSXB] = zb[a][b][QSXB];
				zb[m][n][KSYXB] = (byte)ki;
			}
			if (zb[a][b][2] != jskq(ki)) {
				log.debug("error of breath");
			}
		}

		if (ks > 0) { //�����п�
			log.debug("//ks>0");
			ki++;
			hui[shoushu][0] = (byte)ki;
			kuai[ki][0][1] = 1; //������ʱ��
			kuai[ki][1][0] = a; //a,b������λ
			kuai[ki][1][1] = b;
			zb[a][b][KSYXB] = (byte)ki;
			for (i = 1; i <= kss; i++) { //��ϲ����
				hui[shoushu][12 + i * 2 - 1] = u[k0 + i];
				hui[shoushu][12 + i * 2] = v[k0 + i];
				dkhb(u[k0 + i], v[k0 + i], (byte)ki); //���ڵ㲢����ʱ��
			}
			// dkhb(a,b,k[1]);
			for (j = 1; j <= ks; j++) {
				hui[shoushu][20 + j] = tsk[j - 1];
				kkhb((byte)ki, tsk[j - 1]); //not deal with breath���ϲ�,����δ����.
			}
			// hui[shoushu][0]=ki;
			//zb[a][b][2]=tongse;
			//kuai[k[1]][0][0]=zb[a][b][2];//? need deal with breath.
			dang = jskq(ki);
			kdq((byte)ki, dang);
			/*
			 * if(dang==0){ hui[shoushu][0]=ki; kzq(ki,yise); ktm=-1; ktn=-1;
			 * hui[shoushu][0]=ki; // return; }
			 */
		}
	}

	//one point was eaten
	public void zzq(byte a, byte b, byte tiao) { //function 6.1��֮��ĳ�ӱ������������.
		byte c1 = 0, i, j, yiseks = 0;
		byte m1, n1;
		byte ysk[] = { 0, 0, 0, 0 };
		for (i = 0; i < 4; i++) {
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == tiao) {
				c1 = zb[m1][n1][KSYXB];
				if (c1 == 0) {
					zb[m1][n1][QSXB] += 1;
					if (zb[m1][n1][QSXB] == 2) {
						zb[m1][n1][6] = a;
						zb[m1][n1][7] = b;
					}

				} else {
					for (j = 0; j < yiseks; j++) {
						if (c1 == ysk[j]) {
							break;
						}
					}
					if (j == yiseks) { //���ظ�
						ysk[yiseks++] = c1;
						kdq(c1, kuai[c1][0][0] += 1);
					}

				}
			}
		}
		zb[a][b][ZTXB] = BLANK;
		zb[a][b][QSXB] = 0;
		zb[a][b][KSYXB] = 0;
		zb[a][b][4] = 0;
		zb[a][b][5] = 0;
		zb[a][b][6] = 0;
		zb[a][b][7] = 0;
	}

	public void kzq(byte r, byte tiao) { //6.2 yi se kuai bei ti
		//�����ɫ��ʱ,ͬɫ����������
		byte n = 0;
		byte p = 0, q = 0;
		n = kuai[r][0][1];
		for (byte i = 1; i <= n; i++) {
			p = kuai[r][i][0];
			q = kuai[r][i][1];
			zzq(p, q, tiao);
			//����ԭ����Ϣ,��Ҫ��������Ϣ,���ڻ���ʱ�ָ�
		}
		kuai[r][0][0] = 0;

	}

	public void zjq(byte a, byte b, byte tiao) { //function 6.1
		byte c1 = 0, i, m1, j, n1, yiseks = 0;
		byte ysk[] = { 0, 0, 0, 0 };
		for (i = 0; i < 4; i++) {
			m1 = (byte) (a + szld[i][0]);
			n1 = (byte) (b + szld[i][1]);
			if (zb[m1][n1][ZTXB] == tiao) {
				c1 = zb[m1][n1][3];
				if (c1 == 0) {
					zb[m1][n1][QSXB] -= 1;
					if (zb[m1][n1][QSXB] < 1) {
						log.debug("����ʱ��������:a=" + m1 + ",b=" + n1);
					}
				} else {
					for (j = 0; j < yiseks; j++) {
						if (c1 == ysk[j]) {
							break;
						}
					}
					if (j == yiseks) { //���ظ�
						ysk[yiseks++] = c1;
						kdq(c1, kuai[c1][0][0] -= 1);
					}
				}
			}
		}

	}

	public void kjq(byte r, byte tiao) { //����ʱ,�ɿ�ָ�ʹͬɫ�Ӽ���
		byte n = 0; //the same color block is eaten
		byte p = 0, q = 0; //û������ʱ,tiaoֻ����ͬɫ.
		n = kuai[r][0][1];
		for (byte i = 1; i <= n; i++) {
			p = kuai[r][i][0];
			q = kuai[r][i][1];
			zjq(p, q, tiao);
		}
		kuai[r][0][0] = 1; //�����ָ�,����Ϊ1.
	}

	public byte dd(byte a, byte b, byte c, byte d) { //7.1diang diang gong qi
		byte gq = 0; // consider four points only.
		if (zb[a][d][0] == 0) { //�㹲��ֻ�����ֿ���,���λ��Ϊ����һ����
			gq++; //���߲��ؿ���,�ɵ�ǰ�ŵ��������м�,�����ظ���������.
		}
		if (zb[c][b][0] == 0) {
			gq++;
		}
		log.debug("����dd,���㹲��=" + gq + "\n");
		return gq;
	}

	//6.7diang kuai he bing

	public void dkhb(byte p, byte q, byte r) { //8.1
		byte ss = (byte) (kuai[r][0][1] + 1); //���������1;
		kuai[r][ss][0] = p;
		kuai[r][ss][1] = q;
		kuai[r][0][1] = ss;
		zb[p][q][3] = r;
		zb[p][q][4] = 0;
		zb[p][q][5] = 0;
		zb[p][q][6] = 0;
		zb[p][q][7] = 0;
		log.debug("����dkhb:���ϲ�\n");
	}

	//6.8 ss1 shi zu yao kuai!
	public void kkhb(byte r1, byte r2) { //8.2����ǰ��,����δ��
		byte ss1 = kuai[r1][0][1];
		byte ss2 = kuai[r2][0][1];
		byte m = 0, n = 0;
		for (byte i = 1; i <= ss2; i++) {
			m = kuai[r2][i][0];
			n = kuai[r2][i][1];
			zb[m][n][3] = r1;
			//����ԭ����Ϣ
			kuai[r1][ss1 + i][0] = m;
			kuai[r1][ss1 + i][1] = n;
		}
		kuai[r1][0][1] = (byte) (ss1 + ss2);
		log.debug("����dkhb:���ϲ�");
	}

	public byte jskq(short r2) {
		byte qishu = 0; //the breath of the block
		byte a = 0, b = 0;
		byte m, n;
		byte zishu = kuai[r2][0][1]; //�������
		byte i, j;
		for (i = 1; i <= zishu; i++) {
			m = kuai[r2][i][0];
			n = kuai[r2][i][1];
			for (j = 0; j < 4; j++) {
				a = (byte) (m + szld[j][0]);
				b = (byte) (n + szld[j][1]);
				if (zb[a][b][ZTXB] == BLANK && zb[a][b][SQBZXB] == 0) {
					qishu++;
					zb[a][b][1] = 1;
					kuai[r2][49 + qishu][0] = a; //�洢�����±�50��ʼ
					kuai[r2][49 + qishu][1] = b;
				}
			}
		} //for

		for (i = 1; i <= qishu; i++) { //�ָ���־
			a = kuai[r2][49 + i][0];
			b = kuai[r2][49 + i][1];
			zb[a][b][SQBZXB] = 0;
		}
		return qishu;
	} //2��22�ո�,ԭ��������,��ֻ����ʹ�.

	//10.1ji suan kuai qi.

	public void kdq(byte kin, byte a) { //11.1 kuai ding qi�鶨��
		byte m = 0, n = 0, p = 0;
		p = kuai[kin][0][1];
		for (byte i = 1; i <= p; i++) {
			m = kuai[kin][i][0];
			n = kuai[kin][i][1];
			zb[m][n][2] = a;
		}
		kuai[kin][0][0] = a;
	}

	public void clhuiqi() { //�Ƿ��������ݽṹ���ָܻ�?
		byte p = 0;
		byte yise = 0;
		byte tongse = 0; //yise is diff color.and 2 same.
		byte tdzs = 0;
		byte k0 = 0, k1 = 0, k2 = 0, k3 = 0, i = 0, j = 0; //the count for
														   // three kinds of
														   // point.
		byte ks = 0, kss = 0; //ks is count for block,kss for single point
		byte kin, kin1 = 0, m = 0, n = 0; //the block index.

		tongse = (byte) ((shoushu + 1) % 2 + 1); //tong se
		yise = (byte) (shoushu % 2 + 1);
		m = hui[shoushu][25];
		hui[shoushu][25] = 0;
		n = hui[shoushu][26];
		hui[shoushu][26] = 0;
		if (m <= 0 || n <= 0) { //��Ȩ�Ļָ�
			shoushu--;
			return; //
		}
		zzq(m, n, yise); //����,�Է�����,����ֱ�ӻָ�,�����ڴ�����
		log.debug("����:" + shoushu);
		log.debug("a=" + m + ",b=" + n);
		kin = hui[shoushu][0];
		if (kin > 0) { //�Ƿ���¿�,�Դ˴�������
			for (i = 0; i < 70; i++) {
				kuai[kin][i][0] = 0;
				kuai[kin][i][1] = 0;
			}
			ki = kin; //ȫ�ֿ��ÿ��?
			for (i = 1; i <= 4; i++) {
				if (hui[shoushu][2 * i + 12 - 1] < 0) { //���¿�ĵ�
					break;
				} else {
					m = hui[shoushu][12 + 2 * i - 1]; //13-20
					n = hui[shoushu][12 + 2 * i];
					hui[shoushu][12 + 2 * i - 1] = 0;
					hui[shoushu][12 + 2 * i] = 0;
					zb[m][n][3] = 0;
					zb[m][n][0] = tongse; //fang wei bian cheng
					zb[m][n][2] = jszq(m, n); //�����ӵ���
					log.debug("//����ɿ�����:" + "a=" + m + ",b" + n);
				}
			} //deal with 3 sub
			for (i = 1; i <= 4; i++) { //�Ƿ�ɿ���¿�
				kin1 = hui[shoushu][20 + i]; //21-24
				hui[shoushu][20 + i] = 0;
				if (kin1 == 0) {
					break;
				} else {
					p = kuai[kin1][0][1];
					for (j = 1; j <= p; j++) {
						m = kuai[kin1][j][0];
						n = kuai[kin1][j][1];
						zb[m][n][3] = kin1; //�޸Ŀ��
						//zb[m][n][0]=tongse;
						zb[m][n][2] = kuai[kin1][0][0]; //�ָ�ԭ��ɿ�ʱ����
					}
				} //else
			} //for
		} //if �Ƿ��¿�
		for (i = 1; i <= 4; i++) { //�Ƿ�����
			if (hui[shoushu][2 * i - 1] <= 0) {
				break;
			} else {
				m = hui[shoushu][2 * i - 1];
				n = hui[shoushu][2 * i];
				hui[shoushu][2 * i - 1] = 0;
				hui[shoushu][2 * i] = 0;
				tdzs = i; //?
				zb[m][n][ZTXB] = yise;
				zb[m][n][QSXB] = 1;
				zb[m][n][KSYXB] = 0;
				zjq(m, n, tongse);
				log.debug("�ָ�������:");
				log.debug("a=" + m + ",b=" + n);
			}
		} //for

		for (i = 1; i <= 4; i++) { //�Ƿ��б���Ŀ�
			if (hui[shoushu][8 + i] <= 0) {
				break;
			} else {
				kin1 = hui[shoushu][8 + i];
				hui[shoushu][8 + i] = 0;
				kdq(kin1, (byte) 1);
				kjq(kin1, tongse);
				p = kuai[kin1][0][1];
				for (j = 1; j <= p; j++) {
					m = kuai[kin1][j][0];
					n = kuai[kin1][j][1];
					zb[m][n][0] = yise;
					zb[m][n][3] = kin1;
				}
				tdzs += p;
			} //else
		} //for
		if (tongse == BLACK) {
			ktb -= tdzs;
		}
		if (tongse == WHITE) {
			ktw -= tdzs;
		}
		for (i = 0; i < 9; i++) {
			hui[shoushu][27 + i] = 0; //2yue
		}
		shoushu--;
		log.debug("����clhuiqi:�������\n");
	} //clhuiqi

	public void shengchengjumian() {
		//�����׵�λͼ��ʾ����kuai��zb�������Ӧ��Ϣ
		byte i, j;
		for (i = 1; i < 20; i++) { //i��������
			for (j = 1; j < 20; j++) { //j�Ǻ�����.ƽ��ɨ��
				if (zb[j][i][SQBZXB] == 1) {
					continue; //SQBZXB�˴��൱�ڴ�����ı�־.
				}
				zishu = 0;
				if (zb[j][i][ZTXB] == BLACK) { //��.�ϱ�Ϊ�յ����ɫ��
					ki++;
					chengkuai(j, i, BLACK); //�ж���.���Ƿ�Ϊͬɫ��.
				} else if (zb[j][i][ZTXB] == WHITE) { //��.�ϱ�Ϊ�յ����ɫ��
					ki++;
					chengkuai(j, i, WHITE); //�ж���.���Ƿ�Ϊͬɫ��
				} else { //����
					ki++; //��Ϊ�ɿ麯���� ͳһ��ki��
					chengkuai(j, i, BLANK);
					/*
					 * if(zishu==1){//��λ kuai[ki][1][0]=0; kuai[ki--][1][1]=0;
					 * zb[j][i][KSYXB]=0;//�ǿ� //todo:��λ�Ĵ��� }
					 */
					//else if(zishu>1){
					kuai[ki][0][1] = zishu;
					zishu = 0;
					for (byte p = 0; p < 70; p++) {
						kuai[qiki][p][0] = kuai[ki][p][0];
						kuai[ki][p][0] = 0;
						kuai[qiki][p][1] = kuai[ki][p][1];
						kuai[ki][p][1] = 0;
					}
					qiki--;
					ki--;
					//}
					// else log.debug("error:zishu<1");

					continue;
				}
				if (zishu == 1) {
					//ki--;
					kuai[ki][1][0] = 0;
					kuai[ki--][1][1] = 0;
					zb[j][i][KSYXB] = 0; //�ǿ�
				} else if (zishu > 1) {
					kuai[ki][0][1] = zishu;
					zishu = 0;
				} else {
					log.debug("error:zishu<1");
				}
			}
		} //���ɿ�
		for (i = 1; i < 20; i++) { //i��������
			for (j = 1; j < 20; j++) { //j�Ǻ�����
				zb[j][i][SQBZXB] = 0; //�ָ�ÿ�����������־
				if (zb[j][i][ZTXB] > 0 & zb[j][i][KSYXB] == 0) {
					zb[j][i][QSXB] = jszq(j, i);
				}
			}
		} //�������
		//biaojihuifu();//����֮ǰ�ָ���ǡ�
		for (i = 1; i <= ki; i++) {
			//byte qi=jskq(ki);�������������ֱ�Ӵ�������.
			kuai[ki][0][0] = jskq(ki);
			output(i);
		} //�������

	}

	byte zishu;

	public void chengkuai(byte a, byte b, byte color) {
		//�ռ���Ϣ�Ĺ�����,������color=BLANK,���øú���,�����������Ϣ
		//����פ����kuai������,���������ò����.
		byte m1, n1;
		//byte zishu=0;
		if (zishu < 49) { //��ֹ����̫�ࡣ
			kuai[ki][++zishu][0] = a;
			kuai[ki][zishu][1] = b;
		} else {

			log.debug("�ÿ����������49,���Ϊ:" + ki);
		}
		zb[a][b][SQBZXB] = 1;
		zb[a][b][KSYXB] = (byte)ki;
		//zishu++;
		for (byte k = 0; k < 4; k++) {
			m1 = (byte) (a + szld[k][0]);
			n1 = (byte) (b + szld[k][1]);
			if (zb[m1][n1][SQBZXB] == 0 & zb[m1][n1][ZTXB] == color) {
				chengkuai(m1, n1, color);
			}
		}
	} //�ɿ�ĵ�SQBZXB==1;

	public void output(byte kin) { //��ӡ������������������
		//�Լ��������������ꡣ
		byte i, p;
		p = kuai[kin][0][1];
		byte qishu = kuai[kin][0][0];
		log.debug("\n���Ϊ��" + kin);
		for (i = 1; i <= p; i++) {
			log.debug("" + kuai[kin][i][0] + "," + kuai[kin][i][1]
					+ "\t");
			if (i == 5) {
				log.debug("i=5");
			}
		}
		log.debug("\n����Ϊ��" + qishu);
		for (i = 1; i <= qishu; i++) {
			log.debug("" + kuai[kin][49 + i][0] + ","
					+ kuai[kin][49 + i][1] + "\t");
			if (i == 5) {
				log.debug("i=5");
			}
		}

	}

	public void biaojihuifu() {
		//���ڳɿ��ı�ǻָ�����Ϊÿ��Ĳ������ԣ�ֻ�����ͳһ�ָ�һ�Ρ�
		byte i, j;
		for (i = 0; i < 21; i++) {
			for (j = 0; j < 21; j++) {
				zb[i][j][SQBZXB] = 0;
			}
		}
	}

	public GoBoard() {
		byte i, j;
		final byte PANWAIDIAN = -1; //����֮��ı�־;
		for (i = 0; i < 21; i++) { //2��22�ռ�
			zb[0][i][0] = PANWAIDIAN;
			zb[20][i][0] = PANWAIDIAN;
			zb[i][0][0] = PANWAIDIAN;
			zb[i][20][0] = PANWAIDIAN;
		} //2��22�ռ�
	}

	public byte jszq(byte m, byte n) {
		byte dang = 0; //��������
		byte i, a, b;
		for (i = 0; i < 4; i++) {
			a = (byte) (m + szld[i][0]);
			b = (byte) (n + szld[i][1]);
			if (zb[a][b][ZTXB] == BLANK) { //2.1the breath of blank
				dang++;
			}
		}
		return dang;
	}

	public byte dingdianshu(byte m1, byte n1) {
		byte[][] dingdian = { { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };
		//����-����-����-����
		byte i, dds = 0;
		byte m, n;
		for (i = 0; i < 4; i++) {
			m = (byte) (m1 + dingdian[i][0]);
			n = (byte) (n1 + dingdian[i][1]);
			if (zb[m][n][ZTXB] == BLACK || zb[m][n][ZTXB] == WHITE) {
				dds++;
			}
		}
		return dds;
	}

	public GoBoard(GoBoard goold) {

		byte i, j, k;
		for (i = 0; i < 21; i++) {
			for (j = 0; j < 21; j++) {
				for (k = 0; k < 4; k++) {
					zb[i][j][k] = goold.zb[i][j][k];
				}
			}
		}
		for (i = 0; i < 128; i++) {
			for (j = 0; j < 70; j++) {
				for (k = 0; k < 2; k++) {
					kuai[i][j][k] = goold.kuai[i][j][k];
				}
			}
		}
		for (i = 0; i < 512; i++) {
			for (j = 0; j < 38; j++) {
				hui[i][j] = goold.hui[i][j];
			}
		}

	}

	public byte[][] houxuandian(byte a, byte b) {
		//���ݻ�������Ŀ�š������ף���������ã�ȷ����ѡ��
		//�Ȱ����������������û��������ֻ����λ��ѡ��

		//�����Է�ǿ�壨�����ͣ����ߺ�û��ֱ�ӵ�������Ч�����������������
		//�����ۡ�

		//1.���ǿ�Ӻ�����չ�������Է�ǿ�ӽ�����
		//2.�Ӽ�����������������ȫ�档
		//3.�ֲ���ģʽʶ�������֡�
		//4.���ۣ�����ֱ�����Ͷ��μ������
		byte[][] fanhui = new byte[40][2];
		byte qishu, i, j, kc;
		byte m, n, m1, n1;
		byte kin = zb[a][b][KSYXB];

		qishu = kuai[kin][0][0]; //����
		kc = 0;
		for (j = 1; j <= qishu; j++) { //ֱ����
			kc++;
			m = fanhui[kc][0] = kuai[kin][49 + j][0];
			n = fanhui[kc][1] = kuai[kin][49 + j][1];
			zb[m][n][SQBZXB] = 1; //���ϱ��

			for (i = 0; i < 4; i++) { //�����
				m1 = (byte) (m + szld[i][0]);
				n1 = (byte) (n + szld[i][1]);
				if (zb[m1][n1][ZTXB] == BLANK && zb[m1][n1][SQBZXB] == 0) { //1.1�ұ����ڵ�
					kc++;
					zb[m1][n1][SQBZXB] = 1;
					fanhui[kc][0] = m1;
					fanhui[kc][1] = n1;
				}
			}
		}
		fanhui[0][0] = kc;
		for (j = 1; j <= kc; j++) {
			zb[fanhui[j][0]][fanhui[j][1]][SQBZXB] = 0; //�ָ����
		}
		return fanhui; //�������Ӻ����Ӷ�̬������ɾ��
	}

	public boolean validate(byte a, byte b) {
		//��shortLian�������������ܲ����ݡ���
		//����λ�õ���Ч�ԡ�
		byte m, n, qi = 0;
		//��shoushu����֮ǰ���ã�yise��tongse�ļ���������ͬ��
		byte tongse = (byte) (shoushu % 2 + 1); //yi se=1��2,������Ϊ����
		byte yise = (byte) ((1 + shoushu) % 2 + 1); //tong se=1��2,�׺���Ϊż��
		if (a > ZBXX && a < ZBSX && b > ZBXX && b < ZBSX
				&& zb[a][b][ZTXB] == BLANK) {
			//�±�Ϸ�,�õ�հ�
			if (a == hui[shoushu][2] && b == hui[shoushu][3]) { //�Ƿ���ŵ�
				log.debug("���Ǵ��ʱ�Ľ��ŵ�,�����ҽٲ�!");
				log.debug("���Ϊ��a=" + a + ",b=" + b);
				return false;
			} else {
				//log.debug("���Ϊ��a=" + a + ",b=" + b);
				for (byte i = 0; i < 4; i++) {
					m = (byte) (a + szld[i][0]);
					n = (byte) (b + szld[i][1]);
					if (zb[m][n][ZTXB] == BLANK) {
						return true;
					} else if (zb[m][n][ZTXB] == yise) {
						if (zb[m][n][QSXB] == 1) {
							return true; //todo
						}
					} else if (zb[m][n][ZTXB] == tongse) {
						if (zb[m][n][QSXB] > 1) {
							return true;
						} else {
							qi += zb[m][n][QSXB]; //���۳ɿ���񶼳���
							qi--;
						}
					}
				}
				if (qi == 0) {
					log.debug("������ɱ�Ľ��ŵ㣺");
					log.debug("a=" + a + ",b=" + b);
					return false;
				} else {
					log.debug("���ǺϷ��ŵ㣺");
					log.debug("a=" + a + ",b=" + b);
					return true;
				}
			}
		} else { //��һ�಻�Ϸ���.
			log.debug("�õ㲻�Ϸ�,������֮����߸õ��Ѿ����ӣ�");
			log.debug("a=" + a + ",b=" + b);
			return false;
		}
	} //��shortLian�������������ܲ����ݡ���

	/*
	 * public byte qiangruo(){ //����ǿ���жϳ���������塣���ؿ�š� byte i, j, k; byte zishu;
	 * for (i = 1; i < ki; i++) { zishu = kuai[i][0][1]; } }
	 */
	/*
	 * public byte zhankai(byte a,byte b){ //�����أ��յ������ĸ��� }
	 */

}