package challenge;


public class Position {
	int row;

	int column;

	public Position(int i, int j) {
		row = i;
		column = j;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return row + "/" + column;
	}

	public void increment() {
		row += 1;
		column -= 1;
	}
}
