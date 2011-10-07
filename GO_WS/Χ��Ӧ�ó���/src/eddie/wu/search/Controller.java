package eddie.wu.search;

/**
 * ���ڿ���չ������.������ÿ��� ������Ϣ. ÿһ������ж�����棨״̬�� (a simple data object to store
 * miscellaneous control information)
 * 
 * @author Edward
 * 
 */
public class Controller {
	/**
	 * ��ǰ����˭��?
	 */
	private int whoseTurn = 0;
	/**
	 * �����������.�ò�ľ����������ʼ��
	 */
	private int indexForJuMian = 0;

	/**
	 * �ò�����ľ�����
	 */
	private int numberOfJuMian = 0;
	/**
	 * ��ǰ����ʱ�ļ�����
	 */
	private int tempBestScore = 0;

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

	public void decreaseJuMian() {
		this.numberOfJuMian--;
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

	public int getLastIndexForJumian() {
		return this.getIndexForJuMian() + this.numberOfJuMian - 1;
	}
}
