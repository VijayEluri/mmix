package eddie.wu.domain;

public class BoardStatistic {
	// ���岻�󣬲���˳��������������жϻ������á�
	private byte numberOfWhitePointEaten = 0;

	private byte numberOfBlackPointEaten = 0; // �ڰױ����Ӽ���

	private short giveUpSteps = 0;

	/**
	 * һ����˵,������ֻ������Ȩ.
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
