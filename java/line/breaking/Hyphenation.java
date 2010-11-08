package line.breaking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hyphenation {
	static Map<String, List<Integer>> hyphen = new HashMap<String, List<Integer>>();
	static {
		hyphen.put("wishing", Arrays.asList(4));
		hyphen.put("daughters", Arrays.asList(5));
		hyphen.put("beautiful", Arrays.asList(4, 6));
		hyphen.put("youngest", Arrays.asList(5));
		hyphen.put("itself", Arrays.asList(2));
		hyphen.put("astonished", Arrays.asList(5));
		hyphen.put("whenever", Arrays.asList(4));
		hyphen.put("forest", Arrays.asList(3));
		hyphen.put("under", Arrays.asList(2));
		hyphen.put("fountain", Arrays.asList(4));
		hyphen.put("favorite", Arrays.asList(5));
		hyphen.put("plaything", Arrays.asList(4));
	}

	public static List<Integer> getHyphens(String word) {
		char ch = word.charAt(word.length() - 1);
		if (Character.isLetter(ch)) {
			return hyphen.get(word);
		} else {
			return hyphen.get(word.substring(0, word.length() - 1));
		}
	}
}
