package effective.lazy.init;

public class NormalLazy {
	// Lazy initialization of instance field - synchronized accessor
	private FieldType field;

	synchronized FieldType getField() {
		if (field == null)
			field = computeFieldValue();
		return field;
	}

	private FieldType computeFieldValue() {
		FieldType field = new FieldType();
		int code = 100;
		field.setCode(code);
		return field;
	}
}
