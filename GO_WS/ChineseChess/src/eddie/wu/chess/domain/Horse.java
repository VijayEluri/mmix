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
			System.out.println("不合规则。");
			return false;
		}
		if (Math.abs(ca) == 1) { // 注意负值
			if (cb == 2) {
				if (board.men[ywza][ywzb + 1] != null) {
					System.out.println("不合规则:别马腿。");
					return false;
				}
			} else if (cb == -2) {
				if (board.men[ywza][ywzb - 1] != null) {
					System.out.println("不合规则:别马腿。");
					return false;
				}
			}
		} else if (ca == 2) {
			if (board.men[ywza + 1][ywzb] != null) {
				System.out.println("不合规则:别马腿。");
				return false;
			}
		} else if (ca == -2) {
			if (board.men[ywza - 1][ywzb] != null) {
				System.out.println("不合规则:别马腿。");
				return false;
			}
		}
		System.out.println("合乎规则");
		
		return true;
	}

}
