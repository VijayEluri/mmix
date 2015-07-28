package eddie.wu.domain;

import junit.framework.TestCase;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.TreeGoManual;

public class TestManualMirror extends TestCase {

	public void test1() {
		String name = "White _B_ ___ ___ ";
		// String fileName1 = Constant.rootDir + "smallboard/threethree/"
		// + name + "win.sgf";
		String fileName1 = Constant.rootDir + "smallboard/threethree/"
				+ "Black ___ __B BBB win.sgf";

		TreeGoManual manual = SGFGoManual.loadTreeGoManual(fileName1).get(0);
		System.out.println(manual.getSGFBodyString(false));

		SymmetryResult symmetry = new SymmetryResult();
		symmetry.setVerticalSymmetry(true);
		// SearchNode temp = manual2.getRoot().getChild();
		manual.getCurrent().getChild().mirrorSubTree(symmetry);
		System.out.println(manual.getSGFBodyString(false));
	}

	public void test2() {
		String fileName1 = Constant.rootDir + "smallboard/twotwo/"
				+ "Black __ __.sgf";
		System.out.println("file name "+fileName1);
		TreeGoManual manual = SGFGoManual.loadTreeGoManual(fileName1).get(0);
		System.out.println(manual.getSGFBodyString(false));
		System.out.println("==================");
		SmallGoBoard go = new SmallGoBoard(manual.getInitState());
		Step childMove = manual.getCurrent().getChild().getStep();
		go.oneStepForward(childMove);
		manual.navigateToChild(childMove);
		SymmetryResult symmetry = go.getSymmetryResult();

	    Point move = Point.getPoint(2, 2, 1);		
		SymmetryResult normalizeOperation = GoBoardSymmetry
				.getNormalizeOperation(move, symmetry);
		System.out.println(normalizeOperation);
		manual.getCurrent().containsChildMove_mirrorSubTree(symmetry,new Step(move,Constant.WHITE));
		System.out.println(manual.getSGFBodyString(false));
		assertTrue(manual.getCurrent().containsChildMove(new Step(move,Constant.WHITE)));
	}
	
	public void test3() {
		String fileName1 = Constant.rootDir + "smallboard/threethree/"
				+ "Black ___ ___ ___ win.sgf";
		TreeGoManual manual = SGFGoManual.loadTreeGoManual(fileName1).get(0);
		System.out.println(manual.getSGFBodyString(false));
		System.out.println("==================");
		SmallGoBoard go = new SmallGoBoard(manual.getInitState());
		Step childMove = manual.getCurrent().getChild().getStep();
		go.oneStepForward(childMove);
		manual.navigateToChild(childMove);
		SymmetryResult symmetry = go.getSymmetryResult();

	    Point move = Point.getPoint(3, 2, 1);		
		SymmetryResult normalizeOperation = GoBoardSymmetry
				.getNormalizeOperation(move, symmetry);
		System.out.println(normalizeOperation);
		manual.getCurrent().containsChildMove_mirrorSubTree(symmetry,new Step(move,Constant.WHITE));
		System.out.println(manual.getSGFBodyString(false));
		assertTrue(manual.getCurrent().containsChildMove(new Step(move,Constant.WHITE)));
	}

	public void testSymmetry_33() {
		byte[][] state = new byte[5][5];
		SmallGoBoard go = new SmallGoBoard(state);
		go.oneStepForward(Point.getPoint(3, 2, 2));
		SymmetryResult sym = go.getSymmetryResult();
		Point move = Point.getPoint(3, 2, 1);
		
		SymmetryResult normalizeOperation = GoBoardSymmetry
				.getNormalizeOperation(move, sym);
		System.out.println(normalizeOperation);
		Point temp = move.normalize(normalizeOperation);
		System.out.println("move="+move);
		System.out.println("norm="+temp);
		System.out.println(move.normalize(sym));
		
		move = Point.getPoint(3, 1, 2);
		normalizeOperation = GoBoardSymmetry
				.getNormalizeOperation(move, sym);
		System.out.println(normalizeOperation);
		
		temp = move.normalize(normalizeOperation);
		System.out.println("move="+move);
		System.out.println("norm="+temp);
		System.out.println(move.normalize(sym));
	}
	
	public void testSymmetry_22() {
		byte[][] state = new byte[4][4];
		SmallGoBoard go = new SmallGoBoard(state);
		go.oneStepForward(Point.getPoint(2, 1, 1));
		SymmetryResult sym = go.getSymmetryResult();
		Point move = Point.getPoint(2, 2, 1);
		
		SymmetryResult normalizeOperation = GoBoardSymmetry
				.getNormalizeOperation(move, sym);
		System.out.println(normalizeOperation);
		Point temp = move.normalize(normalizeOperation);
		System.out.println("move="+move);
		System.out.println("norm="+temp);
		System.out.println(move.normalize(sym));
	}

}
