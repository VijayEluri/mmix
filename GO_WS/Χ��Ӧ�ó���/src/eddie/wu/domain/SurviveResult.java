package eddie.wu.domain;
/**
 * �������ı�
 * @author wueddie-wym-wrz
 *
 */
public class SurviveResult {
	/* from the Breath block owner's point of view.*/
	public static final int DIE = 1;
	public static final int LIVE = 2;
	public static final int JIE = 3; //�ɽ١�Ҳ����Ҫ��һ���ķ��������绺һ���٣����ֽٵȵȡ�
	/*mean  that the result is independent of whose turn
	 * ����Ƿ���˭�����޹ء�
	 * */
	boolean independent;
	/**
	 * �Ƿ����˷����˷�һ���壻������������ɱ�Ѿ����˵��塣
	 */
	boolean waste;
	Result xianShou;//���ֵĽ����
	Result houShou;//���ֵĽ����
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
