package eddie.wu.domain;

import java.util.List;

import org.apache.log4j.Logger;

import eddie.wu.manual.SGFGoManual;

/**
 * Better way to express the step. replace of the row,column 表达一步棋的下法。<br/>
 * How to present the step given up? though it is not common in reality, but it
 * may happen in the very end of the game, current idea is to let point==null.<br/>
 * it has same data structure as BoardPoint but the meaning is different.
 * 
 * synonym: Move <br/>
 * Chinese: 步
 * 
 * @author eddie
 * 
 */
public class Step {// implements Step{
	public static Step absLoopThreat = new Step("绝对劫材－找劫材");
	public static Step absLoopThreatAnswer = new Step("绝对劫材－应劫材");
	public static Step relLoopThreat = new Step("相对劫材－找劫材");
	public static Step relLoopThreatCont = new Step("相对劫材－利用劫材");

	static public int outputOverAllStatistic(int[] times, int ms, Logger log) {
		int[] stat = new int[100];
		long total = 0;
		for (int i = 0; i < times.length; i++) {
			stat[times[i] / (1000000 * ms)] += 1;
			total += times[i];
		}
		int count = 0;

		for (int i = 0; i < 100; i++) {
			count += stat[i];
			log.warn("less than " + (i + 1) * ms + " ms count: " + stat[i]);
			if (count == times.length)
				break;
		}
		log.warn("average time " + (total / count / 1000)
				+ " milli-second per manual");
		return (int) (total / count / 1000);
	}

	static public int outputStatistic(List<Step> steps, int ms, Logger log) {
		int[] times = new int[steps.size()];
		int i = 0;
		for (Step step : steps) {
			times[i++] = (int) step.getTime();
		}
		return outputOverAllStatistic(times, ms, log);
	}

	/**
	 * start from 1;
	 */
	private int index;
	private Point point;
	private byte color;
	private String name;

	/**
	 * Auxiliary attribute
	 */
	private boolean loopSuperior;

	private long time;

	public Step(int i, int j, int k, byte black) {
		this.point = Point.getPoint(i, j, k);
		this.color = black;
	}

	public Step(Point point, int color) {
		this.point = point;
		this.color = (byte) color;
	}

	public Step(Point point, int color, int index) {
		this.point = point;
		this.color = (byte) color;
		this.index = index;
	}

	public Step(String name) {
		this.name = name;
	}

	public void backwardSlashMirror() {
		this.point = this.point.backwardSlashMirror();
	}

	public void normalize() {
		this.point = this.point.normalize();
	}

	public void normalize(SymmetryResult operation) {
		this.point = point.normalize(operation);
	}

	public void convert(SymmetryResult operation) {
		if (point == null)
			return;
		System.out.println(operation.getNumberOfSymmetry());
		if (operation.getNumberOfSymmetry() == 0)
			return;
		if (operation.getNumberOfSymmetry() == 4) {
			throw new RuntimeException("operation.getNumberOfSymmetry() = "
					+ operation.getNumberOfSymmetry());
		}

		if (operation.isBackwardSlashSymmetry()) {
			backwardSlashMirror();
		}
		if (operation.isForwardSlashSymmetry()) {
			forwardSlashMirror();

		}
		if (operation.isHorizontalSymmetry()) {
			horizontalMirror();
		}
		if (operation.isVerticalSymmetry()) {
			verticalMirror();
		}
	}

	// public void setColor(int color) {
	// this.color = (byte) color;
	// }

	public void forwardSlashMirror() {
		this.point = this.point.forwardSlashMirror();
	}

	// public void setPoint(Point point) {
	// this.point = point;
	// }

	public int getColor() {
		return color;
	}

	public int getEnemyColor() {
		return ColorUtil.enemyColor(color);
	}

	public String getColorString() {
		return ColorUtil.getColorText(color);

	}

	public byte getColumn() {
		return point.getColumn();
	}

	/**
	 * this field is not necessary during searching
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	public Point getPoint() {
		return point;
	}

	public byte getRow() {

		return point.getRow();
	}

	public long getTime() {
		return time;
	}

	public void horizontalMirror() {
		this.point = this.point.horizontalMirror();
	}

	public boolean isBlack() {

		return color == Constant.BLACK;
	}

	public boolean isPass() {
		return point == null;
	}

	public boolean isLoopSuperior() {
		return loopSuperior;
	}

	public boolean isWhite() {

		return color == Constant.WHITE;
	}

	public void setColor(byte color) {
		this.color = color;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setLoopSuperior(boolean loopSuperior) {
		this.loopSuperior = loopSuperior;
	}

	public void setTime(long l) {
		this.time = l;

	}

	// public boolean isBlack(){
	// return this.color==Constant.BLACK;
	// }

	@Override
	public String toString() {
		return "Step [point="
				+ (point == null ? Constant.PASS : point.toString())
				+ ", color=" + ColorUtil.getColorText(color) + ", index="
				+ index + ", loopSuperior= " + loopSuperior + ", name=" + name
				+ "]";
	}

	private static final String[] CHAR = new String[] {//
	"a", "b", "c", "d", "e", "f", "g",//
			"h", "i", "j", "k", "l", "m", "n",//
			"o", "p", "q", "r", "s", "t",//
			"u", "v", "w", "x", "y", "z"

	};

	/**
	 * ;W[oc]VW[aa:sk]FG[257:Dia. 2]MN[1];B[md];W[mc];B[ld]N[Diagram 2]<br/>
	 * 放弃一手：放弃一手用”[]”表示，也可以用”[tt]”代替（只可用于小于19×19的棋盘），<br/>
	 * 就是说应用软件应该能够处理这两种表示方式。保留”[tt]”的表示方式是为了兼容FF[3]。
	 * 
	 * @return
	 */
	public String toSGFString() {
		StringBuilder sb = new StringBuilder();
		sb.append(";");
		if (this.isBlack()) {
			sb.append(SGFGoManual.BLACK);
		} else {
			sb.append(SGFGoManual.WHITE);
		}
		sb.append("[");
		if (point != null) {
			sb.append(CHAR[point.getColumn() - 1]);
			sb.append(CHAR[point.getRow() - 1]);
		} else {
			// get [tt] for pass in 19*19
			// sb.append(CHAR[point.boardSize]);
			// sb.append(CHAR[point.boardSize]);
		}
		sb.append("]");
		return sb.toString();
	}

	public String toNonSGFString() {
		StringBuilder sb = new StringBuilder();
		if (this.isBlack()) {
			sb.append(SGFGoManual.BLACK);
		} else {
			sb.append(SGFGoManual.WHITE);
		}
		sb.append("[");
		if (point != null) {
			sb.append(point.getRow());
			sb.append(",");
			sb.append(point.getColumn());

		} else {
			sb.append("PAS");
		}
		sb.append("]");
		return sb.toString();
	}

	public void verticalMirror() {
		this.point = this.point.verticalMirror();
	}

	public void revertColor() {
		color = (byte) ColorUtil.enemyColor(color);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + color;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Step other = (Step) obj;
		if (color != other.color)
			return false;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}

	public Step getCopy() {
		return new Step(point, color);
	}

	public void switchColor() {
		this.color = (byte) ColorUtil.enemyColor(color);

	}

	// public void setPoint(Point mirror) {
	// // TODO Auto-generated method stub
	//
	// }

	public static int getPasses(List<Step> list) {
		int count = 0;
		for (Step step : list) {
			if (step.isPass()) {
				count++;
			}
		}
		System.out.print(Step.getString(list));
		System.out.println("---" + count);
		return count;
	}

	public static String getString(List<Step> list) {
		StringBuilder sb = new StringBuilder();
		for (Step step : list) {
			sb.append(step.toNonSGFString() + "-->");
		}
		return sb.toString();
	}

	public static String getString(List<Step> list, String score) {
		StringBuilder sb = new StringBuilder();
		sb.append("[INIT]");
		for (Step step : list) {
			if (step.isBlack()) {
				sb.append("B-->");
			} else {
				sb.append("W-->");
			}
			if (step.getPoint() == null) {
				sb.append("[PAS]");
			} else {
				sb.append(step.getPoint());
			}
		}
		sb.append("(" + score + ")");
		return sb.toString();
	}
}
