package eddie.wu.linkedblock;
/**
 * ���ڿ���չ������.������ÿ��� ������Ϣ.
 * @author eddie
 *
 */
public class Controller {
	/**
	 * �����������.
	 */
	private int indexForJuMian=0;
	/**
	 * ��ǰ����˭��?
	 */
	private int whoseTurn=0;
	/**
	 * �ò�����ľ�����
	 */
	private int numberOfJuMian=0;
	/**
	 * ��ǰ����ʱ�ļ�����
	 */
	private int tempBestScore=0;
	
	public int getIndexForJuMian() {
		return indexForJuMian;
	}
	public void setIndexForJuMian(int indexForJuMian) {
		this.indexForJuMian = indexForJuMian;
	}
	public int getNumberOfJuMian() {
		return numberOfJuMian;
	}
	public void setNumberOfJuMian(int numberOfJuMian) {
		this.numberOfJuMian = numberOfJuMian;
	}
	public int getTempBestScore() {
		return tempBestScore;
	}
	public void setTempBestScore(int tempBestScore) {
		this.tempBestScore = tempBestScore;
	}
	public int getWhoseTurn() {
		return whoseTurn;
	}
	public void setWhoseTurn(int whoseTurn) {
		this.whoseTurn = whoseTurn;
	}
	public int getLastIndexForJumian(){
		return this.getIndexForJuMian()+this.numberOfJuMian-1;
	}
}
