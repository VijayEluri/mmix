package eddie.wu.chess.domain;

public abstract class Chessman {
	public ChessColor getColor() {
		return color;
	}

	public void setColor(ChessColor color) {
		this.color = color;
	}

	public byte getStartA() {
		return startA;
	}

	public void setStartA(byte startA) {
		this.startA = startA;
	}

	public byte getStartB() {
		return startB;
	}

	public void setStartB(byte startB) {
		this.startB = startB;
	}

	public byte getA() {
		return a;
	}

	public void setA(byte a) {
		this.a = a;
	}

	public byte getB() {
		return b;
	}

	public void setB(byte b) {
		this.b = b;
	}

	byte bianhao = 0; // ���ӱ��1-32
	ChessColor color;// = ChessColor.RED; // ����

	protected byte startA;
	protected byte startB;

	byte a = 0; // ��ǰλ�ã�1��ʼ��
	byte b = 0; // ��ʼλ��

	public Chessman(ChessColor color, int a, int b) {
		this.color = color;
		startA = (byte)a;
		startB = (byte)b;
	}

	public abstract boolean isValidStep(ChessBoard board, int m, int n) ;//{
//		throw new RuntimeException("Chessman.isValidStep()"
//				+ " should be override by sub-class");
	//}
}
