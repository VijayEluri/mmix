package line.breaking;

public class Box {
	
	private int width;
	private char ch;

	public char getCh() {
		return ch;
	}

	public void setCh(char ch) {
		this.ch = ch;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public static Box getBox(char ch) {
		Box box = new Box(ch);
		return box;
	}

	public Box() {
	}

	public Box(int width) {
		this.width = width;
	}

	public Box(char ch, int width) {
		this.ch = ch;
		this.width = width;
	}

	public Box(char ch) {
		this.ch = ch;
		this.width = Font.getWidth(ch);
	}

	// public static Box indent = new Box('_',Font.getWidth('m'));
	public static Box indent = new Box('_', 18);

	// static {
	// indent.setWidth();
	//
	// }
	@Override
	public String toString() {
		return "Box [ch=" + ch + ", width=" + width + "]";
	}

}
