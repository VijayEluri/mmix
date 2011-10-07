package eddie.wu.domain;

public class BoardStatistic {
	// 意义不大，不过顺带算出来，形势判断或许有用。
	private byte numberOfWhitePointEaten = 0;

	private byte numberOfBlackPointEaten = 0; // 黑白被吃子计数

	private short giveUpSteps = 0;

	/**
	 * 一般来说,让子棋局会出现弃权.
	 * 
	 * @return
	 */
	public short getGiveUpSteps() {
		return giveUpSteps;
	}

	public void increaseGiveUpSteps() {
		giveUpSteps++;
	}

	/**
	 * @param numberOfBlackPointEaten
	 *            The numberOfBlackPointEaten to set.
	 */
	public void addNumberOfBlackPointEaten(byte tibaizishu) {
		this.numberOfBlackPointEaten += tibaizishu;
	}

	/**
	 * @return Returns the numberOfWhitePointEaten.
	 */
	public byte getNumberOfWhitePointEaten() {
		return numberOfWhitePointEaten;
	}

	/**
	 * @return Returns the numberOfBlackPointEaten.
	 */
	public byte getNumberOfBlackPointEaten() {
		return numberOfBlackPointEaten;
	}

	/**
	 * @param numberOfWhitePointEaten
	 *            The numberOfWhitePointEaten to set.
	 */
	public void addNumberOfWhitePointEaten(byte tiheizishu) {
		this.numberOfWhitePointEaten += tiheizishu;
	}
}
