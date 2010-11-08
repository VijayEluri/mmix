package line.breaking;

import java.util.ArrayList;
import java.util.List;

class Node {
	static boolean DEBUG = Constant.DEBUG;
	
	int width = 0;
	int shrink = 0;
	int stretch = 0;
	double beta;
	List<Box> line = new ArrayList<Box>();
	
	//fixed value.
	List<Box> previousLine = new ArrayList<Box>();	
	double totalBeta;
	
	List<Node> children = new ArrayList<Node>();
	Node parent;
	

	public void notReach(Glue glue) {
		width += glue.getWidth();
		stretch += glue.getY();
		shrink += glue.getZ();
		line.add(glue);
	}

	public void notReach(Penalty glue) {
		
	}
	
	public void reach(Penalty p) {
		
		 width += p.getWidth();
		 line.add(p);
	}

	public NodeResult accept(List<Box> word, Glue glue, int lineWidth) {
		width += LineUtil.getWidth(word);
		line.addAll(word);
		double rStretch = 0;

		if (width <= lineWidth) {
			if (stretch == 0) {
				rStretch = 10000;
				beta = 10000;
				notReach(glue);
				return NodeResult.NotReach;
			} else {
				rStretch = (lineWidth - width) / (double) stretch;
			}
			if (rStretch <= 1) {// break here
				if (DEBUG)
					System.out.println("stretch = " + stretch
							+ "; lineWidth-width=" + (lineWidth - width));
				if (DEBUG)
					System.out.println("Find breakpoint before word,"
							+ " not full, need a little"
							+ " stretch. the copy is:");
				// lineBeforeWord = new ArrayList<Box>(line.size());
				// lineBeforeWord.addAll(line);

				LineUtil.printLine(0, line, lineWidth);
				beta = Math.floor(100 * rStretch * rStretch * rStretch + 0.5);

				NodeResult result = newNode(glue);
				
				notReach(glue);
				return result;
			} else {
				notReach(glue);
				return NodeResult.NotReach;
			}
		} else {
			double rShrink;
			rShrink = (width - lineWidth) / (double) shrink;
			if (rShrink <= 1) {
				if(DEBUG){
				System.out.println("Find a break after word, ");
				LineUtil.printLine(0, line, lineWidth);
				}
				beta = Math.floor(100 * rShrink * rShrink * rShrink + 0.5);

				NodeResult result = newNode(glue);
				notReach(glue);
				return result;

			} else {// no after word break.
				return NodeResult.Impossible;
			}

		}

	}

	private NodeResult newNode(Box box) {
		NodeResult result = new NodeResult(Result.Possible);
		Node node = new Node();
		result.newNode = node;
		
		node.previousLine.addAll(line);//without glue.
		if(box instanceof Penalty) node.previousLine.add(box);
		node.totalBeta = this.totalBeta+beta;
		
		node.parent = this;
		this.children.add(node);
		
		return result;
	}

	public NodeResult accept(List<Box> word, Penalty p, int lineWidth) {
		width += LineUtil.getWidth(word);
		line.addAll(word);
		if (width + p.getWidth() <= lineWidth) {
			double rStretch;
			if (stretch == 0) {

				rStretch = 10000;
				notReach(p);
				return NodeResult.NotReach;
				// betaAfterTemp = 10000;
			} else {
				rStretch = (lineWidth - width - p.getWidth())
						/ (double) stretch;

			}
			if (rStretch <= 1) {// break here
				//reach(p);
				if(DEBUG){
					System.out.println("Find a break after penalty, need stretch, ");
					LineUtil.printLine(0, line, lineWidth);
					}
				beta = Math.floor(100 * rStretch * rStretch * rStretch + 0.5)
						+ p.getPenalty();
				NodeResult result = newNode(p);
				return result;
			} else {
				notReach(p);
				return NodeResult.NotReach;
			}
		} else {
			double rShrink = (width + p.getWidth() - lineWidth)
					/ (double) shrink;

			if (rShrink <= 1) {
				//reach(p);
				if(DEBUG){
					System.out.println("Find a break after penalty, need shrink.");
					LineUtil.printLine(0, line, lineWidth);
					}
				beta = Math.floor(100 * rShrink * rShrink * rShrink + 0.5)
						+ p.getPenalty();
				NodeResult result = newNode(p);
				return result;
			} else {
				return NodeResult.Impossible;
			}

			// lines.add(line);

			// lineBeforeWord = new ArrayList<Box>();

		}

	}

	@Override
	public String toString() {
		return "Node [beta=" + beta + ", shrink=" + shrink + ", stretch="
				+ stretch + ", totalBeta=" + totalBeta + ", width=" + width
				+ "]";
	}
	public void print(int lineWidth){
		if(Constant.DEBUG && previousLine.isEmpty()==false){
			System.out.println(this.toString());
			LineUtil.printLine(0, previousLine, lineWidth);
		}
	}
}