package line.breaking;

import java.util.ArrayList;
import java.util.List;

/**
 * Copy from First Fit and change Algorithm accordingly. without Look Ahead. <br/>
 * 1. each line choose the minimum strechability/shrinkability.<br/>
 * there are still many issues in the implementation.
 */
public class BestFit extends TypeSet {
	public static boolean DEBUG = Constant.DEBUG;

	int lineWidth;

	public BestFit(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public BestFit() {
	}

	/**
	 * local variable is defined ad class field to easy the code structure.
	 */
	int width = 0;
	int shrink = 0;
	int stretch = 0;
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
	double betaBeforeWord = 0;
	double betaAfter = 0;
	double betaAfterTemp = 0;

	@Override
	public List<List<Box>> breakPar(List<Box> par, int lineWidth) {
		this.lineWidth = lineWidth;

		int lineNo = 0;
		List<List<Box>> lines = new ArrayList<List<Box>>();

		for (Box box : par) {

			if (box instanceof Glue) {
				if (DEBUG)
					System.out.print("Adding word ");
				this.printWord(word);
				Glue glue = (Glue) box;
				if (width < lineWidth) {
					/**
					 * finish previous word
					 */
					line.addAll(word);
					
				
					word = new ArrayList<Box>();
					if (stretch == 0) {
						rStretch = 10000;
						betaBeforeWord = 10000;
					} else {
						rStretch = (lineWidth - width) / (double) stretch;
						betaBeforeWord = Math.floor(100 * rStretch * rStretch
								* rStretch + 0.5);
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
						lineBeforeWord.addAll(line);

						this.printLine(0, lineBeforeWord);

					} else {

					}
					width += box.getWidth();
					stretch += glue.getY();
					shrink += glue.getZ();
					line.add(box);
					continue;

				} else if (width == lineWidth) {
					if (DEBUG)
						System.out.println("exact match one line!");
					lines.add(line);
					// line = new ArrayList<Box>();
					lineNo++;

					initParam();

				} else {
					rShrink = (width - lineWidth) / (double) shrink;
					if (rShrink <= 1) {

						if (rShrink < rStretch) { // prefer small one.
							System.out
									.println("Find a break after word, better than break before word");

							line.addAll(word);
							if (DEBUG)
								System.out.print("Adding Word");
							LineUtil.printWord(word);
							lines.add(line);
							printLine(lineNo + 1, line);
							initParam();
							lineNo++;
						} else { // prefer loose line in tie,DF
							List<Box> copy = new ArrayList<Box>();
							if (lineBeforeWord != null) {
								System.out
										.println("Find a break after word, but not as good as break before word");
								lines.add(lineBeforeWord);
								printLine(lineNo + 1, lineBeforeWord);
								copy.addAll(word);

							} else {
								//line.addAll(word);
								lines.add(line);
								printLine(lineNo + 1, line);
								copy.addAll(word);
								//initParam();
								//lineNo++;
								System.out.println("accept loosy line");
							}
							
							
							// copy.add(glue);
							initParam();
							lineNo++;
							//word.addAll(copy);
							line.addAll(copy);
							line.add(glue);
							width = LineUtil.getWidth(copy);
							width += glue.getWidth();
							stretch += glue.getY();
							shrink += glue.getZ();
						}

					} else {// no after word break.
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
						lineNo++;

						List<Box> word2 = new ArrayList<Box>();
						word2.addAll(word);
						initParam();
						word.addAll(word2);
						line.addAll(word);
						width = getWidth(word);
					}

					if (DEBUG)
						System.out
								.println("After glue: one line is settle down.");
					// lines.add(line);

					lineBeforeWord = new ArrayList<Box>();

				}
			} else if (box instanceof Penalty) {
				if(DEBUG) System.out.println("Adding ");
				this.printWord(word);
				Penalty p = (Penalty) box;
				if (width + p.getWidth() <= lineWidth) {

					if (stretch == 0) {
						rStretch = 10000;
						betaAfterTemp = 10000;
					} else {
						rStretch = (lineWidth - width - p.getWidth())
								/ (double) stretch;
						betaAfterTemp = Math.floor(100 * rStretch * rStretch
								* rStretch + 0.5)
								+ p.getPenalty();
					}
					if (rStretch <= 1) {// break here
						if (lineHyphen == null || betaAfterTemp < betaAfter) {
							if (DEBUG)
								System.out.println(" stretch = " + stretch
										+ "; lineWidth-width="
										+ (lineWidth - width - p.getWidth()));
							if (DEBUG)
								System.out
										.println("Find breakpoint at penalty,"
												+ " not full, need a little"
												+ " stretch. the copy is:");
							lineHyphen = new ArrayList<Box>(line.size());
							lineHyphen.addAll(line);
							lineHyphen.addAll(word);
							lineHyphen.add(p);
							this.printLine(0, lineHyphen);
							betaAfter = betaAfterTemp;
						} else {
							// ignore the hyphen.
						}

					} else {
						// rStretch > 1. hyphenation does not make sense.
					}
				} else {
					rShrink = (width + p.getWidth() - lineWidth)
							/ (double) shrink;
					betaAfterTemp = Math.floor(100 * rShrink * rShrink
							* rShrink + 0.5)
							+ p.getPenalty();
					if (rShrink <= 1) {
						if (lineHyphen == null || betaAfterTemp < betaAfter) {
							if (DEBUG)
								System.out.println("Find a break at penalty:");
							if (lineHyphen != null) {
								if (DEBUG)
									System.out
											.println("overwrite pervious hyphen");
							}
							lineHyphen = new ArrayList<Box>();
							lineHyphen.addAll(line);
							lineHyphen.addAll(word);
							lineHyphen.add(p);
							this.printLine(0, lineHyphen);
							betaAfter = betaAfterTemp;
						} else {
							System.out.println("impossible to break"
									+ " after this and later hyphen.");
						}
					} else {
						if (lineHyphen != null) {
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
						} else if (lineBeforeWord != null) {
							if (DEBUG)
								System.out
										.println("impossible to "
												+ "break after penalty, "
												+ " but possible to break before word ");
							lines.add(lineBeforeWord);
							printLine(lineNo + 1, lineBeforeWord);
							lineBeforeWord = null;
						} else {
							if (DEBUG)
								System.out.println("can not achive r<=1");
							lines.add(line);// include extra space at the end.
							printLine(lineNo + 1, line);
						}
						// line = new ArrayList<Box>();
						// stretch = 0;
						// shrink = 0;
						lineNo++;

						List<Box> word2 = new ArrayList<Box>();
						word2.addAll(word);
						initParam();
						word.addAll(word2);

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
						if (lineHyphen != null && lineHyphen.size() != 0) {
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
						} else if (lineBeforeWord != null
								&& lineBeforeWord.size() != 0) {
							System.out
									.println("but possible to break before word");
							// lineBeforeWord.remove(lineBeforeWord.size()-1);
							lines.add(lineBeforeWord);
							printLine(lineNo + 1, lineBeforeWord);
						} else {
							if (DEBUG)
								System.out.println("accept over loosy line.");
							line.remove(line.size() - 1);
							lines.add(line);
							printLine(lineNo + 1, line);
						}
						lineNo++;

						List<Box> word2 = new ArrayList<Box>();
						word2.addAll(word);
						initParam();
						word.addAll(word2);

						width = getWidth(word);
						System.out
								.println("in dealing box: one line is settle down.");
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

	private void initParam() {
		stretch = 0;
		shrink = 0;
		width = 0;

		word = new ArrayList<Box>();
		line = new ArrayList<Box>();
		lineHyphen = null;
		lineBeforeWord = null;
		this.betaAfter = 0;

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
