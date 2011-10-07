package eddie.wu.domain.survive;
/**
 * 死活结果的表达。
 * @author wueddie-wym-wrz
 *
 */
public class RelativeSurviveResult {
	/* from the Breath block owner's point of view.*/
	public static final int DIE = 1;
	public static final int LIVE = 2;
	public static final int JIE = 3; //成劫。也许需要进一步的分析，比如缓一气劫，二手劫等等。
	/*mean  that the result is independent of whose turn
	 * 结果是否与谁先下无关。也就是说当前的状态已经确定，没有下子的价值。<br>
	 * 当然死活的角度没有价值，不等于官子角度也没有价值。不等于没有劫材的利用。
	 * 
	 * */
	Boolean independent;//三值逻辑
	/**
	 * 是否下了反而浪费一首棋；包括后手死，杀已经死了的棋。
	 */
	boolean waste;
	RelativeResult xianShou;//先手的结果。
	RelativeResult houShou;//后手的结果。
	public boolean isIndependent() {
		if(independent != null)
		return independent.booleanValue();
		return xianShou.getSurvive() == houShou.getSurvive();
	}
	public void setIndependent(boolean independent) {
		if(this.independent==null){
			this.independent = independent;
		}else{
			throw new RuntimeException("independent is already decided.");
		}
		
	}
	public boolean isWaste() {
		return waste;
	}
	public void setWaste(boolean waste) {
		this.waste = waste;
	}
	public RelativeResult getXianShou() {
		return xianShou;
	}
	public void setXianShou(RelativeResult xianShou) {
		this.xianShou = xianShou;
	}
	public RelativeResult getHouShou() {
		return houShou;
	}
	public void setHouShou(RelativeResult houShou) {
		this.houShou = houShou;
	}	
	
} 
