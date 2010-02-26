package effective.serialize.map;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import effective.serialize.SeriaTiming;

public class TestMap extends TestCase{
	long start;
	long end;
	SeriaTiming timing = new SeriaTiming();

	public void testMap() {
		long counta = 0;
		long countb = 0;
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "morning");
		map.put(2, "morning good");
		map.put(3, "morning normal");
		MyMap myMap = new MyMap();
		myMap.map = (map);
		for (int i = 0; i < 1; i++) {
			
			countb += timing.timeSerialAndReverse(myMap);
			counta += timing.timeSerialAndReverse(map);
		}
		System.out.println("map: it takes " + ((counta) >> 10)
				+ " K nano seconds.");
		System.out.println("my map: it takes " + (countb >> 10)
				+ " K nano seconds.");
	}

}
