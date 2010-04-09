package effective.lazy.init;

public class DoubleCheck {
	private volatile FieldType field;

	FieldType getField() {
		FieldType result = field;
		if (result == null) { // First check (no locking)
			synchronized (this) {
				result = field;
				if (result == null) // Second check (with locking)
					field = result = computeFieldValue();
			}
		}
		return result;
	}

	private FieldType computeFieldValue() {
		FieldType field = new FieldType();
		int code = 100;
		field.setCode(code);
		System.out.println(field + " is initialized.");
		return field;
	}
}
