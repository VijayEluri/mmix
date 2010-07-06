package eddie.wu.chess.domain;

public enum ErrorMessage {
	EMPTY_START("所选起点为空白点"),
	NOT_THE_TURN("当前不论该方走"),
	DUPLICATE("没有有效移动，目标点和起点相同。"),
	CONFLICT("目标点为己方棋子"),
	OBSTACLE("移动路径上有子阻隔！"),;
	private String message;
	ErrorMessage(String message){
		this.message = message;
	}
}
