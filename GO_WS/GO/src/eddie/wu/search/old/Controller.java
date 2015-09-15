package eddie.wu.search.old;

/**
 * 用于控制展开的类.定义了每层的 控制信息. 每一层可能有多个局面（状态）,<br/>
 * (a simple data object to store miscellaneous control information)
 * @deprecated
 * @author Eddie
 * 
 */
public class Controller {
	/**
	 * 当前层轮谁走?
	 */
	private int whoseTurn = 0;
	/**
	 * 局面的索引号.该层的局面从这个编号开始(inclusive)。
	 */
	private int indexForJuMian = 0;

	/**
	 * 该层包含的局面数
	 */
	private int numberOfJuMian = 0;
	/**
	 * 当前层临时的计算结果
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
