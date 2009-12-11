package generics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class TestArray extends TestCase {
	public void test() {
		Number[] numbers = new Number[3];
		Integer[] integers = new Integer[3];
		numbers = integers;
//		numbers[0] = new Long(1);
//		numbers[1] = new Long(2);
//		numbers[2] = new Long(3);
		numbers[0] = new Integer(1);
		numbers[1] = new Integer(2);
		numbers[2] = new Integer(3);
		for(Integer number : integers){
			System.out.println("" + number);
		}
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> name = new ArrayList<String>();
		List<String> names;
		names = (ArrayList<String>)list;
		names = Collections.checkedList(name,String.class);
	}
	
}
