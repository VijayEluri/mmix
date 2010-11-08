package line.breaking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * without look ahead.s 1. break after the word if possible (without compressing
 * the space to less than the given minimum.<br/>
 * 2. break before the word if possible (without expanding the space to more
 * than the given maximum.<br/>
 * 3. hyphenate the word as much of it on the current line as will fit.<br/>
 * 4. accept a line whose space exceed the given maximum.<br/>
 * note: it does not favor the smaller justification ration. Instead, it favor
 * shrink over stretch
 * 
 * @author eddie
 * 
 */
public class FirstFit extends TypeSet {
//	static boolean DEBUG = false;
	static boolean DEBUG = Constant.DEBUG;
	int lineWidth;

	public FirstFit(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public FirstFit() {
	}

	@Override
	public List<List<Box>> breakPar(List<Box> par, int lineWidth) {
		this.lineWidth = lineWidth;

		int width = 0;
		int shrink = 0;
		int stretch = 0;
		int lineNo = 0;
		List<List<Box>> lines = new ArrayList<List<Box>>();
		/**
		 * need reinitialize properly.
		 */
		List<Box> line = new ArrayList<Box>();
		// List<Box> lineLoosy = new ArrayList<Box>();//break before word
		List<Box> lineBeforeWord = null;// break before word

		/**
		 * will try to put as much into current line once we hyphen it.
		 */
		List<Box> lineHyphen = new ArrayList<Box>();

		List<Box> word = new ArrayList<Box>();

		double rStretch = 0;
		double rShrink = 0;
		for (Box box : par) {

			if (box instanceof Glue) {
				Glue glue = (Glue) box;
				if (width <= lineWidth) {
					/**
					 * finish previous word
					 */
					line.addAll(word);
					if (DEBUG)
						System.out.print("Adding word ");
					this.printWord(word);
					word = new ArrayList<Box>();
					if (stretch == 0) {
						rStretch = 10000;
					} else {
						rStretch = (lineWidth - width) / (double) stretch;
					}
					if (rStretch <= 1) {// break here
						if (DEBUG)
							System.out.println("stretch = " + stretch
									+ "; lineWidth-width="
									+ (lineWidth - width));
						if (DEBUG)
							System.out.println("Find breakpoint before word,"
									+ " not full, need a little"
									+ " stretch. the copy is:");
						lineBeforeWord = new ArrayList<Box>(line.size());
						// Collections.copy(lineBeforeWord, line);
						lineBeforeWord.addAll(line);
						// this.printLine(0, line);
						this.printLine(0, lineBeforeWord);

					} else {

					}
					width += box.getWidth();
					stretch += glue.getY();
					shrink += glue.getZ();
					line.add(box);
					continue;
					//
					// } else if (width == lineWidth) {
					// if(DEBUG) System.out.println("exact match one line!");
					// lines.add(line);
					// line = new ArrayList<Box>();
					// lineNo++;
				} else {
					rShrink = (width - lineWidth) / (double) shrink;
					if (rShrink <= 1) {
						if (DEBUG)
							System.out.println("Find a break after word");

						line.addAll(word);
						word = new ArrayList<Box>();
						lines.add(line);
						printLine(lineNo + 1, line);
						line = new ArrayList<Box>();
						stretch = 0;
						shrink = 0;

						width = 0;
						lineNo++;
						// if (rShrink < rStretch) {
						//
						// } else {
						//
						// }
					} else {
						if (lineBeforeWord != null) {
							if (DEBUG)
								System.out
										.println("impossible to "
												+ "break after word, "
												+ " but possible to break before word ");
							lines.add(lineBeforeWord);
							printLine(lineNo + 1, lineBeforeWord);
						} else {
							if (DEBUG)
								System.out.println("can not achive r<=1");
							line.remove(line.size() - 1);
							lines.add(line);// include extra space at the end.
							printLine(lineNo + 1, line);
						}
						line = new ArrayList<Box>();
						stretch = 0;
						shrink = 0;
						lineNo++;

						line.addAll(word);
						// word = new ArrayList<Box>();
						width = getWidth(word);
					}

					if (DEBUG)
						System.out
								.println("After glue: one line is settle down.");
					// lines.add(line);

					lineBeforeWord = new ArrayList<Box>();

				}
			} else if (box instanceof Penalty) {
				Penalty p = (Penalty) box;
				if (width + p.getWidth() <= lineWidth) {

					if (stretch == 0) {
						rStretch = 10000;
					} else {
						rStretch = (lineWidth - width - p.getWidth())
								/ (double) stretch;
					}
					if (rStretch <= 1) {// break here
						if (DEBUG)
							System.out.println(" stretch = " + stretch
									+ "; lineWidth-width="
									+ (lineWidth - width - p.getWidth()));
						if (DEBUG)
							System.out.println("Find breakpoint at penalty,"
									+ " not full, need a little"
									+ " stretch. the copy is:");
						lineHyphen = new ArrayList<Box>(line.size());
						lineHyphen.addAll(line);
						lineHyphen.addAll(word);
						lineHyphen.add(p);
						this.printLine(0, lineHyphen);

					} else {
						// rStretch > 1. hyphenation does not make sense.
					}
				} else {
					rShrink = (width + p.getWidth() - lineWidth)
							/ (double) shrink;
					if (rShrink <= 1) {
						if (DEBUG)
							System.out.println("Find a break at penalty:");
						if (lineHyphen != null) {
							if (DEBUG)
								System.out.println("overwrite pervious hyphen");
						}
						lineHyphen = new ArrayList<Box>();
						lineHyphen.addAll(line);
						lineHyphen.addAll(word);
						lineHyphen.add(p);
						this.printLine(0, lineHyphen);
					} else {
						if (lineBeforeWord != null) {
							if (DEBUG)
								System.out
										.println("impossible to "
												+ "break after penalty, "
												+ " but possible to break before word ");
							lines.add(lineBeforeWord);
							printLine(lineNo + 1, lineBeforeWord);
							lineBeforeWord = null;
						} else if (lineHyphen != null) {
							System.out
									.println("impossible to "
											+ "break after penalty, "
											+ " but possible to break at previous penalty ");
							lines.add(lineHyphen);
							printLine(lineNo + 1, lineHyphen);
							
							// hack. tricky.
							List<Box> wordTemp = new ArrayList<Box>();

							for (Box b : word) {
								if (!lineHyphen.contains(b)) {
									wordTemp.add(b);
								}
							}
							word = wordTemp;
							printWord(word);
							lineHyphen = null;
						} else {
							if (DEBUG)
								System.out.println("can not achive r<=1");
							lines.add(line);// include extra space at the end.
							printLine(lineNo + 1, line);
						}
						line = new ArrayList<Box>();
						stretch = 0;
						shrink = 0;
						lineNo++;

						// line.addAll(word);

						width = getWidth(word);
						System.out
								.println("After penalty: one line is settle down.");
					}

					// lines.add(line);

					// lineBeforeWord = new ArrayList<Box>();

				}

			} else { // real box
				width += box.getWidth();
				word.add(box);
				if (width <= lineWidth) {

				} else {
					rShrink = (width - lineWidth) / (double) shrink;
					if (rShrink > 1) {
						if (DEBUG)
							System.out.println("handling Box: "
									+ "impossible to break after word");
						if (lineBeforeWord != null
								&& lineBeforeWord.size() != 0) {
							System.out
									.println("but possible to break before word");
							// lineBeforeWord.remove(lineBeforeWord.size()-1);
							lines.add(lineBeforeWord);
							printLine(lineNo + 1, lineBeforeWord);
						} else if (lineHyphen != null && lineHyphen.size() != 0) {
							System.out
									.println("but possible to hyphen  this word");
							lines.add(lineHyphen);
							printLine(lineNo + 1, lineHyphen);
							// hack.
							List<Box> wordTemp = new ArrayList<Box>();

							for (Box b : word) {
								if (!lineHyphen.contains(b)) {
									wordTemp.add(b);
								}
							}
							word = wordTemp;
							printWord(word);
						} else {
							if (DEBUG)
								System.out.println("accept over loosy line.");
							line.remove(line.size() - 1);
							lines.add(line);
							printLine(lineNo + 1, line);
						}
						line = new ArrayList<Box>();
						stretch = 0;
						shrink = 0;
						lineNo++;

						lineBeforeWord = null;// new ArrayList<Box>();
						lineHyphen = null;// new ArrayList<Box>();
						// line.addAll(word);
						// 
						width = getWidth(word);
						if(DEBUG) System.out.println("in dealing box:"
								+ "one line is settle down.");
					}

				}
			}

		}// for
		if (DEBUG)
			System.out.println("accept last line.");
		line.add(new Glue(lineWidth - this.getWidth(line), 0, 0));
		lines.add(line);
		printLine(lineNo + 1, line);
		if (DEBUG)
			System.out.println("total lines is " + lineNo);
		return lines;
	}

	int getWidth(List<Box> word) {
		return LineUtil.getWidth(word);
	}

	public void printLine(int lineNo, List<Box> line) {
		LineUtil.printLine(lineNo, line, this.lineWidth);
	}

	void printWord(List<Box> line) {
		LineUtil.printWord(line);
	}

}
