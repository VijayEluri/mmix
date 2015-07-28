package eddie.wu.domain.analy;

import java.util.HashSet;
import java.util.Set;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;

/**
 * 应该将胜负最终计算的表示和棋局进行中的形势判断的表示区别开来.<br/>
 * 最明显的一点是前者有贴目.而后者没有.<br/>
 * 终局的结果计算<br/>
 * 贴目放到外面去考虑。
 * 
 * @author Eddie
 * 
 */
public class FinalResult {
	private int black;	//黑子數目
	private int white;	//白子數目
	private int shared;	// 与两方同时相邻

	private int whoWin = -1;
	private int net;//净勝多少

	// accessory information for debugging.
	private Set<Point> blackPoints = new HashSet<Point>();// include its eyes.
	private Set<Point> whitePoints = new HashSet<Point>();
	private Set<Point> sharedPoints = new HashSet<Point>();

	public FinalResult() {
	}

	public FinalResult(int black, int white, int shared) {
		this.black = black;
		this.white = white;
		this.shared = shared;
		initWhoWin();
	}

	void initWhoWin() {
		net = black - white;
		if (net > 0) {
			whoWin = Constant.BLACK;
		} else if (net < 0) {
			whoWin = Constant.WHITE;
			net = 0 - net;
		} else {
			// 无胜负或者平局。
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

	public int getWhoWin() {
		initWhoWin();
		return whoWin;
	}

	public int getNet() {
		return net;
	}

	public int getScore() {
		return black - white;
	}

	@Override
	public String toString() {
		String whoWinString;
		if (whoWin == -1)
			initWhoWin();
		if (whoWin == Constant.BLACK)
			whoWinString = "Black";
		else if (whoWin == Constant.WHITE)
			whoWinString = "White";
		else
			whoWinString = "Tie";
		return "FinalResult [black=" + black + ", white=" + white + ", shared="
				+ shared + ", score=" + this.getScore() + " whoWin="
				+ whoWinString + ", net=" + net + "]";
	}

	public Set<Point> getBlackPoints() {
		return blackPoints;
	}

	public Set<Point> getWhitePoints() {
		return whitePoints;
	}

	public Set<Point> getSharedPoints() {
		return sharedPoints;
	}

}