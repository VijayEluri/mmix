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
		
		System.out.println("当前点为炮");
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
			System.out.println("竖向移动");
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
			return true; // 处理完毕
		}
//		if (actnew == 0) { // 移动
//			if (count > 0) {
//				return true;
//			}
//			System.out.println("合乎规则");
//			break;
//
//		} else if (qizi[active].color != qizi[actnew].color) {
//			// 提子
//
//			if (count != 1) {
//				return true;
//			}
//			System.out.println("属于提子");
//
//			System.out.println("合乎规则");
//			break;
//		} else if (qizi[active].color == qizi[actnew].color) {
//			// active = zb[m][n];
//			System.out.println("改变当前活跃点。");
//		}
		/*
		 * else { return true; System.out.println("合乎规则"); }
		 */
		
		return false;
	}

}
