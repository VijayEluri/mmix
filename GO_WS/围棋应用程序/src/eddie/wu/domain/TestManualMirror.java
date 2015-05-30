package eddie.wu.domain;

import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.TreeGoManual;
import junit.framework.TestCase;

public class TestManualMirror extends TestCase {
	
	public void test1(){
		String name = "White _B_ ___ ___ ";
		String fileName1 = Constant.rootDir + "smallboard/threethree/"
				+ name + "win.sgf";
		String fileName2 = Constant.rootDir + "smallboard/threethree/"
				+ name + "lose.sgf";
		
		TreeGoManual manual = SGFGoManual.loadTreeGoManual(
				fileName1).get(0);
		SymmetryResult symmetry = new SymmetryResult();
		symmetry.setVerticalSymmetry(true);
		//SearchNode temp = manual2.getRoot().getChild();
		manual.getCurrent().getChild().mirrorSubTree(symmetry );
		System.out.println(manual.getSGFBodyString(false));
	}

}
