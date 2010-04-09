package puzzle;

import java.util.HashSet;
import java.util.Set;

public class JoyOfSet {
	public static void main(String[] arags) {
		Set<Short> set = new HashSet<Short>();
		for (short i = 0; i < 100; i++) {
			set.add(i);
			set.remove(i - 1);
		}
		System.out.println(set.size());
	}
}
