package effective.lazy.init;

public class FieldType {
	private static int count;
	private int code;

	FieldType() {
		count++;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Filed Type with heavy value " + code;
	}

	public static int countInstance() {
		return count;
	}

	public static void setCount(int count) {
		FieldType.count = count;
	}
}
