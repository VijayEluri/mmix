package core.coll;

import java.util.Arrays;
import java.util.List;

public class ArraysToList {

	public static void main(String[] args2) {

		String[] args = new String[] { "a", "b", "c" };
		// This gives us nothing good
		System.out.println(args);

		// Convert args to a List of String
		List<String> argList = Arrays.asList(args);

		// Print them out
		System.out.println(argList);
	}
}