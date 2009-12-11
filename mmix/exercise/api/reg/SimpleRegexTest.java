
package api.reg;


public class SimpleRegexTest {
	public static void main(String[] args) {
		//testRegex();
		
		//testEscaping1();
		testEscaping1();
		testEscaping2();
	}

	private static void testRegex() {
		String sampleText = "this is the 1st test string";
		System.out.println("sampleText = " + sampleText);
		String sampleRegex = "\\d+\\w+";
		System.out.println("sampleRegex = " + sampleRegex);

		java.util.regex.Pattern p = java.util.regex.Pattern
				.compile(sampleRegex);
		java.util.regex.Matcher m = p.matcher(sampleText);
		if (m.find()) {
			String matchedText = m.group();
			int matchedFrom = m.start();
			int matchedTo = m.end();
			System.out.println("matched [" + matchedText + "] from "
					+ matchedFrom + " to " + matchedTo + ".");
			System.out.println("The " + matchedFrom + " to " + matchedTo
					+ " of " + sampleText + "is: ["
					+ sampleText.substring(matchedFrom, matchedTo) + "]");
		} else {
			System.out.println("didn't match");
		}
	}
	
	private static void testEscaping1(){
		String sampleText = "this is the 1st test string\r\r\n";
		System.out.println("sampleText = " + sampleText);
		String sampleRegex = "(\r){2}\n+";
		System.out.println("sampleRegex = " + sampleRegex);

		java.util.regex.Pattern p = java.util.regex.Pattern
				.compile(sampleRegex);
		java.util.regex.Matcher m = p.matcher(sampleText);
		if (m.find()) {
			String matchedText = m.group();
			int matchedFrom = m.start();
			int matchedTo = m.end();
			System.out.println("matched [" + matchedText + "] from "
					+ matchedFrom + " to " + matchedTo + ".");
			System.out.println("The " + matchedFrom + " to " + matchedTo
					+ " of \"" + sampleText + "\"is: ["
					+ sampleText.substring(matchedFrom, matchedTo) + "]");
		} else {
			System.out.println("didn't match");
		}
	}
	
	private static void testEscaping2(){
		String sampleText = "this is the 1st test string\r\r\n";
		System.out.println("sampleText = " + sampleText);
		String sampleRegex = "(\\r){2}\\n+";
		System.out.println("sampleRegex = " + sampleRegex);

		java.util.regex.Pattern p = java.util.regex.Pattern
				.compile(sampleRegex);
		java.util.regex.Matcher m = p.matcher(sampleText);
		if (m.find()) {
			String matchedText = m.group();
			int matchedFrom = m.start();
			int matchedTo = m.end();
			System.out.println("matched [" + matchedText + "] from "
					+ matchedFrom + " to " + matchedTo + ".");
			System.out.println("The " + matchedFrom + " to " + matchedTo
					+ " of \"" + sampleText + "\"is: ["
					+ sampleText.substring(matchedFrom, matchedTo) + "]");
		} else {
			System.out.println("didn't match");
		}
	}
	
	private static void testEscaping3(){
		String sampleText = "this is the * 1st test string\r\r\n";
		System.out.println("sampleText = " + sampleText);
		String sampleRegex = "[\\*\\^]+";
		System.out.println("sampleRegex = " + sampleRegex);

		java.util.regex.Pattern p = java.util.regex.Pattern
				.compile(sampleRegex);
		java.util.regex.Matcher m = p.matcher(sampleText);
		if (m.find()) {
			String matchedText = m.group();
			int matchedFrom = m.start();
			int matchedTo = m.end();
			System.out.println("matched [" + matchedText + "] from "
					+ matchedFrom + " to " + matchedTo + ".");
			System.out.println("The " + matchedFrom + " to " + matchedTo
					+ " of \"" + sampleText + "\"is: ["
					+ sampleText.substring(matchedFrom, matchedTo) + "]");
		} else {
			System.out.println("didn't match");
		}
	}
}