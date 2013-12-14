package eddie.wu.search.global;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Step;

/**
 * different with the one in package search. no refer to GoBoard
 * 
 * @author Eddie
 * 
 */
public class Candidate {

	private boolean eatingDead;
	/**
	 * if only one eye, then eyePoint means eye size.
	 */
	private int countEyePoint;
	/**
	 * 该步强行提回劫。
	 */
	private boolean loopSuperior;
	/**
	 * the candidate step
	 */
	private Step step;
	/**
	 * 在劫争计算中弃权一手。
	 */
	private boolean skip;

	/**
	 * all the other attribute which impact its priority to be searched.
	 */
	private int breaths;

	// private boolean increaseBreath;

	private int increasedBreath;

	/**
	 * 是否提子
	 */
	private boolean eating;

	/**
	 * 是否打吃对方
	 */
	private boolean capturing;

	/**
	 * 同样是提子,是否同时解除对方的打吃.
	 */
	private boolean removeCapturing;

	/**
	 * 是否被对方打吃
	 */
	private boolean gifting;

	/**
	 * 形成的虎口数目,死活相关
	 */
	private int tigerMouths;

	private int eyes;

	public Step getStep() {
		step.setLoopSuperior(loopSuperior);
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}

	public int getBreaths() {
		return breaths;
	}

	public void setBreaths(int breaths) {
		this.breaths = breaths;

	}

	public boolean isEating() {
		return eating;
	}

	public void setEating(boolean eating) {
		this.eating = eating;
	}

	public int getTigerMouths() {
		return tigerMouths;
	}

	public void setTigerMouths(int tigerMouths) {
		this.tigerMouths = tigerMouths;
	}

	public int getEyes() {
		return eyes;
	}

	public void setEyes(int eyes) {
		this.eyes = eyes;
	}

	@Override
	public String toString() {
		return Constant.lineSeparator
				+ "Candidate [step="
				+ (step.getPoint() == null ? "PAS" : step.getPoint().toString())
				+ ", color=" + step.getColorString() + ", breaths=" + breaths
				+ ", increasedBreath=" + increasedBreath + ", eatingDead = "
				+ eatingDead + ",\r\n\t" + "eating=" + eating + ", capturing="
				+ capturing + ", removeCapturing=" + removeCapturing
				+ ", gifting=" + gifting + ", tigerMouths=" + tigerMouths
				+ ", becomeLive=" + becomeLive + ", eyes=" + eyes
				+ ", eyeSizecount=" + countEyePoint + "]";
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	/**
	 * 允许直接回提。validate和oneStepForward都要相应处理。
	 */
	public boolean isLoopSuperior() {
		return loopSuperior;
	}

	public void setLoopSuperior(boolean loopSuperior) {
		this.loopSuperior = loopSuperior;
	}

	public boolean isCapturing() {
		return capturing;
	}

	public void setCapturing(boolean capturing) {
		this.capturing = capturing;
	}

	public boolean isGifting() {
		return gifting;
	}

	public void setGifting(boolean gifting) {
		this.gifting = gifting;
	}

	public int getCountEyePoint() {
		return countEyePoint;
	}

	public void setCountEyePoint(int countEyePoint) {
		this.countEyePoint = countEyePoint;
	}

	public boolean isRemoveCapturing() {
		return removeCapturing;
	}

	public void setRemoveCapturing(boolean removeCapturing) {
		this.removeCapturing = removeCapturing;
	}

	public int getIncreasedBreath() {
		return increasedBreath;
	}

	public void setIncreasedBreath(int increasedBreath) {
		this.increasedBreath = increasedBreath;
	}

	private boolean becomeLive;

	public void setBecomeLive(boolean b) {
		becomeLive = b;

	}

	public boolean isBecomeLive() {
		return becomeLive;

	}

	public boolean isEatingDead() {
		return eatingDead;
	}

	public void setEatingDead(boolean eatingDead) {
		this.eatingDead = eatingDead;
	}

}
