package eddie.wu.chess.domain;

public class King extends Chessman {
	static byte[][] jiangshuai = new byte[12][11];
	static {
		jiangshuai[1][4] = 1; // 将帅的有效点。
		jiangshuai[1][5] = 1;
		jiangshuai[1][6] = 1;
		jiangshuai[2][4] = 1;
		jiangshuai[2][5] = 1;
		jiangshuai[2][6] = 1;
		jiangshuai[3][4] = 1;
		jiangshuai[3][5] = 1;
		jiangshuai[3][6] = 1;

		jiangshuai[10][4] = 1;
		jiangshuai[10][5] = 1;
		jiangshuai[10][6] = 1;
		jiangshuai[9][4] = 1;
		jiangshuai[9][5] = 1;
		jiangshuai[9][6] = 1;
		jiangshuai[8][4] = 1;
		jiangshuai[8][5] = 1;
		jiangshuai[8][6] = 1; // 共18点。
	}
	
	public King(ChessColor color,int a, int b){
		super(color,a,b);
	}

	@Override
	public boolean isValidStep(ChessBoard board, int m, int n) {
		byte ca = (byte) (m - a);
		byte cb = (byte) (n - b);
		byte xun;
		byte ywzb = b;
		byte ywza = a;

		System.out.println("当前点为帅");
		if (ca == 0 && Math.abs(cb) == 1 || cb == 0 && Math.abs(ca) == 1) {
			if (jiangshuai[m][n] == 1) {
				return true;
			} else {
				return false;

			}
		} else {
			return false;
		}
		//System.out.println("合乎规则");
		//return false;
	}

}
