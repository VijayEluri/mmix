package effective.lazy.init;

public class StaticLazy {
	private static class FieldHolder {
		static final FieldType field = computeFieldValue();

		public static FieldType computeFieldValue() {
			FieldType field = new FieldType();
			int code = 100;
			field.setCode(code);
			System.out.println(field+" is initialized.");
			return field;
		}
	}

	static FieldType getField() {
		return FieldHolder.field;
	}

}
