package core.reflect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class TestAccessPrivate extends TestCase {
	public void accesss() {
	}

	public void test() {
		Data data = new Data();
		data.add("i", "love you");

		try {
			//Doesn't work because it only return public field.
			//Field field = data.getClass().getField("map");
			Field field = data.getClass().getDeclaredField("map");
			field.setAccessible(true);
			Object obj = field.get(data);
			System.out.println(obj.toString());
			
			assertEquals(obj, AccessPrivate.access(data, "map"));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class Data {
	private Map<String, String> map = new HashMap<String, String>();

	public void add(String k, String v) {
		map.put(k, v);
	}
}
