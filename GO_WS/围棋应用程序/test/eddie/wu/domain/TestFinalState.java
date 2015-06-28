package eddie.wu.domain;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.SGFGoManual;

public class TestFinalState extends TestCase {
	private static final Logger log = Logger.getLogger(SurviveAnalysis.class);
//	private static Point point = Point.getPoint(7, 16);

	public void testEyeArea() {
		log.setLevel(Level.DEBUG);

		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		SurviveAnalysis sa = new SurviveAnalysis(manual.getFinalState());
		Point point2;
//		sa.getEyeArea2(sa.getGroup(point));
//
//		point2 = Point.getPoint(14, 3);
//		sa.getEyeArea2(sa.getGroup(point2));

		point2 = Point.getPoint(Constant.BOARD_SIZE,10, 3);
		sa.getEyeArea2(sa.getGroup(point2));
	}

	public void testGroupAnalyze(){
		//log.setLevel(Level.INFO);
		log.setLevel(Level.WARN);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		SurviveAnalysis sa = new SurviveAnalysis(manual.getFinalState());

		sa.analyzeAllGroup();
	}
	
	public void test() {
		log.setLevel(Level.INFO);

		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		SurviveAnalysis sa = new SurviveAnalysis(manual.getFinalState());

		
		// this.assertTrue(sa.isLive(Point.getPoint(1, 3)));
		// this.assertTrue(sa.isLive(Point.getPoint(1, 4)));
		// this.assertTrue(sa.isLive(Point.getPoint(1, 8)));
		// this.assertTrue(sa.isLive(Point.getPoint(1, 10)));
		// this.assertTrue(sa.isLive(Point.getPoint(16, 16)));
		// this.assertTrue(sa.isLive(Point.getPoint(10, 17)));
		// this.assertTrue(sa.isLive(Point.getPoint(8, 3)));
		// this.assertTrue(sa.isLive(Point.getPoint(12, 2)));
		// this.assertTrue(sa.isLive(Point.getPoint(17, 8)));
		// this.assertTrue(sa.isLive(Point.getPoint(16, 10)));
		// this.assertTrue(sa.isLive(Point.getPoint(15, 11)));

		// this.assertTrue(sa.isLive(Point.getPoint(15, 11)));
		// this.assertTrue(sa.isLive(Point.getPoint(17, 16)));
		// this.assertTrue(sa.isLive(Point.getPoint(17, 18)));
		// this.assertTrue(sa.isLive(Point.getPoint(18, 17)));
		// this.assertTrue(sa.isLive(Point.getPoint(18, 15)));

	}
}
