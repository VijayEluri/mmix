package eddie.wu.domain.analy;

import java.util.HashSet;
import java.util.Set;

import eddie.wu.domain.Point;

/**
 * 针对一个block group（棋块组）的眼位静态评估结果
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

	// duplicate with realEyes;
	private Set<Point> bigEyes;

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
	public void init(){
		Set<Point> realEyes = new HashSet<Point>();
		Set<Point> fakeEyes = new HashSet<Point>();
		Set<Point> halfEyes = new HashSet<Point>();
		Set<Point> bigEyes = new HashSet<Point>();
		setFakeEyes(fakeEyes);
		setHalfEyes(halfEyes);
		setRealEyes(realEyes);
		setBigEyes(bigEyes);
	}

	@Override
	public String toString() {
		return "EyeResult [realEyes=" + realEyes + ", fakeEyes=" + fakeEyes
				+ ", halfEyes=" + halfEyes + ", bigEyes=" + bigEyes + "]";
	}

	public Set<Point> getBigEyes() {
		return bigEyes;
	}

	public void setBigEyes(Set<Point> bigEyes) {
		this.bigEyes = bigEyes;
	}

}
