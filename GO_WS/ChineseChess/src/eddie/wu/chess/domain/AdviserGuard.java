package eddie.wu.chess.domain;

public class AdviserGuard extends Chessman {
	static byte[][] shixiang = new byte[12][11]; // 辅助规则实现。
	// 用于辅助实现规则。
	static {
		shixiang[1][4] = 1; // 士的有效点。
		shixiang[1][6] = 1;
		shixiang[3][4] = 1;
		shixiang[3][6] = 1;
		shixiang[2][5] = 4;
		
		shixiang[10][4] = 3;
		shixiang[10][6] = 3;
		shixiang[8][4] = 3;
		shixiang[8][6] = 3;
		shixiang[9][5] = 4;

		
	}
	
	public AdviserGuard(ChessColor color,int a, int b){
		super(color,a,b);
	}

	@Override
	public boolean isValidStep(ChessBoard board, int m, int n) {
		byte ca = (byte) (m - a);
		byte cb = (byte) (n - b);
		byte xun;
		byte ywzb = b;
		byte ywza = a;

		System.out.println("当前点为士");
		if (Math.abs(ca) != 1 || Math.abs(cb) != 1) {
			return false;
		} else if (shixiang[m][n] == 0) {
			return false;
		}

		return true;
	}

}
