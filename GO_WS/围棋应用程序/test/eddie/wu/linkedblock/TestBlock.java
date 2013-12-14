/*
 * Created on 2005-6-16
 *


 */
package eddie.wu.linkedblock;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Logger
;

import eddie.wu.domain.Block;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Point;

/**
 * @author eddie
 *
 * TODO To change the template for this generated type comment go to

 */
public class TestBlock extends TestCase {
	private static final Logger log = Logger.getLogger(TestBlock.class);
	public void testEquals(){
		Block blocka=new Block(ColorUtil.BLACK);
		Block blockb=new Block(ColorUtil.BLACK);
//		blocka.addPoint( Point.getPoint((byte)4,(byte)16));
//		blockb.addPoint(Point.getPoint((byte)4,(byte)16));
//		blocka.addPoint(Point.getPoint((byte)4,(byte)17));
//		blockb.addPoint(Point.getPoint((byte)4,(byte)17));
		assertEquals(blocka, blockb);
		Set<Block> seta=new HashSet<Block>();
		assertTrue(seta.add(blocka));
		assertFalse(seta.add(blocka));
		
		blocka.setEnemyBlocks(seta);
		//blocka.setBreathBlocks(seta);
		assertFalse(seta.add(blocka));
		
		Set<Block> setb=new HashSet<Block>();
		assertTrue(setb.add(blockb));
		assertFalse(setb.add(blockb));
		assertTrue(seta.contains(blocka));
		assertTrue(seta.contains(blockb));
		assertTrue(setb.contains(blocka));
		assertTrue(setb.contains(blockb));
		assertEquals(seta, setb);
		log.debug("color of blocka"+blocka.getColor());
		log.debug("color of blockb"+blockb.getColor());
		assertEquals(blocka.getColor(),blockb.getColor());
		assertFalse(blocka.equals(null));
		Block blockc=new Block(ColorUtil.BLACK);
		log.debug("blockc"+blockc.getColor());
		assertEquals(ColorUtil.BLACK,blockc.getColor());
	}
}
