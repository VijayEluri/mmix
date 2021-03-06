/*
 * Created on 2005-7-3
 *


 */
package eddie.wu.other;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardPoint;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Point;
import eddie.wu.util.GBKToUTF8;

/**
 * @author eddie
 *
 * TODO To change the template for this generated type comment go to

 */
public class TestCloneable extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	
    /**
     * 
     */
    public TestCloneable() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param name
     */
    public TestCloneable(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }
    /**
     * better we manually copy it if necessary.
     */
    public void testCloneable(){
//        BoardPoint point=new BoardPoint();
//        point.setColor(ColorUtil.BLACK);
//        point.setPoint(Point.getPoint(4,4));
//        Block block=new Block(ColorUtil.BLACK);
//        block.addPoint(Point.getPoint(4,4));
//        block.addBreathPoint(Point.getPoint(4,5));
//        block.setColor(ColorUtil.BLACK);
//        point.setBlock(block);
//        BoardPoint pointNew=(BoardPoint)point.clone();
//        assertNotSame(point,pointNew);
//        assertNotSame(point.getBlock(),pointNew.getBlock());
//        assertNotSame(point.getBlock().getPoints(),pointNew.getBlock().getPoints());
//        assertEquals(point,pointNew);
//        assertEquals(point.getBlock(),pointNew.getBlock());
    }
    public void testArrayClone() throws CloneNotSupportedException{
    	//GoBoard goBoard=new GoBoard();
    	//GoBoard goBoard2= (GoBoard)goBoard.clone();
    	//assertSame(goBoard.getB)
    	TestForArray a=new TestForArray();
    	TestForArray b=(TestForArray)a.clone();
    	assertNotSame(a,b);    	
    	assertSame(a.point,b.point);
    }
    
    public void testPerformance(){
//    	Block [] blocks=new Block[1000];
//    	Block [] blocks2=new Block[1000];
//    	long oldTime=0;
//        long newTime=0;
//        oldTime=System.currentTimeMillis();
//    	for(int i=0;i<1000;i++){
//    		blocks [i]=new Block(ColorUtil.BLACK);
//    		blocks [i].addPoint(Point.getPoint(4,4));
//    		blocks [i].addBreathPoint(Point.getPoint(4,5));
//    		blocks [i].setColor(ColorUtil.BLACK);
//    	}
    }
//    	newTime=System.currentTimeMillis();
//    	if(log.isDebugEnabled()) log.debug("new 1000 Block cost ms:"+(newTime-oldTime));
//
//    	oldTime=System.currentTimeMillis();
//    	for(int i=0;i<1000;i++){
//    		blocks2[i]=(Block)blocks[i].clone();
//    	}
//    	newTime=System.currentTimeMillis();
//    	if(log.isDebugEnabled()) log.debug("clone 1000 Block cost ms:"+(newTime-oldTime));
//    	
//    	for(int i=0;i<1000;i++){
//    		assertNotSame(blocks[i],blocks2[i]);
//    		assertEquals(blocks[i],blocks2[i]);
//    	}
//    }
    
}
class  TestForArray implements Cloneable{
	public BoardPoint[][] point=new BoardPoint[2][2];
	public void init(){
		point[0][1]=new BoardPoint();
		point[0][1].setColor(ColorUtil.BLACK);
		point[0][1].setPoint(Point.getPoint(19,5,5));
	}
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	
}
