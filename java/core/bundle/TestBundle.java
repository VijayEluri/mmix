package core.bundle;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import junit.framework.TestCase;

public class TestBundle extends TestCase {
	public void test() {
		try {
			String bundleName = this.getClass().getPackage().getName()
					+ ".message_catalog";
			ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
			System.out.println("bundleName is " + bundleName);
			System.out.println("bundle.keySet().size() "
					+ bundle.keySet().size());
			System.out.println(bundle.keySet());
		} catch (MissingResourceException e) {
			e.printStackTrace();
			// Ignore the exception and try again with JDK Logger.
		}
	}

	public static void main(String[] args) {
		new TestBundle().test();
	}

}
