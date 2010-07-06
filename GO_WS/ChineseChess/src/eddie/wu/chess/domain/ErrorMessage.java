package eddie.wu.chess.domain;

public enum ErrorMessage {
	EMPTY_START("��ѡ���Ϊ�հ׵�"),
	NOT_THE_TURN("��ǰ���۸÷���"),
	DUPLICATE("û����Ч�ƶ���Ŀ���������ͬ��"),
	CONFLICT("Ŀ���Ϊ��������"),
	OBSTACLE("�ƶ�·�������������"),;
	private String message;
	ErrorMessage(String message){
		this.message = message;
	}
}
