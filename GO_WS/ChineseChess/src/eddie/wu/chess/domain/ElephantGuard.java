package eddie.wu.chess.domain;

public class ElephantGuard extends Chessman {
	static byte[][] shixiang = new byte[12][11]; // ��������ʵ��
	static {
		shixiang[3][1] = 2; // �����Ч�㡣
		shixiang[3][5] = 2;
		shixiang[3][9] = 2;
		shixiang[1][3] = 2;
		shixiang[1][7] = 2;
		shixiang[5][3] = 2;
		shixiang[5][7] = 2;

		shixiang[8][1] = 2;
		shixiang[8][5] = 2;
		shixiang[8][9] = 2;
		shixiang[10][3] = 2;
		shixiang[10][7] = 2;
		shixiang[6][3] = 2;
		shixiang[6][7] = 2;
	}

	public ElephantGuard(ChessColor color, int a, int b) {
		super(color, a, b);
	}

	@Override
	public boolean isValidStep(ChessBoard board, int m, int n) {
		byte ca = (byte) (m - a);
		byte cb = (byte) (n - b);
		byte xun;
		byte ywzb = b;
		byte ywza = a;

		System.out.println("��ǰ��Ϊ��");
		if (Math.abs(ca) != 2 || Math.abs(cb) != 2) {
			return false;
		} else if (shixiang[m][n] == 0) {// not valid point
			return false;
		} else if (board.men[ywza + ca / 2][ywzb + cb / 2] != null) {
			System.out.println("������");
			return false;
		}
		System.out.println("�Ϻ�����");
		return true;
	}

}
