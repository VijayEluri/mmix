package eddie.wu.linkedblock;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.manual.ConvertGoManual;
import eddie.wu.manual.LoadGoManual;

public class TestConvertGoManual extends TestCase {
	private static final Log log = LogFactory.getLog(TestConvertGoManual.class);
	
	/**
	 * iterator all manuals.
	 * search the one has special characters.
	 *
	 */
	public void testConvert() {
		Logger logger=Logger.getLogger(ConvertGoManual.class);
		logger.setLevel(Level.ERROR);
		Logger logger1=Logger.getLogger(TestConvertGoManual.class);
		logger1.setLevel(Level.ERROR);
		byte[] temp = LoadGoManual.loadSingleGoManual();

		ConvertGoManual convert = new ConvertGoManual();
		byte[] result = convert.convertFormat(temp);
		if (log.isDebugEnabled()) {
			for (int i = 0; i < temp.length / 2; i++) {
				System.out.println("i=" + i + ", [" + temp[2 * i] + ","
						+ temp[2 * i + 1] + "];" + " [" + result[2 * i] + ","
						+ result[2 * i + 1] + "]");
			}
		}
		  int count = 0;
	        List list = LoadGoManual.loadMultiGoManualFromLib0();

	        byte[] original = null;

	        for (int j = 1; j < 2; j++) {
	        	
	            list = LoadGoManual.loadMultiGoManual(j);
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
	public void testSimgleGoManual(){
		Logger logger=Logger.getLogger(ConvertGoManual.class);
		logger.setLevel(Level.DEBUG);
		byte[] temp =LoadGoManual.loadOneFromAllGoManual(1,760);
		ConvertGoManual convert = new ConvertGoManual();
		byte[] result = convert.convertFormat(temp);
		for (int i = 0; i < temp.length / 2; i++) {
			System.out.println("i=" + i + ", [" + temp[2 * i] + ","
					+ temp[2 * i + 1] + "];" + " [" + result[2 * i] + ","
					+ result[2 * i + 1] + "]");
		}
	}
}
