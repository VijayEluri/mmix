/*
 * Created on 2005-5-5
 *


 */
package eddie.wu.manual;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Constant;
import eddie.wu.linkedblock.GoboardTestApplet;

/**
 * @author eddie
 * 
 *         use to load Go manual form everywhere.
 */
public class LoadGoManual {
	private static final Log log = LogFactory.getLog(GoboardTestApplet.class);
	// private static final String ROOT = "../doc/围棋打谱软件/";
	private String ROOT;

	private String GO_MANUAL_DATA_FILE_LOCATION;
	private static final int FIRSTSTEPINDEX = 224;

	private static final int SHOUSHUINDEX = 160;

	private static final int ByteLengthOfSingleGoManual = 1024;

	public LoadGoManual(String rootDir) {
		this.ROOT = rootDir;
		GO_MANUAL_DATA_FILE_LOCATION = ROOT + "vol000";
	}

	public byte[] loadSingleGoManual() {
		return loadOneFromLib0(0);
	}

	/**
	 * return all go manual of a lib.
	 * 
	 * @param libId
	 *            0-7
	 * @return
	 */
	public List loadMultiGoManual(int libId) {
		int numberOfGoManuals = 0;
		if (libId >= 0 && libId <= 7) {
			if (libId == 0) {
				numberOfGoManuals = 9;
			} else if (libId == 7) {
				numberOfGoManuals = 382;
			} else {
				numberOfGoManuals = 1000;
			}
		} else {
			throw new RuntimeException("should libid>=0&&libid<=7");
		}

		return loadMultiGoManual(GO_MANUAL_DATA_FILE_LOCATION + libId + ".gmd",
				numberOfGoManuals);

	}

	/**
	 * return a series of go manual
	 * 
	 * @param goManualLibFileName
	 *            name of lib
	 * @param numberOfGoManuals
	 *            total number of go manual
	 * @return
	 */
	private List loadMultiGoManual(String goManualLibFileName,
			int numberOfGoManuals) {
		List<byte[]> list = new ArrayList<byte[]>(1024);
		DataInputStream in = null;

		byte a, b;
		byte[] original = new byte[ByteLengthOfSingleGoManual
				* numberOfGoManuals];
		byte[] standard = null;
		try {
			in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(goManualLibFileName)));

			// while (in.available() > 1000 && count < COUNT) {

			log.debug("in.available()=" + in.available());
			int shoushuCount = 0;
			int errorCode = in.read(original, 0, ByteLengthOfSingleGoManual
					* numberOfGoManuals);

			for (int goManualCount = 0; goManualCount < numberOfGoManuals; goManualCount++) {
				log.debug("ii=" + goManualCount);
				int shoushu = original[SHOUSHUINDEX + goManualCount
						* ByteLengthOfSingleGoManual];
				int shoushuHigh = original[SHOUSHUINDEX + goManualCount
						* ByteLengthOfSingleGoManual + 1];

				if (shoushu < 0)
					shoushu += 256;
				shoushu = shoushuHigh * 256 + shoushu;
				standard = new byte[shoushu * 2];
				shoushuCount = 0;
				for (int i = 0; i < shoushu * 2; i++) {
					shoushuCount++;
					a = original[FIRSTSTEPINDEX + goManualCount
							* ByteLengthOfSingleGoManual + i];
					a++;
					// original is X_Y coordinate
					// change into ROW_COLUMN Coordinate
					// standard[i] = a;
					standard[i + 1] = a;
					i++;
					b = original[FIRSTSTEPINDEX + goManualCount
							* ByteLengthOfSingleGoManual + i];
					b++;
					standard[i - 1] = b;
					// standard[i] = b;
					if (a < 1 || a > 19 || b < 1 || b > 19) {
						if (Constant.DEBUG_CGCL) {
							log.debug("载入的数据有误！" + shoushuCount);
							throw new RuntimeException("载入的数据有误");
						}

					}
					if (Constant.DEBUG_CGCL) {
						// log.debug("shoushu=" + shoushuCount);
						// log.debug(": i=" + a);
						// log.debug(",j=" + b);
					}
				}
				log.debug("shoushu=" + shoushuCount);
				list.add(standard);
			}
			// }

			in.close();
		}

		catch (IOException ex) {
			log.debug("the input meet some trouble!");
			log.debug("Exception" + ex.toString());
			throw new RuntimeException(ex);
		} finally {
			log.debug("finally");
		}
		return list;

	}

	/**
	 * load lib 0--9 Go Manual
	 * 
	 * @return
	 */
	public List loadMultiGoManualFromLib0() {
		return loadMultiGoManual(ROOT + "vol0000.gmd", 9);

	}

	/**
	 * get any go manaul from lib 0 0--9
	 * 
	 * @param id
	 * @return
	 */
	public byte[] loadOneFromLib0(int manualid) {
		if (manualid >= 0 && manualid < 9) {
		} else {
			throw new RuntimeException("manulid>=0&&manulid<9");
		}
		List list = loadMultiGoManualFromLib0();
		return (byte[]) list.get(manualid);
	}

	/**
	 * load one go manual from one lib 0 --9 1-6 --1024 7---382
	 * 
	 * @param libid
	 * @param manualid
	 * @return
	 */
	public byte[] loadOneFromAllGoManual(int libid, int manualid) {
		int shu = 0;
		if (libid >= 0 && libid <= 7) {
			if (libid == 0) {
				if (manualid >= 0 && manualid < 9) {
					shu = 9;
				} else {
					throw new RuntimeException("manulid>=0&&manulid<9");
				}
			} else if (libid == 7) {
				if (manualid >= 0 && manualid < 382) {
					shu = 382;
				} else {
					throw new RuntimeException("manulid>=0&&manulid<382");
				}
			} else {
				if (manualid >= 0 && manualid < 1024) {
					shu = 1024;
				} else {
					throw new RuntimeException("manulid>=0&&manulid<1024");
				}
			}
		} else {
			throw new RuntimeException("should libid>=0&&libid<=7");
		}

		List list = loadMultiGoManual(GO_MANUAL_DATA_FILE_LOCATION + libid
				+ ".gmd", shu);
		return (byte[]) list.get(manualid);
	}

}