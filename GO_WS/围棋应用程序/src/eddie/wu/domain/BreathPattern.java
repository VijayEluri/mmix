package eddie.wu.domain;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * the first difficulty is that some time it is not enough to only consider the
 * breath; sometime we also need to consider the point out of board; e.g. 边上的板六。 <br/>
 * 每个pattern代表一种局部的状态。因此需要存储相应的结果。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class BreathPattern {
	private static final Logger log = Logger.getLogger(BreathPattern.class);
	/**
	 * with more context information. some point maybe not so relevant.
	 */
	byte[][] pattern;
	/**
	 * only include the eye point and other point in eye's shape. use their
	 * original color.
	 */
	byte[][] key;
	/**
	 * 涉及的眼位大小。
	 */
	int eyeSize;
	/**
	 * 成眼块的外气
	 */
	int outerBreath;
	
	

	public BreathPattern(byte[][] key) {

		this.key = key;
	}

	public BreathPattern(byte[][] pattern, byte[][] key) {
		this.pattern = pattern;
		this.key = key;
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

	public void printPattern() {
		for (int i = 0; i < pattern.length; i++) {
			// if(log.isDebugEnabled()) log.debug(Arrays.toString(state[i]));
			char[] state = new char[pattern[i].length];
			for (int j = 0; j < pattern[i].length; j++) {
				if (pattern[i][j] == Constant.BLACK) {
					state[j] = ColorUtil.BLACK_STRING;
				} else if (pattern[i][j] == Constant.WHITE) {
					state[j] = ColorUtil.WHITE_STRING;
				} else if (pattern[i][j] == Constant.BLANK) {
					state[j] = ColorUtil.BLANK_STRING;
				}
			}
			if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.toString(state));
		}
	}

}
