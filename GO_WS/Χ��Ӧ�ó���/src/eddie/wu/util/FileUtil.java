package eddie.wu.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import eddie.wu.ui.canvas.EmbedBoardCanvas;

public class FileUtil {
	private static final Logger log = Logger.getLogger(FileUtil.class);
	public static String toString(String filePath, String charset)
			throws IOException {
		File file = new File(filePath);
		String res = null;
		int count = (int) file.length();
		int start = 0;
		byte[] a = new byte[count];

		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		while (count > 0) {
			int read = dis.read(a, start, count);
			if (read < 0)
				break;
			start += read;
			count -= read;
		}
		dis.close();
		res = new String(a, charset);
		return res;
	}

	public static void stringToFile(String old, String newCharset,
			String filePath) throws Exception {
		byte[] buffer = old.getBytes(newCharset);
		File file = new File(filePath);
		DataOutputStream dis = new DataOutputStream(new FileOutputStream(file));
		dis.write(buffer);
		dis.flush();
		dis.close();
	}

	public static void convertOne(String file, String fromCharset,
			String toCharset) throws Exception {
		String content = toString(file, fromCharset);
		String filePath = file ;//+ ".utf8";
		stringToFile(content, toCharset, filePath);
		if(log.isDebugEnabled()) log.debug(filePath);
		
	}

	public static void convertAll(String root, String fromCharset,
			String toCharset) throws Exception {
		File folder = new File(root);
		if(folder.isDirectory()){
			File[] files = folder.listFiles();
			for(File file: files){
				if(file.isDirectory()){			
					convertAll(root+"/"+file.getName(),fromCharset, toCharset);
				}else if(file.getName().endsWith(".java")){
					convertOne(root+"/"+file.getName(),fromCharset, toCharset);
				}
			}
		}
	}
	
	public static void removeUTF8TempFile(String root, String fromCharset,
			String toCharset) throws Exception {
		File folder = new File(root);
		if(folder.isDirectory()){
			File[] files = folder.listFiles();
			for(File file: files){
				if(file.isDirectory()){			
					removeUTF8TempFile(root+"/"+file.getName(),fromCharset, toCharset);
				}else if(file.getName().endsWith(".utf8")){
					new File(root+"/"+file.getName()).delete();
				}
			}
		}
	}
}
