package core.reflect;

import java.lang.reflect.Field;

/**
 * some time the legacy API does not expose enough info. besides change legacy
 * API, we can also use refelction to get the info we want.
 * 
 * @author Wu Jianfeng
 * 
 */
public class AccessPrivate {
	public static Object access(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
