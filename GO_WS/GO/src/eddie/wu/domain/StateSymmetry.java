package eddie.wu.domain;

public class StateSymmetry {

	private BoardColorState state;
	private SymmetryResult symmetry;

	public StateSymmetry(BoardColorState state, SymmetryResult symmetry) {
		this.state = state;
		this.symmetry = symmetry;
	}

	public BoardColorState getState() {
		return state;
	}

	public SymmetryResult getSymmetry() {
		return symmetry;
	}

	public void setSymmetry(SymmetryResult symmetry) {
		this.symmetry = symmetry;
	}

}
