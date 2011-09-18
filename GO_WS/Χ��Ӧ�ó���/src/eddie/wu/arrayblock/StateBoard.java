package eddie.wu.arrayblock;

public class StateBoard {
	
	public byte[][]state = new byte[21][21]; // 0.4k;
	public void play(byte row, byte column,byte color){
		state[row][column]=color;
	}

}
