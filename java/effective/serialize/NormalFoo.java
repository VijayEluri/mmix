package effective.serialize;

import java.io.Serializable;

public class NormalFoo implements Serializable{
	private int x, y; // Our state
	
	public NormalFoo(int x,int y){
		this.x=x;
		this.y=y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
