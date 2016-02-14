package eddie.wu.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PropertyUtil {

	public static String wrapWithQuote(String value) {
		return "\"" + value + "\"";
	}

	public static String mergeIntoList(String old, String value) {
		String meta = ",";
		if (old.contains(";")) {
			meta = ";";
		}
		if (value.startsWith("\"") && value.endsWith("\"")) {
			return old.substring(0, old.length() - 1) + meta + value.substring(1);
		} else {
			return old.substring(0, old.length() - 1) + meta + value + "\"";
		}
	}

	public static Map<String, String> extractDparam(String input) {
		Map<String, String> map = new HashMap<String, String>();

		String[] lines = input.split("\n");
		for (String line : lines) {
			line = line.trim();
			if (line.startsWith("-D") == false) {
				continue;
			}

			if (line.endsWith("\\")) {
				line = line.substring(2, line.length() - 1).trim();
			} else { // last line OK
				line = line.substring(2, line.length());
			}
			String[] word = line.split("=");
			if (word.length == 2) {
				// handle
				// sf.sfv4.dbpool.dblinks=\"dbPool1:sf11;dbPool2:sf12;dbPool3:sf14;dbpool4:sf15\"
				if (word[1].startsWith("\\\"") && word[1].endsWith("\\\"")) {
					System.err.println(word[1] + "==>");
					word[1] = "\"" + word[1].substring(2, word[1].length() - 2) + "\"";
					System.err.println(word[1]);
				}

				if (map.containsKey(word[0])) {
					System.err.println("contains key already: " + word[0]);
					System.err.println("old value: " + map.get(word[0]));
					System.err.println("new value: " + word[1]);
					String result = mergeIntoList(map.get(word[0]), word[1]);
					System.err.println("merger result : " + result);
					map.put(word[0], result);
				} else { // new key
					// if (word[1].contains(",")) {
					if (word[1].startsWith("\"") && word[1].endsWith("\"")) {
						map.put(word[0], word[1]);
					} else {
						String value = wrapWithQuote(word[1]);
						map.put(word[0], value);
						System.err.println("Warning: no double quote for comma separated list, add on the fly ");
						System.err.println(word[1]);
					}
					// } else {
					// map.put(word[0], word[1]);
					// }
				}
			} else {
				System.err.println(word.length);
				for (String text : word) {
					System.err.println(text);
					// default to true?
					if (word.length == 1)
						map.put(word[0], "\"true\"");
				}
			}
		}
		return map;
		// StringBuffer bf = new StringBuffer();
		//
		// return bf.toString();

	}

	public static void main(String[] args) {
		String folder = "C:/Users/i061545/Documents/SF/run.conf/";
		List<String> pathList = new ArrayList<String>();
		String[] fileNames = new String[] { "dc4schdjob01.run.conf", "pc4batch01.run.conf", "pc4bbipub01.run.conf",
				"pc4bcar01.run.conf", "pc4bptsk01.run.conf", "pc4brpt01.run.conf", "pc4bsfapi03.run.conf",
				"pc4bsub01.run.conf", "pc4bagy01.run.conf", "pc4batchimg01.run.conf", "pc4bbirt05.run.conf",
				"pc4bcf01.run.conf", "pc4bqtz01.run.conf", "pc4bsoap01.run.conf" };
		for (String fileName : fileNames) {
			pathList.add(folder + fileName);
		}

		String input = null;
		Set<String> keyOfSameValue = new HashSet<String>();
		// one run.conf ==> one map of key:value.
		Map<String, Map<String, String>> metaMap = new HashMap<String, Map<String, String>>();
		List<String> allKeyList = new ArrayList<String>();
		Set<String> allKeySet = new HashSet<String>();
		List<String> servers = new ArrayList<String>();
		for (String filePath : pathList) {
			try {
				input = FileUtil.toString(filePath, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}

			Map<String, String> map = extractDparam(input);
			List<String> keyList = new ArrayList<String>();// for one server
			for (Entry<String, String> entry : map.entrySet()) {
				keyList.add(entry.getKey());
			}
			Collections.sort(keyList);

			for (String key : keyList) {
				System.out.println(key + "=" + map.get(key));
			}

			int fromIndex = filePath.lastIndexOf("/");
			int toIndex = filePath.lastIndexOf(".run.conf");
			String server = filePath.substring(fromIndex + 1, toIndex);
			System.out.println("server= " + server);
			servers.add(server);
			metaMap.put(server, map);
			allKeySet.addAll(keyList);
		}

		// go through all the keys.
		allKeyList.addAll(allKeySet);
		Collections.sort(allKeyList);
		StringBuffer sb = new StringBuffer();
		sb.append("Key");
		for (String server : servers) {
			sb.append("," + server);
		}
		sb.append(",Same on all Server");
		sb.append(System.lineSeparator());
		for (String key : allKeyList) {
			sb.append(key);
			boolean allSame = true;
			boolean na = false;
			String value = null;
			for (String server : servers) {
				Map<String, String> map = metaMap.get(server);
				if (map.containsKey(key)) {
					sb.append("," + map.get(key));
					if (allSame == true) {
						if (value == null) {
							value = map.get(key);
						} else if (value.equals(map.get(key))) {
							// Nothing to do.
						} else {
							allSame = false;
							// record first different one.
							value = map.get(key);
						}
					}
				} else {
					sb.append(",N/A");
					na = true;
					// no setting is allowed
					// allSame = false;
				}
			}
			if (allSame) {
				if (na) {
					sb.append(",Yes (except some is N/A)");
				} else {
					sb.append(",Yes");
				}
				if (value.contains("$")) {
					System.err.println(key + "\t with same dynamic variable!");
				} else {
					System.err.println(key + "\t with same value on all server!");
					keyOfSameValue.add(key);
				}

			}
			sb.append(System.lineSeparator());

		}
		System.err.println("mock csv:");
		// System.out.println(sb.toString());
		String csvFilePath = folder + "BizXRunConf.csv";
		//FileUtil.stringToFile(sb.toString(), csvFilePath);

		String[] strings = readExcel();
		for (String string : strings) {
			if (keyOfSameValue.contains(string) == false) {
				System.err.println(string);
				if (string.equals("com.successfactors.hermes.provider.call.timeout")) {
					System.err.println("strange!");
				}
			}

		}
	}

	public static String[] readExcel() {
		String filePath = "C:/Users/i061545/Documents/SF/run.conf/removed_parameter.csv";
		try {
			return FileUtil.toString(filePath, "UTF-8").split(System.lineSeparator());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
