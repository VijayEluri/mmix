package eddie.wu.domain;

public class SymmetryResult {
	private boolean horizontalSymmetry;
	private boolean verticalSymmetry;
	private boolean forwardSlashSymmetry;
	private boolean backwardSlashSymmetry;

	// private int numberOfSymmetry;

	public boolean isHorizontalSymmetry() {
		return horizontalSymmetry;
	}

	public void setHorizontalSymmetry(boolean horizontalSymmetry) {
		this.horizontalSymmetry = horizontalSymmetry;
	}

	public boolean isVerticalSymmetry() {
		return verticalSymmetry;
	}

	public void setVerticalSymmetry(boolean verticalSymmetry) {
		this.verticalSymmetry = verticalSymmetry;
	}

	public boolean isForwardSlashSymmetry() {
		return forwardSlashSymmetry;
	}

	public void setForwardSlashSymmetry(boolean forwardSlashSymmetry) {
		this.forwardSlashSymmetry = forwardSlashSymmetry;
	}

	public boolean isBackwardSlashSymmetry() {
		return backwardSlashSymmetry;
	}

	public void setBackwardSlashSymmetry(boolean backwardSlashSymmetry) {
		this.backwardSlashSymmetry = backwardSlashSymmetry;
	}

	public int getNumberOfSymmetry() {
		int numberOfSymmetry = 0;
		numberOfSymmetry += this.horizontalSymmetry ? 1 : 0;
		numberOfSymmetry += this.verticalSymmetry ? 1 : 0;
		numberOfSymmetry += this.forwardSlashSymmetry ? 1 : 0;
		numberOfSymmetry += this.backwardSlashSymmetry ? 1 : 0;
		return numberOfSymmetry;
	}

//	public void setNumberOfSymmetry(int numberOfSymmetry) {
//		this.numberOfSymmetry = numberOfSymmetry;
//	}

}
