package eddie.wu.chess.domain;

public class Chariot extends Chessman {
	public Chariot(ChessColor color,int a, int b){
		super(color,a,b);
	}
	@Override
	public boolean isValidStep(ChessBoard board, int m, int n) {
		byte ca = (byte) (m - a);
		byte cb = (byte) (n - b);
		byte xun;
		byte ywzb = b;
		byte ywza = a;
		if (ca == 0) {
			if (cb == 0) {
				board.message = ErrorMessage.DUPLICATE;
				return false;
			} else if (cb > 0) {
				for (xun = (byte) (ywzb + 1); xun < n; xun++) {
					if (board.men[m][xun] != null) {
						board.message = ErrorMessage.OBSTACLE;
						return false;
					}
				}
			} else if (cb < 0) {
				for (xun = (byte) (ywzb - 1); xun > n; xun--) {
					if (board.men[m][xun] != null) {
						board.message = ErrorMessage.OBSTACLE;
						return false;
					}
				}

			}
			System.out.println("合乎规则");
			// 合乎规则；
			return true;
		} else if (cb == 0) {
			if (ca > 0) {
				for (xun = ywza++; xun < m; xun++) {
					if (board.men[xun][n] != null) {
						board.message = ErrorMessage.OBSTACLE;
						return false;
					}
				}
			} else if (ca < 0) {
				for (xun = ywza--; xun > m; xun--) {
					if (board.men[xun][n] != null) {
						board.message = ErrorMessage.OBSTACLE;
						return false;
					}
				}

			}
			System.out.println("合乎规则");
			return true;
		} else if(board.men[m][n]!=null && board.men[m][n].color.equals(this.color)){
			return false;
		}
		else{
			return true; // 处理完毕
		}

	}

}
