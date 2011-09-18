package eddie.wu.domain;
/**
 * 死活结果的表达。
 * @author wueddie-wym-wrz
 *
 */
public class SurviveResult {
	/* from the Breath block owner's point of view.*/
	public static final int DIE = 1;
	public static final int LIVE = 2;
	public static final int JIE = 3; //成劫。也许需要进一步的分析，比如缓一气劫，二手劫等等。
	/*mean  that the result is independent of whose turn
	 * 结果是否与谁先下无关。
	 * */
	boolean independent;
	/**
	 * 是否下了反而浪费一首棋；包括后手死，杀已经死了的棋。
	 */
	boolean waste;
	Result xianShou;//先手的结果。
	Result houShou;//后手的结果。
	public boolean isIndependent() {
		return independent;
	}
	public void setIndependent(boolean independent) {
		this.independent = independent;
	}
	public boolean isWaste() {
		return waste;
	}
	public void setWaste(boolean waste) {
		this.waste = waste;
	}
	public Result getXianShou() {
		return xianShou;
	}
	public void setXianShou(Result xianShou) {
		this.xianShou = xianShou;
	}
	public Result getHouShou() {
		return houShou;
	}
	public void setHouShou(Result houShou) {
		this.houShou = houShou;
	}	
	
} 
