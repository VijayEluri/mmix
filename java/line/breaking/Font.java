package line.breaking;

public class Font {
	static int[] lowerCaseWidth = new int[] { 9, 10, 8, 10, 8, 6, 9, 10, 5, 6,
			10, 5, 15, 10, 9, 10, 10, 7, 7, 7, 10, 9, 13, 10, 10, 8 };
	static int[] upperCaseWidth = new int[] { 10, 10, 13, 10, 10, 10, 10, 10,
			6, 6, 6 };
	static int[] punctuationWidth = new int[] { 6, 5 };

	static int getWidth(char ch) {
		if (Character.isLowerCase(ch)) {
			return lowerCaseWidth[ch - 'a'];
		} else if (Character.isUpperCase(ch)) {
			return upperCaseWidth[ch - 'A'];
		} else {
			if (ch == '-')
				return punctuationWidth[0];
			else
				return punctuationWidth[1];
		}
	}
}
