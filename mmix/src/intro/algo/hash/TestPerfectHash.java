package intro.algo.hash;

import java.util.Arrays;

import junit.framework.TestCase;
import taocp.v3.TAOCPSample;

public class TestPerfectHash extends TestCase {
	public void testTwoLevel() {
		int[] a = new int[] { 10, 22, 37, 40, 60, 70, 75 };
		Node[] nodes = PerfectHash.perfectHashTwoLevel(a);
		for (Node node : nodes) {
			if (node != null)
				System.out.println(node.toString());
		}

	}

	public void testTwoLevelOO() {
		int[] a = new int[] { 10, 22, 37, 40, 60, 70, 75 };
		Node[] nodes = PerfectHash.perfectHashTwoLevelOO(a);
		for (Node node : nodes) {
			if (node != null)
				System.out.println(node.toString());
		}

	}

	public void testOneLevel() {
		int[] a = TAOCPSample.getSample_ZeroIndexed();
		System.out.println(Arrays.toString(a));
		int[] b = PerfectHash.perfectHashOneLevel(a);
		System.out.println(Arrays.toString(b));
	}

}
