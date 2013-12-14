package eddie.wu.manual;

import eddie.wu.domain.SymmetryResult;

public class StepSymmetry {
	/**
	 * the step implicit decide a board state.
	 */
	private int stepIndex;
	
	private SymmetryResult symmetry;
	
	public StepSymmetry(int stepIndex,SymmetryResult symmetry){
		this.stepIndex = stepIndex;
		this.symmetry = symmetry;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
	}

	public SymmetryResult getSymmetry() {
		return symmetry;
	}

	public void setSymmetry(SymmetryResult symmetry) {
		this.symmetry = symmetry;
	}
	
	
}
