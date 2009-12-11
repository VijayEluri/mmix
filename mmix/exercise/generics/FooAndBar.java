package generics;

import java.util.*;

class Foo {
	public Integer foo(List list) {
		return null;
	}
	// public String foo(List list) {
	// return null;
	// }
}

class Bar {
	public Integer bar(List<Integer> integers) {
		System.out.println("in integer");
		return null;
	}

	public String bar(List<String> strings) {
		System.out.println("in String");
		return null;
	}
}