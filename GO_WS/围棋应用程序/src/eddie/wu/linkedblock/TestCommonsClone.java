//package eddie.wu.linkedblock;
//
//import junit.framework.TestCase;
//
//import org.apache.commons.lang.ArrayUtils;
//
//import eddie.wu.domain.Point;
//
///**
// * To test the clone method of java_commons_lang package.
// * If we can understand the clone framework, then we have understand
// * the key feature of java, such as dynamic binding or poly-morphism.
// * when the subclass of Object call the clone method, this method is running
// * on the subclass instance. and because the subclass has implements the 
// * Cloneable interface. so the superclass method's behavior will change.
// * That is to say. the super class clone methos include some code to test
// * whether the class has implements the cloneable interface. but the mothod
// * is native. we can not see the code.
// * @author eddie
// * 
// */
//
//
//public class TestCommonsClone extends TestCase {
//	public void testCloneable(){
//		Point [] points=new Point[2];
//		points[0]=new Point(25);
//		points[1]=new Point(26);
//		Point [] temp=(Point [])ArrayUtils.clone(points);
//		assertNotSame(temp,points);
//		assertSame(temp[0],points[0]);
//		assertSame(temp[1],points[1]);
//		 
//		Point []temp2=(Point [])points.clone();
//		assertNotSame(temp2,points);
//		assertSame(temp[0],points[0]);
//		assertSame(temp[1],points[1]);		
//		
//	}
//	public void testCloneable2(){
//		Point [][] points=new Point[2][2];
//		points[0][0]=new Point(25);
//		points[1][0]=new Point(26);
//		points[0][1]=new Point(27);
//		points[1][1]=new Point(28);
//		Point [] []temp=(Point [][])ArrayUtils.clone(points);
//		assertNotSame(temp,points);
//		assertSame(temp[0],points[0]);
//		assertSame(temp[1],points[1]);
//		assertSame(temp[0][0],points[0][0]);
//		assertSame(temp[1][0],points[1][0]);
//		Point [][]temp2=(Point [][])points.clone();
//		assertNotSame(temp2,points);
//		assertSame(temp[0],points[0]);
//		assertSame(temp[1],points[1]);		
//		
//	}
//}
