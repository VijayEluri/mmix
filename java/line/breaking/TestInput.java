package line.breaking;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class TestInput extends TestCase {
	static final String name = "example_paragraph.txt";
	static boolean DEBUG = Constant.DEBUG;

	List<Box> list;

	@Override
	public void setUp() throws Exception {
		URL resource = this.getClass().getResource(name);
		byte[] bb = new byte[1024];
		int i = resource.openStream().read(bb);
		// byte [] copy = new byte[i];
		byte[] copy = Arrays.copyOf(bb, i);
		String content = new String(copy);
		if (DEBUG)
			System.out.println(i);
		if (DEBUG)
			System.out.println(content);

		list = Parser.parse(content);
		for (Box box : list) {
			if (DEBUG)
				System.out.println(box);
		}

	}

	public void testFirstFit(int lineWidth) {

		FirstFit ff = new FirstFit();

		List<List<Box>> breakPar = ff.breakPar(list, lineWidth);
		int lineNo = 0;
		for (List<Box> line : breakPar) {
			LineUtil.printLine(++lineNo, line, lineWidth);

		}
	}

	public void testFirstFit_500() {
		this.testFirstFit(500);
	}

	public void testFirstFit_390() {
		this.testFirstFit(390);
	}

	public void testBestFit(int lineWidth) throws Exception {
		BestFit bf = new BestFit();
		 
		List<List<Box>> breakPar = bf.breakPar(list, lineWidth);
		int lineNo = 0;
		for (List<Box> line : breakPar) {
			LineUtil.printLine(++lineNo, line, lineWidth);
		}
	}
	
	public void testBestFit_390()throws Exception{
		this.testBestFit(390);
	}

	public void testBestFit_500()throws Exception{
		this.testBestFit(500);
	}
	
	public void testTotalFit() throws Exception {
		TotalFit bf = new TotalFit();
		List<List<Box>> breakPar = bf.breakPar(list, 390);
		int lineNo = 0;
		for (List<Box> line : breakPar) {
			LineUtil.printLine(++lineNo, line, 390);
		}
	}

	public void testTotalFit_500() throws Exception {
		TotalFit bf = new TotalFit();
		List<List<Box>> breakPar = bf.breakPar(list, 500);
		int lineNo = 0;
		for (List<Box> line : breakPar) {
			LineUtil.printLine(++lineNo, line, 500);
		}
	}

	public void testCompareBetweenFirstFitAndBestFit() {
		int cRMin = 0;
		int cRMax = 0;
		int cHyphen = 0;
		int eqRMin = 0;
		int eqRMax = 0;
		int eqHyphen = 0;
		for (int i = 350; i <= 649; i++) {
			FirstFit ff = new FirstFit();
			List<List<Box>> breakPar = ff.breakPar(list, i);
			Statistics stat1 = LineUtil.getStatistic(breakPar, i);
			System.out.println("first fit" + (i) + " " + stat1);

			BestFit bf = new BestFit();
			List<List<Box>> breakPar2 = bf.breakPar(list, i);
			Statistics stat2 = LineUtil.getStatistic(breakPar2, i);
			System.out.println("best fit" + (i) + " " + stat2);

			if (stat1.getrMin() < stat2.getrMin()) {
				cRMin++;
			} else if (stat1.getrMin() == stat2.getrMin()) {
				eqRMin++;
			}
			if (stat1.getrMax() < stat2.getrMax()) {
				cRMax++;
			} else if (stat1.getrMax() == stat2.getrMax()) {
				eqRMax++;
			}
			if (stat1.getHyphen() < stat2.getHyphen()) {
				cHyphen++;
			} else if (stat1.getHyphen() == stat2.getHyphen()) {
				eqHyphen++;
			}

		}
		double total = 650 - 350;
		System.out.println("cRMin first fit < best fit " + cRMin + "; " + cRMin
				/ total);
		System.out.println("cRMax first fit < best fit " + cRMax + "; " + cRMax
				/ total);
		System.out.println("cHyphen first fit < best fit " + cHyphen + "; "
				+ cHyphen / total);
		System.out.println("eqRMin first fit = best fit " + eqRMin + "; "
				+ eqRMin / total);
		System.out.println("eqRMax first fit = best fit " + eqRMax + "; "
				+ eqRMax / total);
		System.out.println("eqHyphen first fit = best fit " + eqHyphen + "; "
				+ cHyphen / total);

	}

}
