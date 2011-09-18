package line.breaking;

import java.util.List;

public class LineUtil {
	static boolean DEBUG = Constant.DEBUG;
	static int alpha = 3000; // continuous hyphenated line

	public static void printParagraph(List<List<Box>> breakPar) {

		int lineNo = 0;
		boolean previousHyphen = false;
		for (List<Box> line : breakPar) {
			previousHyphen = LineUtil.printLine(++lineNo, line, 390,
					previousHyphen);

		}
	}

	public static boolean printLine(int lineNo, List<Box> line, int lineWidth) {
		return printLine(lineNo, line, lineWidth, false);
	}

	public static boolean printLine(int lineNo, List<Box> line, int lineWidth,
			boolean previousHyphen) {
		if (DEBUG)
			System.out.print("[" + lineNo + "] ");
		int width = 0;

		int stretch = 0;
		int shrink = 0;
		for (Box box : line) {
			width += box.getWidth();
			if (DEBUG)
				System.out.print(box.getCh());
			if (box instanceof Glue) {
				Glue g = (Glue) box;
				stretch += g.getY();
				shrink += g.getZ();
			}
		}
		double r = 0;
		if (DEBUG)
			System.out.print("\t" + (lineWidth - width));
		if ((lineWidth - width) >= 0) {
			if (DEBUG)
				System.out.print("\t" + (stretch));
			r = (lineWidth - width) / (double) stretch;
			if (DEBUG)
				System.out.print("\t" + r);
		} else {
			if (DEBUG)
				System.out.print("\t" + (shrink));
			r = (width - lineWidth) / (double) shrink;
			if (DEBUG)
				System.out.print("\t" + r);

		}
		if (DEBUG) {
			double beta = 100 * r * r * r + 0.5;
			int totalDemerit = (1 + (int) beta) * (1 + (int) beta);

			if (line.get(line.size() - 1) instanceof Penalty) {
				Penalty p = (Penalty) line.get(line.size() - 1);
				totalDemerit = (1 + (int) beta + p.getPenalty())
						* (1 + (int) beta + p.getPenalty());
				if (previousHyphen) {
					totalDemerit += alpha;
				}
			}
			System.out.print("\t" + beta);
			System.out.print("\t" + totalDemerit);
		}
		if (DEBUG)
			System.out.println();
		if (lineNo != 0)
			if (DEBUG)
				System.out.println();
		if (line.isEmpty() == false && line.get(line.size() - 1).getCh() == '-') {
			return true;
		} else {
			return false;
		}
	}

	static int getWidth(List<Box> word) {
		int width = 0;
		for (Box box : word) {
			width += box.getWidth();
		}
		return width;
	}

	static void printWord(List<Box> line) {
		if (DEBUG)
			System.out.print("WORD: ");
		int width = 0;
		for (Box box : line) {
			width += box.getWidth();
			if (DEBUG)
				System.out.print(box.getCh());
		}
		if (DEBUG)
			System.out.print("\t" + width);
		if (DEBUG)
			System.out.println();
	}

	static Statistics getStatistic(List<List<Box>> lists, int lineWidth) {
		Statistics stat = new Statistics();
		int hyphen = 0;
		for (List<Box> line : lists) {
			int width = 0;
			int stretch = 0;
			int shrink = 0;
			if (line.size() != 0) {
				Box o = line.get(line.size() - 1);
				if (o instanceof Penalty) {
					hyphen++;
				}
			}
			for (Box box : line) {
				width += box.getWidth();
				if (DEBUG)
					System.out.print(box.getCh());
				if (box instanceof Glue) {
					Glue g = (Glue) box;
					stretch += g.getY();
					shrink += g.getZ();
				}
			}
			// if(DEBUG) System.out.print("\t" + (lineWidth - width));
			if ((lineWidth - width) >= 0) {
				// if(DEBUG) System.out.print("\t" + (stretch));
				double r = (lineWidth - width) / (double) stretch;
				if (r > stat.getrMax()) {
					stat.setrMax(r);
				}
				// if(DEBUG) System.out.print("\t" + r);
			} else {
				// if(DEBUG) System.out.print("\t" + (shrink));
				double r = (width - lineWidth) / (double) shrink;
				if (r > stat.getrMin()) {
					stat.setrMin(r);
				}

			}
		}
		stat.setHyphen(hyphen);
		return stat;
	}

}
