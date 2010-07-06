package eddie.wu.chess.domain;

public class Horse extends Chessman{
	public Horse(ChessColor color,int a, int b){
		super(color,a,b);
	}
	@Override
	public boolean isValidStep(ChessBoard board, int m, int n) {
		byte ca = (byte) (m - a);
		byte cb = (byte) (n - b);
		byte xun;
		byte ywzb = b;
		byte ywza = a;
		
		if (Math.abs(ca * cb) != 2) {
			System.out.println("���Ϲ���");
			return false;
		}
		if (Math.abs(ca) == 1) { // ע�⸺ֵ
			if (cb == 2) {
				if (board.men[ywza][ywzb + 1] != null) {
					System.out.println("���Ϲ���:�����ȡ�");
					return false;
				}
			} else if (cb == -2) {
				if (board.men[ywza][ywzb - 1] != null) {
					System.out.println("���Ϲ���:�����ȡ�");
					return false;
				}
			}
		} else if (ca == 2) {
			if (board.men[ywza + 1][ywzb] != null) {
				System.out.println("���Ϲ���:�����ȡ�");
				return false;
			}
		} else if (ca == -2) {
			if (board.men[ywza - 1][ywzb] != null) {
				System.out.println("���Ϲ���:�����ȡ�");
				return false;
			}
		}
		System.out.println("�Ϻ�����");
		
		return true;
	}

}
