package eddie.wu.domain.survive;
/**
 * �������ı�
 * @author wueddie-wym-wrz
 *
 */
public class RelativeSurviveResult {
	/* from the Breath block owner's point of view.*/
	public static final int DIE = 1;
	public static final int LIVE = 2;
	public static final int JIE = 3; //�ɽ١�Ҳ����Ҫ��һ���ķ��������绺һ���٣����ֽٵȵȡ�
	/*mean  that the result is independent of whose turn
	 * ����Ƿ���˭�����޹ء�Ҳ����˵��ǰ��״̬�Ѿ�ȷ����û�����ӵļ�ֵ��<br>
	 * ��Ȼ����ĽǶ�û�м�ֵ�������ڹ��ӽǶ�Ҳû�м�ֵ��������û�нٲĵ����á�
	 * 
	 * */
	Boolean independent;//��ֵ�߼�
	/**
	 * �Ƿ����˷����˷�һ���壻������������ɱ�Ѿ����˵��塣
	 */
	boolean waste;
	RelativeResult xianShou;//���ֵĽ����
	RelativeResult houShou;//���ֵĽ����
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
