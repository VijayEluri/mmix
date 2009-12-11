
package api.reg;

import java.util.regex.Pattern;


public class AskPayQAndASubmitAction {
	public static void main(String[] args) {
		String emailAddress = "eddie.wu@bluem.com";
		boolean temp = new AskPayQAndASubmitAction()
				.isValidEmailAddress2(emailAddress);
		System.out.println(emailAddress + "= " + temp);
		emailAddress = "1@bluem.com";
		temp = new AskPayQAndASubmitAction().isValidEmailAddress2(emailAddress);
		System.out.println(emailAddress + "= " + temp);
	}

	private boolean isValidEmailAddress(String emailAddress) {
		String regExp = "^[\\w\\.-]*\\w@\\w[\\w\\.-]*\\.[a-zA-Z]{1,3}+$";
		System.out.println(regExp);
		Pattern pattern = Pattern.compile(regExp);
		return pattern.matcher(emailAddress).matches();
	}
	//best practice: -- define pattern as instance variable.
	private boolean isValidEmailAddress2(String emailAddress) {
		String regExp = "^[-.\\w]*\\w@\\w[-.\\w]*\\.[a-zA-Z]{1,3}+$";
		System.out.println(regExp);
		Pattern pattern = Pattern.compile(regExp);
		return pattern.matcher(emailAddress).matches();
	}
}
