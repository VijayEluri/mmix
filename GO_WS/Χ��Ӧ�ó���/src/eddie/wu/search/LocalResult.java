package eddie.wu.search;


import java.util.ArrayList;
import java.util.List;

public class LocalResult {
	/**
	 * score > 0 when target block succeed. no further investigation necessary.
	 * score < 0 when target block fail. no further investigation necessary.
	 * score = 0 when target block need further investigation.
	 */
	private int score = 0;
	private List<Candidate> candidates;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public List<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}

	public void addCandidate(Candidate candidate) {
		if (candidates == null)
			candidates = new ArrayList<Candidate>();
		candidates.add(candidate);
	}

	public void setCandidate(Candidate candidate) {
		if (candidates == null)
			candidates = new ArrayList<Candidate>();
		else
			candidates.clear();
		candidates.add(candidate);
	}

	public boolean isTargetBlockSucceed() {
		return score > 0;
	}

	public int getNumberOfCandidates() {	
		return this.getCandidates().size();
	}
	
	
}
