package go;

import eddie.wu.domain.Constant;

public class FinalResult {
	int black;
	int white;
	int shared;
	int tiemu = 8;// ÌùÄ¿
	int whoWin;
	int net;

	public FinalResult(int black, int white, int shared) {
		this.black = black;
		this.white = white;
		this.shared = shared;
		net = black - white - tiemu;
		if (net > 0) {
			whoWin = Constant.BLACK;
		} else if (net < 0) {
			whoWin = Constant.WHITE;
			net = 0 - net;
		} else {
			// ÎÞÊ¤¸º
			whoWin = Constant.BLANK;
		}
	}

	public int getBlack() {
		return black;
	}

	public void setBlack(int black) {
		this.black = black;
	}

	public int getWhite() {
		return white;
	}

	public void setWhite(int white) {
		this.white = white;
	}

	public int getShared() {
		return shared;
	}

	public void setShared(int shared) {
		this.shared = shared;
	}

	public int getTiemu() {
		return tiemu;
	}

	public void setTiemu(int tiemu) {
		this.tiemu = tiemu;
	}

	public int getWhoWin() {
		return whoWin;
	}

	public int getNet() {
		return net;
	}

	@Override
	public String toString() {
		String whoWinString;
		if (whoWin == Constant.BLACK)
			whoWinString = "Black";
		else if (whoWin == Constant.WHITE)
			whoWinString = "White";
		else
			whoWinString = "Tie";
		return "FinalResult [black=" + black + ", white=" + white + ", shared="
				+ shared + ", tiemu=" + tiemu + ", whoWin=" + whoWinString
				+ ", net=" + net + "]";
	}

}