package taocp.v3.sort;

public class Metrics {
	int compares;

	int swaps;

	public void reset() {
		compares = 0;
		swaps = 0;
	}

	public int getCompares() {
		return compares;
	}

	public void setCompares(int compares) {
		this.compares = compares;
	}

	public int getSwaps() {
		return swaps;
	}

	public void setSwaps(int swaps) {
		this.swaps = swaps;
	}

	public String toString() {
		return "compares=" + compares + ", swaps=" + swaps;
	}
	public void incrementCompares(){
		compares+=1;
	}
	public void add(Metrics m){
		this.compares+=m.getCompares();
		this.swaps+=m.getSwaps();
	}
}
