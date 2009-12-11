
package challenge;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


public class TestCanto extends TestCase {
	int[] inputArray;

	List<Integer> inputList;

	List<Position> outputList = new ArrayList<Position>();

	@Override
	public void setUp() {
		inputArray = new int[] { 1, 3, 8, 14, 10, 0 };
		inputList = new ArrayList<Integer>(5);
		for (int temp : inputArray) {
			inputList.add(temp);
		}
	}

	public void test() {
		for (int temp : inputList) {
			if (temp == 0)
				print(outputList);
			outputList.add(Cantor.fun(temp));
		}
	}

	private void print(List<Position> outputList2) {
		for (Position p : outputList2) {
			System.out.println(p);
		}

	}
}
