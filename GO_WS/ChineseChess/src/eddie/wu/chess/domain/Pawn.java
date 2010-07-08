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

		System.out.println("当前点为兵");
		if (ChessColor.RED.equals(board.currentColor)) {
			System.out.println("当前点为红兵");
			if (ca == -1) {
				if (cb != 0) {
					return true;
				}
			} else if (ca == 0 && ywza < 6 && Math.abs(cb) == 1) {

			} else {
				return true;
			}
		} else if (ChessColor.BLACK.equals(color)) { // 黑方
			System.out.println("当前点为黑兵");
			if (ca == 1) {
				if (cb != 0) {
					System.out.println("不合乎规则:过河兵");
					return true;
				}
			} else if (ca == 0 && ywza > 5 && Math.abs(cb) == 1) {
				System.out.println("合乎规则:过河兵");
			} else {
				return true;
			}

		}
		System.out.println("合乎规则");
		return false;
	}

}
