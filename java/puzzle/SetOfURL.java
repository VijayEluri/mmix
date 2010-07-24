package puzzle;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class SetOfURL {
	private static final String[] ss = new String[] { "www.google.com",
			"javapuzzllers.com", "findbugs.sourceforge.net",
			"javapuzzllers.com", "www.cs.umd.edu" };

	public static void main(String[] args) throws Exception {
		Set urls = new HashSet<URL>();
		for (String url : ss) {
			urls.add(new URL(url));
		}
		System.out.println(urls.size());

	}
}
