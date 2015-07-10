package eddie.wu.domain.survive;

import java.util.Arrays;

import eddie.wu.domain.Constant;

/**
 * the first difficulty is that some time it is not enough to only consider the
 * breath;<br/>
 * sometime we also need to consider the point out of board; e.g. 边上的板六。<br/>
 * 存储一个状态的结果是不够的，要将一个变化过程中的所有状态都存储，才能真正应用。<br/>
 * 比如直四是活棋，如果对方点入，如何应对呢？<br/>
 * 如果所有的状态都要存储，是否应该将整个变化都存储起来，同时提供所有状态的查询。<br/>
 * 如此则需要提供录入变化的手段并存储。这样比较繁琐，而用处有限，这也是我之前想<br/>
 * 通过搜索来实现的原因。<br/>
 * 折中的方案：仅存储局面的要点，后面的变化要靠搜索计算。<br/>
 * 关于pattern的表示，决定仅仅存储气点，做活方用black表示。杀棋方用white表示。<br/>
 * 假设做活方已经连成一块，如果实际情形有偏离，则做活的结论需要适当减弱。<br/>
 * 假设做活方没有外气，入实际有外气，做活结论可能需要适当加强。<br/>
 * 角部曲四，板六需要存储为特殊的形状，即加上棋盘外的一条线 来表示。<br/>
 * 变化可以存储更多的信息，也许应该人工输入正解及其变化，内部转化成状态和着手的映射。<br/>
 * 
 * 2012/06/08 仅仅考虑完整的大眼，外气没法表达，是一个缺陷，暂且不管。<br/>
 * 眼位所在Shape都要覆盖。外加一圈成眼的棋块，但不必包括棋块中所有的子。<br/>
 * 以曲四为例，不在角上时。<br/>
 * _X_ <br/>
 * X XX_<br/>
 * X X<br/>
 * _XXX_<br/>
 * 有四种变体。 仅有一步未完成的变化。 [3,2] --> [3,3] [3,3] --> [3.2] [2,2] --> [3,4] -->
 * 脱先是正解，但是下一步对方找劫材，如何应对没有存储。 在边上（只有一边是底线）<br/>
 * X_ <br/>
 * XX_<br/>
 * X<br/>
 * XXX_<br/>
 * [3,1] --> [3,2] [3,2] --> [3.1] [2,1] --> [3,3] -->
 * 
 * 在角上（两边或者两边以上在底线）<br/>
 * X_ <br/>
 * XX_<br/>
 * X<br/>
 * 盘角曲四，打劫。（与外气有关，但是常见情形是先紧了外气，再破双活，故与外气无关。）<br/>
 * 没有外气是紧劫，一口外气是缓一气劫，两口外气则活棋。 索引方式： 黑眼/白眼 size大小 shape形状 [3,2] -->
 * [3,1]-->[2,1]-->[3,1] 劫 [2,1] --> [3,1] [3,1] --> [3,2] [3,3]-->
 * 
 * @author wueddie-wym-wrz
 */
public class BreathPattern {
	byte[][] pattern;
	boolean corner;

	public BreathPattern(byte[][] pattern) {
		this.pattern = pattern;
		this.corner = false;
	}

	public BreathPattern(byte[][] pattern, boolean corner) {
		this.pattern = pattern;
		this.corner = corner;
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

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(Constant.lineSeparator);
		for (int i = 0; i < pattern.length; i++) {
			result.append(Arrays.toString(pattern[i]));
			result.append(Constant.lineSeparator);
		}

		result.append(Constant.lineSeparator);
		return result.toString();
	}

}
