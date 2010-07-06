package eddie.wu.chess.domain;

public class Pawn extends Chessman {
	public Pawn(ChessColor color, int a, int b) {
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
		if (ChessColor.RED.equals(board.currentColor)) {
			System.out.println("��ǰ��Ϊ���");
			if (ca == -1) {
				if (cb != 0) {
					return true;
				}
			} else if (ca == 0 && ywza < 6 && Math.abs(cb) == 1) {

			} else {
				return true;
			}
		} else if (ChessColor.BLACK.equals(color)) { // �ڷ�
			System.out.println("��ǰ��Ϊ�ڱ�");
			if (ca == 1) {
				if (cb != 0) {
					System.out.println("���Ϻ�����:���ӱ�");
					return true;
				}
			} else if (ca == 0 && ywza > 5 && Math.abs(cb) == 1) {
				System.out.println("�Ϻ�����:���ӱ�");
			} else {
				return true;
			}

		}
		System.out.println("�Ϻ�����");
		return false;
	}

}
