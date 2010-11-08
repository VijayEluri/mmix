package line.breaking;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	public static List<Box> parse(String paragraph) {
		List<Box> boxes = new ArrayList<Box>();

		boxes.add(Box.indent);
		paragraph = paragraph.replace("\n", " ");
		System.out.println("after replace \\n");
		System.out.println(paragraph);
		String[] words = paragraph.split(" ");
		// paragraph.s
		// for (String word : words){
		// System.out.println("working on [" + word+"]");
		// }

		for (String word : words) {
			if (word.length() == 0)
				continue;

			System.out.println("working on [" + word + "]");
			char ch = word.charAt(word.length() - 1);

			List<Integer> hyphens = Hyphenation.getHyphens(word);
			if (word.contains("-")) {
				int index = word.indexOf('-');
				for (int i = 0; i < word.length(); i++) {
					boxes.add(Box.getBox(word.charAt(i)));
					if (i == index) {
						boxes.add(new Penalty(0, true, 50));
						System.out.println("add penalty.");
					}

				}

			} else if (hyphens == null) {
				for (int i = 0; i < word.length(); i++) {
					boxes.add(Box.getBox(word.charAt(i)));
				}
			} else {
				for (int i = 0; i < word.length(); i++) {

					boxes.add(Box.getBox(word.charAt(i)));
					if (hyphens.contains(i + 1)) {
						System.out.println("add penalty after ["
								+ word.charAt(i) + "]");
						boxes.add(new Penalty(Font.getWidth('-'), true, 50));
					}
				}
			}
			boxes.add(Glue.getGlue(word.charAt(word.length() - 1)));
		}

		System.out.println("boxes.size() = " + boxes.size());

		return boxes;
	}
}
