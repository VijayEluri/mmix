package line.breaking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Copy from Best Fit and change Algorithm accordingly. Global Optimal with Look
 * Ahead. <br/>
 * consider consistent line width only <br/>
 */
public class TotalFit extends TypeSet {
	public static boolean DEBUG = Constant.DEBUG;

	int lineWidth;

	public TotalFit(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public TotalFit() {
	}

	/**
	 * local variable is defined as class field to easy the code structure.
	 */
	List<Box> word = new ArrayList<Box>();

	@Override
	public List<List<Box>> breakPar(List<Box> par, int lineWidth) {
		this.lineWidth = lineWidth;

		// int lineNo = 0;
		List<List<Box>> lines = new ArrayList<List<Box>>();

		List<Node> activeNodes = new ArrayList<Node>();
		Node initNode = new Node();
		activeNodes.add(initNode);

		boxloop: for (Box box : par) {
			if (box instanceof Glue || box instanceof Penalty) {
				if (box instanceof Glue) {
					if (Constant.DEBUG) {
						System.out.print("try word and glue: ");
						LineUtil.printWord(word);
					}
				} else {
					Penalty p = (Penalty) box;
					if (Constant.DEBUG) {
						System.out.print("try word and penalty: ");
						LineUtil.printWord(word);
					}
				}

				List<Node> newNodes = new ArrayList<Node>();
				for (Iterator<Node> iter = activeNodes.iterator(); iter
						.hasNext();) {
					Node node = iter.next();

					NodeResult result;
					if (box instanceof Glue) {
						Glue glue = (Glue) box;
				   		result = node.accept(word, glue, lineWidth);
					} else {
						Penalty p = (Penalty) box;
						result = node.accept(word, p, lineWidth);
					}
					switch (result.result) {
					case NotReach:
						//word = new ArrayList<Box>();
						//continue boxloop;
						break;
					case Possible:
						Node newNode = result.newNode;
						if (Constant.DEBUG) {
							System.out.println("There are " + activeNodes.size()
									+ " nodes for trying.");

						}

						if (Constant.DEBUG) {
							System.out.print("try Node: ");
							node.print(lineWidth);
						}
						newNodes.add(newNode);
						System.out.print("Add Node: ");
						newNode.print(lineWidth);
						// if (Constant.DEBUG) {
						// System.out.println("add node at: see its end");
						// LineUtil.printLine(0, node.line, lineWidth);
						// }
						break;
					case Impossible:
						if (Constant.DEBUG) {
							System.out.println("There are " + activeNodes.size()
									+ " nodes for trying.");
						}

						iter.remove();
						if (Constant.DEBUG) {
							System.out.println("remove node at: see its head");
							//LineUtil.printLine(0, node.previousLine, lineWidth);
							node.print(lineWidth);
						}
						break;
					}
				}//for
				
				// if more than one, choose better one.
				if (newNodes.size() > 1) {
					if (Constant.DEBUG) {
						System.out.println("same point, different way to it.");
					}
				}
				activeNodes.addAll(newNodes);
				if (Constant.DEBUG && newNodes.isEmpty()==false) {
					System.out.println("There are " + activeNodes.size()
							+ " nodes after this turn.");
					System.out.println();
				}
				if (activeNodes.size() == 0) {
					System.out.println("impossible to achieve |r| <= 1");
					break;
				}
				word = new ArrayList<Box>();

			} else { // real box
				word.add(box);
			}

		}// for
		double betaMin = Double.MAX_VALUE;
		Node minNode = null;
		for (Iterator<Node> iter = activeNodes.iterator(); iter.hasNext();) {
			Node node = iter.next();
			if (node.totalBeta < betaMin) {
				betaMin = node.totalBeta;
				minNode = node;
			}
		}
		
		minNode.line.add(new Glue(lineWidth - LineUtil.getWidth(minNode.line),0,0));
		lines.add(minNode.line);//the last line
		
		while (minNode != null && minNode.previousLine.size() > 0) {
			lines.add(minNode.previousLine);
			minNode = minNode.parent;
		}
		// List<List<Box>> lines2 = new ArrayList<List<Box>>();
		// for(List<Box> line:lines.r){
		//			
		// }
		Collections.reverse(lines);
		return lines;// reverse(lines);//lines;
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

enum Result {
	Impossible, NotReach, Possible;
}

class NodeResult {
	public NodeResult(Result result) {
		this.result = result;
	}

	public NodeResult() {

	}

	Result result;
	Node newNode;
	public static NodeResult NotReach = new NodeResult(Result.NotReach);
	public static NodeResult Impossible = new NodeResult(Result.Impossible);
}
