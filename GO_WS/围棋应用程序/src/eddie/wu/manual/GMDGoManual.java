package eddie.wu.manual;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;

/**
 * 处理棋谱格式GMD.土人提供的五千棋谱和六千棋谱皆使用该棋谱格式。<br/>
 * 这个格式也算简明易懂。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class GMDGoManual extends SimpleGoManual {
	private static final Logger log = Logger.getLogger(GMDGoManual.class);
	private static final int FIRSTSTEPINDEX = 224;

	private static final int SHOUSHUINDEX = 160;

	private static final int ByteLengthOfSingleGoManual = 1024;
	private static Map<String, List<GMDGoManual>> map = new HashMap<String, List<GMDGoManual>>();
	
	
	public GMDGoManual(){
		super(Constant.BOARD_SIZE, Constant.BLACK);
	}

	public static GMDGoManual loadGoManual(String libFileName, int manualId) {
		return loadMultiGoManual(libFileName).get(manualId);
	}

	public static List<GMDGoManual> loadMultiGoManual(String goManualLibFileName) {
		if (map.containsKey(goManualLibFileName)) {
			return map.get(goManualLibFileName);
		} else {
			return loadMultiGoManual(goManualLibFileName, 6391);
		}
	}

	/**
	 * return a series of go manual 0F 03 03 0F<br/>
	 * [16,4] [4,16] TODO: numberOfGoManuals should be removed.
	 * 
	 * @param goManualLibFileName
	 *            name of lib
	 * @param numberOfGoManuals
	 *            total number of go manual
	 * @return
	 */
	public static List<GMDGoManual> loadMultiGoManual(
			String goManualLibFileName, int goManuals) {
		if (map.containsKey(goManualLibFileName)) {
			return map.get(goManualLibFileName);
		}

		File file = new File(goManualLibFileName);
		int numberOfGoManuals = (int) (file.length() / ByteLengthOfSingleGoManual);
		if (numberOfGoManuals != goManuals) {
			String string = "expected number of go manual=" + goManuals
					+ ",actual=" + numberOfGoManuals;
			log.debug(string);
			throw new RuntimeException(string);
		}

		List<GMDGoManual> list = new ArrayList<GMDGoManual>(numberOfGoManuals);
		DataInputStream in = null;

		byte a, b;
		byte[] original = new byte[ByteLengthOfSingleGoManual
				* numberOfGoManuals];
		GMDGoManual manual;
		Point step;
		byte[] standard = null;
		try {
			in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(goManualLibFileName)));
			log.debug("in.available()=" + in.available());

			int shoushuCount = 0;
			int errorCode = in.read(original, 0, ByteLengthOfSingleGoManual
					* numberOfGoManuals);
			if (errorCode != original.length) {
				if(log.isEnabledFor(Level.WARN)) log.warn("expected bytes=" + original.length
						+ ", actual bytes=" + errorCode);
			}

			for (int goManualCount = 0; goManualCount < numberOfGoManuals; goManualCount++) {
				if (goManualCount == 5071)// has problem
					continue;

				log.debug("ii=" + goManualCount);
				int base = goManualCount * ByteLengthOfSingleGoManual;
				int shoushu = original[SHOUSHUINDEX + base];
				int shoushuHigh = original[SHOUSHUINDEX + base + 1];

				if (shoushu < 0)
					shoushu += 256;
				shoushu = shoushuHigh * 256 + shoushu;
				standard = new byte[shoushu * 2];
				manual = new GMDGoManual();

				int idLow = original[base + 0];
				if (idLow < 0) {
					idLow += 256;
				}
				int idHigh = original[base + 1];
				if (idHigh < 0) {
					idHigh += 256;
				}
				manual.setId(idHigh * 256 + idLow);

				byte[] blackPlayer = Arrays.copyOfRange(original, base + 4,
						base + 24);
				String black = getString(blackPlayer);
				byte[] whitePlayer = Arrays.copyOfRange(original, base + 24,
						base + 44);
				String white = getString(whitePlayer);
				manual.setBlackName(black);
				manual.setWhiteName(white);
				shoushuCount = 0;
				/*
				 * original is X_Y coordinate; change into ROW_COLUMN
				 * Coordinate<br> original it is index from 0.
				 */

				for (int i = 0; i < shoushu * 2; i++) {
					shoushuCount++;
					a = original[FIRSTSTEPINDEX + base + i];
					a++;
					standard[i + 1] = a;
					i++;
					b = original[FIRSTSTEPINDEX + base + i];
					b++;
					standard[i - 1] = b;
					// standard[i] = b;
					if (a < 1 || a > 19 || b < 1 || b > 19) {
						if (Constant.DEBUG_CGCL) {
							String string = "载入的数据有误！shoushou=" + shoushuCount
									+ "a=" + a + ",b=" + b;
							log.debug(string);
							throw new RuntimeException(string);
						}

					}
					step = Point.getPoint(Constant.BOARD_SIZE, b, a);
					manual.addStep(step,
							ColorUtil.getCurrentStepColor(shoushuCount));

				}
				log.debug("shoushu=" + shoushuCount);
				list.add(manual);
			}
			// }

		}

		catch (IOException ex) {
			log.debug("the input meet some trouble!");
			log.debug("Exception" + ex.toString());
			throw new RuntimeException(ex);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.debug("finally");
		}
		return list;

	}

	private static String getString(byte[] string) {
		boolean hasZero = false;
		for (int i = 0; i < string.length; i++) {
			if (string[i] == 0) {
				if (hasZero == true) {
					return new String(Arrays.copyOfRange(string, 0, i - 1),
							Charset.forName("GBK"));
				} else {
					hasZero = true;
				}
			} else {
				hasZero = false;
			}
		}
		return null;
	}
}
