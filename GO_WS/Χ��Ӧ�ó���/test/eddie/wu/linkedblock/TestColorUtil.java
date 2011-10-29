/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.linkedblock;
import eddie.wu.domain.ColorUtil;
import junit.framework.TestCase;
/**
 * @author eddie
 *
 * TODO To change the template for this generated type comment go to

 */
public class TestColorUtil extends TestCase{
	public void test(){
	short shoushu=1;
	assertEquals("see1",ColorUtil.BLACK,ColorUtil.getCurrentStepColor((short)1));
	assertEquals("see2",ColorUtil.WHITE,ColorUtil.getCurrentStepEnemyColor
			((short)1));
	assertEquals("see3",ColorUtil.WHITE,ColorUtil.getNextStepColor((short)1));
	assertEquals("see4",ColorUtil.BLACK,ColorUtil.getNextStepEnemyColor((short)1));
	
	}
}
