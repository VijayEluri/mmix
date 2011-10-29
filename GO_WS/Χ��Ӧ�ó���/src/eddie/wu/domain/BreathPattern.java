package eddie.wu.domain;

import java.util.Arrays;


/**
 * the first difficulty is that some time it is not enough to only consider
 * the breath; sometime we also need to consider the point out of board; e.g.
 * ±ßÉÏµÄ°åÁù¡£
 * @author wueddie-wym-wrz
 *
 */
public class BreathPattern {
	byte[][] pattern;

	public BreathPattern(byte[][] pattern) {
		this.pattern = pattern;
	}

	public static BreathPattern getBreathPattern(Block block, Shape shape) {
		byte[][] pattern;
		pattern = new byte[shape.getDeltaX()][shape.getDeltaY()];
		for (Point point : block.getAllPoints()) {
			pattern[point.getRow() - shape.getMinX()][point.getColumn()
					- shape.getMinY()] = ColorUtil.BREATH;
		}
		return new BreathPattern(pattern);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(pattern);
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
		BreathPattern other = (BreathPattern) obj;
		if (!Arrays.equals(pattern, other.pattern))
			return false;
		return true;
	}

}
