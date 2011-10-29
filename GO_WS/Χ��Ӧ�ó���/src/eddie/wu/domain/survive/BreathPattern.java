package eddie.wu.domain.survive;

import java.util.Arrays;

import eddie.wu.domain.Block;
import eddie.wu.domain.Point;
import eddie.wu.domain.Shape;
import eddie.wu.linkedblock.ColorUtil;

/**
 * the first difficulty is that some time it is not enough to only consider the
 * breath; sometime we also need to consider the point out of board; e.g. ���ϵİ�����
 * 
 * @author wueddie-wym-wrz �洢һ��״̬�Ľ���ǲ����ģ�Ҫ��һ���仯�����е�����״̬���洢����������Ӧ�á�
 *         ����ֱ���ǻ��壬����Է����룬���Ӧ���أ�<br/>
 *         ������е�״̬��Ҫ�洢���Ƿ�Ӧ�ý������仯���洢������ͬʱ�ṩ����״̬�Ĳ�ѯ��<br/>
 *         �������Ҫ�ṩ¼��仯���ֶβ��洢�������ȽϷ��������ô����ޣ���Ҳ����֮ǰ��<br/>
 *         ͨ��������ʵ�ֵ�ԭ��<br/>
 *         ���еķ��������洢�����Ҫ�㣬����ı仯Ҫ���������㡣<br/>
 *         ����pattern�ı�ʾ�����������洢���㣬�����black��ʾ��ɱ�巽��white��ʾ��<br/>
 *         ��������Ѿ�����һ�飬���ʵ��������ƫ�룬������Ľ�����Ҫ�ʵ�������</br/>
 *         �������û����������ʵ����������������ۿ�����Ҫ�ʵ���ǿ��<br/>
 *         �ǲ����ģ�������Ҫ�洢Ϊ�������״���������������һ���� ����ʾ��<br/>
 *         �仯���Դ洢�������Ϣ��Ҳ��Ӧ���˹��������⼰��仯���ڲ�ת����״̬�����ֵ�ӳ�䡣<br/>
 * 
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

	public static BreathPattern getBreathPattern(Block block) {
		Shape shape = block.getShape();
		byte[][] pattern;
		pattern = new byte[shape.getDeltaX()][shape.getDeltaY()];
		// for (Point point : block.getAllPoints()) {
		// pattern[point.getRow() - shape.getMinX()][point.getColumn()
		// - shape.getMinY()] = ColorUtil.BREATH;
		// }
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
