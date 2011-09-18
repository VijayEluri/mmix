
package line.breaking;

public class Penalty extends Box {
	private boolean flag;//?
	private int penalty ;
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}
	
	public Penalty(){}
	
	public Penalty(int width, boolean flag, int penalty){
		this.setCh('-');
		this.setWidth(width);
		this.flag = flag;
		this.penalty = penalty;
	}

	@Override
	public String toString() {
		return "Penalty [flag=" + flag + ", penalty=" + penalty
				+ ", width=" + getWidth() + "]";
	}
	
}
