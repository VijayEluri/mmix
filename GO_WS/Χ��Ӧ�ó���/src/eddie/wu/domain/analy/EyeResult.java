package eddie.wu.domain.analy;

import java.util.Set;

import eddie.wu.domain.Point;

/**
 * 针对一个group（棋块组）的眼位静态评估结果
 * 
 * @author Eddie
 * 
 */
public class EyeResult {
	private Set<Point> realEyes;
	private Set<Point> fakeEyes;
	/**
	 * 处理接不归可能有用
	 */
	private Set<Point> halfEyes;

	public Set<Point> getRealEyes() {
		return realEyes;
	}

	public void setRealEyes(Set<Point> realEyes) {
		this.realEyes = realEyes;
	}

	public Set<Point> getFakeEyes() {
		return fakeEyes;
	}

	public void setFakeEyes(Set<Point> fakeEyes) {
		this.fakeEyes = fakeEyes;
	}

	public Set<Point> getHalfEyes() {
		return halfEyes;
	}

	public void setHalfEyes(Set<Point> halfEyes) {
		this.halfEyes = halfEyes;
	}

}
