package eddie.wu.chess.domain;

public class Cannon extends Chessman{
	public Cannon(ChessColor color,int a, int b){
		super(color,a,b);
	}
	@Override
	public boolean isValidStep(ChessBoard board, int m, int n) {
		byte ca = (byte) (m - a);
		byte cb = (byte) (n - b);
		byte xun;
		byte ywzb = b;
		byte ywza = a;
		
		System.out.println("��ǰ��Ϊ��");
		byte count = 0;
		if (ca == 0) {
			if (cb == 0) {
				return true;
			} else if (cb > 0) {
				for (xun = (byte) (ywzb + 1); xun < n; xun++) {
					if (board.men[m][xun] != null) {
						count += 1;
					}
				}
			} else if (cb < 0) {
				for (xun = (byte) (ywzb - 1); xun > n; xun--) {
					if (board.men[m][xun] != null) {
						count += 1;
					}
				}

			}

		} else if (cb == 0) {
			System.out.println("�����ƶ�");
			if (ca > 0) {
				for (xun = (byte) (ywza + 1); xun < m; xun++) {
					if (board.men[xun][n] != null) {
						count += 1;
					}
				}
			} else if (ca < 0) {
				for (xun = (byte) (ywza - 1); xun > m; xun--) {
					if (board.men[xun][n] != null) {
						count += 1;
					}
				}

			}

		}		else {
			return true; // �������
		}
//		if (actnew == 0) { // �ƶ�
//			if (count > 0) {
//				return true;
//			}
//			System.out.println("�Ϻ�����");
//			break;
//
//		} else if (qizi[active].color != qizi[actnew].color) {
//			// ����
//
//			if (count != 1) {
//				return true;
//			}
//			System.out.println("��������");
//
//			System.out.println("�Ϻ�����");
//			break;
//		} else if (qizi[active].color == qizi[actnew].color) {
//			// active = zb[m][n];
//			System.out.println("�ı䵱ǰ��Ծ�㡣");
//		}
		/*
		 * else { return true; System.out.println("�Ϻ�����"); }
		 */
		
		return false;
	}

}
