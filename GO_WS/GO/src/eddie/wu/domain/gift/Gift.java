package eddie.wu.domain.gift;

/**
 * 倒脱靴是一种重要的战术手段。在被吃掉一定数目的棋子之后，峰回路转，吃回对方数字而成活。<br/>
 * 暂且翻译为Gift。意为通过弃子取得胜利，先弃后取。<br/>
 * 因为这过过程中对方总能提子而形成气眼，所以和大眼死活有很大的关系。<br/>
 * 只是这里形成的大眼是不完整的，即眼位是由多块（而不是一块）棋块包围而成。<br/>
 * 观察了一些倒脱靴的例子，发现了一个共同的，形成的眼位都是在边上。<br/>
 * 中央的大眼缺陷的例子也有，但是中央大眼倒脱靴的情况尚未看到。因为即使大眼有缺陷,<br/>
 * 要吃掉其中一块也是困难的。但是通过打吃的先手破坏眼位但是可能的。,<br/>
 * 打二还一也可以看成倒脱靴的一例。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class Gift {
	private int originalBreath = 0;
	private int originalStones = 0;

	public int getOriginalBreath() {
		return originalBreath;
	}

	public void setOriginalBreath(int originalBreath) {
		this.originalBreath = originalBreath;
	}

	public int getOriginalStones() {
		return originalStones;
	}

	public void setOriginalStones(int originalStones) {
		this.originalStones = originalStones;
	}

}
