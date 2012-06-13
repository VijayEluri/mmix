package eddie.wu.search.global;

import eddie.wu.domain.Step;

/**
 * different with the one in package search. no refer to GoBoard
 * 
 * @author Eddie
 * 
 */
public class Candidate {
	/**
	 * the candidate step
	 */
	private Step step;

	/**
	 * all the other attribute which impact its priority to be searched.
	 */
	private int breaths;
	
	/**
	 * 是否提子
	 */
	private boolean eating;

	/**
	 * 形成的虎口数目,死活相关
	 */
	private int tigerMouths;
	
	private int eyes;

	public Step getStep() {
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
	
	
}
