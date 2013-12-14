package eddie.wu.linkedblock;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger
;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.manual.ConvertGoManual;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.LoadGMDGoManual;
import eddie.wu.manual.SGFGoManual;

public class TestConvertGoManual extends TestCase {
	private static final Logger log = Logger.getLogger(TestConvertGoManual.class);
	
	public void testConvert_sgf(){
		SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);
		ConvertGoManual convert = new ConvertGoManual();
//		convert.convertFormat(row, column);
	}
	
	/**
	 * iterator all manuals.
	 * search the one has special characters.
	 * @deprecated failed because of lib1 is lost.
	 */
	public void testConvert() {
		Logger logger=Logger.getLogger(ConvertGoManual.class);
		logger.setLevel(Level.ERROR);
		Logger logger1=Logger.getLogger(TestConvertGoManual.class);
		logger1.setLevel(Level.ERROR);
		byte[] temp = new LoadGMDGoManual("doc/围棋打谱软件/").loadSingleGoManual().getMoves();

		ConvertGoManual convert = new ConvertGoManual();
		byte[] result = convert.convertFormat(temp);
		if (log.isDebugEnabled()) {
			for (int i = 0; i < temp.length / 2; i++) {
				if(log.isDebugEnabled()) log.debug("i=" + i + ", [" + temp[2 * i] + ","
						+ temp[2 * i + 1] + "];" + " [" + result[2 * i] + ","
						+ result[2 * i + 1] + "]");
			}
		}
		  int count = 0;
	        List list = new LoadGMDGoManual("doc/围棋打谱软件/").loadMultiGoManualFromLib0();

	        byte[] original = null;

	        for (int j = 1; j < 2; j++) {
	        	
	            list = new LoadGMDGoManual("doc/围棋打谱软件/").loadMultiGoManual(j);
	            for (Iterator iter = list.iterator(); iter.hasNext();) {
	                count++;
	                //if(count<760) continue;
	                original = (byte[]) iter.next();
	                if (log.isInfoEnabled()) {
	                	log.error("j=" + j);
	                	//log.info("GOManual:" + count);
//	                    log.info("GOManualLength:" + original.length);
	                }
	                result = convert.convertFormat(original);
	            }
	        }
	        
	}
	
	/**
	 * @deprecated failed because of lib1 is lost.
	 */
	public void testSimgleGoManual(){
		Logger logger=Logger.getLogger(ConvertGoManual.class);
		logger.setLevel(Level.DEBUG);
		byte[] temp =new LoadGMDGoManual("doc/围棋打谱软件/").loadOneFromAllGoManual(1,760);
		ConvertGoManual convert = new ConvertGoManual();
		byte[] result = convert.convertFormat(temp);
		for (int i = 0; i < temp.length / 2; i++) {
			if(log.isDebugEnabled()) log.debug("i=" + i + ", [" + temp[2 * i] + ","
					+ temp[2 * i + 1] + "];" + " [" + result[2 * i] + ","
					+ result[2 * i + 1] + "]");
		}
	}
}
