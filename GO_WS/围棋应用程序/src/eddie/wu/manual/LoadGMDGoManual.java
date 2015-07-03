/*
 * Created on 2005-5-5
 *


 */
package eddie.wu.manual;

import java.util.List;

import org.apache.log4j.Logger
;

/**
 * 载入原先的五千局棋谱，分成10个库。
 * @author eddie
 * 
 *         use to load Go manual form everywhere.
 */
public class LoadGMDGoManual extends GMDGoManual {
	private static final Logger log = Logger.getLogger(LoadGMDGoManual.class);
	// private static final String ROOT = "../doc/围棋打谱软件/";
	private String ROOT;

	private String GO_MANUAL_DATA_FILE_LOCATION;

	public LoadGMDGoManual(String rootDir) {
		this.ROOT = rootDir;
		GO_MANUAL_DATA_FILE_LOCATION = ROOT + "vol000";
	}

	public GMDGoManual loadSingleGoManual() {
		return loadOneFromLib0(5);
	}

	/**
	 * return all go manual of a lib.
	 * 
	 * @param libId
	 *            0-7
	 * @return
	 */
	public List<GMDGoManual> loadMultiGoManual(int libId) {
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
	 * load lib 0--9 Go Manual
	 * 
	 * @return
	 */
	public List<GMDGoManual> loadMultiGoManualFromLib0() {
		return loadMultiGoManual(ROOT + "vol0000.gmd", 9);

	}

	/**
	 * get any go manaul from lib 0 0--9
	 * 
	 * @param id
	 * @return
	 */
	public GMDGoManual loadOneFromLib0(int manualid) {
		if (manualid >= 0 && manualid < 9) {
		} else {
			throw new RuntimeException("manulid>=0&&manulid<9");
		}
		List<GMDGoManual> list = loadMultiGoManualFromLib0();
		return list.get(manualid);
	}

	/**
	 * load one go manual from one lib <br/>
	 * lib 0 --- 9 manuals. <br/>
	 * lib 1-6 --- 1024 manuals <br/>
	 * lib 7 --- 382 manuals.
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

		List<GMDGoManual> list = loadMultiGoManual(GO_MANUAL_DATA_FILE_LOCATION
				+ libid + ".gmd", shu);
		return list.get(manualid).getMoves();
	}
	
	
	

}